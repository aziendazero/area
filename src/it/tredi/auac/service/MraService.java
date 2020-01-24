package it.tredi.auac.service;

import it.tredi.auac.dao.BinaryAttachmentsApplDao;
import it.tredi.auac.dao.MraModelDao;
import it.tredi.auac.dao.SedeOperModelDao;
import it.tredi.auac.dao.TitolareModelDao;
import it.tredi.auac.dao.UoModelDao;
import it.tredi.auac.mra.MraCentroResponsabilita;
import it.tredi.auac.mra.MraPuntoFisico;
import it.tredi.auac.mra.MraTitolare;
import it.tredi.auac.mra.MraTripletta;
import it.tredi.auac.orm.entity.BinaryAttachmentsAppl;
import it.tredi.auac.orm.entity.MraModel;
import it.tredi.auac.orm.entity.SedeOperModel;
import it.tredi.auac.orm.entity.TitolareModel;
import it.tredi.auac.orm.entity.UdoModel;
import it.tredi.auac.orm.entity.UoModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Service
public class MraService
{
	private static final Logger log = Logger.getLogger(MraService.class);
	private static String codRegione = "050";
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private Date dataPrimoInvio = new Date();
	
	private String eroga = "Eroga";
	private String noneroga = "Non Eroga";
	
	@Autowired
	private UoModelDao uoModelDao;
	
	@Autowired
	private TitolareModelDao titolareModelDao;
	
	@Autowired
	private SedeOperModelDao sedeOperModelDao;

	@Autowired
	private BinaryAttachmentsApplDao binaryAttachmentsApplDao;

	@Autowired
	private MraModelDao mraModelDao;

	public void saveFileXmlTitolari(String pathFileTitolari, String pathFileCdr, String pathFilePuntiFisici, String pathFileTriplette) {
		Document docTitolari = createXmlTitolari();
		Document docCdr = createXmlCentriDiResponsabilita();
		Document docPuntiFisici = createXmlPuntiFisici();
		Document docTriplette = createXmlTriplette();
		try {
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			
			DOMSource sourceTitolari = new DOMSource(docTitolari);
			StreamResult resultTitolari = new StreamResult(new File(pathFileTitolari));
			transformer.transform(sourceTitolari, resultTitolari);
	
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			DOMSource sourceCdr = new DOMSource(docCdr);
			StreamResult resultCdr = new StreamResult(new File(pathFileCdr));
			transformer.transform(sourceCdr, resultCdr);

			DOMSource sourcePuntiFisici = new DOMSource(docPuntiFisici);
			StreamResult resultPuntiFisici = new StreamResult(new File(pathFilePuntiFisici));
			transformer.transform(sourcePuntiFisici, resultPuntiFisici);

			DOMSource sourceTriplette = new DOMSource(docTriplette);
			StreamResult resultTriplette = new StreamResult(new File(pathFileTriplette));
			transformer.transform(sourceTriplette, resultTriplette);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
	}

	@Transactional
	public void createOrUpdateMraModel() {
		Document docTitolari = createXmlTitolari();
		Document docCdr = createXmlCentriDiResponsabilita();
		Document docPuntiFisici = createXmlPuntiFisici();
		Document docTriplette = createXmlTriplette();
		try {
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			
			MraModel mraModel = mraModelDao.getMraModelIfExist();
			List<BinaryAttachmentsAppl> attachmentToDelete = null;
			if(mraModel == null) {
				//Primo inserimento
				mraModel = new MraModel();
				mraModel.setDataPrimoInvio(dataPrimoInvio);
			} else {
				//Modifica
				attachmentToDelete = new ArrayList<BinaryAttachmentsAppl>();
				attachmentToDelete.add(mraModel.getXmlTitolari());
				attachmentToDelete.add(mraModel.getXmlCentriResponsabilita());
				attachmentToDelete.add(mraModel.getXmlPuntiFisici());
				attachmentToDelete.add(mraModel.getXmlTriplette());
			}
			
			//Titolari
			BinaryAttachmentsAppl attachment = new BinaryAttachmentsAppl();
			attachment.setClientid(UUID.randomUUID().toString().toUpperCase());
			attachment.setNome("MRA Titolari.xml");
			attachment.setDescr("File xml dei titolari per MRA");
			attachment.setTipo("xml");
			attachment.setDisabled("N");
			DOMSource sourceTitolari = new DOMSource(docTitolari);
			ByteArrayOutputStream bosTitolari=new ByteArrayOutputStream();
			StreamResult resultTitolari = new StreamResult(bosTitolari);
			transformer.transform(sourceTitolari, resultTitolari);
			attachment.setAllegato(bosTitolari.toByteArray());
			binaryAttachmentsApplDao.save(attachment);
			mraModel.setXmlTitolari(attachment);
			
			//Centri di responsabilità
			attachment = new BinaryAttachmentsAppl();
			attachment.setClientid(UUID.randomUUID().toString().toUpperCase());
			attachment.setNome("MRA Centri di Responsabilità.xml");
			attachment.setDescr("File xml dei centri di responsabilità per MRA");
			attachment.setTipo("xml");
			attachment.setDisabled("N");
			DOMSource sourceCdr = new DOMSource(docCdr);
			ByteArrayOutputStream bosCdr=new ByteArrayOutputStream();
			StreamResult resultCdr = new StreamResult(bosCdr);
			transformer.transform(sourceCdr, resultCdr);
			attachment.setAllegato(bosCdr.toByteArray());
			binaryAttachmentsApplDao.save(attachment);
			mraModel.setXmlCentriResponsabilita(attachment);
	
			//Punti fisici
			attachment = new BinaryAttachmentsAppl();
			attachment.setClientid(UUID.randomUUID().toString().toUpperCase());
			attachment.setNome("MRA Punti Fisici.xml");
			attachment.setDescr("File xml dei punti fisici per MRA");
			attachment.setTipo("xml");
			attachment.setDisabled("N");
			DOMSource sourcePuntiFisici = new DOMSource(docPuntiFisici);
			ByteArrayOutputStream bosPuntiFisici=new ByteArrayOutputStream();
			StreamResult resultPuntiFisici = new StreamResult(bosPuntiFisici);
			transformer.transform(sourcePuntiFisici, resultPuntiFisici);
			attachment.setAllegato(bosPuntiFisici.toByteArray());
			binaryAttachmentsApplDao.save(attachment);
			mraModel.setXmlPuntiFisici(attachment);

			//Triplette
			attachment = new BinaryAttachmentsAppl();
			attachment.setClientid(UUID.randomUUID().toString().toUpperCase());
			attachment.setNome("MRA triplette.xml");
			attachment.setDescr("File xml delle triplette per MRA");
			attachment.setTipo("xml");
			attachment.setDisabled("N");
			DOMSource sourceTriplette = new DOMSource(docTriplette);
			ByteArrayOutputStream bosTriplette = new ByteArrayOutputStream();
			StreamResult resultTriplette = new StreamResult(bosTriplette);
			transformer.transform(sourceTriplette, resultTriplette);
			attachment.setAllegato(bosTriplette.toByteArray());
			binaryAttachmentsApplDao.save(attachment);
			mraModel.setXmlTriplette(attachment);
			
			mraModelDao.save(mraModel);
			
			if(attachmentToDelete != null) {
				for(BinaryAttachmentsAppl attach : attachmentToDelete ) {
					binaryAttachmentsApplDao.delete(attach);
				}
			}
			
		} catch (TransformerException e) {
			log.error("createOrUpdateMraModel", e);
		}
		
	}	
	private Document createXmlTitolari() {
		try {
			long startTime;
			long requiredTime;
			startTime = System.currentTimeMillis();
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			// root elements titolari
			Element rootElement = doc.createElement("titolari");
			doc.appendChild(rootElement);
			
			List<MraTitolare> titolariMra = getTitolariMra();
			for(MraTitolare titolare : titolariMra) {
				addTitolareToXml(doc, rootElement, titolare);
			}

			requiredTime = System.currentTimeMillis() - startTime;
			log.info("Tempo creazione Xml Titolari: " + requiredTime + " ms");
			requiredTime = System.currentTimeMillis() - startTime;
			return doc;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<MraTitolare> getTitolariMra() {
		List<MraTitolare> titolariMra = new ArrayList<MraTitolare>();
		List<TitolareModel> titolari = (List<TitolareModel>)titolareModelDao.findAll();
		for(TitolareModel titolare : titolari) {
			titolariMra.add(getMraTitolareFromTitolareModel(titolare));
		}
		return titolariMra;
	}
	
	private String getCodiceMraTitolare(TitolareModel titolare) {
		//return titolare.getIdTitolare().toString();
		return titolare.getCodUni();
	}
	
	private MraTitolare getMraTitolareFromTitolareModel(TitolareModel titolare) {
		MraTitolare mraTitolare = new MraTitolare();
		//Codice Regione
		//Campo fisso
		mraTitolare.setCodiceRegione(codRegione);
		// codice titolare
		// Id univoco titolare – Tabella TITOLARE_MODEL campo ID_TITOLARE
		mraTitolare.setCodiceTitolare(getCodiceMraTitolare(titolare));
		//Data Inizio Validità
		//Data abilitazione titolare (Tabella TITOLARE_MODEL campo DATA_ABILITAZIONE) oppure data ripresa dai flussi (prima data inserita)
		mraTitolare.setDataInizioValidita(titolare.getDataAbilitazione());
		//Denominazione
		//Ragione Sociale– Tabella TITOLARE_MODEL campo RAG_SOC
		mraTitolare.setDenominazione(titolare.getRagSoc());
		// Natura
		// Natura– Tabella TITOLARE_MODEL campo ID_NATURA_FK
		if(titolare.getNaturaTitolareTempl() != null && titolare.getNaturaTitolareTempl().getDescr() != null)
			mraTitolare.setNatura(titolare.getNaturaTitolareTempl().getDescr());
		else
			mraTitolare.setNatura("");
		// Tipologia Titolare
		// Tipo Titolare– Tabella TITOLARE_MODEL campo ID_TIPO_FK
		if(titolare.getTipoTitolareTempl() != null && titolare.getTipoTitolareTempl().getDescr() != null)
			mraTitolare.setTipologiaTitolare(titolare.getTipoTitolareTempl().getDescr());
		else
			mraTitolare.setTipologiaTitolare("");
		// Indirizzo Sede Legale
		// Sede Legale– Tabella TITOLARE_MODEL campo TOPONIMO_STRADALE; VIA_PIAZZA; CIVICO; COMUNE; PROVINCIA
		mraTitolare.setToponimoStradale(titolare.getToponimoStradale());
		mraTitolare.setViaPiazza(titolare.getViaPiazza());
		mraTitolare.setCivico(titolare.getCivico());
		if(titolare.getComune() != null) {
			if(titolare.getComune().getDescrizione() != null && !titolare.getComune().getDescrizione().isEmpty()) {
				mraTitolare.setComune(titolare.getComune().getDescrizione());
			}
			if(titolare.getComune().getDescrizioneProvincia() != null && !titolare.getComune().getDescrizioneProvincia().isEmpty()) {
				mraTitolare.setProvincia(titolare.getComune().getDescrizioneProvincia());
			}
		}
		// Partita IVA
		// Partita IVA– Tabella TITOLARE_MODEL campo CFISC
		mraTitolare.setPartitaIva(titolare.getPiva());
		// Codice Fiscale
		// Codice Fiscale– Tabella TITOLARE_MODEL campo PIVA
		mraTitolare.setCodiceFiscale(titolare.getCfisc());
		// Prefisso+ numero di telefono
		// Telefono (distinguere in A.re.A. telefono e prefisso che al momento sono salvati nello stesso campo) – Tabella TITOLARE_MODEL campo TELEFONO
		mraTitolare.setTelefono(titolare.getTelefono());
		// Prefisso+ fax
		// Da aggiungere in dati Titolare – al momento non gestito
		mraTitolare.setFax("");
		// Email
		// Email – Tabella TITOLARE_MODEL campo EMAIL
		mraTitolare.setEmail(titolare.getEmail());
		// PEC
		// Pec – Tabella TITOLARE_MODEL campo PEC
		mraTitolare.setPec(titolare.getPec());
		// Sito Web
		// Sito Web – Tabella TITOLARE_MODEL campo URL
		mraTitolare.setUrl(titolare.getUrl());
		return mraTitolare;
	}
	

	private void addTitolareToXml(Document doc, Element rootTitolari, MraTitolare titolare) {
		Element rootTitolare = doc.createElement("titolare");
		rootTitolari.appendChild(rootTitolare);
		Element elem;

		// shorten way
		// rootElement.setAttribute("codiceRegione", codRegione);

		// codice regione
		Attr attr = doc.createAttribute("codiceRegione");
		attr.setValue(codRegione);
		rootTitolare.setAttributeNode(attr);
		
		// codice titolare
		attr = doc.createAttribute("codiceTitolare");
		attr.setValue(titolare.getCodiceTitolare());
		rootTitolare.setAttributeNode(attr);
		
		//Data Inizio Validità
		//Data abilitazione titolare (Tabella TITOLARE_MODEL campo DATA_ABILITAZIONE) oppure data ripresa dai flussi (prima data inserita)
		attr = doc.createAttribute("dataInizioValidita");
		attr.setValue(titolare.getDataInizioValidita() == null ? "" : sdf.format(titolare.getDataInizioValidita()));
		rootTitolare.setAttributeNode(attr);
		
		// Denominazione
		//Ragione Sociale– Tabella TITOLARE_MODEL campo RAG_SOC
		elem = doc.createElement("denominazione");
		elem.appendChild(doc.createTextNode(titolare.getDenominazione()));
		rootTitolare.appendChild(elem);

		// Natura
		// Natura– Tabella TITOLARE_MODEL campo ID_NATURA_FK
		elem = doc.createElement("natura");
		if(titolare.getNatura() != null)
			elem.appendChild(doc.createTextNode(titolare.getNatura()));
		else
			elem.appendChild(doc.createTextNode(""));
		rootTitolare.appendChild(elem);
		
		// Tipologia Titolare
		// Tipo Titolare– Tabella TITOLARE_MODEL campo ID_TIPO_FK
		elem = doc.createElement("tipologiaTitolare");
		if(titolare.getTipologiaTitolare() != null)
			elem.appendChild(doc.createTextNode(titolare.getTipologiaTitolare()));
		else
			elem.appendChild(doc.createTextNode(""));
		rootTitolare.appendChild(elem);
		
		// Indirizzo Sede Legale
		// Sede Legale– Tabella TITOLARE_MODEL campo TOPONIMO_STRADALE; VIA_PIAZZA; CIVICO; COMUNE; PROVINCIA
		elem = doc.createElement("indirizzoSedeLegale");
		elem.appendChild(doc.createTextNode(titolare.getIndirizzoSedeLegale()));
		rootTitolare.appendChild(elem);
		
		// Partita IVA
		// Partita IVA– Tabella TITOLARE_MODEL campo CFISC
		elem = doc.createElement("partitaIva");
		elem.appendChild(doc.createTextNode(titolare.getPartitaIva() == null ? "" : titolare.getPartitaIva()));
		rootTitolare.appendChild(elem);
		
		// Codice Fiscale
		// Codice Fiscale– Tabella TITOLARE_MODEL campo PIVA
		elem = doc.createElement("codiceFiscale");
		elem.appendChild(doc.createTextNode(titolare.getCodiceFiscale() == null ? "" : titolare.getCodiceFiscale()));
		rootTitolare.appendChild(elem);
		
		// Prefisso+ numero di telefono
		// Telefono (distinguere in A.re.A. telefono e prefisso che al momento sono salvati nello stesso campo) – Tabella TITOLARE_MODEL campo TELEFONO
		elem = doc.createElement("numeroTelefono");
		elem.appendChild(doc.createTextNode(titolare.getTelefono() == null ? "" : titolare.getTelefono()));
		rootTitolare.appendChild(elem);
		
		// Prefisso+ fax
		// Da aggiungere in dati Titolare – al momento non gestito
		elem = doc.createElement("numeroFax");
		elem.appendChild(doc.createTextNode(titolare.getFax() == null ? "" : titolare.getFax()));
		rootTitolare.appendChild(elem);
		
		// Email
		// Email – Tabella TITOLARE_MODEL campo EMAIL
		elem = doc.createElement("email");
		elem.appendChild(doc.createTextNode(titolare.getEmail() == null ? "" : titolare.getEmail()));
		rootTitolare.appendChild(elem);
		
		// PEC
		// Pec – Tabella TITOLARE_MODEL campo PEC
		elem = doc.createElement("pec");
		elem.appendChild(doc.createTextNode(titolare.getPec() == null ? "" : titolare.getPec()));
		rootTitolare.appendChild(elem);
		
		// Sito Web
		// Sito Web – Tabella TITOLARE_MODEL campo URL
		elem = doc.createElement("sitoWeb");
		elem.appendChild(doc.createTextNode(titolare.getUrl() == null ? "" : titolare.getUrl()));
		rootTitolare.appendChild(elem);
	}
	
	private Document createXmlCentriDiResponsabilita() {
		try {
			long startTime;
			long requiredTime;
			startTime = System.currentTimeMillis();
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			// root elements centriresponsabilita
			Element rootElement = doc.createElement("centriresponsabilita");
			doc.appendChild(rootElement);
			
			List<MraCentroResponsabilita> centriResponsabilita = getMraCentriResponsabilita();
			for(MraCentroResponsabilita centoResponsabilita : centriResponsabilita) {
				addCentroResponsabilitaToXml(doc, rootElement, centoResponsabilita);
			}
			requiredTime = System.currentTimeMillis() - startTime;
			log.info("Tempo creazione Xml Centri di Responsabilità: " + requiredTime + " ms");
			requiredTime = System.currentTimeMillis() - startTime;
			return doc;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<MraCentroResponsabilita> getMraCentriResponsabilita() {
		List<MraCentroResponsabilita> centriResponsabilita = new ArrayList<MraCentroResponsabilita>();
		List<UoModel> uos = (List<UoModel>)uoModelDao.findAll();
		for(UoModel uo : uos) {
			centriResponsabilita.add(getMraCentroResponsabilitaFromUoModel(uo));
		}
		return centriResponsabilita;
	}
	
	private String getCodiceMraCentroResponsabilita(UoModel uo) {
		return uo.getId().getProvenienza() + "-" + uo.getId().getId();
	}
	
	private MraCentroResponsabilita getMraCentroResponsabilitaFromUoModel(UoModel uo) {
		MraCentroResponsabilita mraCentroResponsabilita = new MraCentroResponsabilita();

		// codice regione
		mraCentroResponsabilita.setCodiceRegione(codRegione);
		// codice identificativo
		mraCentroResponsabilita.setCodiceCentroResponsabilita(getCodiceMraCentroResponsabilita(uo));
		//Data inizio validità
		//Data del primo invio del flusso
		mraCentroResponsabilita.setDataInizioValidita(dataPrimoInvio);
		//Denominazione
		//Denominazione– Tabella UO_MODEL campo DENOMINAZIONE
		mraCentroResponsabilita.setDenominazione(uo.getDenominazione());
		//Natura
		//	1 Eroga direttamente prestazioni sanitarie
		//	2 Non Eroga direttamente prestazioni sanitarie
		//Se il titolare ha UDO eroga; se le UDO sono di supporto (in base al tipo UDO) non eroga.
//		if(uo.getUdoModels().size() > 0)
//			mraCentroResponsabilita.setNatura(eroga);
//		else {
//			boolean isEroga = false;
//			for(UoModel uom : uo.getTitolareModel().getUoModels()) {
//				if(uom.getUdoModels().size() > 0){
//					isEroga = true;
//					break;
//				}
//			}
//			if(isEroga)
//				mraCentroResponsabilita.setNatura(eroga);
//			else
//				mraCentroResponsabilita.setNatura(noneroga);
//		}
		mraCentroResponsabilita.setNatura("");
		//Tipologia Centro di Responsabilità
		//	1 Struttura Complessa (o equivalente per il privato)
		//	2 Struttura Semplice (o equivalente per il privato)
		//	3 Coincide con il Titolare			
		//Tipo Nodo – Tabella UO_ MODEL campo TIPO_NODO
		//Per i privati è la UO_MODEL gestita da A.re.A. – lo facciamo risalire dal tipo titolare o dal tipo UDO 
		if(uo.getId().getProvenienza().equals("UO_MODEL"))
			mraCentroResponsabilita.setTipologiaCentroResponsabilita("");
		else
			mraCentroResponsabilita.setTipologiaCentroResponsabilita(uo.getTipoNodo() == null ? "" : uo.getTipoNodo());

		//Data apertura/Data chiusura
		//Per pubblici: campo ripreso da atti aziendali.
		//Per privati: da decidere (data in cui la UO viene disabilitata, senza cancellazione fisica oppure data in cui la UO non è più collegata a UDO)
		
		return mraCentroResponsabilita;
	}	
	
	private void addCentroResponsabilitaToXml(Document doc, Element rootUos, MraCentroResponsabilita centroResponsabilita) {
		Element rootUo = doc.createElement("centrodiresponsabilita");
		rootUos.appendChild(rootUo);
		Element elem;
		// shorten way
		// rootElement.setAttribute("codiceRegione", codRegione);

		// codice regione
		Attr attr = doc.createAttribute("codiceRegione");
		attr.setValue(codRegione);
		rootUo.setAttributeNode(attr);
		
		// codice identificativo
		attr = doc.createAttribute("codiceTitolare");
		attr.setValue(centroResponsabilita.getCodiceCentroResponsabilita() == null ? "" : centroResponsabilita.getCodiceCentroResponsabilita());
		rootUo.setAttributeNode(attr);
		
		//Data inizio validità
		//Data del primo invio del flusso
		attr = doc.createAttribute("dataInizioValidita");
		if(centroResponsabilita.getDataInizioValidita() == null)
			attr.setValue("");
		else
			attr.setValue(sdf.format(centroResponsabilita.getDataInizioValidita()));
		rootUo.setAttributeNode(attr);

		//Denominazione
		//Denominazione– Tabella UO_MODEL campo DENOMINAZIONE
		elem = doc.createElement("denominazione");
		elem.appendChild(doc.createTextNode(centroResponsabilita.getDenominazione() == null ? "" : centroResponsabilita.getDenominazione()));
		rootUo.appendChild(elem);
		
		//Natura
		//	1 Eroga direttamente prestazioni sanitarie
		//	2 Non Eroga direttamente prestazioni sanitarie
		//Se il titolare ha UDO eroga; se le UDO sono di supporto (in base al tipo UDO) non eroga.
		elem = doc.createElement("natura");
		elem.appendChild(doc.createTextNode(centroResponsabilita.getNatura() == null ? "" : centroResponsabilita.getNatura()));
		rootUo.appendChild(elem);

		//Tipologia Centro di Responsabilità
		//	1 Struttura Complessa (o equivalente per il privato)
		//	2 Struttura Semplice (o equivalente per il privato)
		//	3 Coincide con il Titolare			
		//Tipo Nodo – Tabella UO_ MODEL campo TIPO_NODO
		//Per i privati è la UO_MODEL gestita da A.re.A. – lo facciamo risalire dal tipo titolare o dal tipo UDO 
		elem = doc.createElement("tipocentroresponsabilita");
		elem.appendChild(doc.createTextNode(centroResponsabilita.getTipologiaCentroResponsabilita() == null ? "" : centroResponsabilita.getTipologiaCentroResponsabilita()));
		rootUo.appendChild(elem);

		//Data apertura/Data chiusura
		//Per pubblici: campo ripreso da atti aziendali.
		//Per privati: da decidere (data in cui la UO viene disabilitata, senza cancellazione fisica oppure data in cui la UO non è più collegata a UDO)
		if(centroResponsabilita.getDataApertura() != null) {
			elem = doc.createElement("dataApertura");
			elem.appendChild(doc.createTextNode(sdf.format(centroResponsabilita.getDataApertura())));
			rootUo.appendChild(elem);
		}
		if(centroResponsabilita.getDataChiusura() != null) {
			elem = doc.createElement("dataChiusura");
			elem.appendChild(doc.createTextNode(sdf.format(centroResponsabilita.getDataChiusura())));
			rootUo.appendChild(elem);
		}

	}	

	private Document createXmlPuntiFisici() {
		try {
			long startTime;
			long requiredTime;
			startTime = System.currentTimeMillis();
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			// root elements centriresponsabilita
			Element rootElement = doc.createElement("puntifisici");
			doc.appendChild(rootElement);
			
			List<MraPuntoFisico> puntiFisici = getMraPuntiFisici();
			for(MraPuntoFisico centoResponsabilita : puntiFisici) {
				addPuntoFisicoToXml(doc, rootElement, centoResponsabilita);
//				if(index++ >= 5)
//					break;
			}
			requiredTime = System.currentTimeMillis() - startTime;
			log.info("Tempo creazione Xml Punti Fisici: " + requiredTime + " ms");
			requiredTime = System.currentTimeMillis() - startTime;
			return doc;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<MraPuntoFisico> getMraPuntiFisici() {
		List<MraPuntoFisico> puntiFisici = new ArrayList<MraPuntoFisico>();
		List<SedeOperModel> sedi = (List<SedeOperModel>)sedeOperModelDao.findAll();
		for(SedeOperModel sede : sedi) {
			puntiFisici.add(getMraPuntoFisicoFromSedeOperativa(sede));
		}
		return puntiFisici;
	}
	
	private String getCodiceMraPuntoFisico(SedeOperModel sedeOperModel) {
		return sedeOperModel.getIdSede().toString();
	}
	
	private MraPuntoFisico getMraPuntoFisicoFromSedeOperativa(SedeOperModel sedeOperModel) {
		MraPuntoFisico mraPuntoFisico = new MraPuntoFisico();

		// codice regione
		mraPuntoFisico.setCodiceRegione(codRegione);
		// codice identificativo
		mraPuntoFisico.setCodicePuntoFisico(getCodiceMraPuntoFisico(sedeOperModel));
		//Data inizio validità
		//Data del primo invio del flusso
		mraPuntoFisico.setDataInizioValidita(dataPrimoInvio);
		//Codice azienda
		//Codice Ulss da tabella COMUNI ???? non esiste
		mraPuntoFisico.setCodiceAzienda("");

		//Denominazione
		//Denominazione– Tabella UO_MODEL campo DENOMINAZIONE
		mraPuntoFisico.setDenominazione(sedeOperModel.getDenominazione());
		//Natura
		//mraCentroResponsabilita.setNatura(uo.getTipoPuntoFisico());
		//Tipo punto fisico
		mraPuntoFisico.setTipologiaPuntoFisico(sedeOperModel.getTipoPuntoFisico());

		// Indirizzo Sede Legale
		// Sede Legale– Tabella TITOLARE_MODEL campo TOPONIMO_STRADALE;
		// VIA_PIAZZA; CIVICO; COMUNE; PROVINCIA
		mraPuntoFisico.setToponimoStradale(sedeOperModel.getToponimoStradale());
		mraPuntoFisico.setViaPiazza(sedeOperModel.getViaPiazza());
		mraPuntoFisico.setCivico(sedeOperModel.getCivico());
		mraPuntoFisico.setComune(sedeOperModel.getComune());
		mraPuntoFisico.setProvincia(sedeOperModel.getProvincia());
		mraPuntoFisico.setIstat(sedeOperModel.getIstat());
		mraPuntoFisico.setCap(sedeOperModel.getCap());
		
		mraPuntoFisico.setLatitudine(null);
		mraPuntoFisico.setLongitudine(null);

		// Prefisso+ numero di telefono
		// Telefono (distinguere in A.re.A. telefono e prefisso che al momento sono salvati nello stesso campo) – Tabella TITOLARE_MODEL campo TELEFONO
		mraPuntoFisico.setTelefono(sedeOperModel.getStrutturaModel().getTitolareModel().getTelefono());
		// Prefisso+ fax
		// Da aggiungere in dati Titolare – al momento non gestito
		mraPuntoFisico.setFax("");
		// Email
		// Email – Tabella TITOLARE_MODEL campo EMAIL
		mraPuntoFisico.setEmail(sedeOperModel.getStrutturaModel().getTitolareModel().getEmail());
		// Sito Web
		// Sito Web – Tabella TITOLARE_MODEL campo URL
		mraPuntoFisico.setUrl(sedeOperModel.getStrutturaModel().getTitolareModel().getUrl());

		//Data apertura/Data chiusura
		mraPuntoFisico.setDataApertura(dataPrimoInvio);
		mraPuntoFisico.setDataChiusura(null);
		
		return mraPuntoFisico;
	}	
	
	private void addPuntoFisicoToXml(Document doc, Element rootUos, MraPuntoFisico puntoFisico) {
		Element rootUo = doc.createElement("puntofisico");
		rootUos.appendChild(rootUo);
		Element elem;
		// shorten way
		// rootElement.setAttribute("codiceRegione", codRegione);

		// codice regione
		Attr attr = doc.createAttribute("codiceRegione");
		attr.setValue(codRegione);
		rootUo.setAttributeNode(attr);
		
		// codice identificativo
		attr = doc.createAttribute("codicePuntoFisico");
		attr.setValue(puntoFisico.getCodicePuntoFisico());
		rootUo.setAttributeNode(attr);
		
		//Data inizio validità
		//Data del primo invio del flusso
		attr = doc.createAttribute("dataInizioValidita");
		attr.setValue(sdf.format(puntoFisico.getDataInizioValidita()));
		rootUo.setAttributeNode(attr);

		//Codice azienda
		//Codice Ulss da tabella COMUNI
		attr = doc.createAttribute("codiceAzienda");
		attr.setValue(puntoFisico.getCodiceAzienda() == null ? "" : puntoFisico.getCodiceAzienda());
		rootUo.setAttributeNode(attr);

		//Denominazione
		//Denominazione– Tabella UO_MODEL campo DENOMINAZIONE
		elem = doc.createElement("denominazione");
		elem.appendChild(doc.createTextNode(puntoFisico.getDenominazione()));
		rootUo.appendChild(elem);
		
		//Natura
//		elem = doc.createElement("natura");
//		elem.appendChild(doc.createTextNode(puntoFisico.getNatura()));
//		rootUo.appendChild(elem);

		//Tipologia Punto Fisico
		//	1 Struttura Complessa (o equivalente per il privato)
		//	2 Struttura Semplice (o equivalente per il privato)
		//	3 Coincide con il Titolare			
		//Tipo Nodo – Tabella UO_ MODEL campo TIPO_NODO
		//Per i privati è la UO_MODEL gestita da A.re.A. – lo facciamo risalire dal tipo titolare o dal tipo UDO 
		elem = doc.createElement("tipopuntofisico");
		elem.appendChild(doc.createTextNode(puntoFisico.getTipologiaPuntoFisico() == null ? "" : puntoFisico.getTipologiaPuntoFisico()));
		rootUo.appendChild(elem);
		
		//Indirizzo (Via, Nome, Cap, Comune, Provincia)
		//Indirizzo – Tabella SEDE_OPER_MODEL Campo TOPONIMO_STRADALE, VIA_PIAZZA, CIVICO, ISTAT, CAP, COMUNE, PROVINCIA
		elem = doc.createElement("indirizzo");
		elem.appendChild(doc.createTextNode(puntoFisico.getIndirizzo()));
		rootUo.appendChild(elem);

		//Latitudine/Longitudine
		//Calcolata sulla base del Comune e del Codice Istat
		elem = doc.createElement("latitudine");
		elem.appendChild(doc.createTextNode(puntoFisico.getLatitudine() == null ? "" : puntoFisico.getLatitudine().toString()));
		rootUo.appendChild(elem);
		elem = doc.createElement("longitudine");
		elem.appendChild(doc.createTextNode(puntoFisico.getLongitudine() == null ? "" : puntoFisico.getLatitudine().toString()));
		rootUo.appendChild(elem);
		
		// Prefisso+ numero di telefono
		// Telefono (distinguere in A.re.A. telefono e prefisso che al momento sono salvati nello stesso campo) – Tabella TITOLARE_MODEL campo TELEFONO
		elem = doc.createElement("numeroTelefono");
		elem.appendChild(doc.createTextNode(puntoFisico.getTelefono() == null ? "" : puntoFisico.getTelefono()));
		rootUo.appendChild(elem);
		
		// Prefisso+ fax
		// Da aggiungere in dati Titolare – al momento non gestito
		elem = doc.createElement("numeroFax");
		elem.appendChild(doc.createTextNode(puntoFisico.getFax() == null ? "" : puntoFisico.getFax()));
		rootUo.appendChild(elem);
		
		// Email
		// Email – Tabella TITOLARE_MODEL campo EMAIL
		elem = doc.createElement("email");
		elem.appendChild(doc.createTextNode(puntoFisico.getEmail() == null ? "" : puntoFisico.getEmail()));
		rootUo.appendChild(elem);
		
		// Sito Web
		// Sito Web – Tabella TITOLARE_MODEL campo URL
		elem = doc.createElement("sitoWeb");
		elem.appendChild(doc.createTextNode(puntoFisico.getUrl() == null ? "" : puntoFisico.getUrl()));
		rootUo.appendChild(elem);
		
		//Data apertura/Data chiusura
		//Per pubblici: campo ripreso da atti aziendali.
		//Per privati: da decidere (data in cui la UO viene disabilitata, senza cancellazione fisica oppure data in cui la UO non è più collegata a UDO)
		if(puntoFisico.getDataApertura() != null) {
			elem = doc.createElement("dataApertura");
			elem.appendChild(doc.createTextNode(sdf.format(puntoFisico.getDataApertura())));
			rootUo.appendChild(elem);
		}
		if(puntoFisico.getDataChiusura() != null) {
			elem = doc.createElement("dataChiusura");
			elem.appendChild(doc.createTextNode(sdf.format(puntoFisico.getDataChiusura())));
			rootUo.appendChild(elem);
		}

	}	
	
	private Document createXmlTriplette() {
		try {
			long startTime;
			long requiredTime;
			startTime = System.currentTimeMillis();
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			// root elements centriresponsabilita
			Element rootElement = doc.createElement("triplette");
			doc.appendChild(rootElement);
			
			List<MraTripletta> triplette = getMraTriplette();
			for(MraTripletta centoResponsabilita : triplette) {
				addTriplettaToXml(doc, rootElement, centoResponsabilita);
//				if(index++ >= 5)
//					break;
			}
			requiredTime = System.currentTimeMillis() - startTime;
			log.info("Tempo creazione Xml triplette: " + requiredTime + " ms");
			requiredTime = System.currentTimeMillis() - startTime;
			return doc;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<MraTripletta> getMraTriplette() {
		List<MraTripletta> triplette = new ArrayList<MraTripletta>();
		List<UoModel> uos = (List<UoModel>)uoModelDao.findAll();
		for(UoModel uo : uos) {
			triplette.addAll(getMraTriplettaFromUoModel(uo));
		}
		return triplette;
	}
	
	private List<MraTripletta> getMraTriplettaFromUoModel(UoModel uoModel) {
		List<MraTripletta> triplette = new ArrayList<MraTripletta>();
		
		for(UdoModel udo :  uoModel.getUdoModels()) {
			MraTripletta mraTripletta = new MraTripletta();
	
			// codice regione
			mraTripletta.setCodiceRegione(codRegione);
			mraTripletta.setCodiceCentroResponsabilita(getCodiceMraCentroResponsabilita(uoModel));
			mraTripletta.setCodiceTitolare(getCodiceMraTitolare(uoModel.getTitolareModel()));
			mraTripletta.setCodicePuntoFisico(getCodiceMraPuntoFisico(udo.getSedeOperModel()));

			//Data inizio validità
			//Data del primo invio del flusso
			mraTripletta.setDataInizioValidita(dataPrimoInvio);

			mraTripletta.setDescrizione("");
			
			//Data apertura/Data chiusura
			mraTripletta.setDataApertura(dataPrimoInvio);
			mraTripletta.setDataChiusura(null);

			if(uoModel.getId().getProvenienza().equals("UO_MODEL")) {
				mraTripletta.setTipoPunto(udo.getTipoUdoTempl().getTipoUdo22Templ().getCodiceUdo());
			} else {
				mraTripletta.setTipoPunto(uoModel.getTipoNodo() + "-" + udo.getTipoUdoTempl().getTipoUdo22Templ().getCodiceUdo());
			}
			if(uoModel.getTitolareModel().getTipoTitolareTempl() != null && uoModel.getTitolareModel().getTipoTitolareTempl().getDescr() != null)
				mraTripletta.setTipologia(uoModel.getTitolareModel().getTipoTitolareTempl().getDescr());
			else
				mraTripletta.setTipologia("");

			mraTripletta.setStato("");
			mraTripletta.setNote("");
			
			//mraPuntoFisico.setTripletteCollegate();
			//mraPuntoFisico.setTipologiaLegame("");
			
			triplette.add(mraTripletta);
		}
		
		return triplette;
	}	
	
	private void addTriplettaToXml(Document doc, Element rootUos, MraTripletta puntoFisico) {
		Element rootUo = doc.createElement("tripletta");
		rootUos.appendChild(rootUo);
		Element elem;
		// shorten way
		// rootElement.setAttribute("codiceRegione", codRegione);

		// codice regione
		Attr attr = doc.createAttribute("codiceRegione");
		attr.setValue(codRegione);
		rootUo.setAttributeNode(attr);
		
		attr = doc.createAttribute("codiceTitolare");
		attr.setValue(puntoFisico.getCodiceTitolare());
		rootUo.setAttributeNode(attr);
		attr = doc.createAttribute("codicePCentroResponsabilita");
		attr.setValue(puntoFisico.getCodiceCentroResponsabilita());
		rootUo.setAttributeNode(attr);
		attr = doc.createAttribute("codicePuntoFisico");
		attr.setValue(puntoFisico.getCodicePuntoFisico());
		rootUo.setAttributeNode(attr);
		
		//Data inizio validità
		//Data del primo invio del flusso
		attr = doc.createAttribute("dataInizioValidita");
		attr.setValue(sdf.format(puntoFisico.getDataInizioValidita()));
		rootUo.setAttributeNode(attr);

		//Descrizione
		elem = doc.createElement("descrizione");
		elem.appendChild(doc.createTextNode(puntoFisico.getDescrizione()));
		rootUo.appendChild(elem);

		elem = doc.createElement("tipoPunto");
		elem.appendChild(doc.createTextNode(puntoFisico.getTipoPunto() == null ? "" : puntoFisico.getTipoPunto()));
		rootUo.appendChild(elem);
		
		elem = doc.createElement("tipologia");
		elem.appendChild(doc.createTextNode(puntoFisico.getTipologia() == null ? "" : puntoFisico.getTipologia()));
		rootUo.appendChild(elem);

		elem = doc.createElement("stato");
		elem.appendChild(doc.createTextNode(puntoFisico.getStato() == null ? "" : puntoFisico.getStato()));
		rootUo.appendChild(elem);

		elem = doc.createElement("note");
		elem.appendChild(doc.createTextNode(puntoFisico.getNote() == null ? "" : puntoFisico.getNote()));
		rootUo.appendChild(elem);

		//mraPuntoFisico.setTripletteCollegate();
		//mraPuntoFisico.setTipologiaLegame("");

		//Data apertura/Data chiusura
		//Per pubblici: campo ripreso da atti aziendali.
		//Per privati: da decidere (data in cui la UO viene disabilitata, senza cancellazione fisica oppure data in cui la UO non è più collegata a UDO)
		if(puntoFisico.getDataApertura() != null) {
			elem = doc.createElement("dataApertura");
			elem.appendChild(doc.createTextNode(sdf.format(puntoFisico.getDataApertura())));
			rootUo.appendChild(elem);
		}
		if(puntoFisico.getDataChiusura() != null) {
			elem = doc.createElement("dataChiusura");
			elem.appendChild(doc.createTextNode(sdf.format(puntoFisico.getDataChiusura())));
			rootUo.appendChild(elem);
		}

	}	


}