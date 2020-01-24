package it.tredi.auac.bean;

import java.io.Serializable;
import java.util.List;

public class ReportVerificaUdoPageBean implements Serializable {
	private static final long serialVersionUID = 2700884567597815958L;
	private List<ReportValutazioneUdo> reportValutazioneUdoL;

	public ReportVerificaUdoPageBean(List<ReportValutazioneUdo> reportValutazioneUdoL) {
		this.reportValutazioneUdoL = reportValutazioneUdoL;
	}
	
	public List<ReportValutazioneUdo> getReportValutazioneUdoL() {
		return reportValutazioneUdoL;
	}

	public void setReportValutazioneUdoL(
			List<ReportValutazioneUdo> reportValutazioneUdoL) {
		this.reportValutazioneUdoL = reportValutazioneUdoL;
	}

}
