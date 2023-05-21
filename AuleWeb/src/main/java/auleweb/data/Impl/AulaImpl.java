package auleweb.data.Impl;

import java.util.List;

import auleweb.data.model.Attrezzatura;
import auleweb.data.model.Aula;
import auleweb.data.model.Evento;
import auleweb.data.model.Gruppo;
import auleweb.framework.data.DataItemImpl;

public class AulaImpl extends DataItemImpl<Integer> implements Aula {
    private String nome;
    private int n_prese_elettriche;
    private int n_prese_rete;
    private Integer capienza;
    private String email_responsabile;
    private String note;
    private String luogo;
    private Integer piano;
    private String edificio;
    private Gruppo gruppo;
    private List<Attrezzatura> attrezzatura;
    private List<Evento> listaEventi;

    // COSTRUTTORE
    public AulaImpl() {
    }

    public AulaImpl(String nome, int n_prese_elettriche, int n_prese_rete, Integer capienza, String email_responsabile,
            String note, String luogo, Integer piano, String edificio, List<Attrezzatura> attrezzatura,
            List<Evento> listaEventi) {
        this.nome = nome;
        this.n_prese_elettriche = n_prese_elettriche;
        this.n_prese_rete = n_prese_rete;
        this.capienza = capienza;
        this.email_responsabile = email_responsabile;
        this.note = note;
        this.luogo = luogo;
        this.piano = piano;
        this.edificio = edificio;
        this.attrezzatura = attrezzatura;
        this.listaEventi = listaEventi;
    }

    /******************* GET e SET *******************/
    @Override
    public List<Attrezzatura> getAttrezzatura() {
        return attrezzatura;
    }

    @Override
    public Integer getCapienza() {
        return capienza;
    }

    @Override
    public String getEdificio() {
        return edificio;
    }

    @Override
    public String getEmail_responsabile() {
        return email_responsabile;
    }

    @Override
    public List<Evento> getListaEventi() {
        return listaEventi;
    }

    @Override
    public String getLuogo() {
        return luogo;
    }

    @Override
    public int getN_prese_elettriche() {
        return n_prese_elettriche;
    }

    @Override
    public int getN_prese_rete() {
        return n_prese_rete;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public String getNote() {
        return note;
    }

    @Override
    public Integer getPiano() {
        return piano;
    }

    @Override
    public void setAttrezzatura(List<Attrezzatura> attrezzatura) {
        this.attrezzatura = attrezzatura;
    }

    @Override
    public void setCapienza(Integer capienza) {
        this.capienza = capienza;
    }

    @Override
    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    @Override
    public void setEmail_responsabile(String email_responsabile) {
        this.email_responsabile = email_responsabile;
    }

    @Override
    public void setListaEventi(List<Evento> listaEventi) {
        this.listaEventi = listaEventi;
    }

    @Override
    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    @Override
    public void setN_prese_elettriche(int n_prese_elettriche) {
        this.n_prese_elettriche = n_prese_elettriche;
    }

    @Override
    public void setN_prese_rete(int n_prese_rete) {
        this.n_prese_rete = n_prese_rete;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public void setPiano(Integer piano) {
        this.piano = piano;
    }

    @Override
    public Gruppo getGruppo() {
        return gruppo;
    }

    @Override
    public void setGruppo(Gruppo gruppo) {
        this.gruppo = gruppo;
    }

}
