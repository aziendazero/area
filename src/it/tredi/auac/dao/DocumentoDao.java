package it.tredi.auac.dao;

import it.highwaytech.apps.generic.XMLDocumento;
import it.tredi.auac.DocTypeEnum;
import it.tredi.auac.ExtraWayService;

public class DocumentoDao {
	public static XMLDocumento creaDocumento(ExtraWayService xwService, String oggetto, String creatorLogin, String creatorRole, String reqId, DocTypeEnum docTypeEnum) throws Exception {
		return creaDocumento(xwService, oggetto, creatorLogin, creatorRole, false, reqId, docTypeEnum);
	}
	
	//Overloaded function to save document as admin
	public static XMLDocumento creaDocumento(ExtraWayService xwService, String oggetto, String creatorLogin, String creatorRole, boolean isAdmin, String reqId, DocTypeEnum docTypeEnum) throws Exception {
		XMLDocumento document = new XMLDocumento("<doc/>");
		document.insertXPath(".@tipo", "varie");
		document.insertXPath(".oggetto", oggetto);
		document.insertXPath(".oggetto.@xml:space", "preserve");
		document.insertXPath(".classif.@cod", "00/00");
		document.insertXPath(".rif_interni.rif_interno.@diritto", "RPA");
		document.insertXPath(".rif_interni.rif_interno.@nome_persona", xwService.getNomePersona());
		document.insertXPath(".rif_interni.rif_interno.@nome_uff", xwService.getNomeUfficio());
		document.insertXPath(".extra.creazione_login", creatorLogin);
		document.insertXPath(".extra.creazione_ruolo", creatorRole);
		if(isAdmin)
			document.insertXPath(".extra.creazione_admin", "true");
		else
			document.insertXPath(".extra.creazione_admin", "false");
		if(docTypeEnum != null) {
			///doc/extra/tipo_doc
			document.insertXPath(".extra.tipo_doc", docTypeEnum.name());
		}
		
		if (reqId != null && reqId.length() > 0)
			document.insertXPath(".extra.requisito_client_id", reqId);
		
		String xml = xwService.saveDocument(document);        
        return (new XMLDocumento(xml));
	}
	
	public static XMLDocumento aggiungiDocAFolder(ExtraWayService xwService, String idIUnitFolder, String idIUnitDoc) throws Exception {
		XMLDocumento document = new XMLDocumento("<fascicolo/>");
		document.insertXPath(".@idIUnit", idIUnitFolder);
		document.insertXPath(".doc.@idIUnit", idIUnitDoc);
		String xmlFolder = document.getStringDocument();
		XMLDocumento xmlDocInserito = xwService.addInFolder(xmlFolder);
        return xmlDocInserito;
	}
	
}
