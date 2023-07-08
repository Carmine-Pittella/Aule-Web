
package it.univaq.f4i.iw.Aule_Web.controller;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.univaq.f4i.iw.Aule_Web.data.dao.AuleWebDataLayer;
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

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        action_default(request, response);
    }

}
