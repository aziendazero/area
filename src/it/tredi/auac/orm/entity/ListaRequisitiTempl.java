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


/**
 * The persistent class for the LISTA_REQUISITI_TEMPL database table.
 * 
 */
@Entity
@Table(name="LISTA_REQUISITI_TEMPL")
public class ListaRequisitiTempl implements Serializable {
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

	@Column(name="ID_LISTA")
	private BigDecimal idLista;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String nome;

	//bi-directional many-to-one association to BindListaRequisito
	@OneToMany(mappedBy="listaRequisitiTempl")
	private List<BindListaRequisito> bindListaRequisitos;

	//bi-directional many-to-one association to BindListaRequClassUdo
	@OneToMany(mappedBy="listaRequisitiTempl")
	private List<BindListaRequClassUdo> bindListaRequClassUdos;

	//bi-directional many-to-one association to BindTipo22Lista
	@OneToMany(mappedBy="listaRequisitiTempl")
	private List<BindTipo22Lista> bindTipo22Listas;

	//bi-directional many-to-one association to BindUoProcLista
	@OneToMany(mappedBy="listaRequisitiTempl")
	private List<BindUoProcLista> bindUoProcListas;

	//bi-directional many-to-many association to DeliberaTempl
	@ManyToMany
	@JoinTable(
		name="BIND_LISTA_REQ_DELIBERE"
		, joinColumns={
			@JoinColumn(name="ID_LISTA_FK")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_DELIBERA_FK")
			}
		)
	private List<DeliberaTempl> deliberaTempls1;

	//bi-directional many-to-one association to DeliberaTempl
	@ManyToOne
	@JoinColumn(name="ID_DELIBERA_TEMPL")
	private DeliberaTempl deliberaTempl;

	//bi-directional many-to-many association to DeliberaTempl
	@ManyToMany
	@JoinTable(
		name="BIND_LISTA_REQ_DELIBERE_ADM"
		, joinColumns={
			@JoinColumn(name="ID_LISTA_REQUISITI_FK")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_DELIBERA_FK")
			}
		)
	private List<DeliberaTempl> deliberaTempls2;

	//bi-directional many-to-one association to MonitorRequisiti
	@OneToMany(mappedBy="listaRequisitiTempl")
	private List<MonitorRequisiti> monitorRequisitis;

	//bi-directional many-to-many association to RequisitoTempl
	/*@ManyToMany(mappedBy="listaRequisitiTempls")
	private List<RequisitoTempl> requisitoTempls;*/

	public ListaRequisitiTempl() {
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

	public BigDecimal getIdLista() {
		return this.idLista;
	}

	public void setIdLista(BigDecimal idLista) {
		this.idLista = idLista;
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

	public List<BindListaRequisito> getBindListaRequisitos() {
		return this.bindListaRequisitos;
	}

	public void setBindListaRequisitos(List<BindListaRequisito> bindListaRequisitos) {
		this.bindListaRequisitos = bindListaRequisitos;
	}

	public BindListaRequisito addBindListaRequisito(BindListaRequisito bindListaRequisito) {
		getBindListaRequisitos().add(bindListaRequisito);
		bindListaRequisito.setListaRequisitiTempl(this);

		return bindListaRequisito;
	}

	public BindListaRequisito removeBindListaRequisito(BindListaRequisito bindListaRequisito) {
		getBindListaRequisitos().remove(bindListaRequisito);
		bindListaRequisito.setListaRequisitiTempl(null);

		return bindListaRequisito;
	}

	public List<BindListaRequClassUdo> getBindListaRequClassUdos() {
		return this.bindListaRequClassUdos;
	}

	public void setBindListaRequClassUdos(List<BindListaRequClassUdo> bindListaRequClassUdos) {
		this.bindListaRequClassUdos = bindListaRequClassUdos;
	}

	public BindListaRequClassUdo addBindListaRequClassUdo(BindListaRequClassUdo bindListaRequClassUdo) {
		getBindListaRequClassUdos().add(bindListaRequClassUdo);
		bindListaRequClassUdo.setListaRequisitiTempl(this);

		return bindListaRequClassUdo;
	}

	public BindListaRequClassUdo removeBindListaRequClassUdo(BindListaRequClassUdo bindListaRequClassUdo) {
		getBindListaRequClassUdos().remove(bindListaRequClassUdo);
		bindListaRequClassUdo.setListaRequisitiTempl(null);

		return bindListaRequClassUdo;
	}

	public List<BindTipo22Lista> getBindTipo22Listas() {
		return this.bindTipo22Listas;
	}

	public void setBindTipo22Listas(List<BindTipo22Lista> bindTipo22Listas) {
		this.bindTipo22Listas = bindTipo22Listas;
	}

	public BindTipo22Lista addBindTipo22Lista(BindTipo22Lista bindTipo22Lista) {
		getBindTipo22Listas().add(bindTipo22Lista);
		bindTipo22Lista.setListaRequisitiTempl(this);

		return bindTipo22Lista;
	}

	public BindTipo22Lista removeBindTipo22Lista(BindTipo22Lista bindTipo22Lista) {
		getBindTipo22Listas().remove(bindTipo22Lista);
		bindTipo22Lista.setListaRequisitiTempl(null);

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
		bindUoProcLista.setListaRequisitiTempl(this);

		return bindUoProcLista;
	}

	public BindUoProcLista removeBindUoProcLista(BindUoProcLista bindUoProcLista) {
		getBindUoProcListas().remove(bindUoProcLista);
		bindUoProcLista.setListaRequisitiTempl(null);

		return bindUoProcLista;
	}

	public List<DeliberaTempl> getDeliberaTempls1() {
		return this.deliberaTempls1;
	}

	public void setDeliberaTempls1(List<DeliberaTempl> deliberaTempls1) {
		this.deliberaTempls1 = deliberaTempls1;
	}

	public DeliberaTempl getDeliberaTempl() {
		return this.deliberaTempl;
	}

	public void setDeliberaTempl(DeliberaTempl deliberaTempl) {
		this.deliberaTempl = deliberaTempl;
	}

	public List<DeliberaTempl> getDeliberaTempls2() {
		return this.deliberaTempls2;
	}

	public void setDeliberaTempls2(List<DeliberaTempl> deliberaTempls2) {
		this.deliberaTempls2 = deliberaTempls2;
	}

	public List<MonitorRequisiti> getMonitorRequisitis() {
		return this.monitorRequisitis;
	}

	public void setMonitorRequisitis(List<MonitorRequisiti> monitorRequisitis) {
		this.monitorRequisitis = monitorRequisitis;
	}

	public MonitorRequisiti addMonitorRequisiti(MonitorRequisiti monitorRequisiti) {
		getMonitorRequisitis().add(monitorRequisiti);
		monitorRequisiti.setListaRequisitiTempl(this);

		return monitorRequisiti;
	}

	public MonitorRequisiti removeMonitorRequisiti(MonitorRequisiti monitorRequisiti) {
		getMonitorRequisitis().remove(monitorRequisiti);
		monitorRequisiti.setListaRequisitiTempl(null);

		return monitorRequisiti;
	}

	/*public List<RequisitoTempl> getRequisitoTempls() {
		return this.requisitoTempls;
	}

	public void setRequisitoTempls(List<RequisitoTempl> requisitoTempls) {
		this.requisitoTempls = requisitoTempls;
	}*/

}