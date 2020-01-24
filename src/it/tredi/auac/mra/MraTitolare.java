package it.tredi.auac.mra;

import java.io.Serializable;
import java.util.Date;

public class MraTitolare implements Serializable {
	private static final long serialVersionUID = 1L;

	private String codiceRegione;
	private String codiceTitolare;
	private Date dataInizioValidita;

	private String denominazione;
	private String natura;
	private String tipologiaTitolare;

	//Indirizzo Sede Legale
	private String toponimoStradale;
	private String viaPiazza;
	private String civico;
	private String comune;
	private String provincia;
	
	private String partitaIva;
	private String codiceFiscale;

	private String telefono;
	private String fax;

	private String email;
	private String pec;
	private String url;
	
	public MraTitolare() {
	}

	//	public MraTitolare(TitolareModel titolare, String codiceRegione) {
//		this.codiceRegione = codiceRegione;
//	}

	public String getIndirizzoSedeLegale() {
		boolean write = false;
		String toRet = "";
		if(this.getToponimoStradale() != null && !this.getToponimoStradale().isEmpty()) {
			toRet = this.getToponimoStradale();
			write = true;
		}
		if(this.getViaPiazza() != null && !this.getViaPiazza().isEmpty()) {
			if(write)
				toRet += " ";
			toRet += this.getViaPiazza();
			write = true;
		}
		if(this.getCivico() != null && !this.getCivico().isEmpty()) {
			if(write)
				toRet += " ";
			toRet += this.getCivico();
			write = true;
		}
		if(this.getComune() != null && !this.getComune().isEmpty()) {
			if(write)
				toRet += " ";
			toRet += this.getComune();
			write = true;
		}
		if(this.getProvincia() != null && !this.getProvincia().isEmpty()) {
			if(write)
				toRet += " ";
			toRet += this.getProvincia();
			write = true;
		}
		return toRet;
	}

	public String getCodiceRegione() {
		return codiceRegione;
	}

	public void setCodiceRegione(String codiceRegione) {
		this.codiceRegione = codiceRegione;
	}

	public String getCodiceTitolare() {
		return codiceTitolare;
	}

	public void setCodiceTitolare(String codiceTitolare) {
		this.codiceTitolare = codiceTitolare;
	}

	public Date getDataInizioValidita() {
		return dataInizioValidita;
	}

	public void setDataInizioValidita(Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getNatura() {
		return natura;
	}

	public void setNatura(String natura) {
		this.natura = natura;
	}

	public String getTipologiaTitolare() {
		return tipologiaTitolare;
	}

	public void setTipologiaTitolare(String tipologiaTitolare) {
		this.tipologiaTitolare = tipologiaTitolare;
	}

	public String getToponimoStradale() {
		return toponimoStradale;
	}

	public void setToponimoStradale(String toponimoStradale) {
		this.toponimoStradale = toponimoStradale;
	}

	public String getViaPiazza() {
		return viaPiazza;
	}

	public void setViaPiazza(String viaPiazza) {
		this.viaPiazza = viaPiazza;
	}

	public String getCivico() {
		return civico;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPec() {
		return pec;
	}

	public void setPec(String pec) {
		this.pec = pec;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}