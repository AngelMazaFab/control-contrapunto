package com.contrapunto.control_contrapunto.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad de catálogo para los días de la semana (Lunes, Martes, etc.).
 * Se utiliza para definir la recurrencia de las clases.
 */
@Data
@Entity
@Table(name = "dia_semana")
public class DiaSemana {

    // Identificador del día (1-7 generalmente)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dia_semana")
    private Long idDiaSemana;

    // Nombre textual del día
    @Column(name = "nombre_dia", nullable = false)
    private String nombreDia;
}
