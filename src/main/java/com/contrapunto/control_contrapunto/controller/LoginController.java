package com.contrapunto.control_contrapunto.controller;

import com.contrapunto.control_contrapunto.model.Admin;
import com.contrapunto.control_contrapunto.service.ServicioAdmin;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final ServicioAdmin servicioAdmin;

    /** CU01 – Mostrar pantalla de inicio de sesión */
    @GetMapping({"/", "/login"})
    public String mostrarLogin(@RequestParam(value = "error", required = false) String error,
                               HttpSession session,
                               Model model) {
        // Si ya hay sesión activa, redirigir al menú
        if (session.getAttribute("adminLogueado") != null) {
            return "redirect:/menu";
        }
        if (error != null) {
            model.addAttribute("errorLogin", true);
        }
        return "login";
    }

    /** CU01 – Procesar credenciales (FA_001: error si son inválidas) */
    @PostMapping("/login")
    public String procesarLogin(@RequestParam("usuario") String usuario,
                                @RequestParam("contrasena") String contrasena,
                                HttpSession session) {
        try {
            Optional<Admin> adminOpt = servicioAdmin.validarCredenciales(usuario, contrasena);

            if (adminOpt.isPresent()) {
                session.setAttribute("adminLogueado", adminOpt.get().getUsuario());
                return "redirect:/menu";
            }
        } catch (Exception e) {
            // Error de BD u otro error inesperado → FA_001
            System.err.println("[LoginController] Error al validar credenciales: " + e.getMessage());
        }

        // FA_001 – Credenciales inválidas o error técnico
        return "redirect:/login?error";
    }

    /** Cerrar sesión */
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
