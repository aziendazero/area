package it.tredi.auac;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.csvreader.CsvWriter;

import it.tredi.auac.bean.CsvReportIstruttoriaPageBean;
import it.tredi.auac.bean.UdoUoInstForList;
import it.tredi.auac.orm.entity.BrancaUdoInstInfo;
import it.tredi.auac.orm.entity.DisciplinaUdoInstInfo;
import it.tredi.auac.orm.entity.FattProdUdoInstInfo;
 
public class CsvReportIstruttoriaUdo extends AbstractCsvView {
	private static String NEW_LINE = "\n";

	@Override
    protected void buildCsvDocument(CsvWriter csvWriter,
            Map<String, Object> model) throws IOException {
 
    	CsvReportIstruttoriaPageBean csvReportIstruttoriaPageBean = (CsvReportIstruttoriaPageBean) model.get("csvData");
    	buildCsvDocument(csvWriter, csvReportIstruttoriaPageBean);
	}
	
	
	public void buildCsvDocument(CsvWriter csvWriter,
			CsvReportIstruttoriaPageBean csvReportIstruttoriaPageBean) throws IOException {
         
        //Header
        csvWriter.write("Codice Univoco");//k
        csvWriter.write("Denominazione");//k
        csvWriter.write("Tipologia Udo");//k
        
        csvWriter.write("Cod. ULSS");//k
        csvWriter.write("Codice struttura/Denominazione");//k
        csvWriter.write("Struttura Fisica");//k
        csvWriter.write("Unità Operativa");//k
        csvWriter.write("Sede Operativa");//k
   
        
        csvWriter.write("Indirizzo");//k
        csvWriter.write("Punto Fisico");//k
        csvWriter.write("Edificio");//k Stabilimento
        csvWriter.write("Blocco");//k
        csvWriter.write("Piano");//k
        csvWriter.write("Progressivo");//k
        
        
        csvWriter.write("Direttore Sanitario");//k
        csvWriter.write("Stato UDO");//k
        csvWriter.write("Scadenza");//k
        csvWriter.write("Note");//k
        
        csvWriter.write("Discipline");//k
        csvWriter.write("Branche/Attività (cod \"spe\")");//k
        csvWriter.write("Fattori produttivi");
        
        csvWriter.write("Parere congruenza");//Parere congruenza (Esito)
        csvWriter.write("Note");//Parere congruenza (Esito Note)
        csvWriter.write("Direzione");//Verificatore (Esito operatore e sua direzione)
        csvWriter.write("Presenza N.C.");//Presenza N.C.
        
        csvWriter.write("Uo - Non atti aziendali");//Uo - Non atti aziendali
        
        csvWriter.endRecord();

        String val = "";
        boolean writeval = false;
        //UdoInst udoInstForExtraInfo;
        for (UdoUoInstForList uuifl : csvReportIstruttoriaPageBean.getUdoUoInstForLists()) {
        	if(uuifl.getUdoInst() != null) {
        		//campi UDO
        		csvWriter.write(uuifl.getUdoInst().getIdUnivoco());//Codice Univoco
        		csvWriter.write(uuifl.getUdoInst().getDescr());//Denominazione
        		csvWriter.write(uuifl.getUdoInst().getTipoUdoTemplDescr());//Tipologia Udo
        		//csvWriter.write(req.getUdoInst().getTipoUdoExtendedDescr());//Tipologia Udo

        		csvWriter.write(uuifl.getUdoInst().getCodiceUlssTerritoriale());//Cod. ULSS
        		csvWriter.write(uuifl.getUdoInst().getCodiceStruttDenomin());//Codice struttura/Denominazione
        		csvWriter.write(uuifl.getUdoInst().getDenomStruttFisicaSede());//Struttura Fisica

        		csvWriter.write(uuifl.getUdoInst().getDenominazioneUo());//Unità Operativa
        		csvWriter.write(uuifl.getUdoInst().getDenominazioneSede());//Sede Operativa

        		csvWriter.write(uuifl.getUdoInst().getIndirizzoSede());//Indirizzo
        		csvWriter.write(uuifl.getUdoInst().getTipoPuntoFisicoSede());//Punto Fisico
        		csvWriter.write(uuifl.getUdoInst().getStabilimento());//Edificio(Stabilimento)
        		csvWriter.write(uuifl.getUdoInst().getBlocco());//Blocco
        		csvWriter.write(uuifl.getUdoInst().getPiano());//Piano
        		if(uuifl.getUdoInst().getProgressivo() == null)
        			csvWriter.write("");//Progressivo
        		else
        			csvWriter.write(uuifl.getUdoInst().getProgressivo().toPlainString());//Progressivo
        		
        		val = "";
        		writeval = false;
        		if(uuifl.getUdoInst().getDirSanitarioCogn() != null && !uuifl.getUdoInst().getDirSanitarioCogn().isEmpty()) {
        			val = uuifl.getUdoInst().getDirSanitarioCogn();
        			writeval = true;
        		}	
        		if(uuifl.getUdoInst().getDirSanitarioNome() != null && !uuifl.getUdoInst().getDirSanitarioNome().isEmpty()) {
        			if(writeval)
        				val += " ";
        			val += uuifl.getUdoInst().getDirSanitarioNome();
        			writeval = true;
        		}	
        		if(uuifl.getUdoInst().getDirSanitarioCfisc() != null && !uuifl.getUdoInst().getDirSanitarioCfisc().isEmpty()) {
        			if(writeval)
        				val += " - ";
        			val += uuifl.getUdoInst().getDirSanitarioCfisc();
        		}	
        		csvWriter.write(val);//Direttore Sanitario
        		
        		
        		csvWriter.write(uuifl.getUdoInst().getStato());//Stato UDO
        		
        		//'dd/MM/yyyy'
        		if(uuifl.getUdoInst().getScadenza() != null) {
	        		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        		csvWriter.write(sdf.format(uuifl.getUdoInst().getScadenza()));//Scadenza
        		} else {
	        		csvWriter.write("");//Scadenza
        		}
        		
        		csvWriter.write(uuifl.getUdoInst().getAnnotations());//Note

        		
        		
        		val = "";
        		writeval = false;
        		for(DisciplinaUdoInstInfo disc : uuifl.getUdoInst().getDisciplineUdoInstsInfo()) {
        			if(writeval)
        				val += NEW_LINE;
        			
        			val += disc.getFullDescr();
        			writeval = true;
        		}
        		csvWriter.write(val);//Discipline
        		
        		val = "";
        		writeval = false;
        		for(BrancaUdoInstInfo branca : uuifl.getUdoInst().getBrancheUdoInstsInfo()) {
        			if(writeval)
        				val += NEW_LINE;
        			val += branca.getCodice() + " - " + branca.getDescr();
        			writeval = true;
        		}
        		csvWriter.write(val);//Branche
        		
        		val = "";
        		writeval = false;
        		for(FattProdUdoInstInfo fatProd : uuifl.getUdoInst().getFattProdUdoInstsInfo()) {
        			if(writeval)
        				val += NEW_LINE;
        			//( Tipo: Posti Letto, Descr: , Accreditati: 29, Autorizzati: 0) 

					val += fatProd.getFullDescr();
        			writeval = true;
        		}        			
        		csvWriter.write(val);//Fattori produttivi

                csvWriter.write(uuifl.getEsito());//Parere congruenza (Esito)
                csvWriter.write(uuifl.getEsitoNote());//Parere congruenza (Esito Note)
        		val = "";
        		writeval = false;
        		if(uuifl.getEsitoOperatore() != null && !uuifl.getEsitoOperatore().isEmpty()) {
        			val = uuifl.getEsitoOperatore();
            		writeval = true;
        		}
        		if(uuifl.getEsitoDirezioneOperatore() != null && !uuifl.getEsitoDirezioneOperatore().isEmpty()) {
        			if(writeval)
        				val += " - ";
        			val += uuifl.getEsitoDirezioneOperatore();
        		}
                csvWriter.write(val);//Verificatore (Esito operatore e sua direzione)

                if(csvReportIstruttoriaPageBean.getDomandaInstUoInstUdoInstClientIdsConRequisitiNonConformi().contains(uuifl.getClientId()))
                	csvWriter.write("*");//Presenza N.C.
                else
                	csvWriter.write("");//Presenza N.C.
                
            	csvWriter.write("");//Uo - Non atti aziendali
        		csvWriter.endRecord();
        		
        	} else if(uuifl.getUoInst() != null) {
        		//requisiti della UO
        		csvWriter.write("");//Codice Univoco
        		csvWriter.write(uuifl.getUoInst().getDenominazione());//UO - Denominazione
        		csvWriter.write("");//Tipologia Udo
        		
        		csvWriter.write("");//Cod. ULSS
        		csvWriter.write("");//Codice struttura/Denominazione
        		csvWriter.write("");//Struttura Fisica
        		csvWriter.write("");//Unità Operativa
        		csvWriter.write("");//Sede Operativa
        		
                csvWriter.write("");//Indirizzo
                csvWriter.write("");//Punto Fisico
                csvWriter.write("");//Edificio
                csvWriter.write("");//Blocco
                csvWriter.write("");//Piano
                csvWriter.write("");//Progressivo
                
                
                csvWriter.write("");//Direttore Sanitario
                csvWriter.write("");//Stato UDO
                csvWriter.write("");//Scadenza
                csvWriter.write("");//Note
                
                csvWriter.write("");//Discipline
                csvWriter.write("");//Branche/Attività (cod \"spe\")
                csvWriter.write("");//Fattori produttivi

                csvWriter.write("");//Parere congruenza (Esito)
                csvWriter.write("");//Parere congruenza (Esito Note)
                csvWriter.write("");//Verificatore (Esito operatore e sua direzione)
                if(csvReportIstruttoriaPageBean.getDomandaInstUoInstUdoInstClientIdsConRequisitiNonConformi().contains(uuifl.getClientId()))
                	csvWriter.write("*");//Presenza N.C.
                else
                	csvWriter.write("");//Presenza N.C.

                if("UO_MODEL".equalsIgnoreCase(uuifl.getUoInst().getProvenienzaUo()))
                	csvWriter.write("*");//Uo - Non atti aziendali
                else
	            	csvWriter.write("");//Uo - Non atti aziendali
                
                csvWriter.endRecord();
        	} else {
        		//requisiti della domanda
        		csvWriter.write("");//Codice Univoco
        		csvWriter.write("Requisiti Generali Aziendali");//Denominazione
        		csvWriter.write("");//Tipologia Udo
        		
        		csvWriter.write("");//Cod. ULSS
        		csvWriter.write("");//Codice struttura/Denominazione
        		csvWriter.write("");//Struttura Fisica
        		csvWriter.write("");//Unità Operativa
        		csvWriter.write("");//Sede Operativa
        		
                csvWriter.write("");//Indirizzo
                csvWriter.write("");//Punto Fisico
                csvWriter.write("");//Edificio
                csvWriter.write("");//Blocco
                csvWriter.write("");//Piano
                csvWriter.write("");//Progressivo
                
                
                csvWriter.write("");//Direttore Sanitario
                csvWriter.write("");//Stato UDO
                csvWriter.write("");//Scadenza
                csvWriter.write("");//Note
                
                csvWriter.write("");//Discipline
                csvWriter.write("");//Branche/Attività (cod \"spe\")
                csvWriter.write("");//Fattori produttivi

                csvWriter.write("");//Parere congruenza (Esito)
                csvWriter.write("");//Parere congruenza (Esito Note)
                csvWriter.write("");//Verificatore (Esito operatore e sua direzione)
                if(csvReportIstruttoriaPageBean.getDomandaInstUoInstUdoInstClientIdsConRequisitiNonConformi().contains(uuifl.getClientId()))
                	csvWriter.write("*");//Presenza N.C.
                else
                	csvWriter.write("");//Presenza N.C.

            	csvWriter.write("");//Uo - Non atti aziendali
                csvWriter.endRecord();
        	}
        	
        	
        }
    }
    
}