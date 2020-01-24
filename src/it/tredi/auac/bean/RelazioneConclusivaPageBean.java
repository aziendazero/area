package it.tredi.auac.bean;

import it.tredi.auac.orm.entity.AttoModel;
import it.tredi.auac.orm.entity.TipoProcTempl;
import it.tredi.auac.orm.entity.TitolareModel;
import it.tredi.auac.orm.entity.UtenteModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class RelazioneConclusivaPageBean implements Serializable {
	private static final long serialVersionUID = -8769967201201073517L;

	private String dataInvioDomanda;
	private TitolareModel titolareDomanda;
	private TipoProcTempl tipoProcTempl;
	private String numeroProcedimento;
	
	//Atti agganciati alle UdoModel da cui sono state create le UdoInst per il tipo procedimento della domanda corrente
	//Riporto l'elenco degli Atti univoci agganciati alle UdoModel delle UdoInst
	private List<AttoModel> attiTipoProcedimentoUdoModelUdoInsts;
	
	//Autorizzazione: 
	//	Note per Integrazione Istruttoria - autorizValComplCorrNoteIstrut 
	//	Note per Integrazione Verifica - autorizValComplCorrNoteVerif
	//Accreditamento
	//	Note per Integrazione Istruttoria - valComplCorrNoteIstrut
	//	Note per Integrazione Verifica - valComplCorrNoteVerif
	private String noteIntegrazioneIstruttoria;
	private String noteIntegrazioneVerifica;
	//valutCongrProgrNote
	private String noteValutazioneCongruenzaProgrammazione;

	//La data dell'ultima richiesta integrazione individuato dall'ultimo task "Cambia Stato Richiesta integrazioni" o "Cambia Stato Rich di integraz val risp program"
	private Date ultimaDataRichiestaIntegrazione;
	
	//La data dell'ultimo invio di integrazioni individuato dall'ultimo task "Richiesta Integrazioni" o "Rich di Integr Congr Program"
	private Date ultimaDataInvioIntegrazione;
	
	//La data in cui e' stato eseguito il task "Redazione Rapporto di Verifica"
	private Date dataRedazioneRapportoVerifica;
	private UtenteModel verificatoreTeamLeader;
	private List<UtenteModel> teamVerificatori;
	//pianificazioneVerificaDateVerifica
	private String dateSvolgimentoVerifica;
	
	private boolean convalidaRapportoVerificaShow = false;
	private String convalidaRapportoVerificaNote;
	
	public String getDataInvioDomanda() {
		return dataInvioDomanda;
	}
	public void setDataInvioDomanda(String dataInvioDomanda) {
		this.dataInvioDomanda = dataInvioDomanda;
	}
	public TitolareModel getTitolareDomanda() {
		return titolareDomanda;
	}
	public void setTitolareDomanda(TitolareModel titolareDomanda) {
		this.titolareDomanda = titolareDomanda;
	}
	public TipoProcTempl getTipoProcTempl() {
		return tipoProcTempl;
	}
	public void setTipoProcTempl(TipoProcTempl tipoProcTempl) {
		this.tipoProcTempl = tipoProcTempl;
	}
	public String getNumeroProcedimento() {
		return numeroProcedimento;
	}
	public void setNumeroProcedimento(String numeroProcedimento) {
		this.numeroProcedimento = numeroProcedimento;
	}
	public List<AttoModel> getAttiTipoProcedimentoUdoModelUdoInsts() {
		return attiTipoProcedimentoUdoModelUdoInsts;
	}
	public void setAttiTipoProcedimentoUdoModelUdoInsts(
			List<AttoModel> attiTipoProcedimentoUdoModelUdoInsts) {
		this.attiTipoProcedimentoUdoModelUdoInsts = attiTipoProcedimentoUdoModelUdoInsts;
	}
	public String getNoteIntegrazioneIstruttoria() {
		return noteIntegrazioneIstruttoria;
	}
	public void setNoteIntegrazioneIstruttoria(String noteIntegrazioneIstruttoria) {
		this.noteIntegrazioneIstruttoria = noteIntegrazioneIstruttoria;
	}
	public String getNoteIntegrazioneVerifica() {
		return noteIntegrazioneVerifica;
	}
	public void setNoteIntegrazioneVerifica(String noteIntegrazioneVerifica) {
		this.noteIntegrazioneVerifica = noteIntegrazioneVerifica;
	}
	public String getNoteValutazioneCongruenzaProgrammazione() {
		return noteValutazioneCongruenzaProgrammazione;
	}
	public void setNoteValutazioneCongruenzaProgrammazione(
			String noteValutazioneCongruenzaProgrammazione) {
		this.noteValutazioneCongruenzaProgrammazione = noteValutazioneCongruenzaProgrammazione;
	}
	public Date getUltimaDataRichiestaIntegrazione() {
		return ultimaDataRichiestaIntegrazione;
	}
	public void setUltimaDataRichiestaIntegrazione(
			Date ultimaDataRichiestaIntegrazione) {
		this.ultimaDataRichiestaIntegrazione = ultimaDataRichiestaIntegrazione;
	}
	public Date getUltimaDataInvioIntegrazione() {
		return ultimaDataInvioIntegrazione;
	}
	public void setUltimaDataInvioIntegrazione(Date ultimaDataInvioIntegrazione) {
		this.ultimaDataInvioIntegrazione = ultimaDataInvioIntegrazione;
	}
	public Date getDataRedazioneRapportoVerifica() {
		return dataRedazioneRapportoVerifica;
	}
	public void setDataRedazioneRapportoVerifica(Date dataRedazioneRapportoVerifica) {
		this.dataRedazioneRapportoVerifica = dataRedazioneRapportoVerifica;
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
	public String getDateSvolgimentoVerifica() {
		return dateSvolgimentoVerifica;
	}
	public void setDateSvolgimentoVerifica(String dateSvolgimentoVerifica) {
		this.dateSvolgimentoVerifica = dateSvolgimentoVerifica;
	}
	public boolean isConvalidaRapportoVerificaShow() {
		return convalidaRapportoVerificaShow;
	}
	public void setConvalidaRapportoVerificaShow(
			boolean convalidaRapportoVerificaShow) {
		this.convalidaRapportoVerificaShow = convalidaRapportoVerificaShow;
	}
	public String getConvalidaRapportoVerificaNote() {
		return convalidaRapportoVerificaNote;
	}
	public void setConvalidaRapportoVerificaNote(
			String convalidaRapportoVerificaNote) {
		this.convalidaRapportoVerificaNote = convalidaRapportoVerificaNote;
	}
	
}
