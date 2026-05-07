package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.model.Profesor;
import com.contrapunto.control_contrapunto.model.CorreoProfesor;
import com.contrapunto.control_contrapunto.model.TelefonoProfesor;
import com.contrapunto.control_contrapunto.repository.ProfesorRepository;
import com.contrapunto.control_contrapunto.repository.TelefonoProfesorRepository;
import com.contrapunto.control_contrapunto.repository.CorreoProfesorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio encargado de gestionar la lógica de negocio de los Profesores.
 * Maneja el registro de sus datos laborales y su información de contacto.
 */
@Service
@RequiredArgsConstructor
public class ServicioProfesor {

    private final ProfesorRepository profesorRepository;
    private final TelefonoProfesorRepository telefonoProfesorRepository;
    private final CorreoProfesorRepository correoProfesorRepository;

    /**
     * Obtiene la lista de todos los profesores registrados.
     */
    public List<Profesor> listarTodos() {
        return profesorRepository.findAll();
    }

    /**
     * Busca un profesor por su identificador único.
     */
    public Profesor obtenerPorId(Long id) {
        return profesorRepository.findById(id).orElse(null);
    }

    /**
     * Registra un nuevo profesor con su sueldo base y listas de contacto.
     */
    @Transactional
    public void guardarProfesorConContactos(String nombre, Double sueldoBase, List<String> telefonos, List<String> correos) {
        // Bloque: Creación del perfil laboral
        Profesor profesor = new Profesor();
        profesor.setNombreProfesor(nombre);
        profesor.setSueldoBase(sueldoBase);
        
        // Se guarda explícitamente el profesor primero para obtener su ID real para las relaciones
        profesor = profesorRepository.save(profesor);

        // Bloque: Registro de Teléfonos de contacto
        if (telefonos != null) {
            for (String tel : telefonos) {
                if (tel != null && !tel.trim().isEmpty()) {
                    TelefonoProfesor tp = new TelefonoProfesor();
                    tp.setTelefono(tel.trim());
                    tp.setProfesor(profesor);
                    telefonoProfesorRepository.save(tp);
                }
            }
        }

        // Bloque: Registro de Correos de contacto
        if (correos != null) {
            for (String email : correos) {
                if (email != null && !email.trim().isEmpty()) {
                    CorreoProfesor cp = new CorreoProfesor();
                    cp.setCorreo(email.trim());
                    cp.setProfesor(profesor);
                    correoProfesorRepository.save(cp);
                }
            }
        }
    }

    /**
     * Actualiza la información de un profesor y renueva sus contactos.
     */
    @Transactional
    public void actualizarProfesor(Long id, String nombre, Double sueldoBase, List<String> telefonos, List<String> correos) {
        // Bloque: Búsqueda del registro
        Profesor profesor = obtenerPorId(id);
        if (profesor != null) {
            profesor.setNombreProfesor(nombre);
            profesor.setSueldoBase(sueldoBase);
            
            // Bloque: Limpieza de contactos actuales
            if (profesor.getTelefonos() != null) {
                profesor.getTelefonos().clear();
            } else {
                profesor.setTelefonos(new java.util.ArrayList<>());
            }
            
            if (profesor.getCorreos() != null) {
                profesor.getCorreos().clear();
            } else {
                profesor.setCorreos(new java.util.ArrayList<>());
            }

            // Bloque: Inserción de nuevos Teléfonos
            if (telefonos != null) {
                for (String tel : telefonos) {
                    if (tel != null && !tel.trim().isEmpty()) {
                        TelefonoProfesor tp = new TelefonoProfesor();
                        tp.setTelefono(tel.trim());
                        tp.setProfesor(profesor);
                        profesor.getTelefonos().add(tp);
                    }
                }
            }

            // Bloque: Inserción de nuevos Correos
            if (correos != null) {
                for (String email : correos) {
                    if (email != null && !email.trim().isEmpty()) {
                        CorreoProfesor cp = new CorreoProfesor();
                        cp.setCorreo(email.trim());
                        cp.setProfesor(profesor);
                        profesor.getCorreos().add(cp);
                    }
                }
            }

            // Guarda todos los cambios en cascada
            profesorRepository.save(profesor);
        }
    }

    /**
     * Elimina a un profesor y sus datos de contacto relacionados.
     */
    @Transactional
    public void eliminarProfesor(Long id) {
        Profesor profesor = obtenerPorId(id);
        if (profesor != null) {
            // Bloque: Eliminación manual de relaciones
            if (profesor.getTelefonos() != null) {
                telefonoProfesorRepository.deleteAll(profesor.getTelefonos());
            }
            if (profesor.getCorreos() != null) {
                correoProfesorRepository.deleteAll(profesor.getCorreos());
            }
            
            // Eliminación definitiva del perfil
            profesorRepository.delete(profesor);
        }
    }
}
