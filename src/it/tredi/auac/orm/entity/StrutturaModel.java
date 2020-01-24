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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the STRUTTURA_MODEL database table.
 * 
 */
@Entity
@Table(name="STRUTTURA_MODEL")
public class StrutturaModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	private String annotations;

	@Column(name="CODICE_PF")
	private String codicePf;

	@Temporal(TemporalType.DATE)
	private Date creation;

	private String denominazione;

	private String descr;

	private String disabled;

	@Temporal(TemporalType.DATE)
	private Date ended;

	@Column(name="ID_STRUTTURA")
	private BigDecimal idStruttura;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	//bi-directional many-to-one association to FattProdUdoModel
	@OneToMany(mappedBy="strutturaModel")
	private List<FattProdUdoModel> fattProdUdoModels;

	//bi-directional many-to-one association to SedeOperModel
	@OneToMany(mappedBy="strutturaModel")
	private List<SedeOperModel> sedeOperModels;

	//bi-directional many-to-many association to CodiceArssTempl
	@ManyToMany
	@JoinTable(
		name="BIND_STRUTT_ARSS"
		, joinColumns={
			@JoinColumn(name="ID_STRUTTURA_FK")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_COD_ARSS_FK")
			}
		)
	private List<CodiceArssTempl> codiceArssTempls;

	//bi-directional many-to-one association to TitolareModel
	@ManyToOne
	@JoinColumn(name="ID_TITOLARE_FK")
	private TitolareModel titolareModel;

	public StrutturaModel() {
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

	public String getCodicePf() {
		return codicePf;
	}

	public void setCodicePf(String codicePf) {
		this.codicePf = codicePf;
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

	public BigDecimal getIdStruttura() {
		return this.idStruttura;
	}

	public void setIdStruttura(BigDecimal idStruttura) {
		this.idStruttura = idStruttura;
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

	public List<FattProdUdoModel> getFattProdUdoModels() {
		return this.fattProdUdoModels;
	}

	public void setFattProdUdoModels(List<FattProdUdoModel> fattProdUdoModels) {
		this.fattProdUdoModels = fattProdUdoModels;
	}

	public FattProdUdoModel addFattProdUdoModel(FattProdUdoModel fattProdUdoModel) {
		getFattProdUdoModels().add(fattProdUdoModel);
		fattProdUdoModel.setStrutturaModel(this);

		return fattProdUdoModel;
	}

	public FattProdUdoModel removeFattProdUdoModel(FattProdUdoModel fattProdUdoModel) {
		getFattProdUdoModels().remove(fattProdUdoModel);
		fattProdUdoModel.setStrutturaModel(null);

		return fattProdUdoModel;
	}

	public List<SedeOperModel> getSedeOperModels() {
		return this.sedeOperModels;
	}

	public void setSedeOperModels(List<SedeOperModel> sedeOperModels) {
		this.sedeOperModels = sedeOperModels;
	}

	public SedeOperModel addSedeOperModel(SedeOperModel sedeOperModel) {
		getSedeOperModels().add(sedeOperModel);
		sedeOperModel.setStrutturaModel(this);

		return sedeOperModel;
	}

	public SedeOperModel removeSedeOperModel(SedeOperModel sedeOperModel) {
		getSedeOperModels().remove(sedeOperModel);
		sedeOperModel.setStrutturaModel(null);

		return sedeOperModel;
	}

	public List<CodiceArssTempl> getCodiceArssTempls() {
		return this.codiceArssTempls;
	}

	public void setCodiceArssTempls(List<CodiceArssTempl> codiceArssTempls) {
		this.codiceArssTempls = codiceArssTempls;
	}

	public TitolareModel getTitolareModel() {
		return this.titolareModel;
	}

	public void setTitolareModel(TitolareModel titolareModel) {
		this.titolareModel = titolareModel;
	}

}