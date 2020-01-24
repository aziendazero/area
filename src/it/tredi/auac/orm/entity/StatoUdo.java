package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * The persistent class for the STATO_UDO database table.
 * 
 */
@Entity
@Table(name="STATO_UDO")
public class StatoUdo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	private String annotations;

	@Temporal(TemporalType.DATE)
	private Date creation;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INIZIO")
	private Date dataInizio;

	private String descr;

	private String disabled;

	@Temporal(TemporalType.DATE)
	private Date ended;

	@Column(name="ID_STATO_UDO")
	private BigDecimal idStatoUdo;

	@Column(name="ID_USER")
	private String idUser;

	//Indica che l'inserimento dello stato e' avvenuto dalla chiusura di una domanda (procedimento)
	@Column(name="INS_DA_PROCEDIMENTO")
	private String insDaProcedimento;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	@Temporal(TemporalType.DATE)
	private Date scadenza;

	private String stato;

	//bi-directional many-to-one association to UdoModel
	@ManyToOne
	@JoinColumn(name="ID_UDO_FK")
	private UdoModel udoModel;

	public StatoUdo() {
	}

	public String getClientid() {
		return this.clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public String getAnnotations() {
		return this.annotations;
	}

	public void setAnnotations(String annotations) {
		this.annotations = annotations;
	}

	public Date getCreation() {
		return this.creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public Date getDataInizio() {
		return this.dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getDisabled() {
		return this.disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public Date getEnded() {
		return this.ended;
	}

	public void setEnded(Date ended) {
		this.ended = ended;
	}

	public BigDecimal getIdStatoUdo() {
		return this.idStatoUdo;
	}

	public void setIdStatoUdo(BigDecimal idStatoUdo) {
		this.idStatoUdo = idStatoUdo;
	}

	public String getIdUser() {
		return this.idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getInsDaProcedimento() {
		return insDaProcedimento;
	}

	public void setInsDaProcedimento(String insDaProcedimento) {
		this.insDaProcedimento = insDaProcedimento;
	}

	public Date getLastMod() {
		return this.lastMod;
	}

	public void setLastMod(Date lastMod) {
		this.lastMod = lastMod;
	}

	public Date getScadenza() {
		return this.scadenza;
	}

	public void setScadenza(Date scadenza) {
		this.scadenza = scadenza;
	}

	public String getStato() {
		return this.stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public UdoModel getUdoModel() {
		return this.udoModel;
	}

	public void setUdoModel(UdoModel udoModel) {
		this.udoModel = udoModel;
	}

}