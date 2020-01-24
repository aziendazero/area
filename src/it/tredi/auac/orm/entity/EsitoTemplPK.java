package it.tredi.auac.orm.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the ESITO_TEMPL database table.
 * 
 */
@Embeddable
public class EsitoTemplPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private String clientid;

	@Column(name="ID_TIPO_PROC_FK")
	private String idTipoProcFk;

	public EsitoTemplPK() {
	}
	public String getClientid() {
		return this.clientid;
	}
	public void setClientid(String clientid) {
		this.clientid = clientid;
	}
	public String getIdTipoProcFk() {
		return this.idTipoProcFk;
	}
	public void setIdTipoProcFk(String idTipoProcFk) {
		this.idTipoProcFk = idTipoProcFk;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof EsitoTemplPK)) {
			return false;
		}
		EsitoTemplPK castOther = (EsitoTemplPK)other;
		return 
			this.clientid.equals(castOther.clientid)
			&& this.idTipoProcFk.equals(castOther.idTipoProcFk);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.clientid.hashCode();
		hash = hash * prime + this.idTipoProcFk.hashCode();
		
		return hash;
	}
}