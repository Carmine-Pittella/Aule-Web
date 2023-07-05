
package it.univaq.f4i.iw.Aule_Web.data.proxy;

import java.time.LocalDateTime;

import it.univaq.f4i.iw.Aule_Web.data.impl.Evento_RicorrenteImpl;
import it.univaq.f4i.iw.Aule_Web.data.model.Evento;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;

/**
 *
 * @author Carmine
 */

public class EventoRicorrenteProxy extends Evento_RicorrenteImpl implements DataItemProxy {

    protected boolean modified;
    protected int evento_master_key = 0;
    protected DataLayer dataLayer;

    public EventoRicorrenteProxy(DataLayer d) {
        super();
        this.evento_master_key = 0;
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
    public LocalDateTime getDataFine() {
        return super.getDataFine();
    }

    @Override
    public LocalDateTime getDataInizio() {
        return super.getDataInizio();
    }

    @Override
    public Evento getEventoMaster() {
        return super.getEventoMaster();
    }

    // SET
    @Override
    public void setDataFine(LocalDateTime data_fine) {
        super.setDataFine(data_fine);
        this.modified = true;
    }

    @Override
    public void setDataInizio(LocalDateTime data_inizio) {
        super.setDataInizio(data_inizio);
        this.modified = true;
    }

    @Override
    public void setEventoMaster(Evento evento_master) {
        super.setEventoMaster(evento_master);
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

    public void setEventoMasterKey(int evento_master_key) {
        this.evento_master_key = evento_master_key;
        super.setEventoMaster(null);
        super.setEventoMasterKey(evento_master_key);
    }

}
