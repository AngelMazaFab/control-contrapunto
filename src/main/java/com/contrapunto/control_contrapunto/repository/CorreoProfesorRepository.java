package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.CorreoProfesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorreoProfesorRepository extends JpaRepository<CorreoProfesor, Long> {
}
