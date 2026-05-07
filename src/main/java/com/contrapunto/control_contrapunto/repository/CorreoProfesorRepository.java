package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.CorreoProfesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad CorreoProfesor.
 * Gestiona la persistencia de las direcciones de correo electrónico de los profesores.
 */
@Repository
public interface CorreoProfesorRepository extends JpaRepository<CorreoProfesor, Long> {
}
