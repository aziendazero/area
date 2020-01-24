package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the ESITO_TEMPL database table.
 * 
 */
@Entity
@Table(name="ESITO_TEMPL")
public class EsitoTempl implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private EsitoTemplPK id;

	private String annotations;

	@Temporal(TemporalType.DATE)
	private Date creation;

	private String descr;

	private String disabled;

	@Temporal(TemporalType.DATE)
	private Date ended;

	private String esito;

	@Column(name="ID_ESITO")
	private BigDecimal idEsito;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	//bi-directional many-to-one association to TipoProcTempl
	@ManyToOne
	@JoinColumn(name="ID_TIPO_PROC_FK", insertable=false, updatable=false)
	private TipoProcTempl tipoProcTempl;

	public EsitoTempl() {
	}

	public EsitoTemplPK getId() {
		return this.id;
	}

	public void setId(EsitoTemplPK id) {
		this.id = id;
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

	public String getEsito() {
		return this.esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public BigDecimal getIdEsito() {
		return this.idEsito;
	}

	public void setIdEsito(BigDecimal idEsito) {
		this.idEsito = idEsito;
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

	public TipoProcTempl getTipoProcTempl() {
		return this.tipoProcTempl;
	}

	public void setTipoProcTempl(TipoProcTempl tipoProcTempl) {
		this.tipoProcTempl = tipoProcTempl;
	}

}