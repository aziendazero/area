package it.tredi.auac;

public enum DocOrderTypeEnum {
	OperatoreCrescente("docOrderTypeEnum.operatoreCrescente"),
	OperatoreDecrescente("docOrderTypeEnum.operatoreDecrescente"),
	DataCrescente("docOrderTypeEnum.dataCrescente"),
	DataDecrescente("docOrderTypeEnum.dataDecrescente"),
	TipoCrescente("docOrderTypeEnum.tipoCrescente"),
	TipoDecrescente("docOrderTypeEnum.tipoDecrescente");
	
	private String value;
	 
    private DocOrderTypeEnum(String value) {
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
	
}
