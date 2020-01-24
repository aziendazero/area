package it.tredi.auac;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum ValutazioneTipoSogliaEnum {
	Soglia0("0", "valutazioneTipoSogliaEnum.soglia0"),
	Soglia60("60", "valutazioneTipoSogliaEnum.soglia60"),
	Soglia100("100", "valutazioneTipoSogliaEnum.soglia100"),
	NonApplicabile("-2", "valutazioneTipoSogliaEnum.nonApplicabile");//Non Pertinente

	private String key;
	private String value;
	 
    private ValutazioneTipoSogliaEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }
    
    public String getKey() {
        return key;
    }
 
    public String getValue() {
        return value;
    }
	
    public static ValutazioneTipoSogliaEnum getEnumByKey(String key) {
		for(ValutazioneTipoSogliaEnum enumer : ValutazioneTipoSogliaEnum.values()) {
			if(enumer.getKey().equals(key))
				return enumer;
		}
		return null;
	}
    
    public static Set<String> valoriValidiPerMedia() {
    	return new HashSet<String>(Arrays.asList("0","60", "100"));
    }
    
}
