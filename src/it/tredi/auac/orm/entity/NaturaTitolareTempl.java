package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the NATURA_TITOLARE_TEMPL database table.
 * 
 */
@Entity
@Table(name="NATURA_TITOLARE_TEMPL")
public class NaturaTitolareTempl implements Serializable {
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

	@Column(name="ID_NATURA")
	private BigDecimal idNatura;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String nome;

	//bi-directional many-to-one association to TitolareModel
	@OneToMany(mappedBy="naturaTitolareTempl")
	private List<TitolareModel> titolareModels;

	//bi-directional many-to-many association to UtenteDirezioneModel
	@ManyToMany(mappedBy="naturaTitolareTempls")
	private List<UtenteDirezioneModel> utenteDirezioneModels;

	public NaturaTitolareTempl() {
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

	public BigDecimal getIdNatura() {
		return this.idNatura;
	}

	public void setIdNatura(BigDecimal idNatura) {
		this.idNatura = idNatura;
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

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<TitolareModel> getTitolareModels() {
		return this.titolareModels;
	}

	public void setTitolareModels(List<TitolareModel> titolareModels) {
		this.titolareModels = titolareModels;
	}

	public TitolareModel addTitolareModel(TitolareModel titolareModel) {
		getTitolareModels().add(titolareModel);
		titolareModel.setNaturaTitolareTempl(this);

		return titolareModel;
	}

	public TitolareModel removeTitolareModel(TitolareModel titolareModel) {
		getTitolareModels().remove(titolareModel);
		titolareModel.setNaturaTitolareTempl(null);

		return titolareModel;
	}

	public List<UtenteDirezioneModel> getUtenteDirezioneModels() {
		return this.utenteDirezioneModels;
	}

	public void setUtenteDirezioneModels(List<UtenteDirezioneModel> utenteDirezioneModels) {
		this.utenteDirezioneModels = utenteDirezioneModels;
	}

}