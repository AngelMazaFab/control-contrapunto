package com.contrapunto.control_contrapunto.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad que representa un correo electrónico asociado a un Profesor.
 */
@Data
@Entity
@Table(name = "correo_profesor")
public class CorreoProfesor {

    // Identificador único del registro de correo
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_correo_profesor")
    private Long idCorreoProfesor;

    // Dirección de correo electrónico
    @Column(name = "correo", nullable = false)
    private String correo;

    // Referencia al profesor propietario de este correo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_profesor", nullable = false)
    private Profesor profesor;
}
