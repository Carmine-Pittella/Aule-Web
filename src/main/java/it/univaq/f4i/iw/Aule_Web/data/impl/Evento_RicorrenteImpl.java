
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

    // METODO

}
