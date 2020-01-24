package it.tredi.auac.orm.entity;

import it.tredi.auac.business.ClassificazioneUdoTemplSaluteMentaleDistinctInfo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.CascadeOnDelete;


/**
 * The persistent class for the DOMANDA_INST database table.
 * 
 */
@Entity
@Table(name="DOMANDA_INST_WITH_REQ_INFO")
@NamedEntityGraphs({
	@NamedEntityGraph(name="DomandaInst.showFolder",
		attributeNodes={
			@NamedAttributeNode(value="uoInsts", subgraph="uoInst"),
			@NamedAttributeNode(value="udoInsts", subgraph="udoInst"),
			@NamedAttributeNode(value="titolareModel", subgraph="titolareModel"),
			@NamedAttributeNode(value="tipoProcTempl", subgraph="tipoProcTempl"),
			@NamedAttributeNode("passataDaStatiVerifica")
		},
		subgraphs={
			@NamedSubgraph(name="tipoProcTempl",
					attributeNodes={
						@NamedAttributeNode("descr"),
						@NamedAttributeNode("nomeWf")
			}),
			@NamedSubgraph(name="uoInst",
				attributeNodes={
					@NamedAttributeNode("denominazione"),
					@NamedAttributeNode("esito"),
					@NamedAttributeNode("esitoDataInizio"),
					@NamedAttributeNode("esitoScadenza"),
					@NamedAttributeNode("esitoNote"),
					@NamedAttributeNode("esitoOperatore"),
					@NamedAttributeNode("annotations"),
					@NamedAttributeNode("numReqSenzaRisposta"),
					@NamedAttributeNode("numReqNonAssegnati"),
					//@NamedAttributeNode("uoModelClientid")
					@NamedAttributeNode("provenienzaUo"),
					@NamedAttributeNode("idUo")
					//@NamedAttributeNode(value="uoModel", subgraph="uoModel")
			}),
			@NamedSubgraph(name="udoInst",
				attributeNodes={
					@NamedAttributeNode("descr"),
					@NamedAttributeNode("denominazioneSede"),
					@NamedAttributeNode("dirSanitarioCogn"),
					@NamedAttributeNode("dirSanitarioCfisc"),
					@NamedAttributeNode("dirSanitarioNome"),
					@NamedAttributeNode("stabilimento"),
					@NamedAttributeNode("blocco"),
					@NamedAttributeNode("piano"),
					@NamedAttributeNode("progressivo"),
					@NamedAttributeNode("esito"),
					@NamedAttributeNode("esitoDataInizio"),
					@NamedAttributeNode("esitoScadenza"),
					@NamedAttributeNode("esitoNote"),
					@NamedAttributeNode("esitoOperatore"),
					@NamedAttributeNode("esitoDirezioneOperatore"),
					@NamedAttributeNode("esitoTimeStamp"),
					@NamedAttributeNode("esitoStatoStr"),
					@NamedAttributeNode("annotations"),
					@NamedAttributeNode("codiceUlssTerritoriale"),
					@NamedAttributeNode("codiceStruttDenomin"),
					@NamedAttributeNode("denomStruttFisicaSede"),
					@NamedAttributeNode("denominazioneUo"),
					@NamedAttributeNode("codiceStruttDenomin"),
					@NamedAttributeNode("indirizzoSede"),
					@NamedAttributeNode("tipoPuntoFisicoSede"),
					//@NamedAttributeNode("admin"),
	
					@NamedAttributeNode("idUnivoco"),
					@NamedAttributeNode("stato"),
					@NamedAttributeNode("scadenza"),
					@NamedAttributeNode("udoModelClientid"),
					//@NamedAttributeNode("udoModelUoModelClientid"),
					@NamedAttributeNode("provenienzaUo"),
					@NamedAttributeNode("idUo"),
					
					@NamedAttributeNode("tipoUdoTemplDescr"),
					@NamedAttributeNode("tipoUdoTemplTipoUdo22TemplCodiceUdo"),
					@NamedAttributeNode("tipoUdoTemplTipoUdo22TemplNomeCodiceUdo"),
	
					@NamedAttributeNode("flagModulo"),
					@NamedAttributeNode("week"),

					@NamedAttributeNode("brancheInfo"),
					@NamedAttributeNode("estensioniInfo"),
					@NamedAttributeNode("disciplineInfo"),
					@NamedAttributeNode("prestazioniInfo"),
					@NamedAttributeNode("fattProdInfo"),
	
	
					@NamedAttributeNode("numReqSenzaRisposta"),
					@NamedAttributeNode("numReqNonAssegnati"),
	
					//@NamedAttributeNode(value="disciplinaTempls", subgraph="disciplinaTempl"),
					//@NamedAttributeNode(value="brancaTempls", subgraph="brancaTempl"),
					//@NamedAttributeNode(value="udoModel", subgraph="udoModel"),
					@NamedAttributeNode(value="tipoUdoTempl", subgraph="tipoUdoTempl")
			}),
			/*@NamedSubgraph(name="uoModel",
				attributeNodes={
					//@NamedAttributeNode(value="titolareModel", subgraph="titolareModel")
					
			}),*/
			/*@NamedSubgraph(name="udoModel",
				attributeNodes={
					@NamedAttributeNode("idUnivoco"),
					@NamedAttributeNode("stato"),
					@NamedAttributeNode(value="uoModel", subgraph="uoModel")
			}),*/
			@NamedSubgraph(name="tipoUdoTempl",
				attributeNodes={
					@NamedAttributeNode("descr"),
					@NamedAttributeNode(value="tipoUdo22Templ", subgraph="tipoUdo22Templ")
			}),
			@NamedSubgraph(name="tipoUdo22Templ",
			attributeNodes={
				@NamedAttributeNode("codiceUdo")
				//@NamedAttributeNode(value="titolareModel", subgraph="titolareModel")
			}),
			@NamedSubgraph(name="titolareModel",
				attributeNodes={
					@NamedAttributeNode("ragSoc")
			}),
			@NamedSubgraph(name="disciplinaTempl",
			attributeNodes={
				@NamedAttributeNode("descr")
			})
			/*,
			@NamedSubgraph(name="brancaTempl",
			attributeNodes={
				@NamedAttributeNode("descr"),
				@NamedAttributeNode("codice")
			})*/
		}
	),
	@NamedEntityGraph(name="DomandaInst.addWorkflow",
		attributeNodes={
			@NamedAttributeNode(value="uoInsts", subgraph="uoInst"),
			@NamedAttributeNode(value="udoInsts", subgraph="udoInst"),
			@NamedAttributeNode(value="titolareModel", subgraph="titolareModel"),
			@NamedAttributeNode(value="tipoProcTempl", subgraph="tipoProcTempl")
		},
		subgraphs={
			@NamedSubgraph(name="titolareModel",
					attributeNodes={
						@NamedAttributeNode("ragSoc")
			}),
			@NamedSubgraph(name="tipoProcTempl",
					attributeNodes={
						@NamedAttributeNode("descr")
			}),
			@NamedSubgraph(name="uoInst",
				attributeNodes={
					//@NamedAttributeNode("uoModelClientid")
					@NamedAttributeNode("provenienzaUo"),
					@NamedAttributeNode("idUo")
			}),
			@NamedSubgraph(name="udoInst",
				attributeNodes={
					@NamedAttributeNode("udoModelClientid"),
					//@NamedAttributeNode("udoModelUoModelClientid"),
					@NamedAttributeNode("provenienzaUo"),
					@NamedAttributeNode("idUo"),
					@NamedAttributeNode(value="tipoUdoTempl", subgraph="tipoUdoTempl")
			}),
			@NamedSubgraph(name="tipoUdoTempl",
				attributeNodes={
					@NamedAttributeNode(value="tipoUdo22Templ", subgraph="tipoUdo22Templ")
			}),
			@NamedSubgraph(name="tipoUdo22Templ",
				attributeNodes={
					//.getUdoInst().getTipoUdoTempl().getTipoUdo22Templ().getClassificazioneUdoTempl().getDirezioneTempls()
					//@NamedAttributeNode("clientid")
					@NamedAttributeNode(value="classificazioneUdoTempl", subgraph="classificazioneUdoTempl")
			}),
			@NamedSubgraph(name="classificazioneUdoTempl",
				attributeNodes={
					//.getUdoInst().getTipoUdoTempl().getTipoUdo22Templ().getClassificazioneUdoTempl().getDirezioneTempls()
					//@NamedAttributeNode("clientid")
					@NamedAttributeNode(value="direzioneTempls", subgraph="direzioneTempls")
			}),
			@NamedSubgraph(name="direzioneTempls",
				attributeNodes={
					//.getUdoInst().getTipoUdoTempl().getTipoUdo22Templ().getClassificazioneUdoTempl().getDirezioneTempls()
					//@NamedAttributeNode(value="descr")
					@NamedAttributeNode(value="clientid")
			})
		}
	)
})
public class DomandaInst implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String clientid;

	private String annotations;

	@Temporal(TemporalType.DATE)
	private Date creation;

	private String descr;

	private String disabled;

	@Temporal(TemporalType.DATE)
	private Date ended;

	@Column(name="ID_DOMANDA")
	private BigDecimal idDomanda;

	@Column(name="ID_USER")
	private String idUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD")
	private Date lastMod;

	@Column(name="NUMERO_PROCEDIMENTO")
	private String numeroProcedimento;

	private String stato;

	@Column(name="NUM_REQ_SENZA_RISPOSTA", insertable=false, updatable=false)
	private Integer numReqSenzaRisposta;

	@Column(name="NUM_REQ_NON_ASSEGNATI", insertable=false, updatable=false)
	private Integer numReqNonAssegnati;
	
	//Indica se la domanda è passata da uno degli stati 'GESTIONE DELLE VERIFICHE INSERIMENTO VERIFICHE', 'GESTIONE DELLE VERIFICHE'
	//Utilizzato per gestire i requisiti SR
	@Column(name="PASSATA_STATI_VERIFICA")
	private String passataDaStatiVerifica = "N";

	//bi-directional many-to-one association to AnagraficaUtenteModel
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_LEGALE_RAPPR_FK")
	private AnagraficaUtenteModel anagraficaUtenteModel;

	//bi-directional many-to-one association to TipoProcTempl
	@ManyToOne
	@JoinColumn(name="ID_TIPO_PROC_FK")
	private TipoProcTempl tipoProcTempl;

	////di default eagerly load
	//bi-directional many-to-one association to TitolareModel
	@ManyToOne
	@JoinColumn(name="ID_TITOLARE_FK")
	private TitolareModel titolareModel;

	////di default lazy load
	//bi-directional many-to-one association to RequisitoInst
	@OneToMany(mappedBy="domandaInst", cascade={CascadeType.REMOVE})
	@CascadeOnDelete
	private List<RequisitoInst> requisitoInsts;
	
	////di default lazy load
	//bi-directional many-to-one association to UdoInst
	@OneToMany(mappedBy="domandaInst", cascade={CascadeType.REMOVE})
	private List<EdificioInst> edificioInsts;

	////di default lazy load
	//bi-directional many-to-one association to UdoInst
	@OneToMany(mappedBy="domandaInst", cascade={CascadeType.REMOVE})
	private List<StrutturaInst> strutturaInsts;

	////di default lazy load
	//bi-directional many-to-one association to UdoInst
	@OneToMany(mappedBy="domandaInst", cascade={CascadeType.REMOVE})
	private List<UdoInst> udoInsts;

	//bi-directional many-to-one association to UoInst
	@OneToMany(mappedBy="domandaInst", cascade={CascadeType.REMOVE})
	private List<UoInst> uoInsts;
	
	//bi-directional many-to-one association to AttoInst
	@OneToMany(mappedBy="domandaInst", cascade={CascadeType.REMOVE})
	private List<AttoInst> attoInsts;

	//uni-directional many-to-many association to UtenteModel
	@ManyToMany
	@JoinTable(
		name="BIND_DOMANDA_VERIFICATORE"
		, joinColumns={
			@JoinColumn(name="ID_DOMANDA_INST_FK")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_UTENTE_MODEL_FK")
			}
		)
	private List<UtenteModel> verificatori;
	
	public DomandaInst() {
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

	public BigDecimal getIdDomanda() {
		return this.idDomanda;
	}

	public void setIdDomanda(BigDecimal idDomanda) {
		this.idDomanda = idDomanda;
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

	public String getNumeroProcedimento() {
		return this.numeroProcedimento;
	}

	public void setNumeroProcedimento(String numeroProcedimento) {
		this.numeroProcedimento = numeroProcedimento;
	}

	public String getStato() {
		return this.stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

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

	public String getPassataDaStatiVerifica() {
		return passataDaStatiVerifica;
	}

	public void setPassataDaStatiVerifica(String passataDaStatiVerifica) {
		this.passataDaStatiVerifica = passataDaStatiVerifica;
	}

	public AnagraficaUtenteModel getAnagraficaUtenteModel() {
		return this.anagraficaUtenteModel;
	}

	public void setAnagraficaUtenteModel(AnagraficaUtenteModel anagraficaUtenteModel) {
		this.anagraficaUtenteModel = anagraficaUtenteModel;
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

	public List<RequisitoInst> getRequisitoInsts() {
		return this.requisitoInsts;
	}

	public void setRequisitoInsts(List<RequisitoInst> requisitoInsts) {
		this.requisitoInsts = requisitoInsts;
	}

	public RequisitoInst addRequisitoInst(RequisitoInst requisitoInst) {
		getRequisitoInsts().add(requisitoInst);
		requisitoInst.setDomandaInst(this);

		return requisitoInst;
	}

	public RequisitoInst removeRequisitoInst(RequisitoInst requisitoInst) {
		getRequisitoInsts().remove(requisitoInst);
		requisitoInst.setDomandaInst(null);

		return requisitoInst;
	}

	public List<StrutturaInst> getStrutturaInsts() {
		return this.strutturaInsts;
	}

	public void setStrutturaInsts(List<StrutturaInst> strutturaInsts) {
		this.strutturaInsts = strutturaInsts;
	}

	public StrutturaInst addStrutturaInst(StrutturaInst strutturaInst) {
		getStrutturaInsts().add(strutturaInst);
		strutturaInst.setDomandaInst(this);

		return strutturaInst;
	}

	public StrutturaInst removeStrutturaInst(StrutturaInst strutturaInst) {
		getStrutturaInsts().remove(strutturaInst);
		strutturaInst.setDomandaInst(null);

		return strutturaInst;
	}

	public List<EdificioInst> getEdificioInsts() {
		return this.edificioInsts;
	}

	public void setEdificioInsts(List<EdificioInst> edificioInsts) {
		this.edificioInsts = edificioInsts;
	}

	public EdificioInst addEdificioInst(EdificioInst edificioInst) {
		getEdificioInsts().add(edificioInst);
		edificioInst.setDomandaInst(this);

		return edificioInst;
	}

	public EdificioInst removeEdificioInst(EdificioInst edificioInst) {
		getEdificioInsts().remove(edificioInst);
		edificioInst.setDomandaInst(null);

		return edificioInst;
	}


	public List<UdoInst> getUdoInsts() {
		return this.udoInsts;
	}

	public void setUdoInsts(List<UdoInst> udoInsts) {
		this.udoInsts = udoInsts;
	}

	public UdoInst addUdoInst(UdoInst udoInst) {
		getUdoInsts().add(udoInst);
		udoInst.setDomandaInst(this);

		return udoInst;
	}

	public UdoInst removeUdoInst(UdoInst udoInst) {
		getUdoInsts().remove(udoInst);
		udoInst.setDomandaInst(null);

		return udoInst;
	}

	public List<UoInst> getUoInsts() {
		return this.uoInsts;
	}

	public void setUoInsts(List<UoInst> uoInsts) {
		this.uoInsts = uoInsts;
	}

	public UoInst addUoInst(UoInst uoInst) {
		getUoInsts().add(uoInst);
		uoInst.setDomandaInst(this);

		return uoInst;
	}

	public UoInst removeUoInst(UoInst uoInst) {
		getUoInsts().remove(uoInst);
		uoInst.setDomandaInst(null);

		return uoInst;
	}
	
	public List<AttoInst> getAttoInsts() {
		return attoInsts;
	}

	public void setAttoInsts(List<AttoInst> attoInsts) {
		this.attoInsts = attoInsts;
	}

	public AttoInst addAttoInst(AttoInst attoInst) {
		getAttoInsts().add(attoInst);
		attoInst.setDomandaInst(this);

		return attoInst;
	}

	public AttoInst removeAttoInst(AttoInst attoInst) {
		getAttoInsts().remove(attoInst);
		attoInst.setDomandaInst(null);

		return attoInst;
	}


	public List<UtenteModel> getVerificatori() {
		return this.verificatori;
	}

	public void setVerificatori(List<UtenteModel> verificatori) {
		this.verificatori = verificatori;
	}
	
	public List<ClassificazioneUdoTempl> getClassificazioneUdoTemplForAllUdoInst() {
		HashSet<String> keys = new HashSet<String>();
		ArrayList<ClassificazioneUdoTempl> toRet = new ArrayList<ClassificazioneUdoTempl>();
	    String key;

		for(UdoInst udoInst:getUdoInsts()) {
			key = udoInst.getTipoUdoTempl().getTipoUdo22Templ().getClassificazioneUdoTempl().getClientid();
			if(!keys.contains(key)) {
				keys.add(key);
				toRet.add(udoInst.getTipoUdoTempl().getTipoUdo22Templ().getClassificazioneUdoTempl());
			}
		}
		return toRet;
	}

	public List<ClassificazioneUdoTempl> getClassificazioneUdoTemplForAllUdoInstConScarto(HashSet<String> udoInstsClientIdDaNonConsiderare) {
		HashSet<String> keys = new HashSet<String>();
		ArrayList<ClassificazioneUdoTempl> toRet = new ArrayList<ClassificazioneUdoTempl>();
	    String key;

		for(UdoInst udoInst:getUdoInsts()) {
			if(udoInstsClientIdDaNonConsiderare.contains(udoInst.getClientid()))
				continue;
			key = udoInst.getTipoUdoTempl().getTipoUdo22Templ().getClassificazioneUdoTempl().getClientid();
			if(!keys.contains(key)) {
				keys.add(key);
				toRet.add(udoInst.getTipoUdoTempl().getTipoUdo22Templ().getClassificazioneUdoTempl());
			}
		}
		return toRet;
	}

	public ClassificazioneUdoTemplSaluteMentaleDistinctInfo getClassificazioneUdoTemplSaluteMentaleDistinctInfoForUdoInstOfUoInst(UoInst uoInst) {
		ClassificazioneUdoTemplSaluteMentaleDistinctInfo toRet = new ClassificazioneUdoTemplSaluteMentaleDistinctInfo();

		for(UdoInst udoInst:getUdoInstFiglie(uoInst)) {
			toRet.addUdoInst(udoInst);
		}
		return toRet;
	}

	public ClassificazioneUdoTemplSaluteMentaleDistinctInfo getClassificazioneUdoTemplSaluteMentaleDistinctInfoForUdoInstOfUoInstConScarto(UoInst uoInst, HashSet<String> udoInstsClientIdDaNonConsiderare) {
		ClassificazioneUdoTemplSaluteMentaleDistinctInfo toRet = new ClassificazioneUdoTemplSaluteMentaleDistinctInfo();

		for(UdoInst udoInst:getUdoInstFiglie(uoInst)) {
			toRet.addUdoInstConScarto(udoInst, udoInstsClientIdDaNonConsiderare);
		}
		return toRet;
	}

	/*
	 * Metodo che restituisce la UoInst padre della UdoInst, restituisce null se non trovato
	 */
	public UoInst getUoInstPadre(UdoInst udoInst) {
		for(UoInst uoInst:getUoInsts()) {
			if(uoInst.getUoModelClientid().equals(udoInst.getUdoModelUoModelClientid()))
				return uoInst;
		}

		return null;
	}

	/*
	 * Metodo che restituisce la lista delle UdoInst figlie della UoInst passata, restituisce lista vuota se non trovate
	 */
	public List<UdoInst> getUdoInstFiglie(UoInst uoInst) {
		ArrayList<UdoInst> toRet = new ArrayList<UdoInst>();
		for(UdoInst udoInst:getUdoInsts()) {
			if(uoInst.getUoModelClientid().equals(udoInst.getUdoModelUoModelClientid()))
				toRet.add(udoInst);
		}

		return toRet;
	}

	/*
	 * Metodo che restituisce la lista delle UdoInst figlie della UoInst passata scartando l'elenco delle UdoInst di cui viene passato il clientId, restituisce lista vuota se non trovate
	 */
	public List<UdoInst> getUdoInstFiglieConScarto(UoInst uoInst, HashSet<String> udoInstsClientIdDaNonConsiderare) {
		ArrayList<UdoInst> toRet = new ArrayList<UdoInst>();
		for(UdoInst udoInst:getUdoInsts()) {
			if(udoInstsClientIdDaNonConsiderare.contains(udoInst.getClientid()))
				continue;
			if(uoInst.getUoModelClientid().equals(udoInst.getUdoModelUoModelClientid()))
				toRet.add(udoInst);
		}

		return toRet;
	}
}