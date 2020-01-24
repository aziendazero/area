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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the DIREZIONE_TEMPL database table.
 * 
 */
@Entity
@Table(name="DIREZIONE_TEMPL")
public class DirezioneTempl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	private String annotations;

	@Temporal(TemporalType.DATE)
	private Date creation;

	private String descr;

	private String disabled;

	private String email;

	@Temporal(TemporalType.DATE)
	private Date ended;

	@Column(name="ID_DIREZIONE_TEMPL")
	private BigDecimal idDirezioneTempl;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String nome;

	//Aggiunta inde 20160906
	//@Column(name="NOME_ID")
	//private String nomeId;
	
	@Column(name="SHOW_DICHIARAZIONE_DIR_SAN")
	private String showDichiarazioneDirSan;
	
	@Column(name="DIREZIONE_WF")
	private String direzioneWf;

	//bi-directional many-to-one association to ClassificazioneTempl
	@OneToMany(mappedBy="direzioneTempl")
	private List<ClassificazioneTempl> classificazioneTempls;

	//bi-directional many-to-many association to ClassificazioneUdoTempl
	@ManyToMany
	@JoinTable(
		name="BIND_CLASS_UDO_DIREZIONI"
		, joinColumns={
			@JoinColumn(name="ID_DIREZIONE_FK")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_CLASSIFICAZIONE_UDO_FK")
			}
		)
	private List<ClassificazioneUdoTempl> classificazioneUdoTempls;

	//bi-directional many-to-one association to TitolareModel
	@OneToMany(mappedBy="direzioneTempl")
	private List<TitolareModel> titolareModels;

	//bi-directional many-to-one association to UtenteDirezioneModel
	@OneToMany(mappedBy="direzioneTempl")
	private List<UtenteDirezioneModel> utenteDirezioneModels;

	//bi-directional many-to-one association to UtenteModel
	@OneToMany(mappedBy="direzioneTempl")
	private List<UtenteModel> utenteModels;

	public DirezioneTempl() {
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

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getEnded() {
		return this.ended;
	}

	public void setEnded(Date ended) {
		this.ended = ended;
	}

	public BigDecimal getIdDirezioneTempl() {
		return this.idDirezioneTempl;
	}

	public void setIdDirezioneTempl(BigDecimal idDirezioneTempl) {
		this.idDirezioneTempl = idDirezioneTempl;
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

	public String getShowDichiarazioneDirSan() {
		return showDichiarazioneDirSan;
	}

	public void setShowDichiarazioneDirSan(String showDichiarazioneDirSan) {
		this.showDichiarazioneDirSan = showDichiarazioneDirSan;
	}

	public List<ClassificazioneTempl> getClassificazioneTempls() {
		return this.classificazioneTempls;
	}

	public void setClassificazioneTempls(List<ClassificazioneTempl> classificazioneTempls) {
		this.classificazioneTempls = classificazioneTempls;
	}

	public ClassificazioneTempl addClassificazioneTempl(ClassificazioneTempl classificazioneTempl) {
		getClassificazioneTempls().add(classificazioneTempl);
		classificazioneTempl.setDirezioneTempl(this);

		return classificazioneTempl;
	}

	public ClassificazioneTempl removeClassificazioneTempl(ClassificazioneTempl classificazioneTempl) {
		getClassificazioneTempls().remove(classificazioneTempl);
		classificazioneTempl.setDirezioneTempl(null);

		return classificazioneTempl;
	}

	public List<ClassificazioneUdoTempl> getClassificazioneUdoTempls() {
		return this.classificazioneUdoTempls;
	}

	public void setClassificazioneUdoTempls(List<ClassificazioneUdoTempl> classificazioneUdoTempls) {
		this.classificazioneUdoTempls = classificazioneUdoTempls;
	}

	public List<TitolareModel> getTitolareModels() {
		return this.titolareModels;
	}

	public void setTitolareModels(List<TitolareModel> titolareModels) {
		this.titolareModels = titolareModels;
	}

	public TitolareModel addTitolareModel(TitolareModel titolareModel) {
		getTitolareModels().add(titolareModel);
		titolareModel.setDirezioneTempl(this);

		return titolareModel;
	}

	public TitolareModel removeTitolareModel(TitolareModel titolareModel) {
		getTitolareModels().remove(titolareModel);
		titolareModel.setDirezioneTempl(null);

		return titolareModel;
	}

	public List<UtenteDirezioneModel> getUtenteDirezioneModels() {
		return this.utenteDirezioneModels;
	}

	public void setUtenteDirezioneModels(List<UtenteDirezioneModel> utenteDirezioneModels) {
		this.utenteDirezioneModels = utenteDirezioneModels;
	}

	public UtenteDirezioneModel addUtenteDirezioneModel(UtenteDirezioneModel utenteDirezioneModel) {
		getUtenteDirezioneModels().add(utenteDirezioneModel);
		utenteDirezioneModel.setDirezioneTempl(this);

		return utenteDirezioneModel;
	}

	public UtenteDirezioneModel removeUtenteDirezioneModel(UtenteDirezioneModel utenteDirezioneModel) {
		getUtenteDirezioneModels().remove(utenteDirezioneModel);
		utenteDirezioneModel.setDirezioneTempl(null);

		return utenteDirezioneModel;
	}

	public List<UtenteModel> getUtenteModels() {
		return this.utenteModels;
	}

	public void setUtenteModels(List<UtenteModel> utenteModels) {
		this.utenteModels = utenteModels;
	}

	public UtenteModel addUtenteModel(UtenteModel utenteModel) {
		getUtenteModels().add(utenteModel);
		utenteModel.setDirezioneTempl(this);

		return utenteModel;
	}

	public UtenteModel removeUtenteModel(UtenteModel utenteModel) {
		getUtenteModels().remove(utenteModel);
		utenteModel.setDirezioneTempl(null);

		return utenteModel;
	}

	public String getDirezioneWf() {
		return direzioneWf;
	}

	public void setDirezioneWf(String direzioneWf) {
		this.direzioneWf = direzioneWf;
	}

}