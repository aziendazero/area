package it.tredi.auac.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import it.tredi.auac.dao.DomandaInstDao;
import it.tredi.auac.orm.entity.RequisitoInst;
import it.tredi.auac.orm.entity.RequisitoTempl;
import it.tredi.auac.orm.entity.UoModel;
import it.tredi.auac.orm.entity.UtenteModel;
import it.tredi.auac.utils.AuacUtils;

public class AutovalutazionePageBean implements Serializable {

	private static final long serialVersionUID = 1821617516617079052L;
	private String messages;
	
	//Utilizzato per titolari e operatori_titolari quando si arriva da selezione UO o UDO
	private UdoUoInstForList udoUoInst;
	
	private boolean allChecked;
	private Map<String,String> checkM;
	private String valutazioneAll;
	private String valutazioneSogliaAll;
	private String personaSelected;
	private List<PersonaSelectOption> personaL;
	private String uoSelected;
	private List<UoModel> uoL;
	private String domandaClientId;
	private String domandaStato;
	
	//per la paginazione
	private int pageIndex; //in base 1
	private int pageCount;
	private final static int REQUISITI_PER_PAGE = 100;
	
	//copia/incolla note/evidenze
	private RequisitoInst copiedRequisitoInst;
	private JobBean neamJobBean;
	private boolean neamNoteChecked;
	private boolean neamEvidenzeChecked;
	private boolean neamNoteVerifChecked;

	//Contiene la lista completa dei requisiti che l'utente corrente puo' visualizzare
	//possono essere i requisiti di una UO o UDO che l'utente corrente puo' visualizzare per TITOLARE e OPERATORE_TITOLARE
	//per l'utente COLLABORATORE_VALUTAZIONE sono tutti i requuisiti della domanda corrente che puo' valutare in quanto a lui assegnati
	private List<RequisitoInst> requisitoInstL;

	//Lista dei requisiti che vengono vissualizzati nella pagina, 
	//possono essere i requisiti di una UO o UDO che l'utente corrente puo' visualizzare per TITOLARE e OPERATORE_TITOLARE
	//per l'utente COLLABORATORE_VALUTAZIONE sono tutti i requuisiti della domanda corrente che puo' valutare in quanto a lui assegnati
	//e possono essere filtrati con una ricerca su vari campi
	private List<RequisitoInst> filteredRequisitoInstL;
	
	private RequisitoSearchBean requisitoSearchBean;
	
	private boolean searchAllRequisiti;
	
	//Per la gestione delle verifiche
	private UtenteModel verificatoreTeamLeader = null;
	private List<UtenteModel> teamVerificatori = null;
	
	//bean per la ricerca delle UdoInst
	UdoInstSearchOrderBean udoInstSearchOrderBean = new UdoInstSearchOrderBean();	
	
	public AutovalutazionePageBean() {
		requisitoSearchBean = new RequisitoSearchBean();
		searchAllRequisiti = false;
		copiedRequisitoInst = null;
		neamJobBean = null;
	}
	
	public boolean isSearchAllRequisiti() {
		return searchAllRequisiti;
	}

	public void setSearchAllRequisiti(boolean searchAllRequisiti) {
		this.searchAllRequisiti = searchAllRequisiti;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	public UdoUoInstForList getUdoUoInst() {
		return udoUoInst;
	}

	public void setUdoUoInst(UdoUoInstForList udoUoInst) {
		this.udoUoInst = udoUoInst;
	}

	public boolean isAllChecked() {
		return allChecked;
	}

	public void setAllChecked(boolean allChecked) {
		this.allChecked = allChecked;
	}

	public Map<String, String> getCheckM() {
		return checkM;
	}

	public void setCheckM(Map<String, String> checkM) {
		this.checkM = checkM;
	}

	public boolean requisitoChecked(int index) {
		return checkM.containsKey(filteredRequisitoInstL.get(index).getClientid());
	}

	public String getValutazioneAll() {
		return valutazioneAll;
	}

	public void setValutazioneAll(String valutazioneAll) {
		this.valutazioneAll = valutazioneAll;
	}

	public String getValutazioneSogliaAll() {
		return valutazioneSogliaAll;
	}

	public void setValutazioneSogliaAll(String valutazioneSogliaAll) {
		this.valutazioneSogliaAll = valutazioneSogliaAll;
	}

	public String getPersonaSelected() {
		return personaSelected;
	}

	public void setPersonaSelected(String personaSelected) {
		this.personaSelected = personaSelected;
	}

	public List<PersonaSelectOption> getPersonaL() {
		return personaL;
	}

	public void setPersonaL(List<PersonaSelectOption> personaL) {
		this.personaL = personaL;
	}

	public String getUoSelected() {
		return uoSelected;
	}

	public void setUoSelected(String uoSelected) {
		this.uoSelected = uoSelected;
	}

	public List<UoModel> getUoL() {
		return uoL;
	}

	public void setUoL(List<UoModel> uoL) {
		this.uoL = uoL;
	}
	
	public RequisitoSearchBean getRequisitoSearchBean() {
		return requisitoSearchBean;
	}

	public void setRequisitoSearchBean(RequisitoSearchBean requisitoSearchBean) {
		this.requisitoSearchBean = requisitoSearchBean;
	}

	public List<RequisitoInst> getFilteredRequisitoInstL() {
		return filteredRequisitoInstL;
	}

	public void setFilteredRequisitoInstL(List<RequisitoInst> filteredRquisitoInstL) {
		this.filteredRequisitoInstL = filteredRquisitoInstL;
		
		//per la paginazione
		pageIndex = 1;
		pageCount = (int)(Math.ceil(filteredRequisitoInstL.size() / (float)AutovalutazionePageBean.REQUISITI_PER_PAGE));
	}

	public List<RequisitoInst> getRequisitoInstL() {
		return requisitoInstL;
	}

	public void setRequisitoInstL(List<RequisitoInst> requisitoInstL) {
		this.requisitoInstL = requisitoInstL;
		this.setFilteredRequisitoInstL(requisitoInstL);
	}
	
	public RequisitoInst getCopiedRequisitoInst() {
		return copiedRequisitoInst;
	}

	public void setCopiedRequisitoInst(RequisitoInst copiedRequisitoInst) {
		this.copiedRequisitoInst = copiedRequisitoInst;
	}

	public JobBean getNeamJobBean() {
		return neamJobBean;
	}

	public void setNeamJobBean(JobBean neamJobBean) {
		this.neamJobBean = neamJobBean;
	}

	public boolean isNeamNoteChecked() {
		return neamNoteChecked;
	}

	public void setNeamNoteChecked(boolean neamNoteChecked) {
		this.neamNoteChecked = neamNoteChecked;
	}

	public boolean isNeamEvidenzeChecked() {
		return neamEvidenzeChecked;
	}

	public void setNeamEvidenzeChecked(boolean neamEvidenzeChecked) {
		this.neamEvidenzeChecked = neamEvidenzeChecked;
	}
	
	public boolean isNeamNoteVerifChecked() {
		return neamNoteVerifChecked;
	}

	public void setNeamNoteVerifChecked(boolean neamNoteVerifChecked) {
		this.neamNoteVerifChecked = neamNoteVerifChecked;
	}

	public String getDomandaClientId() {
		return domandaClientId;
	}

	public void setDomandaClientId(String domandaClientId) {
		this.domandaClientId = domandaClientId;
	}

	public String getDomandaStato() {
		return domandaStato;
	}

	public void setDomandaStato(String domandaStato) {
		this.domandaStato = domandaStato;
	}

	public void clearRequisitoSearchForm() {
		this.getRequisitoSearchBean().setSubStringAssegnazioneReq("");
		this.getRequisitoSearchBean().setSubStringAutovalutazioneReq(""); 
		this.getRequisitoSearchBean().setSubStringIdUnivocoReq("");
		this.getRequisitoSearchBean().setSubStringNoteReq("");
		this.getRequisitoSearchBean().setSubStringTestoReq("");
		this.getRequisitoSearchBean().setSubStringDenominazione("");
		this.getRequisitoSearchBean().setSubStringBlocco("");
		this.getRequisitoSearchBean().setSubStringPiano("");
		this.getRequisitoSearchBean().setSubStringStabilimento("");		
		this.getRequisitoSearchBean().setSubStringSedeOperativa("");		
		this.getRequisitoSearchBean().setSubStringTipologiaUDO("");		
		this.getRequisitoSearchBean().setSubStringVerificatoreReq("");;		
		this.getRequisitoSearchBean().setSubStringValutazioneVerificatoreReq("");		
		this.getRequisitoSearchBean().setSubStringNoteVerificatoreReq("");
	}
	
	public void filterRequisitiL() {
		//se non sono impostati parametri di ricerca ritorno la lista completa
		if(
				(getRequisitoSearchBean().getSubStringAssegnazioneReq() == null || getRequisitoSearchBean().getSubStringAssegnazioneReq().isEmpty()) && 
				(getRequisitoSearchBean().getSubStringAutovalutazioneReq() == null || getRequisitoSearchBean().getSubStringAutovalutazioneReq().isEmpty()) && 
				(getRequisitoSearchBean().getSubStringIdUnivocoReq() == null || getRequisitoSearchBean().getSubStringIdUnivocoReq().isEmpty()) && 
				(getRequisitoSearchBean().getSubStringTipoReq() == null || getRequisitoSearchBean().getSubStringTipoReq().isEmpty()) && 
				(getRequisitoSearchBean().getSubStringNoteReq() == null || getRequisitoSearchBean().getSubStringNoteReq().isEmpty()) && 
				(getRequisitoSearchBean().getSubStringTestoReq() == null || getRequisitoSearchBean().getSubStringTestoReq().isEmpty()) &&
				(getRequisitoSearchBean().getSubStringDenominazione() == null || getRequisitoSearchBean().getSubStringDenominazione().isEmpty()) &&
				(getRequisitoSearchBean().getSubStringTipologiaUDO() == null || getRequisitoSearchBean().getSubStringTipologiaUDO().isEmpty()) &&
				(getRequisitoSearchBean().getSubStringStabilimento() == null || getRequisitoSearchBean().getSubStringStabilimento().isEmpty()) &&
				(getRequisitoSearchBean().getSubStringBlocco() == null || getRequisitoSearchBean().getSubStringBlocco().isEmpty()) &&
				(getRequisitoSearchBean().getSubStringPiano() == null || getRequisitoSearchBean().getSubStringPiano().isEmpty()) &&
				(getRequisitoSearchBean().getSubStringProgressivo() == null || getRequisitoSearchBean().getSubStringProgressivo().isEmpty()) &&
				(getRequisitoSearchBean().getSubStringSedeOperativa() == null || getRequisitoSearchBean().getSubStringSedeOperativa().isEmpty()) &&
				(getRequisitoSearchBean().getSubStringVerificatoreReq() == null || getRequisitoSearchBean().getSubStringVerificatoreReq().isEmpty()) &&
				(getRequisitoSearchBean().getSubStringValutazioneVerificatoreReq() == null || getRequisitoSearchBean().getSubStringValutazioneVerificatoreReq().isEmpty()) &&
				(getRequisitoSearchBean().getSubStringNoteVerificatoreReq() == null || getRequisitoSearchBean().getSubStringNoteVerificatoreReq().isEmpty()) &&
				(getRequisitoSearchBean().isSenzaValutazione() == false) &&
				(getRequisitoSearchBean().isNonAssegnati() == false) && 
				(getRequisitoSearchBean().isSenzaVerifica() == false) &&
				(getRequisitoSearchBean().isNonAssegnatiVerifica() == false)
				) {
			if(isSearchAllRequisiti()) {
				//in questo caso i requisiti potrebbero essere tantissimi quindi se non sono impostati parametri di ricerca non mostro nessun requisito
				setFilteredRequisitoInstL(new ArrayList<RequisitoInst>());
			} else {
				setFilteredRequisitoInstL(requisitoInstL);
			}
			return;
		}
		
		List<RequisitoInst> listRequisitoInst = new ArrayList<RequisitoInst>();
		//sono impostati parametri di ricerca ritorno la lista filtrata
		for(RequisitoInst requisitoInst : requisitoInstL) {
			if(checkRequisitoFilter(requisitoInst))
				listRequisitoInst.add(requisitoInst);
		}				
		setFilteredRequisitoInstL(listRequisitoInst);		
	}

	private boolean checkRequisitoFilter(RequisitoInst requisitoInst) {
		String anagUtenteModel = "";
		if(requisitoInst.getUtenteModel() != null) {
			if(requisitoInst.getUtenteModel().getUoModel() != null)
				anagUtenteModel = requisitoInst.getUtenteModel().getUoModel().getDenominazione() + " ";
			if(requisitoInst.getUtenteModel().getAnagraficaUtenteModel() != null)
				anagUtenteModel += requisitoInst.getUtenteModel().getAnagraficaUtenteModel().getCognome() + " " + requisitoInst.getUtenteModel().getAnagraficaUtenteModel().getNome();
			
		}
		String anagVerificatore = "";
		if(requisitoInst.getVerificatore() != null) {
			if(requisitoInst.getVerificatore().getAnagraficaUtenteModel() != null)
				anagVerificatore = requisitoInst.getVerificatore().getAnagraficaUtenteModel().getCognomeNome();
			
		}
		return checkFieldContainsValue(getRequisitoSearchBean().getSubStringAssegnazioneReq(), anagUtenteModel) &&
			checkAutovalutazioneFieldLikeValue(getRequisitoSearchBean().getSubStringAutovalutazioneReq(), requisitoInst) &&
			checkFieldLikeValue(getRequisitoSearchBean().getSubStringIdUnivocoReq(), requisitoInst.getRequisitoTempl() == null ? "" : requisitoInst.getRequisitoTempl().getNome() ) &&
			checkTipoReqEqualValue(getRequisitoSearchBean().getSubStringTipoReq(), requisitoInst.getRequisitoTempl() ) &&
			checkFieldNotEmptyOrContainsValue(getRequisitoSearchBean().getSubStringNoteReq(), requisitoInst.getNote()) &&
			checkFieldContainsValue(getRequisitoSearchBean().getSubStringTestoReq(), requisitoInst.getRequisitoTempl() == null ? "" : requisitoInst.getRequisitoTempl().getTesto()) &&
			checkFieldContainsValue(getRequisitoSearchBean().getSubStringDenominazione(), requisitoInst.getUoInst() != null ? requisitoInst.getUoInst().getDenominazione() : requisitoInst.getUdoInst() != null ? requisitoInst.getUdoInst().getDescr() : "") &&
			checkFieldContainsValue(getRequisitoSearchBean().getSubStringTipologiaUDO(), requisitoInst.getUdoInst() != null ? requisitoInst.getUdoInst().getTipoUdoExtendedDescr() : "") &&
			checkFieldContainsValue(getRequisitoSearchBean().getSubStringStabilimento(), requisitoInst.getUdoInst() != null ? requisitoInst.getUdoInst().getStabilimento() : "") &&
			checkFieldContainsValue(getRequisitoSearchBean().getSubStringBlocco(), requisitoInst.getUdoInst() != null ? requisitoInst.getUdoInst().getBlocco() : "") &&
			checkFieldContainsValue(getRequisitoSearchBean().getSubStringPiano(), requisitoInst.getUdoInst() != null ? requisitoInst.getUdoInst().getPiano() : "") &&
			checkFieldEqualValue(getRequisitoSearchBean().getSubStringProgressivo(), requisitoInst.getUdoInst() != null ? requisitoInst.getUdoInst().getProgressivo() : null) &&
			checkFieldContainsValue(getRequisitoSearchBean().getSubStringSedeOperativa(), requisitoInst.getUdoInst() != null ? requisitoInst.getUdoInst().getDenominazioneSede() : "") &&
			checkFieldContainsValue(getRequisitoSearchBean().getSubStringVerificatoreReq(), anagVerificatore) &&
			checkFieldNotEmptyOrContainsValue(getRequisitoSearchBean().getSubStringNoteVerificatoreReq(), requisitoInst.getNoteVerificatore()) &&
			checkValutazioneVerificatoreFieldLikeValue(getRequisitoSearchBean().getSubStringValutazioneVerificatoreReq(), requisitoInst) &&
			checkSenzaValutazione(getRequisitoSearchBean().isSenzaValutazione(), requisitoInst) &&
			checkNonAssegnati(getRequisitoSearchBean().isNonAssegnati(), requisitoInst) &&
			checkSenzaVerifica(getRequisitoSearchBean().isSenzaVerifica(), requisitoInst) &&
			checkNonAssegnatiVerifica(getRequisitoSearchBean().isNonAssegnatiVerifica(), requisitoInst)
			;
		//${requisitoInst.uoInst != null}" th:text="${requisitoInst.uoInst.denominazione}
		//requisitoInst.udoInst.descr
	}

	private boolean checkSenzaVerifica(boolean trovaReqSenzaVerifica, RequisitoInst requisitoInst) {
		//se non è richiesto di trovare i requisiti senza risposta lo mostro
		if(!trovaReqSenzaVerifica)
			return true;
		//se è richiesto di trovare i requisiti senza risposta non mostro quelli di tipo titolo
		if(AuacUtils.requisitoIsTitolo(requisitoInst))
			return false;
		//se è richiesto di trovare i requisiti senza risposta allora mostro quelli senza risposta
		if(requisitoInst.getValutazioneVerificatore() == null || requisitoInst.getValutazioneVerificatore().isEmpty())
			return true;
		return false;
	}
	
	private boolean checkNonAssegnatiVerifica(boolean trovaReqNonAssegnatiVerifica, RequisitoInst requisitoInst) {
		//se non è richiesto di trovare i requisiti non assegnati in verifica lo mostro
		if(!trovaReqNonAssegnatiVerifica)
			return true;
		//se è richiesto di trovare i requisiti non assegnati in verifica controllo anche quelli di tipo titolo
		if(requisitoInst.getVerificatore() == null)
			return true;
		return false;
	}

	private boolean checkSenzaValutazione(boolean trovaReqSenzaValutazione, RequisitoInst requisitoInst) {
		//se non è richiesto di trovare i requisiti senza risposta lo mostro
		if(!trovaReqSenzaValutazione)
			return true;
		//se è richiesto di trovare i requisiti senza risposta non mostro quelli di tipo titolo
		if(AuacUtils.requisitoIsTitolo(requisitoInst))
			return false;
		//se è richiesto di trovare i requisiti senza risposta allora mostro quelli senza risposta
		if(requisitoInst.getValutazione() == null || requisitoInst.getValutazione().isEmpty())
			return true;
		return false;
	}

	private boolean checkNonAssegnati(boolean trovaReqNonAssegnati, RequisitoInst requisitoInst) {
		//se non è richiesto di trovare i requisiti non assegnati lo mostro
		if(!trovaReqNonAssegnati)
			return true;
		//se è richiesto di trovare i requisiti non assegnati controllo anche quelli di tipo titolo
		if(requisitoInst.getUtenteModel() == null)
			return true;
		return false;
	}

	private boolean checkTipoReqEqualValue(String subStringTipoTipoSpecReq, RequisitoTempl requisitoTempl) {
		//se non è impostato la stringa da cercare ritorno true come se il controllo fosse valido  
		if(subStringTipoTipoSpecReq == null || subStringTipoTipoSpecReq.isEmpty())
			return true;
		if(requisitoTempl == null)
			return false;
		//e' impostato il valore da cercare se non è impostato il valore del campo ritorno null
		if(requisitoTempl.getTipo() == null)
			return false;
		int pos = subStringTipoTipoSpecReq.indexOf("$");
		String searchTipo = subStringTipoTipoSpecReq.substring(0, pos);
		String searchTipoSpec = subStringTipoTipoSpecReq.substring(pos+1);
		
		if(searchTipoSpec.isEmpty()) {
			return searchTipo.equals(requisitoTempl.getTipo()); 
		} else {
			return searchTipoSpec.equals(requisitoTempl.getTipoSpecifico()); 			
		}

	}

	private boolean checkAutovalutazioneFieldLikeValue(String subStringAutovalutazioneReq, RequisitoInst requisitoInst) {
		//se non è impostato la stringa da cercare ritorno true come se il controllo fosse valido  
		if(subStringAutovalutazioneReq == null || subStringAutovalutazioneReq.isEmpty())
			return true;
		//e' impostato il valore da cercare se non è impostato il valore del campo ritorno null
		if(requisitoInst.getValutazione() == null)
			return false;
		if(requisitoInst.getRequisitoTempl() == null || requisitoInst.getRequisitoTempl().getTipoRisposta() == null)
			return false;
		if(requisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Soglia"))
			return checkFieldEqualValue(subStringAutovalutazioneReq, requisitoInst.getValutazione());
		if(subStringAutovalutazioneReq.equalsIgnoreCase("si") || subStringAutovalutazioneReq.equalsIgnoreCase("sì"))
			return (requisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Si/No") && checkFieldContainsValue("1", requisitoInst.getValutazione())) || checkFieldContainsValue(subStringAutovalutazioneReq, requisitoInst.getValutazione());
		if(subStringAutovalutazioneReq.equalsIgnoreCase("no"))
			return (requisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Si/No") && checkFieldContainsValue("0", requisitoInst.getValutazione())) || checkFieldContainsValue(subStringAutovalutazioneReq, requisitoInst.getValutazione());
		if(subStringAutovalutazioneReq.equalsIgnoreCase("non pertinente"))
			return (requisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Si/No") && checkFieldContainsValue("2", requisitoInst.getValutazione())) || checkFieldContainsValue(subStringAutovalutazioneReq, requisitoInst.getValutazione());
		return !requisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Si/No") && checkFieldContainsValue(subStringAutovalutazioneReq, requisitoInst.getValutazione());
	}

	private boolean checkValutazioneVerificatoreFieldLikeValue(String subStringValutazioneVerificatoreReq, RequisitoInst requisitoInst) {
		//se non è impostato la stringa da cercare ritorno true come se il controllo fosse valido  
		if(subStringValutazioneVerificatoreReq == null || subStringValutazioneVerificatoreReq.isEmpty())
			return true;
		//e' impostato il valore da cercare se non è impostato il valore del campo ritorno null
		if(requisitoInst.getValutazioneVerificatore() == null)
			return false;
		if(requisitoInst.getRequisitoTempl() == null || requisitoInst.getRequisitoTempl().getTipoRisposta() == null)
			return false;
//		if(requisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Soglia"))
//			return checkFieldEqualValue(subStringValutazioneVerificatoreReq, requisitoInst.getValutazioneVerificatore());
		if(subStringValutazioneVerificatoreReq.equalsIgnoreCase("si") || subStringValutazioneVerificatoreReq.equalsIgnoreCase("sì"))
			return (requisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Si/No") && checkFieldContainsValue("1", requisitoInst.getValutazioneVerificatore())) || checkFieldContainsValue(subStringValutazioneVerificatoreReq, requisitoInst.getValutazioneVerificatore());
		if(subStringValutazioneVerificatoreReq.equalsIgnoreCase("no"))
			return (requisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Si/No") && checkFieldContainsValue("0", requisitoInst.getValutazioneVerificatore())) || checkFieldContainsValue(subStringValutazioneVerificatoreReq, requisitoInst.getValutazioneVerificatore());
		if(subStringValutazioneVerificatoreReq.equalsIgnoreCase("non pertinente"))
			return (requisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Si/No") && checkFieldContainsValue("2", requisitoInst.getValutazioneVerificatore())) || checkFieldContainsValue(subStringValutazioneVerificatoreReq, requisitoInst.getValutazioneVerificatore());
		if(subStringValutazioneVerificatoreReq.equalsIgnoreCase("non campionato"))
			return (requisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Si/No") && checkFieldContainsValue("-1", requisitoInst.getValutazioneVerificatore())) 
					|| (requisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Soglia") && checkFieldContainsValue("-1", requisitoInst.getValutazioneVerificatore())) 
					|| checkFieldContainsValue(subStringValutazioneVerificatoreReq, requisitoInst.getValutazioneVerificatore());
		return !requisitoInst.getRequisitoTempl().getTipoRisposta().getNome().equals("Si/No") && checkFieldContainsValue(subStringValutazioneVerificatoreReq, requisitoInst.getValutazioneVerificatore());
	}

	private boolean checkFieldContainsValue(String search, String value) {
		//se non è impostato la stringa da cercare ritorno true come se il controllo fosse valido  
		if(search == null || search.isEmpty())
			return true;
		//e' impostato il valore da cercare se non è impostato il valore del campo ritorno null
		if(value == null)
			return false;
		//il valore da cercare è impostato quindi controllo se è valido
		//Pattern.compile(Pattern.quote(s2), Pattern.CASE_INSENSITIVE).matcher(s1).find();//s2 stringa da cercare s1 stringa in cui si cerca
		if(Pattern.compile(Pattern.quote(search), Pattern.CASE_INSENSITIVE).matcher(value).find())
			return true;
		return false;
	}

	private boolean checkFieldEqualValue(String search, String value) {
		//se non è impostato la stringa da cercare ritorno true come se il controllo fosse valido  
		if(search == null || search.isEmpty())
			return true;
		//e' impostato il valore da cercare se non è impostato il valore del campo ritorno null
		if(value == null)
			return false;
		//il valore da cercare è impostato quindi controllo se è valido
		//Pattern.compile(Pattern.quote(s2), Pattern.CASE_INSENSITIVE).matcher(s1).find();//s2 stringa da cercare s1 stringa in cui si cerca
		if(value.equals(search))
			return true;
		return false;
	}

	private boolean checkFieldEqualValue(String search, BigDecimal value) {
		//se non è impostato la stringa da cercare ritorno true come se il controllo fosse valido  
		if(search == null || search.isEmpty())
			return true;
		//e' impostato il valore da cercare se non è impostato il valore del campo ritorno null
		if(value == null)
			return false;
		//il valore da cercare è impostato quindi controllo se è valido
		BigDecimal valSearch = null;
		try {
			valSearch = new BigDecimal(search);
		} catch (Exception e) {
			//Impossibile convertire il valore in BigDecimal
			return false;
		}
		
		if(valSearch.compareTo(value)==0)
			return true;
		return false;
	}

	private boolean checkFieldNotEmptyOrContainsValue(String search, String value) {
		//se non è impostato la stringa da cercare ritorno true come se il controllo fosse valido  
		if("*".equals(search) && value != null && !value.isEmpty()) {
			return true;
		}
		return checkFieldContainsValue(search, value);
	}

	public boolean isCanFirst() {
		return pageIndex > 1;
	}	
	
	public boolean isCanPrev() {
		return pageIndex > 1;
	}
	
	public boolean isCanNext() {
		return pageIndex < pageCount;
	}	
	
	public boolean isCanLast() {
		return pageIndex < pageCount;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageCount() {
		return pageCount == 0? 1 : pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
	public boolean isIndexInPage(Integer index) {
		int upperBound = pageIndex * AutovalutazionePageBean.REQUISITI_PER_PAGE;
		return index < upperBound && index >= upperBound - AutovalutazionePageBean.REQUISITI_PER_PAGE;
	}
	
	
	private boolean checkFieldLikeValue(String search, String value) {
		//se non è impostato la stringa da cercare ritorno true come se il controllo fosse valido  
		if(search == null || search.isEmpty())
			return true;
		//e' impostato il valore da cercare se non è impostato il valore del campo ritorno null
		if(value == null)
			return false;
		//il valore da cercare è impostato quindi controllo se è valido
		String exprSearch = search.toLowerCase();
		exprSearch = exprSearch.replace(".", "\\.");
		exprSearch = exprSearch.replace("*", ".*");
		exprSearch = exprSearch.replace("?", ".");
		String valueToL = value.toLowerCase();
		return valueToL.matches(exprSearch);	    
	}

	public UtenteModel getVerificatoreTeamLeader() {
		return verificatoreTeamLeader;
	}

	public void setVerificatoreTeamLeader(UtenteModel verificatoreTeamLeader) {
		this.verificatoreTeamLeader = verificatoreTeamLeader;
	}

	public List<UtenteModel> getTeamVerificatori() {
		return teamVerificatori;
	}

	public void setTeamVerificatori(List<UtenteModel> teamVerificatori) {
		this.teamVerificatori = teamVerificatori;
	}

	public boolean showVerificaRow(RequisitoInst requisitoInst) {
		if(DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE.equals(domandaStato) || DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE_INSERIMENTO_VERIFICHE.equals(domandaStato) || DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE_VERIFICA.equals(domandaStato) || requisitoInst.getVerificatore() != null)
			return true;
		return false;
	}

	public UdoInstSearchOrderBean getUdoInstSearchOrderBean() {
		return udoInstSearchOrderBean;
	}

	public void setUdoInstSearchOrderBean(
			UdoInstSearchOrderBean udoInstSearchOrderBean) {
		this.udoInstSearchOrderBean = udoInstSearchOrderBean;
	}
}
