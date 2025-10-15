-- Create Tables
CREATE TABLE IF NOT EXISTS persona (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombres VARCHAR(255) NOT NULL,
    apellidos VARCHAR(255) NOT NULL,
    email VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS facultad (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    decano_id BIGINT,
    FOREIGN KEY (decano_id) REFERENCES persona(id)
);

CREATE TABLE IF NOT EXISTS programa (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    duracion DOUBLE,
    registro_calificado DATE,
    facultad_id BIGINT,
    FOREIGN KEY (facultad_id) REFERENCES facultad(id)
);

CREATE TABLE IF NOT EXISTS estudiante (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombres VARCHAR(255) NOT NULL,
    apellidos VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    codigo BIGINT
);

CREATE TABLE IF NOT EXISTS profesor (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombres VARCHAR(255) NOT NULL,
    apellidos VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    tipo_contrato VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS curso (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    programa_id BIGINT,
    activo BOOLEAN,
    FOREIGN KEY (programa_id) REFERENCES programa(id)
);

CREATE TABLE IF NOT EXISTS inscripcion (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ano INT,
    semestre INT,
    estudiante_id BIGINT,
    curso_id BIGINT,
    FOREIGN KEY (estudiante_id) REFERENCES estudiante(id),
    FOREIGN KEY (curso_id) REFERENCES curso(id)
);

CREATE TABLE IF NOT EXISTS cursoprofesor (
    profesor_id BIGINT,
    curso_id BIGINT,
    ano INT,
    semestre INT,
    PRIMARY KEY (profesor_id, curso_id, ano, semestre),
    FOREIGN KEY (profesor_id) REFERENCES profesor(id),
    FOREIGN KEY (curso_id) REFERENCES curso(id)
);

-- --- DATOS DE EJEMPLO ---

INSERT INTO persona (id, nombres, apellidos, email) VALUES
(1, 'Ricardo', 'Perez', 'ricardo.perez@university.com'),
(2, 'Ana', 'Lopez', 'ana.lopez@university.com'),
(3, 'Luis', 'Martinez', 'luis.martinez@external.com')
ON DUPLICATE KEY UPDATE
    nombres = VALUES(nombres),
    apellidos = VALUES(apellidos),
    email = VALUES(email);

INSERT INTO facultad (id, nombre, decano_id) VALUES
(1, 'Ingeniería', 1),
(2, 'Ciencias Sociales', NULL)
ON DUPLICATE KEY UPDATE
    nombre = VALUES(nombre),
    decano_id = VALUES(decano_id);

INSERT INTO programa (id, nombre, duracion, registro_calificado, facultad_id) VALUES
(1, 'Ingeniería de Sistemas', 10, '2023-01-15', 1),
(2, 'Comunicación Social', 8, '2022-05-20', 2)
ON DUPLICATE KEY UPDATE
    nombre = VALUES(nombre),
    duracion = VALUES(duracion),
    registro_calificado = VALUES(registro_calificado),
    facultad_id = VALUES(facultad_id);

INSERT INTO profesor (id, nombres, apellidos, email, tipo_contrato) VALUES
(1, 'Carlos', 'Ramirez', 'carlos.r@university.com', 'Tiempo Completo'),
(2, 'Marta', 'Jimenez', 'marta.j@university.com', 'Cátedra')
ON DUPLICATE KEY UPDATE
    nombres = VALUES(nombres),
    apellidos = VALUES(apellidos),
    email = VALUES(email),
    tipo_contrato = VALUES(tipo_contrato);

INSERT INTO estudiante (id, nombres, apellidos, email, codigo) VALUES
(1, 'Juan', 'Gomez', 'juan.g@student.com', 20241001),
(2, 'Maria', 'Rodriguez', 'maria.r@student.com', 20241002)
ON DUPLICATE KEY UPDATE
    nombres = VALUES(nombres),
    apellidos = VALUES(apellidos),
    email = VALUES(email),
    codigo = VALUES(codigo);

INSERT INTO curso (id, nombre, programa_id, activo) VALUES
(1, 'Cálculo Diferencial', 1, TRUE),
(2, 'Programación Orientada a Objetos', 1, TRUE),
(3, 'Teorías de la Comunicación', 2, FALSE)
ON DUPLICATE KEY UPDATE
    nombre = VALUES(nombre),
    programa_id = VALUES(programa_id),
    activo = VALUES(activo);

INSERT INTO cursoprofesor (profesor_id, curso_id, ano, semestre) VALUES
(1, 1, 2025, 1),
(2, 2, 2025, 1)
ON DUPLICATE KEY UPDATE
    ano = VALUES(ano),
    semestre = VALUES(semestre);

INSERT INTO inscripcion (id, estudiante_id, curso_id, ano, semestre) VALUES
(1, 1, 1, 2025, 1),
(2, 2, 2, 2025, 1)
ON DUPLICATE KEY UPDATE
    estudiante_id = VALUES(estudiante_id),
    curso_id = VALUES(curso_id),
    ano = VALUES(ano),
    semestre = VALUES(semestre);