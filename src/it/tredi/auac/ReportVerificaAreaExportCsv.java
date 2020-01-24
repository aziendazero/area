package it.tredi.auac;

import it.tredi.auac.bean.AreaReportInfoVerificaBean;
import it.tredi.auac.bean.ReportVerificaRGAreaPageBean;
import it.tredi.auac.service.UtilService;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;

import com.csvreader.CsvWriter;

public class ReportVerificaAreaExportCsv extends AbstractCsvView{
	private UtilService utilService;

	@Override
	protected void buildCsvDocument(CsvWriter csvWriter, Map<String, Object> model) throws IOException {
	ReportVerificaRGAreaPageBean esportVerificaRGAreaBean = (ReportVerificaRGAreaPageBean) model.get("csvData");
    	DecimalFormat df = new DecimalFormat("#.00");
    	DecimalFormatSymbols ds = new DecimalFormatSymbols(Locale.getDefault());
    	ds.setDecimalSeparator(',');
    	df.setDecimalFormatSymbols(ds);
    	df.setMaximumFractionDigits(2);
    	df.setGroupingUsed(false);

    	//Header       
        csvWriter.write("Area");        
        csvWriter.write("Media Soglia");        
        csvWriter.write("Numero Si/No");
        csvWriter.endRecord();

        for (AreaReportInfoVerificaBean report : esportVerificaRGAreaBean.getAreaReportInfoBeanL()) {
        		csvWriter.write(report.getNome());
        		if(esportVerificaRGAreaBean.isContainReqSoglia())
        			if(report.isContainReqSoglia())
        				csvWriter.write(df.format(report.getMediaSoglia()));
        			else
        				csvWriter.write("");
        		if(esportVerificaRGAreaBean.isContainReqSiNo())
        			if(report.isContainReqSiNo())
        				csvWriter.write(utilService.getTextSiNoMedia(report.getTotSi(), report.getTotNo()));
        		csvWriter.endRecord();

        }      
    }

	public UtilService getUtilService() {
		return utilService;
	}

	public void setUtilService(UtilService utilService) {
		this.utilService = utilService;
	}
	
}
