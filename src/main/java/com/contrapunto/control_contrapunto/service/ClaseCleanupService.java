package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.repository.ClaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClaseCleanupService {

    private final ClaseRepository claseRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void limpiarReposicionesAntiguas() {
        System.out.println("[ClaseCleanupService] Ejecutando limpieza automática de reposiciones antiguas...");
        claseRepository.eliminarReposicionesAntiguas();
        System.out.println("[ClaseCleanupService] Limpieza finalizada.");
    }
}
