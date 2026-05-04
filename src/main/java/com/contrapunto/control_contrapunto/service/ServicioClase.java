package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.model.Clase;
import com.contrapunto.control_contrapunto.repository.ClaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServicioClase {

    private final ClaseRepository claseRepository;

    public Clase agendarClase(Clase nuevaClase) {
        // 1. Validar que la hora de inicio sea menor a la hora de fin
        if (nuevaClase.getHoraInicio().isAfter(nuevaClase.getHoraFin()) || nuevaClase.getHoraInicio().equals(nuevaClase.getHoraFin())) {
            throw new IllegalArgumentException("La hora de inicio debe ser anterior a la hora de fin.");
        }

        // 2. Ejecutar validación matemática de cruce de horarios en BD
        long empalmes = claseRepository.contarEmpalmesProfesorAula(
                nuevaClase.getFechaExacta(),
                nuevaClase.getHoraInicio(),
                nuevaClase.getHoraFin(),
                nuevaClase.getSalon().getId(),
                nuevaClase.getProfesor().getIdProfesor()
        );

        if (empalmes > 0) {
            throw new IllegalStateException("Transacción denegada: El Aula o el Profesor ya están ocupados en ese bloque de tiempo.");
        }

        // 3. Si pasa las validaciones, guardar en la base de datos
        return claseRepository.save(nuevaClase);
    }
}
