package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the AMBITO_TEMPL database table.
 * 
 */
@Entity
@Table(name="AMBITO_TEMPL")
public class AmbitoTempl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	@Column(name="AGGIUNGI_BRANCHE")
	private String aggiungiBranche;

	@Column(name="AGGIUNGI_DISCIPLINE")
	private String aggiungiDiscipline;

	@Column(name="AGGIUNGI_PRESTAZIONI")
	private String aggiungiPrestazioni;

	private String annotations;

	@Temporal(TemporalType.DATE)
	private Date creation;

	private String descr;

	private String disabled;

	@Temporal(TemporalType.DATE)
	private Date ended;

	@Column(name="ID_AMBITO")
	private BigDecimal idAmbito;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String nome;

	//bi-directional many-to-many association to TipoUdo22Templ
	@ManyToMany(mappedBy="ambitoTempls")
	private List<TipoUdo22Templ> tipoUdo22Templs;

	public AmbitoTempl() {
	}

	public String getClientid() {
		return this.clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public String getAggiungiBranche() {
		return this.aggiungiBranche;
	}

	public void setAggiungiBranche(String aggiungiBranche) {
		this.aggiungiBranche = aggiungiBranche;
	}

	public String getAggiungiDiscipline() {
		return this.aggiungiDiscipline;
	}

	public void setAggiungiDiscipline(String aggiungiDiscipline) {
		this.aggiungiDiscipline = aggiungiDiscipline;
	}

	public String getAggiungiPrestazioni() {
		return this.aggiungiPrestazioni;
	}

	public void setAggiungiPrestazioni(String aggiungiPrestazioni) {
		this.aggiungiPrestazioni = aggiungiPrestazioni;
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

	public BigDecimal getIdAmbito() {
		return this.idAmbito;
	}

	public void setIdAmbito(BigDecimal idAmbito) {
		this.idAmbito = idAmbito;
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

	public List<TipoUdo22Templ> getTipoUdo22Templs() {
		return this.tipoUdo22Templs;
	}

	public void setTipoUdo22Templs(List<TipoUdo22Templ> tipoUdo22Templs) {
		this.tipoUdo22Templs = tipoUdo22Templs;
	}

}