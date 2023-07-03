
package it.univaq.f4i.iw.Aule_Web.controller;

import it.univaq.f4i.iw.Aule_Web.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.Aule_Web.data.model.Attrezzatura;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author pitte
 */

public class HomePageServlet extends AuleWebBaseController {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        TemplateResult res = new TemplateResult(getServletContext());
        try {
            List<Attrezzatura> attrezzatura = new ArrayList<Attrezzatura>();
            attrezzatura = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAttrezzaturaDao()
                    .getListaAttrezzatura();

            request.setAttribute("attrezzatura", attrezzatura);
            res.activate("AllTemplateElement.ftl.html", request, response);

        } catch (TemplateManagerException | DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

}
