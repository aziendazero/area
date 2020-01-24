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
 * The persistent class for the STORICO_RISPOSTE_REQUISITI database table.
 * 
 */
@Entity
@Table(name="STORICO_RISPOSTE_REQUISITI")
public class StoricoRisposteRequisiti implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;
	
	private String admin;

	private String annotations;

	private String assegnatario;

	@Temporal(TemporalType.DATE)
	private Date creation;

	private String descr;

	private String disabled;

	@Temporal(TemporalType.DATE)
	private Date ended;

	private String evidenze;

	@Column(name="ID_STORICO_RISPOSTE_REQUISITI")
	private BigDecimal idStoricoRisposteRequisiti;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String note;

	@Column(name="note_verificatore")
	private String noteVerificatore;

	private String operatore;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="TIME_STAMP")
	private Date timeStamp;

	private String valutazione;

	@Column(name="valut_verificatore")
	private String valutazioneVerificatore;
	
	private String verificatore;

	//bi-directional many-to-one association to RequisitoInst
	@ManyToOne
	@JoinColumn(name="ID_REQUISITO_INST_FK")
	private RequisitoInst requisitoInst;

	public StoricoRisposteRequisiti() {
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

	public String getAssegnatario() {
		return this.assegnatario;
	}

	public void setAssegnatario(String assegnatario) {
		this.assegnatario = assegnatario;
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

	public String getEvidenze() {
		return this.evidenze;
	}

	public void setEvidenze(String evidenze) {
		this.evidenze = evidenze;
	}

	public BigDecimal getIdStoricoRisposteRequisiti() {
		return this.idStoricoRisposteRequisiti;
	}

	public void setIdStoricoRisposteRequisiti(BigDecimal idStoricoRisposteRequisiti) {
		this.idStoricoRisposteRequisiti = idStoricoRisposteRequisiti;
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

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getOperatore() {
		return this.operatore;
	}

	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}

	public Date getTimeStamp() {
		return this.timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getValutazione() {
		return this.valutazione;
	}

	public void setValutazione(String valutazione) {
		this.valutazione = valutazione;
	}

	public RequisitoInst getRequisitoInst() {
		return this.requisitoInst;
	}

	public void setRequisitoInst(RequisitoInst requisitoInst) {
		this.requisitoInst = requisitoInst;
	}

	public String getNoteVerificatore() {
		return noteVerificatore;
	}

	public void setNoteVerificatore(String noteVerificatore) {
		this.noteVerificatore = noteVerificatore;
	}

	public String getValutazioneVerificatore() {
		return valutazioneVerificatore;
	}

	public void setValutazioneVerificatore(String valutazioneVerificatore) {
		this.valutazioneVerificatore = valutazioneVerificatore;
	}

	public String getVerificatore() {
		return verificatore;
	}

	public void setVerificatore(String verificatore) {
		this.verificatore = verificatore;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

}