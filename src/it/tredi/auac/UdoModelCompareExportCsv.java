package it.tredi.auac;

import it.tredi.auac.bean.CompareUdoModelBean;
import it.tredi.auac.bean.CompareUdoModelPageBean;
import it.tredi.auac.orm.entity.UdoInst;
import it.tredi.auac.orm.entity.UdoModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import com.csvreader.CsvWriter;
 
public class UdoModelCompareExportCsv extends AbstractCsvView {

	@Override
    protected void buildCsvDocument(CsvWriter csvWriter, Map<String, Object> model) throws IOException {
 
		CompareUdoModelPageBean compareUdoModelPageBean = (CompareUdoModelPageBean) model.get("csvData");

    	String corrente = "corrente";
    	String precedente = "precedente";
       
    	//Header
        /*Campo identificativo della UdoInst*/
        csvWriter.write("Codice Univoco");//k
        /*Campi da confrontare*/
        csvWriter.write("Denominazione");//Denominazione Udo
        csvWriter.write("Denominazione " + corrente);
        csvWriter.write("Denominazione " + precedente);

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
        csvWriter.write("Direttore Sanitario Sanitario " + precedente);
        
        csvWriter.endRecord();

        //UdoInst udoInstForExtraInfo;
        for (CompareUdoModelBean comp : compareUdoModelPageBean.getCompareUdoBeanL()) {
   	    	if(comp.isChange()) {
       	    	csvWriter.write(comp.getUdoInst().getIdUnivoco());//Codice Univoco
        		//devo confrontare i dati con la precedente UdoInst
        		writeChange(csvWriter, comp.isDescrChange(), comp.getUdoInst().getDescr(), comp.getUdoModel().getDescr());

                //Stabilimento/Blocco/Piano/Progressivo
        		writeChange(csvWriter, comp.isStabilimentoChange(), comp.getUdoInst().getStabilimento(), comp.getUdoModel().getStabilimento());//Edificio(Stabilimento)
        		writeChange(csvWriter, comp.isBloccoChange(), comp.getUdoInst().getBlocco(), comp.getUdoModel().getBlocco());//Blocco
        		writeChange(csvWriter, comp.isPianoChange(), comp.getUdoInst().getPiano(), comp.getUdoModel().getPiano());//Piano
        		writeChange(csvWriter, comp.isProgressivoChange(), comp.getUdoInst().getProgressivo(), comp.getUdoModel().getProgressivo());//Progressivo
        		
        		writeDirettoreSanitarioChange(csvWriter, comp);//Direttore Sanitario (Nome/Cognome/Codice Fiscale)
        		
        		csvWriter.endRecord();
        	}
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
    
    private void writeDirettoreSanitarioChange(CsvWriter csvWriter, CompareUdoModelBean comp) throws IOException {
    	if(comp.isDirettoreSanitarioChange()) {
    		csvWriter.write("Si");
    		csvWriter.write(getDirettoreSanitarioStringVal(comp.getUdoInst(),null));
    		csvWriter.write(getDirettoreSanitarioStringVal(null,comp.getUdoModel()));
    	} else {
    		csvWriter.write("No");
    		csvWriter.write(getDirettoreSanitarioStringVal(comp.getUdoInst(),null));
    		csvWriter.write("");
    	}
    }
    
    private String getDirettoreSanitarioStringVal(UdoInst udoInst,UdoModel udoModel) {
    	String val = "";
		boolean writeval = false;
		if(udoModel == null) {
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
		}else {
			if(udoModel.getDirSanitarioCogn() != null && !udoModel.getDirSanitarioCogn().isEmpty()) {
				val = udoModel.getDirSanitarioCogn();
				writeval = true;
			}	
			if(udoModel.getDirSanitarioNome() != null && !udoModel.getDirSanitarioNome().isEmpty()) {
				if(writeval)
					val += " ";
				val += udoModel.getDirSanitarioNome();
				writeval = true;
			}	
			if(udoModel.getDirSanitarioCfisc() != null && !udoModel.getDirSanitarioCfisc().isEmpty()) {
				if(writeval)
					val += " - ";
				val += udoModel.getDirSanitarioCfisc();
			}	
		}
    	return val;
    }

    
}