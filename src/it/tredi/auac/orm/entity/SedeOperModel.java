package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the SEDE_OPER_MODEL database table.
 * 
 */
@Entity
@Table(name="SEDE_OPER_MODEL")
public class SedeOperModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	private String annotations;

	private String cap;

	private String civico;

	private String comune;

	@Temporal(TemporalType.DATE)
	private Date creation;

	private String denominazione;

	private String descr;

	private String disabled;

	@Temporal(TemporalType.DATE)
	private Date ended;

	@Column(name="ID_SEDE")
	private BigDecimal idSede;

	@Column(name="ID_USER")
	private String idUser;

	private String istat;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String provincia;

	@Column(name="TIPO_PUNTO_FISICO")
	private String tipoPuntoFisico;

	@Column(name="TOPONIMO_STRADALE")
	private String toponimoStradale;

	@Column(name="VIA_PIAZZA")
	private String viaPiazza;

	@Column(name="FLAG_INDIRIZZO_PRINCIPALE")
	private String flagIndirizzoPrincipale;
	
	//bi-directional many-to-one association to StrutturaModel
	@ManyToOne
	@JoinColumn(name="ID_STRUTTURA_FK")
	private StrutturaModel strutturaModel;

	//bi-directional many-to-one association to UdoModel
	@OneToMany(mappedBy="sedeOperModel")
	private List<UdoModel> udoModels;

	public SedeOperModel() {
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

	public String getCap() {
		return this.cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getCivico() {
		return this.civico;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public String getComune() {
		return this.comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
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

	public BigDecimal getIdSede() {
		return this.idSede;
	}

	public void setIdSede(BigDecimal idSede) {
		this.idSede = idSede;
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

	public String getProvincia() {
		return this.provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getTipoPuntoFisico() {
		return this.tipoPuntoFisico;
	}

	public void setTipoPuntoFisico(String tipoPuntoFisico) {
		this.tipoPuntoFisico = tipoPuntoFisico;
	}

	public String getToponimoStradale() {
		return this.toponimoStradale;
	}

	public void setToponimoStradale(String toponimoStradale) {
		this.toponimoStradale = toponimoStradale;
	}

	public String getViaPiazza() {
		return this.viaPiazza;
	}

	public void setViaPiazza(String viaPiazza) {
		this.viaPiazza = viaPiazza;
	}

	public StrutturaModel getStrutturaModel() {
		return this.strutturaModel;
	}

	public void setStrutturaModel(StrutturaModel strutturaModel) {
		this.strutturaModel = strutturaModel;
	}

	public List<UdoModel> getUdoModels() {
		return this.udoModels;
	}

	public void setUdoModels(List<UdoModel> udoModels) {
		this.udoModels = udoModels;
	}

	public UdoModel addUdoModel(UdoModel udoModel) {
		getUdoModels().add(udoModel);
		udoModel.setSedeOperModel(this);

		return udoModel;
	}

	public UdoModel removeUdoModel(UdoModel udoModel) {
		getUdoModels().remove(udoModel);
		udoModel.setSedeOperModel(null);

		return udoModel;
	}

	public String getFlagIndirizzoPrincipale() {
		return flagIndirizzoPrincipale;
	}

	public void setFlagIndirizzoPrincipale(String flagIndirizzoPrincipale) {
		this.flagIndirizzoPrincipale = flagIndirizzoPrincipale;
	}

}