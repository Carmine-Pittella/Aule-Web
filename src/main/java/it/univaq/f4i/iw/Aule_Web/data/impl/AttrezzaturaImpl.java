
package it.univaq.f4i.iw.Aule_Web.data.impl;

import it.univaq.f4i.iw.Aule_Web.data.model.Attrezzatura;

/**
 *
 * @author Carmine
 */

public class AttrezzaturaImpl implements Attrezzatura {

    private String nome_attrezzo;
    private String descrizione;

    // GET
    @Override
    public String getNomeAttrezzo() {
        return nome_attrezzo;
    }

    @Override
    public String getDescrizione() {
        return descrizione;
    }

    // SET
    @Override
    public void setNomeAttrezzo(String nome_attrezzo) {
        this.nome_attrezzo = nome_attrezzo;
    }

    @Override
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    // METODI

}
