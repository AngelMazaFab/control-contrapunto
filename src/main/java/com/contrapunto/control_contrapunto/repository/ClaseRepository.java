package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {

    /**
     * Cuenta si un SALÓN ya tiene una clase que se empalma con el bloque de tiempo dado,
     * en el mismo día de la semana (recurrente). Un salón no puede tener dos clases al mismo tiempo.
     */
    @Query("SELECT COUNT(c) FROM Clase c " +
           "WHERE c.diaSemana.idDiaSemana = :idDia " +
           "AND c.horaInicio < :horaFin AND c.horaFin > :horaInicio " +
           "AND c.salon.id = :idSalon " +
           "AND (c.idClase != :idExcluir OR :idExcluir = -1L)")
    long contarEmpalmesSalon(@Param("idDia") Long idDia,
                             @Param("horaInicio") LocalTime horaInicio,
                             @Param("horaFin") LocalTime horaFin,
                             @Param("idSalon") Long idSalon,
                             @Param("idExcluir") Long idExcluir);

    /**
     * Cuenta si un PROFESOR ya tiene una clase que se empalma con el bloque de tiempo dado,
     * en el mismo día de la semana (recurrente). Un profesor no puede dar dos clases al mismo tiempo.
     */
    @Query("SELECT COUNT(c) FROM Clase c " +
           "WHERE c.diaSemana.idDiaSemana = :idDia " +
           "AND c.horaInicio < :horaFin AND c.horaFin > :horaInicio " +
           "AND c.profesor.idProfesor = :idProfesor " +
           "AND (c.idClase != :idExcluir OR :idExcluir = -1L)")
    long contarEmpalmesProfesor(@Param("idDia") Long idDia,
                                @Param("horaInicio") LocalTime horaInicio,
                                @Param("horaFin") LocalTime horaFin,
                                @Param("idProfesor") Long idProfesor,
                                @Param("idExcluir") Long idExcluir);

    /**
     * Cuenta si un ALUMNO ya está inscrito en alguna clase que se empalma con el bloque dado,
     * en el mismo día de la semana (recurrente). Un alumno no puede estar en dos clases al mismo tiempo.
     */
    @Query("SELECT COUNT(c) FROM Clase c JOIN c.alumnos a " +
           "WHERE c.diaSemana.idDiaSemana = :idDia " +
           "AND c.horaInicio < :horaFin AND c.horaFin > :horaInicio " +
           "AND a.idAlumno = :idAlumno " +
           "AND (c.idClase != :idExcluir OR :idExcluir = -1L)")
    long contarEmpalmesAlumno(@Param("idDia") Long idDia,
                              @Param("horaInicio") LocalTime horaInicio,
                              @Param("horaFin") LocalTime horaFin,
                              @Param("idAlumno") Long idAlumno,
                              @Param("idExcluir") Long idExcluir);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.transaction.annotation.Transactional
    @Query("DELETE FROM Clase c WHERE c.esReposicion = -1 AND c.fechaExacta < CURRENT_DATE")
    void eliminarReposicionesAntiguas();

    /**
     * Cuenta el total de clases (no temporales) asignadas a un profesor.
     * Se usa para calcular el sueldo semanal.
     */
    @Query("SELECT COUNT(c) FROM Clase c WHERE c.profesor.idProfesor = :idProfesor AND (c.esReposicion IS NULL OR c.esReposicion <> -1)")
    long contarClasesPorProfesor(@Param("idProfesor") Long idProfesor);
}
