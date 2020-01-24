package it.tredi.auac.bean;

import it.tredi.auac.orm.entity.RequisitoInst;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class EditEvidenzePageBean implements Serializable {

	private static final long serialVersionUID = -2121686355206164873L;
	private String messages;
	private String evidenze;
	private RequisitoInst requisitoInst;
	private boolean readonly;
	private String evidenzeFolderIdIUnit;
	private List<Map<String,String>> documentsL;

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	public String getEvidenze() {
		return evidenze;
	}

	public void setEvidenze(String evidenze) {
		this.evidenze = evidenze;
	}

	public RequisitoInst getRequisitoInst() {
		return requisitoInst;
	}

	public void setRequisitoInst(RequisitoInst requisitoInst) {
		this.requisitoInst = requisitoInst;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public String getEvidenzeFolderIdIUnit() {
		return evidenzeFolderIdIUnit;
	}

	public void setEvidenzeFolderIdIUnit(String evidenzeFolderIdIUnit) {
		this.evidenzeFolderIdIUnit = evidenzeFolderIdIUnit;
	}

	public List<Map<String, String>> getDocumentsL() {
		return documentsL;
	}

	public void setDocumentsL(List<Map<String, String>> documentsL) {
		this.documentsL = documentsL;
	}

}
