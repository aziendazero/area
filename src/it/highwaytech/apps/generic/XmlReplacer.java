/* XmlReplacer.java */

package it.highwaytech.apps.generic;

import it.highwaytech.util.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;

/**
* Fornisce metodi ad alto livello per la manipolazione di un documento XML.
* Inizialmente l'XMl veniva trattato come stringa, recentemente � stato introdotto - con grande profitto -
* il parser Dom4j. Il codice � stato solo in parte aggiornato secondo la nuova filosofia.
* <br>DIPENDENZE: Nessuna.
*/

public class XmlReplacer {

	/*
	 * costanti definite per le sostitutizioni di caratteri speciali non riconosciuti in iso-8859-1 
	 */
	public static final String EURO_HTML_ENTITY = "&#x80;";
	public static final String EURO_HTML_ENTITY_2 = "&#x20ac;";
	public static final String APICE_SINGOLO_APERTO_HTML_ENTITY = "&#x91;";
	public static final String APICE_SINGOLO_CHIUSO_HTML_ENTITY = "&#x92;";
	public static final String VIRGOLETTE_APERTE_HTML_ENTITY = "&#x93;";
	public static final String VIRGOLETTE_CHIUSE_HTML_ENTITY = "&#x94;";
	public static final String BULLET_HTML_ENTITY = "&#x95;";
	public static final String EN_DASH_HTML_ENTITY = "&#x96;";
	public static final String EM_DASH_HTML_ENTITY = "&#x97;";
	public static final String TILDE_HTML_ENTITY = "&#x98;";
	
	public static final String EURO_DECIMAL_ENTITY = "&#8364;";
	public static final String APICE_SINGOLO_APERTO_DECIMAL_ENTITY = "&#8216;";
	public static final String APICE_SINGOLO_CHIUSO_DECIMAL_ENTITY = "&#8217;";
	public static final String VIRGOLETTE_APERTE_DECIMAL_ENTITY = "&#8220;";
	public static final String VIRGOLETTE_CHIUSE_DECIMAL_ENTITY = "&#8221;";
	public static final String BULLET_DECIMAL_ENTITY = "&#8226;";
	public static final String EN_DASH_DECIMAL_ENTITY = "&#8211;";
	public static final String EM_DASH_DECIMAL_ENTITY = "&#8212;";
	public static final String TILDE_DECIMAL_ENTITY = "&#732;";
	
	public static final String EURO_CODE_1 = "\u20ac"; // il carattere euro puo' essere restituito da eXtraWay in entrambe le codifiche 
	public static final String EURO_CODE_2 = "\u0080";
	public static final String APICE_SINGOLO_APERTO_CODE = "\u0091";
	public static final String APICE_SINGOLO_CHIUSO_CODE = "\u0092";
	public static final String VIRGOLETTE_APERTE_CODE = "\u0093;";
	public static final String VIRGOLETTE_CHIUSE_CODE = "\u0094;";
	public static final String BULLET_CODE = "\u0095;";
	public static final String EN_DASH_CODE = "\u0096";
	public static final String EM_DASH_CODE = "\u0097";
	public static final String TILDE_CODE = "\u0098";
	
    // costanti per la gestione del tipo di filtraggio nel metodo "documentFilter"
    public static final int iTypeFilter_LEAVEONLY 	= 0;

    // Federico 18/01/2005: aggiunta var. "htmlSpecialEntities" e codice statico per la sua inizializzazione
    /**
     * Questo vettore contiene l'elenco delle Special Entities che � possibile utilizzare in HTML 4.0.
     * Il vettore viene inizializzato mediante codice statico che, all'atto del caricamento della classe
     * in memoria, lo riempie con le entities contenute in una lista "cablata" nel codice.
     *
     * @see {@link #escapeAmpersand(String)}
     */
    protected static Vector htmlSpecialEntities;

    private static it.highwaytech.util.Log log = it.highwaytech.util.LogFactory.getLogger(XmlReplacer.class);

    private static final String nameSpaceString  = " xmlns:xw=\"" + XMLDocumento.XW_NAMESPACE +
                                                   "\" xmlns:gml=\"" + XMLDocumento.GML_NAMESPACE +
                                                   "\" xmlns:h=\"" + XMLDocumento.H_NAMESPACE + "\"";

    /* ***********************************************************************************************
     ****************** i n i z i o   c o d i c e   s t a t i c o **********************************
     ***********************************************************************************************/
    static {
        // elenco delle Special Entities che � possibile utilizzare in HTML 4.0
        String[] tmp = {"&quot;",
                        "&amp;",
                        "&lt;",
                        "&gt;",
                        "&OElig;",
                        "&oelig;",
                        "&Scaron;",
                        "&scaron;",
                        "&Yuml;",
                        "&circ;",
                        "&tilde;",
                        "&ensp;",
                        "&emsp;",
                        "&thinsp;",
                        "&zwnj;",
                        "&zwj;",
                        "&lrm;",
                        "&rlm;",
                        "&ndash;",
                        "&mdash;",
                        "&lsquo;",
                        "&rsquo;",
                        "&sbquo;",
                        "&ldquo;",
                        "&rdquo;",
                        "&bdquo;",
                        "&dagger;",
                        "&Dagger;",
                        "&permil;",
                        "&lsaquo;",
                        "&rsaquo;",
                        "&euro;"};

        // creazione e riempimento del vettore delle Special Entities
        htmlSpecialEntities = new Vector();

        for (int j = 0; j < tmp.length; j++) {
            htmlSpecialEntities.add(tmp[j]);
        }

        log.debug("(static code) vector \"htmlSpecialEntities\"" +
                " created with " + htmlSpecialEntities.size() + " entities " + htmlSpecialEntities.toString());
    }
    /* ***********************************************************************************************
     ************************** f i n e   c o d i c e   s t a t i c o ******************************
     ***********************************************************************************************/

    /** ntesini
     * 	Restituisce un documento xml stringa filtrato a seconda dei parametri passati
     *  come elemento e tipo filtraggio.
     *
     * @param strCurXml 			Stringa: Xml document
     * @param strElSearch			Stringa Elemento da cercare nel documento xml
     * @param arSubStringFileProp	ArrayList delle sottostringhe dal file di proprieties
     * @param iTypeFilter			Integer Tipo di filtraggio
     * @return						Stringa: Xml filtrato
     * @throws DocumentException
     */
    public static void documentFilter(XMLDocumento docXml,
                                        String strElSearch,
                                        ArrayList arSubStringFileProp,
                                        int iTypeFilter
                                        )
    {
        String strXPath;
        String strRootName = "NICK"; // default

        strXPath = "/" + strRootName +"//" + strElSearch;
        List lstNodes =  docXml.selectNodes(strXPath);

        String strSubStringValNode;
        String strValNode;
        int iNode = 0;
        boolean bExist = false;


        // loop xml nodes
        for (iNode = 0;iNode < lstNodes.size(); iNode++ ){

            // get node value ( check presence "." )
            strValNode = ((Element)lstNodes.get(iNode)).getText();
            if(strValNode.indexOf(".")> -1){
                strSubStringValNode = strValNode.substring(0, strValNode.indexOf(".")).toLowerCase();
            }
            else if(strValNode.indexOf(" ")> -1){
                strSubStringValNode = strValNode.substring(0, strValNode.indexOf(" ")).toLowerCase();
            }
            else{
                strSubStringValNode = strValNode.toLowerCase();
            }

            bExist = false;
            if(arSubStringFileProp.contains(strSubStringValNode)){
                bExist = true;
            }

            if(!bExist){
                switch (iTypeFilter){
                    case iTypeFilter_LEAVEONLY :
                        // delete node
                        ((Element)lstNodes.get(iNode)).detach();
                        break;

                    default:
                        // delete node
                        ((Element)lstNodes.get(iNode)).detach();
                        break;
                }
            }
        }// loop xml nodes
    }


  /** Rstituisce il valore contenuto in un attributo di un particolare elemento
  * Nel caso di element multistanza restituisce il primo elemento trovato.
  *
  * @param xmlInput XML di input
  * @param element Elemento contenento l'attributo desiderato
  * @param attribute Attributo del quale si vuole il contenuto
  * @return Restiuisce il valore dell'attributo se esiste, null altrimenti
  */
  public static String getAttribute(String xmlInput, String element, String attribute) {
    int startElem = xmlInput.indexOf("<" + element);
    if (startElem == -1)
      return null;

    int endElem = xmlInput.indexOf(">", startElem);
    String rangedString = xmlInput.substring(startElem, endElem + 1);

    int startAttr = rangedString.indexOf(" " + attribute + "=\"");
    if (startAttr == -1)
      return null;
    else
      startAttr += attribute.length() + 3;

    int endAttr = rangedString.indexOf("\"", startAttr);
    if (endAttr == -1)
      return null;

    return Text.htmlToText(rangedString.substring(startAttr, endAttr));
  }

  /**
   * Restituisce l'indice della fine di un element
   * @param xmlInput XML di input contenente al suo interno l'elemento in questione
   * @param element nome dell'elemento di cui trovare la fine
   * @param index indice da cui partire per individuare l'elemento voluto
   */
  public static int seekEndEl(String xmlInput, String element, int index) {
    int endEl, endEl1;
    int endElNewLine;

    endEl = xmlInput.indexOf("<" + element + ">", index); //inizio nuovo el
    if (endEl == -1){
      endEl = xmlInput.indexOf("<" + element + " ", index); //inizio nuovo el
      endElNewLine = xmlInput.indexOf("<" + element + "\n", index); //inizio nuovo el
      if (endEl == -1 || (endElNewLine > -1 && endElNewLine < endEl)){
          endEl = endElNewLine;
      }
    }

    endEl1 = xmlInput.indexOf("</" + element + ">", index); //prima fine trovata
    if (endEl == -1)
      endEl = 99998;
    if (endEl1 == -1)
      endEl1 = 99999;
    if (endEl < endEl1) //self closed
      endEl1 = xmlInput.indexOf("/>", index) + 2;
    else
      endEl1 += (new String("</" + element + ">")).length() + 1;
    return endEl1;
  }

  /** Rimuove tutti gli elementi con un certo nome */
  public static String removeMultistElement(String xmlInput, String element) throws org.dom4j.DocumentException {
    element = element.replaceAll("[.]", "/");
    Document document = XmlReplacer.mountXML("DUMMY", xmlInput);

    if (element.charAt(0) != '/')
      element = "//" + element; //percorso relativo
    else
      element = "/DUMMY" + element; //percorso assoluto

    if (element.endsWith("/"))
      element = element.substring(0, element.length() - 1);

    java.util.List l = document.selectNodes(element);
    if (l != null)
      for (int i = 0; i < l.size(); i++){
         try {
         ((Element) l.get(i)).detach();
         }
         catch(ClassCastException e){
             log.error("Tentativo di eliminare un nodo che non � un elemento " + element + "\n" + e.getMessage());
         }
      }

    return XmlReplacer.unmountXML(document, "DUMMY");
  }

  /** Rimuove un attributo di un elemento (nel caso in cui l'elemento sia
  multistanza lavora solo sul primo trovato) */
  public static String removeAttr(String xmlInput, String element, String attribute) {
    int index = xmlInput.indexOf("<" + element);

    if (index != -1) { //elemento esiste
      int index1 = seekEndEl(xmlInput, element, index);
      int index_attr = xmlInput.indexOf(attribute + "=\"");
      if (index_attr != -1 && index_attr < index1) { //attributo trovato e contenuto in elemento
        int index1_attr = xmlInput.indexOf("\"", index_attr + attribute.length() + 2);
        xmlInput = (new StringBuffer(xmlInput)).delete(index_attr, index1_attr + 1).toString();
      }
    }
    return xmlInput;
  }

  /** Rstituisce un vettore di valori di un attributo di un particolare elemento multistanza
  *
  *
  * @param xmlInput XML di input
  * @param element Elemento contenento l'attributo desiderato
  * @param attribute Attributo del quale si vuole il contenuto
  * @return Restiuisce il valore dell'attributo se esiste, null altrimenti
  */
  public static Vector getMultistAttribute(String xmlInput, String element, String attribute) {
    int index = 0;
    int startAttr, endAttr, start, endEl, endEl1;
    Vector v = new Vector();

    while ((start = xmlInput.indexOf("<" + element, index)) != -1) {
      index = start + 1;
      startAttr = xmlInput.indexOf(" " + attribute + "=\"", index);
      endEl = xmlInput.indexOf(">", index);
      endEl1 = xmlInput.indexOf("/>", index);
      if (endEl == -1)
        endEl = 99999;
      if (endEl1 == -1)
        endEl1 = 99999;
      endEl = Math.min(endEl, endEl1);
      if (startAttr != -1 && startAttr < endEl) {
        startAttr += attribute.length() + 3;
        endAttr = xmlInput.indexOf("\"", startAttr);
        v.add(Text.htmlToText(xmlInput.substring(startAttr, endAttr)));
      }
    }
    return v;
  }

  /**
  * Crea un documento nuovo vuoto
  * @param pne Nome del nodo primario
  */
  public static String newDocument(String pne) {
    String doc = "<" + pne + ">" + "</" + pne + ">";
    return doc;
  }

  /**
  * Restituisce il nome del nodo primario di un documento
  */
  public static String getPrimaryNode(String xmlInput) {
    int index = 0;
    if (xmlInput.toString().startsWith("<?"))
      index = xmlInput.toString().indexOf("?>", index); //non bisogna considerare header iniziale

    index = xmlInput.toString().indexOf('<', index);
    int lindex = xmlInput.substring(index).toString().indexOf('>');
    int lindex1 = xmlInput.substring(index).toString().indexOf(' ');
    if (lindex1 == -1)
      lindex1 = 999999;
    int lindex2 = xmlInput.substring(index).toString().indexOf('\n');
    if (lindex2 == -1)
      lindex2 = 999999;

    lindex = Math.min(lindex, lindex1);
    lindex = Math.min(lindex, lindex2);
    return xmlInput.substring(index + 1, index + lindex);
  }

  /** Rstituisce il valore contenuto in un elemento
  *
  * @param xmlInput XML di input
  * @param element Elemento desiderato
  * @return Restiuisce il valore dell'elemento se esiste, null altrimenti
  */
  public static String getElement(String xmlInput, String element) {
    int startElem = xmlInput.indexOf("<" + element);
    if (startElem == -1)
      return null;

    int startText = xmlInput.indexOf(">", startElem) + 1;
    int endText = xmlInput.indexOf("</" + element + ">", startText);

    return Text.htmlToText(xmlInput.substring(startText, endText));
  }

  /** Aggiunge un attributo a un elemento
  * Se esistono pi� istanze dell'elemento, l'attributo viene aggiunto solo alla prima.
  *
  * @param xmlInput XML di input
  * @param element elemento al quale si vuole aggiungere l'attributo (viene messo in ultima posizione)
  * @param attribute nome dell'attributo che si vuole aggiungere
  * @param val valore dell'attributo che si vuole aggiungere
  * @return Restiuisce la stringa xml prodotta, null in caso di errore durante l'inserimento
  * dell'elemento
  */
  public static String addAttribute(String xmlInput, String element, String attribute, String val) {
    /*
    //versione senza parser
    int start = xmlInput.indexOf("<" + element);
    if (start == -1)
      return null;

    int end = xmlInput.indexOf(">", start);
    StringBuffer sb;
    if (xmlInput.charAt(end - 1) == '/') //self closed
      sb = new StringBuffer(xmlInput).replace(end - 1, end + 1, " " + attribute + "=\"" + val + "\"/>");
    else
      sb = new StringBuffer(xmlInput).replace(end, end + 1, " " + attribute + "=\"" + val + "\">");
    return sb.toString();
    */
    try {
      Document toManipulate = XmlReplacer.mountXML("DUMMY",xmlInput);
      List targetElements = toManipulate.selectNodes("/DUMMY//"+element);
      if ( targetElements.isEmpty() ) throw new DocumentException("Element " + element + " not found!");

      return XmlReplacer.unmountXML(toManipulate,"DUMMY");
    }
    catch (DocumentException e) {
      log.error(e.getMessage());
      return null;
    }
  }

  /** Aggiunge un attributo a un elemento che pero' ha un attributo con un certo valore (gestione multistanza)
  * Attenzione: il meccanismo utilizzato e' affidabile perche' gli attributi
  * di un element non possono contenere il carattere '>'
  *
  * @param xmlInput XML di input
  * @param element elemento al quale si vuole aggiungere l'attributo (viene messo in ultima posizione)
  * @param attrVincleName l'elemento in cui inserire deve avere questo attributo
  * @param attrVincleVal valore che deve avere attrVincleName
  * @param attribute nome dell'attributo che si vuole aggiungere
  * @param val valore dell'attributo che si vuole aggiungere
  * @return Restiuisce la stringa xml prodotta, null in caso di errore durante l'inserimento
  * dell'elemento
  */
  public static String addAttribute(String xmlInput, String attrVincleName, String attrVincleVal, String element, String attribute, String val) {
    int index = 0;
    int startAttr, endAttr, start, endEl, endEl1;

    while ((start = xmlInput.indexOf("<" + element, index)) != -1) {
      index = start + 1;
      startAttr = xmlInput.indexOf(attrVincleName + "=\"", index) + attrVincleName.length() + 2;
      endAttr = xmlInput.indexOf("\"", startAttr);

      endEl = xmlInput.indexOf(">", index);
      endEl1 = xmlInput.indexOf("/>", index);
      if (endEl == -1)
        endEl = 99999;
      if (endEl1 == -1)
        endEl1 = 99999;
      endEl = Math.min(endEl, endEl1);

      if (xmlInput.substring(startAttr, endAttr).compareTo(attrVincleVal) == 0 && startAttr < endEl)
        break;
    }
    int end = xmlInput.indexOf(">", index);

    StringBuffer sb;
    if (xmlInput.charAt(end - 1) == '/') //self closed
      sb = new StringBuffer(xmlInput).replace(end - 1, end + 1, " " + attribute + "=\"" + val + "\"/>");
    else
      sb = new StringBuffer(xmlInput).replace(end, end + 1, " " + attribute + "=\"" + val + "\">");
    return sb.toString();
  }

  /** Aggiunge un elemento come primo figlio di un altro elemento
  *
  * @param xmlInput XML di input
  * @param parent genitore dell'elemento da inserire
  * @param elemName elemento da inserire
  * @param elemVal text dell'elemento inserito
  * @return Restiuisce la stringa xml prodotta, null in caso di errore durante l'inserimento
  * dell'elemento
  */
  public static String addElement(String xmlInput, String parent, String elemName, String elemVal, String attrName, String attrVal) {
    int start = xmlInput.indexOf("<" + parent);
    if (start == -1)
      return null;

    if (attrName.length() > 0)
      attrVal = " " + attrName + "=\"" + attrVal + "\"";

    int end = xmlInput.indexOf(">", start);
    StringBuffer sb = new StringBuffer(xmlInput).replace(end, end + 1, ">\n<" + elemName + attrVal + ">" + elemVal + "</" + elemName + ">\n");
    return sb.toString();
  }

  /**
  * Restituzione del valore di tutti gli element o attribute specificati (multistanza). Implementa anche la funzionalit� di
  * vincolo. Per motivi di efficienza si pu� specificare di restituire solo il primo trovato.
  *
  * @param xmlInput XML di input.
  * @param xPath Percorso xPath SEMPLICE dell'element o attribute desiderato. SEMPLICE significa che pu� assumere solamente le forme
  * element/@attribute oppure element.
  * @param vincle Vincolo di attributo, ovvero vengono restituiti solo gli element/attribute il cui element ha un atributo con un certo valore.
  * La sintassi per specificare il vincolo �: attrname="attrval". Per non utilizzare vincoli specificare la stringa vuota.
  * @param onlyFirst Utilizzare true se si vuole restituire solo il primo element/attribute trovato
  * (quando ad es. si sa a priori che ne esiste uno solo), false altrimenti.
  * @return un vettore contenente tutti gli element o attribute trovati.
  */
  public static Vector getMultistAttrOrElm(String xmlInput, String xPath, String vincle, boolean onlyFirst) {
    Vector v = new Vector();
    String elemName, attrName, elemVal, attrVal, cntxtString;
    int startElem = 0, startElemVal, endElemVal, startAttrVal, endAttrVal;
    boolean endLoop = false;

    int sepIndex = xPath.indexOf("/@");
    if (sepIndex == -1) { //NO attribute
      elemName = xPath;
      attrName = "";
    }
    else {
      elemName = xPath.substring(0, sepIndex);
      attrName = xPath.substring(sepIndex + 2);
    }
    while ((startElem = xmlInput.indexOf("<" + elemName, startElem)) != -1 && endLoop != true) { //per tutti gli element desiderati
      startElemVal = xmlInput.indexOf(">", startElem) + 1;
      /*			startElemVal = ( startElemVal == 0 )? 999999 : startElemVal;
                  endElemSelfClosed = xmlInput.indexOf("/>", startElem) + 1;
                  endElemSelfClosed = ( endElemSelfClosed == 0 )? 999999 : endElemSelfClosed;
                  startElemVal = Math.min(startElemVal, endElemSelfClosed);*/
      cntxtString = xmlInput.substring(startElem, startElemVal);
      if (attrName.length() == 0) { //NO attribute -> cerco inizio valore di element (suppongo no self-closed)
        endElemVal = xmlInput.indexOf("</" + elemName, startElemVal);
        elemVal = xmlInput.substring(startElemVal, endElemVal);
        if (vincle.length() > 0) { //richiesto vincolo
          if (cntxtString.indexOf(vincle) != -1) {
            if (onlyFirst == true)
              endLoop = true;
            v.add(Text.htmlToText(elemVal));
          }
        }
        else {
          v.add(Text.htmlToText(elemVal));
          if (onlyFirst == true)
            endLoop = true;
        }
      }
      else { //cerco attribute (magari vincolato)
        startAttrVal = cntxtString.indexOf(" " + attrName + "=\"");
        if (startAttrVal != -1) { //nell'element esiste l'attributo cercato
          startAttrVal += 3 + attrName.length();
          endAttrVal = cntxtString.indexOf("\"", startAttrVal);
          attrVal = cntxtString.substring(startAttrVal, endAttrVal);
          if (vincle.length() > 0) { //richiesto vincolo
            if (cntxtString.indexOf(vincle) != -1) {
              v.add(Text.htmlToText(attrVal));
              if (onlyFirst == true)
                endLoop = true;
            }
          }
          else {
            v.add(Text.htmlToText(attrVal));
            if (onlyFirst == true)
              endLoop = true;
          }
        }
      }

      startElem++; //altrimenti andrebbe in loop perche' farebbe match sempre sullo stesso element

    } //end_while

    return v;
  }

  /**
  * Restituisce il contenuto della prima istanza di un attributo in una porzione di XML
  * @param bacinoDiPescaggio la porzione di XML da cui estrarre
  * @param nameAttribute il nome dell'attributo di cui si vuole estrarre il contenuto
  */
  public static String extractAttributeContent(String bacinoDiPescaggio, String nameAttribute) {
    int startAttr, endAttr;
    startAttr = bacinoDiPescaggio.indexOf(nameAttribute + "=\"");
    if (startAttr != -1) {
      startAttr += nameAttribute.length() + 2;
      endAttr = bacinoDiPescaggio.indexOf("\"", startAttr);
      if (endAttr != -1) {
        return Text.htmlToText(bacinoDiPescaggio.substring(startAttr, endAttr));
      }
      else {
        log.error("XML structure NOT WELL-FORMED!");
        return "&#60;&#62;";
      }
    }
    else {
        log.error("attribute " + nameAttribute + " NOT FOUND!");
      return "&#60;&#62;";
    }
  }

  /**
  * Restituisce il contenuto della prima istanza di un elemento non vuoto in una porzione di XML
  * @param bacinoDiPescaggio la porzione di XML da cui estrarre
  * @param nameElement il nome dell'elemento di cui si vuole estrarre il contenuto
  */
  public static String extractElementContent(String bacinoDiPescaggio, String nameElement) {
    int startElem, endElem, boundary;
    startElem = bacinoDiPescaggio.indexOf("<" + nameElement);
    if (startElem != -1) {
      boundary = bacinoDiPescaggio.indexOf(">", startElem);
      if (boundary != -1) {
        startElem = boundary + 1;
        endElem = bacinoDiPescaggio.indexOf("</" + nameElement, startElem);
        if (endElem != -1) {
          return Text.htmlToText(bacinoDiPescaggio.substring(startElem, endElem));
        }
        else {
          log.error("XML structure NOT WELL-FORMED!");
          return "&#60;&#62;";
        }
      }
      else {
          log.error("XML structure NOT WELL-FORMED!");
        return "&#60;&#62;";
      }
    }
    else {
        log.error("element " + nameElement + " NOT FOUND!");
      return "&#60;&#62;";
    }
  }

  /**
  * Costruisce un file XML che pu� essere trattato con il parser legato alla libreria dom4j a partire da una porzione di
  * testo XML
  * @param rootContainer il nome dell'elemento radice dentro il quale viene incapsulata la porzione di testo XML in modo da
  * avere in ogni caso una struttura corretta
  * @param xmlBody la porzione di testo XML che voglio trattare come documento
  * @return un'istanza della classe Document (libreria dom4j)
  */
  public static org.dom4j.Document mountXML(String rootContainer, String xmlBody) throws org.dom4j.DocumentException {
    Document document = null;

    if ( xmlBody.indexOf("&#") != -1 ) {
      //per eliminare gli inconvenienti legati alla presenza dell'entity &#xd; RW26135
      xmlBody = xmlBody.replaceAll("&#xd;","");
      //RW20113
      xmlBody = xmlBody.replaceAll("&#","&amp;#");
    }

    if (xmlBody.startsWith("<?xml")) {
      xmlBody = xmlBody.substring(xmlBody.indexOf("?>") + 2);
    }

    // Federico 10/03/2005: aggiunto logging dell'xml da analizzare in caso di eccezione (RW0026955)
    try {
        if (!rootContainer.equals("")) {
            // Federico 01/04/2005: aggiunta dichiarazione del namespace 'h'
            document = DocumentHelper.parseText(XmlReplacer.addNamespacesToIU("<" + rootContainer + ">" + xmlBody + "</" + rootContainer + ">"));
        }
        else {
        document = DocumentHelper.parseText(xmlBody);
        }
    } catch (org.dom4j.DocumentException ex) {
        if (!rootContainer.equals("")) {
            log.error("Error parsing xml:\r\n\n" + XmlReplacer.addNamespacesToIU("<" + rootContainer + ">" + xmlBody + "</" + rootContainer + ">"));
        }
        else {
            log.error("Error parsing xml:\r\n\n" + xmlBody + "\r\n");
        }

        // notifico l'errore alla classe chiamante
        throw ex;
    }

    return document;
  }

  /**
  * Metodo che va chiamato dopo il corrispondente mountXML e restituisce come stringa la porzione di testo XML incapsulata
  * dentro l'elemento radice.
  * @param document il documento XML (classe Document della libreria dom4j) da cui estrarre come stringa il contenuto
  * @param rootContainer il nome dell'elemento radice (tipicamente quello usato dal mountXML per incapsulare il testo XML di
  * partenza)
  */
  public static String unmountXML(org.dom4j.Document document, String rootContainer)
  {
    StringWriter sw = new StringWriter();
    OutputFormat eidon = new OutputFormat("",false,"iso-8859-1");
    XMLWriter coreWriter = new XMLWriter(sw,eidon);
    try {
        coreWriter.write(document);
    }
    catch (IOException e) {
        log.error("caught exception:", e);
        return "";
    }
    sw.flush();
    String xmlCore = sw.toString();
    if ( xmlCore.indexOf("&amp;#") != -1 ) {
      /*RW20113*/xmlCore = xmlCore.replaceAll("&amp;#","&#");
    }
    if (!rootContainer.equals("")) {
      int startExtraction = xmlCore.indexOf("<" + rootContainer);
      if (startExtraction != -1) {
        startExtraction = xmlCore.indexOf(">", startExtraction + rootContainer.length()) + 1;
        int stopExtraction = xmlCore.indexOf("</" + rootContainer + ">");
        if (stopExtraction != -1) {
          xmlCore = xmlCore.substring(startExtraction, stopExtraction);
          return xmlCore;
        }
        else {
          log.error("end of container root NOT found");
          return new String("");
        }
      }
      else {
        log.error("container root NOT found");
        return new String("");
      }
    }
    else {
      if (xmlCore.trim().startsWith("<?xml"))
        xmlCore = xmlCore.substring(xmlCore.indexOf("?>") + 2);
      return xmlCore;
    }
  }

  /**
   * Metodo che, a partire dall'istanza di <code>Document</code> prodotta dal metodo
   * {@link it.highwaytech.apps.generic.XmlReplacer#mountXML} restituisce una stringa con il contenuto XML
   * completo, cio� tale da poter costituire un documento XML
   * @param document l'istanza di <code>Document</code> da convertire in stringa
   * @param rootContainer il nome dell'elemento radice usato da {@link it.highwaytech.apps.generic.XmlReplacer#mountXML}
   * per costruire il <code>Document</code>
   * @return una stringa con tutte le caratteristiche per essere un documento XML
   * @throws IOException
   * @throws UnsupportedEncodingException
   * @throws SAXException
   */
  public static String unmountXML4File(org.dom4j.Document document, String rootContainer) throws IOException,
                                                                                          UnsupportedEncodingException,
                                                                                          SAXException
  {
    String xmlCore = unmountXML(document,rootContainer);

    int c;
    for ( c = 0; c < xmlCore.length() && xmlCore.charAt(c) != '<'; c++ );
    xmlCore = xmlCore.substring(c);

    OutputFormat eidon = new OutputFormat("",false,"iso-8859-1");
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    XMLWriter xmlw = new XMLWriter((OutputStream)bos,eidon);
    xmlw.startDocument();
    xmlw.close();
    return bos.toString() + xmlCore;
  }

  /**
   * Metodo da usare dopo il {@link it.highwaytech.apps.generic.XmlReplacer#mountXML} per ottenere il vero nodo primario,
   * cio� quello che rappresenta l'information unit; il succitato metodo, infatti, riveste lo spezzone XML con un
   * document node fittizio che ha solamente una funzione di supporto per alcuni aspetti tecnici.
   * @param builtDoc il documento prodotto da {@link it.highwaytech.apps.generic.XmlReplacer#mountXML}
   * @return il nodo XML che rappresenta l'effettiva information unit
   * @throws DocumentException se non trova un nodo primario "vero"
   */
  public static Node getTruePrimaryNode(Document builtDoc) throws DocumentException
  {
    Element rootNode = (Element)builtDoc.selectSingleNode("child::*[1]");
    String rootName = rootNode.getName();
    int childrenSelector = 1;
    Node primaryNode = builtDoc.selectSingleNode("/" + rootName + "/*[" + String.valueOf(childrenSelector) + "]");
    try{
      while ( ((Namespace)primaryNode).getPrefix() != null )
        primaryNode = builtDoc.selectSingleNode("/" + rootName + "/*[" + String.valueOf(++childrenSelector) + "]");
    }
    catch(java.lang.ClassCastException cce){
      /*questa eccezione serve semplicemente a farmi capire che primaryNode non � un namespace*/
      /*e a farmi uscire dal ciclo while*******************************************************/
    }
    if ( primaryNode == null ) throw new DocumentException("Information Unit Element not found!");
    return primaryNode;
  }

  /**
   * Aggiunge alla pagina l'indicazione di una nuova funzionalit� disponibile, cio� inserisce nell'apposito elemento XML
   * (creandolo se non esiste) un attributo con il valore richiesto. Se tale attributo esiste gi�, viene sovrascritto
   * @param xmlTillNow il testo XML da modificare
   * @param nomeFunz il nome dell'attributo da inserire per indicare la nuova funzionalit� disponibile
   * @param valoreFunz il valore da assegnare all'attributo
   * @return il testo XML modificato; <code>null</code> se ci sono stati problemi nell'elaborazione del testo originale
   *
   * @deprecated usare {@link XMLDocumento#addFunctionality(String, String)}
   */
  public static String addFunctionality(String xmlTillNow,String nomeFunz,String valoreFunz)
  {
    //variabile che setta il nome dell'elemento che racchiude le funzionalit� disponibili
    String elementToFill = new String("funzionalita_disponibili");
    String result;
    if ( (result = XmlReplacer.addAttribute(xmlTillNow,elementToFill,nomeFunz,valoreFunz)) != null){
      return result;
    }
    String rootName = new String("DUMMY");
    try {
      Document withoutFunctionality = XmlReplacer.mountXML(rootName,xmlTillNow);
      Element father;
      List cercaRoot = withoutFunctionality.selectNodes("/" + rootName + "/response");
      if ( cercaRoot.isEmpty() ) father = withoutFunctionality.getRootElement();
      else father = ((Element)cercaRoot.get(0));
      Element newFunctionalityElement = ((Branch)father).addElement(elementToFill);
      newFunctionalityElement = newFunctionalityElement.addAttribute(nomeFunz,valoreFunz);
      return XmlReplacer.unmountXML(withoutFunctionality,rootName);
    }
    catch (DocumentException e) {
      log.error("", e);
      return null;
    }
  }


    /**
     * Restituisce una lista di elementi o attributi ricercati in base a xPath
     * @param xmlInput stringa che rappresenta il documento
     * @param xPath xpath da soddisfare
     * @return un oggetto List di Element o Attribute a seconda dell'xPath
     * @throws DocumentException
     */
    public static List getXPathValue(String xmlInput, String xPath) throws DocumentException
    {
        Document document = XmlReplacer.mountXML("DUMMY", xmlInput);

        if (xPath.charAt(0) != '/')
            xPath = "/DUMMY//" + xPath; //percorso relativo
        else
            xPath = "/DUMMY" + xPath; //percorso assoluto

        List l = document.selectNodes(xPath);
         return l;
     }

    /**
     * Restituisce una lista di elementi o attributi ricercati in base a xPath
     * @param alreadyDoc documento in cui cercare gli elementi
     * @param xPath xpath da soddisfare
     * @return un oggetto List di Element o Attribute a seconda dell'xPath
     * @throws DocumentException
     */
    public static List getXPathValue(Document alreadyDoc, String xPath) throws DocumentException
    {
      Document document = alreadyDoc;

      if (xPath.charAt(0) != '/')
        xPath = "/DUMMY//" + xPath; //percorso relativo
      else
        xPath = "/DUMMY" + xPath; //percorso assoluto

      List l = document.selectNodes(xPath);
      return l;
}
    /**
     * Stampa sullo stderr informazioni sull'uso della memoria a runtime
     *
     */
    public static void checkMemory()
    {
      log.debug("Free memory:" + Runtime.getRuntime().freeMemory()/1024 + "M");
      log.debug("Max memory: " + Runtime.getRuntime().maxMemory()/1024 + "M");
      log.debug("Total memory: " + Runtime.getRuntime().totalMemory()/1024 + "M");
    }

    /**
     * Stampa sullo stderr informazioni sull'uso della memoria a runtime insieme a un messaggio personalizzabile
     * @param message il messaggio che viene stampato subito prima delle informazioni sulla memoria
     */
    public static void checkMemory(String message)
    {
        log.debug(message);
        log.debug("Free memory:" + Runtime.getRuntime().freeMemory()/1024 + "M");
        log.debug("Max memory: " + Runtime.getRuntime().maxMemory()/1024 + "M");
        log.debug("Total memory: " + Runtime.getRuntime().totalMemory()/1024 + "M");
    }

    /**
     * Autore: simone - 9 Set 2004
     * Trasforma un numero intero compreso tra 0 e 30 (espersso come stringa) in un numero romano
     * @param num numero arabo in ingresso
     * @return numero romano corrispondente
     */
    public static String arabToRoman(String num)
    {
        if ( num.equals("0") ) return "0";
        else if ( num.equals("1") ) return "I";
        else if ( num.equals("2") ) return "II";
        else if ( num.equals("3") ) return "III";
        else if ( num.equals("4") ) return "IV";
        else if ( num.equals("5") ) return "V";
        else if ( num.equals("6") ) return "VI";
        else if ( num.equals("7") ) return "VII";
        else if ( num.equals("8") ) return "VIII";
        else if ( num.equals("9") ) return "IX";
        else if ( num.equals("10") ) return "X";
        else if ( num.equals("11") ) return "XI";
        else if ( num.equals("12") ) return "XII";
        else if ( num.equals("13") ) return "XIII";
        else if ( num.equals("14") ) return "XIV";
        else if ( num.equals("15") ) return "XV";
        else if ( num.equals("16") ) return "XVI";
        else if ( num.equals("17") ) return "XVII";
        else if ( num.equals("18") ) return "XVIII";
        else if ( num.equals("19") ) return "XIX";
        else if ( num.equals("20") ) return "XX";
        else if ( num.equals("21") ) return "XXI";
        else if ( num.equals("22") ) return "XXII";
        else if ( num.equals("23") ) return "XXIII";
        else if ( num.equals("24") ) return "XXIV";
        else if ( num.equals("25") ) return "XXV";
        else if ( num.equals("26") ) return "XXVI";
        else if ( num.equals("27") ) return "XXVII";
        else if ( num.equals("28") ) return "XXVIII";
        else if ( num.equals("29") ) return "XXIX";
        else if ( num.equals("30") ) return "XXX";

        return "";
    }

    /**
     * Formatta una data nel formato aaaammgg<br><br>
     * Autore: simone - 21 Set 2004<br><br>
     * Formati di ingresso riconosciuti:<br><br>
     *  gg?mm?aaaa con (? = qualsiasi carattere separatore)<br>
     *  aaaammgg<br>
     *  n.d.<br>
     *
     * @param s data in input
     * @return data formattata
     */
    public static String normalizeDate(String s)
    {
        // Federico 25/05/06: la data potrebbe anche non essere definita [RW 0037124]
        if (s == null) return "";

        if (s.length() == 0 || s.equalsIgnoreCase("n.d.") || s.length() == 8 )
            return s;
        else
            return s.substring(6, 10) + s.substring(3, 5) + s.substring(0, 2);
    }


    /**
     * Calcola il numero di giorni di differenza tra due date espresse in millisecondi dall'epoca
     * Autore: simone - 27 Set 2004
     * @param a prima data in millisencondi (rispetto all'epoca)
     * @param b seconda data in millisencondi (rispetto all'epoca)
     * @return numero di giorni di differenza
     */
    public static int diffDays(long a, long b) {
        long c = a-b;
        int x = Math.round(c/1000);
        x = x/3600;
        x = x/24;
        return x;
    }


    /**
     * Formatta una data nel formato aaaa-mm-gg
     * Autore: simone - 21 Set 2004
     * Formati di ingresso riconosciuti:
     *  gg?mm?aaaa con (? = qualsiasi carattere separatore)
     *  aaaammgg
     * @param s data in input
     * @return data formattata
     */
    public static String normalizeDateForAIPA(String s)
    {
        s = normalizeDate(s);
        return s.substring(0, 4) + "-" + s.substring(4, 6) + "-" + s.substring(6, 8);
    }

    /**
     * Effettua il replace di una sottostringa con un altra all'interno di una stringa.
     * <br><br>
     *
     * author 3D Informatica - mbussetti<br>
     *
     * @param source     Stringa su cui effettuare il replace.
     * @param what       Stringa da sostituire.
     * @param with       Stringa sostituto.
     * @param ignoreCase Se true, il case di 'source' e 'what' viene ignorato.
     *
     * @return La stringa di partenza eventualmente modificata.
     *
     */
    public static String replaceString(String source, String what, String with, boolean ignoreCase) {
        int index = ignoreCase? indexOfIgnoreCase(source, what) : source.indexOf(what);

        /* Federico 20/07/05: 'offset' deve essere pari a 'with.length()'. Infatti, se 'what' �
         * "federico" e 'with' � "viva federico", con 'offset' uguale a 1 la funzione va in loop
         * infinito!
         */
        //int offset = with.length() == 0 ? 0 : 1;
        int offset = with.length();

        while (index > -1) {
            source = source.substring(0, index) + with + source.substring(index + what.length());
            index = ignoreCase? indexOfIgnoreCase(source, what, index + offset) : source.indexOf(what, index + offset);
        }
        return source;
    }

    /**
     * Metodo analogo a 'indexOf' di String che ignora il case di 'source' e 'what'.
     * <br><br>
     *
     * author 3D Informatica - mbussetti<br>
     *
     * @param source     Stringa in cui effettuare la ricerca.
     * @param what       Stringa da cercare.
     *
     * @return L'indice della prima occorrenza di 'what' in 'source'; -1 se 'what' non viene trovata.
     *
     */
    public static int indexOfIgnoreCase(String source, String what) {
        return indexOfIgnoreCase(source, what, 0);
    }

    /**
     * Metodo analogo a 'indexOf' di String che ignora il case di 'source' e 'what'.
     * <br><br>
     *
     * author 3D Informatica - mbussetti<br>
     *
     * @param source     Stringa in cui effettuare la ricerca.
     * @param what       Stringa da cercare.
     * @param index      Posizione da cui partire per effettuare la ricerca.
     *
     * @return L'indice della prima occorrenza (a partire da 'index') di 'what' in 'source';
     *         -1 se 'what' non viene trovata.
     *
     */
    public static int indexOfIgnoreCase(String source, String what, int index) {
        source = source.toLowerCase();
        what = what.toLowerCase();
        return source.indexOf(what, index);
    }

    /**
     * Effettua il replace di una sottostringa con un altra all'interno di una stringa.
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     *
     * @param source     Stringa su cui effettuare il replace.
     * @param what       Stringa da sostituire.
     * @param with       Stringa sostituto.
     *
     * @return La stringa di partenza eventualmente modificata.
     *
     */
    public static String replaceString(String source, String what, String with) {
        return replaceString(source, what, with, false);
    }

    /**
     * Effettua il replace di una sottostringa con un altra all'interno di una stringa,
     * ignorando il case di 'source' e 'what'.
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     *
     * @param source     Stringa su cui effettuare il replace.
     * @param what       Stringa da sostituire.
     * @param with       Stringa sostituto.
     *
     * @return La stringa di partenza eventualmente modificata.
     *
     */
    public static String replaceStringIgnoreCase(String source, String what, String with) {
        return replaceString(source, what.toLowerCase(), with, true);
    }

    /**
     * Elimina da una stringa tutti i caratteri che non appartengono a un formato numerico
     * @param source la stringa di partenza
     * @return la stringa di partenza "ripulita" dai caratteri che non sono numerici o comunque relativi ad un formato
     * numerico. La stringa restituita � pronta per essere parsata e trasformata nel suo valore numerico
     */
    public static String getNumericFormat(String source){
        StringBuffer numericSource = new StringBuffer(source.length());
        for (int i = 0; i < source.length(); i++){
            char currentChar = source.charAt(i);
            if (Character.isDigit(currentChar)){
                numericSource = numericSource.append(currentChar);
            }
            //aggiunta di mbussetti del 30/03/2005: in Java non viene usata la virgola per separare la parte decimale,
            //bens� il punto; se viene trovata una virgola viene tirata una eccezione
            else if(currentChar == ','){
                numericSource = numericSource.append('.');
            }
        }
        return numericSource.toString();
    }

    /**
     * Questo metodo consente di sostituire tutte le occorrenze del carattere '&' presenti in source con la
     * corrispondente entity html ("&amp;"). La sostituzione viene effettuata solo se il carattere '&' non fa
     * parte di una entity nota (cio� presente nell'elenco contenuto in "htmlSpecialEntities").<br>
     * Per esempio, la stringa "&pippo; prova & &quot; &&& &#128;" viene trasformata nella stringa
     * "&amp;pippo; prova &amp; &quot; &amp;&amp;&amp; &#128;".
     *
     * <br><br>
     * author 3D Informatica - fgr
     * version 1.0
     *
     * @param source stringa da esaminare
     *
     * @return stringa "escapata"
     */
    public static String escapeAmpersand(String source)
    {
        int pos = -1;		// posizione del carattere '&'
        int posSemicolon;	// posizione del carattere ';'
        String entity;

        while ((pos = source.indexOf("&", pos + 1)) != -1) {
            if (source.charAt(pos + 1) == '#') {
                // incontrato codice numerico '&#...' --> vado avanti
                continue;
            }

            posSemicolon = source.indexOf(";", pos + 1);

            if (posSemicolon != -1) {
                // incontrato ';' --> esamino la stringa "&...;" per vedere se � un'entity nota
                entity = source.substring(pos, posSemicolon);

                if (htmlSpecialEntities.contains(entity + ";")) {
                    // la stringa "&...;" � un'entity nota --> vado avanti
                    continue;
                }
                else {
                    // la stringa "&...;" non � un'entity nota --> sostituisco '&' con "&amp;"
                    source = source.substring(0, pos) + "&amp;" + source.substring(pos + 1);
                }
            }
            else {
                // ';' non incontrato --> sostituisco '&' con "&amp;"
                source = source.substring(0, pos) + "&amp;" + source.substring(pos + 1);
            }
        }

        return source;
    }

    /**
     * Questo metodo consente di sostituire tutte le occorrenze del carattere '�' presenti in source con la
     * corrispondente entity individuata dalla stringa 'escapeString'.<br>
     * Il metodo esamina 'source' un byte alla volta e sostituisce con 'escapeString' tutti i byte il cui valore
     * � 128 (codice ASCII di '�').
     *
     * <br><br>
     * author 3D Informatica - fgr
     * version 1.0
     *
     * @param source stringa da esaminare
     * @param escapeString stringa che rappresenta l'entity con cui sostituire '�'
     *
     * @return stringa "escapata"
     */
    public static String escapeEuroSymbol(String source, String escapeString)
    {
        int pos = 0;	// posizione del carattere '�'

        for (; pos < source.length(); pos++) {
            if (((int)source.charAt(pos)) == 128) {
                // incontrato codice del simbolo '�'
                source = source.substring(0, pos) + escapeString + source.substring(pos + 1);

                pos += escapeString.length() - 1;
            }
        }

        return source;
    }

    /**
     * Restituisce la data odierna
     */
    public static String giorno() {
        SimpleDateFormat formatoGiorno = new SimpleDateFormat("yyyyMMdd");
        Date dataOdierna = new Date(System.currentTimeMillis());
        return formatoGiorno.format(dataOdierna);
    }

    /**
     * Restituisce l'ora attuale
     */
    public static String ora() {
        SimpleDateFormat formatoOra = new SimpleDateFormat("HH:mm:ss");
        Date dataOdierna = new Date(System.currentTimeMillis());
        return formatoOra.format(dataOdierna);
    }


    /**
     * Cancella i caratteri strani introdotti da gestione sbagliata di encoding
     * corrispondenti a '�'.
     * @param xml da modificare
     * @return xml modificato
     */
    public static String deleteCharTrash(String xml) {
        xml = xml.replaceAll("[���]\\?+[���]", "��");
        xml = xml.replaceAll("[���]\\?+[���]", "��");
        xml = xml.replaceAll("[���]", "");
        return xml;
    }

    /**
     * Restituisce il numero di millisecondi tra data1,ora1 e data2,ora2
     * @param data1 espressa in yyyymmgg
     * @param ora1 espressa hh:mm:ss
     * @param data2 espressa in yyyymmgg
     * @param ora2 espressa hh:mm:ss
     * @return tempo trascorso in millisecondi
     */
    public static long diffMillisec(String data1, String ora1, String data2, String ora2) {
        GregorianCalendar data1Cal = new GregorianCalendar();
        GregorianCalendar data2Cal = new GregorianCalendar();
        data1Cal.set(Integer.parseInt(data1.substring(0,4)),
                Integer.parseInt(data1.substring(4,6)),
                Integer.parseInt(data1.substring(6,8)),
                Integer.parseInt(ora1.substring(0,2)),
                Integer.parseInt(ora1.substring(3,5)),
                Integer.parseInt(ora1.substring(6,8)));
        data2Cal.set(Integer.parseInt(data2.substring(0,4)),
                Integer.parseInt(data2.substring(4,6)),
                Integer.parseInt(data2.substring(6,8)),
                Integer.parseInt(ora2.substring(0,2)),
                Integer.parseInt(ora2.substring(3,5)),
                Integer.parseInt(ora2.substring(6,8)));
        long elapsed = data1Cal.getTimeInMillis() - data2Cal.getTimeInMillis();
        return elapsed;
    }

    /**
     * Questo metodo inserisce i Node della lista nodes come figli dell'elemento elContainer.<br><br>
     *
     * author 3D Informatica - fgr<br>
     * version 1.0
     *
     * @param elContainer Element in cui vanno inseriti i nodi contenuti nella lista.
     * @param nodes Lista dei Node da inserire in elContainer.
     *
     * @return L'Element elContainer modificato.
     */
    public static Element addElements(Element elContainer, List nodes)
    {
        for (int j = 0; j < nodes.size(); j++) {
            elContainer.add(((Node)nodes.get(j)).detach());
        }

        return elContainer;
    }

    /**
     * Questo metodo rimuove i nodi della lista 'nodes' dal documento 'sourceDoc', esplorando i sottoalberi
     * che hanno come radice gli elementi con nome 'startElemName'.<br>
     * 'nodes' � una lista di stringhe che rappresentano gli xPath dei nodi da eliminare; tali percorsi devono
     * essere relativi agli elementi 'startElemName'.<br>
     * Se 'startElemName' � una stringa vuota, l'esplorazione parte dalla radice di 'sourceDoc'.
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     * version 1.0
     *
     * @param sourceDoc 	Documento sorgente da esaminare.
     * @param startElemName Nome degli elementi in sourceDoc che si vogliono esplorare. Se questa stringa �
     * 						vuota, l'esplorazione parte dalla radice di sourceDoc.
     * @param nodes 		Lista di stringhe che rappresentano gli xPath dei nodi da eliminare; questi percorsi
     * 						devono essere relativi a ogni elemento 'startElemName'.
     *
     * @return Il documento 'sourceDoc' modificato.
     */
    public static XMLDocumento filterNodes(XMLDocumento sourceDoc, String startElemName, List nodes)
    {
        if (startElemName.indexOf("@") != -1) {
            // l'xpath 'startElem' indica un attributo e non un elemento --> errore
            log.error("[XmlReplacer.filterNodes()] element \"" + startElemName + "\" is an attribute! Impossible to procede...");

            return sourceDoc;
        }

        List startElems = null; // radici dei sottoalberi da esplorare

        if (startElemName == "") {
            // si parte dalla radice del documento
            Object[] tmp = {sourceDoc.getRootElement()};
            startElems = Arrays.asList(tmp);
        }
        else {
            // cerco gli elementi 'startElemName' che rappresentano le radici dei sottoalberi da esplorare
            startElems = sourceDoc.getRootElement().selectNodes("//" + startElemName);
        }

        if (startElems.size() > 0) {
            log.trace("[XmlReplacer.filterNodes()] found " + startElems.size() + " elements to explore");

            // per ogni radice elimino gli elementi contenuti in elements
            for (int j = 0; j < startElems.size(); j++) {
                Node root = (Node)startElems.get(j);	// radice del sottoalbero da esplorare

                log.trace("[XmlReplacer.filterNodes()] exploring node \"" + root.getName() + "\"...");

                // ricerca dei nodi da eliminare in root
                for (int k = 0; k < nodes.size(); k++) {
                    String nodesXPath = (String)nodes.get(k);

                    List nodesToBeDeleted = root.selectNodes(nodesXPath);

                    log.trace("[XmlReplacer.filterNodes()] deleting " + nodesToBeDeleted.size() + " nodes in \""
                                + root.getName() + "\"... " + nodesToBeDeleted);

                    // eliminazione dei nodi trovati
                    for (int i = 0; i < nodesToBeDeleted.size(); i++) {
                        ((Node)nodesToBeDeleted.get(i)).detach();
                    }
                }
            }
        }
        else {
            // non � stato possibile trovare le radici dei sottoalberi da esplorare
            log.error("[XmlReplacer.filterNodes()] elements \"" + startElemName + "\" not found! Impossible to procede...");
        }

        return sourceDoc;
    }

    /**
     * Questo metodo rinomina tutti i nodi indicati da 'xPath' dando loro il nome 'newName'.
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     * version 1.0
     *
     * @param sourceDoc 	Documento sorgente da esaminare.
     * @param xPath 		xPath dei nodi da rinominare.
     * @param newName 		Nome da dare ai nodi da rinominare.
     *
     * @return Il documento 'sourceDoc' modificato.
     */
    public static XMLDocumento renameNodes(XMLDocumento sourceDoc, String xPath, String newName)
    {
        List nodesList = sourceDoc.selectNodes("//" + xPath);

        for (int j = 0; j < nodesList.size(); j++) {
            Node curNode = (Node)nodesList.get(j);

            if (curNode.getNodeTypeName().toLowerCase().equals("element")) {
                // il nodo corrente � un Element --> posso usare 'setName'
                curNode.setName(newName);
            }
            else if (curNode.getNodeTypeName().toLowerCase().equals("attribute")) {
                    // il nodo corrente � un Attribute --> non posso usare 'setName', se no rischio un'eccezione
                    curNode.getParent().addAttribute(newName, curNode.getText());	// creo un nuovo attributo
                    curNode.detach();												// elimino il vecchio attributo
                 }
        }

        return sourceDoc;
    }

    /**
     * Questo metodo calcola la somma dei val. contenuti nei nodi indicati dall'xpath 'xPath' partendo
     * dall'elemento radice 'root'.<br>
     * I valori incontrati possono essere numeri con virgola; il metodo effettua automaticamente la
     * sostituzione di eventuali ',' in '.'.
     * <br><br>
     * author 3D Informatica - fgr<br>
     * version 1.0
     *
     * @param root 	Elemento radice del sottoalbero da esplorare.
     * @param xPath	Xpath degli elementi da sommare.
     *
     * @return Il risultato della somma; 0 in caso di errore, cio� quando viene incontrato un valore non numerico.
     */
    public static float sumNodes(Element root, String xPath)
    {
        float totale = 0; // totale

        List nodesList = root.selectNodes(xPath);

        if (nodesList != null && nodesList.size() > 0) {
            log.trace("[XmlReplacer.sumNodes()] found " + nodesList.size() + " nodes");

            // somma dei valori di ogni nodo
            for (int j = 0; j < nodesList.size(); j++) {
                log.trace("[XmlReplacer.sumNodes()] exploring node " + j + "...");

                Node currNode = (Node)nodesList.get(j);

                float fieldValue;
                String strFieldValue = currNode.getText();

                if (strFieldValue != null && strFieldValue != "") {
                    try {
                        // sostituzione di eventuali ',' in '.'
                        strFieldValue = XmlReplacer.replaceString(strFieldValue, ",", ".");
                        fieldValue = Float.parseFloat(strFieldValue);

                        totale += fieldValue;
                    }
                    catch (NumberFormatException ex) {
                        log.error("[XmlReplacer.sumNodes()] the value of node '" + xPath +
                                    "' number " + j +
                                    " is not a number (value=" + strFieldValue + "). Exiting returning 0...");

                        return 0;
                    }
                }
            } // fine for sui nodi di 'nodesList'
        }

        return totale;
    }

    public static String fillChars(String s, String c, int n)
    {
        for ( int i = s.length(); i < n; i++ )
            s = c + s;
        return s;
    }

    /**
     * Aggiunge i namespace opportuni alla radice del documento xml indicato.
     *
     * @param xml Documento xml.
     *
     * @return Il documento modificato; null se il documento e' malformato (radice non chiusa).
     *
     */
    public static String addNamespacesToIU(String xml) {
        // Federico 29/05/07: occorre tenere presente dell'eventuale processing instruction
        // <?xml...?> [RW 0044527]
        int pos = 0;

        if (xml.startsWith("<?xml")) {
            pos = xml.indexOf("?>") + 2;
        }

        // Federico 27/05/09: prima del root element possono esserci commenti o processing instruction [M 0000452]
        for (; pos < xml.length(); pos++) {
            if (xml.charAt(pos) == '<' && (pos + 1) < xml.length() && xml.charAt(pos + 1) != '!' && xml.charAt(pos + 1) != '?') {
                break;
            }
        }

        if (pos == xml.length()) return null; // radice non trovata --> errore!

        pos = xml.indexOf(">", pos);

        if (pos > 0) {
            if (xml.charAt(pos - 1) == '/') pos = pos - 1; // self closed

            return xml.substring(0, pos) + nameSpaceString + xml.substring(pos);
        }
        else {
            // la radice non e' chiusa --> errore!
            return null;
        }
    }

    public static String removeNamespacesToIU(String xml) {
        /* Federico 21/12/05: � possibile che nell'xml siano presenti ns differenti da quelli dichiarati in
         * XMLDocumento --> cerco tutti i nostri ns indipendentemente da quello che viene indicato tra virgolette
         * [RW 0033030]
         */
        xml = xml.replaceAll("xmlns:xw=\"[^\"\r\n]*\"", "");
        xml = xml.replaceAll("xmlns:gml=\"[^\"\r\n]*\"", "");
        xml = xml.replaceAll("xmlns:h=\"[^\"\r\n]*\"", "");

        /*xml = xml.replaceAll("xmlns:xw=\"" + XMLDocumento.XW_NAMESPACE + "\"", "");
        xml = xml.replaceAll("xmlns:gml=\"" + XMLDocumento.GML_NAMESPACE + "\"", "");
        xml = xml.replaceAll("xmlns:h=\"" + XMLDocumento.H_NAMESPACE + "\"", "");*/

        return xml;
    }

    /**
     * Questo metodo rimuove le processing instruction figlie di 'el'.<br>
     * Se 'deepDelete' � true, vengono rimosse tutte le processing instruction presenti nel sottoalbero
     * di 'el'; se, invece, 'deepDelete' � false, vengono eliminate solo le processing instruction figlie di
     * 'el'.<br>
     * E' possibile anche indicare il nome delle processing instruction da eliminare mediante la var. 'name'.
     * <br><br>
     * author 3D Informatica - fgr<br>
     * version 1.0
     *
     * @param el         Elemento da esplorare.
     * @param name       Eventuale nome delle processing instruction da rimuovere. Se � null o vuota, vengono
     *                   cercate tutte le processing instruction indipendentemente dal nome.
     * @param deepDelete Indica se esplorare o meno tutto il sottoalbero con radice 'el'.
     *
     */
    public static void deleteProcessingInstructions(Element el, String name, boolean deepDelete)
    {
        String piName = (name != null && name.length() > 0) ? "'" + name + "'" : "";

        List piList;

        if (deepDelete) {
            // occorre esplorare il sottoalbero di 'el'
            piList = el.selectNodes("//processing-instruction(" + piName + ")");
        }
        else {
            // non occorre esplorare il sottoalbero di 'el'
            piList = el.selectNodes("./processing-instruction(" + piName + ")");
        }

        if (piList != null && piList.size() > 0) {
            log.trace("XmlReplacer.deleteProcessingInstructions(): found " + piList.size() +
                        " processing instructions to be deleted");

            for (int i = 0; i < piList.size(); i++) {
                ((Node)piList.get(i)).detach();
            }
        }
        else {
            log.trace("XmlReplacer.deleteProcessingInstructions(): processing instructions not found");
        }
    }

    /**
     * Somma a una data un numero di giorni
     * sstagni - 27 Lug 2005
     * @param data data espressa nel formato aaaammgg
     * @param days numero di giorni
     * @return data risultante
     */
    public static String sumDays(String data, int days) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Integer.parseInt(data.substring(0,4)), getCalendarMonthConstant(Integer.parseInt(data.substring(4,6))), Integer.parseInt(data.substring(6,8)));
        long daysInMillis = days * 1000 * 3600 * 24;
        calendar.setTimeInMillis(calendar.getTimeInMillis() + daysInMillis);
        return new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
    }

    /**
     * Questo metodo restituisce il vero pne del documento 'theDocument', che deve essere stato creato mediante
     * il metodo 'mountXML' di questa classe indicando una radice fittizia <u>NON VUOTA</u>.<br>
     * Nel caso in cui la determinazione del pne fallisca, il metodo restituisce il pne di default indicato
     * in 'defaultDocPneName'.<br><br>
     *
     * author 3D Informatica - fgr
     *
     * @param theDocument       Documento da esaminare.
     * @param defaultDocPneName pne di default da restituire in caso di errore.
     *
     * @return Il vero pne del documento 'theDocument'.
     *
     * @see #mountXML(String, String)
     */
    public static String getDocPneName(Document theDocument, String defaultDocPneName) {

        Node pne;

        try {
            pne = XmlReplacer.getTruePrimaryNode(theDocument);
            log.trace("XmlReplacer.getDocPneName(): doc's pne is \"" + pne.getName() + "\"");
            return pne.getName();
        }
        catch (Exception ex) {
            // errore nel determinare la pne --> uso un valore di default
            log.debug("XmlReplacer.getDocPneName(): error getting doc's pne. XML:\r\n" + theDocument.asXML() + "\n", ex);
            log.warn("XmlReplacer.getDocPneName(): returning default doc's pne \"" + defaultDocPneName + "\"...");
            return defaultDocPneName;
        }
    }

    /**
     * Restituisce l'indice dell'i-esimo token in s, -1 se non esiste
     * @param s stringa da analizzare
     * @param token token da trovare
     * @param num num-esima istanza da trovare
     * @return l'indice dell'i-esimo token in s, -1 se non esiste
     */
    public static int indexOfNum(String s, String token, int num) {
        int index = -1;
        for (int i=0; i<num; i++) {
            index = s.indexOf(token, index + 1);
            if (index == -1)
                return -1;
        }
        return index;
    }

    /**
     * Effettua il trim standard e in pi� rimuove eventuale newline in testa e in coda
     * @param s stringa di input
     * @return stringa senza spazi e newline in testa e in coda
     */
    public static String fullTrim(String s) {
        s = s.trim();
        if (s.startsWith("\n"))
            s = s.substring(1);
        if (s.endsWith("\n"))
            s = s.substring(0, s.length() - 1);
        return s;
    }

    /**
     * Questo metodo formatta la stringa numerica "importo" secondo il pattern: 123.456,78
     * (sempre con due cifre decimali).
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     *
     * @param importo Stringa numerica da formattare.
     *
     * @return La stringa numerica formattata o una stringa vuota se "importo" � null, vuoto o
     *         non � un numero.
     *
     */
    public static String formattaImporto(String importo) {
        // formattazione: 123.456,78 (sempre con due cifre decimali)
        return formattaImporto(importo, "###,###.00");
    }

    /**
     * Questo metodo formatta la stringa numerica "importo" secondo il pattern "pattern".
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     *
     * @param importo Stringa numerica da formattare.
     * @param pattern Pattern da utilizzare per la formattazione.
     *
     * @return La stringa numerica formattata o una stringa vuota se "importo" � null, vuoto o
     *         non � un numero.
     *
     */
    public static String formattaImporto(String importo, String pattern) {
        if (importo == null || importo.length() == 0) return "";

        importo = XmlReplacer.replaceString(importo, ",", ".");

        try {
            DecimalFormat formatter = new DecimalFormat(pattern);
            String res = formatter.format((new Double(importo)).doubleValue());
            return ((res.startsWith(",") || res.startsWith(".")) ? "0" + res : res);
        }
        catch (Exception ex) {
            log.error("JRXWUtils.formattaImporto(): got exception formatting: " + importo, ex);
            return "";
        }
    }

    /**
     * Questo metodo formatta un importo secondo il pattern: 123.456,78
     * (sempre con due cifre decimali).
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     *
     * @param d L'importo da formattare.
     *
     * @return La stringa numerica formattata.
     *
     */
    public static String formattaImporto(double d) {
        // formattazione: 123.456,78 (sempre con due cifre decimali)
        return formattaImporto(d, "###,###.00");
    }

    /**
     * Questo metodo formatta un importo secondo il pattern "pattern".
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     *
     * @param d       L'importo da formattare.
     * @param pattern Pattern da utilizzare per la formattazione.
     *
     * @return La stringa numerica formattata.
     *
     */
    public static String formattaImporto(double d, String pattern) {
        DecimalFormat formatter = new DecimalFormat(pattern);
        String res = formatter.format(d);

        return ((res.startsWith(",") || res.startsWith(".")) ? "0" + res : res);
    }

    /**
     * Questo metodo converte in lettere un importo in euro.<br>
     * Per esempio, l'importo "4.235.468,80" viene convertito in
     * "quattromilioniduecentotrentacinquemilaquattrocentosessantotto/80".<br>
     * Questo metodo consiste nel porting java della function javascript "digitation" in "isNumeric.js".
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     *
     * @param importo          L'importo da convertire.
     * @param decimalSeparator Separatore dei decimali usato.
     * @param decimalRequired  Se true e se non ci sono decimali, aggiunge "/00" alla fine.
     *
     * @return L'importo convertito in lettere o null se il numero da convertire e' troppo grande (si arriva fino al
     *         milione di miliardi).
     *
     * @throws Exception in caso di errore.
     *
     */
    public static String convertiImportoInLettere(String importo, String decimalSeparator, boolean decimalRequired) throws Exception {
        String[] pto1 = {"", "uno", "mille", "unmilione", "unmiliardo", "mille", "unmilione"};
        String[] pto2 = {"", "", "mila", "milioni", "miliardi", "mila", "milioni"};
        int i;

        for(i = 0; importo.charAt(i) == '0'; i++) ; // si posiziona sulla I cifra != 0

        int pt = importo.lastIndexOf(decimalSeparator);
        String parteIntera = importo.substring((i == pt) ? (i-1) : i, (pt < 0) ? importo.length() : pt);

        parteIntera = "000".substring(((parteIntera.length() % 3) > 0) ? parteIntera.length() % 3 : 3) + parteIntera;

        if (Integer.parseInt(parteIntera) == 0 && pt < 0) return "zero";

        String str1 = "", str2 = "";
        int j = parteIntera.length() / 3;

        if (j > 6) return null; // numero troppo grande

        for (i = 0; j > 0; --j, i += 3) {
            if(Integer.parseInt(parteIntera.substring(i, i + 3)) == 1) {
                str1 += pto1[j];
            }
            else if(Integer.parseInt(parteIntera.substring(i, i + 3)) == 0 && j == 4) {
                str1 += ((Integer.parseInt(parteIntera.substring(i - 3, i + 3)) == 0) ? "di" : "" ) + pto2[j];
            }
            else {
                str1 += digit3(parteIntera.substring(i, i + 3));

                if (Integer.parseInt(parteIntera.substring(i, i + 3)) > 0) str1 += pto2[j];
            }
        }

        // recupero la parte decimale
        if (pt != -1) {
            String parteDecimale = importo.substring(pt + 1);

            if (parteDecimale.length() == 0 && decimalRequired) parteDecimale = "00";
            else if (parteDecimale.length() == 1)               parteDecimale += "0";

            if (Integer.parseInt(parteDecimale) > 0) {
                str2 = ((str1.length() > 0) ? "/" : "") + parteDecimale;
            }
            else {
                // parte decimale nulla
                if (decimalRequired)         str2 = ((str1.length() > 0) ? "/" : "zero/") + "00";
                else if (str1.length() == 0) str2 = "zero";
            }
        }
        else if (decimalRequired) {
            // parte decimale richiesta anche se non presente
            str2 = ((str1.length() > 0) ? "/" : "zero/") + "00";
        }

        return str1 + str2;
    }

    /**
     * Metodo di supporto usato da 'convertiImportoInLettere(String, String, boolean)'.<br>
     * Questo metodo consiste nel porting java dell'omonima function javascript in "isNumeric.js".
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     *
     * @param num Numero di 3 cifre da convertire in lettere.
     *
     * @return Il numero convertito in lettere.
     *
     * @see #convertiImportoInLettere(String, String, boolean)
     *
     */
    private static String digit3(String num) {
        String[] n0_19 = {"", "uno", "due", "tre", "quattro", "cinque", "sei", "sette", "otto",
                          "nove", "dieci", "undici", "dodici", "tredici", "quattordici", "quindici",
                          "sedici", "diciassette", "diciotto", "diciannove"};
        String[] n10a  = {"", "", "venti", "trenta", "quaranta", "cinquanta", "sessanta", "settanta",
                          "ottanta", "novanta", "cento"};
        String[] n10b  = {"", "", "vent", "trent", "quarant", "cinquant", "sessant", "settant", "ottant",
                          "novant", "cento"};

        String str = "";

        if(num.charAt(0) != '0') str += ((num.charAt(0) != '1') ? n0_19[Integer.parseInt("" + num.charAt(0))] : "") + ((num.charAt(1) == '8') ? n10b[10] : n10a[10]);

        if(num.charAt(1) > '1') str += ((num.charAt(2) == '1' || num.charAt(2) == '8') ? n10b[Integer.parseInt("" + num.charAt(1))] : n10a[Integer.parseInt("" + num.charAt(1))]) + n0_19[Integer.parseInt("" + num.charAt(2))];
        else                    str += n0_19[Integer.parseInt(num.substring(1, 3))];

        return str;
    }

    /**
     * Sostituisce tutte le " presenti in 'value' con uno spazio e compatta la stringa cosi'
     * ottenuta sostituendo tutte le sequenze di 2 o piu' spazi con un solo spazio [RW 0040966].
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     *
     * @param value Valore da normalizzare.
     *
     * @return Il valore normalizzato.
     *
     */
    public static String normalizeSpacesAndQuotesForQuery(String value) {
        log.debug("XmlReplacer.normalizeSpacesAndQuotesForQuery(): normalizing string: '" + value + "'");

        while (value.startsWith("\"")) value = value.substring(1);

        while (value.endsWith("\""))   value = value.substring(0, value.length() - 1);

        value = replaceString(value, "\"", " ");

        int spacePos = 0;
        int spacesCounter, tmpPos;

        while ((spacePos = value.indexOf(" ", spacePos)) != -1) {
            spacesCounter = 1;
            tmpPos = spacePos + 1;

            while (tmpPos < value.length() && value.charAt(tmpPos) == ' ') {
                spacesCounter++;
                tmpPos++;
            }

            if (spacesCounter > 1) value = value.substring(0, spacePos) + " " +
                                           ((tmpPos < value.length()) ? value.substring(tmpPos) : "");

            spacePos++;
        }

        return value;
    }

    /**
     * Restituisce la costante 'Calendar.MESE' corrispondente
     * al numero di mese indicato.
     *
     * author 3D Informatica - fcossu<br>
     *
     * @param mese  Il numero del mese che si vuole sapere
     *
     * @return  La costante 'Calendar.MESE' se presente, -1 altrimenti.
     */
    public static int getCalendarMonthConstant(int mese) {
       int[] mesi = new int[12];

       mesi[0] = Calendar.JANUARY;
       mesi[1] = Calendar.FEBRUARY;
       mesi[2] = Calendar.MARCH;
       mesi[3] = Calendar.APRIL;
       mesi[4] = Calendar.MAY;
       mesi[5] = Calendar.JUNE;
       mesi[6] = Calendar.JULY;
       mesi[7] = Calendar.AUGUST;
       mesi[8] = Calendar.SEPTEMBER;
       mesi[9] = Calendar.OCTOBER;
       mesi[10] = Calendar.NOVEMBER;
       mesi[11] = Calendar.DECEMBER;

       return (mese <= 12  && mese > 0 ? mesi[mese - 1] : -1);
    }

    /**
     * Restituisce il numero di mese corrispondente
     * al 'Calendar.MESE' indicato.
     *
     * author 3D Informatica - fcossu<br>
     *
     * @param mese  Il 'Calendar.MESE' che si vuole tradurre in mese
     *
     * @return  Il numero di mese corrispondente a 'Calendar.MESE', -1 altrimenti.
     */
    public static int translateCalendarMonthConstant(int calMonth) {
        return (calMonth <= 11  && calMonth > 0 ? calMonth+1 : -1);
    }
}