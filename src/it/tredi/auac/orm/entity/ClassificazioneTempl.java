package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the CLASSIFICAZIONE_TEMPL database table.
 * 
 */
@Entity
@Table(name="CLASSIFICAZIONE_TEMPL")
public class ClassificazioneTempl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	private String ambito;

	private String annotations;

	@Column(name="CLASSE_CODICE")
	private String classeCodice;

	@Temporal(TemporalType.DATE)
	private Date creation;

	private String descr;

	private String disabled;

	@Temporal(TemporalType.DATE)
	private Date ended;

	@Column(name="ID_CLASSIF")
	private BigDecimal idClassif;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String nome;

	private String servizio;

	@Column(name="TIPO_AREA_FUNZ")
	private String tipoAreaFunz;

	//bi-directional many-to-one association to DirezioneTempl
	@ManyToOne
	@JoinColumn(name="ID_DIREZIONE_FK")
	private DirezioneTempl direzioneTempl;

	//bi-directional many-to-many association to TipoUdoTempl
	@ManyToMany(mappedBy="classificazioneTempls")
	private List<TipoUdoTempl> tipoUdoTempls;

	//bi-directional many-to-many association to TitolareModel
	@ManyToMany(mappedBy="classificazioneTempls")
	private List<TitolareModel> titolareModels;

	public ClassificazioneTempl() {
	}

	public String getClientid() {
		return this.clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public String getAmbito() {
		return this.ambito;
	}

	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}

	public String getAnnotations() {
		return this.annotations;
	}

	public void setAnnotations(String annotations) {
		this.annotations = annotations;
	}

	public String getClasseCodice() {
		return this.classeCodice;
	}

	public void setClasseCodice(String classeCodice) {
		this.classeCodice = classeCodice;
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

	public BigDecimal getIdClassif() {
		return this.idClassif;
	}

	public void setIdClassif(BigDecimal idClassif) {
		this.idClassif = idClassif;
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

	public String getServizio() {
		return this.servizio;
	}

	public void setServizio(String servizio) {
		this.servizio = servizio;
	}

	public String getTipoAreaFunz() {
		return this.tipoAreaFunz;
	}

	public void setTipoAreaFunz(String tipoAreaFunz) {
		this.tipoAreaFunz = tipoAreaFunz;
	}

	public DirezioneTempl getDirezioneTempl() {
		return this.direzioneTempl;
	}

	public void setDirezioneTempl(DirezioneTempl direzioneTempl) {
		this.direzioneTempl = direzioneTempl;
	}

	public List<TipoUdoTempl> getTipoUdoTempls() {
		return this.tipoUdoTempls;
	}

	public void setTipoUdoTempls(List<TipoUdoTempl> tipoUdoTempls) {
		this.tipoUdoTempls = tipoUdoTempls;
	}

	public List<TitolareModel> getTitolareModels() {
		return this.titolareModels;
	}

	public void setTitolareModels(List<TitolareModel> titolareModels) {
		this.titolareModels = titolareModels;
	}

}