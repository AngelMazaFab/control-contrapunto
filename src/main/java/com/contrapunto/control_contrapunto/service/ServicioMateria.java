package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.repository.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de gestionar la lógica de negocio de las Materias.
 */
@Service
@RequiredArgsConstructor
public class ServicioMateria {

    // Repositorio para el acceso a datos de Materias
    private final MateriaRepository materiaRepository;
}
