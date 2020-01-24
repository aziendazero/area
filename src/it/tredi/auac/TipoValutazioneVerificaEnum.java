package it.tredi.auac;

public enum TipoValutazioneVerificaEnum {
	// manualmente (M), automaticamente (A), semi automaticamente (S) cioe' media inserita in campo AUTO_VALUTAZIONE con inserimento manuale in VALUTAZIONE

	MANUALE("TipoValutazioneVerificaEnum.manuale"),
	AUTOMATICA("TipoValutazioneVerificaEnum.automatica"),
	SEMIAUTOMATICA("TipoValutazioneVerificaEnum.semiautomatica");
	
	private String value;
	 
    private TipoValutazioneVerificaEnum(String value) {
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
}
