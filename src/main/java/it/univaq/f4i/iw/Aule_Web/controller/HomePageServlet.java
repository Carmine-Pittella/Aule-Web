
package it.univaq.f4i.iw.Aule_Web.controller;

import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Carmine
 */

public class HomePageServlet extends AuleWebBaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("AllTemplateElement.ftl.html", request, response);
        } catch (Exception e) {
            handleError("Data access exception: " + e.getMessage(), request, response);
        }

    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            action_default(request, response);
        } catch (Exception e) {
            handleError("Data access exception: " + e.getMessage(), request, response);
        }

    }

}
