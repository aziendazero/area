package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NamedSubgraph;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the TIPO_UDO_UTENTE_TEMPL database table.
 * 
 */
@Entity
@Table(name="TIPO_UDO_UTENTE_TEMPL")
@NamedQueries({
	@NamedQuery(name="TipoUdoUtenteTempl.findUtentiPerTipoTitolare", query="select r from TipoUdoUtenteTempl r "
				+ "inner join r.utenteModel um inner join um.anagraficaUtenteModel anag "
				+ "where r.tipoTitolareTempl=:tipoTitolareTempl "
				+ "order by anag.nome, anag.cognome")
})
@NamedEntityGraphs({
	@NamedEntityGraph(name="TipoUdoUtenteTempl.utentiConAnagrafica",
		attributeNodes={
			@NamedAttributeNode(value="utenteModel", subgraph="utenteModel"),
			@NamedAttributeNode(value="tipoUdo22Templ", subgraph="tipoUdo22Templ")
		},
		subgraphs={
			@NamedSubgraph(name="utenteModel",
			attributeNodes={
				@NamedAttributeNode(value="anagraficaUtenteModel", subgraph="anagraficaUtenteModel")
			}),
			@NamedSubgraph(name="anagraficaUtenteModel",
				attributeNodes={
					@NamedAttributeNode("cognome"),
					@NamedAttributeNode("nome")
			}),
			@NamedSubgraph(name="tipoUdo22Templ",
			attributeNodes={
				@NamedAttributeNode("clientid")
			})
		}
	)
})	
public class TipoUdoUtenteTempl implements Serializable {
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

	@Column(name="ID_TIPO_UDO_UTENTE_TEMPL")
	private BigDecimal idTipoUdoUtenteTempl;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	//bi-directional many-to-one association to TipoTitolareTempl
	@ManyToOne
	@JoinColumn(name="ID_TIPO_TITOLARE_FK")
	private TipoTitolareTempl tipoTitolareTempl;

	//bi-directional many-to-one association to TipoUdo22Templ
	@ManyToOne
	@JoinColumn(name="ID_TIPO_UDO_22_TEMPL_FK")
	private TipoUdo22Templ tipoUdo22Templ;

	//bi-directional many-to-one association to UtenteModel
	@ManyToOne
	@JoinColumn(name="ID_UTENTE_MODEL_FK")
	private UtenteModel utenteModel;

	//bi-directional many-to-many association to UtenteModel
	@ManyToMany(mappedBy="tipoUdoUtenteTempls2")
	private List<UtenteModel> utenteModels;

	public TipoUdoUtenteTempl() {
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

	public BigDecimal getIdTipoUdoUtenteTempl() {
		return this.idTipoUdoUtenteTempl;
	}

	public void setIdTipoUdoUtenteTempl(BigDecimal idTipoUdoUtenteTempl) {
		this.idTipoUdoUtenteTempl = idTipoUdoUtenteTempl;
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

	public TipoTitolareTempl getTipoTitolareTempl() {
		return this.tipoTitolareTempl;
	}

	public void setTipoTitolareTempl(TipoTitolareTempl tipoTitolareTempl) {
		this.tipoTitolareTempl = tipoTitolareTempl;
	}

	public TipoUdo22Templ getTipoUdo22Templ() {
		return this.tipoUdo22Templ;
	}

	public void setTipoUdo22Templ(TipoUdo22Templ tipoUdo22Templ) {
		this.tipoUdo22Templ = tipoUdo22Templ;
	}

	public UtenteModel getUtenteModel() {
		return this.utenteModel;
	}

	public void setUtenteModel(UtenteModel utenteModel) {
		this.utenteModel = utenteModel;
	}

	public List<UtenteModel> getUtenteModels() {
		return this.utenteModels;
	}

	public void setUtenteModels(List<UtenteModel> utenteModels) {
		this.utenteModels = utenteModels;
	}

}