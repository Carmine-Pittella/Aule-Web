
package it.univaq.f4i.iw.framework.result;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import it.univaq.f4i.iw.Aule_Web.data.model.Evento;

/**
 *
 * @author Carmine
 */

public class CSVWriter {

    // Directory "relativa" del file in cui caricare gli eventi relativi a un corso
    private static final String CSV_CORSO_FILE = "csv\\eventi.csv";
    // Directory "relativa" del file in cui caricare la configurazione dei gruppi

    public void csv_eventi(List<Evento> eventi, String path) throws IOException {

        // Unione della directory del contesto con la directory relativa --> PATH esatto
        // del file
        String dir = path + CSV_CORSO_FILE;

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(dir));) {

            // Cambio del delimitatore
            CSVFormat csvFormat = CSVFormat.RFC4180.builder().setDelimiter(';').setHeader(EventiHeader.class).build();

            try (CSVPrinter csvPrinter = new CSVPrinter(
                    writer, csvFormat);) {

                for (Evento evento : eventi) {
                    csvPrinter.printRecord(
                            evento.getDataInizio(),
                            evento.getDataFine(),
                            evento.getNome(),
                            evento.getDescrizione(),
                            evento.getEmailResponsabile(),
                            evento.getAula().getNome(),
                            evento.getAula().getEdificio(),
                            evento.getAula().getLuogo(),
                            evento.getTipologiaEvento(),
                            evento.getNomeCorso(),
                            evento.getTipologiaRicorrenza(),
                            evento.getDataFineRicorrenza());
                }
                csvPrinter.flush();
            }
        }
    }
}
