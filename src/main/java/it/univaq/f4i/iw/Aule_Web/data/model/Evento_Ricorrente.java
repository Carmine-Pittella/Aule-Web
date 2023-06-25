
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

    Integer getIdMaster();

    // SET
    void setDataInizio(LocalDateTime data_inizio);

    void setDataFine(LocalDateTime data_fine);

    void setIdMaster(Integer id_master);

    // METODI

}
