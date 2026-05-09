package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.repository.InasistenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de gestionar la lógica de negocio de las Inasistencias.
 */
@Service
@RequiredArgsConstructor
public class ServicioInasistencia {

    // Repositorio para el acceso a datos de Inasistencias
    private final InasistenciaRepository inasistenciaRepository;
}
