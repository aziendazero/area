package it.tredi.auac;

import it.tredi.auac.bean.CheckPostiLettoDomandaPageBean;
import it.tredi.auac.bean.CheckPostiLettoDomandaPageBeanEntity;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;

import com.csvreader.CsvWriter;

public class CheckControlloPostiLettoExportCsv extends AbstractCsvView {
    @Override
	protected void buildCsvDocument(CsvWriter csvWriter, Map<String, Object> model) throws IOException {
    	CheckPostiLettoDomandaPageBean esportaPostiLettoDomanda = (CheckPostiLettoDomandaPageBean) model.get("csvData");
        
    	DecimalFormat df = new DecimalFormat();
    	df.setMaximumFractionDigits(0);
    	df.setGroupingUsed(false);
    	
    	//Header
        //csvWriter.write("Disciplina, Posti letto accreditati, Posti letto autorizzati" + NEW_LINE);        
        csvWriter.write("Disciplina");        
        csvWriter.write("Posti letto accreditati");        
        csvWriter.write("Posti letto autorizzati");        
        csvWriter.endRecord();

        for (CheckPostiLettoDomandaPageBeanEntity postiLetto : esportaPostiLettoDomanda.getCheckPostiLettoDomandaPageBeanEntityList()) {
            //csvWriter.write(postiLetto.getDescr() + ", " + postiLetto.getPostiLettoEntity().getPostiLettoTotaliAccreditati() + ", " + postiLetto.getPostiLettoEntity().getPostiLettoTotaliAutorizzati() + NEW_LINE);
            csvWriter.write(postiLetto.getDescr());
            csvWriter.write(df.format(postiLetto.getPostiLettoEntity().getPostiLettoTotaliAccreditati()));
            csvWriter.write(df.format(postiLetto.getPostiLettoEntity().getPostiLettoTotaliAutorizzati()));
        	csvWriter.endRecord();
        }
        
        //csvWriter.write("Totale, " + esportaPostiLettoDomanda.getPostiLettoTotaliAccreditati() + ", " + esportaPostiLettoDomanda.getPostiLettoTotaliAutorizzati() + NEW_LINE);        
        csvWriter.write("Totale");        
        csvWriter.write(df.format(esportaPostiLettoDomanda.getPostiLettoTotaliAccreditati()));        
        csvWriter.write(df.format(esportaPostiLettoDomanda.getPostiLettoTotaliAutorizzati()));
        csvWriter.endRecord();
    }
}
