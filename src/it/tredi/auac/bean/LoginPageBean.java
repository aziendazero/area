package it.tredi.auac.bean;

import it.tredi.auac.orm.entity.OperatoreModel;
import it.tredi.auac.orm.entity.UtenteModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoginPageBean implements Serializable {

	private static final long serialVersionUID = 8651134730553493153L;
	
	public final static String lOGINRESULT_NOUSERONDB = "NOUSERONDB";
	public final static String lOGINRESULT_LOGGED = "LOGGED";
	public final static String lOGINRESULT_MULTIROLE = "MULTIROLE";
	
	public final static String ADMIN_ACCESS_TYPE_ALTROOPERATORE = "SELEZIONA_ALTRO_OPERATORE";
	public final static String ADMIN_ACCESS_TYPE_SELALTROOPERATORE = "SELEZIONATO_ALTRO_OPERATORE";

	private String login;
	private String password;
	private List<Map<String,String>>titolariL;
	private boolean admin;
	private boolean verificatore;
	private List<OperatoreModel> operatoriModel;
	private List<UtenteModel> utentiModel;
	
	private String altroOperatoreUserName;
	
	private String loginResult;
	private String adminAccessType = "";
	
	public LoginPageBean() {
		login = "";
		password = "";
		titolariL = new ArrayList<Map<String,String>>();
		admin = false;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public List<Map<String, String>> getTitolariL() {
		return titolariL;
	}

	public void setTitolariL(List<Map<String, String>> titolariL) {
		this.titolariL = titolariL;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	public boolean isVerificatore() {
		return verificatore;
	}

	public void setVerificatore(boolean verificatore) {
		this.verificatore = verificatore;
	}

	public List<OperatoreModel> getOperatoriModel() {
		return operatoriModel;
	}

	public void setOperatoriModel(List<OperatoreModel> operatoriModel) {
		this.operatoriModel = operatoriModel;
	}

	public String getLoginResult() {
		return loginResult;
	}

	public void setLoginResult(String loginResult) {
		this.loginResult = loginResult;
	}

	public String getAltroOperatoreUserName() {
		return altroOperatoreUserName;
	}

	public void setAltroOperatoreUserName(String altroOperatoreUserName) {
		this.altroOperatoreUserName = altroOperatoreUserName;
	}

	public List<UtenteModel> getUtentiModel() {
		return utentiModel;
	}

	public void setUtentiModel(List<UtenteModel> utentiModel) {
		this.utentiModel = utentiModel;
	}

	public String getAdminAccessType() {
		return adminAccessType;
	}

	public void setAdminAccessType(String adminAccessType) {
		this.adminAccessType = adminAccessType;
	}
	
	public boolean isAdminAccessTypeAltroOperatore() {
		return ADMIN_ACCESS_TYPE_ALTROOPERATORE.equals(adminAccessType);
	}

	public boolean isAdminAccessTypeSelezionatoAltroOperatore() {
		return ADMIN_ACCESS_TYPE_SELALTROOPERATORE.equals(adminAccessType);
	}
}
