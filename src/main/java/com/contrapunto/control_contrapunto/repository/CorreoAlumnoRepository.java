package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.CorreoAlumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorreoAlumnoRepository extends JpaRepository<CorreoAlumno, Long> {
}
