package it.tredi.auac.bean;

import it.tredi.auac.PostItTypeEnum;

import java.io.Serializable;

public class PostIt implements Serializable {
	public final static String STATO_DA_CONTROLLARE = "Da controllare";
	public final static String STATO_CONTROLLATO = "Controllato";

	private static final long serialVersionUID = 8037370355591723022L;

	private int xmlIndex;
	private PostItTypeEnum tipo;
	private String testo;
	private String operatore;
	private String ruolo;
	private String dataOraForCompare;
	private String data;
	private String ora;
	private boolean isDeletable = false;
	private boolean isAdmin = false;
	private String stato;
	
	public PostIt() {
	}

	public int getXmlIndex() {
		return xmlIndex;
	}

	public void setXmlIndex(int xmlIndex) {
		this.xmlIndex = xmlIndex;
	}

	public PostItTypeEnum getTipo() {
		return tipo;
	}

	public void setTipo(PostItTypeEnum tipo) {
		this.tipo = tipo;
	}

	public String getOperatore() {
		return operatore;
	}

	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}

	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getOra() {
		return ora;
	}

	public void setOra(String ora) {
		this.ora = ora;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public boolean isDeletable() {
		return isDeletable;
	}

	public void setDeletable(boolean isDeletable) {
		this.isDeletable = isDeletable;
	}

	public String getDataOraForCompare() {
		return dataOraForCompare;
	}

	public void setDataOraForCompare(String dataOraForCompare) {
		this.dataOraForCompare = dataOraForCompare;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

}
