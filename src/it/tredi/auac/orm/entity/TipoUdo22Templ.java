package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * The persistent class for the TIPO_UDO_22_TEMPL database table.
 * 
 */
@Entity
@Table(name="TIPO_UDO_22_TEMPL")
public class TipoUdo22Templ implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	private String annotations;

	@Column(name="CODICE_UDO")
	private String codiceUdo;

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

	@Column(name="NOME_CODICE_UDO")
	private String nomeCodiceUdo;

	@Column(name="SALUTE_MENTALE")
	private String saluteMentale;

	//bi-directional many-to-one association to BindTipo22Lista
	@OneToMany(mappedBy="tipoUdo22Templ")
	private List<BindTipo22Lista> bindTipo22Listas;

	//bi-directional many-to-many association to AmbitoTempl
	@ManyToMany
	@JoinTable(
		name="BIND_TIPO_22_AMBITO"
		, joinColumns={
			@JoinColumn(name="ID_TIPO_22_FK")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_AMBITO_FK")
			}
		)
	private List<AmbitoTempl> ambitoTempls;

	//bi-directional many-to-one association to ClassificazioneUdoTempl
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_CLASSIFICAZIONE_UDO_FK")
	private ClassificazioneUdoTempl classificazioneUdoTempl;

	//bi-directional many-to-many association to FlussoTempl
	@ManyToMany
	@JoinTable(
		name="BIND_TIPO_22_FLUSSO"
		, joinColumns={
			@JoinColumn(name="ID_TIPO_UDO_22_FK")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_FLUSSO_FK")
			}
		)
	private List<FlussoTempl> flussoTempls;

	//bi-directional many-to-one association to TipoUdoTempl
	@OneToMany(mappedBy="tipoUdo22Templ")
	private List<TipoUdoTempl> tipoUdoTempls;

	//bi-directional many-to-one association to TipoUdoUtenteTempl
	@OneToMany(mappedBy="tipoUdo22Templ")
	private List<TipoUdoUtenteTempl> tipoUdoUtenteTempls;

	//bi-directional many-to-many association to TipoFattoreProdTempl
	@ManyToMany
	@JoinTable(
		name="BIND_TIPO_22_TIPO_FATT"
		, joinColumns={
			@JoinColumn(name="ID_TIPO_UDO_22_FK")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_TIPO_FATT_FK")
			}
		)
	private List<TipoFattoreProdTempl> tipoFattoreProdTempls;

	public TipoUdo22Templ() {
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

	public String getCodiceUdo() {
		return this.codiceUdo;
	}

	public void setCodiceUdo(String codiceUdo) {
		this.codiceUdo = codiceUdo;
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

	public String getNomeCodiceUdo() {
		return this.nomeCodiceUdo;
	}

	public void setNomeCodiceUdo(String nomeCodiceUdo) {
		this.nomeCodiceUdo = nomeCodiceUdo;
	}

	public String getSaluteMentale() {
		return this.saluteMentale;
	}

	public void setSaluteMentale(String saluteMentale) {
		this.saluteMentale = saluteMentale;
	}

	public List<BindTipo22Lista> getBindTipo22Listas() {
		return this.bindTipo22Listas;
	}

	public void setBindTipo22Listas(List<BindTipo22Lista> bindTipo22Listas) {
		this.bindTipo22Listas = bindTipo22Listas;
	}

	public BindTipo22Lista addBindTipo22Lista(BindTipo22Lista bindTipo22Lista) {
		getBindTipo22Listas().add(bindTipo22Lista);
		bindTipo22Lista.setTipoUdo22Templ(this);

		return bindTipo22Lista;
	}

	public BindTipo22Lista removeBindTipo22Lista(BindTipo22Lista bindTipo22Lista) {
		getBindTipo22Listas().remove(bindTipo22Lista);
		bindTipo22Lista.setTipoUdo22Templ(null);

		return bindTipo22Lista;
	}

	public List<AmbitoTempl> getAmbitoTempls() {
		return this.ambitoTempls;
	}

	public void setAmbitoTempls(List<AmbitoTempl> ambitoTempls) {
		this.ambitoTempls = ambitoTempls;
	}

	public ClassificazioneUdoTempl getClassificazioneUdoTempl() {
		return this.classificazioneUdoTempl;
	}

	public void setClassificazioneUdoTempl(ClassificazioneUdoTempl classificazioneUdoTempl) {
		this.classificazioneUdoTempl = classificazioneUdoTempl;
	}

	public List<FlussoTempl> getFlussoTempls() {
		return this.flussoTempls;
	}

	public void setFlussoTempls(List<FlussoTempl> flussoTempls) {
		this.flussoTempls = flussoTempls;
	}

	public List<TipoUdoTempl> getTipoUdoTempls() {
		return this.tipoUdoTempls;
	}

	public void setTipoUdoTempls(List<TipoUdoTempl> tipoUdoTempls) {
		this.tipoUdoTempls = tipoUdoTempls;
	}

	public TipoUdoTempl addTipoUdoTempl(TipoUdoTempl tipoUdoTempl) {
		getTipoUdoTempls().add(tipoUdoTempl);
		tipoUdoTempl.setTipoUdo22Templ(this);

		return tipoUdoTempl;
	}

	public TipoUdoTempl removeTipoUdoTempl(TipoUdoTempl tipoUdoTempl) {
		getTipoUdoTempls().remove(tipoUdoTempl);
		tipoUdoTempl.setTipoUdo22Templ(null);

		return tipoUdoTempl;
	}

	public List<TipoUdoUtenteTempl> getTipoUdoUtenteTempls() {
		return this.tipoUdoUtenteTempls;
	}

	public void setTipoUdoUtenteTempls(List<TipoUdoUtenteTempl> tipoUdoUtenteTempls) {
		this.tipoUdoUtenteTempls = tipoUdoUtenteTempls;
	}

	public TipoUdoUtenteTempl addTipoUdoUtenteTempl(TipoUdoUtenteTempl tipoUdoUtenteTempl) {
		getTipoUdoUtenteTempls().add(tipoUdoUtenteTempl);
		tipoUdoUtenteTempl.setTipoUdo22Templ(this);

		return tipoUdoUtenteTempl;
	}

	public TipoUdoUtenteTempl removeTipoUdoUtenteTempl(TipoUdoUtenteTempl tipoUdoUtenteTempl) {
		getTipoUdoUtenteTempls().remove(tipoUdoUtenteTempl);
		tipoUdoUtenteTempl.setTipoUdo22Templ(null);

		return tipoUdoUtenteTempl;
	}

	public List<TipoFattoreProdTempl> getTipoFattoreProdTempls() {
		return this.tipoFattoreProdTempls;
	}

	public void setTipoFattoreProdTempls(List<TipoFattoreProdTempl> tipoFattoreProdTempls) {
		this.tipoFattoreProdTempls = tipoFattoreProdTempls;
	}

}