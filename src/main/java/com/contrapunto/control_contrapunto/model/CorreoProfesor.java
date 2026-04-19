package com.contrapunto.control_contrapunto.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "correo_profesor")
public class CorreoProfesor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_correo_profesor")
    private Long idCorreoProfesor;

    @Column(name = "correo", nullable = false)
    private String correo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_profesor", nullable = false)
    private Profesor profesor;
}
