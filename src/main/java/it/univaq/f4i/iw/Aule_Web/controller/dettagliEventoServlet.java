
package it.univaq.f4i.iw.Aule_Web.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;

/**
 *
 * @author Carmine
 */

public class dettagliEventoServlet extends AuleWebBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        request.setAttribute("dettagli", true);

        TemplateResult res = new TemplateResult(getServletContext());
        try {
            res.activate("dettagliEvento.ftl.html", request, response);
        } catch (TemplateManagerException e) {
            e.printStackTrace();
        }

    }

}
