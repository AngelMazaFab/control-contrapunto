package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.model.Admin;
import com.contrapunto.control_contrapunto.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicioAdmin {

    private final AdminRepository adminRepository;

    /**
     * Valida las credenciales del administrador.
     * @param usuario    nombre de usuario ingresado
     * @param contrasena contraseña ingresada (texto plano)
     * @return el Admin si las credenciales son válidas, o vacío si no
     */
    public Optional<Admin> validarCredenciales(String usuario, String contrasena) {
        return adminRepository.findByUsuario(usuario)
                .filter(admin -> admin.getContrasena().equals(contrasena));
    }
}

