package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the TIPO_ATTO database table.
 * 
 */
@Entity
@Table(name="TIPO_ATTO")
public class TipoAtto implements Serializable {
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

	//bi-directional many-to-one association to AttoInst
	@OneToMany(mappedBy="tipoAtto")
	private List<AttoInst> attoInsts;

	//bi-directional many-to-one association to AttoModel
	@OneToMany(mappedBy="tipoAtto")
	private List<AttoModel> attoModels;

	public TipoAtto() {
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

	public List<AttoInst> getAttoInsts() {
		return this.attoInsts;
	}

	public void setAttoInsts(List<AttoInst> attoInsts) {
		this.attoInsts = attoInsts;
	}

	public AttoInst addAttoInst(AttoInst attoInst) {
		getAttoInsts().add(attoInst);
		attoInst.setTipoAtto(this);

		return attoInst;
	}

	public AttoInst removeAttoInst(AttoInst attoInst) {
		getAttoInsts().remove(attoInst);
		attoInst.setTipoAtto(null);

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
		attoModel.setTipoAtto(this);

		return attoModel;
	}

	public AttoModel removeAttoModel(AttoModel attoModel) {
		getAttoModels().remove(attoModel);
		attoModel.setTipoAtto(null);

		return attoModel;
	}

}