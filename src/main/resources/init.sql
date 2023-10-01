DROP TABLE IF EXISTS tiempos;
CREATE TABLE IF NOT EXISTS tiempos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    localidad VARCHAR(255) NOT NULL,
    provincia VARCHAR(255) NOT NULL,
    tempMax NUMERIC(4,2) NOT NULL,
    horaTMax TIME(0) NOT NULL,
    tempMin NUMERIC(4,2) NOT NULL,
    horaTemMin TIME(0) NOT NULL,
    precipìtacion BOOLEAN,
    codigo UUID NOT NULL
);
