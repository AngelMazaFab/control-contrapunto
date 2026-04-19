package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
}
