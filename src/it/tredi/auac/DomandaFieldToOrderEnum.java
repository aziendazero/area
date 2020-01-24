package it.tredi.auac;

public enum DomandaFieldToOrderEnum {
	NumeroProcedimento("domandaFieldToOrderEnum.numeroProcedimento"),
	
	Oggetto("domandaFieldToOrderEnum.oggetto"),
	TipoDomanda("domandaFieldToOrderEnum.tipoDomanda"),
	
	Titolare("domandaFieldToOrderEnum.titolare"),
	Stato("domandaFieldToOrderEnum.stato"),
	
	DataCreazione("domandaFieldToOrderEnum.dataCreazione"),
	DataInvio("domandaFieldToOrderEnum.dataInvio"),
	DataValutazioneCompletezzaCorrettezza("domandaFieldToOrderEnum.dataValutazioneCompletezzaCorrettezza"),
	DataValutazioneRispondenzaProgrammazione("domandaFieldToOrderEnum.dataValutazioneRispondenzaProgrammazione"),
	DataConferimentoIncarico("domandaFieldToOrderEnum.dataConferimentoIncarico"),
	DataRedazioneRapportoVerifica("domandaFieldToOrderEnum.dataRedazioneRapportoVerifica"),
	DataPresentazioneProvvedimento("domandaFieldToOrderEnum.dataPresentazioneProvvedimento"),
	DataValutazioneCollegiale("domandaFieldToOrderEnum.dataValutazioneCollegiale"),
	DataConclusione("domandaFieldToOrderEnum.dataConclusione");
	
	private String value;
	 
    private DomandaFieldToOrderEnum(String value) {
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
	
}
