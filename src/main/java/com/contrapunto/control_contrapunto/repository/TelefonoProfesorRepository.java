package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.TelefonoProfesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad TelefonoProfesor.
 * Almacena y recupera los números telefónicos asociados a los profesores.
 */
@Repository
public interface TelefonoProfesorRepository extends JpaRepository<TelefonoProfesor, Long> {
}
