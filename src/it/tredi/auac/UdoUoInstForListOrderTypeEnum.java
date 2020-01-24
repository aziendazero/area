package it.tredi.auac;

public enum UdoUoInstForListOrderTypeEnum {
	Gerarchico("udoUoInstForListOrderTypeEnum.gerarchico"),
	CodiceUnivocoCrescente("udoUoInstForListOrderTypeEnum.codiceUnivocoCrescente"),
	CodiceUnivocoDecrescente("udoUoInstForListOrderTypeEnum.codiceUnivocoDecrescente"),
	TipologiaUdoCrescente("udoUoInstForListOrderTypeEnum.tipologiaUdoCrescente"),
	TipologiaUdoDecrescente("udoUoInstForListOrderTypeEnum.tipologiaUdoDecrescente");
	
	private String value;
	 
    private UdoUoInstForListOrderTypeEnum(String value) {
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
}
