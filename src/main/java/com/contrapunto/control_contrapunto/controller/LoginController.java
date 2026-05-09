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

/**
 * Controlador encargado de la autenticación de usuarios.
 * Maneja el inicio y cierre de sesión, validando credenciales contra la base de datos.
 */
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final ServicioAdmin servicioAdmin;

    /** 
     * CU01 – Mostrar pantalla de inicio de sesión.
     * Atiende la ruta raíz ("/") y "/login".
     */
    @GetMapping({"/", "/login"})
    public String mostrarLogin(@RequestParam(value = "error", required = false) String error,
                               HttpSession session,
                               Model model) {
        // Bloque: Redirección automática
        // Si el usuario ya cuenta con una sesión activa, no le mostramos el login, va directo al menú.
        if (session.getAttribute("adminLogueado") != null) {
            return "redirect:/menu";
        }
        
        // Bloque: Manejo de bandera de error
        // Si la URL tiene el parámetro ?error (proveniente de un intento fallido), se avisa a la vista.
        if (error != null) {
            model.addAttribute("errorLogin", true);
        }
        
        return "login";
    }

    /** 
     * CU01 – Procesar credenciales enviadas desde el formulario.
     * (FA_001: Flujo alternativo si las credenciales son inválidas).
     */
    @PostMapping("/login")
    public String procesarLogin(@RequestParam("usuario") String usuario,
                                @RequestParam("contrasena") String contrasena,
                                HttpSession session) {
        try {
            // Bloque: Validación
            // El servicio busca al usuario en la BD y comprueba si la contraseña en texto plano coincide.
            Optional<Admin> adminOpt = servicioAdmin.validarCredenciales(usuario, contrasena);

            // Bloque: Creación de Sesión
            if (adminOpt.isPresent()) {
                // Se guarda el nombre de usuario en la variable de sesión para proteger otras rutas del sistema.
                session.setAttribute("adminLogueado", adminOpt.get().getUsuario());
                return "redirect:/menu";
            }
        } catch (Exception e) {
            // Bloque: Manejo de errores
            // Error de BD u otro error inesperado (FA_001). Solo se loguea para debug.
            System.err.println("[LoginController] Error al validar credenciales: " + e.getMessage());
        }

        // FA_001 – Credenciales inválidas o error técnico. Se redirige añadiendo el flag de error.
        return "redirect:/login?error";
    }

    /** 
     * Invalida la sesión actual del usuario, forzándolo a volver a autenticarse.
     */
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        // Destruye todos los datos de la sesión HTTP
        session.invalidate();
        return "redirect:/login";
    }
}
