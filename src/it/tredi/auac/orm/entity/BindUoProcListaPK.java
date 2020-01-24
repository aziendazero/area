package it.tredi.auac.orm.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the BIND_UO_PROC_LISTA database table.
 * 
 */
@Embeddable
public class BindUoProcListaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_TIPO_PROC_FK", insertable=false, updatable=false)
	private String idTipoProcFk;

	@Column(name="ID_LISTA_FK", insertable=false, updatable=false)
	private String idListaFk;

	@Column(name="ID_CLASS_UDO_TEMPL_FK", insertable=false, updatable=false)
	private String idClassUdoTemplFk;

	@Column(name="SALUTE_MENTALE")
	private String saluteMentale;

	public BindUoProcListaPK() {
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
	public String getSaluteMentale() {
		return this.saluteMentale;
	}
	public void setSaluteMentale(String saluteMentale) {
		this.saluteMentale = saluteMentale;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BindUoProcListaPK)) {
			return false;
		}
		BindUoProcListaPK castOther = (BindUoProcListaPK)other;
		return 
			this.idTipoProcFk.equals(castOther.idTipoProcFk)
			&& this.idListaFk.equals(castOther.idListaFk)
			&& this.idClassUdoTemplFk.equals(castOther.idClassUdoTemplFk)
			&& this.saluteMentale.equals(castOther.saluteMentale);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idTipoProcFk.hashCode();
		hash = hash * prime + this.idListaFk.hashCode();
		hash = hash * prime + this.idClassUdoTemplFk.hashCode();
		hash = hash * prime + this.saluteMentale.hashCode();
		
		return hash;
	}
}