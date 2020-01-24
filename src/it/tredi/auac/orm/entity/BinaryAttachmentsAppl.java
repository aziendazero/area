package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.codec.binary.Base64;


/**
 * The persistent class for the BINARY_ATTACHMENTS_APPL database table.
 * 
 */
@Entity
@Table(name="BINARY_ATTACHMENTS_APPL")
public class BinaryAttachmentsAppl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	@Lob
	private byte[] allegato;

	private String annotations;

	@Temporal(TemporalType.DATE)
	private Date creation;

	private String descr;

	private String disabled;

	@Temporal(TemporalType.DATE)
	private Date ended;

	@Column(name="ID_FILE")
	private BigDecimal idFile;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String nome;

	private String tipo;

	//bi-directional many-to-one association to AttoInst
	@OneToMany(mappedBy="binaryAttachmentsAppl")
	private List<AttoInst> attoInsts;

	//bi-directional many-to-one association to AttoModel
	@OneToMany(mappedBy="binaryAttachmentsAppl")
	private List<AttoModel> attoModels;

	//bi-directional many-to-one association to DeliberaTempl
	@OneToMany(mappedBy="binaryAttachmentsAppl")
	private List<DeliberaTempl> deliberaTempls;

	public BinaryAttachmentsAppl() {
	}

	public String getClientid() {
		return this.clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public byte[] getAllegato() {
		return this.allegato;
	}

	public void setAllegato(byte[] allegato) {
		this.allegato = allegato;
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

	public BigDecimal getIdFile() {
		return this.idFile;
	}

	public void setIdFile(BigDecimal idFile) {
		this.idFile = idFile;
	}

	public String getIdUser() {
		return this.idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public Date getLastMod() {
		return this.lastMod;
	}

	public void setLastMod(Date lastMod) {
		this.lastMod = lastMod;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public List<AttoInst> getAttoInsts() {
		return this.attoInsts;
	}

	public void setAttoInsts(List<AttoInst> attoInsts) {
		this.attoInsts = attoInsts;
	}

	public AttoInst addAttoInst(AttoInst attoInst) {
		getAttoInsts().add(attoInst);
		attoInst.setBinaryAttachmentsAppl(this);

		return attoInst;
	}

	public AttoInst removeAttoInst(AttoInst attoInst) {
		getAttoInsts().remove(attoInst);
		attoInst.setBinaryAttachmentsAppl(null);

		return attoInst;
	}

	public List<AttoModel> getAttoModels() {
		return this.attoModels;
	}

	public void setAttoModels(List<AttoModel> attoModels) {
		this.attoModels = attoModels;
	}

	public AttoModel addAttoModel(AttoModel attoModel) {
		getAttoModels().add(attoModel);
		attoModel.setBinaryAttachmentsAppl(this);

		return attoModel;
	}

	public AttoModel removeAttoModel(AttoModel attoModel) {
		getAttoModels().remove(attoModel);
		attoModel.setBinaryAttachmentsAppl(null);

		return attoModel;
	}

	public List<DeliberaTempl> getDeliberaTempls() {
		return this.deliberaTempls;
	}

	public void setDeliberaTempls(List<DeliberaTempl> deliberaTempls) {
		this.deliberaTempls = deliberaTempls;
	}

	public DeliberaTempl addDeliberaTempl(DeliberaTempl deliberaTempl) {
		getDeliberaTempls().add(deliberaTempl);
		deliberaTempl.setBinaryAttachmentsAppl(this);

		return deliberaTempl;
	}

	public DeliberaTempl removeDeliberaTempl(DeliberaTempl deliberaTempl) {
		getDeliberaTempls().remove(deliberaTempl);
		deliberaTempl.setBinaryAttachmentsAppl(null);

		return deliberaTempl;
	}

	public String getClientidBase64() {
		if(this.clientid != null)
			return Base64.encodeBase64String(this.clientid.getBytes());
		return null;
	}

	public String getNomeBase64() {
		if(this.nome != null)
			return Base64.encodeBase64String(this.nome.getBytes());
		return null;
	}

	public String getNomeWithExtension() {
		if(this.nome != null && !this.nome.contains("."))
			return this.nome + "." + this.tipo;
		return this.nome;
	}

	public String getNomeBase64WithExtension() {
		if(this.getNomeWithExtension() != null)
			return Base64.encodeBase64String(this.getNomeWithExtension().getBytes());
		return null;
	}

}