package it.tredi.auac.bean;

import java.io.Serializable;

import org.apache.commons.codec.binary.Base64;

public class FileBinaryAttachmentsApplBean implements Serializable {
	private static final long serialVersionUID = 1891319962193521554L;

	private String intestazioneForm = "File";
	private String descrizioneFileInModifica = "File";
	private String fileName;
	private String binaryAttachmentsApplClientId;
	
	public FileBinaryAttachmentsApplBean() {
		
	}
	
	public FileBinaryAttachmentsApplBean(String intestazioneForm, String descrizioneFileInModifica, String binaryAttachmentsApplClientId, String fileName) {
		this.intestazioneForm = intestazioneForm;
		this.descrizioneFileInModifica = descrizioneFileInModifica;
		this.binaryAttachmentsApplClientId = binaryAttachmentsApplClientId;
		this.fileName = fileName;
	}

	public String getIntestazioneForm() {
		return intestazioneForm;
	}

	public void setIntestazioneForm(String intestazioneForm) {
		this.intestazioneForm = intestazioneForm;
	}

	public String getDescrizioneFileInModifica() {
		return descrizioneFileInModifica;
	}

	public void setDescrizioneFileInModifica(String descrizioneFileInModifica) {
		this.descrizioneFileInModifica = descrizioneFileInModifica;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getBinaryAttachmentsApplClientId() {
		return binaryAttachmentsApplClientId;
	}

	public void setBinaryAttachmentsApplClientId(String binaryAttachmentsApplClientId) {
		this.binaryAttachmentsApplClientId = binaryAttachmentsApplClientId;
	}

	public String getBinaryAttachmentsApplClientIdBase64() {
		if(this.binaryAttachmentsApplClientId != null)
			return Base64.encodeBase64String(this.binaryAttachmentsApplClientId.getBytes());
		return null;
	}
	
}
