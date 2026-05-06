package com.contrapunto.control_contrapunto.controller;

import com.contrapunto.control_contrapunto.model.Alumno;
import com.contrapunto.control_contrapunto.model.ComprobantePago;
import com.contrapunto.control_contrapunto.repository.AlumnoRepository;
import com.contrapunto.control_contrapunto.repository.ComprobantePagoRepository;
import com.contrapunto.control_contrapunto.service.ComprobantePdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ComprobanteController {

    private final ComprobantePagoRepository comprobantePagoRepository;
    private final AlumnoRepository alumnoRepository;
    private final ComprobantePdfService comprobantePdfService;

    /**
     * Devuelve JSON con la lista de comprobantes de un alumno.
     * Consumido por fetch() en el modal de alumnos.html.
     */
    @GetMapping("/comprobantes/alumno/{id}")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> obtenerComprobantesPorAlumno(
            @PathVariable("id") Long id) {

        List<ComprobantePago> comprobantes = comprobantePagoRepository.findByAlumno_IdAlumno(id);

        List<Map<String, Object>> response = comprobantes.stream().map(c -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idComprobantePago", c.getIdComprobantePago());
            map.put("fechaPago", c.getFechaPago() != null ? c.getFechaPago().toString() : "");
            map.put("descripcion", c.getDescripcion());
            map.put("importe", c.getImporte());
            map.put("archivoUrl", c.getArchivoUrl());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Guarda un nuevo comprobante vía AJAX.
     * Siempre devuelve JSON (éxito o error) para que el frontend lo consuma.
     * Se usa @Transactional para mantener el alumno dentro de la sesión JPA.
     */
    @PostMapping("/comprobantes/guardar")
    @ResponseBody
    @Transactional
    public ResponseEntity<Map<String, Object>> guardarComprobante(
            @RequestParam("id_alumno") Long idAlumno,
            @RequestParam("importe") String importeStr,
            @RequestParam("asunto") String asunto) {

        Map<String, Object> result = new HashMap<>();
        try {
            // Parsear importe manualmente para soportar formatos "400", "400.50", "400,50"
            String importeLimpio = importeStr.trim().replace(",", ".");
            BigDecimal importe = new BigDecimal(importeLimpio);

            // Verificar que el alumno exista
            if (!alumnoRepository.existsById(idAlumno)) {
                result.put("exito", false);
                result.put("mensaje", "Alumno no encontrado (id=" + idAlumno + ").");
                return ResponseEntity.badRequest().body(result);
            }

            // getReferenceById() devuelve un proxy JPA gestionado dentro de la transacción actual
            // Evita el DetachedObjectException que ocurría al pasar una entidad de otra sesión
            Alumno alumnoRef = alumnoRepository.getReferenceById(idAlumno);

            ComprobantePago comprobante = new ComprobantePago();
            comprobante.setAlumno(alumnoRef);
            comprobante.setImporte(importe);
            comprobante.setDescripcion(asunto.trim());
            comprobante.setFechaPago(LocalDate.now());

            // Primer save para obtener el ID generado
            comprobante = comprobantePagoRepository.save(comprobante);

            // Guardar la URL de descarga en archivo_url ahora que tenemos el ID
            comprobante.setArchivoUrl("/comprobantes/descargar/" + comprobante.getIdComprobantePago());
            comprobante = comprobantePagoRepository.save(comprobante);

            result.put("exito", true);
            result.put("idAlumno", idAlumno);
            result.put("idComprobante", comprobante.getIdComprobantePago());
            return ResponseEntity.ok(result);

        } catch (NumberFormatException e) {
            result.put("exito", false);
            result.put("mensaje", "El importe ingresado no es un número válido: '" + importeStr + "'.");
            return ResponseEntity.badRequest().body(result);
        } catch (Exception e) {
            // Registrar el error real en consola para depuración
            System.err.println("[ComprobanteController] Error al guardar comprobante: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            result.put("exito", false);
            result.put("mensaje", "Error interno al guardar: " + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }

    /**
     * Elimina un comprobante por su ID y redirige a la lista de alumnos.
     */
    @GetMapping("/comprobantes/eliminar/{id}")
    public String eliminarComprobante(@PathVariable("id") Long id) {
        comprobantePagoRepository.deleteById(id);
        return "redirect:/alumnos";
    }

    /**
     * Genera y descarga el PDF del comprobante solicitado.
     */
    @GetMapping("/comprobantes/descargar/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> descargarComprobante(@PathVariable("id") Long id) {
        ComprobantePago comprobante = comprobantePagoRepository.findById(id).orElse(null);
        if (comprobante == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] pdfBytes = comprobantePdfService.generarPdf(comprobante);

        String nombreAlumno = comprobante.getAlumno().getNombreAlumno().replace(" ", "_");
        String fecha = comprobante.getFechaPago().toString();
        String nombreArchivo = "Comprobante_" + nombreAlumno + "_" + fecha + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", nombreArchivo);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
