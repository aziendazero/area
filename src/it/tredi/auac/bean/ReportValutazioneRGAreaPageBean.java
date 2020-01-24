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

public class ReportValutazioneRGAreaPageBean implements Serializable {
	private static final long serialVersionUID = 8419210768519417220L;

	private Map<String, AreaReportInfoValutazioneBean> areaReportInfoBeanMap;
	private boolean containReqSiNo = false;
	private boolean containReqSoglia = false;
		
	public ReportValutazioneRGAreaPageBean(List<RequisitoInst> requisiti) throws ParseException {
		areaReportInfoBeanMap = new HashMap<String, AreaReportInfoValutazioneBean>();
		
		String area;
		AreaReportInfoValutazioneBean current;
		for(RequisitoInst req : requisiti) {
			area = AuacUtils.getArea(req);
			if(area != null) {
				current = areaReportInfoBeanMap.get(area);
				if(current == null) {
					current = new AreaReportInfoValutazioneBean(area);
					areaReportInfoBeanMap.put(area, current);
				}
				current.addRequisito(req);
			}
		}
		for(AreaReportInfoValutazioneBean areaRep : areaReportInfoBeanMap.values()) {
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
	
	public List<AreaReportInfoValutazioneBean> getAreaReportInfoBeanL() {
		List<AreaReportInfoValutazioneBean> toRet = new ArrayList<AreaReportInfoValutazioneBean>(areaReportInfoBeanMap.values());
		Collections.sort(toRet, new AreaInfoCrescenteComparator());
		return toRet;
	}
}
