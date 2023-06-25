
package it.univaq.f4i.iw.Aule_Web.data.model;

import java.time.LocalDateTime;

/**
 *
 * @author Carmine
 */

public interface Evento_Ricorrente {

    // GET
    LocalDateTime getDataInizio();

    LocalDateTime getDataFine();

    Evento getEventoMaster();

    // SET
    void setDataInizio(LocalDateTime data_inizio);

    void setDataFine(LocalDateTime data_fine);

    void setEventoMaster(Evento evento_master);

    // METODI

}
