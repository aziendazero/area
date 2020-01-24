package it.tredi.auac;

import it.tredi.auac.bean.RelazioneConclusivaPageBean;
import it.tredi.auac.orm.entity.AttoModel;
import it.tredi.auac.orm.entity.UtenteModel;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
 
public class RelazioneConclusivaTxt extends AbstractTextView {
	private static String NEW_LINE = "\r\n";
	@Override
    protected void buildTextDocument(PrintWriter writer, Map<String, Object> model) throws IOException {
 
    	RelazioneConclusivaPageBean rcpb = (RelazioneConclusivaPageBean) model.get("data");
        
        //Scrivo il testo
    	/* 
    	//testo di prova con UTF-8
    	String KALIMAH = "\u0644\u064e\u0622 \u0625\u0650\u0644\u0670\u0647\u064e \u0625\u0650\u0644\u0651\u064e\u0627 \u0627\u0644\u0644\u0647\u064f \u0645\u064f\u062d\u064e\u0645\u0651\u064e\u062f\u064c \u0631\u0651\u064e\u0633\u064f\u0648\u0652\u0644\u064f \u0627\u0644\u0644\u0647\u0650";
    	writer.write("Testo di prova");
    	writer.write(NEW_LINE);
    	writer.write(KALIMAH);
    	*/
    	
    	//date in dd/mm/yyyy %td/%tm/%tY
    	//stringa senza modifiche %s
    	//numeri interi come sono %d
    	/*
			Floating point formatting

		    %f       : will print the number as it is.
		    %15f    : will pint the number as it is. If the number has less than 15 digits, the output will be padded on the left.
		    %.8f : will print maximum 8 decimal digits of the number.
		    %9.4f : will print maximum 4 decimal digits of the number. The output will occupy 9 characters at least. If the number of digits is not enough, it will be padded
    	*/
    	writer.write(String.format("In data %s il titolare %s ha presentato la domanda di %s procedimento n° %s per le UO/UDO di seguito elencate, già autorizzate/accreditate con i provvedimenti indicati:", 
    					rcpb.getDataInvioDomanda(), rcpb.getTitolareDomanda().getRagSoc(), rcpb.getTipoProcTempl().getDescr(), rcpb.getNumeroProcedimento()));
    	writer.write(NEW_LINE);
    	for(AttoModel atto : rcpb.getAttiTipoProcedimentoUdoModelUdoInsts()) {
        	writer.write(atto.getTipoAtto().getDescr() + " " + atto.getNumero() + " del " + atto.getAnno() + NEW_LINE);
    	}
		writer.write(NEW_LINE);
    	
    	if(rcpb.getUltimaDataRichiestaIntegrazione() == null) {
    		writer.write("L'istruttoria della domanda non ha evidenziato rilievi");
    	} else {
    		writer.write("L'istruttoria della domanda ha evidenziato i seguenti rilievi:");
        	if(rcpb.getNoteIntegrazioneIstruttoria() != null && !rcpb.getNoteIntegrazioneIstruttoria().isEmpty()) {
        		writer.write(NEW_LINE);
        		writer.write(rcpb.getNoteIntegrazioneIstruttoria());
        	}
        	if(rcpb.getNoteIntegrazioneVerifica() != null && !rcpb.getNoteIntegrazioneVerifica().isEmpty()) {
        		writer.write(NEW_LINE);
        		writer.write(rcpb.getNoteIntegrazioneVerifica());
        	}
        	if(rcpb.getNoteValutazioneCongruenzaProgrammazione() != null && !rcpb.getNoteValutazioneCongruenzaProgrammazione().isEmpty()) {
        		writer.write(NEW_LINE);
        		writer.write(rcpb.getNoteValutazioneCongruenzaProgrammazione());
        	}
    		writer.write(NEW_LINE);
        	writer.write(String.format("che hanno determinato una richiesta di integrazioni in data %td/%tm/%tY", rcpb.getUltimaDataRichiestaIntegrazione(), rcpb.getUltimaDataRichiestaIntegrazione(), rcpb.getUltimaDataRichiestaIntegrazione()));
        	writer.write(String.format(" integrazioni pervenute dal richiedente in data %td/%tm/%tY con risoluzione completa/parziale da integrare in sede di verifica dei chiarimenti richiesti.", rcpb.getUltimaDataInvioIntegrazione(), rcpb.getUltimaDataInvioIntegrazione(), rcpb.getUltimaDataInvioIntegrazione()));
    	}
		writer.write(NEW_LINE);
		writer.write(NEW_LINE);

    	if(rcpb.getVerificatoreTeamLeader() != null) {
    		writer.write(String.format("In data %td/%tm/%tY il Team Leader %s incaricato della Visita di Verifica, ha trasmesso il piano di verifica, che si è svolto nei GG %s e che ha coinvolto in qualità di verificatori i Sigg:",
    				rcpb.getDataRedazioneRapportoVerifica(), rcpb.getDataRedazioneRapportoVerifica(), rcpb.getDataRedazioneRapportoVerifica(), rcpb.getVerificatoreTeamLeader().getAnagraficaUtenteModel().getCognomeNome(),
    				rcpb.getDateSvolgimentoVerifica()));
    		writer.write(NEW_LINE);
    		for(UtenteModel um : rcpb.getTeamVerificatori()) {
        		writer.write(um.getAnagraficaUtenteModel().getCognomeNome());
        		writer.write(NEW_LINE);
    		}
    		writer.write(NEW_LINE);
    		writer.write(String.format("In data %td/%tm/%tY il Team Leader ha inviato al titolare richiedente il rapporto di verifica che è stato dallo stesso accolto ", rcpb.getDataRedazioneRapportoVerifica(), rcpb.getDataRedazioneRapportoVerifica(), rcpb.getDataRedazioneRapportoVerifica()));
    		if(rcpb.getConvalidaRapportoVerificaNote() == null || rcpb.getConvalidaRapportoVerificaNote().isEmpty()) {
    			writer.write("senza osservazioni");
    		} else {
    			writer.write("con osservazioni (che si riportano di seguito)");
        		writer.write(NEW_LINE);
        		writer.write(rcpb.getConvalidaRapportoVerificaNote());
    		}
    		writer.write(NEW_LINE);
    		writer.write(NEW_LINE);
    		
			writer.write("Il rapporto di verifica, relativamente alle UO/UdO esaminate, ha evidenziato i seguenti rilievi:______________, proponendo le seguenti prescrizioni, e tempi di adeguamento_________");
    		writer.write(NEW_LINE);
    		writer.write(NEW_LINE);
    	}
    	
    	writer.write("Si propone alla CRITE l'approvazione della proposta di DGR di");
		writer.write(NEW_LINE);
    	writer.write("- Autorizzazione/Accreditamento per le UO/UdO di seguito contrassegnate da Au/Ac, per le quali non sono emersi rilievi di completezza, correttezza e coerenza con la programmazione");
		writer.write(NEW_LINE);
    	writer.write("- Diniego dell'Autorizzazione/Accreditamento per le UO/UdO contrassegnate da NAu/NAc, per i motivi indicati");
		writer.write(NEW_LINE);
    	writer.write("- Autorizzazione/Accreditamento con prescrizioni per le UO/UdO contrassegnate da PAu/PAc, a condizione che vengano attuate le prescrizioni nei tempi indicati.");
		writer.write(NEW_LINE);
		writer.write(NEW_LINE);
		
		writer.write("L'esito della CRITE (va in giunta e torna come DGR) consegna all'owner un elenco di UO/UdO sulle quali riportare \"Au/Ac con prescrizioni\" o \"non Au/Ac\" o \"Au/Ac\", allegando il relativo provvedimento");
}
    
}