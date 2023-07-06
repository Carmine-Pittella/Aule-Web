
package it.univaq.f4i.iw.Aule_Web.controller;

import it.univaq.f4i.iw.Aule_Web.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.Aule_Web.data.impl.EventoImpl;
import it.univaq.f4i.iw.Aule_Web.data.model.Aula;
import it.univaq.f4i.iw.Aule_Web.data.model.Evento;
import it.univaq.f4i.iw.Aule_Web.data.model.Evento_Ricorrente;
import it.univaq.f4i.iw.Aule_Web.data.model.Gruppo;
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

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        TemplateResult res = new TemplateResult(getServletContext());
        try {
            List<Evento> eventi;
            eventi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao().getEventi();
            eventi = CompletaListaConEventiRicorrenti(eventi, request, response);
            request.setAttribute("eventi", eventi);
            res.activate("eventi.ftl.html", request, response);

        } catch (TemplateManagerException | DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_barra_filtri(HttpServletRequest request, HttpServletResponse response) {
        try {
            // gruppi
            List<Gruppo> gruppi;
            gruppi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDao().getGruppi();
            request.setAttribute("gruppi", gruppi);

            // aule
            List<Aula> aule;
            aule = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDao().getListaAule();
            request.setAttribute("aule", aule);

            // corsi
            List<String> corsi;
            corsi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao().getCorsi();
            request.setAttribute("corsi", corsi);

        } catch (Exception e) {
            handleError("Data access exception: " + e.getMessage(), request, response);
        }
    }

    private void action_corso(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            // ottengo il Gruppo selezionato
            if (request.getParameter("gruppo") != null) {
                TemplateResult res = new TemplateResult(getServletContext());
                Gruppo gruppo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDao()
                        .getGruppoById(Integer.parseInt(request.getParameter("gruppo")));

                List<Evento> eventi;
                eventi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao()
                        .getEventiByGruppo(gruppo);
                eventi = CompletaListaConEventiRicorrenti(eventi, request, response);
                request.setAttribute("eventi", eventi);
                res.activate("eventi.ftl.html", request, response);
            }
        } catch (TemplateManagerException | DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        action_barra_filtri(request, response);

        // ottengo il Gruppo selezionato
        if (request.getParameter("gruppo") != null) {
            action_corso(request, response);
        } else {
            action_default(request, response);
        }
    }

    // METODI
    private List<Evento> CompletaListaConEventiRicorrenti(List<Evento> eventi, HttpServletRequest request,
            HttpServletResponse response) throws ServletException {
        // il probema principale è che gli eventi ricorrenti vengono caricati tutti,
        // senza filtri
        try {
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
        } catch (DataException e) {
            e.printStackTrace();
        }
        return eventi;
    }

}

// fare un coso grande di default, e nel mezzo chiamare la query necessaria,
// per la barra dei filtri, all'inizio verifico se ci sono valori impostati e
// nel caso li assegno per poi caricarla.