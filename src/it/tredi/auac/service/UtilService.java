package it.tredi.auac.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import it.tredi.auac.BooleanEnum;
import it.tredi.auac.TipoValutazioneVerificaEnum;
import it.tredi.auac.TypeViewEnum;
import it.tredi.auac.ValutazioneTipoSiNoEnum;
import it.tredi.auac.ValutazioneTipoSogliaEnum;
import it.tredi.auac.ValutazioneVerificatoreTipoSiNoEnum;
import it.tredi.auac.ValutazioneVerificatoreTipoSogliaEnum;
import it.tredi.auac.business.ClassificazioneUdoTemplSaluteMentaleDistinctInfo;
import it.tredi.auac.dao.DomandaInstDao;
import it.tredi.auac.dao.RequisitoInstDao;
import it.tredi.auac.orm.entity.BindListaRequClassUdo;
import it.tredi.auac.orm.entity.BindListaRequTipoTitClassTit;
import it.tredi.auac.orm.entity.BindListaRequisito;
import it.tredi.auac.orm.entity.BindListaRequisitoEdificioTipoTitClassTit;
import it.tredi.auac.orm.entity.BindUoProcLista;
import it.tredi.auac.orm.entity.ClassificazioneUdoTempl;
import it.tredi.auac.orm.entity.DomandaInst;
import it.tredi.auac.orm.entity.ListaRequisitiTempl;
import it.tredi.auac.orm.entity.RequisitoInst;
import it.tredi.auac.orm.entity.TipoProcTempl;
import it.tredi.auac.orm.entity.TipoRisposta;
import it.tredi.auac.orm.entity.TitolareModel;
import it.tredi.auac.orm.entity.UoInst;
import it.tredi.auac.utils.AuacUtils;

@Service
public class UtilService {
	private final String valutazioneTipoTitoloCsv = "*";

	public final String TIPO_DOMANDA_COMPLESSIVA = "Complessiva";
	
	@Autowired
	private RequisitoInstDao requisitoInstDao;

	@Autowired
	private MessageSource messageSource;
	
	@Value("#{new java.text.SimpleDateFormat(\"yyyyMMdd\").parse(\"${dataCreazioneDomandeDopoCuiInserireRequisitiEdifici}\")}")
	private Date dataCreazioneDomandeDopoCuiInserireRequisitiEdifici;

	@Value("#{new java.text.SimpleDateFormat(\"yyyyMMdd\").parse(\"${dataCreazioneDomandeDopoCuiInserireRequisitiStrutture}\")}")
	private Date dataCreazioneDomandeDopoCuiInserireRequisitiStrutture;
	
	@Value("#{new java.text.SimpleDateFormat(\"yyyyMMdd\").parse(\"${dataCreazioneDomandeDopoCuiGestireListeRequisitiGeneraliSR}\")}")
	private Date dataCreazioneDomandeDopoCuiGestireListeRequisitiGeneraliSR;

	@Value("#{new java.text.SimpleDateFormat(\"yyyyMMdd\").parse(\"${dataCreazioneDomandeDopoCuiGestireRequisitiDiversificatiPerTipoDomanda}\")}")
	private Date dataCreazioneDomandeDopoCuiGestireRequisitiDiversificatiPerTipoDomanda;

	private String getTextSiNoMedia(String valore) {
		//il valore quando valorizzato e' tipo 5$2 che significa 5 Si e 2 No
		//il carattere $ e' un carattere speciale quindi lo escape
		String[] valSiNo = valore.split(Pattern.quote("$"));
		if (valSiNo.length == 2)
			return getTextSiNoMedia(valSiNo[0], valSiNo[1]);
		else
			return valore;		
	}
	
	public String getTextSiNoMedia(String numSi, String numNo) {
		return numSi + " Si / " + numNo + " No";
	}

	public String getTextSiNoMedia(int numSi, int numNo) {
		return numSi + " Si / " + numNo + " No";
	}

	private String getTextSogliaMedia(String valore) {
		return valore + "%";
	}
		
	public Date getDataCreazioneDomandeDopoCuiInserireRequisitiEdifici() {
		return dataCreazioneDomandeDopoCuiInserireRequisitiEdifici;
	}

	public void setDataCreazioneDomandeDopoCuiInserireRequisitiEdifici(
			Date dataCreazioneDomandeDopoCuiInserireRequisitiEdifici) {
		this.dataCreazioneDomandeDopoCuiInserireRequisitiEdifici = dataCreazioneDomandeDopoCuiInserireRequisitiEdifici;
	}
		
	public Date getDataCreazioneDomandeDopoCuiInserireRequisitiStrutture() {
		return dataCreazioneDomandeDopoCuiInserireRequisitiStrutture;
	}

	public void setDataCreazioneDomandeDopoCuiInserireRequisitiStrutture(
			Date dataCreazioneDomandeDopoCuiInserireRequisitiStrutture) {
		this.dataCreazioneDomandeDopoCuiInserireRequisitiStrutture = dataCreazioneDomandeDopoCuiInserireRequisitiStrutture;
	}
		
	public Date getDataCreazioneDomandeDopoCuiGestireListeRequisitiGeneraliSR() {
		return dataCreazioneDomandeDopoCuiGestireListeRequisitiGeneraliSR;
	}

	public Date getDataCreazioneDomandeDopoCuiGestireRequisitiDiversificatiPerTipoDomanda() {
		return dataCreazioneDomandeDopoCuiGestireRequisitiDiversificatiPerTipoDomanda;
	}

	public void setDataCreazioneDomandeDopoCuiGestireRequisitiDiversificatiPerTipoDomanda(
			Date dataCreazioneDomandeDopoCuiGestireRequisitiDiversificatiPerTipoDomanda) {
		this.dataCreazioneDomandeDopoCuiGestireRequisitiDiversificatiPerTipoDomanda = dataCreazioneDomandeDopoCuiGestireRequisitiDiversificatiPerTipoDomanda;
	}

	public void setDataCreazioneDomandeDopoCuiGestireListeRequisitiGeneraliSR(
			Date dataCreazioneDomandeDopoCuiGestireListeRequisitiGeneraliSR) {
		this.dataCreazioneDomandeDopoCuiGestireListeRequisitiGeneraliSR = dataCreazioneDomandeDopoCuiGestireListeRequisitiGeneraliSR;
	}

	public String getValutazioneVerificatoreFormattedText(RequisitoInst requisitoInst, TypeViewEnum typeView) {
		return getValutazioneVerificatoreFormattedText(requisitoInst.getRequisitoTempl().getTipoRisposta(), requisitoInst.getTipoVerifica(), requisitoInst.getValutazioneVerificatore(), typeView);
	}
	
	public String getValutazioneVerificatoreFormattedText(TipoRisposta tipoRisposta, TipoValutazioneVerificaEnum tipoValutazioneVerificaEnum, String valore, TypeViewEnum typeView) {
		if(valore != null && !valore.isEmpty()) {
			if(tipoRisposta != null) {
				if(tipoValutazioneVerificaEnum == TipoValutazioneVerificaEnum.AUTOMATICA) {
					return getTextMediaForTipoRisposta(tipoRisposta, typeView, valore);
				} else {//MANUALE SEMIAUTOMATICA
					if (tipoRisposta.getNome().equals("Si/No")) {
						return messageSource.getMessage(ValutazioneVerificatoreTipoSiNoEnum.getEnumByKey(valore).getValue() + typeView.getMessagePropertyExtension(), null, LocaleContextHolder.getLocale());
					}
					else if (tipoRisposta.getNome().equals("Soglia")) {
						return messageSource.getMessage(ValutazioneVerificatoreTipoSogliaEnum.getEnumByKey(valore).getValue() + typeView.getMessagePropertyExtension(), null, LocaleContextHolder.getLocale());
					}
				}
			}
		}
		return valore;
	}

	public String getAutoValutazioneVerificatoreFormattedText(RequisitoInst requisitoInst, TypeViewEnum typeView) {
		return getTextMediaForTipoRisposta(requisitoInst.getRequisitoTempl().getTipoRisposta(), typeView, requisitoInst.getAutoValutazioneVerificatore());
	}

	public String getAutoValutazioneFormattedText(RequisitoInst requisitoInst, TypeViewEnum typeView) {
		if(typeView == TypeViewEnum.CSV && AuacUtils.requisitoIsTitolo(requisitoInst)) {
			return valutazioneTipoTitoloCsv;
		}
		return getTextMediaForTipoRisposta(requisitoInst.getRequisitoTempl().getTipoRisposta(), typeView, requisitoInst.getAutoValutazione());
	}

	public String getValutazioneFormattedText(RequisitoInst requisitoInst, TypeViewEnum typeView) {
		if(typeView == TypeViewEnum.CSV && AuacUtils.requisitoIsTitolo(requisitoInst)) {
			return valutazioneTipoTitoloCsv;
		}
		return getValutazioneFormattedText(requisitoInst.getRequisitoTempl().getTipoRisposta(), requisitoInst.getTipoValutazione(), requisitoInst.getValutazione(), typeView);
	}

	public String getValutazioneFormattedText(TipoRisposta tipoRisposta, TipoValutazioneVerificaEnum tipoValutazioneVerificaEnum, String valore, TypeViewEnum typeView) {
		if(valore != null && !valore.isEmpty()) {
			if(tipoRisposta != null) {
				if(tipoValutazioneVerificaEnum == TipoValutazioneVerificaEnum.AUTOMATICA) {
					return getTextMediaForTipoRisposta(tipoRisposta, typeView, valore);
				} else {//MANUALE SEMIAUTOMATICA
					if (tipoRisposta.getNome().equals("Si/No")) {
						return messageSource.getMessage(ValutazioneTipoSiNoEnum.getEnumByKey(valore).getValue() + typeView.getMessagePropertyExtension(), null, LocaleContextHolder.getLocale());
					}
					else if (tipoRisposta.getNome().equals("Soglia")) {
						return messageSource.getMessage(ValutazioneTipoSogliaEnum.getEnumByKey(valore).getValue() + typeView.getMessagePropertyExtension(), null, LocaleContextHolder.getLocale());
					}					
				}
			}
		}
		return valore;
	}

	public String getTextMediaForTipoRisposta(TipoRisposta tipoRisposta, TypeViewEnum typeView, String valore) {
		if(valore != null && !valore.isEmpty()) {
			if(tipoRisposta != null) {
				if (tipoRisposta.getNome().equals("Si/No")) {
					return getTextSiNoMedia(valore);
				}
				else if (tipoRisposta.getNome().equals("Soglia")) {
					if(typeView == TypeViewEnum.CSV)
						return valore;
					else // WEB e PDF
						return getTextSogliaMedia(valore);
				}
			}
		}
		return valore;
	}
	
	public void addRequisitiToUoInst(UoInst uoInst, BindUoProcLista bindUoProcLista, DomandaInst domanda) {
		//for (RequisitoTempl requisitoTempl:bindUoProcLista.getListaRequisitiTempl().getRequisitoTempls()) { //per ogni requisito della lista
		for (BindListaRequisito bindListaRequisito:bindUoProcLista.getListaRequisitiTempl().getBindListaRequisitos()) { //per ogni requisito della lista
			if("N".equals(bindListaRequisito.getAnnullato()) && "S".equals(bindListaRequisito.getValidato()) 
					&& "N".equals(bindListaRequisito.getRequisitoTempl().getAnnullato()) && "S".equals(bindListaRequisito.getRequisitoTempl().getValidato())) {
				//creazione di requisitoTempl
				
				
				//30/05/2016 creato metodo utility per creare il RequisitoInst
				/*
				RequisitoInst requisitoInst = new RequisitoInst();
				if(bindUoProcLista.getId().getSaluteMentale().equals(ClassificazioneUdoTemplSaluteMentaleDistinctInfo.SALUTE_MENTALE_NO))
					requisitoInst.setClassificazioneUdoTempl(bindUoProcLista.getClassificazioneUdoTempl());
				requisitoInst.setSaluteMentale(bindUoProcLista.getId().getSaluteMentale());
				requisitoInst.setDescr(bindListaRequisito.getRequisitoTempl().getDescr());
				requisitoInst.setClientid(UUID.randomUUID().toString().toUpperCase());
				requisitoInst.setAnnotations(bindListaRequisito.getRequisitoTempl().getAnnotations());
				requisitoInst.setDisabled("N");
				requisitoInst.setRequisitoTempl(bindListaRequisito.getRequisitoTempl());
    			//27/05/2016 ordinamentop personalizzato dei requisiti
    			requisitoInst.setIdLista(bindListaRequisito.getListaRequisitiTempl().getIdLista());
    			requisitoInst.setListaRequisitoOrdine(bindListaRequisito.getOrdine());
				requisitoInst.setValutazione("");
				*/
				RequisitoInst requisitoInst;
				if(bindUoProcLista.getId().getSaluteMentale().equals(ClassificazioneUdoTemplSaluteMentaleDistinctInfo.SALUTE_MENTALE_NO))
					requisitoInst = getRequisitoInstByBindListaRequisito(bindListaRequisito, bindUoProcLista.getClassificazioneUdoTempl(), bindUoProcLista.getId().getSaluteMentale(), domanda);
				else
					requisitoInst = getRequisitoInstByBindListaRequisito(bindListaRequisito, null, bindUoProcLista.getId().getSaluteMentale(), domanda);

				uoInst.addRequisitoInst(requisitoInst);
				requisitoInstDao.save(requisitoInst);
			}
		}		
	}

	public void addRequisitiSrToDomandaInst(DomandaInst domandaInst, BindListaRequTipoTitClassTit bindListaRequClassUdo) {
		for (BindListaRequisito bindListaRequisito : bindListaRequClassUdo.getListaRequisitiTempl().getBindListaRequisitos()) { //per ogni requisito della lista
			if("N".equals(bindListaRequisito.getAnnullato()) && "S".equals(bindListaRequisito.getValidato()) 
					&& "N".equals(bindListaRequisito.getRequisitoTempl().getAnnullato()) && "S".equals(bindListaRequisito.getRequisitoTempl().getValidato())) {
				RequisitoInst requisitoInst = getRequisitoInstByBindListaRequisito(bindListaRequisito, null, ClassificazioneUdoTemplSaluteMentaleDistinctInfo.SALUTE_MENTALE_NO, domandaInst);
				//impoto che il requisito è stato ottenuto da una lista SR in modo che la stored di GestioneRequisitiSR lo controlli e setti quanto occorre in tutti i requisiti
				requisitoInst.setIsSr(BooleanEnum.TRUE);
				
				domandaInst.addRequisitoInst(requisitoInst);
				requisitoInstDao.save(requisitoInst);
			}
		}		
	}

	public void addRequisitiToDomandaInst(DomandaInst domandaInst, BindListaRequClassUdo bindListaRequClassUdo) {
		//for (RequisitoTempl requisitoTempl:bindListaRequClassUdo.getListaRequisitiTempl().getRequisitoTempls()) { //per ogni requisito della lista
		for (BindListaRequisito bindListaRequisito:bindListaRequClassUdo.getListaRequisitiTempl().getBindListaRequisitos()) { //per ogni requisito della lista
			if("N".equals(bindListaRequisito.getAnnullato()) && "S".equals(bindListaRequisito.getValidato()) 
					&& "N".equals(bindListaRequisito.getRequisitoTempl().getAnnullato()) && "S".equals(bindListaRequisito.getRequisitoTempl().getValidato())) {
				//creazione di requisitoTempl
				
				//30/05/2016 creato metodo utility per creare il RequisitoInst
				/*
				RequisitoInst requisitoInst = new RequisitoInst();
				requisitoInst.setClassificazioneUdoTempl(bindListaRequClassUdo.getClassificazioneUdoTempl());
				//Imposto il valore di default
				requisitoInst.setSaluteMentale(ClassificazioneUdoTemplSaluteMentaleDistinctInfo.SALUTE_MENTALE_NO);
				requisitoInst.setDescr(bindListaRequisito.getRequisitoTempl().getDescr());
				requisitoInst.setClientid(UUID.randomUUID().toString().toUpperCase());
				requisitoInst.setAnnotations(bindListaRequisito.getRequisitoTempl().getAnnotations());
				requisitoInst.setDisabled("N");
				requisitoInst.setRequisitoTempl(bindListaRequisito.getRequisitoTempl());
    			//27/05/2016 ordinamentop personalizzato dei requisiti
    			requisitoInst.setIdLista(bindListaRequisito.getListaRequisitiTempl().getIdLista());
    			requisitoInst.setListaRequisitoOrdine(bindListaRequisito.getOrdine());
				requisitoInst.setValutazione("");
				*/
				RequisitoInst requisitoInst = getRequisitoInstByBindListaRequisito(bindListaRequisito, bindListaRequClassUdo.getClassificazioneUdoTempl(), ClassificazioneUdoTemplSaluteMentaleDistinctInfo.SALUTE_MENTALE_NO, domandaInst);
				
				domandaInst.addRequisitoInst(requisitoInst);
				requisitoInstDao.save(requisitoInst);
			}
		}		
	}
	
	public RequisitoInst getRequisitoInstByBindListaRequisito(BindListaRequisito bindListaRequisito, ClassificazioneUdoTempl classificazioneUdoTempl, String saluteMentale, DomandaInst rootDomanda) {
		//creazione di RequisitoInst
		RequisitoInst requisitoInst = new RequisitoInst();
		requisitoInst.setRootClientidDomanda(rootDomanda.getClientid());
		requisitoInst.setClassificazioneUdoTempl(classificazioneUdoTempl);
		requisitoInst.setSaluteMentale(saluteMentale);
		requisitoInst.setDescr(bindListaRequisito.getRequisitoTempl().getDescr());
		requisitoInst.setClientid(UUID.randomUUID().toString().toUpperCase());
		requisitoInst.setAnnotations(bindListaRequisito.getRequisitoTempl().getAnnotations());
		requisitoInst.setDisabled("N");
		requisitoInst.setRequisitoTempl(bindListaRequisito.getRequisitoTempl());
		//27/05/2016 ordinamentop personalizzato dei requisiti
		requisitoInst.setIdLista(bindListaRequisito.getListaRequisitiTempl().getIdLista());
		requisitoInst.setListaRequisitoOrdine(bindListaRequisito.getOrdine());
		//udoInst.addRequisitoInst(requisitoInst);
		requisitoInst.setValutazione("");
		requisitoInst.setAutoValutazione("");

		/*
		//addRequisitiToUoInst
		if(bindUoProcLista.getId().getSaluteMentale().equals(ClassificazioneUdoTemplSaluteMentaleDistinctInfo.SALUTE_MENTALE_NO))
			requisitoInst.setClassificazioneUdoTempl(bindUoProcLista.getClassificazioneUdoTempl());
		requisitoInst.setSaluteMentale(bindUoProcLista.getId().getSaluteMentale());
		requisitoInst.setDescr(bindListaRequisito.getRequisitoTempl().getDescr());
		requisitoInst.setClientid(UUID.randomUUID().toString().toUpperCase());
		requisitoInst.setAnnotations(bindListaRequisito.getRequisitoTempl().getAnnotations());
		requisitoInst.setDisabled("N");
		requisitoInst.setRequisitoTempl(bindListaRequisito.getRequisitoTempl());
		//27/05/2016 ordinamentop personalizzato dei requisiti
		requisitoInst.setIdLista(bindListaRequisito.getListaRequisitiTempl().getIdLista());
		requisitoInst.setListaRequisitoOrdine(bindListaRequisito.getOrdine());
		uoInst.addRequisitoInst(requisitoInst);
		requisitoInst.setValutazione("");

		//addRequisitiToDomandaInst
		requisitoInst.setClassificazioneUdoTempl(bindListaRequClassUdo.getClassificazioneUdoTempl());
		//Imposto il valore di default
		requisitoInst.setSaluteMentale(ClassificazioneUdoTemplSaluteMentaleDistinctInfo.SALUTE_MENTALE_NO);
		requisitoInst.setDescr(bindListaRequisito.getRequisitoTempl().getDescr());
		requisitoInst.setClientid(UUID.randomUUID().toString().toUpperCase());
		requisitoInst.setAnnotations(bindListaRequisito.getRequisitoTempl().getAnnotations());
		requisitoInst.setDisabled("N");
		requisitoInst.setRequisitoTempl(bindListaRequisito.getRequisitoTempl());
		//27/05/2016 ordinamentop personalizzato dei requisiti
		requisitoInst.setIdLista(bindListaRequisito.getListaRequisitiTempl().getIdLista());
		requisitoInst.setListaRequisitoOrdine(bindListaRequisito.getOrdine());
		//domandaInst.addRequisitoInst(requisitoInst);
		requisitoInst.setValutazione("");
		*/
		
		
		return requisitoInst;
	}
	
	
	public boolean isStrutturaInstActiveForDomanda(DomandaInst domanda) {
		if(domanda.getCreation() != null) {
			return this.getDataCreazioneDomandeDopoCuiInserireRequisitiStrutture().before(domanda.getCreation());
		} else {
			//la domanda è nuova controllo la data corrente
			Date now = new Date();
			return this.getDataCreazioneDomandeDopoCuiInserireRequisitiStrutture().before(now);
		}
	}
	
	public boolean isEdificioInstActiveForDomanda(DomandaInst domanda) {
		if(domanda.getCreation() != null) {
			return this.getDataCreazioneDomandeDopoCuiInserireRequisitiEdifici().before(domanda.getCreation());
		} else {
			//la domanda è nuova controllo la data corrente
			Date now = new Date();
			return this.getDataCreazioneDomandeDopoCuiInserireRequisitiEdifici().before(now);
		}
	}

	public boolean isGestioneListeRequisitiGeneraliSRActiveForDomanda(DomandaInst domanda) {
		return this.isGestioneListeRequisitiGeneraliSRActiveForDomanda(domanda.getCreation()); 
	}
	
	public boolean isGestioneListeRequisitiGeneraliSRActiveForDomanda(Date domandaCreation) {
		if(domandaCreation != null) {
			return this.getDataCreazioneDomandeDopoCuiGestireListeRequisitiGeneraliSR().before(domandaCreation);
		} else {
			//la domanda è nuova controllo la data corrente
			Date now = new Date();
			return this.getDataCreazioneDomandeDopoCuiGestireListeRequisitiGeneraliSR().before(now);
		}
	}
	
	//Questo comporta che vengano inseriti o meno i RG aziendali
	public boolean isActiveInsertRequisitiGeneraliAziendaliForDomanda(DomandaInst domanda, String tipoDomanda) {
		//questo e il metodo activeInsertRequisitiUoForDomanda ora corrispondono ma sono stati tenuti staccati perchè non si sa mai che non cambino idea
		return isActiveInsertRequisitiGeneraliAziendaliAndRequisitiUoForDomanda(domanda, tipoDomanda);
	}

	//Questo comporta che vengano inseriti o meno i requisiti per le UO
	public boolean isActiveInsertRequisitiUoForDomanda(DomandaInst domanda, String tipoDomanda) {
		//questo e il metodo activeInsertRequisitiGeneraliAziendaliForDomanda ora corrispondono ma sono stati tenuti staccati perchè non si sa mai che non cambino idea 
		return isActiveInsertRequisitiGeneraliAziendaliAndRequisitiUoForDomanda(domanda, tipoDomanda);
	}
	
	private boolean isActiveInsertRequisitiGeneraliAziendaliAndRequisitiUoForDomanda(DomandaInst domanda, String tipoDomanda) {
		//se non è stata superata la data in cui attivare la creazione o meno dei RequisitiGeneraliAzienda a seconda del "Tipo domanda"
		//oppure se la data è stata superata e il "Tipo domanda" è Complessiva 
		// devono essere inseriti i requisiti
		return !isGestioneRequisitiDiversificatiPerTipoDomandaActiveForDomanda(domanda) 
				|| TIPO_DOMANDA_COMPLESSIVA.equals(tipoDomanda);
		
	}

	public boolean isGestioneRequisitiDiversificatiPerTipoDomandaActiveForDomanda(DomandaInst domanda) {
		return isGestioneRequisitiDiversificatiPerTipoDomandaActiveForDomandaCreationDate(domanda.getCreation(), domanda.getTitolareModel());
	}

	public boolean isGestioneRequisitiDiversificatiPerTipoDomandaActiveForDomandaCreationDate(Date dataCreazioneDomanda, TitolareModel titolareDomanda) {
		if(titolareDomanda == null) {
			throw new RuntimeException("titolareDomanda cannot be null");
		}
		if(titolareDomanda.getTipoTitolareTempl() == null || !"S".equals(titolareDomanda.getTipoTitolareTempl().getIsAziendaSanitaria())) {
			//titolare senza "Tipo titolare" oppure con "Tipo titolare" non di tipo Aziende Sanitarie
			return false;
		}
		if(dataCreazioneDomanda != null) {
			return this.getDataCreazioneDomandeDopoCuiGestireRequisitiDiversificatiPerTipoDomanda().before(dataCreazioneDomanda);
		} else {
			//la domanda è nuova controllo la data corrente
			Date now = new Date();
			return this.getDataCreazioneDomandeDopoCuiGestireRequisitiDiversificatiPerTipoDomanda().before(now);
		}
	}

	public void aggiornaRequisitiSrDomanda(DomandaInst domanda, boolean nuovaDomanda) throws Exception {
		if(isGestioneListeRequisitiGeneraliSRActiveForDomanda(domanda)) {
			requisitoInstDao.impostaTipoValutazioneRequisitiSr(domanda);
			requisitoInstDao.impostaTipoGenerazioneRequisitiSr(domanda);
			//Valutazioni dei requisiti non inserite 
			//non occorrerebbe effettuare l'aggiornamento delle valutazioni calcolate in automatico in inserimento
			//ma per coerenza le calcolo lo stesso saranno tutte 0$0 (0 Si / 0 No)
			requisitoInstDao.impostaValutazioneRequisitiGenSrOnDomanda(domanda);
			if(!nuovaDomanda) {
				if(!DomandaInstDao.STATO_BOZZA.equals(domanda.getStato())) {
					//Lo stato non è bozza
					if("S".equals(domanda.getPassataDaStatiVerifica())) {
						//aggiornamento delle valutazioni verificatore calcolate in automatico
						requisitoInstDao.impostaValutazioneVerificatoreRequisitiGenSrOnDomanda(domanda);
					}
				}
			}
		}
	}
	
	public List<ListaRequisitiTempl> getListaRequisitiEdificioByTitolareTipoProcedimento(TitolareModel titolareModel, TipoProcTempl tipoProcTempl) {
		List<ListaRequisitiTempl> toRet = new ArrayList<ListaRequisitiTempl>();
		if(titolareModel.getTipoTitolareTempl() != null ) {
			boolean trovataListaTipoTitOnly = false;
			//Potrebbero esserci dei requisiti generali SR da gestire
			for(BindListaRequisitoEdificioTipoTitClassTit blrs : tipoProcTempl.getBindListaRequisitoEdificioTipoTitClassTits()) {
				if(blrs.getClassificazioneTempl() == null && blrs.getTipoTitolareTempl().getClientid().equals(titolareModel.getTipoTitolareTempl().getClientid())) {
					toRet.add(blrs.getListaRequisitiTempl());
					trovataListaTipoTitOnly = true;
				}
			}
			if(!trovataListaTipoTitOnly && titolareModel.getClassificazioneTempls() != null && !titolareModel.getClassificazioneTempls().isEmpty()) {
				for(BindListaRequisitoEdificioTipoTitClassTit blrs : tipoProcTempl.getBindListaRequisitoEdificioTipoTitClassTits()) {
					if(blrs.getClassificazioneTempl() != null && blrs.getTipoTitolareTempl().getClientid().equals(titolareModel.getTipoTitolareTempl().getClientid()) && titolareModel.getClassificazioneTempls().contains(blrs.getClassificazioneTempl())) {
						toRet.add(blrs.getListaRequisitiTempl());
					}
				}
			}
		}
		return toRet;
	}
}
