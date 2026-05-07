package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.model.Admin;
import com.contrapunto.control_contrapunto.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Servicio para gestionar la lógica de autenticación del administrador.
 */
@Service
@RequiredArgsConstructor
public class ServicioAdmin {

    private final AdminRepository adminRepository;

    /**
     * Valida las credenciales del administrador.
     * Busca al usuario por su nombre y comprueba si la contraseña coincide.
     * 
     * @param usuario    nombre de usuario ingresado
     * @param contrasena contraseña ingresada (texto plano)
     * @return el Admin encapsulado en Optional si las credenciales son válidas, o vacío si no
     */
    public Optional<Admin> validarCredenciales(String usuario, String contrasena) {
        // Bloque: Búsqueda y Validación funcional
        // Se obtiene el usuario de la BD (si existe) y se filtra comprobando la contraseña.
        return adminRepository.findByUsuario(usuario)
                .filter(admin -> admin.getContrasena().equals(contrasena));
    }
}
