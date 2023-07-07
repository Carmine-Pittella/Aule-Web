
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
