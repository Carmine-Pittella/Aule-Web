
package it.univaq.f4i.iw.Aule_Web.data.model;

import java.util.List;

import it.univaq.f4i.iw.framework.data.DataItem;

/**
 *
 * @author Carmine
 */

public interface Aula extends DataItem<Integer> {

    // GET
    String getNome();

    String getLuogo();

    String getEdificio();

    Integer getPiano();

    Integer getCapienza();

    String getEmailResponsabile();

    Integer getN_PreseRete();

    Integer getN_PreseElettriche();

    String getNote();

    Gruppo getGruppo();

    List<Evento> getEventi();

    List<Attrezzatura_Relazione> getListaAttrezzatura();

    // SET
    void setNome(String nome);

    void setLuogo(String luogo);

    void setEdificio(String edificio);

    void setPiano(Integer piano);

    void setCapienza(Integer capienza);

    void setEmailResponsabile(String email_responsabile);

    void setN_PreseRete(Integer n_prese_rete);

    void setN_PreseElettriche(Integer n_prese_elettriche);

    void setNote(String note);

    void setGruppo(Gruppo gruppo);

    void setListaEventi(List<Evento> listaEventi);

    void setListaAttrezzatura(List<Attrezzatura_Relazione> listaAttrezzatura);

    // METODI
    void addGruppo(Gruppo gruppo);

    boolean removeGruppo(Gruppo gruppo);

    void addEvento(Evento evento);

    boolean removeEvento(Evento evento);

}
