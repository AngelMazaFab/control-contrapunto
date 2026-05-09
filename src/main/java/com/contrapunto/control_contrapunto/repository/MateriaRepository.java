package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Materia.
 * Gestiona el catálogo de asignaturas e instrumentos impartidos en la escuela.
 */
@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {
}
