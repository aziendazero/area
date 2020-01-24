package it.tredi.auac.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import it.tredi.auac.DocDataCrescenteComparator;
import it.tredi.auac.DocDataDecrescenteComparator;
import it.tredi.auac.DocOperatoreCrescenteComparator;
import it.tredi.auac.DocOperatoreDecrescenteComparator;
import it.tredi.auac.DocOrderTypeEnum;
import it.tredi.auac.DocTipoCrescenteComparator;
import it.tredi.auac.DocTipoDecrescenteComparator;
import it.tredi.auac.DocTypeEnum;
import it.tredi.auac.MotivoBloccoInvioReinvioDomandaEnum;
import it.tredi.auac.StatoEsitoEnum;
import it.tredi.auac.UdoUoInstForListCodiceUnivocoCrescenteComparator;
import it.tredi.auac.UdoUoInstForListCodiceUnivocoDecrescenteComparator;
import it.tredi.auac.UdoUoInstForListTipologiaUdoCrescenteComparator;
import it.tredi.auac.UdoUoInstForListTipologiaUdoDecrescenteComparator;
import it.tredi.auac.dao.DomandaInstDao;
import it.tredi.auac.orm.entity.AreaDiscipline;
import it.tredi.auac.orm.entity.AttoInst;
import it.tredi.auac.orm.entity.BrancaUdoInstInfo;
import it.tredi.auac.orm.entity.ClassificazioneUdoTempl;
import it.tredi.auac.orm.entity.DisciplinaTempl;
import it.tredi.auac.orm.entity.DisciplinaUdoInstInfo;
import it.tredi.auac.orm.entity.EsitoTempl;
import it.tredi.auac.orm.entity.TipoProcTempl;
import it.tredi.auac.orm.entity.TipoUdo22Templ;
import it.tredi.auac.orm.entity.TipoUdoUtenteTempl;
import it.tredi.auac.orm.entity.TitolareModel;
import it.tredi.auac.orm.entity.UdoInst;
import it.tredi.auac.orm.entity.UoModel;
import it.tredi.auac.orm.entity.UtenteModel;
import it.tredi.auac.service.AclService;
import it.tredi.auac.utils.AuacUtils;

public class ShowFolderPageBean implements Serializable {
	private static final long serialVersionUID = 7606767331995983751L;
	private FolderBean fascicolo;
	private String titolareFolderSubject;
	private int activeRowIndex;
	private List<Map<String,String>> allDocumentsL;
	private List<EsitoTempl> esitiPossibili;
	private List<Map<String,String>> attiML;
	private String nuovaAnnotazione;
	private String messages;
	private String oggettoAllegato;
	private DocTypeEnum docTypeAllegato = null;
	private TipoProcTempl tipoProcTemp;
	private TitolareModel titolareDomanda;
	private Date domandaCreation = null;
	private DeliberaBean delibera;
	private String sessionId;

	//contiene una lista di, uno per ogni udoUoForList selezionato, {udoUoInstForList.getClientId(), Map.Entry<(clientid della uo padre se udo altrimenti ""), true se RequisitoGenerale altrimenti false>>}
	private Map<String,Map.Entry<String, Boolean>> checkM;
	private boolean allChecked;
	
	//per Workflow
	//NOTA nel FolderBean è presente il campo workflowProcessInstance che contiene i dati dell'istanza di workflow agganciata caricati da extraway
	//i dati relativi al workflow caricati da bonita vengono messi qui per aggiornarli in modo indipendente rispetto al FolderBean di extraway
	
	//per assegnazione massiva dei risultati
	private String personaSelected;
	private List<PersonaSelectOption> personaL;
	private String uoSelected;
	private List<UoModel> uoL;
	private List<AreaDiscipline> areeDiscipline;
	
	//Contiene la lista ordinata secondo l'ordinazione scelta delle UdoUoInstForList
	private List<UdoUoInstForList> orderedUdoUoInstForL = null;
	//Contiene la lista filtrata delle UdoUoInstForList il filtro verra' eseguito sulla orderedUdoUoInstForL
	private List<UdoUoInstForList> filteredUdoUoInstForL;
	
	//bean per la ricerca delle UdoInst
	UdoInstSearchOrderBean udoInstSearchOrderBean = new UdoInstSearchOrderBean();

	//Ricerca requisiti
	private RequisitoSearchBean requisitoSearchBean;
	
	//copia/incolla autovalutazione
	private UdoUoInstForList copiedUdoUoInst;
	private JobBean amJobBean;
	
	//Workflow
	private String workflowDefinitionSelected = "";
	private WorkflowProcessInstance workflowProcessInstance;
	
	private List<TipoUdoUtenteTempl> tipiUdoUtenteTemplPerTipoTitolareDomandaCorrente;
    private List<String> tipoUdo22TemplClientIdWithAmbitoAmbulatoriale;
    
	private List<TipoUdo22Templ> tipoUdo22TemplRichiestiMancanti;
	
	//Per la gestione delle verifiche
	private UtenteModel verificatoreTeamLeader = null;
	private List<UtenteModel> teamVerificatori = null;
	//Se true indica che tutte le valutazioni del verificatore sono state inserite
	private boolean domandaVerificata = false;
	//Indica se la domanda è passata da uno degli stati 'GESTIONE DELLE VERIFICHE INSERIMENTO VERIFICHE', 'GESTIONE DELLE VERIFICHE'
	//Utilizzato per gestire i requisiti SR	
	private boolean domandaPassataDaStatiVerifica = false;
	
	//clientid delle Udo, Uo, Rga(domanda) i cui requisiti non hanno tutti i verificatori assegnati
    private List<String> udoUoRgaIdsConReqNoVerificatori;
	//clientid delle Udo, Uo, Rga(domanda) i cui requisiti non sono tutti i verificati
    private List<String> udoUoRgaIdsConReqNonVerificati;
	//clientid delle Udo, Uo, Rga(domanda) che hanno requisiti assegnati in verifica all'utente corrente
    private List<String> udoUoRgaIdsConReqAssVerUserCor;
	private String pianificazioneVerificaDateVerifica;
	
	//Eventuale motivazione per cui la Domanda non puo' essere inviata
	MotivoBloccoInvioReinvioDomandaEnum motivoBloccoInvioReinvioDomandaEnum = MotivoBloccoInvioReinvioDomandaEnum.NonApplicabile;
    
	private List<TipoProcTempl> tipiProcL;
	
	//dati per specchietti posti letto
	private int tipoSpecchiettoSelected = 0;
	private Map<String,Map<String,List<DisciplinaUdoInstInfo>>> specchietto1Map;
	private Map<String, List<DisciplinaUdoInstInfo>> specchietto2Map;
	private Map<String, List<BrancaUdoInstInfo>> specchietto3Map;
	private Map<String, List<BigDecimal>> totMap;
	private List<String> sedi;

	private List<AttoInst> attoInstDomandaL;
	
	private List<ClassificazioneUdoTempl> classificazioniUdoTempl;
	
	public ShowFolderPageBean() {
		requisitoSearchBean = new RequisitoSearchBean();
	}

	//Da chiamare quando si vuole riportare il bean allo stato di default, per esempio quando si passa da una domanda all'altra 
	public void setInitState() {
		this.activeRowIndex = 0;
		this.clearUdoSearchForm();
		checkM = new HashMap<String, Map.Entry<String, Boolean>>();
		copiedUdoUoInst = null;
		amJobBean = null;
		allChecked = false;
	}
	
	public void clearUdoSearchForm() {
		this.udoInstSearchOrderBean.setSubStringDenominazioneDaCercare(""); 
		this.udoInstSearchOrderBean.setSubStringTipoUdoDaCercare(""); 
		this.udoInstSearchOrderBean.setSubStringDisciplinaDaCercare("");
		this.udoInstSearchOrderBean.setSubStringBrancaDaCercare("");
		this.udoInstSearchOrderBean.setSubStringSedeOperativaDaCercare(""); 
		this.udoInstSearchOrderBean.setSubStringUODaCercare("");
		this.udoInstSearchOrderBean.setSubStringStabilimento("");
		this.udoInstSearchOrderBean.setSubStringBlocco("");
		this.udoInstSearchOrderBean.setSubStringPiano("");
		this.udoInstSearchOrderBean.setSubStringProgressivo("");
		this.udoInstSearchOrderBean.setSubStringDirettoreDaCercare(""); 
		this.udoInstSearchOrderBean.setSubStringCodiceUnivocoDaCercare(""); 
		this.udoInstSearchOrderBean.setShowOnlyUdoAssigned(false);
		this.udoInstSearchOrderBean.setShowOnlyUdoAssignedVerifica(false);
		this.udoInstSearchOrderBean.setShowOnlyCongruenzaSenzaEsito(false);
		this.udoInstSearchOrderBean.setDataScadenzaStringDa("");
		this.udoInstSearchOrderBean.setDataScadenzaStringA("");
		this.udoInstSearchOrderBean.setAreaDisciplineDaCercare("");
		this.udoInstSearchOrderBean.setClassificazioneUdoTemplDaCercare("");
		this.udoInstSearchOrderBean.setShowOnlyUdoConRequisiti(false);
	}

	public FolderBean getFascicolo() {
		return fascicolo;
	}

	public void setFascicolo(FolderBean fascicolo) {
		this.fascicolo = fascicolo;
	}

	public String getTitolareFolderSubject() {
		return titolareFolderSubject;
	}

	public void setTitolareFolderSubject(String titolareFolderSubject) {
		this.titolareFolderSubject = titolareFolderSubject;
	}

	public int getActiveRowIndex() {
		return activeRowIndex;
	}

	public void setActiveRowIndex(int activeRowIndex) {
		this.activeRowIndex = activeRowIndex;
	}

	//public List<Map<String, String>> getWorkflowDocumentsL(long processInstanceId) {
	public List<Map<String, String>> getWorkflowDocumentsL() {
		List<Map<String,String>> toRet = new ArrayList<Map<String,String>>();
		if(allDocumentsL != null) {
			for (Map<String,String> map:allDocumentsL) {
				if(map.containsKey("wf_processinstance_id"))
					toRet.add(map);
			}
		}
		return toRet;
	}

	public List<Map<String, String>> getWorkflowDocumentsByTipo(String tipo) {
		List<Map<String,String>> toRet = new ArrayList<Map<String,String>>();
		if(allDocumentsL != null) {
			for (Map<String,String> map:allDocumentsL) {
				if(map.containsKey("wf_processinstance_id") && map.containsKey("tipo") && tipo.equals(map.get("tipo")))
					toRet.add(map);
			}
		}
		
		DocOrderTypeEnum docToOrder = fascicolo.getDocWfTitolareOrderTypeEnum();
		if("regione".equals(tipo))
			docToOrder = fascicolo.getDocWfRegioneOrderTypeEnum();

		switch (docToOrder) {
			case DataDecrescente:
				Collections.sort(toRet, new DocDataDecrescenteComparator());
				break;
		case DataCrescente:
			Collections.sort(toRet, new DocDataCrescenteComparator());			
			break;
		case OperatoreCrescente:
			Collections.sort(toRet, new DocOperatoreCrescenteComparator());			
			break;
		case OperatoreDecrescente:
			Collections.sort(toRet, new DocOperatoreDecrescenteComparator());			
			break;
		}
		
		return toRet;
	}

	public List<Map<String, String>> getDocumentsL() {
		List<Map<String,String>> toRet = new ArrayList<Map<String,String>>();
		if(allDocumentsL != null) {
			for (Map<String,String> map:allDocumentsL) {
				if(!map.containsKey("wf_processinstance_id"))
					toRet.add(map);
			}
		}

		switch (fascicolo.getDocTitolareOrderTypeEnum()) {
			case DataDecrescente:
				Collections.sort(toRet, new DocDataDecrescenteComparator());
				break;
			case DataCrescente:
				Collections.sort(toRet, new DocDataCrescenteComparator());			
				break;
			case OperatoreCrescente:
				Collections.sort(toRet, new DocOperatoreCrescenteComparator());			
				break;
			case OperatoreDecrescente:
				Collections.sort(toRet, new DocOperatoreDecrescenteComparator());			
				break;
			case TipoDecrescente:
				Collections.sort(toRet, new DocTipoDecrescenteComparator());
				break;
			case TipoCrescente:
				Collections.sort(toRet, new DocTipoCrescenteComparator());			
				break;
		}
		return toRet;
	}

	public List<Map<String, String>> getAllDocumentsL() {
		return allDocumentsL;
	}

	public void setAllDocumentsL(List<Map<String, String>> documentsL) {
		this.allDocumentsL = documentsL;
	}

	public List<EsitoTempl> getEsitiPossibili() {
		return esitiPossibili;
	}

	public void setEsitiPossibili(List<EsitoTempl> esitiPossibili) {
		this.esitiPossibili = esitiPossibili;
	}

	public String getNuovaAnnotazione() {
		return nuovaAnnotazione;
	}

	public void setNuovaAnnotazione(String nuovaAnnotazione) {
		this.nuovaAnnotazione = nuovaAnnotazione;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	public String getOggettoAllegato() {
		return oggettoAllegato;
	}

	public void setOggettoAllegato(String oggettoAllegato) {
		this.oggettoAllegato = oggettoAllegato;
	}

	public DocTypeEnum getDocTypeAllegato() {
		return docTypeAllegato;
	}

	public void setDocTypeAllegato(DocTypeEnum docTypeAllegato) {
		this.docTypeAllegato = docTypeAllegato;
	}

	public List<Map<String,String>> getAttiML() {
		return attiML;
	}

	public void setAttiML(List<Map<String,String>> attiML) {
		this.attiML = attiML;
	}

	public TipoProcTempl getTipoProcTemp() {
		return tipoProcTemp;
	}

	public void setTipoProcTemp(TipoProcTempl tipoProcTemp) {
		this.tipoProcTemp = tipoProcTemp;
	}

	public TitolareModel getTitolareDomanda() {
		return titolareDomanda;
	}

	public void setTitolareDomanda(TitolareModel titolareDomanda) {
		this.titolareDomanda = titolareDomanda;
	}

	public String getPersonaSelected() {
		return personaSelected;
	}

	public void setPersonaSelected(String personaSelected) {
		this.personaSelected = personaSelected;
	}

	public List<PersonaSelectOption> getPersonaL() {
		return personaL;
	}

	public void setPersonaL(List<PersonaSelectOption> personaL) {
		this.personaL = personaL;
	}

	public String getUoSelected() {
		return uoSelected;
	}

	public void setUoSelected(String uoSelected) {
		this.uoSelected = uoSelected;
	}

	public List<UoModel> getUoL() {
		return uoL;
	}

	public void setUoL(List<UoModel> uoL) {
		this.uoL = uoL;
	}
	
	public Map<String, Map.Entry<String, Boolean>> getCheckM() {
		return checkM;
	}

	public void setCheckM(Map<String, Map.Entry<String, Boolean>> checkM) {
		this.checkM = checkM;
	}

	public boolean isAllChecked() {
		return allChecked;
	}

	public void setAllChecked(boolean allChecked) {
		this.allChecked = allChecked;
	}

	public boolean isRequisitiGeneraliSelected() {
		if(checkM != null) {
			for(Map.Entry<String, Entry<String, Boolean>> kv : checkM.entrySet()) {
				if(kv.getValue().getKey().isEmpty() && kv.getValue().getValue())
					return true;
			}
		}
		return false;
	}

	public int getNumberUoSelected() {
		int toRet = 0;
		if(checkM != null) {
			for(Map.Entry<String, Entry<String, Boolean>> kv : checkM.entrySet()) {
				if(kv.getValue().getKey().isEmpty() && !kv.getValue().getValue())
					toRet++;
			}
		}
		return toRet;
	}

	public int getNumberUdoSelected() {
		int toRet = 0;
		if(checkM != null) {
			for(Map.Entry<String, Entry<String, Boolean>> kv : checkM.entrySet()) {
				if(!kv.getValue().getKey().isEmpty())
					toRet++;
			}
		}
		return toRet;
	}

	public boolean checked(int index) {
		return checkM.containsKey(filteredUdoUoInstForL.get(index).getClientId());
	}

	public UdoInstSearchOrderBean getUdoInstSearchOrderBean() {
		return udoInstSearchOrderBean;
	}

	public void setUdoInstSearchOrderBean(
			UdoInstSearchOrderBean udoInstSearchOrderBean) {
		this.udoInstSearchOrderBean = udoInstSearchOrderBean;
	}
	
	public void setFilteredUdoUoInstForL(List<UdoUoInstForList> filteredUdoUoInstForL) {
		this.filteredUdoUoInstForL = filteredUdoUoInstForL;
	}

	public List<UdoUoInstForList> getFilteredUdoUoInstForL() {
		return this.filteredUdoUoInstForL;
	}
	
	public UdoUoInstForList getCopiedUdoUoInst() {
		return copiedUdoUoInst;
	}

	public void setCopiedUdoUoInst(UdoUoInstForList copiedUdoUoInst) {
		this.copiedUdoUoInst = copiedUdoUoInst;
	}

	public JobBean getAmJobBean() {
		return amJobBean;
	}

	public void setAmJobBean(JobBean amJobBean) {
		this.amJobBean = amJobBean;
	}

	public void showCheckedUdoUoList() {
		List<UdoUoInstForList> listUdoUoInstForList = new ArrayList<UdoUoInstForList>();
		//filtro solo le uoInst e UdoInst selezionate
		//for(UdoUoInstForList udoUoInstForList : fascicolo.getUdoUoInstForL()) {
		for(UdoUoInstForList udoUoInstForList : getOrderedUdoUoInstForL()) {
			//Pattern.compile(Pattern.quote(s2), Pattern.CASE_INSENSITIVE).matcher(s1).find();//s2 stringa da cercare s1 stringa in cui si cerca
			if(checkM.containsKey(udoUoInstForList.getClientId())) {
				listUdoUoInstForList.add(udoUoInstForList);
			}
		}
		setFilteredUdoUoInstForL(listUdoUoInstForList);
	}
	
	public void mostraTutti() {
		//setFilteredUdoUoInstForL(fascicolo.getUdoUoInstForL());		
		setFilteredUdoUoInstForL(getOrderedUdoUoInstForL());
	}
	
	//devo passare anche il titolare della domanda
	private boolean checkRegionUserCanEditEsito(UdoUoInstForList udoUoInstForList, UtenteModel currentUtenteModel) {
		//UdoUoInstForList udoUoInstForList = pageBean.getFilteredUdoUoInstForL().get(pageBean.getActiveRowIndex());
		//da UtenteModel.tipoUdoUtenteTempls1 ricavo l'elenco delle tipoUdo22 per il tipoTitolare della domanda corrente
		//da udoInst.tipoUdo.tipoUdoTempl.tipoUdo22Templ
		//String tipoUdo22TemplClientid = udoUoInstForList.getTipoUdoTemplTipoUdo22TemplCodiceUdo()
		
//		if(udoUoInstForList.isUdo()) {
//			if(this.getTipiUdoUtenteTemplPerTipoTitolareDomandaCorrente() != null) {
//				for(TipoUdoUtenteTempl tipoUdoUtenteTempl : this.getTipiUdoUtenteTemplPerTipoTitolareDomandaCorrente()) {
//					if(currentUtenteModel.getClientid().equals(tipoUdoUtenteTempl.getUtenteModel().getClientid()) && udoUoInstForList.getUdoInst().getTipoUdoTempl().getTipoUdo22Templ().getClientid().equals(tipoUdoUtenteTempl.getTipoUdo22Templ().getClientid()))
//						return true;
//				}
//			}
//		} else if (udoUoInstForList.isUo()) {
//			//devo controllare se l'utente corrente puo' modificare l'esito di una qualsiasi delle udo figlie della uo corrente
//			for(UdoUoInstForList uuifl : this.getFascicolo().getUdoUoInstForL()) {
//				if(uuifl.isUdo() && uuifl.getUoClientId().equals(udoUoInstForList.getUoClientId()) && this.checkRegionUserCanEditEsito(uuifl, currentUtenteModel))
//					return true;
//			}
//		}
//		return false;
		
		return AuacUtils.checkRegionUserCanEditEsito(udoUoInstForList, currentUtenteModel, this.getTipiUdoUtenteTemplPerTipoTitolareDomandaCorrente(), this.getFascicolo().getUdoUoInstForL());
	}
	
	public boolean checkUdoEsitoIsNull(UdoUoInstForList udoUoInstForList) {
		if(udoUoInstForList.isUdo()) {
			if(udoUoInstForList.getUdoInst() != null)
				if(udoUoInstForList.getUdoInst().getEsito() == null || udoUoInstForList.getUdoInst().getEsito().isEmpty())
					return true;
		}
		
		return false;
	}

	public void orderUdoUoInstForL(UtenteModel currentUtenteModel, AreaDiscipline areaDiscipline, List<String> clientidUdoSenzaRequisiti) {
		List<UdoUoInstForList> udoUoInstFL = null;
		switch (this.udoInstSearchOrderBean.getUdoUoInstForListOrderType()) {
			case CodiceUnivocoCrescente:
				udoUoInstFL = new ArrayList<UdoUoInstForList>(this.getFascicolo().getUdoUoInstForL());
				Collections.sort(udoUoInstFL, new UdoUoInstForListCodiceUnivocoCrescenteComparator());
				//Collections.reverse(udoUoInstFL);
				break;
			case CodiceUnivocoDecrescente:
				udoUoInstFL = new ArrayList<UdoUoInstForList>(this.getFascicolo().getUdoUoInstForL());
				Collections.sort(udoUoInstFL, new UdoUoInstForListCodiceUnivocoDecrescenteComparator());
				break;
			case TipologiaUdoCrescente:
				udoUoInstFL = new ArrayList<UdoUoInstForList>(this.getFascicolo().getUdoUoInstForL());
				Collections.sort(udoUoInstFL, new UdoUoInstForListTipologiaUdoCrescenteComparator());
				break;
			case TipologiaUdoDecrescente:
				udoUoInstFL = new ArrayList<UdoUoInstForList>(this.getFascicolo().getUdoUoInstForL());
				Collections.sort(udoUoInstFL, new UdoUoInstForListTipologiaUdoDecrescenteComparator());
				break;
		}
		this.orderedUdoUoInstForL = udoUoInstFL;
		this.filterUdoUoInstForL(currentUtenteModel, areaDiscipline, clientidUdoSenzaRequisiti);
	}
	
	public void filterUdoUoInstForL(UtenteModel currentUtenteModel, AreaDiscipline areaDiscipline, List<String> clientidUdoSenzaRequisiti) {
		//se non sono impostati parametri di ricerca ritorno la lista completa
		if(udoInstSearchOrderBean == null ||
					(
						(udoInstSearchOrderBean.getSubStringDenominazioneDaCercare() == null || udoInstSearchOrderBean.getSubStringDenominazioneDaCercare().isEmpty()) && 
						(udoInstSearchOrderBean.getSubStringTipoUdoDaCercare() == null || udoInstSearchOrderBean.getSubStringTipoUdoDaCercare().isEmpty()) && 
						(udoInstSearchOrderBean.getSubStringBrancaDaCercare() == null || udoInstSearchOrderBean.getSubStringBrancaDaCercare().isEmpty()) && 
						(udoInstSearchOrderBean.getSubStringDisciplinaDaCercare() == null || udoInstSearchOrderBean.getSubStringDisciplinaDaCercare().isEmpty()) && 
						(udoInstSearchOrderBean.getSubStringSedeOperativaDaCercare() == null || udoInstSearchOrderBean.getSubStringSedeOperativaDaCercare().isEmpty()) && 
						(udoInstSearchOrderBean.getSubStringDirettoreDaCercare() == null || udoInstSearchOrderBean.getSubStringDirettoreDaCercare().isEmpty()) && 
						(udoInstSearchOrderBean.getSubStringCodiceUnivocoDaCercare() == null || udoInstSearchOrderBean.getSubStringCodiceUnivocoDaCercare().isEmpty()) &&
						(udoInstSearchOrderBean.getSubStringStabilimento() == null || udoInstSearchOrderBean.getSubStringStabilimento().isEmpty()) &&
						(udoInstSearchOrderBean.getSubStringBlocco() == null || udoInstSearchOrderBean.getSubStringBlocco().isEmpty()) &&
						(udoInstSearchOrderBean.getSubStringPiano() == null || udoInstSearchOrderBean.getSubStringPiano().isEmpty()) &&
						(udoInstSearchOrderBean.getSubStringProgressivo() == null || udoInstSearchOrderBean.getSubStringProgressivo().isEmpty()) && 
						udoInstSearchOrderBean.getDataScadenzaDa() == null &&
						udoInstSearchOrderBean.getDataScadenzaA() == null &&
						(udoInstSearchOrderBean.getSubStringUODaCercare() == null || udoInstSearchOrderBean.getSubStringUODaCercare().isEmpty()) &&
						(udoInstSearchOrderBean.getSubStringCodiceUlssDaCercare() == null || udoInstSearchOrderBean.getSubStringCodiceUlssDaCercare().isEmpty()) &&
						(udoInstSearchOrderBean.getSubStringCodiceExUlssDaCercare() == null || udoInstSearchOrderBean.getSubStringCodiceExUlssDaCercare().isEmpty()) &&
						!udoInstSearchOrderBean.isShowOnlyUdoAssigned() &&
						!udoInstSearchOrderBean.isShowOnlyUdoAssignedVerifica() &&
						!udoInstSearchOrderBean.isShowOnlyCongruenzaSenzaEsito() &&
						areaDiscipline == null &&
						clientidUdoSenzaRequisiti == null &&
						(udoInstSearchOrderBean.getClassificazioneUdoTemplDaCercare() == null || udoInstSearchOrderBean.getClassificazioneUdoTemplDaCercare().isEmpty())						
					)
				) {
			//setFilteredUdoUoInstForL(fascicolo.getUdoUoInstForL());
			setFilteredUdoUoInstForL(getOrderedUdoUoInstForL());
			return;
		}
		
		List<UdoUoInstForList> listUdoUoInstForList = new ArrayList<UdoUoInstForList>();
		//sono impostati parametri di ricerca ritorno la lista filtrata
		if((udoInstSearchOrderBean.getSubStringDenominazioneDaCercare() == null || udoInstSearchOrderBean.getSubStringDenominazioneDaCercare().isEmpty()) && 
				(udoInstSearchOrderBean.getSubStringTipoUdoDaCercare() == null || udoInstSearchOrderBean.getSubStringTipoUdoDaCercare().isEmpty()) && 
				(udoInstSearchOrderBean.getSubStringBrancaDaCercare() == null || udoInstSearchOrderBean.getSubStringBrancaDaCercare().isEmpty()) && 
				(udoInstSearchOrderBean.getSubStringDisciplinaDaCercare() == null || udoInstSearchOrderBean.getSubStringDisciplinaDaCercare().isEmpty()) && 
				(udoInstSearchOrderBean.getAreaDisciplineDaCercare() == null || udoInstSearchOrderBean.getAreaDisciplineDaCercare().isEmpty()) && 
				(udoInstSearchOrderBean.getSubStringSedeOperativaDaCercare() == null || udoInstSearchOrderBean.getSubStringSedeOperativaDaCercare().isEmpty()) && 
				(udoInstSearchOrderBean.getSubStringDirettoreDaCercare() == null || udoInstSearchOrderBean.getSubStringDirettoreDaCercare().isEmpty()) && 
				(udoInstSearchOrderBean.getSubStringCodiceUnivocoDaCercare() == null || udoInstSearchOrderBean.getSubStringCodiceUnivocoDaCercare().isEmpty()) &&
				(udoInstSearchOrderBean.getSubStringCodiceUlssDaCercare() == null || udoInstSearchOrderBean.getSubStringCodiceUlssDaCercare().isEmpty()) &&
				(udoInstSearchOrderBean.getSubStringCodiceExUlssDaCercare() == null || udoInstSearchOrderBean.getSubStringCodiceExUlssDaCercare().isEmpty()) &&
				(udoInstSearchOrderBean.getSubStringStabilimento() == null || udoInstSearchOrderBean.getSubStringStabilimento().isEmpty()) &&
				(udoInstSearchOrderBean.getSubStringBlocco() == null || udoInstSearchOrderBean.getSubStringBlocco().isEmpty()) &&
				(udoInstSearchOrderBean.getSubStringPiano() == null || udoInstSearchOrderBean.getSubStringPiano().isEmpty()) &&
				(udoInstSearchOrderBean.getSubStringProgressivo() == null || udoInstSearchOrderBean.getSubStringProgressivo().isEmpty()) && 
				udoInstSearchOrderBean.getDataScadenzaDa() == null &&
				udoInstSearchOrderBean.getDataScadenzaA() == null &&
				areaDiscipline == null &&
				clientidUdoSenzaRequisiti == null &&
				(udoInstSearchOrderBean.getClassificazioneUdoTemplDaCercare() == null || udoInstSearchOrderBean.getClassificazioneUdoTemplDaCercare().isEmpty())						
				) {
			if(udoInstSearchOrderBean.getSubStringUODaCercare() != null && !udoInstSearchOrderBean.getSubStringUODaCercare().isEmpty()) {
				//getSubStringUODaCercare() e' valorizzato cerco solo le UO
				//L'unica ricerca attiva è relativa al nome della UO quindi carico tutte le UO con il nome cercato e le relative UDO
				//se isShowOnlyUdoAssigned filtro anche per quelle assegnate
				//se isShowOnlyUdoAssignedVerifica filtro anche per quelle assegnate in verifica
				//String uoClientIdPrecOk = "";
				List<String> uoIds = new ArrayList<String>();
				for(UdoUoInstForList udoUoInstForList : fascicolo.getUdoUoInstForL()) {
					//Pattern.compile(Pattern.quote(s2), Pattern.CASE_INSENSITIVE).matcher(s1).find();//s2 stringa da cercare s1 stringa in cui si cerca
					/*if(udoUoInstForList.isUdo()) {
						if(udoUoInstForList.getUoClientId().equals(uoClientIdPrecOk) 
								&& 
								(!isShowOnlyUdoAssigned() || this.checkRegionUserCanEditEsito(udoUoInstForList, currentUtenteModel)) )
							listUdoUoInstForList.add(udoUoInstForList);
					}
					else*/ if(udoUoInstForList.isUo()) {
						if(checkFieldLikeValue(udoInstSearchOrderBean.getSubStringUODaCercare(), udoUoInstForList.getUoInst().getDenominazione()) 
								&& (!udoInstSearchOrderBean.isShowOnlyUdoAssigned() || this.checkRegionUserCanEditEsito(udoUoInstForList, currentUtenteModel))) {
							//uoClientIdPrecOk = udoUoInstForList.getUoInst().getUoModelClientid();
							//listUdoUoInstForList.add(udoUoInstForList);
							uoIds.add(udoUoInstForList.getUoInst().getUoModelClientid());
						}
					}
				}
				for(UdoUoInstForList udoUoInstForList : getOrderedUdoUoInstForL()) {
					if(udoUoInstForList.isUdo()) {
						if(uoIds.contains(udoUoInstForList.getUoClientId()) 
								&& (!udoInstSearchOrderBean.isShowOnlyUdoAssigned() || this.checkRegionUserCanEditEsito(udoUoInstForList, currentUtenteModel)) 
								&& (!udoInstSearchOrderBean.isShowOnlyCongruenzaSenzaEsito() || this.checkUdoEsitoIsNull(udoUoInstForList)))
							listUdoUoInstForList.add(udoUoInstForList);
					}
					else if(udoUoInstForList.isUo()) {
						if(uoIds.contains(udoUoInstForList.getUoInst().getUoModelClientid())) {
							listUdoUoInstForList.add(udoUoInstForList);
						}
					}
				}
			} else if (udoInstSearchOrderBean.isShowOnlyUdoAssigned()) {
				//devo cercare solo gli assegnati non sono impostati altri parametri
				//for(UdoUoInstForList udoUoInstForList : fascicolo.getUdoUoInstForL()) {
				for(UdoUoInstForList udoUoInstForList : getOrderedUdoUoInstForL()) {
					if(this.checkRegionUserCanEditEsito(udoUoInstForList, currentUtenteModel) 
							&& (!udoInstSearchOrderBean.isShowOnlyCongruenzaSenzaEsito() || this.checkUdoEsitoIsNull(udoUoInstForList)))
						listUdoUoInstForList.add(udoUoInstForList);					
				}
			} else if (udoInstSearchOrderBean.isShowOnlyUdoAssignedVerifica()) {
				//devo cercare solo gli assegnati non sono impostati altri parametri
				if(getUdoUoRgaIdsConReqAssVerUserCor() != null) {
					//for(UdoUoInstForList udoUoInstForList : fascicolo.getUdoUoInstForL()) {
					for(UdoUoInstForList udoUoInstForList : getOrderedUdoUoInstForL()) {
						if(getUdoUoRgaIdsConReqAssVerUserCor().contains(udoUoInstForList.getClientId()))
							listUdoUoInstForList.add(udoUoInstForList);					
					}
				}
				//!(getUdoUoRgaIdsConReqNonVerificati() != null && getUdoUoRgaIdsConReqNonVerificati().contains(udoUoInstForList.getClientId()));

			} else if (udoInstSearchOrderBean.isShowOnlyCongruenzaSenzaEsito()) {
				for(UdoUoInstForList udoUoInstForList : getOrderedUdoUoInstForL()) {
					if(this.checkUdoEsitoIsNull(udoUoInstForList)
							&& (!udoInstSearchOrderBean.isShowOnlyUdoAssigned() || this.checkRegionUserCanEditEsito(udoUoInstForList, currentUtenteModel)))
						listUdoUoInstForList.add(udoUoInstForList);
				}
			}
		}
		else {
			//sono impostati parametri di ricerca della udo ritorno la lista filtrata
			//Ricerco le udo secondo i parametri di ricerca
			Set<String> disciplineClientids = null;
			if(areaDiscipline != null) {
				disciplineClientids = new HashSet<String>();
				for(DisciplinaTempl disc : areaDiscipline.getDisciplinaTempls())
					disciplineClientids.add(disc.getClientid());
			}
			Set<String> tipoUdo22TemplClientidsForClassificazioneUdoTempl = null;
			if(udoInstSearchOrderBean.getClassificazioneUdoTemplDaCercare() != null && !udoInstSearchOrderBean.getClassificazioneUdoTemplDaCercare().isEmpty()) {
				tipoUdo22TemplClientidsForClassificazioneUdoTempl = new HashSet<String>();
				for(ClassificazioneUdoTempl classificazioneUdoTempl: this.getClassificazioniUdoTempl()) {
					if(udoInstSearchOrderBean.getClassificazioneUdoTemplDaCercare().equals(classificazioneUdoTempl.getClientid())) {
						for(TipoUdo22Templ tipo22 : classificazioneUdoTempl.getTipoUdo22Templs()) {
							tipoUdo22TemplClientidsForClassificazioneUdoTempl.add(tipo22.getClientid());
						}
						break;
					}
				}
			}
			if((udoInstSearchOrderBean.getSubStringUODaCercare() == null || udoInstSearchOrderBean.getSubStringUODaCercare().isEmpty())) {
				//non devo cercare anche per UO ma solo per udo verranno restituite solo le udo trovate senza le relative uo
				//for(UdoUoInstForList udoUoInstForList : fascicolo.getUdoUoInstForL()) {
				for(UdoUoInstForList udoUoInstForList : getOrderedUdoUoInstForL()) {
					//Pattern.compile(Pattern.quote(s2), Pattern.CASE_INSENSITIVE).matcher(s1).find();//s2 stringa da cercare s1 stringa in cui si cerca
					if(udoUoInstForList.isUdo()) {
						if(checkUdoFilter(udoUoInstForList.getUdoInst(), disciplineClientids, tipoUdo22TemplClientidsForClassificazioneUdoTempl) 
								&& (!udoInstSearchOrderBean.isShowOnlyUdoAssigned() || this.checkRegionUserCanEditEsito(udoUoInstForList, currentUtenteModel)) 
								&& (!udoInstSearchOrderBean.isShowOnlyUdoAssignedVerifica() || (getUdoUoRgaIdsConReqAssVerUserCor() != null && getUdoUoRgaIdsConReqAssVerUserCor().contains(udoUoInstForList.getClientId()))
								&& (!udoInstSearchOrderBean.isShowOnlyCongruenzaSenzaEsito() || this.checkUdoEsitoIsNull(udoUoInstForList)))
								&& (clientidUdoSenzaRequisiti == null || !clientidUdoSenzaRequisiti.contains(udoUoInstForList.getClientId()))
						)
							listUdoUoInstForList.add(udoUoInstForList);
					}
				}				
			}
			else {
				//devo filtrare sia per uo che per udo ma restituiro' solo udo trovate senza le relative uo
				//String uoClientIdPrecOk = "";
				List<String> uoIds = new ArrayList<String>();
				for(UdoUoInstForList udoUoInstForList : fascicolo.getUdoUoInstForL()) {
					if(udoUoInstForList.isUo()) {
						//e' una UoInst
						if(checkFieldLikeValue(udoInstSearchOrderBean.getSubStringUODaCercare(), udoUoInstForList.getUoInst().getDenominazione()) 
								&& (!udoInstSearchOrderBean.isShowOnlyUdoAssigned() || this.checkRegionUserCanEditEsito(udoUoInstForList, currentUtenteModel)) 
								&& (!udoInstSearchOrderBean.isShowOnlyUdoAssignedVerifica() || (getUdoUoRgaIdsConReqAssVerUserCor() != null && getUdoUoRgaIdsConReqAssVerUserCor().contains(udoUoInstForList.getClientId()))
								&& (!udoInstSearchOrderBean.isShowOnlyCongruenzaSenzaEsito() || this.checkUdoEsitoIsNull(udoUoInstForList)))
						) {
							uoIds.add(udoUoInstForList.getUoInst().getUoModelClientid());
						}
					}
				}				

				for(UdoUoInstForList udoUoInstForList : getOrderedUdoUoInstForL()) {
					//Pattern.compile(Pattern.quote(s2), Pattern.CASE_INSENSITIVE).matcher(s1).find();//s2 stringa da cercare s1 stringa in cui si cerca
					if(udoUoInstForList.isUdo()) {
						if(uoIds.contains(udoUoInstForList.getUoClientId()) 
								&& checkUdoFilter(udoUoInstForList.getUdoInst(), disciplineClientids, tipoUdo22TemplClientidsForClassificazioneUdoTempl) 
								&& (!udoInstSearchOrderBean.isShowOnlyUdoAssigned() || this.checkRegionUserCanEditEsito(udoUoInstForList, currentUtenteModel))
								&& (!udoInstSearchOrderBean.isShowOnlyUdoAssignedVerifica() || (getUdoUoRgaIdsConReqAssVerUserCor() != null && getUdoUoRgaIdsConReqAssVerUserCor().contains(udoUoInstForList.getClientId()))
								&& (!udoInstSearchOrderBean.isShowOnlyCongruenzaSenzaEsito() || this.checkUdoEsitoIsNull(udoUoInstForList)))
								&& (clientidUdoSenzaRequisiti == null || !clientidUdoSenzaRequisiti.contains(udoUoInstForList.getClientId()))
						)
							listUdoUoInstForList.add(udoUoInstForList);
					}
					/*else if(udoUoInstForList.isUo()) {
						//e' una UoInst
						if(checkFieldLikeValue(getSubStringUODaCercare(), udoUoInstForList.getUoInst().getDenominazione()) 
								&& (!isShowOnlyUdoAssigned() || this.checkRegionUserCanEditEsito(udoUoInstForList, currentUtenteModel)) 
								&& (!isShowOnlyUdoAssignedVerifica() || (getUdoUoRgaIdsConReqAssVerUserCor() != null && getUdoUoRgaIdsConReqAssVerUserCor().contains(udoUoInstForList.getClientId())))
						) {
							uoClientIdPrecOk = udoUoInstForList.getUoInst().getUoModelClientid();
						}
					}*/
				}				
			}
		}
		setFilteredUdoUoInstForL(listUdoUoInstForList);		
	}
	
	private boolean checkUdoFilter(UdoInst udoInst, Set<String> disciplineClientids, Set<String> tipoUdo22TemplClientidsForClassificazioneUdoTempl) {
		return checkFieldLikeValue(udoInstSearchOrderBean.getSubStringDenominazioneDaCercare(), udoInst.getDescr()) &&
			checkFieldLikeValue(udoInstSearchOrderBean.getSubStringTipoUdoDaCercare(), udoInst.getTipoUdoExtendedDescr()) &&
			checkFieldLikeValue(udoInstSearchOrderBean.getSubStringSedeOperativaDaCercare(), udoInst.getDenominazioneSede()) &&
			checkFieldLikeValue(udoInstSearchOrderBean.getSubStringDirettoreDaCercare(), udoInst.getDirSanitarioCogn() + " " + udoInst.getDirSanitarioNome()) &&
			checkFieldLikeValue(udoInstSearchOrderBean.getSubStringCodiceUnivocoDaCercare(), udoInst.getIdUnivoco()) &&
			checkFieldLikeValue(udoInstSearchOrderBean.getSubStringStabilimento(), udoInst.getStabilimento()) &&
			checkFieldLikeValue(udoInstSearchOrderBean.getSubStringBlocco(), udoInst.getBlocco()) &&
			checkFieldLikeValue(udoInstSearchOrderBean.getSubStringPiano(), udoInst.getPiano()) &&
			checkFieldLikeValue(udoInstSearchOrderBean.getSubStringProgressivo(), udoInst.getProgressivo() == null ? "" : udoInst.getProgressivo().toString()) &&
			checkFieldLikeValue(udoInstSearchOrderBean.getSubStringCodiceUlssDaCercare(), udoInst.getCodiceUlssTerritoriale()) &&
			checkFieldLikeValue(udoInstSearchOrderBean.getSubStringCodiceExUlssDaCercare(), udoInst.getCodiceUlssPrecedente()) &&
			checkUdoInstBranca(udoInstSearchOrderBean.getSubStringBrancaDaCercare(), udoInst) &&
			checkUdoInstDisciplina(udoInstSearchOrderBean.getSubStringDisciplinaDaCercare(), udoInst) &&
			checkDateFieldBetweenValues(udoInstSearchOrderBean.getDataScadenzaDa(), udoInstSearchOrderBean.getDataScadenzaA(), udoInst.getScadenza()) &&
			checkUdoInstAreaDiscipline(disciplineClientids, udoInst) && 
			checkUdoInstClassificazioneUdoTempl(tipoUdo22TemplClientidsForClassificazioneUdoTempl, udoInst);
	}

	private boolean checkUdoInstClassificazioneUdoTempl(Set<String> tipoUdo22TemplClientidsForClassificazioneUdoTempl, UdoInst udoInst) {
		//se non e' impostato la stringa da cercare ritorno true come se il controllo fosse valido  
		if(tipoUdo22TemplClientidsForClassificazioneUdoTempl == null)
			return true;
		//e' impostato il valore da cercare se non e' impostato il valore del campo ritorno false
		if(udoInst.getTipoUdoTempl() == null || udoInst.getTipoUdoTempl().getTipoUdo22Templ() == null)
			return false;

		if(tipoUdo22TemplClientidsForClassificazioneUdoTempl.contains(udoInst.getTipoUdoTempl().getTipoUdo22Templ().getClientid())){
			return true;
		}	

		return false;		
	}

	private boolean checkUdoInstAreaDiscipline(Set<String> disciplineClientids, UdoInst udoInst) {
		//se non e' impostato la stringa da cercare ritorno true come se il controllo fosse valido  
		if(disciplineClientids == null)
			return true;
		//e' impostato il valore da cercare se non e' impostato il valore del campo ritorno false
		if(udoInst.getDisciplineUdoInstsInfo() == null || udoInst.getDisciplineUdoInstsInfo().size() == 0)
			return false;

		for(DisciplinaUdoInstInfo disciplinaTempl:udoInst.getDisciplineUdoInstsInfo()){
			if(disciplineClientids.contains(disciplinaTempl.getId())){
				return true;
			}	
		}

		return false;		
	}

	private boolean checkDateFieldBetweenValues(Date searchFrom, Date searchTo, Date value) {
		//se non sono impostate le date da cercare ritorno true come se il controllo fosse valido  
		if(searchFrom == null && searchTo == null)
			return true;
		//e' impostata almeno una data da cercare se non e' impostato il valore del campo ritorno false
		if(value == null)
			return false;
		//il valore da cercare e' impostato quindi controllo se e' valido
		if(
			(searchFrom == null || searchFrom.compareTo(value) <= 0)
			&&
			(searchTo == null || searchTo.compareTo(value) >= 0)
		)
			return true;
		return false;
	}

	private boolean checkFieldLikeValue(String search, String value) {
		//se non e' impostato la stringa da cercare ritorno true come se il controllo fosse valido  
		if(search == null || search.isEmpty())
			return true;
		//e' impostato il valore da cercare se non e' impostato il valore del campo ritorno false
		if(value == null)
			return false;
		//il valore da cercare e' impostato quindi controllo se è valido
		//Pattern.compile(Pattern.quote(s2), Pattern.CASE_INSENSITIVE).matcher(s1).find();//s2 stringa da cercare s1 stringa in cui si cerca
		if(Pattern.compile(Pattern.quote(search), Pattern.CASE_INSENSITIVE).matcher(value).find())
			return true;
		return false;
	}

	private boolean checkUdoInstBranca(String search, UdoInst udoInst) {
		//se non e' impostato la stringa da cercare ritorno true come se il controllo fosse valido  
		if(search == null || search.isEmpty())
			return true;
		//e' impostato il valore da cercare se non e' impostato il valore del campo ritorno false
		if(udoInst.getBrancheUdoInstsInfo() == null || udoInst.getBrancheUdoInstsInfo().size() == 0)
			return false;

		for(BrancaUdoInstInfo branca:udoInst.getBrancheUdoInstsInfo()){
			if(Pattern.compile(Pattern.quote(search), Pattern.CASE_INSENSITIVE).matcher(branca.getDescr()).find() ){
				return true;
			}	
		}

		return false;
	}
	
	private boolean checkUdoInstDisciplina(String search, UdoInst udoInst) {
		//se non e' impostato la stringa da cercare ritorno true come se il controllo fosse valido  
		if(search == null || search.isEmpty())
			return true;
		//e' impostato il valore da cercare se non e' impostato il valore del campo ritorno false
		if(udoInst.getDisciplineUdoInstsInfo() == null || udoInst.getDisciplineUdoInstsInfo().size() == 0)
			return false;

		for(DisciplinaUdoInstInfo disciplinaTempl:udoInst.getDisciplineUdoInstsInfo()){
			if(Pattern.compile(Pattern.quote(search), Pattern.CASE_INSENSITIVE).matcher(disciplinaTempl.getDescr()).find() ){
				return true;
			}	
		}

		return false;		
	}
	
	public RequisitoSearchBean getRequisitoSearchBean() {
		return requisitoSearchBean;
	}

	public void setRequisitoSearchBean(RequisitoSearchBean requisitoSearchBean) {
		this.requisitoSearchBean = requisitoSearchBean;
	}

	public String getWorkflowDefinitionSelected() {
		return workflowDefinitionSelected;
	}

	public void setWorkflowDefinitionSelected(String workflowDefinitionSelected) {
		this.workflowDefinitionSelected = workflowDefinitionSelected;
	}

	public WorkflowProcessInstance getWorkflowProcessInstance() {
		return workflowProcessInstance;
	}

	public void setWorkflowProcessInstance(
			WorkflowProcessInstance workflowProcessInstance) {
		this.workflowProcessInstance = workflowProcessInstance;
	}

	public boolean isNoteMancanzaTipiUdoControllate() {
		for(PostIt postIt : this.getFascicolo().getPostitL()) {
			if(PostIt.STATO_DA_CONTROLLARE.equals(postIt.getStato()))
				return false;
		}
		return true;
	}

	public boolean isEsitiCompleti() {
		if(DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE.equals(this.getFascicolo().getFolderStatus())) {
			if(this.getFascicolo().getUdoUoInstForL() != null) {
				for (UdoUoInstForList udoUoInstForList:this.getFascicolo().getUdoUoInstForL()) {
					//if(udoUoInstForList.isUdo() || udoUoInstForList.isUo()) {
					if(udoUoInstForList.isUdo()) {
						if (udoUoInstForList.getEsito() == null || udoUoInstForList.getEsito().isEmpty()) {
							return false;
						} else if (udoUoInstForList.getUdoInst().getEsitoStato() != null && udoUoInstForList.getUdoInst().getEsitoStato() == StatoEsitoEnum.CONTROLLARE) {
							return false;
						}
					}
				}
			}
		} else if(DomandaInstDao.STATO_INSERIMENTO_ESITO.equals(this.getFascicolo().getFolderStatus())) {
			if(this.getFascicolo().getUdoUoInstForL() != null) {
				for (UdoUoInstForList udoUoInstForList:this.getFascicolo().getUdoUoInstForL()) {
					//if(udoUoInstForList.isUdo() || udoUoInstForList.isUo()) {
					if(udoUoInstForList.isUdo()) {
						if (udoUoInstForList.getEsito() == null || udoUoInstForList.getEsito().isEmpty())
							return false;
						//L'esito e' valorizzato a seconda del valore impostato cambiano i controlli
						//se la Udo/Uo e' "Non ammessa al procedimento" e' OK non mi interessa altro
						if(AclService.UDOUO_ESITO_SI.equals(udoUoInstForList.getEsito()) 
								|| AclService.UDOUO_ESITO_PARZIALEDAINTEGRARE.equals(udoUoInstForList.getEsito())
								|| AclService.UDOUO_ESITO_AMMESSACONRISERVA.equals(udoUoInstForList.getEsito())
								) {
							//Esito finale non ancora impostato
							return false;
						}
						else {
							//Le udo con esito "Non ammessa al procedimento" o "Non accreditata" o "Non autorizzata" non richiedono la data
							if(
									!AclService.UDOUO_ESITO_NO.equals(udoUoInstForList.getEsito()) && 
									!"Non Accreditata".equalsIgnoreCase(udoUoInstForList.getEsito()) &&
									!"Non Autorizzata".equalsIgnoreCase(udoUoInstForList.getEsito()) &&
									(udoUoInstForList.getEsitoDataInizio() == null || udoUoInstForList.getEsitoScadenza() == null)
							  )
								return false;
						}
					}
				}
			}			
		}
		return true;
	}

	/*
	public boolean isEsitiUoCongruentiUdoFiglie() {
		if(DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE.equals(this.getFascicolo().getFolderStatus())) {
			if(this.getFascicolo().getUdoUoInstForL() != null) {
				for (UdoUoInstForList udoUoInstForList:this.getFascicolo().getUdoUoInstForL()) {
					if(udoUoInstForList.isUo()) {
						if (udoUoInstForList.getEsito() != null && AclService.UDOUO_ESITO_NO.equals(udoUoInstForList.getEsito()) ) {
							//devo controllare che tutte le udo figlie della uo abbiano esito uguale ad AclService.UDOUO_ESITO_NO
							for (UdoUoInstForList udoUoInstForListFigle:this.getFascicolo().getUdoUoInstForL()) {
								if(udoUoInstForListFigle.isUdo() && udoUoInstForListFigle.getUoClientId().equals(udoUoInstForList.getUoClientId())) {
									if(!udoUoInstForListFigle.getEsito().equals(AclService.UDOUO_ESITO_NO))
										return false;
								}
							}
						}
					}
				}
			}
		} else if(DomandaInstDao.STATO_INSERIMENTO_ESITO.equals(this.getFascicolo().getFolderStatus())) {
			if(this.getFascicolo().getUdoUoInstForL() != null) {
				for (UdoUoInstForList udoUoInstForList:this.getFascicolo().getUdoUoInstForL()) {
					if(udoUoInstForList.isUo()) {
						if (udoUoInstForList.getEsito() != null && 
								(
										"Non Accreditata".equalsIgnoreCase(udoUoInstForList.getEsito()) ||  
										"Non Autorizzata".equalsIgnoreCase(udoUoInstForList.getEsito()) ||
										AclService.UDOUO_ESITO_NO.equals(udoUoInstForList.getEsito())  
								)
							) {
							//devo controllare che tutte le udo figlie della uo abbiano esito uguale ad AclService.UDOUO_ESITO_NO
							for (UdoUoInstForList udoUoInstForListFigle:this.getFascicolo().getUdoUoInstForL()) {
								if(udoUoInstForListFigle.isUdo() && udoUoInstForListFigle.getUoClientId().equals(udoUoInstForList.getUoClientId())) {
									if(
											!AclService.UDOUO_ESITO_NO.equalsIgnoreCase(udoUoInstForListFigle.getEsito()) &&
											!"Non Accreditata".equalsIgnoreCase(udoUoInstForListFigle.getEsito()) &&
											!"Non Autorizzata".equalsIgnoreCase(udoUoInstForListFigle.getEsito())
									  )
										return false;
								}
							}
						}
					}
				}
			}
		}
		return true;
	}
	*/

	public List<TipoUdoUtenteTempl> getTipiUdoUtenteTemplPerTipoTitolareDomandaCorrente() {
		return tipiUdoUtenteTemplPerTipoTitolareDomandaCorrente;
	}

	public void setTipiUdoUtenteTemplPerTipoTitolareDomandaCorrente(
			List<TipoUdoUtenteTempl> tipiUdoUtenteTemplPerTipoTitolareDomandaCorrente) {
		this.tipiUdoUtenteTemplPerTipoTitolareDomandaCorrente = tipiUdoUtenteTemplPerTipoTitolareDomandaCorrente;
	}

	public List<String> getTipoUdo22TemplClientIdWithAmbitoAmbulatoriale() {
		return tipoUdo22TemplClientIdWithAmbitoAmbulatoriale;
	}

	public void setTipoUdo22TemplClientIdWithAmbitoAmbulatoriale(List<String> tipoUdo22TemplClientIdWithAmbitoAmbulatoriale) {
		this.tipoUdo22TemplClientIdWithAmbitoAmbulatoriale = tipoUdo22TemplClientIdWithAmbitoAmbulatoriale;
	}

	public List<TipoUdo22Templ> getTipoUdo22TemplRichiestiMancanti() {
		return this.tipoUdo22TemplRichiestiMancanti;
	}

	public void setTipoUdo22TemplRichiestiMancanti(List<TipoUdo22Templ> tipoUdo22TemplRichiestiMancanti) {
		this.tipoUdo22TemplRichiestiMancanti = tipoUdo22TemplRichiestiMancanti;
	}
	
	public UtenteModel getVerificatoreTeamLeader() {
		return verificatoreTeamLeader;
	}

	public void setVerificatoreTeamLeader(UtenteModel verificatoreTeamLeader) {
		this.verificatoreTeamLeader = verificatoreTeamLeader;
	}

	public List<UtenteModel> getTeamVerificatori() {
		return teamVerificatori;
	}

	public void setTeamVerificatori(List<UtenteModel> teamVerificatori) {
		this.teamVerificatori = teamVerificatori;
	}

	public boolean isDomandaVerificata() {
		return domandaVerificata;
	}

	public void setDomandaVerificata(boolean domandaVerificata) {
		this.domandaVerificata = domandaVerificata;
	}

	public List<String> getUdoUoRgaIdsConReqNoVerificatori() {
		return udoUoRgaIdsConReqNoVerificatori;
	}

	public void setUdoUoRgaIdsConReqNoVerificatori(
			List<String> udoUoRgaIdsConReqNoVerificatori) {
		this.udoUoRgaIdsConReqNoVerificatori = udoUoRgaIdsConReqNoVerificatori;
	}

	public List<String> getUdoUoRgaIdsConReqNonVerificati() {
		return udoUoRgaIdsConReqNonVerificati;
	}

	public void setUdoUoRgaIdsConReqNonVerificati(
			List<String> udoUoRgaIdsConReqNonVerificati) {
		this.udoUoRgaIdsConReqNonVerificati = udoUoRgaIdsConReqNonVerificati;
	}

	public List<String> getUdoUoRgaIdsConReqAssVerUserCor() {
		return udoUoRgaIdsConReqAssVerUserCor;
	}

	public void setUdoUoRgaIdsConReqAssVerUserCor(
			List<String> udoUoRgaIdsConReqAssVerUserCor) {
		this.udoUoRgaIdsConReqAssVerUserCor = udoUoRgaIdsConReqAssVerUserCor;
	}

	public boolean isTuttiVerificatoriAssegnati(UdoUoInstForList udoUoInstForList) {
		return !(getUdoUoRgaIdsConReqNoVerificatori() != null && getUdoUoRgaIdsConReqNoVerificatori().contains(udoUoInstForList.getClientId()));
	}

	public boolean isTuttiRequisitiVerificati(UdoUoInstForList udoUoInstForList) {
		return !(getUdoUoRgaIdsConReqNonVerificati() != null && getUdoUoRgaIdsConReqNonVerificati().contains(udoUoInstForList.getClientId()));
	}

	public String getPianificazioneVerificaDateVerifica() {
		return pianificazioneVerificaDateVerifica;
	}

	public void setPianificazioneVerificaDateVerifica(
			String pianificazioneVerificaDateVerifica) {
		this.pianificazioneVerificaDateVerifica = pianificazioneVerificaDateVerifica;
	}

	public List<UdoUoInstForList> getOrderedUdoUoInstForL() {
		if(orderedUdoUoInstForL == null && fascicolo != null) {
			return fascicolo.getUdoUoInstForL();
		}
		return orderedUdoUoInstForL;
	}

	public void setOrderedUdoUoInstForL(List<UdoUoInstForList> orderedUdoUoInstForL) {
		this.orderedUdoUoInstForL = orderedUdoUoInstForL;
	}

	public MotivoBloccoInvioReinvioDomandaEnum getMotivoBloccoInvioReinvioDomandaEnum() {
		return motivoBloccoInvioReinvioDomandaEnum;
	}

	public void setMotivoBloccoInvioReinvioDomandaEnum(
			MotivoBloccoInvioReinvioDomandaEnum motivoBloccoInvioReinvioDomandaEnum) {
		this.motivoBloccoInvioReinvioDomandaEnum = motivoBloccoInvioReinvioDomandaEnum;
	}

	public List<TipoProcTempl> getTipiProcL() {
		return tipiProcL;
	}

	public void setTipiProcL(List<TipoProcTempl> tipiProcL) {
		this.tipiProcL = tipiProcL;
	}

	public Date getDomandaCreation() {
		return domandaCreation;
	}

	public void setDomandaCreation(Date domandaCreation) {
		this.domandaCreation = domandaCreation;
	}

	public boolean isDomandaPassataDaStatiVerifica() {
		return domandaPassataDaStatiVerifica;
	}

	public void setDomandaPassataDaStatiVerifica(
			boolean domandaPassataDaStatiVerifica) {
		this.domandaPassataDaStatiVerifica = domandaPassataDaStatiVerifica;
	}

	public DeliberaBean getDelibera() {
		return delibera;
	}

	public void setDelibera(DeliberaBean delibera) {
		this.delibera = delibera;
	}

	public List<AreaDiscipline> getAreeDiscipline() {
		return areeDiscipline;
	}

	public void setAreeDiscipline(List<AreaDiscipline> areeDiscipline) {
		this.areeDiscipline = areeDiscipline;
	}
	
	public int getTipoSpecchiettoSelected() {
		return tipoSpecchiettoSelected;
	}

	public void setTipoSpecchiettoSelected(int tipoSpecchiettoSelected) {
		this.tipoSpecchiettoSelected = tipoSpecchiettoSelected;
	}

	public Map<String,Map<String,List<DisciplinaUdoInstInfo>>> getSpecchietto1Map() {
		return specchietto1Map;
	}

	public void setSpecchietto1Map(Map<String,Map<String,List<DisciplinaUdoInstInfo>>> disciplinaUdoInstInfoMap) {
		this.specchietto1Map = disciplinaUdoInstInfoMap;
	}

	public Map<String, List<DisciplinaUdoInstInfo>> getSpecchietto2Map() {
		return specchietto2Map;
	}

	public void setSpecchietto2Map(Map<String, List<DisciplinaUdoInstInfo>> specchietto2Map) {
		this.specchietto2Map = specchietto2Map;
	}
	
	public Map<String, List<BrancaUdoInstInfo>> getSpecchietto3Map() {
		return specchietto3Map;
	}

	public void setSpecchietto3Map(Map<String, List<BrancaUdoInstInfo>> specchietto3Map) {
		this.specchietto3Map = specchietto3Map;
	}


	public Map<String, List<BigDecimal>> getTotMap() {
		return totMap;
	}

	public void setTotMap(Map<String, List<BigDecimal>> totMap) {
		this.totMap = totMap;
	}

	public List<String> getSedi() {
		return sedi;
	}

	public void setSedi(List<String> sedi) {
		this.sedi = sedi;
	}

	public List<AttoInst> getAttoInstDomandaL() {
		return attoInstDomandaL;
	}

	public void setAttoInstDomandaL(List<AttoInst> attoInstDomandaL) {
		this.attoInstDomandaL = attoInstDomandaL;
	}
	
	public List<ClassificazioneUdoTempl> getClassificazioniUdoTempl() {
		return classificazioniUdoTempl;
	}

	public void setClassificazioniUdoTempl(List<ClassificazioneUdoTempl> classificazioniUdoTempl) {
		this.classificazioniUdoTempl = classificazioniUdoTempl;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
