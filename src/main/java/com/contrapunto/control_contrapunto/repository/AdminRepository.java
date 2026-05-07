package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Admin.
 * Proporciona métodos para realizar operaciones CRUD sobre la tabla de administradores.
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    /**
     * Busca un administrador basado en su nombre de usuario.
     * 
     * @param usuario El nombre de usuario a buscar.
     * @return Un Optional que contiene al Admin si se encuentra, o vacío en caso contrario.
     */
    Optional<Admin> findByUsuario(String usuario);
}
