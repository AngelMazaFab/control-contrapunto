package com.contrapunto.control_contrapunto.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * Entidad que registra la falta de un alumno a una clase específica.
 * Sirve como base para generar reposiciones de clase.
 */
@Data
@Entity
@Table(name = "inasistencia")
public class Inasistencia {

    // Identificador único de la inasistencia
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inasistencia")
    private Long idInasistencia;

    // Fecha exacta en la que ocurrió la falta
    @Column(name = "fecha_exacta", nullable = false)
    private LocalDate fechaExacta;

    // Referencia a la clase regular a la que no se asistió
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_clase", nullable = false)
    private Clase clase;

    // Referencia al alumno que faltó
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alumno", nullable = false)
    private Alumno alumno;
}
