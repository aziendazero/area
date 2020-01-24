package it.tredi.auac.bean;

import it.highwaytech.apps.generic.XmlReplacer;
import it.highwaytech.apps.generic.utils.GenericUtils;
import it.highwaytech.util.Text;
import it.tredi.auac.dao.FolderDao;

import java.io.Serializable;
import java.util.Map;
import java.util.Vector;

import org.dom4j.Element;

public class XWFolderTitlesBean extends XWTitlesBean implements Serializable {
	private static final long serialVersionUID = -3485483866566905478L;

	public XWFolderTitlesBean() {
		super();
	}

	@Override
	protected Map<String, String> buildContent(int index, Element xwTitle) throws Exception {
		Map<String, String> content = super.buildContent(index, xwTitle);
		String title = content.get("title");
		@SuppressWarnings("unchecked")
		Vector<String> titleV = Text.split(title, "|");       
		
		//oggetto
		content.put("oggetto", titleV.get(8));
		
		//folder_type
		content.put("folder_type", titleV.get(21));
		
		//folder_status
		content.put("folder_status", titleV.get(22));
		
		//data_creazione
		String s = titleV.get(23).trim();
		s = (s.length() == 0)? "" : GenericUtils.dateFormat(XmlReplacer.normalizeDate(s));
		content.put("data_creazione", s);
		
		//data_invio_domanda
		s = titleV.get(24).trim();
		s = (s.length() == 0)? "" : GenericUtils.dateFormat(XmlReplacer.normalizeDate(s));
		content.put("data_invio_domanda", s);
		
		//data_conclusione
		s = titleV.get(25).trim();
		s = (s.length() == 0)? "" : GenericUtils.dateFormat(XmlReplacer.normalizeDate(s));
		content.put("data_conclusione", s);	
		
		//ragione_sociale
		content.put("ragione_sociale", titleV.get(30));
		
		//numero_procedimento
		content.put("numero_procedimento", FolderDao.formatNumeroProcedimento(titleV.get(31)));

		//domanda_client_id
		content.put("domanda_client_id", titleV.get(32));
		
		//oggetto_domanda
		content.put("oggetto_domanda", titleV.get(50));

		//valutazione_completezza_correttezza
		s = titleV.get(51).trim();
		s = (s.length() == 0)? "" : GenericUtils.dateFormat(XmlReplacer.normalizeDate(s));
		content.put("valutazione_completezza_correttezza", s);

		//valutazione_rispondenza_programmazione
		s = titleV.get(52).trim();
		s = (s.length() == 0)? "" : GenericUtils.dateFormat(XmlReplacer.normalizeDate(s));
		content.put("valutazione_rispondenza_programmazione", s);

		//conferimento_incarico
		s = titleV.get(53).trim();
		s = (s.length() == 0)? "" : GenericUtils.dateFormat(XmlReplacer.normalizeDate(s));
		content.put("conferimento_incarico", s);

		//redazione_rapporto_verifica
		s = titleV.get(54).trim();
		s = (s.length() == 0)? "" : GenericUtils.dateFormat(XmlReplacer.normalizeDate(s));
		content.put("redazione_rapporto_verifica", s);

		//presentazione_provvedimento
		s = titleV.get(55).trim();
		s = (s.length() == 0)? "" : GenericUtils.dateFormat(XmlReplacer.normalizeDate(s));
		content.put("presentazione_provvedimento", s);

		//valutazione_collegiale
		s = titleV.get(56).trim();
		s = (s.length() == 0)? "" : GenericUtils.dateFormat(XmlReplacer.normalizeDate(s));
		content.put("valutazione_collegiale", s);

		return content;
	}
	
}
