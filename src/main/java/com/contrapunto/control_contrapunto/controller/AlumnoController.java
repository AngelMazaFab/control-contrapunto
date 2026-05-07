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

/**
 * Controlador encargado de gestionar las operaciones relacionadas con los Alumnos.
 * Maneja las rutas para listar, crear, editar y eliminar alumnos,
 * protegiendo los accesos mediante validación de sesión.
 */
@Controller
@RequiredArgsConstructor
public class AlumnoController {

    // Servicio que contiene la lógica de negocio para los alumnos
    private final ServicioAlumno servicioAlumno;

    /**
     * Muestra la lista general de alumnos.
     * 
     * @param session Sesión HTTP actual para validar el acceso.
     * @param model   Modelo para pasar datos a la vista.
     * @return El nombre de la vista HTML a renderizar.
     */
    @GetMapping("/alumnos")
    public String listarAlumnos(HttpSession session, Model model) {
        // Bloque: Protección de ruta
        // Verifica si existe un administrador logueado en la sesión; si no, redirige al login
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }

        // Bloque: Carga de datos para la vista
        // Agrega información de la sesión y la página activa para el menú lateral
        model.addAttribute("usuarioActivo", session.getAttribute("adminLogueado"));
        model.addAttribute("activePage", "alumnos");
        // Obtiene todos los alumnos desde el servicio y los inyecta en la vista
        model.addAttribute("alumnos", servicioAlumno.listarTodos());
        
        return "alumnos";
    }

    /**
     * Muestra el formulario para registrar un nuevo alumno.
     */
    @GetMapping("/alumnos/nuevo")
    public String mostrarFormularioRegistro(HttpSession session, Model model) {
        // Bloque: Protección de ruta
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }
        
        // Bloque: Preparación del formulario
        // Se envía un objeto Alumno vacío para que Thymeleaf pueda hacer el binding en el formulario
        model.addAttribute("usuarioActivo", session.getAttribute("adminLogueado"));
        model.addAttribute("activePage", "alumnos");
        model.addAttribute("alumno", new Alumno());
        
        return "registro-alumno";
    }

    /**
     * Recibe los datos del formulario de registro y guarda un nuevo alumno.
     */
    @PostMapping("/alumnos/guardar")
    public String guardarAlumno(
            @RequestParam("nombre") String nombre,
            @RequestParam(value = "telefonos[]", required = false) List<String> listaTelefonos,
            @RequestParam(value = "correos[]", required = false) List<String> listaCorreos,
            HttpSession session) {
            
        // Bloque: Protección de ruta
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }

        // Bloque: Validación y persistencia
        // Verifica que el nombre no esté vacío o sea solo espacios antes de delegar al servicio
        if (nombre != null && !nombre.trim().isEmpty()) {
            // El servicio se encarga de guardar el alumno junto con sus listas de contacto
            servicioAlumno.guardarAlumnoConContactos(nombre.trim(), listaTelefonos, listaCorreos);
        }

        // Redirige a la lista de alumnos una vez guardado
        return "redirect:/alumnos";
    }

    /**
     * Muestra el formulario de edición cargando los datos de un alumno existente.
     */
    @GetMapping("/alumnos/editar/{id}")
    public String mostrarFormularioEdicion(@org.springframework.web.bind.annotation.PathVariable("id") Long id, HttpSession session, Model model) {
        // Bloque: Protección de ruta
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }
        
        // Bloque: Búsqueda del alumno
        // Intenta obtener el alumno por su ID; si no existe, regresa a la tabla principal
        Alumno alumno = servicioAlumno.obtenerPorId(id);
        if (alumno == null) {
            return "redirect:/alumnos";
        }

        // Bloque: Extracción de contactos
        // Convierte las entidades de teléfonos y correos a listas de Strings simples para la vista
        List<String> listaTelefonos = alumno.getTelefonos().stream()
                .map(com.contrapunto.control_contrapunto.model.TelefonoAlumno::getTelefono)
                .toList();
        List<String> listaCorreos = alumno.getCorreos().stream()
                .map(com.contrapunto.control_contrapunto.model.CorreoAlumno::getCorreo)
                .toList();

        // Bloque: Carga de datos al modelo
        // Se inyecta el alumno encontrado y sus contactos para prellenar el formulario de edición
        model.addAttribute("usuarioActivo", session.getAttribute("adminLogueado"));
        model.addAttribute("activePage", "alumnos");
        model.addAttribute("alumno", alumno);
        model.addAttribute("listaTelefonos", listaTelefonos);
        model.addAttribute("listaCorreos", listaCorreos);
        
        return "registro-alumno";
    }

    /**
     * Recibe los datos modificados de un alumno y los actualiza en la base de datos.
     */
    @PostMapping("/alumnos/actualizar/{id}")
    public String actualizarAlumno(
            @org.springframework.web.bind.annotation.PathVariable("id") Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam(value = "telefonos[]", required = false) List<String> listaTelefonos,
            @RequestParam(value = "correos[]", required = false) List<String> listaCorreos,
            HttpSession session) {
            
        // Bloque: Protección de ruta
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }

        // Bloque: Actualización de datos
        // Si el nombre es válido, se llama al servicio para actualizar el alumno y reemplazar sus listas de contactos
        if (nombre != null && !nombre.trim().isEmpty()) {
            servicioAlumno.actualizarAlumno(id, nombre.trim(), listaTelefonos, listaCorreos);
        }

        return "redirect:/alumnos";
    }

    /**
     * Elimina un alumno del sistema basado en su ID.
     */
    @GetMapping("/alumnos/eliminar/{id}")
    public String eliminarAlumno(@org.springframework.web.bind.annotation.PathVariable("id") Long id, HttpSession session) {
        // Bloque: Protección de ruta
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }
        
        // Ejecuta la eliminación lógica o física (según el servicio) del alumno
        servicioAlumno.eliminarAlumno(id);
        
        return "redirect:/alumnos";
    }
}
