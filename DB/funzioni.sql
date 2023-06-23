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
