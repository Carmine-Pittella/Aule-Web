
package it.univaq.f4i.iw.Aule_Web.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.univaq.f4i.iw.Aule_Web.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.Aule_Web.data.impl.EventoImpl;
import it.univaq.f4i.iw.Aule_Web.data.impl.TipologiaEvento;
import it.univaq.f4i.iw.Aule_Web.data.impl.TipologiaRicorrenza;
import it.univaq.f4i.iw.Aule_Web.data.model.Aula;
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

    private void action_modifica(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            Evento evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao()
                    .getEventoById(Integer.parseInt(request.getParameter("evento")));
            List<Aula> aule = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDao().getListaAule();

            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("evento", evento);
            request.setAttribute("aule", aule);
            request.setAttribute("tipologie", TipologiaEvento.values());
            request.setAttribute("tipologieRicorrenza", TipologiaRicorrenza.values());

            res.activate("settingEvento.ftl.html", request, response);
        } catch (TemplateManagerException | NumberFormatException | DataException e) {
            e.printStackTrace();
        }
    }

    private void action_aggiungi(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            List<Aula> aule = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDao().getListaAule();

            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("aule", aule);
            request.setAttribute("tipologie", TipologiaEvento.values());
            request.setAttribute("tipologieRicorrenza", TipologiaRicorrenza.values());

            res.activate("settingEvento.ftl.html", request, response);
        } catch (TemplateManagerException | DataException e) {
            e.printStackTrace();
        }
    }

    private void action_conferma(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            if (request.getParameter("nome") == null ||
                    request.getParameter("dataInizio") == null ||
                    request.getParameter("dataFine") == null ||
                    request.getParameter("descrizione") == null ||
                    request.getParameter("emailResponsabile") == null ||
                    request.getParameter("selectAula") == null ||
                    request.getParameter("selectTipologia") == null ||
                    request.getParameter("nomeCorso") == null) {

                // errore

            } else {

                Evento evento = new EventoImpl();
                if (request.getParameter("ID") != null) {

                    Evento tmp = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao()
                            .getEventoById(Integer.parseInt(request.getParameter("ID").toString()));

                    evento.setKey(Integer.parseInt(request.getParameter("ID")));
                    evento.setVersion(tmp.getVersion());
                }

                Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDao()
                        .getAulaById(Integer.parseInt(request.getParameter("selectAula")));

                evento.setNome(request.getParameter("nome").toString());
                evento.setDataInizio(LocalDateTime.parse(request.getParameter("dataInizio").toString()));
                evento.setDataFine(LocalDateTime.parse(request.getParameter("dataFine").toString()));
                evento.setDescrizione(request.getParameter("descrizione").toString());
                evento.setEmailResponsabile(request.getParameter("emailResponsabile").toString());
                evento.setAula(aula);
                evento.setTipologiaEvento(TipologiaEvento.valueOf(request.getParameter("selectTipologia")));
                evento.setNomeCorso(request.getParameter("nomeCorso").toString());

                if (!request.getParameter("selectTipologiaRicorrenza").toString().equals("NESSUNA")) {
                    evento.setDataFineRicorrenza(
                            LocalDate.parse(request.getParameter("dataFineRicorrenza").toString()));
                    evento.setTipologiaRicorrenza(
                            TipologiaRicorrenza.valueOf(request.getParameter("selectTipologiaRicorrenza").toString()));
                }
                ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao().storeEvento(evento);
                response.sendRedirect("eventiAdmin");
            }

        } catch (IOException | DataException e) {
            response.sendRedirect("settingEvento?errore=1");
            e.printStackTrace();
        }
    }

    private void action_elimina(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {

            Evento tmp = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao()
                    .getEventoById(Integer.parseInt(request.getParameter("delete").toString()));

            ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao().deleteEvento(tmp);
            response.sendRedirect("eventiAdmin");
        } catch (DataException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, TemplateManagerException {
        String urlPath = request.getRequestURI();
        String s = urlPath.substring(urlPath.lastIndexOf("/") + 1);

        // controllo auth
        HttpSession auth = request.getSession(false);
        if (auth == null) {
            try {
                TemplateResult res = new TemplateResult(getServletContext());
                res.activate("error.ftl.html", request, response);
            } catch (TemplateManagerException e) {
                e.printStackTrace();
            }
            return;
        }

        // schermata con tutti gli elementi
        if (s.equals("eventiAdmin")) {
            action_default(request, response);
            return;
        }

        // Errore
        if (request.getParameter("errore") != null) {
            request.setAttribute("errore", true);
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("settingEvento.ftl.html", request, response);
            return;
        }

        // è stato premuto il tasto ELIMINA
        if (request.getParameter("delete") != null) {
            action_elimina(request, response);
            return;
        }

        // è stato premuto il tasto CONFERMA
        if (request.getParameter("nome") != null) {
            action_conferma(request, response);
            return;
        }

        // schermata aggiunta/modifica
        if (request.getParameter("evento") != null) {
            action_modifica(request, response); // modifica
        } else {
            action_aggiungi(request, response); // aggiunta
        }

        // potrebbe essere necessario mettere in ogni servlet per l'admin, un controllo
        // sull'accesso
    }

    // ******************************************************* //

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

}
