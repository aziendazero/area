package it.tredi.auac.service;


import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
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
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Vector;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.bonitasoft.engine.bpm.data.DataInstance;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.webflow.context.ExternalContext;

import it.highwaytech.apps.generic.XMLDocumento;
import it.highwaytech.apps.generic.XmlReplacer;
import it.highwaytech.apps.generic.utils.GenericUtils;
import it.highwaytech.util.Text;
import it.tredi.auac.AutovalutazioneMassivaRunnable;
import it.tredi.auac.BooleanEnum;
import it.tredi.auac.BusinessException;
import it.tredi.auac.DocOrderTypeEnum;
import it.tredi.auac.ExtraWayService;
import it.tredi.auac.InvioReinvioTypeEnum;
import it.tredi.auac.Mail;
import it.tredi.auac.MotivoBloccoInvioReinvioDomandaEnum;
import it.tredi.auac.PostItOrderTypeEnum;
import it.tredi.auac.PostItTypeEnum;
import it.tredi.auac.TipoPostiLettoEnum;
import it.tredi.auac.TipoUdo22DescrComparator;
import it.tredi.auac.UdoUoInstForListOrderTypeEnum;
import it.tredi.auac.bean.AttoInstBean;
import it.tredi.auac.bean.CheckPostiLettoDomandaPageBean;
import it.tredi.auac.bean.CheckTipoUdo22DomandaPageBean;
import it.tredi.auac.bean.CompareUdoBean;
import it.tredi.auac.bean.CompareUdoDomandaPageBean;
import it.tredi.auac.bean.CsvReportIstruttoriaPageBean;
import it.tredi.auac.bean.DeliberaBean;
import it.tredi.auac.bean.EditNoteMancanzaTipiUdoDomandaPageBean;
import it.tredi.auac.bean.EditNoteVerificaDomandaPageBean;
import it.tredi.auac.bean.FileBinaryAttachmentsApplBean;
import it.tredi.auac.bean.FolderBean;
import it.tredi.auac.bean.FolderPdfBean;
import it.tredi.auac.bean.FolderVerbaleVerificaPdfBean;
import it.tredi.auac.bean.JobBean;
import it.tredi.auac.bean.PersonaSelectOption;
import it.tredi.auac.bean.RelazioneConclusivaPageBean;
import it.tredi.auac.bean.ReportValutazioneComplessivaPageBean;
import it.tredi.auac.bean.ReportValutazioneRGAreaPageBean;
import it.tredi.auac.bean.ReportValutazioneRGPageBean;
import it.tredi.auac.bean.ReportValutazioneUdoPageBean;
import it.tredi.auac.bean.ReportVerificaComplessivaPageBean;
import it.tredi.auac.bean.ReportVerificaRGAreaPageBean;
import it.tredi.auac.bean.ReportVerificaRGPageBean;
import it.tredi.auac.bean.ReportVerificaUdoPageBean;
import it.tredi.auac.bean.RequisitiGenUdoUoInstForPdf;
import it.tredi.auac.bean.ShowFolderAttiPageBean;
import it.tredi.auac.bean.ShowFolderPageBean;
import it.tredi.auac.bean.TitolariPageBean;
import it.tredi.auac.bean.UdoInstSearchOrderBean;
import it.tredi.auac.bean.UdoUoInstForList;
import it.tredi.auac.bean.UpdateUdoModelPageBean;
import it.tredi.auac.bean.UserBean;
import it.tredi.auac.bean.WorkflowExecuteTaskPageBean;
import it.tredi.auac.bean.WorkflowProcessInstance;
import it.tredi.auac.bean.WorkflowProcessInstanceImageOnWindow;
import it.tredi.auac.bean.WorkflowXWProcessInstance;
import it.tredi.auac.business.ClassificazioneUdoTemplSaluteMentaleDistinctInfo;
import it.tredi.auac.dao.AreaDisciplineDao;
import it.tredi.auac.dao.AttoInstDao;
import it.tredi.auac.dao.AttoModelDao;
import it.tredi.auac.dao.BinaryAttachmentsApplDao;
import it.tredi.auac.dao.ClassificazioneUdoTemplDao;
import it.tredi.auac.dao.DocumentoDao;
import it.tredi.auac.dao.DomandaInstDao;
import it.tredi.auac.dao.EdificioInstDao;
import it.tredi.auac.dao.FolderDao;
import it.tredi.auac.dao.OperatoreModelDao;
import it.tredi.auac.dao.ParametriDao;
import it.tredi.auac.dao.RequisitoInstDao;
import it.tredi.auac.dao.StoricoRisposteRequisitiDao;
import it.tredi.auac.dao.StrutturaInstDao;
import it.tredi.auac.dao.TipoAttoDao;
import it.tredi.auac.dao.TipoProcTemplDao;
import it.tredi.auac.dao.TipoUdo22TemplDao;
import it.tredi.auac.dao.TipoUdoTemplDao;
import it.tredi.auac.dao.TipoUdoUtenteTemplDao;
import it.tredi.auac.dao.TitolareModelDao;
import it.tredi.auac.dao.TokenSessionDao;
import it.tredi.auac.dao.UdoInstDao;
import it.tredi.auac.dao.UdoModelDao;
import it.tredi.auac.dao.UoInstDao;
import it.tredi.auac.dao.UoModelDao;
import it.tredi.auac.dao.UtenteDao;
import it.tredi.auac.orm.entity.AnagraficaUtenteModel;
import it.tredi.auac.orm.entity.AreaDiscipline;
import it.tredi.auac.orm.entity.AttoInst;
import it.tredi.auac.orm.entity.AttoModel;
import it.tredi.auac.orm.entity.BinaryAttachmentsAppl;
import it.tredi.auac.orm.entity.BindListaRequClassUdo;
import it.tredi.auac.orm.entity.BindUoProcLista;
import it.tredi.auac.orm.entity.BrancaUdoInstInfo;
import it.tredi.auac.orm.entity.ClassificazioneUdoTempl;
import it.tredi.auac.orm.entity.DirezioneTempl;
import it.tredi.auac.orm.entity.DisciplinaUdoInstInfo;
import it.tredi.auac.orm.entity.DomandaInst;
import it.tredi.auac.orm.entity.EdificioInst;
import it.tredi.auac.orm.entity.EsitoTempl;
import it.tredi.auac.orm.entity.Parametri;
import it.tredi.auac.orm.entity.RequisitoInst;
import it.tredi.auac.orm.entity.StoricoRisposteRequisiti;
import it.tredi.auac.orm.entity.StrutturaInst;
import it.tredi.auac.orm.entity.TipoAtto;
import it.tredi.auac.orm.entity.TipoProcTempl;
import it.tredi.auac.orm.entity.TipoUdo22Templ;
import it.tredi.auac.orm.entity.TipoUdoTempl;
import it.tredi.auac.orm.entity.TitolareModel;
import it.tredi.auac.orm.entity.TokenSession;
import it.tredi.auac.orm.entity.UdoInst;
import it.tredi.auac.orm.entity.UdoModel;
import it.tredi.auac.orm.entity.UoInst;
import it.tredi.auac.orm.entity.UoModel;
import it.tredi.auac.orm.entity.UtenteModel;
import it.tredi.auac.utils.AuacUserInfo;
import it.tredi.auac.utils.AuacUtils;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;


@Service
@Transactional
public class ShowFolderService {
	private static final Logger log = Logger.getLogger(ShowFolderService.class);
	
	@Autowired
	private DomandaInstDao domandaInstDao;
	
	@Autowired
	private UoInstDao uoInstDao;
	
	@Autowired
	private UdoInstDao udoInstDao;

	@Autowired
	private UoModelDao uoModelDao;
	
	@Autowired
	private UdoModelDao udoModelDao;

	@Autowired
	private TitolareModelDao titolareModelDao;
	
	@Autowired
	private BinaryAttachmentsApplDao binaryAttachmentsApplDao;
	
	@Autowired
	private AreaDisciplineDao areaDisciplineDao;

	@Autowired
	private ClassificazioneUdoTemplDao classificazioneUdoTemplDao;

	@Autowired
	private UtenteDao utenteDao;
	
	@Autowired
	private RequisitoInstDao requisitoInstDao;
	
	@Autowired
	private StoricoRisposteRequisitiDao storicoRisposteRequisitiDao;

	@Autowired
	private OperatoreModelDao operatoreModelDao;

	@Autowired
	private ParametriDao parametriDao;
	
	@Autowired
	private TokenSessionDao tokenSessionDao;

	@Autowired
	private AutovalutazioneService autovalutazioneService;

	@Autowired
	private AclService aclService;	
	
	@Resource
	private PlatformTransactionManager txManager;
	
	@Autowired
	WorkflowService workflowService;	
	
	@Autowired 
	TipoUdoTemplDao tipoUdoTemplDao;
	
	@Autowired
	private TipoUdoUtenteTemplDao tipoUdoUtenteTemplDao;

	@Autowired
	private TipoUdo22TemplDao tipoUdo22TemplDao;

	@Autowired
	private AttoModelDao attoModelDao;
	
	@Autowired
	private AttoInstDao attoInstDao;

	@Autowired
	private MainService mainService;

	@Autowired
	private UtilService utilService;

	@Autowired
	private TipoProcTemplDao tipoProcTemplDao;
	
	@Autowired
	private EdificioInstDao edificioInstDao;

	@Autowired
	private StrutturaInstDao strutturaInstDao;
	
	@Autowired 
	private TipoAttoDao tipoAttoDao;

	@Value("${ambito_ambulatoriale_clientid}")
	private String ambitoAmbulatorialeClientid;

	@Value("${san_reg_ven.url}")
	private String sanitaRegioneVenetoUrl;

	public ShowFolderService() {
		//do nothing
	}

	public String getAmbitoAmbulatorialeClientid() {
		return ambitoAmbulatorialeClientid;
	}

	public void setAmbitoAmbulatorialeClientid(String ambitoAmbulatorialeClientid) {
		this.ambitoAmbulatorialeClientid = ambitoAmbulatorialeClientid;
	}

	public String getIdUnit(String idUnitRequestParameter, String idUnitFlashScope){
		if (idUnitRequestParameter != null)
			return idUnitRequestParameter;
		else if (idUnitFlashScope != null)
			return idUnitFlashScope;
		return null;
	}
	
	private List<UdoUoInstForList> getFascicoloUdoL(UserBean userBean, DomandaInst domandaInst, String tipoDomanda) {
		//se e' un procedimento carichiamo le udo (in caso di fascicolo padre e fascicolo atti, non ci sono le udo)
		if (domandaInst != null){
			List<UdoUoInstForList> toRet = new ArrayList<UdoUoInstForList>();
			//L'utente che è un collaboratore alla valutazione non accede a questa pagina ma per sicurezza
			if (!userBean.isCOLLABORATORE_VALUTAZIONE()) {
				UdoUoInstForList udoUoInstForList;
				if (userBean.isVALUTATORE_INTERNO()) {
					//Mostro solo i requisiti generali, le StruttureInst, gli EdificioInst, le UoInst e le UdoInst che hanno almeno un requisito assegnato all'utente
					List<String> ids = domandaInstDao.getEntityInstClientIdConRequisitiAssegnatiAdUtente(userBean.getUtenteModel(), domandaInst);
					//Aggiungo la lista dei Requisiti Generali Aziendali della domanda
					if(ids.contains(domandaInst.getClientid())) {
						udoUoInstForList = new UdoUoInstForList(userBean, domandaInst);
						toRet.add(udoUoInstForList);
					}
					for(StrutturaInst strutturaInst : domandaInst.getStrutturaInsts()) {
						if(ids.contains(strutturaInst.getClientid())) {
							udoUoInstForList = new UdoUoInstForList(userBean, strutturaInst);
							toRet.add(udoUoInstForList);
						}
					}
					for(EdificioInst edificioInst : domandaInst.getEdificioInsts()) {
						if(ids.contains(edificioInst.getClientid())) {
							udoUoInstForList = new UdoUoInstForList(userBean, edificioInst);
							toRet.add(udoUoInstForList);
						}
					}
					for(UoInst uoInst : domandaInst.getUoInsts()) {
						if(ids.contains(uoInst.getClientid())) {
							udoUoInstForList = new UdoUoInstForList(userBean, uoInst);
							toRet.add(udoUoInstForList);
						}
						for(UdoInst udoInst : domandaInst.getUdoInsts()) {
							if(udoInst.getUdoModelUoModelClientid().equals(uoInst.getUoModelClientid()) && ids.contains(udoInst.getClientid())) {
								udoUoInstForList = new UdoUoInstForList(userBean, udoInst);
								toRet.add(udoUoInstForList);
							}
						}
					}
				} else {
					//Aggiungo la lista dei Requisiti Generali Aziendali della domanda
					if(utilService.isActiveInsertRequisitiGeneraliAziendaliForDomanda(domandaInst, tipoDomanda)) {
						udoUoInstForList = new UdoUoInstForList(userBean, domandaInst);
						toRet.add(udoUoInstForList);
					}
					for(StrutturaInst strutturaInst : domandaInst.getStrutturaInsts()) {
						udoUoInstForList = new UdoUoInstForList(userBean, strutturaInst);
						toRet.add(udoUoInstForList);
					}
					for(EdificioInst edificioInst : domandaInst.getEdificioInsts()) {
						udoUoInstForList = new UdoUoInstForList(userBean, edificioInst);
						toRet.add(udoUoInstForList);
					}
					for(UoInst uoInst : domandaInst.getUoInsts()) {
						udoUoInstForList = new UdoUoInstForList(userBean, uoInst);
						//if (!userBean.isCOLLABORATORE_VALUTAZIONE()) {
							toRet.add(udoUoInstForList);
						//}
						for(UdoInst udoInst : domandaInst.getUdoInsts()) {
							if(udoInst.getUdoModelUoModelClientid().equals(uoInst.getUoModelClientid())) {
								udoUoInstForList = new UdoUoInstForList(userBean, udoInst);
								//if (!userBean.isCOLLABORATORE_VALUTAZIONE()) {						
									toRet.add(udoUoInstForList);
								//}
							}
						}
					}
				}
			}
			return toRet;
		}
		return null;
	}	

	
	private List<EsitoTempl> getEsitiDaScegliere(DomandaInst domandaInst) {
		//se e' un procedimento carichiamo gli esiti possibili (in caso di fascicolo padre e fascicolo atti, non ci sono le udo)
		if (domandaInst != null){
			//Non eliminare usato per caricamento
			int size = domandaInst.getTipoProcTempl().getEsitoTempls().size();
			List<EsitoTempl> esitiPossibili = domandaInst.getTipoProcTempl().getEsitoTempls();
			return esitiPossibili;
		}		
		return null;
	}	
	
	/*public void calcolaSeInBozza(ShowFolderPageBean showFolderPageBean) {
		if (showFolderPageBean.getFascicolo().getFolderStatus().equals(DomandaInstDao.STATO_BOZZA) && showFolderPageBean.getFascicolo().getFolderType().equals(FolderDao.DOMANDA_TITOLARE_FOLDER_TYPE))
			showFolderPageBean.getFascicolo().setInBozza(true);
		else
			showFolderPageBean.getFascicolo().setInBozza(false);
	}*/
	
	/*public void calcolaSeProntoPerInvio(ShowFolderPageBean showFolderPageBean) {
		//Solo le domande in BOZZA possono essere inviate
		if (!FolderDao.DOMANDA_TITOLARE_FOLDER_TYPE.equals(showFolderPageBean.getFascicolo().getFolderType()) || !DomandaInstDao.STATO_BOZZA.equals(showFolderPageBean.getFascicolo().getFolderStatus())) { 
			showFolderPageBean.getFascicolo().setProntoPerInvio(false);
			return;
		}
		int numUdo = 0;
		boolean isTuttiRequisitiRisposti = true;
		if(showFolderPageBean.getFascicolo().getUdoUoInstForL() != null) {
			numUdo = showFolderPageBean.getFascicolo().getUdoUoInstForL().size();
			for(UdoUoInstForList udoUoIL : showFolderPageBean.getFascicolo().getUdoUoInstForL()) {
				if(!udoUoIL.isRequisitiRisposti()) {
					isTuttiRequisitiRisposti = false;
					break;
				}
			}
		}
		int numAllegati = 0;
		if (showFolderPageBean.getDocumentsL() != null)
			numAllegati = showFolderPageBean.getDocumentsL().size();
		if ( numUdo > 0 && numAllegati > 0 && isTuttiRequisitiRisposti &&
				domandaInstDao.checkAltreDomandeNonConcluseConStesseUoUdo(showFolderPageBean.getFascicolo().getDomandaClienId()) &&
				domandaInstDao.checkUdoSenzaPostiLettoInDomanda(showFolderPageBean.getFascicolo().getDomandaClienId()) ) {
			showFolderPageBean.getFascicolo().setProntoPerInvio(true);
		}
		else
			showFolderPageBean.getFascicolo().setProntoPerInvio(false);
	}*/
	
	/*public void calcolaSeReinviabile(ShowFolderPageBean showFolderPageBean) {
		if (!FolderDao.DOMANDA_TITOLARE_FOLDER_TYPE.equals(showFolderPageBean.getFascicolo().getFolderType()) || DomandaInstDao.STATO_BOZZA.equals(showFolderPageBean.getFascicolo().getFolderStatus())) { 
			showFolderPageBean.getFascicolo().setReinviabile(false);
			return;
		}
		int numUdo = 0;
		boolean isTuttiRequisitiRisposti = true;
		if(showFolderPageBean.getFascicolo().getUdoUoInstForL() != null) {
			numUdo = showFolderPageBean.getFascicolo().getUdoUoInstForL().size();
			for(UdoUoInstForList udoUoIL : showFolderPageBean.getFascicolo().getUdoUoInstForL()) {
				if(!udoUoIL.isRequisitiRisposti()) {
					isTuttiRequisitiRisposti = false;
					break;
				}
			}
		}
		int numAllegati = 0;
		if (showFolderPageBean.getDocumentsL() != null)
			numAllegati = showFolderPageBean.getDocumentsL().size();
		if (
				(
						showFolderPageBean.getFascicolo().getFolderStatus().equals(DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONE_AUTOVALUTAZIONI)
						||
						showFolderPageBean.getFascicolo().getFolderStatus().equals(DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONE_DOCUMENTI)
						||
						showFolderPageBean.getFascicolo().getFolderStatus().equals(DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONE_DOCUMENTI_AUTOVALUTAZIONI)
						||
						showFolderPageBean.getFascicolo().getFolderStatus().equals(DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONE_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE)
				)
				&& numUdo > 0 && numAllegati > 0 && isTuttiRequisitiRisposti && domandaInstDao.checkUdoSenzaPostiLettoInDomanda(showFolderPageBean.getFascicolo().getDomandaClienId())
			)
			showFolderPageBean.getFascicolo().setReinviabile(true);
		else
			showFolderPageBean.getFascicolo().setReinviabile(false);
	}*/
	
	private void calcolaSeInviabileReinviabile(ShowFolderPageBean showFolderPageBean) {
		showFolderPageBean.getFascicolo().setProntoPerInvio(false);
		showFolderPageBean.getFascicolo().setReinviabile(false);
		showFolderPageBean.setMotivoBloccoInvioReinvioDomandaEnum(MotivoBloccoInvioReinvioDomandaEnum.NonApplicabile);
		//Se non e' una domanda non controllo altro
		if (!FolderDao.DOMANDA_TITOLARE_FOLDER_TYPE.equals(showFolderPageBean.getFascicolo().getFolderType())) { 
			return;
		}
		InvioReinvioTypeEnum invioReinvioType = InvioReinvioTypeEnum.Nessuno;
		if( DomandaInstDao.STATO_BOZZA.equals(showFolderPageBean.getFascicolo().getFolderStatus()) ) {
			invioReinvioType = InvioReinvioTypeEnum.Invio;
		} else if ( DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONE_AUTOVALUTAZIONI.equals(showFolderPageBean.getFascicolo().getFolderStatus())
						||
						DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONE_DOCUMENTI.equals(showFolderPageBean.getFascicolo().getFolderStatus())
						||
						DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONE_DOCUMENTI_AUTOVALUTAZIONI.equals(showFolderPageBean.getFascicolo().getFolderStatus())
						||
						DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONE_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE.equals(showFolderPageBean.getFascicolo().getFolderStatus())
			) {
			invioReinvioType = InvioReinvioTypeEnum.Reinvio;
		}
		
		int numUdo = 0;
		boolean isTuttiRequisitiRisposti = true;
		if(showFolderPageBean.getFascicolo().getUdoUoInstForL() != null) {
			numUdo = showFolderPageBean.getFascicolo().getUdoUoInstForL().size();
			for(UdoUoInstForList udoUoIL : showFolderPageBean.getFascicolo().getUdoUoInstForL()) {
				if(!udoUoIL.isRequisitiRisposti()) {
					isTuttiRequisitiRisposti = false;
					break;
				}
			}
		}
		
		if(invioReinvioType == InvioReinvioTypeEnum.Nessuno)
			return;
		
		if(numUdo == 0) {
			showFolderPageBean.setMotivoBloccoInvioReinvioDomandaEnum(MotivoBloccoInvioReinvioDomandaEnum.NessunaUdo);
		} else {
			int numAllegati = 0;
			if (showFolderPageBean.getDocumentsL() != null)
				numAllegati = showFolderPageBean.getDocumentsL().size();
			if ( numAllegati == 0) 
				showFolderPageBean.setMotivoBloccoInvioReinvioDomandaEnum(MotivoBloccoInvioReinvioDomandaEnum.MancanzaAllegati);
			else {
				if(!isTuttiRequisitiRisposti)
					showFolderPageBean.setMotivoBloccoInvioReinvioDomandaEnum(MotivoBloccoInvioReinvioDomandaEnum.RisposteRequisitiIncomplete);
				else {
					if(!domandaInstDao.checkUdoSenzaPostiLettoInDomanda(showFolderPageBean.getFascicolo().getDomandaClienId()))
						showFolderPageBean.setMotivoBloccoInvioReinvioDomandaEnum(MotivoBloccoInvioReinvioDomandaEnum.UdoSenzaPostiLettoInDomanda);
					else if(!domandaInstDao.checkUdoTipoModuloSenzaPostiLettoInDomanda(showFolderPageBean.getFascicolo().getDomandaClienId()))
						showFolderPageBean.setMotivoBloccoInvioReinvioDomandaEnum(MotivoBloccoInvioReinvioDomandaEnum.UdoTipoModuloSenzaPostiLettoInDomanda);
					else {
						if(invioReinvioType == InvioReinvioTypeEnum.Invio) {
							//devo fare un altro controllo
							//if(!domandaInstDao.checkAltreDomandeNonConcluseConStesseUoUdo(showFolderPageBean.getFascicolo().getDomandaClienId()))
							//	showFolderPageBean.setMotivoBloccoInvioReinvioDomandaEnum(MotivoBloccoInvioReinvioDomandaEnum.AltreDomandeNonConcluseConStesseUoUdo);
							if(!domandaInstDao.checkAltreDomandeNonConcluseConStesseUdo(showFolderPageBean.getFascicolo().getDomandaClienId()))
								showFolderPageBean.setMotivoBloccoInvioReinvioDomandaEnum(MotivoBloccoInvioReinvioDomandaEnum.AltreDomandeNonConcluseConStesseUdo);
						}
					}
				}
			}
			
		}
		
		if(showFolderPageBean.getMotivoBloccoInvioReinvioDomandaEnum() == MotivoBloccoInvioReinvioDomandaEnum.NonApplicabile) {
			if(invioReinvioType == InvioReinvioTypeEnum.Invio) {
				//Invio domanda
				showFolderPageBean.getFascicolo().setProntoPerInvio(true);
			} else {
				//Reinvio domanda
				showFolderPageBean.getFascicolo().setReinviabile(true);
			}
		}
	}
	
	/*public void calcolaSeEliminabile(ShowFolderPageBean showFolderPageBean) {
		if (showFolderPageBean.getFascicolo().getFolderStatus().equals(DomandaInstDao.STATO_BOZZA) && showFolderPageBean.getFascicolo().getFolderType().equals(FolderDao.DOMANDA_TITOLARE_FOLDER_TYPE))
			showFolderPageBean.getFascicolo().setEliminabile(true);
		else
			showFolderPageBean.getFascicolo().setEliminabile(false);
	}*/
	
	/*public void calcolaSeValutabile(ShowFolderPageBean showFolderPageBean) {
		if (((showFolderPageBean.getFascicolo().getFolderStatus().equals(DomandaInstDao.STATO_AVVIATO))||(showFolderPageBean.getFascicolo().getFolderStatus().equals(DomandaInstDao.STATO_INVIATA))) && showFolderPageBean.getFascicolo().getFolderType().equals(FolderDao.DOMANDA_TITOLARE_FOLDER_TYPE))
			showFolderPageBean.getFascicolo().setValutabile(true);
		else
			showFolderPageBean.getFascicolo().setValutabile(false);
	}*/
	
	/*
	public void calcolaSeModificabilePerValtuazioneRegione(ShowFolderPageBean showFolderPageBean) {
		if (((showFolderPageBean.getFascicolo().getFolderStatus().equals(DomandaInstDao.STATO_FASE_ISTRUTTORIA))) && showFolderPageBean.getFascicolo().getFolderType().equals(FolderDao.DOMANDA_TITOLARE_FOLDER_TYPE))
			showFolderPageBean.getFascicolo().setModificabilePerValtuazioneRegione(true);
		else
			showFolderPageBean.getFascicolo().setModificabilePerValtuazioneRegione(false);
	}*/
	
	/*public void calcolaSeModificabilePerIntegrazioni(ShowFolderPageBean showFolderPageBean) {
		if (((showFolderPageBean.getFascicolo().getFolderStatus().equals(DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONI))) && showFolderPageBean.getFascicolo().getFolderType().equals(FolderDao.DOMANDA_TITOLARE_FOLDER_TYPE))
			showFolderPageBean.getFascicolo().setModificabilePerIntegrazioni(true);
		else
			showFolderPageBean.getFascicolo().setModificabilePerIntegrazioni(false);
	}*/
	
	/*public void calcolaSeRichiedibileDiIntegrazioni(ShowFolderPageBean showFolderPageBean) {
		if (((showFolderPageBean.getFascicolo().getFolderStatus().equals(DomandaInstDao.STATO_AVVIATO))|| (showFolderPageBean.getFascicolo().getFolderStatus().equals(DomandaInstDao.STATO_INVIATA)) || (showFolderPageBean.getFascicolo().getFolderStatus().equals(DomandaInstDao.STATO_FASE_ISTRUTTORIA))) && showFolderPageBean.getFascicolo().getFolderType().equals(FolderDao.DOMANDA_TITOLARE_FOLDER_TYPE))
			showFolderPageBean.getFascicolo().setRichiedibileDiIntegrazioni(true);
		else
			showFolderPageBean.getFascicolo().setRichiedibileDiIntegrazioni(false);
	}*/

	public void calcolaSeConcludibile(ShowFolderPageBean showFolderPageBean) {
		int numAllegati = 0;
		if (showFolderPageBean.getDocumentsL() != null)
			numAllegati = showFolderPageBean.getDocumentsL().size();
		if (
				DomandaInstDao.STATO_INSERIMENTO_ESITO.equals(showFolderPageBean.getFascicolo().getFolderStatus())
					&& FolderDao.DOMANDA_TITOLARE_FOLDER_TYPE.equals(showFolderPageBean.getFascicolo().getFolderType())  
					&& numAllegati > 0
			) {
			showFolderPageBean.getFascicolo().setConcludibile(showFolderPageBean.isEsitiCompleti());
			/*boolean tuttiGliEsitiInseriti = showFolderPageBean.isEsitiCompleti();
			if (tuttiGliEsitiInseriti) {
				showFolderPageBean.getFascicolo().setConcludibile(showFolderPageBean.isEsitiUoCongruentiUdoFiglie());
			}
			else
				showFolderPageBean.getFascicolo().setConcludibile(false);*/
		} else if (
				DomandaInstDao.STATO_INSERIMENTO_ESITO_VERIFICA.equals(showFolderPageBean.getFascicolo().getFolderStatus())
				&& FolderDao.DOMANDA_TITOLARE_FOLDER_TYPE.equals(showFolderPageBean.getFascicolo().getFolderType()) 
				&& numAllegati > 0
			) {
			showFolderPageBean.getFascicolo().setConcludibile(true);
		}
		else
			showFolderPageBean.getFascicolo().setConcludibile(false);
	}
	
	public void checkAllUoUdo(ShowFolderPageBean showFolderPageBean) {
		for (int i=0; i<showFolderPageBean.getFilteredUdoUoInstForL().size(); i++) {
			checkUdo(showFolderPageBean, i, showFolderPageBean.isAllChecked());
		}
	}	
	
	public void setActiveRowIndex(ShowFolderPageBean showFolderPageBean, int index, boolean changeCheck, boolean checked) {
		log.debug("Valore di changeCheck: " + changeCheck);
		showFolderPageBean.setActiveRowIndex(index);
		
		if (changeCheck) {
			log.debug("Valore di checked: " + checked);
			checkUdo(showFolderPageBean, index, checked);
		}

	}
	
	public void checkUdo(ShowFolderPageBean showFolderPageBean, int index, boolean checked) {
		UdoUoInstForList udo = showFolderPageBean.getFilteredUdoUoInstForL().get(index);
		if(checked) {
			//Aggiungo la selezione
			if(udo.isUo()) {
				//UOInst
				//showFolderPageBean.getCheckM().put(udo.getClientId(), "");
				showFolderPageBean.getCheckM().put(udo.getClientId(), new AbstractMap.SimpleEntry<String, Boolean>("", false));
				//memorizzo anche le UdoInst.ClientId figlie della UOInst selezionata
				for(UdoUoInstForList udoUoInstFL : showFolderPageBean.getFascicolo().getUdoUoInstForL()) {
					if(udoUoInstFL.isUdo() && udoUoInstFL.getUoClientId().equals(udo.getUoClientId())) {
						showFolderPageBean.getCheckM().put(udoUoInstFL.getClientId(), new AbstractMap.SimpleEntry<String, Boolean>(udoUoInstFL.getUoClientId(), false));
					}
				}
			}
			else if (udo.isUdo()) { 
				showFolderPageBean.getCheckM().put(udo.getClientId(), new AbstractMap.SimpleEntry<String, Boolean>(udo.getUoClientId(), false));
			}
			else {
				//RequisitiGenerealiAziendali
				showFolderPageBean.getCheckM().put(udo.getClientId(), new AbstractMap.SimpleEntry<String, Boolean>("", true));
			}
		} else {
			//rimuovo la selezione se presente
			if (showFolderPageBean.getCheckM().containsKey(udo.getClientId())) {
				showFolderPageBean.getCheckM().remove(udo.getClientId());
			}			
		}
		
	}

	private byte[] buildRicevutaInvioDomandaPDF(String data, String operatoreTitolare, String ragioneSocialeTitolare, String codiceTitolare, String direzione, String numeroProcedimento) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("DATA", data);
		parameters.put("OPERATORE_TITOLARE", operatoreTitolare);
		parameters.put("RAGIONE_SOCIALE_TITOLARE", ragioneSocialeTitolare);
		parameters.put("CODICE_TITOLARE", codiceTitolare);
		parameters.put("DIREZIONE", direzione);
		parameters.put("NUMERO_PROCEDIMENTO", numeroProcedimento);
		
		String jasperFile = GenericUtils.getResourceUrl("ricevuta_invio_domanda.jasper").getFile();		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile, parameters, new JREmptyDataSource());
		
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}
	
	
	public void aggiornaOggetto(ShowFolderPageBean showFolderPageBean, ExtraWayService xwService, ExternalContext externalContext, MessageContext messageContext, UserBean userBean, ExtraWayService _xwService) throws Exception {
		//recupero il documento
		XMLDocumento document = null;
		try {
			document = xwService.loadDocument(Integer.parseInt(showFolderPageBean.getFascicolo().getIdUnit()), false, false, false, false); //primo tentativo (caricacamento inset)
		}
		catch (Exception e) {
			document = xwService.loadDocument(Integer.parseInt(showFolderPageBean.getFascicolo().getIdUnit()), false, true, false, false); //secondo tentativo (caricamento outset) nel caso si provenga da inserimento di nuovo fascicolo
		}
		String idIUnit = document.getAttributeValue("/Response/Document/@idIUnit");
		document = ExtraWayService.extractDocumentFromEnvironment(document);

		if(utilService.isGestioneRequisitiDiversificatiPerTipoDomandaActiveForDomandaCreationDate(showFolderPageBean.getDomandaCreation(), showFolderPageBean.getTitolareDomanda())) {
			//Per la domanda di cui si sta modificando l'oggetto è attiva la gestione dei requisiti deiversificati
			//carico l'oggetto precedente della domanda
			String prevTipoDomanda = document.getAttributeValue("//fascicolo/extra/procedimento/@oggetto_domanda", "");
			String newTipoDomanda = showFolderPageBean.getFascicolo().getOggettoDomanda();
			//se attiva la gestione dei requisiti deiversificati la domanda ha sicuramente il campo "Tipo domanda" impostato
			if(!prevTipoDomanda.equals(newTipoDomanda)) {
				//Il Tipo domanda è cambiato
				String domandaClientId = showFolderPageBean.getFascicolo().getDomandaClienId();
				if(utilService.TIPO_DOMANDA_COMPLESSIVA.equals(newTipoDomanda)) {
					//"Complessiva" devo aggiungere i requisiti
					mainService.inserisciRequisitiGeneraliAndUO(messageContext, _xwService, userBean, showFolderPageBean);
				} else {
					//"Parziale" devo rimuovere i requisiti
					requisitoInstDao.deleteAllRequisitiForAllUoDomanda(domandaClientId);
					requisitoInstDao.deleteAllRequisitiGenerealiDomanda(domandaClientId);
				}
				//Ricarico la domanda
				DomandaInst domanda = caricaDatiDomandaInst(externalContext, messageContext, showFolderPageBean, userBean, null);
				//Se ci sono requisiti generali di tipo SR vanno aggiornate le valutazioni, valutazioni verificatore automatiche
				utilService.aggiornaRequisitiSrDomanda(domanda, false);				
				//Aggiorno il bean showFolderPageBean
				calcolaSeInviabileReinviabile(showFolderPageBean);
				calcolaSeConcludibile(showFolderPageBean); //esiti
				//Controlla se ci sono tutti i TipiUdo22 Richiesti
				//calcolaTipoUdo22MancantiDomanda(showFolderPageBean);
			}
		}
		
		//cambio l'oggetto della domanda
		//extra.procedimento.@oggetto_domanda
		document.removeXPath("//fascicolo/extra/procedimento/@oggetto_domanda");
		document.insertXPath(".extra.procedimento.@oggetto_domanda", showFolderPageBean.getFascicolo().getOggettoDomanda());
		
		XMLDocumento reloadedDocument = new XMLDocumento(xwService.saveDocument(document, true, Integer.parseInt(idIUnit)));
		
	}

	public void inviaDomanda(ShowFolderPageBean showFolderPageBean, ExtraWayService xwService, ExternalContext externalContext, MessageContext messageContext, UserBean userBean, ExtraWayService _xwService) throws Exception {
		//recupero il documento
		XMLDocumento document = null;
		try {
			document = xwService.loadDocument(Integer.parseInt(showFolderPageBean.getFascicolo().getIdUnit()), false, false, false, false); //primo tentativo (caricacamento inset)
		}
		catch (Exception e) {
			document = xwService.loadDocument(Integer.parseInt(showFolderPageBean.getFascicolo().getIdUnit()), false, true, false, false); //secondo tentativo (caricamento outset) nel caso si provenga da inserimento di nuovo fascicolo
		}
		String idIUnit = document.getAttributeValue("/Response/Document/@idIUnit");
		document = ExtraWayService.extractDocumentFromEnvironment(document);
		String statoAttuale = document.getAttributeValue("//fascicolo/extra/@folder_status");
		String statoDaInserire = DomandaInstDao.STATO_AVVIATO;
		
		//VERRA' GESTITO NEL WORKFLOW 

		//if(statoAttuale.equals(DomandaInstDao.STATO_BOZZA))
		//	statoDaInserire = DomandaInstDao.STATO_AVVIATO;
		
		//if(statoAttuale.equals(DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONI))
		//	statoDaInserire = DomandaInstDao.STATO_INVIATA;
		
		//cambio lo stato del procedimento nel fascicolo
		document.removeXPath("//fascicolo/extra/@folder_status");
		if (statoAttuale.equals(DomandaInstDao.STATO_BOZZA)){
			//TODO bisogna capire se questa data deve essere risettata o meno, ne ho parlato con elisa, al momento sembra di no. Tuttavia il fatto che manca una storicizzazione e' critico
			//in questo caso inseriamo anche la data
			SimpleDateFormat formatoPerXml = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat formatoPerSalvataggio = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat yearSDF = new SimpleDateFormat("yyyy");
			Date now = new Date();
			document.insertXPath(".extra.procedimento.@data_invio_domanda", formatoPerXml.format(now));
			showFolderPageBean.getFascicolo().setDataInvioDomanda(formatoPerSalvataggio.format(now));
			
			//generazione del numero di procedimento via extraway serial
			String numeroProcedimento = yearSDF.format(now) + "..";
			document.insertXPath(".extra.procedimento.@numero_procedimento", numeroProcedimento);
		}
		document.insertXPath(".extra.@folder_status", statoDaInserire);
		
		XMLDocumento reloadedDocument = new XMLDocumento(xwService.saveDocument(document, true, Integer.parseInt(idIUnit)));
		String numeroProcedimento = reloadedDocument.getAttributeValue("//extra/procedimento/@numero_procedimento");
		
		showFolderPageBean.getFascicolo().setNumeroProcedimento(FolderDao.formatNumeroProcedimento(numeroProcedimento));
		showFolderPageBean.getFascicolo().setFolderStatus(statoDaInserire);
		//showFolderPageBean.getFascicolo().setModificabilePerIntegrazioni(false);
		showFolderPageBean.getFascicolo().setProntoPerInvio(false);
		showFolderPageBean.getFascicolo().setReinviabile(false);
		//showFolderPageBean.getFascicolo().setEliminabile(false);
		//showFolderPageBean.getFascicolo().setInBozza(false);
		
		//cambio lo stato nel procedimento nella domanda 
		String domandaClientId = document.getAttributeValue("//extra/procedimento/@domanda_client_id");
		DomandaInst domanda = domandaInstDao.findOne(domandaClientId);
		domanda.setStato(statoDaInserire);
		domanda.setNumeroProcedimento(numeroProcedimento);
		domandaInstDao.save(domanda);
		
		//upload di ricevuta PDF - report (nel caso di invio da bozza)
		if (statoAttuale.equals(DomandaInstDao.STATO_BOZZA)){
			TitolareModel titolare = titolareModelDao.findByClientId(userBean.getTitolareClientId());
			String codiceTitolare = "P.IVA " + titolare.getPiva();
			if (codiceTitolare == null || codiceTitolare.length() == 0)
				codiceTitolare = "C.F. " + titolare.getCfisc();
			byte[] ricevuta_invio_domanda_pdf = buildRicevutaInvioDomandaPDF(showFolderPageBean.getFascicolo().getDataInvioDomanda(), 
					userBean.getUtenteModel().getAnagraficaUtenteModel().getCognome() + " " + userBean.getUtenteModel().getAnagraficaUtenteModel().getNome(), 
					titolare.getRagSoc(), codiceTitolare, (titolare.getDirezioneTempl()==null)?"":titolare.getDirezioneTempl().getNome(), showFolderPageBean.getFascicolo().getNumeroProcedimento());
			XMLDocumento uploadedDocumento = DocumentoDao.creaDocumento(_xwService, "Ricevuta di Invio Domanda", userBean.getUtenteModel().getLoginDbOrCas(), userBean.getRuolo(), "", null);
			String uploadedDocumentIdIUnit = uploadedDocumento.getAttributeValue("/Response/Document/@idIUnit", "");
			xwService.checkInContentFile(Integer.parseInt(uploadedDocumentIdIUnit), "ricevuta_invio_domanda.pdf", ricevuta_invio_domanda_pdf);
			DocumentoDao.aggiungiDocAFolder(xwService, idIUnit, uploadedDocumentIdIUnit);						
		}

		//refresh della lista dei documenti del fascicolo
		showFolderPageBean.setAllDocumentsL(getFascicoloDocumentsL(externalContext, userBean, document, _xwService));
		
	}
	
	
	//TODO ragionare se unire i metodi valutaDomanda e richiestaIntegrazioni
	public void valutaDomanda(ShowFolderPageBean showFolderPageBean, MessageContext messageContext, ExtraWayService xwService) throws Exception {
		//recupero il documento
		XMLDocumento document = null;
		try {
			document = xwService.loadDocument(Integer.parseInt(showFolderPageBean.getFascicolo().getIdUnit()), false, false, false, false); //primo tentativo (caricacamento inset)
		}
		catch (Exception e) {
			document = xwService.loadDocument(Integer.parseInt(showFolderPageBean.getFascicolo().getIdUnit()), false, true, false, false); //secondo tentativo (caricamento outset) nel caso si provenga da inserimento di nuovo fascicolo
		}
		String idIUnit = document.getAttributeValue("/Response/Document/@idIUnit");
		document = ExtraWayService.extractDocumentFromEnvironment(document);
		
		//cambio lo stato del procedimento nel fascicolo
		String statoRimosso = document.getAttributeValue("//fascicolo/extra/@folder_status");
		document.removeXPath("//fascicolo/extra/@folder_status");
		
		document.insertXPath(".extra.@folder_status", DomandaInstDao.STATO_FASE_ISTRUTTORIA);
		xwService.saveDocument(document, true, Integer.parseInt(idIUnit));
		showFolderPageBean.getFascicolo().setFolderStatus(DomandaInstDao.STATO_FASE_ISTRUTTORIA);
		//showFolderPageBean.getFascicolo().setModificabilePerValtuazioneRegione(true);
		
		//cambio lo stato nel procedimento nella domanda 
		String domandaClientId = document.getAttributeValue("//extra/procedimento/@domanda_client_id");
		DomandaInst domanda = domandaInstDao.findOne(domandaClientId);
		domanda.setStato(DomandaInstDao.STATO_FASE_ISTRUTTORIA);
		domandaInstDao.save(domanda);
		
		//showFolderPageBean.getFascicolo().setValutabile(false);
		
		try {
			//mail
			if(xwService.getAbilitaInvioMail().equals("S")){
				Parametri mailSubject = null;
				Parametri mailBody = null;
				if (statoRimosso.equals(DomandaInstDao.STATO_AVVIATO)){
					mailSubject = parametriDao.findOne(ParametriDao.NOTIFICA_INIZIO_VALUTAZIONE_SUBJECT);
					mailBody = parametriDao.findOne(ParametriDao.NOTIFICA_INIZIO_VALUTAZIONE_BODY);
				}
				if (statoRimosso.equals(DomandaInstDao.STATO_INVIATA)){
					mailSubject = parametriDao.findOne(ParametriDao.NOTIFICA_RICEZIONE_DOMANDA_DOPO_INTEGRAZIONE_SUBJECT);
					mailBody = parametriDao.findOne(ParametriDao.NOTIFICA_RICEZIONE_DOMANDA_DOPO_INTEGRAZIONE_BODY);
				}
				String idProcedimento = document.getAttributeValue("//fascicolo/extra/procedimento/@domanda_client_id", "");
				TitolareModel  titolareDelProcedimento = domandaInstDao.findOne(idProcedimento).getTitolareModel();
				String mailTitolare = titolareDelProcedimento.getEmail();
				
				Mail mailATitolare = new Mail(xwService.getSmtpHost(), xwService.getSmtpPort(), xwService.getSmtpProtocol(), xwService.getSmtpUser(), xwService.getSmtpPwd());
				String mailSender = xwService.getMailPrincipale();;
				String mailReceiver = null;
				if (!xwService.getMailDebug().isEmpty())
					mailReceiver = xwService.getMailDebug();
				else
					mailReceiver = mailTitolare;
				
				String mailSubjectToSend = mailSubject.getValore();
				String mailBodyToSend = mailBody.getValore();
		
				mailATitolare.sendMail(mailSubjectToSend, mailBodyToSend, mailSender, mailSender, mailReceiver);
			}
		}
		catch (Exception e) { //errore di invio email non deve essere bloccante
			Logger.getRootLogger().error(e);
			MessageBuilder mB = new MessageBuilder();
			messageContext.addMessage(mB.error().source("messages").defaultText("Si è verificato un problema nell'invio di una email di notifica").build());
		}		
		
	}

	/*
	public void richiestaIntegrazioniDomanda(ShowFolderPageBean showFolderPageBean, MessageContext messageContext, ExtraWayService xwService) throws Exception {
		//recupero il documento
		XMLDocumento document = null;
		try {
			document = xwService.loadDocument(Integer.parseInt(showFolderPageBean.getFascicolo().getIdUnit()), false, false, false, false); //primo tentativo (caricacamento inset)
		}
		catch (Exception e) {
			document = xwService.loadDocument(Integer.parseInt(showFolderPageBean.getFascicolo().getIdUnit()), false, true, false, false); //secondo tentativo (caricamento outset) nel caso si provenga da inserimento di nuovo fascicolo
		}
		String idIUnit = document.getAttributeValue("/Response/Document/@idIUnit");
		document = ExtraWayService.extractDocumentFromEnvironment(document);
		
		//cambio lo stato del procedimento nel fascicolo
		document.removeXPath("//fascicolo/extra/@folder_status");
		document.insertXPath(".extra.@folder_status", DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONI);
		xwService.saveDocument(document, true, Integer.parseInt(idIUnit));
		showFolderPageBean.getFascicolo().setFolderStatus(DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONI);
		showFolderPageBean.getFascicolo().setModificabilePerValtuazioneRegione(false);
		
		//cambio lo stato nel procedimento nella domanda 
		String domandaClientId = document.getAttributeValue("//extra/procedimento/@domanda_client_id");
		DomandaInst domanda = domandaInstDao.findOne(domandaClientId);
		domanda.setStato(DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONI);
		domandaInstDao.save(domanda);
		
		showFolderPageBean.getFascicolo().setValutabile(false);
		showFolderPageBean.getFascicolo().setRichiedibileDiIntegrazioni(false);

		//mail
		try {
			if(xwService.getAbilitaInvioMail().equals("S")){
				Parametri mailSubject = parametriDao.findOne(ParametriDao.NOTIFICA_RICHIESTA_INTEGRAZIONE_SUBJECT);
				Parametri mailBody = parametriDao.findOne(ParametriDao.NOTIFICA_RICHIESTA_INTEGRAZIONE_BODY);
						
				String idProcedimento = document.getAttributeValue("//fascicolo/extra/procedimento/@domanda_client_id", "");
				TitolareModel  titolareDelProcedimento = domandaInstDao.findOne(idProcedimento).getTitolareModel();
				String mailTitolare = titolareDelProcedimento.getEmail();
						
				Mail mailATitolare = new Mail(xwService.getSmtpHost(), xwService.getSmtpPort(), xwService.getSmtpProtocol(), xwService.getSmtpUser(), xwService.getSmtpPwd());
				String mailSender = xwService.getMailPrincipale();
				String mailReceiver = null;
				if (!xwService.getMailDebug().isEmpty())
					mailReceiver = xwService.getMailDebug();
				else
					mailReceiver = mailTitolare;
						
				String mailSubjectToSend = mailSubject.getValore();
				String mailBodyToSend = mailBody.getValore();
		
				mailATitolare.sendMail(mailSubjectToSend, mailBodyToSend, mailSender, mailSender, mailReceiver);		
			}
		}
		catch (Exception e) { //errore di invio email non deve essere bloccante
			Logger.getRootLogger().error(e);
			MessageBuilder mB = new MessageBuilder();
			messageContext.addMessage(mB.error().source("messages").defaultText("Si è verificato un problema nell'invio di una email di notifica").build());
		}		
		
	}
	*/
	
	public void chiudiDomanda(ShowFolderPageBean showFolderPageBean, MessageContext messageContext, ExtraWayService xwService) throws Exception {
		//recupero il documento
		XMLDocumento document = null;
		try {
			document = xwService.loadDocument(Integer.parseInt(showFolderPageBean.getFascicolo().getIdUnit()), false, false, false, false); //primo tentativo (caricacamento inset)
		}
		catch (Exception e) {
			document = xwService.loadDocument(Integer.parseInt(showFolderPageBean.getFascicolo().getIdUnit()), false, true, false, false); //secondo tentativo (caricamento outset) nel caso si provenga da inserimento di nuovo fascicolo
		}
		String idIUnit = document.getAttributeValue("/Response/Document/@idIUnit");
		document = ExtraWayService.extractDocumentFromEnvironment(document);
		
		//cambio lo stato del procedimento nel fascicolo
		document.removeXPath("//fascicolo/extra/@folder_status");
		document.insertXPath(".extra.@folder_status", DomandaInstDao.PROCEDIMENTO_CONCLUSO);
		showFolderPageBean.getFascicolo().setFolderStatus(DomandaInstDao.PROCEDIMENTO_CONCLUSO);
		//showFolderPageBean.getFascicolo().setModificabilePerValtuazioneRegione(false);
		
		//inserisco la data nella conclusione
		SimpleDateFormat formatoPerXml = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat formatoPerSalvataggio = new SimpleDateFormat("dd/MM/yyyy");
		Date now = new Date();
		document.insertXPath(".extra.procedimento.@data_conclusione", formatoPerXml.format(now));
		showFolderPageBean.getFascicolo().setDataConclusione(formatoPerSalvataggio.format(now));
		
		xwService.saveDocument(document, true, Integer.parseInt(idIUnit));
		
		//cambio lo stato nel procedimento nella domanda 
		String domandaClientId = document.getAttributeValue("//extra/procedimento/@domanda_client_id");
		DomandaInst domanda = domandaInstDao.findOne(domandaClientId);
		domanda.setStato(DomandaInstDao.PROCEDIMENTO_CONCLUSO);
		domandaInstDao.save(domanda);

		for (UdoUoInstForList udoUoInstForInst:showFolderPageBean.getFascicolo().getUdoUoInstForL()) {
			if(udoUoInstForInst.isUdo()) {
				UdoModel udoModel = udoModelDao.findOne(udoUoInstForInst.getClientId());
				udoModel.setProcedimentoInCorso("N");
				udoModelDao.save(udoModel);
			}
			else {
				
			}
		}
		
		//showFolderPageBean.getFascicolo().setValutabile(false);
		//showFolderPageBean.getFascicolo().setRichiedibileDiIntegrazioni(false);
		showFolderPageBean.getFascicolo().setConcludibile(false);
		
		//mail
		try {
			if(xwService.getAbilitaInvioMail().equals("S")){
				Parametri mailSubject = parametriDao.findOne(ParametriDao.NOTIFICA_CHIUSURA_PROCEDIMENTO_SUBJECT);
				Parametri mailBody = parametriDao.findOne(ParametriDao.NOTIFICA_CHIUSURA_PROCEDIMENTO_BODY);
					
				String idProcedimento = document.getAttributeValue("//fascicolo/extra/procedimento/@domanda_client_id", "");
				TitolareModel  titolareDelProcedimento = domandaInstDao.findOne(idProcedimento).getTitolareModel();
				String mailTitolare = titolareDelProcedimento.getEmail();
			
				Mail mailATitolare = new Mail(xwService.getSmtpHost(), xwService.getSmtpPort(), xwService.getSmtpProtocol(), xwService.getSmtpUser(), xwService.getSmtpPwd());
				String mailSender = xwService.getMailPrincipale();
				String mailReceiver = null;
				if (!xwService.getMailDebug().isEmpty())
					mailReceiver = xwService.getMailDebug();
				else
					mailReceiver = mailTitolare;
				
				String mailSubjectToSend = mailSubject.getValore();
				String mailBodyToSend = mailBody.getValore();
		
				mailATitolare.sendMail(mailSubjectToSend, mailBodyToSend, mailSender, mailSender, mailReceiver);
			}
		}
		catch (Exception e) { //errore di invio email non deve essere bloccante
			Logger.getRootLogger().error(e);
			MessageBuilder mB = new MessageBuilder();
			messageContext.addMessage(mB.error().source("messages").defaultText("Si è verificato un problema nell'invio di una email di notifica").build());
		}		
		
	}
	
	public void eliminaDomanda(ShowFolderPageBean showFolderPageBean, ExtraWayService xwService) throws Exception {
		//recupero il documento
		XMLDocumento document = null;
		try {
			document = xwService.loadDocument(Integer.parseInt(showFolderPageBean.getFascicolo().getIdUnit()), false, false, false, false); //primo tentativo (caricacamento inset)
		}
		catch (Exception e) {
			document = xwService.loadDocument(Integer.parseInt(showFolderPageBean.getFascicolo().getIdUnit()), false, true, false, false); //secondo tentativo (caricamento outset) nel caso si provenga da inserimento di nuovo fascicolo
		}
		String idIUnit = document.getAttributeValue("/Response/Document/@idIUnit");
		document = ExtraWayService.extractDocumentFromEnvironment(document);
		
		//cambio lo stato nel procedimento nella domanda 
		String domandaClientId = document.getAttributeValue("//extra/procedimento/@domanda_client_id");
		DomandaInst domanda = domandaInstDao.findOne(domandaClientId);
		domanda.getUdoInsts().size();
		
		//Modifico gli stati delle UdoModel e delle UoModel
		for(UoInst uoInst:domanda.getUoInsts()) {
			UoModel uoModel = uoModelDao.findByClientId(uoInst.getUoModelClientid());
			if(uoModel != null) {
				uoModel.setProcedimentoInCorso("N");
				uoModelDao.saveProcedimentoInCorso(uoModel);
			}
		}
		
		for(UdoInst udoInst:domanda.getUdoInsts()) {
			UdoModel udoModel = udoModelDao.findByClientId(udoInst.getUdoModelClientid());
			if(udoModel != null) {
				udoModel.setProcedimentoInCorso("N");
				udoModelDao.save(udoModel);
			}
		}

		//dichiaro il documento eliminato
		document.removeXPath("//fascicolo/extra/@folder_status");
		document.insertXPath(".extra.@folder_status", DomandaInstDao.ELIMINATO);
		document.removeXPath("//fascicolo/extra/@folder_type");
		xwService.saveDocument(document, true, Integer.parseInt(idIUnit));
		
		domandaInstDao.delete(domanda);
	}
	
	private String getTitolareFolderSubject(XMLDocumento document, ExtraWayService xwService) throws Exception {
		String numeroFascicolo = document.getAttributeValue("//fascicolo/@numero", "");
		if (FolderDao.isSubFolder(document)) { //sottofascicolo
			numeroFascicolo = FolderDao.getRootFolderCode(numeroFascicolo);
			document = FolderDao.findFolder(xwService, numeroFascicolo);
		}
		return document.getElementText("//fascicolo/oggetto", "");
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getFascicoloDocumentsL(ExternalContext externalContext, UserBean userBean, XMLDocumento document, ExtraWayService _xwService) throws Exception {
		String numeroFascicolo = document.getAttributeValue("//fascicolo/@numero", "");
		XMLDocumento titleDocument = FolderDao.findFolderDocuments(_xwService, numeroFascicolo);
		
		if (titleDocument == null) //nessun documento contenuto nel fascicolo
			return null;
		
		List<Map<String,String>> documentsL = new ArrayList<Map<String,String>>();
		
		List<Element> itemsL = titleDocument.selectNodes("//Item");
		String wf_processinstance_id;
		String tipo;
		String tipoDoc;
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
			map.put("creazione_data_formatted", GenericUtils.dateFormat(titleV.get(3).trim()));
			map.put("idIUnit", item.attributeValue("idIUnit"));
			wf_processinstance_id = titleV.get(56).trim();
			if(wf_processinstance_id != null && !wf_processinstance_id.isEmpty()) {
				map.put("wf_processinstance_id", wf_processinstance_id);
				map.put("wf_archivedtask_id", titleV.get(57).trim());
			}
			//tipo specifica per gli allegati da workflow se sono visibili solo alla regione oppure anche ai titolari
			tipo = titleV.get(58).trim();
			if(tipo != null && !tipo.isEmpty()) {
				map.put("tipo", tipo);
			} else {
				//I documenti allegati dal titolare possono anche avere un tipoDoc
				tipoDoc = titleV.get(66).trim();
				if(tipoDoc != null && !tipoDoc.isEmpty()) {
					map.put("tipo_doc", tipoDoc);
				} else {
					map.put("tipo_doc", "Altro");
				}
			}
			

			HttpServletRequest httpServletRequest = (HttpServletRequest)externalContext.getNativeRequest();
			String sessionId = httpServletRequest.getSession().getId();
			map.put("session_id", Base64.encodeBase64String(sessionId.getBytes()));
			
			//check sulla cancellabilità del documento
			map.put("isDeletable", "false");
			String folderType = document.getAttributeValue("//fascicolo/extra/@folder_type", "");
			if (folderType.equals(FolderDao.DOMANDA_TITOLARE_FOLDER_TYPE)) { //se è un fascicolo di un procedimento (domanda del titolare)
				String folderStatus = document.getAttributeValue("//fascicolo/extra/@folder_status", "");
				/*
				if ((userBean.isTITOLARE() || userBean.isOPERATORE_TITOLARE()) && (folderStatus.equals(DomandaInstDao.STATO_BOZZA)|| folderStatus.equals(DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONI))) {
					map.put("isDeletable", "true");
				}
				else if (userBean.isREGIONE() && !folderStatus.equals(DomandaInstDao.PROCEDIMENTO_CONCLUSO)) {
					map.put("isDeletable", "true");
				}*/
				map.put("isDeletable", Boolean.toString(aclService.userCanEditFileDomanda(userBean.getRuolo(), folderStatus)));
			}
			documentsL.add(map);			
		}
		return documentsL;
	}
	
	public void updateFascicoloDocumentsL(ExternalContext externalContext, UserBean userBean, ShowFolderPageBean showFolderPageBean, ExtraWayService _xwService) throws Exception {
		showFolderPageBean.setAllDocumentsL(getFascicoloDocumentsL(externalContext, userBean, showFolderPageBean.getFascicolo().getContents(), _xwService));
		//calcolaSeProntoPerInvio(showFolderPageBean);
		calcolaSeInviabileReinviabile(showFolderPageBean);
	}

	private void caricaAttiInstDomanda(UserBean userBean, ShowFolderPageBean showFolderPageBean) {
		if(aclService.isEditAttoInstActiveForFolder(userBean, showFolderPageBean)
				) {
			showFolderPageBean.setAttoInstDomandaL(attoInstDao.findAllAttiByDomandaInst(showFolderPageBean.getFascicolo().getDomandaClienId()));
		} else {
			showFolderPageBean.setAttoInstDomandaL(null);
		}
	}

	
	@SuppressWarnings("unchecked")
	private void caricaDatiShowFolder(ExternalContext externalContext, MessageContext messageContext, ShowFolderPageBean folderPageBean, UserBean userBean, ExtraWayService _xwService) {
		long startTime = System.currentTimeMillis();
		caricaDatiFolderExtraway(externalContext, messageContext, folderPageBean, userBean, _xwService);
		log.info("caricaDatiFolderExtraway : " + (System.currentTimeMillis() - startTime) + " ms");

		//folderPageBean.getFascicolo().getDomandaClienId()
		//per aprire una domanda dal database oracle non avendola in extraway
		//usato per testare i bug di produzione e test regionale
		//folderPageBean.getFascicolo().setDomandaClienId("09B6CAC4-0D18-4D31-9329-CDF35A506D6D");
		caricaDatiDomandaInst(externalContext, messageContext, folderPageBean, userBean, null);

		if (folderPageBean.getFascicolo().getFolderType().equals(FolderDao.ATTI_TITOLARE_FOLDER_TYPE)){
			//se il fascicolo è di tipo atti carico la lista dei tipi proc
			folderPageBean.setTipiProcL((List<TipoProcTempl>) tipoProcTemplDao.findAll());
		}
		
		startTime = System.currentTimeMillis();
		caricaDatiWorkflow(externalContext, messageContext, folderPageBean, userBean);
		log.info("caricaDatiWorkflow : " + (System.currentTimeMillis() - startTime) + " ms");
		
		HttpServletRequest httpServletRequest = (HttpServletRequest)externalContext.getNativeRequest();
		String sessionId = httpServletRequest.getSession().getId();
		folderPageBean.setSessionId(Base64.encodeBase64String(sessionId.getBytes()));

		startTime = System.currentTimeMillis();
		caricaDatiDeliberaDaWorkflow(externalContext, folderPageBean, userBean);
		log.info("caricaDatiDeliberaDaWorkflow : " + (System.currentTimeMillis() - startTime) + " ms");
		
		startTime = System.currentTimeMillis();
		caricaAttiInstDomanda(userBean, folderPageBean);
		log.info("caricaAttiInstDomanda : " + (System.currentTimeMillis() - startTime) + " ms");

		startTime = System.currentTimeMillis();
		calcolaSeInviabileReinviabile(folderPageBean);
		log.info("calcolaSeInviabileReinviabile : " + (System.currentTimeMillis() - startTime) + " ms");
		//calcolaSeProntoPerInvio(folderPageBean); //requisiti non calcolato ad ogni chiamata per questioni di performance 
		//calcolaSeReinviabile(folderPageBean); //requisiti non calcolato ad ogni chiamata per questioni di performance 

		//calcolaSeInBozza(folderPageBean); //calcolato al volo
		//calcolaSeEliminabile(folderPageBean); //calcolato al volo
		//calcolaSeValutabile(folderPageBean); //calcolato al volo
		//calcolaSeRichiedibileDiIntegrazioni(folderPageBean);//Inutile
		//calcolaSeModificabilePerValtuazioneRegione(folderPageBean);
		//calcolaSeModificabilePerIntegrazioni(folderPageBean);
		startTime = System.currentTimeMillis();
		calcolaSeConcludibile(folderPageBean); //esiti
		log.info("calcolaSeConcludibile : " + (System.currentTimeMillis() - startTime) + " ms");
		
		//Controlla se ci sono tutti i TipiUdo22 Richiesti
		startTime = System.currentTimeMillis();
		calcolaTipoUdo22MancantiDomanda(folderPageBean);
		log.info("calcolaTipoUdo22MancantiDomanda : " + (System.currentTimeMillis() - startTime) + " ms");
	}
	
	private void caricaDatiDeliberaDaWorkflow(ExternalContext externalContext, ShowFolderPageBean folderPageBean, UserBean userBean) {
		if(
			aclService.userCanModifyDeliberaAttoInst(userBean, folderPageBean)
			&&
			!aclService.isGestioneAttoInstInUdoInstActiveForFolder(folderPageBean)
		 ) {
			try {
				if(folderPageBean.getFascicolo() != null && folderPageBean.getFascicolo().getWorkflowXWProcessInstance() != null) {
					DeliberaBean delibera = workflowService.getDelibera(folderPageBean.getFascicolo().getWorkflowXWProcessInstance().getProcessInstanceId(), false);
					//delibera.setDeliberaFileBean(workflowService.getDeliberaFileBean(fascicolo.getWorkflowXWProcessInstance().getProcessInstanceId(), true));
					//Required to download the file
					externalContext.getSessionMap().put("workflowService", workflowService);
					folderPageBean.setDelibera(delibera);
				}
			} catch (Exception e) {
				Logger.getRootLogger().error(e);
			}
		} else {
			folderPageBean.setDelibera(null);
		}
	}
	
	private void calcolaTipoUdo22MancantiDomanda(ShowFolderPageBean showFolderPageBean) {
		if(showFolderPageBean.getFascicolo().getDomandaClienId() != null && !showFolderPageBean.getFascicolo().getDomandaClienId().isEmpty()) {
			List<UdoUoInstForList> domandaUdoUoInstL = showFolderPageBean.getFascicolo().getUdoUoInstForL(); //lista delle UoUdo della domanda
			
			List<TipoUdo22Templ> l = new ArrayList<TipoUdo22Templ>();
			for (TipoUdo22Templ tipoUdo22Templ : showFolderPageBean.getTitolareDomanda().getTipoTitolareTempl().getTipoUdo22TemplRichiesti()) { //per ogni tipo
				if (!domandaContainsTipoUdo22(domandaUdoUoInstL, tipoUdo22Templ))
						l.add(tipoUdo22Templ);
			}
			Collections.sort(l, new TipoUdo22DescrComparator());
			
			showFolderPageBean.setTipoUdo22TemplRichiestiMancanti(l);
		} else {
			showFolderPageBean.setTipoUdo22TemplRichiestiMancanti(null);
		}
	}	

	@SuppressWarnings("unchecked")
	private void caricaDatiWorkflow(ExternalContext externalContext, MessageContext messageContext, ShowFolderPageBean showFolderPageBean, UserBean userBean) {
		//workflowService.caricaDati
		if(showFolderPageBean.getFascicolo().getWorkflowXWProcessInstance() != null && userBean.getWorkflowBean() != null) {
			//il fascicolo ha un workflow agganciato e l'utente puo' accedere a bonita quindi carico il workflow
			try {
				WorkflowProcessInstance wpi = new WorkflowProcessInstance(showFolderPageBean.getFascicolo().getWorkflowXWProcessInstance());
				long startTime = System.currentTimeMillis();
				wpi.setWorkflowProcessInstanceInfo(workflowService.getWorkflowProcessInstanceInfoForUser(showFolderPageBean.getFascicolo().getWorkflowXWProcessInstance().getProcessInstanceId(), userBean.getWorkflowBean().getApiSession().getUserId()));
				log.info("getWorkflowProcessInstanceInfoForUser : " + (System.currentTimeMillis() - startTime) + " ms");

				
				startTime = System.currentTimeMillis();
				wpi.setOverviewFormUrl(workflowService.getWorkflowBonita().getProcessInstanceOverviewFormUrl(wpi.getProcessDefinitionName(), wpi.getProcessDefinitionVersion(), wpi.getProcessInstanceId(), "it"));
				log.info("getProcessInstanceOverviewFormUrl : " + (System.currentTimeMillis() - startTime) + " ms");

				wpi.setImageWorkflowUrl(workflowService.getBonitaViewServerUrl() + "/index.jsp?processInstanceId=" + wpi.getProcessInstanceId());
				//(showFolderPageBean.getWorkflowProcessInstance().getProcessDefinitionName(), showFolderPageBean.getWorkflowProcessInstance().getProcessDefinitionVersion(), task.getName(), task.getId(), "it"));
				showFolderPageBean.setWorkflowProcessInstance(wpi);
				if(userBean.isVERIFICATORE() &&
					(
					  DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE.equals(showFolderPageBean.getFascicolo().getFolderStatus())
							||
					  DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE_INSERIMENTO_VERIFICHE.equals(showFolderPageBean.getFascicolo().getFolderStatus())
					  		||
					  DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE_VERIFICA.equals(showFolderPageBean.getFascicolo().getFolderStatus())
					  		||
					  DomandaInstDao.STATO_REDAZIONE_RAPPORTO_DI_VERIFICA.equals(showFolderPageBean.getFascicolo().getFolderStatus())
					 )
						) {
					//se siamo in GESTIONE_DELLE_VERIFICHE o in REDAZIONE_RAPPORTO_DI_VERIFICA e l'utente e' un VERIFICATORE carico i dati che occorrono per gestire le verifiche e redarre il rapporto di verifica
					//dal workflow
					startTime = System.currentTimeMillis();
					DataInstance verificatoreTeamLeaderBonitaUserName = workflowService.getWorkflowBonita().getVariable("verificatoreTeamLeaderBonitaUserName", showFolderPageBean.getFascicolo().getWorkflowXWProcessInstance().getProcessInstanceId(), userBean.getWorkflowBean().getApiSession());
					log.info("getVariabile verificatoreTeamLeaderBonitaUserName : " + (System.currentTimeMillis() - startTime) + " ms");
					if(verificatoreTeamLeaderBonitaUserName.getValue() != null) {
						AuacUserInfo auacUserInfo = AuacUtils.getAuacUserInfo((String)verificatoreTeamLeaderBonitaUserName.getValue());
						//System.out.println("leaderUserName: " + auaUserIfo.getBonitausername());
						showFolderPageBean.setVerificatoreTeamLeader(utenteDao.getByUserName(auacUserInfo.getUsername()));
					}
					
					startTime = System.currentTimeMillis();
					DataInstance dataInstance = workflowService.getWorkflowBonita().getVariable("teamVerificatori", showFolderPageBean.getFascicolo().getWorkflowXWProcessInstance().getProcessInstanceId(), userBean.getWorkflowBean().getApiSession());
					log.info("getVariabile teamVerificatori : " + (System.currentTimeMillis() - startTime) + " ms");
					if(dataInstance.getValue() != null) {
						List<String> teamVerificatori = (List<String>)dataInstance.getValue();
						if(teamVerificatori.size() > 0) {
							Set<String> usernames = new HashSet<String>();
							for(String bonitaUserName : teamVerificatori) {
								usernames.add(AuacUtils.getAuacUserInfo(bonitaUserName).getUsername());
							}
							showFolderPageBean.setTeamVerificatori(utenteDao.getListByUserNames(usernames));
						}
						/*System.out.println("Numero verificatori: " + teamVerificatori.size());
						int index = 1;
						for(String userName : teamVerificatori) {
							System.out.println(index++ + ") userName: " + userName);
						}*/
					}
					
					//pianificazioneVerificaDateVerifica
					if(DomandaInstDao.STATO_REDAZIONE_RAPPORTO_DI_VERIFICA.equals(showFolderPageBean.getFascicolo().getFolderStatus())) {
						startTime = System.currentTimeMillis();
						DataInstance pianificazioneVerificaDateVerifica = workflowService.getWorkflowBonita().getVariable("pianificazioneVerificaDateVerifica", showFolderPageBean.getFascicolo().getWorkflowXWProcessInstance().getProcessInstanceId(), userBean.getWorkflowBean().getApiSession());
						log.info("getVariabile pianificazioneVerificaDateVerifica : " + (System.currentTimeMillis() - startTime) + " ms");
						if(pianificazioneVerificaDateVerifica.getValue() != null) {
							String dateVerifica = (String)pianificazioneVerificaDateVerifica.getValue();
							showFolderPageBean.setPianificazioneVerificaDateVerifica(dateVerifica);
						}
						
					}					
				}
			}
			catch (Exception e) {
				Logger.getRootLogger().error("Errore nel caricamento del WorkflowProcessInstanceInfoForUser", e);
				MessageBuilder mB = new MessageBuilder();
				messageContext.addMessage(mB.error().source("messages").defaultText("Si è verificato un errore durante il caricamento dei dati del workflow").build());
			}
		} else {
			showFolderPageBean.setWorkflowProcessInstance(null);
			showFolderPageBean.setVerificatoreTeamLeader(null);
			showFolderPageBean.setTeamVerificatori(null);
			showFolderPageBean.setPianificazioneVerificaDateVerifica(null);
		}
	}
	
	private DomandaInst caricaDatiDomandaInst(ExternalContext externalContext, MessageContext messageContext, ShowFolderPageBean folderPageBean, UserBean userBean, DomandaInst domanda) {
		long startTime = System.currentTimeMillis();
		//long startTimeTotal;
		//long startTime;
		//long requiredTime;
		//startTime = System.currentTimeMillis();
		//startTimeTotal = System.currentTimeMillis();
		try {
			if(domanda == null) {
				if(folderPageBean.getFascicolo().getDomandaClienId() != null && !folderPageBean.getFascicolo().getDomandaClienId().isEmpty()) {
					//domanda = domandaInstDao.getDomandaInstByClientIdForShowFolder(folderPageBean.getFascicolo().getDomandaClienId());
					domanda = domandaInstDao.getDomandaInstByClientIdForShowFolderEntityGraph(folderPageBean.getFascicolo().getDomandaClienId());
					folderPageBean.setTipoProcTemp(domanda.getTipoProcTempl());
					folderPageBean.setTitolareDomanda(domanda.getTitolareModel());
					folderPageBean.setDomandaCreation(domanda.getCreation());
					folderPageBean.setDomandaPassataDaStatiVerifica("S".equals(domanda.getPassataDaStatiVerifica()));
					
					if(DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE.equals(folderPageBean.getFascicolo().getFolderStatus()) || DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_INSERIMENTO_ESITI.equals(folderPageBean.getFascicolo().getFolderStatus())) {
						//Carico i dati che occorrono per gestire l'inserimento degli esiti
						long startTimeParz = System.currentTimeMillis();
						folderPageBean.setTipiUdoUtenteTemplPerTipoTitolareDomandaCorrente(tipoUdoUtenteTemplDao.findTipoUdoUtenteTemplByTipoTitolare(domanda.getTitolareModel().getTipoTitolareTempl()));
						log.info("caricaDatiDomandaInst - findTipoUdoUtenteTemplByTipoTitolare - domanda clientid: " + domanda.getClientid() + "; " + (System.currentTimeMillis() - startTimeParz) + " ms");
					}
					
					if(DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE.equals(folderPageBean.getFascicolo().getFolderStatus())) {
						long startTimeParz = System.currentTimeMillis();
						folderPageBean.setDomandaVerificata(domandaInstDao.checkDomandaVerificata(folderPageBean.getFascicolo().getDomandaClienId()));
						log.info("caricaDatiDomandaInst - checkDomandaVerificata - domanda clientid: " + domanda.getClientid() + "; " + (System.currentTimeMillis() - startTimeParz) + " ms");
					}
					if(DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE_VERIFICA.equals(folderPageBean.getFascicolo().getFolderStatus())) {
						folderPageBean.setDomandaVerificata(true);
					}
					if(aclService.userCanShowStatoVerifica(folderPageBean.getFascicolo().getFolderStatus(), userBean)) {
						long startTimeParz = System.currentTimeMillis();
						folderPageBean.setUdoUoRgaIdsConReqNoVerificatori(domandaInstDao.getEntityInstClientIdConRequisitiNonAssegnatiInVerifica(domanda));
						log.info("caricaDatiDomandaInst - getDomandaInstUoInstUdoInstClientIdConRequisitiNonAssegnatiInVerifica - domanda clientid: " + domanda.getClientid() + "; " + (System.currentTimeMillis() - startTimeParz) + " ms");
						startTimeParz = System.currentTimeMillis();
						folderPageBean.setUdoUoRgaIdsConReqNonVerificati(domandaInstDao.getEntityClientIdConRequisitiNonVerificati(domanda));
						log.info("caricaDatiDomandaInst - getDomandaInstUoInstUdoInstClientIdConRequisitiNonVerificati - domanda clientid: " + domanda.getClientid() + "; " + (System.currentTimeMillis() - startTimeParz) + " ms");
					}
					if(aclService.userCanFilterByUdoUoRgaConRequisitiAssegnatiInVerifica(folderPageBean.getFascicolo().getFolderStatus(), userBean)) {
						long startTimeParz = System.currentTimeMillis();
						folderPageBean.setUdoUoRgaIdsConReqAssVerUserCor(domandaInstDao.getEntityInstClientIdConRequisitiAssegnatiInVerificaAdUtente(userBean.getUtenteModel(), domanda));
						log.info("caricaDatiDomandaInst - getDomandaInstUoInstUdoInstClientIdConRequisitiAssegnatiInVerificaAdUtente - domanda clientid: " + domanda.getClientid() + "; " + (System.currentTimeMillis() - startTimeParz) + " ms");
					}


					if(aclService.showInfoUdoInstAmbulatoriale(userBean.getRuolo(), folderPageBean.getFascicolo().getFolderStatus())) {
						long startTimeParz = System.currentTimeMillis();
						folderPageBean.setTipoUdo22TemplClientIdWithAmbitoAmbulatoriale(tipoUdo22TemplDao.getTipoUdo22TemplClientIdByAmbitoClientId(this.ambitoAmbulatorialeClientid));
						log.info("caricaDatiDomandaInst - getTipoUdo22TemplClientIdByAmbitoClientId - domanda clientid: " + domanda.getClientid() + "; " + (System.currentTimeMillis() - startTimeParz) + " ms");
					}
				}
				else {
					folderPageBean.setTipoProcTemp(null);
				}
			} else {
				folderPageBean.setTipoProcTemp(domanda.getTipoProcTempl());
			}
				
				
			//requiredTime = System.currentTimeMillis() - startTime;
			//startTime = System.currentTimeMillis();
			//System.out.println("caricaDatiFolder tempo lettura domanda: " + requiredTime + " ms");
			String tipoDomanda = folderPageBean.getFascicolo().getOggettoDomanda();
			folderPageBean.getFascicolo().setUdoUoInstForL(getFascicoloUdoL(userBean, domanda, tipoDomanda));

			//requiredTime = System.currentTimeMillis() - startTime;
			//startTime = System.currentTimeMillis();
			//System.out.println("caricaDatiFolder tempo getFascicoloUdoL: " + requiredTime + " ms");
			AreaDiscipline areaDiscipline = null;
			if(folderPageBean.getUdoInstSearchOrderBean().getAreaDisciplineDaCercare() != null && !folderPageBean.getUdoInstSearchOrderBean().getAreaDisciplineDaCercare().isEmpty()) {
				//Carico la lista di discipline dell'area selezionata
				areaDiscipline = areaDisciplineDao.findOne(folderPageBean.getUdoInstSearchOrderBean().getAreaDisciplineDaCercare());
				areaDiscipline.getDisciplinaTempls().size();
			}
			
			List<String> clientidUdoSenzaRequisiti = null;
			if(domanda != null && folderPageBean.getUdoInstSearchOrderBean().isShowOnlyUdoConRequisiti()) {
				clientidUdoSenzaRequisiti = udoInstDao.getUdoInstClientIdInDomandaSenzaRequisiti(domanda.getClientid());
			}
			
			folderPageBean.filterUdoUoInstForL(userBean.getUtenteModel(), areaDiscipline, clientidUdoSenzaRequisiti);
			folderPageBean.setEsitiPossibili(getEsitiDaScegliere(domanda));
			
			//estrazione della lista delle persone per le assegnazioni
			folderPageBean.setUoSelected("");
			updatePersonaL(folderPageBean);					
		}
		catch (Exception e) {
			//Logger.getRootLogger().error("Errore in caricamento DomandaInst", e);
			log.error("Errore in caricamento DomandaInst", e);
			MessageBuilder mB = new MessageBuilder();
			messageContext.addMessage(mB.error().source("messages").defaultText("Si è verificato un errore durante il caricamento dei dati della domanda").build());
		}
		
		//requiredTime = System.currentTimeMillis() - startTime;
		//startTime = System.currentTimeMillis();
		//System.out.println("caricaDatiFolder tempo lettura dati: " + requiredTime + " ms");
		

		//requiredTime = System.currentTimeMillis() - startTime;
		//System.out.println("caricaDatiFolder tempo calcola stato: " + requiredTime + " ms");
		//requiredTime = System.currentTimeMillis() - startTimeTotal;
		//System.out.println("caricaDatiFolder tempo totale: " + requiredTime + " ms");
		
		if(domanda != null)
			log.info("caricaDatiDomandaInst - clientid " + domanda.getClientid() + "; " + (System.currentTimeMillis() - startTime) + " ms");
		
		return domanda;
	}

	private void caricaDatiFolderExtraway(ExternalContext externalContext, MessageContext messageContext, ShowFolderPageBean folderPageBean, UserBean userBean, ExtraWayService _xwService) {
		try {
			folderPageBean.setTitolareFolderSubject(getTitolareFolderSubject(folderPageBean.getFascicolo().getContents(), _xwService));
			folderPageBean.setAllDocumentsL(getFascicoloDocumentsL(externalContext, userBean, folderPageBean.getFascicolo().getContents(), _xwService));

			if (folderPageBean.getFascicolo().getFolderType().equals(FolderDao.ATTI_TITOLARE_FOLDER_TYPE)){
				//ATTENZIONE in questo caso non si ha una DomandaInst
				//TODO, warning: questa informazione dovrei ricavarla dall'atto e non dando per scontato che l'utente che arrivera
				// a fare questa operazione e' un titolar
				if (userBean.isTITOLARE() || userBean.isOPERATORE_TITOLARE() || userBean.isOPERATORE_TITOLARE_IN_LETTURA() || userBean.isVERIFICATORE()) {
					TitolareModel titolare = null;
					//TODO evitare questa cosa brutta!!
					if(userBean.isTITOLARE())
						titolare = titolareModelDao.findByUtenteModel(userBean.getUtenteModel());
					if(userBean.isOPERATORE_TITOLARE() || userBean.isOPERATORE_TITOLARE_IN_LETTURA() || userBean.isVERIFICATORE())
						titolare = titolareModelDao.findOne(userBean.getTitolareClientId());
					titolare.getAttoModels().size();
					if (titolare.getAttoModels().size() > 0){
						List<AttoModel> attiL = titolare.getAttoModels();
						List<Map<String,String>> attiML = new ArrayList<Map<String,String>>();
						for (AttoModel atto:attiL){
							Map<String,String> map = new HashMap<String,String>();
							map.put("tipo_atto", atto.getTipoAtto().getDescr());
							map.put("tipo_proc", atto.getTipoProcTempl().getDescr());
							map.put("anno", atto.getAnno());
							map.put("numero", atto.getNumero());
							if(atto.getBinaryAttachmentsAppl()!=null){
								map.put("file_nome", atto.getBinaryAttachmentsAppl().getNome());
								map.put("file_nome_enc",Base64.encodeBase64String(atto.getBinaryAttachmentsAppl().getNome().getBytes()));
								map.put("file_id_enc",Base64.encodeBase64String(atto.getBinaryAttachmentsAppl().getClientid().getBytes()));
								HttpServletRequest httpServletRequest = (HttpServletRequest)externalContext.getNativeRequest();
								String sessionId = httpServletRequest.getSession().getId();
								map.put("session_id", Base64.encodeBase64String(sessionId.getBytes()));
							}
							else{
								map.put("file_nome", "");
							}
							Date dataInizio = atto.getInizioValidita();
							Date dataFine = atto.getFineValidita();
							SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
							String dataInizioS = formato.format(dataInizio).toString();
							String dataFineS = formato.format(dataFine).toString();
							map.put("inizio_validita", dataInizioS);
							map.put("fine_validita", dataFineS);
							attiML.add(map);
						}
						folderPageBean.setAttiML(attiML);
					}
				}
			}
		}
		catch (Exception e) {
			Logger.getRootLogger().error("Si è verificato un errore durante il caricamento dei dati del fascicolo", e);
			MessageBuilder mB = new MessageBuilder();
			messageContext.addMessage(mB.error().source("messages").defaultText("Si è verificato un errore durante il caricamento dei dati del fascicolo").build());
		}		
	}	

	public void removeUdoUoSelected(ExternalContext externalContext, MessageContext messageContext, UserBean userBean, ShowFolderPageBean showFolderPageBean) throws Exception {
		List<UdoUoInstForList> udoUoInstForListList = new ArrayList<UdoUoInstForList>();
		try {
			Map<String, Entry<String, Boolean>> checkM = showFolderPageBean.getCheckM();
			for (UdoUoInstForList udoUoInstForList:showFolderPageBean.getFascicolo().getUdoUoInstForL()) { //per ogni udo/uo nella pagina
				if (checkM.containsKey(udoUoInstForList.getClientId())) { //se la udo/uo/requisitiGeneraliDomanda è checked
					if(udoUoInstForList.isUdo() || udoUoInstForList.isUo()) {
						//Occorre caricare della udo getFattProdUdoInsts e i requisiti ordinati
						//udoUoInstForList.getUdoInst().setFattProdUdoInsts(fattProdUdoInstDao.findFattProdUdoInstByUdoInst(udoUoInstForList.getUdoInst()));
						udoUoInstForListList.add(udoUoInstForList);
					}
				}		
			}
			//log.debug("Tempo caricamento dati per pdf: " + requiredTime + " ms");
		} catch (Exception ex) {
			log.error("Errore creando la listqa delle Udo/Uo da cancellare", ex);
		}
		removeUdoUoList(externalContext, messageContext, userBean, showFolderPageBean, udoUoInstForListList);
	}
	
	public void removeUdoUo(ExternalContext externalContext, MessageContext messageContext, UserBean userBean, ShowFolderPageBean showFolderPageBean, UdoUoInstForList udoUoInstForList) throws Exception {
		List<UdoUoInstForList> udoUoInstForListList = new ArrayList<UdoUoInstForList>();
		udoUoInstForListList.add(udoUoInstForList);
		removeUdoUoList(externalContext, messageContext, userBean, showFolderPageBean, udoUoInstForListList);
	}
	
	public void removeUdoUoList(ExternalContext externalContext, MessageContext messageContext, UserBean userBean, ShowFolderPageBean showFolderPageBean, List<UdoUoInstForList> udoUoInstForListList) throws Exception {
		
		//Attenzione c'è un problema nell'aggiornamento delle liste getUdoInsts(), getUoInsts() della domanda quando si fa il remove di una udoInst la stessa non viene rimossa dalla lista
		//Quindi occerre tenere traccia di tutte le UdoInst e UoInst cancellate
		HashSet<String> udoInstsDeletedClientId = new HashSet<String>();
		//String uoInstDeleteClientId = "";
		//Elenco dei clientid delle strutture model delle UdoInst cancellate per controllare le struttureInst che potrebbero essere da cancellare
		HashSet<String> strutturaModelClientidForStruttureInstsToCheckForDelete = new HashSet<String>();

		//Elenco dei clientid degli edifici template delle UdoInst cancellate per controllare gli edificioInst che potrebbero essere da cancellare
		HashSet<String> edificioTemplClientidForEdificioInstsToCheckForDelete = new HashSet<String>();
		
		if(showFolderPageBean.getFascicolo().getDomandaClienId() != null && !showFolderPageBean.getFascicolo().getDomandaClienId().isEmpty()) {
			showFolderPageBean.setInitState();

			//domanda = domandaInstDao.getDomandaInstByClientIdForShowFolder(folderPageBean.getFascicolo().getDomandaClienId());
			DomandaInst domanda = domandaInstDao.getDomandaInstByClientIdForShowFolderEntityGraph(showFolderPageBean.getFascicolo().getDomandaClienId());
		
			log.debug("domanda.getUdoInsts().size() - removeUdoUo inizio: " + domanda.getUdoInsts().size());
			//I dati del folder extraway sono già caricati e non vengono modificati
			//caricaDatiFolderExtraway(externalContext, messageContext, showFolderPageBean, userBean, _xwService);
			
			//Effettuo la modifica
			for(UdoUoInstForList udoUoInstForList : udoUoInstForListList) {
//				if(udoUoInstForList.isRequisitiGeneraliAziendali())
//					continue;
				if(udoUoInstForList.isUo()) {
					UoInst uoInst = null;
					for(UoInst uoI : domanda.getUoInsts()) {
						if(uoI.getClientid().equals(udoUoInstForList.getClientId())) {
							uoInst = uoI;
							break;
						}
					}
					if(uoInst != null) {
						//devo cancellare tutte le Udo appartenenti alla uo in cancellazione
						for(UdoInst udoInst : domanda.getUdoInstFiglie(uoInst)) {
							removeUdoInst(udoInst, domanda, udoInstsDeletedClientId, true);
							udoInstsDeletedClientId.add(udoInst.getClientid());
							if(udoInst.getStrutturaModelClientid() != null)
								strutturaModelClientidForStruttureInstsToCheckForDelete.add(udoInst.getStrutturaModelClientid());
							
							if(udoInst.getEdificioTemplClientid() != null)
								edificioTemplClientidForEdificioInstsToCheckForDelete.add(udoInst.getEdificioTemplClientid());
						}
						removeUoInst(uoInst, domanda);
						//uoInstDeleteClientId = uoInst.getClientid();
					}
				} else if (udoUoInstForList.isUdo()) {
					UdoInst udoInst = null;
					for(UdoInst udoi : domanda.getUdoInsts()) {
						if(udoi.getClientid().equals(udoUoInstForList.getClientId())) {
							udoInst = udoi;
							break;
						}
					}
					
					if(udoInst != null) {
						//Cancello la UdoInst
						removeUdoInst(udoInst, domanda, udoInstsDeletedClientId, false);
						udoInstsDeletedClientId.add(udoInst.getClientid());
						if(udoInst.getStrutturaModelClientid() != null)
							strutturaModelClientidForStruttureInstsToCheckForDelete.add(udoInst.getStrutturaModelClientid());
						if(udoInst.getEdificioTemplClientid() != null)
							edificioTemplClientidForEdificioInstsToCheckForDelete.add(udoInst.getEdificioTemplClientid());
					}
				}
			}
			
			
			log.debug("domanda.getUdoInsts().size() - removeUdoUo prima controllo per eventuali Requisiti generali da Titolare: " + domanda.getUdoInsts().size());

			//Se ho cancellato almeno una UdoInst e non ho Udo nella domanda e non ho requisiti nella domanda stessa devo inserire i requisiti della domanda usando il campo DirezioneTempl del Titolare e se:
			//DirezioneTempl = 'Sanitario e Salute mentale' allora si utilizzano solo requisiti generali sanitari
			//DirezioneTempl = 'Socio Sanitario' allora si utilizzano solo i requisiti generali sociali
			//DirezioneTempl = 'Sociale' allora si utilizzano solo i requisiti generali sociali
			
			if(udoInstsDeletedClientId.size() > 0) {
				//Ho cancellato almeno una UdoInst
				boolean udoInstTutteCancellate = true;
				
				for(UdoInst udoI : domanda.getUdoInsts()) {
					if(!udoInstsDeletedClientId.contains(udoI.getClientid())) {
						udoInstTutteCancellate = false;
						break;
					}
				}
				
				if(udoInstTutteCancellate) {
					//le UdoInst sono state cancellate tutte
					log.debug("Inserisco i Requisiti Generali da Titolare per la domanda clientid: " + domanda.getClientid());
			    	if(domanda.getTitolareModel().getDirezioneTempl().getClassificazioneUdoTempls() != null && domanda.getTitolareModel().getDirezioneTempl().getClassificazioneUdoTempls().size() > 0) {
				    	List<BindListaRequClassUdo> listBindListaRequClassUdo = domanda.getTipoProcTempl().getBindListaRequClassUdos();
			    		ClassificazioneUdoTempl classUdoTempl = domanda.getTitolareModel().getDirezioneTempl().getClassificazioneUdoTempls().get(0);
			    		for(BindListaRequClassUdo bindListaRequClassUdo:listBindListaRequClassUdo) {
				    		if(bindListaRequClassUdo.getClassificazioneUdoTempl().getClientid().equals(classUdoTempl.getClientid())) {
				    			utilService.addRequisitiToDomandaInst(domanda, bindListaRequClassUdo);
				    		}
				    	}
			    	} else {
			    		log.error("Impossibile ricavare la classificazione Udo della direzione del titolare per inserire i requisiti della domanda con clientid: " + domanda.getClientid());
			    	}
				}
			}
			if(utilService.isStrutturaInstActiveForDomanda(domanda)) {
				if(strutturaModelClientidForStruttureInstsToCheckForDelete.size() > 0) {
					//Devo controllare se devo cancellare le StruttureInst
					for(String struModclientid : strutturaModelClientidForStruttureInstsToCheckForDelete) {
						boolean delStruInst = true;
						for(UdoInst udoI : domanda.getUdoInsts()) {
							//la struttura della UdoInst è la stessa ma la UdoInst non è stata cancellata
							if(struModclientid.equals(udoI.getStrutturaModelClientid()) && !udoInstsDeletedClientId.contains(udoI.getClientid())){
								delStruInst = false;
								break;
							}
						}
						if(delStruInst) {
							//cancello la strutturaInst
							StrutturaInst strutturaInst = null;
							for(StrutturaInst strut : domanda.getStrutturaInsts()) {
								if(struModclientid.equals(strut.getStrutturaModelClientid())) {
									strutturaInst = strut;
									break;
								}
							}
							if(strutturaInst != null) {
								//Cancello la UdoInst
								removeStrutturaInst(strutturaInst, domanda);
							}
						}
					}
				}
			}
			if(utilService.isEdificioInstActiveForDomanda(domanda)) {
				if(edificioTemplClientidForEdificioInstsToCheckForDelete.size() > 0) {
					//Devo controllare se devo cancellare gli edificioInst
					for(String edifTemplClientid : edificioTemplClientidForEdificioInstsToCheckForDelete) {
						boolean delEdifInst = true;
						for(UdoInst udoI : domanda.getUdoInsts()) {
							//l'edificio della UdoInst è lo stesso ma la UdoInst non è stata cancellata
							if(edifTemplClientid.equals(udoI.getEdificioTemplClientid()) && !udoInstsDeletedClientId.contains(udoI.getClientid())){
								delEdifInst = false;
								break;
							}
						}
						if(delEdifInst) {
							//cancello l'edificioInst
							EdificioInst edificioInst = null;
							for(EdificioInst edif : domanda.getEdificioInsts()) {
								if(edifTemplClientid.equals(edif.getEdificioTemplClientid())) {
									edificioInst = edif;
									break;
								}
							}
							if(edificioInst != null) {
								//Cancello l'EdificioInst
								removeEdificioInst(edificioInst, domanda);
							}
						}
					}
				}
			}
			//Se ci sono requisiti generali di tipo SR vanno aggiornate le valutazioni, valutazioni verificatore automatiche
			utilService.aggiornaRequisitiSrDomanda(domanda, false);
			
			//Aggiorno il bean showFolderPageBean
			caricaDatiDomandaInst(externalContext, messageContext, showFolderPageBean, userBean, null);
			
			calcolaSeInviabileReinviabile(showFolderPageBean);
			//calcolaSeProntoPerInvio(showFolderPageBean); //requisiti
			//calcolaSeReinviabile(showFolderPageBean); //requisiti
			
			//calcolaSeInBozza(showFolderPageBean);//calcolato al volo
			//calcolaSeEliminabile(showFolderPageBean);//calcolato al volo
			//calcolaSeValutabile(showFolderPageBean); //calcolato al volo
			//calcolaSeRichiedibileDiIntegrazioni(showFolderPageBean);//inutile
			//calcolaSeModificabilePerValtuazioneRegione(showFolderPageBean);
			//calcolaSeModificabilePerIntegrazioni(showFolderPageBean);
			calcolaSeConcludibile(showFolderPageBean); //esiti

			//Controlla se ci sono tutti i TipiUdo22 Richiesti
			calcolaTipoUdo22MancantiDomanda(showFolderPageBean);
		}
		
	}
	
	private void removeUoInst(UoInst uoInst, DomandaInst domanda) {
		if(uoInst != null) {
			UoModel uoModel = uoModelDao.findByClientId(uoInst.getUoModelClientid());
			if(uoModel != null && uoModel.getProcedimentoInCorso() != null && uoModel.getProcedimentoInCorso().equals("S")) {
				uoModel.setProcedimentoInCorso("N");
				uoModelDao.saveProcedimentoInCorso(uoModel);
			}
			domanda.removeUoInst(uoInst);
			uoInstDao.delete(uoInst);
		}
	}	

	//udoInstsDeletedClientId contiene la lista degli eventuali clientId gia' cancellati non contiene il clientid della udoInst corentemente in cancellazione
	private void removeStrutturaInst(StrutturaInst strutturaInst, DomandaInst domanda) {
		if(utilService.isStrutturaInstActiveForDomanda(domanda))
			strutturaInstDao.delete(strutturaInst);
	}

	private void removeEdificioInst(EdificioInst edificioInst, DomandaInst domanda) {
		if(utilService.isEdificioInstActiveForDomanda(domanda))
			edificioInstDao.delete(edificioInst);
	}

	//udoInstsDeletedClientId contiene la lista degli eventuali clientId gia' cancellati non contiene il clientid della udoInst corentemente in cancellazione
	private void removeUdoInst(UdoInst udoInst, DomandaInst domanda, HashSet<String> udoInstsDeletedClientId, boolean isUoInstPadreOnRemove) {
		//try {
			if(udoInst != null) {
				HashSet<String> udoInstsDeletedClientIdConUdoInstCorrente = new HashSet<String>(udoInstsDeletedClientId);
				udoInstsDeletedClientIdConUdoInstCorrente.add(udoInst.getClientid());
				
				UdoModel udoModel = udoModelDao.findByClientId(udoInst.getUdoModelClientid());
				if(!isUoInstPadreOnRemove) {
					//La UoInstPadre non è in cancellazione, se lo fosse sarebbe inutile modificare i requisiti della UoInst padre perche' verranno comunque eliminati con l'eliminazione della UoInst padre stessa
					//L'eliminazione di questa UdoInst potrebbe richiedere l'eliminazione di Requisiti alla UoInst padre
					//per sapere se vanno eliminati i requisiti controllo se fra le UdoInst non cancellate e figlie del padre non ve ne siano della stessa Classificazione e SaluteMentale
					UoInst uoInstPadre = domanda.getUoInstPadre(udoInst);
					if(uoInstPadre != null) {
						if(udoModel != null) {
							//setto il flag che indica che non e' presente un procedimento in corso per la uoModel
							//solo se la UdoInst in cancellazione ha la relativa UdoModel
							UoModel uoModel = uoModelDao.findByClientId(uoInstPadre.getUoModelClientid());
							if(uoModel != null && uoModel.getProcedimentoInCorso() != null && uoModel.getProcedimentoInCorso().equals("S")) {
								uoModel.setProcedimentoInCorso("N");
								uoModelDao.saveProcedimentoInCorso(uoModel);
							}
						}
	
						ClassificazioneUdoTemplSaluteMentaleDistinctInfo clUdoTemplSMDistInfo = domanda.getClassificazioneUdoTemplSaluteMentaleDistinctInfoForUdoInstOfUoInstConScarto(uoInstPadre, udoInstsDeletedClientIdConUdoInstCorrente);
						boolean removeReqSaluteMentale = clUdoTemplSMDistInfo.removeReqSaluteMentaleOnRemoveUdoInst(udoInst);
						boolean removeReqClassificazioneUdoTempl = clUdoTemplSMDistInfo.removeReqClassificazioneUdoTemplOnRemoveUdoInst(udoInst);
						
	
						//List<Entry<ClassificazioneUdoTempl, String>> listClassSM = domanda.getClassificazioneUdoTemplSaluteMentaleForUdoInstOfUoInstConScarto(uoInstPadre, udoInstsDeletedClientIdConUdoInstCorrente);
						if(removeReqSaluteMentale || removeReqClassificazioneUdoTempl) {
							for(Iterator<RequisitoInst> requisitoInstIter = uoInstPadre.getRequisitoInsts().iterator(); requisitoInstIter.hasNext();) {
								RequisitoInst req = requisitoInstIter.next();
								if(req.getSaluteMentale().equals(ClassificazioneUdoTemplSaluteMentaleDistinctInfo.SALUTE_MENTALE_YES)) {
									if(removeReqSaluteMentale) {
										//uoInstPadre.removeRequisitoInst(req);
										requisitoInstIter.remove();								
										req.setUoInst(null);
										requisitoInstDao.delete(req);
									}
								} else {
									if(removeReqClassificazioneUdoTempl && req.getClassificazioneUdoTempl().getClientid().equals(udoInst.getTipoUdoTempl().getTipoUdo22Templ().getClassificazioneUdoTempl().getClientid())) {
										requisitoInstIter.remove();								
										req.setUoInst(null);
										requisitoInstDao.delete(req);
									}
								}
									
							}
							
						}
						
						//Controllo se la UoInst padre dopo la modifica non abbia UdoInst figlie perche' in tal caso vanno aggiunti i Requisiti dal Titolare
						List<UdoInst> udoInstFiglie = domanda.getUdoInstFiglieConScarto(uoInstPadre, udoInstsDeletedClientIdConUdoInstCorrente);
						if(udoInstFiglie.size() == 0) {
							log.debug("Inserisco requisiti della UoInst legati al Titolare - UoInst.clientid: " + uoInstPadre.getClientid());
						    	if(domanda.getTitolareModel().getDirezioneTempl().getClassificazioneUdoTempls() != null && domanda.getTitolareModel().getDirezioneTempl().getClassificazioneUdoTempls().size() > 0) {
						    		ClassificazioneUdoTempl classUdoTempl = domanda.getTitolareModel().getDirezioneTempl().getClassificazioneUdoTempls().get(0);
							    	List<BindUoProcLista> listBindUoProcLista = domanda.getTipoProcTempl().getBindUoProcListas();
							    	uoInstPadre.setRequisitoInsts(new ArrayList<RequisitoInst>());
						    		for(BindUoProcLista bindUoProcLista:listBindUoProcLista) {
							    		if(bindUoProcLista.getId().getSaluteMentale().equals(ClassificazioneUdoTemplSaluteMentaleDistinctInfo.SALUTE_MENTALE_NO) && bindUoProcLista.getClassificazioneUdoTempl().getClientid().equals(classUdoTempl.getClientid())) {
							    			utilService.addRequisitiToUoInst(uoInstPadre, bindUoProcLista, domanda);
							    		}
							    	}
						    	} else {
						    		log.error("Impossibile ricavare la classificazione Udo della direzione del titolare per inserire i requisiti delle UoInst senza UdoInst della domanda con clientid: " + domanda.getClientid());
						    	}
							//}
						}
						
					}
				}
		
				//La cancellazione di questa UdoInst potrebbe richiedere la cancellazione di Requisiti dalla DomadaInst stessa
				//per sapere se vanno eliminati i requisiti controllo se fra le UdoInst non cancellate della domanda che non ve ne siano della stessa Classificazione
				List<ClassificazioneUdoTempl> listAllClassif = domanda.getClassificazioneUdoTemplForAllUdoInstConScarto(udoInstsDeletedClientIdConUdoInstCorrente);
				boolean cancellaRequisitiDomanda = true;
				for(ClassificazioneUdoTempl classif : listAllClassif) {
					if(classif.getClientid().equals(udoInst.getTipoUdoTempl().getTipoUdo22Templ().getClassificazioneUdoTempl().getClientid())) {
						cancellaRequisitiDomanda = false;
						break;
					}
				}
				if(cancellaRequisitiDomanda) {
					for(Iterator<RequisitoInst> requisitoInstIter = domanda.getRequisitoInsts().iterator(); requisitoInstIter.hasNext();) {
						RequisitoInst req = requisitoInstIter.next();
						//I requisiti di tipo SR non devono essere cancellati
						if(req.getIsSr() == BooleanEnum.FALSE && req.getClassificazioneUdoTempl().getClientid().equals(udoInst.getTipoUdoTempl().getTipoUdo22Templ().getClassificazioneUdoTempl().getClientid())) {
							//domanda.removeRequisitoInst(req);
							requisitoInstIter.remove();
							req.setDomandaInst(null);
							requisitoInstDao.delete(req);
						}
					}
				}
				
				
				if(udoModel != null) {
					//setto il flag che indica che non e' presente un procedimento in corso per la udoModel
					udoModel.setProcedimentoInCorso("N");
					udoModelDao.save(udoModel);
				}
				//cancello la UdoInst
				udoInstDao.delete(udoInst);
				//udoModel.removeUdoInst(udoInst);
				
			}
		/*} 
		catch (Exception e) {
			log.error("Errore in removeUdoInst", e);
			//throw(e);
		}*/
	}
	
	public ShowFolderPageBean createShowFolderPageBean(ExternalContext externalContext, MessageContext messageContext, UserBean userBean, String idUnit, ExtraWayService xwService, ExtraWayService _xwService, ShowFolderPageBean showFolderPageBean, UdoInstSearchOrderBean udoInstSearchOrderBean) throws Exception{
		//long startTime;
		//long requiredTime;
		//startTime = System.currentTimeMillis();
		if (showFolderPageBean != null)
			return showFolderPageBean;

		ShowFolderPageBean folderPageBean = new ShowFolderPageBean();
		folderPageBean.setInitState();
		if(udoInstSearchOrderBean != null)
			folderPageBean.setUdoInstSearchOrderBean(udoInstSearchOrderBean);
		
		XMLDocumento document = null;
		try {
			document = xwService.loadDocument(Integer.parseInt(idUnit), false, false, false, false); //primo tentativo (caricacamento inset)
		}
		catch (Exception e) {
			document = xwService.loadDocument(Integer.parseInt(idUnit), false, true, false, false); //secondo tentativo (caricamento outset) nel caso si provenga da inserimento di nuovo fascicolo
		}
		if (document == null)
			throw new BusinessException("Impossibile caricare il fascicolo. Fascicolo non trovato");
		
		//creo il fascicolo
		FolderBean fascicolo = new FolderBean(userBean);
		folderPageBean.setFascicolo(fascicolo);
		fascicolo.setContents(document);
		
		try {
			//estrazione della lista delle UO
			List<UoModel> uoModelL = null;
			if(userBean.getTitolareClientId() != null && !userBean.getTitolareClientId().isEmpty()) {
				TitolareModel jpaFreshTitolareModel = titolareModelDao.findByClientId(userBean.getTitolareClientId());
				uoModelL = jpaFreshTitolareModel.getUoModels();
				Collections.sort(uoModelL, new Comparator<UoModel>() {
			        @Override
			        public int compare(UoModel uo1, UoModel uo2) {
			        	return uo1.getDenominazione().compareTo(uo2.getDenominazione());
			        }
			    });
				if(uoModelL != null)
					uoModelL.size();
			}
			/*if(domanda != null) {
				TitolareModel jpaFreshTitolareModel = titolareModelDao.findByClientId(domanda.getTitolareModel().getClientid());
				uoModelL = jpaFreshTitolareModel.getUoModels();
				Collections.sort(uoModelL, new Comparator<UoModel>() {
			        @Override
			        public int compare(UoModel uo1, UoModel uo2) {
			        	return uo1.getDenominazione().compareTo(uo2.getDenominazione());
			        }
			    });
			}*/
			folderPageBean.setUoL(uoModelL);		
			folderPageBean.setAreeDiscipline(areaDisciplineDao.findAllForList());
			folderPageBean.setClassificazioniUdoTempl(classificazioneUdoTemplDao.loadAllForShowFolderSearch());
			
			//spostato in caricaDatiFolder
			//estrazione della lista delle persone per le assegnazioni
			//folderPageBean.setUoSelected("");
			//updatePersonaL(folderPageBean);		
			
			
		}
		catch (Exception e) {
			Logger.getRootLogger().error(e);
			MessageBuilder mB = new MessageBuilder();
			messageContext.addMessage(mB.error().source("messages").defaultText("Si è verificato un errore durante il caricamento del fascicolo").build());
		}
		
		caricaDatiShowFolder(externalContext, messageContext, folderPageBean, userBean, _xwService);
		
		//requiredTime = System.currentTimeMillis() - startTime;
		//System.out.println("createShowFolderPageBean tempo: " + requiredTime + " ms");

		if(udoInstSearchOrderBean != null && udoInstSearchOrderBean.getUdoUoInstForListOrderType() != UdoUoInstForListOrderTypeEnum.Gerarchico) { //devo ordinare
			AreaDiscipline areaDiscipline = null;
			if(udoInstSearchOrderBean.getAreaDisciplineDaCercare() != null && !udoInstSearchOrderBean.getAreaDisciplineDaCercare().isEmpty()) {
				areaDiscipline = areaDisciplineDao.findOne(udoInstSearchOrderBean.getAreaDisciplineDaCercare());
				areaDiscipline.getDisciplinaTempls().size();
			}
			List<String> clientidUdoSenzaRequisiti = null;
			if(showFolderPageBean.getUdoInstSearchOrderBean().isShowOnlyUdoConRequisiti() && showFolderPageBean.getFascicolo() != null && showFolderPageBean.getFascicolo().getDomandaClienId() != null) {
				clientidUdoSenzaRequisiti = udoInstDao.getUdoInstClientIdInDomandaSenzaRequisiti(showFolderPageBean.getFascicolo().getDomandaClienId());
			}		
			folderPageBean.setActiveRowIndex(0);
			folderPageBean.orderUdoUoInstForL(userBean.getUtenteModel(), areaDiscipline, clientidUdoSenzaRequisiti);
		}
		
		return folderPageBean;
	}
	
	public void firstFascicolo(ExternalContext externalContext, MessageContext messageContext, UserBean userBean, ShowFolderPageBean showFolderPageBean, ExtraWayService xwService, ExtraWayService _xwService) throws Exception {
		if (showFolderPageBean.getFascicolo().isCanFirst()) {
			showFolderPageBean.setInitState();
			
			XMLDocumento document = xwService.loadFirstDocument(false, false);
			showFolderPageBean.getFascicolo().setContents(document);

			caricaDatiShowFolder(externalContext, messageContext, showFolderPageBean, userBean, _xwService);
		}
	}
	
	public void prevFascicolo(ExternalContext externalContext, MessageContext messageContext, UserBean userBean, ShowFolderPageBean showFolderPageBean, ExtraWayService xwService, ExtraWayService _xwService) throws Exception {
		if (showFolderPageBean.getFascicolo().isCanPrev()) {
			showFolderPageBean.setInitState();
			
			XMLDocumento document = xwService.loadPrevDocument(false, false);
			showFolderPageBean.getFascicolo().setContents(document);

			caricaDatiShowFolder(externalContext, messageContext, showFolderPageBean, userBean, _xwService);
		}
	}
	
	public void nextFascicolo(ExternalContext externalContext, MessageContext messageContext, UserBean userBean, ShowFolderPageBean showFolderPageBean, ExtraWayService xwService, ExtraWayService _xwService) throws Exception {
		if (showFolderPageBean.getFascicolo().isCanNext()) {
			showFolderPageBean.setInitState();
			
			XMLDocumento document = xwService.loadNextDocument(false, false);
			showFolderPageBean.getFascicolo().setContents(document);

			caricaDatiShowFolder(externalContext, messageContext, showFolderPageBean, userBean, _xwService);
		}
	}
	
	public void lastFascicolo(ExternalContext externalContext, MessageContext messageContext, UserBean userBean, ShowFolderPageBean showFolderPageBean, ExtraWayService xwService, ExtraWayService _xwService) throws Exception {
		if (showFolderPageBean.getFascicolo().isCanLast()) {
			showFolderPageBean.setInitState();
			
			XMLDocumento document = xwService.loadLastDocument(false, false);
			showFolderPageBean.getFascicolo().setContents(document);

			caricaDatiShowFolder(externalContext, messageContext, showFolderPageBean, userBean, _xwService);
		}
	}
	
	public void modificaEsitoValRispProg(UserBean userBean, ShowFolderPageBean showFolderPageBean, boolean esitoMulti, String valoreEsito, String notaEsito) throws ParseException{
		//salvo su db
		String operatore = userBean.getUtenteModel().getAnagraficaUtenteModel().getCognome() + " " + userBean.getUtenteModel().getAnagraficaUtenteModel().getNome();
		String direzione = userBean.getUtenteModel().getDirezioneTempl().getNome();
		boolean isAdmin = userBean.isImitatingOperatore();
		if(esitoMulti) {
			for(UdoUoInstForList udoUoInstForList : showFolderPageBean.getFascicolo().getUdoUoInstForL()) {
				if(showFolderPageBean.getCheckM().containsKey(udoUoInstForList.getClientId()) && aclService.userCanEditEsitoForUdoUoInstForList(udoUoInstForList, userBean, showFolderPageBean)) {
					//selezionata e modificabile dall'utente corrente
					modificaEsitoValRispProg(udoUoInstForList, operatore, direzione, isAdmin, valoreEsito, notaEsito);
				}
			}
		} else {
			UdoUoInstForList udoUoInstModificata = showFolderPageBean.getFilteredUdoUoInstForL().get(showFolderPageBean.getActiveRowIndex());
			//UdoUoInstForList udoUoInstModificata = showFolderPageBean.getFilteredUdoUoInstForL().get(Integer.parseInt(indexUdoL));
			modificaEsitoValRispProg(udoUoInstModificata, operatore, direzione, isAdmin, valoreEsito, notaEsito);
		}
		calcolaSeConcludibile(showFolderPageBean);
	}
	
	private void modificaEsitoValRispProg(UdoUoInstForList udoUoInstModificata, String operatore, String direzione, boolean isAdmin, String valoreEsito, String notaEsito) {
		Date curDate = new Date();
		if(udoUoInstModificata.isUdo()) {
			UdoInst udoPerModificaEsito = udoInstDao.findOne(udoUoInstModificata.getClientId());
			udoPerModificaEsito.setEsito(valoreEsito);
			udoPerModificaEsito.setEsitoNote(notaEsito);
			udoPerModificaEsito.setEsitoOperatore(operatore);
			udoPerModificaEsito.setEsitoTimeStamp(curDate);
			udoPerModificaEsito.setEsitoDirezioneOperatore(direzione);
			udoPerModificaEsito.setEsitoStato(null);
			if(isAdmin) {
				udoPerModificaEsito.setAdmin("S");
				udoUoInstModificata.setAdmin("S"); // set the value here inside the if, because uo has no is admin.
			}
			else {
				udoPerModificaEsito.setAdmin("N");
				udoUoInstModificata.setAdmin("N");
			}
				
			udoInstDao.save(udoPerModificaEsito);
		}
		else {
			UoInst uoPerModificaEsito = uoInstDao.findOne(udoUoInstModificata.getClientId());
			uoPerModificaEsito.setEsito(valoreEsito);
			uoPerModificaEsito.setEsitoNote(notaEsito);
			uoPerModificaEsito.setEsitoOperatore(operatore);
			uoInstDao.save(uoPerModificaEsito);
		}
		
		//modifico per visualizzare il dato senza ricaricamento della pagina
		udoUoInstModificata.setEsito(valoreEsito);
		udoUoInstModificata.setEsitoNote(notaEsito);
		udoUoInstModificata.setEsitoOperatore(operatore);
		udoUoInstModificata.setEsitoTimeStamp(curDate);
		udoUoInstModificata.setEsitoStato(null);
		udoUoInstModificata.setEsitoDirezioneOperatore(direzione);
	}

	public void modificaEsito(UserBean userBean, ShowFolderPageBean showFolderPageBean, boolean esitoMulti, String valoreEsito, String notaEsito,  String dataInizio, String scadenza, String attoInstClientid) throws ParseException{
		//salvo su db
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
		Date dataInizioDate = null;
		Date scadenzaDate = null;
		String operatore = userBean.getUtenteModel().getAnagraficaUtenteModel().getCognome() + " " + userBean.getUtenteModel().getAnagraficaUtenteModel().getNome();
		String direzione = userBean.getUtenteModel().getDirezioneTempl().getNome();
		boolean isAdmin = userBean.isImitatingOperatore();

		if(dataInizio != null && !dataInizio.isEmpty())
			dataInizioDate = df.parse(dataInizio);
		if(scadenza != null && !scadenza.isEmpty())
			scadenzaDate = df.parse(scadenza);
		
		if(esitoMulti) {
			for(UdoUoInstForList udoUoInstForList : showFolderPageBean.getFascicolo().getUdoUoInstForL()) {
				if(showFolderPageBean.getCheckM().containsKey(udoUoInstForList.getClientId()) && aclService.userCanEditEsitoForUdoUoInstForList(udoUoInstForList, userBean, showFolderPageBean)) {
					//selezionata e modificabile dall'utente corrente
					modificaEsito(udoUoInstForList, valoreEsito, notaEsito, dataInizioDate, scadenzaDate, operatore, direzione, isAdmin, attoInstClientid);
				}
			}
		} else {
			//UdoUoInstForList udoUoInstModificata = showFolderPageBean.getFilteredUdoUoInstForL().get(Integer.parseInt(indexUdoL));
			UdoUoInstForList udoUoInstModificata = showFolderPageBean.getFilteredUdoUoInstForL().get(showFolderPageBean.getActiveRowIndex());

			modificaEsito(udoUoInstModificata, valoreEsito, notaEsito, dataInizioDate, scadenzaDate, operatore, direzione, isAdmin, attoInstClientid);
		}
		calcolaSeConcludibile(showFolderPageBean);
	}
	
	private void modificaEsito(UdoUoInstForList udoUoInstModificata, String valoreEsito, String notaEsito, Date dataInizioDate, 
			Date scadenzaDate, String operatore, String direzione, boolean isAdmin, String attoInstClientid) {
		Date curDate = new Date();
		if(udoUoInstModificata.isUdo()) {
			UdoInst udoPerModificaEsito = udoInstDao.findOne(udoUoInstModificata.getClientId());
			udoPerModificaEsito.setEsito(valoreEsito);
			udoPerModificaEsito.setEsitoNote(notaEsito);
			
			udoPerModificaEsito.setEsitoOperatore(operatore);
			udoPerModificaEsito.setEsitoTimeStamp(curDate);
			udoPerModificaEsito.setEsitoDirezioneOperatore(direzione);
			
			udoPerModificaEsito.setEsitoDataInizio(dataInizioDate);
			udoPerModificaEsito.setEsitoScadenza(scadenzaDate);
			AttoInst attoInst = null;
			if(attoInstClientid != null && !attoInstClientid.isEmpty()) {
				attoInst = new AttoInst();
				attoInst.setClientid(attoInstClientid);
			}
			udoPerModificaEsito.setAttoInst(attoInst);
			
			if(isAdmin) {
				udoPerModificaEsito.setAdmin("S");
				udoUoInstModificata.setAdmin("S"); // set the value here inside the if, because uo has no is admin.
			}
			else {
				udoPerModificaEsito.setAdmin("N");
				udoUoInstModificata.setAdmin("N");
			}
			udoInstDao.save(udoPerModificaEsito);
			udoUoInstModificata.getUdoInst().setAttoInst(attoInst);
		}
		else {
			UoInst uoPerModificaEsito = uoInstDao.findOne(udoUoInstModificata.getClientId());
			uoPerModificaEsito.setEsito(valoreEsito);
			uoPerModificaEsito.setEsitoNote(notaEsito);
			uoPerModificaEsito.setEsitoDataInizio(dataInizioDate);
			uoPerModificaEsito.setEsitoScadenza(scadenzaDate);
			uoInstDao.save(uoPerModificaEsito);
		}
	
		//modifico per visualizzare il dato senza ricaricamento della pagina
		udoUoInstModificata.setEsito(valoreEsito);
		udoUoInstModificata.setEsitoNote(notaEsito);
		udoUoInstModificata.setEsitoDataInizio(dataInizioDate);
		udoUoInstModificata.setEsitoScadenza(scadenzaDate);
	}

	public void deleteDocument(String idIUnit, ExtraWayService xwService, ShowFolderPageBean showFolderPageBean) throws Exception {
		xwService.deleteDocument(Integer.parseInt(idIUnit));
		calcolaSeInviabileReinviabile(showFolderPageBean);
		//calcolaSeProntoPerInvio(showFolderPageBean);
		//calcolaSeReinviabile(showFolderPageBean);
	}
	
	public boolean validateFormAddWorkflow(MessageContext messageContext, ShowFolderPageBean showFolderPageBean) {
		MessageBuilder mB = new MessageBuilder();
		
		boolean errors = false;
		
		//testo annotazione vuoto
		if (showFolderPageBean.getWorkflowDefinitionSelected().length() == 0) {
			messageContext.addMessage(mB.error().source("nuovaAnnotazione").defaultText("Selezionare il workflow").build());
			messageContext.addMessage(mB.error().source("messages").defaultText("Selezionare il workflow").build());
			errors = true;
		}
		
		return !errors;
	}	
	
	public void addWorkflowProcess(ShowFolderPageBean showFolderPageBean, UserBean userBean, ExternalContext externalContext, MessageContext messageContext, ExtraWayService xwService) throws Exception {
		
		if(!showFolderPageBean.getWorkflowDefinitionSelected().isEmpty()) {
			log.info("ShowFolderService - addWorkflowProcess - showFolderPageBean.getWorkflowDefinitionSelected(): " + showFolderPageBean.getWorkflowDefinitionSelected());
	
			long processDefinitionId = Long.parseLong(showFolderPageBean.getWorkflowDefinitionSelected());
			ProcessDeploymentInfo processDepInf = null;
			for(ProcessDeploymentInfo pdi : userBean.getWorkflowBean().getProcessDeploymentInfos()) {
				if(pdi.getProcessId()==processDefinitionId) {
					processDepInf = pdi;
					break;
				}
			}
			
			if(processDepInf != null) {
				//Carico la domanda perche' in visualizzazione non ho tutto quello che mi serve
				DomandaInst dom = domandaInstDao.getDomandaInstByClientIdForAddWorkflow(showFolderPageBean.getFascicolo().getDomandaClienId());
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("auac_dom_clientid", showFolderPageBean.getFascicolo().getDomandaClienId());			
				variables.put("auac_dom_numero", showFolderPageBean.getFascicolo().getNumeroProcedimento());
				variables.put("auac_dom_stato", DomandaInstDao.STATO_FASE_ISTRUTTORIA);
				variables.put("auac_nrecord", showFolderPageBean.getFascicolo().getNrecord());
	
				variables.put("auac_titolaremodel_clientid", dom.getTitolareModel().getClientid());
				variables.put("auac_titolaremodel_ragsoc", dom.getTitolareModel().getRagSoc());
				
				//TODO ELIMINARE SE SI CREA IL CONNETTORE CHE RICAVA LA EMAIL DEL TITOLARE
				TitolareModel tmPerEmail = titolareModelDao.findByClientId(dom.getTitolareModel().getClientid());
				String emailTitolare = "";
				if (!xwService.getMailDebug().isEmpty())
					emailTitolare = xwService.getMailDebug();
				else {
					//if(tmPerEmail.getUtenteModel() != null && tmPerEmail.getUtenteModel().getAnagraficaUtenteModel() != null)
					//	emailTitolare = tmPerEmail.getUtenteModel().getAnagraficaUtenteModel().getEmail();
					emailTitolare = tmPerEmail.getEmail();
				}
				variables.put("auac_titolaremodel_email", emailTitolare);
				
				boolean showDichDirSan = false;
				if(tmPerEmail.getDirezioneTempl() != null && tmPerEmail.getTipoTitolareTempl() != null && "S".equals(tmPerEmail.getDirezioneTempl().getShowDichiarazioneDirSan()) && "S".equals(tmPerEmail.getTipoTitolareTempl().getShowDichiarazioneDirSan()))
					showDichDirSan = true;
				
				variables.put("auac_showdichiarazionedirsan", showDichDirSan);				
				variables.put("auac_mailSanitaRegioneVeneto", xwService.getMailPrincipale());

				variables.put("auac_config_smtpHost", xwService.getSmtpHost());
				variables.put("auac_config_smtpPort", Integer.parseInt(xwService.getSmtpPort()));
				variables.put("auac_config_smtpUser", xwService.getSmtpUser());
				variables.put("auac_config_smtpPassword", xwService.getSmtpPwd());
				
				Set<String> uoModelClientidConUdoInstFigli = new HashSet<String>();
				Set<String> clientidDirs = new HashSet<String>();
				boolean isSaluteMentale = false;
				for(UdoInst udoInst : dom.getUdoInsts()) {
					uoModelClientidConUdoInstFigli.add(udoInst.getUdoModelUoModelClientid());
					/*
					@ManyToMany
					@JoinTable(
						name="BIND_CLASS_UDO_DIREZIONI"
						, joinColumns={
							@JoinColumn(name="ID_DIREZIONE_FK")
							}
						, inverseJoinColumns={
							@JoinColumn(name="ID_CLASSIFICAZIONE_UDO_FK")
							}
						)
					 */
					//uuifl.getUdoInst().getTipoUdoTempl().getTipoUdo22Templ().getClassificazioneUdoTempl().getDirezioneTempls();
					
					//           udoInst.getTipoUdoTempl().getTipoUdo22Templ().getClassificazioneUdoTempl().getClientid()
					//cUdoSMInfo.addUdoInst(uuifl.getUdoInst());
					
					for(DirezioneTempl dt : udoInst.getTipoUdoTempl().getTipoUdo22Templ().getClassificazioneUdoTempl().getDirezioneTempls())
						clientidDirs.add(dt.getClientid());
						
					if(udoInst.getTipoUdoTempl().getTipoUdo22Templ().getSaluteMentale().equals(ClassificazioneUdoTemplSaluteMentaleDistinctInfo.SALUTE_MENTALE_YES))
						isSaluteMentale = true;
				}
				//la gestione delle UoInst senza UdoInst figlie
				//dove titolare e' il titolare della domanda in quanto il titolare di tutte le UoInst getUoInst().getTitolareModel() corrisponde a quello della domanda
				/*if(addReqUoInstSenzaUdo) {
			    	if(titolare.getDirezioneTempl().getClassificazioneUdoTempls() != null && titolare.getDirezioneTempl().getClassificazioneUdoTempls().size() > 0) {
			    		ClassificazioneUdoTempl classUdoTempl = titolare.getDirezioneTempl().getClassificazioneUdoTempls().get(0);
				    	List<BindUoProcLista> listBindUoProcLista = domanda.getTipoProcTempl().getBindUoProcListas();
				    	uoInst.setRequisitoInsts(new ArrayList<RequisitoInst>());
			    		for(BindUoProcLista bindUoProcLista:listBindUoProcLista) {
				    		if(bindUoProcLista.getId().getSaluteMentale().equals(ClassificazioneUdoTemplSaluteMentaleDistinctInfo.SALUTE_MENTALE_NO) && bindUoProcLista.getClassificazioneUdoTempl().getClientid().equals(classUdoTempl.getClientid())) {
				    			//addRequisitiToUoInst(uoInst, bindUoProcLista);
				    			utilService.addRequisitiToUoInst(uoInst, bindUoProcLista);
				    		}
				    	}
			    	} else {
			    		log.error("Impossibile ricavare la classificazione Udo della direzione del titolare per inserire i requisiti delle UoInst senza UdoInst della domanda con clientid: " + domanda.getClientid());
			    	}
				}*/
				boolean uoInstSenzaUdoInst = false;
				for(UoInst uo : dom.getUoInsts()) {
					if(!uoModelClientidConUdoInstFigli.contains(uo.getUoModelClientid())) {
						//UoInst senza UdoInst figlie
						uoInstSenzaUdoInst = true;
						break;
					}
				}
				if(uoInstSenzaUdoInst && dom.getTitolareModel().getDirezioneTempl() != null)
					clientidDirs.add(dom.getTitolareModel().getDirezioneTempl().getClientid());
				
				//TODO per gestire il caso di isSaluteMentale decommentare
				//if(isSaluteMentale) {
					//TODO cosa si deve fare? Per ora niente
				//}
	
				if(clientidDirs.size() > 0) {
					//variables.put("auac_direzioniclientid", clientidDirs);
					variables.put("auac_direzioniclientid", new ArrayList<String>(clientidDirs));
				}
				
				int majorVersion = 1;
				String strMajVers = processDepInf.getVersion().substring(0, processDepInf.getVersion().indexOf("."));
				try {
					majorVersion = Integer.valueOf(strMajVers); 
				} catch (NumberFormatException e) {
					log.error("Impossibile recuperare la major version del flusso");
					throw(e);
				}
				if(majorVersion > 1) {
					variables.put("auac_tipoprocedimento", dom.getTipoProcTempl().getTipoWf());
					variables.put("auac_direzionetitolare", dom.getTitolareModel().getDirezioneTempl().getDirezioneWf());
				}
				
				//TODO rimettere a posto passando le varabili (prima riga)
				ProcessInstance pi = workflowService.getWorkflowBonita().startWorkflowWithVariables(processDepInf.getProcessId(), variables, userBean.getWorkflowBean().getApiSession());
				//ProcessInstance pi = workflowService.getWorkflowBonita().startWorkflowWithVariables(processDepInf.getProcessId(), null, userBean.getWorkflowBean().getApiSession());
				
				//Date date = new Date();
				String data = new SimpleDateFormat("yyyyMMdd").format(pi.getStartDate());
				String ora = new SimpleDateFormat("HH:mm:ss").format(pi.getStartDate());
				
				WorkflowXWProcessInstance workflowProcessInstance = new WorkflowXWProcessInstance(pi.getId(), processDepInf.getProcessId());
				workflowProcessInstance.setProcessDefinitionName(processDepInf.getName());
				workflowProcessInstance.setProcessDefinitionVersion(processDepInf.getVersion());
				workflowProcessInstance.setOperatore(userBean.getUtenteModel().getLoginDbOrCas());
				workflowProcessInstance.setRuolo(userBean.getRuolo());
				workflowProcessInstance.setData(data);
				workflowProcessInstance.setOra(ora);
				
				//Modifico il Folder
				XMLDocumento document = FolderDao.addWorkflowProcessInstance(xwService, Integer.parseInt(showFolderPageBean.getFascicolo().getIdUnit()), workflowProcessInstance);
				//Cambio lo stato della DomandaInst
				dom.setStato(DomandaInstDao.STATO_FASE_ISTRUTTORIA);
				domandaInstDao.save(dom);

				//refresh immediato (per caricare i dati del workflow)
				showFolderPageBean.getFascicolo().setContents(document);
				caricaDatiWorkflow(externalContext, messageContext, showFolderPageBean, userBean);
				
			}
		}
		showFolderPageBean.setWorkflowDefinitionSelected("");
	}
	
	public boolean checkPrendiTask(int index, ShowFolderPageBean showFolderPageBean, UserBean userBean, ExternalContext externalContext, MessageContext messageContext) throws Exception {
		MessageBuilder mB = new MessageBuilder();
		HumanTaskInstance task = showFolderPageBean.getWorkflowProcessInstance().getWorkflowProcessInstanceInfo().getHumanTasksToExecute().get(index);
		boolean errors = false;
		if (task.getAssigneeId() != 0) {
			messageContext.addMessage(mB.error().source("assegnaTask").defaultText("Il task è già stato preso in carico").build());
			messageContext.addMessage(mB.error().source("messages").defaultText("Il task è già stato preso in carico").build());
			errors = true;
		}
		return !errors;
	}	

	public void prendiTask(int index, ShowFolderPageBean showFolderPageBean, UserBean userBean, ExternalContext externalContext, MessageContext messageContext) throws Exception {
		HumanTaskInstance task = showFolderPageBean.getWorkflowProcessInstance().getWorkflowProcessInstanceInfo().getHumanTasksToExecute().get(index);
		workflowService.getWorkflowBonita().assignUserTask(task.getId(), userBean.getWorkflowBean().getApiSession().getUserId(), userBean.getWorkflowBean().getApiSession());
		caricaDatiWorkflow(externalContext, messageContext, showFolderPageBean, userBean);
	}
	
	public void rilasciaTask(int index, ShowFolderPageBean showFolderPageBean, UserBean userBean, ExternalContext externalContext, MessageContext messageContext) throws Exception {
		HumanTaskInstance task = showFolderPageBean.getWorkflowProcessInstance().getWorkflowProcessInstanceInfo().getHumanTasksToExecute().get(index);
		workflowService.getWorkflowBonita().releaseUserTask(task.getId(), userBean.getWorkflowBean().getApiSession());
		caricaDatiWorkflow(externalContext, messageContext, showFolderPageBean, userBean);
	}
	
	public boolean checkCanExecuteTask(int index, ShowFolderPageBean showFolderPageBean, UserBean userBean, ExternalContext externalContext, MessageContext messageContext) throws Exception {
		MessageBuilder mB = new MessageBuilder();
		HumanTaskInstance task = showFolderPageBean.getWorkflowProcessInstance().getWorkflowProcessInstanceInfo().getHumanTasksToExecute().get(index);
		boolean errors = false;
		if (!workflowService.getWorkflowBonita().canExecuteTask(task.getId(), userBean.getWorkflowBean().getApiSession().getUserId(), userBean.getWorkflowBean().getApiSession())) {
			messageContext.addMessage(mB.error().source("eseguiTask").defaultText("Il task non può essere eseguito").build());
			messageContext.addMessage(mB.error().source("messages").defaultText("Il task non può essere eseguito").build());
			errors = true;
		}
		return !errors;
	}
	
	public void executeTask(int index, ShowFolderPageBean showFolderPageBean, UserBean userBean, ExternalContext externalContext, MessageContext messageContext) throws Exception {
		//HumanTaskInstance task = showFolderPageBean.getWorkflowProcessInstance().getWorkflowProcessInstanceInfo().getHumanTasksToExecute().get(index);
		
		caricaDatiWorkflow(externalContext, messageContext, showFolderPageBean, userBean);
	}

	//<evaluate expression="showFolderService.createWorkflowExecuteTaskPageBean(requestParameters.index, showFolderPageBean, userBean, externalContext, messageContext)" result="viewScope.workflowExecuteTaskPageBean"/>
	//requestParameters.index, showFolderPageBean, userBean, externalContext, messageContext
	public WorkflowExecuteTaskPageBean createWorkflowExecuteTaskPageBean(int index, ShowFolderPageBean showFolderPageBean, UserBean userBean, ExternalContext externalContext, MessageContext messageContext) throws Exception {
		HumanTaskInstance task = showFolderPageBean.getWorkflowProcessInstance().getWorkflowProcessInstanceInfo().getHumanTasksToExecute().get(index);
		WorkflowExecuteTaskPageBean taskpb = new WorkflowExecuteTaskPageBean();
		taskpb.setHumanTaskInstance(task);
		taskpb.setTaskFormUrl(workflowService.getWorkflowBonita().getTaskExecutionFormUrl(showFolderPageBean.getWorkflowProcessInstance().getProcessDefinitionName(), showFolderPageBean.getWorkflowProcessInstance().getProcessDefinitionVersion(), task.getName(), task.getId(), "it"));
		return taskpb;
	}
	
	//<evaluate expression="showFolderService.createWorkflowExecuteTaskPageBean(requestParameters.index, showFolderPageBean, userBean, externalContext, messageContext)" result="viewScope.workflowExecuteTaskPageBean"/>
	//requestParameters.index, showFolderPageBean, userBean, externalContext, messageContext
	public void aggiornaWorkflow(ShowFolderPageBean showFolderPageBean, UserBean userBean, ExternalContext externalContext, MessageContext messageContext, ExtraWayService xwService) throws Exception {
		//Aggiorno anche la parte extraway perche' potrebbe essere stata modificata dal workflow
		XMLDocumento document = null;
		try {
			document = xwService.loadDocument(Integer.parseInt(showFolderPageBean.getFascicolo().getIdUnit()), false, false, false, false); //primo tentativo (caricacamento inset)
		}
		catch (Exception e) {
			document = xwService.loadDocument(Integer.parseInt(showFolderPageBean.getFascicolo().getIdUnit()), false, true, false, false); //secondo tentativo (caricamento outset) nel caso si provenga da inserimento di nuovo fascicolo
		}
		if (document == null)
			throw new BusinessException("Impossibile caricare il fascicolo. Fascicolo non trovato");
		
		showFolderPageBean.getFascicolo().setContents(document);

		//Aggiorno anche la parte documenti allegati perche potrebbero essere stata modificata da dei connettori nel flusso
		showFolderPageBean.setAllDocumentsL(getFascicoloDocumentsL(externalContext, userBean, showFolderPageBean.getFascicolo().getContents(), xwService));

		//ricarico il workflow
		caricaDatiWorkflow(externalContext, messageContext, showFolderPageBean, userBean);
		
		//ricarico l'eventuale delibera da workflow se modificabile, questo per le domande prima della gestione delle delibere doppie con gli AttoInst
		caricaDatiDeliberaDaWorkflow(externalContext, showFolderPageBean, userBean);
		
		//ricarico gli eventuali AttoInst se gestiti, questo per le domande dalla gestione delle delibere doppie con gli AttoInst
		caricaAttiInstDomanda(userBean, showFolderPageBean);
		
		//calcolaSeProntoPerInvio(showFolderPageBean); //requisiti non occorre rieseguirlo

		//calcolaSeInBozza(showFolderPageBean);//calcolato al volo
		//calcolaSeEliminabile(showFolderPageBean);//calcolato al volo
		//calcolaSeValutabile(showFolderPageBean); //calcolato al volo
		//calcolaSeRichiedibileDiIntegrazioni(showFolderPageBean);//Inutile
		calcolaSeInviabileReinviabile(showFolderPageBean);
		//calcolaSeReinviabile(showFolderPageBean); //requisiti
		//calcolaSeModificabilePerValtuazioneRegione(showFolderPageBean);
		//calcolaSeModificabilePerIntegrazioni(showFolderPageBean);
		calcolaSeConcludibile(showFolderPageBean); //esiti
	}
	
	
	public UpdateUdoModelPageBean createUpdateUdoModelPageBean(int index, ShowFolderPageBean showFolderPageBean, UserBean userBean, ExternalContext externalContext, MessageContext messageContext) throws Exception {
		UdoUoInstForList udoUoInstForListMod =  showFolderPageBean.getFilteredUdoUoInstForL().get(index);
		if(udoUoInstForListMod.isUdo()) {
			UpdateUdoModelPageBean toRet = new UpdateUdoModelPageBean();
			//storicoRisposteRequisiti.setClientid(UUID.randomUUID().toString().toUpperCase());
			TokenSession tokenSession = new TokenSession();
			tokenSession.setToken(UUID.randomUUID().toString().toUpperCase());
			tokenSessionDao.save(tokenSession);
			
			toRet.setTokenSession(tokenSession);
			toRet.setUdoModelClientId(udoUoInstForListMod.getUdoInst().getUdoModelClientid());
			toRet.setUdoUpdateUrl(sanitaRegioneVenetoUrl + "?CMD=UDO_MODEL&token=" + tokenSession.getToken() 
					+ "&utente=" + userBean.getUtenteModel().getClientid() 
					+ "&titolare=" + showFolderPageBean.getTitolareDomanda().getClientid()
					+ "&udo=" + toRet.getUdoModelClientId());
			return toRet;
		}
		return null;
	}

	public void aggiornaUdoInst(UpdateUdoModelPageBean updateUdoModelPageBean, ShowFolderPageBean showFolderPageBean, UserBean userBean, ExternalContext externalContext, MessageContext messageContext) throws Exception {
		//Devo ricaricare e aggiornare nella lista delle UdoUoInstForList la udoInst relativa alla UdoModel modificata
		ListIterator<UdoUoInstForList> listIterator = showFolderPageBean.getFascicolo().getUdoUoInstForL().listIterator();

		UdoUoInstForList udoUoInstForList;
	    while (listIterator.hasNext()) {
			udoUoInstForList = listIterator.next();
			if(udoUoInstForList.isUdo() && udoUoInstForList.getUdoInst().getUdoModelClientid().equals(updateUdoModelPageBean.getUdoModelClientId())) {
				UdoInst updatedUdoInst = udoInstDao.findByClientId(udoUoInstForList.getClientId());
				listIterator.set(new UdoUoInstForList(userBean, updatedUdoInst));
				return;
			}
	    }
	}
	
	
	public void addPostit(ShowFolderPageBean showFolderPageBean, UserBean userBean, MessageContext messageContext, ExtraWayService xwService) throws Exception {
		String testo = showFolderPageBean.getNuovaAnnotazione();
		String operatore =  userBean.getUtenteModel().getLoginDbOrCas();
		String ruolo = userBean.getRuolo();
		Date date = new Date();
		String data = new SimpleDateFormat("yyyyMMdd").format(date);
		String ora = new SimpleDateFormat("HH:mm:ss").format(date);

		XMLDocumento document;

		document = FolderDao.addPostit(xwService, Integer.parseInt(showFolderPageBean.getFascicolo().getIdUnit()), testo, operatore, ruolo, data, ora, userBean.isImitatingOperatore());
		
		showFolderPageBean.getFascicolo().setContents(document); //refresh immediato (per visualizzare la nuova annotazione)
		
		showFolderPageBean.setNuovaAnnotazione("");
		
		//mail
		try {
			if(xwService.getAbilitaInvioMail().equals("S")){
				Parametri mailSubject = parametriDao.findOne(ParametriDao.NOTIFICA_ANNOTAZIONE_SUBJECT);
				Parametri mailBody = parametriDao.findOne(ParametriDao.NOTIFICA_ANNOTAZIONE_BODY);
				
				String idProcedimento = document.getAttributeValue("//fascicolo/extra/procedimento/@domanda_client_id", "");
				TitolareModel  titolareDelProcedimento = domandaInstDao.findOne(idProcedimento).getTitolareModel();
				String mailTitolare = titolareDelProcedimento.getEmail();
				
				Mail m = new Mail(xwService.getSmtpHost(), xwService.getSmtpPort(), xwService.getSmtpProtocol(), xwService.getSmtpUser(), xwService.getSmtpPwd());
				String mailSender = xwService.getMailPrincipale();
				String mailReceiver = null;
		
				if (userBean.isREGIONE()){			
					if (!xwService.getMailDebug().isEmpty())
						mailReceiver = xwService.getMailDebug();
					else
						mailReceiver = mailTitolare;
				}
				else{
					mailReceiver = xwService.getMailPrincipale();
				}
				String mailSubjectToSend = mailSubject.getValore();
				String mailBodyToSend = mailBody.getValore();
		
				m.sendMail(mailSubjectToSend, mailBodyToSend, mailSender, mailSender, mailReceiver);
			}
		}
		catch (Exception e) { //errore di invio email non deve essere bloccante
			Logger.getRootLogger().error(e);
			MessageBuilder mB = new MessageBuilder();
			messageContext.addMessage(mB.error().source("messages").defaultText("Si è verificato un problema nell'invio di una email di notifica").build());
		}		
		
	}
	
	public void deletePostitGen(String tipo, String index, ExtraWayService xwService, ShowFolderPageBean showFolderPageBean) throws Exception {
		PostItTypeEnum postItTypeEnum = PostItTypeEnum.valueOf(tipo);
		switch (postItTypeEnum) {
		case Generico:
			deletePostit(index, xwService, showFolderPageBean);
			break;
		case MancanzaTipiUdo:
			deletePostitMancanzaTipiUdo(index, xwService, showFolderPageBean);
			break;
		}
	}	

	public void checkPostitGen(String tipo, String index, ExtraWayService xwService, ShowFolderPageBean showFolderPageBean) throws Exception {
		PostItTypeEnum postItTypeEnum = PostItTypeEnum.valueOf(tipo);
		if (postItTypeEnum == PostItTypeEnum.MancanzaTipiUdo) {
			XMLDocumento docSave = FolderDao.checkPostitMancanzaTipiUdo(xwService, Integer.parseInt(showFolderPageBean.getFascicolo().getIdUnit()), Integer.parseInt(index));
			showFolderPageBean.getFascicolo().setContents(docSave);		
		}
	}	

	public void deletePostit(String index, ExtraWayService xwService, ShowFolderPageBean showFolderPageBean) throws Exception {
		XMLDocumento docSave = FolderDao.deletePostit(xwService, Integer.parseInt(showFolderPageBean.getFascicolo().getIdUnit()), Integer.parseInt(index));
		
		showFolderPageBean.getFascicolo().setContents(docSave);
		//update pagina
		//showFolderPageBean.getFascicolo().getPostitL().remove(Integer.parseInt(index));
		/*int pos = 0;
		for(PostIt postIt : showFolderPageBean.getFascicolo().getPostitL()) {
			if(postIt.getXmlIndex() == Integer.parseInt(index) && postIt.getTipo() == PostItTypeEnum.Generico) {
				showFolderPageBean.getFascicolo().getPostitL().remove(Integer.parseInt(index));
				break;
			}
			pos++;
		}*/
	}	
	
	public void deletePostitMancanzaTipiUdo(String index, ExtraWayService xwService, ShowFolderPageBean showFolderPageBean) throws Exception {
		XMLDocumento docSave = FolderDao.deletePostitMancanzaTipiUdo(xwService, Integer.parseInt(showFolderPageBean.getFascicolo().getIdUnit()), Integer.parseInt(index));
		
		showFolderPageBean.getFascicolo().setContents(docSave);		
		//update pagina
		//showFolderPageBean.getFascicolo().getPostitMancanzaTipiUdoL().remove(Integer.parseInt(index));
		/*int pos = 0;
		for(PostIt postIt : showFolderPageBean.getFascicolo().getPostitL()) {
			if(postIt.getXmlIndex() == Integer.parseInt(index) && postIt.getTipo() == PostItTypeEnum.MancanzaTipiUdo) {
				showFolderPageBean.getFascicolo().getPostitL().remove(Integer.parseInt(index));
				break;
			}
			pos++;
		}*/
	}	
	
	public boolean validateFormAddPostit(MessageContext messageContext, ShowFolderPageBean showFolderPageBean) {
		MessageBuilder mB = new MessageBuilder();
		
		boolean errors = false;
		
		//testo annotazione vuoto
		if (showFolderPageBean.getNuovaAnnotazione().length() == 0) {
			messageContext.addMessage(mB.error().source("nuovaAnnotazione").defaultText("Inserire il testo dell'annotazione").build());
			messageContext.addMessage(mB.error().source("messages").defaultText("Inserire il testo dell'annotazione").build());
			errors = true;
		}
		
		return !errors;
	}	
	
	public void raiseError(MessageContext messageContext, String errorMessage, String field) {
		MessageBuilder mB = new MessageBuilder();
		messageContext.addMessage(mB.error().source("messages").defaultText(errorMessage).build());
		if (field != null && field.length() > 0)
			messageContext.addMessage(mB.error().source(field).defaultText(errorMessage).build());
	}
	
	public void updatePersonaL(ShowFolderPageBean showFolderPageBean) throws Exception {
		List<PersonaSelectOption> personaL = new ArrayList<PersonaSelectOption>();

		List<UoModel> uoModelL = null;
		if (showFolderPageBean.getUoSelected().length() == 0)
			uoModelL = showFolderPageBean.getUoL();
		else {
			uoModelL = new ArrayList<UoModel>();
			uoModelL.add(uoModelDao.findByClientId(showFolderPageBean.getUoSelected()));
		}
		if(uoModelL != null) {
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
		}
		showFolderPageBean.setPersonaL(personaL);
	}

	public void assegnazioneMassivaRequisiti(UserBean userBean, ShowFolderPageBean showFolderPageBean) {
		String operatore = "";
		if(userBean.getUtenteModel() != null && userBean.getUtenteModel().getAnagraficaUtenteModel() != null) {
			operatore = userBean.getUtenteModel().getAnagraficaUtenteModel().getCognome() + " " + userBean.getUtenteModel().getAnagraficaUtenteModel().getNome();
			if(userBean.getUtenteModel().getUoModel() != null && userBean.getUtenteModel().getUoModel().getDenominazione() != null)
				operatore = userBean.getUtenteModel().getUoModel().getDenominazione() + " - " + operatore;
		}

		//Admin N o S
		String admin = "N";
		if(userBean.isImitatingOperatore())
			admin = "S";
		
		domandaInstDao.assegnazioneMassivaRequisiti(showFolderPageBean.getFascicolo().getDomandaClienId(), operatore, admin);
		
	}
	
	public void assegnaRequisitoAll(UserBean userBean, ShowFolderPageBean showFolderPageBean) {
		UtenteModel utenteModel = (showFolderPageBean.getPersonaSelected().length() == 0)? null: utenteDao.findOne(showFolderPageBean.getPersonaSelected());
		Map<String, Entry<String, Boolean>> checkM = showFolderPageBean.getCheckM();
		for (UdoUoInstForList udoUoInstForList:showFolderPageBean.getFascicolo().getUdoUoInstForL()) { //per ogni udo/uo nella pagina

			if (checkM.containsKey(udoUoInstForList.getClientId())) { //se la udo/uo/requisitiGeneraliDomanda è checked
				//carico tutti i requisiti della udo/uo corrente
				List<RequisitoInst> requisitoInstL = null;
				if(udoUoInstForList.isUdo()) {
					UdoInst udoInst = udoInstDao.findOne(udoUoInstForList.getClientId());
					requisitoInstL = udoInst.getRequisitoInsts();
				}
				else if (udoUoInstForList.isUo()) {
					UoInst uoInst = uoInstDao.findOne(udoUoInstForList.getClientId());
					requisitoInstL = uoInst.getRequisitoInsts();
				}
				else if (udoUoInstForList.isStruttura()) {
					StrutturaInst strutturaInst = strutturaInstDao.findOne(udoUoInstForList.getClientId());
					requisitoInstL = strutturaInst.getRequisitoInsts();
				}
				else if (udoUoInstForList.isEdificio()) {
					EdificioInst edificioInst = edificioInstDao.findOne(udoUoInstForList.getClientId());
					requisitoInstL = edificioInst.getRequisitoInsts();
				}
				else {
					DomandaInst domandaInst = domandaInstDao.findOne(udoUoInstForList.getClientId());
					requisitoInstL = domandaInst.getRequisitoInsts();
				}
				
				if(requisitoInstL != null) {
					for (RequisitoInst requisitoInst:requisitoInstL) { //per ogni requisito
						RequisitoInst jpaFreshRequisitoInst = requisitoInstDao.findByClientId(requisitoInst.getClientid());
						
						String assegnatarioStoria = "";
						
						if (utenteModel != null) {
							String newUtenteModelClientId = utenteModel.getClientid();
							String oldUtenteModelClientId =  jpaFreshRequisitoInst.getUtenteModel() == null? "" : jpaFreshRequisitoInst.getUtenteModel().getClientid();
							if (!newUtenteModelClientId.equals(oldUtenteModelClientId)) {
								AnagraficaUtenteModel anagraficaUenteModel = utenteModel.getAnagraficaUtenteModel(); 
								String operatore = anagraficaUenteModel.getCognome() + " " + anagraficaUenteModel.getNome();
								String uo = "";
								try {
									uo = utenteModel.getUoModel().getDenominazione() + " - ";
								}
								catch (Exception e) {
									
								}
								assegnatarioStoria = uo + operatore;					
							}
							jpaFreshRequisitoInst.setUtenteModel(utenteModel);			
						}
						else {
							if (jpaFreshRequisitoInst.getUtenteModel() != null)
								assegnatarioStoria = "---Rimossa assegnazione---";
							jpaFreshRequisitoInst.setUtenteModel(null);						
						}
						
						if (assegnatarioStoria.length() > 0) { //se è stato modificata l'assegnazione -> save
							StoricoRisposteRequisiti storicoRisposteRequisiti = buildStoricoRisposteRequisiti(jpaFreshRequisitoInst, userBean, assegnatarioStoria);
							storicoRisposteRequisitiDao.save(storicoRisposteRequisiti);
							jpaFreshRequisitoInst.addStoricoRisposteRequisiti(storicoRisposteRequisiti);
							requisitoInstDao.save(jpaFreshRequisitoInst);			
						}
						
					}
				}
			}
			
		}
	}
	
	public void assegnaVerificaRequisitoAll(UserBean userBean, ShowFolderPageBean showFolderPageBean) {
		UtenteModel utenteModel = (showFolderPageBean.getPersonaSelected().length() == 0)? null: utenteDao.findOne(showFolderPageBean.getPersonaSelected());
		Map<String, Entry<String, Boolean>> checkM = showFolderPageBean.getCheckM();
		for (UdoUoInstForList udoUoInstForList:showFolderPageBean.getFascicolo().getUdoUoInstForL()) { //per ogni udo/uo nella pagina

			if (checkM.containsKey(udoUoInstForList.getClientId())) { //se la udo/uo/requisitiGeneraliDomanda è checked
				//carico tutti i requisiti della udo/uo corrente
				List<RequisitoInst> requisitoInstL = null;
				if(udoUoInstForList.isUdo()) {
					UdoInst udoInst = udoInstDao.findOne(udoUoInstForList.getClientId());
					requisitoInstL = udoInst.getRequisitoInsts();
				}
				else if (udoUoInstForList.isUo()) {
					UoInst uoInst = uoInstDao.findOne(udoUoInstForList.getClientId());
					requisitoInstL = uoInst.getRequisitoInsts();
				}
				else if (udoUoInstForList.isStruttura()) {
					StrutturaInst strutturaInst = strutturaInstDao.findOne(udoUoInstForList.getClientId());
					requisitoInstL = strutturaInst.getRequisitoInsts();
				}
				else if (udoUoInstForList.isEdificio()) {
					EdificioInst edificioInst = edificioInstDao.findOne(udoUoInstForList.getClientId());
					requisitoInstL = edificioInst.getRequisitoInsts();
				}
				else {
					DomandaInst domandaInst = domandaInstDao.findOne(udoUoInstForList.getClientId());
					requisitoInstL = domandaInst.getRequisitoInsts();
				}
				
				if(requisitoInstL != null) {
					for (RequisitoInst requisitoInst:requisitoInstL) { //per ogni requisito
						RequisitoInst jpaFreshRequisitoInst = requisitoInstDao.findByClientId(requisitoInst.getClientid());
						
						String assegnatarioVerificaStoria = "";
						
						if (utenteModel != null) {
							String newVerificatoreClientId = utenteModel.getClientid();
							String oldVerificatoreClientId =  jpaFreshRequisitoInst.getVerificatore() == null? "" : jpaFreshRequisitoInst.getVerificatore().getClientid();
							if (!newVerificatoreClientId.equals(oldVerificatoreClientId)) {
								AnagraficaUtenteModel anagraficaUenteModel = utenteModel.getAnagraficaUtenteModel(); 
								assegnatarioVerificaStoria = anagraficaUenteModel.getCognome() + " " + anagraficaUenteModel.getNome();					
							}
							jpaFreshRequisitoInst.setVerificatore(utenteModel);			
						}
						else {
							if (jpaFreshRequisitoInst.getUtenteModel() != null)
								assegnatarioVerificaStoria = "---Rimossa assegnazione---";
							jpaFreshRequisitoInst.setVerificatore(null);						
						}
						
						if (assegnatarioVerificaStoria.length() > 0) { //se è stato modificata l'assegnazione -> save
							StoricoRisposteRequisiti storicoRisposteRequisiti = buildStoricoRisposteRequisiti(jpaFreshRequisitoInst, userBean, null, assegnatarioVerificaStoria);
							storicoRisposteRequisitiDao.save(storicoRisposteRequisiti);
							jpaFreshRequisitoInst.addStoricoRisposteRequisiti(storicoRisposteRequisiti);
							requisitoInstDao.save(jpaFreshRequisitoInst);			
						}
					}
				}
			}
			
		}
	}

	public void mostraTutti(UserBean userBean, ShowFolderPageBean showFolderPageBean, boolean skipOnRender) {
		if (skipOnRender)
			return;
		showFolderPageBean.clearUdoSearchForm();
				
		showFolderPageBean.setActiveRowIndex(0);
		
		//showFolderPageBean.filterUdoUoInstForL();
		showFolderPageBean.mostraTutti();
	}
	
	public void filtraUoUdoDomanda(UserBean userBean, ShowFolderPageBean showFolderPageBean, boolean skipOnRender) {
		if (skipOnRender)
			return;
		showFolderPageBean.setActiveRowIndex(0);
		AreaDiscipline areaDiscipline = null;
		if(showFolderPageBean.getUdoInstSearchOrderBean() != null && showFolderPageBean.getUdoInstSearchOrderBean().getAreaDisciplineDaCercare() != null && !showFolderPageBean.getUdoInstSearchOrderBean().getAreaDisciplineDaCercare().isEmpty()) {
			areaDiscipline = areaDisciplineDao.findOne(showFolderPageBean.getUdoInstSearchOrderBean().getAreaDisciplineDaCercare());
			areaDiscipline.getDisciplinaTempls().size();
		}
		List<String> clientidUdoSenzaRequisiti = null;
		if(showFolderPageBean.getUdoInstSearchOrderBean().isShowOnlyUdoConRequisiti() && showFolderPageBean.getFascicolo() != null && showFolderPageBean.getFascicolo().getDomandaClienId() != null) {
			clientidUdoSenzaRequisiti = udoInstDao.getUdoInstClientIdInDomandaSenzaRequisiti(showFolderPageBean.getFascicolo().getDomandaClienId());
		}		
		showFolderPageBean.filterUdoUoInstForL(userBean.getUtenteModel(), areaDiscipline, clientidUdoSenzaRequisiti);
	}

	public void ordinaUoUdoDomanda(UserBean userBean, ShowFolderPageBean showFolderPageBean, boolean skipOnRender) {
		if (skipOnRender)
			return;
		AreaDiscipline areaDiscipline = null;
		if(showFolderPageBean.getUdoInstSearchOrderBean() != null && showFolderPageBean.getUdoInstSearchOrderBean().getAreaDisciplineDaCercare() != null && !showFolderPageBean.getUdoInstSearchOrderBean().getAreaDisciplineDaCercare().isEmpty()) {
			areaDiscipline = areaDisciplineDao.findOne(showFolderPageBean.getUdoInstSearchOrderBean().getAreaDisciplineDaCercare());
			areaDiscipline.getDisciplinaTempls().size();			
		}
		List<String> clientidUdoSenzaRequisiti = null;
		if(showFolderPageBean.getUdoInstSearchOrderBean().isShowOnlyUdoConRequisiti() && showFolderPageBean.getFascicolo() != null && showFolderPageBean.getFascicolo().getDomandaClienId() != null) {
			clientidUdoSenzaRequisiti = udoInstDao.getUdoInstClientIdInDomandaSenzaRequisiti(showFolderPageBean.getFascicolo().getDomandaClienId());
		}		
		showFolderPageBean.setActiveRowIndex(0);
		showFolderPageBean.orderUdoUoInstForL(userBean.getUtenteModel(), areaDiscipline, clientidUdoSenzaRequisiti);
	}

	public void orderOperatorePostIt(ShowFolderPageBean showFolderPageBean, boolean skipOnRender) {
		if (skipOnRender)
			return;
		switch (showFolderPageBean.getFascicolo().getPostItOrderTypeEnum()) {
			case OperatoreCrescente:
				showFolderPageBean.getFascicolo().setPostItOrderTypeEnum(PostItOrderTypeEnum.OperatoreDecrescente);
				break;
			case OperatoreDecrescente:
				showFolderPageBean.getFascicolo().setPostItOrderTypeEnum(PostItOrderTypeEnum.OperatoreCrescente);
				break;
			default:
				showFolderPageBean.getFascicolo().setPostItOrderTypeEnum(PostItOrderTypeEnum.OperatoreDecrescente);
				break;
		}
	}

	public void orderDataPostIt(ShowFolderPageBean showFolderPageBean, boolean skipOnRender) {
		if (skipOnRender)
			return;
		switch (showFolderPageBean.getFascicolo().getPostItOrderTypeEnum()) {
			case DataCrescente:
				showFolderPageBean.getFascicolo().setPostItOrderTypeEnum(PostItOrderTypeEnum.DataDecrescente);
				break;
			case DataDecrescente:
				showFolderPageBean.getFascicolo().setPostItOrderTypeEnum(PostItOrderTypeEnum.DataCrescente);
				break;
			default:
				showFolderPageBean.getFascicolo().setPostItOrderTypeEnum(PostItOrderTypeEnum.DataDecrescente);
				break;
		}
	}

	public void showCheckedUdoUoList(UserBean userBean, ShowFolderPageBean showFolderPageBean, boolean skipOnRender) {
		if (skipOnRender)
			return;
		showFolderPageBean.clearUdoSearchForm();
		showFolderPageBean.setActiveRowIndex(0);
		
		showFolderPageBean.showCheckedUdoUoList();
	}

	public StoricoRisposteRequisiti buildStoricoRisposteRequisiti(RequisitoInst requisitoInst, UserBean userBean, String assegnatario) {
		/*StoricoRisposteRequisiti storicoRisposteRequisiti = new StoricoRisposteRequisiti();
		storicoRisposteRequisiti.setClientid(UUID.randomUUID().toString().toUpperCase());
		storicoRisposteRequisiti.setValutazione("");
		storicoRisposteRequisiti.setNote("");
		storicoRisposteRequisiti.setEvidenze("");
		storicoRisposteRequisiti.setAssegnatario(assegnatario);
		storicoRisposteRequisiti.setRequisitoInst(requisitoInst);
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
		
		return storicoRisposteRequisiti;*/
		return buildStoricoRisposteRequisiti(requisitoInst, userBean, assegnatario, null);
	}
	
	public StoricoRisposteRequisiti buildStoricoRisposteRequisiti(RequisitoInst requisitoInst, UserBean userBean, String assegnatario, String verificatore) {
		StoricoRisposteRequisiti storicoRisposteRequisiti = new StoricoRisposteRequisiti();
		storicoRisposteRequisiti.setClientid(UUID.randomUUID().toString().toUpperCase());
		storicoRisposteRequisiti.setValutazione("");
		storicoRisposteRequisiti.setNote("");
		storicoRisposteRequisiti.setEvidenze("");
		storicoRisposteRequisiti.setAssegnatario(assegnatario);
		storicoRisposteRequisiti.setDisabled("N");

		storicoRisposteRequisiti.setValutazioneVerificatore("");
		storicoRisposteRequisiti.setNoteVerificatore("");
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
	
	
	public void notificaRequisitoAll(UserBean userBean, ShowFolderPageBean showFolderPageBean, ExtraWayService xwService, MessageContext messageContext) {
		String domandaClientId = showFolderPageBean.getFascicolo().getDomandaClienId();
		List<UtenteModel> utenteModelL = new ArrayList<UtenteModel>();
		
		//TODO ottimizzare occorre ottenere tutti gli UtenteModel a cui è assegnato almeno un requisito della domanda
		List<RequisitoInst> requisitoInstL = requisitoInstDao.findAllRequisitiByDomandaInst(domandaClientId);		
		for (RequisitoInst requisitoInst:requisitoInstL) {
			UtenteModel utenteModel = requisitoInst.getUtenteModel();
			if (utenteModel != null && !utenteModelL.contains(utenteModel))
				utenteModelL.add(utenteModel);
		}
		
		String[] emails = new String[utenteModelL.size()];
		for (int i=0; i<utenteModelL.size(); i++) { //per ogni anagrafica individuata
			UtenteModel utenteModel = utenteModelL.get(i);
			emails[i] = utenteModel.getAnagraficaUtenteModel().getEmail();
		}
				
		try {
			//mail
			if(xwService.getAbilitaInvioMail().equals("S")){
//				String mailSubjectToSend = "Notifica di assegnazione requisiti applicativo regionale A.re.A";
	//			String mailBodyToSend = "Gentile utente, le sono stati assegnati dei requisiti di autovalutazione all'interno dell'applicativo A.re.A.\n" +
		//				"Si colleghi all'applicativo per rispondere ai requisiti.";
				String mailSubjectToSend = parametriDao.findOne(ParametriDao.NOTIFICA_TUTTI_REQUISITI_SUBJECT).getValore();
				String mailBodyToSend = parametriDao.findOne(ParametriDao.NOTIFICA_TUTTI_REQUISITI_BODY).getValore();
				Mail mailATitolare = new Mail(xwService.getSmtpHost(), xwService.getSmtpPort(), xwService.getSmtpProtocol(), xwService.getSmtpUser(), xwService.getSmtpPwd());
				String mailSender = xwService.getMailPrincipale();
				if (!xwService.getMailDebug().isEmpty())
					mailATitolare.sendMail(mailSubjectToSend, mailBodyToSend, mailSender, mailSender, xwService.getMailDebug());
				else if (emails.length > 0)
					mailATitolare.sendMail(mailSubjectToSend, mailBodyToSend, mailSender, mailSender, emails);				
			}
		}
		catch (Exception e) { //errore di invio email non deve essere bloccante
			Logger.getRootLogger().error(e);
			MessageBuilder mB = new MessageBuilder();
			messageContext.addMessage(mB.error().source("messages").defaultText("Si è verificato un problema nell'invio di una email di notifica").build());
		}
	}
	
	public void notificaVerificaRequisitoAll(UserBean userBean, ShowFolderPageBean showFolderPageBean, ExtraWayService xwService, MessageContext messageContext) {
		String domandaClientId = showFolderPageBean.getFascicolo().getDomandaClienId();
		Set<UtenteModel> utenteModelL = new HashSet<UtenteModel>();
		
		//TODO ottimizzare occorre ottenere tutti gli UtenteModel a cui è assegnato in verifica almeno un requisito della domanda
		List<RequisitoInst> requisitoInstL = requisitoInstDao.findAllRequisitiByDomandaInst(domandaClientId);		
		UtenteModel utenteModel;
		for (RequisitoInst requisitoInst:requisitoInstL) {
			utenteModel = requisitoInst.getVerificatore();
			if (utenteModel != null && !utenteModelL.contains(utenteModel))
				utenteModelL.add(utenteModel);
		}
		
		if(utenteModelL.size() > 0) {
			String[] emails = new String[utenteModelL.size()];
			int i=0;
			for (UtenteModel um : utenteModelL) { //per ogni anagrafica individuata
				emails[i] = um.getAnagraficaUtenteModel().getEmail();
				i++;
			}
					
			try {
				//mail
				if(xwService.getAbilitaInvioMail().equals("S")){
					//Aggiungere il numero procedimento
					//showFolderPageBean.getFascicolo().getNumeroProcedimento()
					String mailSubjectToSend = parametriDao.findOne(ParametriDao.NOTIFICA_VERIFICA_TUTTI_REQUISITI_SUBJECT).getValore();
					String mailBodyToSend = parametriDao.findOne(ParametriDao.NOTIFICA_VERIFICA_TUTTI_REQUISITI_BODY).getValore();
					mailBodyToSend = String.format(mailBodyToSend, showFolderPageBean.getFascicolo().getNumeroProcedimento());
					Mail mailATitolare = new Mail(xwService.getSmtpHost(), xwService.getSmtpPort(), xwService.getSmtpProtocol(), xwService.getSmtpUser(), xwService.getSmtpPwd());
					String mailSender = xwService.getMailPrincipale();
					if (!xwService.getMailDebug().isEmpty())
						mailATitolare.sendMail(mailSubjectToSend, mailBodyToSend, mailSender, mailSender, xwService.getMailDebug());
					else if (emails.length > 0)
						mailATitolare.sendMail(mailSubjectToSend, mailBodyToSend, mailSender, mailSender, emails);				
				}
			}
			catch (Exception e) { //errore di invio email non deve essere bloccante
				Logger.getRootLogger().error(e);
				MessageBuilder mB = new MessageBuilder();
				messageContext.addMessage(mB.error().source("messages").defaultText("Si è verificato un problema nell'invio di una email di notifica della verifica").build());
			}
		}
	}

	public FolderPdfBean getPdfPageBean(ShowFolderPageBean sfpb) {
		return getPdfPageBean(sfpb, true);
	}

	public FolderPdfBean getPdfPageBean(ShowFolderPageBean sfpb, boolean showAll) {
		FolderPdfBean toRet = new FolderPdfBean();
		
		try {
			Map<String, Entry<String, Boolean>> checkM = sfpb.getCheckM();
	    	
			//long startTime = System.currentTimeMillis();
			//long requiredTime;

			//Testare se veramente piu' lento, ATTENZIONE non si puo' usare cosi' perche' non ordina i requisiti
			/*for (UdoUoInstForList udoUoInstForList:sfpb.getFascicolo().getUdoUoInstForL()) { //per ogni udo/uo nella pagina
				if (showAll || checkM.containsKey(udoUoInstForList.getClientId())) { //se la udo/uo/requisitiGeneraliDomanda è checked
					List<RequisitoInst> requisitoInstL = null;
					if(udoUoInstForList.isUdo()) {
						UdoInst udoInst = udoInstDao.findOne(udoUoInstForList.getClientId());
						requisitoInstL = udoInst.getRequisitoInsts();
						toRet.addRequisitiGenUdoUoInstForPdf(new RequisitiGenUdoUoInstForPdf(udoInst, requisitoInstL));
					}
					else if (udoUoInstForList.isUo()) {
						UoInst uoInst = uoInstDao.findOne(udoUoInstForList.getClientId());
						requisitoInstL = uoInst.getRequisitoInsts();
						toRet.addRequisitiGenUdoUoInstForPdf(new RequisitiGenUdoUoInstForPdf(uoInst, requisitoInstL));
					}
					else {
						DomandaInst domandaInst = domandaInstDao.findOne(udoUoInstForList.getClientId());
						requisitoInstL = domandaInst.getRequisitoInsts();
						toRet.addRequisitiGenUdoUoInstForPdf(new RequisitiGenUdoUoInstForPdf(requisitoInstL));
					}
				}		
			}*/
			
			
			for (UdoUoInstForList udoUoInstForList:sfpb.getFascicolo().getUdoUoInstForL()) { //per ogni udo/uo nella pagina
				if (showAll || checkM.containsKey(udoUoInstForList.getClientId())) { //se la udo/uo/requisitiGeneraliDomanda è checked
					//List<RequisitoInst> requisitoInstL = null;
					if(udoUoInstForList.isUdo()) {
						//Occorre caricare della udo getFattProdUdoInsts e i requisiti ordinati
						//udoUoInstForList.getUdoInst().setFattProdUdoInsts(fattProdUdoInstDao.findFattProdUdoInstByUdoInst(udoUoInstForList.getUdoInst()));
						toRet.addRequisitiGenUdoUoInstForPdf(new RequisitiGenUdoUoInstForPdf(udoUoInstForList.getUdoInst(), requisitoInstDao.findRequisitiByUdoInst(udoUoInstForList.getUdoInst(), true, false, false)));
					}
					else if (udoUoInstForList.isUo()) {
						toRet.addRequisitiGenUdoUoInstForPdf(new RequisitiGenUdoUoInstForPdf(udoUoInstForList.getUoInst(), requisitoInstDao.findRequisitiByUoInst(udoUoInstForList.getUoInst(), true, false, false)));
					}
					else if (udoUoInstForList.isStruttura()) {
						toRet.addRequisitiGenUdoUoInstForPdf(new RequisitiGenUdoUoInstForPdf(udoUoInstForList.getStrutturaInst(), requisitoInstDao.findRequisitiByStrutturaInst(udoUoInstForList.getStrutturaInst(), true, false, false)));
					}
					else if (udoUoInstForList.isEdificio()) {
						toRet.addRequisitiGenUdoUoInstForPdf(new RequisitiGenUdoUoInstForPdf(udoUoInstForList.getEdificioInst(), requisitoInstDao.findRequisitiByEdificioInst(udoUoInstForList.getEdificioInst(), true, false, false)));
					}
					else {
						toRet.addRequisitiGenUdoUoInstForPdf(new RequisitiGenUdoUoInstForPdf(requisitoInstDao.findRequisitiGeneraliAziendali(udoUoInstForList.getDomandaInst(), true, false, false)));
					}
				}		
			}
			//requiredTime = System.currentTimeMillis() - startTime;
			//System.out.println("Tempo caricamento dati per pdf: " + requiredTime + " ms");
			//log.debug("Tempo caricamento dati per pdf: " + requiredTime + " ms");
		} catch (Exception ex) {
			log.error("Errore creando il FolderPdfBean", ex);
		}
		
		return toRet;
	}

	public FolderPdfBean getVerificaPdfPageBean(ShowFolderPageBean sfpb, boolean showAll) {
		FolderPdfBean toRet = new FolderPdfBean();
		
		try {
			Map<String, Entry<String, Boolean>> checkM = sfpb.getCheckM();
	    	
			for (UdoUoInstForList udoUoInstForList:sfpb.getFascicolo().getUdoUoInstForL()) { //per ogni udo/uo nella pagina
				if (showAll || checkM.containsKey(udoUoInstForList.getClientId())) { //se la udo/uo/requisitiGeneraliDomanda è checked
					//List<RequisitoInst> requisitoInstL = null;
					if(udoUoInstForList.isUdo()) {
						//Occorre caricare della udo getFattProdUdoInsts e i requisiti ordinati
						//udoUoInstForList.getUdoInst().setFattProdUdoInsts(fattProdUdoInstDao.findFattProdUdoInstByUdoInst(udoUoInstForList.getUdoInst()));
						toRet.addRequisitiGenUdoUoInstForPdf(new RequisitiGenUdoUoInstForPdf(udoUoInstForList.getUdoInst(), requisitoInstDao.findRequisitiByUdoInst(udoUoInstForList.getUdoInst(), false, true, false)));
					}
					else if (udoUoInstForList.isUo()) {
						toRet.addRequisitiGenUdoUoInstForPdf(new RequisitiGenUdoUoInstForPdf(udoUoInstForList.getUoInst(), requisitoInstDao.findRequisitiByUoInst(udoUoInstForList.getUoInst(), false, true, false)));
					}
					else if (udoUoInstForList.isStruttura()) {
						toRet.addRequisitiGenUdoUoInstForPdf(new RequisitiGenUdoUoInstForPdf(udoUoInstForList.getStrutturaInst(), requisitoInstDao.findRequisitiByUoInst(udoUoInstForList.getUoInst(), false, true, false)));
					}
					else {
						toRet.addRequisitiGenUdoUoInstForPdf(new RequisitiGenUdoUoInstForPdf(requisitoInstDao.findRequisitiGeneraliAziendali(udoUoInstForList.getDomandaInst(), false, true, false)));
					}
				}		
			}
			//requiredTime = System.currentTimeMillis() - startTime;
			//System.out.println("Tempo caricamento dati per pdf: " + requiredTime + " ms");
			//log.debug("Tempo caricamento dati per pdf: " + requiredTime + " ms");
		} catch (Exception ex) {
			log.error("Errore creando il FolderPdfBean", ex);
		}
		
		return toRet;
	}

	public FolderVerbaleVerificaPdfBean getVerbaleVerificaPdfPageBean(ShowFolderPageBean sfpb, boolean showAll) {
		FolderVerbaleVerificaPdfBean toRet = new FolderVerbaleVerificaPdfBean();
		toRet.setPianificazioneVerificaDateVerifica(sfpb.getPianificazioneVerificaDateVerifica());
		toRet.setTeamVerificatori(sfpb.getTeamVerificatori());
		toRet.setVerificatoreTeamLeader(sfpb.getVerificatoreTeamLeader());
		if(sfpb.getFascicolo().getNoteVerifica() != null)
			toRet.setNoteVerifica(sfpb.getFascicolo().getNoteVerifica().getTesto());
		
		try {
			Map<String, Entry<String, Boolean>> checkM = sfpb.getCheckM();
	    	
			for (UdoUoInstForList udoUoInstForList:sfpb.getFascicolo().getUdoUoInstForL()) { //per ogni udo/uo nella pagina
				if (showAll || checkM.containsKey(udoUoInstForList.getClientId())) { //se la udo/uo/requisitiGeneraliDomanda è checked
					//List<RequisitoInst> requisitoInstL = null;
					if(udoUoInstForList.isUdo()) {
						//Occorre caricare della udo getFattProdUdoInsts e i requisiti ordinati
						//udoUoInstForList.getUdoInst().setFattProdUdoInsts(fattProdUdoInstDao.findFattProdUdoInstByUdoInst(udoUoInstForList.getUdoInst()));
						toRet.addRequisitiGenUdoUoInstForPdf(new RequisitiGenUdoUoInstForPdf(udoUoInstForList.getUdoInst(), requisitoInstDao.findRequisitiByUdoInst(udoUoInstForList.getUdoInst(), false, false, true)));
					}
					else if (udoUoInstForList.isUo()) {
						toRet.addRequisitiGenUdoUoInstForPdf(new RequisitiGenUdoUoInstForPdf(udoUoInstForList.getUoInst(), requisitoInstDao.findRequisitiByUoInst(udoUoInstForList.getUoInst(), false, false, true)));
					}
					else if (udoUoInstForList.isStruttura()) {
						toRet.addRequisitiGenUdoUoInstForPdf(new RequisitiGenUdoUoInstForPdf(udoUoInstForList.getStrutturaInst(), requisitoInstDao.findRequisitiByStrutturaInst(udoUoInstForList.getStrutturaInst(), false, false, true)));
					}
					else if (udoUoInstForList.isEdificio()) {
						toRet.addRequisitiGenUdoUoInstForPdf(new RequisitiGenUdoUoInstForPdf(udoUoInstForList.getEdificioInst(), requisitoInstDao.findRequisitiByEdificioInst(udoUoInstForList.getEdificioInst(), false, false, true)));
					}
					else {
						toRet.addRequisitiGenUdoUoInstForPdf(new RequisitiGenUdoUoInstForPdf(requisitoInstDao.findRequisitiGeneraliAziendali(udoUoInstForList.getDomandaInst(), false, false, true)));
					}
				}		
			}
			//requiredTime = System.currentTimeMillis() - startTime;
			//System.out.println("Tempo caricamento dati per pdf: " + requiredTime + " ms");
			//log.debug("Tempo caricamento dati per pdf: " + requiredTime + " ms");
		} catch (Exception ex) {
			log.error("Errore creando il FolderPdfBean", ex);
		}
		
		return toRet;
	}

	public void copyAutovalutazioneUdoUo(ShowFolderPageBean showFolderPageBean, int index) {
		showFolderPageBean.setCopiedUdoUoInst(showFolderPageBean.getFilteredUdoUoInstForL().get(index));
	}
	
	public void startAssegnazioneMassivaAutovalutazione(UserBean userBean, ShowFolderPageBean showFolderPageBean, ExtraWayService _xwService, ExternalContext externalContext) {
		int total = showFolderPageBean.getCheckM().size();
		JobBean amJobBean = new JobBean("autovalutazioneMassiva", total);
		showFolderPageBean.setAmJobBean(amJobBean);
		externalContext.getSessionMap().put("pageBean", showFolderPageBean); //si utilizza la session per fare get dello stato del JobBean (si bypassa la view serialization di webflow)

		//start del runnable che esegue l'operazione massiva
		AutovalutazioneMassivaRunnable amRunnable = new AutovalutazioneMassivaRunnable(amJobBean, showFolderPageBean, requisitoInstDao, storicoRisposteRequisitiDao,
				userBean, txManager, _xwService, autovalutazioneService);
		new Thread(amRunnable).start();
	}

	@SuppressWarnings("unchecked")
	public CheckTipoUdo22DomandaPageBean checkTipoUdo22Domanda(ShowFolderPageBean showFolderPageBean) {
		CheckTipoUdo22DomandaPageBean checkTipoUdo22DomandaPageBean = new CheckTipoUdo22DomandaPageBean();
		
		List<TipoUdoTempl> tipoUdoTemplL = (List<TipoUdoTempl>)tipoUdoTemplDao.findAllOrderByDescr(); //tutti i tipi udo22
		List<UdoUoInstForList> domandaUdoUoInstL = showFolderPageBean.getFascicolo().getUdoUoInstForL(); //lista delle UoUdo della domanda
		
		List<TipoUdoTempl> l = new ArrayList<TipoUdoTempl>();
		for (TipoUdoTempl tipoUdoTempl:tipoUdoTemplL) { //per ogni tipo
			if (!domandaContainsTipoUdo(domandaUdoUoInstL, tipoUdoTempl))
					l.add(tipoUdoTempl);
		}
		
		checkTipoUdo22DomandaPageBean.setTipoUdoTemplL(l);
		return checkTipoUdo22DomandaPageBean;
	}

//	<!-- report requisiti generali inizio -->
	public ReportValutazioneRGPageBean reportValutazioneRG(ShowFolderPageBean showFolderPageBean) throws ParseException {
		ReportValutazioneRGPageBean reportValutazioneRGPageBean = new ReportValutazioneRGPageBean(requisitoInstDao.findRequisitiGeneraliAziendaliForReport(showFolderPageBean.getFascicolo().getDomandaClienId()));
		
		return reportValutazioneRGPageBean;
	}
	
//	<transition on="reportVerificaRG" to="reportVerificaRG">
//		<set name="flashScope.showFolderPageBean" value="pageBean"/>
//	</transition>
	public ReportVerificaRGPageBean reportVerificaRG(ShowFolderPageBean showFolderPageBean) throws ParseException {
		ReportVerificaRGPageBean reportVerificaRGPageBean = new ReportVerificaRGPageBean(requisitoInstDao.findRequisitiGeneraliAziendaliForReport(showFolderPageBean.getFascicolo().getDomandaClienId()));
		
		return reportVerificaRGPageBean;
	}

//	<transition on="reportValutazioneRGArea" to="reportValutazioneRGArea">
//		<set name="flashScope.showFolderPageBean" value="pageBean"/>
//	</transition>
	public ReportValutazioneRGAreaPageBean reportValutazioneRGArea(ShowFolderPageBean showFolderPageBean) throws ParseException {
		ReportValutazioneRGAreaPageBean reportValutazioneRGAreaPageBean = new ReportValutazioneRGAreaPageBean(requisitoInstDao.findRequisitiGeneraliAziendaliForReportArea(showFolderPageBean.getFascicolo().getDomandaClienId()));
		
		return reportValutazioneRGAreaPageBean;
	}
//	<transition on="reportVerificaRGArea" to="reportVerificaRGArea">
//		<set name="flashScope.showFolderPageBean" value="pageBean"/>
//	</transition>
	public ReportVerificaRGAreaPageBean reportVerificaRGArea(ShowFolderPageBean showFolderPageBean) throws ParseException {
		ReportVerificaRGAreaPageBean reportVerificaRGAreaPageBean = new ReportVerificaRGAreaPageBean(requisitoInstDao.findRequisitiGeneraliAziendaliForReportArea(showFolderPageBean.getFascicolo().getDomandaClienId()));
		
		return reportVerificaRGAreaPageBean;
	}
//	<transition on="reportValutazioneUdo" to="reportValutazioneUdo">
//		<set name="flashScope.showFolderPageBean" value="pageBean"/>
//	</transition>
	public ReportValutazioneUdoPageBean reportValutazioneUdo(ShowFolderPageBean showFolderPageBean) {
		ReportValutazioneUdoPageBean reportValutazioneUdoPageBean = new ReportValutazioneUdoPageBean(requisitoInstDao.getReportMediaValutazioneRequisitiPerUdoInstInDomanda(showFolderPageBean.getFascicolo().getDomandaClienId()));
		
		return reportValutazioneUdoPageBean;
	}
//	<transition on="reportVerificaUdo" to="reportVerificaUdo">
//		<set name="flashScope.showFolderPageBean" value="pageBean"/>
//	</transition>
	public ReportVerificaUdoPageBean reportVerificaUdo(ShowFolderPageBean showFolderPageBean) {
		ReportVerificaUdoPageBean reportVerificaUdoPageBean = new ReportVerificaUdoPageBean(requisitoInstDao.getReportMediaValutazioneVerificatoreRequisitiPerUdoInstInDomanda(showFolderPageBean.getFascicolo().getDomandaClienId()));
		
		return reportVerificaUdoPageBean;
	}
//	<transition on="reportValutazioneComplessiva" to="reportValutazioneComplessiva">
//		<set name="flashScope.showFolderPageBean" value="pageBean"/>
//	</transition>
	public ReportValutazioneComplessivaPageBean reportValutazioneComplessiva(ShowFolderPageBean showFolderPageBean) {
		ReportValutazioneComplessivaPageBean reportValutazioneComplessivaPageBean = new ReportValutazioneComplessivaPageBean(requisitoInstDao.getReportMediaComplessivaValutazioneRequisitiPerDomanda(showFolderPageBean.getFascicolo().getDomandaClienId()));
		
		return reportValutazioneComplessivaPageBean;
	}
//	<transition on="reportVerificaComplessiva" to="reportVerificaComplessiva">
//		<set name="flashScope.showFolderPageBean" value="pageBean"/>
//	</transition>
	public ReportVerificaComplessivaPageBean reportVerificaComplessiva(ShowFolderPageBean showFolderPageBean) {
		ReportVerificaComplessivaPageBean reportVerificaComplessivaPageBean = new ReportVerificaComplessivaPageBean(requisitoInstDao.getReportMediaComplessivaValutazioneVerificatoreRequisitiPerDomanda(showFolderPageBean.getFascicolo().getDomandaClienId()));
		
		return reportVerificaComplessivaPageBean;
	}
//<!-- report requisiti generali fine -->
	
	public CheckPostiLettoDomandaPageBean checkPostiLettoDomanda(ShowFolderPageBean showFolderPageBean) {
		CheckPostiLettoDomandaPageBean checkPostiLettoDomandaPageBean = new CheckPostiLettoDomandaPageBean();
		
		for(UdoUoInstForList udoUo : showFolderPageBean.getFascicolo().getUdoUoInstForL()) { //lista delle UoUdo della domanda
			//Nota del 13/06/2017 vengono considerate solo le udo che hanno discipline in quanto non interessano i posti letti non associati alle discipline
			//Se la udo è modulo i posti letto sono agganciati alla discilina
			//Se la udo non è modulo i posti letto nei fattori produttivi sono relativi alla unica disciplina inserita
			if(udoUo.isUdo() && udoUo.getUdoInst().getDisciplineUdoInstsInfo() != null) {
				checkPostiLettoDomandaPageBean.addUdoInst(udoUo.getUdoInst());
			}
		}
		
		return checkPostiLettoDomandaPageBean;
	}

	public CompareUdoDomandaPageBean exportUdoDomandaCompare(ShowFolderPageBean showFolderPageBean) {
		CompareUdoDomandaPageBean compareUdoDomandaPageBean = new CompareUdoDomandaPageBean();
		
		List<UdoInst> udoInstsCompare = udoInstDao.getUdoInstForCompareByDomandaInst(showFolderPageBean.getFascicolo().getDomandaClienId(), showFolderPageBean.getTitolareDomanda().getClientid());
		
		Map<String, UdoInst> oldUdoInstForUdoModelClientid = null; 
		if(udoInstsCompare != null) {
			oldUdoInstForUdoModelClientid = new HashMap<String, UdoInst>();
			for(UdoInst ui : udoInstsCompare)
				oldUdoInstForUdoModelClientid.put(ui.getUdoModelClientid(), ui);
		}
		
		CompareUdoBean compareUdoBean = null;
		UdoInst oldUdoInst = null;
		if(oldUdoInstForUdoModelClientid != null) {
			for (UdoUoInstForList udoUoInstForList : showFolderPageBean.getFascicolo().getUdoUoInstForL()) {
				if(udoUoInstForList.isUdo()) {
					compareUdoBean = new CompareUdoBean(udoUoInstForList.getUdoInst());
					//se esiste una udoInst precedente da comparare la aggiungo
					oldUdoInst = oldUdoInstForUdoModelClientid.get(compareUdoBean.getUdoInst().getUdoModelClientid());
					if(oldUdoInst != null)
						compareUdoBean.setOldUdoInst(oldUdoInst);
					compareUdoDomandaPageBean.getCompareUdoBeanL().add(compareUdoBean);
				}
			}
		} else {
			for (UdoUoInstForList udoUoInstForList : showFolderPageBean.getFascicolo().getUdoUoInstForL()) {
				if(udoUoInstForList.isUdo()) {
					compareUdoBean = new CompareUdoBean(udoUoInstForList.getUdoInst());
					compareUdoDomandaPageBean.getCompareUdoBeanL().add(compareUdoBean);
				}
			}
		}
		
		return compareUdoDomandaPageBean;
	}

	public EditNoteMancanzaTipiUdoDomandaPageBean editNoteMancanzaTipiUdo() {
		EditNoteMancanzaTipiUdoDomandaPageBean editNoteMancanzaTipiUdoDomandaPageBean = new EditNoteMancanzaTipiUdoDomandaPageBean();
		
		return editNoteMancanzaTipiUdoDomandaPageBean;
	}
	
	public EditNoteVerificaDomandaPageBean editNoteVerifica(ShowFolderPageBean showFolderPageBean) {
		EditNoteVerificaDomandaPageBean editNoteVerificaDomandaPageBean = new EditNoteVerificaDomandaPageBean();
		if(showFolderPageBean.getFascicolo().getNoteVerifica() != null)
			editNoteVerificaDomandaPageBean.setNote(showFolderPageBean.getFascicolo().getNoteVerifica().getTesto());
		
		return editNoteVerificaDomandaPageBean;
	}

	public boolean validateFormNoteMancanzaTipiUdo(MessageContext messageContext, EditNoteMancanzaTipiUdoDomandaPageBean editNoteMancanzaTipiUdoDomandaPageBean) {
		MessageBuilder mB = new MessageBuilder();
		
		boolean errors = false;
		
		//testo annotazione vuoto
		if (editNoteMancanzaTipiUdoDomandaPageBean.getNote().trim().length() == 0) {
			messageContext.addMessage(mB.error().source("nuovaAnnotazione").defaultText("Inserire il testo della nota").build());
			messageContext.addMessage(mB.error().source("messages").defaultText("Inserire il testo della nota").build());
			errors = true;
		}
		
		return !errors;
	}	
	
	public void saveNoteMancanzaTipiUdo(ShowFolderPageBean showFolderPageBean, UserBean userBean, EditNoteMancanzaTipiUdoDomandaPageBean editNoteMancanzaTipiUdoDomandaPageBean, ExtraWayService xwService) throws Exception {
		String testo = editNoteMancanzaTipiUdoDomandaPageBean.getNote().trim();
		if(!testo.isEmpty()) {
			String operatore =  userBean.getUtenteModel().getLoginDbOrCas();
			String ruolo = userBean.getRuolo();
			Date date = new Date();
			String data = new SimpleDateFormat("yyyyMMdd").format(date);
			String ora = new SimpleDateFormat("HH:mm:ss").format(date);
			
			XMLDocumento document = FolderDao.addNoteMancanzaTipiUdo(xwService, Integer.parseInt(showFolderPageBean.getFascicolo().getIdUnit()), testo, operatore, ruolo, data, ora);
			showFolderPageBean.getFascicolo().setContents(document); //refresh immediato (per visualizzare la nuova annotazione)
		}
	}

	public void saveNoteVerifica(ShowFolderPageBean showFolderPageBean, UserBean userBean, EditNoteVerificaDomandaPageBean editNoteVerificaDomandaPageBean, ExtraWayService xwService) throws Exception {
		String testo = editNoteVerificaDomandaPageBean.getNote().trim();
		String operatore =  userBean.getUtenteModel().getLoginDbOrCas();
		String ruolo = userBean.getRuolo();
		Date date = new Date();
		String data = new SimpleDateFormat("yyyyMMdd").format(date);
		String ora = new SimpleDateFormat("HH:mm:ss").format(date);
		
		XMLDocumento document = FolderDao.addNoteVerifica(xwService, Integer.parseInt(showFolderPageBean.getFascicolo().getIdUnit()), testo, operatore, ruolo, data, ora, userBean.isImitatingOperatore());
		showFolderPageBean.getFascicolo().setContents(document); //refresh immediato (per visualizzare la nota verifica)
	}

	private boolean domandaContainsTipoUdo(List<UdoUoInstForList> domandaUdoUoInstL, TipoUdoTempl tipoUdoTempl) {
		for (UdoUoInstForList udoUoInst:domandaUdoUoInstL) {
			if (udoUoInst.isUdo())
				if (udoUoInst.getTipoUdoTempl().getClientid().equals(tipoUdoTempl.getClientid()))
					return true;			
		}
		return false;
	}
	
	private boolean domandaContainsTipoUdo22(List<UdoUoInstForList> domandaUdoUoInstL, TipoUdo22Templ tipoUdo22Templ) {
		for (UdoUoInstForList udoUoInst:domandaUdoUoInstL) {
			if (udoUoInst.isUdo())
				if (udoUoInst.getTipoUdoTempl().getTipoUdo22Templ().getClientid().equals(tipoUdo22Templ.getClientid()))
					return true;
		}
		return false;
	}
	
	public CsvReportIstruttoriaPageBean exportCsvReportIstruttoriaUdo(ShowFolderPageBean showFolderPageBean) {
		CsvReportIstruttoriaPageBean csvReportIstruttoriaPageBean = new CsvReportIstruttoriaPageBean();
		csvReportIstruttoriaPageBean.setUdoUoInstForLists(showFolderPageBean.getFascicolo().getUdoUoInstForL());
		csvReportIstruttoriaPageBean.setDomandaInstUoInstUdoInstClientIdsConRequisitiNonConformi(domandaInstDao.getEntityInstClientIdConRequisitiNonConformi(showFolderPageBean.getFascicolo().getDomandaClienId()));
		
		return csvReportIstruttoriaPageBean;
	}

	@SuppressWarnings("unchecked")
	public RelazioneConclusivaPageBean exportaRelazioneConclusiva(ExternalContext externalContext, MessageContext messageContext, ShowFolderPageBean showFolderPageBean, UserBean userBean) {
		RelazioneConclusivaPageBean relazioneConclusivaPageBean = new RelazioneConclusivaPageBean();

		try {
			DataInstance dataInstance;
			relazioneConclusivaPageBean.setDataInvioDomanda(showFolderPageBean.getFascicolo().getDataInvioDomanda());
			relazioneConclusivaPageBean.setTitolareDomanda(showFolderPageBean.getTitolareDomanda());
			relazioneConclusivaPageBean.setTipoProcTempl(showFolderPageBean.getTipoProcTemp());
			relazioneConclusivaPageBean.setNumeroProcedimento(showFolderPageBean.getFascicolo().getNumeroProcedimento());
	
			//Atti agganciati alle UdoModel da cui sono state create le UdoInst per il tipo procedimento della domanda corrente
			//Riporto l'elenco degli Atti univoci agganciati alle UdoModel delle UdoInst
			List<String> udoModelClientids = new ArrayList<String>();
			for(UdoUoInstForList uuifl : showFolderPageBean.getFascicolo().getUdoUoInstForL()) {
				if(uuifl.isUdo())
					udoModelClientids.add(uuifl.getUdoInst().getUdoModelClientid());
			}
			relazioneConclusivaPageBean.setAttiTipoProcedimentoUdoModelUdoInsts(attoModelDao.getAttiModelForUdoModelClientids(showFolderPageBean.getTipoProcTemp(), showFolderPageBean.getTitolareDomanda(), udoModelClientids));
			
			//Autorizzazione: 
			//	Note per Integrazione Istruttoria - autorizValComplCorrNoteIstrut 
			//	Note per Integrazione Verifica - autorizValComplCorrNoteVerif
			//Accreditamento
			//	Note per Integrazione Istruttoria - valComplCorrNoteIstrut
			//	Note per Integrazione Verifica - valComplCorrNoteVerif
			DataInstance noteIntegrazioneIstruttoriaDT = null;
			DataInstance noteIntegrazioneVerificaDT = null;
	
			if("Autorizzazione".equals(showFolderPageBean.getTipoProcTemp().getNomeWf())) {
				noteIntegrazioneIstruttoriaDT = workflowService.getWorkflowBonita().getVariable("autorizValComplCorrNoteIstrut", showFolderPageBean.getFascicolo().getWorkflowXWProcessInstance().getProcessInstanceId(), userBean.getWorkflowBean().getApiSession());
				noteIntegrazioneVerificaDT = workflowService.getWorkflowBonita().getVariable("autorizValComplCorrNoteVerif", showFolderPageBean.getFascicolo().getWorkflowXWProcessInstance().getProcessInstanceId(), userBean.getWorkflowBean().getApiSession());
			} else if("Accreditamento".equals(showFolderPageBean.getTipoProcTemp().getNomeWf())) {
				noteIntegrazioneIstruttoriaDT = workflowService.getWorkflowBonita().getVariable("valComplCorrNoteIstrut", showFolderPageBean.getFascicolo().getWorkflowXWProcessInstance().getProcessInstanceId(), userBean.getWorkflowBean().getApiSession());
				noteIntegrazioneVerificaDT = workflowService.getWorkflowBonita().getVariable("valComplCorrNoteVerif", showFolderPageBean.getFascicolo().getWorkflowXWProcessInstance().getProcessInstanceId(), userBean.getWorkflowBean().getApiSession());
			}

			if(noteIntegrazioneIstruttoriaDT != null && noteIntegrazioneIstruttoriaDT.getValue() != null) {
				relazioneConclusivaPageBean.setNoteIntegrazioneIstruttoria((String)noteIntegrazioneIstruttoriaDT.getValue());
			}
			if(noteIntegrazioneVerificaDT != null && noteIntegrazioneVerificaDT.getValue() != null) {
				relazioneConclusivaPageBean.setNoteIntegrazioneVerifica((String)noteIntegrazioneVerificaDT.getValue());
			}
			
			//valutCongrProgrNote
			if("Autorizzazione".equals(showFolderPageBean.getTipoProcTemp().getNomeWf()) || "Accreditamento".equals(showFolderPageBean.getTipoProcTemp().getNomeWf())) {
				DataInstance noteValutazioneCongruenzaProgrammazioneDT = workflowService.getWorkflowBonita().getVariable("valutCongrProgrNote", showFolderPageBean.getFascicolo().getWorkflowXWProcessInstance().getProcessInstanceId(), userBean.getWorkflowBean().getApiSession());
				if(noteValutazioneCongruenzaProgrammazioneDT.getValue() != null) {
					relazioneConclusivaPageBean.setNoteValutazioneCongruenzaProgrammazione((String)noteValutazioneCongruenzaProgrammazioneDT.getValue());
				}
				
				dataInstance = workflowService.getWorkflowBonita().getVariable("convalidaRapportoVerificaShow", showFolderPageBean.getFascicolo().getWorkflowXWProcessInstance().getProcessInstanceId(), userBean.getWorkflowBean().getApiSession());
				if(dataInstance.getValue() != null) {
					relazioneConclusivaPageBean.setConvalidaRapportoVerificaShow((Boolean)dataInstance.getValue());
				}
				if(relazioneConclusivaPageBean.isConvalidaRapportoVerificaShow()) {
					dataInstance = workflowService.getWorkflowBonita().getVariable("convalidaRapportoVerificaNote", showFolderPageBean.getFascicolo().getWorkflowXWProcessInstance().getProcessInstanceId(), userBean.getWorkflowBean().getApiSession());
					if(dataInstance.getValue() != null) {
						relazioneConclusivaPageBean.setConvalidaRapportoVerificaNote((String)dataInstance.getValue());
					}
				}
				
				//Controllo che conferimentoIncaricoShow=true
				DataInstance conferimentoIncaricoShowDI = workflowService.getWorkflowBonita().getVariable("conferimentoIncaricoShow", showFolderPageBean.getFascicolo().getWorkflowXWProcessInstance().getProcessInstanceId(), userBean.getWorkflowBean().getApiSession());
				boolean conferimentoIncaricoShow = false;
				if(conferimentoIncaricoShowDI.getValue() != null) {
					conferimentoIncaricoShow = (Boolean)conferimentoIncaricoShowDI.getValue();
				}
				if(conferimentoIncaricoShow) {
					DataInstance verificatoreTeamLeaderBonitaUserName = null;
					if(showFolderPageBean.getVerificatoreTeamLeader() != null) {
						relazioneConclusivaPageBean.setVerificatoreTeamLeader(showFolderPageBean.getVerificatoreTeamLeader());
					} else {
						//Forse sono in uno stato in cui non viene caricato nel showFolderPageBean
						verificatoreTeamLeaderBonitaUserName = workflowService.getWorkflowBonita().getVariable("verificatoreTeamLeaderBonitaUserName", showFolderPageBean.getFascicolo().getWorkflowXWProcessInstance().getProcessInstanceId(), userBean.getWorkflowBean().getApiSession());
						if(verificatoreTeamLeaderBonitaUserName.getValue() != null) {
							AuacUserInfo auacUserInfo = AuacUtils.getAuacUserInfo((String)verificatoreTeamLeaderBonitaUserName.getValue());
							relazioneConclusivaPageBean.setVerificatoreTeamLeader(utenteDao.getByUserName(auacUserInfo.getUsername()));
						}
					}
					
					if(showFolderPageBean.getTeamVerificatori() != null) {
						relazioneConclusivaPageBean.setTeamVerificatori(showFolderPageBean.getTeamVerificatori());
					} else {
						//Forse sono in uno stato in cui non viene caricato nel showFolderPageBean
						dataInstance = workflowService.getWorkflowBonita().getVariable("teamVerificatori", showFolderPageBean.getFascicolo().getWorkflowXWProcessInstance().getProcessInstanceId(), userBean.getWorkflowBean().getApiSession());
						if(dataInstance.getValue() != null) {
							List<String> teamVerificatori = (List<String>)dataInstance.getValue();
							if(teamVerificatori.size() > 0) {
								Set<String> usernames = new HashSet<String>();
								for(String bonitaUserName : teamVerificatori) {
									usernames.add(AuacUtils.getAuacUserInfo(bonitaUserName).getUsername());
								}
								relazioneConclusivaPageBean.setTeamVerificatori(utenteDao.getListByUserNames(usernames));
							}
						}
					}
					
					//pianificazioneVerificaDateVerifica
					dataInstance = workflowService.getWorkflowBonita().getVariable("pianificazioneVerificaDateVerifica", showFolderPageBean.getFascicolo().getWorkflowXWProcessInstance().getProcessInstanceId(), userBean.getWorkflowBean().getApiSession());
					if(dataInstance.getValue() != null) {
						relazioneConclusivaPageBean.setDateSvolgimentoVerifica((String)dataInstance.getValue());
					}
		
					//La data in cui e' stato eseguito il task "Redazione Rapporto di Verifica"
					relazioneConclusivaPageBean.setDataRedazioneRapportoVerifica(workflowService.getDataRedazioneRapportoVerifica(showFolderPageBean.getWorkflowProcessInstance().getProcessInstanceId(), showFolderPageBean.getTipoProcTemp()));
				}
				
			}
			
			//La data dell'ultima richiesta integrazione individuato dall'ultimo task "Cambia Stato Richiesta integrazioni" o "Cambia Stato Rich di integraz val risp program"
			relazioneConclusivaPageBean.setUltimaDataRichiestaIntegrazione(workflowService.getUltimaDataRichiestaIntegrazione(showFolderPageBean.getWorkflowProcessInstance().getProcessInstanceId(), showFolderPageBean.getTipoProcTemp()));
			//La data dell'ultimo invio di integrazioni individuato dall'ultimo task "Richiesta Integrazioni" o "Rich di Integr Congr Program"
			relazioneConclusivaPageBean.setUltimaDataInvioIntegrazione(workflowService.getUltimaDataInviaIntegrazione(showFolderPageBean.getWorkflowProcessInstance().getProcessInstanceId(), showFolderPageBean.getTipoProcTemp()));


		} catch (Exception e) {
			Logger.getRootLogger().error("Errore nel caricamento del bean RelazioneConclusivaPageBean", e);
			MessageBuilder mB = new MessageBuilder();
			messageContext.addMessage(mB.error().source("messages").defaultText("Si è verificato un errore durante il caricamento dei dati del bean RelazioneConclusivaPageBean").build());
		}
		return relazioneConclusivaPageBean;
	}	


	public void docTitolareOrderOperatoreDoc(ShowFolderPageBean showFolderPageBean, boolean skipOnRender) {
		if (skipOnRender)
			return;
		switch (showFolderPageBean.getFascicolo().getDocTitolareOrderTypeEnum()) {
			case OperatoreCrescente:
				showFolderPageBean.getFascicolo().setDocTitolareOrderTypeEnum(DocOrderTypeEnum.OperatoreDecrescente);
				break;
			case OperatoreDecrescente:
				showFolderPageBean.getFascicolo().setDocTitolareOrderTypeEnum(DocOrderTypeEnum.OperatoreCrescente);
				break;
			default:
				showFolderPageBean.getFascicolo().setDocTitolareOrderTypeEnum(DocOrderTypeEnum.OperatoreDecrescente);
				break;
		}
	}

	public void docTitolareOrderDataDoc(ShowFolderPageBean showFolderPageBean, boolean skipOnRender) {
		if (skipOnRender)
			return;
		switch (showFolderPageBean.getFascicolo().getDocTitolareOrderTypeEnum()) {
			case DataCrescente:
				showFolderPageBean.getFascicolo().setDocTitolareOrderTypeEnum(DocOrderTypeEnum.DataDecrescente);
				break;
			case DataDecrescente:
				showFolderPageBean.getFascicolo().setDocTitolareOrderTypeEnum(DocOrderTypeEnum.DataCrescente);
				break;
			default:
				showFolderPageBean.getFascicolo().setDocTitolareOrderTypeEnum(DocOrderTypeEnum.DataDecrescente);
				break;
		}
	}

	public void docTitolareOrderTipoDoc(ShowFolderPageBean showFolderPageBean, boolean skipOnRender) {
		if (skipOnRender)
			return;
		switch (showFolderPageBean.getFascicolo().getDocTitolareOrderTypeEnum()) {
			case TipoCrescente:
				showFolderPageBean.getFascicolo().setDocTitolareOrderTypeEnum(DocOrderTypeEnum.TipoDecrescente);
				break;
			case TipoDecrescente:
				showFolderPageBean.getFascicolo().setDocTitolareOrderTypeEnum(DocOrderTypeEnum.TipoCrescente);
				break;
			default:
				showFolderPageBean.getFascicolo().setDocTitolareOrderTypeEnum(DocOrderTypeEnum.TipoDecrescente);
				break;
		}
	}

	public void docWfTitolareOrderOperatoreDoc(ShowFolderPageBean showFolderPageBean, boolean skipOnRender) {
		if (skipOnRender)
			return;
		switch (showFolderPageBean.getFascicolo().getDocWfTitolareOrderTypeEnum()) {
			case OperatoreCrescente:
				showFolderPageBean.getFascicolo().setDocWfTitolareOrderTypeEnum(DocOrderTypeEnum.OperatoreDecrescente);
				break;
			case OperatoreDecrescente:
				showFolderPageBean.getFascicolo().setDocWfTitolareOrderTypeEnum(DocOrderTypeEnum.OperatoreCrescente);
				break;
			default:
				showFolderPageBean.getFascicolo().setDocWfTitolareOrderTypeEnum(DocOrderTypeEnum.OperatoreDecrescente);
				break;
		}
	}

	public void docWfTitolareOrderDataDoc(ShowFolderPageBean showFolderPageBean, boolean skipOnRender) {
		if (skipOnRender)
			return;
		switch (showFolderPageBean.getFascicolo().getDocWfTitolareOrderTypeEnum()) {
			case DataCrescente:
				showFolderPageBean.getFascicolo().setDocWfTitolareOrderTypeEnum(DocOrderTypeEnum.DataDecrescente);
				break;
			case DataDecrescente:
				showFolderPageBean.getFascicolo().setDocWfTitolareOrderTypeEnum(DocOrderTypeEnum.DataCrescente);
				break;
			default:
				showFolderPageBean.getFascicolo().setDocWfTitolareOrderTypeEnum(DocOrderTypeEnum.DataDecrescente);
				break;
		}
	}

	public void docWfRegioneOrderOperatoreDoc(ShowFolderPageBean showFolderPageBean, boolean skipOnRender) {
		if (skipOnRender)
			return;
		switch (showFolderPageBean.getFascicolo().getDocWfRegioneOrderTypeEnum()) {
			case OperatoreCrescente:
				showFolderPageBean.getFascicolo().setDocWfRegioneOrderTypeEnum(DocOrderTypeEnum.OperatoreDecrescente);
				break;
			case OperatoreDecrescente:
				showFolderPageBean.getFascicolo().setDocWfRegioneOrderTypeEnum(DocOrderTypeEnum.OperatoreCrescente);
				break;
			default:
				showFolderPageBean.getFascicolo().setDocWfRegioneOrderTypeEnum(DocOrderTypeEnum.OperatoreDecrescente);
				break;
		}
	}

	public void docWfRegioneOrderDataDoc(ShowFolderPageBean showFolderPageBean, boolean skipOnRender) {
		if (skipOnRender)
			return;
		switch (showFolderPageBean.getFascicolo().getDocWfRegioneOrderTypeEnum()) {
			case DataCrescente:
				showFolderPageBean.getFascicolo().setDocWfRegioneOrderTypeEnum(DocOrderTypeEnum.DataDecrescente);
				break;
			case DataDecrescente:
				showFolderPageBean.getFascicolo().setDocWfRegioneOrderTypeEnum(DocOrderTypeEnum.DataCrescente);
				break;
			default:
				showFolderPageBean.getFascicolo().setDocWfRegioneOrderTypeEnum(DocOrderTypeEnum.DataDecrescente);
				break;
		}
	}

	public ShowFolderAttiPageBean createShowFolderAttiPageBean(ExternalContext externalContext, MessageContext messageContext, UserBean userBean, ExtraWayService xwService, ExtraWayService _xwService, TitolariPageBean titolariPageBean, String clientid, int index) throws Exception{
		ShowFolderAttiPageBean showFolderAttiPageBean = new ShowFolderAttiPageBean(titolariPageBean);
		titolariPageBean.setCurrentTitolareByIndexOnCurrentPage(index);
		
		loadDataShowFolderAttiPageBean(externalContext, messageContext, userBean, xwService, showFolderAttiPageBean);
		return showFolderAttiPageBean;
	}
	
	public DeliberaBean createDeliberaBean(ShowFolderPageBean showFolderPageBean){
		DeliberaBean deliberaBean = showFolderPageBean.getDelibera();
		return deliberaBean;
	}

	public FileBinaryAttachmentsApplBean createFileBinaryAttachmentsApplBean(String intestazioneForm, String descrizioneFileInModifica, String binaryAttachmentsApplClientId) {
		return createFileBinaryAttachmentsApplBean(intestazioneForm, descrizioneFileInModifica, binaryAttachmentsApplClientId, null);
	}

	public FileBinaryAttachmentsApplBean createFileBinaryAttachmentsApplBean(String intestazioneForm, String descrizioneFileInModifica, String binaryAttachmentsApplClientId, String fileName) {
		String fileNameR = fileName;
		if(fileNameR == null || fileNameR.isEmpty()) {
			BinaryAttachmentsAppl binaryAttachmentsAppl = binaryAttachmentsApplDao.findOne(binaryAttachmentsApplClientId);
			fileNameR = binaryAttachmentsAppl.getNome();
		}
		FileBinaryAttachmentsApplBean fileBinaryAttachmentsApplBean = new FileBinaryAttachmentsApplBean(intestazioneForm, descrizioneFileInModifica, binaryAttachmentsApplClientId, fileNameR);
		return fileBinaryAttachmentsApplBean;
	}
	
	public void fileAttoSaved(ShowFolderPageBean showFolderPageBean, FileBinaryAttachmentsApplBean fileBinaryAttachmentsApplBean, String fileName) {
		for(AttoInst atto : showFolderPageBean.getAttoInstDomandaL()) {
			if(atto.getBinaryAttachmentsAppl().getClientid().equals(fileBinaryAttachmentsApplBean.getBinaryAttachmentsApplClientId())) {
				atto.getBinaryAttachmentsAppl().setNome(fileName);
				break;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public AttoInstBean createAttoInstBean(ShowFolderPageBean showFolderPageBean, String attoInstClientId){
		AttoInstBean attoInstBean = new AttoInstBean();
		attoInstBean.setShowFolderPageBean(showFolderPageBean);
		attoInstBean.setTipiAtto((List<TipoAtto>)tipoAttoDao.findAll());
		for(AttoInst atto : showFolderPageBean.getAttoInstDomandaL()) {
			if(atto.getClientid().equals(attoInstClientId)) {
				attoInstBean.setAttoInst(atto);
				break;
			}
		}
		return attoInstBean;
	}
	
	public void saveAttoInst(ShowFolderPageBean folderPageBean, AttoInstBean attoInstBean) {
		try {
			if(attoInstBean != null && attoInstBean.getAttoInst() != null) {
				//Allineo il tipo
				for(TipoAtto tipo : attoInstBean.getTipiAtto()) {
					if(attoInstBean.getAttoInst().getTipoAtto().getClientid().equals(tipo.getClientid())) {
						attoInstBean.getAttoInst().setTipoAtto(tipo);
						break;
					}
				}
				
				//save AttoInst
				attoInstDao.update(attoInstBean.getAttoInst());
				//salvare descr dell'allegato
				BinaryAttachmentsAppl binaryAttachmentsAppl = binaryAttachmentsApplDao.findOne(attoInstBean.getAttoInst().getBinaryAttachmentsAppl().getClientid());
				binaryAttachmentsAppl.setDescr(attoInstBean.getAttoInst().getBinaryAttachmentsAppl().getDescr());
				binaryAttachmentsApplDao.save(binaryAttachmentsAppl);
			}
		} catch (Exception e) {
			Logger.getRootLogger().error(e);
			e.printStackTrace();
		}
	}
	

	@SuppressWarnings("unchecked")
	private void loadDataShowFolderAttiPageBean(ExternalContext externalContext, MessageContext messageContext, UserBean userBean, ExtraWayService xwService, ShowFolderAttiPageBean showFolderAttiPageBean) throws Exception{
		//carico il titolare selezionato
		TitolareModel titolareLoad = titolareModelDao.findOne(showFolderAttiPageBean.getTitolariPageBean().getCurrentTitolare().getClientid());

		//QUESTO DOVREBBE ESSERE QUELLO CORRETTO
		XMLDocumento titolareFolder = FolderDao.findTitolareFolder(xwService, titolareLoad);
		//creo il fascicolo
		FolderBean fascicolo = new FolderBean(userBean);
		showFolderAttiPageBean.setFascicolo(fascicolo);
		if (titolareFolder == null) {
			Logger.getRootLogger().warn("Non è stato possibile caricare il fascicolo atti perchè non è stato possibile caricare il fascicolo del titolare");
			//MessageBuilder mB = new MessageBuilder();
			//messageContext.addMessage(mB.error().source("messages").defaultText("Si è verificato un errore durante il caricamento dei dati del fascicolo atti").build());
		} else {
			String numFasc = titolareFolder.getAttributeValue("//fascicolo/@numero");
			XMLDocumento titolareSubFolder = FolderDao.findAttiSubFolder(xwService, numFasc);
	
			if(titolareSubFolder == null) {
				Logger.getRootLogger().warn("Non è stato possibile caricare il fascicolo atti");
				//MessageBuilder mB = new MessageBuilder();
				//messageContext.addMessage(mB.error().source("messages").defaultText("Si è verificato un errore durante il caricamento dei dati del fascicolo atti").build());
			} else {
				//carico i dati nel fascicolo
				fascicolo.setContents(titolareSubFolder);
			}
		}
		
		showFolderAttiPageBean.setTipiProcL((List<TipoProcTempl>) tipoProcTemplDao.findAll());
		
		try {
			showFolderAttiPageBean.setAttiML(null);
			//Non ci sono documenti degli Atti su extraway
			titolareLoad.getAttoModels().size();
			if (titolareLoad.getAttoModels().size() > 0){
				List<AttoModel> attiL = titolareLoad.getAttoModels();
				List<Map<String,String>> attiML = new ArrayList<Map<String,String>>();
				for (AttoModel atto:attiL){
					Map<String,String> map = new HashMap<String,String>();
					map.put("tipo_atto", atto.getTipoAtto().getDescr());
					map.put("tipo_proc", atto.getTipoProcTempl().getDescr());
					map.put("anno", atto.getAnno());
					map.put("numero", atto.getNumero());
					if(atto.getBinaryAttachmentsAppl()!=null){
						map.put("file_nome", atto.getBinaryAttachmentsAppl().getNomeWithExtension());
						map.put("file_nome_enc",atto.getBinaryAttachmentsAppl().getNomeBase64WithExtension());
						map.put("file_id_enc",Base64.encodeBase64String(atto.getBinaryAttachmentsAppl().getClientid().getBytes()));
						HttpServletRequest httpServletRequest = (HttpServletRequest)externalContext.getNativeRequest();
						String sessionId = httpServletRequest.getSession().getId();
						map.put("session_id", Base64.encodeBase64String(sessionId.getBytes()));
					}
					else{
						map.put("file_nome", "");
					}
					Date dataInizio = atto.getInizioValidita();
					Date dataFine = atto.getFineValidita();
					SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
					String dataInizioS = formato.format(dataInizio).toString();
					String dataFineS = formato.format(dataFine).toString();
					map.put("inizio_validita", dataInizioS);
					map.put("fine_validita", dataFineS);
					attiML.add(map);
				}
				showFolderAttiPageBean.setAttiML(attiML);
			}
		}
		catch (Exception e) {
			Logger.getRootLogger().error("Si è verificato un errore durante il caricamento dei dati del fascicolo atti", e);
			MessageBuilder mB = new MessageBuilder();
			messageContext.addMessage(mB.error().source("messages").defaultText("Si è verificato un errore durante il caricamento dei dati del fascicolo atti").build());
		}		
	}

	public void firstProcTitolareAtti(ExternalContext externalContext, MessageContext messageContext, UserBean userBean, ExtraWayService xwService, ShowFolderAttiPageBean showFolderAttiPageBean) throws Exception {
		showFolderAttiPageBean.getTitolariPageBean().gotoFirstTitolare();
		loadDataShowFolderAttiPageBean(externalContext, messageContext, userBean, xwService, showFolderAttiPageBean);
	}	
	
	public void prevProcTitolareAtti(ExternalContext externalContext, MessageContext messageContext, UserBean userBean, ExtraWayService xwService, ShowFolderAttiPageBean showFolderAttiPageBean) throws Exception {
		showFolderAttiPageBean.getTitolariPageBean().gotoPrevTitolare();
		loadDataShowFolderAttiPageBean(externalContext, messageContext, userBean, xwService, showFolderAttiPageBean);
	}	
	
	public void nextProcTitolareAtti(ExternalContext externalContext, MessageContext messageContext, UserBean userBean, ExtraWayService xwService, ShowFolderAttiPageBean showFolderAttiPageBean) throws Exception {
		showFolderAttiPageBean.getTitolariPageBean().gotoNextTitolare();
		loadDataShowFolderAttiPageBean(externalContext, messageContext, userBean, xwService, showFolderAttiPageBean);
	}
	
	public void lastProcTitolareAtti(ExternalContext externalContext, MessageContext messageContext, UserBean userBean, ExtraWayService xwService, ShowFolderAttiPageBean showFolderAttiPageBean) throws Exception {
		showFolderAttiPageBean.getTitolariPageBean().gotoLastTitolare();
		loadDataShowFolderAttiPageBean(externalContext, messageContext, userBean, xwService, showFolderAttiPageBean);
	}	

	//           showWorkflow(org.springframework.webflow.mvc.servlet.MvcExternalContext,org.springframework.binding.message.DefaultMessageContext,it.tredi.auac.bean.UserBean,null)
	/*public WorkflowProcessInstance showWorkflow(ExternalContext externalContext, MessageContext messageContext, UserBean userBean, long processInstanceId) throws Exception{
		WorkflowProcessInstance wpi = new WorkflowProcessInstance(new WorkflowXWProcessInstance(processInstanceId, 0));
		wpi.setImageWorkflowUrl(workflowService.getBonitaViewServerUrl() + "/index.jsp?processInstanceId=" + processInstanceId);
		return wpi;
	}*/
	public WorkflowProcessInstanceImageOnWindow showWorkflowState(ExternalContext externalContext, MessageContext messageContext, UserBean userBean, long processInstanceId) throws Exception{
		//WorkflowProcessInstanceImageOnWindow wpi = new WorkflowProcessInstanceImageOnWindow(processInstanceId);
		WorkflowProcessInstanceImageOnWindow wpi = new WorkflowProcessInstanceImageOnWindow();
		wpi.setState(true);
		wpi.setImageWorkflowUrl(workflowService.getBonitaViewServerUrl() + "/index.jsp?processInstanceId=" + processInstanceId);
		return wpi;
	}

	public WorkflowProcessInstanceImageOnWindow showWorkflow(ExternalContext externalContext, MessageContext messageContext, UserBean userBean, String wfname, String wfversion) throws Exception{
		WorkflowProcessInstanceImageOnWindow wpi = new WorkflowProcessInstanceImageOnWindow();
		wpi.setState(false);
		wpi.setImageWorkflowUrl(getWorkflowImageName(wfname, wfversion));
		return wpi;
	}
	
	@SuppressWarnings("unchecked")
	public void saveDelibera(ShowFolderPageBean folderPageBean, DeliberaBean delibera) {
		//TODO stub method
		try {
			if(delibera != null) {
				//save Delibera in Bonita Service
				List<TipoAtto> attoTypes = (List<TipoAtto>) tipoAttoDao.findAll();
				
				for(TipoAtto tipoAtto : attoTypes) {
					if(tipoAtto.getDescr().equals(delibera.getTipoAtto().getDescr())) {
						delibera.setTipoAtto(tipoAtto);
						break;
					}
				}
				
				workflowService.setDelibera(folderPageBean.getWorkflowProcessInstance().getProcessInstanceId(), delibera);
			}
		} catch (Exception e) {
			Logger.getRootLogger().error(e);
			e.printStackTrace();
		}
	}

	private String getWorkflowImageName(String wfname, String wfversion) {
		int pos = wfversion.indexOf("_");
		if(pos > 0) {
			wfversion = wfversion.substring(0, pos);
		}
		return wfname + "_" + wfversion + ".jpg";
	}
	
	//         showWorkflow(org.springframework.webflow.mvc.servlet.MvcExternalContext,org.springframework.binding.message.DefaultMessageContext,it.tredi.auac.bean.UserBean,java.lang.String)
	//org.springframework.webflow.context.ExternalContext
	/*public void showWorkflow(ExternalContext externalContext, MessageContext messageContext, UserBean userBean, String processInstanceId) throws Exception{
		showWorkflow(externalContext, messageContext, userBean, Long.valueOf(processInstanceId));
	}*/
	
	public void loadRecordsForSpecchiettoTable(ShowFolderPageBean showFolderPageBean, ExtraWayService xwService) {
		
		int tipoSpecchietto = showFolderPageBean.getTipoSpecchiettoSelected();
		
		//specchietto Programmato/attuato/richiesto/flussi ministeriali
		if(tipoSpecchietto == 1) {
			List<DisciplinaUdoInstInfo> data = new ArrayList<DisciplinaUdoInstInfo>();
			//Programmati
			data.addAll(udoInstDao.findDisciplinaUdoInstInfoByTitolareForProgrammati(showFolderPageBean.getTitolareDomanda()));
			//Attuati
			data.addAll(udoInstDao.findDisciplinaUdoInstInfoByTitolareForAttuati(showFolderPageBean.getTitolareDomanda()));
			//Richiesti
			data.addAll(udoModelDao.findDisciplinaUdoInstInfoByTitolareForRichiesti(showFolderPageBean.getTitolareDomanda()));
			
			showFolderPageBean.setSpecchietto1Map(createSpecchietto1Map(data));

			showFolderPageBean.setTotMap(createSpecchietto1TotMap(data));
			
		}else if(tipoSpecchietto == 2) { 	//specchietto per discipline del titolare
			List<DisciplinaUdoInstInfo> data = new ArrayList<DisciplinaUdoInstInfo>();
			List<String> allSedi = new ArrayList<String>();
			// posti letto attuati
			data.addAll(udoModelDao.findDisciplinaUdoInstInfoAttuatiForSedeOperModel(showFolderPageBean.getTitolareDomanda()));
			//posti letto programmati
			data.addAll(udoModelDao.findDisciplinaUdoInstInfoProgrammatiForSedeOperModel(showFolderPageBean.getTitolareDomanda()));
			
			for (DisciplinaUdoInstInfo disc : data) {
				allSedi.add(disc.getSede());
			}

			showFolderPageBean.setSedi(deleteDuplicates(allSedi));
			
			showFolderPageBean.setSpecchietto2Map(createSpecchietto2Map(data, showFolderPageBean.getSedi()));
			
		}else if(tipoSpecchietto == 3) {	//specchietto branche del titolare 
			List<BrancaUdoInstInfo> data = new ArrayList<BrancaUdoInstInfo>();
			List<String> allSedi = new ArrayList<String>();
			//posti letto programmati
			data.addAll(udoModelDao.findBrancaUdoInstInfoProgrammatiForSedeOperModel(showFolderPageBean.getTitolareDomanda()));

			// posti letto attuati
			data.addAll(udoModelDao.findBrancaUdoInstInfoAttuatiForSedeOperModel(showFolderPageBean.getTitolareDomanda()));
			
			for (BrancaUdoInstInfo bran : data) {
				allSedi.add(bran.getSede());
			}

			showFolderPageBean.setSedi(deleteDuplicates(allSedi));
			
			showFolderPageBean.setSpecchietto3Map(createSpecchietto3Map(data, showFolderPageBean.getSedi()));
		}

	}
	
	/**
	 * Fa il raggruppamento e l'ordinamento delle DisciplinaUdoInstInfo per Area e 
	 * Codice disciplina (discipline doppie).
	 * @param data
	 */
	private Map<String,Map<String,List<DisciplinaUdoInstInfo>>> createSpecchietto1Map(List<DisciplinaUdoInstInfo> data) {
		Map<String,DisciplinaUdoInstInfo> aux_map = new LinkedHashMap<String, DisciplinaUdoInstInfo>();
		Map<String,Map<String,List<DisciplinaUdoInstInfo>>> groupedByAreaMap = new TreeMap<String,Map<String,List<DisciplinaUdoInstInfo>>>();
		String key = "";
		/*
		 * Itero sulle discipline arrivate in input e vado a sommare 
		 * i posti letto tra quelle con la stessa chiave (key)
		 */
		for (DisciplinaUdoInstInfo disc : data) {
			key = disc.getOrdineArea()+"_"+disc.getArea()
			+"_"+disc.getOrdineDisc()+"_"+disc.getCodice()
			+"_"+disc.getTipoPostiLetto();

			DisciplinaUdoInstInfo value = aux_map.get(key);
			
			if(value == null) {
				DisciplinaUdoInstInfo copy = new DisciplinaUdoInstInfo(disc);
				aux_map.put(key, copy);
			}else {
				BigDecimal oldplAu = value.getPostiLetto() == null ? new BigDecimal(0) : value.getPostiLetto();
				BigDecimal oldplAc = value.getPostiLettoAcc() == null ? new BigDecimal(0) : value.getPostiLettoAcc();
				BigDecimal oldplEx = value.getPostiLettoExtra() == null ? new BigDecimal(0) : value.getPostiLettoExtra();
				BigDecimal newplAu = disc.getPostiLetto() == null ? new BigDecimal(0) : disc.getPostiLetto();
				BigDecimal newplAc = disc.getPostiLettoAcc() == null ? new BigDecimal(0) : disc.getPostiLettoAcc();
				BigDecimal newplEx = disc.getPostiLettoExtra() == null ? new BigDecimal(0) : disc.getPostiLettoExtra();
				oldplAu = oldplAu.add(newplAu);
				oldplAc = oldplAc.add(newplAc);
				oldplEx = oldplEx.add(newplEx);
				value.setPostiLetto(oldplAu);
				value.setPostiLettoAcc(oldplAc);
				value.setPostiLettoExtra(oldplEx);
			}
		}
		/*
		 * Utilizzo la mappa ausiliaria aux_map per andare a costruire la mappa dello specchietto1 
		 * con struttura : {ordArea_area}->{ordDisc_cod} -> [programmati,attuati,richiesti,flussi];
		 */
		for (Map.Entry<String, DisciplinaUdoInstInfo> entry1 : aux_map.entrySet()) {
			DisciplinaUdoInstInfo disciplinaUdoInstInfo = entry1.getValue();
			String ordineArea = String.valueOf(disciplinaUdoInstInfo.getOrdineArea());
			String area = disciplinaUdoInstInfo.getArea();
			String ordArea_area = ordineArea+"_"+area;
			Map<String,List<DisciplinaUdoInstInfo>> areeMap = groupedByAreaMap.get(ordArea_area);
			if(areeMap == null) {
				areeMap = new TreeMap<String,List<DisciplinaUdoInstInfo>>();
				groupedByAreaMap.put(ordArea_area, areeMap);
			}
			String ordineDisc = String.valueOf(disciplinaUdoInstInfo.getOrdineDisc());
			String codice = disciplinaUdoInstInfo.getCodice();
			String ordDisc_cod = ordineDisc+"_"+codice;
			TipoPostiLettoEnum tipoPl = disciplinaUdoInstInfo.getTipoPostiLetto();
			List<DisciplinaUdoInstInfo> discSubList = areeMap.get(ordDisc_cod);
			if(discSubList == null) {
				List<TipoPostiLettoEnum> tipiList = new ArrayList<TipoPostiLettoEnum>(Arrays.asList(TipoPostiLettoEnum.PROGRAMMATI,
						TipoPostiLettoEnum.ATTUATI,TipoPostiLettoEnum.RICHIESTI,TipoPostiLettoEnum.FLUSSI));
				discSubList = new ArrayList<DisciplinaUdoInstInfo>(Arrays.asList(new DisciplinaUdoInstInfo(),new DisciplinaUdoInstInfo(),
						new DisciplinaUdoInstInfo(),new DisciplinaUdoInstInfo()));
				for (int i = 0; i < discSubList.size(); i++) {
					DisciplinaUdoInstInfo disc = new DisciplinaUdoInstInfo(discSubList.get(i));
					disc.setCodice(disciplinaUdoInstInfo.getCodice());
					disc.setDescr(disciplinaUdoInstInfo.getDescr());
					disc.setTipoPostiLetto(tipiList.get(i));
					discSubList.set(i, disc);
				}
				areeMap.put(ordDisc_cod, discSubList);
			}
			for (int i = 0; i < discSubList.size(); i++) {
				DisciplinaUdoInstInfo disc = discSubList.get(i);
				if(disc.getTipoPostiLetto().equals(tipoPl))
					discSubList.set(i, disciplinaUdoInstInfo);
			}
		}
		return groupedByAreaMap;
	}

	private Map<String, List<DisciplinaUdoInstInfo>> createSpecchietto2Map(List<DisciplinaUdoInstInfo> data, List<String> sedi) {
		Map<String, List<DisciplinaUdoInstInfo>> toRet = new TreeMap<String, List<DisciplinaUdoInstInfo>>();
		for (DisciplinaUdoInstInfo disciplinaUdoInstInfo : data) {
			String key = disciplinaUdoInstInfo.getCodice()+"_"+disciplinaUdoInstInfo.getDescr();
			BigDecimal attuati = disciplinaUdoInstInfo.getPostiLetto();
			BigDecimal programmati = disciplinaUdoInstInfo.getPostiLettoAcc();
			List<DisciplinaUdoInstInfo> value = toRet.get(key);
			int discIndex = sedi.indexOf(disciplinaUdoInstInfo.getSede());
			if(value == null) {
				List<DisciplinaUdoInstInfo> newList = new ArrayList<DisciplinaUdoInstInfo>();
				for (String sede : sedi) {
					DisciplinaUdoInstInfo d = new DisciplinaUdoInstInfo();
					d.setSede(sede);
					newList.add(d);
				} 
				if(discIndex > -1)
					newList.set(discIndex,disciplinaUdoInstInfo);
				toRet.put(key, newList);
			}else {
				for (DisciplinaUdoInstInfo disc : value) {
					if(disc.getSede().equals(disciplinaUdoInstInfo.getSede())) {
						disc.setId(disciplinaUdoInstInfo.getId());
						disc.setPostiLettoAcc(programmati);
						disc.setPostiLetto(attuati);
					}
				}
			}
		}
		return toRet;
	}

	private Map<String, List<BrancaUdoInstInfo>> createSpecchietto3Map(List<BrancaUdoInstInfo> data,List<String> sedi) {
		Map<String, List<BrancaUdoInstInfo>> toRet = new TreeMap<String, List<BrancaUdoInstInfo>>();
		for (BrancaUdoInstInfo brancaUdoInstInfo : data) {
			String key = brancaUdoInstInfo.getCodice()+"_"+brancaUdoInstInfo.getDescr();
			List<BrancaUdoInstInfo> value = toRet.get(key);
			int branIndex = sedi.indexOf(brancaUdoInstInfo.getSede());
			if(value == null) {
				List<BrancaUdoInstInfo> newList = new ArrayList<BrancaUdoInstInfo>();
				for (String sede : sedi) {
					BrancaUdoInstInfo branProg = new BrancaUdoInstInfo();
					BrancaUdoInstInfo branAtt = new BrancaUdoInstInfo();
					branProg.setSede(sede);
					branAtt.setSede(sede);
					branProg.setTipoPostiLetto(TipoPostiLettoEnum.PROGRAMMATI);
					branAtt.setTipoPostiLetto(TipoPostiLettoEnum.ATTUATI);
					newList.add(branProg);
					newList.add(branAtt);
				}
				if(branIndex > -1)
					newList.set(branIndex,brancaUdoInstInfo);
				toRet.put(key, newList);
			}else {
				for (int i = 0; i < value.size(); i++) {
					BrancaUdoInstInfo bran = value.get(i);	
					if(bran.getSede().equals(brancaUdoInstInfo.getSede()) && 
							bran.getTipoPostiLetto() == brancaUdoInstInfo.getTipoPostiLetto()) {
						value.set(i,brancaUdoInstInfo);
					}
				}
			}
		}
		return toRet;
	}


	private List<String> deleteDuplicates(List<String> data) {
		Set<String> set = new HashSet<String>(data);
		List<String> noDuplSedi = new ArrayList<String>();
		noDuplSedi.addAll(set);
		return noDuplSedi;
	}

	private Map<String, List<BigDecimal>> createSpecchietto1TotMap(List<DisciplinaUdoInstInfo> data) {
	
		Map<String,List<BigDecimal>> toRet = new TreeMap<String, List<BigDecimal>>();
		for (DisciplinaUdoInstInfo disciplinaUdoInstInfo : data) {
			String area = disciplinaUdoInstInfo.getArea();
			String ordine = String.valueOf(disciplinaUdoInstInfo.getOrdineArea());
			String k = ordine+"_"+area;
			TipoPostiLettoEnum tipo = disciplinaUdoInstInfo.getTipoPostiLetto();
			BigDecimal plAu = (disciplinaUdoInstInfo.getPostiLetto() == null) ? new BigDecimal(0) : disciplinaUdoInstInfo.getPostiLetto();
			BigDecimal plAc = (disciplinaUdoInstInfo.getPostiLettoAcc() == null) ? new BigDecimal(0) : disciplinaUdoInstInfo.getPostiLettoAcc();
			BigDecimal plEx = (disciplinaUdoInstInfo.getPostiLettoExtra() == null) ? new BigDecimal(0) : disciplinaUdoInstInfo.getPostiLettoExtra();
					
			List<BigDecimal> totali = toRet.get(k);
	
			if(totali == null) {
				List<BigDecimal> toAdd = new ArrayList<BigDecimal>(Collections.nCopies(11, new BigDecimal(0)));
				if(tipo.equals(TipoPostiLettoEnum.PROGRAMMATI)) {
					toAdd.set(0, plAu);
					toAdd.set(1, plAc);
					toAdd.set(2, plEx);
				}else if(tipo.equals(TipoPostiLettoEnum.ATTUATI)) {
					toAdd.set(3, plAu);
					toAdd.set(4, plAc);
					toAdd.set(5, plEx);
				}else if(tipo.equals(TipoPostiLettoEnum.RICHIESTI)) {
					toAdd.set(6, plAu);
					toAdd.set(7, plAc);
					toAdd.set(8, plEx);
				}else if(tipo.equals(TipoPostiLettoEnum.FLUSSI)) {
					toAdd.set(9,plAu);
				}
				toRet.put(k, toAdd);
			}else {
				if(tipo.equals(TipoPostiLettoEnum.PROGRAMMATI)) {
					totali.set(0, totali.get(0).add(plAu));
					totali.set(1, totali.get(1).add(plAc));
					totali.set(2, totali.get(2).add(plEx));
				}else if(tipo.equals(TipoPostiLettoEnum.ATTUATI)) {
					totali.set(3, totali.get(3).add(plAu));
					totali.set(4, totali.get(4).add(plAc));
					totali.set(5, totali.get(5).add(plEx));
				}else if(tipo.equals(TipoPostiLettoEnum.RICHIESTI)) {
					totali.set(6, totali.get(6).add(plAu));
					totali.set(7, totali.get(7).add(plAc));
					totali.set(8, totali.get(8).add(plEx));
				}else if(tipo.equals(TipoPostiLettoEnum.FLUSSI)) {
					totali.set(9, totali.get(9).add(plAu));
				}
				if(verifyAllEqual(totali))
					totali.set(10,new BigDecimal(1));
				else 
					totali.set(10,new BigDecimal(0));
			}
		}
		return toRet;
	}

	private boolean verifyAllEqual(List<BigDecimal> list) {
		return list.isEmpty() || Collections.frequency(list, list.get(0)) == list.size();
	}

	public void tornaAllaListaUdo(ShowFolderPageBean showFolderPageBean) {
		showFolderPageBean.setTipoSpecchiettoSelected(0);
	}
}
