package com.contrapunto.control_contrapunto.controller;

import com.contrapunto.control_contrapunto.model.Salon;
import com.contrapunto.control_contrapunto.service.SalonService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ClasesSalonesController {

    private final SalonService salonService;

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

        // Inyectar datos de salones
        model.addAttribute("salones", salonService.listarTodos());
        model.addAttribute("salonObj", new Salon());

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
}
