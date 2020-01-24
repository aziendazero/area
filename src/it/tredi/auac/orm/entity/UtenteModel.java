package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the UTENTE_MODEL database table.
 * 
 */
@Entity
@Table(name="UTENTE_MODEL")
public class UtenteModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	private String annotations;

	@Temporal(TemporalType.DATE)
	private Date creation;

	private String descr;

	private String disabled;
	
	//valori possibili N S
	private String disabilitato;

	@Column(name="DATA_DISABILITATO")
	@Temporal(TemporalType.DATE)
	private Date dataDisabilitato;
	
	@Temporal(TemporalType.DATE)
	private Date ended;

	@Column(name="ID_USER")
	private String idUser;

	@Column(name="ID_UTENTE")
	private BigDecimal idUtente;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String passwd;

	private String ruolo;

	private String username;

	@Column(name="USERNAME_CAS")
	private String usernameCas;

	//bi-directional many-to-one association to OperatoreModel
	@OneToMany(mappedBy="utenteModel")
	private List<OperatoreModel> operatoreModels;

	//bi-directional many-to-one association to RequisitoInst
	@OneToMany(mappedBy="utenteModel")
	private List<RequisitoInst> requisitoInsts;

	//bi-directional many-to-one association to TipoUdoUtenteTempl
	@OneToMany(mappedBy="utenteModel")
	private List<TipoUdoUtenteTempl> tipoUdoUtenteTempls1;

	//bi-directional many-to-one association to TitolareModel
	@OneToMany(mappedBy="utenteModel")
	private List<TitolareModel> titolareModels;

	//bi-directional many-to-one association to UtenteDirezioneModel
	@OneToMany(mappedBy="utenteModel")
	private List<UtenteDirezioneModel> utenteDirezioneModels;

	//bi-directional many-to-one association to AnagraficaUtenteModel
	@ManyToOne
	@JoinColumn(name="ID_ANAGR_FK")
	private AnagraficaUtenteModel anagraficaUtenteModel;

	//bi-directional many-to-one association to DirezioneTempl
	@ManyToOne
	@JoinColumn(name="ID_DIREZIONE_FK")
	private DirezioneTempl direzioneTempl;

	//bi-directional many-to-many association to TipoUdoUtenteTempl
	@ManyToMany
	@JoinTable(
		name="BIND_UTENTE_UDO"
		, joinColumns={
			@JoinColumn(name="ID_UTENTE_MODEL_FK")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_TIPO_UDO_UTENTE_TEMPL_FK")
			}
		)
	private List<TipoUdoUtenteTempl> tipoUdoUtenteTempls2;

	//MODIFICA 2015-12-14 aggiunta gestione organigramma
	//bi-directional many-to-one association to UoModel
	//@ManyToOne
	//@JoinColumn(name="ID_UO_FK")
	//private UoModel uoModel;
	@ManyToOne 
    @JoinColumns({
        @JoinColumn(name="PROVENIENZA_UO", referencedColumnName="PROVENIENZA"),
        @JoinColumn(name="ID_UO", referencedColumnName="ID")
    })
    private UoModel uoModel;
	
	public UtenteModel() {
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

	public String getDisabilitato() {
		return this.disabilitato;
	}

	public void setDisabilitato(String disabilitato) {
		this.disabilitato = disabilitato;
	}

	public Date getDataDisabilitato() {
		return dataDisabilitato;
	}

	public void setDataDisabilitato(Date dataDisabilitato) {
		this.dataDisabilitato = dataDisabilitato;
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

	public BigDecimal getIdUtente() {
		return this.idUtente;
	}

	public void setIdUtente(BigDecimal idUtente) {
		this.idUtente = idUtente;
	}

	public Date getLastMod() {
		return this.lastMod;
	}

	public void setLastMod(Date lastMod) {
		this.lastMod = lastMod;
	}

	public String getPasswd() {
		return this.passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getRuolo() {
		return this.ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	/*
	 * Contiene lo username quando veniva gestiro su database, vedere la property getLoginDbOrCas
	 */
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/*
	 * Contiene lo username da quando veniva gestito su cas, vedere la property getLoginDbOrCas
	 */
	public String getUsernameCas() {
		return usernameCas;
	}

	public void setUsernameCas(String usernameCas) {
		this.usernameCas = usernameCas;
	}

	public List<OperatoreModel> getOperatoreModels() {
		return this.operatoreModels;
	}

	public void setOperatoreModels(List<OperatoreModel> operatoreModels) {
		this.operatoreModels = operatoreModels;
	}

	public OperatoreModel addOperatoreModel(OperatoreModel operatoreModel) {
		getOperatoreModels().add(operatoreModel);
		operatoreModel.setUtenteModel(this);

		return operatoreModel;
	}

	public OperatoreModel removeOperatoreModel(OperatoreModel operatoreModel) {
		getOperatoreModels().remove(operatoreModel);
		operatoreModel.setUtenteModel(null);

		return operatoreModel;
	}

	public List<RequisitoInst> getRequisitoInsts() {
		return this.requisitoInsts;
	}

	public void setRequisitoInsts(List<RequisitoInst> requisitoInsts) {
		this.requisitoInsts = requisitoInsts;
	}

	public RequisitoInst addRequisitoInst(RequisitoInst requisitoInst) {
		getRequisitoInsts().add(requisitoInst);
		requisitoInst.setUtenteModel(this);

		return requisitoInst;
	}

	public RequisitoInst removeRequisitoInst(RequisitoInst requisitoInst) {
		getRequisitoInsts().remove(requisitoInst);
		requisitoInst.setUtenteModel(null);

		return requisitoInst;
	}

	public List<TipoUdoUtenteTempl> getTipoUdoUtenteTempls1() {
		return this.tipoUdoUtenteTempls1;
	}

	public void setTipoUdoUtenteTempls1(List<TipoUdoUtenteTempl> tipoUdoUtenteTempls1) {
		this.tipoUdoUtenteTempls1 = tipoUdoUtenteTempls1;
	}

	public TipoUdoUtenteTempl addTipoUdoUtenteTempls1(TipoUdoUtenteTempl tipoUdoUtenteTempls1) {
		getTipoUdoUtenteTempls1().add(tipoUdoUtenteTempls1);
		tipoUdoUtenteTempls1.setUtenteModel(this);

		return tipoUdoUtenteTempls1;
	}

	public TipoUdoUtenteTempl removeTipoUdoUtenteTempls1(TipoUdoUtenteTempl tipoUdoUtenteTempls1) {
		getTipoUdoUtenteTempls1().remove(tipoUdoUtenteTempls1);
		tipoUdoUtenteTempls1.setUtenteModel(null);

		return tipoUdoUtenteTempls1;
	}

	public List<TitolareModel> getTitolareModels() {
		return this.titolareModels;
	}

	public void setTitolareModels(List<TitolareModel> titolareModels) {
		this.titolareModels = titolareModels;
	}

	public TitolareModel addTitolareModel(TitolareModel titolareModel) {
		getTitolareModels().add(titolareModel);
		titolareModel.setUtenteModel(this);

		return titolareModel;
	}

	public TitolareModel removeTitolareModel(TitolareModel titolareModel) {
		getTitolareModels().remove(titolareModel);
		titolareModel.setUtenteModel(null);

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
		utenteDirezioneModel.setUtenteModel(this);

		return utenteDirezioneModel;
	}

	public UtenteDirezioneModel removeUtenteDirezioneModel(UtenteDirezioneModel utenteDirezioneModel) {
		getUtenteDirezioneModels().remove(utenteDirezioneModel);
		utenteDirezioneModel.setUtenteModel(null);

		return utenteDirezioneModel;
	}

	public AnagraficaUtenteModel getAnagraficaUtenteModel() {
		return this.anagraficaUtenteModel;
	}

	public void setAnagraficaUtenteModel(AnagraficaUtenteModel anagraficaUtenteModel) {
		this.anagraficaUtenteModel = anagraficaUtenteModel;
	}

	public DirezioneTempl getDirezioneTempl() {
		return this.direzioneTempl;
	}

	public void setDirezioneTempl(DirezioneTempl direzioneTempl) {
		this.direzioneTempl = direzioneTempl;
	}

	public List<TipoUdoUtenteTempl> getTipoUdoUtenteTempls2() {
		return this.tipoUdoUtenteTempls2;
	}

	public void setTipoUdoUtenteTempls2(List<TipoUdoUtenteTempl> tipoUdoUtenteTempls2) {
		this.tipoUdoUtenteTempls2 = tipoUdoUtenteTempls2;
	}

	public UoModel getUoModel() {
		return this.uoModel;
	}

	public void setUoModel(UoModel uoModel) {
		this.uoModel = uoModel;
	}
	
	/*
	 * Questa property restituisce la vecchia username se valorizzata altrimenti la usernameCas
	 */
	public String getLoginDbOrCas() {
		if(username != null && !username.isEmpty())
			return username;
		return usernameCas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((clientid == null) ? 0 : clientid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UtenteModel other = (UtenteModel) obj;
		if (clientid == null) {
			if (other.clientid != null)
				return false;
		} else if (!clientid.equals(other.clientid))
			return false;
		return true;
	}

}