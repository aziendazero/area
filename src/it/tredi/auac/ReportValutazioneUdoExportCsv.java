package it.tredi.auac;

import it.tredi.auac.bean.ReportValutazioneUdo;
import it.tredi.auac.bean.ReportValutazioneUdoPageBean;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;

import com.csvreader.CsvWriter;

public class ReportValutazioneUdoExportCsv extends AbstractCsvView{

	@Override
	protected void buildCsvDocument(CsvWriter csvWriter, Map<String, Object> model) throws IOException {
    	ReportValutazioneUdoPageBean eportValutazioneUdoBean = (ReportValutazioneUdoPageBean) model.get("csvData");
    	DecimalFormat df = new DecimalFormat("#.00");
    	DecimalFormatSymbols ds = new DecimalFormatSymbols(Locale.getDefault());
    	ds.setDecimalSeparator(',');
    	df.setDecimalFormatSymbols(ds);
    	df.setMaximumFractionDigits(2);
    	df.setGroupingUsed(false);

    	//Header       
        csvWriter.write("Codice Univoco");        
        csvWriter.write("Denominazione");        
        csvWriter.write("Media");
        csvWriter.endRecord();

        for (ReportValutazioneUdo report : eportValutazioneUdoBean.getReportValutazioneUdoL()) {
        		csvWriter.write(report.getIdUnivoco());
        		csvWriter.write(report.getDescr());
        		csvWriter.write(df.format(report.getMedia()));
        		csvWriter.endRecord();

        }      
    }
}
