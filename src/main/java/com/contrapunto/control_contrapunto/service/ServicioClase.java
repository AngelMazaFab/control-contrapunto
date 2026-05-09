package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.model.Alumno;
import com.contrapunto.control_contrapunto.model.Clase;
import com.contrapunto.control_contrapunto.repository.ClaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio núcleo de la aplicación que gestiona la agenda de clases.
 * Implementa las reglas de validación de negocio, principalmente la regla de "Cero Empalmes".
 */
@Service
@RequiredArgsConstructor
public class ServicioClase {

    private final ClaseRepository claseRepository;

    /**
     * Registra una nueva clase en el sistema tras validar disponibilidades.
     */
    public Clase agendarClase(Clase nuevaClase) {
        // Al ser una clase nueva, no hay ID que excluir de las validaciones de empalme
        return procesarAgendamiento(nuevaClase, -1L);
    }

    /**
     * Actualiza los datos de una clase existente, validando que el nuevo horario no genere conflictos.
     */
    @org.springframework.transaction.annotation.Transactional
    public Clase actualizarClase(Long idClase, Clase nuevosDatos) {
        // Bloque: Localización del registro existente
        Clase claseExistente = claseRepository.findById(idClase)
                .orElseThrow(() -> new IllegalArgumentException("La clase no existe."));

        // Bloque: Mapeo de campos modificables
        claseExistente.setSalon(nuevosDatos.getSalon());
        claseExistente.setProfesor(nuevosDatos.getProfesor());
        claseExistente.setMateria(nuevosDatos.getMateria());
        claseExistente.setAlumnos(nuevosDatos.getAlumnos());
        claseExistente.setTipoClase(nuevosDatos.getTipoClase());
        claseExistente.setEsReposicion(nuevosDatos.getEsReposicion());

        // Procesa el agendamiento excluyendo a la clase misma del conteo de empalmes
        return procesarAgendamiento(claseExistente, claseExistente.getIdClase());
    }

    /**
     * Orquestador de validaciones de horario (Regla Cero Empalmes).
     * 
     * @param clase      La clase a validar y guardar.
     * @param idExcluir  ID de la clase a ignorar en validaciones (usado en actualizaciones).
     */
    private Clase procesarAgendamiento(Clase clase, Long idExcluir) {
        // Bloque 1: Validación de consistencia temporal
        // Verifica que la clase no termine antes de empezar
        if (clase.getHoraInicio().isAfter(clase.getHoraFin())
                || clase.getHoraInicio().equals(clase.getHoraFin())) {
            throw new IllegalArgumentException("La hora de inicio debe ser anterior a la hora de fin.");
        }

        Long idDia = clase.getDiaSemana().getIdDiaSemana();

        // Bloque 2: Validación de Disponibilidad del Salón
        // Consulta si el salón ya está ocupado en ese día y rango horario
        long empalmesSalon = claseRepository.contarEmpalmesSalon(
                idDia, clase.getHoraInicio(), clase.getHoraFin(), clase.getSalon().getId(), idExcluir
        );
        if (empalmesSalon > 0) {
            throw new IllegalStateException("El salón '" + clase.getSalon().getNombre()
                    + "' ya tiene una clase en ese día y bloque de tiempo.");
        }

        // Bloque 3: Validación de Disponibilidad del Profesor
        // Consulta si el profesor ya tiene otra clase asignada en el mismo horario
        long empalmesProfesor = claseRepository.contarEmpalmesProfesor(
                idDia, clase.getHoraInicio(), clase.getHoraFin(), clase.getProfesor().getIdProfesor(), idExcluir
        );
        if (empalmesProfesor > 0) {
            throw new IllegalStateException("El profesor ya tiene una clase asignada en ese día y bloque de tiempo.");
        }

        // Bloque 4: Validación de Disponibilidad de Alumnos
        // Itera sobre la lista de alumnos para asegurar que ninguno tenga un cruce de horario
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

        // Bloque 5: Persistencia
        // Si todas las validaciones pasaron, se guarda el registro en la BD
        return claseRepository.save(clase);
    }

    /**
     * Lista todas las clases agendadas en el sistema.
     */
    public List<Clase> listarTodos() {
        return claseRepository.findAll();
    }

    /**
     * Elimina una clase de la agenda.
     */
    public void eliminar(Long id) {
        claseRepository.deleteById(id);
    }
}
