UNLOCK tables;
DROP DATABASE IF EXISTS AuleWeb;
CREATE DATABASE IF NOT EXISTS AuleWeb;
USE AuleWeb;

DROP TABLE IF EXISTS Aula;
DROP TABLE IF EXISTS Evento;
DROP TABLE IF EXISTS Attrezzatura;
DROP TABLE IF EXISTS Gruppo;
DROP TABLE IF EXISTS Evento_Ricorrente;
DROP TABLE IF EXISTS Tipologia_evento;
DROP TABLE IF EXISTS svolge;
DROP TABLE IF EXISTS corrisponde;
DROP TABLE IF EXISTS appartiene;
DROP TABLE IF EXISTS contiene;
DROP TABLE IF EXISTS associato;

/********** TABELLE **********/
CREATE TABLE IF NOT EXISTS Attrezzatura (
	ID INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS Gruppo (
	ID INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL UNIQUE,
	descrizione VARCHAR(255),
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS Tipologia_evento (
	ID INT NOT NULL AUTO_INCREMENT,
    tipologia ENUM('lezione', 'esame', 'seminario', 'parziale', 'riunione', 'lauree', 'altro') DEFAULT 'altro',
    nome_corso VARCHAR(50) UNIQUE,
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS Aula (
	ID INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
	n_prese_elettriche INT NOT NULL,
	n_prese_rete INT NOT NULL,
	capienza INT NOT NULL,
	email_responsabile VARCHAR(255) NOT NULL,
	note VARCHAR(255),
	luogo VARCHAR(50) NOT NULL,
	piano INT NOT NULL,
	edificio VARCHAR(50) NOT NULL,
    gruppo INT NOT NULL,
    
    UNIQUE (nome, luogo, piano, edificio, gruppo),
	PRIMARY KEY (ID),
    FOREIGN KEY (gruppo) REFERENCES Gruppo(ID) 
		ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Evento (
	ID INT NOT NULL AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
	descrizione TEXT,
	email_responsabile VARCHAR(255) NOT NULL,
	data_inizio DATETIME NOT NULL,
	data_fine DATETIME NOT NULL,
	ricorrenza ENUM('nessuna', 'giornaliera', 'settimanale', 'mensile') DEFAULT 'nessuna',
	data_fine_ricorrenza DATE,
	id_master INT,
    aula INT NOT NULL,
    tipologia_evento INT NOT NULL,
    
	PRIMARY KEY (ID),
    FOREIGN KEY (aula) REFERENCES Aula(ID) 
		ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (tipologia_evento) REFERENCES Tipologia_evento(ID) 
		ON DELETE RESTRICT ON UPDATE RESTRICT
  );
  
  CREATE TABLE IF NOT EXISTS Evento_Ricorrente (
	id_evento INT NOT NULL,
	id_master INT NOT NULL,
    data_inizio DATETIME NOT NULL,
	data_fine DATETIME NOT NULL,
    PRIMARY KEY (id_master, data_inizio, data_fine),
	FOREIGN KEY (id_evento) REFERENCES Evento(ID)
		ON DELETE RESTRICT ON UPDATE CASCADE
		
  );

/********** RELAZIONI **********/ -- da qui in poi devi ancora finire il lavoro.
CREATE TABLE IF NOT EXISTS svolge (
	id_aula INT NOT NULL,
	id_evento INT NOT NULL,
	PRIMARY KEY (id_aula, id_evento),
	FOREIGN KEY (id_aula) REFERENCES Aula(ID) 
		ON DELETE RESTRICT ON UPDATE CASCADE,
	FOREIGN KEY (id_evento) REFERENCES Evento(ID) 
		ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS corrisponde (
    id_evento INT NOT NULL,
    id_tipologia_evento INT NOT NULL,
    PRIMARY KEY (id_evento, id_tipologia_evento),
    FOREIGN KEY (id_evento) REFERENCES Evento(ID) 
		ON DELETE NO ACTION ON UPDATE CASCADE,
    FOREIGN KEY (id_tipologia_evento) REFERENCES Tipologia_evento(ID)
		ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE IF NOT EXISTS appartiene (
    id_gruppo INT NOT NULL,
    id_aula INT NOT NULL,
    PRIMARY KEY (id_gruppo, id_aula),
    FOREIGN KEY (id_gruppo) REFERENCES Gruppo(ID)
		ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (id_aula) REFERENCES Aula(ID)
		ON DELETE NO ACTION ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS contiene (
    id_attrezzatura INT NOT NULL,
    id_aula INT NOT NULL,
    PRIMARY KEY (id_attrezzatura, id_aula),
    FOREIGN KEY (id_attrezzatura) REFERENCES Attrezzatura(ID)
		ON DELETE RESTRICT ON UPDATE RESTRICT,
    FOREIGN KEY (id_aula) REFERENCES Aula(ID)
		ON DELETE NO ACTION ON UPDATE CASCADE
);
