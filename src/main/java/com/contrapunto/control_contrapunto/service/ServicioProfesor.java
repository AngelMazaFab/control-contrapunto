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

@Service
@RequiredArgsConstructor
public class ServicioProfesor {

    private final ProfesorRepository profesorRepository;
    private final TelefonoProfesorRepository telefonoProfesorRepository;
    private final CorreoProfesorRepository correoProfesorRepository;

    public List<Profesor> listarTodos() {
        return profesorRepository.findAll();
    }

    public Profesor obtenerPorId(Long id) {
        return profesorRepository.findById(id).orElse(null);
    }

    @Transactional
    public void guardarProfesorConContactos(String nombre, Double sueldoBase, List<String> telefonos, List<String> correos) {
        Profesor profesor = new Profesor();
        profesor.setNombreProfesor(nombre);
        profesor.setSueldoBase(sueldoBase);
        
        // Guardar explícitamente el profesor primero para obtener su ID real
        profesor = profesorRepository.save(profesor);

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

    @Transactional
    public void actualizarProfesor(Long id, String nombre, Double sueldoBase, List<String> telefonos, List<String> correos) {
        Profesor profesor = obtenerPorId(id);
        if (profesor != null) {
            profesor.setNombreProfesor(nombre);
            profesor.setSueldoBase(sueldoBase);
            
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

            profesorRepository.save(profesor);
        }
    }

    @Transactional
    public void eliminarProfesor(Long id) {
        Profesor profesor = obtenerPorId(id);
        if (profesor != null) {
            if (profesor.getTelefonos() != null) {
                telefonoProfesorRepository.deleteAll(profesor.getTelefonos());
            }
            if (profesor.getCorreos() != null) {
                correoProfesorRepository.deleteAll(profesor.getCorreos());
            }
            profesorRepository.delete(profesor);
        }
    }
}
