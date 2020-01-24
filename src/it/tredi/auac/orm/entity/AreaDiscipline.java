package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the AREE_ATTIVITA_TEMPL database table.
 * 
 */
@Entity
@Table(name="RAGG_DISCPL")
@NamedEntityGraphs({
	@NamedEntityGraph(name="AreaDiscipline.loadAllForList",
		attributeNodes={
			@NamedAttributeNode("denominazione"),
		}
	)
})
public class AreaDiscipline implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	private String denominazione;

	private String disabled;

	@Column(name="id_ragg_discipl_templ")
	private BigDecimal idAreaDiscipline;
	
	//bi-directional many-to-one association to UoModel
	@OneToMany(mappedBy="areaDiscipline")
	private List<DisciplinaTempl> disciplinaTempls;

	private BigDecimal ordine;
	
	public AreaDiscipline() {
	}

	public String getClientid() {
		return this.clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public String getDenominazione() {
		return this.denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getDisabled() {
		return this.disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public BigDecimal getIdAreaDiscipline() {
		return this.idAreaDiscipline;
	}

	public void setIdAreaDiscipline(BigDecimal idAreaDiscipline) {
		this.idAreaDiscipline = idAreaDiscipline;
	}

	public List<DisciplinaTempl> getDisciplinaTempls() {
		return this.disciplinaTempls;
	}

	public void setUoModels(List<DisciplinaTempl> disciplinaTempls) {
		this.disciplinaTempls = disciplinaTempls;
	}

	public DisciplinaTempl addDisciplinaTempl(DisciplinaTempl disciplinaTempl) {
		getDisciplinaTempls().add(disciplinaTempl);
		disciplinaTempl.setAreaDiscipline(this);

		return disciplinaTempl;
	}

	public DisciplinaTempl removeDisciplinaTempl(DisciplinaTempl disciplinaTempl) {
		getDisciplinaTempls().remove(disciplinaTempl);
		disciplinaTempl.setAreaDiscipline(null);

		return disciplinaTempl;
	}

	public BigDecimal getOrdine() {
		return ordine;
	}

	public void setOrdine(BigDecimal ordine) {
		this.ordine = ordine;
	}

}