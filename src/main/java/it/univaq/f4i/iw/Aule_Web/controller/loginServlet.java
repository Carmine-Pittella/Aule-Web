
package it.univaq.f4i.iw.Aule_Web.controller;

import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.univaq.f4i.iw.Aule_Web.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.Aule_Web.data.model.Amministratore;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;

/**
 *
 * @author pitte
 */

public class loginServlet extends AuleWebBaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("login.ftl.html", request, response);
        } catch (TemplateManagerException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_login(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (!email.isEmpty() && !password.isEmpty()) {
            try {
                Amministratore admin = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAmministratoreDao()
                        .getAmministratoreByEmail(email);

                if (admin != null) {
                    if (SecurityHelpers.checkHashSHA(password, admin.getPassword())) {
                        // login avvenuto con successo
                        TemplateResult res = new TemplateResult(getServletContext());
                        SecurityHelpers.createSession(request, email, admin.getKey());
                        res.activate("areaAdmin.ftl.html", request, response);
                    } else {
                        TemplateResult res = new TemplateResult(getServletContext());
                        res.activate("login.ftl.html", request, response);
                    }
                } else {
                    // login fallito
                    request.setAttribute("errore", true);
                    TemplateResult res = new TemplateResult(getServletContext());
                    res.activate("login.ftl.html", request, response);
                }
            } catch (DataException | TemplateManagerException | NoSuchAlgorithmException ex) {
                handleError("lallero sium: " + ex.getMessage(), request, response);
            }
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            if (request.getParameter("login") != null) {
                action_login(request, response);
            } else {
                action_default(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
