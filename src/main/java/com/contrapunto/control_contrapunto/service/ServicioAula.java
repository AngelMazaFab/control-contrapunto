package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.repository.AulaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServicioAula {

    private final AulaRepository aulaRepository;
}
