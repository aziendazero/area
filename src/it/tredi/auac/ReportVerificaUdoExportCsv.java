package it.tredi.auac;

import it.tredi.auac.bean.ReportValutazioneUdo;
import it.tredi.auac.bean.ReportVerificaUdoPageBean;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;

import com.csvreader.CsvWriter;


public class ReportVerificaUdoExportCsv extends AbstractCsvView {
		@Override
		protected void buildCsvDocument(CsvWriter csvWriter, Map<String, Object> model) throws IOException {
	    	ReportVerificaUdoPageBean reportVerificaUdoPageBean = (ReportVerificaUdoPageBean) model.get("csvData");
	//		ReportValutazioneUdoPageBean esportaReportValutazioneUdo = ( ReportValutazioneUdoPageBean) model.get("csvData");
			
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

	        for (ReportValutazioneUdo tReportValutazioneUdoL : reportVerificaUdoPageBean.getReportValutazioneUdoL()) {
	            csvWriter.write(tReportValutazioneUdoL.getIdUnivoco());
	            csvWriter.write(tReportValutazioneUdoL.getDescr());
	            csvWriter.write(df.format(tReportValutazioneUdoL.getMedia()));
	        	csvWriter.endRecord();
	        }
	    }
}
