package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the UDO_MODEL database table.
 * 
 */
@Entity
@Table(name="UDO_MODEL_WITH_DESC_FLUSSO_MIN")
public class UdoModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	private String annotations;

	private String blocco;
	
	//Codice Ulss Attuale
	@Column(name="COD_AZIENDA_ULSS")
	private String codiceAziendaUlss;

	//Codice Ulss precedente
	@Column(name="CODICE_ULSS_PRECEDENTE")
	private String codiceUlssPrecedente;

	@Column(name="CODICE_FLUSSO_MINISTERIALE")
	private String codiceFlussoMinisteriale;

	@Column(name="DESC_FLUSSO_MINISTERIALE")
	private String descFlussoMinisteriale;
	
	@Temporal(TemporalType.DATE)
	private Date creation;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INIZIO")
	private Date dataInizio;

	private String descr;

	@Column(name="DIR_SANITARIO_CFISC")
	private String dirSanitarioCfisc;

	@Column(name="DIR_SANITARIO_COGN")
	private String dirSanitarioCogn;

	@Column(name="DIR_SANITARIO_NOME")
	private String dirSanitarioNome;

	private String disabled;

	@Temporal(TemporalType.DATE)
	private Date ended;

	@Column(name="FLAG_MODULO")
	private String flagModulo;

	@Column(name="ID_UDO")
	private BigDecimal idUdo;

	@Column(name="ID_UNIVOCO")
	private String idUnivoco;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String piano;

	@Column(name="PROCEDIMENTO_IN_CORSO")
	private String procedimentoInCorso;

	private BigDecimal progressivo;

	@Temporal(TemporalType.DATE)
	private Date scadenza;

	private String stabilimento;

	private String stato;

	private String validata;

	private String week;
	
//	FLAG_DI_PROPRIETA
//	CF_DI_PROPRIETA
//	PIVA_DI_PROPRIETA
//	NOME_DI_PROPRIETA
//	COGNOME_DI_PROPRIETA
//	RAGIONE_SOCIALE_DI_PROPRIETA
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

	//bi-directional many-to-one association to BindUdoDisciplina
	@OneToMany(mappedBy="udoModel")
	private List<BindUdoDisciplina> bindUdoDisciplinas;

	//bi-directional many-to-one association to StatoUdo
	@OneToMany(mappedBy="udoModel")
	private List<StatoUdo> statoUdos;

	//bi-directional many-to-one association to UdoInst
	/*@OneToMany(mappedBy="udoModel")
	private List<UdoInst> udoInsts;*/

	//bi-directional many-to-many association to AttoModel
	@ManyToMany
	@JoinTable(
		name="BIND_ATTO_UDO"
		, joinColumns={
			@JoinColumn(name="ID_UDO_FK")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_ATTO_FK")
			}
		)
	private List<AttoModel> attoModels;

	//bi-directional many-to-many association to BrancaTempl
	@ManyToMany
	@JoinTable(
		name="BIND_UDO_BRANCA"
		, joinColumns={
			@JoinColumn(name="ID_UDO_FK")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_BRANCA_FK")
			}
		)
	private List<BrancaTempl> brancaTempls;

	/*
	RIMOSSO PER MODIFICA GESTIONE DATI REGIONALI
	//bi-directional many-to-one association to CodiciStruttDenomin
	//@ManyToOne(fetch=FetchType.LAZY)
	@ManyToOne
	@JoinColumn(name="ID_COD_STRUTT_DENOM_FK")
	private CodiciStruttDenomin codiciStruttDenomin;
	*/

	/*
	RIMOSSO PER MODIFICA GESTIONE DATI REGIONALI
	//bi-directional many-to-one association to CodiciUlssTerritoriali
	//@ManyToOne(fetch=FetchType.LAZY)
	@ManyToOne
	@JoinColumn(name="ID_COD_ULSS_TERR_FK")
	private CodiciUlssTerritoriali codiciUlssTerritoriali;
	*/

	//bi-directional many-to-many association to EstenstioneTempl
	@ManyToMany
	@JoinTable(
		name="BIND_UDO_ESTENS"
		, joinColumns={
			@JoinColumn(name="ID_UDO_FK")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_ESTENSIONE_FK")
			}
		)
	private List<EstenstioneTempl> estenstioneTempls;

	//bi-directional many-to-many association to FattProdUdoModel
	@ManyToMany
	@JoinTable(
		name="BIND_UDO_FATT_PROD"
		, joinColumns={
			@JoinColumn(name="ID_UDO_FK")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_FATTORE_FK")
			}
		)
	private List<FattProdUdoModel> fattProdUdoModels;

	//bi-directional many-to-many association to PrestazioneTempl
	@ManyToMany
	@JoinTable(
		name="BIND_UDO_PREST"
		, joinColumns={
			@JoinColumn(name="ID_UDO_FK")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_PRESTAZIONE_FK")
			}
		)
	private List<PrestazioneTempl> prestazioneTempls;

	//bi-directional many-to-one association to SedeOperModel
	//@ManyToOne(fetch=FetchType.LAZY)
	@ManyToOne
	@JoinColumn(name="ID_SEDE_FK")
	private SedeOperModel sedeOperModel;

	//bi-directional many-to-one association to TipoUdoTempl
	@ManyToOne
	@JoinColumn(name="ID_TIPO_UDO_FK")
	private TipoUdoTempl tipoUdoTempl;

	//bi-directional many-to-one association to TipoUdoTempl
	@ManyToOne
	@JoinColumn(name="ID_EDIFICIO_STR_FK")
	private EdificioTempl edificioTempl;

	//bi-directional many-to-one association to UoModel

	//MODIFICA 2015-12-14 aggiunta gestione organigramma
	//bi-directional many-to-one association to UoModel
	//@ManyToOne
	//@JoinColumn(name="ID_UO_FK")
	//private UoModel uoModel;
	@ManyToOne 
    @JoinColumns({
        @JoinColumn(name="PROVENIENZA_UO", referencedColumnName="PROVENIENZA"),
        @JoinColumn(name="ID_UO", referencedColumnName="ID")
    })
    private UoModel uoModel;
	
	
	public UdoModel() {
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

	public String getBlocco() {
		return this.blocco;
	}

	public void setBlocco(String blocco) {
		this.blocco = blocco;
	}

	public Date getCreation() {
		return this.creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public Date getDataInizio() {
		return this.dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getDirSanitarioCfisc() {
		return this.dirSanitarioCfisc;
	}

	public void setDirSanitarioCfisc(String dirSanitarioCfisc) {
		this.dirSanitarioCfisc = dirSanitarioCfisc;
	}

	public String getDirSanitarioCogn() {
		return this.dirSanitarioCogn;
	}

	public void setDirSanitarioCogn(String dirSanitarioCogn) {
		this.dirSanitarioCogn = dirSanitarioCogn;
	}

	public String getDirSanitarioNome() {
		return this.dirSanitarioNome;
	}

	public void setDirSanitarioNome(String dirSanitarioNome) {
		this.dirSanitarioNome = dirSanitarioNome;
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

	public String getFlagModulo() {
		return this.flagModulo;
	}

	public void setFlagModulo(String flagModulo) {
		this.flagModulo = flagModulo;
	}

	public BigDecimal getIdUdo() {
		return this.idUdo;
	}

	public void setIdUdo(BigDecimal idUdo) {
		this.idUdo = idUdo;
	}

	public String getIdUnivoco() {
		return this.idUnivoco;
	}

	public void setIdUnivoco(String idUnivoco) {
		this.idUnivoco = idUnivoco;
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

	public String getPiano() {
		return this.piano;
	}

	public void setPiano(String piano) {
		this.piano = piano;
	}

	public String getProcedimentoInCorso() {
		return this.procedimentoInCorso;
	}

	public void setProcedimentoInCorso(String procedimentoInCorso) {
		this.procedimentoInCorso = procedimentoInCorso;
	}

	public BigDecimal getProgressivo() {
		return this.progressivo;
	}

	public void setProgressivo(BigDecimal progressivo) {
		this.progressivo = progressivo;
	}

	public Date getScadenza() {
		return this.scadenza;
	}

	public void setScadenza(Date scadenza) {
		this.scadenza = scadenza;
	}

	public String getStabilimento() {
		return this.stabilimento;
	}

	public void setStabilimento(String stabilimento) {
		this.stabilimento = stabilimento;
	}

	public String getStato() {
		return this.stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getValidata() {
		return this.validata;
	}

	public void setValidata(String validata) {
		this.validata = validata;
	}

	public String getWeek() {
		return this.week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public List<BindUdoDisciplina> getBindUdoDisciplinas() {
		return this.bindUdoDisciplinas;
	}

	public void setBindUdoDisciplinas(List<BindUdoDisciplina> bindUdoDisciplinas) {
		this.bindUdoDisciplinas = bindUdoDisciplinas;
	}

	public BindUdoDisciplina addBindUdoDisciplina(BindUdoDisciplina bindUdoDisciplina) {
		getBindUdoDisciplinas().add(bindUdoDisciplina);
		bindUdoDisciplina.setUdoModel(this);

		return bindUdoDisciplina;
	}

	public BindUdoDisciplina removeBindUdoDisciplina(BindUdoDisciplina bindUdoDisciplina) {
		getBindUdoDisciplinas().remove(bindUdoDisciplina);
		bindUdoDisciplina.setUdoModel(null);

		return bindUdoDisciplina;
	}

	public List<StatoUdo> getStatoUdos() {
		return this.statoUdos;
	}

	public void setStatoUdos(List<StatoUdo> statoUdos) {
		this.statoUdos = statoUdos;
	}

	public StatoUdo addStatoUdo(StatoUdo statoUdo) {
		getStatoUdos().add(statoUdo);
		statoUdo.setUdoModel(this);

		return statoUdo;
	}

	public StatoUdo removeStatoUdo(StatoUdo statoUdo) {
		getStatoUdos().remove(statoUdo);
		statoUdo.setUdoModel(null);

		return statoUdo;
	}

	/*public List<UdoInst> getUdoInsts() {
		return this.udoInsts;
	}

	public void setUdoInsts(List<UdoInst> udoInsts) {
		this.udoInsts = udoInsts;
	}

	public UdoInst addUdoInst(UdoInst udoInst) {
		getUdoInsts().add(udoInst);
		udoInst.setUdoModel(this);

		return udoInst;
	}

	public UdoInst removeUdoInst(UdoInst udoInst) {
		getUdoInsts().remove(udoInst);
		udoInst.setUdoModel(null);

		return udoInst;
	}*/

	public List<AttoModel> getAttoModels() {
		return this.attoModels;
	}

	public void setAttoModels(List<AttoModel> attoModels) {
		this.attoModels = attoModels;
	}

	public List<BrancaTempl> getBrancaTempls() {
		return this.brancaTempls;
	}

	public void setBrancaTempls(List<BrancaTempl> brancaTempls) {
		this.brancaTempls = brancaTempls;
	}

	/*
	RIMOSSO PER MODIFICA GESTIONE DATI REGIONALI
	public CodiciStruttDenomin getCodiciStruttDenomin() {
		return this.codiciStruttDenomin;
	}

	public void setCodiciStruttDenomin(CodiciStruttDenomin codiciStruttDenomin) {
		this.codiciStruttDenomin = codiciStruttDenomin;
	}
	*/

	/*
	RIMOSSO PER MODIFICA GESTIONE DATI REGIONALI
	public CodiciUlssTerritoriali getCodiciUlssTerritoriali() {
		return this.codiciUlssTerritoriali;
	}

	public void setCodiciUlssTerritoriali(CodiciUlssTerritoriali codiciUlssTerritoriali) {
		this.codiciUlssTerritoriali = codiciUlssTerritoriali;
	}
	*/

	public List<EstenstioneTempl> getEstenstioneTempls() {
		return this.estenstioneTempls;
	}

	public void setEstenstioneTempls(List<EstenstioneTempl> estenstioneTempls) {
		this.estenstioneTempls = estenstioneTempls;
	}

	public List<FattProdUdoModel> getFattProdUdoModels() {
		return this.fattProdUdoModels;
	}

	public void setFattProdUdoModels(List<FattProdUdoModel> fattProdUdoModels) {
		this.fattProdUdoModels = fattProdUdoModels;
	}

	public List<PrestazioneTempl> getPrestazioneTempls() {
		return this.prestazioneTempls;
	}

	public void setPrestazioneTempls(List<PrestazioneTempl> prestazioneTempls) {
		this.prestazioneTempls = prestazioneTempls;
	}

	public SedeOperModel getSedeOperModel() {
		return this.sedeOperModel;
	}

	public void setSedeOperModel(SedeOperModel sedeOperModel) {
		this.sedeOperModel = sedeOperModel;
	}

	public TipoUdoTempl getTipoUdoTempl() {
		return this.tipoUdoTempl;
	}

	public void setTipoUdoTempl(TipoUdoTempl tipoUdoTempl) {
		this.tipoUdoTempl = tipoUdoTempl;
	}

	public UoModel getUoModel() {
		return this.uoModel;
	}

	public void setUoModel(UoModel uoModel) {
		this.uoModel = uoModel;
	}

	public String getCodiceFlussoMinisteriale() {
		return codiceFlussoMinisteriale;
	}

	public void setCodiceFlussoMinisteriale(String codiceFlussoMinisteriale) {
		this.codiceFlussoMinisteriale = codiceFlussoMinisteriale;
	}

	public String getDescFlussoMinisteriale() {
		return descFlussoMinisteriale;
	}

	public void setDescFlussoMinisteriale(String descFlussoMinisteriale) {
		this.descFlussoMinisteriale = descFlussoMinisteriale;
	}

	public String getCodiceAziendaUlss() {
		return codiceAziendaUlss;
	}

	public void setCodiceAziendaUlss(String codiceAziendaUlss) {
		this.codiceAziendaUlss = codiceAziendaUlss;
	}

	public String getCodiceUlssPrecedente() {
		return codiceUlssPrecedente;
	}

	public void setCodiceUlssPrecedente(String codiceUlssPrecedente) {
		this.codiceUlssPrecedente = codiceUlssPrecedente;
	}

	public EdificioTempl getEdificioTempl() {
		return edificioTempl;
	}

	public void setEdificioTempl(EdificioTempl edificioTempl) {
		this.edificioTempl = edificioTempl;
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