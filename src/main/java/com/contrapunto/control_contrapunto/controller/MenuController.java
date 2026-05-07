package com.contrapunto.control_contrapunto.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador de redirección central.
 * Gestiona la página de inicio (Dashboard) y provee rutas de compatibilidad
 * para mantener vivos los enlaces antiguos hacia las pestañas de clases y salones.
 */
@Controller
public class MenuController {

    /** 
     * Pantalla de Menú Principal – Protegida por validación de sesión.
     */
    @GetMapping("/menu")
    public String mostrarMenu(HttpSession session, Model model) {
        // Bloque: Validación de sesión
        String admin = (String) session.getAttribute("adminLogueado");
        if (admin == null) {
            return "redirect:/login";
        }
        
        // Bloque: Inyección de datos
        model.addAttribute("usuarioActivo", admin);
        return "menu";
    }

    /**
     * Bloque de Redireccionadores de compatibilidad.
     * Las rutas antiguas /salones, /clases y /talleres redirigen
     * al nuevo módulo unificado con la pestaña correspondiente abierta.
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
        return "redirect:/clases-salones?tab=talleres"; // Si hubiese tab de talleres en el futuro
    }
}
