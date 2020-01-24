package it.tredi.auac;

public enum DocTypeEnum {
	Planimetrie("docTypeEnum.planimetrie"),
	PianoDiAdeguamento("docTypeEnum.pianoDiAdeguamento"),
	Oneri("docTypeEnum.oneri"),
	RelazioneAttivita("docTypeEnum.relazioneAttivita"),
	DichiarazioneDiIncompatibilita("docTypeEnum.dichiarazioneDiIncompatibilita"),
	CertificatoCasellario("docTypeEnum.certificatoCasellario"),
	Istruttoria("docTypeEnum.istruttoria"),
	Altro("docTypeEnum.altro");

	private String value;
	 
    private DocTypeEnum(String value) {
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
	
}
