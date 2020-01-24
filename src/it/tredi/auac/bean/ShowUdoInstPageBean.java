package it.tredi.auac.bean;

import java.io.Serializable;
import java.util.List;

import it.tredi.auac.orm.entity.TipoUdoUtenteTempl;
import it.tredi.auac.orm.entity.UdoInst;

public class ShowUdoInstPageBean implements Serializable {
	private static final long serialVersionUID = -1702880245386532764L;
	
	private UdoInst udoInst;
	private List<TipoUdoUtenteTempl> tipiUdoUtenteTemplPerTipoTitolareDomandaCorrente;
	private String folderStatus;

	public UdoInst getUdoInst() {
		return udoInst;
	}

	public void setUdoInst(UdoInst udoInst) {
		this.udoInst = udoInst;
	}

	public List<TipoUdoUtenteTempl> getTipiUdoUtenteTemplPerTipoTitolareDomandaCorrente() {
		return tipiUdoUtenteTemplPerTipoTitolareDomandaCorrente;
	}

	public void setTipiUdoUtenteTemplPerTipoTitolareDomandaCorrente(
			List<TipoUdoUtenteTempl> tipiUdoUtenteTemplPerTipoTitolareDomandaCorrente) {
		this.tipiUdoUtenteTemplPerTipoTitolareDomandaCorrente = tipiUdoUtenteTemplPerTipoTitolareDomandaCorrente;
	}

	public String getFolderStatus() {
		return folderStatus;
	}

	public void setFolderStatus(String folderStatus) {
		this.folderStatus = folderStatus;
	}

}
