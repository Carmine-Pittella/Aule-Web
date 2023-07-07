
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

public class adminServlet extends AuleWebBaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("sium", true);
            res.activate("areaAdmin.ftl.html", request, response);
        } catch (TemplateManagerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        action_default(request, response);
    }

}
