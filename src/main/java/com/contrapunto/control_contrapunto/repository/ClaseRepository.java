package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {
}
