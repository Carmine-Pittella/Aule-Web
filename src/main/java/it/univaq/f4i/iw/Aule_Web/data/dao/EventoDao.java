
package it.univaq.f4i.iw.Aule_Web.data.dao;

import java.time.LocalDate;
import java.util.List;

import it.univaq.f4i.iw.Aule_Web.data.model.Aula;
import it.univaq.f4i.iw.Aule_Web.data.model.Evento;
import it.univaq.f4i.iw.Aule_Web.data.model.Gruppo;
import it.univaq.f4i.iw.framework.data.DataException;

/**
 *
 * @author Carmine
 */

public interface EventoDao {

    Evento createEvento();

    Evento getEventoById(int evento_key) throws DataException;

    List<Evento> getEventi() throws DataException;

    List<Evento> getEventiByAula(Aula aula) throws DataException;

    List<Evento> getEventiByCorso(String corso) throws DataException;

    List<Evento> getEventiByData(LocalDate inizio, LocalDate fine) throws DataException;

    List<Evento> getEventiByGiorno(LocalDate giorno) throws DataException;

    List<Evento> getEventiAttuali() throws DataException;

    List<Evento> getEventiByGruppo(Gruppo gruppo) throws DataException;

    List<String> getCorsi() throws DataException;

    List<String> getCorsiByGruppo(Gruppo gruppo) throws DataException;

    List<String> getResponsabili() throws DataException;

    void storeEvento(Evento evento) throws DataException;

    void deleteEvento(Evento evento) throws DataException;

}
