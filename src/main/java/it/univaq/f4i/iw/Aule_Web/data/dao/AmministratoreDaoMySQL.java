
package it.univaq.f4i.iw.Aule_Web.data.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import it.univaq.f4i.iw.Aule_Web.data.model.Amministratore;
import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;

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
                    "INSERT INTO amministratore (nome,cognome,email,password,telefono) VALUES(?,?,?,?,?)",
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
        // non posso continuare perché mi mancano i proxy
    }

    private AmministratoreProxy createAmministratore(ResultSet rs) throws DataException {

    }

    @Override
    public Amministratore getAmministratore(int amministratore_key) throws DataException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAmministratore'");
    }

    @Override
    public Amministratore getAmministratoreByEmail(String email) throws DataException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAmministratoreByEmail'");
    }

    @Override
    public void storeAmministratore(Amministratore amministratore) throws DataException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'storeAmministratore'");
    }

    @Override
    public void deleteAmministratore(Amministratore amministratore) throws DataException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAmministratore'");
    }

}
