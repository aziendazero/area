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


/**
 * The persistent class for the ANAGRAFICA_UTENTE_MODEL database table.
 * 
 */
@Entity
@Table(name="ANAGRAFICA_UTENTE_MODEL")
public class AnagraficaUtenteModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	private String annotations;

	private String cap;

	@Lob
	@Column(name="CARTA_IDENT")
	private byte[] cartaIdent;

	@Column(name="CARTA_IDENT_NUM")
	private String cartaIdentNum;

	@Temporal(TemporalType.DATE)
	@Column(name="CARTA_IDENT_SCAD")
	private Date cartaIdentScad;

	private String cellulare;

	private String cfisc;

	private String civico;

	private String cognome;

	//da sistemare usando il codice del comune COD_COMUNE_ESTESO
	//private String comune;

	@Temporal(TemporalType.DATE)
	private Date creation;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_NASCITA")
	private Date dataNascita;

	private String descr;

	private String disabled;

	private String email;

	@Temporal(TemporalType.DATE)
	private Date ended;

	@Column(name="ID_ANAGR")
	private BigDecimal idAnagr;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	//da sistemare usando il codice del comune di nascita COD_LUOGO_NASCITA	
	//@Column(name="LUOGO_NASCITA")
	//private String luogoNascita;

	private String nome;

	private String professione;

	//da sistemare usando il codice del comune COD_COMUNE_ESTESO	
	//private String provincia;

	private String telefono;

	@Column(name="TOPONIMO_STRADALE")
	private String toponimoStradale;

	@Column(name="VIA_PIAZZA")
	private String viaPiazza;

	//bi-directional many-to-one association to DomandaInst
	@OneToMany(mappedBy="anagraficaUtenteModel")
	private List<DomandaInst> domandaInsts;

	//bi-directional many-to-one association to ProprietarioModel
	@OneToMany(mappedBy="anagraficaUtenteModel")
	private List<ProprietarioModel> proprietarioModels;

	//bi-directional many-to-one association to UtenteModel
	@OneToMany(mappedBy="anagraficaUtenteModel")
	private List<UtenteModel> utenteModels;

	public AnagraficaUtenteModel() {
	}

	public String getClientid() {
		return this.clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public String getAnnotations() {
		return this.annotations;
	}

	public void setAnnotations(String annotations) {
		this.annotations = annotations;
	}

	public String getCap() {
		return this.cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public byte[] getCartaIdent() {
		return this.cartaIdent;
	}

	public void setCartaIdent(byte[] cartaIdent) {
		this.cartaIdent = cartaIdent;
	}

	public String getCartaIdentNum() {
		return this.cartaIdentNum;
	}

	public void setCartaIdentNum(String cartaIdentNum) {
		this.cartaIdentNum = cartaIdentNum;
	}

	public Date getCartaIdentScad() {
		return this.cartaIdentScad;
	}

	public void setCartaIdentScad(Date cartaIdentScad) {
		this.cartaIdentScad = cartaIdentScad;
	}

	public String getCellulare() {
		return this.cellulare;
	}

	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}

	public String getCfisc() {
		return this.cfisc;
	}

	public void setCfisc(String cfisc) {
		this.cfisc = cfisc;
	}

	public String getCivico() {
		return this.civico;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public String getCognome() {
		return this.cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	/*
	public String getComune() {
		return this.comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}
	*/

	public Date getCreation() {
		return this.creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public Date getDataNascita() {
		return this.dataNascita;
	}

	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
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

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getEnded() {
		return this.ended;
	}

	public void setEnded(Date ended) {
		this.ended = ended;
	}

	public BigDecimal getIdAnagr() {
		return this.idAnagr;
	}

	public void setIdAnagr(BigDecimal idAnagr) {
		this.idAnagr = idAnagr;
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

	/*
	public String getLuogoNascita() {
		return this.luogoNascita;
	}

	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = luogoNascita;
	}
	*/

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getProfessione() {
		return this.professione;
	}

	public void setProfessione(String professione) {
		this.professione = professione;
	}

	/*
	public String getProvincia() {
		return this.provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	*/

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getToponimoStradale() {
		return this.toponimoStradale;
	}

	public void setToponimoStradale(String toponimoStradale) {
		this.toponimoStradale = toponimoStradale;
	}

	public String getViaPiazza() {
		return this.viaPiazza;
	}

	public void setViaPiazza(String viaPiazza) {
		this.viaPiazza = viaPiazza;
	}

	public List<DomandaInst> getDomandaInsts() {
		return this.domandaInsts;
	}

	public void setDomandaInsts(List<DomandaInst> domandaInsts) {
		this.domandaInsts = domandaInsts;
	}

	public DomandaInst addDomandaInst(DomandaInst domandaInst) {
		getDomandaInsts().add(domandaInst);
		domandaInst.setAnagraficaUtenteModel(this);

		return domandaInst;
	}

	public DomandaInst removeDomandaInst(DomandaInst domandaInst) {
		getDomandaInsts().remove(domandaInst);
		domandaInst.setAnagraficaUtenteModel(null);

		return domandaInst;
	}

	public List<ProprietarioModel> getProprietarioModels() {
		return this.proprietarioModels;
	}

	public void setProprietarioModels(List<ProprietarioModel> proprietarioModels) {
		this.proprietarioModels = proprietarioModels;
	}

	public ProprietarioModel addProprietarioModel(ProprietarioModel proprietarioModel) {
		getProprietarioModels().add(proprietarioModel);
		proprietarioModel.setAnagraficaUtenteModel(this);

		return proprietarioModel;
	}

	public ProprietarioModel removeProprietarioModel(ProprietarioModel proprietarioModel) {
		getProprietarioModels().remove(proprietarioModel);
		proprietarioModel.setAnagraficaUtenteModel(null);

		return proprietarioModel;
	}

	public List<UtenteModel> getUtenteModels() {
		return this.utenteModels;
	}

	public void setUtenteModels(List<UtenteModel> utenteModels) {
		this.utenteModels = utenteModels;
	}

	public UtenteModel addUtenteModel(UtenteModel utenteModel) {
		getUtenteModels().add(utenteModel);
		utenteModel.setAnagraficaUtenteModel(this);

		return utenteModel;
	}

	public UtenteModel removeUtenteModel(UtenteModel utenteModel) {
		getUtenteModels().remove(utenteModel);
		utenteModel.setAnagraficaUtenteModel(null);

		return utenteModel;
	}
	
	public String getCognomeNome() {
		String toRet = cognome;
		if(toRet == null)
			toRet = "";
		if(nome != null && !nome.isEmpty()) {
			if(!toRet.isEmpty())
				toRet += " ";
			toRet += nome;
		}
		return toRet;
	}

}