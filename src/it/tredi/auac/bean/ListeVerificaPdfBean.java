package it.tredi.auac.bean;

import it.tredi.auac.orm.entity.ClassificazioneUdoTempl;
import it.tredi.auac.orm.entity.RequisitoTempl;
import it.tredi.auac.orm.entity.TipoProcTempl;
import it.tredi.auac.orm.entity.TipoUdo22Templ;

import java.io.Serializable;
import java.util.List;

public class ListeVerificaPdfBean implements Serializable{
	private static final long serialVersionUID = -1549851745984201294L;

	private TipoProcTempl tipoProcTempl;
	private TipoUdo22Templ tipoUdo22ListaVerificaUdo;
	private ClassificazioneUdoTempl classificazioneUdoTemplListaVerificaUo;
	private List<RequisitoTempl> requisitiTempls;
	
	public ListeVerificaPdfBean() {
	}
	
	public TipoProcTempl getTipoProcTempl() {
		return tipoProcTempl;
	}

	public void setTipoProcTempl(TipoProcTempl tipoProcTempl) {
		this.tipoProcTempl = tipoProcTempl;
	}

	public TipoUdo22Templ getTipoUdo22ListaVerificaUdo() {
		return tipoUdo22ListaVerificaUdo;
	}

	public void setTipoUdo22ListaVerificaUdo(
			TipoUdo22Templ tipoUdo22ListaVerificaUdo) {
		this.tipoUdo22ListaVerificaUdo = tipoUdo22ListaVerificaUdo;
	}

	public ClassificazioneUdoTempl getClassificazioneUdoTemplListaVerificaUo() {
		return classificazioneUdoTemplListaVerificaUo;
	}

	public void setClassificazioneUdoTemplListaVerificaUo(
			ClassificazioneUdoTempl classificazioneUdoTemplListaVerificaUo) {
		this.classificazioneUdoTemplListaVerificaUo = classificazioneUdoTemplListaVerificaUo;
	}

	public List<RequisitoTempl> getRequisitiTempls() {
		return requisitiTempls;
	}

	public void setRequisitiTempls(List<RequisitoTempl> requisitiTempls) {
		this.requisitiTempls = requisitiTempls;
	}

}
