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
 * The persistent class for the REQUISITO_TEMPL database table.
 * 
 */
@Entity
@Table(name="REQUISITO_TEMPL")
public class RequisitoTempl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	private String annotations;

	private String annullato;

	@Temporal(TemporalType.DATE)
	private Date creation;

	private String descr;

	private String disabled;

	@Temporal(TemporalType.DATE)
	private Date ended;

	@Column(name="ID_REQUISITO")
	private BigDecimal idRequisito;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String nome;

	private String testo;

	private String tipo;

	@Column(name="TIPO_SPECIFICO")
	private String tipoSpecifico;

	private String validato;

	//bi-directional many-to-one association to BindListaRequisito
	@OneToMany(mappedBy="requisitoTempl")
	private List<BindListaRequisito> bindListaRequisitos;

	//bi-directional many-to-one association to MonitorRequisiti
	@OneToMany(mappedBy="requisitoTempl")
	private List<MonitorRequisiti> monitorRequisitis;

	//bi-directional many-to-one association to RequisitoInst
	@OneToMany(mappedBy="requisitoTempl")
	private List<RequisitoInst> requisitoInsts;

	//bi-directional many-to-many association to ListaRequisitiTempl
	@ManyToMany
	@JoinTable(
		name="BIND_LISTA_REQUISITO"
		, joinColumns={
			@JoinColumn(name="ID_REQUISITO_FK")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_LISTA_FK")
			}
		)
	private List<ListaRequisitiTempl> listaRequisitiTempls;

	//bi-directional many-to-one association to TipoRisposta
	@ManyToOne
	@JoinColumn(name="ID_TIPO_RISPOSTA_FK")
	private TipoRisposta tipoRisposta;

	public RequisitoTempl() {
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

	public String getAnnullato() {
		return this.annullato;
	}

	public void setAnnullato(String annullato) {
		this.annullato = annullato;
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

	public BigDecimal getIdRequisito() {
		return this.idRequisito;
	}

	public void setIdRequisito(BigDecimal idRequisito) {
		this.idRequisito = idRequisito;
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

	public String getTesto() {
		return this.testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTipoSpecifico() {
		return this.tipoSpecifico;
	}

	public void setTipoSpecifico(String tipoSpecifico) {
		this.tipoSpecifico = tipoSpecifico;
	}

	public String getValidato() {
		return this.validato;
	}

	public void setValidato(String validato) {
		this.validato = validato;
	}

	public List<BindListaRequisito> getBindListaRequisitos() {
		return this.bindListaRequisitos;
	}

	public void setBindListaRequisitos(List<BindListaRequisito> bindListaRequisitos) {
		this.bindListaRequisitos = bindListaRequisitos;
	}

	public BindListaRequisito addBindListaRequisito(BindListaRequisito bindListaRequisito) {
		getBindListaRequisitos().add(bindListaRequisito);
		bindListaRequisito.setRequisitoTempl(this);

		return bindListaRequisito;
	}

	public BindListaRequisito removeBindListaRequisito(BindListaRequisito bindListaRequisito) {
		getBindListaRequisitos().remove(bindListaRequisito);
		bindListaRequisito.setRequisitoTempl(null);

		return bindListaRequisito;
	}

	public List<MonitorRequisiti> getMonitorRequisitis() {
		return this.monitorRequisitis;
	}

	public void setMonitorRequisitis(List<MonitorRequisiti> monitorRequisitis) {
		this.monitorRequisitis = monitorRequisitis;
	}

	public MonitorRequisiti addMonitorRequisiti(MonitorRequisiti monitorRequisiti) {
		getMonitorRequisitis().add(monitorRequisiti);
		monitorRequisiti.setRequisitoTempl(this);

		return monitorRequisiti;
	}

	public MonitorRequisiti removeMonitorRequisiti(MonitorRequisiti monitorRequisiti) {
		getMonitorRequisitis().remove(monitorRequisiti);
		monitorRequisiti.setRequisitoTempl(null);

		return monitorRequisiti;
	}

	public List<RequisitoInst> getRequisitoInsts() {
		return this.requisitoInsts;
	}

	public void setRequisitoInsts(List<RequisitoInst> requisitoInsts) {
		this.requisitoInsts = requisitoInsts;
	}

	public RequisitoInst addRequisitoInst(RequisitoInst requisitoInst) {
		getRequisitoInsts().add(requisitoInst);
		requisitoInst.setRequisitoTempl(this);

		return requisitoInst;
	}

	public RequisitoInst removeRequisitoInst(RequisitoInst requisitoInst) {
		getRequisitoInsts().remove(requisitoInst);
		requisitoInst.setRequisitoTempl(null);

		return requisitoInst;
	}

	public List<ListaRequisitiTempl> getListaRequisitiTempls() {
		return this.listaRequisitiTempls;
	}

	public void setListaRequisitiTempls(List<ListaRequisitiTempl> listaRequisitiTempls) {
		this.listaRequisitiTempls = listaRequisitiTempls;
	}

	public TipoRisposta getTipoRisposta() {
		return this.tipoRisposta;
	}

	public void setTipoRisposta(TipoRisposta tipoRisposta) {
		this.tipoRisposta = tipoRisposta;
	}

}