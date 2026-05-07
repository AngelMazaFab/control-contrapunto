package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.repository.AulaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de gestionar la lógica de negocio para las Aulas.
 */
@Service
@RequiredArgsConstructor
public class ServicioAula {

    // Repositorio para el acceso a datos de Aulas
    private final AulaRepository aulaRepository;
}
