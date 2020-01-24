package it.tredi.auac;

public enum StatoEsitoEnum {
	CONTROLLARE("StatoEsitoEnum.controllare"),
	DEFINITIVO("StatoEsitoEnum.definitivo");
	
	private String value;
	 
    private StatoEsitoEnum(String value) {
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
}
