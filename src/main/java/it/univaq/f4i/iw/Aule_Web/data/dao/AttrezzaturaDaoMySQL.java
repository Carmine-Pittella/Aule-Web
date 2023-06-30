/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.Aule_Web.data.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import it.univaq.f4i.iw.Aule_Web.data.model.Attrezzatura;
import it.univaq.f4i.iw.Aule_Web.data.proxy.AttrezzaturaProxy;
import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;

/**
 *
 * @author Carmine
 */
public class AttrezzaturaDaoMySQL extends DAO implements AttrezzaturaDao {

    private PreparedStatement selectAttrezzaturaById, insertAttrezzatura, updateAttrezzatura, deleteAttrezzaturabyId;

    public AttrezzaturaDaoMySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();
            selectAttrezzaturaById = connection.prepareStatement("SELECT * FROM attrezzatura WHERE id = ?");
            insertAttrezzatura = connection.prepareStatement(
                    "INSERT INTO attrezzatura (nome_attrezzo, descrizione, version) VALUES (?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            updateAttrezzatura = connection
                    .prepareStatement("UPDATE attrezzatura SET nome_attrezzo=?, descrizione=?, version=? WHERE Id=?");
            deleteAttrezzaturabyId = connection.prepareStatement("DELETE FROM attrezzatura WHERE Id=?");

        } catch (Exception e) {
            throw new DataException("Errore in init() AttrezzaturaDaoMySQL", e);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            selectAttrezzaturaById.close();
            insertAttrezzatura.close();
            updateAttrezzatura.close();
            deleteAttrezzaturabyId.close();
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
            a.setKey(rs.getInt("nome_attrezzatura"));
            a.setKey(rs.getInt("descrizione"));
            a.setKey(rs.getInt("version"));
        } catch (Exception e) {
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

            } catch (Exception e) {
                throw new DataException("Nessuna Attrezzatura trovata ", e);
            }
        }

        return attrezzatura;

    }

    @Override
    public List<Attrezzatura> getListaAttrezzatura() throws DataException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAttrezzatura'");
    }

    @Override
    public void storeAttrezzatura(Attrezzatura attrezzatura) throws DataException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'storeAttrezzatura'");
    }

    @Override
    public void deleteAttrezzatura(Attrezzatura attrezzatura) throws DataException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAttrezzatura'");
    }

}
