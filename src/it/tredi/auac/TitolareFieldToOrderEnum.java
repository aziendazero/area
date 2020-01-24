package it.tredi.auac;

public enum TitolareFieldToOrderEnum {
	RagioneSociale("titolareFieldToOrderEnum.ragioneSociale"),
	Direzione("titolareFieldToOrderEnum.direzione"),
	PartitaIva("titolareFieldToOrderEnum.partitaIva");
	
	private String value;
	 
    private TitolareFieldToOrderEnum(String value) {
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
	
}
