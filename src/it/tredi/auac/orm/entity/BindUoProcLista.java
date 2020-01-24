package it.tredi.auac.orm.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * The persistent class for the BIND_UO_PROC_LISTA database table.
 * 
 */
@Entity
@Table(name="BIND_UO_PROC_LISTA")
public class BindUoProcLista implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private BindUoProcListaPK id;

	//bi-directional many-to-one association to ClassificazioneUdoTempl
	@ManyToOne
	@JoinColumn(name="ID_CLASS_UDO_TEMPL_FK")
	private ClassificazioneUdoTempl classificazioneUdoTempl;

	//bi-directional many-to-one association to ListaRequisitiTempl
	@ManyToOne
	@JoinColumn(name="ID_LISTA_FK")
	private ListaRequisitiTempl listaRequisitiTempl;

	//bi-directional many-to-one association to TipoProcTempl
	@ManyToOne
	@JoinColumn(name="ID_TIPO_PROC_FK")
	private TipoProcTempl tipoProcTempl;

	public BindUoProcLista() {
	}

	public BindUoProcListaPK getId() {
		return this.id;
	}

	public void setId(BindUoProcListaPK id) {
		this.id = id;
	}

	public ClassificazioneUdoTempl getClassificazioneUdoTempl() {
		return this.classificazioneUdoTempl;
	}

	public void setClassificazioneUdoTempl(ClassificazioneUdoTempl classificazioneUdoTempl) {
		this.classificazioneUdoTempl = classificazioneUdoTempl;
	}

	public ListaRequisitiTempl getListaRequisitiTempl() {
		return this.listaRequisitiTempl;
	}

	public void setListaRequisitiTempl(ListaRequisitiTempl listaRequisitiTempl) {
		this.listaRequisitiTempl = listaRequisitiTempl;
	}

	public TipoProcTempl getTipoProcTempl() {
		return this.tipoProcTempl;
	}

	public void setTipoProcTempl(TipoProcTempl tipoProcTempl) {
		this.tipoProcTempl = tipoProcTempl;
	}

}