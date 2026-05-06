package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.model.Alumno;
import com.contrapunto.control_contrapunto.model.Clase;
import com.contrapunto.control_contrapunto.repository.ClaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioClase {

    private final ClaseRepository claseRepository;

    public Clase agendarClase(Clase nuevaClase) {
        // 1. Validar que hora de inicio sea anterior a hora de fin
        if (nuevaClase.getHoraInicio().isAfter(nuevaClase.getHoraFin())
                || nuevaClase.getHoraInicio().equals(nuevaClase.getHoraFin())) {
            throw new IllegalArgumentException("La hora de inicio debe ser anterior a la hora de fin.");
        }

        Long idDia = nuevaClase.getDiaSemana().getIdDiaSemana();

        // 2. Validar que el SALÓN esté libre ese día/hora (semana recurrente).
        long empalmesSalon = claseRepository.contarEmpalmesSalon(
                idDia,
                nuevaClase.getHoraInicio(),
                nuevaClase.getHoraFin(),
                nuevaClase.getSalon().getId()
        );
        if (empalmesSalon > 0) {
            throw new IllegalStateException("El salón '" + nuevaClase.getSalon().getNombre()
                    + "' ya tiene una clase en ese día y bloque de tiempo.");
        }

        // 3. Validar que el PROFESOR esté libre ese día/hora.
        long empalmesProfesor = claseRepository.contarEmpalmesProfesor(
                idDia,
                nuevaClase.getHoraInicio(),
                nuevaClase.getHoraFin(),
                nuevaClase.getProfesor().getIdProfesor()
        );
        if (empalmesProfesor > 0) {
            throw new IllegalStateException("El profesor ya tiene una clase asignada en ese día y bloque de tiempo.");
        }

        // 4. Validar que ningún ALUMNO ya esté en otra clase ese día/hora.
        if (nuevaClase.getAlumnos() != null) {
            for (Alumno alumno : nuevaClase.getAlumnos()) {
                long empalmesAlumno = claseRepository.contarEmpalmesAlumno(
                        idDia,
                        nuevaClase.getHoraInicio(),
                        nuevaClase.getHoraFin(),
                        alumno.getIdAlumno()
                );
                if (empalmesAlumno > 0) {
                    throw new IllegalStateException("El alumno '" + alumno.getNombreAlumno()
                            + "' ya está inscrito en otra clase en ese día y bloque de tiempo.");
                }
            }
        }

        // 5. Guardar si pasa todas las validaciones
        return claseRepository.save(nuevaClase);
    }

    public List<Clase> listarTodos() {
        return claseRepository.findAll();
    }

    public void eliminar(Long id) {
        claseRepository.deleteById(id);
    }
}
