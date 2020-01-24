package it.tredi.auac;

import it.tredi.auac.bean.ReportVerificaRGPageBean;
import it.tredi.auac.orm.entity.RequisitoInst;
import it.tredi.auac.service.UtilService;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;

import com.csvreader.CsvWriter;

public class ReportVerificaRGExportCsv extends AbstractCsvView{
	private UtilService utilService;
	
	@Override
	protected void buildCsvDocument(CsvWriter csvWriter, Map<String, Object> model) throws IOException {
    	ReportVerificaRGPageBean reportVerificaRGPageBean= (ReportVerificaRGPageBean) model.get("csvData");
    	DecimalFormat df = new DecimalFormat("#.00");
    	DecimalFormatSymbols ds = new DecimalFormatSymbols(Locale.getDefault());
    	ds.setDecimalSeparator(',');
    	df.setDecimalFormatSymbols(ds);
    	df.setMaximumFractionDigits(2);
    	df.setGroupingUsed(false);

    	//Header     
        csvWriter.write("Id Univoco REQ");        
        csvWriter.write("Testo Requisito");        
        csvWriter.write("Valutazione Soglia");
        csvWriter.write("Valutazionec Si/No"); 
        csvWriter.endRecord();

        for (RequisitoInst requisiti : reportVerificaRGPageBean.getRequisitoInstL()) {
        		if(!(requisiti.getRequisitoTempl().getTipoRisposta() != null && 
        				(requisiti.getRequisitoTempl().getTipoRisposta().getNome().equals("Quantitativo") || requisiti.getRequisitoTempl().getTipoRisposta().getNome().equals("Quantitativa")))) {
        			csvWriter.write(requisiti.getRequisitoTempl().getNome());
            		csvWriter.write(requisiti.getRequisitoTempl().getTesto());

            if(requisiti.getRequisitoTempl().getTipoRisposta() != null && requisiti.getRequisitoTempl().getTipoRisposta().getNome().equals("Soglia"))
            		csvWriter.write(utilService.getValutazioneVerificatoreFormattedText(requisiti, TypeViewEnum.CSV));
            else
            		csvWriter.write("");
            
            if(requisiti.getRequisitoTempl().getTipoRisposta() != null && requisiti.getRequisitoTempl().getTipoRisposta().getNome().equals("Si/No"))
            		csvWriter.write(utilService.getValutazioneVerificatoreFormattedText(requisiti, TypeViewEnum.CSV));
            else
            		csvWriter.write("");
        		csvWriter.endRecord();
        		}
        }
        
        csvWriter.write("");      
        csvWriter.write("");
        if(reportVerificaRGPageBean.isContainReqSoglia() && reportVerificaRGPageBean.getMediaSoglia() != null)
        		csvWriter.write(df.format(reportVerificaRGPageBean.getMediaSoglia()));
        else
        		csvWriter.write("");
        if(reportVerificaRGPageBean.isContainReqSiNo())
        		csvWriter.write(utilService.getTextSiNoMedia(reportVerificaRGPageBean.getTotSi(), reportVerificaRGPageBean.getTotNo()));
        else
        		csvWriter.write("");
        csvWriter.endRecord();
    }

	public UtilService getUtilService() {
		return utilService;
	}

	public void setUtilService(UtilService utilService) {
		this.utilService = utilService;
	}
}
