package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.Salon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Salon.
 * Gestiona la persistencia de las aulas físicas disponibles en la institución.
 */
@Repository
public interface SalonRepository extends JpaRepository<Salon, Long> {
}
