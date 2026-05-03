package com.contrapunto.control_contrapunto.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "profesor")
public class Profesor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profesor")
    private Long idProfesor;

    @Column(name = "nombre_profesor", nullable = false)
    private String nombreProfesor;

    @Column(name = "sueldo_base")
    private Double sueldoBase;

    @Column(name = "activo")
    private Boolean activo;

    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TelefonoProfesor> telefonos;

    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CorreoProfesor> correos;
}
