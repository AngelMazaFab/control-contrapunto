package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.model.Profesor;
import com.contrapunto.control_contrapunto.repository.ProfesorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioProfesor {

    private final ProfesorRepository profesorRepository;

    public List<Profesor> listarTodos() {
        return profesorRepository.findAll();
    }
}
