package auleweb.framework.result;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import java.util.List;

import auleweb.framework.security.SecurityHelpers;

public class SplitSlashesFmkExt implements TemplateMethodModelEx {

    @Override
    public Object exec(List list) throws TemplateModelException {
        // la lista contiene i parametri passati alla funzione nel template
        // the list contains the parameters passed to the function in the template
        if (!list.isEmpty()) {
            return SecurityHelpers.stripSlashes(list.get(0).toString());
        } else {
            // e' possibile ritornare qualsiasi tipo che sia gestibile da Freemarker
            // (scalari, hash, liste)
            // it is possible tor eturn any data type recognized by Freemarker (scalars,
            // hashes, lists)
            return "";
        }
    }
}
