
package it.univaq.f4i.iw.Aule_Web.data.dao;

import java.util.List;

import it.univaq.f4i.iw.Aule_Web.data.model.Attrezzatura;
import it.univaq.f4i.iw.Aule_Web.data.model.Aula;
import it.univaq.f4i.iw.framework.data.DataException;

/**
 *
 * @author Carmine
 */

public interface AttrezzaturaDao {

    Attrezzatura createAttrezzatura();

    Attrezzatura getAttrezzatura(int attrezzatura_key) throws DataException;

    List<Attrezzatura> getAttrezzatura() throws DataException;

    void storeAttrezzatura(Attrezzatura attrezzatura) throws DataException;

    void deleteAttrezzatura(Attrezzatura attrezzatura) throws DataException;

}
