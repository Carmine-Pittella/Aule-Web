

-- ************************* INSERT TRIGGER ************************* --
-- *************************      START     ************************* --

-- check validità data_inizio di un Evento (INSERT)
DELIMITER $$
DROP TRIGGER IF EXISTS valida_data_inizio_evento_insert;
CREATE TRIGGER valida_data_inizio_evento_insert
BEFORE INSERT ON Evento
FOR EACH ROW
BEGIN
    IF NEW.data_inizio < CURDATE() THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La data di inizio evento non può essere precedente a oggi.';
    END IF;
END $$
DELIMITER ;



-- check validità data_fine di un Evento (INSERT)
DELIMITER $$
DROP TRIGGER IF EXISTS valida_data_fine_evento_insert;
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



-- check orario con intervallo di 15 minuti (INSERT)
DELIMITER $$
DROP TRIGGER IF EXISTS valida_orario_evento_insert;
CREATE TRIGGER valida_orario_evento_insert
BEFORE INSERT ON Evento
FOR EACH ROW
BEGIN
    IF MINUTE(NEW.data_inizio) % 15 != 0 OR MINUTE(NEW.data_fine) % 15 != 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'L`orario di inizio e di fine evento devono essere multipli di 15 minuti.';
    END IF;
END $$
DELIMITER ;



-- check disponibilità aula per creazione di un evento (INSERT)
DELIMITER $$
DROP TRIGGER IF EXISTS verifica_disponibilita_aula_insert;
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

-- ************************* INSERT TRIGGER ************************* --
-- *************************       END      ************************* --


-- ------------------------------------------------------------------ --


-- ************************* CALL PROCEDURE ************************* --
-- *************************      START     ************************* --

-- check generazione eventi ricorrenti se necessario (funzione) - (INSERT)
DROP TRIGGER IF EXISTS genera_eventi_trigger_insert;
DELIMITER $$
CREATE TRIGGER genera_eventi_trigger_insert
AFTER INSERT ON Evento
FOR EACH ROW
BEGIN
    IF (NEW.tipo_ricorrenza IS NOT NULL)
    THEN
        CALL genera_eventi(NEW.id, NEW.id_aula, NEW.data_inizio, NEW.data_fine, NEW.tipo_ricorrenza, NEW.data_fine_ricorrenza);
    END IF;
END$$
DELIMITER ;


-- check generazione eventi ricorrenti se necessario (funzione) - (UPDATE)
DROP TRIGGER IF EXISTS genera_eventi_trigger_update;
DELIMITER $$
CREATE TRIGGER genera_eventi_trigger_update
AFTER UPDATE ON Evento
FOR EACH ROW
BEGIN
    IF NEW.tipo_ricorrenza IS NOT NULL THEN
        -- Chiamata alla procedura per generare gli eventi ricorrenti
        CALL genera_eventi(NEW.ID, NEW.ID_aula, NEW.data_inizio, NEW.data_fine, NEW.tipo_ricorrenza, NEW.data_fine_ricorrenza);
    END IF;
END$$
DELIMITER ;


-- ************************* CALL PROCEDURE ************************* --
-- *************************       END      ************************* --


-- ------------------------------------------------------------------ --


-- ************************* UPDATE TRIGGER ************************* --
-- *************************      START     ************************* --

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



-- check disponibilità aula per creazione di un evento (UPDATE)
DELIMITER $$
DROP TRIGGER IF EXISTS verifica_disponibilita_aula_update;
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


-- ************************* UPDATE TRIGGER ************************* --
-- *************************       END      ************************* --