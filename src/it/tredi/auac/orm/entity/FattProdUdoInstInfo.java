package it.tredi.auac.orm.entity;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class FattProdUdoInstInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	public final static String POSTI_LETTO = "POSTI_LETTO";
	public final static String POSTI_LETTO_EXTRA = "POSTI_LETTO_EXTRA";
	public final static String POSTI_LETTO_OBI = "POSTI_LETTO_OBI";
	public final static String SALE_OPERATORIE = "SALE_OPERATORIE";
	public final static String POSTI_PAGANTI = "POSTI_PAGANTI";
	
	private String id;
	private String tipo;
	@SerializedName(value="tfp")
	private String tipologiaFattProd;
	@SerializedName(value="val")
	private String valore;
	@SerializedName(value="val2")
	private String valore2;
	@SerializedName(value="val3")
	private String valore3;

	public FattProdUdoInstInfo() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTipologiaFattProd() {
		return tipologiaFattProd;
	}

	public void setTipologiaFattProd(String tipologiaFattProd) {
		this.tipologiaFattProd = tipologiaFattProd;
	}

	public String getValore() {
		return this.valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

	public String getValore2() {
		return this.valore2;
	}

	public void setValore2(String valore2) {
		this.valore2 = valore2;
	}

	public String getValore3() {
		return valore3;
	}

	public void setValore3(String valore3) {
		this.valore3 = valore3;
	}
	
	public String getFullDescr() {
		String val = "";
		if(this.getTipologiaFattProd() == null || FattProdUdoInstInfo.POSTI_LETTO.equals(this.getTipologiaFattProd())) {
			val += "Tipo: " + ((this.getTipo()==null) ? "" : this.getTipo() ) + ", Accreditati: " + (this.getValore2()==null ? "" : this.getValore2()) + ", Autorizzati: " + (this.getValore()==null ? "" : this.getValore());
		} else if (FattProdUdoInstInfo.POSTI_LETTO_EXTRA.equals(this.getTipologiaFattProd())) {
			val += "Tipo: " + ((this.getTipo()==null) ? "" : this.getTipo() ) + ", Accreditati: " + (this.getValore2()==null ? "" : this.getValore2());
		} else if (FattProdUdoInstInfo.POSTI_LETTO_OBI.equals(this.getTipologiaFattProd())) {
			val += "Tipo: " + ((this.getTipo()==null) ? "" : this.getTipo() ) + ", Accreditati: " + (this.getValore2()==null ? "" : this.getValore2()) + ", Autorizzati: " + (this.getValore()==null ? "" : this.getValore());
		} else if (FattProdUdoInstInfo.POSTI_PAGANTI.equals(this.getTipologiaFattProd())) {
			val += "Tipo: " + ((this.getTipo()==null) ? "" : this.getTipo() ) + ": " + (this.getValore()==null ? "" : this.getValore());
		} else if (FattProdUdoInstInfo.SALE_OPERATORIE.equals(this.getTipologiaFattProd())) {
			val += "Tipo: " + ((this.getTipo()==null) ? "" : this.getTipo() ) + ", Numero: " + (this.getValore()==null ? "" : this.getValore()) + ", Ore: " + (this.getValore2()==null ? "" : this.getValore2()) + ", Descrizione: " + (this.getValore3()==null ? "" : this.getValore3());
		}
		return val;
	}
}