
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
import java.time.LocalDate;
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
            if (request.getParameter("gruppo") != null) {
                Gruppo gruppo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDao()
                        .getGruppoById(Integer.parseInt(request.getParameter("gruppo")));
                aule = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDao().getListaAuleByGruppo(gruppo);
            } else {
                aule = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDao().getListaAule();
            }
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

    private void action_eventiAttuali(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            List<Evento> eventiAttuali;
            eventiAttuali = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao()
                    .getEventiAttuali();
            eventi.retainAll(eventiAttuali);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_eventiTreOre(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            List<Evento> eventiTreOre;
            eventiTreOre = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao()
                    .getEventiAttuali();
            // RICORDATI CHE QUA HAI UTILIZZATO LO STESSO METODO DI EVENTIATTUALI()
            eventi.retainAll(eventiTreOre);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_date(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            List<Evento> eventiDate;
            LocalDate inizio = LocalDate.now();
            LocalDate fine = LocalDate.of(2100, 1, 1);
            if (request.getParameter("dataInizio") != null) {
                inizio = LocalDate.parse(request.getParameter("dataInizio"));
                request.setAttribute("dataInizio", inizio.toString());
            }
            if (request.getParameter("dataFine") != null) {
                fine = LocalDate.parse(request.getParameter("dataFine"));
                request.setAttribute("dataFine", fine.toString());
            }
            eventiDate = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao()
                    .getEventiByData(inizio, fine);
            eventi.retainAll(eventiDate);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    // DOWNLOAD EVENTI
    private void action_download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StreamResult result = new StreamResult(getServletContext());
        CSVWriter w = new CSVWriter();
        w.csv_eventi(eventi, getServletContext().getRealPath(""));
        URL url = getServletContext().getResource("/" + "csv" + "/" + "eventi.csv");
        result.setResource(url);
        result.activate(request, response);

        String currentURL = request.getRequestURL().toString();
        String newURL = "";
        if (currentURL.contains("&csv=1")) {
            newURL = currentURL.replace("&csv=1", "");
        } else {
            newURL = currentURL.replace("?csv=1", "");
        }
        response.sendRedirect(newURL);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        action_barra_filtri(request, response);
        action_default(request, response);

        if (request.getParameter("eventiAttuali") != null ||
                request.getParameter("eventiTreOre") != null) {
            if (request.getParameter("eventiAttuali") != null) {
                action_eventiAttuali(request, response);

            } else if (request.getParameter("eventiTreOre") != null) {
                action_eventiTreOre(request, response);
            }
        } else {
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
            // ottengo le Date selezionate
            if (request.getParameter("dataInizio") != null || request.getParameter("dataFine") != null) {
                action_date(request, response);
            }
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

            // eventi attuali
            if (request.getParameter("eventiAttuali") != null || request.getParameter("eventiTreOre") != null) {
                if (request.getParameter("eventiAttuali") != null) {
                    List<Evento> eventiAttuali;
                    eventiAttuali = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao()
                            .getEventiAttuali();
                    eventiTrasformati.retainAll(eventiAttuali);
                }

                // eventi 3 ore
                else if (request.getParameter("eventiTreOre") != null) {
                    List<Evento> eventiTreOre;
                    eventiTreOre = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao()
                            .getEventiAttuali();
                    // RICORDATI CHE QUA HAI UTILIZZATO LO STESSO METODO DI EVENTIATTUALI()
                    eventiTrasformati.retainAll(eventiTreOre);
                }
            } else {

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
                // range di date
                if (request.getParameter("dataInizio") != null || request.getParameter("dataFine") != null) {
                    List<Evento> eventiDate;
                    LocalDate inizio = LocalDate.now();
                    LocalDate fine = LocalDate.of(2100, 1, 1);
                    if (request.getParameter("dataInizio") != null) {
                        inizio = LocalDate.parse(request.getParameter("dataInizio"));
                    }
                    if (request.getParameter("dataFine") != null) {
                        fine = LocalDate.parse(request.getParameter("dataFine"));
                    }
                    eventiDate = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao()
                            .getEventiByData(inizio, fine);
                    eventiTrasformati.retainAll(eventiDate);
                }
            }
            eventi.addAll(eventiTrasformati);
            Collections.sort(eventi, new Comparator<Evento>() {
                @Override
                public int compare(Evento evento1, Evento evento2) {
                    return evento1.getDataInizio().compareTo(evento2.getDataInizio());
                }
            });
        } catch (

        DataException e) {
            e.printStackTrace();
        }
    }

}
