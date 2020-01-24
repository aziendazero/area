package it.tredi.auac;

public enum TipoPostiLettoEnum {
	PROGRAMMATI("TipoPostiLettoEnum.programmati"),
	ATTUATI("TipoPostiLettoEnum.attuati"),
	RICHIESTI("TipoPostiLettoEnum.richiesti"),
	FLUSSI("TipoPostiLettoEnum.programmati");
	
	private String value;
	
	private TipoPostiLettoEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
