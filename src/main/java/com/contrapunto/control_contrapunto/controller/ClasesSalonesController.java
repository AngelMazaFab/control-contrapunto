package com.contrapunto.control_contrapunto.controller;

import com.contrapunto.control_contrapunto.model.Salon;
import com.contrapunto.control_contrapunto.model.Clase;
import com.contrapunto.control_contrapunto.service.ServicioSalon;
import com.contrapunto.control_contrapunto.service.ServicioClase;
import com.contrapunto.control_contrapunto.service.ServicioProfesor;
import com.contrapunto.control_contrapunto.service.ServicioAlumno;
import com.contrapunto.control_contrapunto.model.Materia;
import com.contrapunto.control_contrapunto.service.ServicioMateria;
import com.contrapunto.control_contrapunto.repository.DiaSemanaRepository;
import com.contrapunto.control_contrapunto.repository.InasistenciaRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador principal para la gestión de Clases y Salones.
 * Permite visualizar la matriz de clases, agendar nuevas, y administrar los salones disponibles.
 */
@Controller
@RequiredArgsConstructor
public class ClasesSalonesController {

    // Dependencias inyectadas para interactuar con la lógica de negocio y repositorios
    private final ServicioSalon servicioSalon;
    private final ServicioClase servicioClase;
    private final ServicioProfesor servicioProfesor;
    private final ServicioAlumno servicioAlumno;
    private final ServicioMateria servicioMateria;
    private final DiaSemanaRepository diaSemanaRepository;
    private final InasistenciaRepository inasistenciaRepository;
    private final com.contrapunto.control_contrapunto.service.ServicioClaseCleanup servicioClaseCleanup;

    /**
     * Muestra la vista principal que contiene las pestañas de "Clases" y "Salones".
     */
    @GetMapping("/clases-salones")
    public String mostrarClasesSalones(
            @RequestParam(value = "tab", required = false, defaultValue = "salones") String tab,
            HttpSession session,
            Model model) {

        // Bloque: Validación de sesión
        String admin = (String) session.getAttribute("adminLogueado");
        if (admin == null) {
            return "redirect:/login";
        }

        // Bloque: Limpieza programada manual
        // Garantiza que si el servidor estuvo apagado a medianoche,
        // al cargar la vista se eliminen las clases expiradas (ej. reposiciones antiguas) antes de mostrarlas.
        servicioClaseCleanup.limpiarReposicionesAntiguas();

        // Bloque: Estado de la vista (Tabs)
        model.addAttribute("usuarioActivo", admin);
        model.addAttribute("activePage", "clases-salones");
        model.addAttribute("tabActivo", tab); // Define qué pestaña (salones o clases) se debe mostrar abierta

        // Bloque: Datos para gestión de Salones
        model.addAttribute("salones", servicioSalon.listarTodos());
        model.addAttribute("salonObj", new Salon());

        // Bloque: Datos para gestión de Materias
        model.addAttribute("materias", servicioMateria.listarTodas());
        model.addAttribute("materiaObj", new Materia());

        // Bloque: Diccionarios/Catálogos para el modal de agendar Clase
        // Estas listas alimentan los desplegables (selects) en los formularios de registro/edición
        model.addAttribute("listProfesores", servicioProfesor.listarTodos());
        model.addAttribute("listAlumnos", servicioAlumno.listarTodos());
        model.addAttribute("listMaterias", servicioMateria.listarTodas());
        model.addAttribute("listDiasSemana", diaSemanaRepository.findAll());
        model.addAttribute("listSalones", servicioSalon.listarTodos());
        model.addAttribute("listInasistencias", inasistenciaRepository.findInasistenciasPendientes());
        
        // Datos principales de clases registradas
        model.addAttribute("listClases", servicioClase.listarTodos());
        model.addAttribute("claseObj", new Clase());

        return "clases-salones";
    }

    /**
     * Persiste un nuevo salón en la base de datos.
     */
    @PostMapping("/salones/guardar")
    public String guardarSalon(@ModelAttribute("salonObj") Salon salon, HttpSession session) {
        // Bloque: Protección de ruta
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }
        
        // Delega la persistencia al servicio de salones
        servicioSalon.guardar(salon);
        
        // Retorna a la vista principal indicando que se debe abrir la pestaña de "salones"
        return "redirect:/clases-salones?tab=salones";
    }

    /**
     * Elimina un salón por su identificador.
     */
    @GetMapping("/salones/eliminar/{id}")
    public String eliminarSalon(@PathVariable("id") Long id, HttpSession session) {
        // Bloque: Protección de ruta
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }
        
        // Ejecuta la eliminación física del salón
        servicioSalon.eliminar(id);
        
        return "redirect:/clases-salones?tab=salones";
    }

    /**
     * Persiste una nueva materia en la base de datos.
     */
    @PostMapping("/materias/guardar")
    public String guardarMateria(@ModelAttribute("materiaObj") Materia materia, HttpSession session) {
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }
        servicioMateria.guardar(materia);
        return "redirect:/clases-salones?tab=materias";
    }

    /**
     * Elimina una materia por su identificador.
     */
    @GetMapping("/materias/eliminar/{id}")
    public String eliminarMateria(@PathVariable("id") Long id, HttpSession session) {
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }
        servicioMateria.eliminar(id);
        return "redirect:/clases-salones?tab=materias";
    }

    /**
     * Intenta agendar una nueva clase aplicando las reglas de validación (ej. cero empalmes).
     */
    @PostMapping("/clases/guardar")
    public String guardarClase(@ModelAttribute("claseObj") Clase clase,
                               HttpSession session,
                               RedirectAttributes redirectAttrs) {
        // Bloque: Protección de ruta
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }
        
        // Bloque: Procesamiento y validaciones de negocio
        try {
            // Asegurar que si el checkbox de tipoClase (reposición) no fue enviado, se asigne falso por defecto
            if (clase.getTipoClase() == null) clase.setTipoClase(false);
            
            // Llama al servicio, el cual podría lanzar una excepción si detecta empalmes de horario
            servicioClase.agendarClase(clase);
            
            // Mensaje de éxito visible tras la redirección
            redirectAttrs.addFlashAttribute("claseExito", "Clase registrada correctamente.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            // Bloque: Manejo de errores de negocio (Empalmes)
            // Se captura el mensaje exacto lanzado por el servicio para mostrárselo al usuario
            redirectAttrs.addFlashAttribute("claseError", e.getMessage());
        } catch (Exception e) {
            // Bloque: Manejo de errores genéricos (fallback)
            System.err.println("Error inesperado al agendar clase: " + e.getMessage());
            redirectAttrs.addFlashAttribute("claseError", "Error inesperado al guardar la clase.");
        }
        
        // Retorna a la vista principal en la pestaña de clases
        return "redirect:/clases-salones?tab=clases";
    }

    /**
     * Elimina una clase existente por su ID.
     */
    @GetMapping("/clases/eliminar/{id}")
    public String eliminarClase(@PathVariable("id") Long id, HttpSession session) {
        // Bloque: Protección de ruta
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }
        
        servicioClase.eliminar(id);
        
        return "redirect:/clases-salones?tab=clases";
    }

    /**
     * Actualiza los datos de una clase existente.
     * Recibe el mismo DTO que guardar, más el campo idClase para identificar el registro.
     */
    @PostMapping("/clases/actualizar")
    public String actualizarClase(@ModelAttribute("claseObj") Clase clase,
                                  @RequestParam("idClase") Long idClase,
                                  HttpSession session,
                                  RedirectAttributes redirectAttrs) {
        // Bloque: Protección de ruta
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/login";
        }
        
        // Bloque: Procesamiento de actualización
        try {
            // Se normaliza el flag de reposición
            if (clase.getTipoClase() == null) clase.setTipoClase(false);
            
            // El servicio valida que no haya empalmes con *otras* clases (excluyendo esta misma)
            servicioClase.actualizarClase(idClase, clase);
            
            redirectAttrs.addFlashAttribute("claseExito", "Clase actualizada correctamente.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            // Bloque: Manejo de errores al actualizar (ej. el nuevo horario produce un empalme)
            redirectAttrs.addFlashAttribute("claseError", e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado al actualizar clase: " + e.getMessage());
            redirectAttrs.addFlashAttribute("claseError", "Error inesperado al actualizar la clase.");
        }
        
        return "redirect:/clases-salones?tab=clases";
    }
}
