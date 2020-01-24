package it.tredi.auac;

import it.highwaytech.apps.generic.XMLDocumento;

import java.util.List;

import localhost._3diws.services.eXtraWay.EXtraWay;
import localhost._3diws.services.eXtraWay.EXtraWayServiceLocator;

import org.dom4j.Element;

/**
 * @author SStagni
 */
public class ExtraWayService implements Cloneable {
	
	public final static int DEFAULT_TITLE_PAGE_SIZE = 50;
    
    private String host = "localhost";
    private String port = "4859";
    private String user = "admin";
    private String pwd = "reader";
    private String db = "xdocwaydoc";
    private String encoding = "UTF-8";
    private String wsUrl = "http://localhost:8080/3diws/services/eXtraWay";
    private String authUser = null;
    private String authPwd = null;
    int titlePageSize = DEFAULT_TITLE_PAGE_SIZE;
    int indexPageSize = 50;
    private String nomePersona;
    private String nomeUfficio;
    private String smtpHost;
    private String smtpPort;
    private String smtpProtocol;
    private String smtpUser;
    private String smtpPwd;
    private String mailPrincipale;    
    private String mailDebug;   
    private String abilitaInvioMail;   
    
    private EXtraWay eXtraWay; //handler reale per i web services

    public String getAuthPwd() {
        return authPwd;
    }

    public String getAuthUser() {
        return authUser;
    }

    public String getDb() {
        return db;
    }

    public EXtraWay getEXtraWay() {
        return eXtraWay;
    }

    public String getEncoding() {
        return encoding;
    }

    public String getHost() {
        return host;
    }

    public int getIndexPageSize() {
        return indexPageSize;
    }

    public String getPort() {
        return port;
    }

    public String getPwd() {
        return pwd;
    }

    public int getTitlePageSize() {
        return titlePageSize;
    }

    public String getUser() {
        return user;
    }

    public String getWsUrl() {
        return wsUrl;
    }
    
    public ExtraWayService(String host, String port, String user, String pwd, String db, String encoding, int titlePageSize, int indexPageSize, String wsUrl) {
        this(host, port, user, pwd, db, encoding, titlePageSize, indexPageSize, wsUrl, null, null);
    }

    public ExtraWayService(String host, String port, String user, String pwd, String db, String encoding, int titlePageSize, int indexPageSize, String wsUrl, String authUser, String authPwd) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
        this.db = db;
        this.encoding = encoding;
        this.titlePageSize = titlePageSize;
        this.indexPageSize = indexPageSize;
        this.wsUrl = wsUrl;
        this.authUser = authUser;
        this.authPwd = authPwd;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return new ExtraWayService(host, port, user, pwd, db, encoding, titlePageSize, indexPageSize, wsUrl);
    }    
    
    public void init() throws Exception {
        EXtraWayServiceLocator loc = new EXtraWayServiceLocator();
        loc.seteXtraWayEndpointAddress(wsUrl);
        loc.setMaintainSession(true);
        eXtraWay = loc.geteXtraWay();

        if (authUser != null)
            ((org.apache.axis.client.Stub)eXtraWay).setUsername(authUser);
        if (authPwd != null)
            ((org.apache.axis.client.Stub)eXtraWay).setPassword(authPwd);

        eXtraWay.init(host, port, user, pwd, db, encoding, titlePageSize, indexPageSize);
    }
    
    
    //può essere invocato solo successivamente a init()
    public void init(String matricola) throws Exception {
        eXtraWay.init(host, port, user, pwd, matricola, db, encoding, titlePageSize, indexPageSize);
    }    

    @SuppressWarnings("unchecked")
	public XMLDocumento allTitlePages() throws Exception {
    	XMLDocumento document = null;
        XMLDocumento singleDocument = firstTitlePage();
        document = (XMLDocumento)singleDocument.clone();
        while (singleDocument.testAttributeValue("/Response/@canNext", "true")) {
        	singleDocument = nextTitlePage();
        	List<Element> itemsL = singleDocument.selectNodes("//Item");
        	for (Element item:itemsL) {
        		item.detach();
        		document.getRootElement().add(item);
        	}
        }
        return document;
    }    
    
    public XMLDocumento titlePage (int index) throws Exception {
        return new XMLDocumento(eXtraWay.titlePage(index));
    }

    public XMLDocumento firstTitlePage () throws Exception {
        return new XMLDocumento(eXtraWay.firstTitlePage());
    }

    public XMLDocumento prevTitlePage () throws Exception {
        return new XMLDocumento(eXtraWay.prevTitlePage());
    }    
    
    public XMLDocumento nextTitlePage () throws Exception {
        return new XMLDocumento(eXtraWay.nextTitlePage());
    }    
    
    public XMLDocumento lastTitlePage () throws Exception {
        return new XMLDocumento(eXtraWay.lastTitlePage());
    }    
    
    public XMLDocumento currentTitlePage () throws Exception {
        return new XMLDocumento(eXtraWay.currentTitlePage());
    }
    
    public XMLDocumento executeQuery(String query, String selectionId, String orderby, boolean searchOption) throws Exception {
        return new XMLDocumento(eXtraWay.executeQuery(query, selectionId, orderby, searchOption));
    }

    public XMLDocumento thesaurusPage(String keyName, String keyValue, int size, String ruleList, int lastRelationship, boolean explode) throws Exception {
        String s = eXtraWay.thesaurusPage(keyName, keyValue, size, ruleList, lastRelationship, explode);
        if (s == null || s.length() > 0)
            return new XMLDocumento("<Response/>");
        else
            return new XMLDocumento(s);
    }
    
    public static boolean isActiveSelectionEmpty(XMLDocumento document) {
        return document.testAttributeValue("//Selection[@active='true']/@size", "0", false);
    }

    public byte[] getAttachment(String fileId) throws Exception {
        return eXtraWay.getAttachment(fileId);
    }    
    
    public void validateContentFile(int idIUnit) throws Exception {
        eXtraWay.validateContentFile(idIUnit);
    }
    
    public XMLDocumento loadFirstDocument(boolean hierarchy, boolean highlight) throws Exception {
        return new XMLDocumento(eXtraWay.loadFirstDocument(hierarchy, highlight));
    }

    public XMLDocumento loadPrevDocument(boolean hierarchy, boolean highlight) throws Exception {
        return new XMLDocumento(eXtraWay.loadPrevDocument(hierarchy, highlight));
    }
    
    public XMLDocumento loadNextDocument(boolean hierarchy, boolean highlight) throws Exception {
        return new XMLDocumento(eXtraWay.loadNextDocument(hierarchy, highlight));
    }
    
    public XMLDocumento loadLastDocument(boolean hierarchy, boolean highlight) throws Exception {
        return new XMLDocumento(eXtraWay.loadLastDocument(hierarchy, highlight));
    }    
    
    public XMLDocumento loadDocument(int idIUnit) throws Exception {
        return new XMLDocumento(eXtraWay.loadDocument(idIUnit));
    }    
    
    public XMLDocumento loadDocument(int idIUnit, boolean lock, boolean outofset, boolean hierarchy, boolean highlight) throws Exception {
        return new XMLDocumento(eXtraWay.loadDocument(idIUnit, lock, outofset, hierarchy, highlight));
    }
    
    public String saveDocument(XMLDocumento document, boolean modify, int idIUnit) throws Exception {
        return eXtraWay.saveDocument(document.getStringDocument(), modify, idIUnit);
    }

    public String saveDocument2(XMLDocumento document, boolean modify, int idIUnit) throws Exception {
        return eXtraWay.saveDocument(document.getStringXMLDocument(), modify, idIUnit);
    }

    public String saveDocument(XMLDocumento document) throws Exception {
        return eXtraWay.saveDocument(document.getStringDocument());
    }

    public void unlockDocument(int idIUnit) throws Exception {
        eXtraWay.unlockDocument(idIUnit);
    }

    public void deleteDocument(int idIUnit) throws Exception {
        eXtraWay.deleteDocument(idIUnit);
    }

    public String checkInContentFile(int idIUnit, String fileName, byte []fileContent) throws Exception {
        return eXtraWay.checkInContentFile(idIUnit, fileName, fileContent, false, false);
    }
    
    public XMLDocumento newFolder(String xmlFolder) throws java.lang.Exception {
    	return new XMLDocumento(eXtraWay.newFolder(xmlFolder));
    }
            
    public XMLDocumento newSubFolder(String folderParentCode, String folderChildSubject) throws java.lang.Exception {
        return new XMLDocumento(eXtraWay.newSubFolder(folderParentCode, folderChildSubject));
    }
    
    
    public XMLDocumento addInFolder(String xmlFolder) throws Exception {
        return new XMLDocumento(eXtraWay.addInFolder(xmlFolder));                
    }
    
    public static XMLDocumento extractDocumentFromEnvironment(XMLDocumento document) throws Exception {
        Element docEl = (Element)document.selectSingleNode("/Response/Document");
        Element root = (Element)docEl.elements().get(0); //la radice è il primo figlio di /Response/Document/
        root.detach();
        return new XMLDocumento(root);                
    }

	public String getNomePersona() {
		return nomePersona;
	}

	public void setNomePersona(String nomePersona) {
		this.nomePersona = nomePersona;
	}

	public String getNomeUfficio() {
		return nomeUfficio;
	}

	public void setNomeUfficio(String nomeUfficio) {
		this.nomeUfficio = nomeUfficio;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}

	public String getSmtpProtocol() {
		return smtpProtocol;
	}

	public void setSmtpProtocol(String smtpProtocol) {
		this.smtpProtocol = smtpProtocol;
	}

	public String getSmtpUser() {
		return smtpUser;
	}

	public void setSmtpUser(String smtpUser) {
		this.smtpUser = smtpUser;
	}

	public String getSmtpPwd() {
		return smtpPwd;
	}

	public void setSmtpPwd(String smtpPwd) {
		this.smtpPwd = smtpPwd;
	}

	public String getMailPrincipale() {
		return mailPrincipale;
	}

	public void setMailPrincipale(String mailPrincipale) {
		this.mailPrincipale = mailPrincipale;
	}

	public String getMailDebug() {
		return mailDebug;
	}

	public void setMailDebug(String mailDebug) {
		this.mailDebug = mailDebug;
	}

	public String getAbilitaInvioMail() {
		return abilitaInvioMail;
	}

	public void setAbilitaInvioMail(String abilitaInvioMail) {
		this.abilitaInvioMail = abilitaInvioMail;
	}

}
