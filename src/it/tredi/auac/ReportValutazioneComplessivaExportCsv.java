package it.tredi.auac;

import it.tredi.auac.bean.ReportValutazioneComplessivaPageBean;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;

import com.csvreader.CsvWriter;

public class ReportValutazioneComplessivaExportCsv extends AbstractCsvView{
	@Override
	protected void buildCsvDocument(CsvWriter csvWriter, Map<String, Object> model) throws IOException {
	ReportValutazioneComplessivaPageBean reportValutazioneComplessivaPageBean = (ReportValutazioneComplessivaPageBean) model.get("csvData");
    	DecimalFormat df = new DecimalFormat("#.00");
    	DecimalFormatSymbols ds = new DecimalFormatSymbols(Locale.getDefault());
    	ds.setDecimalSeparator(',');
    	df.setDecimalFormatSymbols(ds);
    	df.setMaximumFractionDigits(2);
    	df.setGroupingUsed(false);

    	//Header       
        csvWriter.write("Punteggio complessivo");
        csvWriter.endRecord();

        if (reportValutazioneComplessivaPageBean.getMediaComplessiva() != null) {
        		csvWriter.write(df.format(reportValutazioneComplessivaPageBean.getMediaComplessiva()));
        		csvWriter.endRecord();

        }      
    }
}
