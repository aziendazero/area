package it.tredi.auac.orm.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the COMUNI_TEMPL database table.
 * 
 */
@Entity
@Table(name="V_COMUNI_PROVINCE")
public class Comune implements Serializable {
	private static final long serialVersionUID = 1L;

	//COD_COMUNE_ESTESO
	//COD_REGIONE,COD_PROVINCIA,COD_COMUNE,DESCRIZIONE,CAP,COD_CATASTO
	//DESCRIZIONE_PROVINCIA SIGLA_PROVINCIA
	
	@Id
	@Column(name="COD_COMUNE_ESTESO")
	private String codComuneEsteso;

	@Column(name="COD_REGIONE")
	private String codRegione;

	@Column(name="COD_PROVINCIA")
	private String codProvincia;

	@Column(name="COD_COMUNE")
	private String codComune;
	
	private String descrizione;
	
	private String cap;

	@Column(name="COD_CATASTO")
	private String codCatasto;
	
	@Column(name="DESCRIZIONE_PROVINCIA")
	private String descrizioneProvincia;

	@Column(name="SIGLA_PROVINCIA")
	private String siglaProvincia;

	public Comune() {
	}

	public String getCodComuneEsteso() {
		return codComuneEsteso;
	}

	public void setCodComuneEsteso(String codComuneEsteso) {
		this.codComuneEsteso = codComuneEsteso;
	}

	public String getCodRegione() {
		return codRegione;
	}

	public void setCodRegione(String codRegione) {
		this.codRegione = codRegione;
	}

	public String getCodProvincia() {
		return codProvincia;
	}

	public void setCodProvincia(String codProvincia) {
		this.codProvincia = codProvincia;
	}

	public String getCodComune() {
		return codComune;
	}

	public void setCodComune(String codComune) {
		this.codComune = codComune;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getCodCatasto() {
		return codCatasto;
	}

	public void setCodCatasto(String codCatasto) {
		this.codCatasto = codCatasto;
	}

	public String getDescrizioneProvincia() {
		return descrizioneProvincia;
	}

	public void setDescrizioneProvincia(String descrizioneProvincia) {
		this.descrizioneProvincia = descrizioneProvincia;
	}

	public String getSiglaProvincia() {
		return siglaProvincia;
	}

	public void setSiglaProvincia(String siglaProvincia) {
		this.siglaProvincia = siglaProvincia;
	}

}