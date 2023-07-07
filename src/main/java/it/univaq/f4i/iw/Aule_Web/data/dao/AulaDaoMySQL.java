
package it.univaq.f4i.iw.Aule_Web.data.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.univaq.f4i.iw.Aule_Web.data.model.Attrezzatura_Relazione;
import it.univaq.f4i.iw.Aule_Web.data.model.Aula;
import it.univaq.f4i.iw.Aule_Web.data.model.Evento;
import it.univaq.f4i.iw.Aule_Web.data.model.Gruppo;
import it.univaq.f4i.iw.Aule_Web.data.proxy.AulaProxy;
import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.framework.data.OptimisticLockException;

/**
 *
 * @author Carmine
 */

public class AulaDaoMySQL extends DAO implements AulaDao {

    private PreparedStatement sAulaById, sAula, sAulaByEvento;
    private PreparedStatement sAule, sAuleByGruppo, sAuleByNPreseRete, sAuleByNPreseElettriche,
            sAuleByCapienza, sAuleByPiano, sAuleByLuogo, sAuleByEdificio;
    private PreparedStatement sResponsabili;
    private PreparedStatement iGruppo, dGruppo;
    private PreparedStatement iAula, uAula, dAula;

    private final AttrezzaturaRelazioneDao attrezzaturaRelazioneDAO;

    public AulaDaoMySQL(DataLayer d) {
        super(d);
        attrezzaturaRelazioneDAO = (AttrezzaturaRelazioneDao) d.getDAO(Attrezzatura_Relazione.class);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();
            sAulaById = connection.prepareStatement("SELECT * FROM aula WHERE Id=?");
            sAula = connection
                    .prepareStatement("SELECT * FROM aula WHERE nome=? AND edificio=? AND luogo=? AND piano=?");
            sAulaByEvento = connection
                    .prepareStatement("SELECT a.* FROM evento e JOIN aula a ON e.Id_aula = a.Id WHERE e.Id=?");
            sAule = connection.prepareStatement("SELECT Id AS aulaId FROM aula");
            sAuleByGruppo = connection.prepareStatement(
                    "SELECT Id AS aulaId FROM aula WHERE Id_gruppo=?");
            sAuleByNPreseRete = connection
                    .prepareStatement("SELECT Id AS aulaId FROM aula WHERE numero_prese_rete >=?");
            sAuleByNPreseElettriche = connection
                    .prepareStatement("SELECT Id AS aulaId FROM aula WHERE numero_prese_elettriche >=?");
            sAuleByCapienza = connection.prepareStatement("SELECT Id AS aulaId FROM aula WHERE capienza >=?");
            sAuleByPiano = connection.prepareStatement("SELECT Id AS aulaId FROM aula WHERE piano =?");
            sAuleByLuogo = connection.prepareStatement("SELECT Id AS aulaId FROM aula WHERE luogo =?");
            sAuleByEdificio = connection.prepareStatement("SELECT Id AS aulaId FROM aula WHERE edificio =?");
            sResponsabili = connection.prepareStatement("SELECT DISTINCT email_responsabile FROM aula");

            iGruppo = connection.prepareStatement("INSERT INTO gruppo_aula (Id_aula, Id_gruppo) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            dGruppo = connection.prepareStatement("DELETE FROM gruppo_aula WHERE Id_aula=? AND Id_gruppo=?");

            iAula = connection.prepareStatement(
                    "INSERT INTO aula (nome, luogo, edificio, piano, capienza, email_responsabile, numero_prese_rete, numero_prese_elettriche, note)"
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS);
            uAula = connection.prepareStatement("UPDATE aula"
                    + " SET nome = ?, luogo = ?, edificio = ?, piano = ?, capienza = ?, email_responsabile = ?, numero_prese_rete = ?, numero_prese_elettriche = ?, note = ?, version=?"
                    + " WHERE Id = ? AND version=?");
            dAula = connection.prepareStatement("DELETE FROM aula WHERE Id=?");

        } catch (SQLException e) {
            throw new DataException("Errore in init() AulaDaoMySQL", e);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            sAula.close();
            sAulaByEvento.close();
            sAulaById.close();
            sAule.close();
            sAuleByGruppo.close();
            sAuleByNPreseRete.close();
            sAuleByNPreseElettriche.close();
            sAuleByCapienza.close();
            sAuleByPiano.close();
            sAuleByLuogo.close();
            sAuleByEdificio.close();
            sResponsabili.close();
            iGruppo.close();
            dGruppo.close();
            iAula.close();
            uAula.close();
            dAula.close();
        } catch (SQLException e) {
            throw new DataException("Errore in destroy() AulaDaoMySQL", e);
        }
        super.destroy();
    }

    @Override
    public Aula createAula() {
        return new AulaProxy(getDataLayer());
    }

    private AulaProxy createAula(ResultSet rs) throws DataException {
        AulaProxy a = (AulaProxy) createAula();
        try {
            a.setKey(rs.getInt("Id"));
            a.setNome(rs.getString("nome"));
            a.setCapienza(rs.getInt("capienza"));
            a.setEdificio(rs.getString("edificio"));
            a.setEmailResponsabile(rs.getString("email_responsabile"));
            a.setLuogo(rs.getString("luogo"));
            a.setNote(rs.getString("note"));
            a.setN_PreseElettriche(rs.getInt("n_prese_elettriche"));
            a.setN_PreseRete(rs.getInt("n_prese_rete"));
            a.setPiano(rs.getInt("piano"));
            a.setVersion(rs.getLong("version"));
            // List<Attrezzatura_Relazione> attrRel = null;
            // attrRel = attrezzaturaRelazioneDAO.getListaAttrezzaturaByAula(a);
            // a.setListaAttrezzatura(attrRel);

        } catch (SQLException ex) {
            throw new DataException("Errore in createAula() ", ex);
        }
        return a;
    }

    @Override
    public Aula getAulaById(int aula_key) throws DataException {
        Aula a = null;
        if (dataLayer.getCache().has(Aula.class, aula_key)) {
            a = dataLayer.getCache().get(Aula.class, aula_key);
        } else {
            try {
                sAulaById.setInt(1, aula_key);
                try (ResultSet rs = sAulaById.executeQuery()) {
                    if (rs.next()) {
                        a = createAula(rs);
                        dataLayer.getCache().add(Aula.class, a);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Errore in getAulaById() ", ex);
            }
        }
        return a;
    }

    @Override
    public Aula getAulaByPosizione(String edificio, String luogo, int piano, String nome) throws DataException {
        Aula a = null;
        try {
            sAula.setString(1, nome);
            sAula.setString(2, edificio);
            sAula.setString(3, luogo);
            sAula.setInt(4, piano);
            try (ResultSet rs = sAula.executeQuery()) {
                if (rs.next()) {
                    a = createAula(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore in getAulaByPosizione() ", ex);
        }
        return a;
    }

    @Override
    public Aula getAulaByEvento(Evento evento) throws DataException {
        Aula a = null;
        try {
            sAulaByEvento.setInt(1, evento.getKey());

            try (ResultSet rs = sAulaByEvento.executeQuery()) {
                if (rs.next()) {
                    a = createAula(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore in getAulaByEvento() ", ex);
        }
        return a;
    }

    @Override
    public List<Aula> getListaAule() throws DataException {
        List<Aula> aule = new ArrayList<>();
        try (ResultSet rs = sAule.executeQuery()) {
            while (rs.next()) {
                aule.add((Aula) getAulaById(rs.getInt("aulaId")));
            }
        } catch (SQLException ex) {
            throw new DataException("Errore in getListaAule() ", ex);
        }
        return aule;
    }

    @Override
    public List<Aula> getListaAuleByGruppo(Gruppo gruppo) throws DataException {
        List<Aula> aule = new ArrayList<>();
        try {
            sAuleByGruppo.setInt(1, gruppo.getKey());
            try (ResultSet rs = sAuleByGruppo.executeQuery()) {
                while (rs.next()) {
                    aule.add((Aula) getAulaById(rs.getInt("aulaId")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore in getListaAuleByGruppo() ", ex);
        }
        return aule;
    }

    @Override
    public List<Aula> getListaAuleByPreseRete(int numeroPreseRete) throws DataException {
        List<Aula> aule = new ArrayList<>();
        try {
            sAuleByNPreseElettriche.setInt(1, numeroPreseRete);
            try (ResultSet rs = sAuleByNPreseElettriche.executeQuery()) {
                while (rs.next()) {
                    aule.add((Aula) getAulaById(rs.getInt("aulaID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore in getListaAuleByPreseRete() ", ex);
        }

        return aule;
    }

    @Override
    public List<Aula> getListaAuleByPreseElettriche(int numeroPreseElettriche) throws DataException {
        List<Aula> aule = new ArrayList<>();
        try {
            sAuleByNPreseRete.setInt(1, numeroPreseElettriche);
            try (ResultSet rs = sAuleByNPreseRete.executeQuery()) {
                while (rs.next()) {
                    aule.add((Aula) getAulaById(rs.getInt("aulaId")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore in getListaAuleByPreseElettriche() ", ex);
        }
        return aule;
    }

    @Override
    public List<Aula> getListaAuleByCapienza(int capienza) throws DataException {
        List<Aula> aule = new ArrayList<>();
        try {
            sAuleByCapienza.setInt(1, capienza);
            try (ResultSet rs = sAuleByCapienza.executeQuery()) {
                while (rs.next()) {
                    aule.add((Aula) getAulaById(rs.getInt("aulaId")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore in getListaAuleByCapienza() ", ex);
        }
        return aule;
    }

    @Override
    public List<Aula> getListaAuleByPiano(int piano) throws DataException {
        List<Aula> aule = new ArrayList<Aula>();
        try {
            sAuleByPiano.setInt(1, piano);
            try (ResultSet rs = sAuleByPiano.executeQuery()) {
                while (rs.next()) {
                    aule.add((Aula) getAulaById(rs.getInt("aulaId")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore in getListaAuleByPiano() ", ex);
        }
        return aule;
    }

    @Override
    public List<Aula> getListaAuleByLuogo(String luogo) throws DataException {
        List<Aula> aule = new ArrayList<Aula>();
        try {
            sAuleByLuogo.setString(1, luogo);
            try (ResultSet rs = sAuleByLuogo.executeQuery()) {
                while (rs.next()) {
                    aule.add((Aula) getAulaById(rs.getInt("aulaId")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore in getListaAuleByLuogo() ", ex);
        }
        return aule;
    }

    @Override
    public List<Aula> getListaAuleByEdificio(String edificio) throws DataException {
        List<Aula> aule = new ArrayList<Aula>();
        try {
            sAuleByEdificio.setString(1, edificio);
            try (ResultSet rs = sAuleByEdificio.executeQuery()) {
                while (rs.next()) {
                    aule.add((Aula) getAulaById(rs.getInt("aulaId")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore in getListaAuleByEdificio() ", ex);
        }
        return aule;
    }

    @Override
    public List<String> getListaResponsabili() throws DataException {
        List<String> resp = new ArrayList<String>();
        try {
            try (ResultSet rs = sResponsabili.executeQuery()) {
                while (rs.next()) {
                    resp.add((String) rs.getString("email_responsabile"));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore in getListaResponsabili() ", ex);
        }
        return resp;
    }

    @Override
    public void setGruppo(Gruppo gruppo, Aula aula) throws DataException {
        try {
            iGruppo.setInt(1, aula.getKey());
            iGruppo.setInt(2, gruppo.getKey());
            if (iGruppo.executeUpdate() == 1) {
                try (ResultSet keys = iAula.getGeneratedKeys()) {
                    if (keys.next()) {
                        aula.addGruppo(gruppo);
                        dataLayer.getCache().add(Aula.class, aula);
                        dataLayer.getCache().add(Gruppo.class, gruppo);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore in setGruppo() ", ex);
        }
    }

    // funziona sia da insert che da update
    @Override
    public void storeAula(Aula aula) throws DataException {
        try {
            if (aula.getKey() != null && aula.getKey() > 0) {
                // update
                if (aula instanceof DataItemProxy && !((DataItemProxy) aula).isModified()) {
                    return;
                }
                uAula.setString(1, aula.getNome());
                uAula.setString(2, aula.getLuogo());
                uAula.setString(3, aula.getEdificio());
                uAula.setInt(4, aula.getPiano());
                uAula.setInt(5, aula.getCapienza());
                uAula.setString(6, aula.getEmailResponsabile());
                uAula.setInt(7, aula.getN_PreseRete());
                uAula.setInt(8, aula.getN_PreseElettriche());
                uAula.setString(9, aula.getNote());
                long current_version = aula.getVersion();
                long next_version = current_version + 1;
                uAula.setLong(10, next_version);
                uAula.setInt(11, aula.getKey());
                uAula.setLong(12, current_version);

                if (uAula.executeUpdate() == 0) {
                    throw new OptimisticLockException(aula);
                } else {
                    aula.setVersion(next_version);
                }
            } else {
                // insert
                iAula.setString(1, aula.getNome());
                iAula.setString(2, aula.getLuogo());
                iAula.setString(3, aula.getEdificio());
                iAula.setInt(4, aula.getPiano());
                iAula.setInt(5, aula.getCapienza());
                iAula.setString(6, aula.getEmailResponsabile());
                iAula.setInt(7, aula.getN_PreseRete());
                iAula.setInt(8, aula.getN_PreseElettriche());
                iAula.setString(9, aula.getNote());

                if (iAula.executeUpdate() == 1) {
                    try (ResultSet keys = iAula.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            aula.setKey(key);
                            dataLayer.getCache().add(Aula.class, aula);
                        }
                    }
                }
            }

            if (aula instanceof DataItemProxy) {
                ((DataItemProxy) aula).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Errore in storeAula() ", ex);
        }
    }

    @Override
    public void deleteAula(Aula aula) throws DataException {
        if (aula.getKey() == null || aula.getKey() <= 0) {
            return;
        }
        try {
            dAula.setInt(1, aula.getKey());
            if (dAula.executeUpdate() == 0) {
                throw new OptimisticLockException(aula);
            } else if (dataLayer.getCache().has(Aula.class, aula.getKey())) {
                dataLayer.getCache().delete(Aula.class, aula.getKey());
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Errore in deleteAula() ", ex);
        }
    }

}
