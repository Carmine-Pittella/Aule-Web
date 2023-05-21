package auleweb.data.model;

import auleweb.framework.data.DataItem;

public interface Attrezzatura extends DataItem<Integer> {

    String getNomeAttrezzo();

    void setNomeAttrezzo(String nome);

}
