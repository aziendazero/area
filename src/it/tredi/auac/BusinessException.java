package it.tredi.auac;

public class BusinessException extends Exception {

	private static final long serialVersionUID = 981589136178663806L;

	public BusinessException(String message) {
		super(message);
	}
	
	public BusinessException(String message, Exception e) {
		super(message, e);
	}

}

//TODO  - inserire livelli: warn, error
//TODO - realizzare meccanismo globale senza utilizzare exception (realizzare classe e in template di base un div che gestisce il messaggio)
//TODO - realizzare meccanismo globale per segnalazione di messaggi basandosi sul messagecontext magari con popup non invasivo, tooltip?