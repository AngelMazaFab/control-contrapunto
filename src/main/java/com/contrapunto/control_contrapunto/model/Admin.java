package com.contrapunto.control_contrapunto.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad que representa a un Administrador del sistema.
 * Contiene las credenciales básicas para el inicio de sesión.
 */
@Data
@Entity
@Table(name = "admin")
public class Admin {

    // Identificador único del administrador
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_admin")
    private Long idAdmin;

    // Nombre de usuario único para el acceso
    @Column(name = "usuario", nullable = false, unique = true)
    private String usuario;

    // Contraseña almacenada (se recomienda cifrado en producción)
    @Column(name = "contrasena", nullable = false)
    private String contrasena;
}
