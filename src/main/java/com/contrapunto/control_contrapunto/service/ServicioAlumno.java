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

@Service
@RequiredArgsConstructor
public class ServicioAlumno {

    private final AlumnoRepository alumnoRepository;
    private final ComprobantePagoRepository comprobantePagoRepository;
    private final TelefonoAlumnoRepository telefonoAlumnoRepository;
    private final CorreoAlumnoRepository correoAlumnoRepository;

    /**
     * Lista todos los alumnos y les calcula dinámicamente
     * 'ultimoPago' (MAX fecha de sus comprobantes) y
     * 'proximoPago' (ultimoPago + 1 mes).
     */
    public List<Alumno> listarTodos() {
        List<Alumno> alumnos = alumnoRepository.findAll();

        for (Alumno alumno : alumnos) {
            Optional<LocalDate> ultimoPago =
                    comprobantePagoRepository.findUltimoPagoByAlumnoId(alumno.getIdAlumno());

            if (ultimoPago.isPresent()) {
                alumno.setUltimoPago(ultimoPago.get());
                alumno.setProximoPago(ultimoPago.get().plusMonths(1));
            } else {
                alumno.setUltimoPago(null);
                alumno.setProximoPago(null);
            }
        }

        return alumnos;
    }

    /**
     * Guarda un nuevo alumno junto con sus listas de teléfonos y correos.
     * Inicializa las fechas de pago como nulas.
     */
    @Transactional
    public void guardarAlumnoConContactos(String nombre, List<String> telefonos, List<String> correos) {
        // Instancia un nuevo objeto Alumno, asigna el nombre y genera ID
        Alumno alumno = new Alumno();
        alumno.setNombreAlumno(nombre);
        alumno.setUltimoPago(null);
        alumno.setProximoPago(null);
        
        alumno = alumnoRepository.save(alumno); // Guarda para generar ID

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

    public Alumno obtenerPorId(Long id) {
        return alumnoRepository.findById(id).orElse(null);
    }

    @Transactional
    public void actualizarAlumno(Long id, String nombre, List<String> telefonos, List<String> correos) {
        Alumno alumno = obtenerPorId(id);
        if (alumno != null) {
            alumno.setNombreAlumno(nombre);
            
            // Limpiar las listas actuales para que JPA ejecute los deletes por orphanRemoval
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

            // Añadir los nuevos teléfonos
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

            // Añadir los nuevos correos
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

            // Guardar cambios del nombre y de los contactos
            alumnoRepository.save(alumno);
        }
    }

    @Transactional
    public void eliminarAlumno(Long id) {
        Alumno alumno = obtenerPorId(id);
        if (alumno != null) {
            // Eliminar comprobantes primero para evitar violación de FK en PostgreSQL
            List<ComprobantePago> comprobantes =
                    comprobantePagoRepository.findByAlumno_IdAlumno(id);
            if (comprobantes != null && !comprobantes.isEmpty()) {
                comprobantePagoRepository.deleteAll(comprobantes);
            }
            if (alumno.getTelefonos() != null) {
                telefonoAlumnoRepository.deleteAll(alumno.getTelefonos());
            }
            if (alumno.getCorreos() != null) {
                correoAlumnoRepository.deleteAll(alumno.getCorreos());
            }
            alumnoRepository.delete(alumno);
        }
    }
}
