package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the ATTO_MODEL database table.
 * 
 */
@Entity
@Table(name="MRA_MODEL")
public class MraModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	@Column(name="DATA_PRIMO_INVIO")
	@Temporal(TemporalType.DATE)
	private Date dataPrimoInvio;

	@ManyToOne
	@JoinColumn(name="ID_ALG_TITOLARI_FK")
	private BinaryAttachmentsAppl xmlTitolari;

	@ManyToOne
	@JoinColumn(name="ID_ALG_CENTRI_RESPONS_FK")
	private BinaryAttachmentsAppl xmlCentriResponsabilita;

	@ManyToOne
	@JoinColumn(name="ID_ALG_PUNTI_FISICI_FK")
	private BinaryAttachmentsAppl xmlPuntiFisici;

	@ManyToOne
	@JoinColumn(name="ID_ALG_TRIPLETTE_FK")
	private BinaryAttachmentsAppl xmlTriplette;

	public MraModel() {
	}

	public String getClientid() {
		return this.clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public Date getDataPrimoInvio() {
		return dataPrimoInvio;
	}

	public void setDataPrimoInvio(Date dataPrimoInvio) {
		this.dataPrimoInvio = dataPrimoInvio;
	}

	public BinaryAttachmentsAppl getXmlTitolari() {
		return this.xmlTitolari;
	}

	public void setXmlTitolari(BinaryAttachmentsAppl xmlTitolari) {
		this.xmlTitolari = xmlTitolari;
	}

	public BinaryAttachmentsAppl getXmlCentriResponsabilita() {
		return xmlCentriResponsabilita;
	}

	public void setXmlCentriResponsabilita(
			BinaryAttachmentsAppl xmlCentriResponsabilita) {
		this.xmlCentriResponsabilita = xmlCentriResponsabilita;
	}

	public BinaryAttachmentsAppl getXmlPuntiFisici() {
		return xmlPuntiFisici;
	}

	public void setXmlPuntiFisici(BinaryAttachmentsAppl xmlPuntiFisici) {
		this.xmlPuntiFisici = xmlPuntiFisici;
	}

	public BinaryAttachmentsAppl getXmlTriplette() {
		return xmlTriplette;
	}

	public void setXmlTriplette(BinaryAttachmentsAppl xmlTriplette) {
		this.xmlTriplette = xmlTriplette;
	}
}