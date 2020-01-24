package it.tredi.auac;

public enum TypeViewEnum {
	WEB(""),
	PDF(".pdf"),
	CSV(".csv");
	
	private String messagePropertyExtension;
	 
    private TypeViewEnum(String messagePropertyExtension) {
        this.messagePropertyExtension = messagePropertyExtension;
    }
 
    public String getMessagePropertyExtension() {
        return messagePropertyExtension;
    }
}
