
package it.univaq.f4i.iw.Aule_Web.data.proxy;

import it.univaq.f4i.iw.Aule_Web.data.impl.AttrezzaturaImpl;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;

/**
 *
 * @author Carmine
 */

public class AttrezzaturaProxy extends AttrezzaturaImpl implements DataItemProxy {

    protected boolean modified;
    protected DataLayer dataLayer;
    protected int aula_key = 0;

    public AttrezzaturaProxy(DataLayer d) {
        super();
        this.dataLayer = d;
        this.modified = false;
        this.aula_key = 0;
    }

    @Override
    public void setKey(Integer key) {
        super.setKey(key);
        this.modified = true;
    }

    /* ********************************************************* */

    // GET
    @Override
    public String getNomeAttrezzo() {
        return super.getNomeAttrezzo();
    }

    @Override
    public String getDescrizione() {
        return super.getDescrizione();
    }

    // SET
    @Override
    public void setNomeAttrezzo(String nome_attrezzo) {
        super.setNomeAttrezzo(nome_attrezzo);
        this.modified = true;
    }

    @Override
    public void setDescrizione(String descrizione) {
        super.setDescrizione(descrizione);
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
