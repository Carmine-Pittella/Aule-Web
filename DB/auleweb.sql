--
-- Database: auleweb
--
DROP DATABASE IF EXISTS aule_web;
CREATE DATABASE aule_web;

DROP USER IF EXISTS 'aule_website'@'localhost';
CREATE USER 'aule_website'@'localhost' IDENTIFIED BY 'aule_webpass';
GRANT ALL ON `aule_web`.* TO 'aule_website'@'localhost';

USE aule_web;


SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


-- Drop Tabelle
DROP TABLE IF EXISTS Amministratore;
DROP TABLE IF EXISTS Gruppo;
DROP TABLE IF EXISTS Aula;
DROP TABLE IF EXISTS Attrezzatura;
DROP TABLE IF EXISTS Attrezzatura_Relazione;
DROP TABLE IF EXISTS Evento;
DROP TABLE IF EXISTS Evento_Ricorrente;



-- ADMIN
CREATE TABLE IF NOT EXISTS Amministratore (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(64) NOT NULL,
    cognome VARCHAR(64) NOT NULL,
    email VARCHAR(128) NOT NULL,
    password VARCHAR(128) NOT NULL,
    version INT UNSIGNED NOT NULL DEFAULT 1,
    UNIQUE (email)
);

-- GRUPPO
CREATE TABLE IF NOT EXISTS Gruppo (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(64) NOT NULL,
    descrizione VARCHAR(255) DEFAULT NULL,
    version INT UNSIGNED NOT NULL DEFAULT 1,
    UNIQUE (nome)
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
    n_prese_rete INT NOT NULL,
    n_prese_elettriche INT NOT NULL,
    note VARCHAR(255) NOT NULL,
    id_gruppo INT,
    version INT UNSIGNED NOT NULL DEFAULT 1,
    UNIQUE (nome, luogo, edificio, piano),
    FOREIGN KEY (id_gruppo) REFERENCES Gruppo(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ATTREZZATURA
CREATE TABLE IF NOT EXISTS Attrezzatura (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome_attrezzo VARCHAR(50) NOT NULL,
    descrizione VARCHAR(500) NOT NULL,
    version INT UNSIGNED NOT NULL DEFAULT 1,
    UNIQUE (nome_attrezzo)
);

-- ATTREZZATURA_RELAZIONE
CREATE TABLE IF NOT EXISTS Attrezzatura_Relazione (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_aula INT NOT NULL,
    id_attrezzo INT NOT NULL,
    quantita INT NOT NULL,
    version INT UNSIGNED NOT NULL DEFAULT 1,
    UNIQUE (id_aula, id_attrezzo),
    FOREIGN KEY (id_aula) REFERENCES Aula(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_attrezzo) REFERENCES Attrezzatura(id)
        ON DELETE CASCADE ON UPDATE CASCADE
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
    version INT UNSIGNED NOT NULL DEFAULT 1,
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
    version INT UNSIGNED NOT NULL DEFAULT 1,
    FOREIGN KEY (id_master) REFERENCES Evento(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);



