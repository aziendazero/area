package it.tredi.auac.orm.entity;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.annotations.JoinFetch;
import org.eclipse.persistence.annotations.JoinFetchType;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import it.tredi.auac.StatoEsitoEnum;


/**
 * The persistent class for the UDO_INST database table.
 * 
 */
@Entity
@Table(name="UDO_INST_WITH_REQ_INFO")
@NamedEntityGraph(name="UdoInst.autovalutazioneExportCsv",
	attributeNodes={
		@NamedAttributeNode("idUnivoco"),
		@NamedAttributeNode("brancheInfo"),
		@NamedAttributeNode("estensioniInfo"),
		@NamedAttributeNode("disciplineInfo"),
		@NamedAttributeNode("prestazioniInfo"),
		@NamedAttributeNode("fattProdInfo")
		//@NamedAttributeNode(value="disciplinaTempls", subgraph="disciplinaTempl"),
		//@NamedAttributeNode(value="brancaTempls", subgraph="brancaTempl"),
		//@NamedAttributeNode(value="udoModel", subgraph="udoModel"),
		//@NamedAttributeNode(value="tipoUdoTempl", subgraph="tipoUdoTempl"),
		//@NamedAttributeNode(value="fattProdUdoInsts", subgraph="fattProdUdoInst")
	}/*,
	subgraphs={
		@NamedSubgraph(name="disciplinaTempl",
		attributeNodes={
			@NamedAttributeNode("descr")
		}),
		@NamedSubgraph(name="brancaTempl",
		attributeNodes={
			@NamedAttributeNode("descr")
		}),
		@NamedSubgraph(name="udoModel",
			attributeNodes={
				@NamedAttributeNode("idUnivoco")
		}),
		@NamedSubgraph(name="tipoUdoTempl",
			attributeNodes={
				@NamedAttributeNode("descr")
		}),
		@NamedSubgraph(name="fattProdUdoInst",
				attributeNodes={
				@NamedAttributeNode("descr"),
				@NamedAttributeNode("valore"),
				@NamedAttributeNode("valore2"),
				@NamedAttributeNode(value="tipoFattoreProdTempl", subgraph="tipoFattoreProdTempl")
		}),
		@NamedSubgraph(name="tipoFattoreProdTempl",
		attributeNodes={
			@NamedAttributeNode("descr")
		}),
	}*/
)
public class UdoInst implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	private String annotations;

	private String blocco;

	@Column(name="BRANCHEINFO")
	private String brancheInfo;

	@Column(name="CODICE_STRUTT_DENOMIN")
	private String codiceStruttDenomin;

	@Column(name="CODICE_ULSS_TERRITORIALE")
	private String codiceUlssTerritoriale;

	@Column(name="CODICE_ULSS_PRECEDENTE")
	private String codiceUlssPrecedente;

	@Temporal(TemporalType.DATE)
	private Date creation;

	@Column(name="DENOM_STRUTT_FISICA_SEDE")
	private String denomStruttFisicaSede;

	@Column(name="DENOMINAZIONE_SEDE")
	private String denominazioneSede;

	@Column(name="DENOMINAZIONE_UO")
	private String denominazioneUo;

	@Column(name="TIPO_NODO_UO")
	private String tipoNodoUo;

	private String descr;

	@Column(name="DIR_SANITARIO_CFISC")
	private String dirSanitarioCfisc;

	@Column(name="DIR_SANITARIO_COGN")
	private String dirSanitarioCogn;

	@Column(name="DIR_SANITARIO_NOME")
	private String dirSanitarioNome;

	private String disabled;

	@Column(name="DISCIPLINEINFO")
	private String disciplineInfo;

	@Temporal(TemporalType.DATE)
	private Date ended;

	private String esito;

	@Temporal(TemporalType.DATE)
	@Column(name="ESITO_DATA_INIZIO")
	private Date esitoDataInizio;

	@Column(name="ESITO_DIREZIONE_OPERATORE")
	private String esitoDirezioneOperatore;

	@Column(name="ESITO_NOTE")
	private String esitoNote;

	@Column(name="ESITO_OPERATORE")
	private String esitoOperatore;

	@Temporal(TemporalType.DATE)
	@Column(name="ESITO_SCADENZA")
	private Date esitoScadenza;
	
	//Registra la data ora dell'inserimento dell'esito o dell'ultima modifica  
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ESITO_TIME_STAMP")
	private Date esitoTimeStamp;
	
	@Column(name="ESTENSIONIINFO")
	private String estensioniInfo;

	@Column(name="FATTPRODINFO")
	private String fattProdInfo;

	@Column(name="FLAG_MODULO")
	private String flagModulo;

	@Column(name="ID_UDO")
	private BigDecimal idUdo;

	@Column(name="ID_UNIVOCO")
	private String idUnivoco;

	//@Column(name="ID_UO_MODEL")
	//private String udoModelUoModelClientid;

	@Column(name="OLD_ID_UO_MODEL")
	private String oldUdoModelUoModelClientid;
	
	@Column(name="PROVENIENZA_UO")
	private String provenienzaUo;

	//Modifica Organigramma aggiunta id univoco COD_NODO_REGIONALE
	@Column(name="ID_UO")
	private String idUo;
	//private BigDecimal idUo;
	
	@Column(name="ID_USER")
	private String idUser;

	@Column(name="INDIRIZZO_SEDE")
	private String indirizzoSede;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String piano;

	@Column(name="PRESTAZIONIINFO")
	private String prestazioniInfo;

	private BigDecimal progressivo;

	@Temporal(TemporalType.DATE)
	private Date scadenza;

	private String stabilimento;
	
	//Non è stato possibile usare il converter quindi e' stata utilizzata una property di appoggio di tipo String che viene usata per mappare l'enum 
	//@Convert(converter = StatoEsitoEnumConverter.class)
	//private StatoEsitoEnum esitoStato;

	@Column(name="ESITO_STATO")
	private String esitoStatoStr;

	@Column(name="STRUTTURA_MODEL_CLIENTID")
	private String strutturaModelClientid;

	@Column(name="EDIFICIO_STR_TEMPL_CLIENTID")
	private String edificioTemplClientid;

	@Column(name="ID_UDO_MODEL_FK")
	private String udoModelClientid;

	private String stato;

	@Column(name="TIPO_PUNTO_FISICO_SEDE")
	private String tipoPuntoFisicoSede;

	@Column(name="TIPOUDO22TEMPL_CODICEUDO")
	private String tipoUdoTemplTipoUdo22TemplCodiceUdo;

	@Column(name="TIPOUDO22TEMPL_NOMECODICEUDO")
	private String tipoUdoTemplTipoUdo22TemplNomeCodiceUdo;

	@Column(name="TIPOUDOTEMPL_DESCR")
	private String tipoUdoTemplDescr;
	
	@Column(name="ADMIN")
	private String admin;
	
	private String week;

	//bi-directional many-to-one association to RequisitoInst
	@OneToMany(mappedBy="udoInst", cascade={CascadeType.REMOVE})
	@CascadeOnDelete
	private List<RequisitoInst> requisitoInsts;


	//bi-directional many-to-one association to DomandaInst
	@ManyToOne
	@JoinColumn(name="ID_DOMANDA_FK")
	private DomandaInst domandaInst;

	//Si mantiene nello storico perche' i TipiUdo non verranno mai cancellati 
	//i dati visualizzati sono comunque storicizzati in tipoUdoTemplDescr e tipoUdoTemplTipoUdo22TemplCodiceUdo
	//bi-directional many-to-one association to TipoUdoTempl
	@ManyToOne
	@JoinColumn(name="ID_TIPO_UDO_FK")
	@JoinFetch(value=JoinFetchType.INNER)
	private TipoUdoTempl tipoUdoTempl;
	
	//bi-directional many-to-one association to AttoInst
	@ManyToOne
	@JoinColumn(name="ID_ATTO_INST_FK")
	private AttoInst attoInst;

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
	
	public UdoInst() {
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

	public String getBrancheInfo() {
		return this.brancheInfo;
	}

	public void setBrancheInfo(String brancheInfo) {
		this.brancheInfo = brancheInfo;
	}
	
	public String getCodiceStruttDenomin() {
		return this.codiceStruttDenomin;
	}

	public void setCodiceStruttDenomin(String codiceStruttDenomin) {
		this.codiceStruttDenomin = codiceStruttDenomin;
	}

	public String getCodiceUlssTerritoriale() {
		return this.codiceUlssTerritoriale;
	}

	public void setCodiceUlssTerritoriale(String codiceUlssTerritoriale) {
		this.codiceUlssTerritoriale = codiceUlssTerritoriale;
	}

	public String getCodiceUlssPrecedente() {
		return codiceUlssPrecedente;
	}

	public void setCodiceUlssPrecedente(String codiceUlssPrecedente) {
		this.codiceUlssPrecedente = codiceUlssPrecedente;
	}

	public Date getCreation() {
		return this.creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public String getDenomStruttFisicaSede() {
		return this.denomStruttFisicaSede;
	}

	public void setDenomStruttFisicaSede(String denomStruttFisicaSede) {
		this.denomStruttFisicaSede = denomStruttFisicaSede;
	}

	public String getDenominazioneSede() {
		return this.denominazioneSede;
	}

	public void setDenominazioneSede(String denominazioneSede) {
		this.denominazioneSede = denominazioneSede;
	}

	public String getDenominazioneUo() {
		return this.denominazioneUo;
	}

	public void setDenominazioneUo(String denominazioneUo) {
		this.denominazioneUo = denominazioneUo;
	}

	public String getTipoNodoUo() {
		return tipoNodoUo;
	}

	public void setTipoNodoUo(String tipoNodoUo) {
		this.tipoNodoUo = tipoNodoUo;
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

	public String getDisciplineInfo() {
		return this.disciplineInfo;
	}

	public void setDisciplineInfo(String disciplineInfo) {
		this.disciplineInfo = disciplineInfo;
	}

	public Date getEnded() {
		return this.ended;
	}

	public void setEnded(Date ended) {
		this.ended = ended;
	}

	public String getEsito() {
		return this.esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public Date getEsitoDataInizio() {
		return this.esitoDataInizio;
	}

	public void setEsitoDataInizio(Date esitoDataInizio) {
		this.esitoDataInizio = esitoDataInizio;
	}

	public String getEsitoDirezioneOperatore() {
		return this.esitoDirezioneOperatore;
	}

	public void setEsitoDirezioneOperatore(String esitoDirezioneOperatore) {
		this.esitoDirezioneOperatore = esitoDirezioneOperatore;
	}	

	public String getEsitoNote() {
		return this.esitoNote;
	}

	public void setEsitoNote(String esitoNote) {
		this.esitoNote = esitoNote;
	}
	
	public String getEsitoOperatore() {
		return this.esitoOperatore;
	}

	public void setEsitoOperatore(String esitoOperatore) {
		this.esitoOperatore = esitoOperatore;
	}

	public Date getEsitoScadenza() {
		return this.esitoScadenza;
	}

	public void setEsitoScadenza(Date esitoScadenza) {
		this.esitoScadenza = esitoScadenza;
	}

	public Date getEsitoTimeStamp() {
		return esitoTimeStamp;
	}

	public void setEsitoTimeStamp(Date esitoTimeStamp) {
		this.esitoTimeStamp = esitoTimeStamp;
	}

	public String getEsitoStatoStr() {
		return esitoStatoStr;
	}

	public void setEsitoStatoStr(String esitoStatoStr) {
		this.esitoStatoStr = esitoStatoStr;
	}

	public StatoEsitoEnum getEsitoStato() {
		if(this.esitoStatoStr == null || this.esitoStatoStr.isEmpty()) {
			return null;
		} else if("C".equals(this.esitoStatoStr)) {
            return StatoEsitoEnum.CONTROLLARE;
		} else if("D".equals(this.esitoStatoStr)) {
	        return StatoEsitoEnum.DEFINITIVO;
		} else {
	        throw new IllegalArgumentException("getEsitoStato esitoStatoStr sconosciuto: " + this.esitoStatoStr);
	    }
		//return esitoStato;
	}

	public void setEsitoStato(StatoEsitoEnum esitoStato) {
		if(esitoStato == null) {
			this.esitoStatoStr = null;
		} else {
			switch (esitoStato) {
		        case CONTROLLARE:
		        	this.esitoStatoStr = "C";
		        case DEFINITIVO:
		        	this.esitoStatoStr = "D";
		        default:
		            throw new IllegalArgumentException("StatoEsitoEnum sconosciuto: " + esitoStato);
		    }
		}
		//this.esitoStato = esitoStato;
	}

	public String getEstensioniInfo() {
		return this.estensioniInfo;
	}

	public void setEstensioniInfo(String estensioniInfo) {
		this.estensioniInfo = estensioniInfo;
	}

	public String getFattProdInfo() {
		return this.fattProdInfo;
	}

	public void setFattProdInfo(String fattProdInfo) {
		this.fattProdInfo = fattProdInfo;
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

	public String getUdoModelUoModelClientid() {
		return this.provenienzaUo + this.idUo.toString();
	}

	/*public void setUdoModelUoModelClientid(String udoModelUoModelClientid) {
		this.udoModelUoModelClientid = udoModelUoModelClientid;
	}*/

	public String getOldUdoModelUoModelClientid() {
		return oldUdoModelUoModelClientid;
	}

	public void setOldUdoModelUoModelClientid(String oldUdoModelUoModelClientid) {
		this.oldUdoModelUoModelClientid = oldUdoModelUoModelClientid;
	}

	public String getProvenienzaUo() {
		return provenienzaUo;
	}

	public void setProvenienzaUo(String provenienzaUo) {
		this.provenienzaUo = provenienzaUo;
	}
	
	//Modifica Organigramma aggiunta id univoco COD_NODO_REGIONALE
	public String getIdUo() {
		return idUo;
	}

	public void setIdUo(String idUo) {
		this.idUo = idUo;
	}

	public String getIdUser() {
		return this.idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getIndirizzoSede() {
		return this.indirizzoSede;
	}

	public void setIndirizzoSede(String indirizzoSede) {
		this.indirizzoSede = indirizzoSede;
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

	public String getPrestazioniInfo() {
		return this.prestazioniInfo;
	}

	public void setPrestazioniInfo(String prestazioniInfo) {
		this.prestazioniInfo = prestazioniInfo;
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

	public String getTipoPuntoFisicoSede() {
		return this.tipoPuntoFisicoSede;
	}

	public void setTipoPuntoFisicoSede(String tipoPuntoFisicoSede) {
		this.tipoPuntoFisicoSede = tipoPuntoFisicoSede;
	}

	public String getTipoUdoTemplTipoUdo22TemplCodiceUdo() {
		return this.tipoUdoTemplTipoUdo22TemplCodiceUdo;
	}

	public void setTipoUdoTemplTipoUdo22TemplCodiceUdo(String tipoUdoTemplTipoUdo22TemplCodiceUdo) {
		this.tipoUdoTemplTipoUdo22TemplCodiceUdo = tipoUdoTemplTipoUdo22TemplCodiceUdo;
	}
	
	public AttoInst getAttoInst() {
		return attoInst;
	}

	public void setAttoInst(AttoInst attoInst) {
		this.attoInst = attoInst;
	}	

	public String getTipoUdoTemplTipoUdo22TemplNomeCodiceUdo() {
		return tipoUdoTemplTipoUdo22TemplNomeCodiceUdo;
	}

	public void setTipoUdoTemplTipoUdo22TemplNomeCodiceUdo(
			String tipoUdoTemplTipoUdo22TemplNomeCodiceUdo) {
		this.tipoUdoTemplTipoUdo22TemplNomeCodiceUdo = tipoUdoTemplTipoUdo22TemplNomeCodiceUdo;
	}

	public String getTipoUdoTemplDescr() {
		return this.tipoUdoTemplDescr;
	}

	public void setTipoUdoTemplDescr(String tipoUdoTemplDescr) {
		this.tipoUdoTemplDescr = tipoUdoTemplDescr;
	}
	
	public String getWeek() {
		return this.week;
	}

	public void setWeek(String week) {
		this.week = week;
	}	
	
	@Transient
	public String getTipoUdoExtendedDescr() {
		String toRet = "";
		if(this.tipoUdoTemplDescr != null)
			toRet = this.tipoUdoTemplDescr;
		if(this.tipoUdoTemplTipoUdo22TemplCodiceUdo != null)
			toRet += " - " + this.tipoUdoTemplTipoUdo22TemplCodiceUdo;
		if(this.tipoUdoTemplTipoUdo22TemplNomeCodiceUdo != null)
			toRet += " - " + this.tipoUdoTemplTipoUdo22TemplNomeCodiceUdo;
		return toRet;
	}

	@Transient
	private List<BrancaUdoInstInfo> brancheUdoInstsInfo = null;
	public List<BrancaUdoInstInfo> getBrancheUdoInstsInfo() {
		if(brancheUdoInstsInfo == null) {
			if(this.getBrancheInfo() != null && !this.getBrancheInfo().trim().isEmpty()) {
				TypeToken<List<BrancaUdoInstInfo>> listType = new TypeToken<List<BrancaUdoInstInfo>>() {};
				Gson gson = new Gson();
				List<BrancaUdoInstInfo> listFromJson = gson.fromJson(this.getBrancheInfo(), listType.getType());
				if(listFromJson != null)
					this.brancheUdoInstsInfo = listFromJson;
				else
					this.brancheUdoInstsInfo = new ArrayList<BrancaUdoInstInfo>();
			} else {
				this.brancheUdoInstsInfo = new ArrayList<BrancaUdoInstInfo>();				
			}
		}
		return this.brancheUdoInstsInfo;
	}

	@Transient
	private List<EstensioneUdoInstInfo> estensioniUdoInstsInfo = null;
	public List<EstensioneUdoInstInfo> getEstensioniUdoInstsInfo() {
		if(estensioniUdoInstsInfo == null) {
			if(this.getEstensioniInfo() != null && !this.getEstensioniInfo().trim().isEmpty()) {
				TypeToken<List<EstensioneUdoInstInfo>> listType = new TypeToken<List<EstensioneUdoInstInfo>>() {};
				Gson gson = new Gson();
				List<EstensioneUdoInstInfo> listFromJson = gson.fromJson(this.getEstensioniInfo(), listType.getType());
				if(listFromJson != null)
					this.estensioniUdoInstsInfo = listFromJson;
				else
					this.estensioniUdoInstsInfo = new ArrayList<EstensioneUdoInstInfo>();
			} else {
				this.estensioniUdoInstsInfo = new ArrayList<EstensioneUdoInstInfo>();
			}
		}
		return this.estensioniUdoInstsInfo;
	}

	@Transient
	private List<DisciplinaUdoInstInfo> disciplineUdoInstsInfo = null;
	public List<DisciplinaUdoInstInfo> getDisciplineUdoInstsInfo() {
		if(disciplineUdoInstsInfo == null) {
			if(this.getDisciplineInfo() != null && !this.getDisciplineInfo().trim().isEmpty()) {
				TypeToken<List<DisciplinaUdoInstInfo>> listType = new TypeToken<List<DisciplinaUdoInstInfo>>() {};
				Gson gson = new Gson();
				List<DisciplinaUdoInstInfo> listFromJson = gson.fromJson(this.getDisciplineInfo(), listType.getType());
				if(listFromJson != null)
					this.disciplineUdoInstsInfo = listFromJson;
				else
					this.disciplineUdoInstsInfo = new ArrayList<DisciplinaUdoInstInfo>();
			} else {
				this.disciplineUdoInstsInfo = new ArrayList<DisciplinaUdoInstInfo>();
			}
		}
		return this.disciplineUdoInstsInfo;
	}

	@Transient
	private List<PrestazioneUdoInstInfo> prestazioniUdoInstsInfo = null;
	public List<PrestazioneUdoInstInfo> getPrestazioniUdoInstsInfo() {
		if(prestazioniUdoInstsInfo == null) {
			if(this.getPrestazioniInfo() != null && !this.getPrestazioniInfo().trim().isEmpty()) {
				TypeToken<List<PrestazioneUdoInstInfo>> listType = new TypeToken<List<PrestazioneUdoInstInfo>>() {};
				Gson gson = new Gson();
				List<PrestazioneUdoInstInfo> listFromJson = gson.fromJson(this.getPrestazioniInfo(), listType.getType());
				if(listFromJson != null)
					this.prestazioniUdoInstsInfo = listFromJson;
				else
					this.prestazioniUdoInstsInfo = new ArrayList<PrestazioneUdoInstInfo>();
			}
		}
		return this.prestazioniUdoInstsInfo;
	}

	@Transient
	private List<FattProdUdoInstInfo> fattProdUdoInstsInfo = null;
	public List<FattProdUdoInstInfo> getFattProdUdoInstsInfo() {
		if(fattProdUdoInstsInfo == null) {
			//carico i dati dal campo in formato json
			
			if(this.getFattProdInfo() != null && !this.getFattProdInfo().trim().isEmpty()) {
				//try {
					TypeToken<List<FattProdUdoInstInfo>> listType = new TypeToken<List<FattProdUdoInstInfo>>() {};
					Gson gson = new Gson();
					List<FattProdUdoInstInfo> listFromJson = gson.fromJson(this.getFattProdInfo(), listType.getType());
					if(listFromJson != null)
						this.fattProdUdoInstsInfo = listFromJson;
					else
						this.fattProdUdoInstsInfo = new ArrayList<FattProdUdoInstInfo>();
			} else {
				this.fattProdUdoInstsInfo = new ArrayList<FattProdUdoInstInfo>();				
			}
		}
		return this.fattProdUdoInstsInfo;
	}
	
	public List<RequisitoInst> getRequisitoInsts() {
		return this.requisitoInsts;
	}

	public void setRequisitoInsts(List<RequisitoInst> requisitoInsts) {
		this.requisitoInsts = requisitoInsts;
	}

	public RequisitoInst addRequisitoInst(RequisitoInst requisitoInst) {
		getRequisitoInsts().add(requisitoInst);
		requisitoInst.setUdoInst(this);

		return requisitoInst;
	}

	public RequisitoInst removeRequisitoInst(RequisitoInst requisitoInst) {
		getRequisitoInsts().remove(requisitoInst);
		requisitoInst.setUdoInst(null);

		return requisitoInst;
	}

	public DomandaInst getDomandaInst() {
		return this.domandaInst;
	}

	public void setDomandaInst(DomandaInst domandaInst) {
		this.domandaInst = domandaInst;
	}

	public TipoUdoTempl getTipoUdoTempl() {
		return this.tipoUdoTempl;
	}

	public void setTipoUdoTempl(TipoUdoTempl tipoUdoTempl) {
		this.tipoUdoTempl = tipoUdoTempl;
	}

	public String getStrutturaModelClientid() {
		return strutturaModelClientid;
	}

	public void setStrutturaModelClientid(String strutturaModelClientid) {
		this.strutturaModelClientid = strutturaModelClientid;
	}

	public String getEdificioTemplClientid() {
		return edificioTemplClientid;
	}

	public void setEdificioTemplClientid(String edificioTemplClientid) {
		this.edificioTemplClientid = edificioTemplClientid;
	}

	public String getUdoModelClientid() {
		return udoModelClientid;
	}

	public void setUdoModelClientid(String udoModelClientid) {
		this.udoModelClientid = udoModelClientid;
	}

	public String getDenominazioneUoConId() {
		if("UO_MODEL".equalsIgnoreCase(this.provenienzaUo))
			return this.denominazioneUo;
		return this.denominazioneUo + " (" + this.idUo + ")";
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

}