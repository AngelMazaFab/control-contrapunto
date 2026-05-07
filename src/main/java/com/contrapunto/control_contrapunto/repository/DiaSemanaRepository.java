package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad DiaSemana.
 * Accede al catálogo de días (Lunes-Domingo) definido en la base de datos.
 */
@Repository
public interface DiaSemanaRepository extends JpaRepository<DiaSemana, Long> {
}
