package com.contrapunto.control_contrapunto.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

/**
 * Entidad que representa a un Profesor de la academia.
 * Contiene información laboral y sus medios de contacto.
 */
@Data
@Entity
@Table(name = "profesor")
public class Profesor {

    // Identificador único del profesor
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profesor")
    private Long idProfesor;

    // Nombre completo del profesor
    @Column(name = "nombre_profesor", nullable = false)
    private String nombreProfesor;

    // Pago base por hora o sesión (dependiendo de la lógica de nómina)
    @Column(name = "sueldo_base")
    private Double sueldoBase;

    // Lista de números telefónicos asociados (Relación Bidireccional)
    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TelefonoProfesor> telefonos;

    // Lista de correos electrónicos asociados (Relación Bidireccional)
    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CorreoProfesor> correos;
}
