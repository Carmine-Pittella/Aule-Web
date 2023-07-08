
package it.univaq.f4i.iw.Aule_Web.controller;

import java.util.List;

import javax.servlet.ServletException;

import it.univaq.f4i.iw.Aule_Web.data.dao.AuleWebDataLayer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        action_default(request, response);

        // potrebbe essere necessario mettere in ogni servlet per l'admin, un controllo
        // sull'accesso
    }

}
