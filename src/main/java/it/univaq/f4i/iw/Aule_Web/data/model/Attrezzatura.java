
package it.univaq.f4i.iw.Aule_Web.data.model;

import it.univaq.f4i.iw.framework.data.DataItem;

/**
 *
 * @author Carmine
 */

public interface Attrezzatura extends DataItem<Integer> {

    // GET
    String getNomeAttrezzo();

    String getDescrizione();

    // SET
    void setNomeAttrezzo(String nome_attrezzo);

    void setDescrizione(String descrizione);

    // METODI

}
