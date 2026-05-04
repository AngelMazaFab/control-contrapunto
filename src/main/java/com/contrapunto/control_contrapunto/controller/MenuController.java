package com.contrapunto.control_contrapunto.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    /** Pantalla de Menú Principal – protegida por sesión */
    @GetMapping("/menu")
    public String mostrarMenu(HttpSession session, Model model) {
        String admin = (String) session.getAttribute("adminLogueado");
        if (admin == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioActivo", admin);
        return "menu";
    }

    /**
     * Redireccionadores de compatibilidad.
     * Las rutas antiguas /salones, /clases y /talleres redirigen
     * al módulo unificado con la pestaña correspondiente.
     */
    @GetMapping("/salones")
    public String redirigirSalones(HttpSession session) {
        if (session.getAttribute("adminLogueado") == null) return "redirect:/login";
        return "redirect:/clases-salones?tab=salones";
    }

    @GetMapping("/clases")
    public String redirigirClases(HttpSession session) {
        if (session.getAttribute("adminLogueado") == null) return "redirect:/login";
        return "redirect:/clases-salones?tab=clases";
    }

    @GetMapping("/talleres")
    public String redirigirTalleres(HttpSession session) {
        if (session.getAttribute("adminLogueado") == null) return "redirect:/login";
        return "redirect:/clases-salones?tab=talleres";
    }
}
