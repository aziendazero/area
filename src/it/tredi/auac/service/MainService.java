package it.tredi.auac.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.webflow.context.ExternalContext;

import com.google.gson.Gson;
import com.itextpdf.text.pdf.events.IndexEvents.Entry;

import it.highwaytech.apps.generic.XMLDocumento;
import it.highwaytech.util.Text;
import it.tredi.auac.BooleanEnum;
import it.tredi.auac.DomandaFieldToOrderEnum;
import it.tredi.auac.DomandaOrderTypeEnum;
import it.tredi.auac.ExtraWayService;
import it.tredi.auac.TipoPostiLettoEnum;
import it.tredi.auac.TitolareFieldToOrderEnum;
import it.tredi.auac.WebflowSafeString;
import it.tredi.auac.bean.HomePageBean;
import it.tredi.auac.bean.JobBean;
import it.tredi.auac.bean.MraPageBean;
import it.tredi.auac.bean.ShowFolderPageBean;
import it.tredi.auac.bean.TitolariPageBean;
import it.tredi.auac.bean.UdoUoForList;
import it.tredi.auac.bean.UserBean;
import it.tredi.auac.bean.XWFolderTitlesBean;
import it.tredi.auac.business.ClassificazioneUdoTemplSaluteMentaleDistinctInfo;
import it.tredi.auac.dao.DirezioneTemplDao;
import it.tredi.auac.dao.DomandaInstDao;
import it.tredi.auac.dao.EdificioInstDao;
import it.tredi.auac.dao.FolderDao;
import it.tredi.auac.dao.MraModelDao;
import it.tredi.auac.dao.RequisitoInstDao;
import it.tredi.auac.dao.StrutturaInstDao;
import it.tredi.auac.dao.TipoProcTemplDao;
import it.tredi.auac.dao.TitolareModelDao;
import it.tredi.auac.dao.UdoInstDao;
import it.tredi.auac.dao.UdoModelDao;
import it.tredi.auac.dao.UoInstDao;
import it.tredi.auac.dao.UoModelDao;
import it.tredi.auac.orm.entity.BindListaRequClassUdo;
import it.tredi.auac.orm.entity.BindListaRequTipoTitClassTit;
import it.tredi.auac.orm.entity.BindListaRequisito;
import it.tredi.auac.orm.entity.BindTipo22Lista;
import it.tredi.auac.orm.entity.BindUdoDisciplina;
import it.tredi.auac.orm.entity.BindUoProcLista;
import it.tredi.auac.orm.entity.BrancaTempl;
import it.tredi.auac.orm.entity.BrancaUdoInstInfo;
import it.tredi.auac.orm.entity.ClassificazioneUdoTempl;
import it.tredi.auac.orm.entity.DirezioneTempl;
//import it.tredi.auac.orm.entity.CodiciStruttDenomin;
//import it.tredi.auac.orm.entity.CodiciUlssTerritoriali;
import it.tredi.auac.orm.entity.DisciplinaUdoInstInfo;
import it.tredi.auac.orm.entity.DomandaInst;
import it.tredi.auac.orm.entity.EdificioInst;
import it.tredi.auac.orm.entity.EdificioTempl;
import it.tredi.auac.orm.entity.EstensioneUdoInstInfo;
import it.tredi.auac.orm.entity.EstenstioneTempl;
import it.tredi.auac.orm.entity.FattProdUdoInstInfo;
import it.tredi.auac.orm.entity.FattProdUdoModel;
import it.tredi.auac.orm.entity.ListaRequisitiTempl;
import it.tredi.auac.orm.entity.PrestazioneTempl;
import it.tredi.auac.orm.entity.PrestazioneUdoInstInfo;
import it.tredi.auac.orm.entity.RequisitoInst;
import it.tredi.auac.orm.entity.SedeOperModel;
import it.tredi.auac.orm.entity.StatoUdo;
import it.tredi.auac.orm.entity.StrutturaInst;
import it.tredi.auac.orm.entity.StrutturaModel;
import it.tredi.auac.orm.entity.TipoProcTempl;
import it.tredi.auac.orm.entity.TitolareModel;
import it.tredi.auac.orm.entity.UdoInst;
import it.tredi.auac.orm.entity.UdoModel;
import it.tredi.auac.orm.entity.UoInst;
import it.tredi.auac.orm.entity.UoModel;


@Service
@Transactional
public class MainService {
	private static final Logger log = Logger.getLogger(MainService.class);

	@Autowired
	private EdificioInstDao edificioInstDao;

	@Autowired
	private StrutturaInstDao strutturaInstDao;

	@Autowired
	private UoModelDao uoModelDao;
	
	@Autowired
	private UdoModelDao udoModelDao;

	@Autowired
	private UdoInstDao udoInstDao;

	@Autowired
	private UoInstDao uoInstDao;

	@Autowired
	private DomandaInstDao domandaInstDao;

	@Autowired
	private RequisitoInstDao requisitoInstDao;
	
	@Autowired
	private TipoProcTemplDao tipoProcTemplDao;
	
	@Autowired
	private TitolareModelDao titolareModelDao;
	
	@Autowired
	private DirezioneTemplDao direzioneTemplDao;

	@Autowired
	WorkflowService workflowService;	

	@Autowired
	UtilService utilService;	

	@Autowired
	MraModelDao mraModelDao;	

	public MainService() {
		//do nothing
	}
	
	public String chechkStartAction(String action) {
		/*if (action != null && action.equals("update"))
			return "update";
		else*/
			return "home";
	}
	
	public HomePageBean createHomePageBean(UserBean userBean) {
		return createHomePageBean(userBean, false);
	}
	
	public HomePageBean createHomePageBean(UserBean userBean, boolean showDomandeConTaskPerUtente) {
		HomePageBean homePageBean = new HomePageBean();
		homePageBean.setShowDomandeConTaskPerUtente(showDomandeConTaskPerUtente);
		if (userBean.isTITOLARE()) {
			TitolareModel titolare = titolareModelDao.findByUtenteModel(userBean.getUtenteModel());
			homePageBean.setTitolareModel(titolare);
		}
		else if (userBean.isOPERATORE_TITOLARE() || userBean.isOPERATORE_TITOLARE_IN_LETTURA()) {
			TitolareModel titolare = titolareModelDao.findOne(userBean.getTitolareClientId());
			homePageBean.setTitolareModel(titolare);
		}
		else if (userBean.isCOLLABORATORE_VALUTAZIONE() || userBean.isVALUTATORE_INTERNO()) {
			TitolareModel titolare;
			if(userBean.getTitolareModel() != null)
				titolare = userBean.getTitolareModel();
			else
				titolare = userBean.getUtenteModel().getUoModel().getTitolareModel();
			homePageBean.setTitolareModel(titolare);
		}		

		return homePageBean;
	}
	
	public void runAggProcInCorsoUdoUoTit(HomePageBean homePageBean) {
		domandaInstDao.runAggProcInCorsoUdoUoTit(homePageBean.getTitolareModel().getClientid());
	}
	
	/*
	Sembra non essere utilizzato da nessuno
	public void loadTitolareUdos(UserBean userBean, UdoModelDao udoModelDao, HomePageBean homePageBean, boolean skipOnRender) {
		if (skipOnRender)
			return;		
		//if (userBean.isTITOLARE() || userBean.isOPERATORE_TITOLARE())
		//	homePageBean.setUdoL(udoModelDao.findValidateByTitolare(homePageBean.getTitolareModel()));
	}
	*/
	
	public void loadTitolareUdosSenzaProcedimentiInCorso(UserBean userBean, UdoModelDao udoModelDao, HomePageBean homePageBean, boolean skipOnRender) {
		if (skipOnRender)
			return;		
		if (userBean.isTITOLARE() || userBean.isOPERATORE_TITOLARE()){
			List<UdoUoForList> listUdoUoForList = new ArrayList<UdoUoForList>();
			if((homePageBean.getSubStringDenominazioneDaCercare() == null || homePageBean.getSubStringDenominazioneDaCercare().isEmpty()) && 
					(homePageBean.getSubStringTipoUdoDaCercare() == null || homePageBean.getSubStringTipoUdoDaCercare().isEmpty()) && 
					(homePageBean.getSubStringBrancaDaCercare() == null || homePageBean.getSubStringBrancaDaCercare().isEmpty()) && 
					(homePageBean.getSubStringDisciplinaDaCercare() == null || homePageBean.getSubStringDisciplinaDaCercare().isEmpty()) && 
					(homePageBean.getSubStringSedeOperativaDaCercare() == null || homePageBean.getSubStringSedeOperativaDaCercare().isEmpty()) && 
					(homePageBean.getSubStringDirettoreDaCercare() == null || homePageBean.getSubStringDirettoreDaCercare().isEmpty()) && 
					(homePageBean.getSubStringCodiceUnivocoDaCercare() == null || homePageBean.getSubStringCodiceUnivocoDaCercare().isEmpty()) &&
					(homePageBean.getSubStringCodiceUlssDaCercare() == null || homePageBean.getSubStringCodiceUlssDaCercare().isEmpty()) &&
					(homePageBean.getSubStringCodiceExUlssDaCercare() == null || homePageBean.getSubStringCodiceExUlssDaCercare().isEmpty())
					) {
				// homePageBean.getSubStringUODaCercare() è diverso da null
				//L'unica ricerca attiva è relativa al nome della UO quindi carico tutte le UO con il nome cercato e le relative UDO
				List<UoModel> listUoModel = uoModelDao.findValidateByTitolareEDenominazioneSenzaProcedimentiInCorso(homePageBean.getTitolareModel(), homePageBean.getSubStringUODaCercare());
				log.debug("Ricerca delle uo per denominazione, Uo trovate: " + listUoModel.size());
				for(UoModel uoModel : listUoModel) {
					listUdoUoForList.add(new UdoUoForList(uoModel));		
					for(UdoModel udoModel : uoModel.getUdoModels()) {
						
						//workaround - JPA CACHE
						//udoModelDao.getEntityManager().refresh(udoModel);
						
						if(udoModel.getValidata().equals("Y") && udoModel.getProcedimentoInCorso().equals("N")){
							//Controllo che siano definitive e senza procedimento in corso
							loadFatProd(udoModel);
							listUdoUoForList.add(new UdoUoForList(udoModel));
						}
					}
				}
			}
			else {
				//Ricerco le udo secondo i parametri di ricerca ordinandole per UO di appartenenza
				List<UdoModel> listUdoModel = udoModelDao.findValidateByTitolareSenzaProcedimentiInCorso(homePageBean.getTitolareModel(), homePageBean.getSubStringDenominazioneDaCercare(), homePageBean.getSubStringTipoUdoDaCercare(), homePageBean.getSubStringBrancaDaCercare(), homePageBean.getSubStringDisciplinaDaCercare(), homePageBean.getSubStringSedeOperativaDaCercare(), homePageBean.getSubStringUODaCercare(), homePageBean.getSubStringDirettoreDaCercare(), homePageBean.getSubStringCodiceUnivocoDaCercare(), homePageBean.getSubStringCodiceUlssDaCercare(), homePageBean.getSubStringCodiceExUlssDaCercare());
				String uoClientIdPrec = "";
				for(UdoModel udoModel : listUdoModel) {
					if(!uoClientIdPrec.equals(udoModel.getUoModel().getId().getClientid())) {
						listUdoUoForList.add(new UdoUoForList(udoModel.getUoModel()));
						uoClientIdPrec = udoModel.getUoModel().getId().getClientid();
					}
					loadFatProd(udoModel);
					listUdoUoForList.add(new UdoUoForList(udoModel));
				}
			}
			homePageBean.setUdoUoL(listUdoUoForList);
			homePageBean.setActiveRowIndex(0);
		}
	}
	
	private void loadFatProd(UdoModel udoModel) {
		for(FattProdUdoModel fp : udoModel.getFattProdUdoModels()) {
			fp.getTipoFattoreProdTempl();
		}
	}
	
	private XMLDocumento findTitolareFolder(ExtraWayService xwService, TitolareModel titolareModel) throws Exception {
		XMLDocumento titolareFolder = FolderDao.findTitolareFolder(xwService, titolareModel);
		if (titolareFolder == null)
			titolareFolder = FolderDao.insertTitolareFolder(xwService, titolareModel);
		String numFasc = titolareFolder.getAttributeValue("//fascicolo/@numero");
		XMLDocumento titolareSubFolder = FolderDao.findAttiSubFolder(xwService, numFasc);
		if (titolareSubFolder == null)
			titolareSubFolder = FolderDao.insertTitolareAttiSubFolder(xwService, numFasc, titolareModel);
		return titolareFolder;
	}
	
	public void loadProcedimenti(MessageContext messageContext, UserBean userBean, HomePageBean homePageBean, ExtraWayService xwService) throws Exception {
		XWFolderTitlesBean folders = new XWFolderTitlesBean();
		XMLDocumento documento = null;
		
		if(homePageBean.isShowOnlyDomandeAssignedCongruenzaEsito()) {
			//l'eventuale valore impostato in ricerca per lo stato viene tolto
			homePageBean.setFolderStatusSelected("");
		}
		
		String filtroSulloStato = homePageBean.getFolderStatusSelected();
		String filtrodataCreazioneDa = homePageBean.getDataCreazioneDa();
		String filtrodataCreazioneA = homePageBean.getDataCreazioneA();
		String filtrodataInvioDomandaDa = homePageBean.getDataInvioDomandaDa();
		String filtrodataInvioDomandaA = homePageBean.getDataInvioDomandaA();
		String filtrodataConclusioneDa = homePageBean.getDataConclusioneDa();
		String filtrodataConclusioneA = homePageBean.getDataConclusioneA();
		String filtroTipoProcedimento = homePageBean.getTipoProcSelected();
		String filtroOggettoDomanda = homePageBean.getOggettoDomandaDaCercare();
		
		if (userBean.isTITOLARE() || userBean.isOPERATORE_TITOLARE() || userBean.isOPERATORE_TITOLARE_IN_LETTURA()) {
			Set<String> domandeClientId = null;
			String extraQuery = "";
			if(homePageBean.isShowDomandeConTaskPerUtente()) {
				//domandeClientId se settato contiene la lista dei clientid delle domande su cui filtrare, 
				//ovviamente se domandeClientId viene settato ad una lista vuota significa che nessuna domanda e' stata trovata quindi nessuna domanda sara' restituita
				domandeClientId = new HashSet<String>(workflowService.getDomandeClientIdWithAvailableTaskForUser(userBean.getWorkflowBean().getApiSession().getUserId()));
				extraQuery = "AND ([/fascicolo/extra/@folder_status/]<>\"" + DomandaInstDao.PROCEDIMENTO_CONCLUSO + "\")";				
			}
			documento = findTitolareFolder(xwService, homePageBean.getTitolareModel());
			String numFasc = documento.getAttributeValue("//fascicolo/@numero");
			//if(homePageBean.getDomandaOrderTypeEnum() == null)
			//	homePageBean.setDomandaOrderTypeEnum(DomandaOrderTypeEnum.NumeroProcedimentoCrescente);
			folders.setContents(FolderDao.findFolderTree(xwService, numFasc, domandeClientId, extraQuery, filtroSulloStato, filtrodataCreazioneDa, filtrodataCreazioneA, filtrodataInvioDomandaDa, filtrodataInvioDomandaA, filtrodataConclusioneDa, filtrodataConclusioneA, filtroTipoProcedimento, filtroOggettoDomanda, homePageBean.getDomandaOrderTypeEnum()));
		}
		else if (userBean.isCOLLABORATORE_VALUTAZIONE() || userBean.isVALUTATORE_INTERNO()) {
			documento = findTitolareFolder(xwService, homePageBean.getTitolareModel());
			String numFasc = documento.getAttributeValue("//fascicolo/@numero");
			//Ricerco le domande con requisiti assegnati al COLLABORATORE_VALUTAZIONE corrente
			Map<String, Integer> domandeClientIdReqAssegn = domandaInstDao.getDomandeClientIdConRequisitiAssegnatiAdUtenteENumeroRisposteMancanti(userBean.getUtenteModel());
			//Se non trovo domande assegnate non deve vedere fascicoli
			
			if(domandeClientIdReqAssegn.size() > 0)
				folders.setContents(FolderDao.findFolderTree(xwService, numFasc, domandeClientIdReqAssegn.keySet(), "", filtroSulloStato, filtrodataCreazioneDa, filtrodataCreazioneA, filtrodataInvioDomandaDa, filtrodataInvioDomandaA, filtrodataConclusioneDa, filtrodataConclusioneA, filtroTipoProcedimento, filtroOggettoDomanda, homePageBean.getDomandaOrderTypeEnum()));
			homePageBean.setDomandeClientIdAssegnateAdUtenteCollaboratoreENumeroRisposteMancanti(domandeClientIdReqAssegn);
		}
		else if (userBean.isREGIONE() || userBean.isREGIONE_IN_LETTURA() || userBean.isUTENTE_PROGRAMMAZIONE()) {
			try {
				String extraQuery = "";
				//20180802 - 15855 1.	Punto 12. Differenziazione della visibilità delle domande e degli attori del processo per utente con profilo REGIONE 
				String filtroSulTitolare = homePageBean.getSubStringtitolareDaCercare();
				
				Set<String> domandeClientIdCongruenza = new HashSet<String>(domandaInstDao.getDomandeClientIdAssignedCongruenzaEsito(userBean.getUtenteModel()));
				//domandeClientId se settato contiene la lista dei clientid delle domande su cui filtrare, 
				//ovviamente se domandeClientId viene settato ad una lista vuota significa che nessuna domanda e' stata trovata quindi nessuna domanda sara' restituita
				Set<String> domandeClientId = null;

				if(homePageBean.isShowOnlyDomandeAssignedCongruenzaEsito()) {
					//Ricavo le domande in stato congruenza programmazione con almeno un esito della udo assegnato all'utente corrente
					//lo stato deve essere STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE oppure STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_INSERIMENTO_ESITI
					//l'utente deve essere REGIONE_ROLE, il checkbox viene mostrato solo a tali utenti
					//gli esiti sono assegnati solo se il tipo_udo_22 e il tipo titolare della relativa domanda sono fra i TIPO_UDO_UTENTE_TEMPL dell'utente stesso
					domandeClientId = new HashSet<String>(domandeClientIdCongruenza);
				}
				
				if(homePageBean.isShowOnlyDomandeAssignedCongruenzaSenzaEsito()) {
					domandeClientId = new HashSet<String>(domandaInstDao.getDomandeClientIdCongruenzaSenzaEsito());
				}
				if(homePageBean.getDirezioneTemplClientidSel() != null && !homePageBean.getDirezioneTemplClientidSel().isEmpty()) {
					if(domandeClientId == null) {
						domandeClientId = new HashSet<String>(domandaInstDao.getDomandeClientIdPerTitolariDiDirezioneTempl(homePageBean.getDirezioneTemplClientidSel()));
					} else {
						//Effettuo l'intersezione dei 2 set
						Set<String> domandeTitDirClientId = new HashSet<String>(domandaInstDao.getDomandeClientIdPerTitolariDiDirezioneTempl(homePageBean.getDirezioneTemplClientidSel()));
						domandeClientId.retainAll(domandeTitDirClientId);
					}
				}
				if(homePageBean.isShowDomandeConTaskPerUtente()) {
					if(domandeClientId == null) {
						domandeClientId = new HashSet<String>(workflowService.getDomandeClientIdWithAvailableTaskForUser(userBean.getWorkflowBean().getApiSession().getUserId()));
					} else {
						//Effettuo l'intersezione dei 2 set
						Set<String> domandeVerifClientId = new HashSet<String>(workflowService.getDomandeClientIdWithAvailableTaskForUser(userBean.getWorkflowBean().getApiSession().getUserId()));
						domandeClientId.retainAll(domandeVerifClientId);
					}
					extraQuery = "AND ([/fascicolo/extra/@folder_status/]<>\"" + DomandaInstDao.PROCEDIMENTO_CONCLUSO + "\")";				
				}

				//
				if(domandeClientId == null) {
					domandeClientId = new HashSet<String>(domandaInstDao.getDomandeClientIdPerUfficioDiAppartenenza(userBean.getUtenteModel().getClientid()));
					if(!domandeClientIdCongruenza.isEmpty()) {
						//devo aggiungere anche le domande della congruenza programmazione
						domandeClientId.addAll(domandeClientIdCongruenza);
					}
				} else {
					//Effettuo l'intersezione dei 2 set
					Set<String> domandeVisibiliUtente = new HashSet<String>(domandaInstDao.getDomandeClientIdPerUfficioDiAppartenenza(userBean.getUtenteModel().getClientid()));
					if(!domandeClientIdCongruenza.isEmpty()) {
						//devo aggiungere anche le domande della congruenza programmazione
						domandeVisibiliUtente.addAll(domandeClientIdCongruenza);
					}
					domandeClientId.retainAll(domandeVisibiliUtente);
				}
				
				
				if(homePageBean.getDomandaOrderTypeEnum() == null)
					homePageBean.setDomandaOrderTypeEnum(DomandaOrderTypeEnum.NumeroProcedimentoCrescente);
				documento = FolderDao.findRegioneFolder(xwService, extraQuery, filtroSulloStato, filtroSulTitolare, filtrodataCreazioneDa, filtrodataCreazioneA, filtrodataInvioDomandaDa, filtrodataInvioDomandaA, filtrodataConclusioneDa, filtrodataConclusioneA, filtroTipoProcedimento, filtroOggettoDomanda, domandeClientId, homePageBean.getDomandaOrderTypeEnum());
				folders.setContents(documento);		
				
			}
			catch (Exception e) {
				//nessun procedimento visibile alla regione
			}
		}
		else if (userBean.isVERIFICATORE()) {
			//20180802 - 15855 1.	Punto 12. Differenziazione della visibilità delle domande e degli attori del processo per utente con profilo REGIONE 
			//il VERIFICATORE non subisce modifiche per questo separato dal REGIONE 
			try {
				String filtroSulTitolare = homePageBean.getSubStringtitolareDaCercare();
				String extraQuery = "";
				
				//domandeClientId se settato contiene la lista dei clientid delle domande su cui filtrare, 
				//ovviamente se domandeClientId viene settato ad una lista vuota significa che nessuna domanda e' stata trovata quindi nessuna domanda sara' restituita
				Set<String> domandeClientId = null;

				if(homePageBean.isShowOnlyDomandeAssignedCongruenzaEsito()) {
					//Ricavo le domande in stato congruenza programmazione con almeno un esito della udo assegnato all'utente corrente
					//lo stato deve essere STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE oppure STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_INSERIMENTO_ESITI
					//l'utente deve essere REGIONE_ROLE, il checkbox viene mostrato solo a tali utenti
					//gli esiti sono assegnati solo se il tipo_udo_22 e il tipo titolare della relativa domanda sono fra i TIPO_UDO_UTENTE_TEMPL dell'utente stesso
					domandeClientId = new HashSet<String>(domandaInstDao.getDomandeClientIdAssignedCongruenzaEsito(userBean.getUtenteModel()));
				}
				
				if(homePageBean.isShowOnlyDomandeAssignedCongruenzaSenzaEsito()) {

					domandeClientId = new HashSet<String>(domandaInstDao.getDomandeClientIdCongruenzaSenzaEsito());
				}
				if(homePageBean.getDirezioneTemplClientidSel() != null && !homePageBean.getDirezioneTemplClientidSel().isEmpty()) {
					if(domandeClientId == null) {
						domandeClientId = new HashSet<String>(domandaInstDao.getDomandeClientIdPerTitolariDiDirezioneTempl(homePageBean.getDirezioneTemplClientidSel()));
					} else {
						//Effettuo l'intersezione dei 2 set
						Set<String> domandeTitDirClientId = new HashSet<String>(domandaInstDao.getDomandeClientIdPerTitolariDiDirezioneTempl(homePageBean.getDirezioneTemplClientidSel()));
						domandeClientId.retainAll(domandeTitDirClientId);
					}
				}
				if(domandeClientId == null) {
					domandeClientId = new HashSet<String>(domandaInstDao.getDomandeClientIdPerVerificatore(userBean.getUtenteModel()));
				} else {
					//Effettuo l'intersezione dei 2 set
					Set<String> domandeVerifClientId = new HashSet<String>(domandaInstDao.getDomandeClientIdPerVerificatore(userBean.getUtenteModel()));
					domandeClientId.retainAll(domandeVerifClientId);
				}
				if(homePageBean.isShowDomandeConTaskPerUtente()) {
					if(domandeClientId == null) {
						domandeClientId = new HashSet<String>(workflowService.getDomandeClientIdWithAvailableTaskForUser(userBean.getWorkflowBean().getApiSession().getUserId()));
					} else {
						//Effettuo l'intersezione dei 2 set
						Set<String> domandeVerifClientId = new HashSet<String>(workflowService.getDomandeClientIdWithAvailableTaskForUser(userBean.getWorkflowBean().getApiSession().getUserId()));
						domandeClientId.retainAll(domandeVerifClientId);
					}
					extraQuery = "AND ([/fascicolo/extra/@folder_status/]<>\"" + DomandaInstDao.PROCEDIMENTO_CONCLUSO + "\")";				
				}

				if(homePageBean.getDomandaOrderTypeEnum() == null)
					homePageBean.setDomandaOrderTypeEnum(DomandaOrderTypeEnum.NumeroProcedimentoCrescente);
				documento = FolderDao.findRegioneFolder(xwService, extraQuery, filtroSulloStato, filtroSulTitolare, filtrodataCreazioneDa, filtrodataCreazioneA, filtrodataInvioDomandaDa, filtrodataInvioDomandaA, filtrodataConclusioneDa, filtrodataConclusioneA, filtroTipoProcedimento, filtroOggettoDomanda, domandeClientId, homePageBean.getDomandaOrderTypeEnum());
				folders.setContents(documento);		
				
			}
			catch (Exception e) {
				//nessun procedimento visibile alla regione
			}
		}
		
		homePageBean.setFascicoli(folders);
	}
	
	public void firstPageTitolareProcedimenti(UserBean userBean, HomePageBean homePageBean, ExtraWayService xwService) throws Exception {
		if (homePageBean.getFascicoli().isCanFirst()) {
			XMLDocumento document = xwService.firstTitlePage();
			homePageBean.getFascicoli().setContents(document);			
		}
	}	
	
	public void prevPageTitolareProcedimenti(UserBean userBean, HomePageBean homePageBean, ExtraWayService xwService) throws Exception {
		if (homePageBean.getFascicoli().isCanPrev()) {
			XMLDocumento document = xwService.prevTitlePage();
			homePageBean.getFascicoli().setContents(document);			
		}
	}	
	
	public XWFolderTitlesBean getAllTitolareProcedimenti(UserBean userBean, HomePageBean homePageBean, ExtraWayService xwService) throws Exception {
		XMLDocumento document = xwService.allTitlePages();
		XWFolderTitlesBean xWFolderTitlesBean = new XWFolderTitlesBean();
		xWFolderTitlesBean.setContents(document);
		return xWFolderTitlesBean;
	}
	
	public void nextPageTitolareProcedimenti(UserBean userBean, HomePageBean homePageBean, ExtraWayService xwService) throws Exception {
		if (homePageBean.getFascicoli().isCanNext()) {
			XMLDocumento document = xwService.nextTitlePage();
			homePageBean.getFascicoli().setContents(document);			
		}
	}

	public void lastPageTitolareProcedimenti(UserBean userBean, HomePageBean homePageBean, ExtraWayService xwService) throws Exception {
		if (homePageBean.getFascicoli().isCanLast()) {
			XMLDocumento document = xwService.lastTitlePage();
			homePageBean.getFascicoli().setContents(document);			
		}
	}	
	
	@SuppressWarnings("unchecked")
	public void loadTipoProcTempl(UserBean userBean, HomePageBean homePageBean, TipoProcTemplDao tipoProcTemplDao){
		if (userBean.isTITOLARE() || userBean.isOPERATORE_TITOLARE() || userBean.isOPERATORE_TITOLARE_IN_LETTURA() || userBean.isREGIONE()) {
			homePageBean.setTipiProcL((List<TipoProcTempl>) tipoProcTemplDao.findAll());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void loadDirezioniTempl(UserBean userBean, HomePageBean homePageBean){
		if (userBean.isREGIONE() || userBean.isVERIFICATORE()) {
			homePageBean.setDirezioniTempl((List<DirezioneTempl>) direzioneTemplDao.findAll());
		}
	}

	public void checkUdo(Map<String,String> checkM, UdoUoForList udo, boolean checked) {
		if(checked) {
			//Aggiungo la selezione
			if(udo.isUdo()) {
				//memorizzo anche UoModel.ClientId e seleziono anche la UO se non selezionata
				checkM.put(udo.getClientId(), udo.getUdoModel().getUoModel().getId().getClientid());
				if (!checkM.containsKey(udo.getUdoModel().getUoModel().getId().getClientid()))
					checkM.put(udo.getUdoModel().getUoModel().getId().getClientid(), "");
			}
			else 
				checkM.put(udo.getClientId(), "");			
		} else {
			//rimuovo la selezione se presente
			if (checkM.containsKey(udo.getClientId())) {
				if(!udo.isUdo()) {
					//sto rimuovendo una UO rimuovo tutte le udo della UO in rimozione
					for(Iterator<Map.Entry<String, String>> it = checkM.entrySet().iterator(); it.hasNext(); ) {
						Map.Entry<String, String> keyVal = it.next();
						if(keyVal.getValue().equals(udo.getClientId()))
							it.remove();
					}
				}
				checkM.remove(udo.getClientId());
			}			
		}
		
	}

	public void checkAllUdo(HomePageBean homePageBean) {
		for (UdoUoForList udoUoFL:homePageBean.getUdoUoL()) {
			checkUdo(homePageBean.getCheckM(), udoUoFL, homePageBean.isAllChecked());
		}
	}
	
	public void setActiveRowIndex(HomePageBean homePageBean, int index, boolean changeCheck, boolean checked) {
		homePageBean.setActiveRowIndex(index);

		if (changeCheck) {
			log.debug("Valore di checked: " + checked);
			checkUdo(homePageBean.getCheckM(), homePageBean.getUdoUoL().get(index), checked);
		}

	}
	
	@SuppressWarnings("unchecked")
	public void creaFascicoliTitolare(MessageContext messageContext, HomePageBean homePageBean, ExtraWayService xwService, boolean fromProgressBar) throws Exception {
		List<TitolareModel> titolareL = (List<TitolareModel>)titolareModelDao.findAll(); 
		
		JobBean jobBean = null;
		if (!fromProgressBar) { //la richiesta proviene dal pulsante premuto dall'utente -> si crea il job
			jobBean = new JobBean("h", titolareL.size());
			homePageBean.setJobBean(jobBean);
		}
		else { // la richiesta proviene da progressBar -> si recupera il job
			jobBean = homePageBean.getJobBean();
		}
		
		TitolareModel titolareModel = titolareL.get(jobBean.getCurrent());
		
		//check se fascicolo titolare esite
		XMLDocumento titolareFolder = FolderDao.findTitolareFolder(xwService, titolareModel);
		if (titolareFolder == null)
			titolareFolder = FolderDao.insertTitolareFolder(xwService, titolareModel);
		String numFasc = titolareFolder.getAttributeValue("//fascicolo/@numero");
		XMLDocumento titolareSubFolder = FolderDao.findAttiSubFolder(xwService, numFasc);
		if (titolareSubFolder == null)
			titolareSubFolder = FolderDao.insertTitolareAttiSubFolder(xwService, numFasc, titolareModel);
		
		//update stato job
		jobBean.setCurrent(jobBean.getCurrent() + 1);
			
		//se il lavoro e' finito si elimina il job
		if (jobBean.isCompleted()) {
			homePageBean.setJobBean(null);
			
			MessageBuilder mB = new MessageBuilder();
			messageContext.addMessage(mB.error().source("messages").defaultText("Operazione completata con successo").build());			
		}

	}

	public void creaSottoFascicoliEvidenze(MessageContext messageContext, HomePageBean homePageBean, ExtraWayService xwService, boolean fromProgressBar) throws Exception {
		XMLDocumento allFolderDomandeTitlesDocument = FolderDao.findAllDomandaFolder(xwService);
		if (allFolderDomandeTitlesDocument == null) {
			MessageBuilder mB = new MessageBuilder();
			messageContext.addMessage(mB.error().source("messages").defaultText("Nessun fascicolo di domanda trovato").build());
			return;
		}
		
		List<Element> itemsL = allFolderDomandeTitlesDocument.selectNodes("//Item");
				
		JobBean jobBean = null;
		if (!fromProgressBar) { //la richiesta proviene dal pulsante premuto dall'utente -> si crea il job
			jobBean = new JobBean("creaSottofascEvidenze", itemsL.size());
			homePageBean.setJobBean(jobBean);
		}
		else { // la richiesta proviene da progressBar -> si recupera il job
			jobBean = homePageBean.getJobBean();
		}
		
		Element itemEl = itemsL.get(jobBean.getCurrent());
        String value = itemEl.attributeValue("value");
        value = value.substring(value.indexOf("|epn|") + 5); //elimino la parte preliminare del titolo (comprende sort e epn)
        Vector<String> titleV = Text.split(value, "|");       
		String numeroFascicolo = titleV.get(7);		
		
		//si crea il sottofascicolo delle evidenze se non è già presente
		if (FolderDao.findEvidenzeSubFolder(xwService,  numeroFascicolo) == null)
			FolderDao.insertDomandaEvidenzeSubFolder(xwService, numeroFascicolo);
		
		//update stato job
		jobBean.setCurrent(jobBean.getCurrent() + 1);
			
		//se il lavoro e' finito si elimina il job
		if (jobBean.isCompleted()) {
			homePageBean.setJobBean(null);
			
			MessageBuilder mB = new MessageBuilder();
			messageContext.addMessage(mB.error().source("messages").defaultText("Operazione completata con successo").build());			
		}

	}
	
	
	public boolean validateFormFormCreazioneDomandaTitolare(MessageContext messageContext, HomePageBean homePageBean, boolean isModify) {
		MessageBuilder mB = new MessageBuilder();
		
		boolean errors = false;
		
		//nessun procedimento selezionato
		if (!isModify && homePageBean.getTipoProcSelected().length() == 0) {
			messageContext.addMessage(mB.error().source("tipoProcSelected").defaultText("Selezionare una tipologia di procedimento").build());
			messageContext.addMessage(mB.error().source("messages").defaultText("Selezionare una tipologia di procedimento").build());
			errors = true;
		}
		
		//nessun tipo domanda selezionato
		if (!isModify && homePageBean.getOggettoDomanda().length() == 0) {
			messageContext.addMessage(mB.error().source("oggettoDomanda").defaultText("Selezionare un tipo domanda").build());
			messageContext.addMessage(mB.error().source("messages").defaultText("Selezionare un tipo domanda").build());
			errors = true;
		}

		//nessuna UDO selezionato
		if (homePageBean.getCheckM().isEmpty()) {
			messageContext.addMessage(mB.error().source("messages").defaultText("Selezionare almeno una Unità Operativa o una Unità di Offerta").build());
			errors = true;
		}		
		
		return !errors;
	}
	
	/*private List<UdoModel> getAllCheckedUdo(HomePageBean homePageBean, UdoModelDao udoModelDao) {
		List<UdoModel> checkedU = new ArrayList<UdoModel>();
		List<UdoModel> allUdoL = (List<UdoModel>)udoModelDao.findValidateByTitolareSenzaProcedimentiInCorso(homePageBean.getTitolareModel(), null, null, null, null, null, null, null, null);
		
		for (UdoModel udo:allUdoL) {
			if (homePageBean.getCheckM().containsKey(udo.getClientid()))
				checkedU.add(udo);
		}
		return checkedU;
	}*/

	private List<UdoUoForList> getAllCheckedUdoUoForList(HomePageBean homePageBean, UdoModelDao udoModelDao) {
		List<UdoUoForList> checkedU = new ArrayList<UdoUoForList>();
		
		List<UoModel> listUoModel = uoModelDao.findValidateByTitolareEDenominazioneSenzaProcedimentiInCorso(homePageBean.getTitolareModel(), null);
		log.debug("Ricerca delle uo per titolare, Uo trovate: " + listUoModel.size());
		UdoUoForList udoUoForList = null; 
		for(UoModel uoModel : listUoModel) {
			udoUoForList = new UdoUoForList(uoModel);
			if (homePageBean.getCheckM().containsKey(udoUoForList.getClientId())) {
				checkedU.add(udoUoForList);
				for(UdoModel udoModel : uoModel.getUdoModels()) {
					udoUoForList = new UdoUoForList(udoModel);
					if(homePageBean.getCheckM().containsKey(udoUoForList.getClientId()))
						checkedU.add(udoUoForList);
				}
			}
		}
				
		log.debug("Elenco Uo e Udo trovate: " + checkedU.size());
		return checkedU;
	}

	public WebflowSafeString creaDomandaTitolare(MessageContext messageContext, HomePageBean homePageBean, ExtraWayService xwService, UserBean utenteBean) throws Exception {
		return creaEditDomandaTitolare(messageContext, homePageBean, xwService, utenteBean, null);
	}
	
	public WebflowSafeString updateDomaTit(MessageContext messageContext, HomePageBean homePageBean, ExtraWayService xwService, UserBean utenteBean, ShowFolderPageBean showFolderPageBean) throws Exception {
		homePageBean.setOggettoDomanda(showFolderPageBean.getFascicolo().getOggettoDomanda());
		return creaEditDomandaTitolare(messageContext, homePageBean, xwService, utenteBean, showFolderPageBean.getFascicolo().getDomandaClienId());
	}

	public WebflowSafeString inserisciRequisitiGeneraliAndUO(MessageContext messageContext, ExtraWayService xwService, UserBean utenteBean, ShowFolderPageBean showFolderPageBean) throws Exception {
		return creaEditDomandaTitolare(messageContext, xwService, utenteBean, showFolderPageBean.getFascicolo().getDomandaClienId(), utilService.TIPO_DOMANDA_COMPLESSIVA, null, null, true, true);
	}
	
	private WebflowSafeString creaEditDomandaTitolare(MessageContext messageContext, HomePageBean homePageBean, ExtraWayService xwService, 
			UserBean utenteBean, String domandaClientId) throws Exception {
		return this.creaEditDomandaTitolare(messageContext, xwService, utenteBean, domandaClientId, homePageBean.getOggettoDomanda(), homePageBean.getTipoProcSelected(), homePageBean.getCheckM(), false, false);
	}
	
	private WebflowSafeString creaEditDomandaTitolare(MessageContext messageContext, ExtraWayService xwService, 
			UserBean utenteBean, String domandaClientId, 
			String tipoDomanda, String idTipoProc, Map<String, String> checkM, boolean insertRequisitiGenerali, boolean insertRequisitiUo) throws Exception {
		try {
			boolean insertRequisitiGeneraliOrUo = insertRequisitiGenerali || insertRequisitiUo;
			//prendo il titolare
			TitolareModel titolare = null;
			if (utenteBean.isTITOLARE()) {
				titolare = titolareModelDao.findByUtenteModel(utenteBean.getUtenteModel());
			}
			else if (utenteBean.isOPERATORE_TITOLARE()) {
				titolare = titolareModelDao.findOne(utenteBean.getTitolareClientId());
			}
			
			DomandaInst domanda = null;
			if(domandaClientId == null) {
				//creo la nuova domanda 
				domanda = new DomandaInst();
				domanda.setAnagraficaUtenteModel(utenteBean.getUtenteModel().getAnagraficaUtenteModel());
				domanda.setTipoProcTempl((TipoProcTempl)tipoProcTemplDao.findById(new BigDecimal(idTipoProc)));
				UUID idOne = UUID.randomUUID();
				String uuid = idOne.toString().toUpperCase();
				domanda.setClientid(uuid);
				domanda.setDisabled("N");
				domanda.setTitolareModel(titolare);
				domanda.setStato(DomandaInstDao.STATO_BOZZA);
		    	domanda.setRequisitoInsts(new ArrayList<RequisitoInst>());
	
				domandaInstDao.save(domanda);
			}
			else {
				//carico la domanda
				domanda = domandaInstDao.getDomandaInstByClientId(domandaClientId);
			}
			if((!insertRequisitiGeneraliOrUo) &&
					(tipoDomanda == null || tipoDomanda.isEmpty())
					&& utilService.isGestioneRequisitiDiversificatiPerTipoDomandaActiveForDomanda(domanda)) {
				log.error("Impossibile creare o modificare una domanda senza il Tipo domanda se attiva la Gestione dei Requisiti Diversificati per Tipo domanda");
				throw new Exception("Impossibile creare o modificare una domanda senza il Tipo domanda se attiva la Gestione dei Requisiti Diversificati per Tipo domanda");
			}
			
			if(insertRequisitiGeneraliOrUo) {
				//devo caricare i clientid di tutte le UoModel e UdoModel delle UoInst e UdoInst della domanda
				checkM = new HashMap<String, String>();
				for(UoInst uoInst : domanda.getUoInsts()) {
					checkM.put(uoInst.getUoModelClientid(), "");
				}
				for(UdoInst udoInst : domanda.getUdoInsts()) {
					checkM.put(udoInst.getUdoModelClientid(), udoInst.getUdoModelUoModelClientid());
				}
			}
			//Abbiamo i clientId delle Uo e delle Udo in homePageBean.getCheckM() carichiamo i dati usando questi
			//senza fare una ricerca delle udo per poi ricaricarle vedi udoLazi, l'ideaale sarebbe poter caricare la udo/uo 
			//già con le liste che interessano per creare le relative UdoInst/UoInst 
			List<String> listAllClassifInsertedClientid = new ArrayList<String>();
			for(Map.Entry<String, String> kv : checkM.entrySet()) {
				if(kv.getValue().isEmpty()) {
					//UoModel
					createUoInstByUoModel(kv.getKey(), domanda, tipoDomanda, insertRequisitiUo);
				} else {
					//UdoModel
					createUdoInstByUdoModel(kv.getKey(), domanda, tipoDomanda, insertRequisitiGenerali, insertRequisitiUo, listAllClassifInsertedClientid);
				}
			}
			
			//In creazione domanda per ogni UoModel viene impostato
			//uoModel.setProcedimentoInCorso("S"); solo se non è presente nessuna UdoModel validata della UoModel con udoModel.getProcedimentoInCorso() == "S"; 
			//in modo che la stessa UoModel non venga agganciata ad un'altra domanda
			if(!insertRequisitiGeneraliOrUo) {
				for(Map.Entry<String, String> kv : checkM.entrySet()) {
					if(kv.getValue().isEmpty()) {
						//UoModel
						setProcedimentoInCorso(kv.getKey());
					}
				}
			}
			
			if(utilService.isGestioneListeRequisitiGeneraliSRActiveForDomanda(domanda)) {
				if(utilService.isActiveInsertRequisitiGeneraliAziendaliForDomanda(domanda, tipoDomanda)) {
					if(domandaClientId == null || insertRequisitiGenerali) {
						//Domada nuova inserisco eventuali requisiti generali SR
						//li inserisco anche in caso di domanda in modifica e insertRequisitiGeneraliAndUo=true (Tipo domanda da Parziale a Completa)
						if(domanda.getTitolareModel().getTipoTitolareTempl() != null) {
							boolean trovataListaTipoTitOnly = false;
							//Potrebbero esserci dei requisiti generali SR da gestire
							for(BindListaRequTipoTitClassTit blrs : domanda.getTipoProcTempl().getBindListaRequTipoTitClassTits()) {
								if(blrs.getClassificazioneTempl() == null &&
									blrs.getTipoTitolareTempl().getClientid().equals(domanda.getTitolareModel().getTipoTitolareTempl().getClientid())) {
									utilService.addRequisitiSrToDomandaInst(domanda, blrs);
									trovataListaTipoTitOnly = true;
								}
							}
							if(!trovataListaTipoTitOnly && domanda.getTitolareModel().getClassificazioneTempls() != null && !domanda.getTitolareModel().getClassificazioneTempls().isEmpty()) {
								for(BindListaRequTipoTitClassTit blrs : domanda.getTipoProcTempl().getBindListaRequTipoTitClassTits()) {
									if(blrs.getClassificazioneTempl() != null && blrs.getTipoTitolareTempl().getClientid().equals(domanda.getTitolareModel().getTipoTitolareTempl().getClientid()) &&
											domanda.getTitolareModel().getClassificazioneTempls().contains(blrs.getClassificazioneTempl())) {
										utilService.addRequisitiSrToDomandaInst(domanda, blrs);
									}
								}
							}
						}
						if(insertRequisitiGenerali) {
							utilService.aggiornaRequisitiSrDomanda(domanda, false);
						} else {
							utilService.aggiornaRequisitiSrDomanda(domanda, true);
						}
					} else {
						//Domanda in modifica 
						utilService.aggiornaRequisitiSrDomanda(domanda, false);
					}
				}
			}
			
			//Se non ho Udo nella domanda devo inserire i requisiti della domanda usando il campo DirezioneTempl del Titolare e se:
			//DirezioneTempl = 'Sanitario e Salute mentale' allora si utilizzano solo requisiti generali sanitari
			//DirezioneTempl = 'Socio Sanitario' allora si utilizzano solo i requisiti generali sociali
			//DirezioneTempl = 'Sociale' allora si utilizzano solo i requisiti generali sociali
			if(domanda.getUdoInsts().size() == 0 && utilService.isActiveInsertRequisitiGeneraliAziendaliForDomanda(domanda,tipoDomanda)) {
				//Potrebbe essere 
				if(domandaClientId == null || insertRequisitiGenerali) {
					//un inserimento con solo UoInst - aggiungo la lista requisiti
			    	if(titolare.getDirezioneTempl().getClassificazioneUdoTempls() != null && titolare.getDirezioneTempl().getClassificazioneUdoTempls().size() > 0) {
				    	List<BindListaRequClassUdo> listBindListaRequClassUdo = domanda.getTipoProcTempl().getBindListaRequClassUdos();
			    		ClassificazioneUdoTempl classUdoTempl = titolare.getDirezioneTempl().getClassificazioneUdoTempls().get(0);
		    			for(BindListaRequClassUdo bindListaRequClassUdo:listBindListaRequClassUdo) {
		    				if(bindListaRequClassUdo.getClassificazioneUdoTempl().getClientid().equals(classUdoTempl.getClientid())) {
		    					utilService.addRequisitiToDomandaInst(domanda, bindListaRequClassUdo);
		    				}
		    			}
			    	} else {
			    		log.error("Impossibile ricavare la classificazione Udo della direzione del titolare per inserire i requisiti della domanda con clientid: " + domanda.getClientid());
			    	}
				} /*else {
					//oppure una modifica con aggiunta di sole UoInst
					//Visto che non e' possibile modificare il titolare e il tipo procedimento la lista deve essere gia' corretta
				}*/
			}
			
			//Controllo se esistono delle UoInst che non hanno UdoInst figlie perche in tal caso a tali UoInst vanno aggiunti i Requisiti
			if(utilService.isActiveInsertRequisitiUoForDomanda(domanda,tipoDomanda)) {
				boolean addReqUoInstSenzaUdo = false;
				for(UoInst uoInst : domanda.getUoInsts()) {
					List<UdoInst> udoInstFiglie = domanda.getUdoInstFiglie(uoInst);
					if(udoInstFiglie.size() == 0) {
						addReqUoInstSenzaUdo = false;
						if(domandaClientId == null || insertRequisitiUo) {
							//domanda nuova aggiungo i requisiti
							addReqUoInstSenzaUdo = true;
						} else {
							//modifica di domanda esistente
							//La uoInst potrebbe essere:
							//un nuovo inserimento
							//una UoInst gia' presente prima della modifica
							if(uoInst.getRequisitoInsts() == null || uoInst.getRequisitoInsts().size() == 0) {
								addReqUoInstSenzaUdo = true;
							}
						}
						
						if(addReqUoInstSenzaUdo) {
					    	if(titolare.getDirezioneTempl().getClassificazioneUdoTempls() != null && titolare.getDirezioneTempl().getClassificazioneUdoTempls().size() > 0) {
					    		ClassificazioneUdoTempl classUdoTempl = titolare.getDirezioneTempl().getClassificazioneUdoTempls().get(0);
						    	List<BindUoProcLista> listBindUoProcLista = domanda.getTipoProcTempl().getBindUoProcListas();
						    	uoInst.setRequisitoInsts(new ArrayList<RequisitoInst>());
					    		for(BindUoProcLista bindUoProcLista:listBindUoProcLista) {
					    			if(bindUoProcLista.getId().getSaluteMentale().equals(ClassificazioneUdoTemplSaluteMentaleDistinctInfo.SALUTE_MENTALE_NO) &&
					    					bindUoProcLista.getClassificazioneUdoTempl().getClientid().equals(classUdoTempl.getClientid())) {
					    				utilService.addRequisitiToUoInst(uoInst, bindUoProcLista, domanda);
					    			}
					    		}
					    	} else {
					    		log.error("Impossibile ricavare la classificazione Udo della direzione del titolare per inserire i requisiti delle UoInst senza UdoInst della domanda con clientid: " + domanda.getClientid());
					    	}
						}
					}
				}
			}
	
			String idUnitSub = null;
			if(domandaClientId == null) {
				//inserimento fascicolo della domanda (sottofascicolo del titolare)
				XMLDocumento subDocument = FolderDao.insertTitolareProcSubFolder(xwService, titolare, domanda, tipoDomanda);
				idUnitSub = subDocument.getAttributeValue("/Response/Document/@idIUnit");
		
				//inserimento fascicolo delle evidenze (sottofascicolo della domanda)
				String numFascDomanda = subDocument.getAttributeValue("//fascicolo/@numero");
				FolderDao.insertDomandaEvidenzeSubFolder(xwService, numFascDomanda);
			} else {
				//devo caricare il fascicolo della domanda e ricavare "/Response/Document/@idIUnit"
				XMLDocumento subDocument = FolderDao.findFolderByDomandaClientId(xwService, domandaClientId);
				idUnitSub = subDocument.getAttributeValue("/Response/Document/@idIUnit");
				
				if(insertRequisitiGeneraliOrUo) {
					domandaInstDao.getEntityManager().refresh(domanda);
					for(UoInst uoInst : domanda.getUoInsts()) {
						uoInstDao.getEntityManager().refresh(uoInst);
					}
				}
			}
		
			return new WebflowSafeString(idUnitSub);
		} catch (Exception ex) {
			log.error(ex);
			throw ex;
		}
	}
	
	private void createUoInstByUoModel(String uoModelClientId, DomandaInst domanda, String tipoDomanda, boolean insertRequisitiUo) {
		//creo la UoInst solo se non e' gia' presente
		boolean createUoInst = true;
		if(insertRequisitiUo) {
			createUoInst = false;
		} else {
			if(domanda.getUoInsts() != null && domanda.getUoInsts().size() > 0) {
				for(UoInst uoInst : domanda.getUoInsts()) {
					if(uoInst.getUoModelClientid().equals(uoModelClientId)) {
						createUoInst = false;
						break;
					}
				}
			}
		}
		
		if(createUoInst || insertRequisitiUo) {
			UoInst uoInst = null;
			if(createUoInst) {
				//usare findByClientId per far si che jpa istanzi le liste
				UoModel uoModel = uoModelDao.findByClientId(uoModelClientId);
				
				uoInst = new UoInst();
				//uoInst.setUoModelClientid(uoModel.getClientid());
				uoInst.setOldUoModelClientid(uoModel.getClientid());
				uoInst.setProvenienzaUo(uoModel.getId().getProvenienza());
				uoInst.setIdUo(uoModel.getId().getId());
				uoInst.setTitolareModel(domanda.getTitolareModel());
				//uoInst.setDescr(uoModel.getDescr());
				uoInst.setTipoNodo(uoModel.getTipoNodo());
				uoInst.setDenominazione(uoModel.getDenominazione());
				//uoInst.setAnnotations(uoModel.getAnnotations());
				//uoInst.setCodiceCdr(uoModel.getCodiceCdr());
				UUID idOne = UUID.randomUUID();
				String uuid = idOne.toString().toUpperCase();
				uoInst.setClientid(uuid);
				uoInst.setDisabled("N");
			}
			if(insertRequisitiUo) {
				for(UoInst uoI : domanda.getUoInsts()) {
					if(uoI.getUoModelClientid().equals(uoModelClientId)) {
						uoInst = uoI;
						break;
					}
				}
			}
			
			ClassificazioneUdoTemplSaluteMentaleDistinctInfo clUdoTemplSMDistInfo = domanda.getClassificazioneUdoTemplSaluteMentaleDistinctInfoForUdoInstOfUoInst(uoInst);
			
			if(utilService.isActiveInsertRequisitiUoForDomanda(domanda,tipoDomanda)) {
				uoInst.setRequisitoInsts(new ArrayList<RequisitoInst>());
				for(BindUoProcLista bindUoProcLista: clUdoTemplSMDistInfo.getBindUoProcListaDaAggiungere(domanda.getTipoProcTempl().getBindUoProcListas())) {
					utilService.addRequisitiToUoInst(uoInst, bindUoProcLista, domanda);
				}
			}
			
			if(createUoInst) {
		    	//salvo uoInst
				domanda.addUoInst(uoInst);
				uoInstDao.save(uoInst);
			}
		}
	}
	
	private void setProcedimentoInCorso(String uoModelClientId) {
		/*
		In creazione domanda per ogni UoModel viene impostato
		uoModel.setProcedimentoInCorso("S"); solo se non è presente nessuna UdoModel validata della UoModel con 
		udoModel.getProcedimentoInCorso() == "N"; 
		in modo che la stessa UoModel non venga agganciata ad un'altra domanda
		 */
		UoModel uoModel = uoModelDao.findByClientId(uoModelClientId);
		boolean esisteUdoModelValidataSenzaProcedimentoInCorso = false;
    	for (UdoModel udoModel:uoModel.getUdoModels()) { 
    		if(udoModel.getValidata().equals("Y") && udoModel.getProcedimentoInCorso().equals("N")) {
    			esisteUdoModelValidataSenzaProcedimentoInCorso = true;
    			break;
    		}
    	}			
		
    	//salvo uoModel
    	if(!esisteUdoModelValidataSenzaProcedimentoInCorso) {
    		uoModel.setProcedimentoInCorso("S");
    		uoModelDao.saveProcedimentoInCorso(uoModel);
    	}
	}

	private void createEdificioInstByEdificioTempl(EdificioTempl edificioTempl, DomandaInst domanda, List<ListaRequisitiTempl> listeRequisitiEdifici, SedeOperModel sedeOperModel) {
		if(!utilService.isEdificioInstActiveForDomanda(domanda) || listeRequisitiEdifici.isEmpty() || edificioTempl == null)
			return;
		
		// chiamata da createUdoInstByUdoModel
		// edificioTempl edificio della udo 
		// sedeOperModel SedeOperModel della udo

		boolean found = false;
		if(edificioTempl.getStrutturaModel() == null // e' l'edificio 00 COINCIDE 
			&& sedeOperModel != null // eventuale sede della udoModel  
			) {
			for(EdificioInst ed : domanda.getEdificioInsts()) {
				if(ed.getEdificioTemplClientid().equals(edificioTempl.getClientid())
					&&
					ed.getSedeOperModelClientid().equals(sedeOperModel.getClientid())) {
					found = true;
					break;
				}
			}			
		} else {
			for(EdificioInst ed : domanda.getEdificioInsts()) {
				if(ed.getEdificioTemplClientid().equals(edificioTempl.getClientid())) {
					found = true;
					break;
				}
			}
		}
		if(!found) {
			//Aggiungo l'edificio inst
			EdificioInst edificioInst = new EdificioInst();
			edificioInst.setClientid(UUID.randomUUID().toString().toUpperCase());
			edificioInst.setEdificioTemplClientid(edificioTempl.getClientid());
			edificioInst.setCodice(edificioTempl.getCodice());
			if(sedeOperModel != null) {
				edificioInst.setSedeOperModelClientid(sedeOperModel.getClientid());
				edificioInst.setSedeOperModelDenominazione(sedeOperModel.getDenominazione());
			}
			edificioInst.setNome(edificioTempl.getNome());
			edificioInst.setCfDiProprieta(edificioTempl.getCfDiProprieta());
			edificioInst.setFlagDiProprieta(edificioTempl.getFlagDiProprieta());
			edificioInst.setNomeDiProprieta(edificioTempl.getNomeDiProprieta());
			edificioInst.setCognomeDiProprieta(edificioTempl.getCognomeDiProprieta());
			edificioInst.setPivaDiProprieta(edificioTempl.getPivaDiProprieta());
			edificioInst.setRagioneSocialeDiProprieta(edificioTempl.getRagioneSocialeDiProprieta());
			
			//log.debug("Creazione EdificioInst, numero EdificioInst prima aggiunta: " + domanda.getEdificioInsts().size());		
			domanda.addEdificioInst(edificioInst);
			//log.debug("Creazione EdificioInst, numero EdificioInst dopo aggiunta: " + domanda.getEdificioInsts().size());
	    	//salvo edificioInst
	    	edificioInstDao.save(edificioInst);
		
	    	edificioInst.setRequisitoInsts(new ArrayList<RequisitoInst>());
	    	for(ListaRequisitiTempl listaRequisitiTempl : listeRequisitiEdifici) {
	    		for (BindListaRequisito bindListaRequisito : listaRequisitiTempl.getBindListaRequisitos()) {
	    			if("N".equals(bindListaRequisito.getAnnullato()) && "S".equals(bindListaRequisito.getValidato()) 
	    					&& "N".equals(bindListaRequisito.getRequisitoTempl().getAnnullato()) && "S".equals(bindListaRequisito.getRequisitoTempl().getValidato())) {
	    				RequisitoInst requisitoInst = utilService.getRequisitoInstByBindListaRequisito(bindListaRequisito, null, ClassificazioneUdoTemplSaluteMentaleDistinctInfo.SALUTE_MENTALE_NO, domanda);
	    				
		    			edificioInst.addRequisitoInst(requisitoInst);
						requisitoInstDao.save(requisitoInst);
	    			
	    			}		    			
	    		}
	    	}
		}
	}
	
	private void createStrutturaInstByStrutturaModel(StrutturaModel strutturaModel, DomandaInst domanda) {
		if(!utilService.isStrutturaInstActiveForDomanda(domanda))
			return;

		if(domanda.getTipoProcTempl().getListaRequisitiTemplStrutture()!= null) {
			boolean found = false;
			for(StrutturaInst si : domanda.getStrutturaInsts()) {
				if(si.getStrutturaModelClientid().equals(strutturaModel.getClientid())) {
					found = true;
					break;
				}
			}
			//Nothing to do if no requisito are in the strutture
			if(!found && (domanda.getTipoProcTempl().getListaRequisitiTemplStrutture() != null 
						&& !domanda.getTipoProcTempl().getListaRequisitiTemplStrutture().isEmpty())) {
				//Aggiungo la struttura inst
				StrutturaInst strutturaInst = new StrutturaInst();
				strutturaInst.setClientid(UUID.randomUUID().toString().toUpperCase());
				strutturaInst.setCodicePf(strutturaModel.getCodicePf());
				strutturaInst.setDenominazione(strutturaModel.getDenominazione());
				strutturaInst.setStrutturaModelClientid(strutturaModel.getClientid());
				
		    	strutturaInst.setRequisitoInsts(new ArrayList<RequisitoInst>());
		    	for(ListaRequisitiTempl listaRequisitiTempl : domanda.getTipoProcTempl().getListaRequisitiTemplStrutture()) {
		    		for (BindListaRequisito bindListaRequisito : listaRequisitiTempl.getBindListaRequisitos()) {
		    			if("N".equals(bindListaRequisito.getAnnullato()) && "S".equals(bindListaRequisito.getValidato()) 
		    					&& "N".equals(bindListaRequisito.getRequisitoTempl().getAnnullato()) && "S".equals(bindListaRequisito.getRequisitoTempl().getValidato())) {
		    				RequisitoInst requisitoInst = utilService.getRequisitoInstByBindListaRequisito(bindListaRequisito, null, ClassificazioneUdoTemplSaluteMentaleDistinctInfo.SALUTE_MENTALE_NO, domanda);
		    				
			    			strutturaInst.addRequisitoInst(requisitoInst);
							requisitoInstDao.save(requisitoInst);
		    			
		    			}		    			
		    		}
		    	}
		    	
		    	//Don't save if no RequisitoInst was added to the Struttura
		    	if(!strutturaInst.getRequisitoInsts().isEmpty()) {
		    		log.debug("Creazione StrutturaInst, numero StrutturaInst prima aggiunta: " + domanda.getStrutturaInsts().size());		
					domanda.addStrutturaInst(strutturaInst);
					log.debug("Creazione StrutturaInst, numero StrutturaInst dopo aggiunta: " + domanda.getStrutturaInsts().size());
			    	//salvo strutturaInst
					strutturaInstDao.save(strutturaInst);
		    	}
		    	//strutturaInstDao.save(strutturaInst);
			
			}
		}
	}

	private void createUdoInstByUdoModel(String udoModelClientId, DomandaInst domanda, String tipoDomanda, 
			boolean insertRequisitiGenerali, boolean insertRequisitiUo, List<String> listAllClassifInsertedClientid) {
		boolean insertRequisitiGeneraliOrUo = insertRequisitiGenerali || insertRequisitiUo;
		//usare findByClientId per far si che jpa istanzi le liste
		UdoModel udoModel = udoModelDao.findByClientId(udoModelClientId);
		UdoInst udoInst = null;

		if(insertRequisitiGeneraliOrUo) {
			for(UdoInst udoI : domanda.getUdoInsts()) {
				if(udoModelClientId.equals(udoI.getUdoModelClientid())) {
					udoInst = udoI;
					break;
				}
			}
		} else {
			udoInst = new UdoInst();
			//Modifica 07/10/2017 aggiunta requisiti EdificioTempl
			List<ListaRequisitiTempl> listeRequisitiEdifici = utilService.getListaRequisitiEdificioByTitolareTipoProcedimento(domanda.getTitolareModel(), domanda.getTipoProcTempl());
			if(udoModel.getEdificioTempl() != null && utilService.isEdificioInstActiveForDomanda(domanda) && !listeRequisitiEdifici.isEmpty()) {
				createEdificioInstByEdificioTempl(udoModel.getEdificioTempl(), domanda, listeRequisitiEdifici, udoModel.getSedeOperModel());			
			}
			
			//Modifica 22/06/2017 aggiunta requisiti StrutturaModel
			createStrutturaInstByStrutturaModel(udoModel.getSedeOperModel().getStrutturaModel(), domanda);
			
			//Modifica 22/06/2017 aggiunta requisiti StrutturaModel
			udoInst.setStrutturaModelClientid(udoModel.getSedeOperModel().getStrutturaModel().getClientid());
			
			//Modifica 07/10/2017 aggiunta requisiti EdificioTempl
			if(udoModel.getEdificioTempl() != null && utilService.isEdificioInstActiveForDomanda(domanda) && !listeRequisitiEdifici.isEmpty()) {
				udoInst.setEdificioTemplClientid(udoModel.getEdificioTempl().getClientid());
			}

			//modificato per storicizzazione 23/10/2014
			/*
			//Questo è quello corretto 
			udoModel.addUdoInst(udoInst);
			//INVECE DI
			//udoInst.setUdoModel(udoModel);
			*/
			udoInst.setUdoModelClientid(udoModel.getClientid());
			//udoInst.setUdoModelUoModelClientid(udoModel.getUoModel().getClientid());
			udoInst.setOldUdoModelUoModelClientid(udoModel.getUoModel().getClientid());
			udoInst.setProvenienzaUo(udoModel.getUoModel().getId().getProvenienza());
			udoInst.setIdUo(udoModel.getUoModel().getId().getId());
			udoInst.setTipoNodoUo(udoModel.getUoModel().getTipoNodo());
			
			udoInst.setDescr(udoModel.getDescr());
			udoInst.setAnnotations(udoModel.getAnnotations());
			udoInst.setTipoUdoTempl(udoModel.getTipoUdoTempl());
			if(udoModel.getEdificioTempl() != null) {
				udoInst.setStabilimento(udoModel.getEdificioTempl().getCodice() + " - " + udoModel.getEdificioTempl().getNome());
			}
			udoInst.setBlocco(udoModel.getBlocco());
			udoInst.setPiano(udoModel.getPiano());
			udoInst.setProgressivo(udoModel.getProgressivo());
		
			//Forse andranno presi dal più recente della lista udoModel.getStato()
			boolean trovatoDaProcedimento = false;
			BigDecimal idStatoUdo = null;
			Date scadenza = null;
			String stato = null;
			for(StatoUdo statoUdo : udoModel.getStatoUdos()) {
				if(!trovatoDaProcedimento && "S".equals(statoUdo.getInsDaProcedimento())) {
					//Questo e' stato inserito lato AuAc quindi prendo l'ultimo per idStatoUdo
					trovatoDaProcedimento = true;
					idStatoUdo = null;
					scadenza = null;
					stato = null;
				} 
				
				if (trovatoDaProcedimento) {
					//prendo l'ultimo inserito fra quelli da procedimento
					if("S".equals(statoUdo.getInsDaProcedimento()) && (idStatoUdo == null || statoUdo.getIdStatoUdo().compareTo(idStatoUdo) > 0) ) {
						idStatoUdo = statoUdo.getIdStatoUdo();
						scadenza = statoUdo.getScadenza();
						stato = statoUdo.getStato();
					}
				} else {
					//Modifica del 09/10/2017 per errore 
	//				at java.util.Date.getMillisOf(Unknown Source)
	//				at java.util.Date.after(Unknown Source)
	//				at it.tredi.auac.service.MainService.createUdoInstByUdoModel(MainService.java:QUI)				
					if(idStatoUdo == null || (statoUdo.getScadenza() != null && (scadenza == null || statoUdo.getScadenza().after(scadenza)))) {
						idStatoUdo = statoUdo.getIdStatoUdo();
						scadenza = statoUdo.getScadenza();
						stato = statoUdo.getStato();
					}
				}
			}
			udoInst.setScadenza(scadenza);
			udoInst.setStato(stato);

			udoInst.setIdUnivoco(udoModel.getIdUnivoco());
			udoInst.setTipoUdoTemplDescr(udoModel.getTipoUdoTempl().getDescr());
			udoInst.setTipoUdoTemplTipoUdo22TemplCodiceUdo(udoModel.getTipoUdoTempl().getTipoUdo22Templ().getCodiceUdo());
			udoInst.setTipoUdoTemplTipoUdo22TemplNomeCodiceUdo(udoModel.getTipoUdoTempl().getTipoUdo22Templ().getNomeCodiceUdo());
		}
		
		if(!insertRequisitiGeneraliOrUo) {
			//in caso di modifica con insertRequisitiGeneraliOrUo questi vengono inseriti gestendo le Uo
			//L'aggiunta di questa UdoInst potrebbe richiedere l'aggiunta di Requisiti alla UoInst padre
			//per sapere se vanno aggiunti i requisiti controllo, OVVIAMENTE PRIMA DI AGGIUNGERLA, se fra le UdoInst gia' presenti e figlie del padre non ve ne siano della stessa Classificazione e SaluteMentale
			if(utilService.isActiveInsertRequisitiUoForDomanda(domanda,tipoDomanda)) {
				UoInst uoInstPadre = domanda.getUoInstPadre(udoInst);
				if(uoInstPadre != null) {
					ClassificazioneUdoTemplSaluteMentaleDistinctInfo clUdoTemplSMDistInfo = domanda.getClassificazioneUdoTemplSaluteMentaleDistinctInfoForUdoInstOfUoInst(uoInstPadre);
					
					if(!clUdoTemplSMDistInfo.containsInfo()) {
						//Prima UdoInst della UoInst potrebbe essere una modifica quindi se la UoInst ha dei requisiti li devo cancellare
						if(uoInstPadre.getRequisitoInsts().size() > 0) {
							for(Iterator<RequisitoInst> iterator = uoInstPadre.getRequisitoInsts().iterator(); iterator.hasNext();) {
								RequisitoInst reqDel = iterator.next();
								iterator.remove();
								reqDel.setUoInst(null);
								//uoInstPadre.removeRequisitoInst(reqDel);
								requisitoInstDao.delete(reqDel);
							}
						}
					}
	
					for(BindUoProcLista bindUoProcLista:clUdoTemplSMDistInfo.getBindUoProcListaDaAggiungerePerNuovaUdoInst(domanda.getTipoProcTempl().getBindUoProcListas(), udoInst)) {
						utilService.addRequisitiToUoInst(uoInstPadre, bindUoProcLista, domanda);
					}
				}
			}
		}
		
		//TODO questo va gestito è complesso
		//L'aggiunta di questa UdoInst potrebbe richiedere l'aggiunta di Requisiti alla DomadaInst stessa
		//per sapere se vanno aggiunti i requisiti controllo, OVVIAMENTE PRIMA DI AGGIUNGERLA, se fra le UdoInst gia' presenti nella domanda non ve ne siano della stessa Classificazione
		if(utilService.isActiveInsertRequisitiGeneraliAziendaliForDomanda(domanda, tipoDomanda)) {
			List<ClassificazioneUdoTempl> listAllClassif = domanda.getClassificazioneUdoTemplForAllUdoInst();
			boolean aggiungiRequisitiDomanda = true;
			if(insertRequisitiGenerali) {
				//List<String> listAllClassifInsertedClientid;
				for(String classifClientid : listAllClassifInsertedClientid) {
					if(classifClientid.equals(udoInst.getTipoUdoTempl().getTipoUdo22Templ().getClassificazioneUdoTempl().getClientid())) {
						aggiungiRequisitiDomanda = false;
						break;
					}
				}
				if(aggiungiRequisitiDomanda) {
					listAllClassifInsertedClientid.add(udoInst.getTipoUdoTempl().getTipoUdo22Templ().getClassificazioneUdoTempl().getClientid());
				}
			} else {
				if(domanda.getUdoInsts().size() == 0) {
					//Prima UdoInst della domanda potrebbe essere una modifica quindi se la domanda ha dei requisiti li devo cancellare
					if(domanda.getRequisitoInsts().size() > 0) {
						for(Iterator<RequisitoInst> requisitoInstIter = domanda.getRequisitoInsts().iterator(); requisitoInstIter.hasNext();) {
							RequisitoInst requisitoInst = requisitoInstIter.next();
							if(requisitoInst.getIsSr() == BooleanEnum.FALSE) {
								requisitoInstIter.remove();
								//domanda.removeRequisitoInst(requisitoInst);
								requisitoInst.setDomandaInst(null);
								requisitoInstDao.delete(requisitoInst);
							}
						}
					}
				} else {
					for(ClassificazioneUdoTempl classif : listAllClassif) {
						if(classif.getClientid().equals(udoInst.getTipoUdoTempl().getTipoUdo22Templ().getClassificazioneUdoTempl().getClientid())) {
							aggiungiRequisitiDomanda = false;
							break;
						}
					}
				}
			}
			if(aggiungiRequisitiDomanda) {
		    	List<BindListaRequClassUdo> listBindListaRequClassUdo = domanda.getTipoProcTempl().getBindListaRequClassUdos();
	    		for(BindListaRequClassUdo bindListaRequClassUdo:listBindListaRequClassUdo) {
		    		if(bindListaRequClassUdo.getClassificazioneUdoTempl().getClientid().equals(udoInst.getTipoUdoTempl().getTipoUdo22Templ().getClassificazioneUdoTempl().getClientid())) {
		    			utilService.addRequisitiToDomandaInst(domanda, bindListaRequClassUdo);
		    		}
		    	}
			}
		}
		
		if(!insertRequisitiGeneraliOrUo) {
			//corretto
			log.debug("Creazione UdoInst, numero UdoInst prima aggiunta: " + domanda.getUdoInsts().size());		
			domanda.addUdoInst(udoInst);
			log.debug("Creazione UdoInst, numero UdoInst dopo aggiunta: " + domanda.getUdoInsts().size());
			//errato
			//udoInst.setDomandaInst(domanda);
			UUID idOne = UUID.randomUUID();
			String uuid = idOne.toString().toUpperCase();
			udoInst.setClientid(uuid);
			udoInst.setDisabled("N");
			
			//MODIFICA PER GESTIONE DATI REGIONALI
			//CodiciUlssTerritoriali codiceU = udoModel.getCodiciUlssTerritoriali();
			//udoInst.setCodiceUlssTerritoriale(codiceU.getNome());
			udoInst.setCodiceUlssTerritoriale(udoModel.getCodiceAziendaUlss());
			udoInst.setCodiceUlssPrecedente(udoModel.getCodiceUlssPrecedente());
			
			//MODIFICA PER GESTIONE DATI REGIONALI
			//CodiciStruttDenomin codiceS = udoModel.getCodiciStruttDenomin();
			//udoInst.setCodiceStruttDenomin(codiceS.getNome() + " - " + codiceS.getDenominazione());
			udoInst.setCodiceStruttDenomin(udoModel.getCodiceFlussoMinisteriale() + " - " + udoModel.getDescFlussoMinisteriale());
			udoInst.setDenominazioneUo(udoModel.getUoModel().getDenominazione());
			udoInst.setDenominazioneSede(udoModel.getSedeOperModel().getDenominazione());
			udoInst.setDenomStruttFisicaSede(udoModel.getSedeOperModel().getStrutturaModel().getDenominazione());
			udoInst.setDirSanitarioNome(udoModel.getDirSanitarioNome());
			udoInst.setDirSanitarioCogn(udoModel.getDirSanitarioCogn());
			udoInst.setDirSanitarioCfisc(udoModel.getDirSanitarioCfisc());
			udoInst.setFlagModulo(udoModel.getFlagModulo());
			udoInst.setWeek(udoModel.getWeek());
			String indirizzo = "";
			if(udoModel.getSedeOperModel().getToponimoStradale() != null)
				indirizzo += udoModel.getSedeOperModel().getToponimoStradale();
			if(udoModel.getSedeOperModel().getViaPiazza() != null && udoModel.getSedeOperModel().getViaPiazza().length() > 0)
				indirizzo += " " + udoModel.getSedeOperModel().getViaPiazza();
			if(udoModel.getSedeOperModel().getCivico() != null && udoModel.getSedeOperModel().getCivico().length() > 0)
				indirizzo += " " + udoModel.getSedeOperModel().getCivico();
			if(udoModel.getSedeOperModel().getCap() != null && udoModel.getSedeOperModel().getCap().length() > 0)
				indirizzo += " " + udoModel.getSedeOperModel().getCap();
			if(udoModel.getSedeOperModel().getComune() != null && udoModel.getSedeOperModel().getComune().length() > 0)
				indirizzo += " " + udoModel.getSedeOperModel().getComune();		
			if(udoModel.getSedeOperModel().getProvincia() != null && udoModel.getSedeOperModel().getProvincia().length() > 0)
				indirizzo += " " + udoModel.getSedeOperModel().getProvincia();		
			udoInst.setIndirizzoSede(indirizzo);
			udoInst.setTipoPuntoFisicoSede(udoModel.getSedeOperModel().getTipoPuntoFisico());
		
			udoInstDao.save(udoInst);
		
			Gson gson = new Gson();
			
			udoModel.getBrancaTempls().size();
			List<BrancaUdoInstInfo> brancheInfoList = new ArrayList<BrancaUdoInstInfo>();
			BrancaUdoInstInfo brancaInfo;		
			if(udoModel.getBrancaTempls().size() > 0) {
				for(BrancaTempl branca : udoModel.getBrancaTempls()) {
					brancaInfo = new BrancaUdoInstInfo();
					brancaInfo.setId(branca.getClientid());
					brancaInfo.setCodice(branca.getCodice());
					brancaInfo.setDescr(branca.getDescr());
					brancheInfoList.add(brancaInfo);
				}
			}
			if(brancheInfoList.size() > 0)
				udoInst.setBrancheInfo(gson.toJson(brancheInfoList));
			
			udoModel.getEstenstioneTempls().size();
			List<EstensioneUdoInstInfo> estensioneInfoList = new ArrayList<EstensioneUdoInstInfo>();
			EstensioneUdoInstInfo estensioneInfo;		
			if(udoModel.getEstenstioneTempls().size() > 0) {
				for(EstenstioneTempl estensTempl : udoModel.getEstenstioneTempls()) {
					estensioneInfo = new EstensioneUdoInstInfo();
					estensioneInfo.setId(estensTempl.getClientid());
					estensioneInfo.setNome(estensTempl.getNome());
					estensioneInfo.setDescr(estensTempl.getDescr());
					estensioneInfoList.add(estensioneInfo);
				}
			}
			if(estensioneInfoList.size() > 0)
				udoInst.setEstensioniInfo(gson.toJson(estensioneInfoList));
			
			udoModel.getBindUdoDisciplinas().size();
			List<DisciplinaUdoInstInfo> disciplineInfoList = new ArrayList<DisciplinaUdoInstInfo>();
			DisciplinaUdoInstInfo disciplinaInfo;		
			if(udoModel.getBindUdoDisciplinas().size() > 0) {
				for(BindUdoDisciplina bindUdoDisc : udoModel.getBindUdoDisciplinas()) {
					disciplinaInfo = new DisciplinaUdoInstInfo();
					if (bindUdoDisc.getDisciplinaTempl() != null) {
						disciplinaInfo.setId(bindUdoDisc.getDisciplinaTempl().getClientid());
						disciplinaInfo.setTipo("d");
						disciplinaInfo.setCodice(bindUdoDisc.getDisciplinaTempl().getCodice());
						disciplinaInfo.setDescr(bindUdoDisc.getDisciplinaTempl().getDescr());
						disciplinaInfo.setPostiLetto(bindUdoDisc.getPostiLetto());
						disciplinaInfo.setPostiLettoExtra(bindUdoDisc.getPostiLettoExtra());
						disciplinaInfo.setPostiLettoObi(bindUdoDisc.getPostiLettoObi());
						disciplinaInfo.setPostiLettoAcc(bindUdoDisc.getPostiLettoAcc());
						disciplinaInfo.setPostiLettoObiAcc(bindUdoDisc.getPostiLettoObiAcc());
						if(bindUdoDisc.getDisciplinaTempl().getAreaDiscipline() != null) {
							disciplinaInfo.setArea(bindUdoDisc.getDisciplinaTempl().getAreaDiscipline().getDenominazione());
							disciplinaInfo.setOrdineArea(bindUdoDisc.getDisciplinaTempl().getAreaDiscipline().getOrdine());
						}
						disciplinaInfo.setOrdineDisc(bindUdoDisc.getDisciplinaTempl().getOrdine());
						disciplinaInfo.setTipoPostiLetto(TipoPostiLettoEnum.ATTUATI);
						disciplineInfoList.add(disciplinaInfo);
					} else if (bindUdoDisc.getAmbitoTempl() != null) {
						disciplinaInfo.setId(bindUdoDisc.getAmbitoTempl().getClientid());
						disciplinaInfo.setTipo("a");
						disciplinaInfo.setDescr(bindUdoDisc.getAmbitoTempl().getDescr());					
						disciplinaInfo.setPostiLetto(bindUdoDisc.getPostiLetto());
						disciplinaInfo.setPostiLettoExtra(bindUdoDisc.getPostiLettoExtra());
						disciplinaInfo.setPostiLettoObi(bindUdoDisc.getPostiLettoObi());
						disciplinaInfo.setPostiLettoAcc(bindUdoDisc.getPostiLettoAcc());
						disciplinaInfo.setPostiLettoObiAcc(bindUdoDisc.getPostiLettoObiAcc());
						disciplineInfoList.add(disciplinaInfo);
					}
				}
			}
			if(disciplineInfoList.size() > 0)
				udoInst.setDisciplineInfo(gson.toJson(disciplineInfoList));
			
			udoModel.getPrestazioneTempls().size();
			List<PrestazioneUdoInstInfo> prestazioneInfoList = new ArrayList<PrestazioneUdoInstInfo>();
			PrestazioneUdoInstInfo prestazioneInfo;		
			if(udoModel.getPrestazioneTempls().size() > 0) {
				for(PrestazioneTempl prestTempl : udoModel.getPrestazioneTempls()) {
					prestazioneInfo = new PrestazioneUdoInstInfo();
					prestazioneInfo.setId(prestTempl.getClientid());
					prestazioneInfo.setNome(prestTempl.getNome());
					prestazioneInfo.setDescrizione(prestTempl.getDescrizione());
					prestazioneInfoList.add(prestazioneInfo);
				}
			}
			if(prestazioneInfoList.size() > 0)
				udoInst.setPrestazioniInfo(gson.toJson(prestazioneInfoList));
	
	
			
			udoModel.getFattProdUdoModels().size();
			List<FattProdUdoInstInfo> fatProdInfoList = new ArrayList<FattProdUdoInstInfo>();
			FattProdUdoInstInfo fattProdInfo;		
			if(udoModel.getFattProdUdoModels().size() > 0) {
				for(FattProdUdoModel fattoreProdModel : udoModel.getFattProdUdoModels()) {
					fattProdInfo = new FattProdUdoInstInfo();
					fattProdInfo.setId(fattoreProdModel.getClientid());
					fattProdInfo.setTipo(fattoreProdModel.getTipoFattoreProdTempl().getDescr());
					fattProdInfo.setTipologiaFattProd(fattoreProdModel.getTipoFattoreProdTempl().getTipologiaFattProd());
					fattProdInfo.setValore(fattoreProdModel.getValore());
					fattProdInfo.setValore2(fattoreProdModel.getValore2());
					fattProdInfo.setValore3(fattoreProdModel.getValore3());
					fatProdInfoList.add(fattProdInfo);
				}
			}
			if(fatProdInfoList.size() > 0)
				udoInst.setFattProdInfo(gson.toJson(fatProdInfoList));
			
			
			/*JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
			for (FattProdUdoModel fattoreProduttivoModel:udoModel.getFattProdUdoModels()) {
				arrayBuilder.add(Json.createObjectBuilder()
					         .add("tipo", fattoreProduttivoModel.getTipoFattoreProdTempl().getDescr())
					         .add("val", fattoreProduttivoModel.getValore())
					         .add("val2", fattoreProduttivoModel.getValore2()));
			
			}
			StringWriter stWriter = new StringWriter();
			JsonWriter jsonWriter = Json.createWriter(stWriter);
			jsonWriter.writeArray(arrayBuilder.build());
			jsonWriter.close();
			udoInst.setFattProdInfo(stWriter.toString());*/

		
		
			//Attualmente non storicizziamo
			/*udoLazi.getAttoModels().size();
			List<AttoModel> attiModel = udoLazi.getAttoModels();
			List<AttoInst> attiInst = new ArrayList<AttoInst>();
			AttoInst attoInst = null;
			for (AttoModel attoModel: attiModel) {
				attoInst = new AttoInst();
				attoInst.setTipoAtto(attoModel.getTipoAtto());
				attoInst.setTipoProcTempl(attoModel.getTipoProcTempl());
				attoInst.setTitolareModel(attoModel.getTitolareModel());
				attoInst.setAnno(attoModel.getAnno());
				attoInst.setNumero(attoModel.getNumero());
				idOne = UUID.randomUUID();
				uuid = idOne.toString().toUpperCase();
				attoInst.setClientid(uuid);
				attoInst.setBinaryAttachmentsAppl(attoModel.getBinaryAttachmentsAppl());
				attoInst.setInizioValidita(attoModel.getInizioValidita());
				attoInst.setFineValidita(attoModel.getFineValidita());
				attoInst.setDescr(attoModel.getDescr());
				attoInst.setAnnotations(attoModel.getAnnotations());
				attoInst.setDisabled("N");
				attoInstDao.save(attoInst);
				attiInst.add(attoInst);
			}
			if(attiInst.size() > 0)
				udo.setAttoInsts(attiInst);*/
		
			//creazione questionario requisiti (tabella REQUISITO_INST) per ogni UDO a partire dal modello (tabella REQUISITO_MODEL)
	    	List<BindTipo22Lista> bt22lL = udoModel.getTipoUdoTempl().getTipoUdo22Templ().getBindTipo22Listas();
	    	udoInst.setRequisitoInsts(new ArrayList<RequisitoInst>());
	    	for (BindTipo22Lista bt22l:bt22lL) { //per ogni lista associata al tipo22
	    		if(bt22l.getTipoProcTempl().getClientid().equals(domanda.getTipoProcTempl().getClientid())) {
		    		//List<RequisitoTempl> requisitoTemplL  = bt22l.getListaRequisitiTempl().getRequisitoTempls();
		    		//for (RequisitoTempl requisitoTempl:requisitoTemplL) { //per ogni requisito della lista
		    		List<BindListaRequisito> bindListaRequisitoL  = bt22l.getListaRequisitiTempl().getBindListaRequisitos();
		    		for (BindListaRequisito bindListaRequisito:bindListaRequisitoL) { //per ogni requisito della lista
		    			if("N".equals(bindListaRequisito.getAnnullato()) && "S".equals(bindListaRequisito.getValidato()) 
		    					&& "N".equals(bindListaRequisito.getRequisitoTempl().getAnnullato()) && "S".equals(bindListaRequisito.getRequisitoTempl().getValidato())) {
			    			//creazione di RequisitoInst
	
		    				//30/05/2016 creato metodo utility per creare il RequisitoInst
		    				/*
		    				RequisitoInst requisitoInst = new RequisitoInst();
			    			requisitoInst.setDescr(bindListaRequisito.getRequisitoTempl().getDescr());
			    			requisitoInst.setClientid(UUID.randomUUID().toString().toUpperCase());
			    			requisitoInst.setAnnotations(bindListaRequisito.getRequisitoTempl().getAnnotations());
			    			requisitoInst.setDisabled("N");
			    			requisitoInst.setSaluteMentale(ClassificazioneUdoTemplSaluteMentaleDistinctInfo.SALUTE_MENTALE_NO);
			    			requisitoInst.setRequisitoTempl(bindListaRequisito.getRequisitoTempl());
			    			//27/05/2016 ordinamentop personalizzato dei requisiti
			    			requisitoInst.setIdLista(bindListaRequisito.getListaRequisitiTempl().getIdLista());
			    			requisitoInst.setListaRequisitoOrdine(bindListaRequisito.getOrdine());
			    			requisitoInst.setValutazione("");
			    			*/
		    				RequisitoInst requisitoInst = utilService.getRequisitoInstByBindListaRequisito(bindListaRequisito, null, ClassificazioneUdoTemplSaluteMentaleDistinctInfo.SALUTE_MENTALE_NO, domanda);
		    				
			    			udoInst.addRequisitoInst(requisitoInst);
							requisitoInstDao.save(requisitoInst);
		    			}
		    		}
	    		}
	    	}			
		
	    	//salvo udoInst
			udoInstDao.save(udoInst);
			
			//setto il flag che indica un procedimento in corso per la udoModel
			udoModel.setProcedimentoInCorso("S");
			udoModelDao.save(udoModel);
		}
		
	}

	public void loadCheckedUdoList(HomePageBean homePageBean, UdoModelDao udoModelDao) {
		homePageBean.setUdoUoL(getAllCheckedUdoUoForList(homePageBean, udoModelDao));
		homePageBean.setActiveRowIndex(0);
	}

	public static void svuotaCampiRicercaRegione (HomePageBean homePageBean){
		homePageBean.setFolderStatusSelected(null);
		homePageBean.setSubStringtitolareDaCercare(null);
		homePageBean.setTipoProcSelected(null);
		homePageBean.setDataCreazioneA(null);
		homePageBean.setDataCreazioneDa(null);
		homePageBean.setDataInvioDomandaA(null);
		homePageBean.setDataInvioDomandaDa(null);
		homePageBean.setDataConclusioneA(null);
		homePageBean.setDataConclusioneDa(null);
		homePageBean.setOggettoDomandaDaCercare(null);
		homePageBean.setDirezioneTemplClientidSel(null);
		homePageBean.setShowOnlyDomandeAssignedCongruenzaEsito(false);
		homePageBean.setShowOnlyDomandeAssignedCongruenzaSenzaEsito(false);
	}
	
	public static void svuotaCampiRicercaTitolare (HomePageBean homePageBean){
		homePageBean.setSubStringDenominazioneDaCercare(null);
		homePageBean.setSubStringTipoUdoDaCercare(null);
		homePageBean.setSubStringBrancaDaCercare(null);
		homePageBean.setSubStringDisciplinaDaCercare(null);
		homePageBean.setSubStringSedeOperativaDaCercare(null);
		homePageBean.setSubStringUODaCercare(null);
		homePageBean.setSubStringDirettoreDaCercare(null);
		homePageBean.setSubStringCodiceUnivocoDaCercare(null);
	}
	
	public void orderDomande(MessageContext messageContext, UserBean userBean, HomePageBean homePageBean, ExtraWayService xwService, DomandaFieldToOrderEnum newDomandaFieldToOrderEnum) throws Exception {
		homePageBean.setDomandaOrderTypeEnum(homePageBean.nextDomandaOrderTypeEnumForField(newDomandaFieldToOrderEnum));
		loadProcedimenti(messageContext, userBean, homePageBean, xwService);
	}

	public TitolariPageBean createTitolariPageBean(UserBean userBean) throws Exception {
		if(userBean.isREGIONE()) {
			TitolariPageBean titolariPageBean = new TitolariPageBean(10);
			titolariPageBean.setDirezioniTempl((List<DirezioneTempl>) direzioneTemplDao.findAll());
			titolariPageBean.setTitolari(titolareModelDao.findForTitolariList(null, null, null, titolariPageBean.getTitolareOrderTypeEnum(), userBean.getUtenteModel().getClientid()));
			return titolariPageBean;
		} else {
			throw new Exception("L'utente corrente non ha i diritti per visualizzare i titolari");
		}
	}
	
	public void orderTitolari(MessageContext messageContext, UserBean userBean, TitolariPageBean titolariPageBean, TitolareFieldToOrderEnum newTitolareFieldToOrderEnum) throws Exception {
		titolariPageBean.setTitolareOrderTypeEnum(titolariPageBean.nextDomandaOrderTypeEnumForField(newTitolareFieldToOrderEnum));
		titolariPageBean.setTitolari(titolareModelDao.findForTitolariList(titolariPageBean.getSubStringtitolareDaCercare(), titolariPageBean.getSubStringPartitaIva(), titolariPageBean.getDirezioneTemplClientidSel(), titolariPageBean.getTitolareOrderTypeEnum(), userBean.getUtenteModel().getClientid()));
	}
	

	public MraPageBean createMraPageBean(ExternalContext externalContext, UserBean userBean) throws Exception {
		if(userBean.isAMMINISTRATORE()) {
			MraPageBean mraPageBean = new MraPageBean();
			mraPageBean.setMraModel(mraModelDao.getMraModelIfExist());
			HttpServletRequest httpServletRequest = (HttpServletRequest)externalContext.getNativeRequest();
			String sessionId = httpServletRequest.getSession().getId();
			mraPageBean.setSessionId(Base64.encodeBase64String(sessionId.getBytes()));
			
			return mraPageBean;
		} else {
			throw new Exception("L'utente corrente non ha i diritti per visualizzare la pagina MRA");
		}
	}

	public void svuotaCampiRicercaTitolareAtti(UserBean userBean, TitolariPageBean titolariPageBean) throws Exception {
		titolariPageBean.setSubStringtitolareDaCercare("");
		titolariPageBean.setDirezioneTemplClientidSel(null);
		titolariPageBean.setSubStringPartitaIva("");
	}	

	public void filtraTitolariAtti(UserBean userBean, TitolariPageBean titolariPageBean) throws Exception {
		if(userBean.isREGIONE()) {
			titolariPageBean.setTitolari(titolareModelDao.findForTitolariList(titolariPageBean.getSubStringtitolareDaCercare(), titolariPageBean.getSubStringPartitaIva(), titolariPageBean.getDirezioneTemplClientidSel(), titolariPageBean.getTitolareOrderTypeEnum(), userBean.getUtenteModel().getClientid()));
		} else {
			throw new Exception("L'utente corrente non ha i diritti per visualizzare i titolari");
		}
	}	
	
	public void firstPageTitolareAtti(UserBean userBean, TitolariPageBean titolariPageBean) throws Exception {
		if (titolariPageBean.isCanFirst()) {
			titolariPageBean.setCurrentPage(1);			
		}
	}	
	
	public void prevPageTitolareAtti(UserBean userBean, TitolariPageBean titolariPageBean) throws Exception {
		if (titolariPageBean.isCanPrev()) {
			titolariPageBean.setCurrentPage(titolariPageBean.getCurrentPage() - 1);			
		}
	}	
	
	public void nextPageTitolareAtti(UserBean userBean, TitolariPageBean titolariPageBean) throws Exception {
		if (titolariPageBean.isCanNext()) {
			titolariPageBean.setCurrentPage(titolariPageBean.getCurrentPage() + 1);			
		}
	}
	
	public void lastPageTitolareAtti(UserBean userBean, TitolariPageBean titolariPageBean) throws Exception {
		if (titolariPageBean.isCanLast()) {
			titolariPageBean.setCurrentPage(titolariPageBean.getTotalPage());			
		}
	}
	
	
	public void aggiornaFascicolo(MessageContext messageContext, ExtraWayService xwService, HomePageBean homePageBean, boolean fromProgressBar){
		@SuppressWarnings("unchecked")
		List<TitolareModel> titolareL = (List<TitolareModel>) titolareModelDao.findAll();

		JobBean jobBean = null;
		if (!fromProgressBar) { //la richiesta proviene dal pulsante premuto dall'utente -> si crea il job
			jobBean = new JobBean("aggiornaFascicolo", titolareL.size());
			homePageBean.setJobBean(jobBean);
		}
		else { // la richiesta proviene da progressBar -> si recupera il job
			jobBean = homePageBean.getJobBean();
		}
		
		log.debug("Processo titolare " + (jobBean.getCurrent()+1) + " di " + titolareL.size() + ") " + titolareL.get(jobBean.getCurrent()).getRagSoc());
		String risultato = FolderDao.aggiornaFascicolo(xwService, titolareL.get(jobBean.getCurrent()));
		
		if(!risultato.equals("Successo! Operazione completata con successo")){
			//Si è verificato un errore
			jobBean.getMsgs().add(risultato);
		}
		
		//update stato job
		jobBean.setCurrent(jobBean.getCurrent() + 1);
					
		//se il lavoro e' finito si elimina il job
		if (jobBean.isCompleted()) {
			homePageBean.setJobBean(null);
			MessageBuilder mB = new MessageBuilder();
			if(jobBean.getMsgs().size() > 0) {
				for(String m : jobBean.getMsgs()) {
					messageContext.addMessage(mB.error().source("messages").defaultText(m).build());
				}
			} else {
				messageContext.addMessage(mB.error().source("messages").defaultText(risultato).build());
			}
		}
	}
	
}
