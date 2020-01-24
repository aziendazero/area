package it.tredi.auac.bean;

import it.tredi.auac.orm.entity.RequisitoInst;

import java.io.Serializable;

public class EditNotePageBean implements Serializable {

	private static final long serialVersionUID = -2121686355206164873L;
	private String messages;
	private String note;
	//Conteiene il valore del campo autoValutazione oppure autoValutazioneVerificatore per la visualizzazione
	private String autoValutazione;
	private RequisitoInst requisitoInst;
	private boolean readonly;
	private boolean noteVerifica;

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public boolean isNoteVerifica() {
		return noteVerifica;
	}

	public void setNoteVerifica(boolean noteVerifica) {
		this.noteVerifica = noteVerifica;
	}

	public String getAutoValutazione() {
		return autoValutazione;
	}

	public void setAutoValutazione(String autoValutazione) {
		this.autoValutazione = autoValutazione;
	}
	
	public String getAutoValutazioneNote() {
		String ret = "";
		if(autoValutazione != null && !autoValutazione.isEmpty())
			ret = autoValutazione + "\n";
		if(note != null && !note.isEmpty())
			ret += note;
		return ret;
	}

	public void setAutoValutazioneNote(String autoValutazioneNote) {
		//Non faccio nulla perche' non e' possibile impstare questo dato
	}
}
