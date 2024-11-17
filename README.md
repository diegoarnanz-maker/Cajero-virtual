Script mysql:

CREATE DATABASE cajero_2024_roles;
USE cajero_2024_roles;

CREATE TABLE roles (
    id_rol INT PRIMARY KEY AUTO_INCREMENT,
    nombre_rol VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE usuarios (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    id_rol INT,
    id_cuenta INT UNIQUE,
    FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
);

CREATE TABLE cuentas (
    id_cuenta INT PRIMARY KEY AUTO_INCREMENT,
    saldo DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    tipo_cuenta VARCHAR(50),
    id_usuario INT UNIQUE,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

CREATE TABLE movimientos (
    id_movimiento INT PRIMARY KEY AUTO_INCREMENT,
    id_cuenta INT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cantidad DECIMAL(15, 2) NOT NULL,
    operacion VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_cuenta) REFERENCES cuentas(id_cuenta)
);

DROP USER IF EXISTS 'ucajeroroles'@'%';

CREATE USER 'ucajeroroles'@'%' IDENTIFIED BY 'ucajeroroles';
GRANT ALL PRIVILEGES ON cajero_2024_roles.* TO 'ucajeroroles'@'%';
FLUSH PRIVILEGES;

INSERT INTO roles (nombre_rol) VALUES ('Administrador'), ('Usuario');

INSERT INTO usuarios (username, password, id_rol, id_cuenta) VALUES
  ('admin', '12345678', 1, 3),
  ('diego', '12345678', 2, 1),
  ('isabel', '12345678', 2, 2);

INSERT INTO cuentas (saldo, tipo_cuenta, id_usuario) VALUES
  (1000.00, 'Ahorros', 2),
  (500.00, 'Corriente', 3);

INSERT INTO movimientos (id_cuenta, cantidad, operacion) VALUES
  (1, 200.00, 'Ingreso'),
  (1, 100.00, 'Extraccion'),
  (2, 50.00, 'Ingreso'),
  (2, 30.00, 'Extraccion');
  
-- Creo una cuenta para el admin con saldo 0
INSERT INTO cuentas (tipo_cuenta, id_usuario) VALUES
  ('Administrador', 1);

-- Asocio la cuenta al admin
INSERT INTO usuarios (username, password, id_rol, id_cuenta) VALUES
  ('admin', '12345678', 1, LAST_INSERT_ID());

