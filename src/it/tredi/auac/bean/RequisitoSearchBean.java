package it.tredi.auac.bean;

import java.io.Serializable;

public class RequisitoSearchBean implements Serializable {
	private static final long serialVersionUID = 7214116220431429214L;

	//campi per Ricerca Requisiti
	private String subStringIdUnivocoReq;
	private String subStringTipoReq;
	private String subStringTestoReq;
	private String subStringAssegnazioneReq;
	private String subStringAutovalutazioneReq;
	private String subStringNoteReq;
	private String subStringDenominazione;
	private String subStringTipologiaUDO;
	private String subStringStabilimento;
	private String subStringBlocco;
	private String subStringPiano;
	private String subStringProgressivo;
	private String subStringSedeOperativa;

	private String subStringVerificatoreReq;
	private String subStringValutazioneVerificatoreReq;
	private String subStringNoteVerificatoreReq;
	
	private boolean senzaValutazione;
	private boolean nonAssegnati;
	
	private boolean senzaVerifica;
	private boolean nonAssegnatiVerifica;

	public String getSubStringIdUnivocoReq() {
		return subStringIdUnivocoReq;
	}

	public void setSubStringIdUnivocoReq(String subStringIdUnivocoReq) {
		this.subStringIdUnivocoReq = subStringIdUnivocoReq;
	}

	public String getSubStringTipoReq() {
		return subStringTipoReq;
	}

	public void setSubStringTipoReq(String subStringTipoReq) {
		this.subStringTipoReq = subStringTipoReq;
	}

	public String getSubStringTestoReq() {
		return subStringTestoReq;
	}

	public void setSubStringTestoReq(String subStringTestoReq) {
		this.subStringTestoReq = subStringTestoReq;
	}

	public String getSubStringAssegnazioneReq() {
		return subStringAssegnazioneReq;
	}

	public void setSubStringAssegnazioneReq(String subStringAssegnazioneReq) {
		this.subStringAssegnazioneReq = subStringAssegnazioneReq;
	}

	public String getSubStringAutovalutazioneReq() {
		return subStringAutovalutazioneReq;
	}

	public void setSubStringAutovalutazioneReq(String subStringAutovalutazioneReq) {
		this.subStringAutovalutazioneReq = subStringAutovalutazioneReq;
	}

	public String getSubStringNoteReq() {
		return subStringNoteReq;
	}

	public void setSubStringNoteReq(String subStringNoteReq) {
		this.subStringNoteReq = subStringNoteReq;
	}
	
	public String getSubStringDenominazione() {
		return subStringDenominazione;
	}

	public void setSubStringDenominazione(String subStringDenominazione) {
		this.subStringDenominazione = subStringDenominazione;
	}

	public String getSubStringTipologiaUDO() {
		return subStringTipologiaUDO;
	}

	public void setSubStringTipologiaUDO(String subStringTipologiaUDO) {
		this.subStringTipologiaUDO = subStringTipologiaUDO;
	}

	public String getSubStringStabilimento() {
		return subStringStabilimento;
	}

	public void setSubStringStabilimento(String subStringStabilimento) {
		this.subStringStabilimento = subStringStabilimento;
	}

	public String getSubStringBlocco() {
		return subStringBlocco;
	}

	public void setSubStringBlocco(String subStringBlocco) {
		this.subStringBlocco = subStringBlocco;
	}

	public String getSubStringPiano() {
		return subStringPiano;
	}

	public void setSubStringPiano(String subStringPiano) {
		this.subStringPiano = subStringPiano;
	}

	public String getSubStringProgressivo() {
		return subStringProgressivo;
	}

	public void setSubStringProgressivo(String subStringProgressivo) {
		this.subStringProgressivo = subStringProgressivo;
	}

	public String getSubStringSedeOperativa() {
		return subStringSedeOperativa;
	}

	public void setSubStringSedeOperativa(String subStringSedeOperativa) {
		this.subStringSedeOperativa = subStringSedeOperativa;
	}

	public String getSubStringVerificatoreReq() {
		return subStringVerificatoreReq;
	}

	public void setSubStringVerificatoreReq(String subStringVerificatoreReq) {
		this.subStringVerificatoreReq = subStringVerificatoreReq;
	}

	public String getSubStringValutazioneVerificatoreReq() {
		return subStringValutazioneVerificatoreReq;
	}

	public void setSubStringValutazioneVerificatoreReq(
			String subStringValutazioneVerificatoreReq) {
		this.subStringValutazioneVerificatoreReq = subStringValutazioneVerificatoreReq;
	}

	public String getSubStringNoteVerificatoreReq() {
		return subStringNoteVerificatoreReq;
	}

	public void setSubStringNoteVerificatoreReq(String subStringNoteVerificatoreReq) {
		this.subStringNoteVerificatoreReq = subStringNoteVerificatoreReq;
	}

	public boolean isNonAssegnati() {
		return nonAssegnati;
	}

	public void setNonAssegnati(boolean nonAssegnati) {
		this.nonAssegnati = nonAssegnati;
	}

	public boolean isSenzaValutazione() {
		return senzaValutazione;
	}

	public void setSenzaValutazione(boolean senzaValutazione) {
		this.senzaValutazione = senzaValutazione;
	}

	public boolean isSenzaVerifica() {
		return senzaVerifica;
	}

	public void setSenzaVerifica(boolean senzaVerifica) {
		this.senzaVerifica = senzaVerifica;
	}

	public boolean isNonAssegnatiVerifica() {
		return nonAssegnatiVerifica;
	}

	public void setNonAssegnatiVerifica(boolean nonAssegnatiVerifica) {
		this.nonAssegnatiVerifica = nonAssegnatiVerifica;
	}

}
