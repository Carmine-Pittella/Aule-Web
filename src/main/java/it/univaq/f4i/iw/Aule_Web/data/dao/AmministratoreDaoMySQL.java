
package it.univaq.f4i.iw.Aule_Web.data.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import it.univaq.f4i.iw.Aule_Web.data.model.Amministratore;
import it.univaq.f4i.iw.Aule_Web.data.proxy.AmministratoreProxy;
import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.framework.data.OptimisticLockException;

/**
 *
 * @author Carmine
 */

public class AmministratoreDaoMySQL extends DAO implements AmministratoreDao {

    private PreparedStatement selectAdminById, selectAdminByEmail, insertAdmin, updateAdmin, deleteAdminbyId;

    public AmministratoreDaoMySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();
            selectAdminById = connection.prepareStatement("SELECT * FROM amministratore WHERE Id = ?");
            selectAdminByEmail = connection.prepareStatement("SELECT * FROM amministratore WHERE email = ?");
            insertAdmin = connection.prepareStatement(
                    "INSERT INTO amministratore (nome, cognome, email, password, version) VALUES(?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            updateAdmin = connection.prepareStatement(
                    "UPDATE amministratore SET nome=?, cognome=?, email=?, password=?, version=? WHERE ID=? and version=?");
            deleteAdminbyId = connection.prepareStatement("DELETE FROM amministratore WHERE Id = ?");

        } catch (SQLException e) {
            throw new DataException("Errore in init() AmministratoreDaoMySQL", e);
        }

    }

    @Override
    public void destroy() throws DataException {

        try {
            selectAdminById.close();
            selectAdminByEmail.close();
            insertAdmin.close();
            updateAdmin.close();
            deleteAdminbyId.close();
        } catch (Exception e) {
            throw new DataException("Errore in destroy() AmministratoreDaoMySQL", e);
        }
        super.destroy();
    }

    @Override
    public Amministratore createAmministratore() {
        return new AmministratoreProxy(getDataLayer());
    }

    private AmministratoreProxy createAmministratore(ResultSet rs) throws DataException {
        AmministratoreProxy a = (AmministratoreProxy) createAmministratore();
        try {
            a.setKey(rs.getInt("Id"));
            a.setNome(rs.getString("nome"));
            a.setCognome(rs.getString("cognome"));
            a.setEmail(rs.getString("email"));
            a.setPassword(rs.getString("password"));
            a.setVersion(rs.getLong("version"));
        } catch (SQLException ex) {
            throw new DataException("Errore durante la creazione di un Amministratore ", ex);
        }
        return a;
    }

    @Override
    public Amministratore getAmministratore(int admin_key) throws DataException {
        Amministratore admin = null;
        if (dataLayer.getCache().has(Amministratore.class, admin_key)) {
            admin = dataLayer.getCache().get(Amministratore.class, admin_key);
        } else {
            try {
                selectAdminById.setInt(1, admin_key);
                try (ResultSet rs = selectAdminById.executeQuery()) {
                    if (rs.next()) {
                        admin = createAmministratore(rs);
                        dataLayer.getCache().add(Amministratore.class, admin);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Nessun Amministratore trovato ", ex);
            }
        }
        return admin;
    }

    @Override
    public Amministratore getAmministratoreByEmail(String email) throws DataException {
        try {
            selectAdminByEmail.setString(1, email);
            try (ResultSet rs = selectAdminByEmail.executeQuery()) {
                if (rs.next()) {
                    return getAmministratore(rs.getInt("Id"));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load amministratore by email", ex);
        }

        return null;
    }

    // funziona sia da insert che da update
    @Override
    public void storeAmministratore(Amministratore admin) throws DataException {
        try {
            if (admin.getKey() != null && admin.getKey() > 0) {
                // update
                if (admin instanceof DataItemProxy && !((DataItemProxy) admin).isModified()) {
                    return;
                }
                updateAdmin.setString(1, admin.getNome());
                updateAdmin.setString(2, admin.getCognome());
                updateAdmin.setString(3, admin.getEmail());
                updateAdmin.setString(4, admin.getPassword());

                long current_version = admin.getVersion();
                long next_version = current_version++;

                updateAdmin.setLong(6, next_version);
                updateAdmin.setInt(7, admin.getKey());
                updateAdmin.setLong(8, current_version);

                if (updateAdmin.executeUpdate() == 0) {
                    throw new OptimisticLockException(admin);
                } else {
                    admin.setVersion(next_version);
                }
            } else {
                // insert
                insertAdmin.setString(1, admin.getNome());
                insertAdmin.setString(2, admin.getCognome());
                insertAdmin.setString(3, admin.getEmail());
                insertAdmin.setString(4, admin.getPassword());

                if (insertAdmin.executeUpdate() == 1) {
                    try (ResultSet keys = insertAdmin.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            admin.setKey(key);
                            dataLayer.getCache().add(Amministratore.class, admin);
                        }
                    }
                }
            }

            if (admin instanceof DataItemProxy) {
                ((DataItemProxy) admin).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Erroredurante l'inserimento di un Amministratore ", ex);
        }

    }

    @Override
    public void deleteAmministratore(Amministratore admin) throws DataException {
        if (admin.getKey() == null || admin.getKey() <= 0) {
            return;
        }
        try {
            deleteAdminbyId.setInt(1, admin.getKey());
            if (deleteAdminbyId.executeUpdate() == 0) {
                throw new OptimisticLockException(admin);
            } else if (dataLayer.getCache().has(Amministratore.class, admin.getKey())) {
                dataLayer.getCache().delete(Amministratore.class, admin.getKey());
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Errore durante la cancellazione di un Amminitratore ", ex);
        }
    }

}
