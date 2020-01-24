package it.tredi.auac.bean;

import java.io.Serializable;

public class EditNoteVerificaDomandaPageBean implements Serializable {
	private static final long serialVersionUID = -4093017616682960510L;
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
