package auleweb.data.Impl;

import auleweb.data.model.Attrezzatura;
import auleweb.framework.data.DataItemImpl;

public class AttrezzaturaImpl extends DataItemImpl<Integer> implements Attrezzatura {
    private String nome;

    // COSTRUTTORE
    public AttrezzaturaImpl() {
    }

    public AttrezzaturaImpl(String nome) {
        this.nome = nome;
    }

    /******************* GET e SET *******************/
    @Override
    public String getNomeAttrezzo() {
        return nome;
    }

    @Override
    public void setNomeAttrezzo(String nome) {
    }

}
