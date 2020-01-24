package it.tredi.auac.mra;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MraTripletta implements Serializable {
	private static final long serialVersionUID = 1L;

	private String codiceRegione;
	private String codiceTitolare;
	private String codiceCentroResponsabilita;
	private String codicePuntoFisico;
	
	private Date dataInizioValidita;

	private String descrizione;
	
	private String tipoPunto;
	private String tipologia;

	private String stato;

	private Date dataApertura;
	private Date dataChiusura;
	
	private String note;

	private List<MraTripletta> tripletteCollegate = new ArrayList<MraTripletta>();
	private String tipologiaLegame;

	public MraTripletta() {
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

	public String getCodiceCentroResponsabilita() {
		return codiceCentroResponsabilita;
	}

	public void setCodiceCentroResponsabilita(String codiceCentroResponsabilita) {
		this.codiceCentroResponsabilita = codiceCentroResponsabilita;
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

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getTipoPunto() {
		return tipoPunto;
	}

	public void setTipoPunto(String tipoPunto) {
		this.tipoPunto = tipoPunto;
	}

	public String getTipologia() {
		return tipologia;
	}

	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<MraTripletta> getTripletteCollegate() {
		return tripletteCollegate;
	}

	public void setTripletteCollegate(List<MraTripletta> tripletteCollegate) {
		this.tripletteCollegate = tripletteCollegate;
	}

	public String getTipologiaLegame() {
		return tipologiaLegame;
	}

	public void setTipologiaLegame(String tipologiaLegame) {
		this.tipologiaLegame = tipologiaLegame;
	}

}