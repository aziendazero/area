package it.tredi.auac.bean;

import it.tredi.auac.orm.entity.DisciplinaUdoInstInfo;
import it.tredi.auac.orm.entity.FattProdUdoInstInfo;

import java.io.Serializable;
import java.math.BigDecimal;

public class PostiLettoEntity implements Serializable {

	private static final long serialVersionUID = 18994960112215983L;
	private BigDecimal postiLettoTotaliAutorizzati;
	private BigDecimal postiLettoTotaliAccreditati;

	public PostiLettoEntity() {
		this.postiLettoTotaliAutorizzati = new BigDecimal(0);
		this.postiLettoTotaliAccreditati = new BigDecimal(0);
	}
	
	public PostiLettoEntity(DisciplinaUdoInstInfo disciplinaUdoInstInfo) {
		if(disciplinaUdoInstInfo.getPostiLetto() != null)
			this.postiLettoTotaliAutorizzati = disciplinaUdoInstInfo.getPostiLetto();
		else
			this.postiLettoTotaliAutorizzati = new BigDecimal(0);
		if(disciplinaUdoInstInfo.getPostiLettoAcc() != null)
			this.postiLettoTotaliAccreditati = disciplinaUdoInstInfo.getPostiLettoAcc();
		else
			this.postiLettoTotaliAccreditati = new BigDecimal(0);
	}

	public PostiLettoEntity(FattProdUdoInstInfo fattProdUdoInstInfo) {
		if(fattProdUdoInstInfo.getTipologiaFattProd() == null || FattProdUdoInstInfo.POSTI_LETTO.equals(fattProdUdoInstInfo.getTipologiaFattProd())) {
			int pl = 0;
			try {
				pl = Integer.parseInt(fattProdUdoInstInfo.getValore2());
			} catch(Exception e) {
				pl = 0;
			}
			this.postiLettoTotaliAccreditati = new BigDecimal(pl);
			pl = 0;
			try {
				pl = Integer.parseInt(fattProdUdoInstInfo.getValore());
			} catch(Exception e) {
				pl = 0;
			}
			this.postiLettoTotaliAutorizzati = new BigDecimal(pl);
		}
	}
	
	public void addPostiLetto(DisciplinaUdoInstInfo disciplinaUdoInstInfo) {
		if(disciplinaUdoInstInfo.getPostiLetto() != null)
			this.postiLettoTotaliAutorizzati = this.postiLettoTotaliAutorizzati.add(disciplinaUdoInstInfo.getPostiLetto());
		if(disciplinaUdoInstInfo.getPostiLettoAcc() != null)
			this.postiLettoTotaliAccreditati = this.postiLettoTotaliAccreditati.add(disciplinaUdoInstInfo.getPostiLettoAcc());
	}

	public void addPostiLetto(FattProdUdoInstInfo fattProdUdoInstInfo) {
		if(fattProdUdoInstInfo.getTipologiaFattProd() == null || FattProdUdoInstInfo.POSTI_LETTO.equals(fattProdUdoInstInfo.getTipologiaFattProd())) {
			int pl = 0;
			try {
				pl = Integer.parseInt(fattProdUdoInstInfo.getValore2());
			} catch(Exception e) {
				pl = 0;
			}
			this.postiLettoTotaliAccreditati = this.postiLettoTotaliAccreditati.add(new BigDecimal(pl));
			pl = 0;
			try {
				pl = Integer.parseInt(fattProdUdoInstInfo.getValore());
			} catch(Exception e) {
				pl = 0;
			}
			this.postiLettoTotaliAutorizzati = this.postiLettoTotaliAutorizzati.add(new BigDecimal(pl));
		}
	}

	public BigDecimal getPostiLettoTotaliAutorizzati() {
		return postiLettoTotaliAutorizzati;
	}

	public void setPostiLettoTotaliAutorizzati(
			BigDecimal postiLettoTotaliAutorizzati) {
		this.postiLettoTotaliAutorizzati = postiLettoTotaliAutorizzati;
	}

	public BigDecimal getPostiLettoTotaliAccreditati() {
		return postiLettoTotaliAccreditati;
	}

	public void setPostiLettoTotaliAccreditati(
			BigDecimal postiLettoTotaliAccreditati) {
		this.postiLettoTotaliAccreditati = postiLettoTotaliAccreditati;
	}
}
