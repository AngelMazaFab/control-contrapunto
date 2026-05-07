package com.contrapunto.control_contrapunto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Entidad que representa un Salón o espacio físico de enseñanza.
 * Es el componente principal para gestionar la ocupación en la agenda de clases.
 */
@Data
@Entity
@Table(name = "salones")
public class Salon {

    // Identificador del salón
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre o identificador visual del salón (ej. Cubículo 1, Salón de Ensayos)
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

}
