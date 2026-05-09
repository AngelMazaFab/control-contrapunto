package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.ComprobantePago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad ComprobantePago.
 * Centraliza las consultas relacionadas con los pagos realizados por los alumnos.
 */
@Repository
public interface ComprobantePagoRepository extends JpaRepository<ComprobantePago, Long> {

    /**
     * Obtiene la fecha del comprobante de pago más reciente para un alumno específico.
     * Esta información es vital para el cálculo de estados de vigencia.
     */
    @Query("SELECT MAX(c.fechaPago) FROM ComprobantePago c WHERE c.alumno.idAlumno = :idAlumno")
    Optional<LocalDate> findUltimoPagoByAlumnoId(@Param("idAlumno") Long idAlumno);

    /**
     * Recupera el historial completo de comprobantes de un alumno.
     */
    List<ComprobantePago> findByAlumno_IdAlumno(Long idAlumno);
}
