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
 * The persistent class for the FLUSSO_TEMPL database table.
 * 
 */
@Entity
@Table(name="FLUSSO_TEMPL")
public class FlussoTempl implements Serializable {
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

	@Column(name="ID_FLUSSO")
	private BigDecimal idFlusso;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String nome;

	//bi-directional many-to-one association to AreeAttivitaTempl
	@OneToMany(mappedBy="flussoTempl")
	private List<AreeAttivitaTempl> areeAttivitaTempls;

	/*
	RIMOSSO PER MODIFICA GESTIONE DATI REGIONALI
	//bi-directional many-to-one association to CodiciStruttDenomin
	@OneToMany(mappedBy="flussoTempl")
	private List<CodiciStruttDenomin> codiciStruttDenomins;
	*/

	/*
	RIMOSSO PER MODIFICA GESTIONE DATI REGIONALI
	//bi-directional many-to-many association to CodiciUlssTerritoriali
	@ManyToMany
	@JoinTable(
		name="BIND_FLUSSO_COD_ULSS"
		, joinColumns={
			@JoinColumn(name="ID_FLUSSO_FK")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_COD_ULSS_FK")
			}
		)
	private List<CodiciUlssTerritoriali> codiciUlssTerritorialis;
	*/

	//bi-directional many-to-many association to TipoUdo22Templ
	@ManyToMany(mappedBy="flussoTempls")
	private List<TipoUdo22Templ> tipoUdo22Templs;

	public FlussoTempl() {
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

	public BigDecimal getIdFlusso() {
		return this.idFlusso;
	}

	public void setIdFlusso(BigDecimal idFlusso) {
		this.idFlusso = idFlusso;
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

	public List<AreeAttivitaTempl> getAreeAttivitaTempls() {
		return this.areeAttivitaTempls;
	}

	public void setAreeAttivitaTempls(List<AreeAttivitaTempl> areeAttivitaTempls) {
		this.areeAttivitaTempls = areeAttivitaTempls;
	}

	public AreeAttivitaTempl addAreeAttivitaTempl(AreeAttivitaTempl areeAttivitaTempl) {
		getAreeAttivitaTempls().add(areeAttivitaTempl);
		areeAttivitaTempl.setFlussoTempl(this);

		return areeAttivitaTempl;
	}

	public AreeAttivitaTempl removeAreeAttivitaTempl(AreeAttivitaTempl areeAttivitaTempl) {
		getAreeAttivitaTempls().remove(areeAttivitaTempl);
		areeAttivitaTempl.setFlussoTempl(null);

		return areeAttivitaTempl;
	}

	/*
	RIMOSSO PER MODIFICA GESTIONE DATI REGIONALI
	public List<CodiciStruttDenomin> getCodiciStruttDenomins() {
		return this.codiciStruttDenomins;
	}

	public void setCodiciStruttDenomins(List<CodiciStruttDenomin> codiciStruttDenomins) {
		this.codiciStruttDenomins = codiciStruttDenomins;
	}

	public CodiciStruttDenomin addCodiciStruttDenomin(CodiciStruttDenomin codiciStruttDenomin) {
		getCodiciStruttDenomins().add(codiciStruttDenomin);
		codiciStruttDenomin.setFlussoTempl(this);

		return codiciStruttDenomin;
	}

	public CodiciStruttDenomin removeCodiciStruttDenomin(CodiciStruttDenomin codiciStruttDenomin) {
		getCodiciStruttDenomins().remove(codiciStruttDenomin);
		codiciStruttDenomin.setFlussoTempl(null);

		return codiciStruttDenomin;
	}
	*/

	/*
	RIMOSSO PER MODIFICA GESTIONE DATI REGIONALI
	public List<CodiciUlssTerritoriali> getCodiciUlssTerritorialis() {
		return this.codiciUlssTerritorialis;
	}

	public void setCodiciUlssTerritorialis(List<CodiciUlssTerritoriali> codiciUlssTerritorialis) {
		this.codiciUlssTerritorialis = codiciUlssTerritorialis;
	}
	*/

	public List<TipoUdo22Templ> getTipoUdo22Templs() {
		return this.tipoUdo22Templs;
	}

	public void setTipoUdo22Templs(List<TipoUdo22Templ> tipoUdo22Templs) {
		this.tipoUdo22Templs = tipoUdo22Templs;
	}

}