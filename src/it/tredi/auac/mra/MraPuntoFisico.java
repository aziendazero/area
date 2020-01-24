package it.tredi.auac.mra;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MraPuntoFisico implements Serializable {
	private static final long serialVersionUID = 1L;

	private String codiceRegione;
	private String codicePuntoFisico;
	private Date dataInizioValidita;

	private String codiceAzienda;
	//private String natura;
	
	//Denominazione
	String denominazione;
	//Tipoplogia punto fisico
	String tipologiaPuntoFisico;

	//Indirizzo
	private String toponimoStradale;
	private String viaPiazza;
	private String civico;
	private String istat;
	private String cap;
	private String comune;
	private String provincia;
	
	private BigDecimal latitudine;
	private BigDecimal longitudine;

	private String telefono;
	private String fax;
	private String email;
	private String url;
	
	private Date dataApertura;
	private Date dataChiusura;
	
	public MraPuntoFisico() {
	}
	
	public String getIndirizzo() {
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
		if(this.getIstat() != null && !this.getIstat().isEmpty()) {
			if(write)
				toRet += " ";
			toRet += this.getIstat();
			write = true;
		}
		if(this.getCap() != null && !this.getCap().isEmpty()) {
			if(write)
				toRet += " ";
			toRet += this.getCap();
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

	public String getCodicePuntoFisico() {
		return codicePuntoFisico;
	}

	public void setCodicePuntoFisico(String codicePuntoFisico) {
		this.codicePuntoFisico = codicePuntoFisico;
	}

	public Date getDataInizioValidita() {
		return dataInizioValidita;
	}

	public void setDataInizioValidita(Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

//	public String getNatura() {
//		return natura;
//	}
//
//	public void setNatura(String natura) {
//		this.natura = natura;
//	}

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

	public String getIstat() {
		return istat;
	}

	public void setIstat(String istat) {
		this.istat = istat;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
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

	public BigDecimal getLatitudine() {
		return latitudine;
	}

	public void setLatitudine(BigDecimal latitudine) {
		this.latitudine = latitudine;
	}

	public BigDecimal getLongitudine() {
		return longitudine;
	}

	public void setLongitudine(BigDecimal longitudine) {
		this.longitudine = longitudine;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getDataApertura() {
		return dataApertura;
	}

	public void setDataApertura(Date dataApertura) {
		this.dataApertura = dataApertura;
	}

	public Date getDataChiusura() {
		return dataChiusura;
	}

	public void setDataChiusura(Date dataChiusura) {
		this.dataChiusura = dataChiusura;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getTipologiaPuntoFisico() {
		return tipologiaPuntoFisico;
	}

	public void setTipologiaPuntoFisico(String tipologiaPuntoFisico) {
		this.tipologiaPuntoFisico = tipologiaPuntoFisico;
	}

}