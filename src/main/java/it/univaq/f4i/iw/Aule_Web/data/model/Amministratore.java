
package it.univaq.f4i.iw.Aule_Web.data.model;

import it.univaq.f4i.iw.framework.data.DataItem;

/**
 *
 * @author Carmine
 */

public interface Amministratore extends DataItem<Integer> {

    // GET
    String getNome();

    String getCognome();

    String getEmail();

    String getPassword();

    // SET
    void setNome(String nome);

    void setCognome(String cognome);

    void setEmail(String email);

    void setPassword(String password);

    // METODI

}
