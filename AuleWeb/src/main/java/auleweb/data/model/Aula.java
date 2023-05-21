package auleweb.data.model;

import java.util.List;

import auleweb.framework.data.DataItem;

public interface Aula extends DataItem<Integer> {

    String getNome();

    void setNome(String nome);

    int getN_prese_elettriche();

    void setN_prese_elettriche(int n_prese_elettriche);

    int getN_prese_rete();

    void setN_prese_rete(int n_prese_rete);

    Integer getCapienza();

    void setCapienza(Integer capienza);

    String getEmail_responsabile();

    void setEmail_responsabile(String email_responsabile);

    String getNote();

    void setNote(String note);

    String getLuogo();

    void setLuogo(String luogo);

    Integer getPiano();

    void setPiano(Integer piano);

    String getEdificio();

    void setEdificio(String edificio);

    List<Attrezzatura> getAttrezzatura();

    void setAttrezzatura(List<Attrezzatura> attrezzatura);

    List<Evento> getListaEventi();

    void setListaEventi(List<Evento> listaEventi);

    public Gruppo getGruppo();

    public void setGruppo(Gruppo gruppo);

}