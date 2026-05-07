package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.model.Salon;
import com.contrapunto.control_contrapunto.repository.SalonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de la lógica de negocio para los Salones (aulas).
 */
@Service
@RequiredArgsConstructor
public class SalonService {

    private final SalonRepository salonRepository;
    private final org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    /**
     * Bloque: Corrección en tiempo de arranque.
     * Este método se ejecuta automáticamente justo después de que Spring inicialice el servicio (@PostConstruct).
     * Su objetivo es eliminar una columna ("capacidad") que causaba errores en versiones anteriores
     * del esquema de base de datos debido a restricciones NOT NULL indeseadas.
     */
    @jakarta.annotation.PostConstruct
    public void corregirEsquemaBaseDatos() {
        try {
            // Elimina la columna capacidad de la BD usando SQL directo
            jdbcTemplate.execute("ALTER TABLE salones DROP COLUMN IF EXISTS capacidad;");
            System.out.println("[SalonService] Columna 'capacidad' eliminada de la base de datos.");
        } catch (Exception e) {
            System.err.println("[SalonService] Nota: No se pudo modificar el esquema (es normal si no existe la tabla aún): " + e.getMessage());
        }
    }

    /**
     * Obtiene el listado de todos los salones registrados.
     */
    public List<Salon> listarTodos() {
        return salonRepository.findAll();
    }

    /**
     * Persiste un salón en la base de datos.
     */
    public void guardar(Salon salon) {
        salonRepository.save(salon);
    }

    /**
     * Busca un salón por su identificador.
     */
    public Optional<Salon> buscarPorId(Long id) {
        return salonRepository.findById(id);
    }

    /**
     * Elimina físicamente un salón de la base de datos.
     */
    public void eliminar(Long id) {
        salonRepository.deleteById(id);
    }
}
