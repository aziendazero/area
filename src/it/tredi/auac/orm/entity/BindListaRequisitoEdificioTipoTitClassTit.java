package it.tredi.auac.orm.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * The persistent class for the BIND_LISTA_REQU_CLASS_UDO database table.
 * 
 */
@Entity
@Table(name="BIND_LISTA_EDIF_TIPTIT_CLTIT")
public class BindListaRequisitoEdificioTipoTitClassTit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	//bi-directional many-to-one association to ListaRequisitiTempl
	@ManyToOne
	@JoinColumn(name="ID_LISTA_FK")
	private ListaRequisitiTempl listaRequisitiTempl;

	//bi-directional many-to-one association to TipoProcTempl
	@ManyToOne
	@JoinColumn(name="ID_TIPO_PROC_FK")
	private TipoProcTempl tipoProcTempl;

	//bi-directional many-to-one association to TipoTitolareTempl
	@ManyToOne
	@JoinColumn(name="ID_TIPO_TITOLARE_TEMPL_FK")
	private TipoTitolareTempl tipoTitolareTempl;

	//mono-directional many-to-one association to ClassificazioneTempl
	@ManyToOne
	@JoinColumn(name="ID_CLASSIFICAZIONE_TEMPL_FK")
	private ClassificazioneTempl classificazioneTempl;

	public BindListaRequisitoEdificioTipoTitClassTit() {
	}

	public String getClientid() {
		return this.clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public TipoTitolareTempl getTipoTitolareTempl() {
		return this.tipoTitolareTempl;
	}

	public void setTipoTitolareTempl(TipoTitolareTempl tipoTitolareTempl) {
		this.tipoTitolareTempl = tipoTitolareTempl;
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

	public ClassificazioneTempl getClassificazioneTempl() {
		return classificazioneTempl;
	}

	public void setClassificazioneTempl(ClassificazioneTempl classificazioneTempl) {
		this.classificazioneTempl = classificazioneTempl;
	}

}