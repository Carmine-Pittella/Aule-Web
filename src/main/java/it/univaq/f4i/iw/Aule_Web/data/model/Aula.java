
package it.univaq.f4i.iw.Aule_Web.data.model;

/**
 *
 * @author Carmine
 */

public interface Aula {

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

    // METODI

}
