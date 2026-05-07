package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.repository.ClaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de las tareas programadas (CRON) y de mantenimiento del sistema.
 */
@Service
@RequiredArgsConstructor
public class ClaseCleanupService {

    private final ClaseRepository claseRepository;

    /**
     * Tarea programada que se ejecuta todos los días a la medianoche.
     * Su objetivo es eliminar lógicamente las clases de tipo "reposición" que ya ocurrieron.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void limpiarReposicionesAntiguas() {
        // Bloque: Limpieza de base de datos
        System.out.println("[ClaseCleanupService] Ejecutando limpieza automática de reposiciones antiguas...");
        // Llama al repositorio para ejecutar la consulta de eliminación
        claseRepository.eliminarReposicionesAntiguas();
        System.out.println("[ClaseCleanupService] Limpieza finalizada.");
    }
}
