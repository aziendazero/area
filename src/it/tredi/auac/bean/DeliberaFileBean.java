package it.tredi.auac.bean;

import java.io.Serializable;

import org.apache.commons.codec.binary.Base64;

public class DeliberaFileBean implements Serializable {
	private static final long serialVersionUID = 1891319962193521554L;

	private String fileOggetto;
	private String fileName;
	private String mimeType;
	byte[] fileContent;
	
	public DeliberaFileBean() {
		
	}
	
	public DeliberaFileBean(String fileName, String mimeType, String fileOggetto, byte[] fileContent) {
		this.fileName = fileName;
		this.mimeType = mimeType;
		this.fileOggetto = fileOggetto;
		this.fileContent = fileContent;
	}

	public DeliberaFileBean(String fileName, String mimeType, String fileOggetto) {
		this.fileName = fileName;
		this.mimeType = mimeType;
		this.fileOggetto = fileOggetto;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	public String getFileOggetto() {
		return fileOggetto;
	}

	public void setFileOggetto(String fileOggetto) {
		this.fileOggetto = fileOggetto;
	}
	
	public String getNomeBase64() {
		if(this.fileName != null)
			return Base64.encodeBase64String(this.fileName.getBytes());
		return null;
	}
	
}
