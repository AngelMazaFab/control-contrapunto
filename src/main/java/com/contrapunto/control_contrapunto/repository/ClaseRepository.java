package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {

    @Query("SELECT COUNT(c) FROM Clase c WHERE c.fechaExacta = :fecha AND c.horaInicio < :horaFin AND c.horaFin > :horaInicio AND (c.aula.idAula = :idAula OR c.profesor.idProfesor = :idProfesor)")
    long contarEmpalmesProfesorAula(@Param("fecha") java.time.LocalDate fecha,
                                    @Param("horaInicio") java.time.LocalTime horaInicio,
                                    @Param("horaFin") java.time.LocalTime horaFin,
                                    @Param("idAula") Long idAula,
                                    @Param("idProfesor") Long idProfesor);
}
