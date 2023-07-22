-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Lug 21, 2023 alle 14:28
-- Versione del server: 10.4.28-MariaDB
-- Versione PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `aule_web`
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


-- -------------------------------------------------------------------- --
DROP FUNCTION IF EXISTS verifica_slot_occupati_eventi_ricorrenti$$

DELIMITER $$

CREATE FUNCTION verifica_slot_occupati_eventi_ricorrenti(
    _id_aula INT,
    _data_inizio DATETIME,
    _data_fine DATETIME,
    _tipo_ricorrenza ENUM('GIORNALIERA', 'SETTIMANALE', 'MENSILE'),
    _data_fine_ricorrenza DATE,
    _id_evento_escluso INT
    ) RETURNS INT
BEGIN
    DECLARE _id INT;
    
    IF _tipo_ricorrenza = 'GIORNALIERA' THEN
        SET _id = (SELECT id FROM Evento
                   WHERE id_aula = _id_aula
                   AND DATE(data_inizio) <= _data_fine_ricorrenza
                   AND DATE(data_fine) >= _data_inizio
                   AND (id <> _id_evento_escluso OR _id_evento_escluso IS NULL)
                   LIMIT 1);
    ELSEIF _tipo_ricorrenza = 'SETTIMANALE' THEN
        SET _id = (SELECT id FROM Evento
                   WHERE id_aula = _id_aula
                   AND WEEK(data_inizio) <= WEEK(_data_fine_ricorrenza)
                   AND WEEK(data_fine) >= WEEK(_data_inizio)
                   AND (id <> _id_evento_escluso OR _id_evento_escluso IS NULL)
                   LIMIT 1);
    ELSEIF _tipo_ricorrenza = 'MENSILE' THEN
        SET _id = (SELECT id FROM Evento
                   WHERE id_aula = _id_aula
                   AND MONTH(data_inizio) <= MONTH(_data_fine_ricorrenza)
                   AND MONTH(data_fine) >= MONTH(_data_inizio)
                   AND (id <> _id_evento_escluso OR _id_evento_escluso IS NULL)
                   LIMIT 1);
    END IF;
    
    IF _id IS NOT NULL THEN
        RETURN -1;
    ELSE
        RETURN 0;
    END IF;
END$$

DELIMITER ;

-- -------------------------------------------------------------------- --

-- procedura per l'inserimento di tutti gli eventi ricorrenti associati ad un 
-- evento tramite il suo id_master

DROP PROCEDURE IF EXISTS genera_eventi_ricorrenti_procedura;
DELIMITER $$
CREATE PROCEDURE genera_eventi_ricorrenti_procedura
(_id INTEGER, _id_aula INTEGER, _data_inizio DATETIME, _data_fine DATETIME, _tipo_ricorrenza VARCHAR(50), _data_fine_ricorrenza DATE) 
BEGIN 
	DECLARE _inizio DATETIME;
    DECLARE _fine DATETIME;
	DECLARE _id_master INT;
    
    SET _inizio = _data_inizio;
    SET _fine = _data_fine;
	SET _id_master=_id;
    
    CASE
		WHEN _tipo_ricorrenza = "GIORNALIERA" 
		THEN 
			SET _inizio = DATE_ADD(_inizio, INTERVAL 1 DAY); 
			SET _fine = DATE_ADD(_fine, INTERVAL 1 DAY);
		WHEN _tipo_ricorrenza = "SETTIMANALE"
		THEN
			SET _inizio = DATE_ADD(_inizio, INTERVAL 7 DAY); 
			SET _fine = DATE_ADD(_fine, INTERVAL 7 DAY); 
		WHEN _tipo_ricorrenza = "MENSILE"
		THEN
			SET _inizio = DATE_ADD(_inizio, INTERVAL 1 MONTH); 
			SET _fine = DATE_ADD(_fine, INTERVAL 1 MONTH);  
	END CASE;

	WHILE DATEDIFF(_inizio,_data_fine_ricorrenza) <= 0 
		DO BEGIN
			INSERT INTO evento_ricorrente
			(data_inizio, data_fine, ID_master) 
			VALUES 
			(_inizio, _fine, _id_master);  
            
			CASE
				WHEN _tipo_ricorrenza = "GIORNALIERA" 
				THEN 
					SET _inizio = DATE_ADD(_inizio, INTERVAL 1 DAY); 
					SET _fine = DATE_ADD(_fine, INTERVAL 1 DAY);
				WHEN _tipo_ricorrenza = "SETTIMANALE"
				THEN
					SET _inizio = DATE_ADD(_inizio, INTERVAL 7 DAY); 
					SET _fine = DATE_ADD(_fine, INTERVAL 7 DAY); 
				WHEN _tipo_ricorrenza = "MENSILE"
				THEN
					SET _inizio = DATE_ADD(_inizio, INTERVAL 1 MONTH); 
					SET _fine = DATE_ADD(_fine, INTERVAL 1 MONTH);  
			END CASE;
		END;
	END WHILE;

END$$
DELIMITER ;


-- -------------------------------------------------------------------- --



-- check validità data_inizio di un Evento (INSERT)
DROP TRIGGER IF EXISTS valida_data_inizio_evento_insert;
DELIMITER $$
CREATE TRIGGER valida_data_inizio_evento_insert
BEFORE INSERT ON Evento
FOR EACH ROW
BEGIN
    IF NEW.data_inizio < CURDATE() THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La data di inizio evento non può essere precedente a oggi.';
    END IF;
END $$
DELIMITER ;

-- check validità data_inizio di un Evento (UPDATE)
DROP TRIGGER IF EXISTS valida_data_inizio_evento_update;
DELIMITER $$
CREATE TRIGGER valida_data_inizio_evento_update
BEFORE UPDATE ON Evento
FOR EACH ROW
BEGIN
    IF (NEW.data_inizio < CURDATE())
    THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La data di inizio non può essere precedente a oggi.';
    END IF;
END$$
DELIMITER ;


-- ******************************************************************** --


-- check validità data_fine di un Evento (INSERT)
DROP TRIGGER IF EXISTS valida_data_fine_evento_insert;
DELIMITER $$
CREATE TRIGGER valida_data_fine_evento_insert
BEFORE INSERT ON Evento
FOR EACH ROW
BEGIN
    IF NEW.data_fine < NEW.data_inizio THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La data di fine evento non può essere precedente alla data di inizio.';
    ELSEIF DATE(NEW.data_fine) != DATE(NEW.data_inizio) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La data di fine evento deve essere nella stessa giornata della data di inizio.';
    END IF;
END $$
DELIMITER ;

-- check validità data_fine di un Evento (UPDATE)
DROP TRIGGER IF EXISTS valida_data_fine_evento_update;
DELIMITER $$
CREATE TRIGGER valida_data_fine_evento_update
BEFORE UPDATE ON Evento
FOR EACH ROW
BEGIN
    IF (DATE(NEW.data_fine) < DATE(NEW.data_inizio))
    THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La data di fine non può essere precedente alla data di inizio.';
    ELSEIF (DATE(NEW.data_fine) > DATE(NEW.data_inizio) AND DATE(NEW.data_fine) <> DATE(NEW.data_inizio))
    THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La data di fine deve essere nella stessa giornata della data di inizio.';
    END IF;
END$$
DELIMITER ;


-- ******************************************************************** --


-- check orario con intervallo di 15 minuti (INSERT)
DROP TRIGGER IF EXISTS valida_orario_evento_insert;
DELIMITER $$
CREATE TRIGGER valida_orario_evento_insert
BEFORE INSERT ON Evento
FOR EACH ROW
BEGIN
    IF MINUTE(NEW.data_inizio) % 15 != 0 OR MINUTE(NEW.data_fine) % 15 != 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L`orario di inizio e di fine evento devono essere multipli di 15 minuti.';
    END IF;
END $$
DELIMITER ;

-- check orario con intervallo di 15 minuti (UPDATE)
DROP TRIGGER IF EXISTS valida_orario_evento_update;
DELIMITER $$
CREATE TRIGGER valida_orario_evento_update
BEFORE UPDATE ON Evento
FOR EACH ROW
BEGIN
    IF MINUTE(NEW.data_inizio) % 15 != 0 OR MINUTE(NEW.data_fine) % 15 != 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L`orario di inizio e di fine evento devono essere multipli di 15 minuti.';
    END IF;
END$$
DELIMITER ;


-- ******************************************************************** --


-- check disponibilità aula per creazione di un evento (INSERT)
DROP TRIGGER IF EXISTS verifica_disponibilita_aula_insert;
DELIMITER $$
CREATE TRIGGER verifica_disponibilita_aula_insert
BEFORE INSERT ON Evento
FOR EACH ROW
BEGIN
    DECLARE count_events INT;
    
    -- Caso: Evento "semplice"
    SET count_events = (
        SELECT COUNT(*) 
        FROM Evento 
        WHERE id_aula = NEW.id_aula 
        AND (
            (NEW.data_inizio >= data_inizio AND NEW.data_inizio < data_fine) OR 
            (NEW.data_fine > data_inizio AND NEW.data_fine <= data_fine) OR 
            (NEW.data_inizio <= data_inizio AND NEW.data_fine >= data_fine)
        )
    );
    
    IF count_events > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L`aula selezionata è già occupata da un altro evento.';
    END IF;
    
    -- Caso: Eventi "ricorrenti"
    SET count_events = (
        SELECT COUNT(*) 
        FROM Evento_Ricorrente er
        INNER JOIN Evento e ON er.id_master = e.id
        WHERE e.id_aula = NEW.id_aula 
        AND (
            (NEW.data_inizio >= e.data_inizio AND NEW.data_inizio < e.data_fine) OR 
            (NEW.data_fine > e.data_inizio AND NEW.data_fine <= e.data_fine) OR 
            (NEW.data_inizio <= e.data_inizio AND NEW.data_fine >= e.data_fine)
        )
    );
    
    IF count_events > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L`aula selezionata è già occupata da un altro evento ricorrente.';
    END IF;
END $$
DELIMITER ;

-- check disponibilità aula per creazione di un evento (UPDATE)
DROP TRIGGER IF EXISTS verifica_disponibilita_aula_update;
DELIMITER $$
CREATE TRIGGER verifica_disponibilita_aula_update
BEFORE UPDATE ON Evento
FOR EACH ROW
BEGIN
    DECLARE count_events INT;
    
    -- Caso: Evento "semplice"
    SET count_events = (
        SELECT COUNT(*) 
        FROM Evento 
        WHERE id_aula = NEW.id_aula 
        AND (
            (NEW.data_inizio >= data_inizio AND NEW.data_inizio < data_fine) OR 
            (NEW.data_fine > data_inizio AND NEW.data_fine <= data_fine) OR 
            (NEW.data_inizio <= data_inizio AND NEW.data_fine >= data_fine)
        )
    );
    
    IF count_events > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L`aula selezionata è già occupata da un altro evento.';
    END IF;
    
    -- Caso: Eventi "ricorrenti"
    SET count_events = (
        SELECT COUNT(*) 
        FROM Evento_Ricorrente er
        INNER JOIN Evento e ON er.id_master = e.id
        WHERE e.id_aula = NEW.id_aula 
        AND (
            (NEW.data_inizio >= e.data_inizio AND NEW.data_inizio < e.data_fine) OR 
            (NEW.data_fine > e.data_inizio AND NEW.data_fine <= e.data_fine) OR 
            (NEW.data_inizio <= e.data_inizio AND NEW.data_fine >= e.data_fine)
        )
    );
    
    IF count_events > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L`aula selezionata è già occupata da un altro evento ricorrente.';
    END IF;
END $$
DELIMITER ;


-- ******************************************************************** --


-- check generazione eventi ricorrenti se necessario (procedura) - (INSERT)
DROP TRIGGER IF EXISTS genera_eventi_trigger_insert;
DELIMITER $$
CREATE TRIGGER genera_eventi_trigger_insert
AFTER INSERT ON Evento
FOR EACH ROW
BEGIN
    IF (NEW.tipo_ricorrenza IS NOT NULL)
    THEN
        CALL genera_eventi_ricorrenti_procedura(NEW.id, NEW.id_aula, NEW.data_inizio, NEW.data_fine, NEW.tipo_ricorrenza, NEW.data_fine_ricorrenza);
    END IF;
END$$
DELIMITER ;


-- check generazione eventi ricorrenti se necessario (procedura) - (UPDATE)
DROP TRIGGER IF EXISTS genera_eventi_trigger_update;
DELIMITER $$
CREATE TRIGGER genera_eventi_trigger_update
AFTER UPDATE ON Evento
FOR EACH ROW
BEGIN
    IF NEW.tipo_ricorrenza IS NOT NULL THEN
        CALL genera_eventi_ricorrenti_procedura(NEW.id, NEW.id_aula, NEW.data_inizio, NEW.data_fine, NEW.tipo_ricorrenza, NEW.data_fine_ricorrenza);
    END IF;
END$$
DELIMITER ;


-- ******************************************************************** --


-- check che verifica se il nome_corso è specificato per le tipologie richieste (INSERT)
DROP TRIGGER IF EXISTS check_nome_corso_insert;
DELIMITER $$
CREATE TRIGGER check_nome_corso_insert
BEFORE INSERT ON Evento
FOR EACH ROW
BEGIN
    IF NEW.tipologia IN ('LEZIONE', 'ESAME', 'PARZIALE') AND NEW.nome_corso IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Il nome del corso non può essere NULL per le tipologie LEZIONE, ESAME o PARZIALE';
    END IF;
END $$
DELIMITER ;


-- check che verifica se il nome_corso è specificato per le tipologie richieste (UPDATE)
DROP TRIGGER IF EXISTS check_nome_corso_update;
DELIMITER $$
CREATE TRIGGER check_nome_corso_update
BEFORE INSERT ON Evento
FOR EACH ROW
BEGIN
    IF NEW.tipologia IN ('LEZIONE', 'ESAME', 'PARZIALE') AND NEW.nome_corso IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Il nome del corso non può essere NULL per le tipologie LEZIONE, ESAME o PARZIALE';
    END IF;
END $$
DELIMITER ;


-- ******************************************************************** --


-- check impostazione corretta degli attributi tipo_ricorrenza e data_fine_ricorrenza (INSERT)
DROP TRIGGER IF EXISTS check_data_fine_ricorrenza_insert;
DELIMITER $$
CREATE TRIGGER check_data_fine_ricorrenza_insert 
BEFORE INSERT ON Evento
FOR EACH ROW
BEGIN
    IF NEW.tipo_ricorrenza IS NOT NULL AND NEW.data_fine_ricorrenza IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Se imposti il tipo di ricorrenza, devi impostare anche la data di fine ricorrenza';
    END IF;
END $$
DELIMITER ;

-- check impostazione corretta degli attributi tipo_ricorrenza e data_fine_ricorrenza (UPDATE)
DROP TRIGGER IF EXISTS check_data_fine_ricorrenza_update;
DELIMITER $$
CREATE TRIGGER check_data_fine_ricorrenza_update 
BEFORE INSERT ON Evento
FOR EACH ROW
BEGIN
    IF NEW.tipo_ricorrenza IS NOT NULL AND NEW.data_fine_ricorrenza IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Se imposti il tipo di ricorrenza, devi impostare anche la data di fine ricorrenza';
    END IF;
END $$
DELIMITER ;


-- ******************************************************************** --


-- check data_fine_ricorrenza successiva alla data di inizio (INSERT)
DROP TRIGGER IF EXISTS check_data_fine_ricorrenza_post_data_inizio_insert;
DELIMITER $$
CREATE TRIGGER check_data_fine_ricorrenza_post_data_inizio_insert
BEFORE INSERT ON Evento
FOR EACH ROW
BEGIN
    IF NEW.data_fine_ricorrenza IS NOT NULL AND NEW.data_fine_ricorrenza <= DATE_ADD(NEW.data_inizio, INTERVAL 1 DAY) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La data di fine ricorrenza deve essere successiva alla data di inizio di almeno un giorno';
    END IF;
END $$
DELIMITER ;

-- check data_fine_ricorrenza successiva alla data di inizio (UPDATE)
DROP TRIGGER IF EXISTS check_data_fine_ricorrenza_post_data_inizio_update;
DELIMITER $$
CREATE TRIGGER check_data_fine_ricorrenza_post_data_inizio_update
BEFORE UPDATE ON Evento
FOR EACH ROW
BEGIN
    IF NEW.data_fine_ricorrenza IS NOT NULL AND NEW.data_fine_ricorrenza <= DATE_ADD(NEW.data_inizio, INTERVAL 1 DAY) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La data di fine ricorrenza deve essere successiva alla data di inizio di almeno un giorno';
    END IF;
END $$
DELIMITER ;

-- funzione per cryptare la password dell'amministratore (INSERT)
DROP TRIGGER IF EXISTS hash_password;
DELIMITER $$
CREATE TRIGGER hash_password
BEFORE INSERT ON Amministratore
FOR EACH ROW
BEGIN 
	SET NEW.password = SHA2(NEW.password, 512);
END $$
DELIMITER ;

-- funzione per cryptare la password dell'amministratore (UPDATE)
DROP TRIGGER IF EXISTS rehash_password;
DELIMITER $$  
CREATE TRIGGER rehash_password
BEFORE UPDATE ON amministratore
FOR EACH ROW
BEGIN 
	IF(length(NEW.password) <> 128)
		THEN BEGIN 
			SET NEW.password = SHA2(NEW.password, 512);
		END;
	END IF;
END $$
DELIMITER ;

-- -------------------------------------------------------------------- --

-- amministratore
INSERT INTO `amministratore` (`id`, `nome`, `cognome`, `email`, `password`, `version`) VALUES
(1, 'Carmine', 'Pittella', 'admin@email.it', 'admin', 1);

-- gruppo
INSERT INTO `gruppo` (`id`, `nome`, `descrizione`, `version`) VALUES
(1, 'DISIM', 'Dipartimento di Ingegneria e Scienze dell`Informazione e Matematica', 1),
(2, 'DISCAB', 'Dipartimento di Scienze Cliniche Applicate e Biotecnologiche', 1),
(3, 'MESVA', 'Dipartimento di Medicina clinica, sanita pubblica, scienze della vita e dell`ambiente', 1),
(4, 'DIIIE', 'Dipartimento Di Ingegneria Industriale e dell`Informazione e di Economia', 1),
(5, 'DICEAA', 'Dipartimento di Ingegneria Civile, Edile - Architettura e Ambientale', 1),
(6, 'POLO DI COPPITO', 'Polo di coppito', 1),
(7, 'POLO DI ROIO', 'polo di roio', 1);

-- aula
INSERT INTO `aula` (`id`, `nome`, `luogo`, `edificio`, `piano`, `capienza`, `email_responsabile`, `n_prese_rete`, `n_prese_elettriche`, `note`, `id_gruppo`, `version`) VALUES
(1, 'A1.7', 'Coppito', 'Alan Turing', 1, 100, 'resp1@resp.uni.it', 20, 25, 'Finestra rotta', 1,1),
(2, 'C1.9', 'Coppito', 'Angelo Camillo De Meis', 0, 80, 'resp2@resp.uni.it', 10, 40, 'Tapparelle difettose', 1,2),
(3, 'A0.4', 'Coppito', 'Alan Turing', 0, 46, 'resp3@resp.uni.it', 8, 12, '', 1,3),
(4, 'Digital Class', 'Coppito','Alan Turing',0,20,'resp4@resp.uni.it',10,10,'',1,4),
(5, 'C1.10', 'Coppito', 'Angelo Camillo De Meis',0,126,'resp5@resp.uni.it',30,35,'Prese elettriche non funzionanti', 1,5),
(6, 'Aula Rossa', 'Coppito', 'Renato Ricamo', 1, 100, 'resp1@resp.uni.it', 88,42, '', 1,4),
(7, '1.1','Coppito','Renato Ricamo',1,24,'resp2@resp.uni.it',10,6,'',1,7),
(8, 'D4.4','Coppito','Paride Stefanini',3,145,'resp3@resp.uni.it',45,55,'Non toccare il quadro elettrico',1,1),
(9, 'D2.31','Coppito','Paride Stefanini',1,225,'resp4@resp.uni.it',85,65,'',1,2),
(10, 'D2.29','Coppito','Paride Stefanini',1,225,'resp5@resp.uni.it',70,30,'',1,3),
(11, 'A0.1','Coppito','Alan Turing',0,42,'resp1@resp.uni.it',14,18,'',1,4),
(12, 'A0.2','Coppito','Alan Turing',0,46,'resp2@resp.uni.it',16,18,'',1,5),
(13, 'A0.3','Coppito','Alan Turing',0,46,'resp3@resp.uni.it',16,18,'',1,6),
(14, 'D1.13','Coppito','Paride Stefanini',0,50,'resp4@resp.uni.it',24,22,'Lavagna non presente',1,7),
(15, 'A-0.1','Roio','Blocco A',0,40,'resp5@resp.uni.it',12,18,'',1,1),
(16, 'A-0.2','Roio','Blocco A',0,50,'resp1@resp.uni.it',22,18,'',1,2),
(17, 'A-1.1','Roio','Blocco A',1,40,'resp2@resp.uni.it',8,16,'',1,2),
(18, 'A-1.13','Roio','Blocco A',1,50,'resp3@resp.uni.it',16,14,'',1,4),
(19, 'B-0.5','Roio','Blocco B',0,27,'resp4@resp.uni.it',12,14,'',1,1),
(20, 'B-0.6','Roio','Blocco B',0,27,'resp5@resp.uni.it',10,18,'',1,3),
(21, 'B-0.7','Roio','Blocco B',0,27,'resp1@resp.uni.it',15,15,'',1,2);

-- attrezzatura
INSERT INTO `attrezzatura` (`id`, `nome_attrezzo`, `descrizione`, `version`) VALUES
(1, 'Proiettore', 'dispositivo per proiettare immagini e video', 1),
(2, 'Schermo Motorizzato', 'Simile a una televisione ma si collegano i PC', 1),
(3, 'Schermo Manuale', 'Descrizione schermo manuale', 1),
(4, 'Impianto Audio', 'Attrezzatura acustica per sentire chiaramente', 1),
(5, 'PC Fisso', 'Computer fisso collegato alla rete elettrica', 1),
(6, 'Microfono a Filo', 'Dispositivo per amplificare il suono della voce', 1),
(7, 'Microfono senza Filo', 'Dispositivo per amplificare il suono della voce', 1),
(8, 'Lavagna Luminosa', 'Lavagna multimediale touchscreen', 1),
(9, 'WiFi', 'Connessione a internet', 1);

-- evento
INSERT INTO `evento` (`id`, `data_inizio`, `data_fine`, `nome`, `descrizione`, `email_responsabile`, `id_aula`, `tipologia`, `nome_corso`, `tipo_ricorrenza`, `data_fine_ricorrenza`, `version`) VALUES
(1,'2023-07-27 14:30:00','2023-07-27 16:30:00','What is a database?',"progettazione dei database",'resp2@resp.uni.it',2,'SEMINARIO',null,null,null,1),
(2,'2023-07-28 09:00:00','2023-07-28 18:00:00','Lauree di Informatica',"prima sessione di laurea",'resp2@resp.uni.it',5,'LAUREE',null,'GIORNALIERA','2023-08-02',1),
(3,'2023-09-11 09:00:00','2023-09-11 18:00:00','Lauree di Informatica',"prima sessione di laurea",'resp3@resp.uni.it',5,'LAUREE',null,'GIORNALIERA','2023-09-13',1),
(4,'2023-12-12 09:00:00','2023-12-12 18:00:00','Lauree di Informatica',"prima sessione di laurea",'resp4@resp.uni.it',5,'LAUREE',null,'GIORNALIERA','2023-12-15',1),
(5,'2023-08-16 09:00:00','2023-08-16 11:00:00','Lauree di Ingegneria',"primo appello di laurea",'resp1@resp.uni.it',18,'LAUREE',null,'GIORNALIERA','2023-08-21',1),
(6,'2023-08-17 09:00:00','2023-08-17 11:00:00','Lauree di Medicina',"primo appello di laurea",'resp2@resp.uni.it',12,'LAUREE',null,'GIORNALIERA','2023-08-22',1);

