package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the CLASSIFICAZIONE_UDO_TEMPL database table.
 * 
 */
@Entity
@Table(name="CLASSIFICAZIONE_UDO_TEMPL")
@NamedEntityGraphs({
	@NamedEntityGraph(name="ClassificazioneUdoTempl.loadAllForShowFolderSearch",
		attributeNodes={
			@NamedAttributeNode("nome"),
			@NamedAttributeNode(value="tipoUdo22Templs", subgraph="tipoUdo22Templs")
		},
		subgraphs={
			@NamedSubgraph(name="tipoUdo22Templs",
				attributeNodes={
						@NamedAttributeNode("clientid")
				})
		}
	)
})
public class ClassificazioneUdoTempl implements Serializable {
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

	@Column(name="ID_CLASSIFICAZIONE_UDO_TEMPL")
	private BigDecimal idClassificazioneUdoTempl;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String nome;

	//bi-directional many-to-one association to BindListaRequClassUdo
	@OneToMany(mappedBy="classificazioneUdoTempl")
	private List<BindListaRequClassUdo> bindListaRequClassUdos;

	//bi-directional many-to-one association to BindUoProcLista
	@OneToMany(mappedBy="classificazioneUdoTempl")
	private List<BindUoProcLista> bindUoProcListas;

	//bi-directional many-to-many association to DirezioneTempl
	@ManyToMany(mappedBy="classificazioneUdoTempls")
	private List<DirezioneTempl> direzioneTempls;

	//non occorre avere questa bidirezionale
	//bi-directional many-to-one association to RequisitoInst
	//@OneToMany(mappedBy="classificazioneUdoTempl")
	//private List<RequisitoInst> requisitoInsts;

	//bi-directional many-to-one association to TipoUdo22Templ
	@OneToMany(mappedBy="classificazioneUdoTempl")
	private List<TipoUdo22Templ> tipoUdo22Templs;

	public ClassificazioneUdoTempl() {
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

	public BigDecimal getIdClassificazioneUdoTempl() {
		return this.idClassificazioneUdoTempl;
	}

	public void setIdClassificazioneUdoTempl(BigDecimal idClassificazioneUdoTempl) {
		this.idClassificazioneUdoTempl = idClassificazioneUdoTempl;
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

	public List<BindListaRequClassUdo> getBindListaRequClassUdos() {
		return this.bindListaRequClassUdos;
	}

	public void setBindListaRequClassUdos(List<BindListaRequClassUdo> bindListaRequClassUdos) {
		this.bindListaRequClassUdos = bindListaRequClassUdos;
	}

	public BindListaRequClassUdo addBindListaRequClassUdo(BindListaRequClassUdo bindListaRequClassUdo) {
		getBindListaRequClassUdos().add(bindListaRequClassUdo);
		bindListaRequClassUdo.setClassificazioneUdoTempl(this);

		return bindListaRequClassUdo;
	}

	public BindListaRequClassUdo removeBindListaRequClassUdo(BindListaRequClassUdo bindListaRequClassUdo) {
		getBindListaRequClassUdos().remove(bindListaRequClassUdo);
		bindListaRequClassUdo.setClassificazioneUdoTempl(null);

		return bindListaRequClassUdo;
	}

	public List<BindUoProcLista> getBindUoProcListas() {
		return this.bindUoProcListas;
	}

	public void setBindUoProcListas(List<BindUoProcLista> bindUoProcListas) {
		this.bindUoProcListas = bindUoProcListas;
	}

	public BindUoProcLista addBindUoProcLista(BindUoProcLista bindUoProcLista) {
		getBindUoProcListas().add(bindUoProcLista);
		bindUoProcLista.setClassificazioneUdoTempl(this);

		return bindUoProcLista;
	}

	public BindUoProcLista removeBindUoProcLista(BindUoProcLista bindUoProcLista) {
		getBindUoProcListas().remove(bindUoProcLista);
		bindUoProcLista.setClassificazioneUdoTempl(null);

		return bindUoProcLista;
	}

	public List<DirezioneTempl> getDirezioneTempls() {
		return this.direzioneTempls;
	}

	public void setDirezioneTempls(List<DirezioneTempl> direzioneTempls) {
		this.direzioneTempls = direzioneTempls;
	}

	//non occorre avere questa bidirezionale
	/*
	public List<RequisitoInst> getRequisitoInsts() {
		return this.requisitoInsts;
	}

	public void setRequisitoInsts(List<RequisitoInst> requisitoInsts) {
		this.requisitoInsts = requisitoInsts;
	}

	public RequisitoInst addRequisitoInst(RequisitoInst requisitoInst) {
		getRequisitoInsts().add(requisitoInst);
		requisitoInst.setClassificazioneUdoTempl(this);

		return requisitoInst;
	}

	public RequisitoInst removeRequisitoInst(RequisitoInst requisitoInst) {
		getRequisitoInsts().remove(requisitoInst);
		requisitoInst.setClassificazioneUdoTempl(null);

		return requisitoInst;
	}*/

	public List<TipoUdo22Templ> getTipoUdo22Templs() {
		return this.tipoUdo22Templs;
	}

	public void setTipoUdo22Templs(List<TipoUdo22Templ> tipoUdo22Templs) {
		this.tipoUdo22Templs = tipoUdo22Templs;
	}

	public TipoUdo22Templ addTipoUdo22Templ(TipoUdo22Templ tipoUdo22Templ) {
		getTipoUdo22Templs().add(tipoUdo22Templ);
		tipoUdo22Templ.setClassificazioneUdoTempl(this);

		return tipoUdo22Templ;
	}

	public TipoUdo22Templ removeTipoUdo22Templ(TipoUdo22Templ tipoUdo22Templ) {
		getTipoUdo22Templs().remove(tipoUdo22Templ);
		tipoUdo22Templ.setClassificazioneUdoTempl(null);

		return tipoUdo22Templ;
	}

}