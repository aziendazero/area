package it.tredi.auac.bean;

import java.io.Serializable;
import java.util.regex.Pattern;

public class SiNoMediaBean implements Serializable {
	private static final long serialVersionUID = 1042217635040098595L;
	
	private int numeroSi = 0;
	private int numeroNo = 0;
	
	public SiNoMediaBean(String valore) {
		if(valore != null) {
			String[] valSiNo = valore.split(Pattern.quote("$"));
			if (valSiNo.length == 2) {
				numeroSi = Integer.parseInt(valSiNo[0]);
				numeroNo = Integer.parseInt(valSiNo[1]);
			}
		}
	}

	public int getNumeroSi() {
		return numeroSi;
	}

	public void setNumeroSi(int numeroSi) {
		this.numeroSi = numeroSi;
	}

	public int getNumeroNo() {
		return numeroNo;
	}

	public void setNumeroNo(int numeroNo) {
		this.numeroNo = numeroNo;
	}
}
