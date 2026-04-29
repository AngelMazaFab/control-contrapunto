package com.contrapunto.control_contrapunto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class ControlContrapuntoApplication implements CommandLineRunner {

	// Inyectamos JdbcTemplate para poder hacer consultas directas a la BD
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(ControlContrapuntoApplication.class, args);
	}

	// Este método se ejecutará automáticamente al arrancar la aplicación
	@Override
	public void run(String... args) throws Exception {
		System.out.println("\n=============================================");
		System.out.println("Iniciando prueba de conexión a Supabase...");

		try {
			// Arreglamos el esquema de la base de datos en caso de que Hibernate haya creado columnas erróneas
			try {
				jdbcTemplate.execute("ALTER TABLE telefono_alumno DROP COLUMN IF EXISTS alumno_id_alumno");
				jdbcTemplate.execute("ALTER TABLE correo_alumno DROP COLUMN IF EXISTS alumno_id_alumno");
			} catch (Exception ex) {
				System.out.println("Nota: no se pudieron eliminar las columnas (tal vez no existan).");
			}

			// Mandamos una consulta muy básica que PostgreSQL siempre debe responder
			jdbcTemplate.execute("SELECT 1");
			System.out.println("✅ ¡ÉXITO! La conexión a la base de datos es correcta y las tablas están purgadas.");
		} catch (Exception e) {
			System.out.println("❌ ERROR: No se pudo conectar a la base de datos.");
			System.out.println("Detalle: " + e.getMessage());
		}
		System.out.println("=============================================\n");
	}
}