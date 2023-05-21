package auleweb.data.model;

import auleweb.framework.data.DataItem;

public interface Gruppo extends DataItem<Integer> {

    public String getNome();

    public void setNome(String nome);

    public String getDescrizione();

    public void setDescrizione(String descrizione);

}
