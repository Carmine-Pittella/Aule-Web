package auleweb.data.Impl;

import java.time.LocalDateTime;

import auleweb.data.model.EventoRicorrente;
import auleweb.framework.data.DataItemImpl;

public class EventoRicorrenteImpl extends DataItemImpl<Integer> implements EventoRicorrente {

    private Integer id_evento;
    private Integer id_master;
    private LocalDateTime data_inizio;
    private LocalDateTime data_fine;

    // COSTRUTTORE
    public EventoRicorrenteImpl() {
    }

    public EventoRicorrenteImpl(Integer id_evento, Integer id_master, LocalDateTime data_inizio,
            LocalDateTime data_fine) {
        this.id_evento = id_evento;
        this.id_master = id_master;
        this.data_inizio = data_inizio;
        this.data_fine = data_fine;
    }

    /******************* GET e SET *******************/
    @Override
    public Integer getId_evento() {
        return id_evento;
    }

    @Override
    public void setId_evento(Integer id_evento) {
        this.id_evento = id_evento;
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
    public LocalDateTime getData_inizio() {
        return data_inizio;
    }

    @Override
    public void setData_inizio(LocalDateTime data_inizio) {
        this.data_inizio = data_inizio;
    }

    @Override
    public LocalDateTime getData_fine() {
        return data_fine;
    }

    @Override
    public void setData_fine(LocalDateTime data_fine) {
        this.data_fine = data_fine;
    }

}
