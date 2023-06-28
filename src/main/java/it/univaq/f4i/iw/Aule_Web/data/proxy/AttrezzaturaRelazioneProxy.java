
package it.univaq.f4i.iw.Aule_Web.data.proxy;

import it.univaq.f4i.iw.Aule_Web.data.impl.Attrezzatura_RelazioneImpl;
import it.univaq.f4i.iw.Aule_Web.data.model.Attrezzatura;
import it.univaq.f4i.iw.Aule_Web.data.model.Aula;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;

/**
 *
 * @author Carmine
 */

public class AttrezzaturaRelazioneProxy extends Attrezzatura_RelazioneImpl implements DataItemProxy {

    protected boolean modified;
    protected DataLayer dataLayer;
    protected int aula_key = 0;
    protected int attrezzatura_key = 0;

    public AttrezzaturaRelazioneProxy(DataLayer d) {
        super();
        this.dataLayer = d;
        this.modified = false;
    }

    @Override
    public void setKey(Integer key) {
        super.setKey(key);
    }

    /* ********************************************************* */

    // GET
    @Override
    public Attrezzatura getAttrezzo() {
        return super.getAttrezzo();
    }

    @Override
    public Aula getAula() {
        return super.getAula();
    }

    @Override
    public Integer getQuantita() {
        return super.getQuantita();
    }

    // SET
    @Override
    public void setAttrezzo(Attrezzatura attrezzo) {
        super.setAttrezzo(attrezzo);
        this.modified = true;
    }

    @Override
    public void setAula(Aula aula) {
        super.setAula(aula);
        this.modified = true;
    }

    @Override
    public void setQuantita(Integer quantita) {
        super.setQuantita(quantita);
        this.modified = true;
    }

    /* ********************************************************* */

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean mod) {
        this.modified = mod;
    }

}
