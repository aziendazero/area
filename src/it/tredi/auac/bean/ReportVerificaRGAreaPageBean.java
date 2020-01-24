package it.tredi.auac.bean;

import it.tredi.auac.AreaInfoCrescenteComparator;
import it.tredi.auac.orm.entity.RequisitoInst;
import it.tredi.auac.utils.AuacUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportVerificaRGAreaPageBean implements Serializable {
	private static final long serialVersionUID = 2700884567597815958L;

	private Map<String, AreaReportInfoVerificaBean> areaReportInfoBeanMap;
	private boolean containReqSiNo = false;
	private boolean containReqSoglia = false;
		
	public ReportVerificaRGAreaPageBean(List<RequisitoInst> requisiti) throws ParseException {
		areaReportInfoBeanMap = new HashMap<String, AreaReportInfoVerificaBean>();
		
		String area;
		AreaReportInfoVerificaBean current;
		for(RequisitoInst req : requisiti) {
			area = AuacUtils.getArea(req);
			if(area != null) {
				current = areaReportInfoBeanMap.get(area);
				if(current == null) {
					current = new AreaReportInfoVerificaBean(area);
					areaReportInfoBeanMap.put(area, current);
				}
				current.addRequisito(req);
			}
		}
		for(AreaReportInfoVerificaBean areaRep : areaReportInfoBeanMap.values()) {
			containReqSiNo = containReqSiNo || areaRep.isContainReqSiNo(); 
			containReqSoglia = containReqSoglia || areaRep.isContainReqSoglia(); 
		}	
	}
	
	public boolean isContainReqSiNo() {
		return containReqSiNo;
	}

	public boolean isContainReqSoglia() {
		return containReqSoglia;
	}
	
	public List<AreaReportInfoVerificaBean> getAreaReportInfoBeanL() {
		List<AreaReportInfoVerificaBean> toRet = new ArrayList<AreaReportInfoVerificaBean>(areaReportInfoBeanMap.values());
		Collections.sort(toRet, new AreaInfoCrescenteComparator());
		return toRet;
	}
	
}
