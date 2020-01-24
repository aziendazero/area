package it.tredi.auac.orm.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the BIND_LISTA_REQUISITO database table.
 * 
 */
@Embeddable
public class BindListaRequisitoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_LISTA_FK", insertable=false, updatable=false)
	private String idListaFk;

	@Column(name="ID_REQUISITO_FK", insertable=false, updatable=false)
	private String idRequisitoFk;

	public BindListaRequisitoPK() {
	}
	public String getIdListaFk() {
		return this.idListaFk;
	}
	public void setIdListaFk(String idListaFk) {
		this.idListaFk = idListaFk;
	}
	public String getIdRequisitoFk() {
		return this.idRequisitoFk;
	}
	public void setIdRequisitoFk(String idRequisitoFk) {
		this.idRequisitoFk = idRequisitoFk;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BindListaRequisitoPK)) {
			return false;
		}
		BindListaRequisitoPK castOther = (BindListaRequisitoPK)other;
		return 
			this.idListaFk.equals(castOther.idListaFk)
			&& this.idRequisitoFk.equals(castOther.idRequisitoFk);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idListaFk.hashCode();
		hash = hash * prime + this.idRequisitoFk.hashCode();
		
		return hash;
	}
}