package it.tredi.auac.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CompareUdoModelPageBean implements Serializable {
	
	private static final long serialVersionUID = -2394737439736172214L;
	private List<CompareUdoModelBean> compareUdoBeanL = new ArrayList<CompareUdoModelBean>();
	
	public List<CompareUdoModelBean> getCompareUdoBeanL() {
		return compareUdoBeanL;
	}
	
	public void setCompareUdoBeanL(List<CompareUdoModelBean> compareUdoBeanL) {
		this.compareUdoBeanL = compareUdoBeanL;
	}
	
}
