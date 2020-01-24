package it.tredi.auac.bean;

import it.tredi.auac.orm.entity.TipoProcTempl;
import it.tredi.auac.orm.entity.TitolareModel;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ShowFolderAttiPageBean implements Serializable {
	private static final long serialVersionUID = 8949826065742001031L;

	//folderPageBean.setTitolareFolderSubject(getTitolareFolderSubject(folderPageBean.getFascicolo().getContents(), _xwService));
	//folderPageBean.setAllDocumentsL(getFascicoloDocumentsL(externalContext, userBean, folderPageBean.getFascicolo().getContents(), _xwService));
	
	
	private TitolariPageBean titolariPageBean; 
	
	//private String titolareFolderSubject;
	private FolderBean fascicolo;
	private int activeRowIndex;
	//private List<Map<String,String>> allDocumentsL;
	private List<Map<String,String>> attiML;
	private String messages;

	private List<TipoProcTempl> tipiProcL;	
    
	public ShowFolderAttiPageBean(TitolariPageBean titolariPageBean) {
		this.titolariPageBean = titolariPageBean;
	}

	public FolderBean getFascicolo() {
		return fascicolo;
	}

	public void setFascicolo(FolderBean fascicolo) {
		this.fascicolo = fascicolo;
	}

	public int getActiveRowIndex() {
		return activeRowIndex;
	}

	public void setActiveRowIndex(int activeRowIndex) {
		this.activeRowIndex = activeRowIndex;
	}

	/*
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
	*/

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	public List<Map<String,String>> getAttiML() {
		return attiML;
	}

	public void setAttiML(List<Map<String,String>> attiML) {
		this.attiML = attiML;
	}

	public TitolareModel getTitolareDomanda() {
		return titolariPageBean.getCurrentTitolare();
	}

	public TitolariPageBean getTitolariPageBean() {
		
		return titolariPageBean;
	}

	public List<TipoProcTempl> getTipiProcL() {
		return tipiProcL;
	}

	public void setTipiProcL(List<TipoProcTempl> tipiProcL) {
		this.tipiProcL = tipiProcL;
	}

	/*public String getTitolareFolderSubject() {
		return titolareFolderSubject;
	}

	public void setTitolareFolderSubject(String titolareFolderSubject) {
		this.titolareFolderSubject = titolareFolderSubject;
	}*/

}
