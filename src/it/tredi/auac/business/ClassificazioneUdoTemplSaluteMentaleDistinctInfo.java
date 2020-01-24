package it.tredi.auac.business;

import it.tredi.auac.orm.entity.BindUoProcLista;
import it.tredi.auac.orm.entity.UdoInst;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassificazioneUdoTemplSaluteMentaleDistinctInfo {
	public static final String SALUTE_MENTALE_YES = "Y";
	public static final String SALUTE_MENTALE_NO = "N";
	private boolean saluteMentaleInsert = false;
	//private List<ClassificazioneUdoTempl> distinctClassificazioneUdoTempl = new ArrayList<ClassificazioneUdoTempl>();
	private Set<String> classificazioneUdoTemplClientids = new HashSet<String>();
	//private static Set<String> typesL = new HashSet<String>();
	
	public void addUdoInst(UdoInst udoInst) {
		if(udoInst.getTipoUdoTempl().getTipoUdo22Templ().getSaluteMentale().equals(SALUTE_MENTALE_YES))
			saluteMentaleInsert = true;
		classificazioneUdoTemplClientids.add(udoInst.getTipoUdoTempl().getTipoUdo22Templ().getClassificazioneUdoTempl().getClientid());
	}
	
	public void addUdoInstConScarto(UdoInst udoInst, HashSet<String> udoInstsClientIdDaNonConsiderare) {
		if(udoInstsClientIdDaNonConsiderare.contains(udoInst.getClientid()))
			return;
		if(udoInst.getTipoUdoTempl().getTipoUdo22Templ().getSaluteMentale().equals(SALUTE_MENTALE_YES))
			saluteMentaleInsert = true;
		classificazioneUdoTemplClientids.add(udoInst.getTipoUdoTempl().getTipoUdo22Templ().getClassificazioneUdoTempl().getClientid());
	}
	
	public boolean containsInfo() {
		return saluteMentaleInsert || classificazioneUdoTemplClientids.size() > 0;
	}

	public List<BindUoProcLista> getBindUoProcListaDaAggiungere(List<BindUoProcLista> listBindUoProcLista) {
		List<BindUoProcLista> toRet = new ArrayList<BindUoProcLista>();
		for(BindUoProcLista bindUoProcLista:listBindUoProcLista) {
    		if(bindUoProcLista.getId().getSaluteMentale().equals(SALUTE_MENTALE_YES)) {
    			//La lista e' di tipo Salute mentale la aggiungo se e' stata aggiunta almeno una UdoInst di tipo Salute Mentale
    			if(saluteMentaleInsert)
    				toRet.add(bindUoProcLista);
    		} else {
    			//La lista non e' di tipo Salute mentale la aggiungo se e' stata aggiunta almeno una UdoInst della stessa ClassificazioneUdoTempl
    			if(classificazioneUdoTemplClientids.contains(bindUoProcLista.getClassificazioneUdoTempl().getClientid()) ) {
    				toRet.add(bindUoProcLista);
    			}
    		}
    	}
		return toRet;
	}
	
	public List<BindUoProcLista> getBindUoProcListaDaAggiungerePerNuovaUdoInst(List<BindUoProcLista> listBindUoProcLista, UdoInst newUdoInst) {
		List<BindUoProcLista> toRet = new ArrayList<BindUoProcLista>();
		boolean addSaluteMentale = false;
		if(newUdoInst.getTipoUdoTempl().getTipoUdo22Templ().getSaluteMentale().equals(SALUTE_MENTALE_YES) && saluteMentaleInsert == false)
			addSaluteMentale = true;
			
		for(BindUoProcLista bindUoProcLista:listBindUoProcLista) {
    		if(bindUoProcLista.getId().getSaluteMentale().equals(SALUTE_MENTALE_YES)) {
    			//La lista e' di tipo Salute mentale la aggiungo se la newUdoInst e' di tipo salute mentale e il tipo Salute Mentale non e' ancora stato aggiunto
    			if(addSaluteMentale)
    				toRet.add(bindUoProcLista);
    		} else {
    			//La lista non e' di tipo Salute mentale la aggiungo se la ClassificazioneUdoTempl della newUdoInst non e' gia' presente e 
    			//la BindUoProcLista e' della stessa ClassificazioneUdoTempl della newUdoInst
    			if( !classificazioneUdoTemplClientids.contains(newUdoInst.getTipoUdoTempl().getTipoUdo22Templ().getClassificazioneUdoTempl().getClientid()) 
    					&&
    				bindUoProcLista.getClassificazioneUdoTempl().getClientid().equals(newUdoInst.getTipoUdoTempl().getTipoUdo22Templ().getClassificazioneUdoTempl().getClientid())
    					) {
    				//bindUoProcLista.getClassificazioneUdoTempl().getClientid()
    				toRet.add(bindUoProcLista);
    			}
    		}
    	}
		return toRet;
	}
	
	public boolean removeReqSaluteMentaleOnRemoveUdoInst(UdoInst removedUdoInst) {
		if(removedUdoInst.getTipoUdoTempl().getTipoUdo22Templ().getSaluteMentale().equals(SALUTE_MENTALE_YES) && saluteMentaleInsert == false)
			return true;
		return false;
	}
	
	public boolean removeReqClassificazioneUdoTemplOnRemoveUdoInst(UdoInst removedUdoInst) {
		if(classificazioneUdoTemplClientids.contains(removedUdoInst.getTipoUdoTempl().getTipoUdo22Templ().getClassificazioneUdoTempl().getClientid()))
			return false;
		return true;
	}

}
