
package it.univaq.f4i.iw.Aule_Web.data.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.univaq.f4i.iw.Aule_Web.data.model.Attrezzatura;
import it.univaq.f4i.iw.Aule_Web.data.model.Aula;
import it.univaq.f4i.iw.Aule_Web.data.proxy.AttrezzaturaProxy;
import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.framework.data.OptimisticLockException;

/**
 *
 * @author Carmine
 */

public class AttrezzaturaDaoMySQL extends DAO implements AttrezzaturaDao {

    private PreparedStatement selectAttrezzaturaById, insertAttrezzatura, updateAttrezzatura, deleteAttrezzaturabyId,
            selectIdAttrezzatura, selectAttrezzaturaByAula;

    public AttrezzaturaDaoMySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();
            selectAttrezzaturaById = connection.prepareStatement("SELECT * FROM attrezzatura WHERE id = ?");
            selectAttrezzaturaByAula = connection
                    .prepareStatement("SELECT ID AS attrezzaturaID FROM attrezzatura WHERE ID_aula=?");
            insertAttrezzatura = connection.prepareStatement(
                    "INSERT INTO attrezzatura (nome_attrezzo, descrizione, version) VALUES (?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);

            updateAttrezzatura = connection
                    .prepareStatement("UPDATE attrezzatura SET nome_attrezzo=?, descrizione=?, version=? WHERE Id=?");
            deleteAttrezzaturabyId = connection.prepareStatement("DELETE FROM attrezzatura WHERE Id=?");
            selectIdAttrezzatura = connection.prepareStatement("SELECT Id AS idAttrezzatura FROM attrezzatura");

        } catch (Exception e) {
            throw new DataException("Errore in init() AttrezzaturaDaoMySQL", e);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            selectAttrezzaturaById.close();
            selectAttrezzaturaByAula.close();
            insertAttrezzatura.close();
            updateAttrezzatura.close();
            deleteAttrezzaturabyId.close();
            selectIdAttrezzatura.close();
        } catch (Exception e) {
            throw new DataException("Errore in destroy() AttrezzaturaDaoMySQL", e);
        }
        super.destroy();
    }

    @Override
    public Attrezzatura createAttrezzatura() {
        return new AttrezzaturaProxy(getDataLayer());
    }

    private AttrezzaturaProxy createAttrezzatura(ResultSet rs) throws DataException {
        AttrezzaturaProxy a = (AttrezzaturaProxy) createAttrezzatura();
        try {
            a.setKey(rs.getInt("id"));
            a.setNomeAttrezzo(rs.getString("nome_attrezzo"));
            a.setDescrizione(rs.getString("descrizione"));
            a.setVersion(rs.getLong("version"));
        } catch (SQLException e) {
            throw new DataException("Errore durante la creazione di un'Attrezzatura ", e);
        }
        return a;
    }

    @Override
    public Attrezzatura getAttrezzaturaById(int attrezzatura_key) throws DataException {
        Attrezzatura attrezzatura = null;
        if (dataLayer.getCache().has(Attrezzatura.class, attrezzatura_key)) {
            attrezzatura = dataLayer.getCache().get(Attrezzatura.class, attrezzatura_key);
        } else {
            try {
                selectAttrezzaturaById.setInt(1, attrezzatura_key);
                try (ResultSet rs = selectAttrezzaturaById.executeQuery()) {
                    if (rs.next()) {
                        attrezzatura = createAttrezzatura(rs);
                        dataLayer.getCache().add(Attrezzatura.class, attrezzatura);
                    }
                }
            } catch (SQLException e) {
                throw new DataException("Nessuna Attrezzatura trovata ", e);
            }
        }
        return attrezzatura;
    }

    @Override
    public List<Attrezzatura> getListaAttrezzatura() throws DataException {
        List<Attrezzatura> listaAttrezzatura = new ArrayList<Attrezzatura>();
        try {
            try (ResultSet rs = selectIdAttrezzatura.executeQuery()) {
                while (rs.next()) {
                    listaAttrezzatura.add((Attrezzatura) getAttrezzaturaById(rs.getInt("idAttrezzatura")));
                }
            }
        } catch (SQLException e) {
            throw new DataException("Errore in getListaAttrezzatura() ", e);
        }
        return listaAttrezzatura;
    }

    public List<Attrezzatura> getListaAttrezzaturaByAula(Aula aula) throws DataException {
        List<Attrezzatura> result = new ArrayList<Attrezzatura>();
        try {
            selectAttrezzaturaByAula.setInt(1, aula.getKey());
            try (ResultSet rs = selectAttrezzaturaByAula.executeQuery()) {
                while (rs.next()) {
                    result.add((Attrezzatura) getAttrezzaturaById(rs.getInt("idAttrezzatura")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore in getListaAttrezzaturaByAula()", ex);
        }
        return result;
    }

    // funziona sia da insert che da update
    @Override
    public void storeAttrezzatura(Attrezzatura attrezzatura) throws DataException {
        try {
            if (attrezzatura.getKey() != null && attrezzatura.getKey() > 0) {
                // update
                if (attrezzatura instanceof DataItemProxy && !((DataItemProxy) attrezzatura).isModified()) {
                    return;
                }
                updateAttrezzatura.setString(1, attrezzatura.getNomeAttrezzo());
                updateAttrezzatura.setString(2, attrezzatura.getDescrizione());
                updateAttrezzatura.setLong(3, attrezzatura.getVersion() + 1); // next
                updateAttrezzatura.setInt(4, attrezzatura.getKey());

                if (updateAttrezzatura.executeUpdate() == 0) {
                    throw new OptimisticLockException(attrezzatura);
                } else {
                    attrezzatura.setVersion(attrezzatura.getVersion() + 1);
                }
            } else {
                // insert
                insertAttrezzatura.setString(1, attrezzatura.getNomeAttrezzo());
                insertAttrezzatura.setString(2, attrezzatura.getDescrizione());
                insertAttrezzatura.setInt(3, 1);
                // version default = 1

                if (insertAttrezzatura.executeUpdate() == 1) {
                    try (ResultSet keys = insertAttrezzatura.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            attrezzatura.setKey(key);
                            dataLayer.getCache().add(Attrezzatura.class, attrezzatura);
                        }
                    }
                }
            }
            if (attrezzatura instanceof DataItemProxy) {
                ((DataItemProxy) attrezzatura).setModified(false);
            }
        } catch (SQLException | OptimisticLockException e) {
            throw new DataException("Errore in storeAttrezzatura() ", e);
        }
    }

    @Override
    public void deleteAttrezzatura(Attrezzatura attrezzatura) throws DataException {
        if (attrezzatura.getKey() == null || attrezzatura.getKey() <= 0) {
            return;
        }
        try {
            deleteAttrezzaturabyId.setInt(1, attrezzatura.getKey());
            if (deleteAttrezzaturabyId.executeUpdate() == 0) {
                throw new OptimisticLockException(attrezzatura);

            } else if (dataLayer.getCache().has(Attrezzatura.class, attrezzatura.getKey())) {
                dataLayer.getCache().delete(Attrezzatura.class, attrezzatura.getKey());
            }

        } catch (SQLException | OptimisticLockException e) {
            throw new DataException("Errore in deleteAttrezzatura() ", e);
        }
    }

}
