package auleweb.data.Impl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import auleweb.data.model.Aula;
import auleweb.data.model.Evento;
import auleweb.framework.data.DataItemImpl;

public class EventoImpl extends DataItemImpl<Integer> implements Evento {

    private String nome;
    private String descrizione;
    private String email_responsabile;
    private LocalDateTime dataInizio;
    private LocalDateTime dataFine;
    private LocalDate data_fine_ricorrenza;
    private Integer id_master;
    private Aula aula;
    private TipologiaEvento tipologiaEvento;
    private Ricorrenza ricorrenza;

    // COSTRUTTORE
    public EventoImpl() {
    }

    public EventoImpl(String nome, String descrizione, String email_responsabile, LocalDateTime dataInizio,
            LocalDateTime dataFine, LocalDate data_fine_ricorrenza, Integer id_master, Aula aula,
            TipologiaEvento tipologiaEvento) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.email_responsabile = email_responsabile;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.data_fine_ricorrenza = data_fine_ricorrenza;
        this.id_master = id_master;
        this.aula = aula;
        this.tipologiaEvento = tipologiaEvento;
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

    @Override
    public String getEmail_responsabile() {
        return email_responsabile;
    }

    @Override
    public void setEmail_responsabile(String email_responsabile) {
        this.email_responsabile = email_responsabile;
    }

    @Override
    public LocalDateTime getDataInizio() {
        return dataInizio;
    }

    @Override
    public void setDataInizio(LocalDateTime dataInizio) {
        this.dataInizio = dataInizio;
    }

    @Override
    public LocalDateTime getDataFine() {
        return dataFine;
    }

    @Override
    public void setDataFine(LocalDateTime dataFine) {
        this.dataFine = dataFine;
    }

    @Override
    public LocalDate getData_fine_ricorrenza() {
        return data_fine_ricorrenza;
    }

    @Override
    public void setData_fine_ricorrenza(LocalDate data_fine_ricorrenza) {
        this.data_fine_ricorrenza = data_fine_ricorrenza;
    }

    @Override
    public Integer getId_master() {
        return id_master;
    }

    @Override
    public void setId_master(Integer id_master) {
        this.id_master = id_master;
    }

    @Override
    public Aula getAula() {
        return aula;
    }

    @Override
    public void setAula(Aula aula) {
        this.aula = aula;
    }

    @Override
    public TipologiaEvento getTipologiaEvento() {
        return tipologiaEvento;
    }

    @Override
    public void setTipologiaEvento(TipologiaEvento tipologiaEvento) {
        this.tipologiaEvento = tipologiaEvento;
    }

    @Override
    public Ricorrenza getRicorrenza() {
        return ricorrenza;
    }

    @Override
    public void setRicorrenza(Ricorrenza ricorrenza) {
        this.ricorrenza = ricorrenza;
    }

}
