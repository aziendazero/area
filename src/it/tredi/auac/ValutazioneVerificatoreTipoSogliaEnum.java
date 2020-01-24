package it.tredi.auac;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum ValutazioneVerificatoreTipoSogliaEnum {
	Soglia0("0", "valutazioneVerificatoreTipoSogliaEnum.soglia0"),
	Soglia60("60", "valutazioneVerificatoreTipoSogliaEnum.soglia60"),
	Soglia100("100", "valutazioneVerificatoreTipoSogliaEnum.soglia100"),
	NonVerificato("-1", "valutazioneVerificatoreTipoSiNoEnum.nonVerificato"),//Non Campionato
	NonApplicabile("-2", "valutazioneVerificatoreTipoSiNoEnum.nonApplicabile");//Non Pertinente

	/*
											  	<option value="0">0%</option>
											  	<option value="60">60%</option>
											  	<option value="100">100%</option>						  
											  	<option value="-1">Non Campionato</option>

	 */
	private String key;
	private String value;
	 
    private ValutazioneVerificatoreTipoSogliaEnum(String key, String value) {
        this.key = key;
    	this.value = value;
    }
 
    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static ValutazioneVerificatoreTipoSogliaEnum getEnumByKey(String key) {
		for(ValutazioneVerificatoreTipoSogliaEnum enumer : ValutazioneVerificatoreTipoSogliaEnum.values()) {
			if(enumer.getKey().equals(key))
				return enumer;
		}
		return null;
	}
    
    public static Set<String> valoriValidiPerMedia() {
    	return new HashSet<String>(Arrays.asList("0","60", "100"));
    }
}
