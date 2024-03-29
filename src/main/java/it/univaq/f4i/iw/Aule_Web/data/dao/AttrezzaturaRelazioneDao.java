
package it.univaq.f4i.iw.Aule_Web.data.dao;

import java.util.List;

import it.univaq.f4i.iw.Aule_Web.data.model.Attrezzatura_Relazione;
import it.univaq.f4i.iw.Aule_Web.data.model.Aula;
import it.univaq.f4i.iw.framework.data.DataException;

/**
 *
 * @author Carmine
 */

public interface AttrezzaturaRelazioneDao {

    Attrezzatura_Relazione createAttrezzaturaRelazione();

    Integer ifExistAulaAttrezzo(Integer id_aula, Integer id_attrezzo) throws DataException;

    void storeAttrezzaturaRelazione(Attrezzatura_Relazione attrezzatura_relazione) throws DataException;

    void deleteAttrezzaturaRelazione(Attrezzatura_Relazione attrezzatura_relazione) throws DataException;

    Attrezzatura_Relazione getAttrezzaturaRelazioneById(int attrezzatura_relazione_key) throws DataException;

    List<Attrezzatura_Relazione> getListaAttrezzaturaRelazione() throws DataException;

    List<Attrezzatura_Relazione> getListaAttrezzaturaByAula(Aula aula) throws DataException;

}
