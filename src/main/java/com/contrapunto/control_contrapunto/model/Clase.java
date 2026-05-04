package com.contrapunto.control_contrapunto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "clase")
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clase")
    private Long idClase;

    @Column(name = "tipo_clase")
    private Boolean tipoClase;

    @Column(name = "fecha_exacta")
    private LocalDate fechaExacta;

    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @Column(name = "hora_fin")
    private LocalTime horaFin;

    @Column(name = "es_reposicion")
    private Long esReposicion;

    @ManyToOne
    @JoinColumn(name = "id_aula")
    private Salon salon;

    @ManyToOne
    @JoinColumn(name = "id_profesor")
    private Profesor profesor;

    @ManyToOne
    @JoinColumn(name = "id_materia")
    private Materia materia;

    @ManyToOne
    @JoinColumn(name = "id_dia_semana")
    private DiaSemana diaSemana;

    @ManyToMany
    @JoinTable(
        name = "clase_alumno",
        joinColumns = @JoinColumn(name = "id_clase"),
        inverseJoinColumns = @JoinColumn(name = "id_alumno")
    )
    private java.util.List<Alumno> alumnos;
}
