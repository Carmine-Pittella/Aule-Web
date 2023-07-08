
package it.univaq.f4i.iw.Aule_Web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.univaq.f4i.iw.Aule_Web.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.Aule_Web.data.model.Evento;
import it.univaq.f4i.iw.Aule_Web.data.model.Evento_Ricorrente;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;

/**
 *
 * @author Carmine
 */

public class eventiAdminServlet extends AuleWebBaseController {

    private List<Evento> eventi;

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            eventi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao().getEventi();
            CompletaListaConEventiRicorrenti(request, response);
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("eventi", eventi);
            res.activate("eventiAdmin.ftl.html", request, response);
        } catch (TemplateManagerException | DataException e) {
            e.printStackTrace();
        }
    }

    private void CompletaListaConEventiRicorrenti(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            List<Evento_Ricorrente> eventi_ricorrenti;
            eventi_ricorrenti = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoRicorrenteDao()
                    .getEventiRicorrenti();

            // trasformazione in Evento
            List<Evento> eventiTrasformati = new ArrayList<Evento>();
            for (Evento_Ricorrente er : eventi_ricorrenti) {
                Evento tmp = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao()
                        .getEventoById(er.getEventoMasterKey());

                Evento ev = ((Evento) tmp).clone();
                ev.setDataInizio(er.getDataInizio());
                ev.setDataFine(er.getDataFine());
                eventiTrasformati.add(ev);
            }

            eventi.addAll(eventiTrasformati);
            Collections.sort(eventi, new Comparator<Evento>() {
                @Override
                public int compare(Evento evento1, Evento evento2) {
                    return evento1.getDataInizio().compareTo(evento2.getDataInizio());
                }
            });
        } catch (DataException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        action_default(request, response);
    }

}
