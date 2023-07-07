-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Giu 28, 2023 alle 17:35
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

DELIMITER $$
--
-- Procedure
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `genera_eventi_ricorrenti_procedura` (`_id` INTEGER, `_id_aula` INTEGER, `_data_inizio` DATETIME, `_data_fine` DATETIME, `_tipo_ricorrenza` VARCHAR(50), `_data_fine_ricorrenza` DATE)   BEGIN 
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

--
-- Funzioni
--
CREATE DEFINER=`root`@`localhost` FUNCTION `verifica_slot_occupati_eventi_ricorrenti` (`_id_aula` INT, `_data_inizio` DATETIME, `_data_fine` DATETIME, `_tipo_ricorrenza` ENUM('GIORNALIERA','SETTIMANALE','MENSILE'), `_data_fine_ricorrenza` DATE, `_id_evento_escluso` INT) RETURNS INT(11)  BEGIN
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

-- --------------------------------------------------------

--
-- Struttura della tabella `amministratore`
--

CREATE TABLE `amministratore` (
  `id` int(11) NOT NULL,
  `nome` varchar(64) NOT NULL,
  `cognome` varchar(64) NOT NULL,
  `email` varchar(128) NOT NULL,
  `password` varchar(64) NOT NULL,
  `version` int(10) UNSIGNED NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `attrezzatura`
--

CREATE TABLE `attrezzatura` (
  `id` int(11) NOT NULL,
  `nome_attrezzo` varchar(50) NOT NULL,
  `descrizione` varchar(500) NOT NULL,
  `version` int(10) UNSIGNED NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `attrezzatura_relazione`
--

CREATE TABLE `attrezzatura_relazione` (
  `id` int(11) NOT NULL,
  `id_aula` int(11) NOT NULL,
  `id_attrezzo` int(11) NOT NULL,
  `quantita` int(11) NOT NULL,
  `version` int(10) UNSIGNED NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `aula`
--

CREATE TABLE `aula` (
  `id` int(11) NOT NULL,
  `nome` varchar(64) NOT NULL,
  `luogo` varchar(64) NOT NULL,
  `edificio` varchar(64) NOT NULL,
  `piano` int(11) NOT NULL,
  `capienza` int(11) NOT NULL,
  `email_responsabile` varchar(64) NOT NULL,
  `n_prese_rete` int(11) NOT NULL,
  `n_prese_elettriche` int(11) NOT NULL,
  `note` varchar(255) NOT NULL,
  `id_gruppo` int(11) NOT NULL,
  `version` int(10) UNSIGNED NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `evento`
--

CREATE TABLE `evento` (
  `id` int(11) NOT NULL,
  `data_inizio` datetime NOT NULL,
  `data_fine` datetime NOT NULL,
  `nome` varchar(64) NOT NULL,
  `descrizione` varchar(500) NOT NULL,
  `email_responsabile` varchar(64) NOT NULL,
  `id_aula` int(11) NOT NULL,
  `tipologia` enum('LEZIONE','ESAME','SEMINARIO','PARZIALE','RIUNIONE','LAUREE','ALTRO') NOT NULL,
  `nome_corso` varchar(50) DEFAULT NULL,
  `tipo_ricorrenza` enum('GIORNALIERA','SETTIMANALE','MENSILE') DEFAULT NULL,
  `data_fine_ricorrenza` date DEFAULT NULL,
  `version` int(10) UNSIGNED NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Trigger `evento`
--
DELIMITER $$
CREATE TRIGGER `check_data_fine_ricorrenza_insert` BEFORE INSERT ON `evento` FOR EACH ROW BEGIN
    IF NEW.tipo_ricorrenza IS NOT NULL AND NEW.data_fine_ricorrenza IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Se imposti il tipo di ricorrenza, devi impostare anche la data di fine ricorrenza';
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `check_data_fine_ricorrenza_post_data_inizio_insert` BEFORE INSERT ON `evento` FOR EACH ROW BEGIN
    IF NEW.data_fine_ricorrenza IS NOT NULL AND NEW.data_fine_ricorrenza <= DATE_ADD(NEW.data_inizio, INTERVAL 1 DAY) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La data di fine ricorrenza deve essere successiva alla data di inizio di almeno un giorno';
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `check_data_fine_ricorrenza_post_data_inizio_update` BEFORE UPDATE ON `evento` FOR EACH ROW BEGIN
    IF NEW.data_fine_ricorrenza IS NOT NULL AND NEW.data_fine_ricorrenza <= DATE_ADD(NEW.data_inizio, INTERVAL 1 DAY) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La data di fine ricorrenza deve essere successiva alla data di inizio di almeno un giorno';
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `check_data_fine_ricorrenza_update` BEFORE INSERT ON `evento` FOR EACH ROW BEGIN
    IF NEW.tipo_ricorrenza IS NOT NULL AND NEW.data_fine_ricorrenza IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Se imposti il tipo di ricorrenza, devi impostare anche la data di fine ricorrenza';
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `check_nome_corso_insert` BEFORE INSERT ON `evento` FOR EACH ROW BEGIN
    IF NEW.tipologia IN ('LEZIONE', 'ESAME', 'PARZIALE') AND NEW.nome_corso IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Il nome del corso non può essere NULL per le tipologie LEZIONE, ESAME o PARZIALE';
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `check_nome_corso_update` BEFORE INSERT ON `evento` FOR EACH ROW BEGIN
    IF NEW.tipologia IN ('LEZIONE', 'ESAME', 'PARZIALE') AND NEW.nome_corso IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Il nome del corso non può essere NULL per le tipologie LEZIONE, ESAME o PARZIALE';
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `genera_eventi_trigger_insert` AFTER INSERT ON `evento` FOR EACH ROW BEGIN
    IF (NEW.tipo_ricorrenza IS NOT NULL)
    THEN
        CALL genera_eventi_ricorrenti_procedura(NEW.id, NEW.id_aula, NEW.data_inizio, NEW.data_fine, NEW.tipo_ricorrenza, NEW.data_fine_ricorrenza);
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `genera_eventi_trigger_update` AFTER UPDATE ON `evento` FOR EACH ROW BEGIN
    IF NEW.tipo_ricorrenza IS NOT NULL THEN
        CALL genera_eventi_ricorrenti_procedura(NEW.id, NEW.id_aula, NEW.data_inizio, NEW.data_fine, NEW.tipo_ricorrenza, NEW.data_fine_ricorrenza);
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `valida_data_fine_evento_insert` BEFORE INSERT ON `evento` FOR EACH ROW BEGIN
    IF NEW.data_fine < NEW.data_inizio THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La data di fine evento non può essere precedente alla data di inizio.';
    ELSEIF DATE(NEW.data_fine) != DATE(NEW.data_inizio) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La data di fine evento deve essere nella stessa giornata della data di inizio.';
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `valida_data_fine_evento_update` BEFORE UPDATE ON `evento` FOR EACH ROW BEGIN
    IF (DATE(NEW.data_fine) < DATE(NEW.data_inizio))
    THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La data di fine non può essere precedente alla data di inizio.';
    ELSEIF (DATE(NEW.data_fine) > DATE(NEW.data_inizio) AND DATE(NEW.data_fine) <> DATE(NEW.data_inizio))
    THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La data di fine deve essere nella stessa giornata della data di inizio.';
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `valida_data_inizio_evento_insert` BEFORE INSERT ON `evento` FOR EACH ROW BEGIN
    IF NEW.data_inizio < CURDATE() THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La data di inizio evento non può essere precedente a oggi.';
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `valida_data_inizio_evento_update` BEFORE UPDATE ON `evento` FOR EACH ROW BEGIN
    IF (NEW.data_inizio < CURDATE())
    THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La data di inizio non può essere precedente a oggi.';
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `valida_orario_evento_insert` BEFORE INSERT ON `evento` FOR EACH ROW BEGIN
    IF MINUTE(NEW.data_inizio) % 15 != 0 OR MINUTE(NEW.data_fine) % 15 != 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L`orario di inizio e di fine evento devono essere multipli di 15 minuti.';
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `valida_orario_evento_update` BEFORE UPDATE ON `evento` FOR EACH ROW BEGIN
    IF MINUTE(NEW.data_inizio) % 15 != 0 OR MINUTE(NEW.data_fine) % 15 != 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L`orario di inizio e di fine evento devono essere multipli di 15 minuti.';
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `verifica_disponibilita_aula_insert` BEFORE INSERT ON `evento` FOR EACH ROW BEGIN
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
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `verifica_disponibilita_aula_update` BEFORE UPDATE ON `evento` FOR EACH ROW BEGIN
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
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Struttura della tabella `evento_ricorrente`
--

CREATE TABLE `evento_ricorrente` (
  `id` int(11) NOT NULL,
  `data_inizio` datetime NOT NULL,
  `data_fine` datetime NOT NULL,
  `id_master` int(11) NOT NULL,
  `version` int(10) UNSIGNED NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `gruppo`
--

CREATE TABLE `gruppo` (
  `id` int(11) NOT NULL,
  `nome` varchar(64) NOT NULL,
  `descrizione` varchar(255) DEFAULT NULL,
  `version` int(10) UNSIGNED NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `amministratore`
--
ALTER TABLE `amministratore`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indici per le tabelle `attrezzatura`
--
ALTER TABLE `attrezzatura`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nome_attrezzo` (`nome_attrezzo`);

--
-- Indici per le tabelle `attrezzatura_relazione`
--
ALTER TABLE `attrezzatura_relazione`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_aula` (`id_aula`,`id_attrezzo`),
  ADD KEY `id_attrezzo` (`id_attrezzo`);

--
-- Indici per le tabelle `aula`
--
ALTER TABLE `aula`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nome` (`nome`,`luogo`,`edificio`,`piano`),
  ADD KEY `id_gruppo` (`id_gruppo`);

--
-- Indici per le tabelle `evento`
--
ALTER TABLE `evento`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `data_inizio` (`data_inizio`,`data_fine`,`nome`,`id_aula`),
  ADD KEY `id_aula` (`id_aula`);

--
-- Indici per le tabelle `evento_ricorrente`
--
ALTER TABLE `evento_ricorrente`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_master` (`id_master`);

--
-- Indici per le tabelle `gruppo`
--
ALTER TABLE `gruppo`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nome` (`nome`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `amministratore`
--
ALTER TABLE `amministratore`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `attrezzatura`
--
ALTER TABLE `attrezzatura`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `attrezzatura_relazione`
--
ALTER TABLE `attrezzatura_relazione`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `aula`
--
ALTER TABLE `aula`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `evento`
--
ALTER TABLE `evento`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `evento_ricorrente`
--
ALTER TABLE `evento_ricorrente`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `gruppo`
--
ALTER TABLE `gruppo`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `attrezzatura_relazione`
--
ALTER TABLE `attrezzatura_relazione`
  ADD CONSTRAINT `attrezzatura_relazione_ibfk_1` FOREIGN KEY (`id_aula`) REFERENCES `aula` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `attrezzatura_relazione_ibfk_2` FOREIGN KEY (`id_attrezzo`) REFERENCES `attrezzatura` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `aula`
--
ALTER TABLE `aula`
  ADD CONSTRAINT `aula_ibfk_1` FOREIGN KEY (`id_gruppo`) REFERENCES `gruppo` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `evento`
--
ALTER TABLE `evento`
  ADD CONSTRAINT `evento_ibfk_1` FOREIGN KEY (`id_aula`) REFERENCES `aula` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `evento_ricorrente`
--
ALTER TABLE `evento_ricorrente`
  ADD CONSTRAINT `evento_ricorrente_ibfk_1` FOREIGN KEY (`id_master`) REFERENCES `evento` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
