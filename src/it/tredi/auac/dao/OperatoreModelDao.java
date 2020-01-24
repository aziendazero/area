package it.tredi.auac.dao;

import it.tredi.auac.orm.entity.OperatoreModel;
import it.tredi.auac.orm.entity.UtenteModel;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository("operatoreModelDao")
public class OperatoreModelDao extends AbstractJpaDao<OperatoreModel> {

	public OperatoreModelDao() {
		super();
		setClazz(OperatoreModel.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<OperatoreModel> findByUtenteModel(UtenteModel utenteModel) {
	    Query q = entityManager.createQuery("select o from OperatoreModel o where o.utenteModel = :utenteModel and o.ruolo!='VERIFICATORE'");
	    q.setParameter("utenteModel", utenteModel);
	    return (List<OperatoreModel>)q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<OperatoreModel> findByUtenteModelTitolareModel(UtenteModel utenteModel, String titolareClientid) {
		//getTitolareModel
	    Query q = entityManager.createQuery("select o from OperatoreModel o where o.utenteModel = :utenteModel and o.titolareModel.clientid = :titolareClientid");
	    q.setParameter("utenteModel", utenteModel);
	    q.setParameter("titolareClientid", titolareClientid);
	    return (List<OperatoreModel>)q.getResultList();
	}
}
