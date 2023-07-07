
package it.univaq.f4i.iw.Aule_Web.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.univaq.f4i.iw.Aule_Web.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.Aule_Web.data.model.Evento;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;

/**
 *
 * @author Carmine
 */

public class dettagliEventoServlet extends AuleWebBaseController {

    private void action_dettagli_evento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            if (request.getParameter("evento") != null) {
                TemplateResult res = new TemplateResult(getServletContext());
                Evento evento;
                evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDao()
                        .getEventoById(Integer.parseInt(request.getParameter("evento")));
                request.setAttribute("evento", evento);
                res.activate("dettagliEvento.ftl.html", request, response);
            }
        } catch (DataException | TemplateManagerException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        request.setAttribute("dettagli", true);
        if (request.getParameter("evento") != null) {
            action_dettagli_evento(request, response);
        }
    }

}
