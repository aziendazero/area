package it.tredi.auac;

public enum BooleanEnum {
	TRUE("BooleanEnum.true"),
	FALSE("BooleanEnum.false");
	
	private String value;
	 
    private BooleanEnum(String value) {
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
}
