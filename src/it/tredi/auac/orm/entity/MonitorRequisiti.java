package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the MONITOR_REQUISITI database table.
 * 
 */
@Entity
@Table(name="MONITOR_REQUISITI")
public class MonitorRequisiti implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	private String annotations;

	@Temporal(TemporalType.DATE)
	private Date creation;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_OPERAZIONE")
	private Date dataOperazione;

	private String descr;

	private String descrizione;

	private String disabled;

	@Temporal(TemporalType.DATE)
	private Date ended;

	@Column(name="ID_MONITOR_REQUISITI")
	private BigDecimal idMonitorRequisiti;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	@Column(name="NOME_LISTA")
	private String nomeLista;

	@Column(name="NOME_REQUISITO")
	private String nomeRequisito;

	private String operazione;

	private String username;

	//bi-directional many-to-one association to ListaRequisitiTempl
	@ManyToOne
	@JoinColumn(name="ID_LISTA_REQUISITI_TEMPL")
	private ListaRequisitiTempl listaRequisitiTempl;

	//bi-directional many-to-one association to RequisitoTempl
	@ManyToOne
	@JoinColumn(name="ID_REQUISITO_TEMPL")
	private RequisitoTempl requisitoTempl;

	public MonitorRequisiti() {
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

	public Date getDataOperazione() {
		return this.dataOperazione;
	}

	public void setDataOperazione(Date dataOperazione) {
		this.dataOperazione = dataOperazione;
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

	public BigDecimal getIdMonitorRequisiti() {
		return this.idMonitorRequisiti;
	}

	public void setIdMonitorRequisiti(BigDecimal idMonitorRequisiti) {
		this.idMonitorRequisiti = idMonitorRequisiti;
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

	public String getNomeLista() {
		return this.nomeLista;
	}

	public void setNomeLista(String nomeLista) {
		this.nomeLista = nomeLista;
	}

	public String getNomeRequisito() {
		return this.nomeRequisito;
	}

	public void setNomeRequisito(String nomeRequisito) {
		this.nomeRequisito = nomeRequisito;
	}

	public String getOperazione() {
		return this.operazione;
	}

	public void setOperazione(String operazione) {
		this.operazione = operazione;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ListaRequisitiTempl getListaRequisitiTempl() {
		return this.listaRequisitiTempl;
	}

	public void setListaRequisitiTempl(ListaRequisitiTempl listaRequisitiTempl) {
		this.listaRequisitiTempl = listaRequisitiTempl;
	}

	public RequisitoTempl getRequisitoTempl() {
		return this.requisitoTempl;
	}

	public void setRequisitoTempl(RequisitoTempl requisitoTempl) {
		this.requisitoTempl = requisitoTempl;
	}

}