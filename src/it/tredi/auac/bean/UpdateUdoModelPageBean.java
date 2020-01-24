package it.tredi.auac.bean;

import it.tredi.auac.orm.entity.TokenSession;

import java.io.Serializable;

public class UpdateUdoModelPageBean implements Serializable {
	private static final long serialVersionUID = 3316415429840219959L;

	private TokenSession tokenSession;
	private String udoUpdateUrl;
	private String udoModelClientId;
	
	public TokenSession getTokenSession() {
		return tokenSession;
	}
	public void setTokenSession(TokenSession tokenSession) {
		this.tokenSession = tokenSession;
	}
	public String getUdoUpdateUrl() {
		return udoUpdateUrl;
	}
	public void setUdoUpdateUrl(String udoUpdateUrl) {
		this.udoUpdateUrl = udoUpdateUrl;
	}
	public String getUdoModelClientId() {
		return udoModelClientId;
	}
	public void setUdoModelClientId(String udoModelClientId) {
		this.udoModelClientId = udoModelClientId;
	}
}
