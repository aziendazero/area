package it.tredi.auac.bean;

import it.tredi.auac.orm.entity.TipoUdoTempl;

import java.io.Serializable;
import java.util.List;

public class CheckTipoUdo22DomandaPageBean implements Serializable {

	private static final long serialVersionUID = -5704195165909494051L;
	private List<TipoUdoTempl> tipoUdoTemplL;
	public List<TipoUdoTempl> getTipoUdoTemplL() {
		return tipoUdoTemplL;
	}
	public void setTipoUdoTemplL(List<TipoUdoTempl> tipoUdoTemplL) {
		this.tipoUdoTemplL = tipoUdoTemplL;
	}
	
}
