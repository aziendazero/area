package it.tredi.auac;

public enum TitolareOrderTypeEnum {
	RagioneSocialeCrescente("titolareOrderTypeEnum.ragioneSocialeCrescente"),
	RagioneSocialeDecrescente("titolareOrderTypeEnum.ragioneSocialeDecrescente"),

	DirezioneCrescente("titolareOrderTypeEnum.direzioneCrescente"),
	DirezioneDecrescente("titolareOrderTypeEnum.direzioneDecrescente"),

	PartitaIvaCrescente("titolareOrderTypeEnum.partitaIvaCrescente"),
	PartitaIvaDecrescente("titolareOrderTypeEnum.partitaIvaDecrescente");

	
	private String value;
	 
    private TitolareOrderTypeEnum(String value) {
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
	
}
