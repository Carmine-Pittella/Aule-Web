
package it.univaq.f4i.iw.Aule_Web.data.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import it.univaq.f4i.iw.Aule_Web.data.model.Aula;
import it.univaq.f4i.iw.Aule_Web.data.model.Evento;
import it.univaq.f4i.iw.Aule_Web.data.model.Evento_Ricorrente;
import it.univaq.f4i.iw.Aule_Web.data.model.Gruppo;
import it.univaq.f4i.iw.Aule_Web.data.proxy.EventoRicorrenteProxy;
import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.framework.data.OptimisticLockException;

/**
 *
 * @author Carmine
 */

public class EventoRicorrenteDaoMySQL extends DAO implements EventoRicorrenteDao {

    private PreparedStatement sEventoRicorrenteById, sEventoMaster, sEventiRicorrenti,
            sEventiRicorrentiByEvento, sEventiRicorrentiByAula, sEventiRicorrentiByCorso,
            sEventiRicorrentiByDate, sEventiRicorrentiByGiorno, sEventiRicorrentiAttuali,
            sEventiRicorrentiByGruppo, dEventoRicorrente;

    private final EventoDao eventoDAO;

    public EventoRicorrenteDaoMySQL(DataLayer d) {
        super(d);
        eventoDAO = (EventoDao) d.getDAO(Evento.class);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();
            sEventoRicorrenteById = connection.prepareStatement("SELECT * FROM evento_ricorrente WHERE Id=?");
            sEventoMaster = connection.prepareStatement("SELECT Id_master FROM evento_ricorrente WHERE Id=?");
            sEventiRicorrenti = connection.prepareStatement("SELECT Id AS eventoRicorrenteId FROM evento_ricorrente");
            sEventiRicorrentiByEvento = connection
                    .prepareStatement("SELECT Id AS eventoRicorrenteId FROM evento_ricorrente WHERE Id_master=?");
            sEventiRicorrentiByAula = connection.prepareStatement(
                    "SELECT er.Id AS eventoRicorrenteId FROM evento e JOIN evento_ricorrente er ON er.Id_master = e.Id WHERE e.Id_aula=?");
            sEventiRicorrentiByCorso = connection.prepareStatement(
                    "SELECT er.Id AS eventoRicorrenteId FROM evento e JOIN evento_ricorrente er ON er.Id_master = e.Id WHERE e.nome_corso=?");
            sEventiRicorrentiByDate = connection.prepareStatement(
                    "SELECT Id AS eventoRicorrenteId FROM evento_ricorrente WHERE DATEDIFF(DATE(data_inizio),?) >= 0 AND DATEDIFF(DATE(data_inizio),?) <= 0");
            sEventiRicorrentiByGiorno = connection.prepareStatement(
                    "SELECT Id AS eventoRicorrenteId FROM evento_ricorrente WHERE DATEDIFF(data_inizio,?)=0");
            sEventiRicorrentiAttuali = connection
                    .prepareStatement("SELECT Id AS eventoRicorrenteId FROM evento_ricorrente "
                            + "WHERE SUBTIME( DATE(data_fine),TIME( now() ) ) >= 0 AND "
                            + "SUBTIME( TIME(data_inizio), TIME( DATE_ADD( now() , INTERVAL 3 HOUR) ) ) <= 0 AND "
                            + "DATEDIFF(DATE(data_inizio), now() ) = 0");
            sEventiRicorrentiByGruppo = connection.prepareStatement(
                    "SELECT er.Id AS eventoRicorrenteId FROM evento e JOIN evento_ricorrente er ON er.Id_master = e.Id "
                            + "JOIN gruppo_aula ga ON ga.Id_aula = e.Id_aula WHERE ga.Id_gruppo=?");
            dEventoRicorrente = connection.prepareStatement("DELETE FROM evento_ricorrente WHERE Id=?");

        } catch (SQLException ex) {
            throw new DataException("Error initializing auleweb data layer EventoRicorrente", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            sEventoRicorrenteById.close();
            sEventoMaster.close();
            sEventiRicorrenti.close();
            sEventiRicorrentiByEvento.close();
            sEventiRicorrentiByAula.close();
            sEventiRicorrentiByCorso.close();
            sEventiRicorrentiByDate.close();
            sEventiRicorrentiByGiorno.close();
            sEventiRicorrentiByGruppo.close();
            dEventoRicorrente.close();
        } catch (SQLException ex) {
            throw new DataException("Error destroy auleweb data layer EventoRicorrente", ex);
        }
        super.destroy();
    }

    @Override
    public Evento_Ricorrente createEventoRicorrente() {
        return new EventoRicorrenteProxy(getDataLayer());
    }

    private EventoRicorrenteProxy createEventoRicorrente(ResultSet rs) throws DataException {
        EventoRicorrenteProxy e = (EventoRicorrenteProxy) createEventoRicorrente();
        try {
            e.setKey(rs.getInt("Id"));
            e.setDataInizio(rs.getDate("data_inizio").toLocalDate().atTime(rs.getTime("data_inizio").toLocalTime()));
            e.setDataFine(rs.getDate("data_fine").toLocalDate().atTime(rs.getTime("data_fine").toLocalTime()));
            Evento eventoMaster = eventoDAO.getEventoById(rs.getInt("Id_master"));
            e.setEventoMaster(eventoMaster);
            e.setEventoMasterKey(rs.getInt("Id_master"));
            e.setVersion(rs.getLong("version"));
        } catch (SQLException ex) {
            throw new DataException("Errore in createEventoRicorrente() ", ex);
        }
        return e;

    }

    @Override
    public Evento_Ricorrente getEventoRicorrente(int evento_ricorrente_key) throws DataException {
        Evento_Ricorrente e = null;
        if (dataLayer.getCache().has(Evento_Ricorrente.class, evento_ricorrente_key)) {
            e = dataLayer.getCache().get(Evento_Ricorrente.class, evento_ricorrente_key);
        } else {
            try {
                sEventoRicorrenteById.setInt(1, evento_ricorrente_key);
                try (ResultSet rs = sEventoRicorrenteById.executeQuery()) {
                    if (rs.next()) {
                        e = createEventoRicorrente(rs);
                        dataLayer.getCache().add(Evento_Ricorrente.class, e);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Errore in getEventoRicorrente() ", ex);
            }
        }
        return e;
    }

    @Override
    public Evento getEventoMaster(int evento_ricorrente_key) throws DataException {
        Evento e = null;
        try {
            sEventoMaster.setInt(1, evento_ricorrente_key);

            try (ResultSet rs = sEventoMaster.executeQuery()) {
                if (rs.next()) {
                    e = eventoDAO.getEventoById(rs.getInt("Id_master"));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore in getEventoMaster() ", ex);
        }
        return e;
    }

    @Override
    public List<Evento_Ricorrente> getEventiRicorrenti() throws DataException {
        List<Evento_Ricorrente> result = new ArrayList<Evento_Ricorrente>();
        try (ResultSet rs = sEventiRicorrenti.executeQuery()) {
            while (rs.next()) {
                result.add(getEventoRicorrente(rs.getInt("eventoRicorrenteId")));
            }
        } catch (SQLException ex) {
            throw new DataException("Errore in getEventiRicorrenti() ", ex);
        }
        return result;
    }

    @Override
    public List<Evento_Ricorrente> getEventiRicorrentiByEvento(Evento evento) throws DataException {
        List<Evento_Ricorrente> result = new ArrayList<Evento_Ricorrente>();
        try {
            sEventiRicorrentiByEvento.setInt(1, evento.getKey());
            try (ResultSet rs = sEventiRicorrentiByEvento.executeQuery()) {
                while (rs.next()) {
                    result.add((Evento_Ricorrente) getEventoRicorrente(rs.getInt("eventoRicorrenteID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore in getEventiRicorrentiByEvento() ", ex);
        }
        return result;
    }

    @Override
    public List<Evento_Ricorrente> getEventiRicorrentiByAula(Aula aula) throws DataException {
        List<Evento_Ricorrente> result = new ArrayList<Evento_Ricorrente>();
        try {
            sEventiRicorrentiByAula.setInt(1, aula.getKey());
            try (ResultSet rs = sEventiRicorrentiByAula.executeQuery()) {
                while (rs.next()) {
                    result.add((Evento_Ricorrente) getEventoRicorrente(rs.getInt("eventoRicorrenteID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore in getEventiRicorrentiByAula() ", ex);
        }
        return result;
    }

    @Override
    public List<Evento_Ricorrente> getEventiRicorrentiByCorso(String corso) throws DataException {
        List<Evento_Ricorrente> result = new ArrayList<Evento_Ricorrente>();
        try {
            sEventiRicorrentiByCorso.setString(1, corso);
            try (ResultSet rs = sEventiRicorrentiByCorso.executeQuery()) {
                while (rs.next()) {
                    result.add((Evento_Ricorrente) getEventoRicorrente(rs.getInt("eventoRicorrenteI")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore in getEventiRicorrentiByCorso() ", ex);
        }
        return result;
    }

    @Override
    public List<Evento_Ricorrente> getEventiRicorrentiByData(LocalDate inizio, LocalDate fine) throws DataException {
        List<Evento_Ricorrente> result = new ArrayList<Evento_Ricorrente>();
        try {
            sEventiRicorrentiByDate.setString(1, inizio.format(DateTimeFormatter.ISO_DATE));
            sEventiRicorrentiByDate.setString(2, fine.format(DateTimeFormatter.ISO_DATE));
            try (ResultSet rs = sEventiRicorrentiByDate.executeQuery()) {
                while (rs.next()) {
                    result.add((Evento_Ricorrente) getEventoRicorrente(rs.getInt("eventoRicorrenteId")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore in getEventiRicorrentiByData() ", ex);
        }
        return result;
    }

    @Override
    public List<Evento_Ricorrente> getEventiRicorrentiByGiorno(LocalDate giorno) throws DataException {
        List<Evento_Ricorrente> result = new ArrayList<Evento_Ricorrente>();
        try {
            sEventiRicorrentiByGiorno.setString(1, giorno.format(DateTimeFormatter.ISO_DATE));
            try (ResultSet rs = sEventiRicorrentiByGiorno.executeQuery()) {
                while (rs.next()) {
                    result.add((Evento_Ricorrente) getEventoRicorrente(rs.getInt("eventoRicorrenteId")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore in getEventiRicorrentiByGiorno() ", ex);
        }
        return result;
    }

    @Override
    public List<Evento_Ricorrente> getEventiRicorrentiAttuali() throws DataException {
        List<Evento_Ricorrente> result = new ArrayList<Evento_Ricorrente>();
        try {
            try (ResultSet rs = sEventiRicorrentiAttuali.executeQuery()) {
                while (rs.next()) {
                    result.add((Evento_Ricorrente) getEventoRicorrente(rs.getInt("eventoRicorrenteId")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore in getEventiRicorrentiAttuali() ", ex);
        }
        return result;
    }

    @Override
    public List<Evento_Ricorrente> getEventiRicorrentiByGruppo(Gruppo gruppo) throws DataException {
        List<Evento_Ricorrente> result = new ArrayList<Evento_Ricorrente>();
        try {
            sEventiRicorrentiByGruppo.setInt(1, gruppo.getKey());
            try (ResultSet rs = sEventiRicorrentiByGruppo.executeQuery()) {
                while (rs.next()) {
                    result.add((Evento_Ricorrente) getEventoRicorrente(rs.getInt("eventoRicorrenteId")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore in getEventiRicorrentiByGruppo() ", ex);
        }
        return result;
    }

    @Override
    public void deleteEventoRicorrente(Evento_Ricorrente evento) throws DataException {
        if (evento.getKey() == null || evento.getKey() <= 0) {
            return;
        }
        try {
            dEventoRicorrente.setInt(1, evento.getKey());
            if (dEventoRicorrente.executeUpdate() == 0) {
                throw new OptimisticLockException(evento);
            } else if (dataLayer.getCache().has(Evento_Ricorrente.class, evento.getKey())) {
                dataLayer.getCache().delete(Evento_Ricorrente.class, evento.getKey());
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to delete evento ricorrente", ex);
        }
    }

}
