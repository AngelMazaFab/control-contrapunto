package com.contrapunto.control_contrapunto.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad que representa un número telefónico de contacto de un Profesor.
 */
@Data
@Entity
@Table(name = "telefono_profesor")
public class TelefonoProfesor {

    // Identificador del registro telefónico
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_telefono_profesor")
    private Long idTelefonoProfesor;

    // Número de teléfono
    @Column(name = "telefono", nullable = false)
    private String telefono;

    // Profesor al que pertenece este número
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_profesor", nullable = false)
    private Profesor profesor;
}
