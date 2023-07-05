
package it.univaq.f4i.iw.Aule_Web.controller;

import it.univaq.f4i.iw.Aule_Web.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.Aule_Web.data.impl.EventoImpl;
import it.univaq.f4i.iw.Aule_Web.data.model.Evento;
import it.univaq.f4i.iw.Aule_Web.data.model.Evento_Ricorrente;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author pitte
 */

public class HomePageServlet extends AuleWebBaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response) {
        TemplateResult res = new TemplateResult(getServletContext());
        try {
            List<Evento> eventi;
            eventi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao().getEventi();

            List<Evento_Ricorrente> eventi_ricorrenti;
            eventi_ricorrenti = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoRicorrenteDao()
                    .getEventiRicorrenti();

            for (Evento_Ricorrente er : eventi_ricorrenti) {
                Evento tmp = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao()
                        .getEventoById(er.getEventoMasterKey());
                Evento ev = new EventoImpl();
                ev.setDataInizio(er.getDataInizio());
                ev.setDataFine(er.getDataFine());
                ev.setAula(tmp.getAula());
                ev.setNome(tmp.getNome());
                ev.setDescrizione(tmp.getDescrizione());
                eventi.add(ev);
            }
            Collections.sort(eventi, new Comparator<Evento>() {
                @Override
                public int compare(Evento evento1, Evento evento2) {
                    return evento1.getDataInizio().compareTo(evento2.getDataInizio());
                }
            });

            request.setAttribute("eventi", eventi);
            res.activate("index.ftl.html", request, response);

        } catch (TemplateManagerException | DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_barra_filtri(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        action_default(request, response);
    }

}
