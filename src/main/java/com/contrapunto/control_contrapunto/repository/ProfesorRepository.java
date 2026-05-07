package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Profesor.
 * Gestiona el acceso a datos para la tabla de profesores.
 */
@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
}
