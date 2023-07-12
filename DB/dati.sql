
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


-- -- attrezzatura relazione



-- evento
INSERT INTO `evento` (`id`, `data_inizio`, `data_fine`, `nome`, `descrizione`, `email_responsabile`, `id_aula`, `tipologia`, `nome_corso`, `tipo_ricorrenza`, `data_fine_ricorrenza`, `version`) VALUES
(1,'2023-07-13 14:30:00','2023-07-13 16:30:00','What is a database?',"progettazione dei database",'resp2@resp.uni.it',2,'SEMINARIO',null,null,null,1),
(2,'2023-07-15 09:00:00','2023-07-15 18:00:00','Lauree di Informatica',"prima sessione di laurea",'resp2@resp.uni.it',5,'LAUREE',null,'GIORNALIERA','2023-07-20',1),
(3,'2023-09-11 09:00:00','2023-09-11 18:00:00','Lauree di Informatica',"prima sessione di laurea",'resp3@resp.uni.it',5,'LAUREE',null,'GIORNALIERA','2023-09-13',1),
(4,'2023-12-12 09:00:00','2023-12-12 18:00:00','Lauree di Informatica',"prima sessione di laurea",'resp4@resp.uni.it',5,'LAUREE',null,'GIORNALIERA','2023-12-15',1),
(5,'2023-07-16 09:00:00','2023-07-16 11:00:00','Lauree di Ingegneria',"primo appello di laurea",'resp1@resp.uni.it',18,'LAUREE',null,'GIORNALIERA','2023-07-21',1),
(6,'2023-07-17 09:00:00','2023-07-17 11:00:00','Lauree di Medicina',"primo appello di laurea",'resp2@resp.uni.it',12,'LAUREE',null,'GIORNALIERA','2023-07-22',1);



-- evento ricorrente

