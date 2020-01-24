package it.tredi.auac.bean;

import java.io.Serializable;
import java.util.List;

public class CsvReportIstruttoriaPageBean implements Serializable {
	private static final long serialVersionUID = -1098152407296445756L;
	List<UdoUoInstForList> udoUoInstForLists;
	List<String> domandaInstUoInstUdoInstClientIdsConRequisitiNonConformi;
	
	public List<UdoUoInstForList> getUdoUoInstForLists() {
		return udoUoInstForLists;
	}
	public void setUdoUoInstForLists(List<UdoUoInstForList> udoUoInstForLists) {
		this.udoUoInstForLists = udoUoInstForLists;
	}
	
	public List<String> getDomandaInstUoInstUdoInstClientIdsConRequisitiNonConformi() {
		return domandaInstUoInstUdoInstClientIdsConRequisitiNonConformi;
	}
	public void setDomandaInstUoInstUdoInstClientIdsConRequisitiNonConformi(
			List<String> domandaInstUoInstUdoInstClientIdsConRequisitiNonConformi) {
		this.domandaInstUoInstUdoInstClientIdsConRequisitiNonConformi = domandaInstUoInstUdoInstClientIdsConRequisitiNonConformi;
	}
}
