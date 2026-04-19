package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaSemanaRepository extends JpaRepository<DiaSemana, Long> {
}
