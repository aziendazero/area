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

import org.eclipse.persistence.annotations.JoinFetch;
import org.eclipse.persistence.annotations.JoinFetchType;


/**
 * The persistent class for the TIPO_UDO_TEMPL database table.
 * 
 */
@Entity
@Table(name="TIPO_UDO_TEMPL")
public class TipoUdoTempl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	@Column(name="AMMETTE_ESTENS")
	private String ammetteEstens;

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

	//bi-directional many-to-many association to ClassificazioneTempl
	@ManyToMany
	@JoinTable(
		name="BIND_CLASSIF_TIPO_UDO"
		, joinColumns={
			@JoinColumn(name="TIPO_UDO_FK")
			}
		, inverseJoinColumns={
			@JoinColumn(name="CLASSIF_FK")
			}
		)
	private List<ClassificazioneTempl> classificazioneTempls;

	//bi-directional many-to-one association to TipoUdo22Templ
	//@BatchFetch(value=BatchFetchType.IN, size=256) nella ManyToOne non ha effetto
	@ManyToOne
	@JoinColumn(name="ID_TIPO_UDO_22_FK")
	@JoinFetch(value=JoinFetchType.INNER)
	private TipoUdo22Templ tipoUdo22Templ;

	//bi-directional many-to-one association to UdoInst
	@OneToMany(mappedBy="tipoUdoTempl")
	private List<UdoInst> udoInsts;

	//bi-directional many-to-one association to UdoModel
	@OneToMany(mappedBy="tipoUdoTempl")
	private List<UdoModel> udoModels;

	public TipoUdoTempl() {
	}

	public String getClientid() {
		return this.clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public String getAmmetteEstens() {
		return this.ammetteEstens;
	}

	public void setAmmetteEstens(String ammetteEstens) {
		this.ammetteEstens = ammetteEstens;
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

	public List<ClassificazioneTempl> getClassificazioneTempls() {
		return this.classificazioneTempls;
	}

	public void setClassificazioneTempls(List<ClassificazioneTempl> classificazioneTempls) {
		this.classificazioneTempls = classificazioneTempls;
	}

	public TipoUdo22Templ getTipoUdo22Templ() {
		return this.tipoUdo22Templ;
	}

	public void setTipoUdo22Templ(TipoUdo22Templ tipoUdo22Templ) {
		this.tipoUdo22Templ = tipoUdo22Templ;
	}

	public List<UdoInst> getUdoInsts() {
		return this.udoInsts;
	}

	public void setUdoInsts(List<UdoInst> udoInsts) {
		this.udoInsts = udoInsts;
	}

	public UdoInst addUdoInst(UdoInst udoInst) {
		getUdoInsts().add(udoInst);
		udoInst.setTipoUdoTempl(this);

		return udoInst;
	}

	public UdoInst removeUdoInst(UdoInst udoInst) {
		getUdoInsts().remove(udoInst);
		udoInst.setTipoUdoTempl(null);

		return udoInst;
	}

	public List<UdoModel> getUdoModels() {
		return this.udoModels;
	}

	public void setUdoModels(List<UdoModel> udoModels) {
		this.udoModels = udoModels;
	}

	public UdoModel addUdoModel(UdoModel udoModel) {
		getUdoModels().add(udoModel);
		udoModel.setTipoUdoTempl(this);

		return udoModel;
	}

	public UdoModel removeUdoModel(UdoModel udoModel) {
		getUdoModels().remove(udoModel);
		udoModel.setTipoUdoTempl(null);

		return udoModel;
	}

}