package it.tredi.auac.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class ReportValutazioneComplessivaPageBean implements Serializable {
	private static final long serialVersionUID = 2700884567597815958L;
	
	private BigDecimal mediaComplessiva;
	
	public ReportValutazioneComplessivaPageBean(BigDecimal mediaComplessiva) {
		this.mediaComplessiva = mediaComplessiva;
	}

	public BigDecimal getMediaComplessiva() {
		return mediaComplessiva;
	}
}
