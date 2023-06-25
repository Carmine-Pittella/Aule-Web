/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.Aule_Web.data.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import it.univaq.f4i.iw.Aule_Web.data.impl.TipologiaEvento;
import it.univaq.f4i.iw.Aule_Web.data.impl.TipologiaRicorrenza;

/**
 *
 * @author Carmine
 */

public interface Evento {

    // GET
    LocalDateTime getDataInizio();

    LocalDateTime getDataFine();

    String getNome();

    String getDescrizione();

    String getEmailResponsabile();

    Aula getAula();

    TipologiaEvento getTipologiaEvento();

    String getNomeCorso();

    TipologiaRicorrenza getTipologiaRicorrenza();

    LocalDate getDataFineRicorrenza();

    // SET
    void setDataInizio(LocalDateTime data_inizio);

    void setDataFine(LocalDateTime data_fine);

    void setNome(String nome);

    void setDescrizione(String descrizione);

    void setEmailResponsabile(String email_responsabile);

    void setAula(Aula aula);

    void setTipologiaEvento(TipologiaEvento tipologia_evento);

    void setNomeCorso(String nome_corso);

    void setTipologiaRicorrenza(TipologiaRicorrenza tipologia_ricorrenza);

    void setDataFineRicorrenza(LocalDate data_fine_ricorrenza);

    // METODI

}
