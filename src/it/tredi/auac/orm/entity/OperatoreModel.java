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
 * The persistent class for the OPERATORE_MODEL database table.
 * 
 */
@Entity
@Table(name="OPERATORE_MODEL")
public class OperatoreModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	private String annotations;

	@Temporal(TemporalType.DATE)
	private Date creation;

	private String descr;

	private String disabled;

	@Temporal(TemporalType.DATE)
	private Date ended;

	@Column(name="ID_OPERATORE")
	private BigDecimal idOperatore;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String ruolo;

	//bi-directional many-to-one association to TitolareModel
	@ManyToOne
	@JoinColumn(name="ID_TITOLARE_FK")
	private TitolareModel titolareModel;

	//bi-directional many-to-one association to UtenteModel
	@ManyToOne
	@JoinColumn(name="ID_UTENTE_FK")
	private UtenteModel utenteModel;

	public OperatoreModel() {
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

	public BigDecimal getIdOperatore() {
		return this.idOperatore;
	}

	public void setIdOperatore(BigDecimal idOperatore) {
		this.idOperatore = idOperatore;
	}

	public String getIdUser() {
		return this.idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public Date getLastMod() {
		return this.lastMod;
	}

	public void setLastMod(Date lastMod) {
		this.lastMod = lastMod;
	}

	public String getRuolo() {
		return this.ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	public TitolareModel getTitolareModel() {
		return this.titolareModel;
	}

	public void setTitolareModel(TitolareModel titolareModel) {
		this.titolareModel = titolareModel;
	}

	public UtenteModel getUtenteModel() {
		return this.utenteModel;
	}

	public void setUtenteModel(UtenteModel utenteModel) {
		this.utenteModel = utenteModel;
	}
	
	public String getDescrizione() {
		String descr = this.getDescr();
		if (descr == null || descr.length() > 0) {
			descr = this.getTitolareModel().getRagSoc();
		}							
		return this.getRuolo() + " - " + descr;
		
	}

}