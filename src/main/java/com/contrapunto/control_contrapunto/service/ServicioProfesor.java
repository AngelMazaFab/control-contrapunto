package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.repository.ProfesorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServicioProfesor {

    private final ProfesorRepository profesorRepository;
}
