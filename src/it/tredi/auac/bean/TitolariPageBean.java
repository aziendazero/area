package it.tredi.auac.bean;


import it.tredi.auac.TitolareFieldToOrderEnum;
import it.tredi.auac.TitolareOrderTypeEnum;
import it.tredi.auac.orm.entity.DirezioneTempl;
import it.tredi.auac.orm.entity.TitolareModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TitolariPageBean implements Serializable {	
	private static final long serialVersionUID = -4192947785980233045L;

	private List<String> messages;

	private List<TitolareModel> titolari;
	private List<DirezioneTempl> direzioniTempl;

	private TitolareOrderTypeEnum titolareOrderTypeEnum = TitolareOrderTypeEnum.RagioneSocialeCrescente;

	private String subStringtitolareDaCercare;
	private String direzioneTemplClientidSel;
	private String subStringPartitaIva;
	private int numRecordsForPage = 10;

	private int currentPage = 1;
	private int currentTitolareIndex = -1;
		
	public TitolariPageBean(int numRecordsForPage) {
		currentPage = 1;
		currentTitolareIndex = -1;
		this.numRecordsForPage = numRecordsForPage;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public TitolareModel getCurrentTitolare() {
		if(this.currentTitolareIndex == -1)
			return null;
		return titolari.get(this.currentTitolareIndex - 1);
	}

	public List<TitolareModel> getTitolari() {
		return titolari;
	}

	public void setTitolari(List<TitolareModel> titolari) {
		currentPage = 1;
		currentTitolareIndex = -1;
		this.titolari = titolari;
	}

	public String getSubStringtitolareDaCercare() {
		return subStringtitolareDaCercare;
	}

	public void setSubStringtitolareDaCercare(String subStringtitolareDaCercare) {
		this.subStringtitolareDaCercare = subStringtitolareDaCercare;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	public boolean isCanFirst() {
		return currentPage > 1;
	}
	
	public boolean isCanPrev() {
		return currentPage > 1;
	}

	public boolean isCanNext() {
		return currentPage < getTotalPage();
	}

	public boolean isCanLast() {
		//ho dei titolari con almeno 2 pagine, non sono nell'ultima pagina
		return currentPage < getTotalPage();
	}
	
	public boolean isTitolareCanFirst() {
		return currentTitolareIndex > 1;
	}
	
	public boolean isTitolareCanPrev() {
		return currentTitolareIndex > 1;
	}

	public boolean isTitolareCanNext() {
		return currentTitolareIndex < getTotalRecords();
	}

	public boolean isTitolareCanLast() {
		return currentTitolareIndex < getTotalRecords();
	}
	
	public void gotoFirstTitolare() {
		if(isTitolareCanFirst()) {
			this.currentTitolareIndex = 1;
		}
	}

	public void gotoPrevTitolare() {
		if(isTitolareCanPrev()) {
			this.currentTitolareIndex--;
		}
	}

	public void gotoNextTitolare() {
		if(isTitolareCanNext()) {
			this.currentTitolareIndex++;
		}
	}

	public void gotoLastTitolare() {
		if(isTitolareCanLast()) {
			this.currentTitolareIndex = getTotalRecords();
		}
	}

	public int getTotalPage() {
		if (getTotalRecords() % numRecordsForPage == 0)
			return getTotalRecords()/numRecordsForPage;
		else
			return (getTotalRecords()/numRecordsForPage) + 1;
	}
	
	public int getTotalRecords() {
		if(titolari == null)
			return 0;
		return titolari.size();
	}

	public List<TitolareModel> getTitolariForCurrentPage() {
		List<TitolareModel> titolarForPage = new ArrayList<TitolareModel>();
		int firstInPage = (currentPage - 1) * numRecordsForPage;
		int lastInPage = firstInPage + numRecordsForPage - 1;
		if(lastInPage >= titolari.size())
			lastInPage = titolari.size() - 1;
		for(int i = firstInPage; i <= lastInPage; i++) 
			titolarForPage.add(titolari.get(i));
		return titolarForPage;
	}

	public void setCurrentTitolareByIndexOnCurrentPage(int indexOnCurrentPage) {
		this.currentTitolareIndex = (currentPage  - 1) * numRecordsForPage + indexOnCurrentPage + 1;
	}

	public int getCurrentTitolareIndex() {
		return currentTitolareIndex;
	}

	public String getSubStringPartitaIva() {
		return subStringPartitaIva;
	}

	public void setSubStringPartitaIva(String subStringPartitaIva) {
		this.subStringPartitaIva = subStringPartitaIva;
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

	public TitolareOrderTypeEnum getTitolareOrderTypeEnum() {
		return titolareOrderTypeEnum;
	}

	public void setTitolareOrderTypeEnum(TitolareOrderTypeEnum titolareOrderTypeEnum) {
		this.titolareOrderTypeEnum = titolareOrderTypeEnum;
	}
	
	public TitolareOrderTypeEnum nextDomandaOrderTypeEnumForField(TitolareFieldToOrderEnum titolareFieldToOrderEnum) {
		switch (titolareFieldToOrderEnum) {
			case RagioneSociale:
				if(titolareOrderTypeEnum != null && titolareOrderTypeEnum==TitolareOrderTypeEnum.RagioneSocialeCrescente)
					return TitolareOrderTypeEnum.RagioneSocialeDecrescente;
				else
					return TitolareOrderTypeEnum.RagioneSocialeCrescente;
			case PartitaIva:
				if(titolareOrderTypeEnum != null && titolareOrderTypeEnum==TitolareOrderTypeEnum.PartitaIvaCrescente)
					return TitolareOrderTypeEnum.PartitaIvaDecrescente;
				else
					return TitolareOrderTypeEnum.PartitaIvaCrescente;
			case Direzione:
				if(titolareOrderTypeEnum != null && titolareOrderTypeEnum==TitolareOrderTypeEnum.DirezioneCrescente)
					return TitolareOrderTypeEnum.DirezioneDecrescente;
				else
					return TitolareOrderTypeEnum.DirezioneCrescente;
			default:
				break;
		}
		return null;
	}
}
