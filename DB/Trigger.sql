
DROP TRIGGER IF EXISTS check_evento_insert; 
DROP TRIGGER IF EXISTS check_evento_update; 
DROP TRIGGER IF EXISTS check_ricorrenza_insert; 
DROP TRIGGER IF EXISTS check_ricorrenza_update; 
DROP TRIGGER IF EXISTS check_event_time_insert; 
DROP TRIGGER IF EXISTS check_event_time_update; 
DROP TRIGGER IF EXISTS check_nome_corso_insert; 
DROP TRIGGER IF EXISTS check_nome_corso_update; 
DROP TRIGGER IF EXISTS creazione_eventi_ricorrenti_insert; 
DROP TRIGGER IF EXISTS creazione_eventi_ricorrenti_update; 

-- check accavallamento date di un Evento durante un inserimento
DELIMITER $$
CREATE TRIGGER check_evento_insert BEFORE INSERT ON Evento FOR EACH ROW
BEGIN
    IF NEW.data_fine < NEW.data_inizio THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La data di fine deve essere successiva alla data di inizio.';
    END IF;
    IF NEW.data_fine > NEW.data_inizio + INTERVAL 1 DAY THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Un evento non può durare più di un giorno.';
    END IF;
END$$
DELIMITER ;

-- check accavallamento date di un Evento durante un aggiornamento
DELIMITER $$
CREATE TRIGGER check_evento_update BEFORE UPDATE ON Evento FOR EACH ROW
BEGIN
    IF NEW.data_fine < NEW.data_inizio THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La data di fine deve essere successiva alla data di inizio.';
    END IF;
    IF NEW.data_fine > NEW.data_inizio + INTERVAL 1 DAY THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Un evento non può durare più di un giorno.';
    END IF;
END$$
DELIMITER ;


/****************************************************************/
-- check che controlla se se la data di fine ricorrenza è impostata quando necessario durante inserimento
DELIMITER $$
CREATE TRIGGER check_ricorrenza_insert BEFORE UPDATE ON Evento FOR EACH ROW
BEGIN
    IF (NEW.ricorrenza <> 'nessuna' AND NEW.data_fine_ricorrenza IS NULL) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Devi impostare una data di fine ricorrenza.';
    END IF;
    IF (NEW.ricorrenza <> 'nessuna' AND NEW.data_fine_ricorrenza <= NEW.data_fine) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La data di fine della ricorrenza deve essere successiva alla data di fine di un evento.';
    END IF;
END$$
DELIMITER ;

-- check che controlla se se la data di fine ricorrenza è impostata quando necessario durante aggiornamento
DELIMITER $$
CREATE TRIGGER check_ricorrenza_update BEFORE INSERT ON Evento FOR EACH ROW
BEGIN
    IF (NEW.ricorrenza <> 'nessuna' AND NEW.data_fine_ricorrenza IS NULL) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Devi impostare una data di fine ricorrenza.';
    END IF;
    IF (NEW.ricorrenza <> 'nessuna' AND NEW.data_fine_ricorrenza <= NEW.data_fine) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La data di fine della ricorrenza deve essere successiva alla data di fine di un evento.';
    END IF;
END$$
DELIMITER ;


/****************************************************************/
-- check orari inizio e fine con scarti di 15 minuti durante inserimento
DELIMITER $$
CREATE TRIGGER check_event_time_insert BEFORE INSERT ON Evento FOR EACH ROW
BEGIN
    IF MINUTE(NEW.data_inizio) NOT IN (0, 15, 30, 45) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'L`orario di inizio evento deve avere uno scarto di 15 minuti.';
    END IF;
    IF MINUTE(NEW.data_fine) NOT IN (0, 15, 30, 45) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'L`orario di fine evento deve avere uno scarto di 15 minuti.';
    END IF;
END$$
DELIMITER ;

-- check orari inizio e fine con scarti di 15 minuti durante aggiornamento
DELIMITER $$
CREATE TRIGGER check_event_time_update BEFORE UPDATE ON Evento FOR EACH ROW
BEGIN
    IF MINUTE(NEW.data_inizio) NOT IN (0, 15, 30, 45) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'L`orario di inizio evento deve avere uno scarto di 15 minuti.';
    END IF;
    IF MINUTE(NEW.data_fine) NOT IN (0, 15, 30, 45) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'L`orario di fine evento deve avere uno scarto di 15 minuti.';
    END IF;
END$$
DELIMITER ;


/****************************************************************/
-- check che verifica se il nome_corso è specificato per le tipologie richiesta durante inserimento
DELIMITER $$
CREATE TRIGGER check_nome_corso_insert BEFORE INSERT ON Tipologia_evento FOR EACH ROW
BEGIN
    IF (NEW.tipologia = 'lezione' OR NEW.tipologia = 'esame' OR NEW.tipologia = 'parziale') AND NEW.nome_corso IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Per le tipologie (lezione, esame, parziale) devi inserire il nome del corso.';
    END IF;
END$$
DELIMITER ;

-- check che verifica se il nome_corso è specificato per le tipologie richiesta durante aggiornamento
DELIMITER $$
CREATE TRIGGER check_nome_corso_update BEFORE UPDATE ON Tipologia_evento FOR EACH ROW
BEGIN
    IF (NEW.tipologia = 'lezione' OR NEW.tipologia = 'esame' OR NEW.tipologia = 'parziale') AND NEW.nome_corso IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Per le tipologie (lezione, esame, parziale) devi inserire il nome del corso.';
    END IF;
END$$
DELIMITER ;


/****************************************************************/
-- creazione degli eventi ricorrenti fino alla data specificata dopo un inserimento
DELIMITER $$
CREATE TRIGGER creazione_eventi_ricorrenti_insert AFTER INSERT ON Evento FOR EACH ROW
BEGIN
	DECLARE id_evento INT;
	DECLARE id_master INT;
	DECLARE start_date DATETIME;
    DECLARE end_date DATETIME;
    DECLARE interval_value INT;
	IF NEW.ricorrenza IN ('giornaliera', 'settimanale', 'mensile') THEN
		SET id_evento = NEW.ID;
		SET id_master = NEW.id_master;
		SET start_date = NEW.data_inizio;
		SET end_date = NEW.data_fine;
		IF NEW.ricorrenza = 'giornaliera' THEN
			SET interval_value = 1;
		ELSEIF NEW.ricorrenza = 'settimanale' THEN
			SET interval_value = 7;
		ELSE -- mensile
			SET interval_value = 30;
    END IF;
    WHILE start_date <= NEW.data_fine_ricorrenza DO
		SET start_date = DATE_ADD(start_date, INTERVAL interval_value DAY);
        SET end_date = DATE_ADD(end_date, INTERVAL interval_value DAY);
        INSERT INTO Evento_Ricorrente (id_evento, id_master, data_inizio, data_fine) VALUES 
			(id_evento, id_master, start_date, end_date);
    END WHILE;
  END IF;
END$$
DELIMITER ;

-- creazione degli eventi ricorrenti fino alla data specificata dopo un aggiornamento
DELIMITER $$
CREATE TRIGGER creazione_eventi_ricorrenti_update AFTER UPDATE ON Evento FOR EACH ROW
BEGIN
	DECLARE id_evento INT;
	DECLARE id_master INT;
	DECLARE start_date DATETIME;
    DECLARE end_date DATETIME;
    DECLARE interval_value INT;
	IF OLD.ricorrenza IN ('giornaliera', 'settimanale', 'mensile') THEN
		SET id_evento = OLD.ID;
		SET id_master = OLD.id_master;
		SET start_date = OLD.data_inizio;
		SET end_date = OLD.data_fine;
		IF OLD.ricorrenza = 'giornaliera' THEN
			SET interval_value = 1;
		ELSEIF OLD.ricorrenza = 'settimanale' THEN
			SET interval_value = 7;
		ELSE -- mensile
			SET interval_value = 30;
    END IF;
    WHILE start_date <= OLD.data_fine_ricorrenza DO
		SET start_date = DATE_ADD(start_date, INTERVAL interval_value DAY);
        SET end_date = DATE_ADD(end_date, INTERVAL interval_value DAY);
        IF NOT EXISTS (SELECT * FROM Evento_Ricorrente WHERE id_evento = OLD.ID AND id_master = OLD.id_master AND data_inizio = start_date AND data_fine = end_date) THEN
			INSERT INTO Evento_Ricorrente (id_evento, id_master, data_inizio, data_fine) VALUES 
				(id_evento, id_master, start_date, end_date);
		END IF;
    END WHILE;
  END IF;
END$$
DELIMITER ;
/****************************************************************/






-- -----------------------------------------------------------------


