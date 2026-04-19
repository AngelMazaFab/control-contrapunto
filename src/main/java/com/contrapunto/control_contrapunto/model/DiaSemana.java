package com.contrapunto.control_contrapunto.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "dia_semana")
public class DiaSemana {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dia_semana")
    private Long idDiaSemana;

    @Column(name = "nombre_dia", nullable = false)
    private String nombreDia;
}
