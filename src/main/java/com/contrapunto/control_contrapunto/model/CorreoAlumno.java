package com.contrapunto.control_contrapunto.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad que representa un correo electrónico asociado a un Alumno.
 */
@Data
@Entity
@Table(name = "correo_alumno")
public class CorreoAlumno {

    // Identificador único del registro de correo
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_correo_alumno")
    private Long idCorreoAlumno;

    // Dirección de correo electrónico
    @Column(name = "correo", nullable = false)
    private String correo;

    // Referencia al alumno propietario de este correo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alumno", nullable = false)
    private Alumno alumno;
}
