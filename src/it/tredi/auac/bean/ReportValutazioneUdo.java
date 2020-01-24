package it.tredi.auac.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class ReportValutazioneUdo implements Serializable {

	private static final long serialVersionUID = 2700884567597815958L;
	//"SELECT RI.ID_UDO_FK, UDOI.ID_UNIVOCO, UDOI.DESCR, FORMATTA_DECIMAL_TO_STR(AVG(RI.VALUTAZIONE)) FROM REQUISITO_INST RI" +
	private String idUdoFk;
	private String idUnivoco;
	private String descr;
	private BigDecimal media;

	public ReportValutazioneUdo(String idUdoFk, String idUnivoco, String descr, BigDecimal media) {
		this.idUdoFk = idUdoFk;
		this.idUnivoco = idUnivoco;
		this.descr = descr;
		this.media = media;
	}

	public String getIdUdoFk() {
		return idUdoFk;
	}

	public void setIdUdoFk(String idUdoFk) {
		this.idUdoFk = idUdoFk;
	}

	public String getIdUnivoco() {
		return idUnivoco;
	}

	public void setIdUnivoco(String idUnivoco) {
		this.idUnivoco = idUnivoco;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public BigDecimal getMedia() {
		return media;
	}

	public void setMedia(BigDecimal media) {
		this.media = media;
	}
	
}
