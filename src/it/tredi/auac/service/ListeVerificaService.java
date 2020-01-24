package it.tredi.auac.service;

import it.tredi.auac.RequisitoTemplNomeComparator;
import it.tredi.auac.TipoUdo22CodiceUdoComparator;
import it.tredi.auac.bean.ListaVerificaPageBean;
import it.tredi.auac.bean.ListeVerificaPdfBean;
import it.tredi.auac.bean.UserBean;
import it.tredi.auac.business.ClassificazioneUdoTemplSaluteMentaleDistinctInfo;
import it.tredi.auac.dao.ClassificazioneUdoTemplDao;
import it.tredi.auac.dao.TipoProcTemplDao;
import it.tredi.auac.dao.TipoUdo22TemplDao;
import it.tredi.auac.orm.entity.BindListaRequisito;
import it.tredi.auac.orm.entity.BindTipo22Lista;
import it.tredi.auac.orm.entity.BindUoProcLista;
import it.tredi.auac.orm.entity.ClassificazioneUdoTempl;
import it.tredi.auac.orm.entity.RequisitoTempl;
import it.tredi.auac.orm.entity.TipoProcTempl;
import it.tredi.auac.orm.entity.TipoUdo22Templ;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ListeVerificaService {
	private static final Logger log = Logger.getLogger(ListeVerificaService.class);

	@Autowired
	private TipoProcTemplDao tipoProcTemplDao;

	@Autowired
	private TipoUdo22TemplDao tipoUdo22TemplDao;

	@Autowired
	private ClassificazioneUdoTemplDao classificazioneUdoTemplDao;
	
	@SuppressWarnings("unchecked")
	public ListaVerificaPageBean createListaVerificaPageBean(UserBean userBean) {
		ListaVerificaPageBean listaVerificaPageBean = new ListaVerificaPageBean();

		listaVerificaPageBean.setTipiProcTempl((List<TipoProcTempl>) tipoProcTemplDao.findAll());
		List<TipoUdo22Templ> tipiUdo22 = (List<TipoUdo22Templ>) tipoUdo22TemplDao.findAll();
		Collections.sort(tipiUdo22, new TipoUdo22CodiceUdoComparator());
		listaVerificaPageBean.setTipiUdo22Templ(tipiUdo22);
		listaVerificaPageBean.setClassificazioniUdoTempl((List<ClassificazioneUdoTempl>) classificazioneUdoTemplDao.findAll());
		
		return listaVerificaPageBean;
	}

	/*public boolean validateFormListeVerificaUdo(MessageContext messageContext, ListaVerificaPageBean listaVerificaPageBean) {
		MessageBuilder mB = new MessageBuilder();
		boolean errors = false;
		
		//nessun procedimento selezionato
		if (listaVerificaPageBean.getTipoProcUdoSelected().length() == 0) {
			messageContext.addMessage(mB.error().source("tipoProcUdoSelected").defaultText("Selezionare una tipologia di procedimento").build());
			messageContext.addMessage(mB.error().source("messages").defaultText("Selezionare una tipologia di procedimento").build());
			errors = true;
		}
		
		//nessuna TipoUdo22 selezionato
		if (listaVerificaPageBean.getTipoUdo22Selected().length() == 0) {
			messageContext.addMessage(mB.error().source("tipoUdo22Selected").defaultText("Selezionare una tipologia di UDO").build());
			messageContext.addMessage(mB.error().source("messages").defaultText("Selezionare una tipologia di UDO").build());
			errors = true;
		}		
		
		return !errors;
	}*/

	public ListeVerificaPdfBean getListeVerificheUdoPdfPageBean(ListaVerificaPageBean listaVerificaPageBean) {
		ListeVerificaPdfBean toRet = new ListeVerificaPdfBean();
		
		String idTipoProc = listaVerificaPageBean.getTipoProcUdoSelected();
		toRet.setTipoProcTempl((TipoProcTempl)tipoProcTemplDao.findById(new BigDecimal(idTipoProc)));
		
		String idTipoUdo22 = listaVerificaPageBean.getTipoUdo22Selected();
		toRet.setTipoUdo22ListaVerificaUdo(tipoUdo22TemplDao.findById(new BigDecimal(idTipoUdo22)));
		try {
	    	List<BindTipo22Lista> bt22lL = toRet.getTipoUdo22ListaVerificaUdo().getBindTipo22Listas();
	    	ArrayList<RequisitoTempl> reqTempls = new ArrayList<RequisitoTempl>();
	    	
	    	for (BindTipo22Lista bt22l:bt22lL) { //per ogni lista associata al tipo22
	    		if(bt22l.getTipoProcTempl().getClientid().equals(toRet.getTipoProcTempl().getClientid())) {
		    		/*List<RequisitoTempl> requisitoTemplL  = bt22l.getListaRequisitiTempl().getRequisitoTempls();
		    		for (RequisitoTempl requisitoTempl:requisitoTemplL) { //per ogni requisito della lista
		    			reqTempls.add(requisitoTempl);
		    		}*/
	    			List<BindListaRequisito> bindListaRequisitoL  = bt22l.getListaRequisitiTempl().getBindListaRequisitos();
		    		for (BindListaRequisito bindListaRequisito:bindListaRequisitoL) { //per ogni requisito della lista
		    			if("N".equals(bindListaRequisito.getAnnullato()) && "S".equals(bindListaRequisito.getValidato()) 
		    					&& "N".equals(bindListaRequisito.getRequisitoTempl().getAnnullato()) && "S".equals(bindListaRequisito.getRequisitoTempl().getValidato()))
		    				reqTempls.add(bindListaRequisito.getRequisitoTempl());
		    		}
	    		}
	    	}
	    	
	    	Collections.sort(reqTempls, new RequisitoTemplNomeComparator());
	    	toRet.setRequisitiTempls(reqTempls);

		} catch (Exception ex) {
			log.error("Errore creando il ListeVerificaPdfBean", ex);
		}
		
		return toRet;
	}

	public ListeVerificaPdfBean getListeVerificheUoPdfPageBean(ListaVerificaPageBean listaVerificaPageBean) {
		ListeVerificaPdfBean toRet = new ListeVerificaPdfBean();
		
		String idTipoProc = listaVerificaPageBean.getTipoProcUoSelected();
		toRet.setTipoProcTempl((TipoProcTempl)tipoProcTemplDao.findById(new BigDecimal(idTipoProc)));
		
		String idClass = listaVerificaPageBean.getClassificazioneUdoTemplSelected();
		toRet.setClassificazioneUdoTemplListaVerificaUo(classificazioneUdoTemplDao.findById(new BigDecimal(idClass)));
		try {
	    	List<BindUoProcLista> listBindUoProcLista = toRet.getTipoProcTempl().getBindUoProcListas();
	    	ArrayList<RequisitoTempl> reqTempls = new ArrayList<RequisitoTempl>();
    		for(BindUoProcLista bindUoProcLista:listBindUoProcLista) {
	    		if(bindUoProcLista.getId().getSaluteMentale().equals(ClassificazioneUdoTemplSaluteMentaleDistinctInfo.SALUTE_MENTALE_NO) && bindUoProcLista.getClassificazioneUdoTempl().getClientid().equals(toRet.getClassificazioneUdoTemplListaVerificaUo().getClientid())) {
	    			/*for (RequisitoTempl requisitoTempl:bindUoProcLista.getListaRequisitiTempl().getRequisitoTempls()) { 
	    				//per ogni requisito della lista
		    			reqTempls.add(requisitoTempl);
	    			}*/
	    			for (BindListaRequisito bindListaRequisito:bindUoProcLista.getListaRequisitiTempl().getBindListaRequisitos()) { 
	    				//per ogni requisito della lista
		    			if("N".equals(bindListaRequisito.getAnnullato()) && "S".equals(bindListaRequisito.getValidato()) 
		    					&& "N".equals(bindListaRequisito.getRequisitoTempl().getAnnullato()) && "S".equals(bindListaRequisito.getRequisitoTempl().getValidato()))
		    				reqTempls.add(bindListaRequisito.getRequisitoTempl());
	    			}
	    		}
	    	}
	    	Collections.sort(reqTempls, new RequisitoTemplNomeComparator());
	    	toRet.setRequisitiTempls(reqTempls);

		} catch (Exception ex) {
			log.error("Errore creando il ListeVerificaPdfBean", ex);
		}
		
		return toRet;
	}
	
}
