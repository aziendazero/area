package it.tredi.auac.dao;

import it.tredi.auac.orm.entity.DomandaInst;
import it.tredi.auac.orm.entity.EdificioInst;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityGraph;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;


@Repository("edificioInstDao")
public class EdificioInstDao extends AbstractJpaDao<EdificioInst> {

	public EdificioInstDao() {
		super();
		setClazz(EdificioInst.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<EdificioInst> getEdificioInstByClientId(String clientId){
		Query q = entityManager.createQuery("select u from EdificioInst u where u.clientid = :clientid");
		q.setParameter("clientid", clientId);
	    return (List<EdificioInst>)q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<EdificioInst> getEdificioInstByDomandaInst(DomandaInst domandaInst){
		Query q = entityManager.createQuery("select u from EdificioInst u" + 
				" where u.domandaInst = :domandaInst");
		q.setParameter("domandaInst", domandaInst);
	    return (List<EdificioInst>)q.getResultList();
	}
	
	public EdificioInst findByClientId(String clientId) {
		Query q = entityManager.createQuery("select u from EdificioInst u where u.clientid = :clientid");		
		q.setParameter("clientid", clientId);
	    return (EdificioInst)q.getSingleResult();
	}	

	@SuppressWarnings("unchecked")
	public Map<String , EdificioInst> findByClientIdsForAutovalutazioneExportCsv(Set<String> edificioInstClientid) {
		EntityGraph<?> edificioInstGraph = entityManager.getEntityGraph("EdificioInst.autovalutazioneExportCsv");
		Query q = entityManager.createQuery("select u from EdificioInst u where u.clientid IN :clientids");		
		q.setParameter("clientids", edificioInstClientid);
		q.setHint("javax.persistence.fetchgraph", edificioInstGraph);
		
		q.setHint("eclipselink.batch.type", "JOIN");//default

		Map<String, EdificioInst> toRet = new HashMap<String, EdificioInst>();
	    for(EdificioInst edificioInst : (List<EdificioInst>)q.getResultList()) {
	    	toRet.put(edificioInst.getClientid(), edificioInst);
	    }
	     
	    return toRet;
	}	
	
}