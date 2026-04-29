package com.contrapunto.control_contrapunto.controller;

import com.contrapunto.control_contrapunto.service.ServicioAlumno;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import com.contrapunto.control_contrapunto.model.Alumno;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AlumnoController {

    private final ServicioAlumno servicioAlumno;

    @GetMapping("/alumnos")
    public String listarAlumnos(HttpSession session, Model model) {
        // Protección de sesión
        // if (session.getAttribute("adminLogueado") == null) {
        //     return "redirect:/login";
        // }

        model.addAttribute("usuarioActivo", session.getAttribute("adminLogueado"));
        model.addAttribute("activePage", "alumnos");
        model.addAttribute("alumnos", servicioAlumno.listarTodos());
        return "alumnos";
    }

    @GetMapping("/alumnos/nuevo")
    public String mostrarFormularioRegistro(HttpSession session, Model model) {
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("usuarioActivo", session.getAttribute("adminLogueado"));
        model.addAttribute("activePage", "alumnos");
        model.addAttribute("alumno", new Alumno());
        return "registro-alumno";
    }

    @PostMapping("/alumnos/guardar")
    public String guardarAlumno(
            @RequestParam("nombre") String nombre,
            @RequestParam(value = "telefonos[]", required = false) List<String> listaTelefonos,
            @RequestParam(value = "correos[]", required = false) List<String> listaCorreos,
            HttpSession session) {
            
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }

        if (nombre != null && !nombre.trim().isEmpty()) {
            servicioAlumno.guardarAlumnoConContactos(nombre.trim(), listaTelefonos, listaCorreos);
        }

        return "redirect:/alumnos";
    }

    @GetMapping("/alumnos/editar/{id}")
    public String mostrarFormularioEdicion(@org.springframework.web.bind.annotation.PathVariable("id") Long id, HttpSession session, Model model) {
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }
        
        Alumno alumno = servicioAlumno.obtenerPorId(id);
        if (alumno == null) {
            return "redirect:/alumnos";
        }

        List<String> listaTelefonos = alumno.getTelefonos().stream()
                .map(com.contrapunto.control_contrapunto.model.TelefonoAlumno::getTelefono)
                .toList();
        List<String> listaCorreos = alumno.getCorreos().stream()
                .map(com.contrapunto.control_contrapunto.model.CorreoAlumno::getCorreo)
                .toList();

        model.addAttribute("usuarioActivo", session.getAttribute("adminLogueado"));
        model.addAttribute("activePage", "alumnos");
        model.addAttribute("alumno", alumno);
        model.addAttribute("listaTelefonos", listaTelefonos);
        model.addAttribute("listaCorreos", listaCorreos);
        
        return "registro-alumno";
    }

    @PostMapping("/alumnos/actualizar/{id}")
    public String actualizarAlumno(
            @org.springframework.web.bind.annotation.PathVariable("id") Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam(value = "telefonos[]", required = false) List<String> listaTelefonos,
            @RequestParam(value = "correos[]", required = false) List<String> listaCorreos,
            HttpSession session) {
            
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }

        if (nombre != null && !nombre.trim().isEmpty()) {
            servicioAlumno.actualizarAlumno(id, nombre.trim(), listaTelefonos, listaCorreos);
        }

        return "redirect:/alumnos";
    }
}
