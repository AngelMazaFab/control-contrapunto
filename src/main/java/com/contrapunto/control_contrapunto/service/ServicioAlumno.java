package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.model.Alumno;
import com.contrapunto.control_contrapunto.model.ComprobantePago;
import com.contrapunto.control_contrapunto.model.CorreoAlumno;
import com.contrapunto.control_contrapunto.model.TelefonoAlumno;
import com.contrapunto.control_contrapunto.repository.AlumnoRepository;
import com.contrapunto.control_contrapunto.repository.ComprobantePagoRepository;
import com.contrapunto.control_contrapunto.repository.TelefonoAlumnoRepository;
import com.contrapunto.control_contrapunto.repository.CorreoAlumnoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de gestionar la lógica de negocio de los Alumnos.
 * Incluye el manejo de sus datos personales, contactos (teléfonos/correos) 
 * y el cálculo dinámico de sus fechas de pago.
 */
@Service
@RequiredArgsConstructor
public class ServicioAlumno {

    private final AlumnoRepository alumnoRepository;
    private final ComprobantePagoRepository comprobantePagoRepository;
    private final TelefonoAlumnoRepository telefonoAlumnoRepository;
    private final CorreoAlumnoRepository correoAlumnoRepository;

    /**
     * Lista todos los alumnos registrados y calcula dinámicamente sus fechas de pago.
     * 'ultimoPago' es la fecha más reciente de sus comprobantes.
     * 'proximoPago' se proyecta a un mes después del último pago.
     */
    public List<Alumno> listarTodos() {
        // Bloque: Recuperación de datos base
        List<Alumno> alumnos = alumnoRepository.findAll();

        // Bloque: Cálculo dinámico de estados de pago
        for (Alumno alumno : alumnos) {
            // Busca la fecha del último comprobante registrado para este alumno
            Optional<LocalDate> ultimoPago =
                    comprobantePagoRepository.findUltimoPagoByAlumnoId(alumno.getIdAlumno());

            if (ultimoPago.isPresent()) {
                // Si ha pagado, se asignan las fechas correspondientes
                alumno.setUltimoPago(ultimoPago.get());
                alumno.setProximoPago(ultimoPago.get().plusMonths(1));
            } else {
                // Si no tiene pagos registrados, los campos quedan nulos
                alumno.setUltimoPago(null);
                alumno.setProximoPago(null);
            }
        }

        return alumnos;
    }

    /**
     * Registra un nuevo alumno en el sistema junto con su información de contacto.
     */
    @Transactional
    public void guardarAlumnoConContactos(String nombre, List<String> telefonos, List<String> correos) {
        // Bloque: Creación de la entidad base
        Alumno alumno = new Alumno();
        alumno.setNombreAlumno(nombre);
        alumno.setUltimoPago(null);
        alumno.setProximoPago(null);
        
        // Se guarda primero para generar el ID necesario para las relaciones
        alumno = alumnoRepository.save(alumno);

        // Bloque: Registro de Teléfonos
        if (telefonos != null) {
            for (String tel : telefonos) {
                if (tel != null && !tel.trim().isEmpty()) {
                    TelefonoAlumno ta = new TelefonoAlumno();
                    ta.setTelefono(tel.trim());
                    ta.setAlumno(alumno);
                    telefonoAlumnoRepository.save(ta);
                }
            }
        }

        // Bloque: Registro de Correos
        if (correos != null) {
            for (String email : correos) {
                if (email != null && !email.trim().isEmpty()) {
                    CorreoAlumno ca = new CorreoAlumno();
                    ca.setCorreo(email.trim());
                    ca.setAlumno(alumno);
                    correoAlumnoRepository.save(ca);
                }
            }
        }
    }

    /**
     * Busca un alumno por su identificador único.
     */
    public Alumno obtenerPorId(Long id) {
        return alumnoRepository.findById(id).orElse(null);
    }

    /**
     * Actualiza los datos de un alumno y reemplaza sus listas de contacto.
     */
    @Transactional
    public void actualizarAlumno(Long id, String nombre, List<String> telefonos, List<String> correos) {
        // Bloque: Localización del registro
        Alumno alumno = obtenerPorId(id);
        if (alumno != null) {
            alumno.setNombreAlumno(nombre);
            
            // Bloque: Limpieza de contactos antiguos
            // Al limpiar la lista, JPA se encarga de eliminar los registros huérfanos en la BD
            if (alumno.getTelefonos() != null) {
                alumno.getTelefonos().clear();
            } else {
                alumno.setTelefonos(new java.util.ArrayList<>());
            }
            
            if (alumno.getCorreos() != null) {
                alumno.getCorreos().clear();
            } else {
                alumno.setCorreos(new java.util.ArrayList<>());
            }

            // Bloque: Inserción de nuevos Teléfonos
            if (telefonos != null) {
                for (String tel : telefonos) {
                    if (tel != null && !tel.trim().isEmpty()) {
                        TelefonoAlumno ta = new TelefonoAlumno();
                        ta.setTelefono(tel.trim());
                        ta.setAlumno(alumno);
                        alumno.getTelefonos().add(ta);
                    }
                }
            }

            // Bloque: Inserción de nuevos Correos
            if (correos != null) {
                for (String email : correos) {
                    if (email != null && !email.trim().isEmpty()) {
                        CorreoAlumno ca = new CorreoAlumno();
                        ca.setCorreo(email.trim());
                        ca.setAlumno(alumno);
                        alumno.getCorreos().add(ca);
                    }
                }
            }

            // Sincroniza los cambios con la base de datos
            alumnoRepository.save(alumno);
        }
    }

    /**
     * Elimina a un alumno y toda su información relacionada (comprobantes y contactos).
     */
    @Transactional
    public void eliminarAlumno(Long id) {
        Alumno alumno = obtenerPorId(id);
        if (alumno != null) {
            // Bloque: Eliminación en cascada manual
            // Se eliminan los comprobantes primero para evitar violaciones de llaves foráneas (FK)
            List<ComprobantePago> comprobantes =
                    comprobantePagoRepository.findByAlumno_IdAlumno(id);
            if (comprobantes != null && !comprobantes.isEmpty()) {
                comprobantePagoRepository.deleteAll(comprobantes);
            }
            
            // Eliminación de contactos
            if (alumno.getTelefonos() != null) {
                telefonoAlumnoRepository.deleteAll(alumno.getTelefonos());
            }
            if (alumno.getCorreos() != null) {
                correoAlumnoRepository.deleteAll(alumno.getCorreos());
            }
            
            // Finalmente se elimina la entidad Alumno
            alumnoRepository.delete(alumno);
        }
    }
}
