package it.tredi.auac.dao;

import it.tredi.auac.orm.entity.AttoInst;
import it.tredi.auac.orm.entity.RequisitoInst;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository("attoInstDao")
public class AttoInstDao extends AbstractJpaDao<AttoInst> {

	public AttoInstDao() {
		super();
		setClazz(AttoInst.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<AttoInst> findAllAttiByDomandaInst(String domandaClientId) {
		Query q = entityManager.createNamedQuery("AttoInst.findAllAttiByDomandaInst", RequisitoInst.class);
		
		q.setParameter("domandaClientId", domandaClientId);
		//setHintRequisitoInst(q);
		return (List<AttoInst>)q.getResultList();
	}

}