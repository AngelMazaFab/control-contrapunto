package com.contrapunto.control_contrapunto.service;

import com.contrapunto.control_contrapunto.model.Alumno;
import com.contrapunto.control_contrapunto.repository.AlumnoRepository;
import com.contrapunto.control_contrapunto.repository.ComprobantePagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicioAlumno {

    private final AlumnoRepository alumnoRepository;
    private final ComprobantePagoRepository comprobantePagoRepository;

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
}
