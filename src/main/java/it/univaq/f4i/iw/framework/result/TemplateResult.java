/*
 * TemplateResult.java
 * 
 * Questa classe permette di generare facilmente output a partire da template
 * Freemarker. Gestisce vari modelli di dati, passati direttamente o attraverso
 * la request, l'uso di outline automatici, e si configura automaticamente 
 * in base a una serie di init parameters del contesto (si veda il codice e 
 * il file web.xml per informazioni)
 * 
 */
package it.univaq.f4i.iw.framework.result;

import freemarker.core.HTMLOutputFormat;
import freemarker.core.JSONOutputFormat;
import freemarker.core.XMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TemplateResult {

    protected ServletContext context;
    protected Configuration cfg;
    protected List<DataModelFiller> fillers;

    // templates in cui visualizzare la navbarAdmin
    private final List<String> tplsNavAdmin = Arrays.asList("admin.ftl.html", "auleadmin.ftl.html",
            "eventiadmin.ftl.html", "gruppiadmin.ftl.html", "gestioneAula.ftl.html", "gestioneGruppo.ftl.html",
            "gestioneEvento.ftl.html");

    public TemplateResult(ServletContext context) {
        this.context = context;
        init();
    }

    private void init() {
        cfg = new Configuration(Configuration.VERSION_2_3_26);
        // impostiamo l'encoding di default per l'input e l'output
        String encoding = "utf-8";
        if (context.getInitParameter("view.encoding") != null) {
            encoding = context.getInitParameter("view.encoding");
        }
        cfg.setOutputEncoding(encoding);
        cfg.setDefaultEncoding(encoding);

        // impostiamo la directory (relativa al contesto) da cui caricare i templates
        if (context.getInitParameter("view.template_directory") != null) {
            cfg.setServletContextForTemplateLoading(context, context.getInitParameter("view.template_directory"));
        } else {
            cfg.setServletContextForTemplateLoading(context, "templates");
        }

        // impostiamo un handler per gli errori nei template - utile per il debug
        if (context.getInitParameter("view.debug") != null && context.getInitParameter("view.debug").equals("true")) {
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        } else {
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        }

        // formato di default per data/ora
        if (context.getInitParameter("view.date_format") != null) {
            cfg.setDateTimeFormat(context.getInitParameter("view.date_format"));
        }

        // impostiamo il gestore degli oggetti - trasformerà in hash i Java beans
        // set the object handler that allows us to "view" Java beans as hashes
        DefaultObjectWrapperBuilder owb = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_26);
        owb.setForceLegacyNonListCollections(false);
        owb.setDefaultDateType(TemplateDateModel.DATETIME);
        cfg.setObjectWrapper(owb.build());

        // classi opzionali che permettono di riempire ogni data model con dati generati
        // dinamicamente
        // optional classes to automatically fill every data model with dynamically
        // generated data
        fillers = new ArrayList<>();
        Enumeration parms = context.getInitParameterNames();
        while (parms.hasMoreElements()) {
            String name = (String) parms.nextElement();
            if (name.startsWith("view.data.filler.")) {
                try {
                    Class filler = Class.forName(context.getInitParameter(name));
                    if (DataModelFiller.class.isAssignableFrom(filler)) {
                        fillers.add((DataModelFiller) filler.getDeclaredConstructor().newInstance());
                    }
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                        | NoSuchMethodException | SecurityException | IllegalArgumentException
                        | InvocationTargetException ex) {
                    Logger.getLogger(TemplateResult.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    // questo metodo restituisce un data model (hash) di base,
    // (qui inizializzato anche con informazioni di base utili alla gestione
    // dell'outline)
    protected Map getDefaultDataModel(HttpServletRequest request) {
        // inizializziamo il contenitore per i dati di deafult
        Map default_data_model = new HashMap();

        // iniettiamo alcuni dati di default nel data model
        // inject some default data in the data model
        default_data_model.put("compiled_on", Calendar.getInstance().getTime()); // data di compilazione del template
        default_data_model.put("outline_tpl", context.getInitParameter("view.outline_template")); // eventuale template
                                                                                                  // di outline
        // default_data_model.put("navbar", context.getInitParameter("view.navbar"));
        default_data_model.put("buttons", context.getInitParameter("view.buttons"));

        // aggiungiamo altri dati di inizializzazione presi dal web.xml
        // add other data taken from web.xml
        Map init_tpl_data = new HashMap();
        default_data_model.put("defaults", init_tpl_data);
        Enumeration parms = context.getInitParameterNames();
        while (parms.hasMoreElements()) {
            String name = (String) parms.nextElement();
            if (name.startsWith("view.data.static.")) {
                init_tpl_data.put(name.substring(17).replace(".", "_"), context.getInitParameter(name));
            }
        }

        // se sono state specificate delle classi filler, facciamo loro riempire il
        // default data model
        for (DataModelFiller f : fillers) {
            f.fillDataModel(default_data_model, request, context);
        }

        return default_data_model;
    }

    // questo metodo restituisce un data model estratto dagli attributi della
    // request
    protected Map getRequestDataModel(HttpServletRequest request) {
        Map datamodel = new HashMap();
        Enumeration attrs = request.getAttributeNames();
        while (attrs.hasMoreElements()) {
            String attrname = (String) attrs.nextElement();
            datamodel.put(attrname, request.getAttribute(attrname));
        }
        return datamodel;
    }

    // questo metodo principale si occupa di chiamare Freemarker e compilare il
    // template
    // se è stato specificato un template di outline, quello richiesto viene
    // inserito
    // all'interno dell'outline
    protected void process(String tplname, Map datamodel, HttpServletRequest request, Writer out)
            throws TemplateManagerException {
        Template t;
        // assicuriamoci di avere sempre un data model da passare al template, che
        // contenga anche tutti i default
        Map<String, Object> localdatamodel = getDefaultDataModel(request);
        // nota: in questo modo il data model utente può eventualmente sovrascrivere i
        // dati precaricati da getDefaultDataModel
        // ad esempio per disattivare l'outline template basta porre a null la
        // rispettiva chiave
        if (datamodel != null) {
            localdatamodel.putAll(datamodel);
        }

        // Se non è richiesta la navbar per gli admin allora ha valore null così che
        // freemarker carichi la classica navbar per gli utenti --> see index.ftl.html
        String nav_name = null;
        if (tplsNavAdmin.contains(tplname)) {
            nav_name = "navAdmin.ftl.html";
        }

        String outline_name = (String) localdatamodel.get("outline_tpl");

        try {
            if (outline_name == null || outline_name.isEmpty()) {
                // se non c'è un outline, carichiamo semplicemente il template specificato
                t = cfg.getTemplate(tplname);
            } else {
                // un template di outline è stato specificato: il template da caricare è quindi
                // sempre l'outline...
                t = cfg.getTemplate(outline_name);
                // ...e il template specifico per questa pagina viene indicato all'outline
                // tramite una variabile content_tpl
                localdatamodel.put("content_tpl", tplname);
                localdatamodel.put("navbar", nav_name);
                // si suppone che l'outline includa questo secondo template
            }
            // associamo i dati al template e lo mandiamo in output
            t.process(localdatamodel, out);
        } catch (IOException | TemplateException e) {
            throw new TemplateManagerException("Template error: " + e.getMessage(), e);
        }
    }

    // questa versione di activate accetta un modello dati esplicito
    public void activate(String tplname, Map datamodel, HttpServletResponse response) throws TemplateManagerException {
        setupServletResponse(datamodel, response);
        try {
            process(tplname, datamodel, null, response.getWriter());
        } catch (IOException ex) {
            throw new TemplateManagerException("Template error: " + ex.getMessage(), ex);
        }
    }

    // questa versione di activate estrae un modello dati dagli attributi della
    // request
    public void activate(String tplname, HttpServletRequest request, HttpServletResponse response)
            throws TemplateManagerException {
        Map datamodel = getRequestDataModel(request);
        setupServletResponse(datamodel, response);
        try {
            process(tplname, datamodel, request, response.getWriter());
        } catch (IOException ex) {
            throw new TemplateManagerException("Template error: " + ex.getMessage(), ex);
        }
    }

    // metodo interno per il setup della response
    private void setupServletResponse(Map datamodel, HttpServletResponse response) {
        // impostiamo il content type, se specificato dall'utente, o usiamo il default
        String contentType = (String) datamodel.get("contentType");
        if (contentType == null) {
            contentType = "text/html";
        }
        response.setContentType(contentType);

        // impostiamo l'encoding, se specificato dall'utente, o usiamo il default
        String encoding = (String) datamodel.get("encoding");
        if (encoding == null) {
            encoding = cfg.getOutputEncoding();
        }
        response.setCharacterEncoding(encoding);

        // impostiamo il tipo di output: in questo modo freemarker abiliterà il
        // necessario escaping
        switch (contentType) {
            case "text/html":
                cfg.setOutputFormat(HTMLOutputFormat.INSTANCE);
                break;
            case "text/xml":
            case "application/xml":
                cfg.setOutputFormat(XMLOutputFormat.INSTANCE);
                break;
            case "application/json":
                cfg.setOutputFormat(JSONOutputFormat.INSTANCE);
                break;
            default:
                break;
        }

    }

    // questa versione di activate può essere usata per generare output non diretto
    // verso il browser, ad esempio
    // su un file
    public void activate(String tplname, Map datamodel, OutputStream out) throws TemplateManagerException {
        // impostiamo l'encoding, se specificato dall'utente, o usiamo il default
        String encoding = (String) datamodel.get("encoding");
        if (encoding == null) {
            encoding = cfg.getOutputEncoding();
        }
        try {
            // notare la gestione dell'encoding, che viene invece eseguita implicitamente
            // tramite il setContentType nel contesto servlet
            process(tplname, datamodel, null, new OutputStreamWriter(out, encoding));
        } catch (UnsupportedEncodingException ex) {
            throw new TemplateManagerException("Template error: " + ex.getMessage(), ex);
        }
    }

}
