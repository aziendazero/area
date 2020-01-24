package it.tredi.auac;

public enum PostItOrderTypeEnum {
	OperatoreCrescente("postItOrderTypeEnum.operatoreCrescente"),
	OperatoreDecrescente("postItOrderTypeEnum.operatoreDecrescente"),
	DataCrescente("postItOrderTypeEnum.dataCrescente"),
	DataDecrescente("postItOrderTypeEnum.dataDecrescente");
	
	private String value;
	 
    private PostItOrderTypeEnum(String value) {
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
	
}
