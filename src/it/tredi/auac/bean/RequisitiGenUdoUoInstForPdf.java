package it.tredi.auac.bean;

import it.tredi.auac.TypeReqList;
import it.tredi.auac.orm.entity.EdificioInst;
import it.tredi.auac.orm.entity.RequisitoInst;
import it.tredi.auac.orm.entity.StrutturaInst;
import it.tredi.auac.orm.entity.UdoInst;
import it.tredi.auac.orm.entity.UoInst;

import java.util.List;

public class RequisitiGenUdoUoInstForPdf {
	private UdoInst udoInst = null;
	private UoInst uoInst = null;
	private StrutturaInst strutturaInst = null;
	private EdificioInst edificioInst = null;
	private TypeReqList typeReqList = null;
	private List<RequisitoInst> requisitoInstList = null;

	public UdoInst getUdoInst() {
		return udoInst;
	}

	public UoInst getUoInst() {
		return uoInst;
	}

	public StrutturaInst getStrutturaInst() {
		return strutturaInst;
	}

	public EdificioInst getEdificioInst() {
		return edificioInst;
	}

	public TypeReqList getTypeReqList() {
		return typeReqList;
	}

	public List<RequisitoInst> getRequisitoInstList() {
		return requisitoInstList;
	}

	public RequisitiGenUdoUoInstForPdf(List<RequisitoInst> requisitoInstList) {
		typeReqList = TypeReqList.RequisitiGeneraliAziendali;
		this.requisitoInstList = requisitoInstList;
	}

	public RequisitiGenUdoUoInstForPdf(UdoInst udoInst, List<RequisitoInst> requisitoInstList) {
		this.udoInst = udoInst;
		typeReqList = TypeReqList.RequisitiUdoInst;
		this.requisitoInstList = requisitoInstList;
	}
	
	public RequisitiGenUdoUoInstForPdf(UoInst uoInst, List<RequisitoInst> requisitoInstList) {
		this.uoInst = uoInst;
		typeReqList = TypeReqList.RequisitiUoInst;
		this.requisitoInstList = requisitoInstList;
	}
	
	public RequisitiGenUdoUoInstForPdf(StrutturaInst strutturaInst, List<RequisitoInst> requisitoInstList) {
		this.strutturaInst = strutturaInst;
		typeReqList = TypeReqList.RequisitiStrutturaInst;
		this.requisitoInstList = requisitoInstList;
	}

	public RequisitiGenUdoUoInstForPdf(EdificioInst edificioInst, List<RequisitoInst> requisitoInstList) {
		this.edificioInst = edificioInst;
		typeReqList = TypeReqList.RequisitiEdificioInst;
		this.requisitoInstList = requisitoInstList;
	}
}

