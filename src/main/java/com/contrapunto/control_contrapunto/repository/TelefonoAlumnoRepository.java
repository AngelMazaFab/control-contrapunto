package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.TelefonoAlumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelefonoAlumnoRepository extends JpaRepository<TelefonoAlumno, Long> {
}
