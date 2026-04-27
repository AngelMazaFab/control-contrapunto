package com.contrapunto.control_contrapunto.controller;

import com.contrapunto.control_contrapunto.service.ServicioAlumno;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AlumnoController {

    private final ServicioAlumno servicioAlumno;

    @GetMapping("/alumnos")
    public String listarAlumnos(HttpSession session, Model model) {
        // Protección de sesión
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuarioActivo", session.getAttribute("adminLogueado"));
        model.addAttribute("activePage", "alumnos");
        model.addAttribute("alumnos", servicioAlumno.listarTodos());
        return "alumnos";
    }
}
