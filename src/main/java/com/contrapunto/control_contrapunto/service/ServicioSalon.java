package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.model.Salon;
import com.contrapunto.control_contrapunto.repository.SalonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de la gestión de Salones (aulas).
 */
@Service
@RequiredArgsConstructor
public class ServicioSalon {

    private final SalonRepository salonRepository;

    /** Lista todos los salones registrados. */
    public List<Salon> listarTodos() {
        return salonRepository.findAll();
    }

    /** Guarda un salón. */
    public void guardar(Salon salon) {
        salonRepository.save(salon);
    }

    /** Busca un salón por ID. */
    public Optional<Salon> buscarPorId(Long id) {
        return salonRepository.findById(id);
    }

    /** Elimina un salón. */
    public void eliminar(Long id) {
        salonRepository.deleteById(id);
    }
}
