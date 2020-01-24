package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the OPERATION_APPL database table.
 * 
 */
@Entity
@Table(name="OPERATION_APPL")
public class OperationAppl implements Serializable {
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

	@Column(name="ID_OGGETTO")
	private String idOggetto;

	@Column(name="ID_OPERATION")
	private BigDecimal idOperation;

	@Column(name="ID_USER")
	private String idUser;

	@Column(name="IDENT_OGGETTO")
	private String identOggetto;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String nome;

	private String operator;

	@Column(name="STATE_AFTER")
	private String stateAfter;

	@Column(name="STATE_BEFORE")
	private String stateBefore;

	@Temporal(TemporalType.DATE)
	@Column(name="TIME_STAMP")
	private Date timeStamp;

	@Column(name="TIPO_OGGETTO")
	private String tipoOggetto;

	//bi-directional many-to-one association to BindOperationFile
	@OneToMany(mappedBy="operationAppl")
	private List<BindOperationFile> bindOperationFiles;

	public OperationAppl() {
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

	public String getIdOggetto() {
		return this.idOggetto;
	}

	public void setIdOggetto(String idOggetto) {
		this.idOggetto = idOggetto;
	}

	public BigDecimal getIdOperation() {
		return this.idOperation;
	}

	public void setIdOperation(BigDecimal idOperation) {
		this.idOperation = idOperation;
	}

	public String getIdUser() {
		return this.idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getIdentOggetto() {
		return this.identOggetto;
	}

	public void setIdentOggetto(String identOggetto) {
		this.identOggetto = identOggetto;
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

	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getStateAfter() {
		return this.stateAfter;
	}

	public void setStateAfter(String stateAfter) {
		this.stateAfter = stateAfter;
	}

	public String getStateBefore() {
		return this.stateBefore;
	}

	public void setStateBefore(String stateBefore) {
		this.stateBefore = stateBefore;
	}

	public Date getTimeStamp() {
		return this.timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getTipoOggetto() {
		return this.tipoOggetto;
	}

	public void setTipoOggetto(String tipoOggetto) {
		this.tipoOggetto = tipoOggetto;
	}

	public List<BindOperationFile> getBindOperationFiles() {
		return this.bindOperationFiles;
	}

	public void setBindOperationFiles(List<BindOperationFile> bindOperationFiles) {
		this.bindOperationFiles = bindOperationFiles;
	}

	public BindOperationFile addBindOperationFile(BindOperationFile bindOperationFile) {
		getBindOperationFiles().add(bindOperationFile);
		bindOperationFile.setOperationAppl(this);

		return bindOperationFile;
	}

	public BindOperationFile removeBindOperationFile(BindOperationFile bindOperationFile) {
		getBindOperationFiles().remove(bindOperationFile);
		bindOperationFile.setOperationAppl(null);

		return bindOperationFile;
	}

}