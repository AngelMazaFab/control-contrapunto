package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.repository.ClaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServicioClase {

    private final ClaseRepository claseRepository;
}
