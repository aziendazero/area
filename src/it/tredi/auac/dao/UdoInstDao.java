package it.tredi.auac.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityGraph;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import it.tredi.auac.TipoPostiLettoEnum;
import it.tredi.auac.orm.entity.DisciplinaUdoInstInfo;
import it.tredi.auac.orm.entity.DomandaInst;
import it.tredi.auac.orm.entity.TitolareModel;
import it.tredi.auac.orm.entity.UdoInst;


@Repository("udoInstDao")
public class UdoInstDao extends AbstractJpaDao<UdoInst> {

	private static final Logger log = Logger.getLogger(UdoModelDao.class);
	
	public UdoInstDao() {
		super();
		setClazz(UdoInst.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<UdoInst> getUdoInstByClientId(String clientId){
		Query q = entityManager.createQuery("select u from UdoInst u where u.clientid = :clientid");
		q.setParameter("clientid", clientId);
	    return (List<UdoInst>)q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<UdoInst> getUdoInstByDomandaInst(DomandaInst domandaInst){
		Query q = entityManager.createQuery("select u from UdoInst u" + 
				//" join u.udoModel u1" + 
				" join u.tipoUdoTempl t" +
				//" left join u1.disciplinaTempls dt"+
				" where u.domandaInst = :domandaInst");
		q.setParameter("domandaInst", domandaInst);
	    return (List<UdoInst>)q.getResultList();
	}
	
	public UdoInst findByClientId(String clientId) {
		Query q = entityManager.createQuery("select u from UdoInst u where u.clientid = :clientid");		
		q.setParameter("clientid", clientId);
	    return (UdoInst)q.getSingleResult();
	}	

	/*public UdoInst findByClientIdForAutovalutazione(String clientId) {
		EntityGraph<?> udoInstGraph = entityManager.getEntityGraph("UdoInst.autovalutazione");
		Query q = entityManager.createQuery("select u from UdoInst u where u.clientid = :clientid");		
		q.setParameter("clientid", clientId);
		q.setHint("javax.persistence.fetchgraph", udoInstGraph);
		
		//q.setHint("eclipselink.left-join-fetch", "u.requisitoInsts");//@OneToMany
		//q.setHint("eclipselink.left-join-fetch", "u.requisitoInsts.requisitoTempl");//@ManyToOne
		q.setHint("eclipselink.left-join-fetch", "u.requisitoInsts.requisitoTempl.tipoRisposta");//@ManyToOne
		//q.setHint("eclipselink.left-join-fetch", "u.requisitoInsts.utenteModel");//@ManyToOne
		q.setHint("eclipselink.left-join-fetch", "u.requisitoInsts.utenteModel.anagraficaUtenteModel");//@ManyToOne
		q.setHint("eclipselink.left-join-fetch", "u.requisitoInsts.utenteModel.uoModel");//@ManyToOne

		//q.setHint("eclipselink.left-join-fetch", "u.udoModel");@ManyToOne
		//q.setHint("eclipselink.left-join-fetch", "u.tipoUdoTempl");@ManyToOne
		
		//q.setHint("eclipselink.batch.type", "JOIN");//default
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

		
	    return (UdoInst)q.getSingleResult();
	}*/

	@SuppressWarnings("unchecked")
	public Map<String , UdoInst> findByClientIdsForAutovalutazioneExportCsv(Set<String> udoInstClientid) {
		EntityGraph<?> udoInstGraph = entityManager.getEntityGraph("UdoInst.autovalutazioneExportCsv");
		Query q = entityManager.createQuery("select u from UdoInst u where u.clientid IN :clientids");		
		q.setParameter("clientids", udoInstClientid);
		q.setHint("javax.persistence.fetchgraph", udoInstGraph);
		
		q.setHint("eclipselink.batch.type", "JOIN");//default

		/*
		@NamedAttributeNode(value="disciplinaTempls", subgraph="disciplinaTempl"),
		@NamedAttributeNode(value="brancaTempls", subgraph="brancaTempl"),
		@NamedAttributeNode(value="udoModel", subgraph="udoModel"),
		@NamedAttributeNode(value="tipoUdoTempl", subgraph="tipoUdoTempl"),
		@NamedAttributeNode(value="fattProdUdoInsts", subgraph="fattProdUdoInsts")
		 */

		//q.setHint("eclipselink.left-join-fetch", "u.disciplinaTempls");//@ManyToMany
		//q.setHint("eclipselink.left-join-fetch", "u.brancaTempls");//@ManyToMany
		//q.setHint("eclipselink.left-join-fetch", "u.fattProdUdoInsts.tipoFattoreProdTempl");//@ManyToMany
		//q.setHint("eclipselink.left-join-fetch", "u.udoModel");//@ManyToOne
		//q.setHint("eclipselink.left-join-fetch", "u.tipoUdoTempl");//@ManyToOne
		
		
		//q.setHint("eclipselink.join-fetch", "d.titolareModel");
		//q.setHint("eclipselink.join-fetch", "d.tipoProcTempl");
		
		//q.setHint("eclipselink.left-join-fetch", "d.uoInsts");//@OneToMany
		//Occorre impostare questo perche' jpa segue l'albero per un solo livello 
		//quindi caricando la domanda carica la lista uoInst (primo livello) e li si ferma caricando d.uoInsts.uoModel con query separate 
		//q.setHint("eclipselink.left-join-fetch", "d.uoInsts.uoModel");//@ManyToOne, @JoinFetch(value=JoinFetchType.INNER)

		//q.setHint("eclipselink.batch", "d.udoInsts");//@OneToMany
		//q.setHint("eclipselink.left-join-fetch", "d.udoInsts.disciplinaTempls");//@ManyToMany

		Map<String, UdoInst> toRet = new HashMap<String, UdoInst>();
	    for(UdoInst udoInst : (List<UdoInst>)q.getResultList()) {
	    	toRet.put(udoInst.getClientid(), udoInst);
	    }
	     
	    return toRet;
	}	

	@SuppressWarnings("unchecked")
	public List<UdoInst> getUdoInstForCompareByDomandaInst(String domandaClientid, String titolareDomandaClientid){
		//Set<String> udoInstClientids = getUdoInstClientIdForCompareByDomandaInst(domandaInst);
		List<String> udoInstClientids = getUdoInstClientIdForCompareByDomandaInst(domandaClientid, titolareDomandaClientid);
		if(udoInstClientids.size() > 0) {		
			Query q = entityManager.createQuery("select u from UdoInst u" + 
					" join u.tipoUdoTempl t" +
					" where u.clientid IN :udoInstClientids");
			q.setParameter("udoInstClientids", udoInstClientids);
		    return (List<UdoInst>)q.getResultList();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private List<String> getUdoInstClientIdForCompareByDomandaInst(String domandaClientid, String titolareDomandaClientid){
/*
SELECT udo.clientid
FROM
(
  SELECT udo.ID_UDO_MODEL_FK ID_UDO_MODEL_FK, MAX(di.NUMERO_PROCEDIMENTO) max_num_proc FROM UDO_INST udo
  INNER JOIN DOMANDA_INST di on di.CLIENTID=udo.ID_DOMANDA_FK
  WHERE udo.ID_UDO_MODEL_FK IN (SELECT ID_UDO_MODEL_FK FROM UDO_INST WHERE ID_DOMANDA_FK='6079F56A-FCCC-42E0-B99C-80CBF8924C06')
  AND udo.ID_DOMANDA_FK IN (SELECT CLIENTID FROM DOMANDA_INST WHERE ID_TITOLARE_FK='F49C7289-BDC9-3865-8A24-C5C7FA2238B2' AND STATO='PROCEDIMENTO CONCLUSO')
  -- AND udo.ID_DOMANDA_FK<>'836A7533-F491-45BD-B15D-9654CAFB05FC' si puo' evitare in quanto la domanda corrente su cui si fa il confronto non puo' assere in STATO='PROCEDIMENTO CONCLUSO'
  GROUP BY udo.ID_UDO_MODEL_FK
) subq 
INNER JOIN UDO_INST udo ON udo.ID_UDO_MODEL_FK=subq.ID_UDO_MODEL_FK
INNER JOIN DOMANDA_INST di on di.CLIENTID=udo.ID_DOMANDA_FK AND di.NUMERO_PROCEDIMENTO=subq.max_num_proc
  WHERE 
  -- udo.ID_UDO_MODEL_FK IN (SELECT ID_UDO_MODEL_FK FROM UDO_INST WHERE ID_DOMANDA_FK='836A7533-F491-45BD-B15D-9654CAFB05FC') gia' incluso con udo.ID_UDO_MODEL_FK=subq.ID_UDO_MODEL_FK AND 
  udo.ID_DOMANDA_FK IN (SELECT CLIENTID FROM DOMANDA_INST WHERE ID_TITOLARE_FK='F49C7289-BDC9-3865-8A24-C5C7FA2238B2' AND STATO='PROCEDIMENTO CONCLUSO')
;
 */
		
		Query q = entityManager.createNativeQuery("SELECT udo.clientid" + 
				" FROM" + 
				" (" + 
				" SELECT udo.ID_UDO_MODEL_FK ID_UDO_MODEL_FK, MAX(di.NUMERO_PROCEDIMENTO) max_num_proc FROM UDO_INST udo" + 
				" INNER JOIN DOMANDA_INST di on di.CLIENTID=udo.ID_DOMANDA_FK" + 
				" WHERE udo.ID_UDO_MODEL_FK IN (SELECT ID_UDO_MODEL_FK FROM UDO_INST WHERE ID_DOMANDA_FK=?)" + 
				" AND udo.ID_DOMANDA_FK IN (SELECT CLIENTID FROM DOMANDA_INST WHERE ID_TITOLARE_FK=? AND STATO='PROCEDIMENTO CONCLUSO')" + 
				" GROUP BY udo.ID_UDO_MODEL_FK" + 
				" ) subq" + 
				" INNER JOIN UDO_INST udo ON udo.ID_UDO_MODEL_FK=subq.ID_UDO_MODEL_FK" + 
				" INNER JOIN DOMANDA_INST di on di.CLIENTID=udo.ID_DOMANDA_FK AND di.NUMERO_PROCEDIMENTO=subq.max_num_proc");
				//questa parte non serve dato NUMERO_PROCEDIMENTO della domanda e ID_UDO_MODEL_FK della UDO_INST recupero una sola UDO_INST
				// + 
				//" WHERE" + 
				//" udo.ID_DOMANDA_FK IN (SELECT CLIENTID FROM DOMANDA_INST WHERE ID_TITOLARE_FK=? AND STATO='PROCEDIMENTO CONCLUSO')"); 
	    q.setParameter(1, domandaClientid);
	    q.setParameter(2, titolareDomandaClientid);
	    //q.setParameter(3, domandaInst.getTitolareModel().getClientid());

	    List<String> toRet = (List<String>)q.getResultList();
	    
	    return toRet;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getUdoInstClientIdInDomandaSenzaRequisiti(String domandaClientid){
		Query q = entityManager.createNativeQuery("SELECT UDOI.CLIENTID FROM UDO_INST UDOI" + 
				" LEFT JOIN REQUISITO_INST RI ON RI.ID_UDO_FK=UDOI.CLIENTID" + 
				" WHERE UDOI.ID_DOMANDA_FK=? AND RI.CLIENTID IS NULL");
	    q.setParameter(1, domandaClientid);
	    List<String> values = (List<String>)q.getResultList();
	    return values;
	}

	@SuppressWarnings("unchecked")
	public List<UdoInst> getUdoInstByUdoModelClientId(String udoModelclientId) {
		Query q = entityManager.createQuery("select u from UdoInst u" + 
				" where u.udoModelClientid=:udomodelclientid"+
				" order by u.domandaInst.creation asc");
		q.setParameter("udomodelclientid", udoModelclientId);
	    return (List<UdoInst>)q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<DisciplinaUdoInstInfo> findDisciplinaUdoInstInfoByTitolareForAttuati(TitolareModel titolare){
		List<DisciplinaUdoInstInfo> toRet = new ArrayList<DisciplinaUdoInstInfo>();
		Query q = entityManager.createQuery(""
			+ "select udo" + 
			" from UdoInst udo" + 
			" where udo.domandaInst in (select dom" + 
			" from DomandaInst dom" + 
			" where dom.titolareModel.clientid=:titolare and dom.stato=:stato)" + 
			" and udo.esito=:esito and udo.disciplineInfo is not null");
		q.setParameter("titolare", titolare.getClientid());
		q.setParameter("stato", "PROCEDIMENTO CONCLUSO");
		q.setParameter("esito", "Accreditata");
		try {
			List<UdoInst> results = (List<UdoInst>)q.getResultList();
			for (UdoInst result : results) {
				toRet.addAll(result.getDisciplineUdoInstsInfo());
			}
	    } catch(Exception ex) {
	    	log.warn("Impossibile trovare la DisciplinaUdoInstInfo per PL attuati", ex);
	    	return null;
	    }
		return toRet;
	}
	
	@SuppressWarnings("unchecked")
	public List<DisciplinaUdoInstInfo> findDisciplinaUdoInstInfoByTitolareForProgrammati(TitolareModel titolare){
		List<DisciplinaUdoInstInfo> toRet = new ArrayList<DisciplinaUdoInstInfo>();
		Date now = new Date();
		Query q = entityManager.createNativeQuery(""
			+ "select rd.DENOMINAZIONE,dt.CLIENTID,dt.CODICE,dt.DESCR,pd.POSTI_LETTO_REGIONE,pd.POSTI_LETTO_EXTRA_REG, rd.ORDINE, dt.ORDINE" + 
			" from PROGRAMMAZIONE_DISCIPLINA pd" + 
			" inner join PROGRAMMAZIONE pr on pr.CLIENTID = pd.ID_PROGRAMMAZIONE_FK" + 
			" inner join DISCIPLINA_TEMPL dt on dt.CLIENTID = pd.ID_DISCIPLINA_FK" + 
			" inner join RAGG_DISCPL rd on rd.CLIENTID = dt.ID_RAGG_DISCIPL_TEMPL_FK" + 
			" where pr.ID_TITOLARE_FK = ? and pd.ID_DISCIPLINA_FK is not null" + 
			" and pd.INIZIO_VALIDITA <= ? and (pd.FINE_VALIDITA >= ? or pd.FINE_VALIDITA is null)"
		);
		q.setParameter(1, titolare.getClientid());
		q.setParameter(2, now);
		q.setParameter(3, now);
		try {
			List<Object[]> results = q.getResultList();
			for (Object[] result : results) {
				DisciplinaUdoInstInfo disciplinaUdoInstInfo = new DisciplinaUdoInstInfo();
				disciplinaUdoInstInfo.setArea((String) result[0]);
				disciplinaUdoInstInfo.setId((String) result[1]);
				disciplinaUdoInstInfo.setCodice((String) result[2]);
				disciplinaUdoInstInfo.setDescr((String) result[3]);
				disciplinaUdoInstInfo.setPostiLetto((BigDecimal) result[4]);
				disciplinaUdoInstInfo.setPostiLettoExtra((BigDecimal) result[5]);
				disciplinaUdoInstInfo.setOrdineArea((BigDecimal) result[6]);
				disciplinaUdoInstInfo.setOrdineDisc((BigDecimal) result[7]);
				disciplinaUdoInstInfo.setTipoPostiLetto(TipoPostiLettoEnum.PROGRAMMATI);
				toRet.add(disciplinaUdoInstInfo);
			}
	    } catch(Exception ex) {
	    	log.warn("Impossibile trovare la DisciplinaUdoInstInfo per PL programmati", ex);
	    	return null;
	    }
		return toRet;
	}
	
}