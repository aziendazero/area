package it.tredi.auac;

import it.highwaytech.apps.generic.XMLDocumento;
import it.highwaytech.util.Text;
import it.tredi.auac.bean.JobBean;
import it.tredi.auac.bean.ShowFolderPageBean;
import it.tredi.auac.bean.UdoUoInstForList;
import it.tredi.auac.bean.UserBean;
import it.tredi.auac.business.UpdaterRequisitiInstValutazioneVerificaInfo;
import it.tredi.auac.business.UpdaterTypeEnum;
import it.tredi.auac.dao.DocumentoDao;
import it.tredi.auac.dao.FolderDao;
import it.tredi.auac.dao.RequisitoInstDao;
import it.tredi.auac.dao.StoricoRisposteRequisitiDao;
import it.tredi.auac.orm.entity.RequisitoInst;
import it.tredi.auac.orm.entity.StoricoRisposteRequisiti;
import it.tredi.auac.service.AutovalutazioneService;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.dom4j.Element;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class AutovalutazioneMassivaRunnable implements Runnable {

	private JobBean amJobBean;
	private ShowFolderPageBean showFolderPageBean;
	private RequisitoInstDao requisitoInstDao;
	private StoricoRisposteRequisitiDao storicoRisposteRequisitiDao;
	private UserBean userBean;
	private PlatformTransactionManager txManager;
	private ExtraWayService _xwService;
	private AutovalutazioneService autovalutazioneService;

	public AutovalutazioneMassivaRunnable(JobBean amJobBean, ShowFolderPageBean showFolderPageBean, RequisitoInstDao requisitoInstDao, StoricoRisposteRequisitiDao storicoRisposteRequisitiDao,
			UserBean userBean, PlatformTransactionManager txManager, ExtraWayService _xwService, AutovalutazioneService autovalutazioneService) {
		this.amJobBean = amJobBean;
		this.showFolderPageBean = showFolderPageBean;
		this.requisitoInstDao = requisitoInstDao;
		this.storicoRisposteRequisitiDao = storicoRisposteRequisitiDao;
		this.userBean = userBean;
		this.txManager = txManager;
		this._xwService = _xwService;
		this.autovalutazioneService = autovalutazioneService;
	}

	@Override
	public void run() {
		List<RequisitoInst> copiedRequisitoInstL = null;
		UdoUoInstForList copiedUdoUoInst = showFolderPageBean.getCopiedUdoUoInst();
		
		//caricamento requisiti di UO/UDO copiata
		if (copiedUdoUoInst.isUdo()) { //UDO
			copiedRequisitoInstL = requisitoInstDao.findRequisitiByUdoInst(copiedUdoUoInst.getUdoInst(), false, false, false);
		} 
		else if (copiedUdoUoInst.isUo()) { //UO
			copiedRequisitoInstL = requisitoInstDao.findRequisitiByUoInst(copiedUdoUoInst.getUoInst(), false, false, false);
		} 
		else if (copiedUdoUoInst.isStruttura()) { //STRUTTURA
			copiedRequisitoInstL = requisitoInstDao.findRequisitiByStrutturaInst(copiedUdoUoInst.getStrutturaInst(), false, false, false);
		} 
		else if (copiedUdoUoInst.isEdificio()) { //EDIFICIO
			copiedRequisitoInstL = requisitoInstDao.findRequisitiByEdificioInst(copiedUdoUoInst.getEdificioInst(), false, false, false);
		} 
		else { //REQUISITI GENERALI
			copiedRequisitoInstL = requisitoInstDao.findRequisitiGeneraliAziendali(copiedUdoUoInst.getDomandaInst(), false, false, false);
		}

		for (UdoUoInstForList udoUoInstForList:showFolderPageBean.getFascicolo().getUdoUoInstForL()) { //per ogni udo/uo nella pagina
			Map<String, Entry<String, Boolean>> checkM = showFolderPageBean.getCheckM();

			//se la udo/uo è checked -> fare incolla (la udo/uo viene scartata se è la stessa su cui è stato fatto incolla)
			if (udoUoInstForList.getClientId().equals(copiedUdoUoInst.getClientId())) {
				amJobBean.incrementCurrent();		
				continue;
			}
			if (checkM.containsKey(udoUoInstForList.getClientId())) {
				try {
					List<RequisitoInst> destRequisitoInstL = null;	
					
					//caricamento requisiti di UO/UDO su cui fare incolla					
					if (copiedUdoUoInst.isUdo()) { //UDO
						destRequisitoInstL = requisitoInstDao.findRequisitiByUdoInst(udoUoInstForList.getUdoInst(), false, false, false);
					} 
					else if (copiedUdoUoInst.isUo()) { //UO
						destRequisitoInstL = requisitoInstDao.findRequisitiByUoInst(udoUoInstForList.getUoInst(), false, false, false);
					} 
					else if (copiedUdoUoInst.isStruttura()) { //STRUTTURA
						destRequisitoInstL = requisitoInstDao.findRequisitiByStrutturaInst(udoUoInstForList.getStrutturaInst(), false, false, false);
					} 
					else if (copiedUdoUoInst.isEdificio()) { //EDIFICIO
						destRequisitoInstL = requisitoInstDao.findRequisitiByEdificioInst(udoUoInstForList.getEdificioInst(), false, false, false);
					} 
					else { //REQUISITI GENERALI
						destRequisitoInstL = requisitoInstDao.findRequisitiGeneraliAziendali(udoUoInstForList.getDomandaInst(), false, false, false);
					}					
				
					pasteAutovalutazione(copiedRequisitoInstL, destRequisitoInstL);
				}
				catch (Exception e) {			
					e.printStackTrace();
//TODO - occorre da qualche parte mostrare quali errori si sono verificati?					
				}
				amJobBean.incrementCurrent();				
			}
		}
	}

	private void pasteAutovalutazione(List<RequisitoInst> copiedRequisitoInstL, List<RequisitoInst> destRequisitoInstL) throws Exception {
		//start transaction programmatically
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = txManager.getTransaction(def);
		
		try {
			//per ogni requisito copiato cerco se esiste un requisito dello stesso tipo su cui fare incolla
			String valutazione;
			String oldValutazione;
			String valutazioneStoria;
			if(copiedRequisitoInstL.size() > 0) {
				//Ricavo il clientid della domanda dal RootClientidDomanda del primo requisito copiato in quanto i requisiti vengono tutti dalla stessa domanda 
				UpdaterRequisitiInstValutazioneVerificaInfo updaterRequisitiInstValutazioneVerificaInfo = new UpdaterRequisitiInstValutazioneVerificaInfo(UpdaterTypeEnum.VALUTAZIONE, copiedRequisitoInstL.get(0).getRootClientidDomanda());
				for (RequisitoInst copiedRequisitoInst:copiedRequisitoInstL) {
					//Copio la valutazione solo se l'origine è manuale
					if(copiedRequisitoInst.getTipoValutazione() != TipoValutazioneVerificaEnum.MANUALE)
						continue;
					RequisitoInst requisitoInst = findRequisitoInstInListByTypeAndTipoValutazione(copiedRequisitoInst, destRequisitoInstL, TipoValutazioneVerificaEnum.MANUALE);
					
					if (requisitoInst != null) { //paste di copiedRequisito in requisitoInst
						String requisitoClientid = requisitoInst.getClientid();
						
						//nel caso di VALUTATORE_INTRNO, se il requisito non gli è stato assegnato -> skip to next one
						if (userBean.isVALUTATORE_INTERNO() && !requisitoInst.getUtenteModel().getClientid().equals(userBean.getUtenteModel().getClientid()))
							continue;
						
						requisitoInst = requisitoInstDao.findByClientId(requisitoClientid); //load jpa refreshed requisitoInst
	
						//autovalutazione
						valutazione = copiedRequisitoInst.getValutazione() == null? "" : copiedRequisitoInst.getValutazione(); 
						oldValutazione = requisitoInst.getValutazione() == null? "" : requisitoInst.getValutazione();
						valutazioneStoria = "";
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
							updaterRequisitiInstValutazioneVerificaInfo.addRequisito(requisitoInst);
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
				requisitoInstDao.impostaValutazioneOrValutazioneVerificatoreRequisitiGenSrByRequisitoUoStruMod(updaterRequisitiInstValutazioneVerificaInfo);
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
	
	private RequisitoInst findRequisitoInstInListByTypeAndTipoValutazione(RequisitoInst requisitoInstToFinf, List<RequisitoInst> requisitoInstL, TipoValutazioneVerificaEnum tipoValutazione) {
		for (RequisitoInst requisitoInst:requisitoInstL) {
			if (requisitoInstToFinf.getClientid().equals(requisitoInst.getClientid())) //ho trovato me stesso
				return null;
			if (requisitoInstToFinf.getRequisitoTempl().getClientid().equals(requisitoInst.getRequisitoTempl().getClientid()) && requisitoInst.getTipoValutazione() == tipoValutazione) //ho trovato un requisito con lo stesso requisitoTempl
				return requisitoInst;
		}
		return null;
	}
	
	private void duplicateEvidenzeDocuments(String copiedRequisitoClientid, String requisitoClientid) {
		try {
			XMLDocumento titleDocument = FolderDao.findRequisitoEvidenzeDocuments(_xwService, copiedRequisitoClientid);
			
			if (titleDocument == null) //nessun documento contenuto nel fascicolo
				return;
			
			//estrazione numero del fascicolo delle evidenze
			XMLDocumento evidenzeFolderDocument = FolderDao.findEvidenzeSubFolder(_xwService, showFolderPageBean.getFascicolo().getNumero());
			String evidenzeFolderIdIUnit = evidenzeFolderDocument.getAttributeValue("/Response/Document/@idIUnit", "");			
			
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
