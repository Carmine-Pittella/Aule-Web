package auleweb.data.model;

import java.time.LocalDateTime;

import auleweb.framework.data.DataItem;

public interface EventoRicorrente extends DataItem<Integer> {

    public Integer getId_evento();

    public void setId_evento(Integer id_evento);

    public Integer getId_master();

    public void setId_master(Integer id_master);

    public LocalDateTime getData_inizio();

    public void setData_inizio(LocalDateTime data_inizio);

    public LocalDateTime getData_fine();

    public void setData_fine(LocalDateTime data_fine);
}
