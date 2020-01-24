package it.tredi.auac.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CompareUdoDomandaPageBean implements Serializable {
	private static final long serialVersionUID = -2394737439736172214L;
	private List<CompareUdoBean> compareUdoBeanL = new ArrayList<CompareUdoBean>();
	public List<CompareUdoBean> getCompareUdoBeanL() {
		return compareUdoBeanL;
	}
	public void setCompareUdoBeanL(List<CompareUdoBean> compareUdoBeanL) {
		this.compareUdoBeanL = compareUdoBeanL;
	}
	
}
