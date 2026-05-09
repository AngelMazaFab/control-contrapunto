package com.contrapunto.control_contrapunto.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Entidad que representa a un Alumno de la institución.
 * Almacena datos personales, registros de pago y relaciones de contacto.
 */
@Data
@Entity
@Table(name = "alumno")
public class Alumno {

    // Identificador único del alumno
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alumno")
    private Long idAlumno;

    // Nombre completo del estudiante
    @Column(name = "nombre_alumno", nullable = false)
    private String nombreAlumno;

    // Fecha en la que se realizó el último pago (calculada dinámicamente)
    @Column(name = "ultimo_pago")
    private LocalDate ultimoPago;

    // Fecha proyectada para el siguiente pago (calculada dinámicamente)
    @Column(name = "proximo_pago")
    private LocalDate proximoPago;

    // Relación uno a muchos con sus números telefónicos
    @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TelefonoAlumno> telefonos;

    // Relación uno a muchos con sus correos electrónicos
    @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CorreoAlumno> correos;
}
