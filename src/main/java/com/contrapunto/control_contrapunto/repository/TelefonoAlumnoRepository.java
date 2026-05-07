package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.TelefonoAlumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad TelefonoAlumno.
 * Almacena y recupera los números telefónicos asociados a los alumnos.
 */
@Repository
public interface TelefonoAlumnoRepository extends JpaRepository<TelefonoAlumno, Long> {
}
