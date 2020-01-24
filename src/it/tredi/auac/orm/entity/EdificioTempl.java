package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the EDIFICIO_TEMPL database table.
 * 
 */
@Entity
@Table(name="EDIFICIO_STR_TEMPL")
public class EdificioTempl implements Serializable {
	private static final long serialVersionUID = 1L;

	private String annotations;

	@Id
	private String clientid;

	private String codice;

	@Temporal(TemporalType.DATE)
	private Date creation;

	private String descr;

	private String disabled;

	@Temporal(TemporalType.DATE)
	private Date ended;
	
	@Column(name="ID_EDIFICIO_STR_TEMPL")
	private BigDecimal idEdificioTempl;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String nome;

	//bi-directional many-to-one association to StrutturaModel
	@ManyToOne
	@JoinColumn(name="ID_STRUTTURA_FK")
	private StrutturaModel strutturaModel;

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
	
	public EdificioTempl() {
	}

	public String getAnnotations() {
		return annotations;
	}

	public void setAnnotations(String annotations) {
		this.annotations = annotations;
	}

	public String getClientid() {
		return clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public Date getEnded() {
		return ended;
	}

	public void setEnded(Date ended) {
		this.ended = ended;
	}

	public BigDecimal getIdEdificioTempl() {
		return idEdificioTempl;
	}

	public void setIdEdificioTempl(BigDecimal idEdificioTempl) {
		this.idEdificioTempl = idEdificioTempl;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public Date getLastMod() {
		return lastMod;
	}

	public void setLastMod(Date lastMod) {
		this.lastMod = lastMod;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public StrutturaModel getStrutturaModel() {
		return strutturaModel;
	}

	public void setStrutturaModel(StrutturaModel strutturaModel) {
		this.strutturaModel = strutturaModel;
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

}