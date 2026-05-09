package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.model.Materia;
import com.contrapunto.control_contrapunto.repository.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de gestionar la lógica de negocio de las Materias.
 */
@Service
@RequiredArgsConstructor
public class ServicioMateria {

    // Repositorio para el acceso a datos de Materias
    private final MateriaRepository materiaRepository;

    /**
     * Retorna el listado completo de materias registradas.
     */
    public java.util.List<Materia> listarTodas() {
        return materiaRepository.findAll();
    }

    /**
     * Guarda o actualiza una materia en la base de datos.
     */
    public void guardar(Materia materia) {
        materiaRepository.save(materia);
    }

    /**
     * Elimina una materia por su identificador único.
     */
    public void eliminar(Long id) {
        materiaRepository.deleteById(id);
    }
}
