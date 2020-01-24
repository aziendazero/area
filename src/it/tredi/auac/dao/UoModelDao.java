package it.tredi.auac.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import it.tredi.auac.orm.entity.TitolareModel;
import it.tredi.auac.orm.entity.UoModel;
import it.tredi.auac.orm.entity.UoModelPK;

@Repository("uoModelDao")
public class UoModelDao extends AbstractJpaDao<UoModel> {
	private static final Logger log = Logger.getLogger(UoModelDao.class);
	
	public UoModelDao() {
		super();
		setClazz(UoModel.class);
	}

	//Deve restituire la lista delle uo del titolare e per ogni uo la lista delle udo con idUnivoco is not null  
	@SuppressWarnings("unchecked")
	public List<UoModel>findValidateByTitolare(TitolareModel titolareModel) {
		Query q = entityManager.createQuery("select u from UoModel u join u.titolareModel t" +
				" where t = :titolareModel");
				//" where t = :titolareModel and u.idUnivoco is not null");
		q.setParameter("titolareModel", titolareModel);
	    return (List<UoModel>)q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<UoModel>findValidateByTitolareEDenominazioneSenzaProcedimentiInCorso(TitolareModel titolareModel, String denominazione) {
		String queryDaEseguire = "select u from UoModel u join u.titolareModel t" + 
				" where t = :titolareModel";
		queryDaEseguire += " and (u.procedimentoInCorso = :procInCorso OR u.procedimentoInCorso IS NULL)";
		if(denominazione != null && denominazione.length() > 0){
			queryDaEseguire += " and lower(u.denominazione) like lower('%" + denominazione + "%')";
		}
		
		queryDaEseguire += " ORDER BY u.denominazione";
		
		Query q = entityManager.createQuery(queryDaEseguire);
		q.setParameter("titolareModel", titolareModel);
		q.setParameter("procInCorso", "N");
	    return (List<UoModel>)q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<UoModel>findValidateByTitolareEDenominazioneSenzaProcedimentiInCorsoOld(TitolareModel titolareModel, String denominazione) {
		String queryDaEseguire = "select u from UoModel u join u.titolareModel t" + 
				" where t = :titolareModel";
		queryDaEseguire += " and u.procedimentoInCorso = :procInCorso";
		if(denominazione != null && denominazione.length() > 0){
			queryDaEseguire += " and lower(u.denominazione) like lower('%" + denominazione + "%')";
		}
		
		queryDaEseguire += " ORDER BY u.denominazione";
		
		Query q = entityManager.createQuery(queryDaEseguire);
		q.setParameter("titolareModel", titolareModel);
		q.setParameter("procInCorso", "N");
	    return (List<UoModel>)q.getResultList();
	}

	public UoModel findByClientId(String clientId) {
		//Modifica per gestione organigramma 08/01/2016
		//il clientid ora e' dato da (provenienza + id.toString())
		//quindi occorre ricavare se arriva dal organigramma o dalla tabella UO_MODEL
		String provenienza = "ORGANIGRAMMA_TREE";
		//Modifica Organigramma aggiunta id univoco COD_NODO_REGIONALE
		//BigDecimal id = null;
		String id = null;
		if("UO_".equalsIgnoreCase(clientId.substring(0, 3))) {
			provenienza = "UO_MODEL";
			//Modifica Organigramma aggiunta id univoco COD_NODO_REGIONALE
			//id = BigDecimal.valueOf(Long.parseLong(clientId.substring(8)));
			id = clientId.substring(8);
		} else {
			//Modifica Organigramma aggiunta id univoco COD_NODO_REGIONALE
			//id = BigDecimal.valueOf(Long.parseLong(clientId.substring(17)));
			id = clientId.substring(17);
		}
		UoModelPK uoModelPK = new UoModelPK();
		uoModelPK.setProvenienza(provenienza);
		uoModelPK.setId(id);
		Query q = entityManager.createQuery("select u from UoModel u where u.id = :id");
		q.setParameter("id", uoModelPK);
		
		//Query q = entityManager.createQuery("select u from UoModel u where u.clientid = :clientid");
		//q.setParameter("clientid", clientId);
		
		
		//Pezza per evitare che cancellando una UoInst di una domanda riportata in BOZZA si ottenga una eccezione
		//cercando la relativa UoModel che in realta e' stata cancellata quando la domanda era in "PROCEDIMENTO AVVIATO"
	    try {
	    	return (UoModel)q.getSingleResult();
	    } catch(Exception ex) {
	    	log.warn("Impossibile trovare la UoModel con clientid: " + clientId, ex);
	    	return null;
	    }
	}
	
	private BigDecimal convertToBigDecimal(String id) {
		//return new BigDecimal(id);
		return BigDecimal.valueOf(Long.parseLong(id));
	}
	
	public void saveProcedimentoInCorso(UoModel entity) {
		//Salvo il valore attuale del ProcedimentoInCorso
		//try
	    //{
			if("UO_MODEL".equalsIgnoreCase(entity.getId().getProvenienza())) {
				//Converto String to BigDecimal
		        this.entityManager.createNamedQuery("updateUoModelProcedimentoInCorso", UoModel.class)
		        .setParameter(1, entity.getProcedimentoInCorso())
		        //.setParameter(2, entity.getId().getId())
		        //.setParameter(2, convertToBigDecimal(entity.getId().getId()))
		        .setParameter(2, entity.getId().getId())
		        .executeUpdate();

		        /*TypedQuery<UoModel> typedQuery = this.entityManager.createNamedQuery("updateUoModelProcedimentoInCorso", UoModel.class);
		        typedQuery.setParameter(1, entity.getProcedimentoInCorso());
		        typedQuery.setParameter(2, entity.getId().getId());
		        typedQuery.executeUpdate();*/
		        
				
				//Query q = this.entityManager.createNativeQuery("UPDATE UO_MODEL SET PROCEDIMENTO_IN_CORSO='S' WHERE ID_UO=40");
				/*Query q = this.entityManager.createNativeQuery("UPDATE UO_MODEL SET PROCEDIMENTO_IN_CORSO=? WHERE ID_UO=?");
		        q.setParameter(1, entity.getProcedimentoInCorso());
		        q.setParameter(2, entity.getId().getId());
		        q.executeUpdate();*/
		        
			} else {
		        int affected = this.entityManager.createNamedQuery("updateUoModelProcedimentoInCorsoOrganigramma", UoModel.class)
		        .setParameter(1, entity.getProcedimentoInCorso())
		        .setParameter(2, entity.getId().getId())
		        .executeUpdate();
		        //System.out.println("update affected: " + affected);
		        if(affected == 0) {
		        	//la UoModel non è ancora stat aggiunta alla tabella
			        affected = this.entityManager.createNamedQuery("insertUoModelProcedimentoInCorsoOrganigramma", UoModel.class)
			        .setParameter(1, entity.getId().getId())
			        .setParameter(2, entity.getProcedimentoInCorso())
			        .executeUpdate();
			        //System.out.println("affected: " + affected);
		        }
			}
	        //return true;
	    /*}
	    catch (Exception e)
	    {
	    	log.error("Impossibile aggiornare il ProcedimentoInCorso " + entity.getId().getClientid(), e);
	        throw e;
	    }*/
	}

//	public void changeProcedimentoInCorso(String procedimentoInCorso, UoModel uoModel) {
//		//Cambio il valore del ProcedimentoInCorso
//		//try
//	    //{
//			if("UO_MODEL".equalsIgnoreCase(uoModel.getId().getProvenienza())) {
//		        this.entityManager.createNamedQuery("updateUoModelProcedimentoInCorso", UoModel.class)
//		        .setParameter(1, uoModel.getProcedimentoInCorso())
//		        //.setParameter(2, uoModel.getId().getId())
//		        .setParameter(2, convertToBigDecimal(uoModel.getId().getId()))
//		        .executeUpdate();
//
//		        /*TypedQuery<UoModel> typedQuery = this.entityManager.createNamedQuery("updateUoModelProcedimentoInCorso", UoModel.class);
//		        typedQuery.setParameter(1, entity.getProcedimentoInCorso());
//		        typedQuery.setParameter(2, entity.getId().getId());
//		        typedQuery.executeUpdate();*/
//		        
//				
//				//Query q = this.entityManager.createNativeQuery("UPDATE UO_MODEL SET PROCEDIMENTO_IN_CORSO='S' WHERE ID_UO=40");
//				/*Query q = this.entityManager.createNativeQuery("UPDATE UO_MODEL SET PROCEDIMENTO_IN_CORSO=? WHERE ID_UO=?");
//		        q.setParameter(1, entity.getProcedimentoInCorso());
//		        q.setParameter(2, entity.getId().getId());
//		        q.executeUpdate();*/
//		        
//			} else {
//		        this.entityManager.createNamedQuery("updateUoModelProcedimentoInCorsoOrganigramma", UoModel.class)
//		        .setParameter(1, uoModel.getProcedimentoInCorso())
//		        .setParameter(2, uoModel.getId().getId())
//		        .executeUpdate();
//			}
//	        //return true;
//	    /*}
//	    catch (Exception e)
//	    {
//	    	log.error("Impossibile aggiornare il ProcedimentoInCorso " + entity.getId().getClientid(), e);
//	        throw e;
//	    }*/
//	}
}
