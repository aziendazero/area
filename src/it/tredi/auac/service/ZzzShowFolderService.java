package it.tredi.auac.service;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import it.tredi.auac.bean.CsvReportIstruttoriaPageBean;
import it.tredi.auac.bean.UdoUoInstForList;
import it.tredi.auac.dao.DomandaInstDao;
import it.tredi.auac.orm.entity.DomandaInst;
import it.tredi.auac.orm.entity.EdificioInst;
import it.tredi.auac.orm.entity.StrutturaInst;
import it.tredi.auac.orm.entity.UdoInst;
import it.tredi.auac.orm.entity.UoInst;


@Service
@Transactional
public class ZzzShowFolderService {
	private static final Logger log = Logger.getLogger(ZzzShowFolderService.class);
	
	@Autowired
	private DomandaInstDao domandaInstDao;
	
	@Resource
	private PlatformTransactionManager txManager;
	
	public ZzzShowFolderService() {
		//do nothing
	}


	public CsvReportIstruttoriaPageBean exportCsvReportIstruttoriaUdoVolante(DomandaInst domandaInst) {
		CsvReportIstruttoriaPageBean csvReportIstruttoriaPageBean = new CsvReportIstruttoriaPageBean();
		csvReportIstruttoriaPageBean.setUdoUoInstForLists(getFascicoloUdoLVolante(domandaInst));
		csvReportIstruttoriaPageBean.setDomandaInstUoInstUdoInstClientIdsConRequisitiNonConformi(domandaInstDao.getEntityInstClientIdConRequisitiNonConformi(domandaInst.getClientid()));
		
		return csvReportIstruttoriaPageBean;
	}
	
	public CsvReportIstruttoriaPageBean exportCsvReportIstruttoriaUdoVolante(String domandaClientid) {
		DomandaInst domandaInst = domandaInstDao.getDomandaInstByClientIdForShowFolderEntityGraph(domandaClientid);
		
		CsvReportIstruttoriaPageBean csvReportIstruttoriaPageBean = new CsvReportIstruttoriaPageBean();
		csvReportIstruttoriaPageBean.setUdoUoInstForLists(getFascicoloUdoLVolante(domandaInst));
		csvReportIstruttoriaPageBean.setDomandaInstUoInstUdoInstClientIdsConRequisitiNonConformi(domandaInstDao.getEntityInstClientIdConRequisitiNonConformi(domandaClientid));
		
		return csvReportIstruttoriaPageBean;
	}	
	
	public List<UdoUoInstForList> getFascicoloUdoLVolante(DomandaInst domandaInst) {
		if (domandaInst != null){
			List<UdoUoInstForList> toRet = new ArrayList<UdoUoInstForList>();
			//L'utente che è un collaboratore alla valutazione non accede a questa pagina ma per sicurezza

			UdoUoInstForList udoUoInstForList;
			//Aggiungo la lista dei Requisiti Generali Aziendali della domanda
			udoUoInstForList = new UdoUoInstForList(null, domandaInst);
			toRet.add(udoUoInstForList);
			for(StrutturaInst strutturaInst : domandaInst.getStrutturaInsts()) {
				udoUoInstForList = new UdoUoInstForList(null, strutturaInst);
				toRet.add(udoUoInstForList);
			}
			for(EdificioInst edificioInst : domandaInst.getEdificioInsts()) {
				udoUoInstForList = new UdoUoInstForList(null, edificioInst);
				toRet.add(udoUoInstForList);
			}
			for(UoInst uoInst : domandaInst.getUoInsts()) {
				udoUoInstForList = new UdoUoInstForList(null, uoInst);
				//if (!userBean.isCOLLABORATORE_VALUTAZIONE()) {
					toRet.add(udoUoInstForList);
				//}
				for(UdoInst udoInst : domandaInst.getUdoInsts()) {
					if(udoInst.getUdoModelUoModelClientid().equals(uoInst.getUoModelClientid())) {
						udoUoInstForList = new UdoUoInstForList(null, udoInst);
						//if (!userBean.isCOLLABORATORE_VALUTAZIONE()) {						
							toRet.add(udoUoInstForList);
						//}
					}
				}
			}
			return toRet;
		}
		return null;
	}
	
	
}
