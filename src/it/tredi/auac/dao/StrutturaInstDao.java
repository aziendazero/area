package it.tredi.auac.dao;

import it.tredi.auac.orm.entity.DomandaInst;
import it.tredi.auac.orm.entity.StrutturaInst;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityGraph;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;


@Repository("strutturaInstDao")
public class StrutturaInstDao extends AbstractJpaDao<StrutturaInst> {

	public StrutturaInstDao() {
		super();
		setClazz(StrutturaInst.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<StrutturaInst> getStrutturaInstByClientId(String clientId){
		Query q = entityManager.createQuery("select u from StrutturaInst u where u.clientid = :clientid");
		q.setParameter("clientid", clientId);
	    return (List<StrutturaInst>)q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<StrutturaInst> getStrutturaInstByDomandaInst(DomandaInst domandaInst){
		Query q = entityManager.createQuery("select u from StrutturaInst u" + 
				" where u.domandaInst = :domandaInst");
		q.setParameter("domandaInst", domandaInst);
	    return (List<StrutturaInst>)q.getResultList();
	}
	
	public StrutturaInst findByClientId(String clientId) {
		Query q = entityManager.createQuery("select u from StrutturaInst u where u.clientid = :clientid");		
		q.setParameter("clientid", clientId);
	    return (StrutturaInst)q.getSingleResult();
	}	

	@SuppressWarnings("unchecked")
	public Map<String , StrutturaInst> findByClientIdsForAutovalutazioneExportCsv(Set<String> strutturaInstClientid) {
		EntityGraph<?> strutturaInstGraph = entityManager.getEntityGraph("StrutturaInst.autovalutazioneExportCsv");
		Query q = entityManager.createQuery("select u from StrutturaInst u where u.clientid IN :clientids");		
		q.setParameter("clientids", strutturaInstClientid);
		q.setHint("javax.persistence.fetchgraph", strutturaInstGraph);
		
		q.setHint("eclipselink.batch.type", "JOIN");//default

		Map<String, StrutturaInst> toRet = new HashMap<String, StrutturaInst>();
	    for(StrutturaInst strutturaInst : (List<StrutturaInst>)q.getResultList()) {
	    	toRet.put(strutturaInst.getClientid(), strutturaInst);
	    }
	     
	    return toRet;
	}	
	
}