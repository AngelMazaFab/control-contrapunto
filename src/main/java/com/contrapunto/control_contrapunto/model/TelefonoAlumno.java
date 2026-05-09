package com.contrapunto.control_contrapunto.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad que representa un número telefónico de contacto de un Alumno.
 */
@Data
@Entity
@Table(name = "telefono_alumno")
public class TelefonoAlumno {

    // Identificador del registro telefónico
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_telefono_alumno")
    private Long idTelefonoAlumno;

    // Número de teléfono (almacenado como String para preservar formato)
    @Column(name = "telefono", nullable = false)
    private String telefono;

    // Alumno al que pertenece este número
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alumno", nullable = false)
    private Alumno alumno;
}
