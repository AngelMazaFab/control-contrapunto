package com.contrapunto.control_contrapunto.controller;

import com.contrapunto.control_contrapunto.model.Profesor;
import com.contrapunto.control_contrapunto.repository.ClaseRepository;
import com.contrapunto.control_contrapunto.service.ServicioProfesor;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador responsable de la gestión de Profesores.
 * Cubre el listado, creación, actualización y eliminación de los maestros,
 * además de proporcionar estadísticas básicas como la cantidad de clases asignadas.
 */
@Controller
@RequiredArgsConstructor
public class ProfesorController {

    private final ServicioProfesor servicioProfesor;
    private final ClaseRepository claseRepository;

    /**
     * Lista todos los profesores registrados.
     */
    @GetMapping("/profesores")
    public String listarProfesores(HttpSession session, Model model) {
        // Bloque: Protección de ruta
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }

        // Obtiene la lista completa de maestros
        List<Profesor> profesores = servicioProfesor.listarTodos();

        // Bloque: Cálculo de Estadísticas (Clases por Profesor)
        // Construye un diccionario para mapear: idProfesor -> cantidad de clases asignadas actualmente
        Map<Long, Long> clasesCount = new HashMap<>();
        for (Profesor p : profesores) {
            clasesCount.put(p.getIdProfesor(), claseRepository.contarClasesPorProfesor(p.getIdProfesor()));
        }

        // Bloque: Preparación de la vista
        model.addAttribute("usuarioActivo", session.getAttribute("adminLogueado"));
        model.addAttribute("activePage", "profesores");
        model.addAttribute("profesores", profesores);
        model.addAttribute("clasesCount", clasesCount);
        
        return "profesores";
    }

    /**
     * Muestra el formulario vacío para agregar un nuevo profesor.
     */
    @GetMapping("/profesores/nuevo")
    public String mostrarFormularioRegistro(HttpSession session, Model model) {
        // Bloque: Protección de ruta
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }
        
        // Bloque: Envío de objeto contenedor
        model.addAttribute("usuarioActivo", session.getAttribute("adminLogueado"));
        model.addAttribute("activePage", "profesores");
        model.addAttribute("profesor", new Profesor());
        
        return "registro-profesor";
    }

    /**
     * Procesa la solicitud POST para guardar un profesor recién creado.
     */
    @PostMapping("/profesores/guardar")
    public String guardarProfesor(
            @RequestParam("nombre") String nombre,
            @RequestParam(value = "sueldoBase", required = false) Double sueldoBase,
            @RequestParam(value = "telefonos", required = false) List<String> listaTelefonos,
            @RequestParam(value = "correos", required = false) List<String> listaCorreos,
            HttpSession session) {
            
        // Bloque: Protección de ruta
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }

        // Bloque: Validación y Almacenamiento
        // Revisa que el nombre no sea nulo ni espacios en blanco
        if (nombre != null && !nombre.trim().isEmpty()) {
            // El servicio orquesta la creación del maestro junto con las entidades hijas (teléfonos y correos)
            servicioProfesor.guardarProfesorConContactos(nombre.trim(), sueldoBase, listaTelefonos, listaCorreos);
        }

        return "redirect:/profesores";
    }

    /**
     * Busca los datos de un profesor existente para prellenar el formulario de edición.
     */
    @GetMapping("/profesores/editar/{id}")
    public String mostrarFormularioEdicion(@org.springframework.web.bind.annotation.PathVariable("id") Long id, HttpSession session, Model model) {
        // Bloque: Protección de ruta
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }
        
        // Bloque: Búsqueda del registro
        Profesor profesor = servicioProfesor.obtenerPorId(id);
        if (profesor == null) {
            return "redirect:/profesores"; // Regresa a la tabla si el ID es inválido
        }

        // Bloque: Extracción de contactos
        // Convierte las listas de entidades (Telefonos/Correos) en listas de Strings planas para la vista
        List<String> listaTelefonos = profesor.getTelefonos().stream()
                .map(com.contrapunto.control_contrapunto.model.TelefonoProfesor::getTelefono)
                .toList();
        List<String> listaCorreos = profesor.getCorreos().stream()
                .map(com.contrapunto.control_contrapunto.model.CorreoProfesor::getCorreo)
                .toList();

        // Bloque: Preparación de la vista de Edición
        model.addAttribute("usuarioActivo", session.getAttribute("adminLogueado"));
        model.addAttribute("activePage", "profesores");
        model.addAttribute("profesor", profesor);
        model.addAttribute("listaTelefonos", listaTelefonos);
        model.addAttribute("listaCorreos", listaCorreos);
        
        return "registro-profesor";
    }

    /**
     * Procesa la actualización de un profesor con sus nuevos datos y listas de contacto.
     */
    @PostMapping("/profesores/actualizar/{id}")
    public String actualizarProfesor(
            @org.springframework.web.bind.annotation.PathVariable("id") Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam(value = "sueldoBase", required = false) Double sueldoBase,
            @RequestParam(value = "telefonos", required = false) List<String> listaTelefonos,
            @RequestParam(value = "correos", required = false) List<String> listaCorreos,
            HttpSession session) {
            
        // Bloque: Protección de ruta
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }

        // Bloque: Procesamiento
        if (nombre != null && !nombre.trim().isEmpty()) {
            // El servicio limpia las listas de contacto antiguas y persiste las nuevas
            servicioProfesor.actualizarProfesor(id, nombre.trim(), sueldoBase, listaTelefonos, listaCorreos);
        }

        return "redirect:/profesores";
    }

    /**
     * Elimina a un profesor permanentemente de la base de datos (según su ID).
     */
    @GetMapping("/profesores/eliminar/{id}")
    public String eliminarProfesor(@org.springframework.web.bind.annotation.PathVariable("id") Long id, HttpSession session) {
        // Bloque: Protección de ruta
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }
        
        servicioProfesor.eliminarProfesor(id);
        
        return "redirect:/profesores";
    }
}
