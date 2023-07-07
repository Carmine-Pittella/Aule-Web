
package it.univaq.f4i.iw.Aule_Web.data.proxy;

import java.util.List;

import it.univaq.f4i.iw.Aule_Web.data.impl.AulaImpl;
import it.univaq.f4i.iw.Aule_Web.data.model.Attrezzatura_Relazione;
import it.univaq.f4i.iw.Aule_Web.data.model.Evento;
import it.univaq.f4i.iw.Aule_Web.data.model.Gruppo;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;

/**
 *
 * @author Carmine
 */

public class AulaProxy extends AulaImpl implements DataItemProxy {

    protected boolean modified;
    protected DataLayer dataLayer;

    public AulaProxy(DataLayer d) {
        super();
        this.dataLayer = d;
        this.modified = false;
    }

    @Override
    public void setKey(Integer key) {
        super.setKey(key);

    }

    /* ********************************************************* */

    // GET
    @Override
    public Integer getCapienza() {
        return super.getCapienza();
    }

    @Override
    public String getEdificio() {
        return super.getEdificio();
    }

    @Override
    public String getEmailResponsabile() {
        return super.getEmailResponsabile();
    }

    @Override
    public List<Evento> getEventi() {
        return super.getEventi();
    }

    @Override
    public Gruppo getGruppo() {
        return super.getGruppo();
    }

    @Override
    public String getLuogo() {
        return super.getLuogo();
    }

    @Override
    public Integer getN_PreseElettriche() {
        return super.getN_PreseElettriche();
    }

    @Override
    public Integer getN_PreseRete() {
        return super.getN_PreseRete();
    }

    @Override
    public String getNome() {
        return super.getNome();
    }

    @Override
    public String getNote() {
        return super.getNote();
    }

    @Override
    public Integer getPiano() {
        return super.getPiano();
    }

    @Override
    public List<Attrezzatura_Relazione> getListaAttrezzatura() {
        return super.getListaAttrezzatura();
    }

    // SET
    @Override
    public void setCapienza(Integer capienza) {
        super.setCapienza(capienza);
        this.modified = true;
    }

    @Override
    public void setEdificio(String edificio) {
        super.setEdificio(edificio);
        this.modified = true;
    }

    @Override
    public void setEmailResponsabile(String email_responsabile) {
        super.setEmailResponsabile(email_responsabile);
        this.modified = true;
    }

    @Override
    public void setGruppo(Gruppo gruppo) {
        super.setGruppo(gruppo);
        this.modified = true;
    }

    @Override
    public void setListaEventi(List<Evento> listaEventi) {
        super.setListaEventi(listaEventi);
        this.modified = true;
    }

    @Override
    public void setLuogo(String luogo) {
        super.setLuogo(luogo);
        this.modified = true;
    }

    @Override
    public void setN_PreseElettriche(Integer n_prese_elettriche) {
        super.setN_PreseElettriche(n_prese_elettriche);
        this.modified = true;
    }

    @Override
    public void setN_PreseRete(Integer n_prese_rete) {
        super.setN_PreseRete(n_prese_rete);
        this.modified = true;
    }

    @Override
    public void setNome(String nome) {
        super.setNome(nome);
        this.modified = true;
    }

    @Override
    public void setNote(String note) {
        super.setNote(note);
        this.modified = true;
    }

    @Override
    public void setPiano(Integer piano) {
        super.setPiano(piano);
        this.modified = true;
    }

    @Override
    public void setListaAttrezzatura(List<Attrezzatura_Relazione> listaAttrezzatura) {
        super.setListaAttrezzatura(listaAttrezzatura);
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
