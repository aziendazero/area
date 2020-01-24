package it.tredi.auac.bean;

import it.tredi.auac.StatoEsitoEnum;
import it.tredi.auac.orm.entity.AttoInst;
import it.tredi.auac.orm.entity.BrancaUdoInstInfo;
import it.tredi.auac.orm.entity.DisciplinaUdoInstInfo;
import it.tredi.auac.orm.entity.DomandaInst;
import it.tredi.auac.orm.entity.EdificioInst;
import it.tredi.auac.orm.entity.StrutturaInst;
import it.tredi.auac.orm.entity.TipoUdoTempl;
import it.tredi.auac.orm.entity.UdoInst;
import it.tredi.auac.orm.entity.UoInst;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UdoUoInstForList implements Serializable {
	private static final long serialVersionUID = -9099296558359054531L;
	private UdoInst udoInst = null;
	private UoInst uoInst = null;
	private DomandaInst domandaInst = null;
	private StrutturaInst strutturaInst = null;
	private EdificioInst edificioInst = null;
	
	public UdoUoInstForList(UserBean userBean, DomandaInst domandaInst) {
		this.domandaInst = domandaInst;
	}

	public UdoUoInstForList(UserBean userBean, StrutturaInst strutturaInst) {
		this.strutturaInst = strutturaInst;
	}

	public UdoUoInstForList(UserBean userBean, EdificioInst edificioInst) {
		this.edificioInst = edificioInst;
	}

	public UdoUoInstForList(UserBean userBean, UdoInst udoInst) {
		this.udoInst = udoInst;
		
		//workaround - serialization & lazy loading
		//20140731 non serve a nulla le ricerche sono su udoInst.getBrancaTempls() e udoInst.getDisciplinaTempls()
		//udoInst.getUdoModel().getBindUdoDisciplinas().size();
		//udoInst.getUdoModel().getBrancaTempls().size();
		//20140801 eliminato per caricamento con EntityGraph
		//udoInst.getDisciplinaTempls().size();
		//udoInst.getBrancaTempls().size();
		
		/*List<RequisitoInst> requisitiInstL = udoInst.getRequisitoInsts(); 
		for (RequisitoInst requisitiInst:requisitiInstL) {
			requisitiInst.getStoricoRisposteRequisitis().size();
		}*/
		
		//filteredRequisitoInstL = buildFilteredRquisitoInstL(userBean, requisitiInstL);
		//calculateRequisitiRisposti();
		//calculateRequisitiAssegnati();
	}
	
	public UdoUoInstForList(UserBean userBean, UoInst uoInst) {
		this.uoInst = uoInst;
		
		//workaround - serialization & lazy loading
		/*List<RequisitoInst> requisitiInstL = uoInst.getRequisitoInsts();
		for (RequisitoInst requisitiInst:requisitiInstL) {
			requisitiInst.getStoricoRisposteRequisitis().size();
		}*/
		
		//filteredRequisitoInstL = buildFilteredRquisitoInstL(userBean, requisitiInstL);
		//calculateRequisitiRisposti();
		//calculateRequisitiAssegnati();
	}
	
	/*private List<RequisitoInst> buildFilteredRquisitoInstL(UserBean userBean, List<RequisitoInst> completeL) {
		Collections.sort(completeL, new Comparator<RequisitoInst>() {
	        @Override
	        public int compare(RequisitoInst req1, RequisitoInst req2) {
	        	return req1.getRequisitoTempl().getNome().compareTo(req2.getRequisitoTempl().getNome());
	        }
	    });		
		
		List<RequisitoInst> l = new ArrayList<RequisitoInst>();
		
		for (RequisitoInst requisitoInst:completeL) {
			if (userBean.isCOLLABORATORE_VALUTAZIONE()) { //nel caso di COLLABORATORE_VALUTAZIONE si scartano i requisiti che non gli sono assegnati
				if (requisitoInst.getUtenteModel() == null || !requisitoInst.getUtenteModel().getClientid().equals(userBean.getUtenteModel().getClientid()))
					continue;
			}
			l.add(requisitoInst);
		}
		
		return l;
	}*/

	public boolean isUdo() {
		if (udoInst != null)
			return true;
		return false;
	}
	
	public boolean isUo() {
		if (uoInst != null)
			return true;
		return false;
	}

	public boolean isStruttura() {
		if (strutturaInst != null)
			return true;
		return false;
	}

	public boolean isEdificio() {
		if (edificioInst != null)
			return true;
		return false;
	}

	public boolean isRequisitiGeneraliAziendali() {
		if (domandaInst != null)
			return true;
		return false;
	}

	public String getCodUnivocoUdo() {
		if (udoInst != null)
			return udoInst.getIdUnivoco();
		return "";
	}
	
	public String getClientId() {
		if(udoInst != null)
			return udoInst.getClientid();
		if(uoInst != null)
			return uoInst.getClientid();
		if(strutturaInst != null)
			return strutturaInst.getClientid();
		if(edificioInst != null)
			return edificioInst.getClientid();
		return domandaInst.getClientid();
	}
	
	public String getDescr() {
		if(udoInst != null)
			return udoInst.getDescr();
		if(uoInst != null)
			return uoInst.getDenominazioneConId();
		if(strutturaInst != null)
			return "Requisiti Struttura - " + strutturaInst.getDenominazione();
		if(edificioInst != null)
			return "Requisiti Edificio - " + edificioInst.getDescr();
		return "Requisiti Generali Aziendali";
	}

	public TipoUdoTempl getTipoUdoTempl() {
		if(udoInst != null)
			return udoInst.getTipoUdoTempl();		
		return null;
	}

	/*era utilizzato ma inutile sostituito con tipoUdoTempl.descr
	public String getDenominazioneUdo() {
		if (udoInst != null)
			return udoInst.getUdoModel().getDescr();
		return "";		
	}*/
	
	public String getUnitaOrganizzativa() {
		if (udoInst != null)
			return udoInst.getDenominazioneUoConId();
		return "";		
	}

	/*Sembra non essere utilizzato
	public String getSedeOperativa() {
		if (udoInst != null)
			return udoInst.getUdoModel().getSedeOperModel().getDenominazione();
		return "";		
	}*/

	public String getDenominazioneSede() {
		if (udoInst != null)
			return udoInst.getDenominazioneSede();
		return "";		
	}
	
	public String getStabilimento() {
		if (udoInst != null)
			return udoInst.getStabilimento();
		return "";		
	}

	public String getBlocco() {
		if (udoInst != null)
			return udoInst.getBlocco();
		return "";		
	}	

	public String getPiano() {
		if (udoInst != null)
			return udoInst.getPiano();
		return "";		
	}		
	
	public String getProgressivo() {
		if (udoInst != null && udoInst.getProgressivo() != null)
			return udoInst.getProgressivo().toPlainString();
		return "";		
	}		
	
	public List<DisciplinaUdoInstInfo> getDisciplineUdoInstsInfo() {
		if (udoInst != null)
			return udoInst.getDisciplineUdoInstsInfo();
		return null;		
	}

	public List<BrancaUdoInstInfo> getBrancheUdoInstsInfo() {
		if (udoInst != null)
			return udoInst.getBrancheUdoInstsInfo();
		return null;
	}
	
	public String getTipoUdoTemplDescr() {
		if (udoInst != null)
			return udoInst.getTipoUdoExtendedDescr();
		return null;
	}
	
	public String getTipoUdoTemplTipoUdo22TemplCodiceUdo() {
		if (udoInst != null)
			return udoInst.getTipoUdoTemplTipoUdo22TemplCodiceUdo();
		return null;
	}

	/*public List<RequisitoInst> getRequisitoInsts() {
		return filteredRequisitoInstL;
	}*/
	
	/*il titolare è lo stesso della DomandaInst veniva utilizzato per caricare le select delle uo ora passo la ShowFolderPageBean e prendo la lista da li
	 * public TitolareModel getTitolareModel() {
		if (udoInst != null)
			return udoInst.getUdoModel().getUoModel().getTitolareModel();
		else
			return uoInst.getUoModel().getTitolareModel();
	}*/
	
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

	public DomandaInst getDomandaInst() {
		return domandaInst;
	}

	public String getEsito() {
		if (udoInst != null)
			return udoInst.getEsito();
		//else if (uoInst != null)
		//	return uoInst.getEsito();
		return "";
	}

	public void setEsito(String esito) {
		if (udoInst != null)
			udoInst.setEsito(esito);
		//else if (uoInst != null)
		//	uoInst.setEsito(esito);
	}
	
	public Date getEsitoDataInizio() {
		if (udoInst != null)
			return udoInst.getEsitoDataInizio();
		//else if (uoInst != null)
		//	return uoInst.getEsitoDataInizio();
		return null;
	}

	public void setEsitoDataInizio(Date dataInizioDate) {
		if (udoInst != null)
			udoInst.setEsitoDataInizio(dataInizioDate);
		//else if (uoInst != null)
		//	uoInst.setEsitoDataInizio(dataInizioDate);

	}
	
	public Date getEsitoScadenza() {
		if (udoInst != null)
			return udoInst.getEsitoScadenza();
		//else if (uoInst != null)
		//	return uoInst.getEsitoScadenza();
		return null;
	}

	public void setEsitoScadenza(Date scadenzaDate) {
		if (udoInst != null)
			udoInst.setEsitoScadenza(scadenzaDate);
		//else if (uoInst != null)
		//	uoInst.setEsitoScadenza(scadenzaDate);
	}

	public String getEsitoNote() {
		if (udoInst != null)
			return udoInst.getEsitoNote();
		//else if (uoInst != null)
		//	return uoInst.getEsitoNote();
		return "";
	}

	public void setEsitoNote(String esitoNote) {
		if (udoInst != null)
			udoInst.setEsitoNote(esitoNote);
		//else if (uoInst != null)
		//	uoInst.setEsitoNote(esitoNote);
	}
	
	public String getEsitoOperatore() {
		if (udoInst != null)
			return udoInst.getEsitoOperatore();
		//else if (uoInst != null)
		//	return uoInst.getEsitoOperatore();
		return "";
	}

	public void setEsitoOperatore(String esitoOperatore) {
		if (udoInst != null)
			udoInst.setEsitoOperatore(esitoOperatore);
		//else if (uoInst != null)
		//	uoInst.setEsitoOperatore(esitoOperatore);
	}
	
	public Date getEsitoTimeStamp() {
		if (udoInst != null)
			return udoInst.getEsitoTimeStamp();
		//else if (uoInst != null)
		//	return uoInst.getEsitoOperatore();
		return null;
	}

	public void setEsitoTimeStamp(Date esitoTimeStamp) {
		if (udoInst != null)
			udoInst.setEsitoTimeStamp(esitoTimeStamp);
		//else if (uoInst != null)
		//	uoInst.setEsitoOperatore(esitoOperatore);
	}

	public String getEsitoDirezioneOperatore() {
		if (udoInst != null)
			return udoInst.getEsitoDirezioneOperatore();
		return "";
	}

	public void setEsitoDirezioneOperatore(String esitoDirezioneOperatore) {
		if (udoInst != null)
			udoInst.setEsitoDirezioneOperatore(esitoDirezioneOperatore);
	}
	
	public StatoEsitoEnum getEsitoStato() {
		if (udoInst != null)
			return udoInst.getEsitoStato();
		return null;
	}

	public void setEsitoStato(StatoEsitoEnum esitoStato) {
		if (udoInst != null)
			udoInst.setEsitoStato(esitoStato);
	}

	public String getAttoInstClientid() {
		if (udoInst != null && udoInst.getAttoInst() != null)
			return udoInst.getAttoInst().getClientid();
		return null;
	}

	public void setAttoInstClientid(String attoInstClientid) {
//		if (udoInst != null)
//			udoInst.setAttoInst(attoInst);
	}
	
	public String getAnnotations() {
		if (udoInst != null)
			return udoInst.getAnnotations();
		else if (uoInst != null)
			return uoInst.getAnnotations();
		return "";
	}

	public void setAnnotations(String notaEsito) {
		if (udoInst != null)
			udoInst.setAnnotations(notaEsito);
		else if (uoInst != null)
			uoInst.setAnnotations(notaEsito);
	}
	
	public boolean isRequisitiRisposti() {
		//return this.requisitiRisposti;
		if (udoInst != null)
			return udoInst.getNumReqSenzaRisposta() == 0;
		else if (uoInst != null)
			return uoInst.getNumReqSenzaRisposta() == 0;
		else if (strutturaInst != null)
			return strutturaInst.getNumReqSenzaRisposta() == 0;
		else if (edificioInst != null)
			return edificioInst.getNumReqSenzaRisposta() == 0;
		return domandaInst.getNumReqSenzaRisposta() == 0;
	}

	/*public void calculateRequisitiRisposti() {
		for (RequisitoInst requisitoInst:getRequisitoInsts()) {
			
			//si saltano i requisiti di tipo Label e quelli senza tipo
			if (requisitoInst.getRequisitoTempl().getTipoRisposta() == null || requisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Titolo"))
				continue;
			
			if (requisitoInst.getValutazione() == null || requisitoInst.getValutazione().length() == 0) {
				this.requisitiRisposti = false;
				return;
			}
		}
		this.requisitiRisposti = true;
	}*/
	
	public boolean isRequisitiAssegnati() {
		//return requisitiAssegnati;
		if (udoInst != null)
			return udoInst.getNumReqNonAssegnati() == 0;
		else if (uoInst != null)
			return uoInst.getNumReqNonAssegnati() == 0;
		else if (strutturaInst != null)
			return strutturaInst.getNumReqNonAssegnati() == 0;
		else if (edificioInst != null)
			return edificioInst.getNumReqNonAssegnati() == 0;
		return domandaInst.getNumReqNonAssegnati() == 0;
	}
	
	/*public void calculateRequisitiAssegnati() {
		for (RequisitoInst requisitoInst:getRequisitoInsts()) {
			if (requisitoInst.getUtenteModel() == null) {
				this.requisitiAssegnati = false;
				return;
			}
		}
		this.requisitiAssegnati = true;
	}*/
	
	public String getUoClientId() {
		if(udoInst != null)
			return udoInst.getUdoModelUoModelClientid();
		else if (uoInst != null)
			return uoInst.getUoModelClientid();
		return "";
	}

	public String getAdmin() {
		if(this.udoInst != null)
			return this.udoInst.getAdmin();
		return null;
	}

	public void setAdmin(String admin) {
		if(this.udoInst != null)
			this.udoInst.setAdmin(admin);
	}
}

