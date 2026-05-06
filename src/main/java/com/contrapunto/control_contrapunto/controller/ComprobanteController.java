package com.contrapunto.control_contrapunto.controller;

import com.contrapunto.control_contrapunto.model.Alumno;
import com.contrapunto.control_contrapunto.model.ComprobantePago;
import com.contrapunto.control_contrapunto.repository.ComprobantePagoRepository;
import com.contrapunto.control_contrapunto.service.ServicioAlumno;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
    private final ServicioAlumno servicioAlumno;

    /**
     * Devuelve JSON con la lista de comprobantes de un alumno.
     * Consumido por el fetch() del modal en alumnos.html.
     */
    @GetMapping("/comprobantes/alumno/{id}")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> obtenerComprobantesPorAlumno(
            @PathVariable("id") Long id) {

        List<ComprobantePago> comprobantes = comprobantePagoRepository.findByAlumno_IdAlumno(id);

        List<Map<String, Object>> response = comprobantes.stream().map(c -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idComprobantePago", c.getIdComprobantePago());
            map.put("fechaPago", c.getFechaPago());
            map.put("descripcion", c.getDescripcion());
            map.put("importe", c.getImporte());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Guarda un nuevo comprobante. Recibe id_alumno, importe y asunto (mapeado a descripcion).
     */
    @PostMapping("/comprobantes/guardar")
    public String guardarComprobante(@RequestParam("id_alumno") Long idAlumno,
                                     @RequestParam("importe") BigDecimal importe,
                                     @RequestParam("asunto") String asunto) {
        Alumno alumno = servicioAlumno.obtenerPorId(idAlumno);
        if (alumno != null) {
            ComprobantePago comprobante = new ComprobantePago();
            comprobante.setAlumno(alumno);
            comprobante.setImporte(importe);
            comprobante.setDescripcion(asunto);      // 'asunto' del form → campo 'descripcion' en BD
            comprobante.setFechaPago(LocalDate.now()); // fecha actual
            comprobantePagoRepository.save(comprobante);
        }
        return "redirect:/alumnos";
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
     * Endpoint esqueleto para descarga de PDF. Pendiente de implementación.
     */
    @GetMapping("/comprobantes/descargar/{id}")
    @ResponseBody
    public ResponseEntity<String> descargarComprobante(@PathVariable("id") Long id) {
        return ResponseEntity.ok("En construcción");
    }
}
