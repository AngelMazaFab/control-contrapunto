package com.contrapunto.control_contrapunto.controller;

import com.contrapunto.control_contrapunto.model.Profesor;
import com.contrapunto.control_contrapunto.service.ServicioProfesor;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProfesorController {

    private final ServicioProfesor servicioProfesor;

    @GetMapping("/profesores")
    public String listarProfesores(HttpSession session, Model model) {
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuarioActivo", session.getAttribute("adminLogueado"));
        model.addAttribute("activePage", "profesores");
        model.addAttribute("profesores", servicioProfesor.listarTodos());
        return "profesores";
    }

    @GetMapping("/profesores/nuevo")
    public String mostrarFormularioRegistro(HttpSession session, Model model) {
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("usuarioActivo", session.getAttribute("adminLogueado"));
        model.addAttribute("activePage", "profesores");
        model.addAttribute("profesor", new Profesor());
        return "registro-profesor";
    }

    @PostMapping("/profesores/guardar")
    public String guardarProfesor(
            @RequestParam("nombre") String nombre,
            @RequestParam(value = "sueldoBase", required = false) Double sueldoBase,
            @RequestParam(value = "telefonos[]", required = false) List<String> listaTelefonos,
            @RequestParam(value = "correos[]", required = false) List<String> listaCorreos,
            HttpSession session) {
            
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }

        if (nombre != null && !nombre.trim().isEmpty()) {
            servicioProfesor.guardarProfesorConContactos(nombre.trim(), sueldoBase, listaTelefonos, listaCorreos);
        }

        return "redirect:/profesores";
    }

    @GetMapping("/profesores/editar/{id}")
    public String mostrarFormularioEdicion(@org.springframework.web.bind.annotation.PathVariable("id") Long id, HttpSession session, Model model) {
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }
        
        Profesor profesor = servicioProfesor.obtenerPorId(id);
        if (profesor == null) {
            return "redirect:/profesores";
        }

        List<String> listaTelefonos = profesor.getTelefonos().stream()
                .map(com.contrapunto.control_contrapunto.model.TelefonoProfesor::getTelefono)
                .toList();
        List<String> listaCorreos = profesor.getCorreos().stream()
                .map(com.contrapunto.control_contrapunto.model.CorreoProfesor::getCorreo)
                .toList();

        model.addAttribute("usuarioActivo", session.getAttribute("adminLogueado"));
        model.addAttribute("activePage", "profesores");
        model.addAttribute("profesor", profesor);
        model.addAttribute("listaTelefonos", listaTelefonos);
        model.addAttribute("listaCorreos", listaCorreos);
        
        return "registro-profesor";
    }

    @PostMapping("/profesores/actualizar/{id}")
    public String actualizarProfesor(
            @org.springframework.web.bind.annotation.PathVariable("id") Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam(value = "sueldoBase", required = false) Double sueldoBase,
            @RequestParam(value = "telefonos[]", required = false) List<String> listaTelefonos,
            @RequestParam(value = "correos[]", required = false) List<String> listaCorreos,
            HttpSession session) {
            
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }

        if (nombre != null && !nombre.trim().isEmpty()) {
            servicioProfesor.actualizarProfesor(id, nombre.trim(), sueldoBase, listaTelefonos, listaCorreos);
        }

        return "redirect:/profesores";
    }

    @GetMapping("/profesores/eliminar/{id}")
    public String eliminarProfesor(@org.springframework.web.bind.annotation.PathVariable("id") Long id, HttpSession session) {
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }
        
        servicioProfesor.eliminarProfesor(id);
        return "redirect:/profesores";
    }
}
