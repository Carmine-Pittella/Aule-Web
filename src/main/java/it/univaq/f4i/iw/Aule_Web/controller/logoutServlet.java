
package it.univaq.f4i.iw.Aule_Web.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.univaq.f4i.iw.framework.security.SecurityHelpers;

/**
 *
 * @author Carmine
 */

public class logoutServlet extends AuleWebBaseController {

    private void action_logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SecurityHelpers.disposeSession(request);
        response.sendRedirect("HomePage");
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            action_logout(request, response);
        } catch (IOException ex) {
            handleError(ex, request, response);
        }
    }

}
