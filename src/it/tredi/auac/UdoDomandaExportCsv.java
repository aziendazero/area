package it.tredi.auac;

import it.tredi.auac.bean.UdoUoInstForList;
import it.tredi.auac.orm.entity.BrancaUdoInstInfo;
import it.tredi.auac.orm.entity.DisciplinaUdoInstInfo;
import it.tredi.auac.orm.entity.FattProdUdoInstInfo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.csvreader.CsvWriter;
 
public class UdoDomandaExportCsv extends AbstractCsvView {
	private static String NEW_LINE = "\n";
    @SuppressWarnings("unchecked")
	@Override
    protected void buildCsvDocument(CsvWriter csvWriter,
            Map<String, Object> model) throws IOException {
 
        List<UdoUoInstForList> listReqs = (List<UdoUoInstForList>) model.get("csvData");
        
        //Map<String, UdoInst> udoInstsInfo = (Map<String, UdoInst>) model.get("csvUdoInstInfo");
        

        
        //In ingresso ho tutti i dati dei requisiti, ma mancano alcuni dati delle UdoInst che devo caricare velocemente con meno query possibili
		//csvWriter.write(req.getUdoInst().getUdoModel().getIdUnivoco());//Codice Univoco
		//csvWriter.write(req.getUdoInst().getTipoUdoTempl().getDescr());//Tipo Udo				tipoUdoTempl.descr
        //req.getUdoInst().getBrancaTempls()
        //req.getUdoInst().getDisciplinaTempls()

/*
 * I dati da esportare sono quelli relativi a:

    UO/UDO di appartenenza (Codice Univoco, denominazione, tipo udo, fattori produttivi, sede operativa, Edificio(stabilimento), blocco, piano progressivo, Uo, branche, discipline)
    Requisiti (Cod univoco, testo)
    Assegnatario
    Risposte (autovalutazione, evidenze, note)

(Nota bene: ripetere intestazione UDO su ogni riga)

 */
        //Header
        //Campi per UO
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
        
        
        csvWriter.endRecord();

        String val = "";
        boolean writeval = false;
        //UdoInst udoInstForExtraInfo;
        for (UdoUoInstForList req : listReqs) {
        	if(req.getUdoInst() != null) {
        		//campi UDO
        		csvWriter.write(req.getUdoInst().getIdUnivoco());//Codice Univoco
        		csvWriter.write(req.getUdoInst().getDescr());//Denominazione
        		csvWriter.write(req.getUdoInst().getTipoUdoTemplDescr());//Tipologia Udo
        		//csvWriter.write(req.getUdoInst().getTipoUdoExtendedDescr());//Tipologia Udo

        		csvWriter.write(req.getUdoInst().getCodiceUlssTerritoriale());//Cod. ULSS
        		csvWriter.write(req.getUdoInst().getCodiceStruttDenomin());//Codice struttura/Denominazione
        		csvWriter.write(req.getUdoInst().getDenomStruttFisicaSede());//Struttura Fisica

        		csvWriter.write(req.getUdoInst().getDenominazioneUo());//Unità Operativa
        		csvWriter.write(req.getUdoInst().getDenominazioneSede());//Sede Operativa

        		csvWriter.write(req.getUdoInst().getIndirizzoSede());//Indirizzo
        		csvWriter.write(req.getUdoInst().getTipoPuntoFisicoSede());//Punto Fisico
        		csvWriter.write(req.getUdoInst().getStabilimento());//Edificio(Stabilimento)
        		csvWriter.write(req.getUdoInst().getBlocco());//Blocco
        		csvWriter.write(req.getUdoInst().getPiano());//Piano
        		if(req.getUdoInst().getProgressivo() == null)
        			csvWriter.write("");//Progressivo
        		else
        			csvWriter.write(req.getUdoInst().getProgressivo().toPlainString());//Progressivo
        		
        		val = "";
        		writeval = false;
        		if(req.getUdoInst().getDirSanitarioCogn() != null && !req.getUdoInst().getDirSanitarioCogn().isEmpty()) {
        			val = req.getUdoInst().getDirSanitarioCogn();
        			writeval = true;
        		}	
        		if(req.getUdoInst().getDirSanitarioNome() != null && !req.getUdoInst().getDirSanitarioNome().isEmpty()) {
        			if(writeval)
        				val += " ";
        			val += req.getUdoInst().getDirSanitarioNome();
        			writeval = true;
        		}	
        		if(req.getUdoInst().getDirSanitarioCfisc() != null && !req.getUdoInst().getDirSanitarioCfisc().isEmpty()) {
        			if(writeval)
        				val += " - ";
        			val += req.getUdoInst().getDirSanitarioCfisc();
        		}	
        		csvWriter.write(val);//Direttore Sanitario
        		
        		
        		csvWriter.write(req.getUdoInst().getStato());//Stato UDO
        		
        		//'dd/MM/yyyy'
        		if(req.getUdoInst().getScadenza() != null) {
	        		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        		csvWriter.write(sdf.format(req.getUdoInst().getScadenza()));//Scadenza
        		} else {
	        		csvWriter.write("");//Scadenza
        		}
        		
        		csvWriter.write(req.getUdoInst().getAnnotations());//Note

        		
        		
        		val = "";
        		writeval = false;
        		for(DisciplinaUdoInstInfo disc : req.getUdoInst().getDisciplineUdoInstsInfo()) {
        			if(writeval)
        				val += NEW_LINE;
        			val += disc.getFullDescr();
        			writeval = true;
        		}
        		csvWriter.write(val);//Discipline
        		
        		val = "";
        		writeval = false;
        		for(BrancaUdoInstInfo branca : req.getUdoInst().getBrancheUdoInstsInfo()) {
        			if(writeval)
        				val += NEW_LINE;
        			val += branca.getCodice() + " - " + branca.getDescr();
        			writeval = true;
        		}
        		csvWriter.write(val);//Branche
        		
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

        		csvWriter.endRecord();
        		
        	}
        	else if(req.getUoInst() != null) {
        		//requisiti della UO
        		csvWriter.write("");//Codice Univoco
        		csvWriter.write(req.getUoInst().getDenominazione());//UO - Denominazione
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

                csvWriter.endRecord();
        	}
        	/*else {
        		//requisiti della domanda
        		csvWriter.write("");//UO - Denominazione
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
        	}*/
        	
        	
        }
    }
    
}