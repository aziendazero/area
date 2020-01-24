package it.tredi.auac.bean;

import it.highwaytech.apps.generic.XMLDocumento;
import it.tredi.auac.ExtraWayService;
import oracle.net.aso.d;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

public class XWTitlesBean implements Serializable {
	
	private static final long serialVersionUID = 4649818284394033366L;
	protected XMLDocumento document; //xml della pagina corrente dei titoli
	private List<Map<String,String>> titleL; //lista dei titoli
	private int titlePageSize = ExtraWayService.DEFAULT_TITLE_PAGE_SIZE;
	
	public XWTitlesBean() {
	}

	public int getTitlePageSize() {
		return titlePageSize;
	}

	public void setTitlePageSize(int titlePageSize) {
		this.titlePageSize = titlePageSize;
	}

	public boolean isCanFirst() {
        return document == null? false : document.testAttributeValue("/Response/@canFirst", "true");
    }

    public boolean isCanPrev() {
        return document == null? false : document.testAttributeValue("/Response/@canPrev", "true");
    }

    public boolean isCanNext() {
        return document == null? false : document.testAttributeValue("/Response/@canNext", "true");
    }

    public boolean isCanLast() {
        return document == null? false : document.testAttributeValue("/Response/@canLast", "true");
    }

    public String getPageCount() {
        return document == null? "0" : document.getAttributeValue("/Response/@pageCount");
    }

    public String getPageIndex() {
        return document == null? "0" : document.getAttributeValue("/Response/@pageIndex");
    }

    public String getContentsCount() {
        return document == null? "0" : document.getAttributeValue("/Response/@seleSize");
    }
    
    public void setContents(XMLDocumento document) throws Exception {
        if (titleL == null)
        	titleL = new ArrayList<Map<String,String>>();
        else
        	titleL.clear();  
        this.document = document;
        int offsetIndex = (Integer.parseInt(getPageIndex())-1)*Integer.parseInt(getPageCount());
        if(this.document != null) {
	        @SuppressWarnings("unchecked")
			List<Element> itemL = document.selectNodes("//Item"); //per ogni titolo
	        for (int i=0; i<itemL.size(); i++) {
	        	Map<String,String> content = buildContent(offsetIndex + i, itemL.get(i));
	    		titleL.add(content);
	        }
        }
    }
    
    public void init() {
    	if (titleL == null)
        	titleL = new ArrayList<Map<String,String>>();
    }
    
    protected Map<String,String> buildContent(int index, Element xwTitle) throws Exception {
    	Map<String,String> content = new HashMap<String,String>();

        //idIUnit
        String idIUnit = xwTitle.attributeValue("idIUnit").trim();
        content.put("idIUnit", idIUnit);
        
        //titolo
        String value = xwTitle.attributeValue("value");
        value = value.substring(value.indexOf("|epn|") + 5); //elimino la parte preliminare del titolo (comprende sort e epn)
        content.put("title", value);
        
        //index globale rispetto alla selezione
        content.put("index", Integer.toString(index));
        
        return content;
    }

	public List<Map<String, String>> getTitleL() {
		return titleL;
	}

	public void setTitleL(List<Map<String, String>> titleL) {
		this.titleL = titleL;
	}
	
}
