package it.tredi.auac.bean;

import it.highwaytech.apps.generic.XMLDocumento;
import it.highwaytech.apps.generic.XmlReplacer;
import it.highwaytech.apps.generic.utils.GenericUtils;
import it.tredi.auac.DocOrderTypeEnum;
import it.tredi.auac.PostItDataCrescenteComparator;
import it.tredi.auac.PostItDataDecrescenteComparator;
import it.tredi.auac.PostItOperatoreCrescenteComparator;
import it.tredi.auac.PostItOperatoreDecrescenteComparator;
import it.tredi.auac.PostItOrderTypeEnum;
import it.tredi.auac.PostItTypeEnum;
import it.tredi.auac.dao.DomandaInstDao;
import it.tredi.auac.dao.FolderDao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dom4j.Element;

public class FolderBean extends XWUIBean implements Serializable{

	private static final long serialVersionUID = -2111232342159087820L;

	private String oggetto;
	private String idUnit;
	private String folderType;
	private String folderStatus;
	private List<UdoUoInstForList> udoUoInstForL;
	private String dataCreazione;
	private String dataInvioDomanda;
	private String numeroProcedimento;
	private String dataConclusione;
	//private boolean inBozza;
	private boolean prontoPerInvio;
	//private boolean modificabilePerValtuazioneRegione;
	//private boolean modificabilePerIntegrazioni;
	//private boolean eliminabile;
	//private boolean valutabile;
	//private boolean richiedibileDiIntegrazioni;
	private boolean reinviabile;
	private boolean concludibile;
	private boolean concluso;
	//private List<Map<String,String>> postitL;
	//private List<Map<String,String>> postitMancanzaTipiUdoL;
	private PostItOrderTypeEnum postItOrderTypeEnum = PostItOrderTypeEnum.DataDecrescente;
	private List<PostIt> postitL;
	private String domandaClienId;
	private String oggettoDomanda;
	private UserBean userBean;
	private String numero;
	private String nrecord;
	private WorkflowXWProcessInstance workflowXWProcessInstance = null;
	private AnnotazioneBean noteVerifica;

	private DocOrderTypeEnum docWfTitolareOrderTypeEnum = DocOrderTypeEnum.DataDecrescente;
	private DocOrderTypeEnum docWfRegioneOrderTypeEnum = DocOrderTypeEnum.DataDecrescente;
	private DocOrderTypeEnum docTitolareOrderTypeEnum = DocOrderTypeEnum.DataDecrescente;
	
	//XML,/fascicolo/storia/creazione/@data "^|" XML,/fascicolo/extra/procedimento/@data_invio_domanda "^|" XML,/fascicolo/extra/procedimento/@data_conclusione 
	public FolderBean(UserBean userBean){
		super();
		this.userBean = userBean;
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public void setContents(XMLDocumento document) throws Exception {
    	super.setContents(document);

    	//nrecord
    	nrecord = document.getAttributeValue("//fascicolo/@nrecord", "");
    	
    	//oggetto
    	oggetto = document.getElementText("//fascicolo/oggetto", "");
    	
    	//numero
    	numero = document.getAttributeValue("//fascicolo/@numero", "");
    	
    	//idUnit
    	idUnit = document.getAttributeValue("/Response/Document/@idIUnit", "");
    	
    	//dataCreazione
    	String s = document.getAttributeValue("//fascicolo/storia/creazione/@data", "");
		s = (s.length() == 0)? "" : GenericUtils.dateFormat(XmlReplacer.normalizeDate(s));
		dataCreazione = s;   
		
    	//dataInvioDomanda
    	s = document.getAttributeValue("//fascicolo/extra/procedimento/@data_invio_domanda", "");
		s = (s.length() == 0)? "" : GenericUtils.dateFormat(XmlReplacer.normalizeDate(s));
		dataInvioDomanda = s;  		

    	//numeroProcedimento
    	s = document.getAttributeValue("//fascicolo/extra/procedimento/@numero_procedimento", "");
    	numeroProcedimento = FolderDao.formatNumeroProcedimento(s);
		
    	//dataConclusione
    	s = document.getAttributeValue("//fascicolo/extra/procedimento/@data_conclusione", "");
		s = (s.length() == 0)? "" : GenericUtils.dateFormat(XmlReplacer.normalizeDate(s));
		dataConclusione = s;  			
    	
    	//folderType
    	folderType = document.getAttributeValue("//fascicolo/extra/@folder_type", "");
    	
    	//folderStatus
    	folderStatus = document.getAttributeValue("//fascicolo/extra/@folder_status", "");    
    	
    	//domandaClienId
    	domandaClienId = document.getAttributeValue("//fascicolo/extra/procedimento/@domanda_client_id", "");

    	//oggettoDomanda
    	oggettoDomanda = document.getAttributeValue("//fascicolo/extra/procedimento/@oggetto_domanda", "");

    	//note verifica
    	List<Element> verificaL = document.selectNodes("//fascicolo/extra/verifica");
    	if(verificaL != null && verificaL.size() > 0) {
	    	for (Element verifica:verificaL) {
	    		noteVerifica= new AnnotazioneBean();
	    		noteVerifica.setTesto(verifica.getText());
	    		noteVerifica.setOperatore(verifica.attributeValue("operatore"));
	    		noteVerifica.setRuolo(verifica.attributeValue("ruolo"));
	    		noteVerifica.setData(GenericUtils.dateFormat(verifica.attributeValue("data")));
	    		noteVerifica.setOra(verifica.attributeValue("ora"));
	    		String admin = verifica.attributeValue("admin");
	    		if(admin != null && admin.equals("true"))
	    			noteVerifica.setAdmin(true);
	    		else
	    			noteVerifica.setAdmin(false);
	    	}
    	} else {
    		noteVerifica = null;
    	}
    	
    	/*
    	//postit
    	postitL = new ArrayList<Map<String,String>>();
    	List<Element> annotazioniL = document.selectNodes("//fascicolo/extra/annotazioni/annotazione");
    	for (Element annotazioneEl:annotazioniL) {
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("testo", annotazioneEl.getText());
    		String operatore = annotazioneEl.attributeValue("operatore");
    		map.put("operatore", operatore);
    		map.put("ruolo", annotazioneEl.attributeValue("ruolo"));
    		map.put("data", GenericUtils.dateFormat(annotazioneEl.attributeValue("data")));
    		map.put("ora", annotazioneEl.attributeValue("ora"));
    		
			//check sulla cancellabilità del postit (lo può fare solo l'operatore che l'ha inserito)
			map.put("isDeletable", "false");
			if (operatore.equals(userBean.getLogin()))
				map.put("isDeletable", "true");

			postitL.add(map);
    	}

    	//Note per tipi udo mancanti
    	postitMancanzaTipiUdoL = new ArrayList<Map<String,String>>();
    	List<Element> annotazioniMancanzaTipiUdoL = document.selectNodes("//fascicolo/extra/annotazionimancanzatipiudo/annotazione");
    	for (Element annotazioneEl:annotazioniMancanzaTipiUdoL) {
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("testo", annotazioneEl.getText());
    		String operatore = annotazioneEl.attributeValue("operatore");
    		map.put("operatore", operatore);
    		map.put("ruolo", annotazioneEl.attributeValue("ruolo"));
    		map.put("data", GenericUtils.dateFormat(annotazioneEl.attributeValue("data")));
    		map.put("ora", annotazioneEl.attributeValue("ora"));
    		
			//check sulla cancellabilità del postit (lo può fare solo l'operatore che l'ha inserito)
			map.put("isDeletable", "false");
			if (operatore.equals(userBean.getLogin()))
				map.put("isDeletable", "true");

			postitMancanzaTipiUdoL.add(map);
    	}*/
    	
    	postitL = new ArrayList<PostIt>();
    	List<Element> annotazioniL = document.selectNodes("//fascicolo/extra/annotazioni/annotazione");
    	int index = 0;
    	PostIt postiIt;
    	for (Element annotazioneEl:annotazioniL) {
    		postiIt = new PostIt();
    		postiIt.setTesto(annotazioneEl.getText());
    		String operatore = annotazioneEl.attributeValue("operatore");
    		postiIt.setOperatore(operatore);
    		postiIt.setRuolo(annotazioneEl.attributeValue("ruolo"));
    		postiIt.setOra(annotazioneEl.attributeValue("ora"));
    		postiIt.setDataOraForCompare(annotazioneEl.attributeValue("data") + postiIt.getOra());
    		postiIt.setData(GenericUtils.dateFormat(annotazioneEl.attributeValue("data")));
    		postiIt.setTipo(PostItTypeEnum.Generico);
    		postiIt.setStato("");
    		String admin = annotazioneEl.attributeValue("admin");
    		if(admin != null && admin.equals("true"))
    			postiIt.setAdmin(true);
    		else
    			postiIt.setAdmin(false);
    		postiIt.setXmlIndex(index++);
    		
			//check sulla cancellabilità del postit (lo può fare solo l'operatore che l'ha inserito)
    		postiIt.setDeletable(false);
			if (operatore.equals(userBean.getUtenteModel().getLoginDbOrCas()))
	    		postiIt.setDeletable(true);
			postitL.add(postiIt);
    	}
    	
    	index = 0;
    	List<Element> annotazioniMancanzaTipiUdoL = document.selectNodes("//fascicolo/extra/annotazionimancanzatipiudo/annotazione");
    	for (Element annotazioneEl:annotazioniMancanzaTipiUdoL) {
    		postiIt = new PostIt();
    		postiIt.setTesto(annotazioneEl.getText());
    		String operatore = annotazioneEl.attributeValue("operatore");
    		postiIt.setOperatore(operatore);
    		postiIt.setRuolo(annotazioneEl.attributeValue("ruolo"));
    		postiIt.setOra(annotazioneEl.attributeValue("ora"));
    		postiIt.setDataOraForCompare(annotazioneEl.attributeValue("data") + postiIt.getOra());
    		postiIt.setData(GenericUtils.dateFormat(annotazioneEl.attributeValue("data")));
    		postiIt.setTipo(PostItTypeEnum.MancanzaTipiUdo);
    		postiIt.setStato(annotazioneEl.attributeValue("stato", ""));
    		String admin = annotazioneEl.attributeValue("admin");
    		if(admin != null && admin.equals("true"))
    			postiIt.setAdmin(true);
    		else
    			postiIt.setAdmin(false);
    		postiIt.setXmlIndex(index++);
    		
			//check sulla cancellabilità del postit (lo può fare solo l'operatore che l'ha inserito)
    		postiIt.setDeletable(false);
			if (operatore.equals(userBean.getUtenteModel().getLoginDbOrCas()))
	    		postiIt.setDeletable(true);
			postitL.add(postiIt);
    	}

    	
    	//l'xml puo' contenere piu' flussi ma per ora ne gestiamo solo uno
		workflowXWProcessInstance = null;
    	List<Element> workflowL = document.selectNodes("//fascicolo/extra/wfs/wf");
    	for (Element workflowEl:workflowL) {
	    	s = workflowEl.attributeValue("processinstance_id");
	    	String pdId = workflowEl.attributeValue("processdefinition_id");
	    	if(s.length()>0) {
	    		workflowXWProcessInstance = new WorkflowXWProcessInstance(Long.parseLong(s), Long.parseLong(pdId));
	    		workflowXWProcessInstance.setProcessDefinitionName(workflowEl.attributeValue("processdefinition_name"));
	    		workflowXWProcessInstance.setProcessDefinitionVersion(workflowEl.attributeValue("processdefinition_version"));
	    		workflowXWProcessInstance.setOperatore(workflowEl.attributeValue("operatore"));
	    		workflowXWProcessInstance.setRuolo(workflowEl.attributeValue("ruolo"));
	    		workflowXWProcessInstance.setData(GenericUtils.dateFormat(workflowEl.attributeValue("data")));
	    		workflowXWProcessInstance.setOra(workflowEl.attributeValue("ora"));
	    	}
    	}
    }

	public String getOggetto() {
		return oggetto;
	}
 
	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	/*public List<UdoInst> getUdoL() {
		return udoL;
	}

	public void setUdoL(List<UdoInst> udoL) {
		this.udoL = udoL;
	}*/
	
	public List<UdoUoInstForList> getUdoUoInstForL() {
		return udoUoInstForL;
	}

	public void setUdoUoInstForL(List<UdoUoInstForList> udoUoInstForL) {
		this.udoUoInstForL = udoUoInstForL;
	}

	public String getFolderType() {
		return folderType;
	}

	public void setFolderType(String folderType) {
		this.folderType = folderType;
	}

	public String getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(String dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public String getDataInvioDomanda() {
		return dataInvioDomanda;
	}

	public void setDataInvioDomanda(String dataInvioDomanda) {
		this.dataInvioDomanda = dataInvioDomanda;
	}

	public String getDataConclusione() {
		return dataConclusione;
	}

	public void setDataConclusione(String dataConclusione) {
		this.dataConclusione = dataConclusione;
	}

	public String getFolderStatus() {
		return folderStatus;
	}

	public void setFolderStatus(String folderStatus) {
		this.folderStatus = folderStatus;
	}

	public String getIdUnit() {
		return idUnit;
	}

	public void setIdUnit(String idUnit) {
		this.idUnit = idUnit;
	}

	public boolean isProntoPerInvio() {
		return prontoPerInvio;
	}

	public void setProntoPerInvio(boolean prontoPerInvio) {
		this.prontoPerInvio = prontoPerInvio;
	}

	public boolean isEliminabile() {
		if (DomandaInstDao.STATO_BOZZA.equals(this.getFolderStatus()) && FolderDao.DOMANDA_TITOLARE_FOLDER_TYPE.equals(this.getFolderType()))
			return true;
		return false;
	}

	/*public void setEliminabile(boolean eliminabile) {
		this.eliminabile = eliminabile;
	}*/

	public boolean isValutabile() {
		if (((DomandaInstDao.STATO_AVVIATO.equals(this.getFolderStatus()))||(DomandaInstDao.STATO_INVIATA.equals(this.getFolderStatus()))) && FolderDao.DOMANDA_TITOLARE_FOLDER_TYPE.equals(this.getFolderType()))
			return true;
		return false;
	}

	/*public void setValutabile(boolean valutabile) {
		this.valutabile = valutabile;
	}*/

	/*public boolean isRichiedibileDiIntegrazioni() {
		return richiedibileDiIntegrazioni;
	}

	public void setRichiedibileDiIntegrazioni(boolean richiedibileDiIntegrazioni) {
		this.richiedibileDiIntegrazioni = richiedibileDiIntegrazioni;
	}*/

	public boolean isReinviabile() {
		return reinviabile;
	}

	public void setReinviabile(boolean reinviabile) {
		this.reinviabile = reinviabile;
	}

	public boolean isConcluso() {
		return concluso;
	}

	public void setConcluso(boolean concluso) {
		this.concluso = concluso;
	}

	/*public boolean isModificabilePerValtuazioneRegione() {
		return modificabilePerValtuazioneRegione;
	}

	public void setModificabilePerValtuazioneRegione(
			boolean modificabilePerValtuazioneRegione) {
		this.modificabilePerValtuazioneRegione = modificabilePerValtuazioneRegione;
	}
	*/

	public boolean isInBozza() {
		if (DomandaInstDao.STATO_BOZZA.equals(this.getFolderStatus()) && FolderDao.DOMANDA_TITOLARE_FOLDER_TYPE.equals(this.getFolderType()))
			return true;
		return false;
	}

	/*public void setInBozza(boolean inBozza) {
		this.inBozza = inBozza;
	}*/

	/*public boolean isModificabilePerIntegrazioni() {
		return modificabilePerIntegrazioni;
	}

	public void setModificabilePerIntegrazioni(boolean modificabilePerIntegrazioni) {
		this.modificabilePerIntegrazioni = modificabilePerIntegrazioni;
	}*/

	public boolean isConcludibile() {
		return concludibile;
	}

	public void setConcludibile(boolean concludibile) {
		this.concludibile = concludibile;
	}

	/*public List<Map<String, String>> getPostitL() {
		return postitL;
	}

	public void setPostitL(List<Map<String, String>> postitL) {
		this.postitL = postitL;
	}

	public List<Map<String, String>> getPostitMancanzaTipiUdoL() {
		return postitMancanzaTipiUdoL;
	}

	public void setPostitMancanzaTipiUdoL(List<Map<String, String>> postitMancanzaTipiUdoL) {
		this.postitMancanzaTipiUdoL = postitMancanzaTipiUdoL;
	}*/

	public List<PostIt> getOrderedPostitL() {
		if(postitL == null)
			return null;
		List<PostIt> toRet = new ArrayList<PostIt>(postitL);
		switch (postItOrderTypeEnum) {
		case DataDecrescente:
			Collections.sort(toRet, new PostItDataDecrescenteComparator());
			break;
		case DataCrescente:
			Collections.sort(toRet, new PostItDataCrescenteComparator());			
			break;
		case OperatoreCrescente:
			Collections.sort(toRet, new PostItOperatoreCrescenteComparator());			
			break;
		case OperatoreDecrescente:
			Collections.sort(toRet, new PostItOperatoreDecrescenteComparator());			
			break;
		}
		return toRet;
	}

	public List<PostIt> getPostitL() {
		return postitL;
	}

	public void setPostitL(List<PostIt> postitL) {
		this.postitL = postitL;
	}

	public String getNumeroProcedimento() {
		return numeroProcedimento;
	}

	public void setNumeroProcedimento(String numeroProcedimento) {
		this.numeroProcedimento = numeroProcedimento;
	}
	
	public String getDomandaClienId() {
		return domandaClienId;
	}

	public void setDomandaClienId(String domandaClienId) {
		this.domandaClienId = domandaClienId;
	}

	public String getOggettoDomanda() {
		return oggettoDomanda;
	}

	public void setOggettoDomanda(String oggettoDomanda) {
		this.oggettoDomanda = oggettoDomanda;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public WorkflowXWProcessInstance getWorkflowXWProcessInstance() {
		return workflowXWProcessInstance;
	}

	public void setWorkflowXWProcessInstance(WorkflowXWProcessInstance workflowXWProcessInstance) {
		this.workflowXWProcessInstance = workflowXWProcessInstance;
	}

	public boolean hasWorkflow() {
		return this.workflowXWProcessInstance != null;
	}

	public String getNrecord() {
		return nrecord;
	}

	public void setNrecord(String nrecord) {
		this.nrecord = nrecord;
	}

	public AnnotazioneBean getNoteVerifica() {
		return noteVerifica;
	}

	public void setNoteVerifica(AnnotazioneBean noteVerifica) {
		this.noteVerifica = noteVerifica;
	}

	public PostItOrderTypeEnum getPostItOrderTypeEnum() {
		return postItOrderTypeEnum;
	}

	public void setPostItOrderTypeEnum(PostItOrderTypeEnum postItOrderTypeEnum) {
		this.postItOrderTypeEnum = postItOrderTypeEnum;
	}

	public DocOrderTypeEnum getDocWfTitolareOrderTypeEnum() {
		return docWfTitolareOrderTypeEnum;
	}

	public void setDocWfTitolareOrderTypeEnum(
			DocOrderTypeEnum docWfTitolareOrderTypeEnum) {
		this.docWfTitolareOrderTypeEnum = docWfTitolareOrderTypeEnum;
	}

	public DocOrderTypeEnum getDocWfRegioneOrderTypeEnum() {
		return docWfRegioneOrderTypeEnum;
	}

	public void setDocWfRegioneOrderTypeEnum(
			DocOrderTypeEnum docWfRegioneOrderTypeEnum) {
		this.docWfRegioneOrderTypeEnum = docWfRegioneOrderTypeEnum;
	}

	public DocOrderTypeEnum getDocTitolareOrderTypeEnum() {
		return docTitolareOrderTypeEnum;
	}

	public void setDocTitolareOrderTypeEnum(
			DocOrderTypeEnum docTitolareOrderTypeEnum) {
		this.docTitolareOrderTypeEnum = docTitolareOrderTypeEnum;
	}

}
