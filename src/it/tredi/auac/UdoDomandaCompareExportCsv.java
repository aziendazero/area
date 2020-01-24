package it.tredi.auac;

import it.tredi.auac.bean.CompareUdoBean;
import it.tredi.auac.bean.CompareUdoDomandaPageBean;
import it.tredi.auac.orm.entity.BrancaUdoInstInfo;
import it.tredi.auac.orm.entity.DisciplinaUdoInstInfo;
import it.tredi.auac.orm.entity.FattProdUdoInstInfo;
import it.tredi.auac.orm.entity.UdoInst;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import com.csvreader.CsvWriter;
 
public class UdoDomandaCompareExportCsv extends AbstractCsvView {
	private static String NEW_LINE = "\n";
	@Override
    protected void buildCsvDocument(CsvWriter csvWriter,
            Map<String, Object> model) throws IOException {
 
    	CompareUdoDomandaPageBean compareUdoDOmandaPageBEan = (CompareUdoDomandaPageBean) model.get("csvData");
    	String corrente = "corrente";
    	String precedente = "precedente";
        //Header
        /*Campo identificativo della UdoInst*/
        csvWriter.write("Codice Univoco");//k
        /*Campi da confrontare*/
        csvWriter.write("Denominazione");//Denominazione Udo
        csvWriter.write("Denominazione " + corrente);
        csvWriter.write("Denominazione " + precedente);

        //Struttura giuridica NON CONFRONTARE sarebbe il titolare
        csvWriter.write("Sede Operativa");//Sede Operativa
        csvWriter.write("Sede Operativa " + corrente);
        csvWriter.write("Sede Operativa " + precedente);
        csvWriter.write("Cod. ULSS");//Cod Ulss
        csvWriter.write("Cod. ULSS " + corrente);
        csvWriter.write("Cod. ULSS " + precedente);
        csvWriter.write("Codice struttura/Denominazione");//nella richiesta viene chiamato "Cod Flussi Ministeriali"
        csvWriter.write("Codice struttura/Denominazione " + corrente);
        csvWriter.write("Codice struttura/Denominazione " + precedente);
        csvWriter.write("Unità Operativa");//Unita' operativa
        csvWriter.write("Unità Operativa " + corrente);
        csvWriter.write("Unità Operativa " + precedente);
        
        //Stabilimento/Blocco/Piano/Progressivo
        csvWriter.write("Edificio");//k Stabilimento
        csvWriter.write("Edificio " + corrente);
        csvWriter.write("Edificio " + precedente);
        csvWriter.write("Blocco");//k
        csvWriter.write("Blocco " + corrente);
        csvWriter.write("Blocco " + precedente);
        csvWriter.write("Piano");//k
        csvWriter.write("Piano " + corrente);
        csvWriter.write("Piano " + precedente);
        csvWriter.write("Progressivo");//k
        csvWriter.write("Progressivo " + corrente);
        csvWriter.write("Progressivo " + precedente);
        
        csvWriter.write("Direttore Sanitario");//Direttore Sanitario (Nome/Cognome/Codice Fiscale)
        csvWriter.write("Direttore Sanitario Sanitario " + corrente);
        csvWriter.write("Progressivo " + precedente);
        csvWriter.write("Discipline");//Discipline
        csvWriter.write("Discipline " + corrente);
        csvWriter.write("Discipline " + precedente);
        csvWriter.write("Branche/Attività (cod \"spe\")");//Branche
        csvWriter.write("Branche/Attività (cod \"spe\") " + corrente);
        csvWriter.write("Branche/Attività (cod \"spe\") " + precedente);
		csvWriter.write("Fattori produttivi");//Fattori produttivi
        csvWriter.write("Fattori produttivi " + corrente);
        csvWriter.write("Fattori produttivi " + precedente);
        //Prestazioni
        
        csvWriter.endRecord();

        //UdoInst udoInstForExtraInfo;
        for (CompareUdoBean comp : compareUdoDOmandaPageBEan.getCompareUdoBeanL()) {
   	    	if(comp.isChange()) {
       	    	csvWriter.write(comp.getUdoInst().getIdUnivoco());//Codice Univoco
        		//devo confrontare i dati con la precedente UdoInst
        		writeChange(csvWriter, comp.isDescrChange(), comp.getUdoInst().getDescr(), comp.getOldUdoInst().getDescr());

        		writeChange(csvWriter, comp.isDenominazioneSedeChange(), comp.getUdoInst().getDenominazioneSede(), comp.getOldUdoInst().getDenominazioneSede());//Sede Operativa
        		writeChange(csvWriter, comp.isCodiceUlssTerritorialeChange(), comp.getUdoInst().getCodiceUlssTerritoriale(), comp.getOldUdoInst().getCodiceUlssTerritoriale());//Cod. ULSS
        		writeChange(csvWriter, comp.isCodiceStruttDenominChange(), comp.getUdoInst().getCodiceStruttDenomin(), comp.getOldUdoInst().getCodiceStruttDenomin());//Codice struttura/Denominazione (nella richiesta viene chiamato "Cod Flussi Ministeriali")
        		writeChange(csvWriter, comp.isDenominazioneUoChange(), comp.getUdoInst().getDenominazioneUo(), comp.getOldUdoInst().getDenominazioneUo());//Unità Operativa

                //Stabilimento/Blocco/Piano/Progressivo
        		writeChange(csvWriter, comp.isStabilimentoChange(), comp.getUdoInst().getStabilimento(), comp.getOldUdoInst().getStabilimento());//Edificio(Stabilimento)
        		writeChange(csvWriter, comp.isBloccoChange(), comp.getUdoInst().getBlocco(), comp.getOldUdoInst().getBlocco());//Blocco
        		writeChange(csvWriter, comp.isPianoChange(), comp.getUdoInst().getPiano(), comp.getOldUdoInst().getPiano());//Piano
        		writeChange(csvWriter, comp.isProgressivoChange(), comp.getUdoInst().getProgressivo(), comp.getOldUdoInst().getProgressivo());//Progressivo
        		
        		writeDirettoreSanitarioChange(csvWriter, comp);//Direttore Sanitario (Nome/Cognome/Codice Fiscale)


                writeDisciplineChange(csvWriter, comp);//Discipline
                writeBrancheChange(csvWriter, comp);//Branche
        		writeFattoriProduttiviChange(csvWriter, comp);//Fattori produttivi
                //Prestazioni

        		csvWriter.endRecord();
        	}/* se non e' presente la UdoInst precedente da confrontare non mostro nulla
        		else {
        		//Non esiste una precedente UdoInst
        		csvWriter.write(req.getUdoInst().getDescr());//Denominazione Udo

        		csvWriter.write(req.getUdoInst().getDenominazioneSede());//Sede Operativa
        		csvWriter.write(req.getUdoInst().getCodiceUlssTerritoriale());//Cod. ULSS
        		csvWriter.write(req.getUdoInst().getCodiceStruttDenomin());//Codice struttura/Denominazione (nella richiesta viene chiamato "Cod Flussi Ministeriali")
        		csvWriter.write(req.getUdoInst().getDenominazioneUo());//Unità Operativa

                //Stabilimento/Blocco/Piano/Progressivo
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
        		csvWriter.write(val);//Direttore Sanitario (Nome/Cognome/Codice Fiscale)

        		val = "";
        		writeval = false;
        		for(DisciplinaUdoInstInfo disc : req.getUdoInst().getDisciplineUdoInstsInfo()) {
        			if(writeval)
        				val += NEW_LINE;
        			val += disc.getCodice() + " - "  + disc.getDescr();
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

        			val += "Tipo: " + ((fatProd.getTipo()==null) ? "" : fatProd.getTipo() ) + ", Accreditati: " + (fatProd.getValore2()==null ? "" : fatProd.getValore2()) + ", Autorizzati: " + (fatProd.getValore()==null ? "" : fatProd.getValore());
        			writeval = true;
        		}        			
        		csvWriter.write(val);//Fattori produttivi
                //Prestazioni

        		csvWriter.endRecord();
        	}*/
        }
    }
    
    private void writeChange(CsvWriter csvWriter, boolean isChanged, String value, String oldValue) throws IOException {
    	if(isChanged) {
    		csvWriter.write("Si");
    		csvWriter.write(value);
    		csvWriter.write(oldValue);
    	} else {
    		csvWriter.write("No");
    		csvWriter.write(value);
    		csvWriter.write("");
    	}
    }
    
    private void writeChange(CsvWriter csvWriter, boolean isChanged, BigDecimal value, BigDecimal oldValue) throws IOException {
    	if(isChanged) {
    		csvWriter.write("Si");
    		if(value == null)
    			csvWriter.write("");
    		else
    			csvWriter.write(value.toPlainString());
    		if(oldValue == null)
    			csvWriter.write("");
    		else
    			csvWriter.write(oldValue.toPlainString());
    	} else {
    		csvWriter.write("No");
    		if(value == null)
    			csvWriter.write("");
    		else
    			csvWriter.write(value.toPlainString());
    		csvWriter.write("");
    	}
    }

    private void writeDirettoreSanitarioChange(CsvWriter csvWriter, CompareUdoBean comp) throws IOException {
    	if(comp.isDirettoreSanitarioChange()) {
    		csvWriter.write("Si");
    		csvWriter.write(getDirettoreSanitarioStringVal(comp.getUdoInst()));
    		csvWriter.write(getDirettoreSanitarioStringVal(comp.getOldUdoInst()));
    	} else {
    		csvWriter.write("No");
    		csvWriter.write(getDirettoreSanitarioStringVal(comp.getUdoInst()));
    		csvWriter.write("");
    	}
    }
    
    private String getDirettoreSanitarioStringVal(UdoInst udoInst) {
    	String val = "";
		boolean writeval = false;
		if(udoInst.getDirSanitarioCogn() != null && !udoInst.getDirSanitarioCogn().isEmpty()) {
			val = udoInst.getDirSanitarioCogn();
			writeval = true;
		}	
		if(udoInst.getDirSanitarioNome() != null && !udoInst.getDirSanitarioNome().isEmpty()) {
			if(writeval)
				val += " ";
			val += udoInst.getDirSanitarioNome();
			writeval = true;
		}	
		if(udoInst.getDirSanitarioCfisc() != null && !udoInst.getDirSanitarioCfisc().isEmpty()) {
			if(writeval)
				val += " - ";
			val += udoInst.getDirSanitarioCfisc();
		}	
    	return val;
    }

    private void writeDisciplineChange(CsvWriter csvWriter, CompareUdoBean comp) throws IOException {
    	if(comp.isDisciplineChange()) {
    		csvWriter.write("Si");
    		csvWriter.write(getDisciplineStringVal(comp.getUdoInst()));
    		csvWriter.write(getDisciplineStringVal(comp.getOldUdoInst()));
    	} else {
    		csvWriter.write("No");
    		csvWriter.write(getDisciplineStringVal(comp.getUdoInst()));
    		csvWriter.write("");
    	}
    }

    private String getDisciplineStringVal(UdoInst udoInst) {
		String val = "";
		boolean writeval = false;
		for(DisciplinaUdoInstInfo disc : udoInst.getDisciplineUdoInstsInfo()) {
			if(writeval)
				val += NEW_LINE;
			val += disc.getFullDescr();
			writeval = true;
		}
    	return val;
    }
    
    private void writeBrancheChange(CsvWriter csvWriter, CompareUdoBean comp) throws IOException {
    	if(comp.isBrancheChange()) {
    		csvWriter.write("Si");
    		csvWriter.write(getBrancheStringVal(comp.getUdoInst()));
    		csvWriter.write(getBrancheStringVal(comp.getOldUdoInst()));
    	} else {
    		csvWriter.write("No");
    		csvWriter.write(getBrancheStringVal(comp.getUdoInst()));
    		csvWriter.write("");
    	}
    }
    
    private String getBrancheStringVal(UdoInst udoInst) {
		String val = "";
		boolean writeval = false;
		for(BrancaUdoInstInfo branca : udoInst.getBrancheUdoInstsInfo()) {
			if(writeval)
				val += NEW_LINE;
			val += branca.getCodice() + " - " + branca.getDescr();
			writeval = true;
		}
    	return val;
    }

    private void writeFattoriProduttiviChange(CsvWriter csvWriter, CompareUdoBean comp) throws IOException {
    	if(comp.isFattoriProduttiviChange()) {
    		csvWriter.write("Si");
    		csvWriter.write(getFattoriProduttiviStringVal(comp.getUdoInst()));
    		csvWriter.write(getFattoriProduttiviStringVal(comp.getOldUdoInst()));
    	} else {
    		csvWriter.write("No");
    		csvWriter.write(getFattoriProduttiviStringVal(comp.getUdoInst()));
    		csvWriter.write("");
    	}
    }

    private String getFattoriProduttiviStringVal(UdoInst udoInst) {
		String val = "";
		boolean writeval = false;
		for(FattProdUdoInstInfo fatProd : udoInst.getFattProdUdoInstsInfo()) {
			if(writeval)
				val += NEW_LINE;
			//( Tipo: Posti Letto, Descr: , Accreditati: 29, Autorizzati: 0) 

			val += fatProd.getFullDescr();
			writeval = true;
		}        			
    	return val;
    }
}