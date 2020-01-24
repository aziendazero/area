package it.tredi.auac;

import it.highwaytech.apps.generic.XMLDocumento;
import it.highwaytech.util.Text;
import it.tredi.auac.bean.AutovalutazionePageBean;
import it.tredi.auac.bean.JobBean;
import it.tredi.auac.bean.UserBean;
import it.tredi.auac.dao.DocumentoDao;
import it.tredi.auac.dao.FolderDao;
import it.tredi.auac.dao.RequisitoInstDao;
import it.tredi.auac.dao.StoricoRisposteRequisitiDao;
import it.tredi.auac.orm.entity.RequisitoInst;
import it.tredi.auac.orm.entity.StoricoRisposteRequisiti;
import it.tredi.auac.service.AclService;
import it.tredi.auac.service.AutovalutazioneService;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.dom4j.Element;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class AssegnazioneMassivaNoteEvidenzeRunnable implements Runnable {

	private JobBean neamJobBean;
	private AutovalutazionePageBean autovalutazionePageBean;
	private RequisitoInstDao requisitoInstDao;
	private StoricoRisposteRequisitiDao storicoRisposteRequisitiDao;
	private UserBean userBean;
	private PlatformTransactionManager txManager;
	private ExtraWayService _xwService;
	private AutovalutazioneService autovalutazioneService;
	private String doamndaFolderIDUnit;
	private AclService aclService;

	public AssegnazioneMassivaNoteEvidenzeRunnable(JobBean neamJobBean, AutovalutazionePageBean autovalutazionePageBean, RequisitoInstDao requisitoInstDao, StoricoRisposteRequisitiDao storicoRisposteRequisitiDao,
			UserBean userBean, PlatformTransactionManager txManager, ExtraWayService _xwService, AutovalutazioneService autovalutazioneService, String doamndaFolderIDUnit, AclService aclService) {
		this.neamJobBean = neamJobBean;
		this.autovalutazionePageBean = autovalutazionePageBean;
		this.requisitoInstDao = requisitoInstDao;
		this.storicoRisposteRequisitiDao = storicoRisposteRequisitiDao;
		this.userBean = userBean;
		this.txManager = txManager;
		this._xwService = _xwService;
		this.autovalutazioneService = autovalutazioneService;
		this.doamndaFolderIDUnit = doamndaFolderIDUnit;
		this.aclService = aclService;
	}

	@Override
	public void run() {
		
		RequisitoInst copiedRequisitoInst = requisitoInstDao.findByClientId(autovalutazionePageBean.getCopiedRequisitoInst().getClientid()); //load jpa refreshed copiedRequisitoInst
		
		//caricamento del fascicolo della domanda e del sottofascicolo delle evidenze (occorre avere idUnit del fascicolo delle evidenze per fare upload dei file)
		String evidenzeFolderIdIUnit = "";
		if (autovalutazionePageBean.isNeamEvidenzeChecked()) {		
			try {
				XMLDocumento domandaFolderDocument = _xwService.loadDocument(Integer.parseInt(doamndaFolderIDUnit));
				String domandaFolderNumFasc = domandaFolderDocument.getAttributeValue("//fascicolo/@numero");
				XMLDocumento evidenzeFolderDocument = FolderDao.findEvidenzeSubFolder(_xwService, domandaFolderNumFasc);
				evidenzeFolderIdIUnit = evidenzeFolderDocument.getAttributeValue("/Response/Document/@idIUnit", "");			
			}
			catch (Exception e) {
	//TODO - bisognerebbe segnalare errore ed uscire			
				e.printStackTrace();
			}
		}
		
		Map<String, String> checkM = autovalutazionePageBean.getCheckM();
		if(autovalutazionePageBean.isNeamEvidenzeChecked() || autovalutazionePageBean.isNeamNoteChecked()) {
			for (RequisitoInst oldRequisitoInst:autovalutazionePageBean.getFilteredRequisitoInstL()) { //per ogni requisito nella pagina
				
				if (oldRequisitoInst.getClientid().equals(copiedRequisitoInst.getClientid())) {
					neamJobBean.incrementCurrent();
					continue;
				}
				//se il requisito è checked
				if (checkM.containsKey(oldRequisitoInst.getClientid())) {
					
					//start transaction programmatically
					try {
						DefaultTransactionDefinition def = new DefaultTransactionDefinition();
						TransactionStatus status = txManager.getTransaction(def);					
						
						try {
							String requisitoClientid = oldRequisitoInst.getClientid();
							
							RequisitoInst requisitoInst = requisitoInstDao.findByClientId(requisitoClientid); //load jpa refreshed requisitoInst
							
							//nel caso di VALUTATORE_INTERNO, se il requisito non gli è stato assegnato -> skip to next one
							//if (userBean.isVALUTATORE_INTERNO() && !requisitoInst.getUtenteModel().getClientid().equals(userBean.getUtenteModel().getClientid())) {
							if (!aclService.userCanEvaluateRequisito(this.autovalutazionePageBean.getDomandaStato(), this.userBean, requisitoInst)) {
								neamJobBean.incrementCurrent();
								continue;
							}
		
							//note
							String noteStoria = "";
							if (autovalutazionePageBean.isNeamNoteChecked()) {
								String note = copiedRequisitoInst.getNote() == null? "" : copiedRequisitoInst.getNote(); 
								String oldNote = requisitoInst.getNote() == null? "" : requisitoInst.getNote();
								if (!note.equals(oldNote)) {
									noteStoria = note.length() == 0? "---Cancellate note---" : note;
								}
								requisitoInst.setNote(note);						
							}
		
							//evidenze
							String evidenzeStoria = "";		
							if (autovalutazionePageBean.isNeamEvidenzeChecked()) {
								String evidenze = copiedRequisitoInst.getEvidenze() == null? "" : copiedRequisitoInst.getEvidenze(); 
								String oldEvidenze = requisitoInst.getEvidenze() == null? "" : requisitoInst.getEvidenze();
								if (!evidenze.equals(oldEvidenze)) {
									evidenzeStoria = evidenze.length() == 0? "---Cancellate evidenze---" : evidenze;
								}
								requisitoInst.setEvidenze(evidenze);						
							}
							
							//storico
							if (noteStoria.length() > 0 || evidenzeStoria.length() > 0) {
								StoricoRisposteRequisiti storicoRisposteRequisiti = autovalutazioneService.buildStoricoRisposteRequisiti(requisitoInst, userBean, "", noteStoria, evidenzeStoria, "");
								storicoRisposteRequisitiDao.save(storicoRisposteRequisiti);
								requisitoInst.setNumStorico(requisitoInst.getNumStorico() + 1);	
							}				
											
							requisitoInstDao.save(requisitoInst);		
							
							//file evidenze (XW)
							if (autovalutazionePageBean.isNeamEvidenzeChecked()) {
								duplicateEvidenzeDocuments(copiedRequisitoInst.getClientid(), requisitoClientid, evidenzeFolderIdIUnit);
								//refreshLinks
								autovalutazioneService.refreshLinks(requisitoClientid, _xwService);						
							}
							/*
							oldRequisitoInst.setAllegatiNRecord(requisitoInst.getAllegatiNRecord());
							oldRequisitoInst.setNote(requisitoInst.getNote());
							oldRequisitoInst.setEvidenze(requisitoInst.getEvidenze());
							oldRequisitoInst.setNumStorico(oldRequisitoInst.getNumStorico());*/
						}
						catch (Exception e) {			
							e.printStackTrace();
							txManager.rollback(status);					
							throw e;
						}
						
						//commit transaction programmatically
						txManager.commit(status);				
					}
					catch (Exception te) {
						te.printStackTrace();
	//TODO - occorre da qualche parte mostrare quali errori si sono verificati?					
					}
					
					neamJobBean.incrementCurrent();
				}
				
			}
		} else if(autovalutazionePageBean.isNeamNoteVerifChecked()) {
			String note = copiedRequisitoInst.getNoteVerificatore() == null? "" : copiedRequisitoInst.getNoteVerificatore(); 
			String noteVerifStoria;
			String oldNote;
			for (RequisitoInst oldRequisitoInst:autovalutazionePageBean.getFilteredRequisitoInstL()) { //per ogni requisito nella pagina
				//se il requisito è checked
				if (checkM.containsKey(oldRequisitoInst.getClientid())) {
					if (oldRequisitoInst.getClientid().equals(copiedRequisitoInst.getClientid())) {
						neamJobBean.incrementCurrent();
						continue;
					}
					if (!aclService.userCanVerifyRequisito(autovalutazionePageBean.getDomandaStato(), userBean, oldRequisitoInst)) {
						neamJobBean.incrementCurrent();
						continue;
					}

					//start transaction programmatically
					try {
						DefaultTransactionDefinition def = new DefaultTransactionDefinition();
						TransactionStatus status = txManager.getTransaction(def);					
						try {
							String requisitoClientid = oldRequisitoInst.getClientid();
							RequisitoInst requisitoInst = requisitoInstDao.findByClientId(requisitoClientid); //load jpa refreshed requisitoInst
							
							//se il requisito non gli è stato assegnato in verifica -> skip to next one
							//note Verificatore
							oldNote = requisitoInst.getNoteVerificatore() == null? "" : requisitoInst.getNoteVerificatore();
							if (!note.equals(oldNote)) {
								noteVerifStoria = note.length() == 0? "---Cancellate note---" : note;
								requisitoInst.setNoteVerificatore(note);
								//storico
								StoricoRisposteRequisiti storicoRisposteRequisiti = autovalutazioneService.buildStoricoRisposteRequisiti(requisitoInst, userBean, "", "", "", "", "", noteVerifStoria, "");
								storicoRisposteRequisitiDao.save(storicoRisposteRequisiti);
								//requisitoInst.setNumStorico(requisitoInst.getNumStorico() + 1);	
								requisitoInstDao.save(requisitoInst);		
							}
						}
						catch (Exception e) {			
							e.printStackTrace();
							txManager.rollback(status);					
							throw e;
						}
						
						//commit transaction programmatically
						txManager.commit(status);				
					}
					catch (Exception te) {
						te.printStackTrace();
	//TODO - occorre da qualche parte mostrare quali errori si sono verificati?					
					}
					
					neamJobBean.incrementCurrent();
				}
				
			}
		}
		
		
		/*

	private void pasteAutovalutazione(List<RequisitoInst> copiedRequisitoInstL, List<RequisitoInst> destRequisitoInstL) throws Exception {
		//start transaction programmatically
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = txManager.getTransaction(def);
		
		try {
			//per ogni requisito copiato cerco se esiste un requisito dello stesso tipo su cui fare incolla
			for (RequisitoInst copiedRequisitoInst:copiedRequisitoInstL) {
				RequisitoInst requisitoInst = findRequisitoInstInListByType(copiedRequisitoInst, destRequisitoInstL);
				
				if (requisitoInst != null) { //paste di copiedRequisito in requisitoInst
					String requisitoClientid = requisitoInst.getClientid();
					
					//nel caso di VALUTATORE_INTRNO, se il requisito non gli è stato assegnato -> skip to next one
					if (userBean.isVALUTATORE_INTERNO() && !requisitoInst.getUtenteModel().getClientid().equals(userBean.getUtenteModel().getClientid()))
						continue;
					
					requisitoInst = requisitoInstDao.findByClientId(requisitoClientid); //load jpa refreshed requisitoInst

					//autovalutazione
					String valutazione = copiedRequisitoInst.getValutazione() == null? "" : copiedRequisitoInst.getValutazione(); 
					String oldValutazione = requisitoInst.getValutazione() == null? "" : requisitoInst.getValutazione();
					String valutazioneStoria = "";
					if (!valutazione.equals(oldValutazione)) {
						if (valutazione.length() == 0) {
							valutazioneStoria = "---Rimossa valutazione---";	
						}
						else {
							if (requisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Si/No"))
								valutazioneStoria = valutazione.equals("1")? "Si" : "No";
							else if (requisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Soglia"))
								valutazioneStoria = valutazione + "%";
							else
								valutazioneStoria = valutazione;
						}
					}
					requisitoInst.setValutazione(valutazione);
					
					//note
					String note = copiedRequisitoInst.getNote() == null? "" : copiedRequisitoInst.getNote(); 
					String oldNote = requisitoInst.getNote() == null? "" : requisitoInst.getNote();
					String noteStoria = "";
					if (!note.equals(oldNote)) {
						noteStoria = note.length() == 0? "---Cancellate note---" : note;
					}
					requisitoInst.setNote(note);
					
					//evidenze
					String evidenze = copiedRequisitoInst.getEvidenze() == null? "" : copiedRequisitoInst.getEvidenze(); 
					String oldEvidenze = requisitoInst.getEvidenze() == null? "" : requisitoInst.getEvidenze();
					String evidenzeStoria = "";
					if (!evidenze.equals(oldEvidenze)) {
						evidenzeStoria = evidenze.length() == 0? "---Cancellate evidenze---" : evidenze;
					}
					requisitoInst.setEvidenze(evidenze);
					
					//storico
					if (valutazioneStoria.length() > 0 || noteStoria.length() > 0 || evidenzeStoria.length() > 0) {
						StoricoRisposteRequisiti storicoRisposteRequisiti = autovalutazioneService.buildStoricoRisposteRequisiti(requisitoInst, userBean, valutazioneStoria, noteStoria, evidenzeStoria, "");
						storicoRisposteRequisitiDao.save(storicoRisposteRequisiti);
						requisitoInst.setNumStorico(requisitoInst.getNumStorico() + 1);	
					}				
									
					requisitoInstDao.save(requisitoInst);		
					
					//file evidenze (XW)
					duplicateEvidenzeDocuments(copiedRequisitoInst.getClientid(), requisitoClientid);
					//refreshLinks
					autovalutazioneService.refreshLinks(requisitoClientid, _xwService);
				}
				
			}			
		}
		catch (Exception e) {
			e.printStackTrace();
			txManager.rollback(status);
			throw e;
		}
		
		//commit transaction programmatically
		txManager.commit(status);*/
	}	
	
	private void duplicateEvidenzeDocuments(String copiedRequisitoClientid, String requisitoClientid, String evidenzeFolderIdIUnit) {
		try {
			XMLDocumento titleDocument = FolderDao.findRequisitoEvidenzeDocuments(_xwService, copiedRequisitoClientid);
			
			if (titleDocument == null) //nessun documento contenuto nel fascicolo
				return;
			
			List<Element> itemsL = titleDocument.selectNodes("//Item");
			for (Element item:itemsL) { //per ogni file da duplicare
		        String value = item.attributeValue("value");
		        value = value.substring(value.indexOf("|epn|") + 5); //elimino la parte preliminare del titolo (comprende sort e epn)
		        Vector<String> titleV = Text.split(value, "|");       
		        String oggettoAllegato = titleV.get(5).trim();
				String fileName = titleV.get(27).trim();
				String fileId = titleV.get(26).trim();
				byte[] fileContent = _xwService.getAttachment(fileId);
				XMLDocumento documento = DocumentoDao.creaDocumento(_xwService, oggettoAllegato, userBean.getUtenteModel().getLoginDbOrCas(), userBean.getRuolo(), userBean.isImitatingOperatore(), requisitoClientid, null);
				String idIUnit = documento.getAttributeValue("/Response/Document/@idIUnit", "");
				String documentoConFile = _xwService.checkInContentFile(Integer.parseInt(idIUnit), fileName, fileContent);
				documento = DocumentoDao.aggiungiDocAFolder(_xwService, evidenzeFolderIdIUnit, idIUnit);				
			}

		}
		catch (Exception e) {
			e.printStackTrace();
//TODO - gli errori xw vengono completamente ignorati. Corretto? NO ROLLBACK NO PARTY			
		}
	}
	
}
