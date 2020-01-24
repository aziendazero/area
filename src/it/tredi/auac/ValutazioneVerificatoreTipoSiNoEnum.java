package it.tredi.auac;

public enum ValutazioneVerificatoreTipoSiNoEnum {
	Si("1", "valutazioneVerificatoreTipoSiNoEnum.si"),
	No("0", "valutazioneVerificatoreTipoSiNoEnum.no"),
	NonApplicabile("2", "valutazioneVerificatoreTipoSiNoEnum.nonApplicabile"), //Non Pertinente
	NonVerificato("-1", "valutazioneVerificatoreTipoSiNoEnum.nonVerificato");  //Non Campionato

	/*
											  	<option value="1">Sì</option>
											  	<option value="0">No</option>
											  	<option value="2">Non Pertinente</option>
											  	<option value="-1">Non Campionato</option>
	 */
	private String key;
	private String value;
	 
    private ValutazioneVerificatoreTipoSiNoEnum(String key, String value) {
    	this.key = key;
        this.value = value;
    }
 
    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
    
    public static ValutazioneVerificatoreTipoSiNoEnum getEnumByKey(String key) {
		for(ValutazioneVerificatoreTipoSiNoEnum enumer : ValutazioneVerificatoreTipoSiNoEnum.values()) {
			if(enumer.getKey().equals(key))
				return enumer;
		}
		return null;
	}
}
