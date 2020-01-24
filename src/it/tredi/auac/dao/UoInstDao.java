package it.tredi.auac.dao;

import it.tredi.auac.orm.entity.UdoInst;
import it.tredi.auac.orm.entity.UoInst;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;


@Repository("uoInstDao")
public class UoInstDao extends AbstractJpaDao<UoInst> {

	public UoInstDao() {
		super();
		setClazz(UoInst.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<UdoInst> getUoInstByClientId(String clientId){
		Query q = entityManager.createQuery("select u from UoInst u where u.clientid = :clientid");
		q.setParameter("clientid", clientId);
	    return (List<UdoInst>)q.getResultList();
	}
	
	/*public List<UdoInst> getUoInstByDomandaInst(DomandaInst domandaInst){
		Query q = entityManager.createQuery("select u from UoInst u" + 
				" join u.udoModel u1" + 
				" join u.tipoUdoTempl t" +
				//" left join u1.disciplinaTempls dt"+
				" where u.domandaInst = :domandaInst");
		q.setParameter("domandaInst", domandaInst);
	    return (List<UdoInst>)q.getResultList();
	}*/
	
	public UoInst findByClientId(String clientId) {
		Query q = entityManager.createQuery("select u from UoInst u where u.clientid = :clientid");		
		q.setParameter("clientid", clientId);
	    return (UoInst)q.getSingleResult();
	}	

}