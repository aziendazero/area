package it.tredi.auac.bean;

import it.tredi.auac.orm.entity.ClassificazioneUdoTempl;
import it.tredi.auac.orm.entity.TipoProcTempl;
import it.tredi.auac.orm.entity.TipoUdo22Templ;

import java.io.Serializable;
import java.util.List;

public class ListaVerificaPageBean implements Serializable {
	private static final long serialVersionUID = -3944969982661457314L;

	private String tipoProcUdoSelected = "";
	//private TipoProcTempl tipoProcTemplListaVerificaUdo;
	private String tipoUdo22Selected = "";
	//private TipoUdo22Templ tipoUdo22ListaVerificaUdo;
	
	private String tipoProcUoSelected = "";
	//private TipoProcTempl tipoProcTemplListaVerificaUo;
	private String classificazioneUdoTemplSelected = "";
	//private ClassificazioneUdoTempl classificazioneUdoTemplListaVerificaUo;
	
	private List<TipoProcTempl> tipiProcTempl;
	private List<TipoUdo22Templ> tipiUdo22Templ;
	private List<ClassificazioneUdoTempl> classificazioniUdoTempl;
	private List<String> messages;
	
	public String getTipoProcUdoSelected() {
		return tipoProcUdoSelected;
	}
	public void setTipoProcUdoSelected(String tipoProcUdoSelected) {
		this.tipoProcUdoSelected = tipoProcUdoSelected;
	}
	/*public TipoProcTempl getTipoProcTemplListaVerificaUdo() {
		return tipoProcTemplListaVerificaUdo;
	}
	public void setTipoProcTemplListaVerificaUdo(
			TipoProcTempl tipoProcTemplListaVerificaUdo) {
		this.tipoProcTemplListaVerificaUdo = tipoProcTemplListaVerificaUdo;
	}*/

	public String getTipoUdo22Selected() {
		return tipoUdo22Selected;
	}
	public void setTipoUdo22Selected(String tipoUdo22Selected) {
		this.tipoUdo22Selected = tipoUdo22Selected;
	}
	/*public TipoUdo22Templ getTipoUdo22ListaVerificaUdo() {
		return tipoUdo22ListaVerificaUdo;
	}
	public void setTipoUdo22ListaVerificaUdo(
			TipoUdo22Templ tipoUdo22ListaVerificaUdo) {
		this.tipoUdo22ListaVerificaUdo = tipoUdo22ListaVerificaUdo;
	}*/
	
	
	public String getTipoProcUoSelected() {
		return tipoProcUoSelected;
	}
	public void setTipoProcUoSelected(String tipoProcUoSelected) {
		this.tipoProcUoSelected = tipoProcUoSelected;
	}
	/*public TipoProcTempl getTipoProcTemplListaVerificaUo() {
		return tipoProcTemplListaVerificaUo;
	}
	public void setTipoProcTemplListaVerificaUo(
			TipoProcTempl tipoProcTemplListaVerificaUo) {
		this.tipoProcTemplListaVerificaUo = tipoProcTemplListaVerificaUo;
	}*/

	public String getClassificazioneUdoTemplSelected() {
		return classificazioneUdoTemplSelected;
	}
	public void setClassificazioneUdoTemplSelected(
			String classificazioneUdoTemplSelected) {
		this.classificazioneUdoTemplSelected = classificazioneUdoTemplSelected;
	}
	/*public ClassificazioneUdoTempl getClassificazioneUdoTemplListaVerificaUo() {
		return classificazioneUdoTemplListaVerificaUo;
	}
	public void setClassificazioneUdoTemplListaVerificaUo(
			ClassificazioneUdoTempl classificazioneUdoTemplListaVerificaUo) {
		this.classificazioneUdoTemplListaVerificaUo = classificazioneUdoTemplListaVerificaUo;
	}*/
	
	public List<TipoProcTempl> getTipiProcTempl() {
		return tipiProcTempl;
	}
	public void setTipiProcTempl(List<TipoProcTempl> tipiProcTempl) {
		this.tipiProcTempl = tipiProcTempl;
	}
	public List<TipoUdo22Templ> getTipiUdo22Templ() {
		return tipiUdo22Templ;
	}
	public void setTipiUdo22Templ(List<TipoUdo22Templ> tipiUdo22Templ) {
		this.tipiUdo22Templ = tipiUdo22Templ;
	}
	public List<ClassificazioneUdoTempl> getClassificazioniUdoTempl() {
		return classificazioniUdoTempl;
	}
	public void setClassificazioniUdoTempl(
			List<ClassificazioneUdoTempl> classificazioniUdoTempl) {
		this.classificazioniUdoTempl = classificazioniUdoTempl;
	}
	
	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}


	
}
