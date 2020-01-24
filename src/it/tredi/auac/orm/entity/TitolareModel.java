package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the TITOLARE_MODEL database table.
 * 
 */
@Entity
@Table(name="TITOLARE_MODEL")
@NamedEntityGraphs({
	@NamedEntityGraph(name="TitolareModel.loadAllForList",
		attributeNodes={
			@NamedAttributeNode("ragSoc"),
			@NamedAttributeNode("piva"),
			@NamedAttributeNode(value="direzioneTempl", subgraph="direzioneTempl")
		},
		subgraphs={
			@NamedSubgraph(name="direzioneTempl",
					attributeNodes={
						@NamedAttributeNode("nome")
			})
		}
	),
	@NamedEntityGraph(name="TitolareModel.titolariAtti",
		attributeNodes={
			@NamedAttributeNode("ragSoc"),
			@NamedAttributeNode("piva"),
			@NamedAttributeNode(value="direzioneTempl", subgraph="direzioneTempl")
		},
		subgraphs={
			@NamedSubgraph(name="direzioneTempl",
					attributeNodes={
						@NamedAttributeNode("nome")
			})
		}
	)
})
public class TitolareModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	private String abilitazione;

	private String annotations;

	private String cap;

	private String cellulare;

	private String cfisc;

	private String civico;

	//da sistemare usando il codice del comune COD_COMUNE_ESTESO
	//private String comune;

	@Temporal(TemporalType.DATE)
	private Date creation;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_ABILITAZIONE")
	private Date dataAbilitazione;

	private String denominazione;

	private String descr;

	private String disabled;

	private String email;

	@Temporal(TemporalType.DATE)
	private Date ended;

	@Column(name="FORMA_SOCIETARIA")
	private String formaSocietaria;

	@Column(name="ID_TITOLARE")
	private BigDecimal idTitolare;

	@Column(name="ID_USER")
	private String idUser;

	private String istat;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String pec;

	private String piva;

	//da sistemare usando il codice del comune COD_COMUNE_ESTESO
	//private String provincia;

	@Column(name="RAG_SOC")
	private String ragSoc;

	private String telefono;

	@Column(name="TOPONIMO_STRADALE")
	private String toponimoStradale;

	private String url;

	@Column(name="VIA_PIAZZA")
	private String viaPiazza;

	//bi-directional many-to-one association to AttoInst
	@OneToMany(mappedBy="titolareModel")
	private List<AttoInst> attoInsts;

	//bi-directional many-to-one association to AttoModel
	@OneToMany(mappedBy="titolareModel")
	private List<AttoModel> attoModels;

	//bi-directional many-to-one association to Comune
	@ManyToOne
	@JoinColumn(name="COD_COMUNE_ESTESO")
	private Comune comune;
	
	//bi-directional many-to-one association to DomandaInst
	@OneToMany(mappedBy="titolareModel")
	private List<DomandaInst> domandaInsts;

	//bi-directional many-to-one association to OperatoreModel
	@OneToMany(mappedBy="titolareModel")
	private List<OperatoreModel> operatoreModels;

	//bi-directional many-to-one association to ProprietarioModel
	@OneToMany(mappedBy="titolareModel")
	private List<ProprietarioModel> proprietarioModels;

	//bi-directional many-to-one association to StrutturaModel
	@OneToMany(mappedBy="titolareModel")
	private List<StrutturaModel> strutturaModels;

	//bi-directional many-to-many association to ClassificazioneTempl
	@ManyToMany
	@JoinTable(
		name="BIND_TITOLARE_CLASSIF"
		, joinColumns={
			@JoinColumn(name="ID_TITOLARE_FK")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_CLASSIF_FK")
			}
		)
	private List<ClassificazioneTempl> classificazioneTempls;

	//bi-directional many-to-one association to DirezioneTempl
	@ManyToOne
	@JoinColumn(name="ID_DIREZIONE_FK")
	private DirezioneTempl direzioneTempl;

	//bi-directional many-to-one association to NaturaTitolareTempl
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_NATURA_FK")
	private NaturaTitolareTempl naturaTitolareTempl;

	//bi-directional many-to-one association to TipologiaRichiedente
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_TIPO_RICH_FK")
	private TipologiaRichiedente tipologiaRichiedente;

	//bi-directional many-to-one association to TipoImprStudioModel
	@ManyToOne
	@JoinColumn(name="ID_TIPO_IMPR_STUDIO_FK")
	private TipoImprStudioModel tipoImprStudioModel;

	//bi-directional many-to-one association to TipoTitolareTempl
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_TIPO_FK")
	private TipoTitolareTempl tipoTitolareTempl;

	//bi-directional many-to-one association to UtenteModel
	@ManyToOne
	@JoinColumn(name="ID_UTENTE_FK")
	private UtenteModel utenteModel;

	//bi-directional many-to-one association to UoInst
	@OneToMany(mappedBy="titolareModel")
	private List<UoInst> uoInsts;

	//bi-directional many-to-one association to UoModel
	@OneToMany(mappedBy="titolareModel")
	private List<UoModel> uoModels;
	
	@Column(name="CODICEUNIVOCO")
	private String codUni;

	public TitolareModel() {
	}

	public String getClientid() {
		return this.clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public String getAbilitazione() {
		return this.abilitazione;
	}

	public void setAbilitazione(String abilitazione) {
		this.abilitazione = abilitazione;
	}

	public String getAnnotations() {
		return this.annotations;
	}

	public void setAnnotations(String annotations) {
		this.annotations = annotations;
	}

	public String getCap() {
		return this.cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getCellulare() {
		return this.cellulare;
	}

	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}

	public String getCfisc() {
		return this.cfisc;
	}

	public void setCfisc(String cfisc) {
		this.cfisc = cfisc;
	}

	public String getCivico() {
		return this.civico;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	/*
	public String getComune() {
		return this.comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}
	*/

	public Date getCreation() {
		return this.creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public Date getDataAbilitazione() {
		return this.dataAbilitazione;
	}

	public void setDataAbilitazione(Date dataAbilitazione) {
		this.dataAbilitazione = dataAbilitazione;
	}

	public String getDenominazione() {
		return this.denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
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

	public String getFormaSocietaria() {
		return this.formaSocietaria;
	}

	public void setFormaSocietaria(String formaSocietaria) {
		this.formaSocietaria = formaSocietaria;
	}

	public BigDecimal getIdTitolare() {
		return this.idTitolare;
	}

	public void setIdTitolare(BigDecimal idTitolare) {
		this.idTitolare = idTitolare;
	}

	public String getIdUser() {
		return this.idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getIstat() {
		return this.istat;
	}

	public void setIstat(String istat) {
		this.istat = istat;
	}

	public Date getLastMod() {
		return this.lastMod;
	}

	public void setLastMod(Date lastMod) {
		this.lastMod = lastMod;
	}

	public String getPec() {
		return this.pec;
	}

	public void setPec(String pec) {
		this.pec = pec;
	}

	public String getPiva() {
		return this.piva;
	}

	public void setPiva(String piva) {
		this.piva = piva;
	}

	/*
	public String getProvincia() {
		return this.provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	*/

	public String getRagSoc() {
		return this.ragSoc;
	}

	public void setRagSoc(String ragSoc) {
		this.ragSoc = ragSoc;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getToponimoStradale() {
		return this.toponimoStradale;
	}

	public void setToponimoStradale(String toponimoStradale) {
		this.toponimoStradale = toponimoStradale;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getViaPiazza() {
		return this.viaPiazza;
	}

	public void setViaPiazza(String viaPiazza) {
		this.viaPiazza = viaPiazza;
	}

	public List<AttoInst> getAttoInsts() {
		return this.attoInsts;
	}

	public void setAttoInsts(List<AttoInst> attoInsts) {
		this.attoInsts = attoInsts;
	}

	public AttoInst addAttoInst(AttoInst attoInst) {
		getAttoInsts().add(attoInst);
		attoInst.setTitolareModel(this);

		return attoInst;
	}

	public AttoInst removeAttoInst(AttoInst attoInst) {
		getAttoInsts().remove(attoInst);
		attoInst.setTitolareModel(null);

		return attoInst;
	}

	public List<AttoModel> getAttoModels() {
		return this.attoModels;
	}

	public void setAttoModels(List<AttoModel> attoModels) {
		this.attoModels = attoModels;
	}

	public AttoModel addAttoModel(AttoModel attoModel) {
		getAttoModels().add(attoModel);
		attoModel.setTitolareModel(this);

		return attoModel;
	}

	public AttoModel removeAttoModel(AttoModel attoModel) {
		getAttoModels().remove(attoModel);
		attoModel.setTitolareModel(null);

		return attoModel;
	}

	public List<DomandaInst> getDomandaInsts() {
		return this.domandaInsts;
	}

	public void setDomandaInsts(List<DomandaInst> domandaInsts) {
		this.domandaInsts = domandaInsts;
	}

	public DomandaInst addDomandaInst(DomandaInst domandaInst) {
		getDomandaInsts().add(domandaInst);
		domandaInst.setTitolareModel(this);

		return domandaInst;
	}

	public DomandaInst removeDomandaInst(DomandaInst domandaInst) {
		getDomandaInsts().remove(domandaInst);
		domandaInst.setTitolareModel(null);

		return domandaInst;
	}

	public List<OperatoreModel> getOperatoreModels() {
		return this.operatoreModels;
	}

	public void setOperatoreModels(List<OperatoreModel> operatoreModels) {
		this.operatoreModels = operatoreModels;
	}

	public OperatoreModel addOperatoreModel(OperatoreModel operatoreModel) {
		getOperatoreModels().add(operatoreModel);
		operatoreModel.setTitolareModel(this);

		return operatoreModel;
	}

	public OperatoreModel removeOperatoreModel(OperatoreModel operatoreModel) {
		getOperatoreModels().remove(operatoreModel);
		operatoreModel.setTitolareModel(null);

		return operatoreModel;
	}

	public List<ProprietarioModel> getProprietarioModels() {
		return this.proprietarioModels;
	}

	public void setProprietarioModels(List<ProprietarioModel> proprietarioModels) {
		this.proprietarioModels = proprietarioModels;
	}

	public ProprietarioModel addProprietarioModel(ProprietarioModel proprietarioModel) {
		getProprietarioModels().add(proprietarioModel);
		proprietarioModel.setTitolareModel(this);

		return proprietarioModel;
	}

	public ProprietarioModel removeProprietarioModel(ProprietarioModel proprietarioModel) {
		getProprietarioModels().remove(proprietarioModel);
		proprietarioModel.setTitolareModel(null);

		return proprietarioModel;
	}

	public List<StrutturaModel> getStrutturaModels() {
		return this.strutturaModels;
	}

	public void setStrutturaModels(List<StrutturaModel> strutturaModels) {
		this.strutturaModels = strutturaModels;
	}

	public StrutturaModel addStrutturaModel(StrutturaModel strutturaModel) {
		getStrutturaModels().add(strutturaModel);
		strutturaModel.setTitolareModel(this);

		return strutturaModel;
	}

	public StrutturaModel removeStrutturaModel(StrutturaModel strutturaModel) {
		getStrutturaModels().remove(strutturaModel);
		strutturaModel.setTitolareModel(null);

		return strutturaModel;
	}

	public List<ClassificazioneTempl> getClassificazioneTempls() {
		return this.classificazioneTempls;
	}

	public void setClassificazioneTempls(List<ClassificazioneTempl> classificazioneTempls) {
		this.classificazioneTempls = classificazioneTempls;
	}

	public Comune getComune() {
		return comune;
	}

	public void setComune(Comune comune) {
		this.comune = comune;
	}

	public DirezioneTempl getDirezioneTempl() {
		return this.direzioneTempl;
	}

	public void setDirezioneTempl(DirezioneTempl direzioneTempl) {
		this.direzioneTempl = direzioneTempl;
	}

	public NaturaTitolareTempl getNaturaTitolareTempl() {
		return this.naturaTitolareTempl;
	}

	public void setNaturaTitolareTempl(NaturaTitolareTempl naturaTitolareTempl) {
		this.naturaTitolareTempl = naturaTitolareTempl;
	}

	public TipologiaRichiedente getTipologiaRichiedente() {
		return this.tipologiaRichiedente;
	}

	public void setTipologiaRichiedente(TipologiaRichiedente tipologiaRichiedente) {
		this.tipologiaRichiedente = tipologiaRichiedente;
	}

	public TipoImprStudioModel getTipoImprStudioModel() {
		return this.tipoImprStudioModel;
	}

	public void setTipoImprStudioModel(TipoImprStudioModel tipoImprStudioModel) {
		this.tipoImprStudioModel = tipoImprStudioModel;
	}

	public TipoTitolareTempl getTipoTitolareTempl() {
		return this.tipoTitolareTempl;
	}

	public void setTipoTitolareTempl(TipoTitolareTempl tipoTitolareTempl) {
		this.tipoTitolareTempl = tipoTitolareTempl;
	}

	public UtenteModel getUtenteModel() {
		return this.utenteModel;
	}

	public void setUtenteModel(UtenteModel utenteModel) {
		this.utenteModel = utenteModel;
	}

	public List<UoInst> getUoInsts() {
		return this.uoInsts;
	}

	public String getCodUni() {
		return codUni;
	}

	public void setCodUni(String codUni) {
		this.codUni = codUni;
	}

	public void setUoInsts(List<UoInst> uoInsts) {
		this.uoInsts = uoInsts;
	}

	public UoInst addUoInst(UoInst uoInst) {
		getUoInsts().add(uoInst);
		uoInst.setTitolareModel(this);

		return uoInst;
	}

	public UoInst removeUoInst(UoInst uoInst) {
		getUoInsts().remove(uoInst);
		uoInst.setTitolareModel(null);

		return uoInst;
	}

	public List<UoModel> getUoModels() {
		return this.uoModels;
	}

	public void setUoModels(List<UoModel> uoModels) {
		this.uoModels = uoModels;
	}

	public UoModel addUoModel(UoModel uoModel) {
		getUoModels().add(uoModel);
		uoModel.setTitolareModel(this);

		return uoModel;
	}

	public UoModel removeUoModel(UoModel uoModel) {
		getUoModels().remove(uoModel);
		uoModel.setTitolareModel(null);

		return uoModel;
	}

}