package it.univaq.f4i.iw.framework.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

import it.univaq.f4i.iw.Aule_Web.data.dao.AmministratoreDaoMySQL;
import it.univaq.f4i.iw.Aule_Web.data.dao.AttrezzaturaDaoMySQL;
import it.univaq.f4i.iw.Aule_Web.data.dao.AulaDaoMySQL;
import it.univaq.f4i.iw.Aule_Web.data.dao.EventoDaoMySQL;
import it.univaq.f4i.iw.Aule_Web.data.dao.EventoRicorrenteDaoMySQL;
import it.univaq.f4i.iw.Aule_Web.data.dao.GruppoDaoMySQL;
import it.univaq.f4i.iw.Aule_Web.data.model.Amministratore;
import it.univaq.f4i.iw.Aule_Web.data.model.Attrezzatura;
import it.univaq.f4i.iw.Aule_Web.data.model.Aula;
import it.univaq.f4i.iw.Aule_Web.data.model.Evento;
import it.univaq.f4i.iw.Aule_Web.data.model.Evento_Ricorrente;
import it.univaq.f4i.iw.Aule_Web.data.model.Gruppo;

public class DataLayer implements AutoCloseable {

    private final DataSource datasource;
    private Connection connection;
    private final Map<Class, DAO> daos;
    private final DataCache cache;

    public DataLayer(DataSource datasource) throws SQLException {
        super();
        this.datasource = datasource;
        this.connection = datasource.getConnection();
        this.daos = new HashMap<>();
        this.cache = new DataCache();
    }

    public void registerDAO(Class entityClass, DAO dao) throws DataException {
        daos.put(entityClass, dao);
        dao.init();
    }

    public DAO getDAO(Class entityClass) {
        return daos.get(entityClass);
    }

    public void init() throws DataException {
        registerDAO(Attrezzatura.class, new AttrezzaturaDaoMySQL(this));
        registerDAO(Aula.class, new AulaDaoMySQL(this));
        registerDAO(Evento.class, new EventoDaoMySQL(this));
        registerDAO(Gruppo.class, new GruppoDaoMySQL(this));
        registerDAO(Amministratore.class, new AmministratoreDaoMySQL(this));
        registerDAO(Evento_Ricorrente.class, new EventoRicorrenteDaoMySQL(this));
    }

    public void destroy() {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException ex) {
            //
        }
    }

    public DataSource getDatasource() {
        return datasource;
    }

    public Connection getConnection() {
        return connection;
    }

    public DataCache getCache() {
        return cache;
    }

    // metodo dell'interfaccia AutoCloseable (permette di usare questa classe nei
    // try-with-resources)
    @Override
    public void close() throws Exception {
        destroy();
    }

}
