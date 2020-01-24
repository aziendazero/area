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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the TIPO_TITOLARE_TEMPL database table.
 * 
 */
@Entity
@Table(name="TIPO_TITOLARE_TEMPL")
public class TipoTitolareTempl implements Serializable {
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
	
	//Aggiunta inde 20160929
	@Column(name="IS_AZIENDA_SANITARIA")
	private String isAziendaSanitaria;	

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String nome;

	@Column(name="POPOLA_TUTTI_CAMPI_DEL_TIT")
	private String popolaTuttiCampiDelTit;

	@Column(name="SHOW_DICHIARAZIONE_DIR_SAN")
	private String showDichiarazioneDirSan;

	//bi-directional many-to-one association to TipoUdoUtenteTempl
	@OneToMany(mappedBy="tipoTitolareTempl")
	private List<TipoUdoUtenteTempl> tipoUdoUtenteTempls;

	//bi-directional many-to-one association to TitolareModel
	@OneToMany(mappedBy="tipoTitolareTempl")
	private List<TitolareModel> titolareModels;

	//mono-directional many-to-many association to ClassificazioneTempl
	@ManyToMany
	@JoinTable(
		name="TIPU22_RICHIESTI_TIPTIT"
		, joinColumns={
			@JoinColumn(name="ID_TIPO_TITOLARE_FK")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_TIPO_UDO_22_FK")
			}
		)
	private List<TipoUdo22Templ> tipoUdo22TemplRichiesti;
	
	public TipoTitolareTempl() {
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

	public String getIsAziendaSanitaria() {
		return isAziendaSanitaria;
	}

	public void setIsAziendaSanitaria(String isAziendaSanitaria) {
		this.isAziendaSanitaria = isAziendaSanitaria;
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

	public String getPopolaTuttiCampiDelTit() {
		return this.popolaTuttiCampiDelTit;
	}

	public void setPopolaTuttiCampiDelTit(String popolaTuttiCampiDelTit) {
		this.popolaTuttiCampiDelTit = popolaTuttiCampiDelTit;
	}

	public String getShowDichiarazioneDirSan() {
		return showDichiarazioneDirSan;
	}

	public void setShowDichiarazioneDirSan(String showDichiarazioneDirSan) {
		this.showDichiarazioneDirSan = showDichiarazioneDirSan;
	}

	public List<TipoUdoUtenteTempl> getTipoUdoUtenteTempls() {
		return this.tipoUdoUtenteTempls;
	}

	public void setTipoUdoUtenteTempls(List<TipoUdoUtenteTempl> tipoUdoUtenteTempls) {
		this.tipoUdoUtenteTempls = tipoUdoUtenteTempls;
	}

	public TipoUdoUtenteTempl addTipoUdoUtenteTempl(TipoUdoUtenteTempl tipoUdoUtenteTempl) {
		getTipoUdoUtenteTempls().add(tipoUdoUtenteTempl);
		tipoUdoUtenteTempl.setTipoTitolareTempl(this);

		return tipoUdoUtenteTempl;
	}

	public TipoUdoUtenteTempl removeTipoUdoUtenteTempl(TipoUdoUtenteTempl tipoUdoUtenteTempl) {
		getTipoUdoUtenteTempls().remove(tipoUdoUtenteTempl);
		tipoUdoUtenteTempl.setTipoTitolareTempl(null);

		return tipoUdoUtenteTempl;
	}

	public List<TitolareModel> getTitolareModels() {
		return this.titolareModels;
	}

	public void setTitolareModels(List<TitolareModel> titolareModels) {
		this.titolareModels = titolareModels;
	}

	public TitolareModel addTitolareModel(TitolareModel titolareModel) {
		getTitolareModels().add(titolareModel);
		titolareModel.setTipoTitolareTempl(this);

		return titolareModel;
	}

	public TitolareModel removeTitolareModel(TitolareModel titolareModel) {
		getTitolareModels().remove(titolareModel);
		titolareModel.setTipoTitolareTempl(null);

		return titolareModel;
	}

	public List<TipoUdo22Templ> getTipoUdo22TemplRichiesti() {
		return this.tipoUdo22TemplRichiesti;
	}

	public void setTipoUdo22TemplRichiesti(List<TipoUdo22Templ> tipoUdo22TemplRichiesti) {
		this.tipoUdo22TemplRichiesti = tipoUdo22TemplRichiesti;
	}

}