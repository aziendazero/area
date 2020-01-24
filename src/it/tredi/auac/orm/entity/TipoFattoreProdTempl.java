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
 * The persistent class for the TIPO_FATTORE_PROD_TEMPL database table.
 * 
 */
@Entity
@Table(name="TIPO_FATTORE_PROD_TEMPL")
public class TipoFattoreProdTempl implements Serializable {
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

	@Column(name="ID_TIPO")
	private BigDecimal idTipo;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String nome;

	@Column(name="TIPOLOGIA_FATT_PROD")
	private String tipologiaFattProd;

	//bi-directional many-to-one association to FattProdUdoModel
	@OneToMany(mappedBy="tipoFattoreProdTempl")
	private List<FattProdUdoModel> fattProdUdoModels;

	//bi-directional many-to-many association to TipoUdo22Templ
	@ManyToMany(mappedBy="tipoFattoreProdTempls")
	private List<TipoUdo22Templ> tipoUdo22Templs;

	public TipoFattoreProdTempl() {
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

	public BigDecimal getIdTipo() {
		return this.idTipo;
	}

	public void setIdTipo(BigDecimal idTipo) {
		this.idTipo = idTipo;
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

	public String getTipologiaFattProd() {
		return this.tipologiaFattProd;
	}

	public void setTipologiaFattProd(String tipologiaFattProd) {
		this.tipologiaFattProd = tipologiaFattProd;
	}

	public List<FattProdUdoModel> getFattProdUdoModels() {
		return this.fattProdUdoModels;
	}

	public void setFattProdUdoModels(List<FattProdUdoModel> fattProdUdoModels) {
		this.fattProdUdoModels = fattProdUdoModels;
	}

	public FattProdUdoModel addFattProdUdoModel(FattProdUdoModel fattProdUdoModel) {
		getFattProdUdoModels().add(fattProdUdoModel);
		fattProdUdoModel.setTipoFattoreProdTempl(this);

		return fattProdUdoModel;
	}

	public FattProdUdoModel removeFattProdUdoModel(FattProdUdoModel fattProdUdoModel) {
		getFattProdUdoModels().remove(fattProdUdoModel);
		fattProdUdoModel.setTipoFattoreProdTempl(null);

		return fattProdUdoModel;
	}

	public List<TipoUdo22Templ> getTipoUdo22Templs() {
		return this.tipoUdo22Templs;
	}

	public void setTipoUdo22Templs(List<TipoUdo22Templ> tipoUdo22Templs) {
		this.tipoUdo22Templs = tipoUdo22Templs;
	}

}