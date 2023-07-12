
package it.univaq.f4i.iw.Aule_Web.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import it.univaq.f4i.iw.Aule_Web.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.Aule_Web.data.impl.AttrezzaturaImpl;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.univaq.f4i.iw.Aule_Web.data.model.Attrezzatura;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;

/**
 *
 * @author Carmine
 */

public class attrezzaturaAdminServlet extends AuleWebBaseController {

    private List<Attrezzatura> attrezzi;

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            attrezzi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAttrezzaturaDao()
                    .getListaAttrezzatura();
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("attrezzi", attrezzi);
            res.activate("attrezzaturaAdmin.ftl.html", request, response);
        } catch (TemplateManagerException | DataException e) {
            e.printStackTrace();
        }
    }

    private void action_modifica(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            Attrezzatura attrezzo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAttrezzaturaDao()
                    .getAttrezzaturaById(Integer.parseInt(request.getParameter("attrezzo").toString()));

            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("attrezzo", attrezzo);
            res.activate("settingAttrezzo.ftl.html", request, response);
        } catch (TemplateManagerException | NumberFormatException | DataException e) {
            e.printStackTrace();
        }
    }

    private void action_aggiungi(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("settingAttrezzo.ftl.html", request, response);
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
                Attrezzatura attrezzo = new AttrezzaturaImpl();
                if (request.getParameter("ID") != null) {
                    attrezzo.setVersion(((AuleWebDataLayer) request.getAttribute("datalayer")).getAttrezzaturaDao()
                            .getAttrezzaturaById(Integer.parseInt(request.getParameter("ID").toString())).getVersion());
                    attrezzo.setKey(Integer.parseInt(request.getParameter("ID").toString()));
                }
                attrezzo.setNomeAttrezzo(request.getParameter("nome").toString());
                attrezzo.setDescrizione(request.getParameter("descrizione").toString());

                ((AuleWebDataLayer) request.getAttribute("datalayer")).getAttrezzaturaDao().storeAttrezzatura(attrezzo);
                response.sendRedirect("attrezzaturaAdmin");
            }
        } catch (DataException | IOException e) {
            e.printStackTrace();
        }
    }

    private void action_elimina(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            Attrezzatura attrezzo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAttrezzaturaDao()
                    .getAttrezzaturaById(Integer.parseInt(request.getParameter("delete").toString()));
            ((AuleWebDataLayer) request.getAttribute("datalayer")).getAttrezzaturaDao().deleteAttrezzatura(attrezzo);
            response.sendRedirect("attrezzaturaAdmin");
        } catch (DataException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
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
        if (s.equals("attrezzaturaAdmin")) {
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
        if (request.getParameter("attrezzo") != null) {
            action_modifica(request, response); // modifica
        } else {
            action_aggiungi(request, response); // aggiunta
        }

        // potrebbe essere necessario mettere in ogni servlet per l'admin, un controllo
        // sull'accesso
    }

}
