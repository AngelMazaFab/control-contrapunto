package com.contrapunto.control_contrapunto.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
     * Módulo unificado Clases/Salones.
     * Acepta el parámetro opcional ?tab= (salones | clases | talleres)
     * para pre-seleccionar la sub-vista desde el servidor si se requiriera.
     * La selección visual se gestiona en el cliente (JS).
     */
    @GetMapping("/clases-salones")
    public String mostrarClasesSalones(
            @RequestParam(value = "tab", required = false, defaultValue = "salones") String tab,
            HttpSession session,
            Model model) {

        String admin = (String) session.getAttribute("adminLogueado");
        if (admin == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuarioActivo", admin);
        model.addAttribute("activePage", "clases-salones");
        model.addAttribute("tabActivo", tab);

        /*
         * TODO (próxima fase): cargar las listas desde los servicios
         *   model.addAttribute("salones", servicioSalon.listarTodos());
         *   model.addAttribute("alumnos", servicioAlumno.listarTodos());
         *   model.addAttribute("clases",  servicioClase.listarTodas());
         *   model.addAttribute("talleres", servicioTaller.listarTodos());
         */

        return "clases-salones";
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
