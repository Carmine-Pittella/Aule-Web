
package it.univaq.f4i.iw.Aule_Web.data.dao;

import it.univaq.f4i.iw.Aule_Web.data.model.Amministratore;
import it.univaq.f4i.iw.framework.data.DataException;

/**
 *
 * @author Carmine
 */

public interface AmministratoreDao {

    Amministratore createAmministratore();

    Amministratore getAmministratore(int amministratore_key) throws DataException;

    Amministratore getAmministratoreByEmail(String email) throws DataException;

    void storeAmministratore(Amministratore amministratore) throws DataException;

    void deleteAmministratore(Amministratore amministratore) throws DataException;

}
