package com.contrapunto.control_contrapunto.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad que representa un Aula física en la escuela.
 * Nota: Actualmente se utiliza la entidad 'Salon' como principal para la agenda.
 */
@Data
@Entity
@Table(name = "aula")
public class Aula {

    // Identificador del aula
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aula")
    private Long idAula;

    // Nombre descriptivo del aula
    @Column(name = "nombre_aula", nullable = false)
    private String nombreAula;
}
