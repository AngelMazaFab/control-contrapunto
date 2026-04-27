package com.contrapunto.control_contrapunto.controller;

import com.contrapunto.control_contrapunto.service.ServicioProfesor;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ProfesorController {

    private final ServicioProfesor servicioProfesor;

    @GetMapping("/profesores")
    public String listarProfesores(HttpSession session, Model model) {
        // Protección de sesión (igual que MenuController)
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuarioActivo", session.getAttribute("adminLogueado"));
        model.addAttribute("activePage", "profesores");
        model.addAttribute("profesores", servicioProfesor.listarTodos());
        return "profesores";
    }
}
