package it.tredi.auac.orm.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * The persistent class for the BIND_TIPO_22_LISTA database table.
 * 
 */
@Entity
@Table(name="BIND_TIPO_22_LISTA")
public class BindTipo22Lista implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private BindTipo22ListaPK id;

	//bi-directional many-to-one association to ListaRequisitiTempl
	@ManyToOne
	@JoinColumn(name="ID_LISTA_FK", insertable=false, updatable=false)
	private ListaRequisitiTempl listaRequisitiTempl;

	//bi-directional many-to-one association to TipoProcTempl
	@ManyToOne
	@JoinColumn(name="ID_TIPO_PROC_FK", insertable=false, updatable=false)
	private TipoProcTempl tipoProcTempl;

	//bi-directional many-to-one association to TipoUdo22Templ
	@ManyToOne
	@JoinColumn(name="ID_TIPO_UDO_22_FK", insertable=false, updatable=false)
	private TipoUdo22Templ tipoUdo22Templ;

	public BindTipo22Lista() {
	}

	public BindTipo22ListaPK getId() {
		return this.id;
	}

	public void setId(BindTipo22ListaPK id) {
		this.id = id;
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

	public TipoUdo22Templ getTipoUdo22Templ() {
		return this.tipoUdo22Templ;
	}

	public void setTipoUdo22Templ(TipoUdo22Templ tipoUdo22Templ) {
		this.tipoUdo22Templ = tipoUdo22Templ;
	}

}