
package it.univaq.f4i.iw.Aule_Web.data.model;

import it.univaq.f4i.iw.framework.data.DataItem;

/**
 *
 * @author Carmine
 */

public interface Attrezzatura_Relazione extends DataItem<Integer> {

    // GET
    Aula getAula();

    Attrezzatura getAttrezzo();

    Integer getQuantita();

    // SET
    void setAula(Aula aula);

    void setAttrezzo(Attrezzatura attrezzo);

    void setQuantita(Integer quantita);

    // METODI

}
