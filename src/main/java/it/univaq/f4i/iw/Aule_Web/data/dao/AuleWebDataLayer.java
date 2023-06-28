
package it.univaq.f4i.iw.Aule_Web.data.dao;

import it.univaq.f4i.iw.framework.data.DataLayer;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 *
 * @author Carmine
 */

public class AuleWebDataLayer extends DataLayer {
    
    public AuleWebDataLayer(DataSource datasource) throws SQLException {
        super(datasource);
    }
    
}
