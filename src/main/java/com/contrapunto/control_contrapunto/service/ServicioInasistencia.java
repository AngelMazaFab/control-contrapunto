package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.repository.InasistenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServicioInasistencia {

    private final InasistenciaRepository inasistenciaRepository;
}
