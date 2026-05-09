package com.contrapunto.control_contrapunto.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad de catálogo para las materias o instrumentos musicales enseñados.
 */
@Data
@Entity
@Table(name = "materia")
public class Materia {

    // Identificador único de la materia
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_materia")
    private Long idMateria;

    // Nombre de la asignatura (ej. Piano, Guitarra, Solfeo)
    @Column(name = "nombre_materia", nullable = false)
    private String nombreMateria;
}
