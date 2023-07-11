
package it.univaq.f4i.iw.Aule_Web.data.impl;

import it.univaq.f4i.iw.Aule_Web.data.model.Attrezzatura;
import it.univaq.f4i.iw.Aule_Web.data.model.Attrezzatura_Relazione;
import it.univaq.f4i.iw.Aule_Web.data.model.Aula;
import it.univaq.f4i.iw.framework.data.DataItemImpl;

/**
 *
 * @author Carmine
 */

public class Attrezzatura_RelazioneImpl extends DataItemImpl<Integer> implements Attrezzatura_Relazione {

    private Aula aula;
    private Integer id_aula;
    private Attrezzatura attrezzo;
    private Integer quantita;

    // GET
    @Override
    public Aula getAula() {
        return aula;
    }

    @Override
    public Attrezzatura getAttrezzo() {
        return attrezzo;
    }

    @Override
    public Integer getQuantita() {
        return quantita;
    }

    // SET
    @Override
    public void setAula(Aula aula) {
        this.aula = aula;
    }

    @Override
    public void setAttrezzo(Attrezzatura attrezzo) {
        this.attrezzo = attrezzo;
    }

    @Override
    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    @Override
    public Integer getIdAula() {
        return id_aula;
    }

    @Override
    public void setIdAula(Integer id_aula) {
        this.id_aula = id_aula;
    }

    // METODI

}
