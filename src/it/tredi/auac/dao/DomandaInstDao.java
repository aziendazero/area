package it.tredi.auac.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityGraph;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import it.tredi.auac.orm.entity.AnagraficaUtenteModel;
import it.tredi.auac.orm.entity.DomandaInst;
import it.tredi.auac.orm.entity.UtenteModel;

@Repository("domandaInstDao")
public class DomandaInstDao extends AbstractJpaDao<DomandaInst> {
	private static final Logger log = Logger.getLogger(DomandaInstDao.class);	

	public final static String STATO_BOZZA = "BOZZA";
	public final static String STATO_AVVIATO = "PROCEDIMENTO AVVIATO";
	//STATO QUANDO VIENE AVVIATO IL FLUSSO
	public final static String STATO_FASE_ISTRUTTORIA = "FASE ISTRUTTORIA";//EX STATO_PROCEDIMENTO_IN_VALUTAZIONE="PROCEDIMENTO IN VALUTAZIONE"
	//public final static String STATO_RICHIESTA_INTEGRAZIONI = "RICHIESTA DI INTEGRAZIONI";
	public final static String STATO_RICHIESTA_INTEGRAZIONE_DOCUMENTI = "RICHIESTA DI INTEGRAZIONE DOCUMENTI";
	public final static String STATO_RICHIESTA_INTEGRAZIONE_AUTOVALUTAZIONI = "RICHIESTA DI INTEGRAZIONE AUTOVALUTAZIONI";
	public final static String STATO_RICHIESTA_INTEGRAZIONE_DOCUMENTI_AUTOVALUTAZIONI = "RICHIESTA DI INTEGRAZIONE DOCUMENTI E AUTOVALUTAZIONI";

	public final static String STATO_INVIATA = "INVIATA";

	public final static String STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE = "VALUTAZIONE RISPONDENZA PROGRAMMAZIONE";
	//20160413 questo stato viene diviso in 2 stati 
	//prima i membri del "Servizio Accreditamento" avevano il task "Valutazione Congruenza Programmazione" assegnato ma non potevano eseguirlo finche' non veniva assegnato l'esito a tutte le udo
	//ora il task "Valutazione Congruenza Programmazione" viene raggiunto solo dopo che tutti gli esiti sono stati inseriti/modificati se "Parziali da integrare" e le note mancanza tipi udo sono state validate
	public final static String STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_INSERIMENTO_ESITI = "VALUTAZIONE RISPONDENZA PROGRAMMAZIONE INSERIMENTO ESITI";
	public final static String STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_VALUTAZIONE = "VALUTAZIONE RISPONDENZA PROGRAMMAZIONE VALUTAZIONE";
	
	public final static String STATO_RICHIESTA_INTEGRAZIONE_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE = "RICHIESTA DI INTEGRAZIONI VALUTAZIONE RISPONDENZA PROGRAMMAZIONE";

	//Questo stato non si verifica piu'
	public final static String STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_COMPLETATA = "VALUTAZIONE RISPONDENZA PROGRAMMAZIONE COMPLETATA";

	public final static String STATO_ISTRUTTORIA_COMPLETATA = "ISTRUTTORIA COMPLETATA";
	public final static String STATO_PROCEDIMENTO_IN_VALUTAZIONE = "PROCEDIMENTO IN VALUTAZIONE";

	public final static String STATO_GESTIONE_DELLE_VERIFICHE = "GESTIONE DELLE VERIFICHE";
	//20160419 questo stato viene diviso in 2 stati 
	//prima il Team Leader aveva il task "Effettuazione Verifica" assegnato ma non poteva eseguirlo finche' non venivano inserite tutte le valutazioni
	//ora il task "Effettuazione Verifica" viene raggiunto solo dopo che tutte le valutazioni sono state inserite
	public final static String STATO_GESTIONE_DELLE_VERIFICHE_INSERIMENTO_VERIFICHE = "GESTIONE DELLE VERIFICHE INSERIMENTO VERIFICHE";
	public final static String STATO_GESTIONE_DELLE_VERIFICHE_VERIFICA= "GESTIONE DELLE VERIFICHE VERIFICA";

	public final static String STATO_REDAZIONE_RAPPORTO_DI_VERIFICA = "REDAZIONE RAPPORTO DI VERIFICA";

	public final static String STATO_INSERIMENTO_ESITO = "INSERIMENTO ESITO";
	//20160419 questo stato viene diviso in 2 stati 
	//prima i membri del "Servizio Accreditamento" avevano il task "Inserimento Esito del Procedimento" assegnato 
	//ma non potevano eseguirlo finche' non veniva assegnato l'esito a tutte le udo attivita' gestita da qualsiasi utente con ruolo REGIONE_ROLE
	//ora il task "Inserimento Esito del Procedimento" viene raggiunto solo dopo che tutti gli esiti sono stati inseriti
	public final static String STATO_INSERIMENTO_ESITO_INSERIMENTO_ESITI = "INSERIMENTO ESITO INSERIMENTO ESITI";
	public final static String STATO_INSERIMENTO_ESITO_VERIFICA= "INSERIMENTO ESITO VERIFICA";

	public final static String PROCEDIMENTO_CONCLUSO = "PROCEDIMENTO CONCLUSO";
	
	//Usato sul FASCICOLO la DomandaInst VIENE CANCELLATA
	public final static String ELIMINATO = "ELIMINATO";
	
	public DomandaInstDao() {
		super();
		setClazz(DomandaInst.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<DomandaInst> findByAnagraficaUtenteModel(AnagraficaUtenteModel anagrafica) {
		Query q = entityManager.createQuery("select d from DomandaInst d where d.anagraficaUtenteModel = :anagraficaUtenteModel");
		q.setParameter("anagraficaUtenteModel", anagrafica);
	    return (List<DomandaInst>)q.getResultList();
	}
	
	public DomandaInst getDomandaInstByClientId(String clientId){
		Query q = entityManager.createQuery("select d from DomandaInst d where d.clientid = :clientid");
		q.setParameter("clientid", clientId);
	    return (DomandaInst)q.getSingleResult();
	}
	
	/*public DomandaInst getDomandaInstByClientIdForShowFolder(String clientId){
		Query q = entityManager.createQuery("select d from DomandaInst d where d.clientid = :clientid");
		q.setParameter("clientid", clientId);
		//q.setHint("eclipselink.batch", "d.uoInsts");
		//q.setHint("eclipselink.batch", "d.udoInsts");
	    return (DomandaInst)q.getSingleResult();
	}*/
	
	public DomandaInst getDomandaInstByClientIdForShowFolderEntityGraph(String clientId){
    	long startTime = System.currentTimeMillis();

		EntityGraph<?> domandaInstShowFolderGraph = entityManager.getEntityGraph("DomandaInst.showFolder");
		Query q = entityManager.createQuery("select d from DomandaInst d where d.clientid = :clientid");
		q.setParameter("clientid", clientId);
		q.setHint("javax.persistence.fetchgraph", domandaInstShowFolderGraph);

		
		q.setHint("eclipselink.batch.type", "JOIN");//default
		//q.setHint("eclipselink.batch.type", "EXISTS");
		//q.setHint("eclipselink.batch.type", "IN");//ERRORE
		
		//q.setHint("eclipselink.join-fetch", "d.titolareModel");
		//q.setHint("eclipselink.join-fetch", "d.tipoProcTempl");
		
		//q.setHint("eclipselink.left-join-fetch", "d.uoInsts");//@OneToMany
		//Occorre impostare questo perche' jpa segue l'albero per un solo livello 
		//quindi caricando la domanda carica la lista uoInst (primo livello) e li si ferma caricando d.uoInsts.uoModel con query separate 
		//q.setHint("eclipselink.left-join-fetch", "d.uoInsts.uoModel");//@ManyToOne, @JoinFetch(value=JoinFetchType.INNER)

		//q.setHint("eclipselink.batch", "d.udoInsts");//@OneToMany
		//q.setHint("eclipselink.left-join-fetch", "d.udoInsts.disciplinaTempls");//@ManyToMany
		//q.setHint("eclipselink.left-join-fetch", "d.udoInsts.brancaTempls");//@ManyToOne
		
		//Questi 2 vengono caricati insieme alla lista udoInsts per come sono settati sull'entity e in quanto inclusi nel EntityGraph
		//q.setHint("eclipselink.left-join-fetch", "d.udoInsts.udoModel");//@ManyToOne, @JoinFetch(value=JoinFetchType.INNER)
		q.setHint("eclipselink.left-join-fetch", "d.udoInsts.tipoUdoTempl");//@ManyToOne, @JoinFetch(value=JoinFetchType.INNER)
		//q.setHint("eclipselink.left-join-fetch", "d.udoInsts.udoModel.uoModel");//@ManyToOne, @JoinFetch(value=JoinFetchType.INNER)
		
		//L'ideale sarebbe che venisse caricato anche questo al caricamento della lista udoInsts
		q.setHint("eclipselink.left-join-fetch", "d.udoInsts.tipoUdoTempl.tipoUdo22Templ");//@ManyToOne, @JoinFetch(value=JoinFetchType.INNER)

	    DomandaInst dom = (DomandaInst)q.getSingleResult();
	    
    	//PersistenceUnitUtil unitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
		//System.out.println("unitUtil.isLoaded(dom, \"uoInsts\"): " + unitUtil.isLoaded(dom, "uoInsts"));
		//System.out.println("unitUtil.isLoaded(dom, \"udoInsts\"): " + unitUtil.isLoaded(dom, "udoInsts"));
	    
	    //Carico i dati per controllare se sono richiesti dei TipiUdo per la tipologia del titolare della domanda corrente 
	    dom.getTitolareModel().getTipoTitolareTempl().getTipoUdo22TemplRichiesti().size();
	    
		log.info("getDomandaInstByClientIdForShowFolderEntityGraph - clientId: " + clientId + " tempo: " + (System.currentTimeMillis() - startTime) + " ms");
	    return dom;
	    //return (DomandaInst)q.getSingleResult();
	}
	
	public DomandaInst getDomandaInstByClientIdForAddWorkflow(String clientId){
		EntityGraph<?> domandaInstShowFolderGraph = entityManager.getEntityGraph("DomandaInst.addWorkflow");
		Query q = entityManager.createQuery("select d from DomandaInst d where d.clientid = :clientid");
		q.setParameter("clientid", clientId);
		q.setHint("javax.persistence.fetchgraph", domandaInstShowFolderGraph);

		
		q.setHint("eclipselink.batch.type", "JOIN");//default
		//q.setHint("eclipselink.batch.type", "EXISTS");
		//q.setHint("eclipselink.batch.type", "IN");//ERRORE
		
		//q.setHint("eclipselink.join-fetch", "d.titolareModel");
		//q.setHint("eclipselink.join-fetch", "d.tipoProcTempl");
		
		//q.setHint("eclipselink.left-join-fetch", "d.uoInsts");//@OneToMany
		//Occorre impostare questo perche' jpa segue l'albero per un solo livello 
		//quindi caricando la domanda carica la lista uoInst (primo livello) e li si ferma caricando d.uoInsts.uoModel con query separate 
		//q.setHint("eclipselink.left-join-fetch", "d.uoInsts.uoModel");//@ManyToOne, @JoinFetch(value=JoinFetchType.INNER)

		//q.setHint("eclipselink.batch", "d.udoInsts");//@OneToMany
		//q.setHint("eclipselink.left-join-fetch", "d.udoInsts.disciplinaTempls");//@ManyToMany
		//q.setHint("eclipselink.left-join-fetch", "d.udoInsts.brancaTempls");//@ManyToOne
		
		//Questi 2 vengono caricati insieme alla lista udoInsts per come sono settati sull'entity e in quanto inclusi nel EntityGraph
		//q.setHint("eclipselink.left-join-fetch", "d.udoInsts.udoModel");//@ManyToOne, @JoinFetch(value=JoinFetchType.INNER)
		//q.setHint("eclipselink.left-join-fetch", "d.udoInsts.tipoUdoTempl");//@ManyToOne, @JoinFetch(value=JoinFetchType.INNER)
		//q.setHint("eclipselink.left-join-fetch", "d.udoInsts.udoModel.uoModel");//@ManyToOne, @JoinFetch(value=JoinFetchType.INNER)
		
		//L'ideale sarebbe che venisse caricato anche questo al caricamento della lista udoInsts
		//q.setHint("eclipselink.left-join-fetch", "d.udoInsts.tipoUdoTempl.tipoUdo22Templ");//@ManyToOne, @JoinFetch(value=JoinFetchType.INNER)

		q.setHint("eclipselink.left-join-fetch", "d.udoInsts.tipoUdoTempl.tipoUdo22Templ.classificazioneUdoTempl");//@ManyToOne, @JoinFetch(value=JoinFetchType.INNER)

		DomandaInst dom = (DomandaInst)q.getSingleResult();
	    
    	//PersistenceUnitUtil unitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
		//System.out.println("unitUtil.isLoaded(dom, \"uoInsts\"): " + unitUtil.isLoaded(dom, "uoInsts"));
		//System.out.println("unitUtil.isLoaded(dom, \"udoInsts\"): " + unitUtil.isLoaded(dom, "udoInsts"));
	    
	    return dom;
	    //return (DomandaInst)q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public Map<String, Integer> getDomandeClientIdConRequisitiAssegnatiAdUtenteENumeroRisposteMancanti(UtenteModel utenteModel){
//		Query q = entityManager.createNativeQuery("SELECT iddomanda, sum(ancoradarisondere) requisitisenzarisposta" + 
//				" FROM" + 
//				" (SELECT CASE WHEN tipo_risposta.nome IS NULL THEN 1 WHEN tipo_risposta.nome='Titolo' THEN 0 WHEN requisito_inst.valutazione IS NULL or length(requisito_inst.valutazione)=0 THEN 1 ELSE 0 END ancoradarisondere" + 
//				" , concat(requisito_inst.id_domanda_fk, concat(udo_inst.id_domanda_fk, uo_inst.id_domanda_fk)) iddomanda" + 
//				" FROM requisito_inst " + 
//				" left join udo_inst on udo_inst.clientid=requisito_inst.id_udo_fk" + 
//				" left join uo_inst on uo_inst.clientid=requisito_inst.id_uo_fk" + 
//
//				" left join requisito_templ on requisito_templ.clientid=requisito_inst.id_req_templ_fk" +
//				" left join tipo_risposta on tipo_risposta.clientid=requisito_templ.id_tipo_risposta_fk" +
//				
//				" where id_assegnatario_fk = ? )" + 
//				" GROUP BY iddomanda"); 
		//Modificato il 09/10/2017 per sfruttare il nuovo campo root_id_domanda_fk evitando tutti i join
		Query q = entityManager.createNativeQuery("SELECT iddomanda, sum(ancoradarisondere) requisitisenzarisposta" + 
				" FROM" + 
				" (SELECT CASE WHEN tipo_risposta.nome IS NULL THEN 1 WHEN tipo_risposta.nome='Titolo' THEN 0 WHEN requisito_inst.valutazione IS NULL or length(requisito_inst.valutazione)=0 THEN 1 ELSE 0 END ancoradarisondere" + 
				" , root_id_domanda_fk iddomanda" + 
				" FROM requisito_inst " + 
				" left join requisito_templ on requisito_templ.clientid=requisito_inst.id_req_templ_fk" +
				" left join tipo_risposta on tipo_risposta.clientid=requisito_templ.id_tipo_risposta_fk" +
				
				" where id_assegnatario_fk = ? )" + 
				" GROUP BY iddomanda"); 
	    q.setParameter(1, utenteModel.getClientid());

		List<Object[]> results = q.getResultList();
	    Map<String, Integer> toRet = new HashMap<String, Integer>();
		for(Object[] obj : results)
			toRet.put((String)obj[0], ((BigDecimal)obj[1]).intValueExact());
		
	    return toRet;
	}

	@SuppressWarnings("unchecked")
	public List<String> getDomandeClientIdPerVerificatore(UtenteModel utenteModel){
		Query q = entityManager.createNativeQuery("SELECT DISTINCT DI.CLIENTID" + 
											" FROM DOMANDA_INST DI" +
											" INNER JOIN BIND_DOMANDA_VERIFICATORE BDV ON BDV.ID_DOMANDA_INST_FK=DI.CLIENTID" +
											//Modifica del 22/01/2016 in cui viene aggiunto un nuovo stato STATO_GESTIONE_DELLE_VERIFICHE durante il quale vengono effettuate le verifiche
											//Modifica del 21/04/2016 in cui lo stato STATO_GESTIONE_DELLE_VERIFICHE e' stato sdoppiato in due STATO_GESTIONE_DELLE_VERIFICHE_INSERIMENTO_VERIFICHE e STATO_GESTIONE_DELLE_VERIFICHE_VERIFICA
											" WHERE BDV.ID_UTENTE_MODEL_FK=? AND DI.STATO IN ('" + DomandaInstDao.STATO_PROCEDIMENTO_IN_VALUTAZIONE + "', '" + DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE + "', '" + DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE_INSERIMENTO_VERIFICHE + "', '" + DomandaInstDao.STATO_GESTIONE_DELLE_VERIFICHE_VERIFICA + "', '" + DomandaInstDao.STATO_REDAZIONE_RAPPORTO_DI_VERIFICA + "')");
	    q.setParameter(1, utenteModel.getClientid());
	    List<String> values = (List<String>)q.getResultList();
	    return values;
	}

	@SuppressWarnings("unchecked")
	public List<String> getDomandeClientIdPerTitolariDiDirezioneTempl(String direzioneTemplClientid){
		//SELECT DISTINCT CLIENTID FROM DOMANDA_INST WHERE ID_TITOLARE_FK IN (SELECT CLIENTID FROM TITOLARE_MODEL WHERE ID_DIREZIONE_FK='51E69106-9526-6877-0856-2F2D89D5173E');
		Query q = entityManager.createNativeQuery("SELECT DISTINCT CLIENTID" + 
											" FROM DOMANDA_INST" +
											" WHERE ID_TITOLARE_FK IN (SELECT CLIENTID FROM TITOLARE_MODEL WHERE ID_DIREZIONE_FK=?)");
											//" WHERE BDV.ID_UTENTE_MODEL_FK=? ");
	    q.setParameter(1, direzioneTemplClientid);
	    List<String> values = (List<String>)q.getResultList();
	    return values;
	}

	@SuppressWarnings("unchecked")
	public List<String> getDomandeClientIdPerUfficioDiAppartenenza(String utenteClientid){
		Query q = entityManager.createNativeQuery("SELECT DISTINCT DI.CLIENTID " + 
				"FROM DOMANDA_INST DI " + 
				"INNER JOIN TITOLARE_MODEL TM ON TM.CLIENTID=DI.ID_TITOLARE_FK " + 
				"INNER JOIN BIND_UFF_TIPPROC_VISIB_DOM BUTD " + 
				"  ON BUTD.ID_TIPO_PROC_FK=DI.ID_TIPO_PROC_FK " + 
				"INNER JOIN BIND_UTENTE_DIR_UFF BUDU " + 
				"  ON BUDU.ID_UFFICIO_TEMPL=BUTD.ID_UFFICIO_FK " + 
				"INNER JOIN UTENTE_DIREZIONE_MODEL UDM" + 
				"  ON UDM.CLIENTID=BUDU.ID_UTENTE_DIREZIONE_MODEL AND UDM.ID_DIREZIONE_TEMPL=TM.ID_DIREZIONE_FK " + 
				"WHERE UDM.ID_UTENTE_MODEL=?"
				);
	    q.setParameter(1, utenteClientid);
	    List<String> values = (List<String>)q.getResultList();
	    return values;
	}

	@SuppressWarnings("unchecked")
	public List<String> getDomandeClientIdAssignedCongruenzaEsito(UtenteModel utenteModel){
		Query q = entityManager.createNativeQuery("SELECT DISTINCT DI.CLIENTID" + 
											" FROM UDO_INST UI" +
											" INNER JOIN TIPO_UDO_TEMPL TUT ON TUT.CLIENTID=UI.ID_TIPO_UDO_FK" +
											" INNER JOIN DOMANDA_INST DI ON DI.CLIENTID=UI.ID_DOMANDA_FK" +
											" INNER JOIN TITOLARE_MODEL TM ON TM.CLIENTID=DI.ID_TITOLARE_FK" +
											" INNER JOIN TIPO_UDO_UTENTE_TEMPL TUUT ON TUUT.ID_TIPO_UDO_22_TEMPL_FK=TUT.ID_TIPO_UDO_22_FK AND TUUT.ID_TIPO_TITOLARE_FK=TM.ID_TIPO_FK" +
											" WHERE TUUT.ID_UTENTE_MODEL_FK=? AND DI.STATO IN ('" + DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE + "', '" + DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_INSERIMENTO_ESITI + "')");
											//" WHERE BDV.ID_UTENTE_MODEL_FK=? ");
	    q.setParameter(1, utenteModel.getClientid());
	    List<String> values = (List<String>)q.getResultList();
	    return values;
	}

	@SuppressWarnings("unchecked")
	public List<String> getDomandeClientIdCongruenzaSenzaEsito(){

		Query q = entityManager.createNativeQuery("SELECT DISTINCT DI.CLIENTID" + 
											" FROM UDO_INST UI" +
											" INNER JOIN DOMANDA_INST DI ON DI.CLIENTID=UI.ID_DOMANDA_FK" +
											" WHERE DI.STATO IN " +
												"('" + DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE + "', '" 
													 + DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_INSERIMENTO_ESITI + "', '" 
												     + DomandaInstDao.STATO_VALUTAZIONE_RISPONDENZA_PROGRAMMAZIONE_VALUTAZIONE + "') " +
											" AND UI.ESITO IS NULL");
	    List<String> values = (List<String>)q.getResultList();
	    return values;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getDomandeClientIdConRequisitiAssegnatiAdUtente(UtenteModel utenteModel){
//		Query q = entityManager.createNativeQuery("select distinct concat(requisito_inst.id_domanda_fk, concat(udo_inst.id_domanda_fk, uo_inst.id_domanda_fk))" + 
//				" from requisito_inst" +
//				" left join udo_inst on udo_inst.clientid=requisito_inst.id_udo_fk" +
//				" left join uo_inst on uo_inst.clientid=requisito_inst.id_uo_fk" +
//				" where id_assegnatario_fk = ? ");
		//Modificato il 09/10/2017 per sfruttare il nuovo campo root_id_domanda_fk evitando tutti i join
		Query q = entityManager.createNativeQuery("select distinct root_id_domanda_fk" + 
	    				" from requisito_inst" +
	    				" where id_assegnatario_fk = ? ");
	    q.setParameter(1, utenteModel.getClientid());
	    List<String> values = (List<String>)q.getResultList();
	    return values;
	}

	@SuppressWarnings("unchecked")
	public List<String> getEntityInstClientIdConRequisitiAssegnatiAdUtente(UtenteModel utenteModel, DomandaInst domandaInst){
//		Query q = entityManager.createNativeQuery("select distinct concat(requisito_inst.id_edificio_fk, concat(requisito_inst.id_struttura_fk, concat(requisito_inst.id_domanda_fk, concat(udo_inst.clientid, uo_inst.clientid))))" + 
//				" from requisito_inst" +
//				" left join udo_inst on udo_inst.clientid=requisito_inst.id_udo_fk" +
//				" left join uo_inst on uo_inst.clientid=requisito_inst.id_uo_fk" +
//				" left join struttura_inst on struttura_inst.clientid=requisito_inst.id_struttura_fk" +
//				" left join edificio_inst on edificio_inst.clientid=requisito_inst.id_edificio_fk" +
//				" where id_assegnatario_fk = ? and (requisito_inst.id_domanda_fk = ? or uo_inst.id_domanda_fk = ? or udo_inst.id_domanda_fk = ? or struttura_inst.id_domanda_fk = ? or edificio_inst.id_domanda_fk = ?)");
//		q.setParameter(1, utenteModel.getClientid());
//		q.setParameter(2, domandaInst.getClientid());
//		q.setParameter(3, domandaInst.getClientid());
//		q.setParameter(4, domandaInst.getClientid());
//		q.setParameter(5, domandaInst.getClientid());
//		q.setParameter(6, domandaInst.getClientid());
		//Modificato il 09/10/2017 per sfruttare il nuovo campo root_id_domanda_fk evitando tutti i join
		Query q = entityManager.createNativeQuery("select distinct concat(id_edificio_fk, concat(id_struttura_fk, concat(id_domanda_fk, concat(id_udo_fk, id_uo_fk))))" + 
				" from requisito_inst" +
				" where id_assegnatario_fk = ? and root_id_domanda_fk = ?");
		q.setParameter(1, utenteModel.getClientid());
		q.setParameter(2, domandaInst.getClientid());
		List<String> values = (List<String>)q.getResultList();
		return values;
	}
	
	/**
	 * Controlla che non esistano altre domande non concluse con stesse Uo o Udo
	 * @param domandaClientid
	 * @return ritorna true se non vengono trovate altre domande con stesse Uo o Udo non concluse
	 */
	public boolean checkAltreDomandeNonConcluseConStesseUoUdo(String domandaClientid) {
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("NUM_DOM_NO_CONCL_STESSE_UO_UDO");
		// set parameters
		storedProcedure.registerStoredProcedureParameter("DOMANDACLIENTID", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("NUMDOMANDE", Integer.class, ParameterMode.OUT);
		storedProcedure.setParameter("DOMANDACLIENTID", domandaClientid);
		// execute SP
		storedProcedure.execute();
		// get result
		Integer numDomandeTrovate = (Integer)storedProcedure.getOutputParameterValue("NUMDOMANDE");
		if(numDomandeTrovate > 0)
			return false;
		return true;
	}
	
	public void runAggProcInCorsoUdoUoTit(String titolareClientid) {
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("AGG_PROC_IN_CORSO_UDO_UO_TIT");
		// set parameters
		storedProcedure.registerStoredProcedureParameter("TITOLARECLIENTID", String.class, ParameterMode.IN);
		storedProcedure.setParameter("TITOLARECLIENTID", titolareClientid);
		// execute SP
		storedProcedure.execute();
	}

	/**
	 * Controlla che non esistano altre domande non concluse con stesse Udo
	 * @param domandaClientid
	 * @return ritorna true se non vengono trovate altre domande con stesse Udo non concluse
	 */
	public boolean checkAltreDomandeNonConcluseConStesseUdo(String domandaClientid) {
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("NUM_DOM_NO_CONCL_STESSE_UDO");
		// set parameters
		storedProcedure.registerStoredProcedureParameter("DOMANDACLIENTID", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("NUMDOMANDE", Integer.class, ParameterMode.OUT);
		storedProcedure.setParameter("DOMANDACLIENTID", domandaClientid);
		// execute SP
		storedProcedure.execute();
		// get result
		Integer numDomandeTrovate = (Integer)storedProcedure.getOutputParameterValue("NUMDOMANDE");
		if(numDomandeTrovate > 0)
			return false;
		return true;
	}
	
	/**
	 * Controlla che nella domanda di cui viene passato il clientid non ci siano delle Udo senza posti letto ma il cui tipoUdo22Templ lo richieda
	 * @param domandaClientid
	 * @return ritorna true se il controllo e' superato cioe' se non vengono trovate Udo che richiedano posti letto ma senza averli impostati
	 */
	public boolean checkUdoSenzaPostiLettoInDomanda(String domandaClientid) {
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("CHECK_POSTILETTO_UDO");
		// set parameters
		storedProcedure.registerStoredProcedureParameter("DOMANDACLIENTID", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("NUMUDO", Integer.class, ParameterMode.OUT);
		storedProcedure.setParameter("DOMANDACLIENTID", domandaClientid);
		// execute SP
		storedProcedure.execute();
		// get result
		Integer numUdoTrovate = (Integer)storedProcedure.getOutputParameterValue("NUMUDO");
		if(numUdoTrovate > 0)
			return false;
		return true;
	}

	/**
	 * Controlla che nella domanda di cui viene passato il clientid non ci siano delle Udo di tipo Modulo senza posti letto
	 * @param domandaClientid
	 * @return ritorna true se il controllo e' superato cioe' se non vengono trovate Udo che richiedano posti letto ma senza averli impostati
	 */
	public boolean checkUdoTipoModuloSenzaPostiLettoInDomanda(String domandaClientid) {
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("CHECK_POSTILETTO_UDO_MODULO");
		// set parameters
		storedProcedure.registerStoredProcedureParameter("DOMANDACLIENTID", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("NUMUDO", Integer.class, ParameterMode.OUT);
		storedProcedure.setParameter("DOMANDACLIENTID", domandaClientid);
		// execute SP
		storedProcedure.execute();
		// get result
		Integer numUdoTrovate = (Integer)storedProcedure.getOutputParameterValue("NUMUDO");
		if(numUdoTrovate > 0)
			return false;
		return true;
	}

	/**
	 * Controlla che tutti i requisiti della domanda di cui viene passato il clientid siano stati valutati
	 * @param domandaClientid
	 * @return ritorna true se il controllo e' superato cioe' se non vengono trovati requisiti non valutati
	 */
	public boolean checkDomandaVerificata(String domandaClientid) {
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("CHECK_DOM_VERIFICATA");
		// set parameters
		storedProcedure.registerStoredProcedureParameter("DOMANDACLIENTID", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("VERIFICATA", Integer.class, ParameterMode.OUT);
		storedProcedure.setParameter("DOMANDACLIENTID", domandaClientid);
		// execute SP
		storedProcedure.execute();
		// get result
		Integer verificata = (Integer)storedProcedure.getOutputParameterValue("VERIFICATA");
		if(verificata > 0)
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public List<String> getEntityInstClientIdConRequisitiAssegnatiInVerificaAdUtente(UtenteModel utenteModel, DomandaInst domandaInst){
//		Query q = entityManager.createNativeQuery("select distinct concat(requisito_inst.id_domanda_fk, concat(udo_inst.clientid, uo_inst.clientid))" + 
//				" from requisito_inst" +
//				" left join udo_inst on udo_inst.clientid=requisito_inst.id_udo_fk" +
//				" left join uo_inst on uo_inst.clientid=requisito_inst.id_uo_fk" +
//				" where id_verificatore_fk = ? and (requisito_inst.id_domanda_fk = ? or uo_inst.id_domanda_fk = ? or udo_inst.id_domanda_fk = ?)");
//		q.setParameter(1, utenteModel.getClientid());
//		q.setParameter(2, domandaInst.getClientid());
//		q.setParameter(3, domandaInst.getClientid());
//		q.setParameter(4, domandaInst.getClientid());
		//Modificato il 09/10/2017 per sfruttare il nuovo campo root_id_domanda_fk evitando tutti i join
		Query q = entityManager.createNativeQuery("select distinct concat(id_edificio_fk, concat(id_struttura_fk, concat(id_domanda_fk, concat(id_udo_fk, id_uo_fk))))" + 
				" from requisito_inst" +
				" where id_verificatore_fk = ? and root_id_domanda_fk = ?");
		q.setParameter(1, utenteModel.getClientid());
		q.setParameter(2, domandaInst.getClientid());
		List<String> values = (List<String>)q.getResultList();
		return values;
	}

	@SuppressWarnings("unchecked")
	public List<String> getEntityInstClientIdConRequisitiNonAssegnatiInVerifica(DomandaInst domandaInst){
//		Query q = entityManager.createNativeQuery("select distinct concat(r.id_domanda_fk, concat(r.id_udo_fk, r.id_uo_fk))" + 
//				" from requisito_inst r" +
//				" where r.id_verificatore_fk is null and" +
//				" (" +
//				" r.id_domanda_fk = ? or" +
//				" r.id_udo_fk in (select clientid from udo_inst where id_domanda_fk = ?) or" + 
//				" r.id_uo_fk in (select clientid from uo_inst where id_domanda_fk = ?)" +
//				")");
//		q.setParameter(1, domandaInst.getClientid());
//		q.setParameter(2, domandaInst.getClientid());
//		q.setParameter(3, domandaInst.getClientid());
		//Modificato il 09/10/2017 per sfruttare il nuovo campo root_id_domanda_fk evitando tutti i join
		Query q = entityManager.createNativeQuery("select distinct concat(id_edificio_fk, concat(id_struttura_fk, concat(id_domanda_fk, concat(id_udo_fk, id_uo_fk))))" + 
				" from requisito_inst r" +
				" where r.id_verificatore_fk is null and r.root_id_domanda_fk = ?");
		q.setParameter(1, domandaInst.getClientid());
		List<String> values = (List<String>)q.getResultList();
		return values;
	}

	@SuppressWarnings("unchecked")
	public List<String> getEntityClientIdConRequisitiNonVerificati(DomandaInst domandaInst){
//		Query q = entityManager.createNativeQuery("select distinct concat(r.id_domanda_fk, concat(r.id_udo_fk, r.id_uo_fk))" + 
//				" from requisito_inst r" +
//				" left join requisito_templ rt on rt.clientid=r.id_req_templ_fk" +
//				" left join tipo_risposta tr on tr.clientid=rt.id_tipo_risposta_fk" +
//				" where (tr.nome is null or (tr.nome<>'Titolo' and r.valut_verificatore is null)) and" +
//				" (" +
//				" r.id_domanda_fk = ? or" +
//				" r.id_udo_fk in (select clientid from udo_inst where id_domanda_fk = ?) or" + 
//				" r.id_uo_fk in (select clientid from uo_inst where id_domanda_fk = ?)" +
//				")");
//		q.setParameter(1, domandaInst.getClientid());
//		q.setParameter(2, domandaInst.getClientid());
//		q.setParameter(3, domandaInst.getClientid());
		//Modificato il 09/10/2017 per sfruttare il nuovo campo root_id_domanda_fk evitando tutti i join
		Query q = entityManager.createNativeQuery("select distinct concat(r.id_edificio_fk, concat(r.id_struttura_fk, concat(r.id_domanda_fk, concat(r.id_udo_fk, r.id_uo_fk))))" + 
				" from requisito_inst r" +
				" left join requisito_templ rt on rt.clientid=r.id_req_templ_fk" +
				" left join tipo_risposta tr on tr.clientid=rt.id_tipo_risposta_fk" +
				" where (tr.nome is null or (tr.nome<>'Titolo' and r.valut_verificatore is null)) and r.root_id_domanda_fk = ?");
		q.setParameter(1, domandaInst.getClientid());
		List<String> values = (List<String>)q.getResultList();
		return values;
	}

	@SuppressWarnings("unchecked")
	public List<String> getEntityInstClientIdConRequisitiNonConformi(String domandaClientid){
//		Query q = entityManager.createNativeQuery("select distinct concat(r.id_domanda_fk, concat(r.id_udo_fk, r.id_uo_fk))" + 
//				" from requisito_inst r" +
//				" left join requisito_templ rt on rt.clientid=r.id_req_templ_fk" +
//				" left join tipo_risposta tr on tr.clientid=rt.id_tipo_risposta_fk" +
//				" where (tr.nome is null or (tr.nome<>'Titolo' and R.VALUT_VERIFICATORE<>R.VALUTAZIONE )) and" +
//				" (" +
//				" r.id_domanda_fk = ? or" +
//				" r.id_udo_fk in (select clientid from udo_inst where id_domanda_fk = ?) or" + 
//				" r.id_uo_fk in (select clientid from uo_inst where id_domanda_fk = ?)" +
//				")");
//		q.setParameter(1, domandaClientid);
//		q.setParameter(2, domandaClientid);
//		q.setParameter(3, domandaClientid);
		//Modificato il 09/10/2017 per sfruttare il nuovo campo root_id_domanda_fk evitando tutti i join
		Query q = entityManager.createNativeQuery("select distinct concat(r.id_edificio_fk, concat(r.id_struttura_fk, concat(r.id_domanda_fk, concat(r.id_udo_fk, r.id_uo_fk))))" + 
				" from requisito_inst r" +
				" left join requisito_templ rt on rt.clientid=r.id_req_templ_fk" +
				" left join tipo_risposta tr on tr.clientid=rt.id_tipo_risposta_fk" +
				" where (tr.nome is null or (tr.nome<>'Titolo' and R.VALUT_VERIFICATORE<>R.VALUTAZIONE )) and r.root_id_domanda_fk = ?");
		q.setParameter(1, domandaClientid);
		List<String> values = (List<String>)q.getResultList();
		return values;
	}
	
	public void assegnazioneMassivaRequisiti(String domandaClientid, String operatore, String admin) {
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("AUAC_REQ_ASSEGNAZIONE_MASSIVA");
		// set parameters
		storedProcedure.registerStoredProcedureParameter("DOMANDACLIENTID", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("OPERATORE", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("ADMIN", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("ERRORSTATE", Integer.class, ParameterMode.OUT);
		storedProcedure.setParameter("DOMANDACLIENTID", domandaClientid);
		storedProcedure.setParameter("OPERATORE", operatore);
		storedProcedure.setParameter("ADMIN", admin);
		// execute SP
		storedProcedure.execute();
		// get result
		Integer errorState = (Integer)storedProcedure.getOutputParameterValue("ERRORSTATE");
		if(errorState != 0) {
			log.error("Si è verificato un errore nella assegnazione massiva dei requisiti della domanda " + domandaClientid + " errorState: " + errorState);
		}
	}
}
