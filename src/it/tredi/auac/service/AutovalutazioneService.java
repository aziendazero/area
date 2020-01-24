package it.tredi.auac.service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.webflow.context.ExternalContext;

import it.highwaytech.apps.generic.XMLDocumento;
import it.highwaytech.util.Text;
import it.tredi.auac.AssegnazioneMassivaNoteEvidenzeRunnable;
import it.tredi.auac.ExtraWayService;
import it.tredi.auac.TipoValutazioneVerificaEnum;
import it.tredi.auac.TypeViewEnum;
import it.tredi.auac.bean.AutovalutazionePageBean;
import it.tredi.auac.bean.EditEvidenzePageBean;
import it.tredi.auac.bean.EditNotePageBean;
import it.tredi.auac.bean.HomePageBean;
import it.tredi.auac.bean.JobBean;
import it.tredi.auac.bean.PersonaSelectOption;
import it.tredi.auac.bean.ShowFolderPageBean;
import it.tredi.auac.bean.ShowStoriaRequisitoPageBean;
import it.tredi.auac.bean.ShowUdoInstPageBean;
import it.tredi.auac.bean.UdoUoInstForList;
import it.tredi.auac.bean.UserBean;
import it.tredi.auac.business.UpdaterRequisitiInstValutazioneVerificaInfo;
import it.tredi.auac.business.UpdaterTypeEnum;
import it.tredi.auac.dao.DomandaInstDao;
import it.tredi.auac.dao.FolderDao;
import it.tredi.auac.dao.RequisitoInstDao;
import it.tredi.auac.dao.StoricoRisposteRequisitiDao;
import it.tredi.auac.dao.TipoUdoUtenteTemplDao;
import it.tredi.auac.dao.TitolareModelDao;
import it.tredi.auac.dao.UdoInstDao;
import it.tredi.auac.dao.UoInstDao;
import it.tredi.auac.dao.UoModelDao;
import it.tredi.auac.dao.UtenteDao;
import it.tredi.auac.orm.entity.AnagraficaUtenteModel;
import it.tredi.auac.orm.entity.DomandaInst;
import it.tredi.auac.orm.entity.RequisitoInst;
import it.tredi.auac.orm.entity.StoricoRisposteRequisiti;
import it.tredi.auac.orm.entity.UdoInst;
import it.tredi.auac.orm.entity.UoModel;
import it.tredi.auac.orm.entity.UtenteModel;

@Service
@Transactional
public class AutovalutazioneService {
	private static final Logger log = Logger.getLogger(MainService.class);

	@Autowired
	private UtilService utilService;

	@Autowired
	private TitolareModelDao titolareModelDao;	
	
	@Autowired
	private UoModelDao uoModelDao;

	@Autowired
	private UdoInstDao udoInstDao;
	
	@Autowired
	private UoInstDao uoInstDao;

	@Autowired
	private UtenteDao utenteDao;
	
	@Autowired
	private TipoUdoUtenteTemplDao tipoUdoUtenteTemplDao;

	@Autowired
	private RequisitoInstDao requisitoInstDao;	
	
	@Autowired
	private StoricoRisposteRequisitiDao storicoRisposteRequisitiDao;

	@Autowired
	private DomandaInstDao domandaInstDao;		
	
	@Autowired
	private AclService aclService;	

	@Resource
	private PlatformTransactionManager txManager;		
	
	public AutovalutazioneService() {
		//do nothing
	}

	//Crea AutovalutazionePageBean in modo che si vedano tutti i requisiti della domanda filtrati dai dati presenti in showFolderPageBean.getRequisitoSearchBean()
	//public AutovalutazionePageBean createAutovalutazionePageBean(UserBean userBean, RequisitoSearchBean requisitoSearchBean, String domandaClientId) throws Exception {
	public AutovalutazionePageBean createAutovalutazionePageBean(UserBean userBean, ShowFolderPageBean showFolderPageBean) throws Exception {
    	long startTime = System.currentTimeMillis();
		AutovalutazionePageBean autovalutazionePageBean = new AutovalutazionePageBean();
		autovalutazionePageBean.setUdoInstSearchOrderBean(showFolderPageBean.getUdoInstSearchOrderBean());
		//autovalutazionePageBean.setRequisitoSearchBean(requisitoSearchBean);
		autovalutazionePageBean.setRequisitoSearchBean(showFolderPageBean.getRequisitoSearchBean());
		autovalutazionePageBean.setSearchAllRequisiti(true);
		autovalutazionePageBean.setDomandaClientId(showFolderPageBean.getFascicolo().getDomandaClienId());
		//impostare che autovalutazionePageBean sia tipo filtraTuttiRequisiti
		autovalutazionePageBean.setCheckM(new HashMap<String, String>());
		autovalutazionePageBean.setDomandaStato(showFolderPageBean.getFascicolo().getFolderStatus());
		
		autovalutazionePageBean.setVerificatoreTeamLeader(showFolderPageBean.getVerificatoreTeamLeader());
		autovalutazionePageBean.setTeamVerificatori(showFolderPageBean.getTeamVerificatori());
		
		if(log.isInfoEnabled()) {
			//log.info("Autovalutazione carico tutti i requisiti della domanda clientid: " + domandaClientId);
			log.info("Autovalutazione carico tutti i requisiti della domanda clientid: " + showFolderPageBean.getFascicolo().getDomandaClienId());
		}
		
		//Carico e setto la lista dei requisiti
		//List<RequisitoInst> requisitiInstL = requisitoInstDao.findRequisitiByDomandaInst(domandaClientId);
		List<RequisitoInst> requisitiInstL = requisitoInstDao.findAllRequisitiByDomandaInst(showFolderPageBean.getFascicolo().getDomandaClienId());
		log.info("createAutovalutazionePageBean caricamento requisiti : " + (System.currentTimeMillis() - startTime) + " ms");
		/*for (RequisitoInst requisitiInst:requisitiInstL) {
			requisitiInst.getStoricoRisposteRequisitis().size();
			//log.info("requisitiInst.getStoricoRisposteRequisitis().size(): " + requisitiInst.getStoricoRisposteRequisitis().size());
		}*/
		autovalutazionePageBean.setRequisitoInstL(requisitiInstL);
		startTime = System.currentTimeMillis();
		autovalutazionePageBean.filterRequisitiL();
		log.info("createAutovalutazionePageBean applicazione filtro requisiti : " + (System.currentTimeMillis() - startTime) + " ms");
		
		autovalutazionePageBean.setUoL(showFolderPageBean.getUoL());		
		//estrazione della lista delle persone per le assegnazioni
		autovalutazionePageBean.setUoSelected("");
		updatePersonaL(autovalutazionePageBean);					

		
		return autovalutazionePageBean;
	}

	//Crea AutovalutazionePageBean in modo che si vedano tutti i requisiti della domanda di cui viene passato il ClientId assegnati all'utente
	public AutovalutazionePageBean createAutovalutazionePageBean(UserBean userBean, String domandaClientId) throws Exception {
		AutovalutazionePageBean autovalutazionePageBean = new AutovalutazionePageBean();
		autovalutazionePageBean.setCheckM(new HashMap<String, String>());
		autovalutazionePageBean.setDomandaClientId(domandaClientId);

		//caricamento domanda per set di stato
		DomandaInst domandaInst = domandaInstDao.getDomandaInstByClientId(domandaClientId);
		autovalutazionePageBean.setDomandaStato(domandaInst.getStato());
		
		if(log.isInfoEnabled()) {
			log.info("Autovalutazione carico i requisiti del utente " + userBean.getUtenteModel().getClientid() + " per la domanda clientid: " + domandaClientId);
		}
		
		//Carico e setto la lista dei requisiti
		List<RequisitoInst> requisitiInstL = requisitoInstDao.findRequisitiByDomandaInstUtenteModel(domandaClientId, userBean.getUtenteModel()); 
		/*for (RequisitoInst requisitiInst:requisitiInstL) {
			requisitiInst.getStoricoRisposteRequisitis().size();
			//log.info("requisitiInst.getStoricoRisposteRequisitis().size(): " + requisitiInst.getStoricoRisposteRequisitis().size());
		}*/
		autovalutazionePageBean.setRequisitoInstL(requisitiInstL);
		
		return autovalutazionePageBean;
	}
	
	//Crea AutovalutazionePageBean in modo che si vedano tutti i requisiti della UdoInst o UoInst passata in udoUoInst 
	public AutovalutazionePageBean createAutovalutazionePageBean(UserBean userBean, UdoUoInstForList udoUoInst, ShowFolderPageBean showFolderPageBean) throws Exception {
		AutovalutazionePageBean autovalutazionePageBean = new AutovalutazionePageBean();
		autovalutazionePageBean.setUdoInstSearchOrderBean(showFolderPageBean.getUdoInstSearchOrderBean());
		autovalutazionePageBean.setCheckM(new HashMap<String, String>());
		autovalutazionePageBean.setDomandaStato(showFolderPageBean.getFascicolo().getFolderStatus());
		autovalutazionePageBean.setDomandaClientId(showFolderPageBean.getFascicolo().getDomandaClienId());
		
		autovalutazionePageBean.setVerificatoreTeamLeader(showFolderPageBean.getVerificatoreTeamLeader());
		autovalutazionePageBean.setTeamVerificatori(showFolderPageBean.getTeamVerificatori());
		
		if(log.isInfoEnabled()) {
			log.info("Autovalutazione carico l'entity " + (udoUoInst.isUdo()?"UdoInst ":"UoInst ") + "con clientid: " + udoUoInst.getClientId());
		}
		
		if(udoUoInst != null) {
			//ricarico da db
			if(udoUoInst.isUdo()) {
				//UdoInst udoInst = udoInstDao.findByClientIdForAutovalutazione(udoUoInst.getClientId());
				//UdoInst udoInst = udoInstDao.findByClientId(udoUoInst.getClientId());
				/*for (RequisitoInst reqInst:udoInst.getRequisitoInsts()) {
					reqInst.getStoricoRisposteRequisitis().size();
				}*/
				//autovalutazionePageBean.setRequisitoInstL(udoInst.getRequisitoInsts());
				autovalutazionePageBean.setRequisitoInstL(requisitoInstDao.findRequisitiByUdoInst(udoUoInst.getUdoInst(), false, false, false));
				//udoUoInst = new UdoUoInstForList(userBean, udoInst);
			} else if (udoUoInst.isUo()) {
				//UoInst uoInst = uoInstDao.findByClientId(udoUoInst.getClientId());
				/*for (RequisitoInst reqInst:uoInst.getRequisitoInsts()) {
					reqInst.getStoricoRisposteRequisitis().size();
				}*/
				//autovalutazionePageBean.setRequisitoInstL(uoInst.getRequisitoInsts());
				autovalutazionePageBean.setRequisitoInstL(requisitoInstDao.findRequisitiByUoInst(udoUoInst.getUoInst(), false, false, false));
				//udoUoInst = new UdoUoInstForList(userBean, uoInst);
			} else if (udoUoInst.isStruttura()) {
				autovalutazionePageBean.setRequisitoInstL(requisitoInstDao.findRequisitiByStrutturaInst(udoUoInst.getStrutturaInst(), false, false, false));
			} else if (udoUoInst.isEdificio()) {
				autovalutazionePageBean.setRequisitoInstL(requisitoInstDao.findRequisitiByEdificioInst(udoUoInst.getEdificioInst(), false, false, false));
			} else {
				//UoInst uoInst = uoInstDao.findByClientId(udoUoInst.getClientId());
				/*for (RequisitoInst reqInst:uoInst.getRequisitoInsts()) {
					reqInst.getStoricoRisposteRequisitis().size();
				}*/
				//autovalutazionePageBean.setRequisitoInstL(uoInst.getRequisitoInsts());
				autovalutazionePageBean.setRequisitoInstL(requisitoInstDao.findRequisitiGeneraliAziendali(udoUoInst.getDomandaInst(), false, false, false));
				//udoUoInst = new UdoUoInstForList(userBean, uoInst);
			}
			
			autovalutazionePageBean.setUdoUoInst(udoUoInst);			
			
			//estrazione della lista delle UO
			/*TitolareModel jpaFreshTitolareModel = titolareModelDao.findByClientId(udoUoInst.getTitolareModel().getClientid());
			List<UoModel> uoModelL = jpaFreshTitolareModel.getUoModels();
			Collections.sort(uoModelL, new Comparator<UoModel>() {
		        @Override
		        public int compare(UoModel uo1, UoModel uo2) {
		        	return uo1.getDenominazione().compareTo(uo2.getDenominazione());
		        }
		    });		
			autovalutazionePageBean.setUoL(uoModelL);*/
			autovalutazionePageBean.setUoL(showFolderPageBean.getUoL());		
		}
		
		//estrazione della lista delle persone per le assegnazioni
		autovalutazionePageBean.setUoSelected("");
		updatePersonaL(autovalutazionePageBean);
		
		return autovalutazionePageBean;
	}
	
	public void updatePersonaL(AutovalutazionePageBean autovalutazionePageBean) throws Exception {
		List<PersonaSelectOption> personaL = new ArrayList<PersonaSelectOption>();

		List<UoModel> uoModelL = null;
		if (autovalutazionePageBean.getUoSelected().length() == 0)
			uoModelL = autovalutazionePageBean.getUoL();
		else {
			uoModelL = new ArrayList<UoModel>();
			uoModelL.add(uoModelDao.findByClientId(autovalutazionePageBean.getUoSelected()));
		}
		if (uoModelL != null) {
			for (UoModel uoModel:uoModelL) {
				List<UtenteModel> utenteModelL = uoModel.getUtenteModels();
				for (UtenteModel utenteModel:utenteModelL) {
					if("N".equals(utenteModel.getDisabilitato())) {
						PersonaSelectOption pSO =  new PersonaSelectOption();
						pSO.setUtenteModelClientid(utenteModel.getClientid());
						pSO.setCognomeNome(utenteModel.getAnagraficaUtenteModel().getCognome() + " " + utenteModel.getAnagraficaUtenteModel().getNome());
						personaL.add(pSO);
					}
				}			
			}
			Collections.sort(personaL, new Comparator<PersonaSelectOption>() {
		        @Override
		        public int compare(PersonaSelectOption persona1, PersonaSelectOption persona2) {
		            return  persona1.getCognomeNome().compareTo(persona2.getCognomeNome());
		        }
		    });
			autovalutazionePageBean.setPersonaL(personaL);			
		}
	}

	/*public EditNotePageBean createEditNotePageBean(String requisitoInstClientId, boolean readonly) {
		return createEditNotePageBean(requisitoInstClientId, readonly, false);
	}*/
	
	public EditNotePageBean createEditNotePageBean(String requisitoInstClientId, boolean readonly, boolean isNoteVerifica) {
		EditNotePageBean editNotePageBean = new EditNotePageBean();
		editNotePageBean.setRequisitoInst(requisitoInstDao.findByClientId(requisitoInstClientId));
		editNotePageBean.setNoteVerifica(isNoteVerifica);
		if(isNoteVerifica) {
			editNotePageBean.setNote(editNotePageBean.getRequisitoInst().getNoteVerificatore());
			editNotePageBean.setAutoValutazione(utilService.getAutoValutazioneVerificatoreFormattedText(editNotePageBean.getRequisitoInst(), TypeViewEnum.WEB));
		} else {
			editNotePageBean.setNote(editNotePageBean.getRequisitoInst().getNote());
			editNotePageBean.setAutoValutazione(utilService.getAutoValutazioneFormattedText(editNotePageBean.getRequisitoInst(), TypeViewEnum.WEB));
		}
			
		editNotePageBean.setReadonly(readonly);
		return editNotePageBean;
	}
	
	public void saveNote(UserBean userBean, EditNotePageBean editNotePageBean, AutovalutazionePageBean autovalutazionePageBean) {
		String reqisitoInstClientId = editNotePageBean.getRequisitoInst().getClientid();
		RequisitoInst jpaFreshRequisitoInst = requisitoInstDao.findByClientId(reqisitoInstClientId);
		//jpaFreshRequisitoInst.getStoricoRisposteRequisitis().size();
		String note = editNotePageBean.getNote();
		
		//storico
		String oldNote;
		if(editNotePageBean.isNoteVerifica())
			oldNote = jpaFreshRequisitoInst.getNoteVerificatore() == null? "" : jpaFreshRequisitoInst.getNoteVerificatore();
		else
			oldNote = jpaFreshRequisitoInst.getNote() == null? "" : jpaFreshRequisitoInst.getNote();
		if (!note.equals(oldNote)) {
			//Aggiunto perche' non veniva caricato
			jpaFreshRequisitoInst.getStoricoRisposteRequisitis().size();
			String noteStoria = note.length() == 0? "---Cancellate note---" : note;
			StoricoRisposteRequisiti storicoRisposteRequisiti;
			
			if(editNotePageBean.isNoteVerifica())
				storicoRisposteRequisiti = buildStoricoRisposteRequisiti(jpaFreshRequisitoInst, userBean, "", "", "", "", "", noteStoria, "");
			else
				storicoRisposteRequisiti = buildStoricoRisposteRequisiti(jpaFreshRequisitoInst, userBean, "", noteStoria, "", "");
			
			storicoRisposteRequisitiDao.save(storicoRisposteRequisiti);
			//spostato in buildStoricoRisposteRequisiti
			//jpaFreshRequisitoInst.addStoricoRisposteRequisiti(storicoRisposteRequisiti);			

			if(editNotePageBean.isNoteVerifica())
				jpaFreshRequisitoInst.setNoteVerificatore(note);
			else
				jpaFreshRequisitoInst.setNote(note);
			requisitoInstDao.save(jpaFreshRequisitoInst);

			//Aggiorno il RequisitoInst in autovalutazionePageBean
			for(RequisitoInst reqInst : autovalutazionePageBean.getRequisitoInstL()) {
				if(reqInst.getClientid().equals(reqisitoInstClientId)) {
					if(editNotePageBean.isNoteVerifica())
						reqInst.setNoteVerificatore(note);
					else
						reqInst.setNote(note);
					//reqInst.setStoricoRisposteRequisitis(jpaFreshRequisitoInst.getStoricoRisposteRequisitis());
					reqInst.setNumStorico(jpaFreshRequisitoInst.getNumStorico() + 1);
				}
			}
		}
		
	}
	
	public void saveNoteVerifica(UserBean userBean, EditNotePageBean editNotePageBean, AutovalutazionePageBean autovalutazionePageBean) {
		String reqisitoInstClientId = editNotePageBean.getRequisitoInst().getClientid();
		RequisitoInst jpaFreshRequisitoInst = requisitoInstDao.findByClientId(reqisitoInstClientId);
		//jpaFreshRequisitoInst.getStoricoRisposteRequisitis().size();
		String note = editNotePageBean.getNote();
		
		//storico
		String oldNote = jpaFreshRequisitoInst.getNoteVerificatore() == null? "" : jpaFreshRequisitoInst.getNoteVerificatore();
		if (!note.equals(oldNote)) {
			//Aggiunto perche' non veniva caricato
			jpaFreshRequisitoInst.getStoricoRisposteRequisitis().size();
			String noteStoria = note.length() == 0? "---Cancellate note---" : note;
			StoricoRisposteRequisiti storicoRisposteRequisiti = buildStoricoVerificaRequisiti(jpaFreshRequisitoInst, userBean, "", noteStoria, "");
			storicoRisposteRequisitiDao.save(storicoRisposteRequisiti);
			//spostato in buildStoricoRisposteRequisiti
			//jpaFreshRequisitoInst.addStoricoRisposteRequisiti(storicoRisposteRequisiti);			
		}
		
		jpaFreshRequisitoInst.setNoteVerificatore(note);
		requisitoInstDao.save(jpaFreshRequisitoInst);
		
		if (!note.equals(oldNote)) {
			//Aggiorno il RequisitoInst in autovalutazionePageBean
			for(RequisitoInst reqInst : autovalutazionePageBean.getRequisitoInstL()) {
				if(reqInst.getClientid().equals(reqisitoInstClientId)) {
					reqInst.setNoteVerificatore(note);
					//reqInst.setNumStorico(jpaFreshRequisitoInst.getNumStorico() + 1);
				}
			}
		}
	}

	public EditEvidenzePageBean createEditEvidenzePageBean(String requisitoInstClientId, boolean readonly, String doamndaFolderIDUnit, ExtraWayService _xwService, ExternalContext externalContext, UserBean userBean) throws Exception {
		EditEvidenzePageBean editEvidenzePageBean = new EditEvidenzePageBean();
		editEvidenzePageBean.setRequisitoInst(requisitoInstDao.findByClientId(requisitoInstClientId));
		editEvidenzePageBean.setEvidenze(editEvidenzePageBean.getRequisitoInst().getEvidenze());
		editEvidenzePageBean.setReadonly(readonly);
		
		//caricamento della lista dei file (evidenze)
		editEvidenzePageBean.setDocumentsL(getFascicoloEvidenzeDocumentsL(externalContext, userBean, requisitoInstClientId, _xwService));
		
		if (!readonly) { //solo in modalità editing
			//caricamento del fascicolo della domanda e del sottofascicolo delle evidenze (occorre avere idUnit del fascicolo delle evidenze per fare upload dei file)
			XMLDocumento domandaFolderDocument = _xwService.loadDocument(Integer.parseInt(doamndaFolderIDUnit));
			String domandaFolderNumFasc = domandaFolderDocument.getAttributeValue("//fascicolo/@numero");
			XMLDocumento evidenzeFolderDocument = FolderDao.findEvidenzeSubFolder(_xwService, domandaFolderNumFasc);
			String evidenzeFolderIdIUnit = evidenzeFolderDocument.getAttributeValue("/Response/Document/@idIUnit", "");
			editEvidenzePageBean.setEvidenzeFolderIdIUnit(evidenzeFolderIdIUnit);
		}
				
		return editEvidenzePageBean;
	}
	
	public void saveEvidenze(UserBean userBean, EditEvidenzePageBean editEvidenzePageBean, AutovalutazionePageBean autovalutazionePageBean) {
		String reqisitoInstClientId = editEvidenzePageBean.getRequisitoInst().getClientid();
		RequisitoInst jpaFreshRequisitoInst = requisitoInstDao.findByClientId(reqisitoInstClientId);
		String evidenze = editEvidenzePageBean.getEvidenze();
		
		//storico
		String oldEvidenze = jpaFreshRequisitoInst.getEvidenze() == null? "" : jpaFreshRequisitoInst.getEvidenze(); 
		if (!evidenze.equals(oldEvidenze)) {
			jpaFreshRequisitoInst.getStoricoRisposteRequisitis().size();
			String evidenzeStoria = evidenze.length() == 0? "---Cancellate evidenze---" : evidenze;
			StoricoRisposteRequisiti storicoRisposteRequisiti = buildStoricoRisposteRequisiti(jpaFreshRequisitoInst, userBean, "", "", evidenzeStoria, "");
			storicoRisposteRequisitiDao.save(storicoRisposteRequisiti);
			//spostato in buildStoricoRisposteRequisiti
			//jpaFreshRequisitoInst.addStoricoRisposteRequisiti(storicoRisposteRequisiti);
		}
		
		jpaFreshRequisitoInst.setEvidenze(evidenze);
		requisitoInstDao.save(jpaFreshRequisitoInst);
		
		if (!evidenze.equals(oldEvidenze)) {
			//Aggiorno il RequisitoInst in autovalutazionePageBean
			for(RequisitoInst reqInst : autovalutazionePageBean.getRequisitoInstL()) {
				if(reqInst.getClientid().equals(reqisitoInstClientId)) {
					reqInst.setEvidenze(evidenze);
					//reqInst.setStoricoRisposteRequisitis(jpaFreshRequisitoInst.getStoricoRisposteRequisitis());
					reqInst.setNumStorico(jpaFreshRequisitoInst.getNumStorico() + 1);
				}
			}
		}
	}
	
	public void saveAutovalutazione(UserBean userBean, AutovalutazionePageBean autovalutazionePageBean) throws Exception {
		String valutazione;
		String oldValutazione;
		String valutazioneStoria;
		String assegnatarioStoria;
		String newUtenteModelClientId;
		String oldUtenteModelClientId;
		UpdaterRequisitiInstValutazioneVerificaInfo updaterRequisitiInstValutazioneVerificaInfo = new UpdaterRequisitiInstValutazioneVerificaInfo(UpdaterTypeEnum.VALUTAZIONE, autovalutazionePageBean.getDomandaClientId());
		for (RequisitoInst requisitoInst:autovalutazionePageBean.getFilteredRequisitoInstL()) {
			RequisitoInst jpaFreshRequisitoInst = requisitoInstDao.findByClientId(requisitoInst.getClientid());
			jpaFreshRequisitoInst.getStoricoRisposteRequisitis().size();
			valutazione = requisitoInst.getValutazione() == null? "" : requisitoInst.getValutazione(); 
			
			//valutazione
			oldValutazione = jpaFreshRequisitoInst.getValutazione() == null? "" : jpaFreshRequisitoInst.getValutazione();
			valutazioneStoria = "";
			if (!valutazione.equals(oldValutazione)) {
				if (valutazione.length() == 0) {
					valutazioneStoria = "---Rimossa valutazione---";	
				}
				else {
					/*
					if (jpaFreshRequisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Si/No"))
						valutazioneStoria = valutazione.equals("1")? "Si" : "No";
					else if (jpaFreshRequisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Soglia"))
						valutazioneStoria = valutazione + "%";
					else
						valutazioneStoria = valutazione;
					*/
					valutazioneStoria = utilService.getValutazioneFormattedText(jpaFreshRequisitoInst.getRequisitoTempl().getTipoRisposta(), jpaFreshRequisitoInst.getTipoValutazione(), valutazione, TypeViewEnum.WEB);
				}
				updaterRequisitiInstValutazioneVerificaInfo.addRequisito(jpaFreshRequisitoInst);
			}
			jpaFreshRequisitoInst.setValutazione(valutazione);
			
			//assegnatario
			assegnatarioStoria = "";
			if (requisitoInst.getUtenteModel() != null) {
				UtenteModel jpaFreshUtenteModel = utenteDao.findOne(requisitoInst.getUtenteModel().getClientid());
				
				//check se occorre storicizzare
				newUtenteModelClientId = jpaFreshUtenteModel.getClientid();
				oldUtenteModelClientId =  jpaFreshRequisitoInst.getUtenteModel() == null? "" : jpaFreshRequisitoInst.getUtenteModel().getClientid();
				if (!newUtenteModelClientId.equals(oldUtenteModelClientId)) {
					AnagraficaUtenteModel anagraficaUenteModel = jpaFreshUtenteModel.getAnagraficaUtenteModel(); 
					String operatore = anagraficaUenteModel.getCognome() + " " + anagraficaUenteModel.getNome();
					String uo = "";
					try {
						uo = jpaFreshUtenteModel.getUoModel().getDenominazione() + " - ";
					}
					catch (Exception e) {
						
					}
					assegnatarioStoria = uo + operatore;					
				}
				jpaFreshRequisitoInst.setUtenteModel(jpaFreshUtenteModel);				
			}
			else {
				if (jpaFreshRequisitoInst.getUtenteModel() != null)
					assegnatarioStoria = "---Rimossa assegnazione---";
				jpaFreshRequisitoInst.setUtenteModel(null);
			}
			
			if (valutazioneStoria.length() > 0 || assegnatarioStoria.length() > 0) {
				StoricoRisposteRequisiti storicoRisposteRequisiti = buildStoricoRisposteRequisiti(jpaFreshRequisitoInst, userBean, valutazioneStoria, "", "", assegnatarioStoria);
				storicoRisposteRequisitiDao.save(storicoRisposteRequisiti);
				
				requisitoInst.setNumStorico(jpaFreshRequisitoInst.getNumStorico() + 1);
				
				//spostato in buildStoricoRisposteRequisiti
				//jpaFreshRequisitoInst.addStoricoRisposteRequisiti(storicoRisposteRequisiti);			
			}

			requisitoInstDao.save(jpaFreshRequisitoInst);	
		}
		requisitoInstDao.impostaValutazioneOrValutazioneVerificatoreRequisitiGenSrByRequisitoUoStruMod(updaterRequisitiInstValutazioneVerificaInfo);
	}
	
	//Controlla se sono modificati Verificatore e Verifica e li salva se modificati salvando anche la storia 
	public void saveVerificatoreVerifica(UserBean userBean, AutovalutazionePageBean autovalutazionePageBean) throws Exception {
		String newValutazioneVerifica;
		String oldValutazioneVerifica;
		String valutazioneVerificaStoria;
		String verificatoreStoria;
		String newUtenteModelClientId;
		String oldUtenteModelClientId;
		UpdaterRequisitiInstValutazioneVerificaInfo updaterRequisitiInstValutazioneVerificaInfo = new UpdaterRequisitiInstValutazioneVerificaInfo(UpdaterTypeEnum.VALUTAZIONEVERIFICA, autovalutazionePageBean.getDomandaClientId());
		for (RequisitoInst requisitoInst:autovalutazionePageBean.getFilteredRequisitoInstL()) {
			RequisitoInst jpaFreshRequisitoInst = requisitoInstDao.findByClientId(requisitoInst.getClientid());
			jpaFreshRequisitoInst.getStoricoRisposteRequisitis().size();
			newValutazioneVerifica = requisitoInst.getValutazioneVerificatore() == null? "" : requisitoInst.getValutazioneVerificatore(); 
			
			//valutazioneVerifica
			oldValutazioneVerifica = jpaFreshRequisitoInst.getValutazioneVerificatore() == null? "" : jpaFreshRequisitoInst.getValutazioneVerificatore();
			valutazioneVerificaStoria = "";
			if (!newValutazioneVerifica.equals(oldValutazioneVerifica)) {
				if (newValutazioneVerifica.length() == 0) {
					valutazioneVerificaStoria = "---Rimossa valutazione---";
				}
				else {
					valutazioneVerificaStoria = utilService.getValutazioneVerificatoreFormattedText(jpaFreshRequisitoInst.getRequisitoTempl().getTipoRisposta(), jpaFreshRequisitoInst.getTipoVerifica(), newValutazioneVerifica, TypeViewEnum.WEB);
					/*
					if (jpaFreshRequisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Si/No"))
						valutazioneVerificaStoria = newValutazioneVerifica.equals("1")? "Si" : "No";
					else if (jpaFreshRequisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Soglia"))
						valutazioneVerificaStoria = newValutazioneVerifica + "%";
					else
						valutazioneVerificaStoria = newValutazioneVerifica;
					*/
				}
				jpaFreshRequisitoInst.setValutazioneVerificatore(newValutazioneVerifica);
				updaterRequisitiInstValutazioneVerificaInfo.addRequisito(jpaFreshRequisitoInst);
			}
			
			//verificatore
			verificatoreStoria = "";
			if (requisitoInst.getVerificatore() != null) {
				UtenteModel jpaFreshVerificatore = utenteDao.findOne(requisitoInst.getVerificatore().getClientid());
				
				//check se occorre storicizzare
				newUtenteModelClientId = jpaFreshVerificatore.getClientid();
				oldUtenteModelClientId = jpaFreshRequisitoInst.getVerificatore() == null? "" : jpaFreshRequisitoInst.getVerificatore().getClientid();
				if (!newUtenteModelClientId.equals(oldUtenteModelClientId)) {
					AnagraficaUtenteModel anagraficaUenteModel = jpaFreshVerificatore.getAnagraficaUtenteModel(); 
					String operatore = anagraficaUenteModel.getCognomeNome();
					verificatoreStoria = operatore;					
					jpaFreshRequisitoInst.setVerificatore(jpaFreshVerificatore);				
				}
			}
			else {
				if (jpaFreshRequisitoInst.getVerificatore() != null) {
					verificatoreStoria = "---Rimosso verificatore---";
					jpaFreshRequisitoInst.setVerificatore(null);
				}
			}
			
			if (valutazioneVerificaStoria.length() > 0 || verificatoreStoria.length() > 0) {
				StoricoRisposteRequisiti storicoVerificaRequisiti = buildStoricoVerificaRequisiti(jpaFreshRequisitoInst, userBean, valutazioneVerificaStoria, "", verificatoreStoria);
				storicoRisposteRequisitiDao.save(storicoVerificaRequisiti);
				
				//requisitoInst.setNumStorico(jpaFreshRequisitoInst.getNumStorico() + 1);
				
				//spostato in buildStoricoRisposteRequisiti
				//jpaFreshRequisitoInst.addStoricoRisposteRequisiti(storicoRisposteRequisiti);			
				requisitoInstDao.save(jpaFreshRequisitoInst);	
			}

		}
		requisitoInstDao.impostaValutazioneOrValutazioneVerificatoreRequisitiGenSrByRequisitoUoStruMod(updaterRequisitiInstValutazioneVerificaInfo);
	}

	//Controlla se e' modificato il Verificatore e se si lo salva salvando anche la storia
	public void saveVerificatore(UserBean userBean, AutovalutazionePageBean autovalutazionePageBean) {
		for (RequisitoInst requisitoInst:autovalutazionePageBean.getFilteredRequisitoInstL()) {
			RequisitoInst jpaFreshRequisitoInst = requisitoInstDao.findByClientId(requisitoInst.getClientid());
			jpaFreshRequisitoInst.getStoricoRisposteRequisitis().size();
			
			//verificatore
			String verificatoreStoria = "";
			if (requisitoInst.getVerificatore() != null) {
				
				//check se occorre storicizzare
				String newUtenteModelClientId = requisitoInst.getVerificatore().getClientid();
				String oldUtenteModelClientId =  jpaFreshRequisitoInst.getVerificatore() == null? "" : jpaFreshRequisitoInst.getVerificatore().getClientid();
				if (!newUtenteModelClientId.equals(oldUtenteModelClientId)) {
					//il verificatore è stato modificato
					UtenteModel jpaFreshVerificatore = utenteDao.findOne(newUtenteModelClientId);
					AnagraficaUtenteModel anagraficaUenteModel = jpaFreshVerificatore.getAnagraficaUtenteModel();
					String operatore = anagraficaUenteModel.getCognomeNome();
					verificatoreStoria = operatore;
					jpaFreshRequisitoInst.setVerificatore(jpaFreshVerificatore);
				}
				
			}
			else {
				if (jpaFreshRequisitoInst.getVerificatore() != null)
					verificatoreStoria = "---Rimosso verificatore---";
				jpaFreshRequisitoInst.setVerificatore(null);
			}
			
			if (verificatoreStoria.length() > 0) {
				StoricoRisposteRequisiti storicoVerificaRequisiti = buildStoricoVerificaRequisiti(jpaFreshRequisitoInst, userBean, "", "", verificatoreStoria);
				storicoRisposteRequisitiDao.save(storicoVerificaRequisiti);
				
				//requisitoInst.setNumStorico(jpaFreshRequisitoInst.getNumStorico() + 1);
				
				//spostato in buildStoricoRisposteRequisiti
				//jpaFreshRequisitoInst.addStoricoRisposteRequisiti(storicoRisposteRequisiti);			

				requisitoInstDao.save(jpaFreshRequisitoInst);	
			}

		}
	}

	//Controlla se e' modificata la Valutazione del Verificatore e se si la salva salvando anche la storia
	public void saveVerifica(UserBean userBean, AutovalutazionePageBean autovalutazionePageBean) throws Exception {
		UpdaterRequisitiInstValutazioneVerificaInfo updaterRequisitiInstValutazioneVerificaInfo = new UpdaterRequisitiInstValutazioneVerificaInfo(UpdaterTypeEnum.VALUTAZIONEVERIFICA, autovalutazionePageBean.getDomandaClientId());
		for (RequisitoInst requisitoInst:autovalutazionePageBean.getFilteredRequisitoInstL()) {
			RequisitoInst jpaFreshRequisitoInst = requisitoInstDao.findByClientId(requisitoInst.getClientid());
			jpaFreshRequisitoInst.getStoricoRisposteRequisitis().size();
			
			String newValutazioneVerifica = requisitoInst.getValutazioneVerificatore() == null? "" : requisitoInst.getValutazioneVerificatore(); 
			
			//valutazioneVerifica
			String oldValutazioneVerifica = jpaFreshRequisitoInst.getValutazioneVerificatore() == null? "" : jpaFreshRequisitoInst.getValutazioneVerificatore();
			String valutazioneStoria = "";
			if (!newValutazioneVerifica.equals(oldValutazioneVerifica)) {
				if (newValutazioneVerifica.length() == 0) {
					valutazioneStoria = "---Rimossa valutazione---";
				}
				else {
					valutazioneStoria = utilService.getValutazioneVerificatoreFormattedText(jpaFreshRequisitoInst.getRequisitoTempl().getTipoRisposta(), jpaFreshRequisitoInst.getTipoVerifica(), newValutazioneVerifica, TypeViewEnum.WEB);
					/*
					if (jpaFreshRequisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Si/No")) {
						//valutazioneStoria = newValutazioneVerifica.equals("1")? "Si" : "No";
						//String message = this.messageSource.getMessage("docTypeEnum.planimetrie", null, LocaleContextHolder.getLocale());
						valutazioneStoria = messageSource.getMessage(ValutazioneVerificatoreTipoSiNoEnum.getEnumByKey(newValutazioneVerifica).getValue(), null, LocaleContextHolder.getLocale());
					}
					else if (jpaFreshRequisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Soglia")) {
						//valutazioneStoria = newValutazioneVerifica + "%";
						valutazioneStoria = messageSource.getMessage(ValutazioneVerificatoreTipoSogliaEnum.getEnumByKey(newValutazioneVerifica).getValue(), null, LocaleContextHolder.getLocale());
					}
					else
						valutazioneStoria = newValutazioneVerifica;
					*/
				}

				jpaFreshRequisitoInst.setValutazioneVerificatore(newValutazioneVerifica);
				updaterRequisitiInstValutazioneVerificaInfo.addRequisito(jpaFreshRequisitoInst);
				requisitoInstDao.save(jpaFreshRequisitoInst);	

				//if (valutazioneStoria.length() > 0) {
					StoricoRisposteRequisiti storicoVerificaRequisiti = buildStoricoVerificaRequisiti(jpaFreshRequisitoInst, userBean, valutazioneStoria, "", "");
					storicoRisposteRequisitiDao.save(storicoVerificaRequisiti);
					
					//requisitoInst.setNumStorico(jpaFreshRequisitoInst.getNumStorico() + 1);
					
					//spostato in buildStoricoRisposteRequisiti
					//jpaFreshRequisitoInst.addStoricoRisposteRequisiti(storicoRisposteRequisiti);			
				//}

			}
		}
		requisitoInstDao.impostaValutazioneOrValutazioneVerificatoreRequisitiGenSrByRequisitoUoStruMod(updaterRequisitiInstValutazioneVerificaInfo);
	}

	
	
	
	public void checkAllRequisito(AutovalutazionePageBean autovalutazionePageBean) {
		Map<String,String> checkM = autovalutazionePageBean.getCheckM();
		for (RequisitoInst requisitoInst:autovalutazionePageBean.getFilteredRequisitoInstL()) {
			String clientid = requisitoInst.getClientid();
			if (autovalutazionePageBean.isAllChecked()) {
				checkM.put(clientid, clientid);
			}
			else {
				if (checkM.containsKey(clientid))
					checkM.remove(clientid);
			}
		}
	}	
	
	public void checkRequisito(AutovalutazionePageBean autovalutazionePageBean, int index) {
		RequisitoInst requisitoInst = autovalutazionePageBean.getFilteredRequisitoInstL().get(index);
		String clientid = requisitoInst.getClientid();
		Map<String, String> checkM = autovalutazionePageBean.getCheckM();
		if (checkM.containsKey(clientid))
			checkM.remove(clientid);
		else
			checkM.put(clientid, clientid);
	}	
	
	public void applicaValutazioneAll(AutovalutazionePageBean autovalutazionePageBean) {
		Map<String, String> checkM = autovalutazionePageBean.getCheckM();
		for (RequisitoInst requisitoInst:autovalutazionePageBean.getFilteredRequisitoInstL()) {
			if (checkM.containsKey(requisitoInst.getClientid())) {
				if (
					requisitoInst.getRequisitoTempl().getTipoRisposta() != null 
					&& requisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Si/No") 
					&& 
						(requisitoInst.getTipoValutazione() == TipoValutazioneVerificaEnum.MANUALE
						||
						requisitoInst.getTipoValutazione() == TipoValutazioneVerificaEnum.SEMIAUTOMATICA)
					) {
					requisitoInst.setValutazione(autovalutazionePageBean.getValutazioneAll());
				}
			}
		}
	}
	
	public void applicaValutazioneVerificaAll(UserBean userBean, AutovalutazionePageBean autovalutazionePageBean) {
		Map<String, String> checkM = autovalutazionePageBean.getCheckM();
		for (RequisitoInst requisitoInst:autovalutazionePageBean.getFilteredRequisitoInstL()) {
			if (checkM.containsKey(requisitoInst.getClientid()) && aclService.userCanVerifyRequisito(autovalutazionePageBean.getDomandaStato(), userBean, requisitoInst)) {
				if (
					requisitoInst.getRequisitoTempl().getTipoRisposta() != null 
					&& requisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Si/No")
					&&
					(requisitoInst.getTipoVerifica() == TipoValutazioneVerificaEnum.MANUALE
					||
					requisitoInst.getTipoVerifica() == TipoValutazioneVerificaEnum.SEMIAUTOMATICA)
					) {
					requisitoInst.setValutazioneVerificatore(autovalutazionePageBean.getValutazioneAll());
				}
			}
		}
	}


	
	

	public void applicaValutazioneSogliaAll(AutovalutazionePageBean autovalutazionePageBean) {
		Map<String, String> checkM = autovalutazionePageBean.getCheckM();
		for (RequisitoInst requisitoInst:autovalutazionePageBean.getFilteredRequisitoInstL()) {
			if (checkM.containsKey(requisitoInst.getClientid())) {
				if (
					requisitoInst.getRequisitoTempl().getTipoRisposta() != null 
					&& requisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Soglia") 
					&& 
						(requisitoInst.getTipoValutazione() == TipoValutazioneVerificaEnum.MANUALE
						||
						requisitoInst.getTipoValutazione() == TipoValutazioneVerificaEnum.SEMIAUTOMATICA)
					) {
					requisitoInst.setValutazione(autovalutazionePageBean.getValutazioneSogliaAll());
				}
			}
		}
	}
	
	public void applicaValutazioneVerificaSogliaAll(UserBean userBean, AutovalutazionePageBean autovalutazionePageBean) {
		Map<String, String> checkM = autovalutazionePageBean.getCheckM();
		for (RequisitoInst requisitoInst:autovalutazionePageBean.getFilteredRequisitoInstL()) {
			if (checkM.containsKey(requisitoInst.getClientid()) && aclService.userCanVerifyRequisito(autovalutazionePageBean.getDomandaStato(), userBean, requisitoInst)) {
				if (
					requisitoInst.getRequisitoTempl().getTipoRisposta() != null 
					&& requisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Soglia")
					&&
					(requisitoInst.getTipoVerifica() == TipoValutazioneVerificaEnum.MANUALE
					||
					requisitoInst.getTipoVerifica() == TipoValutazioneVerificaEnum.SEMIAUTOMATICA)
					) {
					requisitoInst.setValutazioneVerificatore(autovalutazionePageBean.getValutazioneSogliaAll());
				}
			}
		}
	}

	
	
	
	
	
	public void assegnaRequisitoAll(AutovalutazionePageBean autovalutazionePageBean) {
		UtenteModel utenteModel = utenteDao.findOne(autovalutazionePageBean.getPersonaSelected());
		Map<String, String> checkM = autovalutazionePageBean.getCheckM();
		for (RequisitoInst requisitoInst:autovalutazionePageBean.getFilteredRequisitoInstL()) {
			if (checkM.containsKey(requisitoInst.getClientid()))
				requisitoInst.setUtenteModel(utenteModel);
		}		
	}	
	
	public void assegnaVerificaRequisitoAll(AutovalutazionePageBean autovalutazionePageBean) {
		UtenteModel utenteModel = utenteDao.findOne(autovalutazionePageBean.getPersonaSelected());
		Map<String, String> checkM = autovalutazionePageBean.getCheckM();
		for (RequisitoInst requisitoInst:autovalutazionePageBean.getFilteredRequisitoInstL()) {
			if (checkM.containsKey(requisitoInst.getClientid()))
				requisitoInst.setVerificatore(utenteModel);
		}		
	}	

	public void refreshLinks(String requisitoClientid, ExtraWayService xwService) throws Exception {
		//String requisitoClientid = editEvidenzePageBean.getRequisitoInst().getClientid();
		RequisitoInst jpaFreshRequisitoInst = requisitoInstDao.findByClientId(requisitoClientid);
		
		String allegatiNRecord = "";
		String allegatiFilename = "";
		XMLDocumento titleDocument = FolderDao.findRequisitoEvidenzeDocuments(xwService, requisitoClientid);
		if (titleDocument != null) {
			List<Element> itemsL = titleDocument.selectNodes("//Item");
			for (Element item:itemsL) {
		        String value = item.attributeValue("value");
		        value = value.substring(value.indexOf("|epn|") + 5); //elimino la parte preliminare del titolo (comprende sort e epn)
		        Vector<String> titleV = Text.split(value, "|");       
				String nrecord = titleV.get(0);
				//String file_name = titleV.get(27).trim();
				String file_name = org.apache.commons.lang3.StringEscapeUtils.unescapeXml(titleV.get(27).trim());
				//log.error(file_name);

				allegatiNRecord += "|" + nrecord;
				allegatiFilename += "|" + file_name;
			}			
		}

		if (allegatiNRecord.length() > 0)
			allegatiNRecord = allegatiNRecord.substring(1);
		if (allegatiFilename.length() > 0)
			allegatiFilename = allegatiFilename.substring(1);
		
		jpaFreshRequisitoInst.setAllegatiNRecord(allegatiNRecord);
		jpaFreshRequisitoInst.setAllegatiFilename(allegatiFilename);
		requisitoInstDao.save(jpaFreshRequisitoInst);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getFascicoloEvidenzeDocumentsL(ExternalContext externalContext, UserBean userBean, String requisitoClientid, ExtraWayService _xwService) throws Exception {
		XMLDocumento titleDocument = FolderDao.findRequisitoEvidenzeDocuments(_xwService, requisitoClientid);
		
		if (titleDocument == null) //nessun documento contenuto nel fascicolo
			return null;
		
		List<Map<String,String>> documentsL = new ArrayList<Map<String,String>>();
		
		List<Element> itemsL = titleDocument.selectNodes("//Item");
		for (Element item:itemsL) {
	        String value = item.attributeValue("value");
	        value = value.substring(value.indexOf("|epn|") + 5); //elimino la parte preliminare del titolo (comprende sort e epn)
	        Vector<String> titleV = Text.split(value, "|");       
			Map<String,String> map = new HashMap<String,String>();
			map.put("oggetto", titleV.get(5).trim());
			map.put("file_name", titleV.get(27).trim());
			map.put("file_id", Base64.encodeBase64String(titleV.get(26).trim().getBytes()));
			map.put("file_name_enc", Base64.encodeBase64String(titleV.get(27).trim().getBytes()));
			map.put("creazione_login", titleV.get(28).trim());
			map.put("creazione_ruolo", titleV.get(29).trim());
			map.put("creazione_data", titleV.get(3).trim());
			
			if(titleV.get(67).trim().equals("true"))
				map.put("creazione_admin", titleV.get(67).trim());
			else
				map.put("creazione_admin", "false");
			map.put("idIUnit", item.attributeValue("idIUnit"));
			
			HttpServletRequest httpServletRequest = (HttpServletRequest)externalContext.getNativeRequest();
			String sessionId = httpServletRequest.getSession().getId();
			map.put("session_id", Base64.encodeBase64String(sessionId.getBytes()));

			documentsL.add(map);			
		}
		return documentsL;
	}
	
	public void updateFascicoloEvidenzeDocumentsL(EditEvidenzePageBean editEvidenzePageBean, ExternalContext externalContext, UserBean userBean, ExtraWayService _xwService) throws Exception {
		editEvidenzePageBean.setDocumentsL(getFascicoloEvidenzeDocumentsL(externalContext, userBean, editEvidenzePageBean.getRequisitoInst().getClientid(), _xwService));
	}	
	
	public void deleteDocument(String idIUnit, ExtraWayService _xwService) throws Exception {
		_xwService.deleteDocument(Integer.parseInt(idIUnit));
	}	

	public StoricoRisposteRequisiti buildStoricoRisposteRequisiti(RequisitoInst requisitoInst, UserBean userBean, String valutazione, String note, String evidenze, String assegnatario) {
		/*StoricoRisposteRequisiti storicoRisposteRequisiti = new StoricoRisposteRequisiti();
		storicoRisposteRequisiti.setClientid(UUID.randomUUID().toString().toUpperCase());
		storicoRisposteRequisiti.setValutazione(valutazione);
		storicoRisposteRequisiti.setNote(note);
		storicoRisposteRequisiti.setEvidenze(evidenze);
		storicoRisposteRequisiti.setAssegnatario(assegnatario);
		storicoRisposteRequisiti.setDisabled("N");
		storicoRisposteRequisiti.setTimeStamp(new Date());
		
		//operatore
		AnagraficaUtenteModel anagraficaUenteModel = userBean.getUtenteModel().getAnagraficaUtenteModel(); 
		String operatore = anagraficaUenteModel.getCognome() + " " + anagraficaUenteModel.getNome();
		String uo = "";
		try {
			uo = userBean.getUtenteModel().getUoModel().getDenominazione() + " - ";
		}
		catch (Exception e) {
			
		}
		storicoRisposteRequisiti.setOperatore(uo + operatore);

		//storicoRisposteRequisiti.setRequisitoInst(requisitoInst);
		requisitoInst.addStoricoRisposteRequisiti(storicoRisposteRequisiti);
		
		return storicoRisposteRequisiti;*/
		return buildStoricoRisposteRequisiti(requisitoInst, userBean, valutazione, note, evidenze, assegnatario, null, null, null);
	}
	
	public StoricoRisposteRequisiti buildStoricoRisposteRequisiti(RequisitoInst requisitoInst, UserBean userBean, String valutazione, String note, String evidenze, String assegnatario, String valutazioneVerificatore, String noteVerificatore, String verificatore) {
		StoricoRisposteRequisiti storicoRisposteRequisiti = new StoricoRisposteRequisiti();
		storicoRisposteRequisiti.setClientid(UUID.randomUUID().toString().toUpperCase());
		storicoRisposteRequisiti.setValutazione(valutazione);
		storicoRisposteRequisiti.setNote(note);
		storicoRisposteRequisiti.setEvidenze(evidenze);
		storicoRisposteRequisiti.setAssegnatario(assegnatario);
		storicoRisposteRequisiti.setDisabled("N");
		
		if(userBean.isImitatingOperatore())
			storicoRisposteRequisiti.setAdmin("S");
		else
			storicoRisposteRequisiti.setAdmin("N");

		storicoRisposteRequisiti.setValutazioneVerificatore(valutazioneVerificatore);
		storicoRisposteRequisiti.setNoteVerificatore(noteVerificatore);
		storicoRisposteRequisiti.setVerificatore(verificatore);

		storicoRisposteRequisiti.setTimeStamp(new Date());
		
		//operatore
		AnagraficaUtenteModel anagraficaUenteModel = userBean.getUtenteModel().getAnagraficaUtenteModel(); 
		String operatore = anagraficaUenteModel.getCognome() + " " + anagraficaUenteModel.getNome();
		String uo = "";
		try {
			uo = userBean.getUtenteModel().getUoModel().getDenominazione() + " - ";
		}
		catch (Exception e) {
			
		}
		storicoRisposteRequisiti.setOperatore(uo + operatore);

		//storicoRisposteRequisiti.setRequisitoInst(requisitoInst);
		requisitoInst.addStoricoRisposteRequisiti(storicoRisposteRequisiti);
		
		return storicoRisposteRequisiti;
	}

	public StoricoRisposteRequisiti buildStoricoVerificaRequisiti(RequisitoInst requisitoInst, UserBean userBean, String valutazioneVerificatore, String noteVerificatore, String verificatore) {
		return buildStoricoRisposteRequisiti(requisitoInst, userBean, null, null, null, null, valutazioneVerificatore, noteVerificatore, verificatore);
	}

	public ShowStoriaRequisitoPageBean createShowStoriaRequisito(String requisitoInstClientId) {
		ShowStoriaRequisitoPageBean showStoriaRequisitoPageBean = new ShowStoriaRequisitoPageBean();
		RequisitoInst requisitoInst = requisitoInstDao.findByClientId(requisitoInstClientId);
		
		//workaround - serialization & lazy loading
		List<StoricoRisposteRequisiti> storicoRisposteRequisitiL = requisitoInst.getStoricoRisposteRequisitis();
		storicoRisposteRequisitiL.size();
		Collections.sort(storicoRisposteRequisitiL, new Comparator<StoricoRisposteRequisiti>() {
	        @Override
	        public int compare(StoricoRisposteRequisiti s1, StoricoRisposteRequisiti s2) {
	        	return s1.getTimeStamp().compareTo(s2.getTimeStamp());
	        }
	    });				
		
		showStoriaRequisitoPageBean.setStoricoRisposteRequisitiL(storicoRisposteRequisitiL);
		return showStoriaRequisitoPageBean;
	}	
	
	public ShowUdoInstPageBean createShowUdoInst(String udoInstClientId) {
		ShowUdoInstPageBean shoUdoInstPageBean = new ShowUdoInstPageBean();
		UdoInst udoInst = udoInstDao.findByClientId(udoInstClientId);
		shoUdoInstPageBean.setFolderStatus(udoInst.getDomandaInst().getStato());
		if(DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE.equals(shoUdoInstPageBean.getFolderStatus()) || DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_INSERIMENTO_ESITI.equals(shoUdoInstPageBean.getFolderStatus())) {
			//Carico i dati che occorrono per gestire l'inserimento degli esiti
			long startTimeParz = System.currentTimeMillis();
			shoUdoInstPageBean.setTipiUdoUtenteTemplPerTipoTitolareDomandaCorrente(tipoUdoUtenteTemplDao.findTipoUdoUtenteTemplByTipoTitolare(udoInst.getDomandaInst().getTitolareModel().getTipoTitolareTempl()));
			log.info("autovalutazioneService - createShowUdoInst - domanda clientid: " + udoInst.getDomandaInst().getClientid() + "; " + (System.currentTimeMillis() - startTimeParz) + " ms");
		}
		shoUdoInstPageBean.setUdoInst(udoInst);
		return shoUdoInstPageBean;
	}	

	public void filtraRequisiti(UserBean userBean, AutovalutazionePageBean autovalutazionePageBean, boolean skipOnRender) {
		if (skipOnRender)
			return;
		autovalutazionePageBean.filterRequisitiL();
	}

	public void rimuoviFiltroRequisiti(UserBean userBean, AutovalutazionePageBean autovalutazionePageBean, boolean skipOnRender) {
		if (skipOnRender)
			return;
		autovalutazionePageBean.clearRequisitoSearchForm();
		autovalutazionePageBean.filterRequisitiL();
	}

	
	public Map<String, UdoInst> getUdoInstsInfo(AutovalutazionePageBean autovalutazionePageBean) {
		//autovalutazionePageBean.getFilteredRequisitoInstL();
		log.info("passato da downloadCsv");
		log.info("autovalutazionePageBean.getFilteredRequisitoInstL().size(): " + autovalutazionePageBean.getFilteredRequisitoInstL().size());
		//Carico i dati che occorrono per le UdoInst
		Set<String> udoInstClientid = new HashSet<String>();
		for(RequisitoInst req : autovalutazionePageBean.getFilteredRequisitoInstL()) {
			if(req.getUdoInst() != null) {
				udoInstClientid.add(req.getUdoInst().getClientid());
			}
		}
		//ricavo
		if(udoInstClientid.size() > 0)
			return udoInstDao.findByClientIdsForAutovalutazioneExportCsv(udoInstClientid);
		
		return null;
	}
	
	public String jsCloseFunction(String prefix, String index, boolean isEmptyField) {
		String ret = "setClass('" + prefix + index + "'," + isEmptyField + ");";
		if (!isEmptyField)
			ret += "setClass('showStoriaRequisitoLink" + index + "',false);";
		return ret;
	}
	
	public String jsCloseFunction(EditNotePageBean editNotePageBean, String index) {
		//autovalutazioneService.jsCloseFunction('showNoteLink', index, editNotePageBean.note eq '')
		boolean isEmptyField = editNotePageBean.getNote() == null || editNotePageBean.getNote().isEmpty();
		String ret = "";
		if(editNotePageBean.isNoteVerifica())
			ret = "setClass('showNoteVerifLink" + index + "'," + isEmptyField + ");";
		else
			ret = "setClass('showNoteLink" + index + "'," + isEmptyField + ");";
		if (!isEmptyField)
			ret += "setClass('showStoriaRequisitoLink" + index + "',false);";
		return ret;
	}

	public void pageFirst(AutovalutazionePageBean autovalutazionePageBean) {
		if (autovalutazionePageBean.isCanFirst()) {
			autovalutazionePageBean.setPageIndex(1);
		}
	}
	
	public void pagePrev(AutovalutazionePageBean autovalutazionePageBean) {
		if (autovalutazionePageBean.isCanPrev()) {
			autovalutazionePageBean.setPageIndex(autovalutazionePageBean.getPageIndex() - 1);
		}
	}
	
	public void pageNext(AutovalutazionePageBean autovalutazionePageBean) {
		if (autovalutazionePageBean.isCanNext()) {
			autovalutazionePageBean.setPageIndex(autovalutazionePageBean.getPageIndex() + 1);
		}
	}
	
	public void pageLast(AutovalutazionePageBean autovalutazionePageBean) {
		if (autovalutazionePageBean.isCanLast()) {
			autovalutazionePageBean.setPageIndex(autovalutazionePageBean.getPageCount());
		}
	}		

	public void aggiornaCampoAllegatiFileName(MessageContext messageContext, HomePageBean homePageBean, ExtraWayService xwService, boolean fromProgressBar, ExternalContext externalContext) throws Exception {
		@SuppressWarnings("unchecked")
		List<RequisitoInst> reqs = (List<RequisitoInst>)externalContext.getSessionMap().get("aggiornaCampoAllegatiFileNameRequisitoInstL");
		if (reqs == null) {
			reqs = requisitoInstDao.findRequisitiConAllegati();
			externalContext.getSessionMap().put("aggiornaCampoAllegatiFileNameRequisitoInstL", reqs); //si utilizza la session per fare get dello stato del JobBean (si bypassa la view serialization di webflow)		
		}

		if (reqs == null || reqs.size() == 0) {
			MessageBuilder mB = new MessageBuilder();
			messageContext.addMessage(mB.error().source("messages").defaultText("Nessun requisito con allegati trovato").build());
			return;
		}
		
		JobBean jobBean = null;
		if (!fromProgressBar) { //la richiesta proviene dal pulsante premuto dall'utente -> si crea il job
			jobBean = new JobBean("aggiornaCampoAllegatiFileName", reqs.size());
			homePageBean.setJobBean(jobBean);
		}
		else { // la richiesta proviene da progressBar -> si recupera il job
			jobBean = homePageBean.getJobBean();
		}

		RequisitoInst req = reqs.get(jobBean.getCurrent());
        
		//si aggiorna il valore
		this.refreshLinks(req.getClientid(), xwService);
		
		//update stato job
		jobBean.incrementCurrent();
			
		//se il lavoro e' finito si elimina il job
		if (jobBean.isCompleted()) {
			homePageBean.setJobBean(null);
			
			MessageBuilder mB = new MessageBuilder();
			messageContext.addMessage(mB.error().source("messages").defaultText("Operazione completata con successo").build());			
		}

	}
	
	
	public void copyNoteEvidenzeRequisito(AutovalutazionePageBean autovalutazionePageBean, int index) {
		autovalutazionePageBean.setCopiedRequisitoInst(autovalutazionePageBean.getFilteredRequisitoInstL().get(index));
	}		
	
	public void startAssegnazioneMassivaNoteEvidenze(UserBean userBean, AutovalutazionePageBean autovalutazionePageBean, ExtraWayService _xwService, ExternalContext externalContext, String doamndaFolderIDUnit) {
		int total = autovalutazionePageBean.getCheckM().size();
		JobBean neamJobBean = new JobBean("assegnazioneMassivaNoteEvidenze", total);
		autovalutazionePageBean.setNeamJobBean(neamJobBean);
		externalContext.getSessionMap().put("pageBean", autovalutazionePageBean); //si utilizza la session per fare get dello stato del JobBean (si bypassa la view serialization di webflow)

		//start del runnable che esegue l'operazione massiva
		AssegnazioneMassivaNoteEvidenzeRunnable neamRunnable = new AssegnazioneMassivaNoteEvidenzeRunnable(neamJobBean, autovalutazionePageBean, requisitoInstDao, storicoRisposteRequisitiDao,
				userBean, txManager, _xwService, this, doamndaFolderIDUnit, aclService);
		new Thread(neamRunnable).start();
	}	
	
	public void resetAssegnazioneMassivaNoteEvidenze(AutovalutazionePageBean autovalutazionePageBean) {
		autovalutazionePageBean.getCheckM().clear();
		autovalutazionePageBean.setCopiedRequisitoInst(null);
		autovalutazionePageBean.setNeamJobBean(null);
		autovalutazionePageBean.setNeamNoteChecked(false);		
		autovalutazionePageBean.setNeamEvidenzeChecked(false);
		autovalutazionePageBean.setNeamNoteVerifChecked(false);		
		autovalutazionePageBean.setAllChecked(false);
	}
	
	public void refreshAutovalutazionePageBean(UserBean userBean, AutovalutazionePageBean autovalutazionePageBean) {
		UdoUoInstForList udoUoInst = autovalutazionePageBean.getUdoUoInst();
		if (udoUoInst != null) {
			if(udoUoInst.isUdo()) {
				autovalutazionePageBean.setRequisitoInstL(requisitoInstDao.findRequisitiByUdoInst(udoUoInst.getUdoInst(), false, false, false));
			} else if (udoUoInst.isUo()) {
				autovalutazionePageBean.setRequisitoInstL(requisitoInstDao.findRequisitiByUoInst(udoUoInst.getUoInst(), false, false, false));
			} else if (udoUoInst.isStruttura()) {
				autovalutazionePageBean.setRequisitoInstL(requisitoInstDao.findRequisitiByStrutturaInst(udoUoInst.getStrutturaInst(), false, false, false));
			} else if (udoUoInst.isEdificio()) {
				autovalutazionePageBean.setRequisitoInstL(requisitoInstDao.findRequisitiByEdificioInst(udoUoInst.getEdificioInst(), false, false, false));
			} else {
				autovalutazionePageBean.setRequisitoInstL(requisitoInstDao.findRequisitiGeneraliAziendali(udoUoInst.getDomandaInst(), false, false, false));
			}			
		}
		else if (autovalutazionePageBean.isSearchAllRequisiti()) {
			List<RequisitoInst> requisitiInstL = requisitoInstDao.findAllRequisitiByDomandaInst(autovalutazionePageBean.getDomandaClientId());
			autovalutazionePageBean.setRequisitoInstL(requisitiInstL);
		}
		else {
			List<RequisitoInst> requisitiInstL = requisitoInstDao.findRequisitiByDomandaInstUtenteModel(autovalutazionePageBean.getDomandaClientId(), userBean.getUtenteModel());
			autovalutazionePageBean.setRequisitoInstL(requisitiInstL);			
		}
		filtraRequisiti(userBean, autovalutazionePageBean, false);
	}

	public void copyNoteVerifRequisito(AutovalutazionePageBean autovalutazionePageBean, int index) {
		autovalutazionePageBean.setCopiedRequisitoInst(autovalutazionePageBean.getFilteredRequisitoInstL().get(index));
	}		
	
	public void startAssegnazioneMassivaNoteVerif(UserBean userBean, AutovalutazionePageBean autovalutazionePageBean, ExtraWayService _xwService, ExternalContext externalContext, String doamndaFolderIDUnit) {
		int total = autovalutazionePageBean.getCheckM().size();
		JobBean neamJobBean = new JobBean("assegnazioneMassivaNoteVerif", total);
		autovalutazionePageBean.setNeamJobBean(neamJobBean);
		autovalutazionePageBean.setNeamNoteChecked(false);
		autovalutazionePageBean.setNeamEvidenzeChecked(false);
		autovalutazionePageBean.setNeamNoteVerifChecked(true);
		externalContext.getSessionMap().put("pageBean", autovalutazionePageBean); //si utilizza la session per fare get dello stato del JobBean (si bypassa la view serialization di webflow)

		//start del runnable che esegue l'operazione massiva
		AssegnazioneMassivaNoteEvidenzeRunnable neamRunnable = new AssegnazioneMassivaNoteEvidenzeRunnable(neamJobBean, autovalutazionePageBean, requisitoInstDao, storicoRisposteRequisitiDao,
				userBean, txManager, _xwService, this, doamndaFolderIDUnit, aclService);
		new Thread(neamRunnable).start();
	}	
	
}
