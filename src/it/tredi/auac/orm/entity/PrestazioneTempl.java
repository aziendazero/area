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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the PRESTAZIONE_TEMPL database table.
 * 
 */
@Entity
@Table(name="PRESTAZIONE_TEMPL")
public class PrestazioneTempl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	private String annotations;

	@Temporal(TemporalType.DATE)
	private Date creation;

	private String descr;

	private String descrizione;

	private String disabled;

	@Temporal(TemporalType.DATE)
	private Date ended;

	@Column(name="ID_PRESTAZIONE")
	private BigDecimal idPrestazione;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String nome;

	//bi-directional many-to-many association to BrancaTempl
	@ManyToMany
	@JoinTable(
		name="BIND_BRANCA_PREST"
		, joinColumns={
			@JoinColumn(name="ID_PRESTAZIONE_FK")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_BRANCA_FK")
			}
		)
	private List<BrancaTempl> brancaTempls;

	//bi-directional many-to-many association to UdoModel
	@ManyToMany(mappedBy="prestazioneTempls")
	private List<UdoModel> udoModels;

	public PrestazioneTempl() {
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

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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

	public BigDecimal getIdPrestazione() {
		return this.idPrestazione;
	}

	public void setIdPrestazione(BigDecimal idPrestazione) {
		this.idPrestazione = idPrestazione;
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

	public List<BrancaTempl> getBrancaTempls() {
		return this.brancaTempls;
	}

	public void setBrancaTempls(List<BrancaTempl> brancaTempls) {
		this.brancaTempls = brancaTempls;
	}

	public List<UdoModel> getUdoModels() {
		return this.udoModels;
	}

	public void setUdoModels(List<UdoModel> udoModels) {
		this.udoModels = udoModels;
	}

}