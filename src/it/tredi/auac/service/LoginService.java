package it.tredi.auac.service;

import it.tredi.auac.BusinessException;
import it.tredi.auac.ExtraWayService;
import it.tredi.auac.bean.LoginPageBean;
import it.tredi.auac.bean.UserBean;
import it.tredi.auac.dao.BinaryAttachmentsApplDao;
import it.tredi.auac.dao.OperatoreModelDao;
import it.tredi.auac.dao.TitolareModelDao;
import it.tredi.auac.dao.UtenteDao;
import it.tredi.auac.orm.entity.OperatoreModel;
import it.tredi.auac.orm.entity.TitolareModel;
import it.tredi.auac.orm.entity.UtenteModel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.webflow.context.ExternalContext;

@Service
public class LoginService {
	private static final Logger log = Logger.getLogger(LoginService.class);	
	
	@Autowired
	TitolareModelDao titolareModelDao;
	
	@Autowired
	WorkflowService workflowService;
	
	@Autowired
	UtenteDao utenteDao;
	
	@Autowired
	OperatoreModelDao operatoreModelDao;
	
	@Autowired
	BinaryAttachmentsApplDao binaryAttachmentsApplDao;
	
	@Autowired
	PlatformTransactionManager platformTransactionManager;

	public LoginService() {
		// do nothing
	}
	
	public LoginPageBean checkCas(MessageContext messageContext, ExternalContext context, String redirectUrl, String defaultRedirectUrl) throws Exception {
		LoginPageBean loginPageBean = new LoginPageBean();
		loginPageBean.setLoginResult(LoginPageBean.lOGINRESULT_NOUSERONDB);
		HttpServletRequest httpServletRequest = (HttpServletRequest) context.getNativeRequest();
		httpServletRequest.getSession().invalidate();
		loginPageBean.setLogin(httpServletRequest.getRemoteUser());
		
		UtenteModel utenteModel = null;		
		
		Properties prop = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();           
		InputStream stream = loader.getResourceAsStream("/auac.properties");
		prop.load(stream);
		
		if (loginPageBean.getLogin().equals("titolare") && prop.getProperty("Debug").equals("S")){
			utenteModel = utenteDao.checkUser("renzo.boschetto8914", null, false); //check login e password su RDBMS giuseppe.cenci9762(ha degli allegati)  giuseppe.dalben5346(ha degli atti senza allegati)
		}
		else if (loginPageBean.getLogin().equals("valutatore") && prop.getProperty("Debug").equals("S")){
			utenteModel = utenteDao.checkUser("giuseppe.zordan7901", null, false);
		}
		else {
			utenteModel = utenteDao.checkUserByUsernameCas(loginPageBean.getLogin()); //check login e password su RDBMS
		}

		try {
			stream.close();
		} catch (Exception ex) {
			
		}
		
		return logWithUtenteModel(loginPageBean, messageContext, context, redirectUrl, defaultRedirectUrl, utenteModel);
	}

	//selectUtenteModel(loginPageBean, messageContext, externalContext, redirectUrl, defaultRedirectUrl, requestParameters.utenteClientId)
	public LoginPageBean selectUtenteModel(LoginPageBean loginPageBean, MessageContext messageContext, ExternalContext context, String redirectUrl, String defaultRedirectUrl, String utenteModelClientid) throws Exception {
		UtenteModel utenteModel = utenteDao.findOne(utenteModelClientid);
		loginPageBean.setAdminAccessType(LoginPageBean.ADMIN_ACCESS_TYPE_SELALTROOPERATORE);
		loginPageBean.setUtentiModel(null);
		
		return logWithUtenteModel(loginPageBean, messageContext, context, redirectUrl, defaultRedirectUrl, utenteModel);
	}

		
		
	public LoginPageBean logWithUtenteModel(LoginPageBean loginPageBean, MessageContext messageContext, ExternalContext context, String redirectUrl, String defaultRedirectUrl, UtenteModel utenteModel) throws Exception {
		UserBean userBean = null;
		
		if (utenteModel == null) { //utente non presente su db
			loginPageBean.setLoginResult(LoginPageBean.lOGINRESULT_NOUSERONDB);
			MessageBuilder mB = new MessageBuilder();
			messageContext.addMessage(mB.error().source("login").defaultText("Utente non autorizzato ad accedere all'applicativo").build());
			return loginPageBean;
		}
		else { //login con successo
			Properties prop = new Properties();
			ClassLoader loader = Thread.currentThread().getContextClassLoader();           
			InputStream stream = loader.getResourceAsStream("/auac.properties");
			prop.load(stream);

			loginPageBean.setLoginResult(LoginPageBean.lOGINRESULT_LOGGED);
			userBean = new UserBean(utenteModel);
			
			//Mark a userBean, if it is an admin logged in as another operatore
			if(loginPageBean.isAdmin() && loginPageBean.getAdminAccessType().equals(LoginPageBean.ADMIN_ACCESS_TYPE_SELALTROOPERATORE))
				userBean.setImitatingOperatore(true);
			
			//si mette in sessione lo UserBean
			context.getSessionMap().put("userBean", userBean);
			
			//si inizializza la connessione con extraway e la si mette in sessione
			//ServletContext sC = (ServletContext)context.getNativeContext();
			
			if(log.isDebugEnabled())
				log.debug("xwHost: " + prop.getProperty("xwHost") + "; xwPort: " + prop.getProperty("xwPort")
					 + "; xwUser: " + prop.getProperty("xwUser")  + "; xwPassword: " + prop.getProperty("xwPassword")
					 + "; xwDb: " + prop.getProperty("xwDb")  + "; xwWsUrl: " + prop.getProperty("xwWsUrl")
					 + "; xwWsAuthUser: " + prop.getProperty("xwWsAuthUser")  + "; xwWsAuthPassword: " + prop.getProperty("xwWsAuthPassword"));
			
			
			ExtraWayService xw = new ExtraWayService(prop.getProperty("xwHost"), 
					prop.getProperty("xwPort"),
					prop.getProperty("xwUser"), //si utilizza un utente applicativo
					prop.getProperty("xwPassword"),
					prop.getProperty("xwDb"),
					"UTF-8", 
					50, 
					50, 
					prop.getProperty("xwWsUrl"), 
					prop.getProperty("xwWsAuthUser"), 
					prop.getProperty("xwWsAuthPassword")); 
					
			//handler per le operazioni di servizio
			ExtraWayService _xw = new ExtraWayService(prop.getProperty("xwHost"), 
					prop.getProperty("xwPort"),
					prop.getProperty("xwUser"), //si utilizza un utente applicativo
					prop.getProperty("xwPassword"),
					prop.getProperty("xwDb"),
					"UTF-8", 
					50, 
					50, 
					prop.getProperty("xwWsUrl"), 
					prop.getProperty("xwWsAuthUser"), 
					prop.getProperty("xwWsAuthPassword"));
			try {
				xw.init();
				_xw.init();
				xw.setNomePersona(prop.getProperty("nome_persona"));
				xw.setNomeUfficio(prop.getProperty("nome_uff"));
				_xw.setNomePersona(prop.getProperty("nome_persona"));
				_xw.setNomeUfficio(prop.getProperty("nome_uff"));
				
				xw.setSmtpHost(prop.getProperty("smtpHost"));
				xw.setSmtpPort(prop.getProperty("smtpPort"));
				xw.setSmtpProtocol(prop.getProperty("smtpProtocol"));
				xw.setSmtpPwd(prop.getProperty("smtpPwd"));
				xw.setSmtpUser(prop.getProperty("smtpUser"));
				xw.setMailPrincipale(prop.getProperty("mailPrincipale"));
				xw.setMailDebug(prop.getProperty("mailDebug"));
				xw.setAbilitaInvioMail(prop.getProperty("abilitaInvioMail"));
				_xw.setSmtpHost(prop.getProperty("smtpHost"));
				_xw.setSmtpPort(prop.getProperty("smtpPort"));
				_xw.setSmtpProtocol(prop.getProperty("smtpProtocol"));
				_xw.setSmtpPwd(prop.getProperty("smtpPwd"));
				_xw.setSmtpUser(prop.getProperty("smtpUser"));
				_xw.setMailPrincipale(prop.getProperty("mailPrincipale"));
				_xw.setMailDebug(prop.getProperty("mailDebug"));
				_xw.setAbilitaInvioMail(prop.getProperty("abilitaInvioMail"));
				
				//si mettono in sessione tutte le properties
				context.getSessionMap().put("properties", prop);
			}
			catch (Exception e) {
				try {
					stream.close();
				} catch (Exception ex) {
					
				}
				throw new BusinessException("Errore in fase di init a ExtraWay Server (web services) - " + e.getMessage(), e);
			}
			context.getSessionMap().put("xw", xw);
			context.getSessionMap().put("_xw", _xw);		
			context.getSessionMap().put("binaryAttachmentsApplDao", binaryAttachmentsApplDao);
			context.getSessionMap().put("platformTransactionManager", platformTransactionManager);
			context.getSessionMap().put("workflowService", workflowService);
			//Connection connection = binaryAttachmentsApplDao.getConnection();
			//DatabaseMetaData dmd = connection.getMetaData();
			//String url = dmd.getURL();
			//String username = dmd.getUserName();

			
			
/*				context.getSessionMap().put("jdbcUrl", binaryAttachmentsApplDao.getConnectionProperties().get("url"));
				context.getSessionMap().put("jdbcUsername", binaryAttachmentsApplDao.getConnectionProperties().get("username"));
				context.getSessionMap().put("jdbcPassword", binaryAttachmentsApplDao.getConnectionProperties().get("password"));*/
			
			//se ruolo AMMINISTRATORE -> mostro tutti i titolari
			if (userBean.isAMMINISTRATORE()) {
				loginPageBean.setLoginResult(LoginPageBean.lOGINRESULT_MULTIROLE);
				loginPageBean.setAdmin(true);
				List<Map<String,String>>titolariL = new ArrayList<Map<String,String>>();

				long startTimeParz = System.currentTimeMillis();
				//List<TitolareModel> lt = (List<TitolareModel>)titolareModelDao.findAll();
				List<TitolareModel> lt = (List<TitolareModel>)titolareModelDao.findAllForList();
				log.info("doLogin - titolareModelDao.findAllForList: " + (System.currentTimeMillis() - startTimeParz) + " ms");
				
				for (TitolareModel titolareModel:lt) {						
					Map<String,String> map = new HashMap<String,String>();
					titolariL.add(map);
					map.put("titolareClientId",titolareModel.getClientid());
					String descr = titolareModel.getRagSoc();	
					map.put("descr",descr);
				}
				//userBean.setTitolariL(titolariL);
				loginPageBean.setTitolariL(titolariL);
				try {
					stream.close();
				} catch (Exception ex) {
					
				}
				return loginPageBean;
			}
			
			//Se l'utenteModel ha ruolo VERIFICATORE non controllo se l'utente e' associato a degli OperatoreModel
			if(userBean.isVERIFICATORE()) {
				loginPageBean.setVerificatore(true);
			}
			//check se utente ha più righe nella tabella OPERATORE_MODEL
			List<OperatoreModel> operatoreModelL = operatoreModelDao.findByUtenteModel(utenteModel);
			if (operatoreModelL.size() > 0) { 
				//nel caso di utente associato a uno più titolari tramite la tabella OPERATORE_MODEL 
				//(si tratta di utente OPERATORE_TITOLARE o COLLABORATORE_VALUTAZIONE o VALUTATORE_INTERNO)
				
				//si ricava la lista degli operatoreModel tra cui far scegliere all'utente
				/*List<Map<String,String>>operatoriModelTitolariL = new ArrayList<Map<String,String>>();
				for (OperatoreModel operatoreModel:operatoreModelL) {
					Map<String,String> map = new HashMap<String,String>();
					operatoriModelTitolariL.add(map);
					map.put("operatoreModelClientId",operatoreModel.getClientid());
					map.put("titolareClientId",operatoreModel.getTitolareModel().getClientid());
					
					String descr = operatoreModel.getDescr();
					if (descr == null || descr.length() > 0) {
						descr = operatoreModel.getTitolareModel().getRagSoc();
					}							
					map.put("descr", operatoreModel.getRuolo() + " - " + descr);
				}*/

				if (operatoreModelL.size() == 1 && !userBean.isVERIFICATORE()) { 
					//se l'operatoreModel a cui è associato l'operatore è uno e il ruolo non e' verificatore lo si forza
					userBean.setOperatoreModel(operatoreModelL.get(0));
					//userBean.setTitolareClientId(operatoreModelL.get(0).getTitolareModel().getClientid());
					userBean.setTitolareModel(operatoreModelL.get(0).getTitolareModel());
				}
				else { 
					//userBean.setTitolariL(operatoriModelTitolariL);
					loginPageBean.setLoginResult(LoginPageBean.lOGINRESULT_MULTIROLE);
					loginPageBean.setOperatoriModel(operatoreModelL);
					try {
						stream.close();
					} catch (Exception ex) {
						
					}
					return loginPageBean;
				}
			}
			try {
				stream.close();
			} catch (Exception ex) {
				
			}
		}

		
		/*
		Preso da MainService.createHomePageBean
		HomePageBean homePageBean = new HomePageBean();
		if (userBean.isTITOLARE()) {
			TitolareModel titolare = titolareDao.findByUtenteModel(userBean.getUtenteModel());
			homePageBean.setTitolareModel(titolare);
		}
		else if (userBean.isOPERATORE_TITOLARE() || userBean.isVERIFICATORE()) {
			TitolareModel titolare = titolareDao.findOne(userBean.getTitolareClientId());
			homePageBean.setTitolareModel(titolare);
		}
		else if (userBean.isCOLLABORATORE_VALUTAZIONE() || userBean.isVALUTATORE_INTERNO()) {
			TitolareModel titolare = userBean.getUtenteModel().getUoModel().getTitolareModel();
			homePageBean.setTitolareModel(titolare);
		}
		
		Sul DB
		-- L'UTENTE_MODEL (con campo RUOLO) con relativa anagrafica 
		-- puo' essere agganciato a una UO_MODEL
		-- puo' essere agganciato a una DIREZIONE_TEMPL
		-- puo' essere agganciato a 0-n OPERATORE_MODEL (con campo RUOLO) ogni OPERATORE_MODEL ha il suo TITOLARE_MODEL
		-- il TITOLARE_MODEL puo' avere una DIREZIONE_TEMPL		

		COSE CHE NON TORNANO:
		1) Se il campo RUOLO di UTENTE_MODEL puo' prendere tutti i valori possibili 
			AMMINISTRATORE, REGIONE, TITOLARE, OPERATORE_TITOLARE, COLLABORATORE_VALUTAZIONE, VALUTATORE_INTERNO
			quando diventa OPERATORE_TITOLARE da dove prendiamo il TITOLARE_MODEL?
		2) Se il campo RUOLO di OPERATORE_MODEL puo' prendere tutti i valori possibili sembra che un utente possa essere TITOLARE di piu' TITOLARE_MODEL ma non è cosi'
			perche' quando e' TITOLARE il 
		 *
		 */
		
		//Se sono qui vuol dire che l'autenticazione e' avvenuta con successo e l'userBean e' quasi pronto
		if (userBean.isTITOLARE()) {
			userBean.setTitolareModel(titolareModelDao.findByUtenteModel(userBean.getUtenteModel()));
		}
		//else if (userBean.isOPERATORE_TITOLARE() || userBean.isVERIFICATORE()) {
		//	titolare = titolareDao.findOne(userBean.getTitolareClientId());
		//}
		else if (userBean.isCOLLABORATORE_VALUTAZIONE() || userBean.isVALUTATORE_INTERNO()) {
			if(userBean.getTitolareModel() == null && userBean.getUtenteModel().getUoModel() != null)
				userBean.setTitolareModel(userBean.getUtenteModel().getUoModel().getTitolareModel());
		}

		//TODO RIABILITARE
		workflowService.checkAndSetWorkflowBean(userBean, context);
		
		//se user era entrato con una url specifica si fa redirect
		if (redirectUrl == null || redirectUrl.length() == 0)
			redirectUrl = defaultRedirectUrl;
		else
			redirectUrl = new String(Base64.decodeBase64(redirectUrl));
		context.requestExternalRedirect(redirectUrl);
		
		return loginPageBean;
	}	

	/*
	 * Questo metodo viene chiamato in caso l'utente sia associato a piu' ruoli e sia un amministratore
	 */
	@Transactional
	public boolean selectUtenteModel(MessageContext messageContext, ExternalContext context, String redirectUrl, String defaultRedirectUrl, String operatoreModelClientId, String titolareClientId) throws Exception {
		UserBean userBean = null;
		if (operatoreModelClientId != null && operatoreModelClientId.length() > 0) {
			userBean = (UserBean)context.getSessionMap().get("userBean");
			if (operatoreModelClientId.equals("VERIFICATORE")) { //nel caso di ruolo VERIFICATORE
				//do nothig
			} else {
				OperatoreModel operatoreModelSel = operatoreModelDao.findOne(operatoreModelClientId);
				userBean.setTitolareModel(operatoreModelSel.getTitolareModel());
				userBean.setOperatoreModel(operatoreModelSel);
			}
		} else if (titolareClientId != null && titolareClientId.length() > 0) { //caso di utente operatore associato a più titolari (l'autenticazione è già avvenuta e l'utente ha appena selezionato il titolare su cui vuole lavorare)
			userBean = (UserBean)context.getSessionMap().get("userBean");
			
			if (titolareClientId.equals("ADMIN_TOOLS")) { //nel caso di ruolo amministratore
				//do nothig
			}
			else {
				//userBean.setTitolareClientId(titolareClientId);
				userBean.setTitolareModel(titolareModelDao.findByClientId(titolareClientId));
				
				if (userBean.isAMMINISTRATORE()) {
					userBean.getUtenteModel().setRuolo("OPERATORE_TITOLARE"); //utile nel caso in cui il ruolo sia l'AMMINISTRATORE che impersona un titolare
				} else {
					List<OperatoreModel> operatoreModelL = operatoreModelDao.findByUtenteModelTitolareModel(userBean.getUtenteModel(), titolareClientId);
					if (operatoreModelL.size() > 0) {
						userBean.setOperatoreModel(operatoreModelL.get(0));
					}					
				}
			}
		}
		else { //ERRORE non risulta essere stato selezionato nessun UtenteModel
			MessageBuilder mB = new MessageBuilder();
			messageContext.addMessage(mB.error().source("login").defaultText("Nessun utente\ruolo selezionato").build());
			return false;
		}
		
		//Se sono qui vuol dire che l'autenticazione e' avvenuta con successo e l'userBean e' quasi pronto
		if (userBean.isTITOLARE()) {
			userBean.setTitolareModel(titolareModelDao.findByUtenteModel(userBean.getUtenteModel()));
		}
		else if (userBean.isCOLLABORATORE_VALUTAZIONE() || userBean.isVALUTATORE_INTERNO()) {
			if(userBean.getTitolareModel() == null && userBean.getUtenteModel().getUoModel() != null)
				userBean.setTitolareModel(userBean.getUtenteModel().getUoModel().getTitolareModel());
		}		

		//TODO RIABILITARE
		workflowService.checkAndSetWorkflowBean(userBean, context);
		
		//se user era entrato con una url specifica si fa redirect
		if (redirectUrl == null || redirectUrl.length() == 0)
			redirectUrl = defaultRedirectUrl;
		else
			redirectUrl = new String(Base64.decodeBase64(redirectUrl));
		context.requestExternalRedirect(redirectUrl);
		return true;
	}
	
	/*
	 * Questo metodo viene chiamato in caso l'utente sia un amministratore e selezioni di entrare come un altro operatore effettuando una ricerca per nome e/o cognome dell'altro operatore
	 */
	@Transactional
	public void findUtentiModel(LoginPageBean loginPageBean, MessageContext messageContext, ExternalContext context, String redirectUrl, String defaultRedirectUrl) throws Exception {
		loginPageBean.setAdminAccessType(LoginPageBean.ADMIN_ACCESS_TYPE_ALTROOPERATORE);
		loginPageBean.setUtentiModel(utenteDao.getListByUserNameCasLike(loginPageBean.getAltroOperatoreUserName()));
		//return false;
	}
	
	/*
	//ORIGINALE
	//HttpServletResponse response
	@Transactional
	public boolean doLogin(UtenteDao utenteDao, OperatoreModelDao operatoreModelDao, LoginPageBean loginPageBean, MessageContext messageContext, ExternalContext context, String redirectUrl, String defaultRedirectUrl
			, String operatoreModelClientId, String titolareClientId, boolean encodePassword, BinaryAttachmentsApplDao binaryAttachmentsApplDao) throws Exception {
		UserBean userBean = null;
		if (operatoreModelClientId != null && operatoreModelClientId.length() > 0) {
			userBean = (UserBean)context.getSessionMap().get("userBean");
			if (operatoreModelClientId.equals("VERIFICATORE")) { //nel caso di ruolo VERIFICATORE
				//do nothig
			} else {
				//userBean.setTitolareClientId(titolareClientId);
				OperatoreModel operatoreModelSel = operatoreModelDao.findOne(operatoreModelClientId);
				userBean.setTitolareModel(operatoreModelSel.getTitolareModel());
				userBean.setOperatoreModel(operatoreModelSel);
			}
		} else if (titolareClientId != null && titolareClientId.length() > 0) { //caso di utente operatore associato a più titolari (l'autenticazione è già avvenuta e l'utente ha appena selezionato il titolare su cui vuole lavorare)
			userBean = (UserBean)context.getSessionMap().get("userBean");
			
			if (titolareClientId.equals("ADMIN_TOOLS")) { //nel caso di ruolo amministratore
				//do nothig
			}
			else {
				//userBean.setTitolareClientId(titolareClientId);
				userBean.setTitolareModel(titolareModelDao.findByClientId(titolareClientId));
				
				if (userBean.isAMMINISTRATORE()) {
					userBean.getUtenteModel().setRuolo("OPERATORE_TITOLARE"); //utile nel caso in cui il ruolo sia l'AMMINISTRATORE che impersona un titolare
				} else {
					List<OperatoreModel> operatoreModelL = operatoreModelDao.findByUtenteModelTitolareModel(userBean.getUtenteModel(), titolareClientId);
					if (operatoreModelL.size() > 0) {
						userBean.setOperatoreModel(operatoreModelL.get(0));
					}					
				}
			}
		}
		else { //occorre effettuare l'autenticazione vera e propria
			String login = loginPageBean.getLogin();
			String password = loginPageBean.getPassword();
			if (login.length() == 0 || password.length() == 0) { //login o password non compilati
				MessageBuilder mB = new MessageBuilder();
				messageContext.addMessage(mB.error().source("login").defaultText("Username o password non valorizzati").build());
				return false;
			}
			
			UtenteModel utenteModel = null;		
			
			Properties prop = new Properties();
			ClassLoader loader = Thread.currentThread().getContextClassLoader();           
			InputStream stream = loader.getResourceAsStream("/auac.properties");
			prop.load(stream);
			
			if (login.equals("titolare") && prop.getProperty("Debug").equals("S")){
				utenteModel = utenteDao.checkUser("renzo.boschetto8914", null, false); //check login e password su RDBMS giuseppe.cenci9762(ha degli allegati)  giuseppe.dalben5346(ha degli atti senza allegati)
			}
			else if (login.equals("valutatore") && prop.getProperty("Debug").equals("S")){
				utenteModel = utenteDao.checkUser("giuseppe.zordan7901", null, false);
			}
			else {
				utenteModel = utenteDao.checkUser(login, password, encodePassword); //check login e password su RDBMS
			}

			if (utenteModel == null) { //login fallito
				MessageBuilder mB = new MessageBuilder();
				messageContext.addMessage(mB.error().source("login").defaultText("Username o password non corretti").build());
				try {
					stream.close();
				} catch (Exception ex) {
					
				}
				return false;
			}
			else { //login con successo
				userBean = new UserBean(loginPageBean.getLogin(), utenteModel);
				
				//si mette in sessione lo UserBean
				context.getSessionMap().put("userBean", userBean);
				
				//si inizializza la connessione con extraway e la si mette in sessione
				//ServletContext sC = (ServletContext)context.getNativeContext();
				
				if(log.isDebugEnabled())
					log.debug("xwHost: " + prop.getProperty("xwHost") + "; xwPort: " + prop.getProperty("xwPort")
						 + "; xwUser: " + prop.getProperty("xwUser")  + "; xwPassword: " + prop.getProperty("xwPassword")
						 + "; xwDb: " + prop.getProperty("xwDb")  + "; xwWsUrl: " + prop.getProperty("xwWsUrl")
						 + "; xwWsAuthUser: " + prop.getProperty("xwWsAuthUser")  + "; xwWsAuthPassword: " + prop.getProperty("xwWsAuthPassword"));
				
				
				ExtraWayService xw = new ExtraWayService(prop.getProperty("xwHost"), 
						prop.getProperty("xwPort"),
						prop.getProperty("xwUser"), //si utilizza un utente applicativo
						prop.getProperty("xwPassword"),
						prop.getProperty("xwDb"),
						"UTF-8", 
						10, 
						10, 
						prop.getProperty("xwWsUrl"), 
						prop.getProperty("xwWsAuthUser"), 
						prop.getProperty("xwWsAuthPassword")); 
						
						/*new ExtraWayService(sC.getInitParameter("xwHost"), 
						sC.getInitParameter("xwPort"),
						sC.getInitParameter("xwUser"), //si utilizza un utente applicativo
						sC.getInitParameter("xwPassword"),
						sC.getInitParameter("xwDb"),
						"UTF-8", 
						10, 
						10, 
						sC.getInitParameter("xwWsUrl"), 
						sC.getInitParameter("xwWsAuthUser"), 
						sC.getInitParameter("xwWsAuthPassword"));*-/
				
				//handler per le operazioni di servizio
				ExtraWayService _xw = new ExtraWayService(prop.getProperty("xwHost"), 
						prop.getProperty("xwPort"),
						prop.getProperty("xwUser"), //si utilizza un utente applicativo
						prop.getProperty("xwPassword"),
						prop.getProperty("xwDb"),
						"UTF-8", 
						10, 
						10, 
						prop.getProperty("xwWsUrl"), 
						prop.getProperty("xwWsAuthUser"), 
						prop.getProperty("xwWsAuthPassword"));
				
						/*sC.getInitParameter("xwHost"), 
						sC.getInitParameter("xwPort"),
						sC.getInitParameter("xwUser"), //si utilizza un utente applicativo
						sC.getInitParameter("xwPassword"),
						sC.getInitParameter("xwDb"),
						"UTF-8", 
						10, 
						10, 
						sC.getInitParameter("xwWsUrl"), 
						sC.getInitParameter("xwWsAuthUser"), 
						sC.getInitParameter("xwWsAuthPassword"));*-/				
				
				try {
					xw.init();
					_xw.init();
					xw.setNomePersona(prop.getProperty("nome_persona"));
					xw.setNomeUfficio(prop.getProperty("nome_uff"));
					_xw.setNomePersona(prop.getProperty("nome_persona"));
					_xw.setNomeUfficio(prop.getProperty("nome_uff"));
					
					xw.setSmtpHost(prop.getProperty("smtpHost"));
					xw.setSmtpPort(prop.getProperty("smtpPort"));
					xw.setSmtpProtocol(prop.getProperty("smtpProtocol"));
					xw.setSmtpPwd(prop.getProperty("smtpPwd"));
					xw.setSmtpUser(prop.getProperty("smtpUser"));
					xw.setMailPrincipale(prop.getProperty("mailPrincipale"));
					xw.setMailDebug(prop.getProperty("mailDebug"));
					xw.setAbilitaInvioMail(prop.getProperty("abilitaInvioMail"));
					_xw.setSmtpHost(prop.getProperty("smtpHost"));
					_xw.setSmtpPort(prop.getProperty("smtpPort"));
					_xw.setSmtpProtocol(prop.getProperty("smtpProtocol"));
					_xw.setSmtpPwd(prop.getProperty("smtpPwd"));
					_xw.setSmtpUser(prop.getProperty("smtpUser"));
					_xw.setMailPrincipale(prop.getProperty("mailPrincipale"));
					_xw.setMailDebug(prop.getProperty("mailDebug"));
					_xw.setAbilitaInvioMail(prop.getProperty("abilitaInvioMail"));
					
					//si mettono in sessione tutte le properties
					context.getSessionMap().put("properties", prop);
				}
				catch (Exception e) {
					try {
						stream.close();
					} catch (Exception ex) {
						
					}
					throw new BusinessException("Errore in fase di init a ExtraWay Server (web services) - " + e.getMessage(), e);
				}
				context.getSessionMap().put("xw", xw);
				context.getSessionMap().put("_xw", _xw);		
				context.getSessionMap().put("binaryAttachmentsApplDao", binaryAttachmentsApplDao);
				//Connection connection = binaryAttachmentsApplDao.getConnection();
				//DatabaseMetaData dmd = connection.getMetaData();
				//String url = dmd.getURL();
				//String username = dmd.getUserName();

				
				
/*				context.getSessionMap().put("jdbcUrl", binaryAttachmentsApplDao.getConnectionProperties().get("url"));
				context.getSessionMap().put("jdbcUsername", binaryAttachmentsApplDao.getConnectionProperties().get("username"));
				context.getSessionMap().put("jdbcPassword", binaryAttachmentsApplDao.getConnectionProperties().get("password"));*-/
				
				//se ruolo AMMINISTRATORE -> mostro tutti i titolari
				if (userBean.isAMMINISTRATORE()) {
					loginPageBean.setAdmin(true);
					List<Map<String,String>>titolariL = new ArrayList<Map<String,String>>();

					long startTimeParz = System.currentTimeMillis();
					//List<TitolareModel> lt = (List<TitolareModel>)titolareModelDao.findAll();
					List<TitolareModel> lt = (List<TitolareModel>)titolareModelDao.findAllForList();
					log.info("doLogin - titolareModelDao.findAll: " + (System.currentTimeMillis() - startTimeParz) + " ms");
					
					for (TitolareModel titolareModel:lt) {						
						Map<String,String> map = new HashMap<String,String>();
						titolariL.add(map);
						map.put("titolareClientId",titolareModel.getClientid());
						String descr = titolareModel.getRagSoc();	
						map.put("descr",descr);
					}
					//userBean.setTitolariL(titolariL);
					loginPageBean.setTitolariL(titolariL);
					try {
						stream.close();
					} catch (Exception ex) {
						
					}
					return false;
				}
				
				//Se l'utenteModel ha ruolo VERIFICATORE non controllo se l'utente e' associato a degli OperatoreModel
				if(userBean.isVERIFICATORE()) {
					loginPageBean.setVerificatore(true);
				}
					
				//check se utente ha più righe nella tabella OPERATORE_MODEL
				List<OperatoreModel> operatoreModelL = operatoreModelDao.findByUtenteModel(utenteModel);
				if (operatoreModelL.size() > 0) { 
					//nel caso di utente associato a uno più titolari tramite la tabella OPERATORE_MODEL 
					//(si tratta di utente OPERATORE_TITOLARE o COLLABORATORE_VALUTAZIONE o VALUTATORE_INTERNO)
					
					//si ricava la lista degli operatoreModel tra cui far scegliere all'utente
					/*List<Map<String,String>>operatoriModelTitolariL = new ArrayList<Map<String,String>>();
					for (OperatoreModel operatoreModel:operatoreModelL) {
						Map<String,String> map = new HashMap<String,String>();
						operatoriModelTitolariL.add(map);
						map.put("operatoreModelClientId",operatoreModel.getClientid());
						map.put("titolareClientId",operatoreModel.getTitolareModel().getClientid());
						
						String descr = operatoreModel.getDescr();
						if (descr == null || descr.length() > 0) {
							descr = operatoreModel.getTitolareModel().getRagSoc();
						}							
						map.put("descr", operatoreModel.getRuolo() + " - " + descr);
					}*-/

					if (operatoreModelL.size() == 1 && !userBean.isVERIFICATORE()) { 
						//se l'operatoreModel a cui è associato l'operatore è uno e il ruoolo non e' verificatore lo si forza
						userBean.setOperatoreModel(operatoreModelL.get(0));
						//userBean.setTitolareClientId(operatoreModelL.get(0).getTitolareModel().getClientid());
						userBean.setTitolareModel(operatoreModelL.get(0).getTitolareModel());
					}
					else { 
						//userBean.setTitolariL(operatoriModelTitolariL);
						loginPageBean.setOperatoriModel(operatoreModelL);
						try {
							stream.close();
						} catch (Exception ex) {
							
						}
						return false;
					}
				}
			}
			try {
				stream.close();
			} catch (Exception ex) {
				
			}

		}
		
		/*
		Preso da MainService.createHomePageBean
		HomePageBean homePageBean = new HomePageBean();
		if (userBean.isTITOLARE()) {
			TitolareModel titolare = titolareDao.findByUtenteModel(userBean.getUtenteModel());
			homePageBean.setTitolareModel(titolare);
		}
		else if (userBean.isOPERATORE_TITOLARE() || userBean.isVERIFICATORE()) {
			TitolareModel titolare = titolareDao.findOne(userBean.getTitolareClientId());
			homePageBean.setTitolareModel(titolare);
		}
		else if (userBean.isCOLLABORATORE_VALUTAZIONE() || userBean.isVALUTATORE_INTERNO()) {
			TitolareModel titolare = userBean.getUtenteModel().getUoModel().getTitolareModel();
			homePageBean.setTitolareModel(titolare);
		}
		
		Sul DB
		-- L'UTENTE_MODEL (con campo RUOLO) con relativa anagrafica 
		-- puo' essere agganciato a una UO_MODEL
		-- puo' essere agganciato a una DIREZIONE_TEMPL
		-- puo' essere agganciato a 0-n OPERATORE_MODEL (con campo RUOLO) ogni OPERATORE_MODEL ha il suo TITOLARE_MODEL
		-- il TITOLARE_MODEL puo' avere una DIREZIONE_TEMPL		

		COSE CHE NON TORNANO:
		1) Se il campo RUOLO di UTENTE_MODEL puo' prendere tutti i valori possibili 
			AMMINISTRATORE, REGIONE, TITOLARE, OPERATORE_TITOLARE, COLLABORATORE_VALUTAZIONE, VALUTATORE_INTERNO
			quando diventa OPERATORE_TITOLARE da dove prendiamo il TITOLARE_MODEL?
		2) Se il campo RUOLO di OPERATORE_MODEL puo' prendere tutti i valori possibili sembra che un utente possa essere TITOLARE di piu' TITOLARE_MODEL ma non è cosi'
			perche' quando e' TITOLARE il 
		 *
		 *-/
		
		//Se sono qui vuol dire che l'autenticazione e' avvenuta con successo e l'userBean e' quasi pronto
		if (userBean.isTITOLARE()) {
			userBean.setTitolareModel(titolareModelDao.findByUtenteModel(userBean.getUtenteModel()));
		}
		//else if (userBean.isOPERATORE_TITOLARE() || userBean.isVERIFICATORE()) {
		//	titolare = titolareDao.findOne(userBean.getTitolareClientId());
		//}
		else if (userBean.isCOLLABORATORE_VALUTAZIONE() || userBean.isVALUTATORE_INTERNO()) {
			if(userBean.getTitolareModel() == null && userBean.getUtenteModel().getUoModel() != null)
				userBean.setTitolareModel(userBean.getUtenteModel().getUoModel().getTitolareModel());
		}		

		workflowService.checkAndSetWorkflowBean(userBean, context);
		
			
		//se user era entrato con una url specifica si fa redirect
		if (redirectUrl == null || redirectUrl.length() == 0)
			redirectUrl = defaultRedirectUrl;
		else
			redirectUrl = new String(Base64.decodeBase64(redirectUrl));
		context.requestExternalRedirect(redirectUrl);
		return true;
	}
	*/
	
	/*
	public void doLogout(ExternalContext context) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) context.getNativeRequest();
		httpServletRequest.getSession().invalidate();
	}
	*/

	public void doLogoutCas(ExternalContext context) {
		//HttpServletRequest httpServletRequest = (HttpServletRequest) context.getNativeRequest();
		context.getSessionMap().put("userBean", null);
		//httpServletRequest.getSession().invalidate();
		//context.requestExternalRedirect("https://salute.regione.veneto.it/auth/logout");		
	}

	/*
	public LoginPageBean createEmptyLoginPageBean() {
		return new LoginPageBean();
	}
	*/

	/*
	public LoginPageBean createLoginPageBean(String token) {
		LoginPageBean loginPageBean = new LoginPageBean();
		int index = token.indexOf(",");
		String login = token.substring(0, index);
		String password = token.substring(index + 1);
		loginPageBean.setLogin(login);
		loginPageBean.setPassword(password);
		return loginPageBean;
	}
	*/

	// estrazione di lista matricole da LoginException
	/*private List<Map<String, String>> extractMatricoleL(Exception e) {
		List<Map<String, String>> matricoleL = new ArrayList<Map<String, String>>();
		String message = e.getMessage();
		message = message.substring(message.indexOf("{") + 2,
				message.length() - 2);
		String[] utenti = message.split(Pattern.quote("]["));
		for (int i = 0; i < utenti.length; i++) {
			String utente = utenti[i];
			int index = utente.indexOf("|");
			String matricola = utente.substring(0, index);
			String descr = utente.substring(index + 1);
			Map<String, String> map = new HashMap<String, String>();
			map.put("matricola", matricola);
			map.put("descr", descr);
			matricoleL.add(map);
		}
		return matricoleL;
	}*/

	/*
	public String checkSSO(String token) {
		if (token != null && token.length() > 0)
			return "doSSOLogin";
		return "loginPage";
	}
	*/

}
