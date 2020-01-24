package it.tredi.auac;

public enum DocTypeAltroEnum {
	Planimetrie("docTypeEnum.planimetrie"),
	PianoDiAdeguamento("docTypeEnum.pianoDiAdeguamento"),
	Oneri("docTypeEnum.oneri"),
	RelazioneAttivita("docTypeEnum.relazioneAttivita"),
	DichiarazioneDiIncompatibilita("docTypeEnum.dichiarazioneDiIncompatibilita"),
	CertificatoCasellario("docTypeEnum.certificatoCasellario"),
	Istruttoria("docTypeEnum.istruttoria"),
	Altro("docTypeEnum.altro");

	private String value;
	 
    private DocTypeAltroEnum(String value) {
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
	
}
