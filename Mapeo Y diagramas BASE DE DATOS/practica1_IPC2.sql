CREATE DATABASE practica1_IPC2;
USE practica1_IPC2;

CREATE TABLE roles (
    id_rol         INT AUTO_INCREMENT PRIMARY KEY,
    nombre         VARCHAR(100)  NOT NULL UNIQUE,
    descripcion    VARCHAR(255),
    activo         BOOLEAN      NOT NULL DEFAULT TRUE,
    fecha_creacion DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion DATETIME ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE sucursales (
    id_sucursal    INT AUTO_INCREMENT PRIMARY KEY,
    nombre         VARCHAR(100) NOT NULL,
    direccion      VARCHAR(250),
    telefono       VARCHAR(10),
    activa         BOOLEAN      NOT NULL DEFAULT TRUE,
    fecha_creacion DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion DATETIME ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE usuarios (
    id_usuario     INT AUTO_INCREMENT PRIMARY KEY,
    usuario      VARCHAR(50)  NOT NULL UNIQUE,
    contraseña  VARCHAR(255) NOT NULL,         
    nombre         VARCHAR(100) NOT NULL,
    apellido       VARCHAR(100),
    email          VARCHAR(100) UNIQUE,
    id_rol         INT NOT NULL,
    id_sucursal    INT,                    
    activo         BOOLEAN      NOT NULL DEFAULT TRUE,
    fecha_creacion DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion DATETIME ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario_rol     FOREIGN KEY (id_rol)      REFERENCES roles(id_rol),
    CONSTRAINT fk_usuario_sucursal FOREIGN KEY (id_sucursal) REFERENCES sucursales(id_sucursal)
);

CREATE TABLE productos (
    id_producto    INT AUTO_INCREMENT PRIMARY KEY,
    nombre         VARCHAR(100) NOT NULL,
    descripcion    VARCHAR(250),
    precio         DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    categoria      VARCHAR(80),
    imagen         VARCHAR(255),
    activo         BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE producto_sucursal (
    id_producto_sucursal INT AUTO_INCREMENT PRIMARY KEY,
    id_producto    INT NOT NULL,
    id_sucursal    INT NOT NULL,
    disponible     BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto),
    FOREIGN KEY (id_sucursal) REFERENCES sucursales(id_sucursal)
);

CREATE TABLE parametros_juego (
    id_parametro   INT AUTO_INCREMENT PRIMARY KEY,
    clave          VARCHAR(100)  NOT NULL UNIQUE,
    valor          VARCHAR(250) NOT NULL,
    descripcion    VARCHAR(255)
);

CREATE TABLE partida (
    id_partida            INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario            INT NOT NULL,
    id_sucursal           INT NOT NULL,
    fecha_inicio          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_fin             DATETIME,
    estado                ENUM('EN_CURSO','FINALIZADA','ABANDONADA') NOT NULL DEFAULT 'EN_CURSO',
    puntaje_total         INT NOT NULL DEFAULT 0,
    nivel_maximo          INT NOT NULL DEFAULT 1,
    pedidos_completados   INT NOT NULL DEFAULT 0,
    pedidos_cancelados    INT NOT NULL DEFAULT 0,
    pedidos_no_entregados INT NOT NULL DEFAULT 0,
    FOREIGN KEY (id_usuario)  REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_sucursal) REFERENCES sucursales(id_sucursal)
);

CREATE TABLE niveles_partida (
    id_nivel_partida     INT AUTO_INCREMENT PRIMARY KEY,
    id_partida           INT NOT NULL,
    nivel                INT NOT NULL,
    fecha_alcanzado      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    puntaje_por_alcanzar  INT NOT NULL DEFAULT 0,
    pedidos_por_alcanzar  INT NOT NULL DEFAULT 0,
    FOREIGN KEY (id_partida) REFERENCES partida(id_partida)
);

CREATE TABLE pedidos (
    id_pedido          INT AUTO_INCREMENT PRIMARY KEY,
    id_partida         INT NOT NULL,
    numero_pedido      INT NOT NULL,
    nivel_al_crear     INT NOT NULL DEFAULT 1,
    tiempo_limite_seg  INT NOT NULL,
    fecha_creacion     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_finalizacion DATETIME,
    estado             ENUM('RECIBIDA','PREPARANDO','EN_HORNO','LISTA',
                            'CANCELADA','NO_ENTREGADO') NOT NULL DEFAULT 'RECIBIDA',
    tiempo_usado_seg   INT,
    puntos_obtenidos   INT NOT NULL DEFAULT 0,
    FOREIGN KEY (id_partida) REFERENCES partida(id_partida)
);

CREATE TABLE detalle_pedido (
    id_detalle    INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido     INT NOT NULL,
    id_producto   INT NOT NULL,
    cantidad      INT NOT NULL DEFAULT 1,
    FOREIGN KEY (id_pedido)   REFERENCES pedidos(id_pedido),
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);

CREATE TABLE historial_estados_pedido (
    id_historial    INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido       INT NOT NULL,
    estado_anterior ENUM('RECIBIDA','PREPARANDO','EN_HORNO','LISTA','CANCELADA','NO_ENTREGADO'),
    estado_nuevo    ENUM('RECIBIDA','PREPARANDO','EN_HORNO','LISTA','CANCELADA','NO_ENTREGADO') NOT NULL,
    fecha_cambio    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    origen          ENUM('JUGADOR','SISTEMA') NOT NULL DEFAULT 'JUGADOR',
    FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedido)
);

INSERT INTO roles (nombre, descripcion) VALUES
    ('SUPER_ADMINISTRADOR',  'Acceso global al sistema'),
    ('ADMIN_TIENDA', 'Gestiona sucursal'),
    ('JUGADOR',      'Trabaja en la sucursal');

SHOW TABLES;