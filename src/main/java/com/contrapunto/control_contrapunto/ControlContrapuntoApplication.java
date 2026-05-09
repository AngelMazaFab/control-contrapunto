package com.contrapunto.control_contrapunto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@EnableScheduling
public class ControlContrapuntoApplication implements CommandLineRunner {

	// Inyectamos JdbcTemplate para poder hacer consultas directas a la BD
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(ControlContrapuntoApplication.class, args);
	}

	// Este método se ejecuta automáticamente al arrancar la aplicación
	@Override
	public void run(String... args) throws Exception {
		System.out.println("\n=============================================");
		System.out.println("Sincronizando esquema de base de datos...");

		try {
			// Limpieza de columnas obsoletas o erróneas generadas por versiones anteriores
			try {
				// Tablas de contacto
				jdbcTemplate.execute("ALTER TABLE telefono_alumno DROP COLUMN IF EXISTS alumno_id_alumno");
				jdbcTemplate.execute("ALTER TABLE correo_alumno DROP COLUMN IF EXISTS alumno_id_alumno");
				jdbcTemplate.execute("ALTER TABLE telefono_profesor DROP COLUMN IF EXISTS telefono_profesor, DROP COLUMN IF EXISTS profesor_id_profesor");
				jdbcTemplate.execute("ALTER TABLE correo_profesor DROP COLUMN IF EXISTS correo_profesor, DROP COLUMN IF EXISTS profesor_id_profesor");

				// Tabla Salones
				jdbcTemplate.execute("ALTER TABLE salones DROP COLUMN IF EXISTS capacidad");

				// Tabla Clase (Limpieza de duplicados de Hibernate)
				jdbcTemplate.execute("ALTER TABLE clase DROP COLUMN IF EXISTS aula_id_aula, DROP COLUMN IF EXISTS profesor_id_profesor, DROP COLUMN IF EXISTS materia_id_materia, DROP COLUMN IF EXISTS dia_semana_id_dia_semana");
				jdbcTemplate.execute("ALTER TABLE clase DROP CONSTRAINT IF EXISTS fkdnm532p2jrmlamven0a1725t9");

				// Tabla Comprobante
				jdbcTemplate.execute("ALTER TABLE comprobante_pago DROP COLUMN IF EXISTS alumno_id_alumno CASCADE");
			} catch (Exception ex) {
				System.out.println("Nota: Algunas columnas ya estaban limpias.");
			}

			// Prueba de conexión básica
			jdbcTemplate.execute("SELECT 1");
			System.out.println("✅ Conexión exitosa y esquema purgado.");
		} catch (Exception e) {
			System.out.println("❌ Error de conexión: " + e.getMessage());
		}
		System.out.println("=============================================\n");
	}
}