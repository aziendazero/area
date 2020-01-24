package it.tredi.auac;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.csvreader.CsvWriter;

import it.tredi.auac.bean.UserBean;
import it.tredi.auac.bean.XWTitlesBean;
import it.tredi.auac.service.MainService;
import it.tredi.auac.service.UtilService;


public class DomandeRegioneExportCsv extends AbstractCsvView {
	
	private UtilService utilService;
	
	@Override
	protected void buildCsvDocument(CsvWriter csvWriter,Map<String, Object> model) throws IOException {
			XWTitlesBean fascicoli = (XWTitlesBean) model.get("csvData");
	        List<Map<String,String>> titleL = fascicoli.getTitleL();

	        //definizione header documento csv
	        csvWriter.write("Tipo");
	        csvWriter.write("Numero Procedimento");
	        csvWriter.write("Oggetto");
	        csvWriter.write("Titolare");
	        csvWriter.write("Tipo domanda");
	        csvWriter.write("Data creazione");
	        csvWriter.write("Data invio domanda");
	        csvWriter.write("Valutazione domanda");
	        csvWriter.write("Valutazione congruenza");
	        csvWriter.write("Incarico Vdv");
	        csvWriter.write("Inserito RDV");
	        csvWriter.write("Relazione accessibile");
	        csvWriter.write("Crite");
	        csvWriter.write("Chiusura");
	        csvWriter.write("Stato domanda");
	        
	        csvWriter.endRecord();
	        	
	        for (Map<String,String> title: titleL) {
	        	csvWriter.write(title.get("folder_type"));
	        	csvWriter.write(title.get("numero_procedimento"));
	        	csvWriter.write(title.get("oggetto"));
	        	csvWriter.write(title.get("ragione_sociale"));
	        	csvWriter.write(title.get("oggetto_domanda"));
	        	csvWriter.write(title.get("data_creazione"));
	        	csvWriter.write(title.get("data_invio_domanda"));
	        	csvWriter.write(title.get("valutazione_completezza_correttezza"));
	        	csvWriter.write(title.get("valutazione_rispondenza_programmazione"));
	        	csvWriter.write(title.get("conferimento_incarico"));
	        	csvWriter.write(title.get("redazione_rapporto_verifica"));
	        	csvWriter.write(title.get("presentazione_provvedimento"));
	        	csvWriter.write(title.get("valutazione_collegiale"));
	        	csvWriter.write(title.get("data_conclusione"));
	        	csvWriter.write(title.get("folder_status"));
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
