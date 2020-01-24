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
 * The persistent class for the TIPO_PROC_TEMPL database table.
 * 
 */
@Entity
@Table(name="TIPO_PROC_TEMPL")
public class TipoProcTempl implements Serializable {
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

	@Column(name="NOME_WF")
	private String nomeWf;

	private String tipo;

	@Column(name="TIPO_WF")
	private String tipoWf;
	
	//bi-directional many-to-one association to AttoInst
	@OneToMany(mappedBy="tipoProcTempl")
	private List<AttoInst> attoInsts;

	//bi-directional many-to-one association to AttoModel
	@OneToMany(mappedBy="tipoProcTempl")
	private List<AttoModel> attoModels;

	//bi-directional many-to-one association to BindListaRequClassUdo
	@OneToMany(mappedBy="tipoProcTempl")
	private List<BindListaRequClassUdo> bindListaRequClassUdos;

	//bi-directional many-to-one association to BindListaRequClassUdo
	@OneToMany(mappedBy="tipoProcTempl")
	private List<BindListaRequTipoTitClassTit> bindListaRequTipoTitClassTits;

	//bi-directional many-to-one association to BindListaRequClassUdo
	@OneToMany(mappedBy="tipoProcTempl")
	private List<BindListaRequisitoEdificioTipoTitClassTit> bindListaRequisitoEdificioTipoTitClassTits;

	//bi-directional many-to-many association to ListaRequisitiTempl
	@ManyToMany
	@JoinTable(
		name="BIND_LISTA_REQU_STRUTT_FIS"
		, joinColumns={
			@JoinColumn(name="ID_TIPO_PROC_FK")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_LISTA_FK")
			}
		)
	private List<ListaRequisitiTempl> listaRequisitiTemplStrutture;

	//bi-directional many-to-one association to BindTipo22Lista
	@OneToMany(mappedBy="tipoProcTempl")
	private List<BindTipo22Lista> bindTipo22Listas;

	//bi-directional many-to-one association to BindUoProcLista
	@OneToMany(mappedBy="tipoProcTempl")
	private List<BindUoProcLista> bindUoProcListas;

	//bi-directional many-to-one association to DeliberaTempl
	@OneToMany(mappedBy="tipoProcTempl")
	private List<DeliberaTempl> deliberaTempls;

	//bi-directional many-to-one association to DomandaInst
	@OneToMany(mappedBy="tipoProcTempl")
	private List<DomandaInst> domandaInsts;

	//bi-directional many-to-one association to EsitoTempl
	@OneToMany(mappedBy="tipoProcTempl")
	private List<EsitoTempl> esitoTempls;

	//bi-directional many-to-many association to TipologiaRichiedente
	@ManyToMany
	@JoinTable(
		name="BIND_TIPO_RIC_PROC"
		, joinColumns={
			@JoinColumn(name="ID_TIPO_PROC_FK")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_TIPOLOGIA_RIC_FK")
			}
		)
	private List<TipologiaRichiedente> tipologiaRichiedentes;

	//bi-directional many-to-many association to TipoDocTempl
	@ManyToMany
	@JoinTable(
		name="BIND_PROC_DOC"
		, joinColumns={
			@JoinColumn(name="ID_TIPO_PROC_FK")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_TIPO_DOC_FK")
			}
		)
	private List<TipoDocTempl> tipoDocTempls;

	public TipoProcTempl() {
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

	public String getNomeWf() {
		return this.nomeWf;
	}

	public void setNomeWf(String nomeWf) {
		this.nomeWf = nomeWf;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public List<AttoInst> getAttoInsts() {
		return this.attoInsts;
	}

	public void setAttoInsts(List<AttoInst> attoInsts) {
		this.attoInsts = attoInsts;
	}

	public AttoInst addAttoInst(AttoInst attoInst) {
		getAttoInsts().add(attoInst);
		attoInst.setTipoProcTempl(this);

		return attoInst;
	}

	public AttoInst removeAttoInst(AttoInst attoInst) {
		getAttoInsts().remove(attoInst);
		attoInst.setTipoProcTempl(null);

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
		attoModel.setTipoProcTempl(this);

		return attoModel;
	}

	public AttoModel removeAttoModel(AttoModel attoModel) {
		getAttoModels().remove(attoModel);
		attoModel.setTipoProcTempl(null);

		return attoModel;
	}

	public List<BindListaRequClassUdo> getBindListaRequClassUdos() {
		return this.bindListaRequClassUdos;
	}

	public void setBindListaRequClassUdos(List<BindListaRequClassUdo> bindListaRequClassUdos) {
		this.bindListaRequClassUdos = bindListaRequClassUdos;
	}

	public BindListaRequClassUdo addBindListaRequClassUdo(BindListaRequClassUdo bindListaRequClassUdo) {
		getBindListaRequClassUdos().add(bindListaRequClassUdo);
		bindListaRequClassUdo.setTipoProcTempl(this);

		return bindListaRequClassUdo;
	}

	public BindListaRequClassUdo removeBindListaRequClassUdo(BindListaRequClassUdo bindListaRequClassUdo) {
		getBindListaRequClassUdos().remove(bindListaRequClassUdo);
		bindListaRequClassUdo.setTipoProcTempl(null);

		return bindListaRequClassUdo;
	}

	public List<BindListaRequTipoTitClassTit> getBindListaRequTipoTitClassTits() {
		return this.bindListaRequTipoTitClassTits;
	}

	public void setBindListaRequTipoTitClassTits(List<BindListaRequTipoTitClassTit> bindListaRequTipoTitClassTits) {
		this.bindListaRequTipoTitClassTits = bindListaRequTipoTitClassTits;
	}

	public BindListaRequTipoTitClassTit addBindListaRequTipoTitClassTit(BindListaRequTipoTitClassTit bindListaRequTipoTitClassTit) {
		getBindListaRequTipoTitClassTits().add(bindListaRequTipoTitClassTit);
		bindListaRequTipoTitClassTit.setTipoProcTempl(this);

		return bindListaRequTipoTitClassTit;
	}

	public List<BindListaRequisitoEdificioTipoTitClassTit> getBindListaRequisitoEdificioTipoTitClassTits() {
		return this.bindListaRequisitoEdificioTipoTitClassTits;
	}

	public void setBindListaRequisitoEdificioTipoTitClassTits(List<BindListaRequisitoEdificioTipoTitClassTit> bindListaRequisitoEdificioTipoTitClassTits) {
		this.bindListaRequisitoEdificioTipoTitClassTits = bindListaRequisitoEdificioTipoTitClassTits;
	}

	public BindListaRequisitoEdificioTipoTitClassTit addBindListaRequisitoEdificioTipoTitClassTit(BindListaRequisitoEdificioTipoTitClassTit bindListaRequisitoEdificioTipoTitClassTit) {
		getBindListaRequisitoEdificioTipoTitClassTits().add(bindListaRequisitoEdificioTipoTitClassTit);
		bindListaRequisitoEdificioTipoTitClassTit.setTipoProcTempl(this);

		return bindListaRequisitoEdificioTipoTitClassTit;
	}

	public BindListaRequTipoTitClassTit removeBindListaRequTipoTitClassTit(BindListaRequTipoTitClassTit bindListaRequTipoTitClassTit) {
		getBindListaRequClassUdos().remove(bindListaRequTipoTitClassTit);
		bindListaRequTipoTitClassTit.setTipoProcTempl(null);

		return bindListaRequTipoTitClassTit;
	}
	
	public List<BindTipo22Lista> getBindTipo22Listas() {
		return this.bindTipo22Listas;
	}

	public void setBindTipo22Listas(List<BindTipo22Lista> bindTipo22Listas) {
		this.bindTipo22Listas = bindTipo22Listas;
	}

	public BindTipo22Lista addBindTipo22Lista(BindTipo22Lista bindTipo22Lista) {
		getBindTipo22Listas().add(bindTipo22Lista);
		bindTipo22Lista.setTipoProcTempl(this);

		return bindTipo22Lista;
	}

	public BindTipo22Lista removeBindTipo22Lista(BindTipo22Lista bindTipo22Lista) {
		getBindTipo22Listas().remove(bindTipo22Lista);
		bindTipo22Lista.setTipoProcTempl(null);

		return bindTipo22Lista;
	}

	public List<BindUoProcLista> getBindUoProcListas() {
		return this.bindUoProcListas;
	}

	public void setBindUoProcListas(List<BindUoProcLista> bindUoProcListas) {
		this.bindUoProcListas = bindUoProcListas;
	}

	public BindUoProcLista addBindUoProcLista(BindUoProcLista bindUoProcLista) {
		getBindUoProcListas().add(bindUoProcLista);
		bindUoProcLista.setTipoProcTempl(this);

		return bindUoProcLista;
	}

	public BindUoProcLista removeBindUoProcLista(BindUoProcLista bindUoProcLista) {
		getBindUoProcListas().remove(bindUoProcLista);
		bindUoProcLista.setTipoProcTempl(null);

		return bindUoProcLista;
	}

	public List<DeliberaTempl> getDeliberaTempls() {
		return this.deliberaTempls;
	}

	public void setDeliberaTempls(List<DeliberaTempl> deliberaTempls) {
		this.deliberaTempls = deliberaTempls;
	}

	public DeliberaTempl addDeliberaTempl(DeliberaTempl deliberaTempl) {
		getDeliberaTempls().add(deliberaTempl);
		deliberaTempl.setTipoProcTempl(this);

		return deliberaTempl;
	}

	public DeliberaTempl removeDeliberaTempl(DeliberaTempl deliberaTempl) {
		getDeliberaTempls().remove(deliberaTempl);
		deliberaTempl.setTipoProcTempl(null);

		return deliberaTempl;
	}

	public List<DomandaInst> getDomandaInsts() {
		return this.domandaInsts;
	}

	public void setDomandaInsts(List<DomandaInst> domandaInsts) {
		this.domandaInsts = domandaInsts;
	}

	public DomandaInst addDomandaInst(DomandaInst domandaInst) {
		getDomandaInsts().add(domandaInst);
		domandaInst.setTipoProcTempl(this);

		return domandaInst;
	}

	public DomandaInst removeDomandaInst(DomandaInst domandaInst) {
		getDomandaInsts().remove(domandaInst);
		domandaInst.setTipoProcTempl(null);

		return domandaInst;
	}

	public List<EsitoTempl> getEsitoTempls() {
		return this.esitoTempls;
	}

	public void setEsitoTempls(List<EsitoTempl> esitoTempls) {
		this.esitoTempls = esitoTempls;
	}

	public EsitoTempl addEsitoTempl(EsitoTempl esitoTempl) {
		getEsitoTempls().add(esitoTempl);
		esitoTempl.setTipoProcTempl(this);

		return esitoTempl;
	}

	public EsitoTempl removeEsitoTempl(EsitoTempl esitoTempl) {
		getEsitoTempls().remove(esitoTempl);
		esitoTempl.setTipoProcTempl(null);

		return esitoTempl;
	}

	public List<TipologiaRichiedente> getTipologiaRichiedentes() {
		return this.tipologiaRichiedentes;
	}

	public void setTipologiaRichiedentes(List<TipologiaRichiedente> tipologiaRichiedentes) {
		this.tipologiaRichiedentes = tipologiaRichiedentes;
	}

	public List<TipoDocTempl> getTipoDocTempls() {
		return this.tipoDocTempls;
	}

	public void setTipoDocTempls(List<TipoDocTempl> tipoDocTempls) {
		this.tipoDocTempls = tipoDocTempls;
	}

	public List<ListaRequisitiTempl> getListaRequisitiTemplStrutture() {
		return listaRequisitiTemplStrutture;
	}

	public void setListaRequisitiTemplStrutture(
			List<ListaRequisitiTempl> listaRequisitiTemplStrutture) {
		this.listaRequisitiTemplStrutture = listaRequisitiTemplStrutture;
	}

	public String getTipoWf() {
		return tipoWf;
	}

	public void setTipoWf(String tipoWf) {
		this.tipoWf = tipoWf;
	}

}