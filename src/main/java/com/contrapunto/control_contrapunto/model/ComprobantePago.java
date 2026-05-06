package com.contrapunto.control_contrapunto.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "comprobante_pago")
public class ComprobantePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comprobante_pago")
    private Long idComprobantePago;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "importe", precision = 10, scale = 2, nullable = false)
    private BigDecimal importe;

    @Column(name = "archivo_url")
    private String archivoUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_alumno", nullable = false)
    private Alumno alumno;
}
