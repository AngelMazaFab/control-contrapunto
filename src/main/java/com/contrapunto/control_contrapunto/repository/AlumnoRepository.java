package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Alumno.
 * Gestiona el acceso a datos para la tabla de alumnos en la base de datos PostgreSQL.
 */
@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
}
