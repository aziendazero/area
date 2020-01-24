package it.tredi.auac.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import it.highwaytech.apps.generic.XMLDocumento;
import it.highwaytech.apps.generic.utils.GenericUtils;
import it.tredi.auac.DomandaOrderTypeEnum;
import it.tredi.auac.ExtraWayService;
import it.tredi.auac.bean.PostIt;
import it.tredi.auac.bean.WorkflowXWProcessInstance;
import it.tredi.auac.orm.entity.DomandaInst;
import it.tredi.auac.orm.entity.TitolareModel;

public class FolderDao {
	private static final Logger log = Logger.getLogger(FolderDao.class);	
	
	public final static String TITOLARE_FOLDER_TYPE = "titolare";
	public final static String ATTI_TITOLARE_FOLDER_TYPE = "atti_titolare";
	public final static String DOMANDA_TITOLARE_FOLDER_TYPE = "domanda_titolare";
	public final static String EVIDENZE_DOMANDA_TITOLARE_FOLDER_TYPE = "evidenze_domanda_titolare";
	
	
	public static XMLDocumento insertTitolareFolder(ExtraWayService xwService, TitolareModel titolareModel) throws Exception {
		XMLDocumento document = new XMLDocumento("<fascicolo/>");
		document.insertXPath(".oggetto", titolareModel.getRagSoc());
		document.insertXPath(".classif.@cod", "00/00");
		document.insertXPath(".rif_interni.rif.@diritto", "RPA");
		document.insertXPath(".rif_interni.rif.@nome_persona", xwService.getNomePersona());
		document.insertXPath(".rif_interni.rif.@nome_uff", xwService.getNomeUfficio());
		document.insertXPath(".extra.titolare.codice_fiscale", titolareModel.getCfisc());
		document.insertXPath(".extra.titolare.partita_iva", titolareModel.getPiva());
		document.insertXPath(".extra.titolare.clientid", titolareModel.getClientid());
		document.insertXPath(".extra.@folder_type", FolderDao.TITOLARE_FOLDER_TYPE);
		
		String xmlFolder = document.getStringDocument();
		XMLDocumento newDocument = xwService.newFolder(xmlFolder);
		return newDocument;
	}
	
	
	//Synchronize Fascicolo in Extraway With Oracle Database
	public static String aggiornaFascicolo(ExtraWayService xwService, TitolareModel titolare){
		int selSize = 0;
		XMLDocumento document = null;
		boolean found = false;
		//First search
		String queryDaEseguire = "[/fascicolo/extra/titolare/clientid]=\"" + titolare.getClientid() + "\" AND [/fascicolo/extra/@folder_type/]=\"" + FolderDao.TITOLARE_FOLDER_TYPE +"\"";
		
		try{
			document = xwService.executeQuery(queryDaEseguire, null, null, false);
			if(document != null)
				selSize = Integer.parseInt(document.getAttributeValue("//Selection[@active='true']/@size"));
			if(selSize == 1){
				found  = true;
				document = xwService.loadFirstDocument(false, false);
			} else if ( selSize > 1){
				String msg = "Errore! Sono stati trovati piu' records per il titolare : " + titolare.getRagSoc() + ", ClientID: " + titolare.getClientid();
				log.warn("Aggiornamento Ragione Sociale Titolare. " + msg);
				return msg;
			}
		
			//second search
			queryDaEseguire = "[/fascicolo/extra/titolare/codice_fiscale]=\"" + titolare.getCfisc() + "\" AND [/fascicolo/extra/titolare/partita_iva]=\"" + titolare.getPiva() +
						"\" AND [/fascicolo/extra/@folder_type/]=\"" + FolderDao.TITOLARE_FOLDER_TYPE +"\"";
			if(!found){
				document = xwService.executeQuery(queryDaEseguire, null, null, false);
				if(document != null)
					selSize = Integer.parseInt(document.getAttributeValue("//Selection[@active='true']/@size"));
				if(selSize == 1){
					found  = true;
					document = xwService.loadFirstDocument(false, false);
				} else if ( selSize > 1){
					String msg = "Errore! Sono stati trovati piu' records per il titolare : " + titolare.getRagSoc() + ", Cfisc + Piva : "  + titolare.getCfisc()
					+ " + " + titolare.getPiva();
					log.warn("Aggiornamento Ragione Sociale Titolare. " + msg);
					return msg;
				} 
			}
		
			//Third search
			queryDaEseguire = "[/fascicolo/extra/titolare/codice_fiscale]=\"" + titolare.getCfisc() + "\" AND [/fascicolo/extra/@folder_type/]=\"" + FolderDao.TITOLARE_FOLDER_TYPE +"\"";
			if(!found){
				document = xwService.executeQuery(queryDaEseguire, null, null, false);
				if(document != null)
					selSize = Integer.parseInt(document.getAttributeValue("//Selection[@active='true']/@size"));
				if(selSize == 1){
					found  = true;
					document = xwService.loadFirstDocument(false, false);
				} else if ( selSize > 1){
					String msg = "Errore! Sono stati trovati piu' records per il titolare : " + titolare.getRagSoc() + ", Cfisc : "  + titolare.getCfisc();
					log.warn("Aggiornamento Ragione Sociale Titolare. " + msg);
					return msg;
				}
			}
		
			//fourth search
			queryDaEseguire = "[/fascicolo/extra/titolare/partita_iva]=\"" + titolare.getPiva() + "\" AND [/fascicolo/extra/@folder_type/]=\"" + FolderDao.TITOLARE_FOLDER_TYPE +"\"";
			if(!found){
				document = xwService.executeQuery(queryDaEseguire, null, null, false);
				if(document != null)
					selSize = Integer.parseInt(document.getAttributeValue("//Selection[@active='true']/@size"));
				if(selSize == 1){
					found  = true;
					document = xwService.loadFirstDocument(false, false);
				} else if ( selSize > 1){
					String msg = "Errore! Sono stati trovati piu' records per il titolare : " + titolare.getRagSoc() + ", Piva : "  + titolare.getPiva();
					log.warn("Aggiornamento Ragione Sociale Titolare. " + msg);
					return msg;
				} 
			}
			
			//Return error if zero records were found
			if(!found) {
				String msg = "Nessun record trovato per il titolare con Ragione Sociale: " + titolare.getRagSoc() + "; clientid: " + titolare.getClientid();
				log.warn("Aggiornamento Ragione Sociale Titolare. " + msg);
				return msg;
			}
		
			String numeroFascicolo = document.getAttributeValue("//fascicolo/@numero", "");
			String idIUnit = document.getAttributeValue("/Response/Document/@idIUnit");
			document = ExtraWayService.extractDocumentFromEnvironment(document);
			String ragSoc = document.getElementText("/fascicolo/oggetto");
			String clientId = document.getElementText("/fascicolo/extra/titolare/clientid");
			if(!ragSoc.equals(titolare.getRagSoc()) || clientId == null){
				document.insertXPath(".extra.titolare.clientid", titolare.getClientid());
				document.insertXPath(".oggetto", titolare.getRagSoc());
				xwService.saveDocument(document, true, Integer.parseInt(idIUnit));
					
				queryDaEseguire = "[/fascicolo/@numero/]=\"" + numeroFascicolo + ".*\" AND [/fascicolo/extra/@folder_type/]=\"" + FolderDao.DOMANDA_TITOLARE_FOLDER_TYPE +"\"";
					
				document = xwService.executeQuery(queryDaEseguire, null, null, false);
				selSize = Integer.parseInt(document.getAttributeValue("//Selection[@active='true']/@size"));
					
				if(selSize != 0){
					document = xwService.loadFirstDocument(false, false);
						
					for(int i = 0; i < selSize; i++){
						if ( i > 0)
							document = xwService.loadNextDocument(false, false);
						numeroFascicolo = document.getAttributeValue("//fascicolo/@numero", "");
						idIUnit = document.getAttributeValue("/Response/Document/@idIUnit");
						document = ExtraWayService.extractDocumentFromEnvironment(document);
						document.insertXPath(".extra.titolare.clientid", titolare.getClientid());
						document.insertXPath(".extra.titolare.ragione_sociale", titolare.getRagSoc());
	
						xwService.saveDocument(document, true, Integer.parseInt(idIUnit));
					}		
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	return "Successo! Operazione completata con successo";
}
	
	public static XMLDocumento findTitolareFolder(ExtraWayService xwService, TitolareModel titolareModel) throws Exception {
		String queryDaEseguire = "([/fascicolo/extra/titolare/clientid/]=" + titolareModel.getClientid() + ")";

		XMLDocumento document = xwService.executeQuery(queryDaEseguire, null, null, false);
		int selSize = Integer.parseInt(document.getAttributeValue("//Selection[@active='true']/@size"));
        if (selSize != 0) { 
        	 document =  xwService.loadFirstDocument(false, false);
        	 return document;
        }
		
        //cerco con Partita IVA o Codice Fiscale
		if (titolareModel.getPiva() != null)
			queryDaEseguire = "([/fascicolo/extra/titolare/partita_iva/]=" + titolareModel.getPiva() + ")";
		else if (titolareModel.getCfisc() != null)
			queryDaEseguire = "([/fascicolo/extra/titolare/codice_fiscale/]=" + titolareModel.getCfisc() + ")";

		
		document = xwService.executeQuery(queryDaEseguire, null, null, false);
		selSize = Integer.parseInt(document.getAttributeValue("//Selection[@active='true']/@size"));
        if (selSize == 0) { 
        	return null;
        }
        else{
        	 document =  xwService.loadFirstDocument(false, false);
        	 return document;
        }
  	}
	
	/*public static XMLDocumento findRegioneFolder(ExtraWayService xwService, String filtroSulloStato, String filtroSulTitolare, 
			String filtrodataCreazioneDa, String filtrodataCreazioneA, 
			String filtrodataInvioDomandaDa, String filtrodataInvioDomandaA, String filtrodataConclusioneDa, String filtrodataConclusioneA, 
			String filtroTipoProcedimento)
			throws Exception {
		return findRegioneFolder(xwService, filtroSulloStato, filtroSulTitolare, filtrodataCreazioneDa, filtrodataCreazioneA, filtrodataInvioDomandaDa, filtrodataInvioDomandaA, 
				filtrodataConclusioneDa, filtrodataConclusioneA, filtroTipoProcedimento, null);
	}*/
	
	public static XMLDocumento findRegioneFolder(ExtraWayService xwService, String extraQuery, String filtroSulloStato, String filtroSulTitolare, 
			String filtrodataCreazioneDa, String filtrodataCreazioneA, 
			String filtrodataInvioDomandaDa, String filtrodataInvioDomandaA, String filtrodataConclusioneDa, String filtrodataConclusioneA, 
			String filtroTipoProcedimento, String filtroOggettoDomanda, Set<String> domandeClientId, DomandaOrderTypeEnum domandaOrderTypeEnum)
			throws Exception {
		if(domandeClientId != null && domandeClientId.size() == 0) {
			//Devo filtrare per le domandeClientId passate ma se la lista e' vuota significa che non ne sono state trovate quindi non restituisco nessuna domanda
			return null;
		}
		//titolareDaCercare
		//Modifica del 28/11/2017 la regione puo' vedere le domande in bozza
//		String queryDaEseguire = "([/fascicolo/extra/@folder_status/]<>\""
//				+ DomandaInstDao.STATO_BOZZA
//				+ "\")";
//		queryDaEseguire += " AND ([/fascicolo/extra/@folder_type/]=\""
//				+ FolderDao.DOMANDA_TITOLARE_FOLDER_TYPE
//				+ "\")";
		//nuovo 28/11/2017
//		String queryDaEseguire = "([/fascicolo/extra/@folder_type/]=\""
//				+ FolderDao.DOMANDA_TITOLARE_FOLDER_TYPE
//				+ "\")";
		
		//Modifica del 10/04/2019 la regione non deve vedere le domande Eliminate, che restano in extraway ma non in oracle
		String queryDaEseguire = "([/fascicolo/extra/@folder_status/]<>\""
				+ DomandaInstDao.ELIMINATO
				+ "\")";
		queryDaEseguire += " AND ([/fascicolo/extra/@folder_type/]=\""
				+ FolderDao.DOMANDA_TITOLARE_FOLDER_TYPE
				+ "\")";
		
		if (filtroSulloStato != null && filtroSulloStato.length() > 0){
			queryDaEseguire += " AND ([/fascicolo/extra/@folder_status/]=\""
				+ filtroSulloStato
				+ "\")";
		}
		if (filtroSulTitolare != null && filtroSulTitolare.length() > 0){
			filtroSulTitolare = filtroSulTitolare.replace("\"", " ");
			queryDaEseguire += " AND ([/fascicolo/extra/titolare/ragione_sociale/]=\""
				+ filtroSulTitolare
				+ "\")";
		}
		if (filtroTipoProcedimento != null && filtroTipoProcedimento.length() > 0){
			queryDaEseguire += " AND ([/fascicolo/extra/procedimento/@tipo_domanda/]=\""
				+ filtroTipoProcedimento
				+ "\")";
		}
		if (filtroOggettoDomanda != null && filtroOggettoDomanda.length() > 0){
			queryDaEseguire += " AND ([/fascicolo/extra/procedimento/@oggetto_domanda/]=\""
				+ filtroOggettoDomanda
				+ "\")";
		}		
		if ((filtrodataCreazioneDa != null && filtrodataCreazioneDa.length() > 0)||(filtrodataCreazioneA != null && filtrodataCreazioneA.length() > 0)){
			//([/fascicolo/extra/procedimento/@data_invio_domanda/]={20140129|20140130})
			if (filtrodataCreazioneDa != null && filtrodataCreazioneDa.length() > 0){
				SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
				Date d = formato.parse(filtrodataCreazioneDa);
				formato.applyPattern("yyyyMMdd");
				filtrodataCreazioneDa = formato.format(d);
			}
			if (filtrodataCreazioneA != null && filtrodataCreazioneA.length() > 0){
				SimpleDateFormat formato2 = new SimpleDateFormat("dd/MM/yyyy");
				Date d = formato2.parse(filtrodataCreazioneA);
				formato2.applyPattern("yyyyMMdd");
				filtrodataCreazioneA = formato2.format(d);
			}
			queryDaEseguire += " AND ([/fascicolo/storia/creazione/@data/]={"
				+ filtrodataCreazioneDa
				+ "|"
				+ filtrodataCreazioneA
				+"})";
		}
		
		if ((filtrodataInvioDomandaDa != null && filtrodataInvioDomandaDa.length() > 0)||(filtrodataInvioDomandaA != null && filtrodataInvioDomandaA.length() > 0)){
			//([/fascicolo/extra/procedimento/@data_invio_domanda/]={20140129|20140130})
			if (filtrodataInvioDomandaDa != null && filtrodataInvioDomandaDa.length() > 0){
				SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
				Date d = formato.parse(filtrodataInvioDomandaDa);
				formato.applyPattern("yyyyMMdd");
				filtrodataInvioDomandaDa = formato.format(d);
			}
			if (filtrodataInvioDomandaA != null && filtrodataInvioDomandaA.length() > 0){
				SimpleDateFormat formato2 = new SimpleDateFormat("dd/MM/yyyy");
				Date d = formato2.parse(filtrodataInvioDomandaA);
				formato2.applyPattern("yyyyMMdd");
				filtrodataInvioDomandaA = formato2.format(d);
			}
			queryDaEseguire += " AND ([/fascicolo/extra/procedimento/@data_invio_domanda/]={"
				+ filtrodataInvioDomandaDa
				+ "|"
				+ filtrodataInvioDomandaA
				+"})";
		}
		
		if ((filtrodataConclusioneDa != null && filtrodataConclusioneDa.length() > 0)||(filtrodataInvioDomandaA != null && filtrodataConclusioneA.length() > 0)){
			//([/fascicolo/extra/procedimento/@data_invio_domanda/]={20140129|20140130})
			if (filtrodataConclusioneDa != null && filtrodataConclusioneDa.length() > 0){
				SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
				Date d = formato.parse(filtrodataConclusioneDa);
				formato.applyPattern("yyyyMMdd");
				filtrodataConclusioneDa = formato.format(d);
			}
			if (filtrodataConclusioneA != null && filtrodataConclusioneA.length() > 0){
				SimpleDateFormat formato2 = new SimpleDateFormat("dd/MM/yyyy");
				Date d = formato2.parse(filtrodataConclusioneA);
				formato2.applyPattern("yyyyMMdd");
				filtrodataConclusioneA = formato2.format(d);
			}
			queryDaEseguire += " AND ([/fascicolo/extra/procedimento/@data_conclusione/]={"
				+ filtrodataConclusioneDa
				+ "|"
				+ filtrodataConclusioneA
				+"})";
		}
		
		if(domandeClientId != null && domandeClientId.size() > 0) {
			boolean write = false;
			String clientsId = "";
			for(String domandaClientId : domandeClientId) {
				if(write)
					clientsId += " OR ";
				clientsId += "\"" + domandaClientId + "\"";
				write = true;
			}
			queryDaEseguire += " AND ([/fascicolo/extra/procedimento/@domanda_client_id/]=" + clientsId + ")";
		}
		
		if(extraQuery != null && !extraQuery.isEmpty()) {
			queryDaEseguire += " " + extraQuery;
		}

		String orderBy = null;
		if(domandaOrderTypeEnum != null)
			orderBy = domandaOrderTypeEnum.getSortRule();
		XMLDocumento document = xwService.executeQuery(queryDaEseguire, null, orderBy, false);
		int selSize = Integer.parseInt(document.getAttributeValue("//Selection[@active='true']/@size"));
		if (selSize == 0) {
			return null;
		} else {
			document = xwService.firstTitlePage();
			return document;
		}
	}
	
	public static XMLDocumento findAttiSubFolder(ExtraWayService xwService,  String numeroFascicolo) throws Exception {
		String queryDaEseguire = "[/fascicolo/@numero/]=\"" + numeroFascicolo + ".*\" AND [/fascicolo/extra/@folder_type/]=\"" + FolderDao.ATTI_TITOLARE_FOLDER_TYPE +"\"";
		
		XMLDocumento document = xwService.executeQuery(queryDaEseguire, null, null, false);
		int selSize = Integer.parseInt(document.getAttributeValue("//Selection[@active='true']/@size"));
        if (selSize == 0) {
        	return null;
        }
        else{
        	 document =  xwService.loadFirstDocument(false, false);
        	 return ExtraWayService.extractDocumentFromEnvironment(document);
        }
  	}
	
	public static XMLDocumento findFolderTree(ExtraWayService xwService, String numeroFascicolo, 
			Set<String> domandeClientId, String extraQuery, 
			String filtroSulloStato, 
			String filtrodataCreazioneDa, String filtrodataCreazioneA, 
			String filtrodataInvioDomandaDa, String filtrodataInvioDomandaA, 
			String filtrodataConclusioneDa, String filtrodataConclusioneA, 
			String filtroTipoProcedimento, String filtroOggettoDomanda, DomandaOrderTypeEnum domandaOrderTypeEnum) throws Exception {
		String queryDaEseguire = "";
		
		if(numeroFascicolo != null && !numeroFascicolo.isEmpty()) {
			queryDaEseguire = "[/fascicolo/@numero/]=\"" + numeroFascicolo + "*\" " +
					"AND NOT([/fascicolo/extra/@folder_type]=\"" + EVIDENZE_DOMANDA_TITOLARE_FOLDER_TYPE + "\") AND NOT ([/fascicolo/extra/@folder_status/]=\"ELIMINATO\") " +
					extraQuery;
		} else {
			queryDaEseguire = "NOT([/fascicolo/extra/@folder_type]=\"" + EVIDENZE_DOMANDA_TITOLARE_FOLDER_TYPE + "\") AND NOT ([/fascicolo/extra/@folder_status/]=\"ELIMINATO\") " +
					extraQuery;
		}

		if(domandeClientId != null && domandeClientId.size() > 0) {
			boolean write = false;
			String clientsId = "";
			for(String domandaClientId : domandeClientId) {
				if(write)
					clientsId += " OR ";
				clientsId += "\"" + domandaClientId + "\"";
				write = true;
			}
			queryDaEseguire += " AND ([/fascicolo/extra/procedimento/@domanda_client_id/]=" + clientsId + ")";
		}
		
		//Controllo se ci sono filtri impostati e nel caso modifico la query
		if (filtroSulloStato != null && filtroSulloStato.length() > 0){
			queryDaEseguire += " AND ([/fascicolo/extra/@folder_status/]=\""
				+ filtroSulloStato
				+ "\")";
		}
		if (filtroTipoProcedimento != null && filtroTipoProcedimento.length() > 0){
			queryDaEseguire += " AND ([/fascicolo/extra/procedimento/@tipo_domanda/]=\""
				+ filtroTipoProcedimento
				+ "\")";
		}
		if (filtroOggettoDomanda != null && filtroOggettoDomanda.length() > 0){
			queryDaEseguire += " AND ([/fascicolo/extra/procedimento/@oggetto_domanda/]=\""
				+ filtroOggettoDomanda
				+ "\")";
		}
		if ((filtrodataCreazioneDa != null && filtrodataCreazioneDa.length() > 0)||(filtrodataCreazioneA != null && filtrodataCreazioneA.length() > 0)){
			//([/fascicolo/extra/procedimento/@data_invio_domanda/]={20140129|20140130})
			if (filtrodataCreazioneDa != null && filtrodataCreazioneDa.length() > 0){
				SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
				Date d = formato.parse(filtrodataCreazioneDa);
				formato.applyPattern("yyyyMMdd");
				filtrodataCreazioneDa = formato.format(d);
			}
			if (filtrodataCreazioneA != null && filtrodataCreazioneA.length() > 0){
				SimpleDateFormat formato2 = new SimpleDateFormat("dd/MM/yyyy");
				Date d = formato2.parse(filtrodataCreazioneA);
				formato2.applyPattern("yyyyMMdd");
				filtrodataCreazioneA = formato2.format(d);
			}
			queryDaEseguire += " AND ([/fascicolo/storia/creazione/@data/]={"
				+ filtrodataCreazioneDa
				+ "|"
				+ filtrodataCreazioneA
				+"})";
		}
		
		if ((filtrodataInvioDomandaDa != null && filtrodataInvioDomandaDa.length() > 0)||(filtrodataInvioDomandaA != null && filtrodataInvioDomandaA.length() > 0)){
			//([/fascicolo/extra/procedimento/@data_invio_domanda/]={20140129|20140130})
			if (filtrodataInvioDomandaDa != null && filtrodataInvioDomandaDa.length() > 0){
				SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
				Date d = formato.parse(filtrodataInvioDomandaDa);
				formato.applyPattern("yyyyMMdd");
				filtrodataInvioDomandaDa = formato.format(d);
			}
			if (filtrodataInvioDomandaA != null && filtrodataInvioDomandaA.length() > 0){
				SimpleDateFormat formato2 = new SimpleDateFormat("dd/MM/yyyy");
				Date d = formato2.parse(filtrodataInvioDomandaA);
				formato2.applyPattern("yyyyMMdd");
				filtrodataInvioDomandaA = formato2.format(d);
			}
			queryDaEseguire += " AND ([/fascicolo/extra/procedimento/@data_invio_domanda/]={"
				+ filtrodataInvioDomandaDa
				+ "|"
				+ filtrodataInvioDomandaA
				+"})";
		}
		
		if ((filtrodataConclusioneDa != null && filtrodataConclusioneDa.length() > 0)||(filtrodataInvioDomandaA != null && filtrodataConclusioneA.length() > 0)){
			//([/fascicolo/extra/procedimento/@data_invio_domanda/]={20140129|20140130})
			if (filtrodataConclusioneDa != null && filtrodataConclusioneDa.length() > 0){
				SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
				Date d = formato.parse(filtrodataConclusioneDa);
				formato.applyPattern("yyyyMMdd");
				filtrodataConclusioneDa = formato.format(d);
			}
			if (filtrodataConclusioneA != null && filtrodataConclusioneA.length() > 0){
				SimpleDateFormat formato2 = new SimpleDateFormat("dd/MM/yyyy");
				Date d = formato2.parse(filtrodataConclusioneA);
				formato2.applyPattern("yyyyMMdd");
				filtrodataConclusioneA = formato2.format(d);
			}
			queryDaEseguire += " AND ([/fascicolo/extra/procedimento/@data_conclusione/]={"
				+ filtrodataConclusioneDa
				+ "|"
				+ filtrodataConclusioneA
				+"})";
		}
		
		
		//([/fascicolo/extra/procedimento/@domanda_client_id/]="05B93958-60EA-4CF9-B414-4AB40D407805" OR "12EC5624-B138-4CB5-8433-4BD71FE4C115" OR "6ADE6DC1-179A-44E5-86FC-832DF7DCDF2A")
		String orderBy = null;
		if(domandaOrderTypeEnum != null)
			orderBy = domandaOrderTypeEnum.getSortRule();
		XMLDocumento document = xwService.executeQuery(queryDaEseguire, null, orderBy, false);
		int selSize = Integer.parseInt(document.getAttributeValue("//Selection[@active='true']/@size"));
        if (selSize == 0) {
        	return null;
        }
        else{
        	 document =  xwService.firstTitlePage();
        	 return document;
        }
  	}
	
	public static XMLDocumento findFolderByDomandaClientId(ExtraWayService xwService, String domandaClientId) throws Exception {
		String queryDaEseguire = "([/fascicolo/extra/procedimento/@domanda_client_id/]=" + domandaClientId + ") " +
				"AND NOT([/fascicolo/extra/@folder_type]=\"" + EVIDENZE_DOMANDA_TITOLARE_FOLDER_TYPE + "\") AND NOT ([/fascicolo/extra/@folder_status/]=\"ELIMINATO\")";
		
		XMLDocumento document = xwService.executeQuery(queryDaEseguire, null, null, false);
		int selSize = Integer.parseInt(document.getAttributeValue("//Selection[@active='true']/@size"));
        if (selSize == 0) {
        	return null;
        }
        else{
       	 document =  xwService.loadFirstDocument(false, false);
       	 return document;
        }
  	}

	public static XMLDocumento findFolder(ExtraWayService xwService,  String numeroFascicolo) throws Exception {
		String queryDaEseguire = "[/fascicolo/@numero/]=\"" + numeroFascicolo + "*\"";
		
		XMLDocumento document = xwService.executeQuery(queryDaEseguire, null, null, false);
		int selSize = Integer.parseInt(document.getAttributeValue("//Selection[@active='true']/@size"));
        if (selSize == 0) {
        	return null;
        }
        else{
        	 document =  xwService.loadFirstDocument(false, false);
        	 return document;
        }
  	}	
	
	public static XMLDocumento insertTitolareAttiSubFolder(ExtraWayService xwService, String folderParenteCode, TitolareModel titolareModel) throws Exception {
		XMLDocumento document = xwService.newSubFolder(folderParenteCode, "Atti");
        String idIUnitS = document.getAttributeValue("/Response/Document/@idIUnit");
        document = ExtraWayService.extractDocumentFromEnvironment(document);
        document.insertXPath(".extra.@folder_type", FolderDao.ATTI_TITOLARE_FOLDER_TYPE);
		document.insertXPath(".rif_interni.rif.@diritto", "RPA");
		document.insertXPath(".rif_interni.rif.@nome_persona", xwService.getNomePersona());
		document.insertXPath(".rif_interni.rif.@nome_uff", xwService.getNomeUfficio());

		xwService.saveDocument(document, true, Integer.parseInt(idIUnitS));        
		return document;
	}
	
	public static XMLDocumento insertTitolareProcSubFolder(ExtraWayService xwService, TitolareModel titolareModel, DomandaInst domanda, String oggettoDomanda) throws Exception {
		//recupero il fascicolo del titolare
		XMLDocumento document = FolderDao.findTitolareFolder(xwService, titolareModel);
		String numFasc = document.getAttributeValue("//fascicolo/@numero");
		//creo il sottofasciolo del procedimento e lo restituisco
		XMLDocumento subDocument = xwService.newSubFolder(numFasc, domanda.getTipoProcTempl().getDescr());
		String idIUnitS = subDocument.getAttributeValue("/Response/Document/@idIUnit");
		subDocument = ExtraWayService.extractDocumentFromEnvironment(subDocument);
		subDocument.insertXPath(".extra.@folder_type", DOMANDA_TITOLARE_FOLDER_TYPE);
		subDocument.insertXPath(".extra.@folder_status", domanda.getStato());
		subDocument.insertXPath(".extra.titolare.ragione_sociale", titolareModel.getRagSoc());//titolareModel.getCfisc());
		subDocument.insertXPath(".rif_interni.rif.@diritto", "RPA");
		subDocument.insertXPath(".rif_interni.rif.@nome_persona", xwService.getNomePersona());
		subDocument.insertXPath(".rif_interni.rif.@nome_uff", xwService.getNomeUfficio());
		subDocument.insertXPath(".extra.procedimento.@domanda_client_id", domanda.getClientid());
		subDocument.insertXPath(".extra.procedimento.@tipo_domanda", domanda.getTipoProcTempl().getDescr());
		subDocument.insertXPath(".extra.procedimento.@oggetto_domanda", oggettoDomanda);
		//da ricordare: non ho esplicitato: data di creazione, data di chiusura, note, annotazioni (richieste nei requisiti)
        String xml = xwService.saveDocument(subDocument, true, Integer.parseInt(idIUnitS));        
        return (new XMLDocumento(xml));
	}
	
    public static boolean isSubFolder(XMLDocumento doc) {
        if (doc.getAttributeValue("//fascicolo/@numero").indexOf(".") != doc.getAttributeValue("//fascicolo/@numero").lastIndexOf(".")) {
            return true;
        }
        return false;
    }
    
    public static String getRootFolderCode(String numFasc) {
        int index = numFasc.indexOf(".", numFasc.indexOf(".") + 1);
    	return numFasc.substring(0, index);
    }
    
    public static XMLDocumento findFolderDocuments(ExtraWayService xwService, String numeroFascicolo) throws Exception {
		String queryDaEseguire = "[/doc/rif_interni/rif/@cod_fasc]=\"" + numeroFascicolo + "\"";
		
		XMLDocumento document = xwService.executeQuery(queryDaEseguire, null, null, false);
		int selSize = Integer.parseInt(document.getAttributeValue("//Selection[@active='true']/@size"));
        if (selSize == 0) {
        	return null;
        }
        else{
        	 document =  xwService.allTitlePages();
        	 return document;
        }    	
    }
    
	public static XMLDocumento addPostit(ExtraWayService xwService, int idIUnit, String testo, String operatore, String ruolo, String data, String ora,boolean isImitatingOperatore) throws Exception {
		XMLDocumento document = xwService.loadDocument(idIUnit, true, true, false, false);
		document = ExtraWayService.extractDocumentFromEnvironment(document);
		
		//aggiunta del postit
		Element annotazioneEl = DocumentHelper.createElement("annotazione");
		annotazioneEl.setText(testo);
		if(isImitatingOperatore)
			annotazioneEl.addAttribute("admin", "true");
		else
			annotazioneEl.addAttribute("admin", "false");
		annotazioneEl.addAttribute("operatore", operatore);
		annotazioneEl.addAttribute("ruolo", ruolo);
		annotazioneEl.addAttribute("data", data);
		annotazioneEl.addAttribute("ora", ora);
		
		Element extraEl = document.getRootElement().element("extra");
		if (extraEl == null) {
			extraEl = DocumentHelper.createElement("extra");
			document.getRootElement().add(extraEl);
		}
		Element annotazioniEl = extraEl.element("annotazioni");
		if (annotazioniEl == null) {
			annotazioniEl = DocumentHelper.createElement("annotazioni");
			extraEl.add(annotazioniEl);
		}
		annotazioniEl.add(annotazioneEl);
		
		document = new XMLDocumento(xwService.saveDocument(document, true, idIUnit));        
		return document;
	}    
	
	public static XMLDocumento addNoteMancanzaTipiUdo(ExtraWayService xwService, int idIUnit, String testo, String operatore, String ruolo, String data, String ora) throws Exception {
		XMLDocumento document = xwService.loadDocument(idIUnit, true, true, false, false);
		document = ExtraWayService.extractDocumentFromEnvironment(document);
		
		//aggiunta del postit
		Element annotazioneEl = DocumentHelper.createElement("annotazione");
		annotazioneEl.setText(testo);
		annotazioneEl.addAttribute("operatore", operatore);
		annotazioneEl.addAttribute("ruolo", ruolo);
		annotazioneEl.addAttribute("data", data);
		annotazioneEl.addAttribute("ora", ora);
		
		Element extraEl = document.getRootElement().element("extra");
		if (extraEl == null) {
			extraEl = DocumentHelper.createElement("extra");
			document.getRootElement().add(extraEl);
		}
		Element annotazioniEl = extraEl.element("annotazionimancanzatipiudo");
		if (annotazioniEl == null) {
			annotazioniEl = DocumentHelper.createElement("annotazionimancanzatipiudo");
			extraEl.add(annotazioniEl);
		}
		annotazioniEl.add(annotazioneEl);
		
		document = new XMLDocumento(xwService.saveDocument(document, true, idIUnit));        
		return document;
	}    

	public static XMLDocumento addNoteVerifica(ExtraWayService xwService, int idIUnit, String testo, String operatore, String ruolo, String data, String ora, boolean admin) throws Exception {
		XMLDocumento document = xwService.loadDocument(idIUnit, true, true, false, false);
		document = ExtraWayService.extractDocumentFromEnvironment(document);
		
		if(testo.isEmpty()) {
			//rimozione note verifica
			Element extraEl = document.getRootElement().element("extra");
			if (extraEl != null) {
				Element verificaEl = extraEl.element("verifica");
				if (verificaEl != null) {
					extraEl.remove(verificaEl);
				}
			}
		} else {
			//aggiunta o modifcxa note verifica
			Element extraEl = document.getRootElement().element("extra");
			if (extraEl == null) {
				extraEl = DocumentHelper.createElement("extra");
				document.getRootElement().add(extraEl);
			}
			Element verificaEl = extraEl.element("verifica");
			if (verificaEl == null) {
				verificaEl = DocumentHelper.createElement("verifica");
				extraEl.add(verificaEl);
			}
	
			verificaEl.setText(testo);
			verificaEl.addAttribute("operatore", operatore);
			verificaEl.addAttribute("ruolo", ruolo);
			verificaEl.addAttribute("data", data);
			verificaEl.addAttribute("ora", ora);
			if(admin)
				verificaEl.addAttribute("admin", "true");
			else
				verificaEl.addAttribute("admin", "false");
		}		
		document = new XMLDocumento(xwService.saveDocument(document, true, idIUnit));        
		return document;
	}    

	public static XMLDocumento addWorkflowProcessInstance(ExtraWayService xwService, int idIUnit, WorkflowXWProcessInstance workflowProcessInstance) throws Exception {
		XMLDocumento document = xwService.loadDocument(idIUnit, true, true, false, false);
		document = ExtraWayService.extractDocumentFromEnvironment(document);
		
		//Cambio Stato
		document.removeXPath("//fascicolo/extra/@folder_status");
		document.insertXPath(".extra.@folder_status", DomandaInstDao.STATO_FASE_ISTRUTTORIA);
		
		//aggiunta del workflow
		Element workflowEl = DocumentHelper.createElement("wf");
		workflowEl.setText(workflowProcessInstance.getProcessDefinitionName() + " [" + workflowProcessInstance.getProcessDefinitionVersion() + "]");
		workflowEl.addAttribute("processinstance_id", Long.toString(workflowProcessInstance.getProcessInstanceId()));
		workflowEl.addAttribute("processdefinition_id", Long.toString(workflowProcessInstance.getProcessDefinitionId()));
		workflowEl.addAttribute("processdefinition_name", workflowProcessInstance.getProcessDefinitionName());
		workflowEl.addAttribute("processdefinition_version", workflowProcessInstance.getProcessDefinitionVersion());
		workflowEl.addAttribute("operatore", workflowProcessInstance.getOperatore());
		workflowEl.addAttribute("ruolo", workflowProcessInstance.getRuolo());
		workflowEl.addAttribute("data", workflowProcessInstance.getData());
		workflowEl.addAttribute("ora", workflowProcessInstance.getOra());
		
		Element extraEl = document.getRootElement().element("extra");
		if (extraEl == null) {
			extraEl = DocumentHelper.createElement("extra");
			document.getRootElement().add(extraEl);
		}
		Element workflowsEl = extraEl.element("wfs");
		if (workflowsEl == null) {
			workflowsEl = DocumentHelper.createElement("wfs");
			extraEl.add(workflowsEl);
		}
		workflowsEl.add(workflowEl);
		
		document = new XMLDocumento(xwService.saveDocument(document, true, idIUnit));        
		return document;
	}    

	public static XMLDocumento insertDomandaEvidenzeSubFolder(ExtraWayService xwService, String folderParenteCode) throws Exception {
		XMLDocumento document = xwService.newSubFolder(folderParenteCode, "Atti");
        String idIUnitS = document.getAttributeValue("/Response/Document/@idIUnit");
        document = ExtraWayService.extractDocumentFromEnvironment(document);
        document.insertXPath(".extra.@folder_type", FolderDao.EVIDENZE_DOMANDA_TITOLARE_FOLDER_TYPE);
		document.insertXPath(".rif_interni.rif.@diritto", "RPA");
		document.insertXPath(".rif_interni.rif.@nome_persona", xwService.getNomePersona());
		document.insertXPath(".rif_interni.rif.@nome_uff", xwService.getNomeUfficio());

		xwService.saveDocument(document, true, Integer.parseInt(idIUnitS));        
		return document;
	}
	
	public static XMLDocumento findEvidenzeSubFolder(ExtraWayService xwService,  String numeroFascicolo) throws Exception {
		String queryDaEseguire = "[/fascicolo/@numero/]=\"" + numeroFascicolo + ".*\" AND [/fascicolo/extra/@folder_type/]=\"" + FolderDao.EVIDENZE_DOMANDA_TITOLARE_FOLDER_TYPE +"\"";
		
		XMLDocumento document = xwService.executeQuery(queryDaEseguire, null, null, false);
		int selSize = Integer.parseInt(document.getAttributeValue("//Selection[@active='true']/@size"));
        if (selSize == 0) {
        	return null;
        }
        else{
        	 document =  xwService.loadFirstDocument(false, false);
        	 return document;
        }
  	}	
	
    public static XMLDocumento findRequisitoEvidenzeDocuments(ExtraWayService xwService, String requisitoClientid) throws Exception {
		String queryDaEseguire = "[/doc/extra/requisito_client_id]=\"" + requisitoClientid + "\"";
		
		XMLDocumento document = xwService.executeQuery(queryDaEseguire, null, null, false);
		int selSize = Integer.parseInt(document.getAttributeValue("//Selection[@active='true']/@size"));
        if (selSize == 0) {
        	return null;
        }
        else{
        	 document =  xwService.allTitlePages();
        	 return document;
        }    	
    }	

	public static XMLDocumento findAllDomandaFolder(ExtraWayService xwService) throws Exception {
		String queryDaEseguire = "[/fascicolo/extra/@folder_type]=\"" + DOMANDA_TITOLARE_FOLDER_TYPE + "\"";
		
		XMLDocumento document = xwService.executeQuery(queryDaEseguire, null, null, false);
		int selSize = Integer.parseInt(document.getAttributeValue("//Selection[@active='true']/@size"));
        if (selSize == 0) {
        	return null;
        }
        else{
        	 document =  xwService.allTitlePages();
        	 return document;
        }
  	}
	
	public static String formatNumeroProcedimento(String numeroProcedimento) {
		if (numeroProcedimento == null || numeroProcedimento.length() == 0)
			return "";
		int separatorIndex = numeroProcedimento.indexOf(".");
		return GenericUtils.trimLeftZeros(numeroProcedimento.substring(separatorIndex + 1)) + "/" + numeroProcedimento.substring(0, 4);
	}
	
	public static XMLDocumento deletePostit(ExtraWayService xwService, int idIUnit, int index) throws Exception {
		XMLDocumento document = xwService.loadDocument(idIUnit, true, true, false, false);
		document = ExtraWayService.extractDocumentFromEnvironment(document);
		
		//rimozione del postit
		index++; //index è in base 0 mentre la posizion degli xpath è in base 1
		document.removeXPath("//extra/annotazioni/annotazione[position()='" + index + "']");
		
		document = new XMLDocumento(xwService.saveDocument(document, true, idIUnit));        
		return document;
	} 	
    
	public static XMLDocumento deletePostitMancanzaTipiUdo(ExtraWayService xwService, int idIUnit, int index) throws Exception {
		XMLDocumento document = xwService.loadDocument(idIUnit, true, true, false, false);
		document = ExtraWayService.extractDocumentFromEnvironment(document);
		
		//rimozione del postit
		index++; //index è in base 0 mentre la posizion degli xpath è in base 1
		document.removeXPath("//extra/annotazionimancanzatipiudo/annotazione[position()='" + index + "']");
		
		document = new XMLDocumento(xwService.saveDocument(document, true, idIUnit));        
		return document;
	} 	

	public static XMLDocumento checkPostitMancanzaTipiUdo(ExtraWayService xwService, int idIUnit, int index) throws Exception {
		XMLDocumento document = xwService.loadDocument(idIUnit, true, true, false, false);
		document = ExtraWayService.extractDocumentFromEnvironment(document);

		index++; //index è in base 0 mentre la posizion degli xpath è in base 1
    	List<Element> annotazioniMancanzaTipiUdoL = document.selectNodes("//fascicolo/extra/annotazionimancanzatipiudo/annotazione[position()='" + index + "']");
    	for (Element annotazioneEl:annotazioniMancanzaTipiUdoL) {
			annotazioneEl.addAttribute("stato", PostIt.STATO_CONTROLLATO);
    	}
		document = new XMLDocumento(xwService.saveDocument(document, true, idIUnit));        
		return document;
	} 	
}
