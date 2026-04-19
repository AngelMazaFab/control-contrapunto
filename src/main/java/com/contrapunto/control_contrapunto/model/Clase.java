package com.contrapunto.control_contrapunto.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@Table(name = "clase")
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clase")
    private Long idClase;

    @Column(name = "tipo_clase", nullable = false)
    private String tipoClase;

    @Column(name = "fecha_exacta")
    private LocalDate fechaExacta;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @Column(name = "es_reposicion")
    private Integer esReposicion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dia_semana")
    private DiaSemana diaSemana;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aula")
    private Aula aula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_profesor")
    private Profesor profesor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_materia")
    private Materia materia;

    @ManyToMany
    @JoinTable(
            name = "alumno_clase",
            joinColumns = @JoinColumn(name = "id_clase"),
            inverseJoinColumns = @JoinColumn(name = "id_alumno")
    )
    private List<Alumno> alumnos;
}
