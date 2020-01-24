package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * The persistent class for the BIND_UDO_DISCIPLINA database table.
 * 
 */
@Entity
@Table(name="BIND_UDO_DISCIPLINA")
public class BindUdoDisciplina implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	@Column(name="POSTI_LETTO")
	private BigDecimal postiLetto;

	@Column(name="POSTI_LETTO_EXTRA")
	private BigDecimal postiLettoExtra;

	@Column(name="POSTI_LETTO_OBI")
	private BigDecimal postiLettoObi;

	//Aggiunta inde 20160906
	@Column(name="POSTI_LETTO_ACC")
	private BigDecimal postiLettoAcc;

	//Aggiunta inde 20160906
	@Column(name="POSTI_LETTO_OBI_ACC")
	private BigDecimal postiLettoObiAcc;
	
	//bi-directional many-to-one association to DisciplinaTempl
	@ManyToOne
	@JoinColumn(name="ID_DISCIPLINA_FK", insertable=false, updatable=false)
	private DisciplinaTempl disciplinaTempl;

	//bi-directional many-to-one association to UdoModel
	@ManyToOne
	@JoinColumn(name="ID_UDO_FK", insertable=false, updatable=false)
	private UdoModel udoModel;

	@ManyToOne
	@JoinColumn(name="ID_AMBITO_FK", insertable=false, updatable=false)
	private AmbitoTempl ambitoTempl;

	public BindUdoDisciplina() {
	}

	public String getClientid() {
		return clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public AmbitoTempl getAmbitoTempl() {
		return ambitoTempl;
	}

	public void setAmbitoTempl(AmbitoTempl ambitoTempl) {
		this.ambitoTempl = ambitoTempl;
	}

	public BigDecimal getPostiLetto() {
		return this.postiLetto;
	}

	public void setPostiLetto(BigDecimal postiLetto) {
		this.postiLetto = postiLetto;
	}

	public BigDecimal getPostiLettoExtra() {
		return this.postiLettoExtra;
	}

	public void setPostiLettoExtra(BigDecimal postiLettoExtra) {
		this.postiLettoExtra = postiLettoExtra;
	}

	public BigDecimal getPostiLettoObi() {
		return this.postiLettoObi;
	}

	public void setPostiLettoObi(BigDecimal postiLettoObi) {
		this.postiLettoObi = postiLettoObi;
	}

	public BigDecimal getPostiLettoAcc() {
		return this.postiLettoAcc;
	}

	public void setPostiLettoAcc(BigDecimal postiLettoAcc) {
		this.postiLettoAcc = postiLettoAcc;
	}

	public BigDecimal getPostiLettoObiAcc() {
		return this.postiLettoObiAcc;
	}

	public void setPostiLettoObiAcc(BigDecimal postiLettoObiAcc) {
		this.postiLettoObiAcc = postiLettoObiAcc;
	}
	
	public DisciplinaTempl getDisciplinaTempl() {
		return this.disciplinaTempl;
	}

	public void setDisciplinaTempl(DisciplinaTempl disciplinaTempl) {
		this.disciplinaTempl = disciplinaTempl;
	}

	public UdoModel getUdoModel() {
		return this.udoModel;
	}

	public void setUdoModel(UdoModel udoModel) {
		this.udoModel = udoModel;
	}

	public String getDescr() {
		if(this.getDisciplinaTempl() != null) {
			return this.getDisciplinaTempl().getDescr();
		} else if(this.getAmbitoTempl() != null) {
			return this.getAmbitoTempl().getDescr();
		}
		return "";
	}
	
	public String getFullDescr() {
		//disciplinaInfo.setCodice(bindUdoDisc.getDisciplinaTempl().getCodice());
		//disciplinaInfo.setDescr(bindUdoDisc.getDisciplinaTempl().getDescr());
		String val = "";
		if(this.getDisciplinaTempl() != null)
			val = this.getDisciplinaTempl().getCodice() + " - "  + this.getDisciplinaTempl().getDescr();
		if(this.getAmbitoTempl() != null)
			val = this.getAmbitoTempl().getDescr();
		if(this.getPostiLetto() != null)
			val += ", posti letto AU: " + this.getPostiLetto();
		if(this.getPostiLettoAcc() != null)
			val += ", posti letto AC: " + this.getPostiLettoAcc();
		if(this.getPostiLettoExtra() != null)
			val += ", posti letto extra Regione: " + this.getPostiLettoExtra();
		if(this.getPostiLettoObi() != null)
			val += ", posti tecnici OBI: " + this.getPostiLettoObi();
		//Questo campo non e' piu' gestito
//		if(this.getPostiLettoObiAcc() != null)
//			val += ", posti tecnici OBI AC: " + this.getPostiLettoObiAcc();

		return val;
	}

}