package it.tredi.auac.dao;

import it.tredi.auac.orm.entity.AttoModel;
import it.tredi.auac.orm.entity.TipoProcTempl;
import it.tredi.auac.orm.entity.TitolareModel;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository("attoModelDao")
public class AttoModelDao extends AbstractJpaDao<AttoModel> {

	public AttoModelDao() {
		super();
		setClazz(AttoModel.class);
	}
	
	public List<AttoModel> getAttiModelForUdoModelClientids(TipoProcTempl tipoProcTempl, TitolareModel titolareModel, List<String> udoModelClientids) {
		//List<String> udoInstClientids = getUdoInstClientIdForCompareByDomandaInst(domandaClientid, titolareDomandaClientid);
		if(udoModelClientids.size() > 0) {		
			Query q = entityManager.createQuery("select distinct a from AttoModel a" + 
					" join a.tipoProcTempl tp" +
					" join a.titolareModel tm" +
					" join a.udoModels um" +
					" where tp = :tipoProcTempl" +
					" and tm = :titolareModel" +
					" and um.clientid IN :udoModelClientids" +
					" ");
			q.setParameter("tipoProcTempl", tipoProcTempl);
			q.setParameter("titolareModel", titolareModel);
			q.setParameter("udoModelClientids", udoModelClientids);
		    return (List<AttoModel>)q.getResultList();
		}
		return null;
		
	}
}