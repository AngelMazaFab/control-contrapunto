package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.repository.ClaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Servicio de mantenimiento para limpieza de datos.
 */
@Service
@RequiredArgsConstructor
public class ServicioClaseCleanup {

    private final ClaseRepository claseRepository;

    /** Limpieza automática diaria de reposiciones antiguas. */
    @Scheduled(cron = "0 0 0 * * ?")
    public void limpiarReposicionesAntiguas() {
        System.out.println("[ServicioClaseCleanup] Ejecutando limpieza...");
        claseRepository.eliminarReposicionesAntiguas();
        System.out.println("[ServicioClaseCleanup] Limpieza finalizada.");
    }
}
