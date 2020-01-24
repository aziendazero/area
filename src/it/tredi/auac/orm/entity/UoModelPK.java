package it.tredi.auac.orm.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the UO_MODEL database table.
 * 
 */
@Embeddable
public class UoModelPK implements Serializable {
	private static final long serialVersionUID = -5976541547964312165L;

	@Column(name="PROVENIENZA")
	private String provenienza;
	@Column(name="ID")
	//Modifica Organigramma aggiunta id univoco COD_NODO_REGIONALE
	//private BigDecimal id;
	private String id;

	public UoModelPK() {
	}

	public String getProvenienza() {
		return provenienza;
	}
	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	//Modifica Organigramma aggiunta id univoco COD_NODO_REGIONALE
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof UoModelPK)) {
			return false;
		}
		UoModelPK castOther = (UoModelPK)other;
		return 
			this.id == castOther.id && this.provenienza.equals(castOther.provenienza);
	}

	public int hashCode() {
		/*final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.id;
		hash = hash * prime + this.provenienza.hashCode();*/
		
		//Modifica Organigramma aggiunta id univoco COD_NODO_REGIONALE
		return (int) this.provenienza.hashCode() + this.id.hashCode();
	}
	
	public String getClientid() {
		return provenienza + id.toString();
	}
}