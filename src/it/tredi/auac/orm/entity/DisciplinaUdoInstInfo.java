package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

import it.tredi.auac.TipoPostiLettoEnum;


public class DisciplinaUdoInstInfo implements Serializable{
	private static final long serialVersionUID = 1L;

	private String id;
	private String tipo;
	private String codice;
	private String descr;
	private String area;
	private String sede;
	
	@SerializedName(value="pl")
	private BigDecimal postiLetto;

	@SerializedName(value="plex")
	private BigDecimal postiLettoExtra;

	@SerializedName(value="plob")
	private BigDecimal postiLettoObi;

	@SerializedName(value="placc")
	private BigDecimal postiLettoAcc;

	@SerializedName(value="plobacc")
	private BigDecimal postiLettoObiAcc;

	@SerializedName(value="ordArea")
	private BigDecimal ordineArea;
	
	@SerializedName(value="ordDisc")
	private BigDecimal ordineDisc;
	
	@SerializedName(value="tipoPl")
	private TipoPostiLettoEnum tipoPostiLetto;
	
	public DisciplinaUdoInstInfo() {
	}
	
	public DisciplinaUdoInstInfo(DisciplinaUdoInstInfo disc) {
		this.id = disc.id;
		this.codice = disc.codice;
		this.tipo = disc.tipo;
		this.descr = disc.descr;
		this.area = disc.area;
		this.sede = disc.sede;
		this.postiLetto = disc.postiLetto;
		this.postiLettoAcc = disc.postiLettoAcc;
		this.postiLettoExtra = disc.postiLettoExtra;
		this.postiLettoObi = disc.postiLettoObi;
		this.postiLettoObiAcc = disc.postiLettoObiAcc;
		this.ordineArea = disc.ordineArea;
		this.ordineDisc = disc.ordineDisc;
		this.tipoPostiLetto = disc.tipoPostiLetto;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCodice() {
		return this.codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public BigDecimal getPostiLetto() {
		return this.postiLetto;
	}

	public void setPostiLetto(BigDecimal postiLetto) {
		this.postiLetto = postiLetto;
	}

	public BigDecimal getPostiLettoExtra() {
		return postiLettoExtra;
	}

	public void setPostiLettoExtra(BigDecimal postiLettoExtra) {
		this.postiLettoExtra = postiLettoExtra;
	}

	public BigDecimal getPostiLettoObi() {
		return postiLettoObi;
	}

	public void setPostiLettoObi(BigDecimal postiLettoObi) {
		this.postiLettoObi = postiLettoObi;
	}

	public BigDecimal getPostiLettoAcc() {
		return postiLettoAcc;
	}

	public void setPostiLettoAcc(BigDecimal postiLettoAcc) {
		this.postiLettoAcc = postiLettoAcc;
	}

	public BigDecimal getPostiLettoObiAcc() {
		return postiLettoObiAcc;
	}

	public void setPostiLettoObiAcc(BigDecimal postiLettoObiAcc) {
		this.postiLettoObiAcc = postiLettoObiAcc;
	}
	
	public boolean isDisciplina() {
		return tipo == null || tipo.isEmpty() || tipo.equals("d");
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public BigDecimal getOrdineArea() {
		return ordineArea;
	}

	public void setOrdineArea(BigDecimal ordineArea) {
		this.ordineArea = ordineArea;
	}

	public BigDecimal getOrdineDisc() {
		return ordineDisc;
	}

	public void setOrdineDisc(BigDecimal ordineDisc) {
		this.ordineDisc = ordineDisc;
	}

	public TipoPostiLettoEnum getTipoPostiLetto() {
		return tipoPostiLetto;
	}

	public void setTipoPostiLetto(TipoPostiLettoEnum tipoPostiLetto) {
		this.tipoPostiLetto = tipoPostiLetto;
	}

	public String getSede() {
		return sede;
	}

	public void setSede(String sede) {
		this.sede = sede;
	}

	public String getCodiceDescr() {
		if(this.isDisciplina()) {
			return this.getCodice() + " - "  + this.getDescr();
		} else {
			return this.getDescr();
		}
	}
	
	public String getFullDescr() {
		String val = "";
		if(this.isDisciplina()) {
			val = this.getArea();
			val += " " + this.getCodice() + " - "  + this.getDescr();
		} else {
			val = this.getDescr();
		}
		if(this.getPostiLetto() != null)
			val += ", posti letto AU: " + this.getPostiLetto();
		if(this.getPostiLettoAcc() != null)
			val += ", posti letto AC: " + this.getPostiLettoAcc();
		if(this.getPostiLettoExtra() != null)
			val += ", posti letto EX: " + this.getPostiLettoExtra();
		if(this.getPostiLettoObi() != null)
			val += ", posti tecnici OBI: " + this.getPostiLettoObi();
		if(this.getTipoPostiLetto() != null)
			val += ", tipo PL: " + this.getTipoPostiLetto();
		return val;
	}

}