/*
 * Created on 25-ott-2005
 *
 */
package it.highwaytech.apps.generic;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.io.DOMWriter;
import org.dom4j.io.DOMReader;
import java.io.Writer;
import it.highwaytech.apps.generic.utils.GenericUtils;

/**
 * @author SStagni
 *
 * Classe per la gestione di un documento XML
 *
 */
public class XMLDocumento implements Cloneable, Serializable {

	private static final long serialVersionUID = 7210083455606220946L;

	public static final String ENCODING = "iso-8859-1"; // encoding di output (xml formato stringa)

    public static final String XW_NAMESPACE = "http://www.3di.it/ns/xw-200303121136";

    public static final String GML_NAMESPACE = "http://www.opengis.net/gml";

    public static final String H_NAMESPACE = "http://www.w3.org/HTML/1998/html4";

    /**
     * Forza la rimozione dell'eventuale processing instruction <?xml...?> dal codice xml
     * passato (come stringa) al costruttore.
     *
     * @see #XMLDocumento(String, int, boolean)
     *
     */
    public static final int REMOVE_VER_ENC_DECLARATION = 0;

    /**
     * Indica di lasciare l'eventuale processing instruction <?xml...?> nel codice xml
     * passato (come stringa) al costruttore.
     *
     * @see #XMLDocumento(String, int, boolean)
     *
     */
    public static final int LEAVE_VER_ENC_DECLARATION = 1;

    /**
     * Forza l'aggiunta (se non c'e') della processing instruction <?xml...?> nel codice xml
     * passato (come stringa) al costruttore.
     *
     * @see #XMLDocumento(String, int, boolean)
     *
     */
    public static final int FORCE_VER_ENC_DECLARATION = 2;

    private Document document; //org.dom4j.Document - documento vero e proprio

    /**
     * Indica se la dichiarazione dell'encoding e' stata rimossa o meno dal codice xml in fase di creazione
     * di un'istanza della classe a partire da una stringa.
     *
     * @see #XMLDocumento(String)
     * @see #XMLDocumento(String, int, boolean)
     *
     */
    private boolean encodingDeclarationRemoved = false;

    private static it.highwaytech.util.Log log = it.highwaytech.util.LogFactory.getLogger(XMLDocumento.class);//logger

    /**
     * Restituisce la stringa dei namespace usati nei db extraway.
     * <br><br>
     *
     * author 3D Informatica - fgr
     *
     * @return La stringa dei namespace usati nei db extraway.
     *
     */
    public static String getNameSpacesString() {
        return "xmlns:xw=\"" + XW_NAMESPACE + "\" xmlns:gml=\"" + GML_NAMESPACE + "\" xmlns:h=\"" + H_NAMESPACE + "\"";
    }

    /**
     * Aggiunge al codice xml passato i namespace usati nei db extraway e, se manca, la
     * dichiarazione dell'encoding.
     * <br><br>
     *
     * author 3D Informatica - fgr
     *
     * @param xmlString Xml da aggiornare.
     *
     * @return Il codice xml aggiornato.
     *
     */
    public static String addNameSpacesAndEncDeclaration(String xmlString) {
        xmlString = XmlReplacer.removeNamespacesToIU(xmlString);

        if (!xmlString.startsWith("<?xml")) {
            // aggiunta della processing instruction <?xml...?>
            xmlString = "<?xml version=\"1.0\" encoding=\"" + ENCODING + "\"?>\n" + xmlString;
        }

        return XmlReplacer.addNamespacesToIU(xmlString);
    }

    /**
     * Costruttore da stringa.<br>
     * Vengono aggiunti i namespace opportuni e viene rimossa l'eventuale
     * processing instruction &lt;?xml...?&gt;.<br>
     * Inoltre, vengono effettuate le sostituzioni "&amp;#xd;" --&gt; "" e
     * "&#" --&gt; "&amp;amp;#".
     *
     * @param xmlString Documento xml di input sotto forma di stringa.
     *
     * @throws DocumentException in caso di errore.
     *
     * @see #XMLDocumento(String, int, boolean)
     *
     */
    public XMLDocumento(String xmlString) throws DocumentException {
        this(xmlString, REMOVE_VER_ENC_DECLARATION, false);
    }

    /**
     * Costruttore da stringa.<br>
     * Vengono aggiunti i namespace opportuni e, <u>se indicato</u>, viene rimossa l'eventuale
     * processing instruction &lt;?xml...?&gt; e vengono effettuate le sostituzioni "&amp;#xd;" --&gt; "" e
     * "&#" --&gt; "&amp;amp;#".
     *
     * @param xmlString                      Documento xml di input sotto forma di stringa.
     * @param xmlVerEncDeclarationManagement Indica come gestire la processing instruction
     *                                       <?xml...?> [RW 0044527].<br>
     *                                       Per le operazioni consentite si faccia riferimento
     *                                       alle variabili <i>*_VER_ENC_DECLARATION</i>.
     * @param doNotModifyEntities            Se true, fa in modo che le entity '&#' non vengano
     *                                       toccate; altrimenti vengono effettuate le sostituzioni
     *                                       "&amp;#xd;" --&gt; "" e "&#" --&gt; "&amp;amp;#".
     *
     * @throws DocumentException in caso di errore.
     *
     */
    public XMLDocumento(String xmlString, int xmlVerEncDeclarationManagement, boolean doNotModifyEntities) throws DocumentException {
        log.debug("XMLDocumento(String, int, boolean): xmlVerEncDeclarationManagement: " +
                  xmlVerEncDeclarationManagementToString(xmlVerEncDeclarationManagement));
        log.debug("XMLDocumento(String, int, boolean): doNotModifyEntities: " + doNotModifyEntities);

        try {
            xmlString = XmlReplacer.removeNamespacesToIU(xmlString);

            // Federico 30/05/07: resa opzionale l'elaborazione delle entity [RW 0044527]
            if (!doNotModifyEntities && xmlString.indexOf("&#") != -1) {//fix al documento
                //per eliminare gli inconvenienti legati alla presenza dell'entity &#xd; RW26135
                xmlString = xmlString.replaceAll("&#xd;", "");
                //RW20113
                xmlString = xmlString.replaceAll("&#", "&amp;#");
            }

            // Federico 29/05/07: introdotte gestioni alternative alla rimozione dell'eventuale
            // processing instruction <?xml...?> [RW 0044527]
            switch (xmlVerEncDeclarationManagement) {
                case REMOVE_VER_ENC_DECLARATION:
                    if (xmlString.startsWith("<?xml")) {
                        // rimozione della processing instruction <?xml...?>
                        xmlString = xmlString.substring(xmlString.indexOf("?>") + 2);
                        encodingDeclarationRemoved = true;
                    }

                    break;

                case FORCE_VER_ENC_DECLARATION:
                    if (!xmlString.startsWith("<?xml")) {
                        // aggiunta della processing instruction <?xml...?>
                        xmlString = "<?xml version=\"1.0\" encoding=\"" + ENCODING + "\"?>\n" + xmlString;
                    }
            }

            // Federico 02/07/08: il simbolo EURO va escapato per evitare che venga convertito in '?', non essendo
            // presente questo carattere nella codifica "iso-8859-1" [RW 0052875]
            xmlString = xmlString.replaceAll("(" + XmlReplacer.EURO_CODE_1 + "|" + XmlReplacer.EURO_CODE_2 + ")", XmlReplacer.EURO_HTML_ENTITY);

            xmlString = XmlReplacer.addNamespacesToIU(xmlString);
            document = DocumentHelper.parseText(xmlString);

            // Federico 08/09/08: eliminazione del testo estratto dagli allegati proveniente da Docway 2 [RW 0050152]
            checkTextInXWFiles();
        }
        catch (DocumentException e) {
            log.error("XMLDocumento(String, int, boolean): Error parsing xml:\r\n\n" + xmlString + "\r\n", e);
            throw e;
        }
    }

    /**
     * Converte in stringa il valore numerico dalle variabili <i>*_VER_ENC_DECLARATION</i>.
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     *
     * @param xmlVerEncDeclarationManagement Indica al costruttore <i>XMLDocumento(String, int, boolean)</i>
     *                                       come gestire la processing instruction <?xml...?> [RW 0044527].<br>
     *                                       Per le operazioni consentite si faccia riferimento
     *                                       alle variabili <i>*_VER_ENC_DECLARATION</i>.
     *
     * @return Il nome della variabile che corrisponde all'operazione indicata; <i>"unknown operation"</i> se
     *         l'operazione non e' riconosciuta.
     *
     */
    public static String xmlVerEncDeclarationManagementToString(int xmlVerEncDeclarationManagement) {
        switch (xmlVerEncDeclarationManagement) {
            case REMOVE_VER_ENC_DECLARATION:
                return "REMOVE_VER_ENC_DECLARATION";

            case FORCE_VER_ENC_DECLARATION:
                return "FORCE_VER_ENC_DECLARATION";

            case LEAVE_VER_ENC_DECLARATION:
                return "LEAVE_VER_ENC_DECLARATION";

            default: return "unknown operation";
        }
    }

    /**
     * Restituisce il documento xml sotto forma di stringa
     * Senza la header
     * @return il documento xml sotto forma di stringa
     */
    public String getStringDocument() {
        if (document == null)
            return "";
        StringWriter sw = new StringWriter();
        OutputFormat eidon = new OutputFormat("", false, ENCODING);
        XMLWriter coreWriter = new XMLWriter(sw, eidon);
        try {
            coreWriter.write(document);
        }
        catch (IOException e) {
            log.error("caught exception:", e);
            return "";
        }
        sw.flush();
        String xmlString = sw.toString();
        if (xmlString.indexOf("&amp;#") != -1) {
            xmlString = xmlString.replaceAll("&amp;#", "&#"); //RW20113
        }

        if (xmlString.trim().startsWith("<?xml")) //rimozione di eventuale header
            xmlString = xmlString.substring(xmlString.indexOf("?>") + 2);

        int index;

        if ((index = xmlString.indexOf("<DUMMY")) != -1) {
            xmlString = XmlReplacer.replaceString(xmlString, "</DUMMY>", "");
            xmlString = xmlString.substring(0, index) + xmlString.substring(xmlString.indexOf(">", index) + 1);
        }

        if ((index = xmlString.indexOf("<response")) != -1) {
            xmlString = XmlReplacer.replaceString(xmlString, "</response>", "");
            xmlString = xmlString.substring(0, index) + xmlString.substring(xmlString.indexOf(">", index) + 1);
        }

        xmlString = XmlReplacer.removeNamespacesToIU(xmlString);

        // Federico 02/07/08: il simbolo EURO va escapato per evitare che venga convertito in '?', non essendo
        // presente questo carattere nella codifica "iso-8859-1" [RW 0052875]
        xmlString = xmlString.replaceAll("(" + XmlReplacer.EURO_CODE_1 + "|" + XmlReplacer.EURO_CODE_2 + ")", XmlReplacer.EURO_HTML_ENTITY);

        return xmlString;
    }
    /**
     * ndrago 09/02/2006
     * Restituisce il documento xml sotto forma di stringa
     * Non rimuove la header ne il namespace
     * Si potrebbe parametrizzare il metodo precedente, ma non ho
     * nozioni sufficienti sul perch� vengono tolti header e ns
     * @return il documento xml sotto forma di stringa
     */
    public String getStringXMLDocument() {
        if (document == null) {
            return "";
        }

        StringWriter sw = new StringWriter();
        OutputFormat eidon = new OutputFormat("", false, ENCODING);
        XMLWriter coreWriter = new XMLWriter(sw, eidon);

        try {
            coreWriter.write(document);
        }
        catch (NullPointerException e) {
        	//TODO: da rimuovere una volta scoperto quale e' il valore null
        	log.error("Errore nullpointer in dom4j"); 
        	log.error("Documento xml ottenuto tramite asXML:" + document.asXML());
        	log.error("Documento xml ottenuto tramite toString:" + document.toString());
        	throw e;
        }
        catch (IOException e) {
            log.error("caught exception:", e);
            return "";
        }
        finally{
        	try {
				coreWriter.close();
			} catch (IOException e) {
				log.warn("XMLDocumento.getStringXMLDocument(): got exception while closing XMLWriter:", e);
			}
        }
        sw.flush();

        String xmlString = sw.toString();

        if (xmlString.indexOf("&amp;#") != -1) {
            xmlString = xmlString.replaceAll("&amp;#", "&#"); //RW 20113
        }

        int index = 0;

        if ((index = xmlString.indexOf("<DUMMY")) != -1) {
            xmlString = XmlReplacer.replaceString(xmlString, "</DUMMY>", "");
            xmlString = xmlString.substring(0, index) + xmlString.substring(xmlString.indexOf(">", index) + 1);
        }

        if ((index = xmlString.indexOf("<response")) != -1) {
            xmlString = XmlReplacer.replaceString(xmlString, "</response>", "");
            xmlString = xmlString.substring(0, index) + xmlString.substring(xmlString.indexOf(">", index) + 1);
        }

        // Federico 02/07/08: il simbolo EURO va escapato per evitare che venga convertito in '?', non essendo
        // presente questo carattere nella codifica "iso-8859-1" [RW 0052875]
        xmlString = xmlString.replaceAll("(" + XmlReplacer.EURO_CODE_1 + "|" + XmlReplacer.EURO_CODE_2 + ")", XmlReplacer.EURO_HTML_ENTITY);

        return xmlString;
    }

    /**
     * Restituisce il qualified name dell'element root, ovvero che definisce l'information unit
     * @return il qualified name dell'element root, ovvero che definisce l'information unit
     */
    public String getIUName() {
        return document.getRootElement().getQualifiedName();
    }

    /**
     * Restituisce il valore di un attributo della prima istanza di un elemento
     * @param xpath xpath dell'attributo da restiuire
     * @return valore dell'attributo ricercato, null se xpath non trovato
     */
    public String getAttributeValue(String xpath) {
        Attribute at = (Attribute) document.selectSingleNode(xpath);
        if (at == null)
            return null;
        else
            return at.getValue();
    }

    /**
     * Restituisce il valore di un attributo della prima istanza di un elemento
     * @param xpath xpath dell'attributo da restiuire
     * @return valore dell'attributo ricercato, default se xpath non trovato
     */
    public String getAttributeValue(String xpath, String defValue) {
        String val = getAttributeValue(xpath);
        if (val == null)
            return defValue;
        else
            return val;
    }

    /**
     * Restituisce i valori dei nodi individuati da <i>xpath</i> separandoli con il separatore indicato.<br>
     * Se l'xpath non produce alcun risultato viene restituito il valore di default [RW 0045059].
     * <br><br>
     *
     * author 3D Informatica - fgr
     *
     * @param xpath        Xpath dei nodi da cercare.
     * @param defValue     Valore di default da restituire se l'xpath non trova nulla.
     * @param valSeparator Separatore da utilizzare per separare i valori dei nodi trovati.
     *
     * @return I valori dei nodi trovati separati con <i>valSeparator</i>; <i>defValue</i> se l'xpath
     *         non trova nulla.
     *
     */
    public String getNodesValues(String xpath, String defValue, String valSeparator) {
        List nodes = document.selectNodes(xpath);

        if (nodes.size() == 0) {
            return defValue;
        }
        else {
            String result = "";

            for (int j = 0; j < nodes.size(); j++) {
                Node node = (Node)nodes.get(j);
                result += node.getText() + ((j < nodes.size() - 1) ? valSeparator : "");
            }

            return result;
        }
    }

    /**
     * Testa il valore di un attributo della prima istanza di un elemento.
     *
     * @param xpath xpath dell'attributo da testare
     * @param value valore da confrontare con quello dell'attributo trovato
     * @return true se attributo assume il valore richiesto, false altrimenti (attributo ha valore differente o assente)
     */
    public boolean testAttributeValue(String xpath, String value) {
        return testAttributeValue(xpath, value, false);
    }

    /**
     * Testa il valore di un attributo della prima istanza di un elemento ignorando il case dei valori
     * da confrontare.
     *
     * @param xpath xpath dell'attributo da testare
     * @param value valore da confrontare con quello dell'attributo trovato
     * @return true se attributo assume il valore richiesto, false altrimenti (attributo ha valore differente o assente)
     */
    public boolean testAttributeValueIgnoreCase(String xpath, String value) {
        return testAttributeValue(xpath, value, true);
    }

    /**
     * Testa il valore di un attributo della prima istanza di un elemento.
     *
     * @param xpath  xpath dell'attributo da testare
     * @param value  valore da confrontare con quello dell'attributo trovato
     * @param ignore indica se ignorare o meno il case dei valori da confrontare
     * @return true se attributo assume il valore richiesto, false altrimenti (attributo ha valore differente o assente)
     */
    public boolean testAttributeValue(String xpath, String value, boolean ignore) {
        String xpathVal = getAttributeValue(xpath);
        if (xpathVal == null)
            return false;
        else {
            if (ignore)
                return xpathVal.equalsIgnoreCase(value);
            else
                return xpathVal.equals(value);
        }
    }

    /**
     * Verifica se un certo xpath esiste nel documento xml
     * @param xpath xpath di cui si vuole sapere l'esistenza
     * @return true se xpath trovato, false altrimenti
     */
    public boolean isXPathFound(String xpath) {
        return document.selectSingleNode(xpath) == null ? false : true;
    }

    /**
     * Verifica se un certo xpath esiste nel documento xml e se il nodo trovato contiene
     * del testo (i whitespace non vengono considerati come testo valido).
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     *
     * @param xpath Xpath da controllare.
     *
     * @return True se xpath trovato e non vuoto; false altrimenti.
     *
     */
    public boolean isXPathFoundAndNotEmpty(String xpath) {
        Node n = document.selectSingleNode(xpath);

        if (n != null && n.getText().trim().length() > 0) return true;
        else                                              return  false;
    }

    /**
     * Restituisce la lista dei nodi corrispondenti a un xpath
     * @param xpath xpath
     * @return nodi trovati
     */
    public List selectNodes(String xpath) {
        return document.selectNodes(xpath);
    }

    /**
     * Restituisce la lista dei nodi corrispondenti a un xpath ordinati
     * @param xpath xpath
     * @return nodi trovati
     */
    public List selectNodes(String xpath, String ord) {
        return document.selectNodes(xpath, ord);
    }

    /**
     * Restituisce il nodo corrispondenti a un xpath
     * @param xpath xpath
     * @return nodo trovati
     */
    public Node selectSingleNode(String xpath) {
        return document.selectSingleNode(xpath);
    }

    /**
     * Restituisce il root element
     * @return il root element del documento
     */
    public Element getRootElement() {
        return document.getRootElement();
    }

    /**
     * Restituisce l'encoding del documento xml
     * @return encoding del documento xml
     */
    public String getXMLEncoding() {
        return document.getXMLEncoding();
    }

    /**
     * Questo metodo consente di inserire un elemento 'el' nel documento dato l'xpath di salvataggio (RW0027037).<br>
     * L'XPath deve essere riferito alla radice del documento (omettendola) e deve essere espresso mediante la dot
     * notation "&lt;nodo&gt;.&lt;nodo&gt;[x].@&lt;attr&gt;".<br>
     * Notare che nell'esempio � stato indicato anche il numero di istanza che interessa (notazione "[x]").<br>
     * L'inserimento avviene nel seguente modo:
     * <br><br>
     *
     * <ul>
     * <li>Viene creato l'elemento/attributo indicato dall'xPath. Qualora il percorso di salvataggio contenga elementi
     * che non esistono, essi vengono creati.</li>
     * <li>Il contenuto dell'elemento da inserire 'el' viene impostato come contenuto dell'elemento creato.</li>
     * </ul>
     * <br>
     * Se si richiede il salvataggio in un attributo e 'el' rappresenta un elemento (con eventuali figli), viene preso
     * solo il contenuto testuale di 'el'. <br>
     * Il salvataggio di elementi in elementi, invece, prevede la copia dell'intero sottoalbero con radice 'el'.
     * <br><br>
     *
     * author 3D Informatica - fgr <br>
     * version 1.0
     *
     * @param xPath XPath dell'elemento da creare, completo e riferito alla radice del documento che, quindi, va
     *              omessa.
     * @param el    Elemento da inserire.
     *
     */
    public void insertElement(String xPath, Node el) {
        String currentEl;
        int index, instPos, index2;
        Element elToInsert = null;
        Element theRoot = document.getRootElement();
        List l;

        Element lastSeenEl = theRoot;

        while ((index = xPath.indexOf(".")) != -1) {
            currentEl = xPath.substring(0, index);
            xPath = xPath.substring(index + 1); // elimino currentEl da xPath

            if (currentEl.endsWith("]")) {
                index2 = currentEl.indexOf("[");
                instPos = Integer.parseInt(currentEl.substring(index2 + 1, currentEl.indexOf("]")));
                currentEl = currentEl.substring(0, index2);
                instPos++;
            }
            else {
                instPos = 1;
            }

            //currentEl � il nome dell'elemento da inserire o da cui passare per la navigazione
            //instPos � il numero dell'istanza di currentEl

            l = lastSeenEl.selectNodes(currentEl);

            if (l.size() < instPos) {
                // currentEl va inserito
                for (int j = l.size(); j < instPos; j++) {
                    elToInsert = DocumentHelper.createElement(currentEl);
                    lastSeenEl.add(elToInsert);
                }
                lastSeenEl = elToInsert;
            }
            else {
                lastSeenEl = (Element) l.get(instPos - 1);
            }
        } // fine while

        //ora inserisco l'elemento el. xPath contiene il nome che deve avere l'elemento da inserire.
        boolean attrib = false; // indica se l'elemento da creare � un attributo o meno

        if (xPath.charAt(0) == '@') {
            xPath = xPath.substring(1);
            attrib = true;
        }

        String typeOfEl = el.getNodeTypeName(); // tipo del nodo el

        if (attrib) {
            // occorre creare un attributo
            lastSeenEl.add(DocumentHelper.createAttribute(lastSeenEl, xPath, el.getStringValue()));
        }
        else {
            // occorre creare un elemento
            Element newEl = DocumentHelper.createElement(xPath);
            lastSeenEl.add(newEl);

            if (typeOfEl.equalsIgnoreCase("attribute") || typeOfEl.equalsIgnoreCase("text")) {
                // el � un attributo o un nodo testo
                newEl.setText(el.getStringValue());
            }
            else {
                // el � un Element
                newEl.setContent(((Element) el).content());
            }
        }
    }

    /**
     * Questo metodo consente di recuperare un elemento dal documento dato il suo xpath (RW0027037).<br>
     * L'XPath deve essere riferito alla radice del documento (omettendola) e deve essere espresso mediante la
     * dot notation "&lt;nodo&gt;.&lt;nodo&gt;[x].@&lt;attr&gt;".<br>
     * Notare che nell'esempio � stato indicato anche il numero di istanza che interessa (notazione "[x]").
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     * version 1.0
     *
     * @param xPath XPath dell'elemento da cercare, completo e riferito alla radice del documento che, quindi, va
     *              omessa.
     *
     * @return L'elemento cercato o null se tale elemento non esiste.
     *
     * @see #extractElement(String)
     */
    public Node getElement(String xPath) {
        return findElement(xPath, false);
    }

    /**
     * Questo metodo consente di estrarre un elemento dal documento dato il suo xpath (RW0027037).<br>
     * L'XPath deve essere riferito alla radice del documento (omettendola) e deve essere espresso mediante la
     * dot notation "&lt;nodo&gt;.&lt;nodo&gt;[x].@&lt;attr&gt;".<br>
     * Notare che nell'esempio � stato indicato anche il numero di istanza che interessa (notazione "[x]").
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     * version 1.0
     *
     * @param xPath XPath dell'elemento da estrarre, completo e riferito alla radice del documento che, quindi, va
     *              omessa.
     *
     * @return L'elemento cercato o null se tale elemento non esiste. Notare che l'elemento restituito viene <b>tolto</b>
     *         dal documento.
     *
     * @see #getElement(String)
     */
    public Node extractElement(String xPath) {
        return findElement(xPath, true);
    }

    /**
     * Questo metodo consente di cercare/estrarre un elemento dal documento dato il suo xpath (RW0027037).<br>
     * L'XPath deve essere riferito alla radice del documento (omettendola) e deve essere espresso mediante la
     * dot notation "&lt;nodo&gt;.&lt;nodo&gt;[x].@&lt;attr&gt;".<br>
     * Notare che nell'esempio � stato indicato anche il numero di istanza che interessa (notazione "[x]").
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     * version 1.0
     *
     * @param xPath          XPath dell'elemento da cercare/estrarre, completo e riferito alla radice del documento che, quindi, va
     *                       omessa.
     * @param extractElement Indica se estrarre o meno dal documento l'elemento indicato da 'xPath'.
     *
     * @return L'elemento cercato o null se tale elemento non esiste. Notare che se 'extractElement' � true,
     *         l'elemento restituito viene <b>tolto</b> dal documento.
     *
     * @see #getElement(String)
     * @see #extractElement(String)
     */
    public Node findElement(String xPath, boolean extractElement) {
        String currentEl;
        int index, instPos, index2;
        Element theRoot = document.getRootElement();
        List l;

        Node lastSeenEl = theRoot;
        xPath += ".";               // per iterare su ogni elemento di xPath (compreso l'ultimo)

        while ((index = xPath.indexOf(".")) != -1) {
            currentEl = xPath.substring(0, index);
            xPath = xPath.substring(index + 1);     // elimino currentEl da xPath

            if (currentEl.endsWith("]")) {
                index2 = currentEl.indexOf("[");
                instPos = Integer.parseInt(currentEl.substring(index2 + 1, currentEl.indexOf("]")));
                currentEl = currentEl.substring(0, index2);
                instPos++;
            }
            else {
                instPos = 1;
            }

            //currentEl � il nome dell'elemento da cui passare per la navigazione
            //instPos � il numero dell'istanza di currentEl

            l = lastSeenEl.selectNodes(currentEl);

            if (l.size() >= instPos) {
                lastSeenEl = (Node)l.get(instPos - 1);
            }
            else {
                // l.size() < instPos --> l'elemento cercato non esiste
                return null;
            }
        } // fine while

        // restituisco l'elemento cercato
        if (extractElement) {
            // � richiesta l'estrazione dal documento dell'elemento cercato
            return lastSeenEl.detach();
        }
        else {
            // restituisco l'elemento cercato senza estrarlo dal documento
            return lastSeenEl;
        }
    }

    /**
     * Rimuove i nodi identificati da un xpath (se esistono).
     *
     * @param xpath xpath dei nodi da eliminare.
     *
     */
    public void removeXPath(String xpath) {
        List l = document.selectNodes(xpath);

        for (int i = 0; i < l.size(); i++) ((Node) l.get(i)).detach();
    }

    /**
     * Costruttore che crea un document con radice root.
     * <br><br>
     *
     * author 3DInformatica - ss<br>
     *
     * @param root Radice del documento xml.
     *
     */
    public XMLDocumento(Element root) {
        root.addNamespace("xw", XMLDocumento.XW_NAMESPACE);
        root.addNamespace("gml", XMLDocumento.GML_NAMESPACE);
        root.addNamespace("h", XMLDocumento.H_NAMESPACE);
        document = DocumentHelper.createDocument(root);

        // Federico 08/09/08: eliminazione del testo estratto dagli allegati proveniente da Docway 2 [RW 0050152]
        checkTextInXWFiles();
    }

    /**
     * Costruttore che crea un XMLDocumento a partire da un document dom4j.
     * <br><br>
     *
     * author 3DInformatica - ss<br>
     *
     * @param doc Documento dom4j con cui costruire l'XMLDocumento.
     *
     */
    public XMLDocumento(Document doc) {
        // Federico 09/07/09: spostato codice nel costruttore 'XMLDocumento(Document, boolean)' [M 0000498]
        this(doc, true);
    }

    /**
     * Costruttore che crea un XMLDocumento a partire da un document dom4j.
     * <br><br>
     *
     * author 3DInformatica - ss<br>
     *
     * @param doc                Documento dom4j con cui costruire l'XMLDocumento.
     * @param checkTextInXWFiles Se <code>true</code>, comporta l'eliminazione del testo nei nodi xw:file
     *                           (testo estratto in dw 2) con l'aggiunta dell'attributo "convert=yes"
     *                           per forzare la conversione tramite FCS.
     *
     */
    public XMLDocumento(Document doc, boolean checkTextInXWFiles) {
        document = doc;

        // Federico 08/09/08: eliminazione del testo estratto dagli allegati proveniente da Docway 2 [RW 0050152]
        if (checkTextInXWFiles) checkTextInXWFiles();
    }

    /**
     * Costruttore che crea un XMLDocumento a partire da un oggetto org.w3c.dom.Document.
     * <br><br>
     *
     * author 3DInformatica - fgr<br>
     *
     * @param doc Oggetto org.w3c.dom.Document con cui costruire l'XMLDocumento.
     *
     */
    public XMLDocumento(org.w3c.dom.Document doc) {
        setW3CDocument(doc);

        // Federico 08/09/08: eliminazione del testo estratto dagli allegati proveniente da Docway 2 [RW 0050152]
        checkTextInXWFiles();
    }

    /**
     * Aggiunge all'elemento funzionalita_disponibili un attributo
     * @param attrName nome dell'attributo da aggiungere
     * @param attrVal valore dell'attributo da aggiungere
     */
    public void addFunctionality(String attrName, String attrVal) {
        Element el = document.getRootElement().element("funzionalita_disponibili");
        if (el == null) {
            el = DocumentHelper.createElement("funzionalita_disponibili");
            document.getRootElement().add(el);
        }
        el.addAttribute(attrName, attrVal);
    }

    /**
     * clone method
     */
    public Object clone() throws CloneNotSupportedException {
        return new XMLDocumento((Document) document.clone());
    }

    /**
     * Aggiunge al document un altro documento.<br>
     * Se l'elemento radice di 'doc' � "DUMMY" o "response", esso viene eliminato, nel senso che non viene
     * aggiunto al document.
     * <br><br>
     *
     * <b>ATTENZIONE:</b> 'doc' viene svuotato, dato che i suoi elementi devono essere aggiunti al
     * document di questa classe!
     *
     * @param doc Documento da aggiungere al document di questa classe.
     *
     */
    public void addDocument(XMLDocumento doc) {
        if (doc != null) {
            Element el = doc.getRootElement();
            if (doc.getIUName().equals("DUMMY") || doc.getIUName().equals("response")) {
                List l = el.elements();
                for (int i = 0; i < l.size(); i++) {
                    el = (Element) l.get(i);
                    el.detach();
                    document.getRootElement().add(el);
                }
            }
            else {
                el.detach();
                document.getRootElement().add(el);
            }
        }
    }

    /**
     * Inserisce un xpath espresso nella nostra dot notation a partire dalla radice del documento.<br>
     * Il percorso xml <b>non</b> deve cominciare per '.' e <b>non</b> deve contenere l'elemento radice
     * (<i>'.' iniziale e radice vengono automaticamente rimossi</i>).<br>
     * Se viene indicato un xpath canonico, tutti i caratteri '/' vengono sostituiti da '.'.<br>
     * Esempi di path:<br><br>
     *
     * pippo.pluto<br>
     * pippo.pluto[2].paperino<br>
     * pippo.pluto.&#64;nome -> attr.nome di pluto<br>
     * pippo.pluto.$cdata -> nodo cdata di pluto<br>
     * pippo.pluto.$fragment -> frammento xml<br><br>
     *
     * In caso di inserimento di elementi multistanza con buchi nella numerazione, vengono
     * inseriti anche gli elementi corrispondenti alle instanze mancanti. Per esempio,
     * inserendo in sequenza<br><br>
     *
     * pippo.pluto[0]<br>
     * pippo.pluto[2]<br><br>
     *
     * vengono inseriti all'interno dell'elemento "pippo" 3 elementi "pluto".
     * <br><br>
     *
     * <b>NOTA</b>: Si supponga di usare 2 xpath in sequenza del tipo "pippo.pluto.@A" e
     *              "pippo.pluto.@B". Se esiste gia' nel documento un elemento "/root/pippo/pluto"
     *              con attributi "A" e "B", allora gli xpath usati comportano la <u>sovrascrittura</u>
     *              dei valori degli attributi "A" e "B" dell'elemento "pluto" esistente.
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     *
     * @param path   Xpath.
     * @param value  Valore.
     *
     * @throws Exception in caso di errori sintattici nel xpath o se 'value' e' un frammento xml malformato.
     *
     */
    public Node insertXPath(String path, String value) throws Exception {
        return insertXPath(path, value, null);
    }

    /**
     * Inserisce un xpath espresso nella nostra dot notation a partire da un nodo radice.<br>
     * Se la radice non viene indicata ('rootEl == null'), viene presa la radice del documento.<br>
     * Il percorso xml <b>non</b> deve cominciare per '.' e <b>non</b> deve contenere l'elemento radice
     * (<i>'.' iniziale e radice vengono automaticamente rimossi</i>).<br>
     * Se viene indicato un xpath canonico, tutti i caratteri '/' vengono sostituiti da '.'.<br>
     * Esempi di path:<br><br>
     *
     * pippo.pluto<br>
     * pippo.pluto[2].paperino<br>
     * pippo.pluto.&#64;nome -> attr.nome di pluto<br>
     * pippo.pluto.$cdata -> nodo cdata di pluto<br>
     * pippo.pluto.$fragment -> frammento xml<br><br>
     *
     * In caso di inserimento di elementi multistanza con buchi nella numerazione, vengono
     * inseriti anche gli elementi corrispondenti alle instanze mancanti. Per esempio,
     * inserendo in sequenza<br><br>
     *
     * pippo.pluto[0]<br>
     * pippo.pluto[2]<br><br>
     *
     * vengono inseriti all'interno dell'elemento "pippo" 3 elementi "pluto".
     * <br><br>
     *
     * <b>NOTA</b>: Si supponga di usare 2 xpath in sequenza del tipo "pippo.pluto.@A" e
     *              "pippo.pluto.@B". Se esiste gia' nel documento un elemento "/root/pippo/pluto"
     *              con attributi "A" e "B", allora gli xpath usati comportano la <u>sovrascrittura</u>
     *              dei valori degli attributi "A" e "B" dell'elemento "pluto" esistente.
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     *
     * @param path   Xpath.
     * @param value  Valore.
     * @param rootEl Elemento in cui si vuol fare l'inserimento.
     *
     * @throws Exception in caso di errori sintattici nel xpath o se 'value' e' un frammento xml malformato.
     *
     */
    public Node insertXPath(String path, String value, Element rootEl) throws Exception {
        // Federico 01/02/07: migliorati controlli sul xpath indicato [RW 0042023]
        if (path == null || path.length() == 0) throw new Exception("Null or empty xpath!");

        // fcossu 28/06/2006
        String elementName;
        Element node = null;

        if (rootEl == null) {
            node = document.getRootElement();
        }
        else {
            node = rootEl;
        }

        // Federico 01/02/07: migliorati controlli sul xpath indicato [RW 0042023]
        String rootName = node.getQualifiedName();

        log.trace("XMLDocumento.insertXPath(): root node: " + rootName);
        log.trace("XMLDocumento.insertXPath(): original xpath: " + path);

        path = XmlReplacer.replaceString(path, "/", ".");

        if (path.startsWith(".")) path = path.substring(1);

        if (path.startsWith(rootName + ".")) {
            int rootEnd = path.indexOf(".");

            if (rootEnd == -1) {
                // e' stato indicato nel path solo il root element --> non so cosa inserire --> errore sintattico
                throw new Exception("The xpath contains only the root element. It must contain also what must be inserted!");
            }

            path = path.substring(rootEnd + 1);
        }

        log.trace("XMLDocumento.insertXPath(): final xpath: " + path);
        log.trace("XMLDocumento.insertXPath(): value: " + value);

        int pos, lastPos = 0, childPos, j, k, elementIndex;

        for (;;) {
            pos = path.indexOf('.', lastPos);
            elementName = path.substring(lastPos, pos < 0 ? path.length() : pos);

            if (elementName.charAt(0) == '@') {//richiesta la valorizzazione di un attributo
                String name = path.substring(lastPos + 1);
                Attribute at;
                if (name.equals("xml:space") && (at = node.attribute("space")) != null)
                    node.remove(at);
                node.addAttribute(name, value);
                return node.attribute(name);
            }

            if (elementName.charAt(0) == '$') {
                if (elementName.equals("$cdata"))
                    node.addCDATA(value);
                else if (elementName.equals("$fragment"))
                    writeFragment(node, value);
                else
                    throw new Exception("Unknown syntax '" + elementName + "'");
                return node;
            }

            k = elementName.indexOf('[');

            if (k >= 0) {
                int stop = elementName.indexOf(']', k);
                childPos = Integer.parseInt(elementName.substring(k + 1, stop));
                elementName = elementName.substring(0, k);
            }
            else {
                childPos = 0;
            }

            elementIndex = 0;
            Element nextNode = null;
            List l = node.elements();

            if (l.size() > 0) {
                for (j = 0; j < l.size(); j++) {
                    nextNode = (Element) l.get(j);
                    if ((k == 0 || elementName.equals(nextNode.getName())) && elementIndex++ == childPos)
                        break;
                }
            }

            if (k == 0) {//nodo testo
                if (pos > 0 || elementIndex < childPos) {
                    log.error("pos=" + pos + " elementIndex=" + elementIndex + " childPos=" + childPos);
                    throw new Exception("Bad index or position for text node '" + path + "'");
                }

                if (nextNode == null) {
                    node.setText(value);
                    nextNode = node;
                }
                else {
                    nextNode.setText(value);
                }

                value = null;
                break;
            }

            for (j = elementIndex; j <= childPos; j++) {
                nextNode = DocumentHelper.createElement(elementName);
                node.add(nextNode);
            }

            node = nextNode;

            if (pos < 0) break;

            lastPos = pos + 1;
        }

        if (value != null/* &&value.length()>0 */) node.setText(value);
        
        return node;
    }

    /**
     * Attacca a un element un fragment xml
     * @param node elemento a cui appendere il fragment
     * @param fragment fragment
     * @throws DocumentException
     */
    public static void writeFragment(Element node, String fragment) throws DocumentException {
        XMLDocumento document = new XMLDocumento("<DUMMY>" + fragment + "</DUMMY>");
        List l = node.elements();
        Element el;

        // ndrago 25/01/2006 - 20060125
        // Aggiunto ciclo di cancellazione - in Spring XML vengono usati i campi note.$fragment
        // salvando con la vecchia procedura venivano duplicate le note, il clico serve a cancellare
        // gli element del nodo (nel caso specifico di Spring � in campo NOTE)
        for (int i = 0; i < l.size(); i++) {
            el = (Element) l.get(i);
            el.detach();
        }

        // ndrago 31/01/2006 - 20060131
        // Non inseriva nulla se era solo testo, estraggo il txt dal doc creato e lo
        // inserisco nel nodo
        node.setText(document.getRootElement().getText());
        l = document.getRootElement().elements();

        for (int i = 0; i < l.size(); i++) {
            el = (Element) l.get(i);
            el.detach();
            node.add(el);
        }
    }

    /**
     * Questo metodo restituisce l'oggetto Document contenuto in questa classe.
     * <br><br>
     *
     * author 3D Informatica - ss<br>
     *
     * @return L'oggetto Document contenuto in questa classe.
     *
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Questo metodo restituisce una <b>copia</b> dell'oggetto Document contenuto in questa classe.
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     * version 1.0
     *
     * @return Una <b>copia</b> dell'oggetto Document contenuto in questa classe.
     *
     */
    public Document getDocumentCopy() {

        return (Document)document.clone();
    }

    /**
     * Questo metodo restituisce una <b>copia</b> dell'oggetto Document contenuto in questa classe, aggiungendo
     * la radice indicata.<br><br>
     *
     * <b>NOTA:</b> la vecchia radice diventa figlia della nuova.
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     *
     * @param rootName Nome della nuova radice.
     *
     * @return Una <b>copia</b> dell'oggetto Document contenuto in questa classe con la radice indicata;
     *         null se 'rootName' e' null o vuoto.
     *
     */
    public Document getDocumentCopyWithNewRoot(String rootName) {
        if (rootName == null || rootName.length() == 0) return null;

        Document copy = (Document)document.clone();
        Element oldRoot = (Element)copy.getRootElement().detach();
        Element newRoot = DocumentHelper.createElement(rootName);
        newRoot.add(oldRoot);
        copy.setRootElement(newRoot);

        return copy;
    }

    /**
     * Questo metodo consente di impostare l'oggetto Document contenuto in questa classe.
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     * version 1.0
     *
     * @param doc Un oggetto Document da immagazzinare nella classe.
     *
     */
    public void setDocument(Document doc) {

        document = doc;
    }

    /**
     * Questo metodo consente di impostare l'oggetto Document contenuto in questa classe dato un
     * oggetto Document <b>con una radice fittizia (quindi da eliminare)</b>.<br>
     * La radice fittizia deve avere un solo elemento figlio, che rappresenta la nuova radice
     * del documento impostato.
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     *
     * @param doc Un oggetto Document con una radice fittizia (quindi da eliminare) da
     *            immagazzinare nella classe.
     *
     * @throws Exception qualora la radice fittizia non abbia un solo figlio.
     *
     */
    public void setDocumentWithDummyRoot(Document doc) throws Exception {
        List rootChildren = doc.getRootElement().elements();

        if (rootChildren.size() != 1) throw new Exception("Dummy root MUST have only one child!");

        Document copy = (Document)doc.clone();
        copy.getRootElement().detach();
        copy.setRootElement((Element)((Element)rootChildren.get(0)).detach());
        document = copy;
    }

    //TODO - serve solo per il workflow - Andr� rimosso
    //differisce da 'getStringDocument' in quanto non rimuove i namespace
    public String getStringDocumentFORWF(boolean remove) {
        if (document == null)
            return "";
        StringWriter sw = new StringWriter();
        OutputFormat eidon = new OutputFormat("", false, ENCODING);
        XMLWriter coreWriter = new XMLWriter(sw, eidon);
        try {
            coreWriter.write(document);
        }
        catch (IOException e) {
            log.error("caught exception:", e);
            return "";
        }
        sw.flush();
        String xmlString = sw.toString();
        if (xmlString.indexOf("&amp;#") != -1) {
            xmlString = xmlString.replaceAll("&amp;#", "&#"); //RW20113
        }

        if (xmlString.trim().startsWith("<?xml")) //rimozione di eventuale header
            xmlString = xmlString.substring(xmlString.indexOf("?>") + 2);

        int index;
        if ((index = xmlString.indexOf("<DUMMY")) != -1) {
            xmlString = XmlReplacer.replaceString(xmlString, "</DUMMY>", "");
            xmlString = xmlString.substring(0, index) + xmlString.substring(xmlString.indexOf(">", index) + 1);
        }

        if ((index = xmlString.indexOf("<response")) != -1) {
            xmlString = XmlReplacer.replaceString(xmlString, "</response>", "");
            xmlString = xmlString.substring(0, index) + xmlString.substring(xmlString.indexOf(">", index) + 1);
        }

        xmlString = XmlReplacer.removeNamespacesToIU(xmlString);

        // Federico 02/07/08: il simbolo EURO va escapato per evitare che venga convertito in '?', non essendo
        // presente questo carattere nella codifica "iso-8859-1" [RW 0052875]
        xmlString = xmlString.replaceAll("(" + XmlReplacer.EURO_CODE_1 + "|" + XmlReplacer.EURO_CODE_2 + ")", XmlReplacer.EURO_HTML_ENTITY);

        return xmlString;
    }

    /**
     * Aggiunge alla root l'elemento dal nome passato e valorizza l'attributo val con il valore passato
     * @author DD - 3D Informatica
     * @serialData 20060428
     * @param attrName nome dell'attributo da aggiungere
     * @param attrVal valore
     */
    public void createElementWithVal(String attrName, String attrVal) {
        Element el1 = DocumentHelper.createElement(attrName);
        el1.addAttribute("val", attrVal);
        document.getRootElement().add(el1);
    }

    /**
     * Restituisce il testo di un elemento
     * @param xpath xpath dell'elemento da restiuire
     * @return valore del text ricercato, null se xpath non trovato
     */
    public String getElementText(String xpath) {
        Element el = (Element) document.selectSingleNode(xpath);
        if (el == null)
            return null;
        else
            return el.getText();
    }

    /**
     * Restituisce il testo di un elemento
     * @param xpath xpath dell'elemento da restiuire
     * @param defValue valore di default
     * @return valore dell'elemento ricercato, 'defValue' se xpath non trovato
     */
    public String getElementText(String xpath, String defValue) {
        String val = getElementText(xpath);
        if (val == null || val.length() == 0)
            return defValue;
        else
            return val;
    }

    /**
     * Questo metodo prepara un test xpath per un attributo/elemento [RW 0038946].<br>
     * Supponiamo, per esempio, di dover effettuare la seguente ricerca:<br><br>
     *
     * doc.selectSingleNode("//xw:file[&#64;title='" + variableString + "']")<br><br>
     *
     * Se 'variableString' contiene entrambi i caratteri ' e ", la select lancia un'eccezione a causa della
     * presenza di ' nel xpath. Il problema non e' risolvibile usando " come delimitatore della stringa del
     * test su &#64;title, dato che si otterrebbe nuovamente un'eccezione dovuta alla presenza di " nella
     * variabile.<br>
     * In questo caso ha senso utilizzare questo metodo per ottenere un test valido per l'xpath. In altri
     * termini la select diventa:<br><br>
     *
     * doc.selectSingleNode("//xw:file[&#64;title=" + XMLDocumento.prepareTest4Xpath(variableString) + "]")<br><br>
     *
     * (<b>notare l'assenza di ' o " nel test &#64;title=...</b>).<br>
     * Il metodo restituisce il valore di 'testValue' tra ' se esso non contiene il carattere '; altrimenti
     * restituisce la concatenazione di tante stringhe ottenute spezzando 'testValue' mediante il separatore '.<br>
     * Se, per esempio, 'testValue' e' "prova sett'06&lt;&gt;&amp;"ccc'pppp&amp;&amp;&amp;'pluto.txt", il metodo
     * restituisce la stringa:<br><br>
     *
     * concat('prova sett',"'",'06&lt;&gt;&amp;"ccc',"'",'pppp&amp;&amp;&amp;',"'",'pluto.txt')<br><br>
     *
     * In questo caso la select non fallisce e restituisce il risultato voluto.
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     *
     * @param testValue Stringa test da elaborare.
     *
     * @return La stringa test opportunamente elaborata.
     *
     */
    public static String prepareTest4Xpath(String testValue) {
        String res = "";
        int prevAposPos = -1, aposPos;

        while ((aposPos = testValue.indexOf("'", prevAposPos + 1)) != -1) {
            if (res.length() == 0) res = "concat(";

            res += "'" + testValue.substring(prevAposPos + 1, aposPos) + "',\"'\",";
            prevAposPos = aposPos;
        }

        if (res.length() == 0) return "'" + testValue + "'";

        return res + "'" + testValue.substring(prevAposPos + 1) + "')";
    }

    /**
     * Restituisce l'oggetto Document contenuto in questa classe sotto forma di oggetto org.w3c.dom.Document.
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     *
     * @return L'oggetto Document contenuto in questa classe sotto forma di oggetto org.w3c.dom.Document.
     *
     * @throws DocumentException in caso di errore.
     *
     */
    public org.w3c.dom.Document getW3CDocument() throws DocumentException {
        return (new DOMWriter()).write(document);
    }

    /**
     * Imposta l'oggetto Document contenuto in questa classe partendo da un oggetto org.w3c.dom.Document.
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     *
     * @param doc Oggetto org.w3c.dom.Document che deve essere salvato nell'istanza come oggetto dom4j.
     *
     */
    public void setW3CDocument(org.w3c.dom.Document doc) {
        document = (new DOMReader()).read(doc);
    }

    /**
     * Restituisce il codice xml del documento senza apportare alcuna modifica ad esso e formattandolo in
     * modo che sia facilmente leggibile.
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     *
     * @return Il codice xml del documento formattato per la lettura o non formattato in caso di errore
     *         nella formattazione (prodotto con <i>org.dom4j.Node.asXML()</i>).
     *
     */
    public String asXML() {
        StringWriter sw = new StringWriter();

        try {
            asXML(sw);

            return sw.toString();
        }
        catch (Exception ex) {
            log.error("XMLDocumento.asXML(): got exception formatting xml code:", ex);
            log.error("XMLDocumento.asXML(): using org.dom4j.Node.asXML()...");

            return this.document.asXML();
        }
    }

    /**
     * Restituisce il codice xml del documento senza apportare alcuna modifica ad esso e formattandolo in
     * modo che sia facilmente leggibile.
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     *
     * @param w Oggetto writer su cui scrivere l'xml.
     *
     * @throws Exception in caso di errore nella scrittura sul Writer.
     *
     */
    public void asXML(Writer w) throws Exception {
        OutputFormat outformat = OutputFormat.createPrettyPrint();

        // Federico 03/08/07: l'encoding deve essere quello del documento (se non e' stato rimosso dal costruttore)
        String encoding = (encodingDeclarationRemoved ? ENCODING : document.getXMLEncoding());

        if (encoding == null) encoding = ENCODING; // 'getXMLEncoding' puo' restituire null

        outformat.setEncoding(encoding);
        XMLWriter writer = new XMLWriter(w, outformat);
        writer.write(document);

        // Federico 23/07/08: la chiusura del writer e' giusto che sia a carico del chiamante
        //writer.close();
    }

    /**
     * Chiamare questo metodo equivale a chiamare <i>getStringXMLDocument</i>.
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     *
     * @return Il documento sotto forma di stringa.
     *
     * @see #getStringXMLDocument()
     *
     */
    public String toString() {
        return getStringXMLDocument();
    }

    /**
     * In alcuni archivi migrati da Docway 2 il testo estratto dagli allegati di un documento non figura in allegati
     * a parte ma e' presente (non in un CDATA...) all'interno degli elementi xw:file degli originali.<br>
     * Il metodo elimina il testo e aggiunge l'attributo "convert=yes" nell'elemento xw:file corrispondente
     * (<u>versione piu' recente</u>) per forzare la conversione tramite FCS. [RW 0050152]
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     *
     */
    private void checkTextInXWFiles() {
        String originalXML                  = this.getStringDocument();
        int deletedXwFileExtractedTextNodes = GenericUtils.cleanXWFileExtractedText(this.document);

        if (deletedXwFileExtractedTextNodes > 0) {
            log.debug("XMLDocumento.checkTextInXWFiles(): removed txt nodes in 'xw:file' elements and forced txt conversion by FCS");
            log.debug("XMLDocumento.checkTextInXWFiles(): original document:\r\n\n" + originalXML + "\n");
            log.debug("XMLDocumento.checkTextInXWFiles(): new document:\r\n\n" + this.getStringDocument() + "\n");
        }
    }
}