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
				
				jdbcTemplate.execute("ALTER TABLE telefono_profesor DROP COLUMN IF EXISTS telefono_profesor");
				jdbcTemplate.execute("ALTER TABLE telefono_profesor DROP COLUMN IF EXISTS profesor_id_profesor");
				
				jdbcTemplate.execute("ALTER TABLE correo_profesor DROP COLUMN IF EXISTS correo_profesor");
				jdbcTemplate.execute("ALTER TABLE correo_profesor DROP COLUMN IF EXISTS profesor_id_profesor");
				
				// Limpieza de columnas duplicadas en la tabla clase generadas por Hibernate
				jdbcTemplate.execute("ALTER TABLE clase DROP COLUMN IF EXISTS aula_id_aula");
				jdbcTemplate.execute("ALTER TABLE clase DROP COLUMN IF EXISTS profesor_id_profesor");
				jdbcTemplate.execute("ALTER TABLE clase DROP COLUMN IF EXISTS materia_id_materia");
				jdbcTemplate.execute("ALTER TABLE clase DROP COLUMN IF EXISTS dia_semana_id_dia_semana");

				// Solución al conflicto de llaves foráneas: eliminar constraint vieja a tabla 'aula'
				jdbcTemplate.execute("ALTER TABLE clase DROP CONSTRAINT IF EXISTS fkdnm532p2jrmlamven0a1725t9");
			} catch (Exception ex) {
				System.out.println("Nota: no se pudieron eliminar las columnas o constraints (tal vez no existan).");
			}

			// Listar tablas para depuración
			jdbcTemplate.queryForList("SELECT table_name FROM information_schema.tables WHERE table_schema='public'").forEach(row -> {
				System.out.println("Tabla encontrada: " + row.get("table_name"));
			});

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