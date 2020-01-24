package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the ATTO_INST database table.
 * 
 */
@Entity
@Table(name="ATTO_INST")
@NamedQueries({
	@NamedQuery(name="AttoInst.findAllAttiByDomandaInst", query="select r from AttoInst r "
			//+ "left join r.domandaInst dom "
			+ "where r.domandaInst.clientid = :domandaClientId "
			+ "order by r.anno, r.numero")			
})
public class AttoInst implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	private String anno;

	private String annotations;

	@Temporal(TemporalType.DATE)
	private Date creation;

	private String descr;

	private String disabled;

	@Temporal(TemporalType.DATE)
	private Date ended;

	@Temporal(TemporalType.DATE)
	@Column(name="FINE_VALIDITA")
	private Date fineValidita;

	@Column(name="ID_ATTO")
	private BigDecimal idAtto;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="INIZIO_VALIDITA")
	private Date inizioValidita;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String numero;

	//bi-directional many-to-one association to BinaryAttachmentsAppl
	@ManyToOne
	@JoinColumn(name="ID_ALLEGATO_FK")
	private BinaryAttachmentsAppl binaryAttachmentsAppl;

	//bi-directional many-to-one association to TipoAtto
	@ManyToOne
	@JoinColumn(name="ID_TIPO_FK")
	private TipoAtto tipoAtto;

	//bi-directional many-to-one association to TipoProcTempl
	@ManyToOne
	@JoinColumn(name="ID_TIPO_PROC_FK")
	private TipoProcTempl tipoProcTempl;

	//bi-directional many-to-one association to TitolareModel
	@ManyToOne
	@JoinColumn(name="ID_TITOLARE_FK")
	private TitolareModel titolareModel;

	//bi-directional many-to-one association to DomandaInst
	@ManyToOne
	@JoinColumn(name="ID_DOMANDA_FK")
	private DomandaInst domandaInst;
	
	//bi-directional many-to-one association to UdoInst
	@OneToMany(mappedBy="attoInst", cascade={CascadeType.REMOVE})
	private List<UdoInst> udoInsts;
	
	public AttoInst() {
	}

	public String getClientid() {
		return this.clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public String getAnno() {
		return this.anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getAnnotations() {
		return this.annotations;
	}

	public void setAnnotations(String annotations) {
		this.annotations = annotations;
	}

	public Date getCreation() {
		return this.creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getDisabled() {
		return this.disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public Date getEnded() {
		return this.ended;
	}

	public void setEnded(Date ended) {
		this.ended = ended;
	}

	public Date getFineValidita() {
		return this.fineValidita;
	}

	public void setFineValidita(Date fineValidita) {
		this.fineValidita = fineValidita;
	}

	public BigDecimal getIdAtto() {
		return this.idAtto;
	}

	public void setIdAtto(BigDecimal idAtto) {
		this.idAtto = idAtto;
	}

	public String getIdUser() {
		return this.idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public Date getInizioValidita() {
		return this.inizioValidita;
	}

	public void setInizioValidita(Date inizioValidita) {
		this.inizioValidita = inizioValidita;
	}

	public Date getLastMod() {
		return this.lastMod;
	}

	public void setLastMod(Date lastMod) {
		this.lastMod = lastMod;
	}

	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public BinaryAttachmentsAppl getBinaryAttachmentsAppl() {
		return this.binaryAttachmentsAppl;
	}

	public void setBinaryAttachmentsAppl(BinaryAttachmentsAppl binaryAttachmentsAppl) {
		this.binaryAttachmentsAppl = binaryAttachmentsAppl;
	}

	public TipoAtto getTipoAtto() {
		return this.tipoAtto;
	}

	public void setTipoAtto(TipoAtto tipoAtto) {
		this.tipoAtto = tipoAtto;
	}

	public TipoProcTempl getTipoProcTempl() {
		return this.tipoProcTempl;
	}

	public void setTipoProcTempl(TipoProcTempl tipoProcTempl) {
		this.tipoProcTempl = tipoProcTempl;
	}

	public TitolareModel getTitolareModel() {
		return this.titolareModel;
	}

	public void setTitolareModel(TitolareModel titolareModel) {
		this.titolareModel = titolareModel;
	}

	public DomandaInst getDomandaInst() {
		return this.domandaInst;
	}

	public void setDomandaInst(DomandaInst domandaInst) {
		this.domandaInst = domandaInst;
	}

	public List<UdoInst> getUdoInsts() {
		return this.udoInsts;
	}

	public void setUdoInsts(List<UdoInst> udoInsts) {
		this.udoInsts = udoInsts;
	}
	
	public UdoInst addUdoInst(UdoInst udoInst) {
		getUdoInsts().add(udoInst);
		udoInst.setAttoInst(this);

		return udoInst;
	}

	public UdoInst removeUdoInst(UdoInst udoInst) {
		getUdoInsts().remove(udoInst);
		udoInst.setAttoInst(null);

		return udoInst;
	}

	public String getDescrizione() {
		String toRet = "";
		if(this.anno != null && !this.anno.isEmpty())
			toRet = this.anno;
		if(this.numero != null && !this.numero.isEmpty()) {
			if(!toRet.isEmpty()) {
				toRet += " ";
			}
			toRet += this.numero;
		}
		return toRet;
	}
}