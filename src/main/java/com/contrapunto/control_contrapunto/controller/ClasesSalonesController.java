package com.contrapunto.control_contrapunto.controller;

import com.contrapunto.control_contrapunto.model.Salon;
import com.contrapunto.control_contrapunto.model.Clase;
import com.contrapunto.control_contrapunto.service.SalonService;
import com.contrapunto.control_contrapunto.service.ServicioClase;
import com.contrapunto.control_contrapunto.service.ServicioProfesor;
import com.contrapunto.control_contrapunto.service.ServicioAlumno;
import com.contrapunto.control_contrapunto.repository.MateriaRepository;
import com.contrapunto.control_contrapunto.repository.DiaSemanaRepository;
import com.contrapunto.control_contrapunto.repository.InasistenciaRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ClasesSalonesController {

    private final SalonService salonService;
    private final ServicioClase servicioClase;
    private final ServicioProfesor servicioProfesor;
    private final ServicioAlumno servicioAlumno;
    private final MateriaRepository materiaRepository;
    private final DiaSemanaRepository diaSemanaRepository;
    private final InasistenciaRepository inasistenciaRepository;
    private final com.contrapunto.control_contrapunto.service.ClaseCleanupService claseCleanupService;

    @GetMapping("/clases-salones")
    public String mostrarClasesSalones(
            @RequestParam(value = "tab", required = false, defaultValue = "salones") String tab,
            HttpSession session,
            Model model) {

        String admin = (String) session.getAttribute("adminLogueado");
        if (admin == null) {
            return "redirect:/login";
        }

        // Limpieza pasiva de clases temporales:
        // Garantiza que si el servidor estuvo apagado a medianoche,
        // al cargar la vista se eliminen las clases expiradas antes de mostrarlas.
        claseCleanupService.limpiarReposicionesAntiguas();

        model.addAttribute("usuarioActivo", admin);
        model.addAttribute("activePage", "clases-salones");
        model.addAttribute("tabActivo", tab);

        // Inyectar datos de salones
        model.addAttribute("salones", salonService.listarTodos());
        model.addAttribute("salonObj", new Salon());

        // Inyectar listas para los dropdowns de Clase
        model.addAttribute("listProfesores", servicioProfesor.listarTodos());
        model.addAttribute("listAlumnos", servicioAlumno.listarTodos());
        model.addAttribute("listMaterias", materiaRepository.findAll());
        model.addAttribute("listDiasSemana", diaSemanaRepository.findAll());
        model.addAttribute("listSalones", salonService.listarTodos());
        model.addAttribute("listInasistencias", inasistenciaRepository.findInasistenciasPendientes());
        model.addAttribute("listClases", servicioClase.listarTodos());
        model.addAttribute("claseObj", new Clase());

        return "clases-salones";
    }

    @PostMapping("/salones/guardar")
    public String guardarSalon(@ModelAttribute("salonObj") Salon salon, HttpSession session) {
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }
        salonService.guardar(salon);
        return "redirect:/clases-salones?tab=salones";
    }

    @GetMapping("/salones/eliminar/{id}")
    public String eliminarSalon(@PathVariable("id") Long id, HttpSession session) {
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }
        salonService.eliminar(id);
        return "redirect:/clases-salones?tab=salones";
    }

    @PostMapping("/clases/guardar")
    public String guardarClase(@ModelAttribute("claseObj") Clase clase, HttpSession session) {
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }
        
        // El @Scheduled se encarga de limpiar las reposiciones pasadas (ClaseCleanupService).
        try {
            if (clase.getTipoClase() == null) clase.setTipoClase(false); // Seguro en caso de checkbox vacío
            servicioClase.agendarClase(clase);
        } catch (Exception e) {
            System.err.println("Error al agendar clase: " + e.getMessage());
            e.printStackTrace();
        }
        
        return "redirect:/clases-salones?tab=clases";
    }

    @GetMapping("/clases/eliminar/{id}")
    public String eliminarClase(@PathVariable("id") Long id, HttpSession session) {
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }
        servicioClase.eliminar(id);
        return "redirect:/clases-salones?tab=clases";
    }
}
