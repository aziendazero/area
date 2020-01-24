package it.tredi.auac.bean;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.tredi.auac.DomandaFieldToOrderEnum;
import it.tredi.auac.DomandaOrderTypeEnum;
import it.tredi.auac.orm.entity.AreaDiscipline;
import it.tredi.auac.orm.entity.BrancaUdoInstInfo;
import it.tredi.auac.orm.entity.DirezioneTempl;
import it.tredi.auac.orm.entity.DisciplinaUdoInstInfo;
import it.tredi.auac.orm.entity.TipoProcTempl;
import it.tredi.auac.orm.entity.TitolareModel;

public class HomePageBean implements Serializable {
	
	private List<String> messages;

	private static final long serialVersionUID = -9006118046738759503L;
	
	private TitolareModel titolareModel; //nel caso di titolare
	//private List<UdoModel>udoL; //nel caso di titolare ELIMINATO PER AVERE LA LISTA DELLE OU e UDO insieme
	private List<UdoUoForList>udoUoL; //nel caso di titolare
	private XWFolderTitlesBean fascicoli; //nel caso di titolare
	private boolean allChecked;
	private JobBean JobBean;
	private List<TipoProcTempl> tipiProcL;
	private List<String> folderStatusL;
	private String tipoProcSelected = "";
	private String oggettoDomanda = "";
	private String oggettoDomandaDaCercare;
	private String subStringtitolareDaCercare;
	private String dataCreazioneDa;
	private String dataCreazioneA;
	private String dataInvioDomandaDa;
	private String dataInvioDomandaA;
	private String dataConclusioneDa;
	private String dataConclusioneA;
	private String subStringDenominazioneDaCercare;
	private String subStringTipoUdoDaCercare;
	private String subStringBrancaDaCercare;
	private String subStringDisciplinaDaCercare;
	private AreaDiscipline areaDisciplineDaCercare;
	private String subStringSedeOperativaDaCercare;
	private String subStringUODaCercare;
	private String subStringCodiceUnivocoDaCercare;
	private String subStringDirettoreDaCercare;
	private String subStringCodiceExUlssDaCercare;
	private String subStringCodiceUlssDaCercare;
	private String folderStatusSelected = "";
	private int activeRowIndex;
	
	private Map<String,String> checkM;
	private Map<String,Integer> domandeClientIdAssegnateAdUtenteCollaboratoreENumeroRisposteMancanti;
	
	private DomandaOrderTypeEnum domandaOrderTypeEnum = null;
	private List<DirezioneTempl> direzioniTempl;
	private String direzioneTemplClientidSel;
	
	private boolean showOnlyDomandeAssignedCongruenzaEsito = false;
	private boolean showOnlyDomandeAssignedCongruenzaSenzaEsito = false;
	private boolean showDomandeConTaskPerUtente = false;
	
	public HomePageBean() {
		activeRowIndex = 0;		
	}	
	
	public TitolareModel getTitolareModel() {
		return titolareModel;
	}

	public void setTitolareModel(TitolareModel titolareModel) {
		this.titolareModel = titolareModel;
	}

	public List<UdoUoForList> getUdoUoL() {
		return udoUoL;
	}

	public void setUdoUoL(List<UdoUoForList> udoUoL) {
		this.udoUoL = udoUoL;
		
		if (checkM == null) {
			checkM = new HashMap<String, String>();
		}
	}

	public boolean isAllChecked() {
		return allChecked;
	}

	public void setAllChecked(boolean allChecked) {
		this.allChecked = allChecked;
	}

	public boolean checked(int index) {
		return checkM.containsKey(udoUoL.get(index).getClientId());
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public JobBean getJobBean() {
		return JobBean;
	}

	public void setJobBean(JobBean jobBean) {
		JobBean = jobBean;
	}
	
	public String getTipoProcSelected() {
		return tipoProcSelected;
	}

	public void setTipoProcSelected(String tipoProcSelected) {
		this.tipoProcSelected = tipoProcSelected;
	}

	public String getOggettoDomanda() {
		return oggettoDomanda;
	}

	public void setOggettoDomanda(String oggettoDomanda) {
		this.oggettoDomanda = oggettoDomanda;
	}

	public String getOggettoDomandaDaCercare() {
		return oggettoDomandaDaCercare;
	}

	public void setOggettoDomandaDaCercare(String oggettoDomandaDaCercare) {
		this.oggettoDomandaDaCercare = oggettoDomandaDaCercare;
	}

	public List<TipoProcTempl> getTipiProcL() {
		return tipiProcL;
	}

	public void setTipiProcL(List<TipoProcTempl> tipiProcL) {
		this.tipiProcL = tipiProcL;
	}

	public XWFolderTitlesBean getFascicoli() {
		return fascicoli;
	}

	public void setFascicoli(XWFolderTitlesBean fascicoli) {
		this.fascicoli = fascicoli;
	}

	public int getActiveRowIndex() {
		return activeRowIndex;
	}

	public void setActiveRowIndex(int activeRowIndex) {
		this.activeRowIndex = activeRowIndex;
	}

	public Map<String, String> getCheckM() {
		return checkM;
	}

	public void setCheckM(Map<String, String> checkM) {
		this.checkM = checkM;
	}

	public String getFolderStatusSelected() {
		return folderStatusSelected;
	}

	public void setFolderStatusSelected(String folderStatusSelected) {
		this.folderStatusSelected = folderStatusSelected;
	}

	public List<String> getFolderStatusL() {
		return folderStatusL;
	}

	public void setFolderStatusL(List<String> folderStatusL) {
		this.folderStatusL = folderStatusL;
	}

	public String getSubStringtitolareDaCercare() {
		return subStringtitolareDaCercare;
	}

	public void setSubStringtitolareDaCercare(String subStringtitolareDaCercare) {
		this.subStringtitolareDaCercare = subStringtitolareDaCercare;
	}

	public String getDataCreazioneDa() {
		return dataCreazioneDa;
	}

	public void setDataCreazioneDa(String dataCreazioneDa) {
		this.dataCreazioneDa = dataCreazioneDa;
	}

	public String getDataCreazioneA() {
		return dataCreazioneA;
	}

	public void setDataCreazioneA(String dataCreazioneA) {
		this.dataCreazioneA = dataCreazioneA;
	}

	public String getDataInvioDomandaDa() {
		return dataInvioDomandaDa;
	}

	public void setDataInvioDomandaDa(String dataInvioDomandaDa) {
		this.dataInvioDomandaDa = dataInvioDomandaDa;
	}

	public String getDataInvioDomandaA() {
		return dataInvioDomandaA;
	}

	public void setDataInvioDomandaA(String dataInvioDomandaA) {
		this.dataInvioDomandaA = dataInvioDomandaA;
	}

	public String getDataConclusioneDa() {
		return dataConclusioneDa;
	}

	public void setDataConclusioneDa(String dataConclusioneDa) {
		this.dataConclusioneDa = dataConclusioneDa;
	}

	public String getDataConclusioneA() {
		return dataConclusioneA;
	}

	public void setDataConclusioneA(String dataConclusioneA) {
		this.dataConclusioneA = dataConclusioneA;
	}

	public String getSubStringDenominazioneDaCercare() {
		return subStringDenominazioneDaCercare;
	}

	public void setSubStringDenominazioneDaCercare(
			String subStringDenominazioneDaCercare) {
		this.subStringDenominazioneDaCercare = subStringDenominazioneDaCercare;
	}


	public String getSubStringTipoUdoDaCercare() {
		return subStringTipoUdoDaCercare;
	}

	public void setSubStringTipoUdoDaCercare(String subStringTipoUdoDaCercare) {
		this.subStringTipoUdoDaCercare = subStringTipoUdoDaCercare;
	}

	public String getSubStringBrancaDaCercare() {
		return subStringBrancaDaCercare;
	}

	public void setSubStringBrancaDaCercare(String subStringBrancaDaCercare) {
		this.subStringBrancaDaCercare = subStringBrancaDaCercare;
	}

	public String getSubStringDisciplinaDaCercare() {
		return subStringDisciplinaDaCercare;
	}

	public void setSubStringDisciplinaDaCercare(
			String subStringDisciplinaDaCercare) {
		this.subStringDisciplinaDaCercare = subStringDisciplinaDaCercare;
	}

	public String getSubStringSedeOperativaDaCercare() {
		return subStringSedeOperativaDaCercare;
	}

	public void setSubStringSedeOperativaDaCercare(
			String subStringSedeOperativaDaCercare) {
		this.subStringSedeOperativaDaCercare = subStringSedeOperativaDaCercare;
	}

	public String getSubStringUODaCercare() {
		return subStringUODaCercare;
	}

	public void setSubStringUODaCercare(String subStringUODaCercare) {
		this.subStringUODaCercare = subStringUODaCercare;
	}

	public String getSubStringCodiceUnivocoDaCercare() {
		return subStringCodiceUnivocoDaCercare;
	}

	public void setSubStringCodiceUnivocoDaCercare(
			String subStringCodiceUnivocoDaCercare) {
		this.subStringCodiceUnivocoDaCercare = subStringCodiceUnivocoDaCercare;
	}

	public String getSubStringDirettoreDaCercare() {
		return subStringDirettoreDaCercare;
	}

	public void setSubStringDirettoreDaCercare(
			String subStringDirettoreDaCercare) {
		this.subStringDirettoreDaCercare = subStringDirettoreDaCercare;
	}	
	
	public int getNumberUoSelected() {
		int toRet = 0;
		if(checkM != null) {
			for(Map.Entry<String, String> kv : checkM.entrySet()) {
				if(kv.getValue().isEmpty())
					toRet++;
			}
		}
		return toRet;
	}

	public int getNumberUdoSelected() {
		int toRet = 0;
		if(checkM != null) {
			for(Map.Entry<String, String> kv : checkM.entrySet()) {
				if(!kv.getValue().isEmpty())
					toRet++;
			}
		}
		return toRet;
	}
	
	public Map<String, Integer> getDomandeClientIdAssegnateAdUtenteCollaboratoreENumeroRisposteMancanti() {
		return domandeClientIdAssegnateAdUtenteCollaboratoreENumeroRisposteMancanti;
	}

	public void setDomandeClientIdAssegnateAdUtenteCollaboratoreENumeroRisposteMancanti(
			Map<String, Integer> domandeClientIdAssegnateAdUtenteCollaboratoreENumeroRisposteMancanti) {
		this.domandeClientIdAssegnateAdUtenteCollaboratoreENumeroRisposteMancanti = domandeClientIdAssegnateAdUtenteCollaboratoreENumeroRisposteMancanti;
	}

	public DomandaOrderTypeEnum getDomandaOrderTypeEnum() {
		return domandaOrderTypeEnum;
	}

	public void setDomandaOrderTypeEnum(DomandaOrderTypeEnum domandaOrderTypeEnum) {
		this.domandaOrderTypeEnum = domandaOrderTypeEnum;
	}

	public List<DirezioneTempl> getDirezioniTempl() {
		return direzioniTempl;
	}

	public void setDirezioniTempl(List<DirezioneTempl> direzioniTempl) {
		this.direzioniTempl = direzioniTempl;
	}

	public String getDirezioneTemplClientidSel() {
		return direzioneTemplClientidSel;
	}

	public void setDirezioneTemplClientidSel(String direzioneTemplClientidSel) {
		this.direzioneTemplClientidSel = direzioneTemplClientidSel;
	}

	public boolean isShowOnlyDomandeAssignedCongruenzaEsito() {
		return showOnlyDomandeAssignedCongruenzaEsito;
	}

	public void setShowOnlyDomandeAssignedCongruenzaEsito(
			boolean showOnlyDomandeAssignedCongruenzaEsito) {
		this.showOnlyDomandeAssignedCongruenzaEsito = showOnlyDomandeAssignedCongruenzaEsito;
	}

	public boolean isShowDomandeConTaskPerUtente() {
		return showDomandeConTaskPerUtente;
	}

	public void setShowDomandeConTaskPerUtente(boolean showDomandeConTaskPerUtente) {
		this.showDomandeConTaskPerUtente = showDomandeConTaskPerUtente;
	}

	public boolean isShowOnlyDomandeAssignedCongruenzaSenzaEsito() {
		return showOnlyDomandeAssignedCongruenzaSenzaEsito;
	}

	public void setShowOnlyDomandeAssignedCongruenzaSenzaEsito(boolean showOnlyDomandeAssignedCongruenzaSenzaEsito) {
		this.showOnlyDomandeAssignedCongruenzaSenzaEsito = showOnlyDomandeAssignedCongruenzaSenzaEsito;
	}

	public DomandaOrderTypeEnum nextDomandaOrderTypeEnumForField(DomandaFieldToOrderEnum domandaFieldToOrderEnum) {
		switch (domandaFieldToOrderEnum) {
			case NumeroProcedimento:
				if(domandaOrderTypeEnum != null && domandaOrderTypeEnum==DomandaOrderTypeEnum.NumeroProcedimentoCrescente)
					return DomandaOrderTypeEnum.NumeroProcedimentoDecrescente;
				else
					return DomandaOrderTypeEnum.NumeroProcedimentoCrescente;
			case Oggetto:
				if(domandaOrderTypeEnum != null && domandaOrderTypeEnum==DomandaOrderTypeEnum.OggettoCrescente)
					return DomandaOrderTypeEnum.OggettoDecrescente;
				else
					return DomandaOrderTypeEnum.OggettoCrescente;
			case TipoDomanda:
				if(domandaOrderTypeEnum != null && domandaOrderTypeEnum==DomandaOrderTypeEnum.TipoDomandaCrescente)
					return DomandaOrderTypeEnum.TipoDomandaDecrescente;
				else
					return DomandaOrderTypeEnum.TipoDomandaCrescente;
			case DataCreazione:
				if(domandaOrderTypeEnum != null && domandaOrderTypeEnum==DomandaOrderTypeEnum.DataCreazioneCrescente)
					return DomandaOrderTypeEnum.DataCreazioneDecrescente;
				else
					return DomandaOrderTypeEnum.DataCreazioneCrescente;
			case DataConclusione:
				if(domandaOrderTypeEnum != null && domandaOrderTypeEnum==DomandaOrderTypeEnum.DataConclusioneCrescente)
					return DomandaOrderTypeEnum.DataConclusioneDecrescente;
				else
					return DomandaOrderTypeEnum.DataConclusioneCrescente;
			case DataConferimentoIncarico:
				if(domandaOrderTypeEnum != null && domandaOrderTypeEnum==DomandaOrderTypeEnum.DataConferimentoIncaricoCrescente)
					return DomandaOrderTypeEnum.DataConferimentoIncaricoDecrescente;
				else
					return DomandaOrderTypeEnum.DataConferimentoIncaricoCrescente;
			case DataInvio:
				if(domandaOrderTypeEnum != null && domandaOrderTypeEnum==DomandaOrderTypeEnum.DataInvioCrescente)
					return DomandaOrderTypeEnum.DataInvioDecrescente;
				else
					return DomandaOrderTypeEnum.DataInvioCrescente;
			case DataPresentazioneProvvedimento:
				if(domandaOrderTypeEnum != null && domandaOrderTypeEnum==DomandaOrderTypeEnum.DataPresentazioneProvvedimentoCrescente)
					return DomandaOrderTypeEnum.DataPresentazioneProvvedimentoDecrescente;
				else
					return DomandaOrderTypeEnum.DataPresentazioneProvvedimentoCrescente;
			case DataRedazioneRapportoVerifica:
				if(domandaOrderTypeEnum != null && domandaOrderTypeEnum==DomandaOrderTypeEnum.DataRedazioneRapportoVerificaCrescente)
					return DomandaOrderTypeEnum.DataRedazioneRapportoVerificaDecrescente;
				else
					return DomandaOrderTypeEnum.DataRedazioneRapportoVerificaCrescente;
			case DataValutazioneCollegiale:
				if(domandaOrderTypeEnum != null && domandaOrderTypeEnum==DomandaOrderTypeEnum.DataValutazioneCollegialeCrescente)
					return DomandaOrderTypeEnum.DataValutazioneCollegialeDecrescente;
				else
					return DomandaOrderTypeEnum.DataValutazioneCollegialeCrescente;
			case DataValutazioneCompletezzaCorrettezza:
				if(domandaOrderTypeEnum != null && domandaOrderTypeEnum==DomandaOrderTypeEnum.DataValutazioneCompletezzaCorrettezzaCrescente)
					return DomandaOrderTypeEnum.DataValutazioneCompletezzaCorrettezzaDecrescente;
				else
					return DomandaOrderTypeEnum.DataValutazioneCompletezzaCorrettezzaCrescente;

			case DataValutazioneRispondenzaProgrammazione:
				if(domandaOrderTypeEnum != null && domandaOrderTypeEnum==DomandaOrderTypeEnum.DataValutazioneRispondenzaProgrammazioneCrescente)
					return DomandaOrderTypeEnum.DataValutazioneRispondenzaProgrammazioneDecrescente;
				else
					return DomandaOrderTypeEnum.DataValutazioneRispondenzaProgrammazioneCrescente;
			case Stato:
				if(domandaOrderTypeEnum != null && domandaOrderTypeEnum==DomandaOrderTypeEnum.StatoCrescente)
					return DomandaOrderTypeEnum.StatoDecrescente;
				else
					return DomandaOrderTypeEnum.StatoCrescente;
			case Titolare:
				if(domandaOrderTypeEnum != null && domandaOrderTypeEnum==DomandaOrderTypeEnum.TitolareCrescente)
					return DomandaOrderTypeEnum.TitolareDecrescente;
				else
					return DomandaOrderTypeEnum.TitolareCrescente;
			
			
			default:
				break;
		}
		return null;
	}

	//Da utilizzare solo per utente COLLABORATORE_VALUTAZIONE
	public boolean valutazioneCompletata(Map<String,String> procedura) {
		if(numeroRequisitiSenzaRisposta(procedura)>0)
			return false;
		
		return true;
	}

	//Da utilizzare solo per utente COLLABORATORE_VALUTAZIONE
	public int numeroRequisitiSenzaRisposta(Map<String,String> procedura) {
		return domandeClientIdAssegnateAdUtenteCollaboratoreENumeroRisposteMancanti.get(procedura.get("domanda_client_id"));
	}

	public String getSubStringCodiceExUlssDaCercare() {
		return subStringCodiceExUlssDaCercare;
	}

	public void setSubStringCodiceExUlssDaCercare(String subStringCodiceExUlssDaCercare) {
		this.subStringCodiceExUlssDaCercare = subStringCodiceExUlssDaCercare;
	}

	public String getSubStringCodiceUlssDaCercare() {
		return subStringCodiceUlssDaCercare;
	}

	public void setSubStringCodiceUlssDaCercare(String subStringCodiceUlssDaCercare) {
		this.subStringCodiceUlssDaCercare = subStringCodiceUlssDaCercare;
	}

	public AreaDiscipline getAreaDisciplineDaCercare() {
		return areaDisciplineDaCercare;
	}

	public void setAreaDisciplineDaCercare(AreaDiscipline areaDisciplineDaCercare) {
		this.areaDisciplineDaCercare = areaDisciplineDaCercare;
	}

}
