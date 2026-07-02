-- EventosMS - crea las 6 bases de datos que usan los microservicios.
-- MySQL en el host (localhost:3306, usuario root). Ejecutar una sola vez:
--   mysql -u root -p < init-databases.sql
CREATE DATABASE IF NOT EXISTS ms_evento        CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS ms_participants   CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS ms_registro       CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS ms_auth           CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS ms_pago           CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS ms_asistencia     CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
