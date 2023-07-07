
package it.univaq.f4i.iw.Aule_Web.controller;

import it.univaq.f4i.iw.Aule_Web.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.framework.controller.AbstractBaseController;
import it.univaq.f4i.iw.framework.data.DataLayer;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.sql.DataSource;

/**
 *
 * @author Carmine
 */

public abstract class AuleWebBaseController extends AbstractBaseController {
    
    @Override
    protected DataLayer createDataLayer(DataSource ds) throws ServletException {
        try {
            return new AuleWebDataLayer(ds);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

}
