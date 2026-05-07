package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.repository.ComprobantePagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de gestionar la lógica de negocio de los Comprobantes de Pago.
 */
@Service
@RequiredArgsConstructor
public class ServicioComprobante {

    // Repositorio para el acceso a datos de Comprobantes
    private final ComprobantePagoRepository comprobantePagoRepository;
}
