package it.tredi.auac.bean;

import it.tredi.auac.orm.entity.UdoInst;
import it.tredi.auac.orm.entity.UdoModel;

import java.io.Serializable;
import java.math.BigDecimal;

public class CompareUdoModelBean implements Serializable {
	private static final long serialVersionUID = -5611111001371452471L;
	private UdoInst udoInst;
	private UdoModel udoModel = null;
	
	public CompareUdoModelBean(UdoInst udoInst) {
		this.udoInst = udoInst;
	}
	
	public UdoInst getUdoInst() {
		return udoInst;
	}
	public void setUdoInst(UdoInst udoInst) {
		this.udoInst = udoInst;
	}
	public UdoModel getUdoModel() {
		return udoModel;
	}
	public void setUdoModel(UdoModel udoModel) {
		this.udoModel = udoModel;
	}
	
	public boolean isChange() {
		if(udoModel == null)
			return false;
		return isDescrChange() || isStabilimentoChange() || isBloccoChange() || isPianoChange() || isProgressivoChange()
				|| isDirettoreSanitarioChange();
	}

	public boolean isDescrChange() {
		if(udoModel != null) {
			return isFieldsChanged(udoInst.getDescr(), udoModel.getDescr());
		}
		return false;
	}
	
	public boolean isStabilimentoChange() {
		if(udoModel != null) {
			return isFieldsChanged(udoInst.getStabilimento(), udoModel.getStabilimento());
		}
		return false;
	}

	public boolean isBloccoChange() {
		if(udoModel != null) {
			return isFieldsChanged(udoInst.getBlocco(), udoModel.getBlocco());
		}
		return false;
	}

	public boolean isPianoChange() {
		if(udoModel != null) {
			return isFieldsChanged(udoInst.getPiano(), udoModel.getPiano());
		}
		return false;
	}
	
	public boolean isProgressivoChange() {
		if(udoModel != null) {
			return isFieldsChanged(udoInst.getProgressivo(), udoModel.getProgressivo());
		}
		return false;
	}
	
	public boolean isDirettoreSanitarioChange() {
		if(udoModel != null) {
			return isFieldsChanged(udoInst.getDirSanitarioCogn(), udoModel.getDirSanitarioCogn()) 
					|| isFieldsChanged(udoInst.getDirSanitarioNome(), udoModel.getDirSanitarioNome())
					|| isFieldsChanged(udoInst.getDirSanitarioCfisc(), udoModel.getDirSanitarioCfisc());
		}
		return false;
	}

	private boolean isFieldsChanged(String value, String oldValue) {
		if(value == null || value.isEmpty()) {
			if(oldValue == null || oldValue.isEmpty())
				return false;
			else
				return true;
		} else {
			if(oldValue == null || oldValue.isEmpty())
				return true;
		}
		return !value.equalsIgnoreCase(oldValue);
	}
	
	private boolean isFieldsChanged(BigDecimal value, BigDecimal oldValue) {
		if(value == null) {
			if(oldValue == null)
				return false;
			else
				return true;
		} else {
			if(oldValue == null)
				return true;
		}
		return value.compareTo(oldValue) != 0;
	}

}
