package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.repository.AlumnoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServicioAlumno {

    private final AlumnoRepository alumnoRepository;
}
