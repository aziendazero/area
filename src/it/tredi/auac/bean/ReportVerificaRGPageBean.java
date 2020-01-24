package it.tredi.auac.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.List;

import it.tredi.auac.TipoValutazioneVerificaEnum;
import it.tredi.auac.ValutazioneVerificatoreTipoSiNoEnum;
import it.tredi.auac.ValutazioneVerificatoreTipoSogliaEnum;
import it.tredi.auac.orm.entity.RequisitoInst;
import it.tredi.auac.utils.AuacUtils;

public class ReportVerificaRGPageBean implements Serializable {
	private static final long serialVersionUID = 2700884567597815958L;

	private List<RequisitoInst> requisitoInstL;
	private boolean containReqSiNo = false;
	private boolean containReqSoglia = false;
	private int totSi = 0;
	private int totNo = 0;
	private BigDecimal mediaSoglia = null;
	
	public ReportVerificaRGPageBean(List<RequisitoInst> requisiti) throws ParseException {
		requisitoInstL = requisiti;
		BigDecimal sommaSoglia = new BigDecimal(0);
		int numeroSoglia = 0;
		
		//calcolo il totale e le varie info
		ValutazioneVerificatoreTipoSiNoEnum valutazione;
		for(RequisitoInst req : requisiti) {
			if(AuacUtils.requisitoIsSoglia(req)) {
				containReqSoglia = true;
				if(req.getValutazioneVerificatore() != null && ValutazioneVerificatoreTipoSogliaEnum.valoriValidiPerMedia().contains(req.getValutazioneVerificatore())) {
					sommaSoglia = sommaSoglia.add(AuacUtils.convertValutazioneVerificaToBigDecimal(req.getValutazioneVerificatore()));
					numeroSoglia++;
				}
			} else if (AuacUtils.requisitoIsSiNo(req)) {
				if(req.getTipoVerifica() == TipoValutazioneVerificaEnum.AUTOMATICA) {
					containReqSiNo = true;
					if(req.getValutazioneVerificatore() != null) {
						SiNoMediaBean siNoMediaBean = new SiNoMediaBean(req.getValutazioneVerificatore());
						totSi += siNoMediaBean.getNumeroSi();
						totNo += siNoMediaBean.getNumeroNo();
					}
				} else {//MANUALE SEMIAUTOMATICA
					containReqSiNo = true;
					if(req.getValutazioneVerificatore() != null) {
						valutazione = ValutazioneVerificatoreTipoSiNoEnum.getEnumByKey(req.getValutazioneVerificatore());
						if(valutazione == ValutazioneVerificatoreTipoSiNoEnum.Si) {
							totSi++;
						} else if(valutazione == ValutazioneVerificatoreTipoSiNoEnum.No) {
							totNo++;
						}
					}
				}
			}
		}
		
		if(numeroSoglia != 0)
			mediaSoglia = sommaSoglia.divide(new BigDecimal(numeroSoglia), 2, RoundingMode.HALF_UP);
	}
	
	public List<RequisitoInst> getRequisitoInstL() {
		return requisitoInstL;
	}

	public void setRequisitoInstL(List<RequisitoInst> requisitoInstL) {
		this.requisitoInstL = requisitoInstL;
	}

	public boolean isContainReqSiNo() {
		return containReqSiNo;
	}

	public void setContainReqSiNo(boolean containReqSiNo) {
		this.containReqSiNo = containReqSiNo;
	}

	public boolean isContainReqSoglia() {
		return containReqSoglia;
	}

	public void setContainReqSoglia(boolean containReqSoglia) {
		this.containReqSoglia = containReqSoglia;
	}

	public int getTotSi() {
		return totSi;
	}

	public void setTotSi(int totSi) {
		this.totSi = totSi;
	}

	public int getTotNo() {
		return totNo;
	}

	public void setTotNo(int totNo) {
		this.totNo = totNo;
	}

	public BigDecimal getMediaSoglia() {
		return mediaSoglia;
	}

	public void setMediaSoglia(BigDecimal mediaSoglia) {
		this.mediaSoglia = mediaSoglia;
	}


}
