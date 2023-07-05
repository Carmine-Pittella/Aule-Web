
package it.univaq.f4i.iw.Aule_Web.data.impl;

import java.time.LocalDateTime;

import it.univaq.f4i.iw.Aule_Web.data.model.Evento;
import it.univaq.f4i.iw.Aule_Web.data.model.Evento_Ricorrente;
import it.univaq.f4i.iw.framework.data.DataItemImpl;

/**
 *
 * @author Carmine
 */

public class Evento_RicorrenteImpl extends DataItemImpl<Integer> implements Evento_Ricorrente {

    private LocalDateTime data_inizio;
    private LocalDateTime data_fine;
    private Evento evento_master;
    private Integer evento_master_key;

    public Evento_RicorrenteImpl() {
        this.data_inizio = null;
        this.data_fine = null;
        this.evento_master = null;
    }

    // GET
    @Override
    public LocalDateTime getDataInizio() {
        return data_inizio;
    }

    @Override
    public LocalDateTime getDataFine() {
        return data_fine;
    }

    @Override
    public Evento getEventoMaster() {
        return evento_master;
    }

    @Override
    public Integer getEventoMasterKey() {
        return evento_master_key;
    }

    // SET
    @Override
    public void setDataInizio(LocalDateTime data_inizio) {
        this.data_inizio = data_inizio;
    }

    @Override
    public void setDataFine(LocalDateTime data_fine) {
        this.data_fine = data_fine;
    }

    @Override
    public void setEventoMaster(Evento evento_master) {
        this.evento_master = evento_master;
    }

    @Override
    public void setEventoMasterKey(Integer evento_master_key) {
        this.evento_master_key = evento_master_key;
    }

    // METODO

}
