package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the UTENTE_DIREZIONE_MODEL database table.
 * 
 */
@Entity
@Table(name="UTENTE_DIREZIONE_MODEL")
public class UtenteDirezioneModel implements Serializable {
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

	@Column(name="ID_USER")
	private String idUser;

	@Column(name="ID_UTENTE_DIREZIONE_MODEL")
	private BigDecimal idUtenteDirezioneModel;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	//bi-directional many-to-one association to DirezioneTempl
	@ManyToOne
	@JoinColumn(name="ID_DIREZIONE_TEMPL")
	private DirezioneTempl direzioneTempl;

	//bi-directional many-to-many association to NaturaTitolareTempl
	@ManyToMany
	@JoinTable(
		name="BIND_UTENTE_DIR_NATURA"
		, joinColumns={
			@JoinColumn(name="ID_UTENTE_DIREZIONE_MODEL")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_NATURA_TITOLARE_TEMPL")
			}
		)
	private List<NaturaTitolareTempl> naturaTitolareTempls;

	//bi-directional many-to-many association to UfficioTempl
	@ManyToMany
	@JoinTable(
		name="BIND_UTENTE_DIR_UFF"
		, joinColumns={
			@JoinColumn(name="ID_UTENTE_DIREZIONE_MODEL")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_UFFICIO_TEMPL")
			}
		)
	private List<UfficioTempl> ufficioTempls;

	//bi-directional many-to-one association to UtenteModel
	@ManyToOne
	@JoinColumn(name="ID_UTENTE_MODEL")
	private UtenteModel utenteModel;

	public UtenteDirezioneModel() {
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

	public String getIdUser() {
		return this.idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public BigDecimal getIdUtenteDirezioneModel() {
		return this.idUtenteDirezioneModel;
	}

	public void setIdUtenteDirezioneModel(BigDecimal idUtenteDirezioneModel) {
		this.idUtenteDirezioneModel = idUtenteDirezioneModel;
	}

	public Date getLastMod() {
		return this.lastMod;
	}

	public void setLastMod(Date lastMod) {
		this.lastMod = lastMod;
	}

	public DirezioneTempl getDirezioneTempl() {
		return this.direzioneTempl;
	}

	public void setDirezioneTempl(DirezioneTempl direzioneTempl) {
		this.direzioneTempl = direzioneTempl;
	}

	public List<NaturaTitolareTempl> getNaturaTitolareTempls() {
		return this.naturaTitolareTempls;
	}

	public void setNaturaTitolareTempls(List<NaturaTitolareTempl> naturaTitolareTempls) {
		this.naturaTitolareTempls = naturaTitolareTempls;
	}

	public List<UfficioTempl> getUfficioTempls() {
		return this.ufficioTempls;
	}

	public void setUfficioTempls(List<UfficioTempl> ufficioTempls) {
		this.ufficioTempls = ufficioTempls;
	}

	public UtenteModel getUtenteModel() {
		return this.utenteModel;
	}

	public void setUtenteModel(UtenteModel utenteModel) {
		this.utenteModel = utenteModel;
	}

}