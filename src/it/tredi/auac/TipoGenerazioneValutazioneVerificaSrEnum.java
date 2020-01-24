package it.tredi.auac;

public enum TipoGenerazioneValutazioneVerificaSrEnum {
	// possibili valori: N non aggiorna i dati, P aggiorna il campo VALUTAZIONE, A aggiorna il campo AUTO_VALUTAZIONE
	// possibili valori: N non aggiorna i dati, P aggiorna il campo VALUT_VERIFICATORE, A aggiorna il campo AUTO_VALUT_VERIFICATORE

	NESSUNAGGIORNAMENTO("TipoGenerazioneValutazioneVerificaSrEnum.nessunaggiornamento"),
	PRINCIPALE("TipoGenerazioneValutazioneVerificaSrEnum.principale"),
	APPOGGIO("TipoGenerazioneValutazioneVerificaSrEnum.appoggio");
	
	private String value;
	 
    private TipoGenerazioneValutazioneVerificaSrEnum(String value) {
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
}
