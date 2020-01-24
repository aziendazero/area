package it.tredi.auac;

import it.tredi.auac.orm.entity.TipoUdoTempl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.csvreader.CsvWriter;
 
public class CheckTipoUdo22DomandaExportCsv extends AbstractCsvView {
	//private static String NEW_LINE = "\n";
    @SuppressWarnings("unchecked")
	@Override
    protected void buildCsvDocument(CsvWriter csvWriter, Map<String, Object> model) throws IOException {
 
        List<TipoUdoTempl> listReqs = (List<TipoUdoTempl>) model.get("csvData");
        
        //Header
        csvWriter.write("Controllo completezza Udo");        
        csvWriter.endRecord();

        for (TipoUdoTempl tUodTempl : listReqs) {
            csvWriter.write(tUodTempl.getDescr());
        	csvWriter.endRecord();
        }
    }
    
}