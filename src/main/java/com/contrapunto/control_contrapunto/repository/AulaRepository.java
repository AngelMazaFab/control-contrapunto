package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Aula.
 * Permite realizar operaciones de persistencia sobre las aulas registradas.
 */
@Repository
public interface AulaRepository extends JpaRepository<Aula, Long> {
}
