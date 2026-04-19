package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServicioAdmin {

    private final AdminRepository adminRepository;
}
