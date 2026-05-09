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

/**
 * Entidad central del sistema que representa una sesión de clase.
 * Almacena el horario, el salón, el profesor, la materia y los alumnos inscritos.
 */
@Data
@Entity
@Table(name = "clase")
public class Clase {

    // Identificador único de la clase
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clase")
    private Long idClase;

    // Flag para distinguir entre clase regular (false) y reposición (true)
    @Column(name = "tipo_clase")
    private Boolean tipoClase;

    // Fecha específica (solo aplica si es una reposición puntual)
    @Column(name = "fecha_exacta")
    private LocalDate fechaExacta;

    // Hora en la que inicia la sesión
    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    // Hora en la que finaliza la sesión
    @Column(name = "hora_fin")
    private LocalTime horaFin;

    // Referencia opcional al ID de inasistencia que está cubriendo esta reposición
    @Column(name = "es_reposicion")
    private Long esReposicion;

    // Relación con el salón físico asignado
    @ManyToOne
    @JoinColumn(name = "id_aula")
    private Salon salon;

    // Relación con el profesor que imparte la clase
    @ManyToOne
    @JoinColumn(name = "id_profesor")
    private Profesor profesor;

    // Relación con la materia o instrumento que se enseña
    @ManyToOne
    @JoinColumn(name = "id_materia")
    private Materia materia;

    // Día de la semana (Lunes-Domingo) para clases recurrentes
    @ManyToOne
    @JoinColumn(name = "id_dia_semana")
    private DiaSemana diaSemana;

    // Relación muchos a muchos con los alumnos inscritos en esta sesión
    @ManyToMany
    @JoinTable(
        name = "clase_alumno",
        joinColumns = @JoinColumn(name = "id_clase"),
        inverseJoinColumns = @JoinColumn(name = "id_alumno")
    )
    private java.util.List<Alumno> alumnos;
}
