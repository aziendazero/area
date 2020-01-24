package it.tredi.auac.orm.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the BIND_OPERATION_FILE database table.
 * 
 */
@Embeddable
public class BindOperationFilePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_OPERATION_FK")
	private String idOperationFk;

	@Column(name="ID_FILE_FK")
	private String idFileFk;

	public BindOperationFilePK() {
	}
	public String getIdOperationFk() {
		return this.idOperationFk;
	}
	public void setIdOperationFk(String idOperationFk) {
		this.idOperationFk = idOperationFk;
	}
	public String getIdFileFk() {
		return this.idFileFk;
	}
	public void setIdFileFk(String idFileFk) {
		this.idFileFk = idFileFk;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BindOperationFilePK)) {
			return false;
		}
		BindOperationFilePK castOther = (BindOperationFilePK)other;
		return 
			this.idOperationFk.equals(castOther.idOperationFk)
			&& this.idFileFk.equals(castOther.idFileFk);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idOperationFk.hashCode();
		hash = hash * prime + this.idFileFk.hashCode();
		
		return hash;
	}
}