package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * The persistent class for the BIND_LISTA_REQUISITO database table.
 * 
 */
@Entity
@Table(name="BIND_LISTA_REQUISITO")
public class BindListaRequisito implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private BindListaRequisitoPK id;

	private String annullato;

	private BigDecimal ordine;

	private String validato;

	//bi-directional many-to-one association to ListaRequisitiTempl
	@ManyToOne
	@JoinColumn(name="ID_LISTA_FK")
	private ListaRequisitiTempl listaRequisitiTempl;

	//bi-directional many-to-one association to RequisitoTempl
	@ManyToOne
	@JoinColumn(name="ID_REQUISITO_FK")
	private RequisitoTempl requisitoTempl;

	public BindListaRequisito() {
	}

	public BindListaRequisitoPK getId() {
		return this.id;
	}

	public void setId(BindListaRequisitoPK id) {
		this.id = id;
	}

	public String getAnnullato() {
		return this.annullato;
	}

	public void setAnnullato(String annullato) {
		this.annullato = annullato;
	}

	public BigDecimal getOrdine() {
		return ordine;
	}

	public void setOrdine(BigDecimal ordine) {
		this.ordine = ordine;
	}

	public String getValidato() {
		return this.validato;
	}

	public void setValidato(String validato) {
		this.validato = validato;
	}

	public ListaRequisitiTempl getListaRequisitiTempl() {
		return this.listaRequisitiTempl;
	}

	public void setListaRequisitiTempl(ListaRequisitiTempl listaRequisitiTempl) {
		this.listaRequisitiTempl = listaRequisitiTempl;
	}

	public RequisitoTempl getRequisitoTempl() {
		return this.requisitoTempl;
	}

	public void setRequisitoTempl(RequisitoTempl requisitoTempl) {
		this.requisitoTempl = requisitoTempl;
	}
}