package it.tredi.auac.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import it.tredi.auac.orm.entity.AttoInst;
import it.tredi.auac.orm.entity.TipoAtto;

public class AttoInstBean implements Serializable {
	private static final long serialVersionUID = 1042217635040098595L;
	
	private AttoInst attoInst;
	private ShowFolderPageBean showFolderPageBean;
	private List<TipoAtto> tipiAtto;
	
	public AttoInstBean() {
		
	}

	public AttoInst getAttoInst() {
		return attoInst;
	}

	public void setAttoInst(AttoInst attoInst) {
		this.attoInst = attoInst;
	}

	public ShowFolderPageBean getShowFolderPageBean() {
		return showFolderPageBean;
	}

	public void setShowFolderPageBean(ShowFolderPageBean showFolderPageBean) {
		this.showFolderPageBean = showFolderPageBean;
	}	

	public List<TipoAtto> getTipiAtto() {
		return tipiAtto;
	}

	public void setTipiAtto(List<TipoAtto> tipiAtto) {
		this.tipiAtto = tipiAtto;
	}
}
