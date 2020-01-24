package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the DISCIPLINA_TEMPL database table.
 * 
 */
@Entity
@Table(name="DISCIPLINA_TEMPL")
public class DisciplinaTempl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	private String annotations;

	//Aggiunta inde 20160705
	//private String attiva;

	private String codice;

	@Temporal(TemporalType.DATE)
	private Date creation;

	private String descr;

	private String disabled;

	@Temporal(TemporalType.DATE)
	private Date ended;

	@Column(name="ID_DISCIPLINA")
	private BigDecimal idDisciplina;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String nome;

	private String tipo;

	//bi-directional many-to-one association to AreaDiscipline
	@ManyToOne
	@JoinColumn(name="ID_RAGG_DISCIPL_TEMPL_FK")
	private AreaDiscipline areaDiscipline;

	//bi-directional many-to-one association to BindUdoDisciplina
	@OneToMany(mappedBy="disciplinaTempl")
	private List<BindUdoDisciplina> bindUdoDisciplinas;

	private BigDecimal ordine;
	
	public DisciplinaTempl() {
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

	public String getCodice() {
		return this.codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
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

	public BigDecimal getIdDisciplina() {
		return this.idDisciplina;
	}

	public void setIdDisciplina(BigDecimal idDisciplina) {
		this.idDisciplina = idDisciplina;
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

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public List<BindUdoDisciplina> getBindUdoDisciplinas() {
		return this.bindUdoDisciplinas;
	}

	public void setBindUdoDisciplinas(List<BindUdoDisciplina> bindUdoDisciplinas) {
		this.bindUdoDisciplinas = bindUdoDisciplinas;
	}

	public BindUdoDisciplina addBindUdoDisciplina(BindUdoDisciplina bindUdoDisciplina) {
		getBindUdoDisciplinas().add(bindUdoDisciplina);
		bindUdoDisciplina.setDisciplinaTempl(this);

		return bindUdoDisciplina;
	}

	public BindUdoDisciplina removeBindUdoDisciplina(BindUdoDisciplina bindUdoDisciplina) {
		getBindUdoDisciplinas().remove(bindUdoDisciplina);
		bindUdoDisciplina.setDisciplinaTempl(null);

		return bindUdoDisciplina;
	}

	public AreaDiscipline getAreaDiscipline() {
		return areaDiscipline;
	}

	public void setAreaDiscipline(AreaDiscipline areaDiscipline) {
		this.areaDiscipline = areaDiscipline;
	}

	public BigDecimal getOrdine() {
		return ordine;
	}

	public void setOrdine(BigDecimal ordine) {
		this.ordine = ordine;
	}

}