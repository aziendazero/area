package it.tredi.auac;

import it.tredi.auac.orm.entity.BrancaUdoInstInfo;
import it.tredi.auac.orm.entity.DisciplinaUdoInstInfo;
import it.tredi.auac.orm.entity.FattProdUdoInstInfo;
import it.tredi.auac.orm.entity.RequisitoInst;
import it.tredi.auac.service.UtilService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.csvreader.CsvWriter;
 
public class RequisitiInstVerificaExportCsv extends AbstractCsvView {
	private UtilService utilService;
	
	private static String NEW_LINE = "\n";
    @SuppressWarnings("unchecked")
	@Override
    protected void buildCsvDocument(CsvWriter csvWriter,
            Map<String, Object> model) throws IOException {
    	
    	//Test messagesource
    	//Locale locale = LocaleContextHolder.getLocale();
    	//String message = this.messageSource.getMessage("docTypeEnum.planimetrie", null, LocaleContextHolder.getLocale());
    	//System.out.println(message);
    	
 
        List<RequisitoInst> listReqs = (List<RequisitoInst>) model.get("csvData");
        
        //Map<String, UdoInst> udoInstsInfo = (Map<String, UdoInst>) model.get("csvUdoInstInfo");
        

        
        //In ingresso ho tutti i dati dei requisiti, ma mancano alcuni dati delle UdoInst che devo caricare velocemente con meno query possibili
		//csvWriter.write(req.getUdoInst().getUdoModel().getIdUnivoco());//Codice Univoco
		//csvWriter.write(req.getUdoInst().getTipoUdoTempl().getDescr());//Tipo Udo				tipoUdoTempl.descr
        //req.getUdoInst().getBrancaTempls()
        //req.getUdoInst().getDisciplinaTempls()

/*
 * I dati da esportare sono quelli relativi a:

    UO/UDO di appartenenza (Codice Univoco, denominazione, tipo udo, fattori produttivi, sede operativa, edificio(stabilimento), blocco, piano progressivo, Uo, branche, discipline)
    Requisiti (Cod univoco, testo)
    Assegnatario
    Risposte (autovalutazione, evidenze, note)

(Nota bene: ripetere intestazione UDO su ogni riga)

 */
        //Header
        //Campi per UO
        csvWriter.write("UO - Denominazione");
        
        //Campi per STRUTTURA
        csvWriter.write("STRUTTURA - Denominazione");

		//Campi Edificio
        csvWriter.write("EDIFICIO - Codice");
        csvWriter.write("EDIFICIO - Nome");
        
        csvWriter.write("UDO - Codice Univoco");
        csvWriter.write("UDO - Denominazione");
        csvWriter.write("UDO - Tipo Udo");
        csvWriter.write("UDO - Fattori produttivi");
        csvWriter.write("UDO - Sede Operativa");
        csvWriter.write("UDO - Edificio");
        csvWriter.write("UDO - Blocco");
        csvWriter.write("UDO - Piano");
        csvWriter.write("UDO - Progressivo");
        csvWriter.write("UDO - Denominazione UO");
        csvWriter.write("UDO - Branche/Attività (cod \"spe\")");
        csvWriter.write("UDO - Discipline");
        
        csvWriter.write("Requisito - Id Univoco");
        csvWriter.write("Requisito - Testo");
        csvWriter.write("Requisito - Assegnazione");
        csvWriter.write("Requisito - Autovalutazione");
        csvWriter.write("Requisito - Evidenze");
        csvWriter.write("Requisito - Note");
        
        csvWriter.write("Requisito - Verificatore");
        csvWriter.write("Requisito - Valutazione Verificatore");
        csvWriter.write("Requisito - Note Verificatore");

        csvWriter.endRecord();

        String val = "";
        boolean writeval = false;
        //UdoInst udoInstForExtraInfo;
        for (RequisitoInst req : listReqs) {
        	if(req.getUdoInst() != null) {
        		//udoInstForExtraInfo = udoInstsInfo.get(req.getUdoInst().getClientid());
            	//requisiti della Udo
        		//Campi UO
        		csvWriter.write("");//UO - Denominazione
        		//Campi Struttura
        		csvWriter.write("");//Struttura - Denominazione
        		//Campi Edificio
        		csvWriter.write("");//Edificio - Codice
        		csvWriter.write("");//Edificio - Nome
        		//campi UDO
        		//csvWriter.write(udoInstForExtraInfo.getUdoModel().getIdUnivoco());//Codice Univoco
        		//csvWriter.write(udoInstForExtraInfo.getIdUnivoco());//Codice Univoco
        		csvWriter.write(req.getUdoInst().getIdUnivoco());//Codice Univoco
        		csvWriter.write(req.getUdoInst().getDescr());//Denominazione		requisitoInst.udoInst.descr
        		//csvWriter.write(udoInstForExtraInfo.getTipoUdoTemplDescr());//Tipo Udo				tipoUdoTempl.descr
        		csvWriter.write(req.getUdoInst().getTipoUdoTemplDescr());
        		//csvWriter.write(req.getUdoInst().getTipoUdoExtendedDescr());
        		val = "";
        		writeval = false;
        		for(FattProdUdoInstInfo fatProd : req.getUdoInst().getFattProdUdoInstsInfo()) {
        			if(writeval)
        				val += NEW_LINE;
        			//( Tipo: Posti Letto, Descr: , Accreditati: 29, Autorizzati: 0) 

					val += fatProd.getFullDescr();
        			writeval = true;
        		}        			
        		csvWriter.write(val);//Fattori produttivi

        		csvWriter.write(req.getUdoInst().getDenominazioneSede());//Sede Operativa
        		csvWriter.write(req.getUdoInst().getStabilimento());//Edificio(Stabilimento)
        		csvWriter.write(req.getUdoInst().getBlocco());//Blocco
        		csvWriter.write(req.getUdoInst().getPiano());//Piano
        		if(req.getUdoInst().getProgressivo() == null)
        			csvWriter.write("");//Progressivo
        		else
        			csvWriter.write(req.getUdoInst().getProgressivo().toPlainString());//Progressivo
        		csvWriter.write(req.getUdoInst().getDenominazioneUo());//UDO - Denominazione UO
        		
        		val = "";
        		writeval = false;
        		for(BrancaUdoInstInfo branca : req.getUdoInst().getBrancheUdoInstsInfo()) {
        			if(writeval)
        				val += NEW_LINE;
        			val += branca.getCodice() + " - " + branca.getDescr();
        			writeval = true;
        		}
        		//if(req.getUdoInst().getBrancheInfo() != null)
        		//	val = udoInstForExtraInfo.getBrancheInfo();
        		csvWriter.write(val);//Branche
        		
        		val = "";
        		writeval = false;
        		for(DisciplinaUdoInstInfo disc : req.getUdoInst().getDisciplineUdoInstsInfo()) {
        			if(writeval)
        				val += NEW_LINE;
        			val += disc.getFullDescr();
        			writeval = true;
        		}
        		csvWriter.write(val);//Discipline
        	}
        	else if(req.getUoInst() != null) {
        		//requisiti della UO
        		//Campi UO
        		csvWriter.write(req.getUoInst().getDenominazione());//UO - Denominazione
        		//Campi Struttura
        		csvWriter.write("");//Struttura - Denominazione
        		//Campi Edificio
        		csvWriter.write("");//Edificio - Codice
        		csvWriter.write("");//Edificio - Nome
        		//campi UDO
        		csvWriter.write("");//Codice Univoco
        		csvWriter.write("");//Denominazione
        		csvWriter.write("");//Tipo Udo
        		csvWriter.write("");//Fattori produttivi
        		csvWriter.write("");//Sede Operativa
        		csvWriter.write("");//Edificio(Stabilimento)
        		csvWriter.write("");//Blocco
        		csvWriter.write("");//Piano
        		csvWriter.write("");//Progressivo
                csvWriter.write("");//UDO - Denominazione UO
        		csvWriter.write("");//Branche
        		csvWriter.write("");//Discipline
        		//requisitoInst.uoInst.denominazione
        	}
        	else if(req.getStrutturaInst() != null) {
        		//requisiti della Struttura
        		//Campi UO
        		csvWriter.write("");//UO - Denominazione
        		//Campi Struttura
        		csvWriter.write(req.getStrutturaInst().getDenominazione());//Struttura - Denominazione
        		//Campi Edificio
        		csvWriter.write("");//Edificio - Codice
        		csvWriter.write("");//Edificio - Nome
        		//campi UDO
        		csvWriter.write("");//Codice Univoco
        		csvWriter.write("");//Denominazione
        		csvWriter.write("");//Tipo Udo
        		csvWriter.write("");//Fattori produttivi
        		csvWriter.write("");//Sede Operativa
        		csvWriter.write("");//Edificio(Stabilimento)
        		csvWriter.write("");//Blocco
        		csvWriter.write("");//Piano
        		csvWriter.write("");//Progressivo
                csvWriter.write("");//UDO - Denominazione UO
        		csvWriter.write("");//Branche
        		csvWriter.write("");//Discipline
        		
        		
        		//requisitoInst.uoInst.denominazione
        	}
        	else if(req.getEdificioInst() != null) {
        		//requisiti del Edificio
        		//Campi UO
        		csvWriter.write("");//UO - Denominazione
        		//Campi Struttura
        		csvWriter.write("");//Struttura - Denominazione
        		//Campi Edificio
        		csvWriter.write(req.getEdificioInst().getCodice());//Edificio - Codice
        		csvWriter.write(req.getEdificioInst().getNome());//Edificio - Nome
        		//campi UDO
        		csvWriter.write("");//Codice Univoco
        		csvWriter.write("");//Denominazione
        		csvWriter.write("");//Tipo Udo
        		csvWriter.write("");//Fattori produttivi
        		csvWriter.write("");//Sede Operativa
        		csvWriter.write("");//Edificio(Stabilimento)
        		csvWriter.write("");//Blocco
        		csvWriter.write("");//Piano
        		csvWriter.write("");//Progressivo
                csvWriter.write("");//UDO - Denominazione UO
        		csvWriter.write("");//Branche
        		csvWriter.write("");//Discipline
        		
        		
        		//requisitoInst.uoInst.denominazione
        	}
        	else {
        		//requisiti della domanda
        		csvWriter.write("");//UO - Denominazione
        		//Campi Struttura
        		csvWriter.write(req.getStrutturaInst().getDenominazione());//Struttura - Denominazione
        		//Campi Edificio
        		csvWriter.write("");//Edificio - Codice
        		csvWriter.write("");//Edificio - Nome
        		//campi UDO
        		csvWriter.write("");//Codice Univoco
        		csvWriter.write("");//Denominazione
        		csvWriter.write("");//Tipo Udo
        		csvWriter.write("");//Fattori produttivi
        		csvWriter.write("");//Sede Operativa
        		csvWriter.write("");//Edificio(Stabilimento)
        		csvWriter.write("");//Blocco
        		csvWriter.write("");//Piano
        		csvWriter.write("");//Progressivo
                csvWriter.write("");//UDO - Denominazione UO
        		csvWriter.write("");//Branche
        		csvWriter.write("");//Discipline
        	}
        	
        	//Dati del requisito
        	
        	csvWriter.write(req.getRequisitoTempl().getNome());//Requisito - Id Univoco
            csvWriter.write(req.getRequisitoTempl().getTesto());//Requisito - Testo
            
            //if(req.getUtenteModel() != null)
            //	csvWriter.write(req.getUtenteModel().getUoModel().getDenominazione() + " - " + req.getUtenteModel().getAnagraficaUtenteModel().getCognome() + " " + req.getUtenteModel().getAnagraficaUtenteModel().getNome());//Requisito - Assegnazione
            //else
            //	csvWriter.write("");
            csvWriter.write(req.getAssegnazioneForExport());//Requisito - Assegnazione
            
            //csvWriter.write(req.getAutovalutazioneForExport());//Requisito - Autovalutazione
            csvWriter.write(utilService.getValutazioneFormattedText(req, TypeViewEnum.CSV));//Requisito - Autovalutazione
            
            csvWriter.write(req.getEvidenze());//Requisito - Evidenze
            csvWriter.write(req.getNote());//Requisito - Note
        	
            csvWriter.write(req.getVerificatoreForExport());//Requisito - Verificatore
            
            //csvWriter.write(req.getValutazioneVerificatoreForExport());//Requisito - Valutazione Verificatore
            csvWriter.write(utilService.getValutazioneVerificatoreFormattedText(req, TypeViewEnum.CSV));//Requisito - Valutazione Verificatore
            
            
            csvWriter.write(req.getNoteVerificatore());//Requisito - Note Verificatore

            csvWriter.endRecord();
        }
    }
    
	public UtilService getUtilService() {
		return utilService;
	}
	public void setUtilService(UtilService utilService) {
		this.utilService = utilService;
	}

    /*private Object convertNullToStringEmpty(Object obj) {
    	if(obj == null)
    		return "";
    	else
    		return obj;
    }*/
}