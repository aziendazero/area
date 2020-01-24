package it.tredi.auac;

public enum ValutazioneTipoSiNoEnum {
	Si("1", "valutazioneTipoSiNoEnum.si"),
	No("0", "valutazioneTipoSiNoEnum.no"),
	NonApplicabile("2", "valutazioneTipoSiNoEnum.nonApplicabile");

	private String key;
	private String value;
	 
    private ValutazioneTipoSiNoEnum(String key, String value) {
        this.key = key;
    	this.value = value;
    }
    
    public String getKey() {
        return key;
    }
 
    public String getValue() {
        return value;
    }
	
    public static ValutazioneTipoSiNoEnum getEnumByKey(String key) {
		for(ValutazioneTipoSiNoEnum enumer : ValutazioneTipoSiNoEnum.values()) {
			if(enumer.getKey().equals(key))
				return enumer;
		}
		return null;
	}
}
