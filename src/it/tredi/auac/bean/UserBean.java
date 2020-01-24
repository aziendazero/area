package it.tredi.auac.bean;

import java.io.Serializable;

import it.tredi.auac.orm.entity.OperatoreModel;
import it.tredi.auac.orm.entity.TitolareModel;
import it.tredi.auac.orm.entity.UtenteModel;

public class UserBean implements Serializable {
	
	private static final long serialVersionUID = -1827176938220025909L;
	//private String login;
	//private String titolareClientId;
	private UtenteModel utenteModel;
	private OperatoreModel operatoreModel = null;
	private TitolareModel titolareModel = null;
	private WorkflowBean workflowBean = null;
	//private List<Map<String,String>>titolariL;
	private boolean isImitatingOperatore = false; //True if an admin is logged in as an other operator
	
	public final static String TITOLARE_ROLE = "TITOLARE";
	public final static String OPERATORE_TITOLARE_ROLE = "OPERATORE_TITOLARE";
	public final static String OPERATORE_TITOLARE_IN_LETTURA_ROLE = "OPERATORE_TITOLARE_IN_LETTURA";
	public final static String REGIONE_ROLE = "REGIONE";
	public final static String AMMINISTRATORE_ROLE = "AMMINISTRATORE";
	public final static String COLLABORATORE_VALUTAZIONE_ROLE = "COLLABORATORE_VALUTAZIONE";
	public final static String VALUTATORE_INTERNO_ROLE = "VALUTATORE_INTERNO";
	public final static String VERIFICATORE_ROLE = "VERIFICATORE";
	public final static String REGIONE_IN_LETTURA_ROLE = "REGIONE_SOLA_LETTURA";
	public final static String UTENTE_PROGRAMMAZIONE_ROLE = "UTENTE_PROGRAMMAZIONE";
	
	
	public UserBean(UtenteModel utenteModel) {
		//this.login = login;
		this.utenteModel = utenteModel;
	}

	/*
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	*/
	
	public String getTitolareClientId() {
		if(titolareModel != null)
			return titolareModel.getClientid();
		return null;
	}

	/*public void setTitolareClientId(String titolareClientId) {
		this.titolareClientId = titolareClientId;
	}*/

	public TitolareModel getTitolareModel() {
		return titolareModel;
	}

	public void setTitolareModel(TitolareModel titolareModel) {
		this.titolareModel = titolareModel;
	}

	/*public List<Map<String, String>> getTitolariL() {
		return titolariL;
	}

	public void setTitolariL(List<Map<String, String>> titolariL) {
		this.titolariL = titolariL;
	}*/

	public UtenteModel getUtenteModel() {
		return utenteModel;
	}

	public void setUtenteModel(UtenteModel utenteModel) {
		this.utenteModel = utenteModel;
	}
	
	public OperatoreModel getOperatoreModel() {
		return operatoreModel;
	}

	public void setOperatoreModel(OperatoreModel operatoreModel) {
		this.operatoreModel = operatoreModel;
	}

	public WorkflowBean getWorkflowBean() {
		return workflowBean;
	}

	public void setWorkflowBean(WorkflowBean workflowBean) {
		this.workflowBean = workflowBean;
	}

	public boolean isWorkflowUser() {
		return this.workflowBean != null;
	}

	public boolean isTITOLARE() {
		return this.getRuolo().equals(UserBean.TITOLARE_ROLE);
	}

	public boolean isOPERATORE_TITOLARE() {
		return this.getRuolo().equals(UserBean.OPERATORE_TITOLARE_ROLE);
	}	
	
	public boolean isOPERATORE_TITOLARE_IN_LETTURA() {
		return this.getRuolo().equals(UserBean.OPERATORE_TITOLARE_IN_LETTURA_ROLE);
	}	

	public boolean isREGIONE() {
		return this.getRuolo().equals(UserBean.REGIONE_ROLE);
	}

	public boolean isAMMINISTRATORE() {
		return this.getRuolo().equals(UserBean.AMMINISTRATORE_ROLE);
	}	
	
	public boolean isCOLLABORATORE_VALUTAZIONE() {
		return this.getRuolo().equals(UserBean.COLLABORATORE_VALUTAZIONE_ROLE);
	}

	public boolean isVALUTATORE_INTERNO() {
		return this.getRuolo().equals(UserBean.VALUTATORE_INTERNO_ROLE);
	}
	
	public boolean isVERIFICATORE() {
		return this.getRuolo().equals(UserBean.VERIFICATORE_ROLE);
	}
	
	public boolean isREGIONE_IN_LETTURA() {
		return this.getRuolo().equals(UserBean.REGIONE_IN_LETTURA_ROLE);
	}
	
	public boolean isUTENTE_PROGRAMMAZIONE() {
		return this.getRuolo().equals(UserBean.UTENTE_PROGRAMMAZIONE_ROLE);
	}
	
	public boolean mostraElencoDomandeConTask() {
		return isREGIONE() || isVERIFICATORE() || isOPERATORE_TITOLARE();
				//|| isCOLLABORATORE_VALUTAZIONE() || isVALUTATORE_INTERNO();
	}

	public String getRuolo() {
		if(operatoreModel != null)
			return operatoreModel.getRuolo();
		return utenteModel.getRuolo();
	}

	public boolean isImitatingOperatore() {
		return isImitatingOperatore;
	}

	public void setImitatingOperatore(boolean isImmitatingOperatore) {
		this.isImitatingOperatore = isImmitatingOperatore;
	}
	
}
