package it.tredi.auac.bean;


import it.tredi.auac.orm.entity.MraModel;

import java.io.Serializable;
import java.util.List;

public class MraPageBean implements Serializable {	
	private static final long serialVersionUID = -1124605003860880163L;

	private String sessionId;
	private List<String> messages;

	private MraModel mraModel;

	public MraPageBean() {
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public MraModel getMraModel() {
		return mraModel;
	}

	public void setMraModel(MraModel mraModel) {
		this.mraModel = mraModel;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	
	//map.put("file_nome_enc",Base64.encodeBase64String(atto.getBinaryAttachmentsAppl().getNome().getBytes()));
	//map.put("file_id_enc",Base64.encodeBase64String(atto.getBinaryAttachmentsAppl().getClientid().getBytes()));


}
