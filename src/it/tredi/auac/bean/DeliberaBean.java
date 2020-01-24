package it.tredi.auac.bean;

import java.io.Serializable;
import java.util.Date;

import it.tredi.auac.orm.entity.TipoAtto;

public class DeliberaBean implements Serializable {
	private static final long serialVersionUID = 1042217635040098595L;
	
	private long processInstanceId;
	private TipoAtto tipoAtto;
	private String numeroDelibera;
	private Integer annoDelibera;
	private Date dataInizioValidita;
	private Date dataFineValidita;
	private DeliberaFileBean deliberaFileBean;
	
	public DeliberaBean() {
		
	}
	
	public long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public TipoAtto getTipoAtto() {
		return tipoAtto;
	}
	public void setTipoAtto(TipoAtto tipoAtto) {
		this.tipoAtto = tipoAtto;
	}
	public String getNumeroDelibera() {
		return numeroDelibera;
	}
	public void setNumeroDelibera(String numeroDelibera) {
		this.numeroDelibera = numeroDelibera;
	}
	public Integer getAnnoDelibera() {
		return annoDelibera;
	}
	public void setAnnoDelibera(Integer annoDelibera) {
		this.annoDelibera = annoDelibera;
	}
	public Date getDataInizioValidita() {
		return dataInizioValidita;
	}
	public void setDataInizioValidita(Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}
	public Date getDataFineValidita() {
		return dataFineValidita;
	}
	public void setDataFineValidita(Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	public DeliberaFileBean getDeliberaFileBean() {
		return deliberaFileBean;
	}

	public void setDeliberaFileBean(DeliberaFileBean deliberaFileBean) {
		this.deliberaFileBean = deliberaFileBean;
	}

}
