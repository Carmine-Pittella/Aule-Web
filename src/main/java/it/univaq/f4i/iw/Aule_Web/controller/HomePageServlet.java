
package it.univaq.f4i.iw.Aule_Web.controller;

import it.univaq.f4i.iw.Aule_Web.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.Aule_Web.data.model.Aula;
import it.univaq.f4i.iw.Aule_Web.data.model.Evento;
import it.univaq.f4i.iw.Aule_Web.data.model.Evento_Ricorrente;
import it.univaq.f4i.iw.Aule_Web.data.model.Gruppo;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.CSVWriter;
import it.univaq.f4i.iw.framework.result.StreamResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Carmine
 */

public class HomePageServlet extends AuleWebBaseController {

    private List<Evento> eventi;

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            eventi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao().getEventi();
        } catch (DataException ex) {
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

    private void action_gruppo(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            // ottengo il Gruppo selezionato
            if (request.getParameter("gruppo") != null) {
                Gruppo gruppo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDao()
                        .getGruppoById(Integer.parseInt(request.getParameter("gruppo")));
                request.setAttribute("gruppo", gruppo.getNome());

                List<Evento> eventiByGruppo;
                eventiByGruppo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao()
                        .getEventiByGruppo(gruppo);
                eventi.retainAll(eventiByGruppo);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_aula(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            if (request.getParameter("aula") != null) {
                Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDao()
                        .getAulaById(Integer.parseInt(request.getParameter("aula")));
                request.setAttribute("aula", aula.getNome());
                List<Evento> eventiByAula;
                eventiByAula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao()
                        .getEventiByAula(aula);
                eventi.retainAll(eventiByAula);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_corso(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            if (request.getParameter("corso") != null) {
                String corso = request.getParameter("corso");

                List<Evento> eventiByCorso;
                eventiByCorso = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao()
                        .getEventiByCorso(corso);
                request.setAttribute("corso", corso);
                eventi.retainAll(eventiByCorso);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    // DOWNLOAD EVENTI
    private void action_download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StreamResult result = new StreamResult(getServletContext());
        CSVWriter w = new CSVWriter();
        w.csv_time(eventi, getServletContext().getRealPath(""));
        URL url = getServletContext().getResource("/" + "csv" + "/" + "eventi_time.csv");
        result.setResource(url);
        result.activate(request, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        action_barra_filtri(request, response);
        action_default(request, response);

        // ottengo il Gruppo selezionato
        if (request.getParameter("gruppo") != null) {
            action_gruppo(request, response);
        }
        // ottengo l'Aula selezionata
        if (request.getParameter("aula") != null) {
            action_aula(request, response);
        }
        // ottengo il Corso selezionato
        if (request.getParameter("corso") != null) {
            action_corso(request, response);
        }
        // aggiungo alla lista gli eventi ricorrenti
        CompletaListaConEventiRicorrenti(request, response);

        if (request.getParameter("csv") != null) {
            try {
                action_download(request, response);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("eventi", eventi);
            res.activate("eventi.ftl.html", request, response);
        } catch (TemplateManagerException e) {
            e.printStackTrace();
        }

    }

    /* **************************************************************** */
    // METODI
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

            // Gruppo
            if (request.getParameter("gruppo") != null) {
                Gruppo gruppo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDao()
                        .getGruppoById(Integer.parseInt(request.getParameter("gruppo")));
                List<Evento> eventiByGruppo;
                eventiByGruppo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao()
                        .getEventiByGruppo(gruppo);
                eventiTrasformati.retainAll(eventiByGruppo);
            }
            // Aula
            if (request.getParameter("aula") != null) {
                Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDao()
                        .getAulaById(Integer.parseInt(request.getParameter("aula")));
                List<Evento> eventiByAula;
                eventiByAula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao()
                        .getEventiByAula(aula);
                eventiTrasformati.retainAll(eventiByAula);
            }
            // Corso
            if (request.getParameter("corso") != null) {
                String corso = request.getParameter("corso");
                List<Evento> eventiByCorso;
                eventiByCorso = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao()
                        .getEventiByCorso(corso);
                eventiTrasformati.retainAll(eventiByCorso);
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
}
