package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.model.Alumno;
import com.contrapunto.control_contrapunto.repository.AlumnoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioAlumno {

    private final AlumnoRepository alumnoRepository;

    public List<Alumno> listarTodos() {
        return alumnoRepository.findAll();
    }
}
