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
        return procesarAgendamiento(nuevaClase, -1L);
    }

    @org.springframework.transaction.annotation.Transactional
    public Clase actualizarClase(Long idClase, Clase nuevosDatos) {
        Clase claseExistente = claseRepository.findById(idClase)
                .orElseThrow(() -> new IllegalArgumentException("La clase no existe."));

        // Actualizar datos
        claseExistente.setSalon(nuevosDatos.getSalon());
        claseExistente.setProfesor(nuevosDatos.getProfesor());
        claseExistente.setMateria(nuevosDatos.getMateria());
        claseExistente.setAlumnos(nuevosDatos.getAlumnos());
        claseExistente.setTipoClase(nuevosDatos.getTipoClase());
        claseExistente.setEsReposicion(nuevosDatos.getEsReposicion());
        // NO actualizamos idClase, fechaExacta, horaInicio, horaFin, diaSemana 
        // porque el edit modal los trae como informativos o los mantiene iguales.
        // Si horaInicio/horaFin se pueden editar, deberíamos actualizarlos aquí.
        // Pero en la UI están deshabilitados.

        return procesarAgendamiento(claseExistente, claseExistente.getIdClase());
    }

    private Clase procesarAgendamiento(Clase clase, Long idExcluir) {
        // 1. Validar que hora de inicio sea anterior a hora de fin
        if (clase.getHoraInicio().isAfter(clase.getHoraFin())
                || clase.getHoraInicio().equals(clase.getHoraFin())) {
            throw new IllegalArgumentException("La hora de inicio debe ser anterior a la hora de fin.");
        }

        Long idDia = clase.getDiaSemana().getIdDiaSemana();

        // 2. Validar que el SALÓN esté libre ese día/hora.
        long empalmesSalon = claseRepository.contarEmpalmesSalon(
                idDia, clase.getHoraInicio(), clase.getHoraFin(), clase.getSalon().getId(), idExcluir
        );
        if (empalmesSalon > 0) {
            throw new IllegalStateException("El salón '" + clase.getSalon().getNombre()
                    + "' ya tiene una clase en ese día y bloque de tiempo.");
        }

        // 3. Validar que el PROFESOR esté libre ese día/hora.
        long empalmesProfesor = claseRepository.contarEmpalmesProfesor(
                idDia, clase.getHoraInicio(), clase.getHoraFin(), clase.getProfesor().getIdProfesor(), idExcluir
        );
        if (empalmesProfesor > 0) {
            throw new IllegalStateException("El profesor ya tiene una clase asignada en ese día y bloque de tiempo.");
        }

        // 4. Validar que ningún ALUMNO ya esté en otra clase ese día/hora.
        if (clase.getAlumnos() != null) {
            for (Alumno alumno : clase.getAlumnos()) {
                long empalmesAlumno = claseRepository.contarEmpalmesAlumno(
                        idDia, clase.getHoraInicio(), clase.getHoraFin(), alumno.getIdAlumno(), idExcluir
                );
                if (empalmesAlumno > 0) {
                    throw new IllegalStateException("El alumno '" + alumno.getNombreAlumno()
                            + "' ya está inscrito en otra clase en ese día y bloque de tiempo.");
                }
            }
        }

        // 5. Guardar si pasa todas las validaciones
        return claseRepository.save(clase);
    }


    public List<Clase> listarTodos() {
        return claseRepository.findAll();
    }

    public void eliminar(Long id) {
        claseRepository.deleteById(id);
    }
}
