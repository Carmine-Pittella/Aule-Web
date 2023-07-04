
package it.univaq.f4i.iw.Aule_Web.controller;

import it.univaq.f4i.iw.Aule_Web.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.Aule_Web.data.model.Aula;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        TemplateResult res = new TemplateResult(getServletContext());
        try {
            List<Aula> aule;
            aule = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDao().getListaAule();

            request.setAttribute("aule", aule);
            res.activate("index.ftl.html", request, response);

        } catch (TemplateManagerException | DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

}
