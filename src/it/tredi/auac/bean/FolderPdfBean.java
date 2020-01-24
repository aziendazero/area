package it.tredi.auac.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FolderPdfBean implements Serializable{
	private static final long serialVersionUID = 7564980639487402479L;
	private List<RequisitiGenUdoUoInstForPdf> requisitiGenUdoUoInstForPdfList = null;
	public FolderPdfBean() {
		requisitiGenUdoUoInstForPdfList = new ArrayList<RequisitiGenUdoUoInstForPdf>();
	}
	
	public void addRequisitiGenUdoUoInstForPdf(RequisitiGenUdoUoInstForPdf requisitiGenUdoUoInstForPdf) {
		this.requisitiGenUdoUoInstForPdfList.add(requisitiGenUdoUoInstForPdf);
	}
	
	public List<RequisitiGenUdoUoInstForPdf> getRequisitiGenUdoUoInstForPdfList() {
		return requisitiGenUdoUoInstForPdfList;
	}

}
