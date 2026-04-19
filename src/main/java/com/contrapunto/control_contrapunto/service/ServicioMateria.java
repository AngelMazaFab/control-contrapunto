package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.repository.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServicioMateria {

    private final MateriaRepository materiaRepository;
}
