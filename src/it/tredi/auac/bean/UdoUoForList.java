package it.tredi.auac.bean;

import it.tredi.auac.orm.entity.BindUdoDisciplina;
import it.tredi.auac.orm.entity.BrancaTempl;
import it.tredi.auac.orm.entity.TipoUdoTempl;
import it.tredi.auac.orm.entity.UdoModel;
import it.tredi.auac.orm.entity.UoModel;

import java.io.Serializable;
import java.util.List;

public class UdoUoForList implements Serializable {
	private static final long serialVersionUID = 8195206824668906178L;
	private UdoModel udoModel = null;
	private UoModel uoModel = null;

	public UdoUoForList(UdoModel udoModel) {
		this.udoModel = udoModel;
	}
	
	public UdoUoForList(UoModel uoModel) {
		this.uoModel = uoModel;		
	}

	public boolean isUdo() {
		if(udoModel != null)
			return true;
		return false;
	}
	
	public String getClientId() {
		if(udoModel != null)
			return udoModel.getClientid();
		//return uoModel.getClientid();
		return uoModel.getId().getClientid();
	}

	public String getIdUnivoco() {
		if(udoModel != null)
			return udoModel.getIdUnivoco();
		return "";
	}

	public String getDescr() {
		if(udoModel != null)
			return udoModel.getDescr();
		return uoModel.getDenominazioneConId();
	}

	public TipoUdoTempl getTipoUdoTempl() {
		if(udoModel != null)
			return udoModel.getTipoUdoTempl();		
		return null;
	}
	
	public List<BindUdoDisciplina> getBindUdoDisciplinas() {
		if(udoModel != null)
			return udoModel.getBindUdoDisciplinas();		
		return null;
	}
	
	public List<BrancaTempl> getBrancaTempls() {
		if(udoModel != null)
			return udoModel.getBrancaTempls();		
		return null;
	}

	public UdoModel getUdoModel() {
		return udoModel;
	}

	public UoModel getUoModel() {
		return uoModel;
	}

	public String getUoClientId() {
		if(udoModel != null) {
			//return udoModel.getUoModel().getClientid();
			return udoModel.getUoModel().getId().getClientid();
		}
		//return uoModel.getClientid();
		return uoModel.getId().getClientid();
	}
}