package it.tredi.auac.bean;

import java.io.Serializable;

public class PersonaSelectOption implements Serializable {

	private static final long serialVersionUID = 4370325465931773430L;
	private String cognomeNome;
	private String utenteModelClientid;
	
	public String getCognomeNome() {
		return cognomeNome;
	}

	public void setCognomeNome(String cognomeNome) {
		this.cognomeNome = cognomeNome;
	}

	public String getUtenteModelClientid() {
		return utenteModelClientid;
	}

	public void setUtenteModelClientid(String utenteModelClientid) {
		this.utenteModelClientid = utenteModelClientid;
	}

}
