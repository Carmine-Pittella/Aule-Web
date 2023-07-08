
package it.univaq.f4i.iw.Aule_Web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import it.univaq.f4i.iw.Aule_Web.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.Aule_Web.data.impl.AulaImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.univaq.f4i.iw.Aule_Web.data.model.Aula;
import it.univaq.f4i.iw.Aule_Web.data.model.Gruppo;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;

/**
 *
 * @author Carmine
 */

public class auleAdminServlet extends AuleWebBaseController {

    private List<Aula> aule;

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            aule = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDao().getListaAule();
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("aule", aule);
            res.activate("auleAdmin.ftl.html", request, response);
        } catch (TemplateManagerException | DataException e) {
            e.printStackTrace();
        }
    }

    private void action_modifica(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDao()
                    .getAulaById(Integer.parseInt(request.getParameter("aula")));
            TemplateResult res = new TemplateResult(getServletContext());

            // gruppi
            List<Gruppo> gruppi;
            gruppi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDao().getGruppi();
            request.setAttribute("gruppi", gruppi);

            request.setAttribute("aula", aula);
            res.activate("settingAula.ftl.html", request, response);
        } catch (TemplateManagerException | NumberFormatException | DataException e) {
            e.printStackTrace();
        }
    }

    private void action_aggiungi(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            // gruppi
            List<Gruppo> gruppi;
            gruppi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDao().getGruppi();
            request.setAttribute("gruppi", gruppi);

            request.setAttribute("aule", aule);
            res.activate("settingAula.ftl.html", request, response);
        } catch (TemplateManagerException | DataException e) {
            e.printStackTrace();
        }
    }

    private void action_conferma(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            if (request.getParameter("nome") == null ||
                    request.getParameter("luogo") == null ||
                    request.getParameter("edificio") == null ||
                    request.getParameter("piano") == null ||
                    request.getParameter("capienza") == null ||
                    request.getParameter("emailResponsabile") == null ||
                    request.getParameter("note") == null ||
                    request.getParameter("preseElettriche") == null ||
                    request.getParameter("preseRete") == null) {

                // errore

            } else {

                Aula aula = new AulaImpl();
                if (request.getParameter("ID") != null) {
                    aula.setKey(Integer.parseInt(request.getParameter("ID")));
                    aula.setVersion(((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDao()
                            .getAulaById(Integer.parseInt(request.getParameter("ID"))).getVersion());
                }
                aula.setNome(request.getParameter("nome").toString());
                aula.setLuogo(request.getParameter("luogo").toString());
                aula.setEdificio(request.getParameter("edificio").toString());
                aula.setPiano(Integer.parseInt(request.getParameter("piano")));
                aula.setCapienza(Integer.parseInt(request.getParameter("capienza")));
                aula.setEmailResponsabile(request.getParameter("emailResponsabile").toString());
                aula.setNote(request.getParameter("note").toString());
                aula.setN_PreseElettriche(Integer.parseInt(request.getParameter("preseElettriche")));
                aula.setN_PreseRete(Integer.parseInt(request.getParameter("preseRete")));

                ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDao().storeAula(aula);
                response.sendRedirect("auleAdmin");
            }
        } catch (IOException | DataException e) {
            e.printStackTrace();
        }
    }

    private void action_elimina(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            Aula tmp = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDao()
                    .getAulaById(Integer.parseInt(request.getParameter("delete").toString()));
            ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDao().deleteAula(tmp);
            response.sendRedirect("auleAdmin");
        } catch (DataException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String urlPath = request.getRequestURI();
        String s = urlPath.substring(urlPath.lastIndexOf("/") + 1);

        // schermata con tutti gli elementi
        if (s.equals("auleAdmin")) {
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
        if (request.getParameter("aula") != null) {
            action_modifica(request, response); // modifica
        } else {
            action_aggiungi(request, response); // aggiunta
        }

        // potrebbe essere necessario mettere in ogni servlet per l'admin, un controllo
        // sull'accesso
    }

}
