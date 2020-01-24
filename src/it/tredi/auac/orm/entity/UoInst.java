package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.CascadeOnDelete;


/**
 * The persistent class for the UO_INST database table.
 * 
 */
@Entity
@Table(name="UO_INST_WITH_REQ_INFO")
public class UoInst implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	private String annotations;

	@Column(name="CODICE_CDR")
	private String codiceCdr;

	@Temporal(TemporalType.DATE)
	private Date creation;

	private String denominazione;

	@Column(name="TIPO_NODO")
	private String tipoNodo;

	private String descr;

	private String disabled;

	@Temporal(TemporalType.DATE)
	private Date ended;

	private String esito;

	@Temporal(TemporalType.DATE)
	@Column(name="ESITO_DATA_INIZIO")
	private Date esitoDataInizio;

	@Column(name="ESITO_NOTE")
	private String esitoNote;

	@Column(name="ESITO_OPERATORE")
	private String esitoOperatore;

	@Temporal(TemporalType.DATE)
	@Column(name="ESITO_SCADENZA")
	private Date esitoScadenza;

	@Column(name="ID_UO_INST")
	private BigDecimal idUoInst;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	@Column(name="OLD_ID_UO_MODEL_FK")
	private String oldUoModelClientid;
	
	@Column(name="PROVENIENZA_UO")
	private String provenienzaUo;

	//Modifica Organigramma aggiunta id univoco COD_NODO_REGIONALE
	@Column(name="ID_UO")
	private String idUo;
	//private BigDecimal idUo;
	
	//bi-directional many-to-one association to RequisitoInst
	@OneToMany(mappedBy="uoInst", cascade={CascadeType.REMOVE})
	@CascadeOnDelete
	private List<RequisitoInst> requisitoInsts;

	//bi-directional many-to-one association to DomandaInst
	@ManyToOne
	@JoinColumn(name="ID_DOMANDA_FK")
	private DomandaInst domandaInst;

	//bi-directional many-to-one association to TitolareModel
	@ManyToOne
	@JoinColumn(name="ID_TITOLARE_FK")
	private TitolareModel titolareModel;

	/*
	//bi-directional many-to-one association to UoModel
	@ManyToOne
	@JoinColumn(name="ID_UO_MODEL_FK")
	@JoinFetch(value=JoinFetchType.INNER)
	private UoModel uoModel;
	*/

	@Column(name="NUM_REQ_SENZA_RISPOSTA", insertable=false, updatable=false)
	private Integer numReqSenzaRisposta;

	@Column(name="NUM_REQ_NON_ASSEGNATI", insertable=false, updatable=false)
	private Integer numReqNonAssegnati;

	public Integer getNumReqSenzaRisposta() {
		return numReqSenzaRisposta;
	}

	public void setNumReqSenzaRisposta(Integer numReqSenzaRisposta) {
		this.numReqSenzaRisposta = numReqSenzaRisposta;
	}

	public Integer getNumReqNonAssegnati() {
		return numReqNonAssegnati;
	}

	public void setNumReqNonAssegnati(Integer numReqNonAssegnati) {
		this.numReqNonAssegnati = numReqNonAssegnati;
	}

	public UoInst() {
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

	public String getCodiceCdr() {
		return this.codiceCdr;
	}

	public void setCodiceCdr(String codiceCdr) {
		this.codiceCdr = codiceCdr;
	}

	public Date getCreation() {
		return this.creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

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

	public String getEsito() {
		return this.esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public Date getEsitoDataInizio() {
		return this.esitoDataInizio;
	}

	public void setEsitoDataInizio(Date esitoDataInizio) {
		this.esitoDataInizio = esitoDataInizio;
	}

	public String getEsitoNote() {
		return this.esitoNote;
	}

	public void setEsitoNote(String esitoNote) {
		this.esitoNote = esitoNote;
	}

	public String getEsitoOperatore() {
		return this.esitoOperatore;
	}

	public void setEsitoOperatore(String esitoOperatore) {
		this.esitoOperatore = esitoOperatore;
	}	

	public Date getEsitoScadenza() {
		return this.esitoScadenza;
	}

	public void setEsitoScadenza(Date esitoScadenza) {
		this.esitoScadenza = esitoScadenza;
	}

	public BigDecimal getIdUoInst() {
		return this.idUoInst;
	}

	public void setIdUoInst(BigDecimal idUoInst) {
		this.idUoInst = idUoInst;
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

	public String getUoModelClientid() {
		return this.provenienzaUo + this.idUo.toString();
	}

	/*public void setUoModelClientid(String uoModelClientid) {
		this.uoModelClientid = uoModelClientid;
	}*/

	public String getOldUoModelClientid() {
		return oldUoModelClientid;
	}

	public void setOldUoModelClientid(String oldUoModelClientid) {
		this.oldUoModelClientid = oldUoModelClientid;
	}

	public String getProvenienzaUo() {
		return provenienzaUo;
	}

	public void setProvenienzaUo(String provenienzaUo) {
		this.provenienzaUo = provenienzaUo;
	}
	
	//Modifica Organigramma aggiunta id univoco COD_NODO_REGIONALE
	public String getIdUo() {
		return idUo;
	}

	public void setIdUo(String idUo) {
		this.idUo = idUo;
	}

	public List<RequisitoInst> getRequisitoInsts() {
		return this.requisitoInsts;
	}

	public void setRequisitoInsts(List<RequisitoInst> requisitoInsts) {
		this.requisitoInsts = requisitoInsts;
	}

	public RequisitoInst addRequisitoInst(RequisitoInst requisitoInst) {
		getRequisitoInsts().add(requisitoInst);
		requisitoInst.setUoInst(this);

		return requisitoInst;
	}

	public RequisitoInst removeRequisitoInst(RequisitoInst requisitoInst) {
		getRequisitoInsts().remove(requisitoInst);
		requisitoInst.setUoInst(null);

		return requisitoInst;
	}

	public DomandaInst getDomandaInst() {
		return this.domandaInst;
	}

	public void setDomandaInst(DomandaInst domandaInst) {
		this.domandaInst = domandaInst;
	}

	public TitolareModel getTitolareModel() {
		return this.titolareModel;
	}

	public void setTitolareModel(TitolareModel titolareModel) {
		this.titolareModel = titolareModel;
	}

	/*public UoModel getUoModel() {
		return this.uoModel;
	}

	public void setUoModel(UoModel uoModel) {
		this.uoModel = uoModel;
	}*/

	public String getDenominazioneConId() {
		if("UO_MODEL".equalsIgnoreCase(this.provenienzaUo))
			return this.denominazione;
		return this.denominazione + " (" + this.idUo + ")";
	}
}