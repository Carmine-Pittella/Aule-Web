package auleweb.data.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import auleweb.data.Impl.Ricorrenza;
import auleweb.data.Impl.TipologiaEvento;
import auleweb.framework.data.DataItem;

public interface Evento extends DataItem<Integer> {

    String getNome();

    void setNome(String nome);

    String getDescrizione();

    void setDescrizione(String descrizione);

    String getEmail_responsabile();

    void setEmail_responsabile(String email_responsabile);

    LocalDateTime getDataInizio();

    void setDataInizio(LocalDateTime dataInizio);

    LocalDateTime getDataFine();

    void setDataFine(LocalDateTime dataFine);

    LocalDate getData_fine_ricorrenza();

    void setData_fine_ricorrenza(LocalDate data_fine_ricorrenza);

    Integer getId_master();

    void setId_master(Integer id_master);

    Aula getAula();

    void setAula(Aula aula);

    TipologiaEvento getTipologiaEvento();

    void setTipologiaEvento(TipologiaEvento tipologiaEvento);

    public Ricorrenza getRicorrenza();

    public void setRicorrenza(Ricorrenza ricorrenza);

}