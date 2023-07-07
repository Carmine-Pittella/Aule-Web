
package it.univaq.f4i.iw.Aule_Web.data.dao;

import java.time.LocalDate;
import java.util.List;

import it.univaq.f4i.iw.Aule_Web.data.model.Aula;
import it.univaq.f4i.iw.Aule_Web.data.model.Evento;
import it.univaq.f4i.iw.Aule_Web.data.model.Evento_Ricorrente;
import it.univaq.f4i.iw.Aule_Web.data.model.Gruppo;
import it.univaq.f4i.iw.framework.data.DataException;

/**
 *
 * @author Carmine
 */

public interface EventoRicorrenteDao {

    Evento_Ricorrente createEventoRicorrente();

    Evento_Ricorrente getEventoRicorrente(int evento_ricorrente_key) throws DataException;

    Evento getEventoMaster(int evento_ricorrente_key) throws DataException;

    List<Evento_Ricorrente> getEventiRicorrenti() throws DataException;

    List<Evento_Ricorrente> getEventiRicorrentiByEvento(Evento evento) throws DataException;

    List<Evento_Ricorrente> getEventiRicorrentiByAula(Aula aula) throws DataException;

    List<Evento_Ricorrente> getEventiRicorrentiByCorso(String corso) throws DataException;

    List<Evento_Ricorrente> getEventiRicorrentiByData(LocalDate inizio, LocalDate fine) throws DataException;

    List<Evento_Ricorrente> getEventiRicorrentiByGiorno(LocalDate giorno) throws DataException;

    List<Evento_Ricorrente> getEventiRicorrentiAttuali() throws DataException;

    List<Evento_Ricorrente> getEventiRicorrentiByGruppo(Gruppo gruppo) throws DataException;

    // la store non serve perché viene fatta automaticamente tramite il DB

    void deleteEventoRicorrente(Evento_Ricorrente evento) throws DataException;

}
