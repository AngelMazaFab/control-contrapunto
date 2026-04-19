package com.contrapunto.control_contrapunto.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "correo_alumno")
public class CorreoAlumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_correo_alumno")
    private Long idCorreoAlumno;

    @Column(name = "correo", nullable = false)
    private String correo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alumno", nullable = false)
    private Alumno alumno;
}
