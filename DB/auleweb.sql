--
-- Database: auleweb
--
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


-- Drop Tabelle
DROP TABLE IF EXISTS Amministratore;
DROP TABLE IF EXISTS Aula;
DROP TABLE IF EXISTS Attrezzatura;
DROP TABLE IF EXISTS Evento;
DROP TABLE IF EXISTS Evento_Ricorrente;
DROP TABLE IF EXISTS Gruppo;
DROP TABLE IF EXISTS Gruppo_Aula;




-- DROP USER IF EXISTS 'aulewebsite'@'localhost';
-- CREATE USER 'aulewebsite'@'localhost' IDENTIFIED BY 'aulewebpass';
-- GRANT ALL ON auleweb.* TO 'aulewebsite'@'localhost';
-- (non so se sono fondamentali per il funzionamento dell'app)




-- ADMIN
CREATE TABLE IF NOT EXISTS Amministratore (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(64) NOT NULL,
    cognome VARCHAR(64) NOT NULL,
    email VARCHAR(128) NOT NULL,
    password VARCHAR(64) NOT NULL,
    cellulare VARCHAR(10) NOT NULL,
    -- version e token da capire a cosa servono
    version INT UNSIGNED NOT NULL DEFAULT 1,
    token VARCHAR(128) DEFAULT NULL,
    UNIQUE (email),
    UNIQUE (telefono)
);

-- AULA
CREATE TABLE IF NOT EXISTS Aula (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(64) NOT NULL,
    luogo VARCHAR(64) NOT NULL,
    edificio VARCHAR(64) NOT NULL,
    piano INT NOT NULL,
    capienza INT NOT NULL,
    email_responsabile VARCHAR(64) NOT NULL,
    numero_prese_rete INT NOT NULL,
    numero_prese_elettriche INT NOT NULL,
    note VARCHAR(255) NOT NULL,
    version INT unsigned NOT NULL DEFAULT 1,
    UNIQUE (nome, luogo, edificio, piano)
);

-- ATTREZZATURA
CREATE TABLE IF NOT EXISTS Attrezzatura (
    id INT PRIMARY KEY AUTO_INCREMENT,
    numero_seriale VARCHAR(10) NOT NULL,
    descrizione VARCHAR(500) NOT NULL,
    id_aula INT DEFAULT NULL,
    -- a che cosa serve "version" ?
    version INT unsigned NOT NULL DEFAULT 1,
    UNIQUE (numero_seriale),
    FOREIGN KEY (id_aula) REFERENCES Aula(id) 
        ON DELETE SET NULL ON UPDATE CASCADE
);

-- EVENTO
CREATE TABLE IF NOT EXISTS Evento (
    id INT PRIMARY KEY AUTO_INCREMENT,
    data_inizio DATETIME NOT NULL,
    data_fine DATETIME NOT NULL,
    nome VARCHAR(64) NOT NULL,
    descrizione VARCHAR(500) NOT NULL,
    email_responsabile VARCHAR(64) NOT NULL,
    id_aula INT NOT NULL,
    tipologia ENUM('LEZIONE', 'ESAME', 'SEMINARIO', 'PARZIALE', 'RIUNIONE', 'LAUREE', 'ALTRO') NOT NULL,
    nome_corso VARCHAR(50) DEFAULT NULL,
    tipo_ricorrenza ENUM('GIORNALIERA', 'SETTIMANALE', 'MENSILE') DEFAULT NULL,
    data_fine_ricorrenza date DEFAULT NULL,
    -- a che cosa serve "version" ?
    version INT unsigned NOT NULL DEFAULT 1,
    UNIQUE (data_inizio, data_fine, nome, id_aula),
    FOREIGN KEY (id_aula) REFERENCES Aula(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- EVENTO RICORRENTE
CREATE TABLE IF NOT EXISTS Evento_Ricorrente (
    id INT PRIMARY KEY AUTO_INCREMENT,
    data_inizio DATETIME NOT NULL,
    data_fine DATETIME NOT NULL,
    id_master INT NOT NULL,
    -- a che cosa serve "version" ?
    version INT unsigned NOT NULL DEFAULT 1,
    FOREIGN KEY (id_master) REFERENCES Evento(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- GRUPPO
CREATE TABLE IF NOT EXISTS Gruppo (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(64) NOT NULL,
    descrizione VARCHAR(255) DEFAULT NULL,
    version INT unsigned NOT NULL DEFAULT 1,
    UNIQUE (nome)
);

-- GRUPPO AULA
CREATE TABLE IF NOT EXISTS Gruppo_Aula (
    id_aula INT NOT NULL,
    id_gruppo INT NOT NULL,
    -- a che cosa serve "version" ?
    version INT unsigned NOT NULL DEFAULT 1,
    PRIMARY KEY (id_aula, id_gruppo),
    FOREIGN KEY (id_aula) REFERENCES Aula(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_gruppo) REFERENCES Gruppo(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);
