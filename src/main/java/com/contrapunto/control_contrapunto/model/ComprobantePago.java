package com.contrapunto.control_contrapunto.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad que registra una transacción de pago realizada por un alumno.
 */
@Data
@Entity
@Table(name = "comprobante_pago")
public class ComprobantePago {

    // Identificador único del comprobante
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comprobante_pago")
    private Long idComprobantePago;

    // Fecha efectiva en la que se realizó el pago
    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;

    // Concepto del pago (ej. Mensualidad Mayo)
    @Column(name = "descripcion")
    private String descripcion;

    // Cantidad económica pagada
    @Column(name = "importe", precision = 10, scale = 2, nullable = false)
    private BigDecimal importe;

    // Ruta o URL para acceder al archivo PDF generado
    @Column(name = "archivo_url")
    private String archivoUrl;

    // Relación con el alumno que efectuó el pago
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_alumno", nullable = false)
    private Alumno alumno;
}
