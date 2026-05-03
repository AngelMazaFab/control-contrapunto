package com.contrapunto.control_contrapunto.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "telefono_profesor")
public class TelefonoProfesor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_telefono_profesor")
    private Long idTelefonoProfesor;

    @Column(name = "telefono_profesor", nullable = false)
    private String telefono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_profesor", nullable = false)
    private Profesor profesor;
}
