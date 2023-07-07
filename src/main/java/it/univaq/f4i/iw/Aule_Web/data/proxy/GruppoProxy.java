
package it.univaq.f4i.iw.Aule_Web.data.proxy;

import it.univaq.f4i.iw.Aule_Web.data.impl.GruppoImpl;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;

/**
 *
 * @author Carmine
 */

public class GruppoProxy extends GruppoImpl implements DataItemProxy {

    protected boolean modified;
    protected DataLayer dataLayer;

    public GruppoProxy(DataLayer d) {
        super();
        this.dataLayer = d;
        this.modified = false;
    }

    @Override
    public void setKey(Integer key) {
        super.setKey(key);
        this.modified = true;
    }

    /* ********************************************************* */

    // GET
    @Override
    public String getNome() {
        return super.getNome();
    }

    @Override
    public String getDescrizione() {
        return super.getDescrizione();
    }

    // SET
    @Override
    public void setNome(String nome) {
        super.setNome(nome);
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
