
package it.univaq.f4i.iw.Aule_Web.data.impl;

import it.univaq.f4i.iw.Aule_Web.data.model.Aula;
import it.univaq.f4i.iw.Aule_Web.data.model.Gruppo;
import it.univaq.f4i.iw.framework.data.DataItemImpl;

/**
 *
 * @author Carmine
 */

public class AulaImpl extends DataItemImpl<Integer> implements Aula {

    private String nome;
    private String luogo;
    private String edificio;
    private Integer piano;
    private Integer capienza;
    private String email_responsabile;
    private Integer n_prese_rete;
    private Integer n_prese_elettriche;
    private String note;
    private Gruppo gruppo;

    // GET
    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public String getLuogo() {
        return luogo;
    }

    @Override
    public String getEdificio() {
        return edificio;
    }

    @Override
    public Integer getPiano() {
        return piano;
    }

    @Override
    public Integer getCapienza() {
        return capienza;
    }

    @Override
    public String getEmailResponsabile() {
        return email_responsabile;
    }

    @Override
    public Integer getN_PreseRete() {
        return n_prese_rete;
    }

    @Override
    public Integer getN_PreseElettriche() {
        return n_prese_elettriche;
    }

    @Override
    public String getNote() {
        return note;
    }

    @Override
    public Gruppo getGruppo() {
        return gruppo;
    }

    // SET
    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    @Override
    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    @Override
    public void setPiano(Integer piano) {
        this.piano = piano;
    }

    @Override
    public void setCapienza(Integer capienza) {
        this.capienza = capienza;
    }

    @Override
    public void setEmailResponsabile(String email_responsabile) {
        this.email_responsabile = email_responsabile;
    }

    @Override
    public void setN_PreseRete(Integer n_prese_rete) {
        this.n_prese_rete = n_prese_rete;
    }

    @Override
    public void setN_PreseElettriche(Integer n_prese_elettriche) {
        this.n_prese_elettriche = n_prese_elettriche;
    }

    @Override
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public void setGruppo(Gruppo gruppo) {
        this.gruppo = gruppo;
    }

    // METODI

}
