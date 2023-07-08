
package it.univaq.f4i.iw.Aule_Web.data.dao;

import java.util.List;

import it.univaq.f4i.iw.Aule_Web.data.model.Aula;
import it.univaq.f4i.iw.Aule_Web.data.model.Evento;
import it.univaq.f4i.iw.Aule_Web.data.model.Gruppo;
import it.univaq.f4i.iw.framework.data.DataException;

/**
 *
 * @author Carmine
 */

public interface AulaDao {

    Aula createAula();

    Aula getAulaById(int aula_key) throws DataException;

    Aula getAulaByNome(String nome) throws DataException;

    Aula getAulaByPosizione(String edificio, String luogo, int piano, String nome) throws DataException;

    Aula getAulaByEvento(Evento evento) throws DataException;

    List<Aula> getListaAule() throws DataException;

    List<Aula> getListaAuleByGruppo(Gruppo gruppo) throws DataException;

    List<Aula> getListaAuleByPreseRete(int numeroPreseRete) throws DataException;

    List<Aula> getListaAuleByPreseElettriche(int numeroPreseElettriche) throws DataException;

    List<Aula> getListaAuleByCapienza(int capienza) throws DataException;

    List<Aula> getListaAuleByPiano(int piano) throws DataException;

    List<Aula> getListaAuleByLuogo(String luogo) throws DataException;

    List<Aula> getListaAuleByEdificio(String edificio) throws DataException;

    List<String> getListaResponsabili() throws DataException;

    void setGruppo(Gruppo gruppo, Aula aula) throws DataException;

    void storeAula(Aula aula) throws DataException;

    void deleteAula(Aula aula) throws DataException;

}
