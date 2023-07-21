
package it.univaq.f4i.iw.Aule_Web.data.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.univaq.f4i.iw.Aule_Web.data.model.Attrezzatura;
import it.univaq.f4i.iw.Aule_Web.data.model.Attrezzatura_Relazione;
import it.univaq.f4i.iw.Aule_Web.data.model.Aula;
import it.univaq.f4i.iw.Aule_Web.data.proxy.AttrezzaturaRelazioneProxy;
import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.framework.data.OptimisticLockException;

/**
 *
 * @author Carmine
 */

public class AttrezzaturaRelazioneDaoMySQL extends DAO implements AttrezzaturaRelazioneDao {

    private PreparedStatement selectAttrezzaturaRelazioneById,
            selectIdAttrezzaturaRelazione,
            ifExistAulaAttrezzo,
            insertAttrezzaturaRelazione,
            updateAttrezzaturaRelazione,
            deleteAttrezzaturaRelazione;

    private final AttrezzaturaDao attrezzaturaDao;

    public AttrezzaturaRelazioneDaoMySQL(DataLayer d) {
        super(d);
        attrezzaturaDao = (AttrezzaturaDao) d.getDAO(Attrezzatura.class);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();
            selectAttrezzaturaRelazioneById = connection
                    .prepareStatement("SELECT * FROM attrezzatura_relazione WHERE id = ?");
            selectIdAttrezzaturaRelazione = connection
                    .prepareStatement("SELECT Id AS idAttRel FROM attrezzatura_relazione");
            ifExistAulaAttrezzo = connection
                    .prepareStatement(
                            "SELECT Id AS idAttRel FROM attrezzatura_relazione WHERE id_aula=? AND id_attrezzo=?");
            insertAttrezzaturaRelazione = connection.prepareStatement(
                    "INSERT INTO attrezzatura_relazione (id_aula, id_attrezzo, quantita, version) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            updateAttrezzaturaRelazione = connection.prepareStatement(
                    "UPDATE attrezzatura_relazione SET id_aula=?, id_attrezzo=?, quantita=?, version=? WHERE Id=?",
                    Statement.RETURN_GENERATED_KEYS);

            deleteAttrezzaturaRelazione = connection
                    .prepareStatement(
                            "DELETE FROM attrezzatura_relazione WHERE Id=?");
        } catch (Exception e) {
            throw new DataException("Errore in init() AttrezzaturaRelazioneDaoMySQL", e);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            selectAttrezzaturaRelazioneById.close();
            selectIdAttrezzaturaRelazione.close();
            insertAttrezzaturaRelazione.close();
            ifExistAulaAttrezzo.close();
            updateAttrezzaturaRelazione.close();
            deleteAttrezzaturaRelazione.close();

        } catch (Exception e) {
            throw new DataException("Errore in destroy() AttrezzaturaRelazioneDaoMySQL", e);
        }
        super.destroy();
    }

    @Override
    public Attrezzatura_Relazione createAttrezzaturaRelazione() {
        return new AttrezzaturaRelazioneProxy(getDataLayer());
    }

    private AttrezzaturaRelazioneProxy createAttrezzaturaRelazione(ResultSet rs) throws DataException {
        AttrezzaturaRelazioneProxy a = (AttrezzaturaRelazioneProxy) createAttrezzaturaRelazione();
        try {
            a.setKey(rs.getInt("id"));
            a.setIdAula(rs.getInt("id_aula"));
            Attrezzatura attrezzo = attrezzaturaDao.getAttrezzaturaById(rs.getInt("id_attrezzo"));
            a.setAttrezzo(attrezzo);
            a.setQuantita(rs.getInt("quantita"));
            a.setVersion(rs.getInt("version"));
        } catch (SQLException e) {
            throw new DataException("Errore durante la creazione di un'Attrezzatura_Relazione ", e);
        }
        return a;
    }

    @Override
    public Attrezzatura_Relazione getAttrezzaturaRelazioneById(int attrezzatura_relazione_key) throws DataException {
        Attrezzatura_Relazione attrezzatura_relazione = null;
        if (dataLayer.getCache().has(Attrezzatura.class, attrezzatura_relazione_key)) {
            attrezzatura_relazione = dataLayer.getCache().get(Attrezzatura_Relazione.class, attrezzatura_relazione_key);
        } else {
            try {
                selectAttrezzaturaRelazioneById.setInt(1, attrezzatura_relazione_key);
                try (ResultSet rs = selectAttrezzaturaRelazioneById.executeQuery()) {
                    if (rs.next()) {
                        attrezzatura_relazione = createAttrezzaturaRelazione(rs);
                        dataLayer.getCache().add(Attrezzatura_Relazione.class, attrezzatura_relazione);
                    }
                }
            } catch (SQLException e) {
                throw new DataException("Nessuna Attrezzatura trovata ", e);
            }
        }
        return attrezzatura_relazione;
    }

    @Override
    public List<Attrezzatura_Relazione> getListaAttrezzaturaRelazione() throws DataException {
        List<Attrezzatura_Relazione> lista = new ArrayList<Attrezzatura_Relazione>();
        try {
            try (ResultSet rs = selectIdAttrezzaturaRelazione.executeQuery()) {
                while (rs.next()) {
                    lista.add((Attrezzatura_Relazione) getAttrezzaturaRelazioneById(rs.getInt("idAttRel")));
                }
            }
        } catch (SQLException e) {
            throw new DataException("Errore in getListaAttrezzaturaByAula() ", e);
        }
        return lista;
    }

    @Override
    public List<Attrezzatura_Relazione> getListaAttrezzaturaByAula(Aula aula) throws DataException {
        List<Attrezzatura_Relazione> listaAttrezzatura = new ArrayList<Attrezzatura_Relazione>();
        try {
            List<Attrezzatura_Relazione> listaCompleta = getListaAttrezzaturaRelazione();
            for (int i = 0; i < listaCompleta.size(); i++) {
                if (listaCompleta.get(i).getIdAula() == aula.getKey()) {
                    listaAttrezzatura.add(listaCompleta.get(i));
                }
            }
        } catch (Exception e) {
            throw new DataException("Errore in getListaAttrezzaturaByAula() ", e);
        }
        return listaAttrezzatura;
    }

    // funziona sia da insert che da update
    @Override
    public void storeAttrezzaturaRelazione(Attrezzatura_Relazione attrezzatura_relazione) throws DataException {
        try {
            if (attrezzatura_relazione.getKey() != null && attrezzatura_relazione.getKey() > 0) {
                // update
                if (attrezzatura_relazione instanceof DataItemProxy
                        && !((DataItemProxy) attrezzatura_relazione).isModified()) {
                    return;
                }
                updateAttrezzaturaRelazione.setInt(1, attrezzatura_relazione.getAula().getKey());
                updateAttrezzaturaRelazione.setInt(2, attrezzatura_relazione.getAttrezzo().getKey());
                updateAttrezzaturaRelazione.setInt(3, attrezzatura_relazione.getQuantita());
                updateAttrezzaturaRelazione.setLong(4, attrezzatura_relazione.getVersion() + 1); // next
                updateAttrezzaturaRelazione.setInt(5, attrezzatura_relazione.getKey());

                if (updateAttrezzaturaRelazione.executeUpdate() == 0) {
                    throw new OptimisticLockException(attrezzatura_relazione);
                } else {
                    attrezzatura_relazione.setVersion(attrezzatura_relazione.getVersion() + 1);
                }

            } else {
                // insert
                insertAttrezzaturaRelazione.setInt(1, attrezzatura_relazione.getAula().getKey());
                insertAttrezzaturaRelazione.setInt(2, attrezzatura_relazione.getAttrezzo().getKey());
                insertAttrezzaturaRelazione.setInt(3, attrezzatura_relazione.getQuantita());
                insertAttrezzaturaRelazione.setInt(4, 1);
                // version default = 1

                if (insertAttrezzaturaRelazione.executeUpdate() == 1) {
                    try (ResultSet keys = insertAttrezzaturaRelazione.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            attrezzatura_relazione.setKey(key);
                            dataLayer.getCache().add(Attrezzatura_Relazione.class, attrezzatura_relazione);
                        }
                    }
                }
            }
        } catch (SQLException | OptimisticLockException e) {
            throw new DataException("Errore in storeAttrezzaturaRelazione() ", e);
        }
    }

    @Override
    public void deleteAttrezzaturaRelazione(Attrezzatura_Relazione attrezzatura_relazione) throws DataException {
        if (attrezzatura_relazione.getKey() == null || attrezzatura_relazione.getKey() <= 0) {
            return;
        }
        try {
            deleteAttrezzaturaRelazione.setInt(1, attrezzatura_relazione.getKey());
            if (deleteAttrezzaturaRelazione.executeUpdate() == 0) {
                throw new OptimisticLockException(attrezzatura_relazione);

            } else if (dataLayer.getCache().has(Attrezzatura.class, attrezzatura_relazione.getKey())) {
                dataLayer.getCache().delete(Attrezzatura.class, attrezzatura_relazione.getKey());
            }
        } catch (SQLException | OptimisticLockException e) {
            throw new DataException("Errore in deleteAttrezzaturaRelazione() ", e);
        }
    }

    @Override
    public Integer ifExistAulaAttrezzo(Integer id_aula, Integer id_attrezzo) throws DataException {
        try {
            ifExistAulaAttrezzo.setInt(1, id_aula);
            ifExistAulaAttrezzo.setInt(2, id_attrezzo);
            try (ResultSet rs = ifExistAulaAttrezzo.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idAttRel");
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataException("Nessuna AttrezzaturaRelazione trovata ", e);
        }
    }

}
