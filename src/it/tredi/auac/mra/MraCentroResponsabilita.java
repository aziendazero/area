package it.tredi.auac.mra;

import java.io.Serializable;
import java.util.Date;

public class MraCentroResponsabilita implements Serializable {
	private static final long serialVersionUID = 1L;

	private String codiceRegione;
	private String codiceCentroResponsabilita;
	private Date dataInizioValidita;

	private String denominazione;
	private String natura;
	private String tipologiaCentroResponsabilita;

	private Date dataApertura;
	private Date dataChiusura;
	
	public MraCentroResponsabilita() {
	}

	public String getCodiceRegione() {
		return codiceRegione;
	}

	public void setCodiceRegione(String codiceRegione) {
		this.codiceRegione = codiceRegione;
	}

	public String getCodiceCentroResponsabilita() {
		return codiceCentroResponsabilita;
	}

	public void setCodiceCentroResponsabilita(String codiceCentroResponsabilita) {
		this.codiceCentroResponsabilita = codiceCentroResponsabilita;
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

	public String getTipologiaCentroResponsabilita() {
		return tipologiaCentroResponsabilita;
	}

	public void setTipologiaCentroResponsabilita(
			String tipologiaCentroResponsabilita) {
		this.tipologiaCentroResponsabilita = tipologiaCentroResponsabilita;
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
}