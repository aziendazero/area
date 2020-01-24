package it.tredi.auac;

public enum InvioReinvioTypeEnum {
	Invio("invioReinvioTypeEnum.invio"),
	Reinvio("invioReinvioTypeEnum.reinvio"),
	Nessuno("invioReinvioTypeEnum.nessuno");
	
	private String value;
	 
    private InvioReinvioTypeEnum(String value) {
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
}
