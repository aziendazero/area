package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the UO_MODEL database table.
 * 
 */
@Entity
//@Table(name="UO_MODEL")
@Table(name="UO_MODEL_ORGANIGRAMMA")

//@SqlResultSetMapping(name="updateResult", columns = { @ColumnResult(name = "count")})

@NamedNativeQueries({
    @NamedNativeQuery(
            name    =   "updateUoModelProcedimentoInCorsoOrganigramma",
            query   =   "UPDATE ORGANIGRAMMA_PROCEDIMENTO SET PROCEDIMENTO_IN_CORSO=? WHERE ID_UO=?"
    ),
    @NamedNativeQuery(
            name    =   "insertUoModelProcedimentoInCorsoOrganigramma",
            query   =   "INSERT INTO ORGANIGRAMMA_PROCEDIMENTO (ID_UO, PROCEDIMENTO_IN_CORSO) VALUES (?, ?)"
    ),
    @NamedNativeQuery(
            name    =   "updateUoModelProcedimentoInCorso",
            query   =   "UPDATE UO_MODEL SET PROCEDIMENTO_IN_CORSO=? WHERE ID_UO=?"
    )
})

public class UoModel implements Serializable {
	private static final long serialVersionUID = 1L;

	//@Id non e' piu' la chiave quando letto da organigramma tale campo e' null
	private String clientid;

	@EmbeddedId
	private UoModelPK id;
	
	//private String annotations;

	//@Column(name="CODICE_CDR")
	//private String codiceCdr;

	//@Temporal(TemporalType.DATE)
	//private Date creation;

	private String denominazione;

	@Column(name="TIPO_NODO")
	private String tipoNodo;

	//private String descr;

	private String disabled;

	//@Temporal(TemporalType.DATE)
	//private Date ended;

	//@Column(name="ID_UO")
	//private BigDecimal idUo;

	//@Column(name="ID_USER")
	//private String idUser;

	//@Temporal(TemporalType.DATE)
	//@Column(name="LAST_MOD")
	//private Date lastMod;

	@Column(name="PROCEDIMENTO_IN_CORSO", insertable=false, updatable=false)
	private String procedimentoInCorso;

	//bi-directional many-to-one association to UdoModel
	@OneToMany(mappedBy="uoModel")
	private List<UdoModel> udoModels;

	/*
	//bi-directional many-to-one association to UoInst
	@OneToMany(mappedBy="uoModel")
	private List<UoInst> uoInsts;*/

	//bi-directional many-to-one association to TitolareModel
	@ManyToOne
	@JoinColumn(name="ID_TITOLARE_FK")
	private TitolareModel titolareModel;

	//bi-directional many-to-one association to UtenteModel
	@OneToMany(mappedBy="uoModel")
	private List<UtenteModel> utenteModels;

	public UoModel() {
	}

	public String getClientid() {
		return this.clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}
	
	public UoModelPK getId() {
		return this.id;
	}

	public void setId(UoModelPK id) {
		this.id = id;
	}	

	/*public String getAnnotations() {
		return this.annotations;
	}

	public void setAnnotations(String annotations) {
		this.annotations = annotations;
	}*/

	/*public String getCodiceCdr() {
		return this.codiceCdr;
	}

	public void setCodiceCdr(String codiceCdr) {
		this.codiceCdr = codiceCdr;
	}*/

	/*public Date getCreation() {
		return this.creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}*/

	public String getDenominazione() {
		return this.denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getTipoNodo() {
		return tipoNodo;
	}

	public void setTipoNodo(String tipoNodo) {
		this.tipoNodo = tipoNodo;
	}
	
	/*public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}*/

	public String getDisabled() {
		return this.disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	/*public Date getEnded() {
		return this.ended;
	}

	public void setEnded(Date ended) {
		this.ended = ended;
	}*/

	/*public BigDecimal getIdUo() {
		return this.idUo;
	}

	public void setIdUo(BigDecimal idUo) {
		this.idUo = idUo;
	}*/

	/*public String getIdUser() {
		return this.idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}*/

	/*public Date getLastMod() {
		return this.lastMod;
	}

	public void setLastMod(Date lastMod) {
		this.lastMod = lastMod;
	}*/

	public String getProcedimentoInCorso() {
		return this.procedimentoInCorso;
	}

	public void setProcedimentoInCorso(String procedimentoInCorso) {
		this.procedimentoInCorso = procedimentoInCorso;
	}

	public List<UdoModel> getUdoModels() {
		return this.udoModels;
	}

	public void setUdoModels(List<UdoModel> udoModels) {
		this.udoModels = udoModels;
	}

	public UdoModel addUdoModel(UdoModel udoModel) {
		getUdoModels().add(udoModel);
		udoModel.setUoModel(this);

		return udoModel;
	}

	public UdoModel removeUdoModel(UdoModel udoModel) {
		getUdoModels().remove(udoModel);
		udoModel.setUoModel(null);

		return udoModel;
	}

	/*public List<UoInst> getUoInsts() {
		return this.uoInsts;
	}

	public void setUoInsts(List<UoInst> uoInsts) {
		this.uoInsts = uoInsts;
	}

	public UoInst addUoInst(UoInst uoInst) {
		getUoInsts().add(uoInst);
		uoInst.setUoModel(this);

		return uoInst;
	}

	public UoInst removeUoInst(UoInst uoInst) {
		getUoInsts().remove(uoInst);
		uoInst.setUoModel(null);

		return uoInst;
	}*/

	public TitolareModel getTitolareModel() {
		return this.titolareModel;
	}

	public void setTitolareModel(TitolareModel titolareModel) {
		this.titolareModel = titolareModel;
	}

	public List<UtenteModel> getUtenteModels() {
		return this.utenteModels;
	}

	public void setUtenteModels(List<UtenteModel> utenteModels) {
		this.utenteModels = utenteModels;
	}

	public UtenteModel addUtenteModel(UtenteModel utenteModel) {
		getUtenteModels().add(utenteModel);
		utenteModel.setUoModel(this);

		return utenteModel;
	}

	public UtenteModel removeUtenteModel(UtenteModel utenteModel) {
		getUtenteModels().remove(utenteModel);
		utenteModel.setUoModel(null);

		return utenteModel;
	}

	public String getDenominazioneConId() {
		if("UO_MODEL".equalsIgnoreCase(this.id.getProvenienza()))
			return this.denominazione;
		return this.denominazione + " (" + this.id.getId() + ")";
	}
}