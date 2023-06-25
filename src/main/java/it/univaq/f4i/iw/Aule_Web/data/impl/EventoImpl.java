
package it.univaq.f4i.iw.Aule_Web.data.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import it.univaq.f4i.iw.Aule_Web.data.model.Aula;
import it.univaq.f4i.iw.Aule_Web.data.model.Evento;

/**
 *
 * @author Carmine
 */

public class EventoImpl implements Evento {

    private LocalDateTime data_inizio;
    private LocalDateTime data_fine;
    private String nome;
    private String descrizione;
    private String email_responsabile;
    private Aula aula;
    private TipologiaEvento tipologia_evento;
    private String nome_corso;
    private TipologiaRicorrenza tipologia_ricorrenza;
    private LocalDate data_fine_ricorrenza;

    // GET
    @Override
    public LocalDateTime getDataInizio() {
        return data_inizio;
    }

    @Override
    public LocalDateTime getDataFine() {
        return data_fine;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public String getEmailResponsabile() {
        return email_responsabile;
    }

    @Override
    public Aula getAula() {
        return aula;
    }

    @Override
    public TipologiaEvento getTipologiaEvento() {
        return tipologia_evento;
    }

    @Override
    public String getNomeCorso() {
        return nome_corso;
    }

    @Override
    public TipologiaRicorrenza getTipologiaRicorrenza() {
        return tipologia_ricorrenza;
    }

    @Override
    public LocalDate getDataFineRicorrenza() {
        return data_fine_ricorrenza;
    }

    // SET
    @Override
    public void setDataInizio(LocalDateTime data_inizio) {
        this.data_inizio = data_inizio;
    }

    @Override
    public void setDataFine(LocalDateTime data_fine) {
        this.data_fine = data_fine;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public void setEmailResponsabile(String email_responsabile) {
        this.email_responsabile = email_responsabile;
    }

    @Override
    public void setAula(Aula aula) {
        this.aula = aula;
    }

    @Override
    public void setTipologiaEvento(TipologiaEvento tipologia_evento) {
        this.tipologia_evento = tipologia_evento;
    }

    @Override
    public void setNomeCorso(String nome_corso) {
        this.nome_corso = nome_corso;
    }

    @Override
    public void setTipologiaRicorrenza(TipologiaRicorrenza tipologia_ricorrenza) {
        this.tipologia_ricorrenza = tipologia_ricorrenza;
    }

    @Override
    public void setDataFineRicorrenza(LocalDate data_fine_ricorrenza) {
        this.data_fine_ricorrenza = data_fine_ricorrenza;
    }

    // METODI

}
