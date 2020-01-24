/* GenericUtils.java */

/*
 * Created on.....: 22/01/2007
 * by.............: fgr
 * organization...: 3D Informatica
 * description....: In questo file � definita la classe "GenericUtils" che contiene dei metodi utili per le applicazioni
 *                  che usano common.jar [RW 0041847].
 */
package it.highwaytech.apps.generic.utils;

import java.net.URL;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import it.highwaytech.apps.generic.XMLDocumento;

/**
 * Questa classe contiene dei metodi utili per le applicazioni che usano common.jar [RW 0041847].
 * <br><br>
 *
 * @author 3dInformatica - fgr<br>
 *
 */
public class GenericUtils {
	
	/**
     * Logger della classe.
     */
    private static it.highwaytech.util.Log log = it.highwaytech.util.LogFactory.getLogger(GenericUtils.class);

//    /**
//     * Individua una risorsa dato il suo nome.<br>
//     * La localizzazione della risorsa avviene tramite ClassLoader.<br>
//     * Se la risorsa non viene trovata, si ritenta aggiungendo o togliendo il carattere '/' all'inizio
//     * del nome.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr<br>
//     *
//     * @param resourceName Nome della risorsa da cercare.
//     *
//     * @return Un oggetto URL che localizza la risorsa o null se la risorsa non viene trovata.
//     *
//     */
    public static URL getResourceUrl(String resourceName) {

        return getResourceUrl(resourceName, null);
    }

    /**
     * Individua una risorsa dato il suo nome.<br>
     * La localizzazione della risorsa avviene tramite ClassLoader.<br>
     * Se la risorsa non viene trovata, si ritenta aggiungendo o togliendo il carattere '/' all'inizio
     * del nome.
     * <br><br>
     *
     * author 3D Informatica - fgr<br>
     *
     * @param resourceName Nome della risorsa da cercare.
     * @param cl           Eventuale ClassLoader da utilizzare per localizzare la risorsa.
     *                     Se null, viene utilizzato il ClassLoader corrente.
     *
     * @return Un oggetto URL che localizza la risorsa o null se la risorsa non viene trovata.
     *
     */
    public static URL getResourceUrl(String resourceName, ClassLoader cl) {
        log.debug("GenericUtils.getResourceUrl(): looking for resource: " + resourceName);
        log.debug("GenericUtils.getResourceUrl(): ClassLoader: " + cl);

        if (cl == null) cl = GenericUtils.class.getClassLoader();

        URL url = cl.getResource(resourceName);

        if (url == null) {
            log.debug("GenericUtils.getResourceUrl(): resource \"" + resourceName + "\" not found!");

            if (resourceName.startsWith("/")) resourceName = resourceName.substring(1);
            else                              resourceName = "/" + resourceName;

            log.debug("GenericUtils.getResourceUrl(): trying to look for resource: " + resourceName);

            url = cl.getResource(resourceName);
        }

        if (url != null) log.debug("GenericUtils.getResourceUrl(): found resource: " + url.toString());
        else             log.debug("GenericUtils.getResourceUrl(): resource not found!");

        return url;
    }

//    /**
//     * Crea un oggetto File dato il nome di una risorsa.<br>
//     * La localizzazione della risorsa avviene tramite ClassLoader.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr<br>
//     *
//     * @param resourceName Nome della risorsa da cercare.
//     *
//     * @return Un oggetto File corrispondente alla risorsa.
//     *
//     * @throws Exception in caso di errore.
//     *
//     */
//    public static File getResourceAsFile(String resourceName) throws Exception {
//        log.debug("GenericUtils.getResourceAsFile(): looking for resource: " + resourceName);
//
//        // Federico 17/04/07: la localizzazione della risorsa deve tenere conto dell'eventuale "/"
//        // all'inizio del suo nome. Se la prima volta la risorsa non viene trovata, si deve ritentare
//        // aggiungendo/togliendo tale carattere [RW 0043643]
//        URL url = getResourceUrl(resourceName);
//
//        if (url == null) throw new Exception("Resource not found! [" + resourceName + "]");
//
//        log.debug("GenericUtils.getResourceAsFile(): found resource: " + url.toString());
//
//        return new File(urlToUri(url));
//    }
//
//    /**
//     * Restituisce un InputStream a partire da un URL di un file.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr<br>
//     *
//     * @param fileUrl URL del file di cui si vuole un InputStream.
//     *
//     * @return L'InputStream del file indicato.
//     *
//     * @throws NullPointerException  se <i>fileUrl</i> e' null.
//     * @throws URISyntaxException    in caso di errore nella conversione URL --&gt; URI.
//     * @throws FileNotFoundException se il file non esiste, e' una directory o se non puo' essere aperto in lettura.
//     * @throws SecurityException     se esiste un security manager che nega l'accesso in lettura al file.
//     *
//     */
//    public static InputStream getInputStreamFromFileUrl(URL fileUrl) throws NullPointerException,
//                                                                            URISyntaxException,
//                                                                            FileNotFoundException,
//                                                                            SecurityException  {
//
//        if (fileUrl == null) throw new NullPointerException("File's URL must not be null!");
//
//        return new FileInputStream(new File(urlToUri(fileUrl)));
//    }
//
//    /**
//     * Carica una risorsa in memoria dato il suo nome.<br>
//     * La localizzazione della risorsa avviene tramite ClassLoader.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr<br>
//     *
//     * @param resourceName Nome della risorsa da caricare.
//     *
//     * @return Il contenuto della risorsa.
//     *
//     * @throws Exception in caso di errore.
//     *
//     */
//    public static byte[] loadResource(String resourceName) throws Exception {
//        // Federico 17/10/06: ristrutturato codice [RW 0039105]
//        log.debug("GenericUtils.loadResource(): loading resource: " + resourceName);
//
//        File resource = getResourceAsFile(resourceName);
//
//        return loadFile(resource);
//    }
//
//    /**
//     * Carica una risorsa in memoria dato il suo URL.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr<br>
//     *
//     * @param resourceURL URL della risorsa da caricare.
//     *
//     * @return Il contenuto della risorsa.
//     *
//     * @throws Exception in caso di errore.
//     *
//     */
//    public static byte[] loadResource(URL resourceURL) throws Exception {
//        log.debug("GenericUtils.loadResource(): loading resource: " + resourceURL.toString());
//
//        return loadFile(new File(urlToUri(resourceURL)));
//    }
//
//    /**
//     * Converte un oggetto java.net.URL in un oggetto java.net.URI senza utilizzare il metodo 'toURI'
//     * della classe URL per non introdurre una dipendenza con il jdk 1.5.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr<br>
//     *
//     * @param url URL da convertire.
//     *
//     * @return L'URL convertito in un oggetto URI.
//     *
//     * @throws URISyntaxException in caso di errore nella creazione del URI.
//     *
//     */
//    public static URI urlToUri(URL url) throws URISyntaxException {
//        /*
//         * Federico 17/10/06: i metodi 'toString()' e 'toExternalForm()' della classe java.net.URL sono
//         * equivalenti e non effettuano l'escaping dei caratteri proibiti nelle URL.
//         * Quindi, e' necessario effettuare tale escaping prima di creare l'oggetto URI [RW 0039105].
//         */
//        String urlStr = url.toString();
//        urlStr = XmlReplacer.replaceString(urlStr, " ", "%20");
//
//        // Federico 28/09/06: il metodo 'url.toURI()' e' presente dalla versione 1.5 di java -->
//        // meglio non usarlo per avere compatibilita' con la 1.4! [RW 0039102]
//        return new URI(urlStr);
//    }
//
//    /**
//     * Carica un file in memoria.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr<br>
//     *
//     * @param fileToBeLoaded File da caricare.
//     *
//     * @return Il contenuto del file.
//     *
//     * @throws Exception in caso di errore.
//     *
//     */
//    public static byte[] loadFile(File fileToBeLoaded) throws Exception {
//        BufferedInputStream ins = null;
//        boolean loaded = false;
//
//        try {
//            // caricamento del file in memoria
//            log.debug("GenericUtils.loadFile(): loading file: " + fileToBeLoaded.getAbsolutePath());
//
//            ins = new BufferedInputStream(new FileInputStream(fileToBeLoaded));
//            int fileSize = (int)fileToBeLoaded.length();
//            byte[] buf = new byte[fileSize];
//            int nBytesRead = 0;
//            int effectiveBytesRead;
//
//            while (nBytesRead < fileSize &&
//                    (effectiveBytesRead = ins.read(buf, nBytesRead, fileSize - nBytesRead)) != -1) {
//                nBytesRead += effectiveBytesRead;
//            }
//
//            log.debug("GenericUtils.loadFile(): bytes read: " + nBytesRead + "/" + fileSize);
//
//            loaded = true;
//            ins.close();
//
//            return buf;
//        }
//        catch (Exception e) {
//            log.error("GenericUtils.loadFile(): got exception:", e);
//
//            try {
//                if (ins != null && loaded) {
//                    log.debug("GenericUtils.loadFile(): closing input stream...");
//                    ins.close();
//                }
//            }
//            catch (IOException ex) {
//                log.error("GenericUtils.loadFile(): got exception closing input stream:", ex);
//            }
//
//            throw new Exception("Not able to load the file!", e);
//        }
//    }
//
//    /**
//     * Restituisce l'estensione del file.
//     * <br><br>
//     *
//     * author 3D Informatica - fcossu<br>
//     *
//     * @param file File di cui si vuole sapere l'estensione.
//     *
//     * @return L'estensione del file, stringa vuota se file privo di estensione.
//     *
//     * @throws Exception in caso di errore.
//     *
//     */
//    public static String getFileExtension(File file, boolean keepDot) throws Exception {
//        String extension = "";
//        String filePath = file.getName();
//        extension = filePath.substring(filePath.lastIndexOf('.')+(keepDot? 0 : 1));
//        return extension;
//    }
//
//    /**
//     * Salva un array di byte in un file.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param content       Array di byte da salvare.
//     * @param fileToBeSaved File da creare.
//     *
//     * @throws IOException in caso di errore.
//     *
//     */
//    public static void saveFile(byte[] content, File fileToBeSaved) throws IOException {
//        FileOutputStream fos = new FileOutputStream(fileToBeSaved);
//        fos.write(content);
//        fos.flush();
//        fos.close();
//    }
//
//    /**
//     * Salva uno stream di byte in un file.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param in            InputStream da salvare nel file indicato (al termine, <u>l'InputStream non viene chiuso</u>).
//     * @param bufferSize    Dimensione del buffer di lettura da utilizzare. Se &lt;= 0, viene
//     *                      utilizzato un buffer di <i>2 * 1024 * 1024</i> byte.
//     * @param fileToBeSaved File da creare.
//     *
//     * @throws IOException in caso di errore.
//     *
//     */
//    public static void saveFile(InputStream in, int bufferSize, File fileToBeSaved) throws IOException {
//        int bytesRead        = 0;
//        byte[] buffer        = new byte[(bufferSize > 0 ? bufferSize : 2 * 1024 * 1024)];
//        FileOutputStream fos = new FileOutputStream(fileToBeSaved);
//
//        while ((bytesRead = in.read(buffer, 0, buffer.length)) != -1) {
//            fos.write(buffer, 0, bytesRead);
//        }
//
//        fos.flush();
//        fos.close();
//    }
//
//
//    /**
//     * Analizza la stringa 's' e restituisce l'indice della sottostringa di chiusura corispondente alla sottostringa di apertura
//     * specificata come parametro 'apertureS'.
//     * @param s Stringa sulla quale effettuare la ricerca
//     * @param apertureI posizione della sottostringa di apertura di cui si desidera conoscere l'indice di chiusura
//     * @param apertureS sottostringa di apertura
//     * @param closureS sottostringa di chiusura
//     * @return indice della sottostringa corrispondente di chiusura, -1 se non trovata la chiusura corrispondente
//     */
//    public static int searchMatchingClosureIndex(String s, int apertureI, String apertureS, String closureS) {
//        int count = 0;
//        s = s.substring(apertureI);
//        while (s.length() > 0) {
//            if (s.indexOf(apertureS) == 0)
//                count++;
//            else if (s.indexOf(closureS) == 0)
//                count--;
//            if (count == 0)
//                return apertureI;
//            apertureI++;
//            s = s.substring(1);
//        }
//        return -1;
//    }
//
//    /**
//     * Per le funzionalita' si veda il metodo 'parseModel(String, XMLDocumento, boolean)'.
//     *
//     * @param s   Stringa da analizzare.
//     * @param doc Documento xml da cui recuperare i valori degli xpath.
//     *
//     * @return La stringa modificata sostituendo gli xpath con i rispettivi valori.
//     *
//     * @throws Exception in caso di errori sintattici nella notazione degli xpath o se un xpath non viene trovato.
//     *
//     * @see #parseModel(String, XMLDocumento, boolean)
//     *
//     */
//    public static String parseModel(String s, XMLDocumento doc) throws Exception {
//        return parseModel(s, doc, false);
//    }
//
//    /**
//     * Sostituisce tutti gli xpath dom4j presenti nella stringa 's' con i rispettivi valori recuperati dal
//     * documento xml 'doc' [RW 0039102].<br>
//     * Gli xpath devono avere la seguente notazione: [xml,xpath_dom4j<i>,opzione,opzione...</i>].<br>
//     * E' possibile specificare al posto degli xpath delle funzioni utilizzando la notazione [fun,FUNZIONE<i>,opzione,opzione...</i>].<br>
//     * Per la lista delle funzioni disponibili consultare il metodo 'getFunctionValue'.<br>
//     * Qualora un xpath produca come risultato una stringa vuota, viene segnalato un warning nei log.<br>
//     * Le opzioni supportate - separate con ',' e non obbligatorie - sono:<br><br>
//     *
//     * <ul>
//     * <li><b>x-y</b>: effettua il substring del valore individuato dal xpath a partire dal carattere x
//     *                 fino al carattere y escluso (come 'substring' di Java).<br>
//     *                 Omettere y e' equivalente a specificare la lunghezza della stringa (come accade in Java).</li>
//     * <li><b>trim-left-zeros</b>: elimina gli '0' a sinistra del valore individuato dal xpath.</li>
//     * <li><b>format-date</b>: formatta una data nel formato gg/mm/aaaa.</li>
//     * </ul>
//     * <br>
//     *
//     * author 3D Informatica - fgr, ss<br>
//     *
//     * @param s              Stringa da analizzare.
//     * @param doc            Documento xml da cui recuperare i valori degli xpath.
//     * @param allowNullValue True se ignorare la presenza di xpath inesistenti.
//     *
//     * @return La stringa modificata sostituendo gli xpath con i rispettivi valori.
//     *
//     * @throws Exception in caso di errori sintattici nella notazione degli xpath/function o
//     *         se un xpath non viene trovato (sempre che 'allowNullValue' valga false).
//     *
//     * @see #parseXpathOptions(String, String)
//     *
//     */
//    public static String parseModel(String s, XMLDocumento doc, boolean allowNullValue) throws Exception {
//        int startXpath = -1;
//        int endXpath = -1;
//
//        final int XML = 0;
//        final int FUN = 1;
//        int type = XML;
//        while ((startXpath = s.indexOf("[xml,")) != -1 || (startXpath = s.indexOf("[fun,")) != -1) {
//            if (s.substring(startXpath).startsWith("[fun,"))
//                type = FUN;
//            endXpath = searchMatchingClosureIndex(s, startXpath, "[", "]");
//
//            if (endXpath != -1) {
//                String xpath = s.substring(startXpath + "[xml,".length(), endXpath);
//
//                if (xpath.length() > 0) {
//                    // Federico 23/01/07: introdotta gestione opzioni di formattazione del valore trovato
//                    // dal xpath [RW 0041847]
//                    Vector parts = Text.split(xpath, ",");
//                    String realXpath = (String)parts.get(0);
//
//                    String value = null;
//                    if (type == XML)
//                        value = getXpathValue(doc, realXpath);
//                    else if (type == FUN)
//                        value = getFunctionValue(doc, realXpath);
//
//                    if (allowNullValue || value != null) {
//                        if (value == null)
//                            value = "";
//
//                        if (value.length() == 0) {
//                            log.warn("GenericUtils.parseModel(): found an empty value... [xpath: " + realXpath + "]");
//                        }
//
//                        if (parts.size() > 1) {
//                            // sono state indicate delle opzioni
//                            for (int j = 1; j < parts.size(); j++) {
//                                value = parseXpathOptions(value, (String)parts.get(j));
//                            }
//                        }
//
//                        s = s.substring(0, startXpath) + value + s.substring(endXpath + 1);
//                    }
//                    else {
//                        // nodo non trovato!
//                        log.error("GenericUtils.parseModel(): xpath not found! [xpath: " + realXpath + "]");
//                        throw new Exception("Xpath not found! [xpath: " + realXpath + "]");
//                    }
//                }
//                else {
//                    // xpath vuoto --> errore sintattico!
//                    log.error("GenericUtils.parseModel(): empty xpath! [starting position: " + startXpath + "]");
//                    throw new Exception("Syntax error: empty xpath! [starting position: " + startXpath + "]");
//                }
//            }
//            else {
//                // xpath non chiuso --> errore sintattico!
//                log.error("GenericUtils.parseModel(): xpath not closed! [starting position: " + startXpath + "]");
//                throw new Exception("Syntax error: xpath not closed! [starting position: " + startXpath + "]");
//            }
//        } // fine while
//
//        log.debug("GenericUtils.parseModel(): returning: " + s);
//
//        return s;
//    }
//
//    /**
//     * Questo metodo analizza le regole contenute in 'rules' (una regola per ogni riga) che
//     * devono essere nella forma: xpath,valore.<br>
//     * Ogni valore viene salvato nel documento xml 'xmlDoc' nel xpath indicato.<br>
//     * Se xpath comincia con "zerofilledbefore_dec#", verranno eliminati gli '0' iniziali che
//     * riempiono il campo e verra' eventualmente aggiunto il separatore dei decimali.<br>
//     * Se, invece, xpath comincia con "compose#", il valore da salvare viene composto secondo gli
//     * xpath in esso indicati.<br>
//     * Per esempio, la regola<br><br>
//     *
//     * "compose#/doc/oggetto,Bonifico [xml-/doc/extra/stipendi/&#64;tipologia] Mese di [xml-/doc/extra/stipendi/&#64;mese]"<br><br>
//     *
//     * salva in "/doc/oggetto" la stringa "Bonifico..." riempita con i valori corrispondenti agli
//     * xpath indicati, valori estratti dal documento 'xmlDoc'.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr<br>
//     *
//     * @param rules    Regole da esaminare.
//     * @param xmlDoc   Documento da aggiornare.
//     *
//     * @throws Exception in caso di errore.
//     *
//     */
//    public static void fillDocument(String rules, XMLDocumento xmlDoc) throws Exception {
//        log.debug("GenericUtils.fillDocument(): parsing rules:\r\n\n" + rules + "\n");
//
//        BufferedReader br = new BufferedReader(new StringReader(rules));
//        String line;
//
//        while ((line = br.readLine()) != null) {
//            if (line.length() == 0) {
//                log.warn("GenericUtils.fillDocument(): Skipping empty rule...");
//                continue;
//            }
//
//            Vector ruleElements = Text.split(line, ",");
//
//            if (ruleElements.size() != 2) {
//                log.error("GenericUtils.fillDocument(): wrong rule's syntax! Skipping rule: " + line);
//                continue;
//            }
//
//            String xpath = (String)ruleElements.get(0);
//            String val   = (String)ruleElements.get(1);
//
//            if (val.length() > 0) {
//
//                // Federica RW0044363 - Gestione della data odierna
//                if (val.equalsIgnoreCase("<data_odierna>")) {
//                    Date dt = new Date();
//                    val = getData(dt);
//                }
//
//                // Federica RW0044363 - Gestione dell'anno corrente
//                if (val.equalsIgnoreCase("<anno>")) {
//                    GregorianCalendar currDate = new GregorianCalendar();
//                    val = String.valueOf(currDate.get(GregorianCalendar.YEAR));
//                }
//
//                if (xpath.startsWith("compose#")) {
//                    // valore che deve essere composto con altri xpath
//                    xpath = xpath.substring("compose#".length());
//                    val   = XmlReplacer.replaceString(val, "[xml-", "[xml,");
//                    val   = parseModel(val, xmlDoc);
//                }
//                else if (xpath.startsWith("zerofilledbefore_dec#")) {
//                    // il valore del campo da salvare ha degli '0' iniziali da rimuovere
//                    xpath = xpath.substring("zerofilledbefore_dec#".length());
//                    int startNumber = 0;
//
//                    while (startNumber < val.length() && val.charAt(startNumber) == '0') startNumber++;
//
//                    if (startNumber < val.length()) val = val.substring(startNumber);
//
//                    // controllo dei decimali
//                    if (val.endsWith("00")) val = val.substring(0, val.length() - 2);
//                    else                    val = val.substring(0, val.length() - 2) + "," +
//                                                  val.substring(val.length() - 2);
//                }
//
//                // preparazione del xpath
//                String rootElementName = xmlDoc.getIUName();
//                xpath = XmlReplacer.replaceString(xpath, "/" + rootElementName + "/", "");
//                xpath = XmlReplacer.replaceString(xpath, "/", ".");
//
//                // salvo il campo nel documento
//                log.debug("GenericUtils.fillDocument(): updating document [\"" + val +
//                          "\" --> \"" + xpath + "\"]...");
//
//                xmlDoc.insertXPath(xpath, val);
//            }
//            else {
//                log.debug("GenericUtils.fillDocument(): empty value! Skipping rule: " + line);
//            }
//        } // fine ciclo sulle regole
//
//        br.close();
//
//        // Federico 16/03/07: l'if permette di valutare 'xmlDoc.asXML()' solo se necessario
//        if (log.getLogLevel().startsWith("TRACE")) {
//            log.trace("GenericUtils.fillDocument(): filled document:\r\n\n" + xmlDoc.asXML() + "\n");
//        }
//    }
//
//    /**
//     * Restituisce il valore dell'xpath 'xpath' nel documento 'doc', null se non trovato
//     */
//    private static String getXpathValue(XMLDocumento doc, String xpath) {
//        Node xmlNode = doc.selectSingleNode(xpath);
//
//        if (xmlNode != null) {
//            String value = xmlNode.getText();
//            return value;
//        }
//
//        return null;
//    }
//
//    /**
//     * Restituisce il valore della funziona calcolata specificata in 'function', null se specificata funzione inesistente
//     * Funzioni disponibili:
//     *
//     * CURRENT_HOUR : restituisce l'ora corrente nel formato HH:mm:ss
//     * DATI_DOC_MITTENTE : dati documento mittente (stile Docway)
//     * ALLEGATO : campo allegato (stile Docway)
//     */
//    private static String getFunctionValue(XMLDocumento doc, String function) {
//        if (function .equals("CURRENT_HOUR")) {
//            DateFormat df = new SimpleDateFormat("HH:mm:ss");
//            return df.format(new Date());
//        }
//        if (function .equals("CURRENT_DATE")) {
//            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//            return df.format(new Date());
//        }        
//        else if (function.equals("DATI_DOC_MITTENTE")) {
//            String value = doc.getAttributeValue("/doc/mezzo_trasmissione/@cod", "");
//            String nprot = doc.getAttributeValue("/doc/rif_esterni/rif/@n_prot", "");
//            if (nprot.length() > 0)
//                value += " Prot n. " + nprot;
//            String dataprot = doc.getAttributeValue("/doc/rif_esterni/rif/@data_prot", "");
//            if (dataprot.length() > 0)
//                value += " del " + dateFormat(dataprot);
//            return value;
//        }
//        else if (function.equals("ALLEGATO")) {
//            String value = "";
//            List l = doc.selectNodes("/doc/allegato");
//            for (int i=0; i<l.size(); i++) {
//                value += ((Element)l.get(i)).getText() + "; ";
//            }
//            boolean addCount = true;
//            if (l.size() == 1) {
//                String fA = ((Element)l.get(0)).getText();
//                if (fA.equals("0 - nessun allegato"))
//                    addCount = false;
//            }
//            if (addCount)
//                value = l.size() + " - " + value;
//            return value;
//        }
//
//        return null;
//    }
//
//    /**
//     * Aggiorna il valore 'value' secondo l'opzione indicata.<br>
//     * Per le opzioni supportate si veda il metodo 'parseModel(String, XMLDocumento)'.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr<br>
//     *
//     * @param value  Valore da modificare.
//     * @param option Opzione da applicare.
//     *
//     * @return Il valore aggiornato secondo l'opzione 'option'.
//     *
//     * @throws Exception se l'opzione non e' supportata o contiene degli errori.
//     *
//     * @see #parseModel(String, XMLDocumento)
//     *
//     */
//    private static String parseXpathOptions(String value, String option) throws Exception {
//        int pos;
//
//        if (option.equals("trim-left-zeros")) {
//            // trim degli '0' iniziali
//            return trimLeftZeros(value);
//        }
//        else if (option.equals("format-date")) {//formatta data nel formato gg/mm/aaaa
//            return dateFormat(value);
//        }
//        else if ((pos = option.indexOf("-")) != -1) {
//            // opzione 'x-y' (substring)
//            String start = option.substring(0, pos);
//            String end = option.substring(pos + 1);
//
//            if (end.length() > 0) {
//                return value.substring(Integer.parseInt(start), Integer.parseInt(end));
//            }
//            else {
//                return value.substring(Integer.parseInt(start));
//            }
//        }
//
//
//        log.error("GenericUtils.parseXpathOptions(): Unsupported option \"" + option + "\"!");
//        throw new Exception("Opzione non supportata [" + option + "]");
//    }
//
//    /**
//     * Invia dei dati su uno stream http impostando opportunamente gli header http.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr<br>
//     *
//     * @param httpResp        Oggetto che consente di recuperare lo stream http e che mette a disposizione
//     *                        i metodi per impostare gli header.
//     * @param mimeType        Tipo mime dei dati da inviare.
//     * @param contentDispType Content-Disposition da impostare. I valori accettati sono:
//     *                        "<b>inline</b>" per forzare l'apertura automatica da parte del browser dei dati inviati;
//     *                        "<b>attachment</b>" per fare in modo che il browser chieda di effettuare il
//     *                        download.
//     * @param fileName        Nome del file che deve comparire nella finestra di download del browser.
//     *                        Se null viene ignorato.
//     * @param data            Dati da inviare.
//     *
//     * @throws Exception in caso di errore.
//     *
//     */
//    public static void sendHTTPData(Response httpResp,
//                                    String mimeType,
//                                    String contentDispType,
//                                    String fileName,
//                                    byte[] data) throws Exception {
//
//        log.debug("GenericUtils.sendHTTPData(): setting \"ContentType\" to: " + mimeType);
//        httpResp.setContentType(mimeType);
//
//        // Federico 26/01/06: rivista gestione degli header http della risposta da inviare al client [RW 0033921]
//        // "Pragma" deve essere svuotato per non entrare in conflitto con "Cache-Control".
//        // "Cache-Control" � meglio che sia "private" in modo che i nodi intermedi non facciano caching.
//        log.debug("GenericUtils.sendHTTPData(): clearing \"Pragma\" header");
//        httpResp.setHeader("Pragma", "");
//
//        log.debug("GenericUtils.sendHTTPData(): setting \"Cache-Control\" to: private");
//        httpResp.setHeader("Cache-Control", "private");
//
//        if (contentDispType == null ||
//                contentDispType.length() == 0 ||
//                (!contentDispType.equals("inline") && !contentDispType.equals("attachment"))) {
//
//            log.error("GenericUtils.sendHTTPData(): wrong Content-Disposition \"" + contentDispType +
//                      "\"! Only \"inline\" and \"attachment\" accepted.");
//            throw new Exception("Content-Disposition errato! Sono accettati solo i valori \"inline\" e \"attachment\".");
//        }
//
//        if (fileName != null && fileName.trim().length() > 0) {
//            log.debug("GenericUtils.sendHTTPData(): setting \"Content-Disposition\" to: " +
//                      contentDispType + "; filename=\"" + fileName + "\"");
//            httpResp.setHeader("Content-Disposition", contentDispType + "; filename=\"" + fileName + "\"");
//        }
//        else {
//            log.debug("GenericUtils.sendHTTPData(): setting \"Content-Disposition\" to: " +
//                      contentDispType + ";");
//            httpResp.setHeader("Content-Disposition", contentDispType + ";");
//        }
//
//        log.debug("GenericUtils.sendHTTPData(): data length: " + data.length);
//        httpResp.setIntHeader("Content-Length", data.length);
//
//        log.debug("GenericUtils.sendHTTPData(): sending data...");
//
//        java.io.OutputStream outputStream = httpResp.getOutputStream();
//        outputStream.write(data);
//        outputStream.flush();
//        outputStream.close();
//    }
//
//    /**
//     * Rimuove gli allegati/elementi prodotti dal FileConversionService e gli attributi di richiesta
//     * per il FileConversionService introdotti da extraway.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr<br>
//     *
//     * @param doc       Documento da ripulire.
//     * @param baseXPath Radice del xpath da utilizzare per trovare gli allegati. A questo valore viene
//     *                  accodato l'xpath "/*[name()='xw:file']".<br>
//     *                  Notare che se 'baseXPath' termina con "/", viene accodato solo "*[name()='xw:file']".
//     *
//     * @return Un Vector di stringhe che rappresentano gli id degli allegati rimossi.
//     *
//     */
//    public static Vector cleanFCSData(XMLDocumento doc, String baseXPath) {
//        Vector<String> removedAttachments = new Vector<String>();
//        baseXPath += (baseXPath.endsWith("/") ? "" : "/") + "*[name()='xw:file']";
//
//        // rimozione allegati prodotti dal FCS
//        List fcsAttachments = doc.selectNodes(baseXPath + "[@der_from!='']");
//
//        for (int j = 0; j < fcsAttachments.size(); j++) {
//            Element attachment = (Element)fcsAttachments.get(j);
//            String attachmentName = attachment.attributeValue("name");
//            removedAttachments.add(attachmentName);
//            attachment.detach();
//        }
//
//        // rimozione attributi/elementi relativi al FCS
//        String[] nodeList = {"@der_to", "@agent.meta", "@agent.xml", "@agent.pdf", "@agent.thumb", "*[name()='metadata']"};
//
//        for (int j = 0; j < nodeList.length; j++) {
//            doc.removeXPath(baseXPath + "/" + nodeList[j]);
//        }
//
//        return removedAttachments;
//    }
//
//    /**
//     * Metodo che restituisce il nome del sistema operativo ("windows" per Windows 95/98/2000/Xp,
//     * "linux" per linux, "mac_os_x" per MacOsX).<br><br>
//     *
//     * (Metodo copiato da <i>it.extrawaytech.dialogue.Tools</i> per evitare di avere troppe dipendenze
//     * in common.jar da indexLib.jar)
//     * <br><br>
//     *
//     * author 3D Informatica - fgr<br>
//     *
//     * @return Il nome del sistema operativo o una stringa vuota qualora non venga trovata la property
//     *         di sistema "os.name".
//     *
//     */
//    public static String getOSName() {
//        Properties systemProps = System.getProperties();
//
//        //debug
//        /*Log log = it.highwaytech.util.LogFactory.getLogger(GenericUtils.class);
//
//        java.util.Enumeration en = systemProps.propertyNames();
//
//        log.debug("GenericUtils.getOSName(): *** system properties ***");
//
//        while (en.hasMoreElements()) {
//            String propName = (String)en.nextElement();
//            log.debug("GenericUtils.getOSName(): " + propName + "=" + systemProps.getProperty(propName));
//        }*/
//        // debug
//
//        String os_name = systemProps.getProperty("os.name", "");
//
//        if (os_name.length() > 0) {
//            os_name = os_name.replaceAll("\\s", "_").toLowerCase();
//
//            if (os_name.indexOf("windows") != -1) os_name = "windows";
//        }
//
//        return os_name;
//    }
//
//    /**
//     * Consente di capire qual'e' il chiamante di un metodo restituendo lo stack delle chiamate.<br>
//     * Questo metodo e' stato concepito solo per il debugging, vista la sua inefficienza.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr<br>
//     *
//     * @return Lo stack delle chiamate che portano al metodo 'printMethodCallStackTrace'.
//     *
//     */
//    public static String getMethodCallStackTrace() {
//        Throwable t = new Throwable();
//        StringWriter st = new StringWriter();
//        t.printStackTrace(new PrintWriter(st));
//
//        return st.getBuffer().toString();
//    }
//
//    /**
//     * Verifica se all'interno di un insieme di stringhe racchiuse in un'unica String ce n'� una specificata.
//     * Il confronto viene effettuato in modalit� case-insensitive e tra stringhe sottoposte a trim().
//     * @param possibilities racchiude, separati da un separatore, un insieme di valori stringa
//     * @param separator il separatore dei diversi valori; se lasciato vuoto, come default viene preso ";"
//     * @param lookFor la stringa che voglio trovare nell'insieme specificato
//     * @return <code>true</code> se una delle stringhe appartenenti alla String globale coincide - trascurando il case
//     * e togliendo spazi in testa e in coda - con quella specificata; <code>false</code> altrimenti.
//     */
//    public static boolean belongs(String possibilities, String separator, String lookFor) {
//        if (separator.length() == 0)
//            separator = ";";
//        boolean atLeastOne = false;
//        if (lookFor.length() > 0) {
//            String[] chances = possibilities.split(separator);
//            for (int i = 0; i < chances.length; i++) {
//                String currentChoice = chances[i];
//                if (currentChoice.length() > 0 && currentChoice.trim().equalsIgnoreCase(lookFor.trim())) {
//                    atLeastOne = true;
//                    break;
//                }
//            }
//        }
//        return atLeastOne;
//    }
//
//    /**
//     * Stampa informazioni sull'uso della memoria a runtime
//     *
//     */
//    public static void checkMemory() {
//        log.trace("Free memory:" + Runtime.getRuntime().freeMemory() / 1024 + "M");
//        log.trace("Max memory: " + Runtime.getRuntime().maxMemory() / 1024 + "M");
//        log.trace("Total memory: " + Runtime.getRuntime().totalMemory() / 1024 + "M");
//    }
//
//    /**
//     * Stampa informazioni sull'uso della memoria a runtime insieme a un messaggio personalizzabile
//     * @param message il messaggio che viene stampato subito prima delle informazioni sulla memoria
//     */
//    public static void checkMemory(String message) {
//        log.trace(message);
//        checkMemory();
//    }
//
//    /**
//     * Restituisce una data in formato ggmmaa.
//     *
//     * @param dataOdierna: se � <code>null</code> viene restituita la data odierna; altrimenti rappresenta la data
//     * da convertire, per la quale sono riconosciuti e tradotti i seguenti formati:
//     * <ul>
//     * <li>gg/mm/aaaa</li>
//     * <li>ggmmaaaa</li>
//     * </ul>
//     */
//     public static String dataItalianaCompatta(String dataOdierna) {
//         SimpleDateFormat formatoGiorno = new SimpleDateFormat("ddMMyy");
//         Date dataDaCompattare = null;
//
//         if (null == dataOdierna || dataOdierna.length() == 0) {
//             dataDaCompattare = new Date(System.currentTimeMillis());
//         }
//         else{
//             // Federico 06/10/06: introdotti piu' pattern per interpretare 'dataOdierna' [RW 0039104]
//             String[] formatiRiconosciuti = {"dd/MM/yyyy", "ddMMyyyy"};
//             SimpleDateFormat sdf;
//
//             for (int j = 0; j < formatiRiconosciuti.length; j++) {
//                 sdf = new SimpleDateFormat(formatiRiconosciuti[j]);
//
//                 try {
//                     log.debug("GenericUtils.dataItalianaCompatta(): trying to parse date \"" + dataOdierna + "\"" +
//                               " with pattern \"" + formatiRiconosciuti[j] + "\"...");
//
//                     dataDaCompattare = sdf.parse(dataOdierna);
//                     break;  // tutto e' andato bene --> esco dal ciclo
//                 }
//                 catch (ParseException e) {
//                     log.error("GenericUtils.dataItalianaCompatta(): parsing date \"" + dataOdierna + "\"" +
//                               " with pattern \"" + formatiRiconosciuti[j] + "\" failed");
//
//                     if (j == formatiRiconosciuti.length - 1) {
//                         log.error("GenericUtils.dataItalianaCompatta(): not able to parse \"" + dataOdierna + "\"");
//                         log.error("GenericUtils.dataItalianaCompatta(): returning: " + dataOdierna);
//
//                         return dataOdierna;
//                     }
//                     else {
//                         log.debug("GenericUtils.dataItalianaCompatta(): trying next pattern...");
//                     }
//                 }
//             }
//         }
//
//         return formatoGiorno.format(dataDaCompattare);
//     }
//
//     /**
//      * Formatta una stringa numerica imponendo il numero di decimali indicato da 'numMaxDecimali' ed eliminando
//      * i separatori delle migliaia e dei decimali indicati rispettivamente da 'sepMigliaia' e 'sepDecimali'.
//      * <br><br>
//      *
//      * author 3D Informatica - fgr<br>
//      *
//      * @param importo        Importo da formattare.
//      * @param sepDecimali    Separatore dei decimali.
//      * @param sepMigliaia    Separatore delle migliaia.
//      * @param numMaxDecimali Numero massimo di decimali. Qualora manchino i decimali richiesti, vengono
//      *                       aggiunti degli '0'.
//      *
//      * @return L'importo formattato.
//      *
//      */
//     public static String formattaStringaNumerica(String importo,
//                                                  String sepDecimali,
//                                                  String sepMigliaia,
//                                                  int numMaxDecimali) {
//
//         return formattaStringaNumerica(importo, sepDecimali, sepMigliaia, numMaxDecimali, -1);
//     }
//
//     /**
//      * Formatta una stringa numerica imponendo il numero di decimali indicato da 'numMaxDecimali' ed eliminando
//      * i separatori delle migliaia e dei decimali indicati rispettivamente da 'sepMigliaia' e 'sepDecimali'.<br>
//      * E' anche possibile indicare la lunghezza che deve avere la stringa risultante, cosa che comporta
//      * l'inserimento di '0' in testa fino al raggiungimento della lunghezza desiderata.
//      * <br><br>
//      *
//      * author 3D Informatica - fgr<br>
//      *
//      * @param importo        Importo da formattare.
//      * @param sepDecimali    Separatore dei decimali.
//      * @param sepMigliaia    Separatore delle migliaia.
//      * @param numMaxDecimali Numero massimo di decimali. Qualora manchino i decimali richiesti, vengono
//      *                       aggiunti degli '0'.
//      * @param strLen         Se maggiore di 0 e se maggiore della lunghezza della stringa risultante,
//      *                       comporta l'inserimento di '0' in testa fino al raggiungimento della lunghezza
//      *                       desiderata.
//      *
//      * @return L'importo formattato.
//      *
//      */
//     public static String formattaStringaNumerica(String importo,
//                                                  String sepDecimali,
//                                                  String sepMigliaia,
//                                                  int numMaxDecimali,
//                                                  int strLen) {
//
//         if (sepMigliaia != null && sepMigliaia.length() > 0) importo = XmlReplacer.replaceString(importo, sepMigliaia, "");
//
//         int sepPos;
//
//         if (sepDecimali != null && sepDecimali.length() > 0 &&
//                 (sepPos = importo.indexOf(sepDecimali)) != -1) {
//
//             int numeroCifreDecimali = importo.length() - sepPos - 1;
//
//             if (numeroCifreDecimali < numMaxDecimali) {
//                 for (int j = 1; j <= (numMaxDecimali - numeroCifreDecimali); j++) {
//                     importo += "0";
//                 }
//             }
//             else if (numeroCifreDecimali > numMaxDecimali) {
//                 importo = importo.substring(0, importo.length() - (numeroCifreDecimali - numMaxDecimali));
//             }
//
//             importo = XmlReplacer.replaceString(importo, sepDecimali, "");
//         }
//         else {
//             for (int j = 1; j <= numMaxDecimali; j++) {
//                 importo += "0";
//             }
//         }
//
//         // Federico 06/10/06: introdotta possibilita' di indicare la lunghezza che deve avere la stringa
//         // risultante [RW 0039104]
//         String prefix = "";
//
//         if (strLen > 0 && importo.length() < strLen) {
//             for (int j = strLen - importo.length(); j > 0; j--) {
//                 prefix += "0";
//             }
//         }
//
//         return prefix + importo;
//     }
//
//     /**
//      * Restituisce una data in formato 'aaaammgg'.
//      * <br><br>
//      *
//      * author 3D Informatica - fgr<br>
//      *
//      * @param data Oggetto Date da formattare.
//      *
//      * @return La data formattata.
//      *
//      */
//     public static String getData(Date data) {
//         DateFormat df = new SimpleDateFormat("yyyyMMdd");
//         String fdata = df.format(data);
//
//         return fdata;
//     }
//
//     /**
//      * Restituisce una data in formato 'dd/MM/yyyy'.
//      * <br><br>
//      *
//      * author 3D Informatica - fgr<br>
//      *
//      * @param date Oggetto Date da formattare.
//      *
//      * @return La data formattata.
//      *
//      */
//     public static String getDataIt(Date date) {
//         DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//         String fdata = df.format(date);
//         return fdata;
//     }
//
//     /**
//      * Restituisce un'ora in formato 'oo:mm:ss'.
//      * <br><br>
//      *
//      * author 3D Informatica - fgr<br>
//      *
//      * @param data Oggetto Date da formattare.
//      *
//      * @return L'ora formattata.
//      *
//      */
//     public static String getOra(Date data) {
//         DateFormat df = new SimpleDateFormat("HH:mm:ss");
//         String fora = df.format(data);
//
//         return fora;
//     }
//
//     /**
//      * Recupera dei documenti data una lista di nrecord [RW 0037104].
//      * <br><br>
//      *
//      * author 3D Informatica - fgr<br>
//      *
//      * @param conn           Connessione da utilizzare.
//      * @param listOfNrecords Lista di nrecord da usare per effettuare la query.
//      *
//      * @return Un oggetto QueryResult che rappresenta la query effettuata. Null se non viene trovato nulla.
//      *
//      * @throws Exception in caso di errore.
//      *
//      */
//     public static QueryResult getDocsByNrecords(Connessione conn, Vector listOfNrecords) throws Exception {
//         log.debug("GenericUtils.getDocsByNrecords(): using connection: " + conn.getConnection());
//
//         // costruzione della frase di ricerca per il recupero dei documenti
//         String query = "[docnrecord]=";
//
//         log.debug("GenericUtils.getDocsByNrecords(): there are " + listOfNrecords.size() + " nrecords");
//
//         for (int j = 0; j < listOfNrecords.size(); j++) {
//             query += "\"" + (String)listOfNrecords.get(j) + "\"";
//
//             if (j < listOfNrecords.size() - 1) query += " OR ";
//         }
//
//         log.debug("GenericUtils.getDocsByNrecords(): query is: " + query);
//
//         // Federico 09/10/06: ristrutturato codice [RW 0039705]
//         return doXWQuery(conn, query);
//     }
//
//     /**
//      * Recupera dei documenti data una query [RW 0039705].
//      * <br><br>
//      *
//      * author 3D Informatica - fgr<br>
//      *
//      * @param conn  Connessione da utilizzare.
//      * @param query La query da effettuare.
//      *
//      * @return Un oggetto QueryResult che rappresenta la query effettuata. Null se non viene trovato nulla.
//      *
//      * @throws Exception in caso di errore.
//      *
//      */
//     public static QueryResult doXWQuery(Connessione conn, String query) throws Exception {
//         log.debug("GenericUtils.doXWQuery(): using connection: " + conn.getConnection());
//         log.debug("GenericUtils.doXWQuery(): querying: " + query);
//
//         Selezione selezione = new Selezione(conn);
//         int docsFound = selezione.Search(query, "", "", 0, 1);
//
//         log.debug("GenericUtils.doXWQuery(): found " + docsFound + " documents");
//
//         if (docsFound > 0) return selezione.getQueryResult();
//         else               return null;
//     }
//
//     /**
//      * Carica un documento data una query in grado di individuarlo in modo univoco [RW 0039705].
//      * <br><br>
//      *
//      * author 3D Informatica - fgr<br>
//      *
//      * @param conn      Connessione da utilizzare.
//      * @param query     La query da effettuare.
//      * @param locked    Indica se bloccare o meno il documento.
//      *
//      * @return Un array di tre oggetti: il documento caricato (XMLDocumento), il numero fisico del documento (Integer) e il
//      *         suo eventuale codice di lock (String).
//      *
//      * @throws Exception in caso di errore.
//      *
//      */
//     public static Object[] loadSingleDoc(Connessione conn,
//                                          String query,
//                                          boolean locked) throws Exception {
//
//         return loadSingleDoc(conn, query, locked, 10, 3000);
//     }
//
//     /**
//      * Carica un documento data una query in grado di individuarlo in modo univoco [RW 0039705].
//      * <br><br>
//      *
//      * author 3D Informatica - fgr<br>
//      *
//      * @param conn      Connessione da utilizzare.
//      * @param query     La query da effettuare.
//      * @param locked    Indica se bloccare o meno il documento.
//      * @param attempts  Numero di tentativi da effettuare in caso di fallimento del
//      *                  caricamento del documento.
//      * @param delay     Intervallo intercorrente tra i tentativi di caricamento del
//      *                  documento in caso di fallimento (in millisecondi).
//      *
//      * @return Un array di tre oggetti: il documento caricato (XMLDocumento), il numero fisico del documento (Integer) e il
//      *         suo eventuale codice di lock (String).
//      *
//      * @throws Exception in caso di errore.
//      *
//      */
//     public static Object[] loadSingleDoc(Connessione conn,
//                                          String query,
//                                          boolean locked,
//                                          int attempts,
//                                          int delay) throws Exception {
//
//         log.debug("GenericUtils.loadSingleDoc(): using connection: " + conn.getConnection());
//
//         QueryResult qr = doXWQuery(conn, query);
//
//         if (qr == null) {
//             log.error("GenericUtils.loadSingleDoc(): document not found!");
//             throw new Exception("Document not found!");
//         }
//
//         if (qr.elements > 1) {
//             log.error("GenericUtils.loadSingleDoc(): found " + qr.elements + " documents!");
//             throw new Exception("Found " + qr.elements + " documents!");
//         }
//
//         log.debug("GenericUtils.loadSingleDoc(): loading found document [locked is: " + locked + "]");
//
//         XmlDoc xmlDoc = new XmlDoc(qr.id, 0, -1, conn);
//         int bits = 0;
//
//         if (locked) bits = 1;
//
//         // fcossu - federico 07/09/06: introdotta ripetizione dell'operazione di caricamento in caso di errore
//         // Federico 19/03/07:          resi parametrici il numero di tentativi e il delay [RW 0042308]
//         xmlDoc.load(bits, attempts, delay);
//
//         Object[] res = new Object[3];
//
//         res[0] = xmlDoc.getDocument();
//         res[1] = new Integer(xmlDoc.getPhysDoc());
//
//         if (locked) res[2] = xmlDoc.getLock();
//         else        res[2] = null;
//
//         return res;
//     }
//
//     /**
//      * Recupera un documento da un oggetto QueryResult [RW 0037104].
//      * <br><br>
//      *
//      * author 3D Informatica - fgr<br>
//      *
//      * @param conn      Connessione da utilizzare.
//      * @param arch      Nome logico dell'archivio.
//      * @param result    Oggetto QueryResult da cui estrarre il documento.
//      * @param position  Posizione nella selezione del documento.
//      * @param ext       Stringa con ulteriori opzioni per il caricamento
//      *
//      * @return Il documento richiesto.
//      *
//      * @throws Exception in caso di errore.
//      *
//      */
//     public static Doc getDoc(Connessione conn,
//                              String arch,
//                              QueryResult result,
//                              int position,
//                              String ext) throws Exception {
//
//         return conn.getBroker().getDoc(conn.getConnection(),
//                                        arch,
//                                        result,
//                                        position,
//                                        it.highwaytech.broker.ServerCommand.subcmd_NONE,
//                                        ext);
//     }
//
//     /**
//      * Carica un documento dato il suo nrecord [RW 0037105].
//      * <br><br>
//      *
//      * author 3D Informatica - fgr<br>
//      *
//      * @param conn      Connessione da utilizzare.
//      * @param nrecord   Nrecord del documento.
//      * @param locked    Indica se bloccare o meno il documento.
//      *
//      * @return Un array di tre oggetti: il documento caricato (XMLDocumento), il numero fisico del documento (Integer) e il
//      *         suo eventuale codice di lock (String).
//      *
//      * @throws Exception in caso di errore.
//      *
//      */
//     public static Object[] loadDocByNrecord(Connessione conn,
//                                             String nrecord,
//                                             boolean locked) throws Exception {
//
//         return loadDocByNrecord(conn, nrecord, locked, 10, 3000);
//     }
//
//     /**
//      * Carica un documento dato il suo nrecord [RW 0037105].
//      * <br><br>
//      *
//      * author 3D Informatica - fgr<br>
//      *
//      * @param conn      Connessione da utilizzare.
//      * @param nrecord   Nrecord del documento.
//      * @param locked    Indica se bloccare o meno il documento.
//      * @param attempts  Numero di tentativi da effettuare in caso di fallimento del
//      *                  caricamento del documento.
//      * @param delay     Intervallo intercorrente tra i tentativi di caricamento del
//      *                  documento in caso di fallimento (in millisecondi).
//      *
//      * @return Un array di tre oggetti: il documento caricato (XMLDocumento), il numero fisico del documento (Integer) e il
//      *         suo eventuale codice di lock (String).
//      *
//      * @throws Exception in caso di errore.
//      *
//      */
//     public static Object[] loadDocByNrecord(Connessione conn,
//                                             String nrecord,
//                                             boolean locked,
//                                             int attempts,
//                                             int delay) throws Exception {
//
//         log.debug("GenericUtils.loadDocByNrecord(): using connection: " + conn.getConnection());
//
//         // Federico 09/10/06: ristrutturato codice [RW 0039705]
//         String query = "[docnrecord]=\"" + nrecord + "\"";
//
//         // Federico 19/03/07: resi parametrici il numero di tentativi e il delay [RW 0042308]
//         return loadSingleDoc(conn, query, locked, attempts, delay);
//     }
//
//     /**
//      * Questo metodo verifica la presenza del file di lock 'lockFile'.<br>
//      * Se il file esiste, il metodo si mette in attesa per 'lockSleepTime' ms e riprova per
//      * un massimo di 'maxLockAttempts' volte. In caso di fallimento, viene generata un'eccezione.<br>
//      * Se il file non esiste, viene creato.
//      * <br><br>
//      *
//      * author 3D Informatica - fgr<br>
//      *
//      * @param lockFile        Path/URI del file di lock. Se comincia con "uri:", significa che � stato indicato
//      *                        un URI al posto del path.
//      * @param maxLockAttempts Numero di tentativi di lettura del file di lock: se il lock viene rilevato per
//      *                        'maxLockAttempts' volte, il metodo di controllo del lock fallisce.
//      * @param lockSleepTime   Tempo di attesa (ms) tra un tentativo di rilevamento del lock e quello successivo.
//      *
//      * @return Un oggetto File che rappresenta il file di lock.
//      *
//      * @throws Exception in caso di errore (timeout per il controllo del lock scaduto o impossibile creare
//      *         il file di lock).
//      *
//      */
//     public static File checkLockFile(String lockFile, int maxLockAttempts, long lockSleepTime) throws Exception {
//         log.debug("GenericUtils.checkLockFile(): checking lock file: " + lockFile);
//
//         File lock = createFileObj(lockFile);
//
//         if (lock == null) {
//             // non � possibile creare/aprire il file di lock o 'lockFile' esiste ma non � un file
//             log.error("GenericUtils.checkLockFile(): Not able to handle lock file!");
//             throw new Exception("Not able to handle lock file!");
//         }
//
//         // controllo dell'esistenza del lock
//         int counter = 0;
//
//         for (; counter < maxLockAttempts; counter++) {
//             if (lock.exists()) {
//                 // il file di lock esiste --> wait
//                 try {
//                     Thread.sleep(lockSleepTime);
//                 }
//                 catch (InterruptedException e) {
//                     log.error("GenericUtils.checkLockFile(): got exception waiting", e);
//                 }
//             }
//             else {
//                 break;
//             }
//         }
//
//         if (counter == maxLockAttempts) {
//             // il file di lock � presente da troppo tempo --> errore
//             log.error("GenericUtils.checkLockFile(): lock file timeout expired --> error [lockSleepTime=" +
//                       String.valueOf(lockSleepTime) + ", MAX_LOCK_ATTEMPTS=" +
//                       String.valueOf(maxLockAttempts) + "]");
//             throw new Exception("lock file timeout expired");
//         }
//
//         // creazione del file di lock
//         log.debug("GenericUtils.checkLockFile(): creating lock file...");
//
//         if (lock.createNewFile()) {
//             // tutto � andato bene
//             return lock;
//         }
//         else {
//             // non � stato possibile creare il file di lock
//             log.error("GenericUtils.checkLockFile(): not able to create lock file --> error");
//             throw new Exception("not able to create lock file");
//         }
//     }
//
//     /**
//      * Questo metodo crea un oggetto File a partire dal suo path/URI.
//      * <br><br>
//      *
//      * author 3D Informatica - fgr<br>
//      *
//      * @param file Path/URI del file. Se comincia con "uri:", significa che � stato indicato
//      *             un URI al posto del path.
//      *
//      * @return Un oggetto File; null in caso di errore.
//      *
//      */
//     public static File createFileObj(String file) {
//         File fileObj = null;
//
//         if (file != null && file.length() > 0) {
//             try {
//                 if (file.startsWith("uri:")) {
//                     // � stato indicato un URI
//                     fileObj = new File(new URI(file.substring(4)));
//                 }
//                 else {
//                     // � stato indicato un path
//                     fileObj = new File(file);
//                 }
//             }
//             catch (Exception ex) {
//                 log.error("GenericUtils.createFileObj(): got exception:", ex);
//             }
//         }
//
//         return fileObj;
//     }
//
//     public static byte[] getAttach(BinData binData) throws Exception {
//         // ripulitura dei dati (Federico 03/10/06: usare sempre 'binData.length'!!! [RW 0035920])
//         byte[] b = binData.content;
////         int dataOffset = 0;
////System.out.print("b.length:"+b.length+"\nbinData.length:"+binData.length+"\n"+Text.bytesToHex(b));
////         while (b[dataOffset] != 0) dataOffset++;
////         dataOffset++;
//         int dataOffset = binData.offset;
//
//         byte[] outB = new byte[binData.length];
//         int pos = 0, end = dataOffset + binData.length;
//
//         while (dataOffset < end) {
//             outB[pos] = b[dataOffset];
//             pos++;
//             dataOffset++;
//         }
//
//         log.debug("GenericUtils.getAttach(): returning " + outB.length + " bytes");
//
//         return outB;
//     }
//
//     /**
//      * Recupera un allegato chiedendolo ad extraway.
//      * <br><br>
//      *
//      * author 3D Informatica - fgr<br>
//      *
//      * @param xwId Id del file da chiedere ad extraway.
//      * @param conn Connessione da utilizzare (ristrutturazione RW 0042308).
//      *
//      * @return Il contenuto del file caricato.
//      *
//      * @throws Exception in caso di errore.
//      *
//      */
//     public static byte[] getAttach(String xwId, Connessione conn) throws Exception {
//         log.debug("GenericUtils.getAttach(): file's id: " + xwId);
//         log.debug("GenericUtils.getAttach(): conn: " + conn);
//
//         if (xwId != null && xwId.length() > 0 && conn != null && conn.isConnected()) {
//             int connection = conn.getConnection();
//             String db = conn.getDb();
//
//             log.debug("GenericUtils.getAttach(): loading file \"" + xwId + "\" using connection " +
//                       connection + " to db \"" + db + "\"...");
//
//             // richiedo l'allegato
//             BinData binData;
//
//             try {
//                 Broker broker = conn.getBroker();
//                 binData = broker.getAttachData(connection, db, xwId);
//             }
//             catch (Exception ex) {
//                 log.error("GenericUtils.getAttach(): got exception:", ex);
//                 throw ex;
//             }
//
//             log.debug("GenericUtils.getAttach(): file loaded");
//
//             return getAttach(binData);
//
//         }
//         else {
//             // id null/vuoto o connessione null/non connessa --> non � possibile caricare il file
//             throw new Exception("Null/empty file's id or connection null/not connected!");
//         }
//     }
//
//     /**
//      * Effettua l'upload di un allegato in un db extraway.
//      * <br><br>
//      *
//      * author 3D Informatica - fgr<br>
//      *
//      * @param attachment File di cui fare upload.
//      * @param name       Nome del file. Questo parametro serve solo per capire qual'� <u>l'estensione del file</u>.
//      *                   Se, infatti, 'name' � privo di estensione, <u>anche l'id restituito lo �</u>.
//      * @param conn       Connessione da utilizzare (<u>deve essere gi� connessa</u>).
//      *
//      * @return L'id creato da extraway associato all'allegato.
//      *
//      * @throws Exception in caso di errore nell'upload o se:<br><br>
//      *
//      *         <ul><li>attachment.length == 0</li>
//      *         <li>name == null || name.length() == 0</li>
//      *         <li>!conn.connected</i></ul>
//      *
//      */
//     public static String uploadAttach(byte[] attachment, String name, Connessione conn) throws Exception {
//         if (attachment.length == 0) throw new Exception("Attachment dimension is 0!");
//
//         if (name == null || name.length() == 0) throw new Exception("Null or empty name not accepted!");
//
//         if (!conn.connected) throw new Exception("Connection not connected!");
//
//         Broker broker = conn.getBroker();
//
//         return broker.addAttach(conn.getConnection(), conn.getDb(), attachment, name);
//     }
//
//     /**
//      * Recupera un'immagine chiedendola ad extraway.
//      * <br><br>
//      *
//      * <b>Nota:</b> E' possibile produrre un oggetto <i>java.awt.image.BufferedImage</i>
//      *              solo per i formati <b>BMP</b>, <b>JPG</b>, <b>GIF</b> e <b>PNG</b>.
//      *              Il <b>TIFF</b> <u>non viene gestito</u>.
//      * <br><br>
//      *
//      * author 3D Informatica - fgr<br>
//      *
//      * @param xwId Id del file da chiedere ad extraway (questo file deve essere un'immagine).
//      * @param conn Connessione da utilizzare.
//      *
//      * @return Un oggetto <i>java.awt.image.BufferedImage</i>; null se il tipo dell'immagine
//      *         non viene gestito.
//      *
//      * @throws Exception in caso di errore.
//      *
//      */
//     public static BufferedImage getImageFromXwDbAsBufferedImage(String xwId, Connessione conn) throws Exception {
//         log.debug("GenericUtils.getImageFromXwDbAsBufferedImage(): loading image \"" + xwId + "\" using connection " +
//                   conn.getConnection() + "...");
//
//         byte[] imgBytes = getAttach(xwId, conn);
//         ByteArrayInputStream is = new ByteArrayInputStream(imgBytes);
//         BufferedImage image = ImageIO.read(is);
//
//         if (image != null) {
//             log.debug("GenericUtils.getImageFromXwDbAsBufferedImage(): returning an image of resolution: " +
//                       image.getWidth() + "x" + image.getHeight() + " pixel");
//         }
//         else {
//             log.error("GenericUtils.getImageFromXwDbAsBufferedImage(): type of image not handled! Returning null...");
//         }
//
//         return image;
//     }
//
//     /**
//      * Restituisce la differenza in millisecondi tra due oggetti di tipo di GregorianCalendar.
//      * <br><br>
//      *
//      * author 3DInformatica - ss<br>
//      *
//      * @param aTime Minuendo.
//      * @param bTime Sottrattore.
//      *
//      * @return Differenza tra aTime e bTime in millisecondi.
//      *
//      */
//     public static long computeDiffTime(GregorianCalendar aTime, GregorianCalendar bTime) {
//         long aTLong = aTime.getTimeInMillis();
//         long bTLong = bTime.getTimeInMillis();
//
//         return aTLong - bTLong;
//     }
//
//     /**
//      * Converte un oggetto in un array di byte.
//      * <br><br>
//      *
//      * author 3DInformatica - fgr<br>
//      *
//      * @param obj Oggetto da convertire.
//      *
//      * @return L'oggetto convertito in un array di byte.
//      *
//      * @throws IOException in caso di errore.
//      *
//      */
//     public static byte[] objectToArray(Object obj) throws IOException {
//         ByteArrayOutputStream baos = new ByteArrayOutputStream();
//         ObjectOutputStream oos = new ObjectOutputStream(baos);
//         oos.writeObject(obj);
//         byte[] data = baos.toByteArray();
//         oos.close();
//
//         return data;
//     }
//
//     /**
//      * Calcola e restituisce l'impronta dell'oggetto indicato.
//      * <br><br>
//      *
//      * author 3DInformatica - fgr<br>
//      *
//      * @param algorithm         Nome dell'algoritmo (hash) per calcolare l'impronta (RIPEMD160, SHA1...).
//      * @param obj               Oggetto di cui si vuole calcolare l'impronta.
//      * @param retValCharsetName Charset da usare per la codifica della stringa da restituire. Se null o
//      *                          vuoto, viene utilizzato il charset di default del sistema.
//      *
//      * @return La stringa che rappresenta l'impronta calcolata.
//      *
//      * @throws IOException                  in caso di errore nella conversione dell'oggetto 'obj' in un
//      *                                      array di byte.
//      * @throws NoSuchAlgorithmException     se l'algoritmo indicato non e' presente.
//      * @throws UnsupportedEncodingException se <i>retValCharsetName</i> non e' supportato.
//      *
//      */
//     public static String computePrint(String algorithm,
//                                       Object obj,
//                                       String retValCharsetName) throws IOException,
//                                                                        NoSuchAlgorithmException,
//                                                                        UnsupportedEncodingException {
//         byte[] data = objectToArray(obj);
//
//         return computePrint(algorithm, data, retValCharsetName);
//     }
//
//     /**
//      * Calcola e restituisce l'impronta dell'array <i>data</i>.
//      * <br><br>
//      *
//      * author 3DInformatica - fgr<br>
//      *
//      * @param algorithm         Nome dell'algoritmo (hash) per calcolare l'impronta (RIPEMD160, SHA1...).
//      * @param data              Dati di cui si vuole calcolare l'impronta.
//      * @param retValCharsetName Charset da usare per la codifica della stringa da restituire. Se null o
//      *                          vuoto, viene utilizzato il charset di default del sistema.
//      *
//      * @return La stringa che rappresenta l'impronta calcolata.
//      *
//      * @throws NoSuchAlgorithmException     se l'algoritmo indicato non e' presente.
//      * @throws UnsupportedEncodingException se <i>retValCharsetName</i> non e' supportato.
//      *
//      */
//     public static String computePrint(String algorithm,
//                                       byte[] data,
//                                       String retValCharsetName) throws NoSuchAlgorithmException,
//                                                                        UnsupportedEncodingException {
//
//         log.debug("GenericUtils.computePrint(): algorithm: " + algorithm);
//         log.debug("GenericUtils.computePrint(): retValCharsetName: " + retValCharsetName);
//
//         MessageDigest md = MessageDigest.getInstance(algorithm);
//         byte[] dg = md.digest(data);
//         byte[] encoded_dg = (new Base64()).encode(dg);
//         String retVal;
//
//         if (retValCharsetName != null && retValCharsetName.trim().length() > 0) {
//             retVal = new String(encoded_dg, retValCharsetName.trim());
//         }
//         else {
//             retVal = new String(encoded_dg);
//         }
//
//         log.debug("GenericUtils.computePrint(): returning: " + retVal);
//
//         return retVal;
//     }
//
//     /**
//      * Restituisce un oggetto Date data una stringa.
//      * <br><br>
//      *
//      * author 3D Informatica - fcossu<br>
//      *
//      * @param stringDate   La stringa da convertire.
//      *
//      * @return Date rappresentante la data.
//      *
//      * @throws Exception in caso di errore.
//      *
//      */
//     public static Date stringToDate(String stringDate) throws Exception {
//
//         stringDate = XmlReplacer.normalizeDate(stringDate);
//         DateFormat df = new SimpleDateFormat("yyyyMMdd");
//         Date date = df.parse(stringDate);
//
//         return date;
//     }
//
//     /**
//      * Restituisce un oggetto GregorianCalendar a partire da una data e un tempo in formato stringa.
//      * <br><br>
//      *
//      * author 3D Informatica - fgr
//      *
//      * @param date Data (formati accettati: gg?mm?aaaa e aaaammgg).
//      * @param time Tempo (formato accettato: hh:mm:ss). Se null/vuoto o malformato, il parametro viene ignorato.
//      *
//      * @return Un oggetto GregorianCalendar rappresentante la data e il tempo indicati.
//      *
//      * @throws Exception in caso di errore.
//      *
//      */
//     public static GregorianCalendar stringToGregorianCalendar(String date, String time) throws Exception {
//         int year, month, dayOfMonth, hourOfDay, minute, second;
//
//         date       = XmlReplacer.normalizeDate(date);
//         year       = Integer.parseInt(date.substring(0, 4));
//         month      = Integer.parseInt(date.substring(4, 6)) - 1;
//         dayOfMonth = Integer.parseInt(date.substring(6));
//
//         if (time != null && time.length() == 8) {
//             hourOfDay = Integer.parseInt(time.substring(0, 2));
//             minute    = Integer.parseInt(time.substring(3, 5));
//             second    = Integer.parseInt(time.substring(6));
//
//             return new GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute, second);
//         }
//         else {
//             return new GregorianCalendar(year, month, dayOfMonth);
//         }
//     }
//
//     /**
//      * Converte un id di file highway firmato in un id extraway fatto nel seguente modo 98XXXXXX
//      * @param fileId
//      * @return
//      */
//     public static String convertFileIdForHighwaySignedFiles(String fileId) {
//         if (fileId.toLowerCase().startsWith("f_")) {
//             fileId = fileId.substring(2);
//
//             if (fileId.toLowerCase().startsWith("f_")) {
//                 fileId = fileId.substring(2);
//             }
//
//             // Federico 24/03/09: 'fileId' contiene le estensioni del file (es. ".doc.p7m") --> il filling
//             // con 0 deve essere fatto solo sull'id numerico effettivo [M 0000180]
//             String fileExt = "";
//             int pos        = fileId.indexOf(".");
//
//             if (pos != -1) {
//                 fileExt = fileId.substring(pos);
//                 fileId  = fileId.substring(0, pos);
//             }
//
//             fileId = "98" + XmlReplacer.fillChars(fileId, "0", 6) + fileExt;
//
//         }
//         return fileId;
//     }
//
//     /**
//      * Cripta il file id indicato.
//      * <br><br>
//      *
//      * author 3D Informatica - fgr<br>
//      *
//      * @param fileId File id da criptare.
//      *
//      * @return Il file id criptato; una stringa vuota se <i>fileId</i> e' null o vuoto; null in caso di errore.
//      *
//      */
//     public static String encryptFileId(String fileId) {
//         if (fileId != null && fileId.length() > 0) {
//             // nelle versioni DCW 2 gli allegati firmati hanno il prefisso F_ nel nome
//             fileId = convertFileIdForHighwaySignedFiles(fileId);
//
//             int pos = fileId.lastIndexOf("\\");
//
//             if (pos == -1) pos = fileId.lastIndexOf("/");
//
//             if (pos == -1) pos = 0;
//
//             pos = fileId.indexOf(".", pos);
//
//             // Federico 09/04/09: introdotta inizializzazione della classe CryptoUtils (che non � detto che sia stata inizializzata) [RW 0057587]
//             CryptoUtils.init();
//
//             if (pos == -1) {
//                 return CryptoUtils.encrypt(fileId);
//             }
//             else {
//                 String encryptedId = CryptoUtils.encrypt(fileId.substring(0, pos));
//
//                 if (encryptedId != null) return encryptedId + fileId.substring(pos);
//                 else                     return null;
//             }
//         }
//         else {
//             return "";
//         }
//     }
//
//     /**
//      * Decripta il file id indicato.
//      * <br><br>
//      *
//      * author 3D Informatica - fgr<br>
//      *
//      * @param fileId File id da decriptare.
//      *
//      * @return Il file id decriptato; una stringa vuota se <i>fileId</i> e' null o vuoto; null in caso di errore.
//      *
//      */
//     public static String decryptFileId(String fileId) {
//         if (fileId != null && fileId.length() > 0) {
//             int pos = fileId.indexOf(".");
//
//             // Federico 09/04/09: introdotta inizializzazione della classe CryptoUtils (che non � detto che sia stata inizializzata) [RW 0057587]
//             CryptoUtils.init();
//
//             if (pos == -1) {
//                 return CryptoUtils.decrypt(fileId);
//             }
//             else {
//                 String decryptedId = CryptoUtils.decrypt(fileId.substring(0, pos));
//
//                 if (decryptedId != null) return decryptedId + fileId.substring(pos);
//                 else                     return null;
//             }
//         }
//         else {
//             return "";
//         }
//     }
//
//     /**
//      * Trasforma il Vector di stringhe numeriche <i>v</i> in un Vector di oggetti Integer
//      * in ordine crescente, eliminando gli eventuali valori nulli o vuoti presenti in <i>v</i>.
//      * <br><br>
//      *
//      * author 3D Informatica - fgr<br>
//      *
//      * @param v Il Vector di stringhe numeriche da trasformare e ordinare.
//      *
//      * @throws NumberFormatException se viene incontrata una stringa non numerica.
//      *
//      * @return Un Vector di oggetti Integer in ordine crescente.
//      *
//      */
//     public static Vector orderNumericStringVector(Vector v) throws NumberFormatException {
//         ArrayList<Integer> intArr = new ArrayList<Integer>();
//
//         for (int j = 0; j < v.size(); j++) {
//             String numberStr = (String)v.get(j);
//
//             if (numberStr == null || numberStr.length() == 0) continue;
//
//             intArr.add(new Integer(numberStr));
//         }
//
//         if (intArr.size() > 0) {
//             Object[] arrToBeSorted = intArr.toArray();
//             Arrays.sort(arrToBeSorted);
//
//             return new Vector(Arrays.asList(arrToBeSorted));
//         }
//         else {
//             return new Vector();
//         }
//     }

     /**
     * Autore: simone - 9 Set 2004<br><br>
     * Formatta una data nel formato gg/mm/aaaa<br>
     * Formati di ingresso riconosciuti:<br><br>
     *  gg?mm?aaaa con (? = qualsiasi carattere separatore)<br>
     *  aaaammgg<br>
     * @param s data in input
     * @return data formattata
     */
    public static String dateFormat(String s) {
         // Federico 13/01/06: introdotto test per evitare eccezioni in caso di stringa vuota/null
         if (s == null || s.length() == 0) return s;

        if (s.length() == 8) {
            return s.substring(6, 8) + "/" + s.substring(4, 6) + "/" + s.substring(0, 4);
        }
        // Federico 02/07/07: reso piu' robusto tua sorella il codice [RW 0045047]
        else if (s.length() == 10) {
            return s.substring(0, 2) + "/" + s.substring(3, 5) + "/" + s.substring(6, 10);
        }
        else {
            log.warn("GenericUtils.dateFormat(): unknown date format! [" + s + "]");
            return s;
        }
    }

    /**
     * Autore: simone - 9 Set 2004
     * Elimina gli zeri in testa a una stringa. Se la string � fatta di tutti zeri restituisce "0"
     * @param s stringa da elaborare
     * @return stringa elaborata
     */
    public static String trimLeftZeros(String s) {
        if ( s!= null && s.length() > 0 )
            while ( s.length() > 0 && s.charAt(0) == '0' )
                s = s.substring(1);

        if ( s.length() == 0 )
            return "0";

        return s;
    }

//    /**
//     * Data la matricola di una persona interna restituisce il nome della Struttura Interna
//     * cui appartiene.
//     *
//     * @param connessione   La connessione da utilizzare
//     * @param matricola     La matricola della persona interna
//     * @param persIntNome   Se impostato a true al nome della struttura interna verr� accodato
//     *                      ":nome della persona interna"
//     *
//     * @return  Il nome della struttura interna seguito da ":nome persona interna" (se richiesto)
//     */
//    public static String getStruIntNomeFromPersIntMatricola(Connessione connessione, String matricola, boolean persIntNome) throws Exception {
//        String struIntNome = null;
//
//        //Ricerco la persona interna con questa matricola
//        String query = "[PERSINT_MATRICOLA]=\""+matricola+"\"";
//        Selezione selezione = new Selezione(connessione);
//        int count = selezione.Search(query, null, "", 0, 0);
//
//        if (count != 1) {
//         log.error("GenericUtils.getStruIntNomeFromPersIntMatricola: Numero di persone interne trovate (" + count + ") non corretto!");
//         throw new Exception("Numero di persone interne trovate (" + count + ") non corretto!");
//        }
//
//        //identificata univocamente la persona interna
//        XmlDoc persIntXDoc = new XmlDoc(selezione.getSelId(), 0, -1, connessione);
//        persIntXDoc.load();
//        XMLDocumento persIntXMLDoc = persIntXDoc.getDocument();
//
//        //recupero il codice della struttura interna cui la persona appartiene
//        String codDitta = persIntXMLDoc.getAttributeValue("/persona_interna/@cod_uff");
//        query = "[STRUINT_CODUFF]=\""+codDitta+"\"";
//        count = selezione.Search(query, null, "", 0, 0);
//
//        if (count != 1) {
//            log.error("GenericUtils.getStruIntNomeFromPersIntMatricola: Numero di strutture interne trovate (" + count + ") non corretto!");
//            throw new Exception("Numero di strutture interne trovate (" + count + ") non corretto!");
//        }
//
//        //identificata univocamente la struttura interna
//        XmlDoc struIntXDoc = new XmlDoc(selezione.getSelId(), 0, -1, connessione);
//        struIntXDoc.load();
//        XMLDocumento struIntXMLDoc = struIntXDoc.getDocument();
//
//        struIntNome = struIntXMLDoc.getElementText("/struttura_interna/nome");
//
//        if (persIntNome) {
//            struIntNome += ":"+persIntXMLDoc.getAttributeValue("/persona_interna/@cognome")+" "+persIntXMLDoc.getAttributeValue("/persona_interna/@nome");
//        }
//
//        return struIntNome;
//    }
//
//    /**
//     * Dato il nome completo (cognome nome) di una persona interna restituisce il nome della struttura interna a
//     * cui appartiene. [M 0000222]
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param connessione   La connessione da utilizzare.
//     * @param nomeCompleto  Nome completo della persona interna (cognome nome).
//     *
//     * @return  Il nome della struttura interna o <code>null</code> se vengono trovate pi� persone o se la persona
//     *          non viene trovata.
//     *
//     */
//    public static String getStruIntNomeFromPersIntNome(Connessione connessione, String nomeCompleto) throws Exception {
//        String struIntNome = null;
//
//        // ricerco la persona interna
//        String query        = "([/persona_interna/#cgnm/]=\"" + nomeCompleto + "\")";
//
//        log.debug("GenericUtils.getStruIntNomeFromPersIntNome: querying: " + query);
//
//        Selezione selezione = new Selezione(connessione);
//        int count           = selezione.Search(query, null, "", 0, 0);
//
//        if (count != 1) {
//            log.error("GenericUtils.getStruIntNomeFromPersIntNome: found " + count + " persint!");
//
//            return null;
//        }
//
//        // identificata univocamente la persona interna
//        log.debug("GenericUtils.getStruIntNomeFromPersIntNome: loading persint...");
//
//        XmlDoc persIntXDoc         = new XmlDoc(selezione.getSelId(), 0, -1, connessione);
//        persIntXDoc.load();
//        XMLDocumento persIntXMLDoc = persIntXDoc.getDocument();
//
//        // recupero il codice della struttura interna a cui la persona appartiene
//        String codStruInt = persIntXMLDoc.getAttributeValue("/persona_interna/@cod_uff");
//        query             = "[STRUINT_CODUFF]=\"" + codStruInt + "\"";
//
//        log.debug("GenericUtils.getStruIntNomeFromPersIntNome: querying: " + query);
//
//        count             = selezione.Search(query, null, "", 0, 0);
//
//        if (count != 1) {
//            log.error("GenericUtils.getStruIntNomeFromPersIntNome: found " + count + " struint!");
//
//            return null;
//        }
//
//        // identificata univocamente la struttura interna
//        log.debug("GenericUtils.getStruIntNomeFromPersIntNome: loading struint...");
//
//        XmlDoc struIntXDoc         = new XmlDoc(selezione.getSelId(), 0, -1, connessione);
//        struIntXDoc.load();
//        XMLDocumento struIntXMLDoc = struIntXDoc.getDocument();
//
//        struIntNome = struIntXMLDoc.getElementText("/struttura_interna/nome");
//
//        log.debug("GenericUtils.getStruIntNomeFromPersIntNome: returning: " + struIntNome);
//
//        return struIntNome;
//    }
//
//    /**
//     * Recupera le informazioni sul responsabile di un ufficio.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param connessione La connessione da utilizzare (<b>Nota</b>: deve essere gia' connessa al db dell'anagrafica).
//     * @param struintCode Il codice della struttura interna di cui si vuole trovare il responsabile.
//     *
//     * @return Un xml (DOM) con radice <i>rif</i> che contiene le informazioni sul responsabile dell'ufficio,
//     *         avente la stessa struttura di un riferimento interno.<br>
//     *         <code>null</code> se non e' possibile trovare il responsabile.
//     *
//     * @throws Exception in caso di errore.
//     *
//     */
//    public static Document getStruIntRespAsRif(Connessione connessione, String struintCode) throws Exception {
//        String query = "[/struttura_interna/@cod_uff/]=\"" + struintCode + "\"";
//
//        log.debug("GenericUtils.getStruIntRespAsRif(): executing query: " + query);
//
//        Document result = null;
//        Selezione sel   = new Selezione(connessione);
//        int elements    = sel.Search(query, null, "", 0, 0);
//
//        if (elements == 1) {
//            log.debug("GenericUtils.getStruIntRespAsRif(): loading struint...");
//
//            XmlDoc xDoc = new XmlDoc(sel.getSelId(), 0, -1, connessione);
//            xDoc.load();
//
//            XMLDocumento struintDoc = xDoc.getDocument();
//            String codResponsabile  = struintDoc.getRootElement().attributeValue("cod_responsabile", "");
//
//            log.debug("GenericUtils.getStruIntRespAsRif(): codResponsabile: " + codResponsabile);
//
//            if (codResponsabile.length() > 0) {
//                query = "[persint_matricola]=\"" + codResponsabile + "\"";
//
//                log.debug("GenericUtils.getStruIntRespAsRif(): executing query: " + query);
//
//                elements  = sel.Search(query, null, "", 0, 0);
//
//                if (elements == 1) {
//                    log.debug("GenericUtils.getStruIntRespAsRif(): loading persint...");
//
//                    xDoc = new XmlDoc(sel.getSelId(), 0, -1, connessione);
//                    xDoc.load();
//
//                    XMLDocumento responsabileDoc = xDoc.getDocument();
//
//                    // preparazione del DOM contenente le informazioni sul responsabile
//                    result        = DocumentHelper.parseText("<?xml version=\"1.0\" encoding=\"" + XMLDocumento.ENCODING + "\"?>" +
//                                                             "<rif/>");
//                    Element rifEl = result.getRootElement();
//
//                    rifEl.addAttribute("nome_persona",
//                                       responsabileDoc.getRootElement().attributeValue("cognome") + " " +
//                                       responsabileDoc.getRootElement().attributeValue("nome"));
//                    rifEl.addAttribute("cod_persona", codResponsabile);
//
//                    String codUffResp = responsabileDoc.getRootElement().attributeValue("cod_uff");
//                    rifEl.addAttribute("cod_uff", codUffResp);
//
//                    if (codUffResp.equals(struintCode)) {
//                        // il responsabile appartiene allo stesso ufficio
//                        rifEl.addAttribute("nome_uff", struintDoc.getElementText("/" + struintDoc.getIUName() + "/nome"));
//                    }
//                    else {
//                        // il responsabile appartiene ad un altro ufficio
//                        rifEl.addAttribute("nome_uff",
//                                           GenericUtils.getStruIntNomeFromPersIntMatricola(connessione,
//                                                                                           codResponsabile,
//                                                                                           false));
//                    }
//                }
//                else {
//                    log.error("GenericUtils.getStruIntRespAsRif(): not able to find persint \"" + codResponsabile +
//                              "\" [docs found: " + elements + "]");
//                }
//            }
//            else {
//                log.error("GenericUtils.getStruIntRespAsRif(): 'cod_responsabile' not found in struint \"" + struintCode + "\"!");
//            }
//        }
//        else {
//            log.error("GenericUtils.getStruIntRespAsRif(): not able to find struint \"" + struintCode + "\" [docs found: " + elements + "]");
//        }
//
//        return result;
//    }
//
//    /**
//     * Recupera i nomi degli uffici data un elenco dei loro codici.
//     * <br><br>
//     *
//     * author 3D Informatica - fcossu
//     * [EA 440]
//     *
//     * @param connessione   La connessione da utilizzare (<b>Nota</b>: deve essere gia' connessa al db dell'anagrafica).
//     * @param struintCodes  I codici delle strutture interne.
//     * @param separ         Il separatore da utilizzare.
//     *
//     * @return Una Stringa contenente i nomi degli uffici; <code>null</code> se non e' possibile trovare gli uffici.
//     *
//     * @throws Exception in caso di errore.
//     *
//     */
//    public static String getStruIntNomi(Connessione connessione, String[] struintCodes, String separ) throws Exception {
//        String uffNames = "";
//
//        log.debug("GenericUtils.getStruIntNomi(): searching for " + struintCodes.length + " codes");
//        
//        for (int i=0; i < struintCodes.length; i++) {
//            String struIntNome = getStruIntNome(connessione, struintCodes[i].trim());
//            if (struIntNome != null) uffNames += (uffNames != "" ? separ : "") + struIntNome;
//        }
//        
//        return uffNames != "" ? uffNames : null;
//    }
//    
//    /**
//     * Recupera il nome di un ufficio.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param connessione La connessione da utilizzare (<b>Nota</b>: deve essere gia' connessa al db dell'anagrafica).
//     * @param struintCode Il codice della struttura interna.
//     *
//     * @return Il nome dell'ufficio; <code>null</code> se non e' possibile trovare l'ufficio.
//     *
//     * @throws Exception in caso di errore.
//     *
//     */
//    public static String getStruIntNome(Connessione connessione, String struintCode) throws Exception {
//        String query = "[/struttura_interna/@cod_uff/]=\"" + struintCode + "\"";
//
//        log.debug("GenericUtils.getStruIntNome(): executing query: " + query);
//
//        Selezione sel   = new Selezione(connessione);
//        int elements    = sel.Search(query, null, "", 0, 0);
//
//        if (elements == 1) {
//            log.debug("GenericUtils.getStruIntNome(): loading struint...");
//
//            XmlDoc xDoc = new XmlDoc(sel.getSelId(), 0, -1, connessione);
//            xDoc.load();
//
//            XMLDocumento struintDoc = xDoc.getDocument();
//
//            return struintDoc.getRootElement().elementText("nome") ;
//        }
//        log.error("GenericUtils.getStruIntNome(): not able to find struint \"" + struintCode + "\" [docs found: " + elements + "]");
//
//
//        return null;
//    }
//
//    /**
//     * Recupera i codici amm e aoo di un ufficio dato il suo codice.
//     * <br><br>
//     *
//     * author 3D Informatica - fcossu
//     *
//     * @param connessione La connessione da utilizzare (<b>Nota</b>: deve essere gia' connessa al db dell'anagrafica).
//     * @param struintCode Il codice della struttura interna.
//     *
//     * @return I codici amm e aoo dell'ufficio; <code>null</code> se non e' possibile trovare l'ufficio.
//     *
//     * @throws Exception in caso di errore.
//     *
//     */
//    public static String[] getStruIntAmmAoo(Connessione connessione, String struintCode) throws Exception {
//        String query = "[/struttura_interna/@cod_uff/]=\"" + struintCode + "\"";
//
//        log.debug("GenericUtils.getStruIntAmmAoo(): executing query: " + query);
//
//        Selezione sel   = new Selezione(connessione);
//        int elements    = sel.Search(query, null, "", 0, 0);
//
//        if (elements == 1) {
//            log.debug("GenericUtils.getStruIntAmmAoo(): loading struint...");
//
//            XmlDoc xDoc = new XmlDoc(sel.getSelId(), 0, -1, connessione);
//            xDoc.load();
//
//            XMLDocumento struintDoc = xDoc.getDocument();
//
//            return new String[]{ struintDoc.getRootElement().attributeValue("cod_amm", ""),
//                                 struintDoc.getRootElement().attributeValue("cod_aoo", "") };
//        }
//        else {
//            log.error("GenericUtils.getStruIntAmmAoo(): not able to find struint \"" + struintCode + "\" [docs found: " + elements + "]");
//        }
//
//        return null;
//    }
//
//    /**
//     * Dato il codice di una persona interna/esterna restituisce l'xml della sua struttura.
//     *
//     * @param connessione      La connessione da utilizzare (<u>deve essere gia' connessa al db ACL</u>).
//     * @param personCode       Codice della persona interna/esterna.
//     * @param isInternalPerson Indica se la persona e' interna o esterna (<code>true</code> se interna; <code>false</code>
//     *                         altrimenti).
//     *
//     * @return L'xml della struttura della persona indicata.
//     *
//     * @throws Exception in caso di errore.
//     *
//     */
//    public static XMLDocumento getStructureWithPersonCode(Connessione connessione,
//                                                          String personCode,
//                                                          boolean isInternalPerson) throws Exception {
//        String queryAlias;
//
//        if (isInternalPerson) queryAlias = "persint_matricola";
//        else                  queryAlias = "persest_matricola";
//
//        // ricerco la persona interna/esterna con il codice indicato
//        String query        = "[" + queryAlias + "]=\"" + personCode + "\"";
//        Selezione selezione = new Selezione(connessione);
//        int count           = selezione.Search(query, null, "", 0, 0);
//
//        if (count != 1) {
//            log.error("GenericUtils.getStructureWithPersonCode(): found " + count + " documents!");
//            throw new Exception("Numero di persone interne/esterne trovate (" + count + ") non corretto!");
//        }
//
//        // carico la persona
//        XmlDoc personXDoc         = new XmlDoc(selezione.getSelId(), 0, -1, connessione);
//        personXDoc.load();
//        XMLDocumento personXMLDoc = personXDoc.getDocument();
//
//        // recupero il codice della struttura della persona e cerco l'ufficio
//        String xpath;
//
//        if (isInternalPerson) xpath = "/" + personXMLDoc.getIUName() + "/@cod_uff";
//        else                  xpath = "/" + personXMLDoc.getIUName() + "/appartenenza/@cod_uff";
//
//        String structureCod = personXMLDoc.getAttributeValue(xpath);
//
//        if (isInternalPerson) queryAlias = "struint_coduff";
//        else                  queryAlias = "struest_coduff";
//
//        query = "[" + queryAlias + "]=\"" + structureCod + "\"";
//        count = selezione.Search(query, null, "", 0, 0);
//
//        if (count != 1) {
//            log.error("GenericUtils.getStructureWithPersonCode(): found " + count + " documents!");
//            throw new Exception("Numero di strutture interne/esterne trovate (" + count + ") non corretto!");
//        }
//
//        // carico la struttura
//        XmlDoc structureXDoc = new XmlDoc(selezione.getSelId(), 0, -1, connessione);
//        structureXDoc.load();
//
//        return structureXDoc.getDocument();
//    }
//
//    /**
//     * Inserisce nel XMLDocumento passato come parametro l'elenco di properties passato come paramentro.
//     * <br><br>
//     *
//     * author 3D Informatica - fcossu
//     *
//     * @param propContainer
//     * @param prop
//     * @param doc
//     */
//    public static void insertPropertiesListDoc(String propContainer, Properties prop, XMLDocumento doc) {
//        Element propList = DocumentHelper.createElement("propertiesList");
//        propList.addAttribute("property_file", propContainer);
//
//        Enumeration e = prop.keys();
//
//        while(e.hasMoreElements()) {
//            Element property = DocumentHelper.createElement("property");
//            String key = (String)e.nextElement();
//            property.addAttribute("key", key);
//            property.addAttribute("value", prop.getProperty(key));
//            propList.add(property);
//        }
//
//        doc.insertElement("propertiesList", propList);
//    }
//
//    /**
//     * Restituisce tutti i nodi (elementi o attributi) contenuti nel sottoalbero indicato che
//     * contengono del testo.<br>
//     * Vengono esclusi tutti quei nodi che contengono del testo fatto solo di whitespace e gli
//     * attributi del namespace "<i>xml:</i>".
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param doc              Documento da esplorare
//     * @param xmlTreeRootXpath Xpath che individua l'elemento radice del sottoalbero da esplorare.
//     *                         Se null o vuoto, viene presa la radice del documento.
//     *
//     * @return La lista dei nodi (elementi o attributi) contenuti nel sottoalbero indicato che
//     *         contengono del testo (whitespace esclusi).
//     *
//     */
//    public static List getNodesWithText(Document doc, String xmlTreeRootXpath) {
//        // Federico 26/05/09: migliorato logging
//        log.debug("GenericUtils.getNodesWithText(): doc:\r\n\n" + doc.asXML() + "\n");
//        log.debug("GenericUtils.getNodesWithText(): xmlTreeRootXpath: " + xmlTreeRootXpath);
//
//        Element xmlTreeRoot = null;
//        List<Node> result = new ArrayList<Node>();
//
//        if (xmlTreeRootXpath == null || xmlTreeRootXpath.length() == 0) {
//            xmlTreeRoot = doc.getRootElement();
//        }
//        else {
//            xmlTreeRoot = (Element)doc.selectSingleNode(xmlTreeRootXpath);
//        }
//
//        if (xmlTreeRoot != null) {
//            log.debug("GenericUtils.getNodesWithText(): exploring subtree with root node: " + xmlTreeRoot.getQualifiedName());
//
//            /*
//             * NOTA: per trovare gli elementi contenenti del testo basterebbe usare l'xpath:
//             *       "descendant-or-self::*[string-length(normalize-space(text()))>0]".
//             *       Purtroppo non e' possibile trovare con un xpath tutti gli attributi non vuoti
//             *       contenuti nel sottoalbero da esplorare.
//             *       Quindi, bisogna optare per una soluzione iterativa.
//             */
//            List treeContent = xmlTreeRoot.selectNodes(".//*");
//
//            for (int j = 0; j < treeContent.size(); j++) {
//                Node n = (Node)treeContent.get(j);
//                short nodeType = n.getNodeType();
//
//                if (nodeType == Node.ELEMENT_NODE) {
//                    if (n.getText().trim().length() > 0) {
//                        result.add(n);
//                    }
//
//                    // esplorazione attributi dell'elemento
//                    List attrList = n.selectNodes("./@*");
//
//                    for (int k = 0; k < attrList.size(); k++) {
//                        Attribute attr = (Attribute)attrList.get(k);
//
//                        // Federico 26/06/07: gli attributi del namespace 'xml:' devono essere saltati [RW 0044489]
//                        String attrName = attr.getQualifiedName();
//
//                        if (attrName.startsWith("xml:")) {
//                            log.debug("GenericUtils.getNodesWithText(): skipping xml namespace's attribute... ['" +
//                                      attrName + "']");
//
//                            continue;
//                        }
//
//                        if (attr.getText().trim().length() > 0) {
//                            result.add(attr);
//                        }
//                    }
//                }
//            }
//        }
//        else {
//            log.warn("GenericUtils.getNodesWithText(): xpath \"" + xmlTreeRootXpath + "\" has found 0 nodes...");
//        }
//
//        return result;
//    }
//
//    /**
//     * Restituisce true se c'e' almeno un nodo (elemento o attributo) contenuto nel sottoalbero indicato
//     * contenente del testo.<br>
//     * Vengono ignorati tutti quei nodi che contengono del testo fatto solo di whitespace.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param doc              Documento da esplorare
//     * @param xmlTreeRootXpath Xpath che individua l'elemento radice del sottoalbero da esplorare.
//     *                         Se null o vuoto, viene presa la radice del documento.
//     *
//     * @return True se c'e' almeno un nodo (elemento o attributo) contenuto nel sottoalbero indicato
//     *         contenente del testo (whitespace esclusi); false altrimenti.
//     *
//     */
//    public static boolean xmlTreeHasNodesWithText(Document doc, String xmlTreeRootXpath) {
//        List nodeList = getNodesWithText(doc, xmlTreeRootXpath);
//
//        return nodeList.size() > 0;
//    }
//
//    /**
//     * simone - 22 Nov 2004
//     * Restituisce il numero di fascicolo stile docway.
//     * Esempio:<br><br>
//     *
//     * input: 2004-3DINBOL-04/03/02.00001.00002.00004<br>
//     * output: 2004-IV/3/2.1.2.4<br><br>
//     *
//     * input: 2004-3DINBOL-00001.00002.00004<br>
//     * output: 2004-1.2.4
//     *
//     * @param numero numero del fascicolo nel formato del file xml
//     *
//     * @return numero di fascicolo stile docway
//     *
//     */
//    public static String printFascNum(String numero) {
//        String format = "R/D";
//        try {
//            format = getClassifFormat();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        return printFascNum(numero, format);
//    }
//
//    /**
//     * simone - 22 Nov 2004
//     * Restituisce il numero di fascicolo stile docway.
//     * Esempio:<br><br>
//     *
//     * input: 2004-3DINBOL-04/03/02.00001.00002.00004<br>
//     * output: 2004-IV/3/2.1.2.4<br><br>
//     *
//     * input: 2004-3DINBOL-00001.00002.00004<br>
//     * output: 2004-1.2.4
//     *
//     * @param numero        numero del fascicolo nel formato del file xml
//     * @param classifFormat formattazione
//     *
//     * @return numero di fascicolo stile docway
//     *
//     */
//    public static String printFascNum(String numero, String classifFormat) {
//        log.trace("GenericUtils.printFascNum(): numero = " + numero);
//
//        // Federico 05/07/07: introdotta gestione numeri di fascicolo privi di classificazione [RW 0045509]
//        int index;
//        String ret;
//
//        if (numero.indexOf("/") != -1) {
//            // numero di fascicolo con classificazione
//            //RW0049984 - Prendere primo '.' dopo la classificazione per possibili punti nella amm_aoo
//            index = numero.indexOf(".", numero.indexOf("/"));
//            ret = numero.substring(0, 5) + printClassif(numero.substring(13, index), classifFormat) + ".";
//        }
//        else {
//            // numero di fascicolo privo di classificazione
//            index = numero.lastIndexOf("-");
//            ret = numero.substring(0, 5);
//        }
//
//        numero = numero.substring(index + 1);
//
//        while((index = numero.indexOf(".")) != -1) {
//            ret += GenericUtils.trimLeftZeros(numero.substring(0, index)) + ".";
//            numero = numero.substring(index + 1);
//        }
//
//        ret += GenericUtils.trimLeftZeros(numero);
//
//        return ret;
//    }
//
//    /**
//     * simone - 22 Nov 2004
//     * Restituisce la classificazione stile docway.
//     * Esempio:<br><br>
//     *
//     * input: 04/03/02
//     * output: IV/3/2
//     *
//     * RW0049389 - fcossu - 07/01/2008
//     *
//     * @param classif numero del fascicolo nel formato del file xml
//     *
//     * @return numero di fascicolo stile docway
//     *
//     */
//    public static String printClassif(String classif) throws Exception {
//        String format = "R/D";
//        try {
//            format = getClassifFormat();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        return printClassif(classif, format);
//    }
//
//    /**
//     * RW0049389 - fcossu - 07/01/2008
//     * Restituisce il formato di classificazione utilizzato.
//     */
//    public static String getClassifFormat() throws Exception {
//        Properties prop = new Properties();
//        File propFile = null;
//
//        try {
//            propFile = GenericUtils.getResourceAsFile("/it.highwaytech.apps.xdocway.properties");
//        }
//        catch (Exception e) {
//           propFile = new File("/it.highwaytech.apps.xdocway.properties");
//        }
//
//        FileInputStream fis = new FileInputStream(propFile);
//        prop.load(fis);
//        String format = prop.getProperty("aspettoClassificazione", "R/D");
//        return format;
//    }
//
//    /**
//     * simone - 22 Nov 2004
//     * Restituisce la classificazione stile docway.
//     * Esempio:<br><br>
//     *
//     * input: 04/03/02
//     * output: IV/3/2
//     *
//     * @param classif numero del fascicolo nel formato del file xml
//     * @param format  formattazione
//     *
//     * @return numero di fascicolo stile docway
//     *
//     */
//    public static String printClassif(String classif, String format) {
//        if (classif.length() == 0)
//            return "";
//
//        if (format.length() == 0)
//            format = "R/D";
//
//        String titoloFormat = getPartialFormat(format);//titolo
//
//        if (titoloFormat.length() == 2)
//            format = format.substring(2);
//        else
//            format = format.substring(1);
//
//        String separator = format.substring(0, 1);//separatore
//
//        String classeFormat = getPartialFormat(format.substring(1));//classe
//
//
//        int index = classif.indexOf("/");
//        if (index == -1) //sstagni - 6 Mar 2006 - un solo livello
//            return getFormattedPart(classif, titoloFormat);
//
//        String ret = getFormattedPart(classif.substring(0, index), titoloFormat);
//
//        //classe
//        classif = classif.substring(index + 1);
//        while((index=classif.indexOf("/")) != -1) {
//            ret += separator + getFormattedPart(classif.substring(0, index), classeFormat);
//            classif = classif.substring(index + 1);
//        }
//        ret += separator + getFormattedPart(classif, classeFormat);
//        return ret;
//    }
//
//    // usato da: printClassif(String, String)
//    private static String getPartialFormat(String format) {
//        if (format.startsWith("AA"))
//            return "AA";
//        else if (format.startsWith("A"))
//            return "A";
//        else if (format.startsWith("DD"))
//            return "DD";
//        else if (format.startsWith("D"))
//            return "D";
//        if (format.startsWith("R"))
//            return "R";
//
//        return (format.length() <= 2)? "D" : "R"; //default per titolo e classe
//    }
//
//    // usato da: printClassif(String, String)
//    private static String getFormattedPart(String val, String format) {
//        String []alfa = {"0","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
//        val = trimLeftZeros(val);
//        if (format.equals("R"))
//            return XmlReplacer.arabToRoman(val);
//        else if (format.equals("DD")) {
//            if (val.equals("0"))
//                return val;
//            else
//                return XmlReplacer.fillChars(val, "0", 2);
//        }
//        else if (format.equals("D"))
//            return val;
//        else if (format.equals("AA")) {
//            int x = Integer.parseInt(val);
//            if (x <= 26 && x > 0)
//                return "A" + alfa[x];
//            else
//                return alfa[x];
//        }
//        else if (format.equals("A")) {
//            int x = Integer.parseInt(val);
//            return alfa[x];
//        }
//
//        return "";
//    }
//
//    public static String digitation(String num, boolean lower) {
//        char decimalSeparator = '.';
//        String[] pto1 = {"", "uno", "mille", "unmilione", "unmiliardo", "mille", "unmilione"};
//        String[] pto2 = {"", "", "mila", "milioni", "miliardi", "mila", "milioni"};
//        //String[] pto3 = {"", "undecimo", "uncentesimo", "unmillesimo"};
//        //String[] pto4 = {"", "decimi", "centesimi", "millesimi"};
//        int i;
//        for(i=0; num.length() > i && num.charAt(i) == '0'; ++i)
//            ;
//        int pt = num.lastIndexOf(decimalSeparator);
//        String num1 = num.substring((i==pt)?(i-1):i,(pt<0)?num.length():pt);
//        num1 = "000".substring((num1.length()%3>0)? num1.length()%3 : 3) + num1;
//        if (num1.length() == 0)
//            num1 = "0";
//        if (Integer.parseInt(num1)==0 && pt<0)
//            return (lower)?"zero":"Zero";
//        String str1 = "", str2 = "";
//        int j = num1.length()/3;
//        if (j > 6)
//            return null;
//        for(i=0; j>0; --j,i+=3){
//            if (Integer.parseInt(num1.substring(i,i+3))==1)
//                str1 += pto1[j];
//            else if (Integer.parseInt(num1.substring(i,i+3))==0 && j==4)
//                str1+=((Integer.parseInt(num1.substring(i-3,i+3))==0)?"di":"")+pto2[j];
//            else{
//                str1+=digit3(num1.substring(i,i+3));
//                if(Integer.parseInt(num1.substring(i,i+3)) > 0)
//                    str1+=pto2[j];
//            }
//        }
//        if (pt>-1){
//            String num2 = num.substring(pt+1, pt+4);
//            if (Integer.parseInt(num2,10)>0)
//                //str2=((str1)?" e ":"")+((parseInt(num2,10)>1)?(digit3("000".substring((num2.length%3)?num2.length%3:3)+num2)+pto4[num2.length]):pto3[num2.length]);
//                str2=((str1.length()>0)?"/":"")+num2;
//            else if(str1.length() == 0)
//                str2="zero";
//            if(str1.length() == 0)
//                str2=(lower)?str2:(str2.substring(0,1).toUpperCase()+str2.substring(1));
//        }
//        return ((lower)?str1:(str1.substring(0,1).toUpperCase()+str1.substring(1)))+str2;
//    }
//
//    private static String digit3(String num) {
//        String[] n0_19 = {"", "uno", "due", "tre", "quattro", "cinque", "sei", "sette", "otto", "nove", "dieci",
//                          "undici", "dodici", "tredici", "quattordici", "quindici", "sedici", "diciassette", "diciotto", "diciannove"};
//        String[] n10a = {"" , "", "venti", "trenta", "quaranta", "cinquanta", "sessanta", "settanta", "ottanta", "novanta", "cento"};
//        String[] n10b = {"", "", "vent", "trent", "quarant", "cinquant", "sessant", "settant", "ottant", "novant", "cento"};
//        String str = "";
//        if (num.charAt(0) != '0')
//            str += ((num.charAt(0)!='1')? n0_19[Integer.parseInt(String.valueOf(num.charAt(0)))] : "") + ((num.charAt(1)=='8')? n10b[10] : n10a[10]);
//        if (num.charAt(1) > '1')
//            str += ((num.charAt(2)=='1' || num.charAt(2)=='8')? n10b[Integer.parseInt(String.valueOf(num.charAt(1)))] :
//                n10a[Integer.parseInt(String.valueOf(num.charAt(1)))]) + n0_19[Integer.parseInt(String.valueOf(num.charAt(2)))];
//        else
//            str += n0_19[Integer.parseInt(num.substring(1,3),10)];
//        return str;
//    }
//
//    /**
//     * Restituisce l'id dell'ultima versione del primo allegato del documento indicato.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param document             Documento da analizzare.
//     * @param xpathAttachContainer Xpath che individua l'elemento che contiene gli allegati. Se null o
//     *                             vuoto, viene presa la radice del documento.
//     *
//     * @return L'id dell'ultima versione del primo allegato del documento; <i>null</i> se l'id non
//     *         viene trovato.
//     *
//     */
//    public static String getIdLastVersionFirstFile(XMLDocumento document, String xpathAttachContainer) {
//        return getIdLastVersionAttach(document, xpathAttachContainer, 1);
//    }
//
//    /**
//     * Restituisce l'id dell'ultima versione dell'allegato in posizione <i>attachPos</i> del
//     * documento indicato.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param document             Documento da analizzare.
//     * @param xpathAttachContainer Xpath che individua l'elemento che contiene gli allegati. Se null o
//     *                             vuoto, viene presa la radice del documento.
//     * @param attachPos            Posizione dell'allegato cercato (<i>a partire da 1</i>). Se <= 0,
//     *                             viene posto a 1.<br><br>
//     *
//     *                             <b>NOTA</b>: <i>attachPos</i> e' da riferirsi solo agli allegati di cui e'
//     *                                          possibile fare il versioning.
//     *                                          Cioe': vengono presi tutti i figli di <i>xpathAttachContainer</i>
//     *                                          che sono <i>xw:file</i> e che non possiedono l'attributo <i>der_from</i>;
//     *                                          poi, di questi viene preso l'allegato in posizione <i>attachPos</i>.
//     *
//     * @return L'id dell'ultima versione dell'allegato cercato; <i>null</i> se l'id non
//     *         viene trovato.
//     *
//     */
//    public static String getIdLastVersionAttach(XMLDocumento document, String xpathAttachContainer, int attachPos) {
//        String theId = null;
//
//        if (xpathAttachContainer == null || xpathAttachContainer.length() == 0) {
//            xpathAttachContainer = "/";
//        }
//        else if (!xpathAttachContainer.endsWith("/")) {
//            xpathAttachContainer += "/";
//        }
//
//        if (attachPos <= 0) attachPos = 1;
//
//        String xpath = xpathAttachContainer + "child::*[name()='xw:file'][not(@der_from)][" + String.valueOf(attachPos) + "]";
//
//        log.trace("GenericUtils.getIdLastVersionAttach(): using xpath: " + xpath);
//
//        Element attachEl = (Element)document.selectSingleNode(xpath);
//
//        if (attachEl != null) {
//            Element lastVerAttachEl = (Element)attachEl.selectSingleNode(".//*[name()='xw:file'][count(./*[name()='xw:file'])=0]");
//
//            if (lastVerAttachEl !=  null) theId = lastVerAttachEl.attributeValue("name");
//            else                          theId = attachEl.attributeValue("name");
//        }
//        else {
//            log.trace("GenericUtils.getIdLastVersionAttach(): 0 nodes found using xpath: " + xpath);
//        }
//
//        return theId;
//    }
//
//    /**
//     * Restituisce una lista di elementi xml <i>Element</i> che rappresentano gli xw:file (solo <u>l'ultima versione</u>
//     * e <u>non prodotti da una conversione del FCS</u>) contenuti nell'elemento individuato dal xpath
//     * <i>filesContainerXpath</i>.<br>
//     * Gli elementi della lista sono ordinati come gli allegati nel documento.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param doc                 Documento da analizzare.
//     * @param filesContainerXpath Xpath dell'elemento che contiene gli xw:file che interessano. Se null o vuoto, viene
//     *                            considerata la radice del documento.
//     *
//     * @return Una lista di elementi xml <i>Element</i>. Se non vengono trovati degli allegati, viene restituita una
//     *         lista vuota.
//     *
//     */
//    public static List getAttachLastVersionsInDocOrder(XMLDocumento doc, String filesContainerXpath) {
//        Element filesContainer;
//        ArrayList<Element> result = new ArrayList<Element>();
//
//        if (filesContainerXpath == null || filesContainerXpath.length() == 0) {
//            filesContainer = doc.getRootElement();
//        }
//        else {
//            filesContainer = (Element)doc.selectSingleNode(filesContainerXpath);
//
//            // Federico 04/05/09: meglio non lanciare eccezione se 'filesContainerXpath' non trova nulla [M 0000058]
//            if (filesContainer == null) return result;
//        }
//
//        List elementList = filesContainer.elements();
//        Element el, lastVerAttachEl;
//
//        for (int j = 0; j < elementList.size(); j++) {
//            el = (Element)elementList.get(j);
//
//            if (!el.getQualifiedName().equals("xw:file")) continue;   // l'elemento non e' un xw:file
//
//            if (el.selectSingleNode("./@der_from") != null) continue; // l'allegato e' il prodotto di una conversione
//
//            lastVerAttachEl = (Element)el.selectSingleNode(".//*[name()='xw:file'][count(./*[name()='xw:file'])=0]");
//
//            if (lastVerAttachEl !=  null) result.add(lastVerAttachEl);
//            else                          result.add(el);
//        }
//
//        return result;
//    }
//
//    /**
//     * Metodo che restituisce la data modificata in base ai giorni indicati.
//     * @param dateToMod     La data da modificare
//     * @param days          Il numero di giorni da sommare alla data
//     * @param countDateDay  Indica se includere il giorno indicato dalla data
//     *
//     * author 3D Informatica - fcossu
//     *
//     * @return  La data modificata
//     */
//    public static Date modifyDateWithDays(Date dateToMod, int days, boolean IncludeDateDay) {
//        final long millisecInDay = 86400000;
//        GregorianCalendar startDate = new GregorianCalendar();
//        startDate.setTime(dateToMod);
//
//        long longTimeOut = startDate.getTimeInMillis() + (millisecInDay*days) + (IncludeDateDay ? 0 : millisecInDay);
//
//        GregorianCalendar timeOut = new GregorianCalendar();
//        timeOut.setTimeInMillis(longTimeOut);
//
//        Date date = timeOut.getTime();
//
//        return date;
//	}
//
//    /**
//     * Trasforma un xml mediante un foglio di stile xsl.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param xml         Xml da trasformare.
//     * @param xslFilePath Percorso completo del foglio di stile da usare.
//     *
//     * @return Il risultato della trasformazione.
//     *
//     * @throws Exception in caso di errore.
//     *
//     */
//    public static String xmlTransform(String xml, String xslFilePath) throws Exception {
//
//        log.debug("GenericUtils.xmlTransform(): xslFilePath: " + xslFilePath);
//        log.trace("GenericUtils.xmlTransform(): xml:\r\n\n" + xml + "\n");
//
//        TransformerFactory tf   = TransformerFactory.newInstance();
//        Transformer engine      = tf.newTransformer(new StreamSource(xslFilePath));
//        java.io.StringWriter sw = new java.io.StringWriter();
//        StreamSource ss         = new StreamSource(new StringReader(xml));
//        engine.transform(ss, new StreamResult(sw));
//        String res              = sw.toString();
//
//        log.trace("GenericUtils.xmlTransform(): transformation's result:\r\n\n" + res + "\n");
//
//        return res;
//    }
//
//    /**
//     * Questo metodo fa in modo di trasformare la frase di ricerca in modo tale da escludere qualsiasi stoplist.
//     * RW0049002 - Spostato nullStoplist() in GenericUtils
//     * @param query frase di ricerca originale
//     * @return stringa contenente la nuova frase di ricerca rivista in modo tale da forzare la presenza di stoplist
//     *         nulle per ogni campo.
//     */
//    public static String nullStoplist(String query) {
//        StringBuffer result = new StringBuffer();
//        int start = 0, stop;
//
//        for (int index = 0; index < query.length(); ++index) {
//            switch (query.charAt(index)) {
//            case '"':
//                if (index == 0 || query.charAt(index - 1) != '\\') {
//                    for (stop = index + 1; stop > 0; ++stop) {
//                        stop = query.indexOf('"', stop);
//                        if (stop == -1 || query.charAt(stop - 1) != '\\')
//                            break;
//                    }
//                    if (stop == -1)
//                        stop = query.length();
//                    else
//                        ++stop;
//                    result.append(query.substring(start, stop));
//                    start = stop;
//                    index = stop - 1;
//                }
//                break;
//            case ']':
//                if (checkSyntax(query, index)) {
//                    result.append(query.substring(start, index));
//                    result.append("|(SrcStp:null)");
//                    start = index;
//                }
//                break;
//            }
//        }
//        result.append(query.substring(start, query.length()));
//        return result.toString();
//    }
//
//    // Da chiamare con index che punta a ']'. Questa funzione
//    // verifica che dopo tale carattere ci sia un '='. Verifica
//    // anche che:
//    // - quanto segue '[' non sia ?SE o ?TH
//    // - quanto contenuto tra quadre non contenga la stringa '|(SrcStp:'
//    private static boolean checkSyntax(String query, int index) {
//        for (int j = index + 1; j < query.length(); ++j) {
//            switch (query.charAt(j)) {
//            case '\t':
//            case '\r':
//            case '\n':
//            case ' ':
//                break;
//            case '=':
//                j = query.length();
//                break;
//            default:
//                return false;
//            }
//        }
//        int stop = index;
//        while (index > 0 && query.charAt(index) != '[')
//            --index;
//        String test = query.substring(index, index + 4);
//        return (query.substring(index, stop).indexOf("|(SrcStp:") == -1 && test.compareToIgnoreCase("[?SE") != 0 && test
//                .compareToIgnoreCase("[?TH") != 0);
//    }
//
//    /**
//     * Metodo che ricevuto un tag con la sintassi <TAG>contenuto</TAG>
//     * effettuano il replace con il valore xml corrispondente mantenendo
//     * il contenuto. Se non viene trovato il valore xml il metodo
//     * restituisce stringa vuota.
//     *
//     * @param   Il tag di cui fare il replace
//     * @param   Xpath da cercare nell'xml
//     * @param   Stringa pattern che indica il punto da cui eventualmente prendere il valore dell'xpath
//     * @param   Documento di riferimento
//     *
//     * @return  La stringa per la segnatura dopo il replace, stringa vuota
//     *          se il tag non � gestito o non viene trovato nell'xml
//     */
//    public static String replaceTagForSignature(String signTag, String xpath, String patternXpath, XMLDocumento document) {
//        String signTagReplaced = "";
//
//        String testoFisso = signTag.substring(signTag.indexOf(">")+1, signTag.indexOf("</"));
//        Node replacementNode = document.selectSingleNode(xpath);
//        if (replacementNode != null) {
//            String replacement = replacementNode.getStringValue();
//            if (patternXpath != null && patternXpath.length() > 0) {
//                replacement = replacement.substring(replacement.lastIndexOf(patternXpath)+1);
//            }
//            signTagReplaced = testoFisso + replacement;
//        }
//
//        return signTagReplaced;
//
//    }
//
//    /**
//     * Recupera le informazioni di lock di un documento bloccato.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr<br>
//     *
//     * @param conn    Connessione da utilizzare.
//     * @param physdoc Numero fisico del documento bloccato.
//     *
//     * @return Le informazioni di lock di un documento bloccato.
//     *
//     * @throws Exception in caso di errore.<br><br>
//     *
//     *         <b>NOTA</b>: se il documento non e' bloccato, xw segnala un errore di lettura del file chk corrispondente
//     *                      al documento indicato, file che, ovviamente, non esiste.<br>
//     *                      Questo comporta un'eccezione.
//     *
//     * @see it.highwaytech.apps.generic.utils.db.LockInfo
//     *
//     */
//    public static LockInfo getLockInfo(Connessione conn, int physdoc) throws Exception {
//        log.debug("GenericUtils.getLockInfo(): getting lock info for document '" + physdoc + "' using connection '" +
//                  conn.getConnection() + "' [db is: " + conn.getDb() + "]");
//
//        // Federico 26/11/08: ristrutturata la classe LockInfo in seguito alla patch (RW 0051714) effettuata da Roberto
//        // sul comando per il recupero delle informazioni di lock [RW 0055565]
//        return new LockInfo(conn, physdoc);
//    }
//
//    /**
//     * Recupera le informazioni di lock dei documenti bloccati da un utente.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr<br>
//     *
//     * @param conn Connessione da utilizzare.
//     * @param user Utente.
//     *
//     * @return Le informazioni di lock dei documenti bloccati da un utente.
//     *
//     * @throws Exception in caso di errore.
//     *
//     * @see it.highwaytech.apps.generic.utils.db.LockInfo
//     *
//     */
//    public static LockInfo getLockInfo(Connessione conn, String user) throws Exception {
//        log.debug("GenericUtils.getLockInfo(): getting lock info for documents locked by user '" + user + "' using connection '" +
//                  conn.getConnection() + "' [db is: " + conn.getDb() + "]");
//
//        // Federico 26/11/08: ristrutturata la classe LockInfo in seguito alla patch (RW 0051714) effettuata da Roberto
//        // sul comando per il recupero delle informazioni di lock [RW 0055565]
//        return new LockInfo(conn, user);
//    }
//
//    /**
//     * Recupera le informazioni di lock di tutti i documenti bloccati.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr<br>
//     *
//     * @param conn Connessione da utilizzare.
//     *
//     * @return Le informazioni di lock di tutti i documenti bloccati.
//     *
//     * @throws Exception in caso di errore.
//     *
//     * @see it.highwaytech.apps.generic.utils.db.LockInfo
//     *
//     */
//    public static LockInfo getLockInfo(Connessione conn) throws Exception {
//        log.debug("GenericUtils.getLockInfo(): getting lock info for all locked documents using connection '" +
//                  conn.getConnection() + "' [db is: " + conn.getDb() + "]");
//
//        // Federico 26/11/08: ristrutturata la classe LockInfo in seguito alla patch (RW 0051714) effettuata da Roberto
//        // sul comando per il recupero delle informazioni di lock [RW 0055565]
//        return new LockInfo(conn);
//    }
//
//    /**
//     * Aggiunge degli "0" in testa alla stringa numerica 'strToBeChecked' fino al raggiungimento
//     * della lunghezza 'strLength', solo se, ovviamente, la lunghezza di 'strToBeChecked' e' minore
//     * di 'strLength'.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr<br>
//     *
//     * @param strToBeChecked Stringa numerica da controllare.
//     * @param strLength      Lunghezza che dovrebbe avere la stringa numerica.
//     *
//     * @return La stringa numerica eventualmente "allungata" con degli "0" in testa.
//     *
//     */
//    public static String fillNumericStringWithZeros(String strToBeChecked, int strLength) {
//        if (strToBeChecked != null) {
//            while (strToBeChecked.length() < strLength) strToBeChecked = "0" + strToBeChecked;
//        }
//
//        return strToBeChecked;
//    }
//
//    /**
//     * Questo metodo formatta il numero di repertorio di un documento utilizzando la notazione
//     * "numero/anno" o "numero" (se il numero di repertorio e' privo di anno).
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param numRep Numero di repertorio da formattare.
//     *
//     * @return Il numero di repertorio formattato o una stringa vuota qualora "numRep" non sia
//     *         corretto.
//     *
//     */
//    public static String formatNumRep(String numRep) {
//        int sepPos;
//        String anno_num;
//
//        // Federico 29/06/07: introdotta gestione numeri di repertorio privi di anno [RW 0045047]
//        if (numRep != null &&
//                numRep.length() > 0 &&
//                (sepPos = numRep.indexOf("-")) != -1) {
//
//            if ((anno_num = numRep.substring(sepPos + 1)).length() == 11) {
//                return  GenericUtils.trimLeftZeros(anno_num.substring(4)) + "/" + anno_num.substring(0, 4);
//            }
//            else if ((anno_num = numRep.substring(sepPos + 1)).length() == 7) {
//                return  GenericUtils.trimLeftZeros(anno_num);
//            }
//            else {
//                // errore nel formato di numRep
//                log.error("GenericUtils.formatNumRep(): numRep's format is wrong! [" + numRep + "]");
//                return "";
//            }
//        }
//        // errore nel formato di numRep
//        log.error("GenericUtils.formatNumRep(): numRep's format is wrong! [" + numRep + "]");
//        return "";
//    }
//
//    /**
//     * Questo metodo estrae il numero di protocollo dalla stringa 'numProt' che rappresenta il contenuto
//     * dell'attributo 'num_prot' di un documento.
//     * <br><br>
//     *
//     * author 3D Informatica - fcossu
//     *
//     * @param numProt Numero di protocollo da formattare.
//     *
//     * @return Il numero di protocollo formattato nel formato 'numero/anno'.
//     *
//     */
//    public static String formatNumProt(String numProt) {
//        String anno = numProt.substring(0, numProt.indexOf("-"));
//        return GenericUtils.trimLeftZeros(numProt.substring(numProt.lastIndexOf("-") + 1)) + "/" + anno;
//    }
//
//    /**
//     * In alcuni archivi migrati da Docway 2 il testo estratto dagli allegati di un documento non
//     * figura in allegati a parte ma e' presente (non in un CDATA...) all'interno degli elementi
//     * xw:file degli originali.<br>
//     * Il metodo elimina il testo e aggiunge l'attributo "convert=yes" nell'elemento xw:file corrispondente
//     * per forzare la conversione tramite FCS. [RW 0050152]
//     * <br><br>
//     *
//     * author 3D Informatica - fgr<br>
//     *
//     * @param doc Documento da ripulire.
//     *
//     * @return Il numero di nodi testo rimossi.
//     *
//     */
    public static int cleanXWFileExtractedText(XMLDocumento doc) {
        return cleanXWFileExtractedText(doc.getDocument());
    }
//
//    /**
//     * In alcuni archivi migrati da Docway 2 il testo estratto dagli allegati di un documento non
//     * figura in allegati a parte ma e' presente (non in un CDATA...) all'interno degli elementi
//     * xw:file degli originali.<br>
//     * Il metodo elimina il testo e aggiunge l'attributo "convert=yes" nell'elemento xw:file corrispondente
//     * (<u>versione piu' recente</u>) per forzare la conversione tramite FCS. [RW 0050152]
//     * <br><br>
//     *
//     * author 3D Informatica - fgr<br>
//     *
//     * @param doc Documento da ripulire.
//     *
//     * @return Il numero di nodi testo rimossi.
//     *
//     */
    public static int cleanXWFileExtractedText(Document doc) {
    	List txtNodes = doc.selectNodes("//*[name()='xw:file']/text()");

        for (int j = 0; j < txtNodes.size(); j++) {
            Node txtNode         = (Node)txtNodes.get(j);
            Element parentXwFile = txtNode.getParent();

            String theNodeRealContent = txtNode.getText().trim();
            if ( theNodeRealContent.length() > 0 ) {
               // richiesta di estrazione del testo solo sul xw:file piu' interno (versione piu' recente)
               if (parentXwFile.selectNodes("./*[name()='xw:file']").size() == 0)
            	   parentXwFile.addAttribute("convert", "yes");
            }
            
            txtNode.detach();
        }

        return txtNodes.size();
    }
//
//    /**
//     * "Prepara" una stringa in modo che possa essere inserita in una condizione xpath
//     * senza che gli eventuali caratteri " e ' creino dei problemi.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param xpathCondition Stringa che deve essere inserita in una condizione di un xpath.
//     *
//     * @return La stringa opportunamente preparata.
//     *
//     * @see http://www.guru4.net/articoli/xpath-quote/
//     *
//     */
//    public static String getXPathString(String xpathCondition) {
//        if (xpathCondition.indexOf("'") > -1 && xpathCondition.indexOf("\"") > -1) {
//            return "concat('" + xpathCondition.replaceAll("'", "', \"'\", '") + "')";
//        }
//        else if (xpathCondition.indexOf("\"") > -1) {
//            return "'" + xpathCondition + "'";
//        }
//        else {
//            return "\"" + xpathCondition + "\"";
//        }
//    }
//
//    /**
//     * Converte un importo numerico in lettere [M 0000174].<br><br>
//     *
//     * Es: "11.593,27" --&gt; "undicimilacinquecentonovantatre/27"
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param importo           L'importo da convertire. Come separatori sono accettati "," per i decimali e
//     *                          "." per le migliaia.
//     * @param inizialeMinuscola Se <code>true</code>, comporta che l'iniziale della stringa restituita sia
//     *                          minuscola.
//     *
//     * @return L'importo convertito in lettere.
//     *
//     * @see #digitation(String, boolean)
//     *
//     */
//    public static String importoInLettere(String importo, boolean inizialeMinuscola) {
//        importo    = importo.replaceAll("\\.", "");
//        int pos    = importo.indexOf(",");
//        String dec = "";
//
//        if (pos != -1) {
//            dec     = importo.substring(pos + 1);
//            importo = importo.substring(0, pos);
//        }
//
//        return GenericUtils.digitation(importo, inizialeMinuscola) + (dec.length() > 0 ? "/" + dec : "");
//    }
//
//    /**
//     * Restituisce il mese (in lettere) a partire da un oggetto <i>GregorianCalendar</i> [RW 0052678].
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param gc L'oggetto <i>GregorianCalendar</i> da usare.
//     *
//     * @return Il mese in lettere.
//     *
//     */
//    public static String meseInLettere(GregorianCalendar gc) {
//        return GenericUtils.meseInLettere(String.valueOf(gc.get(Calendar.MONTH) + 1));
//    }
//
//    /**
//     * Converte un mese numerico in lettere [M 0000235].<br><br>
//     *
//     * Es: "5" --&gt; "Maggio"
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param meseNumerico Il mese da convertire (numero da 1 a 12).
//     *
//     * @return Il mese convertito in lettere o 'meseNumerico' qualora il numero sia fuori dall'intervallo [1,12] o 'meseNumerico'
//     *         non sia un numero.
//     *
//     */
//    public static String meseInLettere(String meseNumerico) {
//        String[] mesi = { "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre" };
//
//        try {
//            int pos = Integer.parseInt(meseNumerico) - 1;
//
//            if (pos >= 0 && pos <= 11) return mesi[pos];
//            else                       return meseNumerico;
//        }
//        catch (Exception ex) {
//            return meseNumerico;
//        }
//    }
//
//    /**
//     * Verifica se un utente � CC nel fascicolo indicato [M 0000378].
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param conn       Connessione da utilizzare.
//     * @param numFasc    Numero del fascicolo.
//     * @param codPersona Codice della persona da usare per il controllo.
//     * @param codUff     Codice dell'ufficio da usare per il controllo.
//     *
//     * @return <code>true</code> se la persona indicata � CC nel fascicolo; <code>false</code> altrimenti.
//     *
//     * @throws Exception in caso di errore.
//     *
//     */
//    public static boolean userIsCCInFasc(Connessione conn, String numFasc, String codPersona, String codUff) throws Exception {
//        String rootFascNum = GenericUtils.isSubFasc(numFasc);
//
//        if (rootFascNum.length() > 0) {
//            log.info("GenericUtils.userIsCCInFasc(): fascicle " + numFasc + " is a sub-fascicle --> looking for CCs of root fascicle " + rootFascNum);
//
//            numFasc = rootFascNum;
//        }
//
//        log.debug("GenericUtils.userIsCCInFasc(): using connection: " + conn);
//        log.debug("GenericUtils.userIsCCInFasc(): loading fasc.: " + numFasc);
//
//        Object[]     res       = GenericUtils.loadSingleDoc(conn, "[FASC_NUMERO]=\"" + numFasc + "\"", false);
//        XMLDocumento fascicolo = (XMLDocumento)res[0];
//
//        log.debug("GenericUtils.userIsCCInFasc(): looking for CC with @cod_persona='" + codPersona + "' or @cod_uff='" + codUff + "'");
//
//        boolean result = fascicolo.selectSingleNode("//rif_interni/rif[@diritto='CC'][@cod_persona='" + codPersona + "' or @cod_uff='" + codUff + "']") != null;
//
//        log.debug("GenericUtils.userIsCCInFasc(): returning: " + result);
//
//        return result;
//    }
//
//    /**
//     * Se il documento passato risulta fascicolato, aggiunge i CC del fascicolo a quelli del documento [M 0000378].<br>
//     * In caso di documenti tra uffici vengono aggiunti i CC dei fascicoli dell'originale e della minuta.<br>
//     * Vengono inoltre recuperati anche i CC degli eventuali fascicoli collegati.<br>
//     * Insieme ai CC vengono trasferite nel documento anche le informazioni di assegnazione dei CC
//     * presenti nella storia dei fascicoli in modo che esse possano essere mostrate all'utente.<br>
//     * Tutti gli elementi aggiunti (cc ed elementi della storia) sono marcati con l'attributo <i>cc_from_fasc</i> che contiene
//     * o il numero del fascicolo di provenienza o, se � richiesta la replicazione dei CC del fascicolo della minuta/fascicoli
//     * collegati, le stringhe "linked_fasc"/"minuta_fasc" (caso di fascicolo <u>collegato</u>/fascicolo della <u>minuta</u>).<br>
//     * Se � richiesta la replicazione dei CC del fascicolo della minuta/fascicoli collegati, i CC dei fascicoli collegati vengono
//     * ripetuti anche all'interno dei fascicoli collegati di appartenenza (nel percorso "<i>//rif_interni/fascicoli_collegati/fasc/rifInt_fasc/rif_fasc</i>")
//     * in modo da consentirne la visualizzazione sotto i fascicoli collegati di appartenenza. In questo caso l'attributo <i>cc_from_fasc</i>
//     * contiene sempre il numero del fascicolo.<br>
//     * Se � richiesta la replicazione dei CC del fascicolo della minuta/fascicoli collegati, i CC del fascicolo della minuta vengono
//     * ripetuti anche all'interno dell'elemento "minuta" (nel percorso "<i>//minuta/rifInt_minuta/rif_minuta</i>") in modo da consentirne
//     * la visualizzazione sotto la minuta. In questo caso l'attributo <i>cc_from_fasc</i> contiene sempre il numero del fascicolo.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param conn                          Connessione da utilizzare.
//     * @param doc                           Documento da esaminare ed eventualmente aggiornare.
//     * @param replicaCCMinuta_FascCollegati Se <code>true</code>, comporta la replicazione dei CC del fascicolo della minuta e dei fascicoli
//     *                                      collegati rispettivamente nei percorsi "<i>//minuta/rifInt_minuta/rif_minuta</i>" e
//     *                                      "<i>//rif_interni/fascicoli_collegati/fasc/rifInt_fasc/rif_fasc</i>".
//     *
//     * @return In caso di errore (cio� se si verifica un'eccezione) un messaggio che identifica l'errore (cio� il messaggio
//     *         dell'eccezione o, se questo � <code>null</code>, il messaggio "<i>got exception with null error message</i>").
//     *         Se, invece, tutto va bene viene restituito <code>null</code>.
//     *
//     */
//    public static String inserisciCCFascicolo(Connessione conn, XMLDocumento doc, boolean replicaCCMinuta_FascCollegati) {
//        return GenericUtils.inserisciCCFascicolo(conn, doc.getRootElement(), replicaCCMinuta_FascCollegati);
//    }
//
//    /**
//     * Se il documento passato risulta fascicolato, aggiunge i CC del fascicolo a quelli del documento [M 0000378].<br>
//     * In caso di documenti tra uffici vengono aggiunti i CC dei fascicoli dell'originale e della minuta.<br>
//     * Vengono inoltre recuperati anche i CC degli eventuali fascicoli collegati.<br>
//     * Insieme ai CC vengono trasferite nel documento anche le informazioni di assegnazione dei CC
//     * presenti nella storia dei fascicoli in modo che esse possano essere mostrate all'utente.<br>
//     * Tutti gli elementi aggiunti (cc ed elementi della storia) sono marcati con l'attributo <i>cc_from_fasc</i> che contiene
//     * o il numero del fascicolo di provenienza o, se � richiesta la replicazione dei CC del fascicolo della minuta/fascicoli
//     * collegati, le stringhe "linked_fasc"/"minuta_fasc" (caso di fascicolo <u>collegato</u>/fascicolo della <u>minuta</u>).<br>
//     * Se � richiesta la replicazione dei CC del fascicolo della minuta/fascicoli collegati, i CC dei fascicoli collegati vengono
//     * ripetuti anche all'interno dei fascicoli collegati di appartenenza (nel percorso "<i>//rif_interni/fascicoli_collegati/fasc/rifInt_fasc/rif_fasc</i>")
//     * in modo da consentirne la visualizzazione sotto i fascicoli collegati di appartenenza. In questo caso l'attributo <i>cc_from_fasc</i>
//     * contiene sempre il numero del fascicolo.<br>
//     * Se � richiesta la replicazione dei CC del fascicolo della minuta/fascicoli collegati, i CC del fascicolo della minuta vengono
//     * ripetuti anche all'interno dell'elemento "minuta" (nel percorso "<i>//minuta/rifInt_minuta/rif_minuta</i>") in modo da consentirne
//     * la visualizzazione sotto la minuta. In questo caso l'attributo <i>cc_from_fasc</i> contiene sempre il numero del fascicolo.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param conn                          Connessione da utilizzare.
//     * @param docRoot                       Radice del documento da esaminare ed eventualmente aggiornare.
//     * @param replicaCCMinuta_FascCollegati Se <code>true</code>, comporta la replicazione dei CC del fascicolo della minuta e dei fascicoli
//     *                                      collegati rispettivamente nei percorsi "<i>//minuta/rifInt_minuta/rif_minuta</i>" e
//     *                                      "<i>//rif_interni/fascicoli_collegati/fasc/rifInt_fasc/rif_fasc</i>".
//     *
//     * @return In caso di errore (cio� se si verifica un'eccezione) un messaggio che identifica l'errore (cio� il messaggio
//     *         dell'eccezione o, se questo � <code>null</code>, il messaggio "<i>got exception with null error message</i>").
//     *         Se, invece, tutto va bene viene restituito <code>null</code>.
//     *
//     */
//    public static String inserisciCCFascicolo(Connessione conn, Element docRoot, boolean replicaCCMinuta_FascCollegati) {
//        String errorMsg = null;
//
//        if (!docRoot.getName().equals("fascicolo") && !docRoot.getName().equals("raccoglitore")) {
//            log.info("GenericUtils.inserisciCCFascicolo(): looking for fascicle's CCs...");
//
//            try {
//                // originale
//                Node   rpaCodFascAttr = docRoot.selectSingleNode(".//rif_interni/rif[@diritto='RPA']/@cod_fasc");
//                String rpaCodFasc     = (rpaCodFascAttr != null ? rpaCodFascAttr.getText().trim() : "");
//
//                if (rpaCodFasc.length() > 0) {
//                    log.info("GenericUtils.inserisciCCFascicolo(): document '" + docRoot.attributeValue("nrecord", "") + "' has RPA cod fasc '" + rpaCodFasc + "'");
//
//                    GenericUtils.inserisciCCFascicolo(conn, docRoot, rpaCodFasc, null, null, null, null);
//                }
//
//                // eventuale minuta
//                Node   rpamCodFascAttr = docRoot.selectSingleNode(".//rif_interni/rif[@diritto='RPAM']/@cod_fasc");
//                String rpamCodFasc     = (rpamCodFascAttr != null ? rpamCodFascAttr.getText().trim() : "");
//
//                if (rpamCodFasc.length() > 0) {
//                    log.info("GenericUtils.inserisciCCFascicolo(): document '" + docRoot.attributeValue("nrecord", "") + "' has RPAM cod fasc '" + rpamCodFasc + "'");
//
//                    if (replicaCCMinuta_FascCollegati) {
//                        Element minutaEl = docRoot.element("minuta");
//
//                        if (minutaEl == null) {
//                            minutaEl = DocumentHelper.createElement("minuta");
//                            docRoot.add(minutaEl);
//                        }
//
//                        GenericUtils.inserisciCCFascicolo(conn, docRoot, rpamCodFasc, "minuta_fasc", minutaEl, "rifInt_minuta", "rif_minuta");
//                    }
//                    else {
//                        GenericUtils.inserisciCCFascicolo(conn, docRoot, rpamCodFasc, null, null, null, null);
//                    }
//                }
//
//                // fascicoli collegati
//                @SuppressWarnings("unchecked")
//                List<Node> fascCollegatiCod =(List<Node>)docRoot.selectNodes(".//rif_interni/fascicoli_collegati/fasc/@cod");
//
//                if (fascCollegatiCod.size() > 0) {
//                    log.info("GenericUtils.inserisciCCFascicolo(): document '" + docRoot.attributeValue("nrecord", "") +
//                              "' has " + fascCollegatiCod.size() + " linked fasc.");
//
//                    for (Node numFascColl : fascCollegatiCod) {
//                        String numFasc = numFascColl.getText().trim();
//
//                        if (numFasc.length() > 0) {
//                            if (replicaCCMinuta_FascCollegati) {
//                                GenericUtils.inserisciCCFascicolo(conn, docRoot, numFasc, "linked_fasc", numFascColl.getParent(), "rifInt_fasc", "rif_fasc");
//                            }
//                            else {
//                                GenericUtils.inserisciCCFascicolo(conn, docRoot, numFasc, null, null, null, null);
//                            }
//                        }
//                    }
//                }
//            }
//            catch (Exception ex) {
//                log.error("GenericUtils.inserisciCCFascicolo(): got exception adding fasc. CCs to loaded document", ex);
//
//                errorMsg = ex.getMessage();
//
//                if (errorMsg == null) errorMsg = "got exception with null error message";
//            }
//        }
//        else {
//            log.info("GenericUtils.inserisciCCFascicolo(): pne is '" + docRoot.getName() + "'. Nothing to do...");
//        }
//
//        return errorMsg;
//    }
//
//    /**
//     * Aggiunge i CC del fascicolo radice a quelli del sottofascicolo indicato [M 0000378].<br>
//     * Insieme ai CC vengono trasferite nel documento anche le informazioni di assegnazione dei CC
//     * presenti nella storia dei fascicoli in modo che esse possano essere mostrate all'utente.<br>
//     * Tutti gli elementi aggiunti (cc ed elementi della storia) sono marcati con l'attributo <i>cc_from_fasc</i> che contiene
//     * il numero del fascicolo radice.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param conn      Connessione da utilizzare.
//     * @param sottofasc Sottofascicolo da esaminare ed eventualmente aggiornare.
//     *
//     * @return In caso di errore (cio� se si verifica un'eccezione) un messaggio che identifica l'errore (cio� il messaggio
//     *         dell'eccezione o, se questo � <code>null</code>, il messaggio "<i>got exception with null error message</i>").
//     *         Se, invece, tutto va bene viene restituito <code>null</code>.
//     *
//     */
//    public static String inserisciCCFascicoloSottoFasc(Connessione conn, XMLDocumento sottofasc) {
//        String errorMsg    = null;
//        String rootFascNum = null;
//
//        if ((rootFascNum = GenericUtils.isSubFasc(sottofasc)).length() > 0) {
//            log.info("GenericUtils.inserisciCCFascicoloSottoFasc(): document is a sub-fascicle. Looking for root fascicle's CCs...");
//
//            try {
//                GenericUtils.inserisciCCFascicolo(conn, sottofasc.getRootElement(), rootFascNum, null, null, null, null);
//            }
//            catch (Exception ex) {
//                log.error("GenericUtils.inserisciCCFascicoloSottoFasc(): got exception adding root fascicle's CCs to loaded sub-fascicle", ex);
//
//                errorMsg = ex.getMessage();
//
//                if (errorMsg == null) errorMsg = "got exception with null error message";
//            }
//        }
//        else {
//            log.info("GenericUtils.inserisciCCFascicoloSottoFasc(): document is not a sub-fascicle. Nothing to do...");
//        }
//
//        return errorMsg;
//    }
//
//    /**
//     * Aggiunge i CC del fascicolo indicato a quelli del documento passato [M 0000378].<br>
//     * Insieme ai CC del fascicolo vengono trasferite nel documento anche le informazioni di assegnazione dei CC
//     * presenti nella storia del fascicolo in modo che esse possano essere mostrate all'utente.<br>
//     * Tutti gli elementi aggiunti (cc ed elementi della storia) sono marcati con l'attributo <i>cc_from_fasc</i> che contiene
//     * o il numero del fascicolo di provenienza o la stringa <i>ccFromFascAttrContent</i> (se valorizzata).<br>
//     * I CC del fascicolo vengono eventualmente replicati nel documento all'interno dell'elemento <i>extraRifIntRootContainer</i>
//     * (se diverso da <code>null</code>) nel percorso "<i>extraRifIntRootContainer/extraRifIntRootName/extraRifIntElsName</i>").
//     * Questa replicazione avviene solo se tutti e tre i parametri <i>extra*</i> sono valorizzati.<br>
//     * Nei CC replicati l'attributo <i>cc_from_fasc</i> contiene sempre il numero del fascicolo.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param conn                     Connessione da utilizzare.
//     * @param docRoot                  Radice del documento da aggiornare.
//     * @param numFasc                  Numero del fascicolo.
//     * @param ccFromFascAttrContent    Eventuale contenuto da imporre all'attributo <i>cc_from_fasc</i>.
//     * @param extraRifIntRootContainer Eventuale elemento xml in cui replicare i CC del fascicolo.
//     * @param extraRifIntRootName      Nome del contenitore dei CC da replicare. Diverr� figlio di <i>extraRifIntRootContainer</i>.
//     * @param extraRifIntElsName       Nomi dei nuovi elementi dei CC da replicare. Saranno figli di <i>extraRifIntRootName</i>.
//     *
//     * @throws Exception in caso di errore.
//     *
//     */
//    private static void inserisciCCFascicolo(Connessione conn,
//                                             Element docRoot,
//                                             String numFasc,
//                                             String ccFromFascAttrContent,
//                                             Element extraRifIntRootContainer,
//                                             String extraRifIntRootName,
//                                             String extraRifIntElsName) throws Exception {
//
//        String rootFascNum = GenericUtils.isSubFasc(numFasc);
//
//        if (rootFascNum.length() > 0) {
//            log.info("GenericUtils.inserisciCCFascicolo(): fascicle " + numFasc + " is a sub-fascicle --> looking for CCs of root fascicle " + rootFascNum);
//
//            numFasc = rootFascNum;
//        }
//
//        log.info("GenericUtils.inserisciCCFascicolo(): using connection: " + conn);
//        log.info("GenericUtils.inserisciCCFascicolo(): loading fasc.: " + numFasc);
//
//        Object[]     res       = GenericUtils.loadSingleDoc(conn, "[FASC_NUMERO]=\"" + numFasc + "\"", false);
//        XMLDocumento fascicolo = (XMLDocumento)res[0];
//
//        log.info("GenericUtils.inserisciCCFascicolo(): adding fasc. CCs to document...");
//
//        Element       rifInt           = (Element)docRoot.selectSingleNode(".//rif_interni");
//        Element       storia           = (Element)docRoot.selectSingleNode(".//storia");
//        @SuppressWarnings("unchecked")
//        List<Element> fascCCs          = (List<Element>)fascicolo.selectNodes("//rif_interni/rif[@diritto='CC']");
//        String        formattedNumFasc = GenericUtils.printFascNum(numFasc);
//
//        if (fascCCs.size() > 0) {
//            log.info("GenericUtils.inserisciCCFascicolo(): found " + fascCCs.size() + " CCs");
//
//            // per ogni cc del fascicolo...
//            for (Element rif : fascCCs) {
//                if (rifInt.selectSingleNode("./rif[@cod_persona='" + rif.attributeValue("cod_persona", "") + "']") == null) {
//                    // il CC non c'� nel doc. --> agg. rif. interni
//                    rif.addAttribute("cc_from_fasc",
//                                     ((ccFromFascAttrContent == null || ccFromFascAttrContent.length() == 0) ? formattedNumFasc : ccFromFascAttrContent));
//                    rifInt.add(rif.detach());
//
//                    if (storia != null) { // se si lavora su un doc xml costruito da un titolo 'storia' pu� non esserci
//                        Element assCCEl = (Element)fascicolo.selectSingleNode("//storia/assegnazione_cc[@cod_persona='" + rif.attributeValue("cod_persona", "") + "'][last()]");
//
//                        if (assCCEl != null) {
//                            // agg. storia
//                            assCCEl.addAttribute("cc_from_fasc",
//                                                 ((ccFromFascAttrContent == null || ccFromFascAttrContent.length() == 0) ? formattedNumFasc : ccFromFascAttrContent));
//                            storia.add(assCCEl.detach());
//                        }
//                    }
//                }
//
//                // eventuale replicazione dei CC
//                if (extraRifIntRootContainer != null &&
//                        extraRifIntRootName != null && extraRifIntRootName.length() > 0 &&
//                        extraRifIntElsName != null && extraRifIntElsName.length() > 0) {
//
//                    Element extraRifIntRoot = extraRifIntRootContainer.element(extraRifIntRootName);
//
//                    if (extraRifIntRoot == null) {
//                        extraRifIntRoot = DocumentHelper.createElement(extraRifIntRootName);
//                        extraRifIntRootContainer.add(extraRifIntRoot);
//                    }
//
//                    Element rifCopy = rif.createCopy();
//                    extraRifIntRoot.add(rifCopy);
//                    rifCopy.addAttribute("cc_from_fasc", formattedNumFasc);
//                    rifCopy.setName(extraRifIntElsName);
//                }
//            }
//        }
//        else {
//            log.info("GenericUtils.inserisciCCFascicolo(): fascicle " + numFasc + " has no CCs");
//        }
//    }
//    
//    /**
//     * Per ogni link interno contenuto nel documento passato aggiunge, oltre all'nrecord dei documenti collegati,
//     * il numero di protocollo e la data di protocollo di ogni documento.
//     * Utilizzato nell'esportazione CSV di documenti
//     * 
//     * @param conn		Connessione da utilizzare.
//     * @param docRoot	Radice del documento da esaminare ed eventualmente aggiornare.
//     * 
//     * @return In caso di errore (cio� se si verifica un'eccezione) un messaggio che identifica l'errore (cio� il messaggio
//     *         dell'eccezione o, se questo � <code>null</code>, il messaggio "<i>got exception with null error message</i>").
//     *         Se, invece, tutto va bene viene restituito <code>null</code>.
//     */
//    public static String inserisciProtLinkInterni(Connessione conn, Element docRoot) {
//        String errorMsg = null;
//
//        if (docRoot.getName().equals("doc")) {
//            log.info("GenericUtils.inserisciProtLinkInterni(): looking for link_interno...");
//
//            try {
//            	// recupero dei dati di protocollo di tutti i documenti collegati a quello corrente ...
//            	
//            	@SuppressWarnings("unchecked")
//				List<Element> linkInterni = docRoot.selectNodes(".//link_interno");
//            	
//            	if (linkInterni != null && linkInterni.size() > 0) {
//            		log.info("GenericUtils.inserisciProtLinkInterni(): document '" + docRoot.attributeValue("nrecord", "") + "' has " + linkInterni.size() + " 'link_interno'");
//            		
//            		for (int i=0; i<linkInterni.size(); i++) {
//            			Element link = (Element) linkInterni.get(i);
//            			if (link != null) {
//            				String nrecordLink = link.attributeValue("href", ""); //recupero dell'nrecord del documento collegato
//            				if (nrecordLink.length() > 0) {
//		            			XMLDocumento docLinkato = (XMLDocumento) (GenericUtils.loadDocByNrecord(conn, nrecordLink, false)[0]);
//		            			if (docLinkato != null) {
//		            				link.addAttribute("num_prot_link", docLinkato.getRootElement().attributeValue("num_prot", "")); //recupero del numero di protocollo del documento collegato
//		            				link.addAttribute("data_prot_link", docLinkato.getRootElement().attributeValue("data_prot", "")); //recupero della data di protocollo del documento collegato
//		            			}
//	            			}
//            			}
//            		}
//            	}
//            	
//            	// ... e recupero di tutti i dati di protocollo ai quali e' stato collegato il documento corrente
//            	String nrecordDocRoot = docRoot.attributeValue("nrecord", "");
//            	if (!nrecordDocRoot.equals("")) {
//	            	List<Object[]> otherLinkedDocs = loadDocs(conn, "[/doc/link_interno/@href/]=\"" + nrecordDocRoot + "\"", false, 10, 3000);
//	            	if (otherLinkedDocs != null && otherLinkedDocs.size() > 0) {
//	            		log.info("GenericUtils.inserisciProtLinkInterni(): document '" + docRoot.attributeValue("nrecord", "") + "' has other " + otherLinkedDocs.size() + " 'link_interno'");
//	            		
//	            		for (int i=0; i<otherLinkedDocs.size(); i++) {
//	            			XMLDocumento docLinkato = (XMLDocumento) otherLinkedDocs.get(i)[0];
//	            			if (docLinkato != null) {
//	            				String nrecordDocLinkato = docLinkato.getRootElement().attributeValue("nrecord", "");
//	            				String numProtDocLinkato = docLinkato.getRootElement().attributeValue("num_prot", "");
//	            				String dataProtDocLinkato = docLinkato.getRootElement().attributeValue("data_prot", "");
//	            				
//	            				log.info("GenericUtils.inserisciProtLinkInterni(): found other linked doc '" + nrecordDocLinkato + " [" + numProtDocLinkato + "]");
//	            				
//	            				if (!nrecordDocLinkato.equals("")) {
//	            					Element newLinkInterno = docRoot.addElement("link_interno");
//	            					newLinkInterno.addAttribute("href", nrecordDocLinkato);
//	            					newLinkInterno.addAttribute("num_prot_link", numProtDocLinkato);
//	            					newLinkInterno.addAttribute("data_prot_link", dataProtDocLinkato);
//	            				}
//	            			}
//	            		}
//	            	}
//            	}
//            	
//            }
//            catch (Exception ex) {
//                log.error("GenericUtils.inserisciProtLinkInterni(): got exception updating 'link_interno' to loaded document", ex);
//
//                errorMsg = ex.getMessage();
//
//                if (errorMsg == null) errorMsg = "got exception with null error message";
//            }
//        }
//        else {
//            log.info("GenericUtils.inserisciProtLinkInterni(): pne is '" + docRoot.getName() + "'. Nothing to do...");
//        }
//
//        return errorMsg;
//    }
//    
//    /**
//     * Carica una lista di documenti data una query.
//     *
//     * @param conn      Connessione da utilizzare.
//     * @param query     La query da effettuare.
//     * @param locked    Indica se bloccare o meno il documento.
//     * @param attempts  Numero di tentativi da effettuare in caso di fallimento del
//     *                  caricamento del documento.
//     * @param delay     Intervallo intercorrente tra i tentativi di caricamento del
//     *                  documento in caso di fallimento (in millisecondi).
//     *
//     * @return Una lista di array di tre oggetti: il documento caricato (XMLDocumento), il numero fisico del documento (Integer) e il
//     *         suo eventuale codice di lock (String).
//     *
//     * @throws Exception in caso di errore.
//     *
//     */
//    private static List<Object[]> loadDocs(Connessione conn,
//                                         String query,
//                                         boolean locked,
//                                         int attempts,
//                                         int delay) throws Exception {
//
//        log.debug("GenericUtils.loadDocs(): using connection: " + conn.getConnection());
//
//        List<Object[]> docs = new ArrayList<Object[]>(); 
//        
//        QueryResult qr = doXWQuery(conn, query);
//
//        if (qr == null) {
//            log.error("GenericUtils.loadDocs(): document not found!");
//            throw new Exception("Document not found!");
//        }
//
//        if (qr.elements > 1) {
//            log.error("GenericUtils.loadDocs(): found " + qr.elements + " documents!");
//            throw new Exception("Found " + qr.elements + " documents!");
//        }
//
//        log.debug("GenericUtils.loadDocs(): loading found document [locked is: " + locked + "]");
//
//        for (int i=0; i<qr.elements; i++) {
//	        XmlDoc xmlDoc = new XmlDoc(qr.id, i, -1, conn);
//	        int bits = 0;
//	
//	        if (locked) bits = 1;
//	
//	        // fcossu - federico 07/09/06: introdotta ripetizione dell'operazione di caricamento in caso di errore
//	        // Federico 19/03/07:          resi parametrici il numero di tentativi e il delay [RW 0042308]
//	        xmlDoc.load(bits, attempts, delay);
//	
//	        Object[] res = new Object[3];
//	
//	        res[0] = xmlDoc.getDocument();
//	        res[1] = new Integer(xmlDoc.getPhysDoc());
//	
//	        if (locked) res[2] = xmlDoc.getLock();
//	        else        res[2] = null;
//	        
//	        docs.add(res);
//        }
//        
//        return docs;
//    }
//
//    /**
//     * Rimuove dal documento i CC derivati dai suoi fascicoli (originale, minuta e fascicoli collegati) [M 0000378].<br>
//     * Insieme ai CC vengono rimosse anche le informazioni di assegnazione dei CC derivanti dalla storia dei fascicoli.<br>
//     * In altri termini vengono eliminati tutti gli elementi marcati con l'attributo <i>cc_from_fasc</i>.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param doc Documento da aggiornare.
//     *
//     * @return Il numero di elementi xml rimossi.
//     *
//     * @see #inserisciCCFascicolo(Connessione, XMLDocumento)
//     * @see #inserisciCCFascicolo(Connessione, XMLDocumento, String, String, Element, String, String)
//     *
//     */
//    @SuppressWarnings("unchecked")
//    public static int rimuoviCCFascicolo(XMLDocumento doc) {
//        // eliminazione di tutti gli elementi "rifInt_fasc" nei fascicoli collegati
//        List<Element> fascCCs = (List<Element>)doc.selectNodes("//rif_interni/fascicoli_collegati/fasc/rifInt_fasc");
//
//        for (Element el : fascCCs) el.detach();
//
//        int removedEls = fascCCs.size();
//
//        // eliminazione dell'elemento "rifInt_minuta" (cc fascicolo minuta)
//        Element fascCCsMinuta = (Element)doc.selectSingleNode("//minuta/rifInt_minuta");
//
//        if (fascCCsMinuta != null) {
//            fascCCsMinuta.detach();
//            removedEls += fascCCsMinuta.selectNodes("rif_minuta").size();
//        }
//
//        // eliminazione di tutti gli elementi marcati con l'attributo "cc_from_fasc"
//        fascCCs = (List<Element>)doc.selectNodes("//*[@cc_from_fasc]");
//
//        for (Element el : fascCCs) el.detach();
//
//        return removedEls + fascCCs.size();
//    }
//
//    /**
//     * Ordina i CC del documento per nome ufficio. [M 0000378]
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param doc             Documento da aggiornare.
//     * @param appendXPathCond Eventuale condizione da accodare al xpath expression usato per individuare i CC
//     *                        (cio� "//rif_interni/rif[@diritto='CC']").
//     *
//     */
//    public static void ordinaCCPerNomeUfficio(XMLDocumento doc, String appendXPathCond) {
//        if (appendXPathCond == null) appendXPathCond = "";
//
//        List lCC = doc.selectNodes("//rif_interni/rif[@diritto='CC']" + appendXPathCond, "@nome_uff");
//
//        // riscrittura nel doc riportando la rottura di ufficio e lo stato iniziale della visualizzazione CC
//        Element eRifs      = (Element)doc.selectSingleNode("//rif_interni");
//        String  codUfficio = "";
//
//        for (int i = 0; i < lCC.size(); i++) {
//            Element eCC = (Element)lCC.get(i);
//
//            if(!codUfficio.equals(eCC.attributeValue("cod_uff"))) {
//                // rottura di ufficio - riporto il totale ufficio
//                int uffCount = doc.selectNodes("//rif_interni/rif[@diritto='CC'][@cod_uff=\"" + eCC.attributeValue("cod_uff") + "\"]" + appendXPathCond).size();
//
//                eCC.addAttribute("totale_uff", String.valueOf(uffCount));
//            }
//
//            codUfficio = eCC.attributeValue("cod_uff");
//            eRifs.remove(eCC);
//            eRifs.add(eCC);
//        }
//    }
//
//    /**
//     * Ordina i CC del fascicolo della minuta per nome ufficio. [M 0000378]
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param doc Documento da aggiornare.
//     *
//     */
//    @SuppressWarnings("unchecked")
//    public static void ordinaCCFascMinutaPerNomeUfficio(XMLDocumento doc) {
//        List<Element> fascMinutaCCs = (List<Element>)doc.selectNodes("//minuta/rifInt_minuta/rif_minuta[@diritto='CC']");
//
//        if (fascMinutaCCs.size() > 0) {
//            log.debug("GenericUtils.ordinaCCFascMinutaPerNomeUfficio(): document '" + doc.getRootElement().attributeValue("nrecord", "") +
//                      "' has " + fascMinutaCCs.size() + " fasc. minuta CCs");
//
//            // riscrittura nel doc riportando la rottura di ufficio e lo stato iniziale della visualizzazione CC
//            Element eRifs      = (Element)doc.selectSingleNode("//minuta/rifInt_minuta");
//            String  codUfficio = "";
//
//            for (Element fascMinutaCC : fascMinutaCCs) {
//                if(!codUfficio.equals(fascMinutaCC.attributeValue("cod_uff"))) {
//                    // rottura di ufficio - riporto il totale ufficio
//                    int uffCount = eRifs.selectNodes("./rif_minuta[@diritto='CC'][@cod_uff=\"" + fascMinutaCC.attributeValue("cod_uff") + "\"]").size();
//
//                    fascMinutaCC.addAttribute("totale_uff", String.valueOf(uffCount));
//                }
//
//                codUfficio = fascMinutaCC.attributeValue("cod_uff");
//                eRifs.remove(fascMinutaCC);
//                eRifs.add(fascMinutaCC);
//            }
//        }
//    }
//
//    /**
//     * Ordina i CC dei fascicoli collegati per nome ufficio. [M 0000378]
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param doc Documento da aggiornare.
//     *
//     */
//    @SuppressWarnings("unchecked")
//    public static void ordinaCCFascCollegatiPerNomeUfficio(XMLDocumento doc) {
//        List<Element> fascCollegati = (List<Element>)doc.selectNodes("//rif_interni/fascicoli_collegati/fasc");
//
//        if (fascCollegati.size() > 0) {
//            log.debug("GenericUtils.ordinaCCFascCollegatiPerNomeUfficio(): document '" + doc.getRootElement().attributeValue("nrecord", "") +
//                      "' has " + fascCollegati.size() + " linked fasc.");
//
//            for (Element fascColl : fascCollegati) {
//                List<Element> lCC = (List<Element>)fascColl.selectNodes("./rifInt_fasc/rif_fasc[@diritto='CC']", "@nome_uff");
//
//                // riscrittura nel doc riportando la rottura di ufficio e lo stato iniziale della visualizzazione CC
//                Element eRifs      = (Element)fascColl.selectSingleNode("./rifInt_fasc");
//                String  codUfficio = "";
//
//                for (int i = 0; i < lCC.size(); i++) {
//                    Element eCC = (Element)lCC.get(i);
//
//                    if(!codUfficio.equals(eCC.attributeValue("cod_uff"))) {
//                        // rottura di ufficio - riporto il totale ufficio
//                        int uffCount = fascColl.selectNodes("./rifInt_fasc/rif_fasc[@diritto='CC'][@cod_uff=\"" + eCC.attributeValue("cod_uff") + "\"]").size();
//
//                        eCC.addAttribute("totale_uff", String.valueOf(uffCount));
//                    }
//
//                    codUfficio = eCC.attributeValue("cod_uff");
//                    eRifs.remove(eCC);
//                    eRifs.add(eCC);
//                }
//            }
//        }
//    }
//
//    /**
//     * Marca i CC personali del documento con l'attributo '<i>personale="true"</i>'. [M 0000378]<br>
//     * Il metodo esamina anche i CC dei fascicoli collegati e del fascicolo della minuta.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param doc        Documento da aggiornare.
//     * @param codPersona Codice della persona da usare per individuare i CC personali.
//     *
//     */
//    @SuppressWarnings("unchecked")
//    public static void evidenziaCCPersonali(XMLDocumento doc, String codPersona) {
//        List      lCC           = doc.selectNodes("//rif_interni/rif[@diritto='CC']");
//        List      lCCFascColl   = doc.selectNodes("//rif_interni/fascicoli_collegati/fasc/rifInt_fasc/rif_fasc[@diritto='CC']");
//        List      lCCFascMinuta = doc.selectNodes("//minuta/rifInt_minuta/rif_minuta[@diritto='CC']");
//        ArrayList allCC       = new ArrayList(lCC);
//
//        allCC.addAll(lCCFascColl);
//        allCC.addAll(lCCFascMinuta);
//
//        for (int i = 0; i < allCC.size(); i++) {
//            Element eCC = (Element)allCC.get(i);
//
//            if(eCC.attributeValue("cod_persona").equalsIgnoreCase(codPersona) ) {
//                // � un CC personale
//                eCC.addAttribute("personale", "true");
//            }
//        }
//    }
//
//    /**
//     * Determina se il documento passato � un sottofascicolo o meno.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param sottofasc Documento da esaminare.
//     *
//     * @return Il numero del fascicolo radice se il documento � un sottofascicolo;
//     *         una stringa vuota altrimenti.
//     *
//     */
//    public static String isSubFasc(XMLDocumento sottofasc) {
//        if (sottofasc.getIUName().equals("fascicolo")) {
//            String numero = sottofasc.getRootElement().attributeValue("numero", "");
//
//            return GenericUtils.isSubFasc(numero);
//        }
//
//        return "";
//    }
//
//    /**
//     * Determina se il fascicolo indicato � un sottofascicolo o meno.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param numFasc Numero di fascicolo da esaminare.
//     *
//     * @return Il numero del fascicolo radice se il fascicolo � un sottofascicolo;
//     *         una stringa vuota altrimenti.
//     *
//     */
//    public static String isSubFasc(String numFasc) {
//        if (numFasc != null && numFasc.length() > 0) {
//            int pos;
//
//            if (numFasc.indexOf("/") != -1) {
//                // numero di fascicolo con classificazione
//                pos = numFasc.indexOf(".");
//            }
//            else {
//                // numero di fascicolo senza classificazione (numerazione unica)
//                pos = numFasc.lastIndexOf("-");
//            }
//
//            if ((pos = numFasc.indexOf(".", pos + 1)) != -1) {
//                return numFasc.substring(0, pos);
//            }
//        }
//
//        return "";
//    }
//
//    /**
//     * Trova i fascicoli in cui l'utente indicato � in CC [M 0000378].
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param conn       Connessione da usare (deve essere <u>gi� connessa</u>).
//     * @param codPersona Matricola dell'utente.
//     * @param separator  Separatore da usare per separare i numeri di fascicolo trovati.
//     *
//     * @return Un elenco di numeri di fascicolo separati da <i>separator</i>. Una stringa vuota se
//     *         non vengono trovati fascicoli in CC.
//     *
//     * @throws Exception in caso di errore.
//     *
//     */
//    public static String trovaFascicoliInCC(Connessione conn, String codPersona, String separator) throws Exception {
//        String result = "";
//        String query  = "([/fascicolo/rif_interni/rif/@cod_persona/]=\"" + codPersona + "\" ADJ [/fascicolo/rif_interni/rif/@diritto/]=\"CC\")";
//
//        log.info("GenericUtils.trovaFascicoliInCC(): looking for fascicles where user '" + codPersona + "' is CC");
//        log.info("GenericUtils.trovaFascicoliInCC(): querying: " + query);
//        log.info("GenericUtils.trovaFascicoliInCC(): using connection: " + conn);
//        log.info("GenericUtils.trovaFascicoliInCC(): separator is: " + separator);
//
//        QueryResult qr = GenericUtils.doXWQuery(conn, query);
//
//        if (qr != null) {
//            log.info("GenericUtils.trovaFascicoliInCC(): found " + qr.elements + " fascicles");
//
//            Vector titoli = (new Titoli(new Selezione(conn, qr))).getFullTitles(0, qr.elements, null);
//
//            for (int j = 0; j < titoli.size(); j++) {
//                String titolo  = ((Title)titoli.get(j)).getTitle();
//                String numFasc = titolo.split("\\|")[0].trim();
//                result        += numFasc + (j < titoli.size() - 1 ? separator : "");
//            }
//        }
//        else {
//            log.info("GenericUtils.trovaFascicoliInCC(): no fascicles found");
//        }
//
//        log.info("GenericUtils.trovaFascicoliInCC(): returning: " + result);
//
//        return result;
//    }
//
//    /**
//     * Effettua l'escaping del testo indicato affinch� possa essere utilizzato come testo contenuto
//     * in un elemento xml [RW 0052678].<br>
//     * In altri termini vengono effettuate le sostituzioni:<br><br>
//     *
//     * '&lt;' --&gt; "&amp;lt;"<br>
//     * '&gt;' --&gt; "&amp;gt;"<br>
//     * '&' --&gt; "&amp;amp;"<br>
//     * "&#" --&gt; "&#" (le character reference rimangono invariate)
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param txt Testo da controllare.
//     *
//     * @return Il testo corretto.
//     *
//     */
//    public static String escapeTextForXMLElement(String txt) {
//        if (txt == null) return null;
//
//        txt = txt.replaceAll(XmlReplacer.EURO_HTML_ENTITY, XmlReplacer.EURO_DECIMAL_ENTITY).replaceAll("&#128;", XmlReplacer.EURO_DECIMAL_ENTITY);
//
//        StringBuffer res = new StringBuffer(txt.length() * 5);
//
//        for (int j = 0; j < txt.length(); j++) {
//            char c = txt.charAt(j);
//
//            if (c == '<') {
//                res.append("&lt;");
//            }
//            else if (c == '>') {
//                res.append("&gt;");
//            }
//            else if (c == '&') {
//                if (j < (txt.length() - 1) && txt.charAt(j + 1) == '#') res.append(c);
//                else                                                    res.append("&amp;");
//            }
//            else {
//                res.append(c);
//            }
//        }
//
//        return res.toString();
//    }
//
//    /**
//     * Effettua l'unescaping degli URL encoding "%XX" nella stringa indicata [M 0000428].
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param urlEncodedString Stringa da controllare.
//     *
//     * @return La stringa eventualmente modificata o, in caso di errore, la stringa originale.
//     *
//     */
//    public static String unescapeURLEncodedString(String urlEncodedString) {
//        if (urlEncodedString == null || urlEncodedString.length() == 0) {
//            return urlEncodedString;
//        }
//
//        Matcher      matcher    = Pattern.compile("%[0-9a-fA-F][0-9a-fA-F]").matcher(urlEncodedString);
//        StringBuffer sb         = new StringBuffer();
//        String       matchedStr;
//        int          charInt;
//
//        try {
//            while (matcher.find()) {
//                matchedStr = matcher.group();
//                charInt    = Integer.parseInt(matchedStr.substring(1), 16);
//
//                matcher.appendReplacement(sb, String.valueOf((char)charInt));
//            }
//
//            matcher.appendTail(sb);
//
//            return sb.toString();
//        }
//        catch (Exception e) {
//            log.error("GenericUtils.unescapeURLEncodedString(): got exception:", e);
//            log.error("GenericUtils.unescapeURLEncodedString(): returning original string");
//
//            return urlEncodedString;
//        }
//    }
//
//    /**
//     * Indica se un file � un'immagine.
//     * <br><br>
//     *
//     * NOTA: metodo "importato" da 'it.highwaytech.apps.generic.mailarchiver.Interoperabilita'.
//     * <br><br>
//     *
//     * author 3D Informatica - fgr
//     *
//     * @param fileName Nome del file.
//     *
//     * @return <code>true</code> se il file indicato � un'immagine;
//     *         <code>false</code> altrimenti.
//     *
//     */
//     public static boolean isImage(String fileName) {
//         int pos = fileName.lastIndexOf('.');
//
//         if (pos > 0) {
//             String ext = fileName.substring(pos + 1);
//
//             if (ext.equalsIgnoreCase("tiff")
//                     || ext.equalsIgnoreCase("tif")
//                     || ext.equalsIgnoreCase("jpeg")
//                     || ext.equalsIgnoreCase("jpg")
//                     || ext.equalsIgnoreCase("bmp")
//                     || ext.equalsIgnoreCase("png")) {
//
//                 return true;
//             }
//         }
//
//         return false;
//     }
//
//     /**
//      * Estrae da una stringa tutti gli indirizzi email trovati [EB 130].
//      * <br><br>
//      *
//      * Per esempio da<br><br>
//      *
//      * "Federico Grillini <fgrillini@3di.it> *** Per conto di: cesiat@pec.unirc.it, writelli@3di.it,cZAPPAVIGNA@3Di.it,,,,_fcappelli.tredi@3di.it.com"<br><br>
//      *
//      * vengono estratti gli indirizzi<br><br>
//      *
//      * fgrillini@3di.it<br>
//      * cesiat@pec.unirc.it<br>
//      * writelli@3di.it<br>
//      * cZAPPAVIGNA@3Di.it<br>
//      * _fcappelli.tredi@3di.it.com
//      * <br><br>
//      *
//      * author 3D Informatica - fgr
//      *
//      * @param s Stringa da esaminare.
//      *
//      * @return Una lista degli indirizzi email trovati. Se non viene individuato alcun indirizzo, la lista
//      *         � vuota.
//      *
//      * @throws IllegalStateException in caso di fallimenti nel match del pattern usato con la stringa 's'.
//      *
//      */
//     public static ArrayList<String> extractEmailAddresses(String s) throws IllegalStateException {
//         ArrayList<String> result          = new ArrayList<String>();
//         String            emailPatternStr = "[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z]{2,}){1})";
//         Pattern           emailPattern    = Pattern.compile(emailPatternStr);
//         Matcher           emailMatcher    = emailPattern.matcher(s);
//
//         while (emailMatcher.find()) {
//             result.add(emailMatcher.group());
//         }
//
//         return result;
//     }
//
//     /**
//      * Verifica se una stringa di una certa lunghezza contiene un pattern il numero di volte indicato [EF 127].
//      * <br><br>
//      *
//      * author 3D Informatica - fgr
//      *
//      * @param s                  Stringa da esaminare.
//      * @param minLength          Lunghezza minima che deve avere la stringa.
//      * @param maxLength          Lunghezza massima che deve avere la stringa.
//      * @param pattern            Pattern da usare per il matching.
//      * @param patternOccurrences Numero di volte che il pattern deve essere contenuto nella stringa.
//      *
//      * @return <code>true</code> se il pattern 'pattern' viene trovato 'patternOccurrences' volte nella stringa 's' (che deve
//      *         rispettare i limiti di lunghezza indicati); <code>false</code> altrimenti.
//      *
//      */
//     public static boolean stringMatchesPattern(String s, int minLength, int maxLength, String pattern, int patternOccurrences) {
//         if (s == null || s.length() < minLength || s.length() > maxLength) return false;
//
//         Pattern p                  = Pattern.compile(pattern);
//         Matcher m                  = p.matcher(s);
//         int     occurrencesCounter = 0;
//
//         while (m.find()) occurrencesCounter++;
//
//         return occurrencesCounter == patternOccurrences;
//     }
//
//     /**
//      * Cerca un ruolo in ACL dato il suo nome o il suo codice [EF 127].
//      * <br><br>
//      *
//      * author 3D Informatica - fgr
//      *
//      * @param conn   Connessione da utilizzare (deve essere <u>gi� connessa</u>).
//      * @param role   Nome o id ("RLXXXX") del ruolo.
//      * @param locked Indica se caricare il ruolo trovato con lock o meno.
//      *
//      * @return Un array di tre oggetti: il documento caricato (XMLDocumento), il numero fisico del documento (Integer) e
//      *         il suo eventuale codice di lock (String).
//      *
//      * @throws Exception in caso di errore.
//      *
//      */
//     public static Object[] findRole(Connessione conn, String role, boolean locked) throws Exception {
//         String query;
//
//         if (GenericUtils.stringMatchesPattern(role, 6, 6, "^RL[0-9]{4}", 1)) {
//             // ricerca per codice di ruolo
//             query = "[/ruolo/@id]=\"" + role + "\"";
//         }
//         else {
//             // ricerca per nome di ruolo
//             query = "[/ruolo/nome]=\" " + role + "\"";
//         }
//
//          return GenericUtils.loadSingleDoc(conn, query, locked);
//     }
//
//     /**
//      * Converte i millisecondi indicati in una stringa nel formato "HHh MMm SSs XXms".
//      * <br><br>
//      *
//      * author 3D Informatica - fgr
//      *
//      * @param millis Millisecondi da convertire.
//      *
//      * @return Una stringa nel formato "HHh MMm SSs XXms".
//      *
//      */
//     public static String millisToString(long millis) {
//         return millis / 3600000 + "h " +
//                (millis % 3600000) / 60000 + "m " +
//                ((millis % 3600000) % 60000) / 1000 + "s " +
//                (((millis % 3600000) % 60000) % 1000) + "ms";
//     }
//     
//     /**
//      * Data la matricola di una persona restituisce l'elenco delle strutture e dei gruppi di responsabilit�.
//      * <br><br>
//      * 
//      * author 3D Informatica - fcossu
//      * @see [EA 440]
//      *
//      * @param connessione La connessione da utilizzare (<b>Nota</b>: deve essere gia' connessa al db dell'anagrafica).
//      * @param persintCode La matricola della persona interna.
//      * @param separ       Il separatore da utilizzare.
//      *
//      * @return L'elenco delle strutture e dei gruppi di responsabilit�; <code>null</code> se non e' possibile trovarne.
//      *
//      * @throws Exception in caso di errore.
//      */
//     public static String getRespGroupStruIntFromPersIntMatricola(Connessione connessione, String matricola, String separ) throws Exception{
//         String respGroupStruInt = "";
//
//         //Ricerco la persona interna con questa matricola
//         String query = "[/gruppo/@cod_responsabile]=\""+matricola+"\" or [/struttura_interna/@cod_responsabile]=\""+matricola+"\"";
//         
//         QueryResult qr = doXWQuery(connessione, query);
//
//         if (qr != null) {
//             log.info("GenericUtils.getRespGroupStruIntFromPersIntMatricola(): found " + qr.elements + " groups and internal structures");
//
//             Vector titoli = (new Titoli(new Selezione(connessione, qr))).getFullTitles(0, qr.elements, null);
//
//             for (int j = 0; j < titoli.size(); j++) {
//                 String titolo  = ((Title)titoli.get(j)).getTitle();
//                 respGroupStruInt += (respGroupStruInt != "" ? separ : "") + titolo.split("\\|")[0].trim();
//             }
//         }
//         else {
//             log.info("GenericUtils.getRespGroupStruIntFromPersIntMatricola(): groups or internal structures not found");
//         }
//
//         return (respGroupStruInt != "" ? respGroupStruInt : null);
//     }
//     
//     /**
//      * Recupera il nome di un gruppo.
//      * <br><br>
//      *
//      * author 3D Informatica - fcossu
//      * @see [EA 440]
//      *
//      * @param connessione La connessione da utilizzare (<b>Nota</b>: deve essere gia' connessa al db dell'anagrafica).
//      * @param struintCode Il codice del gruppo.
//      *
//      * @return Il nome del gruppo; <code>null</code> se non e' possibile trovare il gruppo.
//      *
//      * @throws Exception in caso di errore.
//      *
//      */
//     public static String getGruppoNome(Connessione connessione, String groupCode) throws Exception {
//         String query = "[/gruppo/@id]=\"" + groupCode + "\"";
//
//         log.debug("GenericUtils.getGruppoNome(): executing query: " + query);
//
//         Selezione sel   = new Selezione(connessione);
//         int elements    = sel.Search(query, null, "", 0, 0);
//
//         if (elements == 1) {
//             log.debug("GenericUtils.getGruppoNome(): loading group...");
//
//             XmlDoc xDoc = new XmlDoc(sel.getSelId(), 0, -1, connessione);
//             xDoc.load();
//
//             XMLDocumento struintDoc = xDoc.getDocument();
//
//             return struintDoc.getRootElement().elementText("nome") ;
//         }
//         log.error("GenericUtils.getGruppoNome(): not able to find group \"" + groupCode + "\" [docs found: " + elements + "]");
//
//
//         return null;
//     }
// 	// Massimiliano 28/03/2013: serve una funzione per ripulire i campi per prevenire errori sintattici. 
// 	/**
//      * Funzione per ripulire i campi che possono essere sottoposti a query.
//      * @param baseString qualsiasi stringa
//      * @return stringa corretta 
//      * @author mballerini
//      */	
// 	public static String cleanForQuery(String baseString) {
// 		if ( baseString != null ) {
//	 		baseString = baseString.replaceAll(Pattern.quote("'"), "");
//	 		baseString = baseString.replaceAll(Pattern.quote("="), "");
//	 		baseString = baseString.replaceAll(Pattern.quote("|"), "-");
//	 		baseString = baseString.replaceAll(Pattern.quote("\""), "");
//	 		baseString = baseString.replaceAll(Pattern.quote("&"), "e");
//	 		baseString = baseString.replaceAll(Pattern.quote("{"), "");
//	 		baseString = baseString.replaceAll(Pattern.quote("}"), "");
//	 		baseString = baseString.replaceAll(Pattern.quote("?"), "");
//	 		baseString = baseString.replaceAll(Pattern.quote(";"), "");
//	 		baseString = baseString.replaceAll("\u2026", "");
// 		}
//        return baseString;
// 	}
//
// 	/**
//	 * Ripulisce la stringa contenente XML in input dagli spazi all'inizio e
//	 * alla fine e rimuove eventuali BOM ad inizio stringa.
//	 * 
//	 * @param inputXML
//	 *            La stringa contenente il XML da pulire
//	 * @return La stringa senza spazi e BOM all'inizio/fine o la stringa
//	 *         iniziale se vuota o null.
//	 */
//	public static String cleanXMLString(String inputXML) {
//		String result = inputXML;
//		if (inputXML != null && !inputXML.isEmpty()) {
//			result = result.trim();
//			result = UTFBOMStringCleaner.removeBOM(result);
//		}
//		return result;
//	}
}