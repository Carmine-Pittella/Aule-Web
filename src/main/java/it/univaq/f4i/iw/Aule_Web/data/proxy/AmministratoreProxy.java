/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.Aule_Web.data.proxy;

import it.univaq.f4i.iw.Aule_Web.data.impl.AmministratoreImpl;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;

/**
 *
 * @author Carmine
 */

public class AmministratoreProxy extends AmministratoreImpl implements DataItemProxy {

    protected boolean modified;
    protected DataLayer dataLayer;

    public AmministratoreProxy(DataLayer d) {
        super();
        this.dataLayer = d;
        this.modified = false;
    }

    @Override
    public void setKey(Integer key) {
        super.setKey(key);
        this.modified = true;
    }

    /* ********************************************************* */

    // GET
    @Override
    public String getNome() {
        return super.getNome();
    }

    @Override
    public String getCognome() {
        return super.getCognome();
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    // SET
    @Override
    public void setNome(String nome) {
        super.setNome(nome);
        this.modified = true;
    }

    @Override
    public void setCognome(String cognome) {
        super.setCognome(cognome);
        this.modified = true;
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
        this.modified = true;
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
        this.modified = true;
    }

    /* ********************************************************* */

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean mod) {
        this.modified = mod;

    }

}
