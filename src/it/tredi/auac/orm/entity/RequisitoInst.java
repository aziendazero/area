package it.tredi.auac.orm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.CascadeOnDelete;

import it.tredi.auac.BooleanEnum;
import it.tredi.auac.TipoGenerazioneValutazioneVerificaSrEnum;
import it.tredi.auac.TipoValutazioneVerificaEnum;
import it.tredi.auac.converter.BooleanEnumConverter;
import it.tredi.auac.converter.TipoGenerazioneValutazioneVerificaSrEnumConverter;
import it.tredi.auac.converter.TipoValutazioneVerificaEnumConverter;

/**
 * The persistent class for the REQUISITO_INST database table.
 * 
 */
@Entity
@Table(name="REQUISITO_INST_WITH_STOR_INFO")
@NamedQueries({
	@NamedQuery(name="RequisitoInst.findRequisitiByDomandaInstUtenteModel", query="select r from RequisitoInst r "
			+ "left join r.domandaInst dom left join r.udoInst udo left join r.edificioInst ed left join r.strutturaInst st left join r.uoInst uo left join r.classificazioneUdoTempl cludo inner join r.requisitoTempl rt "
			+ "where r.utenteModel=:utenteModel and r.rootClientidDomanda = :domandaClientId "
			+ "order by dom.clientid, uo.clientid, udo.clientid, st.clientid, ed.clientid, cludo.clientid, r.idLista, r.listaRequisitoOrdine, r.saluteMentale, rt.nome"),
	@NamedQuery(name="RequisitoInst.findAllRequisitiByDomandaInst", query="select r from RequisitoInst r "
			+ "left join r.domandaInst dom left join r.udoInst udo left join r.edificioInst ed left join r.strutturaInst st left join r.uoInst uo left join r.classificazioneUdoTempl cludo inner join r.requisitoTempl rt "
			+ "where r.rootClientidDomanda = :domandaClientId "
			+ "order by dom.clientid, uo.clientid, udo.clientid, st.clientid, ed.clientid, cludo.clientid, r.idLista, r.listaRequisitoOrdine, r.saluteMentale, rt.nome"),
	@NamedQuery(name="RequisitoInst.findRequisitiGeneraliAziendali", query="select r from RequisitoInst r "
			+ "left join r.domandaInst dom left join r.classificazioneUdoTempl cludo inner join r.requisitoTempl rt "
			+ "where r.domandaInst=:domandaInst "
			+ "order by cludo.clientid, r.idLista, r.listaRequisitoOrdine, rt.nome"),
	@NamedQuery(name="RequisitoInst.findRequisitiGeneraliAziendaliForReportArea", query="select r from RequisitoInst r "
			+ "left join r.domandaInst dom left join r.classificazioneUdoTempl cludo inner join r.requisitoTempl rt inner join rt.tipoRisposta trisp "
			+ "where r.domandaInst=:domandaInst and trisp.nome IN ('Soglia', 'Si/No')"),
	@NamedQuery(name="RequisitoInst.findRequisitiByUoInst", query="select r from RequisitoInst r "
			+ "left join r.classificazioneUdoTempl cludo left join r.uoInst uo inner join r.requisitoTempl rt "
			+ "where r.uoInst=:uoInst "
			+ "order by cludo.clientid, r.idLista, r.listaRequisitoOrdine, r.saluteMentale, rt.nome"),
	@NamedQuery(name="RequisitoInst.findRequisitiByUdoInst", query="select r from RequisitoInst r "
			+ "left join r.udoInst udo left join r.uoInst uo inner join r.requisitoTempl rt "
			+ "where r.udoInst=:udoInst "
			+ "order by r.idLista, r.listaRequisitoOrdine, rt.nome"),
	@NamedQuery(name="RequisitoInst.findRequisitiByStrutturaInst", query="select r from RequisitoInst r "
			+ "left join r.strutturaInst struttura inner join r.requisitoTempl rt "
			+ "where r.strutturaInst=:strutturaInst "
			+ "order by r.idLista, r.listaRequisitoOrdine, rt.nome"),
	@NamedQuery(name="RequisitoInst.findRequisitiByEdificioInst", query="select r from RequisitoInst r "
			+ "left join r.edificioInst edificio inner join r.requisitoTempl rt "
			+ "where r.edificioInst=:edificioInst "
			+ "order by r.idLista, r.listaRequisitoOrdine, rt.nome"),
	@NamedQuery(name="RequisitoInst.findRequisitiGeneraliAziendaliForVerbaleVerifica", query="select r from RequisitoInst r "
			+ "left join r.domandaInst dom left join r.classificazioneUdoTempl cludo inner join r.requisitoTempl rt left join rt.tipoRisposta trisp "
			+ "where r.domandaInst=:domandaInst and r.valutazione!=r.valutazioneVerificatore "
			+ "order by cludo.clientid, r.idLista, r.listaRequisitoOrdine, rt.nome"),
	@NamedQuery(name="RequisitoInst.findRequisitiByUoInstForVerbaleVerifica", query="select r from RequisitoInst r "
			+ "left join r.classificazioneUdoTempl cludo left join r.uoInst uo inner join r.requisitoTempl rt left join rt.tipoRisposta trisp "
			+ "where r.uoInst=:uoInst and r.valutazione!=r.valutazioneVerificatore "
			+ "order by cludo.clientid, r.idLista, r.listaRequisitoOrdine, r.saluteMentale, rt.nome"),
	@NamedQuery(name="RequisitoInst.findRequisitiByUdoInstForVerbaleVerifica", query="select r from RequisitoInst r "
			+ "inner join r.udoInst udo inner join r.requisitoTempl rt left join rt.tipoRisposta trisp "
			//+ "where r.udoInst=:udoInst and (trisp.nome='Titolo' or r.valutazione!=r.valutazioneVerificatore) "
			+ "where r.udoInst=:udoInst and r.valutazione!=r.valutazioneVerificatore "
			+ "order by r.idLista, r.listaRequisitoOrdine, rt.nome"),
	@NamedQuery(name="RequisitoInst.findRequisitiByStrutturaInstForVerbaleVerifica", query="select r from RequisitoInst r "
			+ "left join r.strutturaInst struttura inner join r.requisitoTempl rt left join rt.tipoRisposta trisp "
			+ "where r.strutturaInst=:strutturaInst and r.valutazione!=r.valutazioneVerificatore "
			+ "order by r.idLista, r.listaRequisitoOrdine, rt.nome"),
	@NamedQuery(name="RequisitoInst.findRequisitiByEdificioInstForVerbaleVerifica", query="select r from RequisitoInst r "
			+ "left join r.edificioInst edificio inner join r.requisitoTempl rt left join rt.tipoRisposta trisp "
			+ "where r.edificioInst=:edificioInst and r.valutazione!=r.valutazioneVerificatore "
			+ "order by r.idLista, r.listaRequisitoOrdine, rt.nome"),
	
	@NamedQuery(name="RequisitoInst.findRequisitiConAllegati", query="select r from RequisitoInst r "
			+ "where r.allegatiNRecord IS NOT NULL ")
			
})
@NamedEntityGraphs({
	@NamedEntityGraph(name="RequisitoInst.autovalutazione",
		attributeNodes={
			@NamedAttributeNode("tipoValutazione"),
			@NamedAttributeNode("tipoVerifica"),
			@NamedAttributeNode("valutazione"),
			@NamedAttributeNode("autoValutazione"),
			@NamedAttributeNode("note"),
			@NamedAttributeNode("valutazioneVerificatore"),
			@NamedAttributeNode("autoValutazioneVerificatore"),
			@NamedAttributeNode("noteVerificatore"),
			@NamedAttributeNode("allegatiNRecord"),
			@NamedAttributeNode("evidenze"),
			@NamedAttributeNode("numStorico"),
			@NamedAttributeNode(value="uoInst", subgraph="uoInst"),
			@NamedAttributeNode(value="udoInst", subgraph="udoInst"),
			@NamedAttributeNode(value="edificioInst", subgraph="edificioInst"),
			@NamedAttributeNode(value="strutturaInst", subgraph="strutturaInst"),
			@NamedAttributeNode(value="requisitoTempl", subgraph="requisitoTempl"),
			@NamedAttributeNode(value="utenteModel", subgraph="utenteModel"),
			@NamedAttributeNode(value="verificatore", subgraph="verificatore")
		},
		subgraphs={
			@NamedSubgraph(name="uoInst",
				attributeNodes={
					@NamedAttributeNode("denominazione"),
					@NamedAttributeNode("provenienzaUo"),
					@NamedAttributeNode("idUo")
			}),
			@NamedSubgraph(name="strutturaInst",
				attributeNodes={
					@NamedAttributeNode("denominazione")
			}),
			@NamedSubgraph(name="edificioInst",
				attributeNodes={
					@NamedAttributeNode("codice"),
					@NamedAttributeNode("nome"),
					@NamedAttributeNode("sedeOperModelDenominazione")
			}),
			@NamedSubgraph(name="udoInst",
				attributeNodes={
					@NamedAttributeNode("idUnivoco"),
					@NamedAttributeNode("descr"),
					@NamedAttributeNode("blocco"),
					@NamedAttributeNode("piano"),
					@NamedAttributeNode("stabilimento"),
					@NamedAttributeNode("progressivo"),
					@NamedAttributeNode("denominazioneSede"),
					@NamedAttributeNode("denominazioneUo"),
					@NamedAttributeNode("tipoUdoTemplDescr"),
					@NamedAttributeNode("tipoUdoTemplTipoUdo22TemplCodiceUdo"),
					@NamedAttributeNode("tipoUdoTemplTipoUdo22TemplNomeCodiceUdo"),
					
					@NamedAttributeNode("fattProdInfo"),
					@NamedAttributeNode("brancheInfo"),
					@NamedAttributeNode("disciplineInfo")
					
					//@NamedAttributeNode(value="tipoUdoTempl", subgraph="tipoUdoTempl"),
			}),
			@NamedSubgraph(name="requisitoTempl",
				attributeNodes={
					@NamedAttributeNode("nome"),
					@NamedAttributeNode("testo"),
					@NamedAttributeNode("tipo"),
					@NamedAttributeNode("tipoSpecifico"),
					@NamedAttributeNode(value="tipoRisposta", subgraph="tipoRisposta")
			}),
			@NamedSubgraph(name="tipoRisposta",
			attributeNodes={
				@NamedAttributeNode("nome")
			}),
			@NamedSubgraph(name="utenteModel",
				attributeNodes={
					@NamedAttributeNode(value="anagraficaUtenteModel", subgraph="anagraficaUtenteModel"),
					@NamedAttributeNode(value="uoModel", subgraph="uoModel")
			}),
			@NamedSubgraph(name="verificatore",
				attributeNodes={
					@NamedAttributeNode(value="anagraficaUtenteModel", subgraph="anagraficaUtenteModel")
			}),
			@NamedSubgraph(name="anagraficaUtenteModel",
				attributeNodes={
					@NamedAttributeNode("cognome"),
					@NamedAttributeNode("nome")
			}),
			@NamedSubgraph(name="uoModel",
			attributeNodes={
				@NamedAttributeNode("denominazione")
			})
		}
	),
	@NamedEntityGraph(name="RequisitoInst.exportPdf",
		attributeNodes={
			@NamedAttributeNode("tipoValutazione"),
			@NamedAttributeNode("tipoVerifica"),
			@NamedAttributeNode("valutazione"),
			@NamedAttributeNode("note"),
			@NamedAttributeNode("valutazioneVerificatore"),
			@NamedAttributeNode("noteVerificatore"),
			@NamedAttributeNode("evidenze"),
			@NamedAttributeNode("allegatiFilename"),
			@NamedAttributeNode(value="requisitoTempl", subgraph="requisitoTempl"),
			@NamedAttributeNode(value="utenteModel", subgraph="utenteModel"),
			@NamedAttributeNode(value="verificatore", subgraph="verificatore")
		},
		subgraphs={
			@NamedSubgraph(name="requisitoTempl",
				attributeNodes={
					@NamedAttributeNode("nome"),
					@NamedAttributeNode("testo"),
					@NamedAttributeNode(value="tipoRisposta", subgraph="tipoRisposta")
			}),
			@NamedSubgraph(name="tipoRisposta",
			attributeNodes={
				@NamedAttributeNode("nome")
			}),
			@NamedSubgraph(name="utenteModel",
			attributeNodes={
				@NamedAttributeNode(value="anagraficaUtenteModel", subgraph="anagraficaUtenteModel"),
				@NamedAttributeNode(value="uoModel", subgraph="uoModel")
			}),
			@NamedSubgraph(name="verificatore",
			attributeNodes={
				@NamedAttributeNode(value="anagraficaUtenteModel", subgraph="anagraficaUtenteModel")
			}),
			@NamedSubgraph(name="anagraficaUtenteModel",
				attributeNodes={
					@NamedAttributeNode("cognome"),
					@NamedAttributeNode("nome")
			}),
			@NamedSubgraph(name="uoModel",
			attributeNodes={
				@NamedAttributeNode("denominazione")
			})
		}
	),
	
	@NamedEntityGraph(name="RequisitoInst.requisitiGeneraliForReport",
		attributeNodes={
			@NamedAttributeNode("tipoValutazione"),
			@NamedAttributeNode("tipoVerifica"),
			@NamedAttributeNode("valutazione"),
			//@NamedAttributeNode("note"),
			@NamedAttributeNode("valutazioneVerificatore"),
			//@NamedAttributeNode("noteVerificatore"),
			//@NamedAttributeNode("evidenze"),
			//@NamedAttributeNode("allegatiFilename"),
			@NamedAttributeNode(value="requisitoTempl", subgraph="requisitoTempl")
			//@NamedAttributeNode(value="utenteModel", subgraph="utenteModel"),
			//@NamedAttributeNode(value="verificatore", subgraph="verificatore")
		},
		subgraphs={
			@NamedSubgraph(name="requisitoTempl",
				attributeNodes={
					@NamedAttributeNode("nome"),
					@NamedAttributeNode("testo"),
					@NamedAttributeNode(value="tipoRisposta", subgraph="tipoRisposta")
			}),
			@NamedSubgraph(name="tipoRisposta",
			attributeNodes={
				@NamedAttributeNode("nome")
			})
		}
	)
})	
public class RequisitoInst implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	@Column(name="ALLEGATI_FILENAME")
	private String allegatiFilename;

	@Column(name="ALLEGATI_N_RECORD")
	private String allegatiNRecord;

	private String annotations;

	@Temporal(TemporalType.DATE)
	private Date creation;

	private String descr;

	private String disabled;

	@Temporal(TemporalType.DATE)
	private Date ended;

	private String evidenze;

	@Column(name="ID_REQUISITO")
	private BigDecimal idRequisito;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	private String note;
	
	@Column(name="note_verificatore")
	private String noteVerificatore;

	@Column(name="SALUTE_MENTALE")
	private String saluteMentale;

	private String valutazione;

	@Column(name="valut_verificatore")
	private String valutazioneVerificatore;

	@Column(name="ROOT_ID_DOMANDA_FK")
	private String rootClientidDomanda;
	
	@Column(name="tipo_sr")
	@Convert(converter = BooleanEnumConverter.class)
	private BooleanEnum isSr = BooleanEnum.FALSE;
	
//	ALTER TABLE REQUISITO_INST ADD (AUTO_VALUTAZIONE VARCHAR2(200 BYTE));
	@Column(name="auto_valutazione")
	private String autoValutazione;

//	ALTER TABLE REQUISITO_INST ADD (AUTO_VALUT_VERIFICATORE VARCHAR2(200 BYTE));
	@Column(name="auto_valut_verificatore")
	private String autoValutazioneVerificatore;

//	-- Campo in cui indicare se la valutazione del requisito e' inseribile 
//	-- manualmente (M), automaticamente (A), semi automaticamente (S) cioe' media inserita in campo AUTO_VALUTAZIONE con inserimento manuale in VALUTAZIONE
//	ALTER TABLE REQUISITO_INST ADD (TIPO_VALUTAZIONE CHAR(1 BYTE) DEFAULT 'M' NOT NULL ENABLE);
	@Column(name="tipo_valutazione")
	@Convert(converter = TipoValutazioneVerificaEnumConverter.class)
	private TipoValutazioneVerificaEnum tipoValutazione = TipoValutazioneVerificaEnum.MANUALE;
	
	//	-- Campo in cui indicare se la verifica del requisito e' inseribile
//	-- manualmente (M), automaticamente (A), semi automaticamente (S) cioe' media inserita in campo AUTO_VALUT_VERIFICATORE con inserimento manuale in VALUT_VERIFICATORE
//	ALTER TABLE REQUISITO_INST ADD (TIPO_VERIFICA CHAR(1 BYTE) DEFAULT 'M' NOT NULL ENABLE);
	@Column(name="tipo_verifica")
	@Convert(converter = TipoValutazioneVerificaEnumConverter.class)
	private TipoValutazioneVerificaEnum tipoVerifica = TipoValutazioneVerificaEnum.MANUALE;

//	-- Campo in cui indicare se i valori inseriti in valutazione del requisito devono essere usati per calcolare in modo automatico la valutazione dei requisiti generali SR
//	-- possibili valori: N non aggiorna i dati, P aggiorna il campo VALUTAZIONE, A aggiorna il campo AUTO_VALUTAZIONE
//	ALTER TABLE REQUISITO_INST ADD (GEN_VALUTAZIONE_SR CHAR(1 BYTE) DEFAULT 'N' NOT NULL ENABLE);
	@Column(name="gen_valutazione_sr")
	@Convert(converter = TipoGenerazioneValutazioneVerificaSrEnumConverter.class)
	private TipoGenerazioneValutazioneVerificaSrEnum generaValutazioneSr = TipoGenerazioneValutazioneVerificaSrEnum.NESSUNAGGIORNAMENTO;

//	-- Campo in cui indicare se i valori inseriti in verifica del requisito devono essere usati per calcolare in modo automatico la verifica dei requisiti generali SR
//	-- possibili valori: N non aggiorna i dati, P aggiorna il campo VALUT_VERIFICATORE, A aggiorna il campo AUTO_VALUT_VERIFICATORE
//	ALTER TABLE REQUISITO_INST ADD (GEN_VALUT_VERIFICATORE_SR CHAR(1 BYTE) DEFAULT 'N' NOT NULL ENABLE);
	@Column(name="gen_valut_verificatore_sr")
	@Convert(converter = TipoGenerazioneValutazioneVerificaSrEnumConverter.class)
	private TipoGenerazioneValutazioneVerificaSrEnum generaValutazioneVerificatoreSr = TipoGenerazioneValutazioneVerificaSrEnum.NESSUNAGGIORNAMENTO;
	
	//27-05-2016 Aggiunto per gestire l'ordinamento dei requisiti
	//idlista della lista di appartenenza del RequisitoTemplate da cui e' stato creato il RequisitpInst
	@Column(name="ID_LISTA")
	private BigDecimal idLista;

	//LISTA_REQUISITO_ORDINE riporta il valore del campo BIND_LISTA_REQUISITO.ORDINE
	@Column(name="LISTA_REQUISITO_ORDINE")
	private BigDecimal listaRequisitoOrdine;

	//bi-directional many-to-one association to ClassificazioneUdoTempl
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_CLASS_UDO_TEMPL_FK")
	private ClassificazioneUdoTempl classificazioneUdoTempl;

	//bi-directional many-to-one association to DomandaInst
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_DOMANDA_FK")
	private DomandaInst domandaInst;

	//bi-directional many-to-one association to RequisitoTempl
	@ManyToOne
	@JoinColumn(name="ID_REQ_TEMPL_FK")
	private RequisitoTempl requisitoTempl;

	//bi-directional many-to-one association to StrutturaInst
	@ManyToOne
	@JoinColumn(name="ID_STRUTTURA_FK")
	private StrutturaInst strutturaInst;

	//bi-directional many-to-one association to EdificioInst
	@ManyToOne
	@JoinColumn(name="ID_EDIFICIO_FK")
	private EdificioInst edificioInst;

	//bi-directional many-to-one association to UdoInst
	@ManyToOne
	@JoinColumn(name="ID_UDO_FK")
	private UdoInst udoInst;

	//bi-directional many-to-one association to UoInst
	@ManyToOne
	@JoinColumn(name="ID_UO_FK")
	private UoInst uoInst;

	//bi-directional many-to-one association to UtenteModel
	@ManyToOne
	@JoinColumn(name="ID_ASSEGNATARIO_FK")
	private UtenteModel utenteModel;

	//bi-directional many-to-one association to UtenteModel
	@ManyToOne
	@JoinColumn(name="ID_VERIFICATORE_FK")
	private UtenteModel verificatore;

	//bi-directional many-to-one association to StoricoRisposteRequisiti
	@OneToMany(mappedBy="requisitoInst", cascade={CascadeType.REMOVE})
	@CascadeOnDelete
	private List<StoricoRisposteRequisiti> storicoRisposteRequisitis;

	@Column(name="NUM_STORICO", insertable=false, updatable=false)
	private Integer numStorico;
	
	public RequisitoInst() {
	}

	public String getClientid() {
		return this.clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public String getAllegatiFilename() {
		return this.allegatiFilename;
	}

	public void setAllegatiFilename(String allegatiFilename) {
		this.allegatiFilename = allegatiFilename;
	}

	public String getAllegatiNRecord() {
		return this.allegatiNRecord;
	}

	public void setAllegatiNRecord(String allegatiNRecord) {
		this.allegatiNRecord = allegatiNRecord;
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

	public String getEvidenze() {
		return this.evidenze;
	}

	public void setEvidenze(String evidenze) {
		this.evidenze = evidenze;
	}

	public BigDecimal getIdRequisito() {
		return this.idRequisito;
	}

	public void setIdRequisito(BigDecimal idRequisito) {
		this.idRequisito = idRequisito;
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

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNoteVerificatore() {
		return noteVerificatore;
	}

	public void setNoteVerificatore(String noteVerificatore) {
		this.noteVerificatore = noteVerificatore;
	}

	public String getSaluteMentale() {
		return this.saluteMentale;
	}

	public void setSaluteMentale(String saluteMentale) {
		this.saluteMentale = saluteMentale;
	}

	public String getValutazione() {
		return this.valutazione;
	}

	public void setValutazione(String valutazione) {
		this.valutazione = valutazione;
	}

	public String getValutazioneVerificatore() {
		return valutazioneVerificatore;
	}

	public void setValutazioneVerificatore(String valutazioneVerificatore) {
		this.valutazioneVerificatore = valutazioneVerificatore;
	}

	public ClassificazioneUdoTempl getClassificazioneUdoTempl() {
		return this.classificazioneUdoTempl;
	}

	public void setClassificazioneUdoTempl(ClassificazioneUdoTempl classificazioneUdoTempl) {
		this.classificazioneUdoTempl = classificazioneUdoTempl;
	}

	public DomandaInst getDomandaInst() {
		return this.domandaInst;
	}

	public void setDomandaInst(DomandaInst domandaInst) {
		this.domandaInst = domandaInst;
	}

	public RequisitoTempl getRequisitoTempl() {
		return this.requisitoTempl;
	}

	public void setRequisitoTempl(RequisitoTempl requisitoTempl) {
		this.requisitoTempl = requisitoTempl;
	}

	public StrutturaInst getStrutturaInst() {
		return this.strutturaInst;
	}

	public void setStrutturaInst(StrutturaInst strutturaInst) {
		this.strutturaInst = strutturaInst;
	}

	public EdificioInst getEdificioInst() {
		return edificioInst;
	}

	public void setEdificioInst(EdificioInst edificioInst) {
		this.edificioInst = edificioInst;
	}

	public UdoInst getUdoInst() {
		return this.udoInst;
	}

	public void setUdoInst(UdoInst udoInst) {
		this.udoInst = udoInst;
	}

	public UoInst getUoInst() {
		return this.uoInst;
	}

	public void setUoInst(UoInst uoInst) {
		this.uoInst = uoInst;
	}

	public UtenteModel getUtenteModel() {
		return this.utenteModel;
	}

	public void setUtenteModel(UtenteModel utenteModel) {
		this.utenteModel = utenteModel;
	}

	public UtenteModel getVerificatore() {
		return verificatore;
	}

	public void setVerificatore(UtenteModel verificatore) {
		this.verificatore = verificatore;
	}

	public List<StoricoRisposteRequisiti> getStoricoRisposteRequisitis() {
		return this.storicoRisposteRequisitis;
	}

	public void setStoricoRisposteRequisitis(List<StoricoRisposteRequisiti> storicoRisposteRequisitis) {
		this.storicoRisposteRequisitis = storicoRisposteRequisitis;
	}

	public StoricoRisposteRequisiti addStoricoRisposteRequisiti(StoricoRisposteRequisiti storicoRisposteRequisiti) {
		getStoricoRisposteRequisitis().add(storicoRisposteRequisiti);
		storicoRisposteRequisiti.setRequisitoInst(this);

		return storicoRisposteRequisiti;
	}

	public StoricoRisposteRequisiti removeStoricoRisposteRequisiti(StoricoRisposteRequisiti storicoRisposteRequisiti) {
		getStoricoRisposteRequisitis().remove(storicoRisposteRequisiti);
		storicoRisposteRequisiti.setRequisitoInst(null);

		return storicoRisposteRequisiti;
	}

	public Integer getNumStorico() {
		return numStorico;
	}

	public void setNumStorico(Integer numStorico) {
		this.numStorico = numStorico;
	}
	
	/*
	public String getAutovalutazioneForExport() {
        return getRispostaForReport(getRequisitoTempl().getTipoRisposta(), getValutazione());
	}
	*/
	
	public String getAssegnazioneForExport() {
		String toRet = "";
		if(getUtenteModel() != null) {
			if(getUtenteModel().getUoModel() != null)
				toRet = getUtenteModel().getUoModel().getDenominazione() + " - " + getUtenteModel().getAnagraficaUtenteModel().getCognome() + " " + getUtenteModel().getAnagraficaUtenteModel().getNome();
			else
				toRet = getUtenteModel().getAnagraficaUtenteModel().getCognome() + " " + getUtenteModel().getAnagraficaUtenteModel().getNome();
		}
        return toRet;
	}
	
	public String getVerificatoreForExport() {
		String toRet = "";
		if(getVerificatore() != null) {
			toRet = getVerificatore().getAnagraficaUtenteModel().getCognomeNome();
		}
        return toRet;
	}

	/*
	public String getValutazioneVerificatoreForExport() {
        return getRispostaForReport(getRequisitoTempl().getTipoRisposta(), getValutazioneVerificatore());
	}
	*/
	
	/*
	private String getRispostaForReport(TipoRisposta tipoRisposta, String risposta) {
		String toRet = "";
        if(tipoRisposta != null) {
        	if(tipoRisposta.getNome().equals("Soglia"))
        		toRet = risposta;
        	else if(tipoRisposta.getNome().equals("Si/No"))
        		if("1".equals(risposta))
        			toRet = "Si";
        		else if("0".equals(risposta))
        			toRet = "No";
        		else if("2".equals(risposta))
        			toRet = "Non Pertinente";
        		else if("-1".equals(risposta))
        			toRet = "Non Campionato";
        		//else
        		//	toRet = "";//non valorizzato
        	else if(tipoRisposta.getNome().equals("Quantitativo") || tipoRisposta.getNome().equals("Quantitativa"))
        		toRet = risposta;//Requisito - Autovalutazione
        }
        return toRet;
	}
	*/
	
	public boolean hasStoricoVerifica() {
		return verificatore != null || valutazioneVerificatore != null || noteVerificatore != null;
	}

	public BigDecimal getIdLista() {
		return idLista;
	}

	public void setIdLista(BigDecimal idLista) {
		this.idLista = idLista;
	}

	public BigDecimal getListaRequisitoOrdine() {
		return listaRequisitoOrdine;
	}

	public void setListaRequisitoOrdine(BigDecimal listaRequisitoOrdine) {
		this.listaRequisitoOrdine = listaRequisitoOrdine;
	}

	public TipoValutazioneVerificaEnum getTipoValutazione() {
		return tipoValutazione;
	}

	public void setTipoValutazione(TipoValutazioneVerificaEnum tipoValutazione) {
		this.tipoValutazione = tipoValutazione;
	}

	public String getAutoValutazione() {
		return autoValutazione;
	}

	public void setAutoValutazione(String autoValutazione) {
		this.autoValutazione = autoValutazione;
	}

	public String getAutoValutazioneVerificatore() {
		return autoValutazioneVerificatore;
	}

	public void setAutoValutazioneVerificatore(String autoValutazioneVerificatore) {
		this.autoValutazioneVerificatore = autoValutazioneVerificatore;
	}

	public TipoValutazioneVerificaEnum getTipoVerifica() {
		return tipoVerifica;
	}

	public void setTipoVerifica(TipoValutazioneVerificaEnum tipoVerifica) {
		this.tipoVerifica = tipoVerifica;
	}

	public TipoGenerazioneValutazioneVerificaSrEnum getGeneraValutazioneSr() {
		return generaValutazioneSr;
	}

	public void setGeneraValutazioneSr(
			TipoGenerazioneValutazioneVerificaSrEnum generaValutazioneSr) {
		this.generaValutazioneSr = generaValutazioneSr;
	}

	public TipoGenerazioneValutazioneVerificaSrEnum getGeneraValutazioneVerificatoreSr() {
		return generaValutazioneVerificatoreSr;
	}

	public void setGeneraValutazioneVerificatoreSr(
			TipoGenerazioneValutazioneVerificaSrEnum generaValutazioneVerificatoreSr) {
		this.generaValutazioneVerificatoreSr = generaValutazioneVerificatoreSr;
	}

	public BooleanEnum getIsSr() {
		return isSr;
	}

	public void setIsSr(BooleanEnum isSr) {
		this.isSr = isSr;
	}

	public String getRootClientidDomanda() {
		return rootClientidDomanda;
	}

	public void setRootClientidDomanda(String rootClientidDomanda) {
		this.rootClientidDomanda = rootClientidDomanda;
	}
	
	public String getType() {
		if(uoInst != null)
			return "uo";
		else if(udoInst != null)
			return "ud";
		else if(strutturaInst != null)
			return "st";
		else if(edificioInst != null)
			return "ed";
		return "ge";
	}
}