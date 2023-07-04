
package it.univaq.f4i.iw.Aule_Web.data.dao;

import java.util.List;

import it.univaq.f4i.iw.Aule_Web.data.model.Aula;
import it.univaq.f4i.iw.Aule_Web.data.model.Gruppo;
import it.univaq.f4i.iw.framework.data.DataException;

/**
 *
 * @author Carmine
 */

public interface GruppoDao {

    Gruppo createGruppo();

    Gruppo getGruppoById(int gruppo_key) throws DataException;

    Gruppo getGruppoByNome(String nome) throws DataException;

    List<Gruppo> getGruppi() throws DataException;

    List<Gruppo> getGruppiByAula(Aula aula) throws DataException;

    void storeGruppo(Gruppo gruppo) throws DataException;

    void deleteGruppo(Gruppo gruppo) throws DataException;

}
