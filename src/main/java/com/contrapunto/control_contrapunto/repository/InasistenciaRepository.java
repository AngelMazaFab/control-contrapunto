package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.Inasistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InasistenciaRepository extends JpaRepository<Inasistencia, Long> {
}
