package it.tredi.auac.bean;

import it.tredi.auac.orm.entity.UtenteModel;

import java.io.Serializable;
import java.util.List;

public class FolderVerbaleVerificaPdfBean extends FolderPdfBean implements Serializable{
	private static final long serialVersionUID = 7958311819193125707L;

	private UtenteModel verificatoreTeamLeader = null;
	private List<UtenteModel> teamVerificatori = null;
	private String pianificazioneVerificaDateVerifica;
	private String noteVerifica;

	public FolderVerbaleVerificaPdfBean() {
		super();
	}
	
	public UtenteModel getVerificatoreTeamLeader() {
		return verificatoreTeamLeader;
	}

	public void setVerificatoreTeamLeader(UtenteModel verificatoreTeamLeader) {
		this.verificatoreTeamLeader = verificatoreTeamLeader;
	}

	public List<UtenteModel> getTeamVerificatori() {
		return teamVerificatori;
	}

	public void setTeamVerificatori(List<UtenteModel> teamVerificatori) {
		this.teamVerificatori = teamVerificatori;
	}

	public String getPianificazioneVerificaDateVerifica() {
		return pianificazioneVerificaDateVerifica;
	}

	public void setPianificazioneVerificaDateVerifica(
			String pianificazioneVerificaDateVerifica) {
		this.pianificazioneVerificaDateVerifica = pianificazioneVerificaDateVerifica;
	}

	public String getNoteVerifica() {
		return noteVerifica;
	}

	public void setNoteVerifica(String noteVerifica) {
		this.noteVerifica = noteVerifica;
	}

}
