package it.tredi.auac.orm.entity;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.CascadeOnDelete;


/**
 * The persistent class for the UDO_INST database table.
 * 
 */
@Entity
@Table(name="STRUTTURA_INST_WITH_REQ_INFO")
@NamedEntityGraph(name="StrutturaInst.autovalutazioneExportCsv",
	attributeNodes={
		@NamedAttributeNode("denominazione")
	}
)
public class StrutturaInst implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	@Column(name="ID_STRUTTURA_INST")
	private BigDecimal idStrutturaInst;

	@Column(name="DENOMINAZIONE")
	private String denominazione;

	@Column(name="CODICE_PF")
	private String codicePf;

	@Column(name="ID_STRUTTURA_MODEL_FK")
	private String strutturaModelClientid;

	//bi-directional many-to-one association to RequisitoInst
	@OneToMany(mappedBy="strutturaInst", cascade={CascadeType.REMOVE})
	@CascadeOnDelete
	private List<RequisitoInst> requisitoInsts;

	//bi-directional many-to-one association to DomandaInst
	@ManyToOne
	@JoinColumn(name="ID_DOMANDA_FK")
	private DomandaInst domandaInst;

	@Column(name="NUM_REQ_SENZA_RISPOSTA", insertable=false, updatable=false)
	private Integer numReqSenzaRisposta;

	@Column(name="NUM_REQ_NON_ASSEGNATI", insertable=false, updatable=false)
	private Integer numReqNonAssegnati;

	public Integer getNumReqSenzaRisposta() {
		return numReqSenzaRisposta;
	}

	public void setNumReqSenzaRisposta(Integer numReqSenzaRisposta) {
		this.numReqSenzaRisposta = numReqSenzaRisposta;
	}

	public Integer getNumReqNonAssegnati() {
		return numReqNonAssegnati;
	}

	public void setNumReqNonAssegnati(Integer numReqNonAssegnati) {
		this.numReqNonAssegnati = numReqNonAssegnati;
	}
	
	public StrutturaInst() {
	}

	public String getClientid() {
		return this.clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public BigDecimal getIdStrutturaInst() {
		return this.idStrutturaInst;
	}

	public void setIdStrutturaInst(BigDecimal idStrutturaInst) {
		this.idStrutturaInst = idStrutturaInst;
	}

	public List<RequisitoInst> getRequisitoInsts() {
		return this.requisitoInsts;
	}

	public void setRequisitoInsts(List<RequisitoInst> requisitoInsts) {
		this.requisitoInsts = requisitoInsts;
	}

	public RequisitoInst addRequisitoInst(RequisitoInst requisitoInst) {
		getRequisitoInsts().add(requisitoInst);
		requisitoInst.setStrutturaInst(this);

		return requisitoInst;
	}

	public RequisitoInst removeRequisitoInst(RequisitoInst requisitoInst) {
		getRequisitoInsts().remove(requisitoInst);
		requisitoInst.setStrutturaInst(null);

		return requisitoInst;
	}

	public DomandaInst getDomandaInst() {
		return this.domandaInst;
	}

	public void setDomandaInst(DomandaInst domandaInst) {
		this.domandaInst = domandaInst;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getCodicePf() {
		return codicePf;
	}

	public void setCodicePf(String codicePf) {
		this.codicePf = codicePf;
	}

	public String getStrutturaModelClientid() {
		return strutturaModelClientid;
	}

	public void setStrutturaModelClientid(String strutturaModelClientid) {
		this.strutturaModelClientid = strutturaModelClientid;
	}

}