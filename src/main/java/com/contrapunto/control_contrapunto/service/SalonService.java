package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.model.Salon;
import com.contrapunto.control_contrapunto.repository.SalonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalonService {

    private final SalonRepository salonRepository;
    private final org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @jakarta.annotation.PostConstruct
    public void corregirEsquemaBaseDatos() {
        try {
            // Elimina la columna capacidad de la BD para que no salte el error de NOT NULL
            jdbcTemplate.execute("ALTER TABLE salones DROP COLUMN IF EXISTS capacidad;");
            System.out.println("[SalonService] Columna 'capacidad' eliminada de la base de datos.");
        } catch (Exception e) {
            System.err.println("[SalonService] Nota: No se pudo modificar el esquema (es normal si no existe la tabla aún): " + e.getMessage());
        }
    }

    public List<Salon> listarTodos() {
        return salonRepository.findAll();
    }

    public void guardar(Salon salon) {
        salonRepository.save(salon);
    }

    public Optional<Salon> buscarPorId(Long id) {
        return salonRepository.findById(id);
    }

    public void eliminar(Long id) {
        salonRepository.deleteById(id);
    }
}
