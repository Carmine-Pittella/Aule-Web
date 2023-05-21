package auleweb.data.Impl;

import auleweb.data.model.Gruppo;
import auleweb.framework.data.DataItemImpl;

public class GruppoImpl extends DataItemImpl<Integer> implements Gruppo {

    private String nome;
    private String descrizione;

    // COSTRUTTORE
    public GruppoImpl() {
    }

    public GruppoImpl(String nome, String descrizione) {
        this.nome = nome;
        this.descrizione = descrizione;
    }

    /******************* GET e SET *******************/
    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

}
