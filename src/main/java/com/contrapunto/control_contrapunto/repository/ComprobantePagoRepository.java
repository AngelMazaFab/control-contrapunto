package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.ComprobantePago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ComprobantePagoRepository extends JpaRepository<ComprobantePago, Long> {

    /**
     * Obtiene la fecha del comprobante de pago más reciente de un alumno.
     * Se usa para calcular 'último pago' y 'próximo pago'.
     */
    @Query("SELECT MAX(c.fechaPago) FROM ComprobantePago c WHERE c.alumno.idAlumno = :idAlumno")
    Optional<LocalDate> findUltimoPagoByAlumnoId(@Param("idAlumno") Long idAlumno);

    List<ComprobantePago> findByAlumno_IdAlumno(Long idAlumno);
}
