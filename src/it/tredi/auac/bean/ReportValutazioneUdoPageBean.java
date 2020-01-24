package it.tredi.auac.bean;

import java.io.Serializable;
import java.util.List;

public class ReportValutazioneUdoPageBean implements Serializable {
	private static final long serialVersionUID = 2700884567597815958L;
	private List<ReportValutazioneUdo> reportValutazioneUdoL;

	public ReportValutazioneUdoPageBean(List<ReportValutazioneUdo> reportValutazioneUdoL) {
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
