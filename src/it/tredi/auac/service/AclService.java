package it.tredi.auac.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;
import org.springframework.stereotype.Service;

import it.tredi.auac.InvioReinvioTypeEnum;
import it.tredi.auac.PostItTypeEnum;
import it.tredi.auac.bean.AutovalutazionePageBean;
import it.tredi.auac.bean.PostIt;
import it.tredi.auac.bean.ShowFolderPageBean;
import it.tredi.auac.bean.ShowUdoInstPageBean;
import it.tredi.auac.bean.UdoUoInstForList;
import it.tredi.auac.bean.UserBean;
import it.tredi.auac.dao.DomandaInstDao;
import it.tredi.auac.orm.entity.EsitoTempl;
import it.tredi.auac.orm.entity.RequisitoInst;
import it.tredi.auac.orm.entity.TipoUdoUtenteTempl;
import it.tredi.auac.orm.entity.UdoInst;
import it.tredi.auac.orm.entity.UtenteModel;
import it.tredi.auac.utils.AuacUtils;

@Service
public class AclService {
	
	//private static final Logger log = Logger.getLogger(AclService.class);
	private static Map<String, Integer>indexMap;
	private static boolean[][] editDomandaMatrix;
	private static boolean[][] addRemoveUdoUoDomandaMatrix;
	private static boolean[][] editNoteDomandaMatrix;
	private static boolean[][] editFileDomandaMatrix;

	//private static boolean[][] editValutazioneDomandaMatrix;
	private static boolean[][] checkTipoUdo22DomandaMatrix;
	
	static {
		indexMap = new HashMap<String, Integer>();
		
		//indici dei ruoli
		indexMap.put(UserBean.REGIONE_ROLE, new Integer(0));
		indexMap.put(UserBean.AMMINISTRATORE_ROLE, new Integer(1));
		indexMap.put(UserBean.TITOLARE_ROLE, new Integer(2));
		indexMap.put(UserBean.OPERATORE_TITOLARE_ROLE, new Integer(3));
		indexMap.put(UserBean.COLLABORATORE_VALUTAZIONE_ROLE, new Integer(4));
		indexMap.put(UserBean.VALUTATORE_INTERNO_ROLE, new Integer(5));
		indexMap.put(UserBean.VERIFICATORE_ROLE, new Integer(6));
		indexMap.put(UserBean.OPERATORE_TITOLARE_IN_LETTURA_ROLE, new Integer(7));
		indexMap.put(UserBean.REGIONE_IN_LETTURA_ROLE, new Integer(8));
		indexMap.put(UserBean.UTENTE_PROGRAMMAZIONE_ROLE, new Integer(9));
		
		//indici degli stati di una domanda
		int posOfArray = 0;
		indexMap.put(DomandaInstDao.STATO_BOZZA, new Integer(posOfArray++));
		indexMap.put(DomandaInstDao.STATO_AVVIATO, new Integer(posOfArray++));
		indexMap.put(DomandaInstDao.STATO_FASE_ISTRUTTORIA, new Integer(posOfArray++));
		indexMap.put(DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONE_DOCUMENTI_AUTOVALUTAZIONI, new Integer(posOfArray++));
		indexMap.put(DomandaInstDao.STATO_INVIATA, new Integer(posOfArray++));
		//numero 5
		indexMap.put(DomandaInstDao.PROCEDIMENTO_CONCLUSO, new Integer(posOfArray++));

		indexMap.put(DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONE_AUTOVALUTAZIONI, new Integer(posOfArray++));
		indexMap.put(DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONE_DOCUMENTI, new Integer(posOfArray++));
		indexMap.put(DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE, new Integer(posOfArray++));
		//21/04/2016 gestione nuovi stati
		indexMap.put(DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_INSERIMENTO_ESITI, new Integer(posOfArray++));
		//numero 10
		indexMap.put(DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_VALUTAZIONE, new Integer(posOfArray++));
		
		indexMap.put(DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONE_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE, new Integer(posOfArray++));

		indexMap.put(DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_COMPLETATA, new Integer(posOfArray++));
		indexMap.put(DomandaInstDao.STATO_ISTRUTTORIA_COMPLETATA, new Integer(posOfArray++));
		indexMap.put(DomandaInstDao.STATO_PROCEDIMENTO_IN_VALUTAZIONE, new Integer(posOfArray++));
		//nunmero 15
		indexMap.put(DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE, new Integer(posOfArray++));
		//21/04/2016 gestione nuovi stati
		indexMap.put(DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE_INSERIMENTO_VERIFICHE, new Integer(posOfArray++));
		indexMap.put(DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE_VERIFICA, new Integer(posOfArray++));
		
		
		indexMap.put(DomandaInstDao.STATO_REDAZIONE_RAPPORTO_DI_VERIFICA, new Integer(posOfArray++));
		indexMap.put(DomandaInstDao.STATO_INSERIMENTO_ESITO, new Integer(posOfArray++));
		//21/04/2016 gestione nuovi stati
		//numero 20
		indexMap.put(DomandaInstDao.STATO_INSERIMENTO_ESITO_INSERIMENTO_ESITI, new Integer(posOfArray++));
		indexMap.put(DomandaInstDao.STATO_INSERIMENTO_ESITO_VERIFICA, new Integer(posOfArray++));
		
		indexMap.put("TITOLARE_FOLDER", new Integer(posOfArray++));

		//Flusso stato domanda
		//BOZZA > PROCEDIMENTO AVVIATO(STATO_AVVIATO) >
		//								RICHIESTA_INTEGRAZIONE_DOCUMENTI_AUTOVALUTAZIONI or RICHIESTA_INTEGRAZIONE_AUTOVALUTAZIONI or RICHIESTA_INTEGRAZIONE_DOCUMENTI > INVIATA (equivalente a PROCEDIMENTO AVVIATO)
		//								VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE >
		//										RICHIESTA_INTEGRAZIONE_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE > VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE
		//										ISTRUTTORIA_COMPLETATA >
		//													PROCEDIMENTO CONCLUSO [FINE]
		//													PROCEDIMENTO IN VALUTAZIONE > INSERIMENTO_ESITO > PROCEDIMENTO CONCLUSO
		
		//init matrice editDomandaMatrix gestione modifica Autovalutazioni
		editDomandaMatrix = new boolean[][]{
			//BOZZA		AVVIATO		FASE_IST	RIC_INT_D&A		INVIATA		CONCLUSO	RIC_INT_A	RIC_INT_D	VAL_RP	VAL_RP_I	VAL_RP_V	RIC_INT_RP	VAL_RPC	ISTR_COMPL	PROC_VAL	GEST_VERIF	GEST_VER_I	GEST_VER_V	RED_RAP_V	INS_ESITO	INS_ES_I	INS_ES_V	TIT_FOLDER	
			{false 		,false		,false		,false			,false		,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false}, //REGIONE
			{true       ,true		,true       ,true           ,true       ,false		,true		,true		,true	,true		,true		,true		,true	,true		,true		,true		,false		,false		,true		,true		,true		,true		,false}, //AMMINISTRATORE
			{true       ,false		,false      ,true           ,false      ,false		,true		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false}, //TITOLARE
			{true       ,false      ,false      ,true           ,false      ,false		,true		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false}, //OPERATORE_TITOLARE
			{true       ,false      ,false      ,true           ,false      ,false		,true		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false}, //COLLABORATORE_VALUTAZIONE
			{true       ,false      ,false      ,true           ,false      ,false		,true		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false}, //VALUTATORE INTERNO
			{false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false}, //VERIFICATORE
			{false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false}, //OPERATORE_TITOLARE_IN_LETTURA
			{false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false}, //REGIONE_SOLA_LETTURA
			{false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false}  //UTENTE_PROGRAMMAZIONE
		};

		//init matrice addRemoveUdoUoDomandaMatrix
		addRemoveUdoUoDomandaMatrix = new boolean[][]{
			//BOZZA		AVVIATO		FASE_IST	RIC_INT_D&A		INVIATA		CONCLUSO	RIC_INT_A	RIC_INT_D	VAL_RP	VAL_RP_I	VAL_RP_V	RIC_INT_RP	VAL_RPC	ISTR_COMPL	PROC_VAL	GEST_VERIF	GEST_VER_I	GEST_VER_V	RED_RAP_V	INS_ESITO	INS_ES_I	INS_ES_V	TIT_FOLDER	
			{false 		,false		,false		,false			,false		,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //REGIONE
			{true       ,true       ,true       ,true           ,true       ,false		,true		,true		,true	,true		,true		,true		,true	,true		,true		,true		,true		,true		,true		,true		,true		,true		,false},  //AMMINISTRATORE
			{true       ,false      ,false      ,true           ,false      ,false		,true		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //TITOLARE
			{true       ,false      ,false      ,true           ,false      ,false		,true		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //OPERATORE_TITOLARE
			{false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //COLLABORATORE_VALUTAZIONE
			{false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //VALUTATORE INTERNO
			{false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //VERIFICATORE
			{false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //OPERATORE_TITOLARE_IN_LETTURA
			{false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //REGIONE_SOLA_LETTURA
			{false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false}   //UTENTE_PROGRAMMAZIONE
		}; 
		
		//init matrice editNoteDomandaMatrix
		editNoteDomandaMatrix = new boolean[][]{
			//BOZZA		AVVIATO		FASE_IST	RIC_INT_D&A		INVIATA		CONCLUSO	RIC_INT_A	RIC_INT_D	VAL_RP	VAL_RP_I	VAL_RP_V	RIC_INT_RP	VAL_RPC	ISTR_COMPL	PROC_VAL	GEST_VERIF	GEST_VER_I	GEST_VER_V	RED_RAP_V	INS_ESITO	INS_ES_I	INS_ES_V	TIT_FOLDER	
			{false		,false		,true		,false			,true		,false		,false		,false		,true	,false		,false		,false		,true	,true		,true		,false		,false		,false		,false		,true		,true		,true		,true },  //REGIONE
			{true       ,true       ,true       ,true           ,true       ,false      ,true		,true		,true	,true		,true		,true		,true	,true		,true		,true		,true		,true		,true		,true		,true		,true		,true },  //AMMINISTRATORE
			{true       ,false      ,false      ,false          ,false      ,false      ,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,true },  //TITOLARE
			{true       ,false      ,false      ,false          ,false      ,false      ,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,true },  //OPERATORE_TITOLARE
    		{false      ,false      ,false      ,false          ,false      ,false      ,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //COLLABORATORE_VALUTAZIONE
	    	{false      ,false      ,false      ,false          ,false      ,false      ,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //VALUTATORE INTERNO
		    {false      ,false      ,false      ,false          ,false      ,false      ,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,true },  //VERIFICATORE
			{false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //OPERATORE_TITOLARE_IN_LETTURA
			{false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //REGIONE_SOLA_LETTURA
			{false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false}   //UTENTE_PROGRAMMAZIONE
		};
		
		//init matrice editFileDomandaMatrix
		editFileDomandaMatrix = new boolean[][]{
			//BOZZA		AVVIATO		FASE_IST	RIC_INT_D&A		INVIATA		CONCLUSO	RIC_INT_A	RIC_INT_D	VAL_RP	VAL_RP_I	VAL_RP_V	RIC_INT_RP	VAL_RPC	ISTR_COMPL	PROC_VAL	GEST_VERIF	GEST_VER_I	GEST_VER_V	RED_RAP_V	INS_ESITO	INS_ES_I	INS_ES_V	TIT_FOLDER	
			{false		,false		,false		,false			,false		,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //REGIONE
			{true       ,true       ,true       ,true           ,true       ,false		,true		,true		,true	,true		,true		,true		,true	,true		,true		,true		,true		,true		,true		,true		,true		,true		,false},  //AMMINISTRATORE
			{true       ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //TITOLARE
			{true       ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //OPERATORE_TITOLARE
    		{false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //COLLABORATORE_VALUTAZIONE
	    	{false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //VALUTATORE INTERNO
		    {false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //VERIFICATORE
			{false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //OPERATORE_TITOLARE_IN_LETTURA
			{false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //REGIONE_SOLA_LETTURA
			{false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false}   //UTENTE_PROGRAMMAZIONE
		};
		
		//init matrice checkTipoUdo22DomandaMatrix
		checkTipoUdo22DomandaMatrix = new boolean[][]{
			//BOZZA		AVVIATO		FASE_IST	RIC_INT_D&A		INVIATA		CONCLUSO	RIC_INT_A	RIC_INT_D	VAL_RP	VAL_RP_I	VAL_RP_V	RIC_INT_RP	VAL_RPC	ISTR_COMPL	PROC_VAL	GEST_VERIF	GEST_VER_I	GEST_VER_V	RED_RAP_V	INS_ESITO	INS_ES_I	INS_ES_V	TIT_FOLDER	
			{true       ,true       ,true       ,true           ,true       ,false		,true		,true		,true	,true		,true		,true		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //REGIONE
			{true       ,true       ,true       ,true           ,true       ,false		,true		,true		,true	,true		,true		,true		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //AMMINISTRATORE
			{true       ,true       ,true       ,true           ,true       ,false		,true		,true		,true	,true		,true		,true		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //TITOLARE
			{true       ,true       ,true       ,true           ,true       ,false		,true		,true		,true	,true		,true		,true		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //OPERATORE_TITOLARE
			{false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //COLLABORATORE_VALUTAZIONE
			{false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //VALUTATORE INTERNO
			{false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //VERIFICATORE
			{false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //OPERATORE_TITOLARE_IN_LETTURA
			{false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false},  //REGIONE_SOLA_LETTURA
			{false      ,false      ,false      ,false          ,false      ,false		,false		,false		,false	,false		,false		,false		,false	,false		,false		,false		,false		,false		,false		,false		,false		,false		,false}   //UTENTE_PROGRAMMAZIONE
		}; 		
	}
	
	public AclService() {
		//do nothing
	}

	//assegnazione, risposte ai risultati
	public boolean userCanEditDomanda(String userRole, String statoDomanda) {
		if (statoDomanda == null || statoDomanda.length() == 0)
			statoDomanda = "TITOLARE_FOLDER";
		return editDomandaMatrix[indexMap.get(userRole)][indexMap.get(statoDomanda)];
	}
	
	//aggiunta/rimozione udo
	public boolean userCanAddRemoveUdoUoDomanda(String userRole, String statoDomanda) {
		if (statoDomanda == null || statoDomanda.length() == 0)
			statoDomanda = "TITOLARE_FOLDER";
		return addRemoveUdoUoDomandaMatrix[indexMap.get(userRole)][indexMap.get(statoDomanda)];
	}
	
	public boolean userCanEditUdoModel(UdoUoInstForList udoUoInstForList, UserBean userBean, ShowFolderPageBean pageBean) {
		if (pageBean.getFascicolo().getFolderStatus() == null || pageBean.getFascicolo().getFolderStatus().length() == 0)
			return false;
		if(userBean.isREGIONE() || userBean.isOPERATORE_TITOLARE() || userBean.isTITOLARE() || userBean.isAMMINISTRATORE()) {
			if(
					(
							(DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE.equals(pageBean.getFascicolo().getFolderStatus()) || DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_INSERIMENTO_ESITI.equals(pageBean.getFascicolo().getFolderStatus()))
							&&
							UDOUO_ESITO_AMMESSACONRISERVA.equals(udoUoInstForList.getEsito())
					)
					||
					(
							(DomandaInstDao.STATO_INSERIMENTO_ESITO.equals(pageBean.getFascicolo().getFolderStatus()) || DomandaInstDao.STATO_INSERIMENTO_ESITO_INSERIMENTO_ESITI.equals(pageBean.getFascicolo().getFolderStatus()))
							&&
							(
								"Autorizzata con prescrizioni".equalsIgnoreCase(udoUoInstForList.getEsito()) ||
								"Accreditata con prescrizioni".equalsIgnoreCase(udoUoInstForList.getEsito())	
							)
					)
			)
				return true;	
		}
		return false;
		
	}

	public boolean notaDomandaChecked(PostIt postIt) {
		return PostIt.STATO_CONTROLLATO.equals(postIt.getStato());
	}

	public boolean notaDomandaToCheck(PostIt postIt) {
		return PostIt.STATO_DA_CONTROLLARE.equals(postIt.getStato());
	}

	public boolean userCanCheckNoteDomanda(PostIt postIt, String userRole, String statoDomanda) {
		if((DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE.equals(statoDomanda) || DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_INSERIMENTO_ESITI.equals(statoDomanda)) && postIt.isDeletable() && postIt.getTipo() == PostItTypeEnum.MancanzaTipiUdo && PostIt.STATO_DA_CONTROLLARE.equals(postIt.getStato())) {
			return true;
		}
		return false;
	}

	//aggiunta/rimozione note
	public boolean userCanEditNoteDomanda(PostIt postIt, String userRole, String statoDomanda) {
		if(postIt.isDeletable()) {
			switch (postIt.getTipo()) {
				case Generico:
					return userCanEditNoteDomanda(userRole, statoDomanda);
				case MancanzaTipiUdo:
					return userCanInsertNoteMancanzaTipiUdo(userRole, statoDomanda) && postIt.getStato().isEmpty();
			}
		}
		return false;
	}

	//aggiunta/rimozione note
	public boolean userCanEditNoteDomanda(String userRole, String statoDomanda) {
		if (statoDomanda == null || statoDomanda.length() == 0)
			statoDomanda = "TITOLARE_FOLDER";
		return editNoteDomandaMatrix[indexMap.get(userRole)][indexMap.get(statoDomanda)];
	}

	//aggiunta/rimozione file
	public boolean userCanEditFileDomanda(String userRole, String statoDomanda) {
		if (statoDomanda == null || statoDomanda.length() == 0)
			statoDomanda = "TITOLARE_FOLDER";
		return editFileDomandaMatrix[indexMap.get(userRole)][indexMap.get(statoDomanda)];
	}
	
	public boolean userCanCheckSpecchiettiPL(UserBean userBean) {
		String userRole = userBean.getRuolo();
		return userRole.equals(UserBean.OPERATORE_TITOLARE_ROLE) || userRole.equals(UserBean.OPERATORE_TITOLARE_IN_LETTURA_ROLE) ||
				userRole.equals(UserBean.REGIONE_ROLE) || userRole.equals(UserBean.UTENTE_PROGRAMMAZIONE_ROLE);
	}
	
	//modificabilePerValtuazioneRegione
	//Sostituisce
	//${userBean.REGIONE} and *{fascicolo.modificabilePerValtuazioneRegione}
	//con
	//${theAclService.userCanEditEsito(userBean.ruolo,pageBean.fascicolo.folderStatus)}
	/*
	public void calcolaSeModificabilePerValtuazioneRegione(ShowFolderPageBean showFolderPageBean) {
		if (((showFolderPageBean.getFascicolo().getFolderStatus().equals(DomandaInstDao.STATO_FASE_ISTRUTTORIA))) && showFolderPageBean.getFascicolo().getFolderType().equals(FolderDao.DOMANDA_TITOLARE_FOLDER_TYPE))
			showFolderPageBean.getFascicolo().setModificabilePerValtuazioneRegione(true);
		else
			showFolderPageBean.getFascicolo().setModificabilePerValtuazioneRegione(false);
	}
	 */

	public boolean userCanFilterForUdoAssegnate(UserBean userBean, ShowFolderPageBean pageBean) {
		String userRole = userBean.getRuolo();
		String statoDomanda = pageBean.getFascicolo().getFolderStatus();
		return (DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE.equals(statoDomanda) || DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_INSERIMENTO_ESITI.equals(statoDomanda) || DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_VALUTAZIONE.equals(statoDomanda)) 
					&&
				userRole.equals(UserBean.REGIONE_ROLE);
	}
	
	public boolean userCanFilterForUdoSenzaEsito(UserBean userBean, ShowFolderPageBean pageBean) {
		String userRole = userBean.getRuolo();
		String statoDomanda = pageBean.getFascicolo().getFolderStatus();
		return (DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE.equals(statoDomanda) 
				|| DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_INSERIMENTO_ESITI.equals(statoDomanda) 
				|| DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_VALUTAZIONE.equals(statoDomanda));
	}
	
	public boolean userCanAssegnareVerificaDomanda(UserBean userBean, String statoDomanda, UtenteModel teamLeader) {
		if(!DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE.equals(statoDomanda) && !DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE_INSERIMENTO_VERIFICHE.equals(statoDomanda))
			return false;
		if(userBean.isAMMINISTRATORE())
			return true;
		if(!userBean.isVERIFICATORE())
			return false;
		if(teamLeader == null)
			return false;
		return userBean.getUtenteModel() != null && userBean.getUtenteModel().getClientid().equals(teamLeader.getClientid());
	}

	DateFormat dataOraFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
	public String getAssegnatariUdo(UdoUoInstForList udoUoInstForList, UserBean userBean, ShowFolderPageBean pageBean) {
		String toRet = "";
		boolean write = false;
		
		if(udoUoInstForList.isUdo()) {
			if(pageBean.getTipiUdoUtenteTemplPerTipoTitolareDomandaCorrente() != null) {
				for(TipoUdoUtenteTempl tipoUdoUtenteTempl : pageBean.getTipiUdoUtenteTemplPerTipoTitolareDomandaCorrente()) {
					if(udoUoInstForList.getUdoInst().getTipoUdoTempl().getTipoUdo22Templ().getClientid().equals(tipoUdoUtenteTempl.getTipoUdo22Templ().getClientid())) {
						if(write) 
							toRet += ", ";
						toRet += tipoUdoUtenteTempl.getUtenteModel().getAnagraficaUtenteModel().getNome() + " " + tipoUdoUtenteTempl.getUtenteModel().getAnagraficaUtenteModel().getCognome(); 
						write = true;
					}
				}
			}
		}
		if(write)
			toRet = "Assegnatari: " + toRet + ".";
		if(udoUoInstForList.getEsitoOperatore() != null && !udoUoInstForList.getEsitoOperatore().isEmpty()) {
			if(write) 
				toRet += " ";
			toRet += "Operatore: " + udoUoInstForList.getEsitoOperatore();
			if(udoUoInstForList.getAdmin() != null && udoUoInstForList.getAdmin().equals("S"))
				toRet += " - Delega admin";
			if(udoUoInstForList.getEsitoTimeStamp() != null) {
				//th:text="${#dates.format(storyEntry.esitoTimeStamp, 'dd/MM/yyyy - HH:mm')}"
				//Format
				toRet += " (" + dataOraFormat.format(udoUoInstForList.getEsitoTimeStamp()) + ")";
			}
			
		}
		
		return toRet;
	}
	
	/**
	 * Indica se si deve mostrare il form per l'inserimento multiplo degli esiti
	 * @return
	 */
	public boolean showInsertEsitoMultiplo(UserBean userBean, ShowFolderPageBean pageBean) {
		//String userRole = userBean.getRuolo();
		String statoDomanda = pageBean.getFascicolo().getFolderStatus();
		if(DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_INSERIMENTO_ESITI.equals(statoDomanda) || DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE.equals(statoDomanda) || DomandaInstDao.STATO_INSERIMENTO_ESITO_INSERIMENTO_ESITI.equals(statoDomanda) || DomandaInstDao.STATO_INSERIMENTO_ESITO.equals(statoDomanda)) {
			int numAssignedSelected = 0;
			//filtro solo le uoInst e UdoInst selezionate
			for(UdoUoInstForList udoUoInstForList : pageBean.getFascicolo().getUdoUoInstForL()) {
				//if(!udoUoInstForList.isRequisitiGeneraliAziendali() && pageBean.getCheckM().containsKey(udoUoInstForList.getClientId()) && userCanEditEsitoForUdoUoInstForList(udoUoInstForList, userBean, pageBean)) {
				if(udoUoInstForList.isUdo() && pageBean.getCheckM().containsKey(udoUoInstForList.getClientId()) && userCanEditEsitoForUdoUoInstForList(udoUoInstForList, userBean, pageBean)) {
					numAssignedSelected++;
				}
				if(numAssignedSelected > 1)
					return true;
			}
		}
		return false;
	}
	
	public boolean userCanEditEsito(UserBean userBean, ShowFolderPageBean pageBean) {
		if(pageBean.getFilteredUdoUoInstForL() == null || pageBean.getFilteredUdoUoInstForL().size() == 0)
			return false;
		UdoUoInstForList udoUoInstForList = pageBean.getFilteredUdoUoInstForL().get(pageBean.getActiveRowIndex());
		return userCanEditEsitoForUdoUoInstForList(udoUoInstForList, userBean, pageBean);
	}
		
	public boolean userCanEditEsitoForUdoUoInstForList(UdoUoInstForList udoUoInstForList, UserBean userBean, ShowFolderPageBean pageBean) {
		String userRole = userBean.getRuolo();
		String statoDomanda = pageBean.getFascicolo().getFolderStatus();
		//return !udoUoInstForList.isRequisitiGeneraliAziendali()
		return udoUoInstForList.isUdo()
					&& 
				userCanEditEsitoForUdoInst(userBean, udoUoInstForList.getUdoInst(), statoDomanda, pageBean.getTipiUdoUtenteTemplPerTipoTitolareDomandaCorrente());
//				(
//						(
//							(DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE.equals(statoDomanda) || DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_INSERIMENTO_ESITI.equals(statoDomanda)) 
//								&&
//							(
//									(userRole.equals(UserBean.REGIONE_ROLE) 
//											&& 
//											//pageBean.checkRegionUserCanEditEsito(udoUoInstForList, userBean.getUtenteModel()) 
//											AuacUtils.checkRegionUserCanEditEsito(udoUoInstForList.getUdoInst(), userBean.getUtenteModel(), pageBean.getTipiUdoUtenteTemplPerTipoTitolareDomandaCorrente())
//										) 
//										|| 
//									userRole.equals(UserBean.AMMINISTRATORE_ROLE)
//							)
//						)
//							||
//						(
//							(DomandaInstDao.STATO_INSERIMENTO_ESITO.equals(statoDomanda) || DomandaInstDao.STATO_INSERIMENTO_ESITO_INSERIMENTO_ESITI.equals(statoDomanda))
//							&&
//							(
//									userRole.equals(UserBean.REGIONE_ROLE) || userRole.equals(UserBean.AMMINISTRATORE_ROLE)
//							)
//							&& !UDOUO_ESITO_NO.equals(udoUoInstForList.getEsito())
//						)
//				);
		
	}

	private boolean userCanEditEsitoForUdoInst(UserBean userBean, UdoInst udoInst, String statoDomanda, List<TipoUdoUtenteTempl> tipiUdoUtenteTemplPerTipoTitolareDomandaCorrente) {
		String userRole = userBean.getRuolo();		
		return
				(
						(DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE.equals(statoDomanda) || DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_INSERIMENTO_ESITI.equals(statoDomanda)) 
							&&
						(
								(userRole.equals(UserBean.REGIONE_ROLE) 
										&& 
										//pageBean.checkRegionUserCanEditEsito(udoUoInstForList, userBean.getUtenteModel()) 
										AuacUtils.checkRegionUserCanEditEsito(udoInst, userBean.getUtenteModel(), tipiUdoUtenteTemplPerTipoTitolareDomandaCorrente)
									) 
									|| 
								userRole.equals(UserBean.AMMINISTRATORE_ROLE)
						)
					)
						||
					(
						(DomandaInstDao.STATO_INSERIMENTO_ESITO.equals(statoDomanda) || DomandaInstDao.STATO_INSERIMENTO_ESITO_INSERIMENTO_ESITI.equals(statoDomanda))
						&&
						(
								userRole.equals(UserBean.REGIONE_ROLE) || userRole.equals(UserBean.AMMINISTRATORE_ROLE)
						)
						&& !UDOUO_ESITO_NO.equals(udoInst.getEsito())
					);
	}

	public boolean userCanEditEsito(UserBean userBean, ShowUdoInstPageBean pageBean) {
		String userRole = userBean.getRuolo();
		String statoDomanda = pageBean.getFolderStatus();
		
		return userCanEditEsitoForUdoInst(userBean, pageBean.getUdoInst(), statoDomanda, pageBean.getTipiUdoUtenteTemplPerTipoTitolareDomandaCorrente());
	}
	
	public boolean userCanShowEsito(UserBean userBean, ShowFolderPageBean pageBean) {
		if(pageBean.getFilteredUdoUoInstForL() == null || pageBean.getFilteredUdoUoInstForL().size() == 0)
			return false;
		//<div th:if="${theAclService.userCanEditEsito(userBean.ruolo,pageBean.fascicolo.folderStatus)} and not *{filteredUdoUoInstForL[__*{activeRowIndex}__].isRequisitiGeneraliAziendali()}">
		String userRole = userBean.getRuolo();
		String statoDomanda = pageBean.getFascicolo().getFolderStatus();
		
		return !userCanEditEsito(userBean, pageBean) //Se posso modificare e' inutile visualizzare
					&&
				//!pageBean.getFilteredUdoUoInstForL().get(pageBean.getActiveRowIndex()).isRequisitiGeneraliAziendali() //i RequisitiGeneraliAziendali non hanno esito
				pageBean.getFilteredUdoUoInstForL().get(pageBean.getActiveRowIndex()).isUdo() //i RequisitiGeneraliAziendali non hanno esito
					&&
				(userRole.equals(UserBean.REGIONE_ROLE) || userRole.equals(UserBean.AMMINISTRATORE_ROLE)) //ruoli ammessi a vedere gli esiti
					&& 
				(DomandaInstDao.PROCEDIMENTO_CONCLUSO.equals(statoDomanda) || DomandaInstDao.STATO_INSERIMENTO_ESITO.equals(statoDomanda) || DomandaInstDao.STATO_INSERIMENTO_ESITO_INSERIMENTO_ESITI.equals(statoDomanda) || DomandaInstDao.STATO_INSERIMENTO_ESITO_VERIFICA.equals(statoDomanda));
	}

	public boolean userCanShowEsitoDataInizioScadenza(String userRole, ShowFolderPageBean pageBean) {
		if(pageBean.getFilteredUdoUoInstForL() == null || pageBean.getFilteredUdoUoInstForL().size() == 0)
			return false;
		
		return userCanShowEsitoDataInizioScadenza(userRole, pageBean, pageBean.getFilteredUdoUoInstForL().get(pageBean.getActiveRowIndex()));
	}

	public boolean userCanShowEsitoDataInizioScadenza(String userRole, ShowFolderPageBean pageBean, UdoUoInstForList udoUoInstForList) {
		String statoDomanda = pageBean.getFascicolo().getFolderStatus();
		
		//return !udoUoInstForList.isRequisitiGeneraliAziendali() //i RequisitiGeneraliAziendali non hanno esito ne inzio ne scadenza
		return udoUoInstForList.isUdo() //i RequisitiGeneraliAziendali non hanno esito ne inzio ne scadenza
					&&
				(DomandaInstDao.PROCEDIMENTO_CONCLUSO.equals(statoDomanda) || DomandaInstDao.STATO_INSERIMENTO_ESITO.equals(statoDomanda) || DomandaInstDao.STATO_INSERIMENTO_ESITO_INSERIMENTO_ESITI.equals(statoDomanda) || DomandaInstDao.STATO_INSERIMENTO_ESITO_VERIFICA.equals(statoDomanda))
					&&
				!UDOUO_ESITO_NO.equals(udoUoInstForList.getEsito()) 
					&&
				!UDOUO_ESITO_SI.equals(udoUoInstForList.getEsito()) 
					&&
				!UDOUO_ESITO_PARZIALEDAINTEGRARE.equals(udoUoInstForList.getEsito()) 
					&&
				!UDOUO_ESITO_AMMESSACONRISERVA.equals(udoUoInstForList.getEsito()) 
				;
	}

	/*
	 * Restituisce true quando è possibile assegnare l'AttoInst alla UdoInst, ovviamente solo se gli AttoInst sono gestiti e se la domanda è in certi stati
	 * questo controllo non fa check sul'utente si da per scontato che in contemporanea venga anche effettuato il controllo 
	 * (userCanEditEsito(userBean,pageBean) or showInsertEsitoMultiplo(userBean,pageBean)) 
	 */
	public boolean isEditAttoInstInUdoInstActiveForFolder(ShowFolderPageBean showFolderPageBean) {
		String statoDomanda = "";
		if(showFolderPageBean.getFascicolo() != null)
			statoDomanda = showFolderPageBean.getFascicolo().getFolderStatus();
		return
			(
				(DomandaInstDao.STATO_INSERIMENTO_ESITO.equals(statoDomanda) || DomandaInstDao.STATO_INSERIMENTO_ESITO_INSERIMENTO_ESITI.equals(statoDomanda))
				&&
				isGestioneAttoInstInUdoInstActiveForFolder(showFolderPageBean)
			);
	}

	/*
	 * Restituisce true quando è possibile modificare gli AttoInst, ovviamente solo se sono gestiti e se la domanda è in certi stati
	 */
	public boolean isEditAttoInstActiveForFolder(UserBean userBean, ShowFolderPageBean showFolderPageBean) {
		String statoDomanda = "";
		if(showFolderPageBean.getFascicolo() != null)
			statoDomanda = showFolderPageBean.getFascicolo().getFolderStatus();
		return
			(
				userCanModifyDeliberaAttoInst(userBean, showFolderPageBean)
				&&
				isGestioneAttoInstInUdoInstActiveForFolder(showFolderPageBean)
			);
	}

	/*
	 * Controlla dalla versione del workflow se vengono gestite le delibere con gli AttoInst
	 */
	public boolean isGestioneAttoInstInUdoInstActiveForFolder(ShowFolderPageBean showFolderPageBean) {
		return
			(
				showFolderPageBean.getWorkflowProcessInstance() != null && showFolderPageBean.getWorkflowProcessInstance().getProcessMajorVersion() >= 3
			);
	}
	
	public final static String UDOUO_ESITO_NO = "Non ammessa al procedimento";
	public final static String UDOUO_ESITO_SI = "Ammessa al procedimento";
	public final static String UDOUO_ESITO_PARZIALEDAINTEGRARE = "Parziale da integrare";
	public final static String UDOUO_ESITO_AMMESSACONRISERVA = "Ammessa al procedimento con riserva";

	public final static String UDOAMBULATORIALE_ESITO_RILEVATO = "Rilevato";

	public Map<String, String> getEsitiPossibili(UserBean userBean, ShowFolderPageBean pageBean, boolean inserimentoEsitiMultipli) {
		//List<String> toRet = new ArrayList<String>();
		//Map<String, String> toRet = new HashMap<String, String>();
		Map<String, String> toRet = new LinkedHashMap<String, String>();
		
		String statoDomanda = pageBean.getFascicolo().getFolderStatus();
		//*{filteredUdoUoInstForL[__*{activeRowIndex}__].esito}
		String currentValue = pageBean.getFilteredUdoUoInstForL().get(pageBean.getActiveRowIndex()).getEsito();
		
		/*for(Map.Entry<String, String> es : toRet.entrySet()) {
			es.getKey();
			es.getValue();
		}*/
		
		if(DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE.equals(statoDomanda) || DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_INSERIMENTO_ESITI.equals(statoDomanda)) {
			//"", "Si", "No", "Parziale da integrare"
			toRet.put("", "---seleziona il tipo di esito---");
			toRet.put(UDOUO_ESITO_SI, UDOUO_ESITO_SI);
			toRet.put(UDOUO_ESITO_NO, UDOUO_ESITO_NO);
			toRet.put(UDOUO_ESITO_PARZIALEDAINTEGRARE, UDOUO_ESITO_PARZIALEDAINTEGRARE);
			toRet.put(UDOUO_ESITO_AMMESSACONRISERVA, UDOUO_ESITO_AMMESSACONRISERVA);
			if(inserimentoEsitiMultipli) {
				boolean addEsitoAmbulatoriale = true;
				for(UdoUoInstForList udoUoInstForList : pageBean.getFascicolo().getUdoUoInstForL()) {
					if(	pageBean.getCheckM().containsKey(udoUoInstForList.getClientId()) 
							&& userCanEditEsitoForUdoUoInstForList(udoUoInstForList, userBean, pageBean)
							&& (
									udoUoInstForList.isUo() 
									|| 
									!isUdoInstAmbulatoriale(udoUoInstForList.getUdoInst(), pageBean)
								) 
							) {
						addEsitoAmbulatoriale = false;
						break;
					}
				}
				if(addEsitoAmbulatoriale)
					toRet.put(UDOAMBULATORIALE_ESITO_RILEVATO, UDOAMBULATORIALE_ESITO_RILEVATO);				
			} else {
				if(pageBean.getFilteredUdoUoInstForL().get(pageBean.getActiveRowIndex()).isUdo() && isUdoInstAmbulatoriale(pageBean.getFilteredUdoUoInstForL().get(pageBean.getActiveRowIndex()).getUdoInst(), pageBean)) {
					toRet.put(UDOAMBULATORIALE_ESITO_RILEVATO, UDOAMBULATORIALE_ESITO_RILEVATO);				
				}
			}
		} else if(DomandaInstDao.STATO_INSERIMENTO_ESITO.equals(statoDomanda) || DomandaInstDao.STATO_INSERIMENTO_ESITO_INSERIMENTO_ESITI.equals(statoDomanda)) {
			if(!inserimentoEsitiMultipli && UDOUO_ESITO_NO.equals(currentValue)) {
				toRet.put(UDOUO_ESITO_NO, UDOUO_ESITO_NO);
			} else {
				toRet.put("", "---seleziona il tipo di esito---");
				for(EsitoTempl esito : pageBean.getEsitiPossibili()) {
					toRet.put(esito.getEsito(), esito.getEsito());
				}
			}
		}
		return toRet;
	}

	public boolean userCanExecuteTask(HumanTaskInstance task, UserBean userBean, ShowFolderPageBean pageBean) {
		//${task.assigneeId != 0} and
		// se non è stato preso in carico sicuramente non puo' essere eseguito 
		if(task.getAssigneeId() == 0)
			return false;
		//se preso in carico ci sono casi da considerare a parte 
		if (DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONE_AUTOVALUTAZIONI.equals(pageBean.getFascicolo().getFolderStatus())
				||
			DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONE_DOCUMENTI.equals(pageBean.getFascicolo().getFolderStatus())
				||
			DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONE_DOCUMENTI_AUTOVALUTAZIONI.equals(pageBean.getFascicolo().getFolderStatus())
				||
			DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONE_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE.equals(pageBean.getFascicolo().getFolderStatus())
			) {
			//se siamo in richiesta integrazioni si puo' inviare solo se tutte le autovalutazioni e documenti sono stati inseriti
			return pageBean.getFascicolo().isReinviabile();
		} else if (DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE.equals(pageBean.getFascicolo().getFolderStatus())) {
			return pageBean.isEsitiCompleti() && pageBean.isNoteMancanzaTipiUdoControllate();// && pageBean.isEsitiUoCongruentiUdoFiglie();
		} else if (DomandaInstDao.STATO_INSERIMENTO_ESITO.equals(pageBean.getFascicolo().getFolderStatus())) {
			return pageBean.getFascicolo().isConcludibile();
		} else if (DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE.equals(pageBean.getFascicolo().getFolderStatus())) {
			return pageBean.isDomandaVerificata();
		}
		
		return true;
	}
	
	public boolean userCanShowWorkflowDocumentsForTipo(UserBean userBean, String tipo) {
		//se preso in carico ci sono casi da considerare a parte 
		if ("titolare".equals(tipo)) {
			//I documenti allegati al Workflow con “Mostra a Titolare”=true verranno visti dagli utenti con ruolo 
			//Amministratore, Regione, Verificatore, Titolare, Operatore titolare indipendentemente dallo stato della domanda
			return userBean.isREGIONE() || userBean.isOPERATORE_TITOLARE() || userBean.isOPERATORE_TITOLARE_IN_LETTURA() || userBean.isVERIFICATORE() || userBean.isTITOLARE() || userBean.isAMMINISTRATORE();
		} else if ("regione".equals(tipo)) {
			//I documenti allegati al Workflow con “Mostra a Titolare”=false verranno visti solo dagli utenti con ruolo 
			//Amministratore, Regione, Verificatore indipendentemente dallo stato della domanda
			return userBean.isREGIONE() || userBean.isVERIFICATORE() || userBean.isAMMINISTRATORE();
		}
		
		return false;
	}

	public boolean hello() {
		return true;
	}
	
	//controllo su quali tipi udo22 sono stati utilizzati nella domanda
	public boolean userCanCheckTipoUdo22Domanda(String userRole, String statoDomanda) {
		if (statoDomanda == null || statoDomanda.length() == 0)
			statoDomanda = "TITOLARE_FOLDER";
		return checkTipoUdo22DomandaMatrix[indexMap.get(userRole)][indexMap.get(statoDomanda)];
	}	
	
	//controllo su quali tipi udo22 sono stati utilizzati nella domanda
	public boolean userCanCheckPostiLetto(String userRole, String statoDomanda) {
		return true;
		/*if (statoDomanda == null || statoDomanda.length() == 0)
			statoDomanda = "TITOLARE_FOLDER";
		return checkTipoUdo22DomandaMatrix[indexMap.get(userRole)][indexMap.get(statoDomanda)];*/
	}	

	public List<ProcessDeploymentInfo> getWorkflowPossibili(UserBean userBean, ShowFolderPageBean pageBean) {
		List<ProcessDeploymentInfo> toRet = new ArrayList<ProcessDeploymentInfo>();
		
		if(pageBean.getTipoProcTemp() != null && pageBean.getTipoProcTemp().getNomeWf() != null && !pageBean.getTipoProcTemp().getNomeWf().isEmpty()) {
			for(ProcessDeploymentInfo pdi : userBean.getWorkflowBean().getProcessDeploymentInfos()) {
				if(pageBean.getTipoProcTemp().getNomeWf().equalsIgnoreCase(pdi.getName()))
					toRet.add(pdi);
			}
		}
		
		return toRet;
	}

	//controllo per sapere se si deve mostrare l'informazione che la UDoInst e' di tipo ambulatoriale
	public boolean showInfoUdoInstAmbulatoriale(String userRole, String statoDomanda) {
		//if (statoDomanda == null || statoDomanda.length() == 0)
		//	statoDomanda = "TITOLARE_FOLDER";
		return UserBean.REGIONE_ROLE.equals(userRole);
	}	

	public boolean isUdoInstAmbulatoriale(UdoUoInstForList udoUoInstForList, UserBean userBean, ShowFolderPageBean pageBean) {
		//if (statoDomanda == null || statoDomanda.length() == 0)
		//	statoDomanda = "TITOLARE_FOLDER";
		return showInfoUdoInstAmbulatoriale(userBean.getRuolo(), pageBean.getFascicolo().getFolderStatus()) && udoUoInstForList.isUdo() 
				&& isUdoInstAmbulatoriale(udoUoInstForList.getUdoInst(), pageBean);
	}
	
	private boolean isUdoInstAmbulatoriale(UdoInst udoInst, ShowFolderPageBean pageBean) {
		return pageBean.getTipoUdo22TemplClientIdWithAmbitoAmbulatoriale().contains(udoInst.getTipoUdoTempl().getTipoUdo22Templ().getClientid());
	}

	public boolean userCanInsertNoteMancanzaTipiUdo(String userRole, String statoDomanda) {
		if (statoDomanda == null || statoDomanda.length() == 0)
			return false;
		return (DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE.equals(statoDomanda) || DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_INSERIMENTO_ESITI.equals(statoDomanda)) && (UserBean.REGIONE_ROLE.equals(userRole) || UserBean.AMMINISTRATORE_ROLE.equals(userRole));
	}	

	//verifica delle risposte ai requisito
	public boolean userCanVerifyRequisito(String statoDomanda, UserBean userBean, RequisitoInst requisitoInst) {
		if (
				(DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE.equals(statoDomanda) || DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE_INSERIMENTO_VERIFICHE.equals(statoDomanda)) 
				&& 
				(
					(userBean.isVERIFICATORE() && requisitoInst.getVerificatore() != null && requisitoInst.getVerificatore().equals(userBean.getUtenteModel()))
					||
					userBean.isAMMINISTRATORE()
				)
			)
			return true;
		return false;
	}

	//theAclService.userCanEditDomanda(userBean.ruolo,pageBean.domandaStato)
	public boolean userCanEvaluateRequisito(String statoDomanda, UserBean userBean, RequisitoInst requisitoInst) {
		if (userCanEditDomanda(userBean.getRuolo(), statoDomanda) && 
				(
					!userBean.isVALUTATORE_INTERNO()
					||
					requisitoInst.getUtenteModel() != null && requisitoInst.getUtenteModel().equals(userBean.getUtenteModel())
				)
			)
			return true;
		return false;
	}

	//verifica se l'UserBean passato puo' modificare uno dei requisiti dell'autovalutazione passata
	public boolean userCanSaveVerifiche(UserBean userBean, AutovalutazionePageBean autovalutazionePageBean) {
		if (!DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE.equals(autovalutazionePageBean.getDomandaStato()) && !DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE_INSERIMENTO_VERIFICHE.equals(autovalutazionePageBean.getDomandaStato()))
			return false;
		if(userBean.isAMMINISTRATORE())
			return true;
		if(!userBean.isVERIFICATORE())
			return false;

		for(RequisitoInst requisitoInst : autovalutazionePageBean.getRequisitoInstL()) {
			if(requisitoInst.getVerificatore() != null && requisitoInst.getVerificatore().equals(userBean.getUtenteModel()))
				return true;
		}
		
		return false;
	}

	public boolean userCanShowStatoVerifica(String statoDomanda, UserBean userBean) {
		if (DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE.equals(statoDomanda) || DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE_INSERIMENTO_VERIFICHE.equals(statoDomanda) || DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE_VERIFICA.equals(statoDomanda)
				/*&& 
				(
					userBean.isVERIFICATORE()
					||
					userBean.isAMMINISTRATORE()
				)*/
			)
			return true;
		return false;
	}

	public boolean userCanExportVerifica(String statoDomanda, UserBean userBean) {
		if ((DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE.equals(statoDomanda) || DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE_INSERIMENTO_VERIFICHE.equals(statoDomanda) || !DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE_VERIFICA.equals(statoDomanda))
				&& 
				(
					userBean.isREGIONE()
					||
					userBean.isVERIFICATORE()
					||
					userBean.isAMMINISTRATORE()
				)
			)
			return true;
		return false;
	}

	public boolean userCanFilterByUdoUoRgaConRequisitiAssegnatiInVerifica(String statoDomanda, UserBean userBean) {
		if ((DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE.equals(statoDomanda) || DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE_INSERIMENTO_VERIFICHE.equals(statoDomanda))
				&& userBean.isVERIFICATORE()
			)
			return true;
		return false;
	}

	public boolean userCanEditNoteVerifica(UserBean userBean, String statoDomanda, UtenteModel teamLeader) {
		if(!DomandaInstDao.STATO_REDAZIONE_RAPPORTO_DI_VERIFICA.equals(statoDomanda))
			return false;
		if(userBean.isAMMINISTRATORE())
			return true;
		if(!userBean.isVERIFICATORE())
			return false;
		if(teamLeader == null)
			return false;
		return userBean.getUtenteModel() != null && userBean.getUtenteModel().getClientid().equals(teamLeader.getClientid());
	}

	//Creazione Rapporto di Verifica in pdf
	public boolean userCanCreateRapportoVerifica(UserBean userBean, String statoDomanda, UtenteModel teamLeader) {
		if(!DomandaInstDao.STATO_REDAZIONE_RAPPORTO_DI_VERIFICA.equals(statoDomanda))
			return false;
		if(userBean.isAMMINISTRATORE())
			return true;
		if(!userBean.isVERIFICATORE())
			return false;
		if(teamLeader == null)
			return false;
		return userBean.getUtenteModel() != null && userBean.getUtenteModel().getClientid().equals(teamLeader.getClientid());
	}

	public boolean userCanGetCsvCompareUdo(UserBean userBean, ShowFolderPageBean pageBean) {
		if (DomandaInstDao.PROCEDIMENTO_CONCLUSO.equals(pageBean.getFascicolo().getFolderStatus())) {
			//se siamo in PROCEDIMENTO_CONCLUSO disabilito
			return false;
		}
		return true;
	}

	public boolean userCanGetCsvRelazioneConclusiva(UserBean userBean, ShowFolderPageBean pageBean) {
		if (
			(
				DomandaInstDao.STATO_PROCEDIMENTO_IN_VALUTAZIONE.equals(pageBean.getFascicolo().getFolderStatus())
				||
				DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE.equals(pageBean.getFascicolo().getFolderStatus())
				||
				DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_INSERIMENTO_ESITI.equals(pageBean.getFascicolo().getFolderStatus())
				||
				DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_VALUTAZIONE.equals(pageBean.getFascicolo().getFolderStatus())
				||
				DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_COMPLETATA.equals(pageBean.getFascicolo().getFolderStatus())
				||
				DomandaInstDao.STATO_ISTRUTTORIA_COMPLETATA.equals(pageBean.getFascicolo().getFolderStatus())
			)
				&& (userBean.isREGIONE() || userBean.isAMMINISTRATORE())) {
			return true;
		}
		return false;
	}

	public boolean userCanGetTxtRelazioneConclusiva(UserBean userBean, ShowFolderPageBean pageBean) {
		if (
			(
				DomandaInstDao.STATO_PROCEDIMENTO_IN_VALUTAZIONE.equals(pageBean.getFascicolo().getFolderStatus())
				||
				DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE.equals(pageBean.getFascicolo().getFolderStatus())
				||
				DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_INSERIMENTO_ESITI.equals(pageBean.getFascicolo().getFolderStatus())
				||
				DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_VALUTAZIONE.equals(pageBean.getFascicolo().getFolderStatus())
				||
				DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_COMPLETATA.equals(pageBean.getFascicolo().getFolderStatus())
				||
				DomandaInstDao.STATO_ISTRUTTORIA_COMPLETATA.equals(pageBean.getFascicolo().getFolderStatus())
			)
				&& (userBean.isREGIONE() || userBean.isAMMINISTRATORE())) {
			return true;
		}
		return false;
	}
	
	/*
	 * Indica se l'utente corrente può modificare la Delibera o AttoInst a seconda della gestione
	 */
	public boolean userCanModifyDeliberaAttoInst(UserBean userBean, ShowFolderPageBean pageBean) {

		//18/09/2018
		//modificato perchè basandosi sul nome del ufficio che può cambiare non va
		//Ora lo possono fare tutti gli utenti regionali che vedono la domanda
//		boolean isResponsibiliAcreditamento = false;
//		
//		List<UtenteDirezioneModel> utenteDirezioneModels = userBean.getUtenteModel().getUtenteDirezioneModels();
//		for(UtenteDirezioneModel utenteDirezioneModel : utenteDirezioneModels) {
//			List<UfficioTempl> ufficioTempls = utenteDirezioneModel.getUfficioTempls();
//			for(UfficioTempl ufficioTempl : ufficioTempls) {
//				if(ufficioTempl.getNome().trim().equals("Responsabili Accreditamento")) {
//					isResponsibiliAcreditamento = true;
//					break;
//				}
//			}
//		}
//		
//		if(isResponsibiliAcreditamento && !DomandaInstDao.PROCEDIMENTO_CONCLUSO.equals(pageBean.getFascicolo().getFolderStatus()))
		
		//17/09/2019 abarducci troppi stati gestiti li riduco
		String statoDomanda = pageBean.getFascicolo().getFolderStatus();
		if(userBean.isREGIONE() && pageBean.getFascicolo() != null 
				&& 
				(
						DomandaInstDao.STATO_INSERIMENTO_ESITO.equals(statoDomanda) 
						|| DomandaInstDao.STATO_INSERIMENTO_ESITO_INSERIMENTO_ESITI.equals(statoDomanda) 
						|| DomandaInstDao.STATO_INSERIMENTO_ESITO_VERIFICA.equals(statoDomanda)
				)
			)
			return true;
		
		return false;
	}

	//Indica se l'utente può modificare il l'oggetto domanda (Tipo domanda) per la nuova gestione dei RequisitiDiversificatiPerTipoDomanda
	public boolean userCanEditOggettoDomanda(UserBean userBean, ShowFolderPageBean pageBean) {
		//th:if="${theAclService.userCanEditOggettoDomanda(userBean, pageBean)}"
		//folderPageBean.getFascicolo().getOggettoDomanda()
		String statoDomanda = pageBean.getFascicolo().getFolderStatus();
		
		return (
					DomandaInstDao.STATO_BOZZA.equals(statoDomanda) 
					|| DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONE_AUTOVALUTAZIONI.equals(statoDomanda) 
					|| DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONE_DOCUMENTI.equals(statoDomanda) 
					|| DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONE_DOCUMENTI_AUTOVALUTAZIONI.equals(statoDomanda) 
					|| DomandaInstDao.STATO_RICHIESTA_INTEGRAZIONE_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE.equals(statoDomanda)
				)
					&&
				(
					userBean.isREGIONE() || userBean.isTITOLARE() || userBean.isOPERATORE_TITOLARE() || userBean.isAMMINISTRATORE()
				)
				;
	}
}
