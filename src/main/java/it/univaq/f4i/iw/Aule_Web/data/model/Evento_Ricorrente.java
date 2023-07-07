
package it.univaq.f4i.iw.Aule_Web.data.model;

import java.time.LocalDateTime;

import it.univaq.f4i.iw.framework.data.DataItem;

/**
 *
 * @author Carmine
 */

public interface Evento_Ricorrente extends DataItem<Integer> {

    // GET
    LocalDateTime getDataInizio();

    LocalDateTime getDataFine();

    Evento getEventoMaster();

    Integer getEventoMasterKey();

    // SET
    void setDataInizio(LocalDateTime data_inizio);

    void setDataFine(LocalDateTime data_fine);

    void setEventoMaster(Evento evento_master);

    void setEventoMasterKey(Integer evento_master_key);

    // METODI

}
