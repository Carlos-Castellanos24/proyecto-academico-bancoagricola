-- DROP DATABASE transferba;

CREATE DATABASE TransferBA;

USE TransferBA;

-- Tabla Usuarios
CREATE TABLE clientes (
	id_cliente INT AUTO_INCREMENT, 
    CONSTRAINT PK_CLIENTE PRIMARY KEY (id_cliente),
	nombres VARCHAR(50), 
	apellidos VARCHAR(50), 
	dui VARCHAR(9),
	telefono VARCHAR(15), 
	direccion VARCHAR(200), 
	fecha_nac DATE, 
	correo VARCHAR(100), 
	estado ENUM('A','I')
);

-- Tabla Tipo_Cuenta
CREATE TABLE tipo_cuenta (
	id_tipo_cuenta INT AUTO_INCREMENT,
    CONSTRAINT PK_TIPO_CUENTA PRIMARY KEY (id_tipo_cuenta),
	nombre VARCHAR(25), 
	descripcion VARCHAR(50)
);

-- Tabla Cuentas
CREATE TABLE cuentas (
	id_cuenta  INT AUTO_INCREMENT, 
    CONSTRAINT PK_CUENTA PRIMARY KEY (id_cuenta),
    id_cliente INT,
	numero VARCHAR(25), 
	saldo DECIMAL(20,2), 
	estado ENUM('A','I'),
	id_tipo_cuenta INT
);

-- Tabla Clientes_Cuentas
CREATE TABLE clientes_cuentas (
	id_cliente_cuenta  INT AUTO_INCREMENT,
    CONSTRAINT PK_CLIENTE_CUENTA PRIMARY KEY (id_cliente_cuenta),
	id_cliente INT, 
    id_cuenta INT
);

-- Tabla Login
CREATE TABLE login (
	id_login INT AUTO_INCREMENT, 
    CONSTRAINT PK_LOGIN PRIMARY KEY (id_login),
	id_cliente INT, 
	username VARCHAR(200), 
	clave VARCHAR(200), 
    password_token VARCHAR(60),
	estado ENUM('A','I')
);

-- Tabla Bitacora
CREATE TABLE bitacora (
	id_bitacora INT AUTO_INCREMENT, 
    CONSTRAINT PK_BITACORA PRIMARY KEY (id_bitacora),
	id_cliente INT , 
	fecha DATETIME, 
	descripcion TEXT
);

-- Tabla Transferencias
CREATE TABLE transferencias (
	id_transfer INT AUTO_INCREMENT, 
    CONSTRAINT PK_TRANSFERENCIA PRIMARY KEY (id_transfer),    
	id_cuenta INT,
	referencia VARCHAR(25),
	cuenta_receptor VARCHAR(25), 
	monto DECIMAL(20,2), 
	fecha DATETIME, 
	correo VARCHAR(200), 
	concepto VARCHAR(500),
	autorizacion VARCHAR(200)
);

-- Tabla Clientes_Transferencias
CREATE TABLE clientes_transferencias (
	id_cliente_transfer INT AUTO_INCREMENT,
    CONSTRAINT PRIMARY KEY (id_cliente_transfer),
	id_cliente INT, 
	id_transfer INT
);

-- RELACIONES
ALTER TABLE cuentas ADD CONSTRAINT FK_CUENTA_CLIENTE FOREIGN KEY (id_cliente) REFERENCES clientes (id_cliente);
ALTER TABLE cuentas ADD CONSTRAINT FK_TIPO_CUENTA  FOREIGN KEY (id_tipo_cuenta) REFERENCES tipo_cuenta (id_tipo_cuenta);
ALTER TABLE clientes_cuentas ADD CONSTRAINT FK_CLIENTES_CUENTA FOREIGN KEY (id_cliente) REFERENCES clientes (id_cliente);
ALTER TABLE clientes_cuentas ADD CONSTRAINT FK_CLIENTE_CUENTA_CLIENTE FOREIGN KEY (id_cuenta) REFERENCES cuentas (id_cuenta);
ALTER TABLE login ADD CONSTRAINT FK_CLIENTE_LOGIN FOREIGN KEY (id_cliente) REFERENCES clientes (id_cliente);
ALTER TABLE transferencias ADD CONSTRAINT FK_TRANSFERENCIAS_CUENTA FOREIGN KEY (id_cuenta) REFERENCES cuentas (id_cuenta);
ALTER TABLE clientes_transferencias ADD CONSTRAINT FK_CLITRANSFER_CLIENTE FOREIGN KEY (id_cliente) REFERENCES clientes (id_cliente);
ALTER TABLE clientes_transferencias ADD CONSTRAINT FK_CLITRANSFER_TRANSFERENCIA FOREIGN KEY (id_transfer) REFERENCES transferencias (id_transfer);

-- TRIGGER

-- DROP TRIGGER trg_actualizar_saldos_cuentas;

DELIMITER //
CREATE TRIGGER trg_actualizar_saldos_cuentas
AFTER INSERT ON transferencias
FOR EACH ROW
BEGIN
    UPDATE cuentas
    SET saldo = saldo - NEW.monto
    WHERE id_cuenta = NEW.id_cuenta;
    
    IF NEW.cuenta_receptor IS NOT NULL THEN
        UPDATE cuentas
        SET saldo = saldo + NEW.monto
        WHERE numero = NEW.cuenta_receptor;
    END IF;
END;
//
DELIMITER ;

-- DROP TRIGGER trg_insertar_clientes_transferencias;

DELIMITER //
CREATE TRIGGER trg_insertar_clientes_transferencias
AFTER INSERT ON transferencias
FOR EACH ROW
BEGIN	
    DECLARE id_cliente INT;
    
    SELECT c.id_cliente INTO id_cliente FROM cuentas c  LEFT JOIN clientes b
    ON b.id_cliente = c.id_cliente WHERE id_cuenta = NEW.id_cuenta; 

	IF id_cliente IS NOT NULL THEN
		INSERT INTO clientes_transferencias (id_cliente, id_transfer)
		VALUES (id_cliente, NEW.id_transfer);
    END IF;
END;
//
DELIMITER ;

-- DROP TRIGGER trg_insertar_bitacora;

DELIMITER //
CREATE TRIGGER trg_insertar_bitacora
AFTER INSERT ON clientes_transferencias
FOR EACH ROW 
BEGIN
    DECLARE nombre_emisor VARCHAR(100);
    DECLARE nombre_receptor VARCHAR(100);
    DECLARE monto DECIMAL(20,2);
    DECLARE fecha DATETIME;
    DECLARE id_cliente INT;
    DECLARE cuenta_receptor VARCHAR(25);
    
    -- PARA RECUPERAR TODOS LOS CAMPOS DECLARADOS EXCEPTO EL NOMBRE DEL RECEPTOR
    SELECT CONCAT(b.nombres, ' ', b.apellidos) AS nombre_emisor, t.monto, t.fecha, c.id_cliente, t.cuenta_receptor 
    INTO nombre_emisor, monto, fecha, id_cliente, cuenta_receptor
    FROM clientes_transferencias ct
    LEFT JOIN clientes b ON ct.id_cliente = b.id_cliente
    LEFT JOIN transferencias t ON t.id_transfer = ct.id_transfer
    LEFT JOIN cuentas c ON c.numero = t.cuenta_receptor
    WHERE ct.id_transfer = NEW.id_transfer;
    
    -- PARA OBTENER EL NOMBRE DEL RECEPTOR
    SELECT CONCAT(c.nombres, ' ', c.apellidos) AS nombre_receptor INTO nombre_receptor FROM clientes_transferencias ct
	LEFT JOIN transferencias t ON t.id_transfer = NEW.id_transfer
	LEFT JOIN cuentas b ON b.numero = t.cuenta_receptor
	LEFT JOIN clientes c ON c.id_cliente = b.id_cliente LIMIT 1; 
    
    IF nombre_emisor IS NOT NULL AND nombre_receptor IS NOT NULL AND monto IS NOT NULL AND fecha IS NOT NULL AND id_cliente IS NOT NULL AND cuenta_receptor IS NOT NULL THEN
        IF NEW.id_cliente = id_cliente THEN
			INSERT INTO bitacora (id_bitacora, id_cliente, fecha, descripcion) VALUES 
			(NULL, id_cliente, fecha, CONCAT('El cliente ', nombre_emisor, ' realizó una transferencia de ', '$', monto, ' dólares a una cuenta propia con el numero de cuenta ', cuenta_receptor, ' el día ', fecha));
        ELSE
			INSERT INTO bitacora (id_bitacora, id_cliente, fecha, descripcion) VALUES 
			(NULL, id_cliente, fecha, CONCAT('El cliente ', nombre_emisor, ' realizó una transferencia de ', '$', monto, ' dólares a nombre del beneficiario ', nombre_receptor, ' el día ', fecha));
		END IF;
    END IF;
END;
//
DELIMITER ;

-- INSERTS

-- INSERT CLIENTE
INSERT INTO clientes (id_cliente, nombres, apellidos, dui, telefono, direccion, fecha_nac, correo, estado) VALUES (NULL, 'Carlos Rodolfo', 'Vásquez Castellanos', '0567434-9', '7393-3502', 'San Salvador, El Salvador', '2002-03-24', '2508282019@mail.utec.edu.sv', 'A');
INSERT INTO clientes (id_cliente, nombres, apellidos, dui, telefono, direccion, fecha_nac, correo, estado) VALUES (NULL, 'Rogelio Isai', 'Menjivar Marroquin', '0664378-9', '8655-3467', 'San Salvador, El Salvador', '2000-07-15', '2527862020@mail.utec.edu.sv', 'A');
INSERT INTO clientes (id_cliente, nombres, apellidos, dui, telefono, direccion, fecha_nac, correo, estado) VALUES (NULL, 'Diego Armando', 'Echeverria Santacruz', '8673456-9', '9564-3456', 'San Salvador, El Salvador', '2000-07-15', '2500612020@mail.utec.edu.sv', 'A');

-- INSERT LOGIN
INSERT INTO login (id_login, id_cliente, username, clave, estado) VALUES (NULL, 1, 'Carlos Castellanos', '1234', 'A');
INSERT INTO login (id_login, id_cliente, username, clave, estado) VALUES (NULL, 2, 'Rogelio Menjivar', '1234', 'A');
INSERT INTO login (id_login, id_cliente, username, clave, estado) VALUES (NULL, 3, 'Diego Echeverria', '1234', 'A');

-- INSERT TIPO CUENTA
INSERT INTO tipo_cuenta (id_tipo_cuenta, nombre, descripcion) VALUES (NULL, 'AHORROS', 'CUENTA DE AHORROS');
INSERT INTO tipo_cuenta (id_tipo_cuenta, nombre, descripcion) VALUES (NULL, 'CORRIENTE', 'CUENTA CORRIENTE');

-- INSERT CUENTAS
INSERT INTO cuentas (id_cuenta, id_cliente, numero, saldo, estado, id_tipo_cuenta) VALUES (NULL, 1, '8745294852', 700, 'A', 1);
INSERT INTO cuentas (id_cuenta, id_cliente, numero, saldo, estado, id_tipo_cuenta) VALUES (NULL, 1, '7774234512', 100, 'A', 1);
INSERT INTO cuentas (id_cuenta, id_cliente, numero, saldo, estado, id_tipo_cuenta) VALUES (NULL, 2, '6457562345', 1000, 'A', 1);
INSERT INTO cuentas (id_cuenta, id_cliente, numero, saldo, estado, id_tipo_cuenta) VALUES (NULL, 2, '6452346864', 100, 'A', 1);
INSERT INTO cuentas (id_cuenta, id_cliente, numero, saldo, estado, id_tipo_cuenta) VALUES (NULL, 3, '6457234654', 600, 'A', 1);

-- INSERT CLIENTES_CUENTAS
INSERT INTO clientes_cuentas (id_cliente_cuenta, id_cliente, id_cuenta) VALUES (NULL, 1, 1);
INSERT INTO clientes_cuentas (id_cliente_cuenta, id_cliente, id_cuenta) VALUES (NULL, 1, 2);
INSERT INTO clientes_cuentas (id_cliente_cuenta, id_cliente, id_cuenta) VALUES (NULL, 2, 3);
INSERT INTO clientes_cuentas (id_cliente_cuenta, id_cliente, id_cuenta) VALUES (NULL, 2, 4);
INSERT INTO clientes_cuentas (id_cliente_cuenta, id_cliente, id_cuenta) VALUES (NULL, 3, 5);