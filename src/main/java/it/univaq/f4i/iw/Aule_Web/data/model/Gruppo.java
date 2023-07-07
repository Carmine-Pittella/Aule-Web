
package it.univaq.f4i.iw.Aule_Web.data.model;

import it.univaq.f4i.iw.framework.data.DataItem;

/**
 *
 * @author Carmine
 */

public interface Gruppo extends DataItem<Integer> {

    // GET
    String getNome();

    String getDescrizione();

    // SET
    void setNome(String nome);

    void setDescrizione(String descrizione);

    // METODI

}
