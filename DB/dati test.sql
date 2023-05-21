-- Inserimento dati nella tabella Gruppo
INSERT INTO Gruppo (nome, descrizione) VALUES
('Gruppo1', 'Descrizione del gruppo 1'),
('Gruppo2', 'Descrizione del gruppo 2'),
('Gruppo3', 'Descrizione del gruppo 3');

-- Inserimento dati nella tabella Attrezzatura
INSERT INTO Attrezzatura (nome) VALUES
('Proiettore'),
('Lavagna'),
('Computer');

-- Inserimento dati nella tabella Tipologia_evento
INSERT INTO Tipologia_evento (tipologia, nome_corso) VALUES
('lezione', 'Corso1'),
('esame', 'Esame1'),
('seminario', 'Seminario1');

-- Inserimento dati nella tabella Aula
INSERT INTO Aula (nome, n_prese_elettriche, n_prese_rete, capienza, email_responsabile, note, luogo, piano, edificio, gruppo) VALUES
('Aula1', 20, 10, 50, 'email1@test.com', NULL, 'Piano1', 1, 'Edificio1', 1),
('Aula2', 10, 5, 30, 'email2@test.com', NULL, 'Piano2', 2, 'Edificio1', 2),
('Aula3', 30, 15, 80, 'email3@test.com', NULL, 'Piano1', 1, 'Edificio2', 3);

-- Inserimento dati nella tabella Evento
INSERT INTO Evento (nome, descrizione, email_responsabile, data_inizio, data_fine, ricorrenza, data_fine_ricorrenza, id_master, aula, tipologia_evento) VALUES
('Evento1', 'Descrizione evento 1', 'email1@test.com', '2023-06-01 10:00:00', '2023-06-01 13:00:00', 'nessuna', NULL, NULL, 1, 1),
('Evento2', 'Descrizione evento 2', 'email2@test.com', '2023-06-02 15:00:00', '2023-06-02 16:00:00', 'giornaliera', '2023-06-03', 1, 2, 2),
('Evento3', 'Descrizione evento 3', 'email3@test.com', '2023-06-03 14:00:00', '2023-06-03 18:00:00', 'settimanale', '2023-06-30', 2, 3, 3);

-- Inserimento dati nella tabella svolge
INSERT INTO svolge (id_aula, id_evento) VALUES
(1, 1),
(2, 2),
(3, 3);

-- Inserimento dati nella tabella corrisponde
INSERT INTO corrisponde (id_evento, id_tipologia_evento) VALUES
(1, 1),
(2, 2),
(3, 3);

-- Inserimento dati nella tabella appartiene
INSERT INTO appartiene (id_gruppo, id_aula) VALUES
(1, 1),
(2, 2),
(3, 3);

-- Inserimento dati nella tabella contiene
INSERT INTO contiene (id_attrezzatura, id_aula) VALUES
(1, 1),
(1, 2),
(2, 3);

