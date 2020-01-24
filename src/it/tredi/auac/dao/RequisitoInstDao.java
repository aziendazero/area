package it.tredi.auac.dao;

import it.tredi.auac.bean.ReportValutazioneUdo;
import it.tredi.auac.business.UpdaterRequisitiInstValutazioneVerificaInfo;
import it.tredi.auac.business.UpdaterTypeEnum;
import it.tredi.auac.converter.TipoGenerazioneValutazioneVerificaSrEnumConverter;
import it.tredi.auac.orm.entity.DomandaInst;
import it.tredi.auac.orm.entity.EdificioInst;
import it.tredi.auac.orm.entity.RequisitoInst;
import it.tredi.auac.orm.entity.StrutturaInst;
import it.tredi.auac.orm.entity.UdoInst;
import it.tredi.auac.orm.entity.UoInst;
import it.tredi.auac.orm.entity.UtenteModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityGraph;
import javax.persistence.NamedQuery;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository("requisitoInstDao")
public class RequisitoInstDao extends AbstractJpaDao<RequisitoInst> {
	private static final Logger log = Logger.getLogger(RequisitoInstDao.class);	
	
	public RequisitoInstDao() {
		super();
		setClazz(RequisitoInst.class);
	}
	
	public RequisitoInst findByClientId(String clientId) {
		Query q = entityManager.createQuery("select r from RequisitoInst r where r.clientid = :clientid");
		q.setParameter("clientid", clientId);
	    return (RequisitoInst)q.getSingleResult();
	}	
	
	@SuppressWarnings("unchecked")
	public List<RequisitoInst> findRequisitiByDomandaInstUtenteModel(String domandaClientId, UtenteModel utenteModel) {
		Query q = entityManager.createNamedQuery("RequisitoInst.findRequisitiByDomandaInstUtenteModel", RequisitoInst.class);
		
		q.setParameter("utenteModel", utenteModel);
		q.setParameter("domandaClientId", domandaClientId);
		setHintRequisitoInst(q);
		//return (List<RequisitoInst>)q.getResultList();
		//Patch per evitare che i requisiti siano doppi a causa del fatto che le uo da organigramma sono ora agganciate a 2 soggetti giuridici (quindi a 2 titolari) 
		return distinctResult((List<RequisitoInst>)q.getResultList());

	}	
	
	@SuppressWarnings("unchecked")
	public List<RequisitoInst> findAllRequisitiByDomandaInst(String domandaClientId) {
		Query q = entityManager.createNamedQuery("RequisitoInst.findAllRequisitiByDomandaInst", RequisitoInst.class);
		
		q.setParameter("domandaClientId", domandaClientId);
		setHintRequisitoInst(q);
	    //return (List<RequisitoInst>)q.getResultList();
		//Patch per evitare che i requisiti siano doppi a causa del fatto che le uo da organigramma sono ora agganciate a 2 soggetti giuridici (quindi a 2 titolari) 
		return distinctResult((List<RequisitoInst>)q.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<RequisitoInst> findRequisitiByUdoInst(UdoInst udoInst, boolean forPdf, boolean forPdfVerifica, boolean forPdfRapportoVerifica) {
		Query q;
		if(forPdfRapportoVerifica)
			q = entityManager.createNamedQuery("RequisitoInst.findRequisitiByUdoInstForVerbaleVerifica", RequisitoInst.class);
		else
			q = entityManager.createNamedQuery("RequisitoInst.findRequisitiByUdoInst", RequisitoInst.class);
		
		q.setParameter("udoInst", udoInst);
		if(forPdf)
			setHintRequisitoInstForPdf(q);
		else if(forPdfVerifica || forPdfRapportoVerifica)
			setHintRequisitoInstForPdfVerifica(q);
		else
			setHintRequisitoInst(q);
		
		//return (List<RequisitoInst>)q.getResultList();
		//Patch per evitare che i requisiti siano doppi a causa del fatto che le uo da organigramma sono ora agganciate a 2 soggetti giuridici (quindi a 2 titolari) 
		return distinctResult((List<RequisitoInst>)q.getResultList());
	}

	private List<RequisitoInst> distinctResult(List<RequisitoInst> results) {
		List<RequisitoInst> toRet = new ArrayList<RequisitoInst>();
		Set<String> clientids = new HashSet<String>();
		for(RequisitoInst req : results) {
			if(!clientids.contains(req.getClientid())) {
				toRet.add(req);
				clientids.add(req.getClientid());
			}
		}
		return toRet;
	}

	@SuppressWarnings("unchecked")
	public List<RequisitoInst> findRequisitiByUoInst(UoInst uoInst, boolean forPdf, boolean forPdfVerifica, boolean forPdfRapportoVerifica) {
		//Query q = entityManager.createNamedQuery("RequisitoInst.findRequisitiByUoInst", RequisitoInst.class);
		Query q;
		if(forPdfRapportoVerifica)
			q = entityManager.createNamedQuery("RequisitoInst.findRequisitiByUoInstForVerbaleVerifica", RequisitoInst.class);
		else
			q = entityManager.createNamedQuery("RequisitoInst.findRequisitiByUoInst", RequisitoInst.class);
		
		q.setParameter("uoInst", uoInst);
		if(forPdf)
			setHintRequisitoInstForPdf(q);
		else if(forPdfVerifica || forPdfRapportoVerifica)
			setHintRequisitoInstForPdfVerifica(q);
		else
			setHintRequisitoInst(q);
		//return (List<RequisitoInst>)q.getResultList();
		//Patch per evitare che i requisiti siano doppi a causa del fatto che le uo da organigramma sono ora agganciate a 2 soggetti giuridici (quindi a 2 titolari) 
		return distinctResult((List<RequisitoInst>)q.getResultList());
	}	

	@SuppressWarnings("unchecked")
	public List<RequisitoInst> findRequisitiByEdificioInst(EdificioInst edificioInst, boolean forPdf, boolean forPdfVerifica, boolean forPdfRapportoVerifica) {
		Query q;
		if(forPdfRapportoVerifica)
			q = entityManager.createNamedQuery("RequisitoInst.findRequisitiByEdificioInstForVerbaleVerifica", RequisitoInst.class);
		else
			q = entityManager.createNamedQuery("RequisitoInst.findRequisitiByEdificioInst", RequisitoInst.class);
		
		q.setParameter("edificioInst", edificioInst);
		if(forPdf)
			setHintRequisitoInstForPdf(q);
		else if(forPdfVerifica || forPdfRapportoVerifica)
			setHintRequisitoInstForPdfVerifica(q);
		else
			setHintRequisitoInst(q);
		//return (List<RequisitoInst>)q.getResultList();
		//Patch per evitare che i requisiti siano doppi a causa del fatto che le uo da organigramma sono ora agganciate a 2 soggetti giuridici (quindi a 2 titolari) 
		return distinctResult((List<RequisitoInst>)q.getResultList());
	}	

	@SuppressWarnings("unchecked")
	public List<RequisitoInst> findRequisitiByStrutturaInst(StrutturaInst strutturaInst, boolean forPdf, boolean forPdfVerifica, boolean forPdfRapportoVerifica) {
		//Query q = entityManager.createNamedQuery("RequisitoInst.findRequisitiByUoInst", RequisitoInst.class);
		Query q;
		if(forPdfRapportoVerifica)
			q = entityManager.createNamedQuery("RequisitoInst.findRequisitiByStrutturaInstForVerbaleVerifica", RequisitoInst.class);
		else
			q = entityManager.createNamedQuery("RequisitoInst.findRequisitiByStrutturaInst", RequisitoInst.class);
		
		q.setParameter("strutturaInst", strutturaInst);
		if(forPdf)
			setHintRequisitoInstForPdf(q);
		else if(forPdfVerifica || forPdfRapportoVerifica)
			setHintRequisitoInstForPdfVerifica(q);
		else
			setHintRequisitoInst(q);
		//return (List<RequisitoInst>)q.getResultList();
		//Patch per evitare che i requisiti siano doppi a causa del fatto che le uo da organigramma sono ora agganciate a 2 soggetti giuridici (quindi a 2 titolari) 
		return distinctResult((List<RequisitoInst>)q.getResultList());
	}	

	@SuppressWarnings("unchecked")
	public List<RequisitoInst> findRequisitiGeneraliAziendaliForReport(DomandaInst domandaInst) {
		Query q = entityManager.createNamedQuery("RequisitoInst.findRequisitiGeneraliAziendali", RequisitoInst.class);
		
		q.setParameter("domandaInst", domandaInst);
		setHintRequisitoInstForRequisitiGeneraliForReport(q);
		//return (List<RequisitoInst>)q.getResultList();
		//Patch per evitare che i requisiti siano doppi a causa del fatto che le uo da organigramma sono ora agganciate a 2 soggetti giuridici (quindi a 2 titolari)
		// nei requisitiGeneraliAziendali non serve perche' non carico le UoModel
		//return distinctResult((List<RequisitoInst>)q.getResultList());
		return (List<RequisitoInst>)q.getResultList();
	}	
	
	public List<RequisitoInst> findRequisitiGeneraliAziendaliForReport(String domandaInstClientid) {
		DomandaInst domanda = new DomandaInst();
		domanda.setClientid(domandaInstClientid);
		return findRequisitiGeneraliAziendaliForReport(domanda);
	}
	
	@SuppressWarnings("unchecked")
	public List<RequisitoInst> findRequisitiGeneraliAziendaliForReportArea(DomandaInst domandaInst) {
		Query q = entityManager.createNamedQuery("RequisitoInst.findRequisitiGeneraliAziendaliForReportArea", RequisitoInst.class);
		
		q.setParameter("domandaInst", domandaInst);
		setHintRequisitoInstForRequisitiGeneraliForReport(q);
		//return (List<RequisitoInst>)q.getResultList();
		//Patch per evitare che i requisiti siano doppi a causa del fatto che le uo da organigramma sono ora agganciate a 2 soggetti giuridici (quindi a 2 titolari)
		// nei requisitiGeneraliAziendali non serve perche' non carico le UoModel
		//return distinctResult((List<RequisitoInst>)q.getResultList());
		return (List<RequisitoInst>)q.getResultList();
	}	

	public List<RequisitoInst> findRequisitiGeneraliAziendaliForReportArea(String domandaInstClientid) {
		DomandaInst domanda = new DomandaInst();
		domanda.setClientid(domandaInstClientid);
		return findRequisitiGeneraliAziendaliForReportArea(domanda);
	}

	@SuppressWarnings("unchecked")
	public List<RequisitoInst> findRequisitiGeneraliAziendali(DomandaInst domandaInst, boolean forPdf, boolean forPdfVerifica, boolean forPdfRapportoVerifica) {
		//Query q = entityManager.createNamedQuery("RequisitoInst.findRequisitiGeneraliAziendali", RequisitoInst.class);
		Query q;
		if(forPdfRapportoVerifica)
			q = entityManager.createNamedQuery("RequisitoInst.findRequisitiGeneraliAziendaliForVerbaleVerifica", RequisitoInst.class);
		else
			q = entityManager.createNamedQuery("RequisitoInst.findRequisitiGeneraliAziendali", RequisitoInst.class);
		
		q.setParameter("domandaInst", domandaInst);
		if(forPdf)
			setHintRequisitoInstForPdf(q);
		else if(forPdfVerifica || forPdfRapportoVerifica)
			setHintRequisitoInstForPdfVerifica(q);
		else
			setHintRequisitoInst(q);
		//return (List<RequisitoInst>)q.getResultList();
		//Patch per evitare che i requisiti siano doppi a causa del fatto che le uo da organigramma sono ora agganciate a 2 soggetti giuridici (quindi a 2 titolari) 
		return distinctResult((List<RequisitoInst>)q.getResultList());
	}	

	private void setHintRequisitoInst(Query q) {
		EntityGraph<?> riInstGraph = entityManager.getEntityGraph("RequisitoInst.autovalutazione");
		q.setHint("javax.persistence.fetchgraph", riInstGraph);
		
		q.setHint("eclipselink.left-join-fetch", "r.uoInst");//@ManyToOne
		
		q.setHint("eclipselink.left-join-fetch", "r.udoInst");//@ManyToOne
		
		//q.setHint("eclipselink.join-fetch", "r.requisitoTempl");//@ManyToOne
		q.setHint("eclipselink.left-join-fetch", "r.requisitoTempl.tipoRisposta");//@ManyToOne
		//q.setHint("eclipselink.left-join-fetch", "r.utenteModel");//@ManyToOne
		q.setHint("eclipselink.left-join-fetch", "r.utenteModel.anagraficaUtenteModel");//@ManyToOne
		q.setHint("eclipselink.left-join-fetch", "r.utenteModel.uoModel");//@ManyToOne
		
		//Questo sembra non avere effetto sulle performance
		//TODO da testare ulteriormente
		//q.setHint("eclipselink.JDBC_FETCH_SIZE", "60000");
	}

	private void setHintRequisitoInstForRequisitiGeneraliForReport(Query q) {
		EntityGraph<?> riInstGraph = entityManager.getEntityGraph("RequisitoInst.requisitiGeneraliForReport");
		q.setHint("javax.persistence.fetchgraph", riInstGraph);
		
//		q.setHint("eclipselink.left-join-fetch", "r.uoInst");//@ManyToOne
//		q.setHint("eclipselink.left-join-fetch", "r.udoInst");//@ManyToOne
		
		q.setHint("eclipselink.left-join-fetch", "r.requisitoTempl.tipoRisposta");//@ManyToOne
//		q.setHint("eclipselink.left-join-fetch", "r.utenteModel.anagraficaUtenteModel");//@ManyToOne
//		q.setHint("eclipselink.left-join-fetch", "r.utenteModel.uoModel");//@ManyToOne
		
		//Questo sembra non avere effetto sulle performance
		//TODO da testare ulteriormente
		//q.setHint("eclipselink.JDBC_FETCH_SIZE", "60000");
	}

	private void setHintRequisitoInstForPdf(Query q) {
		EntityGraph<?> riInstGraph = entityManager.getEntityGraph("RequisitoInst.exportPdf");
		q.setHint("javax.persistence.fetchgraph", riInstGraph);
		
		//q.setHint("eclipselink.left-join-fetch", "r.uoInst");//@ManyToOne
		//q.setHint("eclipselink.left-join-fetch", "r.udoInst");//@ManyToOne
		q.setHint("eclipselink.left-join-fetch", "r.requisitoTempl.tipoRisposta");//@ManyToOne
		q.setHint("eclipselink.left-join-fetch", "r.utenteModel.anagraficaUtenteModel");//@ManyToOne
		q.setHint("eclipselink.left-join-fetch", "r.utenteModel.uoModel");//@ManyToOne
		
		//Questo sembra non avere effetto sulle performance
		//TODO da testare ulteriormente
		//q.setHint("eclipselink.JDBC_FETCH_SIZE", "60000");
	}
	
	private void setHintRequisitoInstForPdfVerifica(Query q) {
		EntityGraph<?> riInstGraph = entityManager.getEntityGraph("RequisitoInst.exportPdf");
		q.setHint("javax.persistence.fetchgraph", riInstGraph);
		
		q.setHint("eclipselink.left-join-fetch", "r.requisitoTempl.tipoRisposta");//@ManyToOne
		q.setHint("eclipselink.left-join-fetch", "r.utenteModel.anagraficaUtenteModel");//@ManyToOne
		q.setHint("eclipselink.left-join-fetch", "r.utenteModel.uoModel");//@ManyToOne
		q.setHint("eclipselink.left-join-fetch", "r.verificatore.anagraficaUtenteModel");//@ManyToOne
		
		//Questo sembra non avere effetto sulle performance
		//TODO da testare ulteriormente
		//q.setHint("eclipselink.JDBC_FETCH_SIZE", "60000");
	}

	@SuppressWarnings("unchecked")
	public List<RequisitoInst> findRequisitiConAllegati() {
		Query q = entityManager.createNamedQuery("RequisitoInst.findRequisitiConAllegati", RequisitoInst.class);
		
		setHintRequisitoInst(q);
		//return (List<RequisitoInst>)q.getResultList();
		//Patch per evitare che i requisiti siano doppi a causa del fatto che le uo da organigramma sono ora agganciate a 2 soggetti giuridici (quindi a 2 titolari) 
		return distinctResult((List<RequisitoInst>)q.getResultList());
	}	
	
	public void impostaTipoValutazioneRequisitiSr(DomandaInst domanda) throws Exception {
		//CREATE OR REPLACE PROCEDURE AUAC_SET_TIPO_VAL_REQ_DOMANDA(DOMANDACLIENTID CHAR, DOMPROCCLIENTID CHAR, ERRORSTATE OUT NUMBER) AS
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("AUAC_SET_TIPO_VAL_REQ_DOMANDA");
		// set parameters
		storedProcedure.registerStoredProcedureParameter("DOMANDACLIENTID", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("DOMPROCCLIENTID", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("ERRORSTATE", Integer.class, ParameterMode.OUT);
		storedProcedure.setParameter("DOMANDACLIENTID", domanda.getClientid());
		storedProcedure.setParameter("DOMPROCCLIENTID", domanda.getTipoProcTempl().getClientid());
		// execute SP
		storedProcedure.execute();
		// get result
		Integer errorState = (Integer)storedProcedure.getOutputParameterValue("ERRORSTATE");
		if(errorState != 0) {
			log.error("Errore lanciando la stored procedure AUAC_SET_TIPO_VAL_REQ_DOMANDA domanda: " + domanda.getClientid());
			throw new Exception("Impossibile aggiornare il tipo valutazione dei requisiti SR");
		}
	}
	
	public void impostaTipoGenerazioneRequisitiSr(DomandaInst domanda) throws Exception {
		//CREATE OR REPLACE PROCEDURE AUAC_SET_TIPO_GEN_REQ_DOMANDA(DOMANDACLIENTID CHAR, ERRORSTATE OUT NUMBER) AS
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("AUAC_SET_TIPO_GEN_REQ_DOMANDA");
		// set parameters
		storedProcedure.registerStoredProcedureParameter("DOMANDACLIENTID", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("ERRORSTATE", Integer.class, ParameterMode.OUT);
		storedProcedure.setParameter("DOMANDACLIENTID", domanda.getClientid());
		// execute SP
		storedProcedure.execute();
		// get result
		Integer errorState = (Integer)storedProcedure.getOutputParameterValue("ERRORSTATE");
		if(errorState != 0) {
			log.error("Errore lanciando la stored procedure AUAC_SET_TIPO_GEN_REQ_DOMANDA domanda: " + domanda.getClientid());
			throw new Exception("Impossibile aggiornare il tipo generazione dei requisiti SR");
		}
	}

	public void impostaValutazioneRequisitiGenSrOnDomanda(DomandaInst domanda) throws Exception {
		//CREATE OR REPLACE PROCEDURE AUAC_UPD_DOM_SR_REQ_INST_VAL(DOMANDACLIENTID CHAR, ERRORSTATE OUT NUMBER) AS
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("AUAC_UPD_DOM_SR_REQ_INST_VAL");
		// set parameters
		storedProcedure.registerStoredProcedureParameter("DOMANDACLIENTID", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("ERRORSTATE", Integer.class, ParameterMode.OUT);
		storedProcedure.setParameter("DOMANDACLIENTID", domanda.getClientid());
		// execute SP
		storedProcedure.execute();
		// get result
		Integer errorState = (Integer)storedProcedure.getOutputParameterValue("ERRORSTATE");
		if(errorState != 0) {
			log.error("Errore lanciando la stored procedure AUAC_UPD_DOM_SR_REQ_INST_VAL domanda: " + domanda.getClientid());
			throw new Exception("Impossibile aggiornare la valutazione dei requisiti SR sulla domanda");
		}
	}

	public void impostaValutazioneVerificatoreRequisitiGenSrOnDomanda(DomandaInst domanda) throws Exception {
		//CREATE OR REPLACE PROCEDURE AUAC_UPD_DOM_SR_REQ_INST_VER(DOMANDACLIENTID CHAR, ERRORSTATE OUT NUMBER) AS
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("AUAC_UPD_DOM_SR_REQ_INST_VER");
		// set parameters
		storedProcedure.registerStoredProcedureParameter("DOMANDACLIENTID", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("ERRORSTATE", Integer.class, ParameterMode.OUT);
		storedProcedure.setParameter("DOMANDACLIENTID", domanda.getClientid());
		// execute SP
		storedProcedure.execute();
		// get result
		Integer errorState = (Integer)storedProcedure.getOutputParameterValue("ERRORSTATE");
		if(errorState != 0) {
			log.error("Errore lanciando la stored procedure AUAC_UPD_DOM_SR_REQ_INST_VER domanda: " + domanda.getClientid());
			throw new Exception("Impossibile aggiornare la valutazione verificatore dei requisiti SR sulla domanda");
		}
	}

	public void impostaValutazioneRequisitiGenSrByRequisitoUoStruMod(DomandaInst domanda, RequisitoInst requisitoInst) throws Exception {
		this.impostaValutazioneRequisitiGenSrByRequisitoUoStruMod(domanda.getClientid(), requisitoInst);
	}
	
	public void impostaValutazioneRequisitiGenSrByRequisitoUoStruMod(String domandaClientid, RequisitoInst requisitoInst) throws Exception {
		TipoGenerazioneValutazioneVerificaSrEnumConverter converter = new TipoGenerazioneValutazioneVerificaSrEnumConverter();
		//CREATE OR REPLACE PROCEDURE AUAC_UPDATE_SR_REQ_INST_VAL(DOMANDACLIENTID CHAR, IDREQTEMPLFKCLID CHAR, IDTIPORISPCLID CHAR, TIPOGENSR CHAR, ERRORSTATE OUT NUMBER) AS
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("AUAC_UPDATE_SR_REQ_INST_VAL");
		// set parameters
		storedProcedure.registerStoredProcedureParameter("DOMANDACLIENTID", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("IDREQTEMPLFKCLID", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("IDTIPORISPCLID", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("TIPOGENSR", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("ERRORSTATE", Integer.class, ParameterMode.OUT);
		storedProcedure.setParameter("DOMANDACLIENTID", domandaClientid);
		storedProcedure.setParameter("IDREQTEMPLFKCLID", requisitoInst.getRequisitoTempl().getClientid());
		storedProcedure.setParameter("IDTIPORISPCLID", requisitoInst.getRequisitoTempl().getTipoRisposta().getClientid());
		storedProcedure.setParameter("TIPOGENSR", converter.convertToDatabaseColumn(requisitoInst.getGeneraValutazioneSr()));
		// execute SP
		storedProcedure.execute();
		// get result
		Integer errorState = (Integer)storedProcedure.getOutputParameterValue("ERRORSTATE");
		if(errorState != 0) {
			log.error("Errore lanciando la stored procedure AUAC_UPDATE_SR_REQ_INST_VAL domanda: " + domandaClientid + "; requisito: " + requisitoInst.getDescr() + " [" + requisitoInst.getClientid() + "]");
			throw new Exception("Impossibile aggiornare la valutazione dei requisiti SR");
		}
	}

	public void impostaValutazioneVerificatoreRequisitiGenSrByRequisitoUoStruMod(DomandaInst domanda, RequisitoInst requisitoInst) throws Exception {
		this.impostaValutazioneVerificatoreRequisitiGenSrByRequisitoUoStruMod(domanda.getClientid(), requisitoInst);
	}
	
	public void impostaValutazioneVerificatoreRequisitiGenSrByRequisitoUoStruMod(String domandaClientid, RequisitoInst requisitoInst) throws Exception {
		TipoGenerazioneValutazioneVerificaSrEnumConverter converter = new TipoGenerazioneValutazioneVerificaSrEnumConverter();
		//CREATE OR REPLACE PROCEDURE AUAC_UPDATE_SR_REQ_INST_VER(DOMANDACLIENTID CHAR, IDREQTEMPLFKCLID CHAR, IDTIPORISPCLID CHAR, TIPOGENSR CHAR, ERRORSTATE OUT NUMBER) AS
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("AUAC_UPDATE_SR_REQ_INST_VER");
		// set parameters
		storedProcedure.registerStoredProcedureParameter("DOMANDACLIENTID", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("IDREQTEMPLFKCLID", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("IDTIPORISPCLID", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("TIPOGENSR", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("ERRORSTATE", Integer.class, ParameterMode.OUT);
		storedProcedure.setParameter("DOMANDACLIENTID", domandaClientid);
		storedProcedure.setParameter("IDREQTEMPLFKCLID", requisitoInst.getRequisitoTempl().getClientid());
		storedProcedure.setParameter("IDTIPORISPCLID", requisitoInst.getRequisitoTempl().getTipoRisposta().getClientid());
		storedProcedure.setParameter("TIPOGENSR", converter.convertToDatabaseColumn(requisitoInst.getGeneraValutazioneSr()));
		// execute SP
		storedProcedure.execute();
		// get result
		Integer errorState = (Integer)storedProcedure.getOutputParameterValue("ERRORSTATE");
		if(errorState != 0) {
			log.error("Errore lanciando la stored procedure AUAC_UPDATE_SR_REQ_INST_VER domanda: " + domandaClientid + "; requisito: " + requisitoInst.getDescr() + " [" + requisitoInst.getClientid() + "]");
			throw new Exception("Impossibile aggiornare la valutazione verificatore dei requisiti SR");
		}
	}
	
	public void impostaValutazioneOrValutazioneVerificatoreRequisitiGenSrByRequisitoUoStruMod(UpdaterRequisitiInstValutazioneVerificaInfo updaterRequisitiInstValutazioneVerificaInfo) throws Exception {
		if(updaterRequisitiInstValutazioneVerificaInfo.getUpdaterTypeEnum() == UpdaterTypeEnum.VALUTAZIONE) {
			for(RequisitoInst requisitoInst : updaterRequisitiInstValutazioneVerificaInfo.getClassificazioneUdoTemplClientids().values()) {
				this.impostaValutazioneRequisitiGenSrByRequisitoUoStruMod(updaterRequisitiInstValutazioneVerificaInfo.getDomandaClientid(), requisitoInst);
			}
		} else {
			for(RequisitoInst requisitoInst : updaterRequisitiInstValutazioneVerificaInfo.getClassificazioneUdoTemplClientids().values()) {
				this.impostaValutazioneVerificatoreRequisitiGenSrByRequisitoUoStruMod(updaterRequisitiInstValutazioneVerificaInfo.getDomandaClientid(), requisitoInst);
			}			
		}
	}
	
//	@SuppressWarnings("unchecked")
//	public List<ReportValutazioneUdo> getReportMediaRequisitiPerUdoInstInDomandas(String clientidDomanda){
//		Query q = entityManager.createNativeQuery("SELECT RI.ID_UDO_FK, UDOI.ID_UNIVOCO, UDOI.DESCR, FORMATTA_DECIMAL_TO_STR(AVG(RI.VALUTAZIONE)) FROM REQUISITO_INST RI" +
//			" INNER JOIN REQUISITO_TEMPL RT ON RT.CLIENTID=RI.ID_REQ_TEMPL_FK" +
//			" INNER JOIN TIPO_RISPOSTA TR ON TR.CLIENTID=RT.ID_TIPO_RISPOSTA_FK" + 
//			" INNER JOIN UDO_INST UDOI ON UDOI.CLIENTID=RI.ID_UDO_FK" +
//			" WHERE ROOT_ID_DOMANDA_FK=? AND ID_UDO_FK IS NOT NULL" +
//			" AND TR.NOME = 'Soglia' AND VALUTAZIONE IN ('0', '60', '100')" +
//			" GROUP BY RI.ID_UDO_FK, UDOI.ID_UNIVOCO, UDOI.DESCR" +
//			" ORDER BY UDOI.ID_UNIVOCO"); 
//	    
//		q.setParameter(1, clientidDomanda);
//
//		List<Object[]> results = q.getResultList();
//		List<ReportValutazioneUdo> toRet = new ArrayList<ReportValutazioneUdo>();
//		for(Object[] obj : results)
//			toRet.add(new ReportValutazioneUdo((String)obj[0], (String)obj[1], (String)obj[2], (String)obj[3]));
//		
//	    return toRet;
//	}

	@SuppressWarnings("unchecked")
	public List<ReportValutazioneUdo> getReportMediaValutazioneRequisitiPerUdoInstInDomanda(String clientidDomanda){
		Query q = entityManager.createNativeQuery("SELECT RI.ID_UDO_FK, UDOI.ID_UNIVOCO, UDOI.DESCR, AVG(RI.VALUTAZIONE) FROM REQUISITO_INST RI" +
			" INNER JOIN REQUISITO_TEMPL RT ON RT.CLIENTID=RI.ID_REQ_TEMPL_FK" +
			" INNER JOIN TIPO_RISPOSTA TR ON TR.CLIENTID=RT.ID_TIPO_RISPOSTA_FK" + 
			" INNER JOIN UDO_INST UDOI ON UDOI.CLIENTID=RI.ID_UDO_FK" +
			" WHERE ROOT_ID_DOMANDA_FK=? AND ID_UDO_FK IS NOT NULL" +
			" AND TR.NOME = 'Soglia' AND VALUTAZIONE IN ('0', '60', '100')" +
			" GROUP BY RI.ID_UDO_FK, UDOI.ID_UNIVOCO, UDOI.DESCR" +
			" ORDER BY UDOI.ID_UNIVOCO"); 
	    
		q.setParameter(1, clientidDomanda);

		List<Object[]> results = q.getResultList();
		List<ReportValutazioneUdo> toRet = new ArrayList<ReportValutazioneUdo>();
		for(Object[] obj : results)
			toRet.add(new ReportValutazioneUdo((String)obj[0], (String)obj[1], (String)obj[2], (BigDecimal)obj[3]));
		
	    return toRet;
	}

	@SuppressWarnings("unchecked")
	public List<ReportValutazioneUdo> getReportMediaValutazioneVerificatoreRequisitiPerUdoInstInDomanda(String clientidDomanda){
		Query q = entityManager.createNativeQuery("SELECT RI.ID_UDO_FK, UDOI.ID_UNIVOCO, UDOI.DESCR, AVG(RI.VALUT_VERIFICATORE) FROM REQUISITO_INST RI" +
			" INNER JOIN REQUISITO_TEMPL RT ON RT.CLIENTID=RI.ID_REQ_TEMPL_FK" +
			" INNER JOIN TIPO_RISPOSTA TR ON TR.CLIENTID=RT.ID_TIPO_RISPOSTA_FK" + 
			" INNER JOIN UDO_INST UDOI ON UDOI.CLIENTID=RI.ID_UDO_FK" +
			" WHERE ROOT_ID_DOMANDA_FK=? AND ID_UDO_FK IS NOT NULL" +
			" AND TR.NOME = 'Soglia' AND VALUT_VERIFICATORE IN ('0', '60', '100')" +
			" GROUP BY RI.ID_UDO_FK, UDOI.ID_UNIVOCO, UDOI.DESCR" +
			" ORDER BY UDOI.ID_UNIVOCO"); 
	    
		q.setParameter(1, clientidDomanda);

		List<Object[]> results = q.getResultList();
		List<ReportValutazioneUdo> toRet = new ArrayList<ReportValutazioneUdo>();
		for(Object[] obj : results)
			toRet.add(new ReportValutazioneUdo((String)obj[0], (String)obj[1], (String)obj[2], (BigDecimal)obj[3]));
		
	    return toRet;
	}

	public BigDecimal getReportMediaComplessivaValutazioneRequisitiPerDomanda(String clientidDomanda){
		//Filtro tutti i requisiti di tipo soglia con risposta '0', '60', '100'
		// che siano in udo o requisiti generali
		
		//ID_UDO_FK, ID_DOMANDA_FK
		//ID_UO_FK, ID_STRUTTURA_FK, ID_EDIFICIO_FK
		Query q = entityManager.createNativeQuery("SELECT AVG(RI.VALUTAZIONE)" +
			" FROM REQUISITO_INST RI" +
			" INNER JOIN REQUISITO_TEMPL RT ON RT.CLIENTID=RI.ID_REQ_TEMPL_FK" +
			" INNER JOIN TIPO_RISPOSTA TR ON TR.CLIENTID=RT.ID_TIPO_RISPOSTA_FK" + 
			" WHERE ROOT_ID_DOMANDA_FK=? AND (ID_UDO_FK IS NOT NULL OR ID_DOMANDA_FK IS NOT NULL)" +
			" AND TR.NOME = 'Soglia' AND VALUTAZIONE IN ('0', '60', '100')" +
			" GROUP BY RI.ROOT_ID_DOMANDA_FK"); 
	    
		q.setParameter(1, clientidDomanda);

		Object result = null;
		try {
			result = q.getSingleResult();
		} catch(Exception e) {
			log.warn("nessun record restituito", e);
		}

		BigDecimal toRet = null;
		if(result != null)
			toRet = (BigDecimal)result;
	    return toRet;
	}

	public BigDecimal getReportMediaComplessivaValutazioneVerificatoreRequisitiPerDomanda(String clientidDomanda){
		//Filtro tutti i requisiti di tipo soglia con risposta '0', '60', '100'
		// che siano in udo o requisiti generali
		
		//ID_UDO_FK, ID_DOMANDA_FK
		//ID_UO_FK, ID_STRUTTURA_FK, ID_EDIFICIO_FK
		Query q = entityManager.createNativeQuery("SELECT AVG(RI.VALUT_VERIFICATORE)" +
			" FROM REQUISITO_INST RI" +
			" INNER JOIN REQUISITO_TEMPL RT ON RT.CLIENTID=RI.ID_REQ_TEMPL_FK" +
			" INNER JOIN TIPO_RISPOSTA TR ON TR.CLIENTID=RT.ID_TIPO_RISPOSTA_FK" + 
			" WHERE ROOT_ID_DOMANDA_FK=? AND (ID_UDO_FK IS NOT NULL OR ID_DOMANDA_FK IS NOT NULL)" +
			" AND TR.NOME = 'Soglia' AND VALUT_VERIFICATORE IN ('0', '60', '100')" +
			" GROUP BY RI.ROOT_ID_DOMANDA_FK"); 
	    
		q.setParameter(1, clientidDomanda);

		Object result = null;
		try {
			result = q.getSingleResult();
		} catch(Exception e) {
			log.warn("nessun record restituito", e);
		}
		BigDecimal toRet = null;
		if(result != null)
			toRet = (BigDecimal)result;
	    return toRet;
	}

	@SuppressWarnings("unchecked")
	public void deleteAllRequisitiGenerealiDomanda(String domandaClientId) {
		Query q = entityManager.createNativeQuery("DELETE" +
				" FROM REQUISITO_INST RI" +
				" WHERE RI.ID_DOMANDA_FK=?"); 
		    
		q.setParameter(1, domandaClientId);
		q.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public void deleteAllRequisitiForAllUoDomanda(String domandaClientId) {
		Query q = entityManager.createNativeQuery("DELETE" +
				" FROM REQUISITO_INST RI" +
				" WHERE RI.ROOT_ID_DOMANDA_FK=? AND RI.ID_UO_FK IS NOT NULL"); 
		    
		q.setParameter(1, domandaClientId);
		q.executeUpdate();
	}
}
