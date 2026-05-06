-- ================================================
-- MIGRACIÓN: Adaptar tabla comprobante_pago
-- Ejecutar manualmente en Supabase (SQL Editor)
-- ================================================

-- 1. Renombrar columna fecha_pago -> fecha_emision (si existe)
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'comprobante_pago' AND column_name = 'fecha_pago'
    ) THEN
        ALTER TABLE comprobante_pago RENAME COLUMN fecha_pago TO fecha_emision;
    END IF;
END $$;

-- 2. Renombrar columna descripcion -> asunto (si existe)
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'comprobante_pago' AND column_name = 'descripcion'
    ) THEN
        ALTER TABLE comprobante_pago RENAME COLUMN descripcion TO asunto;
    END IF;
END $$;

-- 3. Cambiar tipo de importe de NUMERIC(10,2) a DOUBLE PRECISION (si es necesario)
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'comprobante_pago'
          AND column_name = 'importe'
          AND data_type = 'numeric'
    ) THEN
        ALTER TABLE comprobante_pago
            ALTER COLUMN importe TYPE DOUBLE PRECISION USING importe::DOUBLE PRECISION;
    END IF;
END $$;

-- 4. Eliminar columna archivo_url si ya no se usa (OPCIONAL - comentar si prefieres conservarla)
-- ALTER TABLE comprobante_pago DROP COLUMN IF EXISTS archivo_url;

-- Verificar resultado
SELECT column_name, data_type FROM information_schema.columns
WHERE table_name = 'comprobante_pago'
ORDER BY ordinal_position;
