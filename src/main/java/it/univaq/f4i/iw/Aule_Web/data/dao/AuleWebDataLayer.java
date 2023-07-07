package it.univaq.f4i.iw.Aule_Web.data.dao;

import it.univaq.f4i.iw.Aule_Web.data.model.Amministratore;
import it.univaq.f4i.iw.Aule_Web.data.model.Attrezzatura;
import it.univaq.f4i.iw.Aule_Web.data.model.Attrezzatura_Relazione;
import it.univaq.f4i.iw.Aule_Web.data.model.Aula;
import it.univaq.f4i.iw.Aule_Web.data.model.Evento;
import it.univaq.f4i.iw.Aule_Web.data.model.Evento_Ricorrente;
import it.univaq.f4i.iw.Aule_Web.data.model.Gruppo;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 *
 * @author Carmine
 */

public class AuleWebDataLayer extends DataLayer {

    public AuleWebDataLayer(DataSource datasource) throws SQLException {
        super(datasource);
    }

    @Override
    public void init() throws DataException {
        super.init();
    }

    public AmministratoreDao getAmministratoreDao() {
        return (AmministratoreDao) getDAO(Amministratore.class);
    }

    public AttrezzaturaDao getAttrezzaturaDao() {
        return (AttrezzaturaDao) getDAO(Attrezzatura.class);
    }

    public AttrezzaturaRelazioneDao getAttrezzaturaRelazioneDao() {
        return (AttrezzaturaRelazioneDao) getDAO(Attrezzatura_Relazione.class);
    }

    public AulaDao getAulaDao() {
        return (AulaDao) getDAO(Aula.class);
    }

    public EventoDao getEventoDao() {
        return (EventoDao) getDAO(Evento.class);
    }

    public EventoRicorrenteDao getEventoRicorrenteDao() {
        return (EventoRicorrenteDao) getDAO(Evento_Ricorrente.class);
    }

    public GruppoDao getGruppoDao() {
        return (GruppoDao) getDAO(Gruppo.class);
    }

}
