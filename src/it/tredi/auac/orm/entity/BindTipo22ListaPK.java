package it.tredi.auac.orm.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the BIND_TIPO_22_LISTA database table.
 * 
 */
@Embeddable
public class BindTipo22ListaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_TIPO_UDO_22_FK")
	private String idTipoUdo22Fk;

	@Column(name="ID_TIPO_PROC_FK")
	private String idTipoProcFk;

	@Column(name="ID_LISTA_FK")
	private String idListaFk;

	public BindTipo22ListaPK() {
	}
	public String getIdTipoUdo22Fk() {
		return this.idTipoUdo22Fk;
	}
	public void setIdTipoUdo22Fk(String idTipoUdo22Fk) {
		this.idTipoUdo22Fk = idTipoUdo22Fk;
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

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BindTipo22ListaPK)) {
			return false;
		}
		BindTipo22ListaPK castOther = (BindTipo22ListaPK)other;
		return 
			this.idTipoUdo22Fk.equals(castOther.idTipoUdo22Fk)
			&& this.idTipoProcFk.equals(castOther.idTipoProcFk)
			&& this.idListaFk.equals(castOther.idListaFk);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idTipoUdo22Fk.hashCode();
		hash = hash * prime + this.idTipoProcFk.hashCode();
		hash = hash * prime + this.idListaFk.hashCode();
		
		return hash;
	}
}