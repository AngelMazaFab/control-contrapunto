package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
}
