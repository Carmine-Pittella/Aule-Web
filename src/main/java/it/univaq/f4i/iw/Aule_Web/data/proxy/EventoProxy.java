
package it.univaq.f4i.iw.Aule_Web.data.proxy;

import java.time.LocalDate;
import java.time.LocalDateTime;

import it.univaq.f4i.iw.Aule_Web.data.impl.EventoImpl;
import it.univaq.f4i.iw.Aule_Web.data.impl.TipologiaEvento;
import it.univaq.f4i.iw.Aule_Web.data.impl.TipologiaRicorrenza;
import it.univaq.f4i.iw.Aule_Web.data.model.Aula;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;

/**
 *
 * @author Carmine
 */

public class EventoProxy extends EventoImpl implements DataItemProxy {

    protected boolean modified;
    protected DataLayer dataLayer;
    protected int aula_key = 0;

    public EventoProxy(DataLayer d) {
        super();
        this.dataLayer = d;
        this.modified = false;
    }

    @Override
    public void setKey(Integer key) {
        super.setKey(key);
    }

    /* ********************************************************* */

    // GET
    @Override
    public Aula getAula() {
        return super.getAula();
    }

    @Override
    public LocalDateTime getDataFine() {
        return super.getDataFine();
    }

    @Override
    public LocalDate getDataFineRicorrenza() {
        return super.getDataFineRicorrenza();
    }

    @Override
    public LocalDateTime getDataInizio() {
        return super.getDataInizio();
    }

    @Override
    public String getDescrizione() {
        return super.getDescrizione();
    }

    @Override
    public String getEmailResponsabile() {
        return super.getEmailResponsabile();
    }

    @Override
    public String getNome() {
        return super.getNome();
    }

    @Override
    public String getNomeCorso() {
        return super.getNomeCorso();
    }

    @Override
    public TipologiaEvento getTipologiaEvento() {
        return super.getTipologiaEvento();
    }

    @Override
    public TipologiaRicorrenza getTipologiaRicorrenza() {
        return super.getTipologiaRicorrenza();
    }

    // SET
    @Override
    public void setAula(Aula aula) {
        super.setAula(aula);
        this.modified = true;
    }

    @Override
    public void setDataFine(LocalDateTime data_fine) {
        super.setDataFine(data_fine);
        this.modified = true;
    }

    @Override
    public void setDataFineRicorrenza(LocalDate data_fine_ricorrenza) {
        super.setDataFineRicorrenza(data_fine_ricorrenza);
        this.modified = true;
    }

    @Override
    public void setDataInizio(LocalDateTime data_inizio) {
        super.setDataInizio(data_inizio);
        this.modified = true;
    }

    @Override
    public void setDescrizione(String descrizione) {
        super.setDescrizione(descrizione);
        this.modified = true;
    }

    @Override
    public void setEmailResponsabile(String email_responsabile) {
        super.setEmailResponsabile(email_responsabile);
        this.modified = true;
    }

    @Override
    public void setNome(String nome) {
        super.setNome(nome);
        this.modified = true;
    }

    @Override
    public void setNomeCorso(String nome_corso) {
        super.setNomeCorso(nome_corso);
        this.modified = true;
    }

    public void setAulaKey(int aula_key) {
        this.aula_key = aula_key;
    }

    /* ********************************************************* */

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean mod) {
        this.modified = mod;
    }

}
