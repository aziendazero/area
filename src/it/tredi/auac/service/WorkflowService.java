package it.tredi.auac.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.bonitasoft.engine.bpm.data.DataInstance;
import org.bonitasoft.engine.bpm.document.Document;
import org.bonitasoft.engine.bpm.flownode.ActivityStates;
import org.bonitasoft.engine.bpm.flownode.ArchivedActivityInstance;
import org.bonitasoft.engine.bpm.flownode.ArchivedActivityInstanceSearchDescriptor;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.platform.LoginException;
import org.bonitasoft.engine.search.Order;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.webflow.context.ExternalContext;

import it.tredi.auac.bean.CompareUdoModelBean;
import it.tredi.auac.bean.CompareUdoModelPageBean;
import it.tredi.auac.bean.DeliberaBean;
import it.tredi.auac.bean.DeliberaFileBean;
import it.tredi.auac.bean.UserBean;
import it.tredi.auac.bean.WorkflowBean;
import it.tredi.auac.bean.WorkflowGroup;
import it.tredi.auac.bean.WorkflowUser;
import it.tredi.auac.dao.TipoAttoDao;
import it.tredi.auac.dao.UdoInstDao;
import it.tredi.auac.dao.UdoModelDao;
import it.tredi.auac.orm.entity.DirezioneTempl;
import it.tredi.auac.orm.entity.TipoAtto;
import it.tredi.auac.orm.entity.TipoProcTempl;
import it.tredi.auac.orm.entity.TitolareModel;
import it.tredi.auac.orm.entity.UdoInst;
import it.tredi.auac.orm.entity.UdoModel;
import it.tredi.auac.orm.entity.UfficioTempl;
import it.tredi.auac.orm.entity.UtenteDirezioneModel;
import it.tredi.workflow.bonita6.CookieBonita;
import it.tredi.workflow.bonita6.WorkflowBonita;
import it.tredi.workflow.bonita6.WorkflowProcessInstanceInfo;

@Service
public class WorkflowService {
	private static final Logger log = Logger.getLogger(WorkflowService.class);
	
	@Value("${bonita.admin.username}")
	private String adminUsername;

	@Value("${bonita.admin.password}")
	private String adminPassword;

	@Value("${bonita.users.password}")
	private String usersPassword;

	@Value("${bonita.bonitaviewserverurl}")
	private String bonitaViewServerUrl;
	
	@Autowired
	private WorkflowBonita workflowBonita;
	
	@Autowired
	private TipoAttoDao tipoAttoDao;
	
	public WorkflowService() {
		//do nothing
		log.info("WorkflowService constructor");
	}
	
	public String getAdminUsername() {
		return adminUsername;
	}

	public void setAdminUsername(String adminUsername) {
		this.adminUsername = adminUsername;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getUserPassword() {
		return usersPassword;
	}

	public void setUserPassword(String usersPassword) {
		this.usersPassword = usersPassword;
	}

	public String getBonitaViewServerUrl() {
		return bonitaViewServerUrl;
	}

	public void setBonitaViewServerUrl(String bonitaViewServerUrl) {
		this.bonitaViewServerUrl = bonitaViewServerUrl;
	}

	//Controlla se l'utente corrente è anche un WorkflowUser e in tal caso imposta nel'UserBean il WorkflowBean
	public void checkAndSetWorkflowBean(UserBean userBean, ExternalContext context) throws Exception {
		log.debug("WorkflowService checkAndSetWorkflowBean userBean.getLoginDbOrCas(): " + userBean.getUtenteModel().getLoginDbOrCas());
		log.debug("WorkflowService checkAndSetWorkflowBean userBean.getUtenteModel().getUsername(): " + userBean.getUtenteModel().getUsername());
		log.debug("WorkflowService checkAndSetWorkflowBean userBean.getUtenteModel().getUsernameCas(): " + userBean.getUtenteModel().getUsernameCas());
		log.debug("WorkflowService checkAndSetWorkflowBean adminUsername: " + adminUsername);
		log.debug("WorkflowService checkAndSetWorkflowBean adminPassword: " + adminPassword);
		log.debug("WorkflowService checkAndSetWorkflowBean usersPassword: " + usersPassword);
		//Controllo se l'utente e' anche un WorkflowUser
		WorkflowUser workflowUser = null;
		if(userBean.isAMMINISTRATORE()) {
			//                                                                  username                                              password
			// se utente AMMINISTRATORE allora Membro di                        admin_auacusername                                    auac_password
			//    Amministratori [Gruppo - nome=amministratori, nome visualizzato=Amministratori]
			workflowUser = new WorkflowUser("admin_" + userBean.getUtenteModel().getLoginDbOrCas(), usersPassword, userBean.getUtenteModel().getAnagraficaUtenteModel().getNome(), userBean.getUtenteModel().getAnagraficaUtenteModel().getCognome());
			workflowUser.addWorkflowGroup(getWorkflowGroupTutti());
			workflowUser.addWorkflowGroup(getWorkflowGroupAmministratori());
		} else if (userBean.isREGIONE()) {
			//                                                                  username                                              password
			// se utente REGIONE allora Membro di                               regione_auacusername                                  auac_password
			//    Regione [Gruppo - nome=regione, nome visualizzato=Regione]
			//    Direzione data da UTENTE_MODEL.ID_DIREZIONE_FK (UtenteModel.getDirezioneTempl()) [Gruppo - nome=dir_DirezioneTempl.getClientId(), nome visualizzato=Direzione - DirezioneTempl.getNome()]
			workflowUser = new WorkflowUser("regione_" + userBean.getUtenteModel().getLoginDbOrCas(), usersPassword, userBean.getUtenteModel().getAnagraficaUtenteModel().getNome(), userBean.getUtenteModel().getAnagraficaUtenteModel().getCognome());
			workflowUser.addWorkflowGroup(getWorkflowGroupTutti());
			workflowUser.addWorkflowGroup(getWorkflowGroupRegione());
			if(userBean.getUtenteModel() != null && userBean.getUtenteModel().getDirezioneTempl() != null) {
				workflowUser.addWorkflowGroup(getWorkflowGroupDirezione(userBean.getUtenteModel().getDirezioneTempl()));
				//Gestione uffici
				for(UtenteDirezioneModel udm : userBean.getUtenteModel().getUtenteDirezioneModels()) {
					workflowUser.addWorkflowGroups(getWorkflowGroupsUfficiDirezione(udm));
				}
			}
			
		} else if (userBean.isTITOLARE()) {
			//                                                                  username                                              password
			// se utente TITOLARE allora Membro di                              tit_TITOLARE_MODEL.CLIENTID_auacusername              auac_password
			//    TITOLARE_MODEL data da TITOLARE_MODEL.ID_UTENTE_FK=UTENTE_MODEL.CLIENTID [Gruppo - nome=titmod_TitolareModel.getClientId(), nome visualizzato=Titolari - TitolareModel.getRagSoc() (o Denominazione)]
			workflowUser = new WorkflowUser("tit_" + userBean.getTitolareModel().getClientid() + "_" + userBean.getUtenteModel().getLoginDbOrCas(), usersPassword, userBean.getUtenteModel().getAnagraficaUtenteModel().getNome(), userBean.getUtenteModel().getAnagraficaUtenteModel().getCognome());
			workflowUser.addWorkflowGroup(getWorkflowGroupTutti());
			if(userBean.getTitolareModel() != null)
				workflowUser.addWorkflowGroup(getWorkflowGroupTitolareModel(userBean.getTitolareModel()));
			
		} else if (userBean.isOPERATORE_TITOLARE()) {
			//                                                                  username                                              password
			// se utente OPERATORE_TITOLARE allora Membro di                    optit_TITOLARE_MODEL.CLIENTID_auacusername            auac_password
			//    OPERATORE TITOLARE_MODEL data da operatoreModel.getTitolareModel().getClientid() [Gruppo - nome=optitmod_TitolareModel.getClientId(), nome visualizzato=Operatori Titolare - TitolareModel.getRagSoc() (o Denominazione)]
			workflowUser = new WorkflowUser("optit_" + userBean.getTitolareModel().getClientid() + "_" + userBean.getUtenteModel().getLoginDbOrCas(), usersPassword, userBean.getUtenteModel().getAnagraficaUtenteModel().getNome(), userBean.getUtenteModel().getAnagraficaUtenteModel().getCognome());
			workflowUser.addWorkflowGroup(getWorkflowGroupTutti());
			if(userBean.getTitolareModel() != null)
				workflowUser.addWorkflowGroup(getWorkflowGroupOperatoriTitolareModel(userBean.getTitolareModel()));
		} else if (userBean.isOPERATORE_TITOLARE_IN_LETTURA()) {
			//                                                                  username                                              password
			// se utente OPERATORE_TITOLARE_IN_LETTURA allora Membro di         optitlt_TITOLARE_MODEL.CLIENTID_auacusername            auac_password
			//    OPERATORE TITOLARE_MODEL data da operatoreModel.getTitolareModel().getClientid() [Gruppo - nome=optitmod_TitolareModel.getClientId(), nome visualizzato=Operatori Titolare - TitolareModel.getRagSoc() (o Denominazione)]
			workflowUser = new WorkflowUser("optitlt_" + userBean.getTitolareModel().getClientid() + "_" + userBean.getUtenteModel().getLoginDbOrCas(), usersPassword, userBean.getUtenteModel().getAnagraficaUtenteModel().getNome(), userBean.getUtenteModel().getAnagraficaUtenteModel().getCognome());
			workflowUser.addWorkflowGroup(getWorkflowGroupTutti());
			//non imposto altri gruppi di appartenenza a parte TUTTI
			//if(userBean.getTitolareModel() != null)
			//	workflowUser.addWorkflowGroup(getWorkflowGroupOperatoriTitolareModel(userBean.getTitolareModel()));
		} else if (userBean.isVERIFICATORE()) {
			//                                                                  username                                            password
			// se utente VERIFICATORE allora Membro di                    		verif_auacusername            					auac_password
			//    Verificatori [Gruppo - nome=verificatori, nome visualizzato=Verificatori]
			workflowUser = new WorkflowUser("verif_" + userBean.getUtenteModel().getLoginDbOrCas(), usersPassword, userBean.getUtenteModel().getAnagraficaUtenteModel().getNome(), userBean.getUtenteModel().getAnagraficaUtenteModel().getCognome());
			workflowUser.addWorkflowGroup(getWorkflowGroupTutti());
			workflowUser.addWorkflowGroup(getWorkflowGroupVerificatori());
		}
		// se utente COLLABORATORE_VALUTAZIONE o VALUTATORE_INTERNO non faccio nulla perche' non possono eseguire task dei flussi
		if(workflowUser != null) {
			APISession apiSession = null;
			Map<String, String> userGroups = new HashMap<String, String>();
			for(WorkflowGroup group:workflowUser.getWorkflowGroups()) {
				userGroups.put(group.getName(), group.getDisplayName());
			}
			try {
				log.info("checkAndSetWorkflowBean - autentico l'utente: " + workflowUser.getUsername());
				apiSession = workflowBonita.getSession(workflowUser.getUsername(), workflowUser.getPassword());
				log.info("checkAndSetWorkflowBean - apiSession.getId(): " + apiSession.getId());
				
				//controllo se l'utente e' stato modificato rispetto all'ultimo accesso e nel caso aggiorno l'utente
				workflowBonita.updateUserOfUserProfile(apiSession.getUserId(), userGroups, this.adminUsername, this.adminPassword);
			} catch (LoginException e) {
				log.debug("checkAndSetWorkflowBean - L'utente " + userBean.getUtenteModel().getLoginDbOrCas() + " non esiste lo creo.");
				//Creo l'utente
				workflowBonita.createUserOfUserProfile(workflowUser.getUsername(), workflowUser.getPassword(), workflowUser.getFirstname(), workflowUser.getLastname(), userGroups, this.adminUsername, this.adminPassword);
				apiSession = workflowBonita.getSession(workflowUser.getUsername(), workflowUser.getPassword());
				//Aggiorno gli attori che utilizzano i filtri
				workflowBonita.updateActorsOfAllUserTask(apiSession);
			} catch (Exception e) {
				log.error("Errore in checkAndSetWorkflowBean", e);
				throw e;
			}
			
			CookieBonita restCookie = workflowBonita.getRestCookie(workflowUser.getUsername(), workflowUser.getPassword());
			CookieBonita adminRestCookie = workflowBonita.getRestCookie(this.getAdminUsername(), this.getAdminPassword());
			//Aggiungo il cookie alla request
			//ServletExternalContext externalContext = (ServletExternalContext)context.getNativeContext();
			//setCookie(context, restCookie);
			
			//userBean.setWorkflowBean(new WorkflowBean(workflowUser, apiSession, workflowBonita.getRestCookie(workflowUser.getUsername(), workflowUser.getPassword())));
			userBean.setWorkflowBean(new WorkflowBean(workflowUser, apiSession, restCookie, adminRestCookie));
			if(userBean.isREGIONE() || userBean.isAMMINISTRATORE()) {
				//Carico i WorkflowDefinition
				//userBean.getWorkflowBean().setProcessDeploymentInfos(workflowBonita.getEnabledWorkflowDefinitions(apiSession, 0, 100).getResult());
				userBean.getWorkflowBean().setProcessDeploymentInfos(workflowBonita.getEnabledWorkflowDefinitionsCanBeStartedBy(apiSession.getUserId(), 0, 100, apiSession).getResult());
			}
		}

	}
	
	public void setCookie(ExternalContext context, CookieBonita restCookie) {
		HttpServletResponse response = (HttpServletResponse)context.getNativeResponse();
		Cookie cookie = new Cookie(restCookie.getName(), restCookie.getValue());
		cookie.setPath(restCookie.getPath());
		if(restCookie.getDomain() != null)
			cookie.setDomain(restCookie.getDomain());
		response.addCookie(cookie);
	}
	
	private WorkflowGroup getWorkflowGroupTutti() {
		return new WorkflowGroup("tutti", "Tutti");
	}

	private WorkflowGroup getWorkflowGroupAmministratori() {
		return new WorkflowGroup("amministratori", "Amministratori");
	}
    
	private WorkflowGroup getWorkflowGroupRegione() {
		return new WorkflowGroup("regione", "Regione");
	}

	private WorkflowGroup getWorkflowGroupVerificatori() {
		return new WorkflowGroup("verificatori", "Verificatori");
	}

	private WorkflowGroup getWorkflowGroupDirezione(DirezioneTempl direzioneTempl) {
		String displayName = "Dir. - " + direzioneTempl.getNome();
		if(displayName.length() > 75)
			displayName = displayName.substring(0, 75);
		return new WorkflowGroup("dir_" + direzioneTempl.getClientid(), displayName);
	}
	
	private WorkflowGroup getWorkflowGroupTitolareModel(TitolareModel titolareModel) {
		String displayName = "Tit. - " + titolareModel.getRagSoc();
		if(displayName.length() > 75)
			displayName = displayName.substring(0, 75);
		return new WorkflowGroup("titmod_" + titolareModel.getClientid(), displayName);
	}

	private WorkflowGroup getWorkflowGroupOperatoriTitolareModel(TitolareModel titolareModel) {
		String displayName = "Op. Tit. - " + titolareModel.getRagSoc();
		if(displayName.length() > 75)
			displayName = displayName.substring(0, 75);
		return new WorkflowGroup("optitmod_" + titolareModel.getClientid(), displayName);
	}

	private List<WorkflowGroup> getWorkflowGroupsUfficiDirezione(UtenteDirezioneModel udm) {
		List<WorkflowGroup> toRet = new ArrayList<WorkflowGroup>();
		String displayName;
		for(UfficioTempl ufficio : udm.getUfficioTempls()) {
			displayName = "Uff. - " + ufficio.getNome() + " [" + udm.getDirezioneTempl().getNome() + "]";
			if(displayName.length() > 75)
				displayName = displayName.substring(0, 75);
			toRet.add(new WorkflowGroup("uff_" + udm.getDirezioneTempl().getIdDirezioneTempl().toBigInteger().toString() + "_" + ufficio.getClientid(), displayName));
		}
		return toRet;
	}

	/*private WorkflowGroup getWorkflowGroupVerificatoriTitolareModel(TitolareModel titolareModel) {
		String displayName = "Verif. Tit. - " + titolareModel.getRagSoc();
		if(displayName.length() > 75)
			displayName = displayName.substring(0, 75);
		return new WorkflowGroup("veriftitmod_" + titolareModel.getClientid(), displayName);
	}*/

	public WorkflowBonita getWorkflowBonita() {
		return workflowBonita;
	}
	
	public WorkflowProcessInstanceInfo getWorkflowProcessInstanceInfoForUser(long processInstanceId, long userId) throws Exception {
		try {
			return workflowBonita.getWorkflowProcessInstanceInfoForUser(processInstanceId, userId, this.getAdminUsername(), this.getAdminPassword());
		} catch (Exception e) {
			log.error("Errore in getWorkflowProcessInstanceInfoForUser", e);
			throw e;		
		}
	}

	public Date getUltimaDataRichiestaIntegrazione(long processInstanceId, TipoProcTempl tipoProcTempl) throws Exception {
		Date toRet = null;
		
		if("Autorizzazione".equals(tipoProcTempl.getNomeWf()) || "Accreditamento".equals(tipoProcTempl.getNomeWf())) {
			//Per Autorizzazione e Accreditamento
			//La data dell'ultima richiesta integrazione individuato dall'ultimo task "Cambia Stato Richiesta integrazioni" o "Cambia Stato Rich di integraz val risp program"
			APISession apiSession = workflowBonita.getSession(this.getAdminUsername(), this.getAdminPassword());
			
			SearchOptionsBuilder builderAAI = new SearchOptionsBuilder(0, 1);
			builderAAI.filter(ArchivedActivityInstanceSearchDescriptor.STATE_NAME, ActivityStates.COMPLETED_STATE);
			//USARE ROOT_PROCESS_INSTANCE_ID per ottenere i le activities del processo(processInstanceId) e degli eventuali sottoprocessi del processo steasso (processInstanceId) 
			//USARE PARENT_PROCESS_INSTANCE_ID per ottenere le activities del proceddo(processInstanceId) ma non quelle dei sottoprocessi
			//builderAAI.filter(ArchivedActivityInstanceSearchDescriptor.ROOT_PROCESS_INSTANCE_ID, processInstanceID);
			builderAAI.filter(ArchivedActivityInstanceSearchDescriptor.ROOT_PROCESS_INSTANCE_ID, processInstanceId);
			builderAAI.leftParenthesis();
				builderAAI.filter(ArchivedActivityInstanceSearchDescriptor.NAME, "Cambia Stato Richiesta integrazioni");
				builderAAI.or();
				builderAAI.filter(ArchivedActivityInstanceSearchDescriptor.NAME, "Cambia Stato Rich di integraz val risp program");
			builderAAI.rightParenthesis();
			builderAAI.sort(ArchivedActivityInstanceSearchDescriptor.REACHED_STATE_DATE, Order.DESC);
	
			SearchResult<ArchivedActivityInstance> archActivitResult = workflowBonita.getProcessAPIForSession(apiSession).searchArchivedActivities(builderAAI.done());
			if(archActivitResult.getCount() > 0) {
				toRet = archActivitResult.getResult().get(0).getReachedStateDate();
			}
		}
		
		return toRet;
	}
	
	
	public Date getUltimaDataInviaIntegrazione(long processInstanceId, TipoProcTempl tipoProcTempl) throws Exception {
		Date toRet = null;
		
		if("Autorizzazione".equals(tipoProcTempl.getNomeWf()) || "Accreditamento".equals(tipoProcTempl.getNomeWf())) {
			//Per Autorizzazione e Accreditamento
			//La data dell'ultimo invio di integrazioni individuato dall'ultimo task "Richiesta Integrazioni" o "Rich di Integr Congr Program"
			APISession apiSession = workflowBonita.getSession(this.getAdminUsername(), this.getAdminPassword());
			
			SearchOptionsBuilder builderAAI = new SearchOptionsBuilder(0, 1);
			builderAAI.filter(ArchivedActivityInstanceSearchDescriptor.STATE_NAME, ActivityStates.COMPLETED_STATE);
			//USARE ROOT_PROCESS_INSTANCE_ID per ottenere i le activities del processo(processInstanceId) e degli eventuali sottoprocessi del processo steasso (processInstanceId) 
			//USARE PARENT_PROCESS_INSTANCE_ID per ottenere le activities del proceddo(processInstanceId) ma non quelle dei sottoprocessi
			//builderAAI.filter(ArchivedActivityInstanceSearchDescriptor.ROOT_PROCESS_INSTANCE_ID, processInstanceID);
			builderAAI.filter(ArchivedActivityInstanceSearchDescriptor.ROOT_PROCESS_INSTANCE_ID, processInstanceId);
			builderAAI.leftParenthesis();
				builderAAI.filter(ArchivedActivityInstanceSearchDescriptor.NAME, "Richiesta Integrazioni");
				builderAAI.or();
				builderAAI.filter(ArchivedActivityInstanceSearchDescriptor.NAME, "Rich di Integr Congr Program");
			builderAAI.rightParenthesis();
			builderAAI.sort(ArchivedActivityInstanceSearchDescriptor.REACHED_STATE_DATE, Order.DESC);
	
			SearchResult<ArchivedActivityInstance> archActivitResult = workflowBonita.getProcessAPIForSession(apiSession).searchArchivedActivities(builderAAI.done());
			if(archActivitResult.getCount() > 0) {
				toRet = archActivitResult.getResult().get(0).getReachedStateDate();
			}
		}
		
		return toRet;
	}
	
	public Date getDataRedazioneRapportoVerifica(long processInstanceId, TipoProcTempl tipoProcTempl) throws Exception {
		Date toRet = null;
		
		if("Autorizzazione".equals(tipoProcTempl.getNomeWf()) || "Accreditamento".equals(tipoProcTempl.getNomeWf())) {
			//Per Autorizzazione e Accreditamento
			//La data in cui e' stato eseguito il task "Redazione Rapporto di Verifica"
			APISession apiSession = workflowBonita.getSession(this.getAdminUsername(), this.getAdminPassword());
			
			SearchOptionsBuilder builderAAI = new SearchOptionsBuilder(0, 1);
			builderAAI.filter(ArchivedActivityInstanceSearchDescriptor.STATE_NAME, ActivityStates.COMPLETED_STATE);
			//USARE ROOT_PROCESS_INSTANCE_ID per ottenere i le activities del processo(processInstanceId) e degli eventuali sottoprocessi del processo steasso (processInstanceId) 
			//USARE PARENT_PROCESS_INSTANCE_ID per ottenere le activities del proceddo(processInstanceId) ma non quelle dei sottoprocessi
			//builderAAI.filter(ArchivedActivityInstanceSearchDescriptor.ROOT_PROCESS_INSTANCE_ID, processInstanceID);
			builderAAI.filter(ArchivedActivityInstanceSearchDescriptor.ROOT_PROCESS_INSTANCE_ID, processInstanceId);
			builderAAI.filter(ArchivedActivityInstanceSearchDescriptor.NAME, "Redazione Rapporto di Verifica");
			builderAAI.sort(ArchivedActivityInstanceSearchDescriptor.REACHED_STATE_DATE, Order.DESC);
	
			SearchResult<ArchivedActivityInstance> archActivitResult = workflowBonita.getProcessAPIForSession(apiSession).searchArchivedActivities(builderAAI.done());
			if(archActivitResult.getCount() > 0) {
				toRet = archActivitResult.getResult().get(0).getReachedStateDate();
			}
		}
		
		return toRet;
	}
	
	/*
	 * Restituisce i clientid delle domande che hanno un task assegnato all'utente passato
	 */
	public List<String> getDomandeClientIdWithAvailableTaskForUser(long userId) throws Exception {
		List<String> clientIds = new ArrayList<String>();
		Set<Long> processInstanceIds = new HashSet<Long>();
		
		APISession apiSession = workflowBonita.getSession(this.getAdminUsername(), this.getAdminPassword());
		//Ricavo 1000 (impossibile che ci siano piu' di 1000 domande attive) task disponibili per l'utente passato
		SearchResult<HumanTaskInstance> tasks = workflowBonita.getAvailableHumanTaskForUser(userId, apiSession, 0, 1000);
		for(HumanTaskInstance task : tasks.getResult()) {
			processInstanceIds.add(task.getParentProcessInstanceId());
		}
		DataInstance diAuacDomClientid;
		String auacDomClientId;
		//Non esiste un metodo che permetta di ricavare contemporaneamente la stessa variabile da piu' flussi
		//Per farlo occorrerebbe conettersi direttamente al db bonita ed effettuare delle query
		for(Long processInstanceId : processInstanceIds) {
			diAuacDomClientid = workflowBonita.getVariable("auac_dom_clientid", processInstanceId, apiSession);
			if(diAuacDomClientid.getValue() != null) {
				auacDomClientId = (String)diAuacDomClientid.getValue();
				System.out.println("auacDomClientId: " + auacDomClientId);
				clientIds.add(auacDomClientId);
			}
		}
		
		return clientIds;
	}
	
	/**
	 * Salva la delibera passata nel flusso passato processInstanceId senza salvare il file in delibera.getDeliberaFileBean() ma solo il campo delibera.getDeliberaFileBean().getFileOggetto() 
	 * @param processInstanceId
	 * @param delibera
	 * @throws Exception
	 */
	public void setDelibera(long processInstanceId, DeliberaBean delibera) throws Exception {
		/*
		predispRegistrazProvvedTipoAttoClientid			Testo
		predispRegistrazProvvedDeliberaAnno				numero intero
		predispRegistrazProvvedDeliberaNumero			Testo
		predispRegistrazProvvedDeliberaDataInizio		Data
		predispRegistrazProvvedDeliberaDataFine			Data
		-- oggettoFile1
		-- docValue1
		delibera										file il file passato al connettore AggiornaStatiUdoModelDomanda
		deliberaOggetto									Testo
		*/
		
		APISession apiSession = workflowBonita.getSession(this.getAdminUsername(), this.getAdminPassword());

		workflowBonita.setVariable("predispRegistrazProvvedTipoAttoClientid", processInstanceId, delibera.getTipoAtto().getClientid(), apiSession);
		workflowBonita.setVariable("predispRegistrazProvvedDeliberaAnno", processInstanceId, delibera.getAnnoDelibera(), apiSession);
		workflowBonita.setVariable("predispRegistrazProvvedDeliberaNumero", processInstanceId, delibera.getNumeroDelibera(), apiSession);
		workflowBonita.setVariable("predispRegistrazProvvedDeliberaDataInizio", processInstanceId, delibera.getDataInizioValidita(), apiSession);
		workflowBonita.setVariable("predispRegistrazProvvedDeliberaDataFine", processInstanceId, delibera.getDataFineValidita(), apiSession);
		
		if(delibera.getDeliberaFileBean() != null && delibera.getDeliberaFileBean().getFileOggetto() != null && !delibera.getDeliberaFileBean().getFileOggetto().isEmpty()) {
			workflowBonita.setVariable("deliberaOggetto", processInstanceId, delibera.getDeliberaFileBean().getFileOggetto(), apiSession);
		}
	}
	
	/**
	 * Salva il file della delibera passata nel flusso con il processInstanceId passato, non viene salvato il campo fileOggetto 
	 * @param processInstanceId
	 * @param delibera
	 * @throws Exception
	 */
	public void setFileDeliberaSenzaOggetto(long processInstanceId, DeliberaFileBean deliberaFile) throws Exception {
		/*
		predispRegistrazProvvedTipoAttoClientid			Testo
		predispRegistrazProvvedDeliberaAnno				numero intero
		predispRegistrazProvvedDeliberaNumero			Testo
		predispRegistrazProvvedDeliberaDataInizio		Data
		predispRegistrazProvvedDeliberaDataFine			Data
		-- oggettoFile1
		-- docValue1
		delibera										file il file passato al connettore AggiornaStatiUdoModelDomanda
		deliberaOggetto									Testo
		*/
		
		APISession apiSession = workflowBonita.getSession(this.getAdminUsername(), this.getAdminPassword());

		if(deliberaFile != null) {
			if(deliberaFile.getFileName() == null
					|| deliberaFile.getMimeType() == null || deliberaFile.getFileContent() == null) {
				throw new Exception("Impossibile salvare il file della delibera senza compilare tutti i campi relativi al file: Oggetto, FileName, MimeType, FileContent");
			}
			workflowBonita.updateDocument("delibera", processInstanceId, deliberaFile.getFileName(), deliberaFile.getMimeType(), deliberaFile.getFileContent(), apiSession);
		}
	}

	/**
	 * Carica tutti i dati della delibera del flusso passato processInstanceId, evitare di caricare il byte[] del file, in caso di richiesta di download del file utilizzare il metodo getDeliberaFileBean con withFileContent=true che carica solo i dati del file
	 * @param processInstanceId
	 * @param withFileContent
	 * @return
	 * @throws Exception
	 */
	public DeliberaBean getDelibera(long processInstanceId, boolean withFileContent) throws Exception {
		DeliberaBean delibera = new DeliberaBean();
		delibera.setProcessInstanceId(processInstanceId);
		APISession apiSession = workflowBonita.getSession(this.getAdminUsername(), this.getAdminPassword());
		/*
		predispRegistrazProvvedTipoAttoClientid			Testo
		predispRegistrazProvvedDeliberaAnno				numero intero
		predispRegistrazProvvedDeliberaNumero			Testo
		predispRegistrazProvvedDeliberaDataInizio		Data
		predispRegistrazProvvedDeliberaDataFine			Data
		-- oggettoFile1
		-- docValue1
		delibera										file il file passato al connettore AggiornaStatiUdoModelDomanda
		deliberaOggetto									Testo
		*/
		DataInstance dataInstance = workflowBonita.getVariable("predispRegistrazProvvedTipoAttoClientid", processInstanceId, apiSession);
		if(dataInstance.getValue() != null) {
			String tipoAttoClientid = (String)dataInstance.getValue();
			try {
				TipoAtto tipoAtto = tipoAttoDao.findOne(tipoAttoClientid);
				delibera.setTipoAtto(tipoAtto);
			} catch (Exception e) {
				log.warn("getDelibera impossibile trovare il TipoAtto per il clientid: " + tipoAttoClientid);
			}
		}
		dataInstance = workflowBonita.getVariable("predispRegistrazProvvedDeliberaAnno", processInstanceId, apiSession);
		if(dataInstance.getValue() != null) {
			delibera.setAnnoDelibera((Integer)dataInstance.getValue());
		}
		dataInstance = workflowBonita.getVariable("predispRegistrazProvvedDeliberaNumero", processInstanceId, apiSession);
		if(dataInstance.getValue() != null) {
			delibera.setNumeroDelibera((String)dataInstance.getValue());
		}
		dataInstance = workflowBonita.getVariable("predispRegistrazProvvedDeliberaDataInizio", processInstanceId, apiSession);
		if(dataInstance.getValue() != null) {
			delibera.setDataInizioValidita((Date)dataInstance.getValue());
		}
		dataInstance = workflowBonita.getVariable("predispRegistrazProvvedDeliberaDataFine", processInstanceId, apiSession);
		if(dataInstance.getValue() != null) {
			delibera.setDataFineValidita((Date)dataInstance.getValue());
		}
		delibera.setDeliberaFileBean(getDeliberaFileBean(processInstanceId, withFileContent));
		return delibera;
	}
	
	/**
	 * Restituisce il DeliberaFileBean della delibera del flusso passato processInstanceId, caricando anche il byte[] content del file se withFileContent=true  
	 * @param processInstanceId
	 * @param withFileContent
	 * @return
	 * @throws Exception
	 */
	public DeliberaFileBean getDeliberaFileBean(long processInstanceId, boolean withFileContent) throws Exception {
		APISession apiSession = workflowBonita.getSession(this.getAdminUsername(), this.getAdminPassword());
		return getDeliberaFileBean(processInstanceId, apiSession, withFileContent);
	}

	private DeliberaFileBean getDeliberaFileBean(long processInstanceId, APISession apiSession, boolean withFileContent) throws Exception {
		DeliberaFileBean deliberaFileBean = null;
		
		String oggettoFile = null;
		DataInstance dataInstance = workflowBonita.getVariable("deliberaOggetto", processInstanceId, apiSession);
		if(dataInstance.getValue() != null) {
			oggettoFile = (String)dataInstance.getValue();
		}
		try {
			Document document = workflowBonita.getLastDocument("delibera", processInstanceId, apiSession);
			if(document != null) {
				if(withFileContent) {
					byte[] fileByteArray = getWorkflowBonita().getLastDocumentContent("delibera", processInstanceId, apiSession);
					deliberaFileBean = new DeliberaFileBean(document.getContentFileName(), document.getContentMimeType(), oggettoFile, fileByteArray);
				} else {
					deliberaFileBean = new DeliberaFileBean(document.getContentFileName(), document.getContentMimeType(), oggettoFile);
				}
			}
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
		return deliberaFileBean;
	}
	
}
