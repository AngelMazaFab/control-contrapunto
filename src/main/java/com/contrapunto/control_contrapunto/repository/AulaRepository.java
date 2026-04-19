package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AulaRepository extends JpaRepository<Aula, Long> {
}
