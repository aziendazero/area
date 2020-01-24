package it.tredi.auac.orm.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the BIND_LISTA_REQU_CLASS_UDO database table.
 * 
 */
@Embeddable
public class BindListaRequClassUdoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_TIPO_PROC_FK", insertable=false, updatable=false)
	private String idTipoProcFk;

	@Column(name="ID_LISTA_FK", insertable=false, updatable=false)
	private String idListaFk;

	@Column(name="ID_CLASS_UDO_TEMPL_FK", insertable=false, updatable=false)
	private String idClassUdoTemplFk;

	public BindListaRequClassUdoPK() {
	}
	public String getIdTipoProcFk() {
		return this.idTipoProcFk;
	}
	public void setIdTipoProcFk(String idTipoProcFk) {
		this.idTipoProcFk = idTipoProcFk;
	}
	public String getIdListaFk() {
		return this.idListaFk;
	}
	public void setIdListaFk(String idListaFk) {
		this.idListaFk = idListaFk;
	}
	public String getIdClassUdoTemplFk() {
		return this.idClassUdoTemplFk;
	}
	public void setIdClassUdoTemplFk(String idClassUdoTemplFk) {
		this.idClassUdoTemplFk = idClassUdoTemplFk;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BindListaRequClassUdoPK)) {
			return false;
		}
		BindListaRequClassUdoPK castOther = (BindListaRequClassUdoPK)other;
		return 
			this.idTipoProcFk.equals(castOther.idTipoProcFk)
			&& this.idListaFk.equals(castOther.idListaFk)
			&& this.idClassUdoTemplFk.equals(castOther.idClassUdoTemplFk);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idTipoProcFk.hashCode();
		hash = hash * prime + this.idListaFk.hashCode();
		hash = hash * prime + this.idClassUdoTemplFk.hashCode();
		
		return hash;
	}
}