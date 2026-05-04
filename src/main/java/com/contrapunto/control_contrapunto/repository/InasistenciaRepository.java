package com.contrapunto.control_contrapunto.repository;

import com.contrapunto.control_contrapunto.model.Inasistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InasistenciaRepository extends JpaRepository<Inasistencia, Long> {

    // Inasistencias que aún no han sido cubiertas por ninguna clase de reposición
    @Query("SELECT i FROM Inasistencia i WHERE NOT EXISTS " +
           "(SELECT c FROM Clase c WHERE c.esReposicion = i.idInasistencia)")
    List<Inasistencia> findInasistenciasPendientes();
}
