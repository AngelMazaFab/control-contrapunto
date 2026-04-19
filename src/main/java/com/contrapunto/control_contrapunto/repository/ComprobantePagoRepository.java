package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.ComprobantePago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComprobantePagoRepository extends JpaRepository<ComprobantePago, Long> {
}
