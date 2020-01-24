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
@Table(name="EDIFICIO_INST_WITH_REQ_INFO")
@NamedEntityGraph(name="EdificioInst.autovalutazioneExportCsv",
	attributeNodes={
		@NamedAttributeNode("nome"),
		@NamedAttributeNode("codice"),
		@NamedAttributeNode("sedeOperModelDenominazione")
	}
)
public class EdificioInst implements Serializable {
	private static final long serialVersionUID = 1L;

  	@Id
	private String clientid;

	@Column(name="ID_EDIFICIO_INST")
	private BigDecimal idEdificioInst;

	private String nome;

	private String codice;

	@Column(name="ID_EDIFICIO_STR_TEMPL_FK")
	private String edificioTemplClientid;

	@Column(name="SEDE_OPER_MODEL_CLIENTID")
	private String sedeOperModelClientid;

	@Column(name="SEDE_OPER_MODE_DENOMINAZIONE")
	private String sedeOperModelDenominazione;

	//bi-directional many-to-one association to RequisitoInst
	@OneToMany(mappedBy="edificioInst", cascade={CascadeType.REMOVE})
	@CascadeOnDelete
	private List<RequisitoInst> requisitoInsts;

	//bi-directional many-to-one association to DomandaInst
	@ManyToOne
	@JoinColumn(name="ID_DOMANDA_FK")
	private DomandaInst domandaInst;

	@Column(name="FLAG_DI_PROPRIETA")
	private BigDecimal flagDiProprieta;
	
	@Column(name="CF_DI_PROPRIETA")
	private String cfDiProprieta;

	@Column(name="PIVA_DI_PROPRIETA")
	private String pivaDiProprieta;

	@Column(name="NOME_DI_PROPRIETA")
	private String nomeDiProprieta;

	@Column(name="COGNOME_DI_PROPRIETA")
	private String cognomeDiProprieta;

	@Column(name="RAGIONE_SOCIALE_DI_PROPRIETA")
	private String ragioneSocialeDiProprieta;

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
	
	public EdificioInst() {
	}

	public String getClientid() {
		return this.clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public BigDecimal getIdEdificioInst() {
		return this.idEdificioInst;
	}

	public void setIdEdificioInst(BigDecimal idEdificioInst) {
		this.idEdificioInst = idEdificioInst;
	}

	public List<RequisitoInst> getRequisitoInsts() {
		return this.requisitoInsts;
	}

	public void setRequisitoInsts(List<RequisitoInst> requisitoInsts) {
		this.requisitoInsts = requisitoInsts;
	}

	public RequisitoInst addRequisitoInst(RequisitoInst requisitoInst) {
		getRequisitoInsts().add(requisitoInst);
		requisitoInst.setEdificioInst(this);

		return requisitoInst;
	}

	public RequisitoInst removeRequisitoInst(RequisitoInst requisitoInst) {
		getRequisitoInsts().remove(requisitoInst);
		requisitoInst.setEdificioInst(null);

		return requisitoInst;
	}

	public DomandaInst getDomandaInst() {
		return this.domandaInst;
	}

	public void setDomandaInst(DomandaInst domandaInst) {
		this.domandaInst = domandaInst;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getEdificioTemplClientid() {
		return edificioTemplClientid;
	}

	public void setEdificioTemplClientid(String edificioTemplClientid) {
		this.edificioTemplClientid = edificioTemplClientid;
	}

	public String getSedeOperModelClientid() {
		return sedeOperModelClientid;
	}

	public void setSedeOperModelClientid(String sedeOperModelClientid) {
		this.sedeOperModelClientid = sedeOperModelClientid;
	}

	public String getSedeOperModelDenominazione() {
		return sedeOperModelDenominazione;
	}

	public void setSedeOperModelDenominazione(String sedeOperModelDenominazione) {
		this.sedeOperModelDenominazione = sedeOperModelDenominazione;
	}

	public BigDecimal getFlagDiProprieta() {
		return flagDiProprieta;
	}

	public void setFlagDiProprieta(BigDecimal flagDiProprieta) {
		this.flagDiProprieta = flagDiProprieta;
	}

	public String getCfDiProprieta() {
		return cfDiProprieta;
	}

	public void setCfDiProprieta(String cfDiProprieta) {
		this.cfDiProprieta = cfDiProprieta;
	}

	public String getPivaDiProprieta() {
		return pivaDiProprieta;
	}

	public void setPivaDiProprieta(String pivaDiProprieta) {
		this.pivaDiProprieta = pivaDiProprieta;
	}

	public String getNomeDiProprieta() {
		return nomeDiProprieta;
	}

	public void setNomeDiProprieta(String nomeDiProprieta) {
		this.nomeDiProprieta = nomeDiProprieta;
	}

	public String getCognomeDiProprieta() {
		return cognomeDiProprieta;
	}

	public void setCognomeDiProprieta(String cognomeDiProprieta) {
		this.cognomeDiProprieta = cognomeDiProprieta;
	}

	public String getRagioneSocialeDiProprieta() {
		return ragioneSocialeDiProprieta;
	}

	public void setRagioneSocialeDiProprieta(String ragioneSocialeDiProprieta) {
		this.ragioneSocialeDiProprieta = ragioneSocialeDiProprieta;
	}

	public String getDescr() {
		if(this.sedeOperModelDenominazione != null)
			return this.sedeOperModelDenominazione + " - " + this.getCodice() + " - " + this.getNome();
		return this.getCodice() + " - " + this.getNome();
	}

}