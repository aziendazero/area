package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import it.tredi.auac.TipoPostiLettoEnum;

public class BrancaUdoInstInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String codice;
	private String descr;
	private String sede;
	private BigDecimal ordine;
	private TipoPostiLettoEnum tipoPostiLetto;
	
	public BrancaUdoInstInfo() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getSede() {
		return sede;
	}

	public void setSede(String sede) {
		this.sede = sede;
	}

	public BigDecimal getOrdine() {
		return ordine;
	}

	public void setOrdine(BigDecimal ordine) {
		this.ordine = ordine;
	}

	public TipoPostiLettoEnum getTipoPostiLetto() {
		return tipoPostiLetto;
	}

	public void setTipoPostiLetto(TipoPostiLettoEnum tipoPostiLetto) {
		this.tipoPostiLetto = tipoPostiLetto;
	}

}