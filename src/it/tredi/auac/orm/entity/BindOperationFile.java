package it.tredi.auac.orm.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * The persistent class for the BIND_OPERATION_FILE database table.
 * 
 */
@Entity
@Table(name="BIND_OPERATION_FILE")
public class BindOperationFile implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private BindOperationFilePK id;

	private String phase;

	//bi-directional many-to-one association to OperationAppl
	@ManyToOne
	@JoinColumn(name="ID_OPERATION_FK", insertable=false, updatable=false)
	private OperationAppl operationAppl;

	public BindOperationFile() {
	}

	public BindOperationFilePK getId() {
		return this.id;
	}

	public void setId(BindOperationFilePK id) {
		this.id = id;
	}

	public String getPhase() {
		return this.phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public OperationAppl getOperationAppl() {
		return this.operationAppl;
	}

	public void setOperationAppl(OperationAppl operationAppl) {
		this.operationAppl = operationAppl;
	}

}