package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.Inasistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Inasistencia.
 * Administra el registro de faltas que requieren una futura reposición de clase.
 */
@Repository
public interface InasistenciaRepository extends JpaRepository<Inasistencia, Long> {

    /**
     * Consulta las inasistencias que aún no han sido cubiertas por ninguna clase de reposición.
     * Se verifica que no exista una clase cuyo campo 'esReposicion' coincida con el ID de la inasistencia.
     */
    @Query("SELECT i FROM Inasistencia i WHERE NOT EXISTS " +
           "(SELECT c FROM Clase c WHERE c.esReposicion = i.idInasistencia)")
    List<Inasistencia> findInasistenciasPendientes();
}
