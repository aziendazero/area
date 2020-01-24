package it.tredi.auac;

public enum MotivoBloccoInvioReinvioDomandaEnum {
	NonApplicabile("motivoBloccoInvioReinvioDomandaEnum.nonApplicabile"),
	NessunaUdo("motivoBloccoInvioReinvioDomandaEnum.nessunaUdo"),
	RisposteRequisitiIncomplete("motivoBloccoInvioReinvioDomandaEnum.risposteRequisitiIncomplete"),
	MancanzaAllegati("motivoBloccoInvioReinvioDomandaEnum.mancanzaAllegati"),
	UdoSenzaPostiLettoInDomanda("motivoBloccoInvioReinvioDomandaEnum.udoSenzaPostiLettoInDomanda"),
	UdoTipoModuloSenzaPostiLettoInDomanda("motivoBloccoInvioReinvioDomandaEnum.udoTipoModuloSenzaPostiLettoInDomanda"),
	AltreDomandeNonConcluseConStesseUoUdo("motivoBloccoInvioReinvioDomandaEnum.altreDomandeNonConcluseConStesseUoUdo"),
	AltreDomandeNonConcluseConStesseUdo("motivoBloccoInvioReinvioDomandaEnum.altreDomandeNonConcluseConStesseUdo");
	
	private String value;
	 
    private MotivoBloccoInvioReinvioDomandaEnum(String value) {
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
}
