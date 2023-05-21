package auleweb.data.Impl;

public enum TipologiaEvento {
    lezione,
    esame,
    seminario,
    parziale,
    riunione,
    lauree,
    altro;

    private String nome_corso;

    /******************* GET e SET *******************/
    public String getNome_corso() {
        return nome_corso;
    }

    public void setNome_corso(String nome_corso) {
        this.nome_corso = nome_corso;
    }

}
