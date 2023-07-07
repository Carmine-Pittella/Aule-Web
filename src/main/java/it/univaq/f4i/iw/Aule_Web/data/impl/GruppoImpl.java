
package it.univaq.f4i.iw.Aule_Web.data.impl;

import it.univaq.f4i.iw.Aule_Web.data.model.Gruppo;
import it.univaq.f4i.iw.framework.data.DataItemImpl;

/**
 *
 * @author Carmine
 */

public class GruppoImpl extends DataItemImpl<Integer> implements Gruppo {

    private String nome;
    private String descrizione;

    // GET
    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public String getDescrizione() {
        return descrizione;
    }

    // SET
    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    // METODI

}
