package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the DELIBERA_TEMPL database table.
 * 
 */
@Entity
@Table(name="DELIBERA_TEMPL")
public class DeliberaTempl implements Serializable {
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

	@Column(name="ID_DELIBERA_TEMPL")
	private BigDecimal idDeliberaTempl;

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

	//bi-directional many-to-one association to TipoDelibera
	@ManyToOne
	@JoinColumn(name="ID_TIPO_FK")
	private TipoDelibera tipoDelibera;

	//bi-directional many-to-one association to TipoProcTempl
	@ManyToOne
	@JoinColumn(name="ID_TIPO_PROC_FK")
	private TipoProcTempl tipoProcTempl;

	//bi-directional many-to-many association to ListaRequisitiTempl
	@ManyToMany(mappedBy="deliberaTempls1")
	private List<ListaRequisitiTempl> listaRequisitiTempls1;

	//bi-directional many-to-one association to ListaRequisitiTempl
	@OneToMany(mappedBy="deliberaTempl")
	private List<ListaRequisitiTempl> listaRequisitiTempls2;

	//bi-directional many-to-many association to ListaRequisitiTempl
	@ManyToMany(mappedBy="deliberaTempls2")
	private List<ListaRequisitiTempl> listaRequisitiTempls3;

	public DeliberaTempl() {
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

	public BigDecimal getIdDeliberaTempl() {
		return this.idDeliberaTempl;
	}

	public void setIdDeliberaTempl(BigDecimal idDeliberaTempl) {
		this.idDeliberaTempl = idDeliberaTempl;
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

	public TipoDelibera getTipoDelibera() {
		return this.tipoDelibera;
	}

	public void setTipoDelibera(TipoDelibera tipoDelibera) {
		this.tipoDelibera = tipoDelibera;
	}

	public TipoProcTempl getTipoProcTempl() {
		return this.tipoProcTempl;
	}

	public void setTipoProcTempl(TipoProcTempl tipoProcTempl) {
		this.tipoProcTempl = tipoProcTempl;
	}

	public List<ListaRequisitiTempl> getListaRequisitiTempls1() {
		return this.listaRequisitiTempls1;
	}

	public void setListaRequisitiTempls1(List<ListaRequisitiTempl> listaRequisitiTempls1) {
		this.listaRequisitiTempls1 = listaRequisitiTempls1;
	}

	public List<ListaRequisitiTempl> getListaRequisitiTempls2() {
		return this.listaRequisitiTempls2;
	}

	public void setListaRequisitiTempls2(List<ListaRequisitiTempl> listaRequisitiTempls2) {
		this.listaRequisitiTempls2 = listaRequisitiTempls2;
	}

	public ListaRequisitiTempl addListaRequisitiTempls2(ListaRequisitiTempl listaRequisitiTempls2) {
		getListaRequisitiTempls2().add(listaRequisitiTempls2);
		listaRequisitiTempls2.setDeliberaTempl(this);

		return listaRequisitiTempls2;
	}

	public ListaRequisitiTempl removeListaRequisitiTempls2(ListaRequisitiTempl listaRequisitiTempls2) {
		getListaRequisitiTempls2().remove(listaRequisitiTempls2);
		listaRequisitiTempls2.setDeliberaTempl(null);

		return listaRequisitiTempls2;
	}

	public List<ListaRequisitiTempl> getListaRequisitiTempls3() {
		return this.listaRequisitiTempls3;
	}

	public void setListaRequisitiTempls3(List<ListaRequisitiTempl> listaRequisitiTempls3) {
		this.listaRequisitiTempls3 = listaRequisitiTempls3;
	}

}