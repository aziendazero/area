package it.tredi.auac.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;

import it.tredi.auac.TipoValutazioneVerificaEnum;
import it.tredi.auac.ValutazioneTipoSiNoEnum;
import it.tredi.auac.ValutazioneVerificatoreTipoSogliaEnum;
import it.tredi.auac.orm.entity.RequisitoInst;
import it.tredi.auac.utils.AuacUtils;

public class AreaReportInfoVerificaBean implements Serializable, IAreaInfo {
	private static final long serialVersionUID = 1042217635040098595L;
	
	private boolean containReqSiNo = false;
	private boolean containReqSoglia = false;
	
	private String nome;
	private int totSi = 0;
	private int totNo = 0;
	
	BigDecimal sommaSoglia = new BigDecimal(0);
	int numeroSoglia = 0;	
	
	public AreaReportInfoVerificaBean(String nome) {
		this.nome = nome;
	}

	public void addRequisito(RequisitoInst req) throws ParseException {
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
					ValutazioneTipoSiNoEnum valutazione = ValutazioneTipoSiNoEnum.getEnumByKey(req.getValutazioneVerificatore());
					if(valutazione == ValutazioneTipoSiNoEnum.Si) {
						totSi++;
					} else if(valutazione == ValutazioneTipoSiNoEnum.No) {
						totNo++;
					}
				}
			}
		}

	}

	public boolean isContainReqSiNo() {
		return containReqSiNo;
	}

	public boolean isContainReqSoglia() {
		return containReqSoglia;
	}

	public String getNome() {
		return nome;
	}

	public int getTotSi() {
		return totSi;
	}

	public int getTotNo() {
		return totNo;
	}

	public BigDecimal getMediaSoglia() {
		if(numeroSoglia != 0)
			return sommaSoglia.divide(new BigDecimal(numeroSoglia), 2, RoundingMode.HALF_UP);
		return null;
	}
}
