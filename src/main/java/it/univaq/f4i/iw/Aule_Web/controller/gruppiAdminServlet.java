
package it.univaq.f4i.iw.Aule_Web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.univaq.f4i.iw.Aule_Web.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.Aule_Web.data.impl.GruppoImpl;
import it.univaq.f4i.iw.Aule_Web.data.model.Gruppo;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;

/**
 *
 * @author Carmine
 */

public class gruppiAdminServlet extends AuleWebBaseController {

    private List<Gruppo> gruppi;

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            gruppi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDao().getGruppi();
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("gruppi", gruppi);
            res.activate("gruppiAdmin.ftl.html", request, response);
        } catch (TemplateManagerException | DataException e) {
            e.printStackTrace();
        }
    }

    private void action_modifica(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            Gruppo gruppo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDao()
                    .getGruppoById(Integer.parseInt(request.getParameter("gruppo").toString()));

            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("gruppo", gruppo);
            res.activate("settingGruppo.ftl.html", request, response);
        } catch (TemplateManagerException | NumberFormatException | DataException e) {
            e.printStackTrace();
        }
    }

    private void action_aggiungi(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("settingGruppo.ftl.html", request, response);
        } catch (TemplateManagerException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void action_conferma(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            if (request.getParameter("nome") == null ||
                    request.getParameter("descrizione") == null) {
                // errore
            } else {
                Gruppo gruppo = new GruppoImpl();
                if (request.getParameter("ID") != null) {
                    gruppo.setVersion(((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDao()
                            .getGruppoById(Integer.parseInt(request.getParameter("ID").toString())).getVersion());
                    gruppo.setKey(Integer.parseInt(request.getParameter("ID").toString()));
                }
                gruppo.setNome(request.getParameter("nome").toString());
                gruppo.setDescrizione(request.getParameter("descrizione").toString());

                ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDao().storeGruppo(gruppo);
                response.sendRedirect("gruppiAdmin");
            }
        } catch (DataException | IOException e) {
            e.printStackTrace();
        }
    }

    private void action_elimina(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            Gruppo tmp = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDao()
                    .getGruppoById(Integer.parseInt(request.getParameter("delete").toString()));
            ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDao().deleteGruppo(tmp);
            response.sendRedirect("gruppiAdmin");
        } catch (DataException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String urlPath = request.getRequestURI();
        String s = urlPath.substring(urlPath.lastIndexOf("/") + 1);

        // schermata con tutti gli elementi
        if (s.equals("gruppiAdmin")) {
            action_default(request, response);
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
        if (request.getParameter("gruppo") != null) {
            action_modifica(request, response); // modifica
        } else {
            action_aggiungi(request, response); // aggiunta
        }

        // potrebbe essere necessario mettere in ogni servlet per l'admin, un controllo
        // sull'accesso
    }

}
