package it.tredi.auac.utils;

public class AuacUserInfo {
	private String ruolo = "";
	private String username = "";
	private String bonitausername = "";
	public String getRuolo() {
		return ruolo;
	}
	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getBonitausername() {
		return bonitausername;
	}
	public void setBonitausername(String bonitausername) {
		this.bonitausername = bonitausername;
	}
	
	public boolean isRegione() {
		return "REGIONE".equals(ruolo);
	}

	public boolean isOperatoreTitolare() {
		return "OPERATORE_TITOLARE".equals(ruolo);
	}

	public boolean isOperatoreTitolareInLettura() {
		return "OPERATORE_TITOLARE_IN_LETTURA".equals(ruolo);
	}

	public boolean isVerificatore() {
		return "VERIFICATORE".equals(ruolo);
	}
	
	public boolean isTitolare() {
		return "TITOLARE".equals(ruolo);
	}

	public boolean isAmministratore() {
		return "AMMINISTRATORE".equals(ruolo);
	}

}
