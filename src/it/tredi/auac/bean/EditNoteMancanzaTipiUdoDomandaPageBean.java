package it.tredi.auac.bean;

import java.io.Serializable;

public class EditNoteMancanzaTipiUdoDomandaPageBean implements Serializable {

	private static final long serialVersionUID = -4500136544708066808L;
	private String note;
	private boolean readonly;

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	
}
