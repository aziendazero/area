package it.tredi.auac.bean;

import it.highwaytech.apps.generic.XMLDocumento;

import java.io.Serializable;

public class XWUIBean implements Serializable {

	private static final long serialVersionUID = 5365424703034415525L;
	protected XMLDocumento document; //xml della pagina corrente dei titoli
	
	public XWUIBean() {
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
        return document == null? "0" : document.getAttributeValue("/Response/@pageCount", "1");
    }

    public String getPageIndex() {
        return document == null? "0" : document.getAttributeValue("/Response/@pageIndex", "1");
    }

    public String getContentsCount() {
        return document == null? "0" : document.getAttributeValue("/Response/@seleSize");
    }
    
    public XMLDocumento getContents() throws Exception {
    	return this.document;
    }    
    
    public void setContents(XMLDocumento document) throws Exception {
    	this.document = document;
    }

}
