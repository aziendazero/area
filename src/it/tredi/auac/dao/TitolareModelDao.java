package it.tredi.auac.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityGraph;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import it.tredi.auac.TitolareOrderTypeEnum;
import it.tredi.auac.orm.entity.TitolareModel;
import it.tredi.auac.orm.entity.UtenteModel;

@Repository("titolareModelDao")
public class TitolareModelDao extends AbstractJpaDao<TitolareModel> {
	
	public TitolareModelDao() {
		super();
		setClazz(TitolareModel.class);
	}

	public TitolareModel findByClientId(String clientId) {
		Query q = entityManager.createQuery("select t from TitolareModel t where t.clientid = :clientid");
		q.setParameter("clientid", clientId);
	    return (TitolareModel)q.getSingleResult();
	}

	public TitolareModel findByUtenteModel(UtenteModel utenteModel) {
	    Query q = entityManager.createQuery("select t from TitolareModel t where t.utenteModel = :utenteModel");
	    q.setParameter("utenteModel", utenteModel);
	    return (TitolareModel)q.getSingleResult();
	}

	public List<TitolareModel> findAllForList() {
		EntityGraph<?> titolareModelGraph = entityManager.getEntityGraph("TitolareModel.loadAllForList");
		Query q = entityManager.createQuery("select t from TitolareModel t");
		q.setHint("javax.persistence.fetchgraph", titolareModelGraph);
		return (List<TitolareModel>)q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	private List<String> getTitolariAttiClientIdPerUfficioDiAppartenenza(String utenteModelClientid){
		Query q = entityManager.createNativeQuery("SELECT DISTINCT TM.CLIENTID " + 
				"FROM TITOLARE_MODEL TM " + 
				"INNER JOIN ATTO_MODEL AM ON AM.ID_TITOLARE_FK=TM.CLIENTID " + 
				"INNER JOIN ( " + 
				"  SELECT UDM.ID_DIREZIONE_TEMPL, BUTD.ID_TIPO_PROC_FK " + 
				"  FROM BIND_UFF_TIPPROC_VISIB_DOM BUTD " + 
				"  INNER JOIN BIND_UTENTE_DIR_UFF BUDU " + 
				"    ON BUDU.ID_UFFICIO_TEMPL=BUTD.ID_UFFICIO_FK " + 
				"  INNER JOIN UTENTE_DIREZIONE_MODEL UDM " + 
				"    ON UDM.CLIENTID=BUDU.ID_UTENTE_DIREZIONE_MODEL " + 
				"  WHERE UDM.ID_UTENTE_MODEL=?) DIR_TPROC_FOR_USER " + 
				"  ON DIR_TPROC_FOR_USER.ID_DIREZIONE_TEMPL=TM.ID_DIREZIONE_FK AND DIR_TPROC_FOR_USER.ID_TIPO_PROC_FK=AM.ID_TIPO_PROC_FK"
				);
	    q.setParameter(1, utenteModelClientid);
	    List<String> values = (List<String>)q.getResultList();
	    return values;
	}
	
	//public List<TitolareModel> findForTitolariList(String subStringRagSoc, String subStringPartitaIva, String direzioneTemplClientid, TitolareOrderTypeEnum titolareOrderTypeEnum) {
	public List<TitolareModel> findForTitolariList(String subStringRagSoc, String subStringPartitaIva, String direzioneTemplClientid, TitolareOrderTypeEnum titolareOrderTypeEnum, String utenteModelClientid) {
		EntityGraph<?> titolareModelGraph = entityManager.getEntityGraph("TitolareModel.titolariAtti");
		List<String> listTitClientid = getTitolariAttiClientIdPerUfficioDiAppartenenza(utenteModelClientid);
		if(listTitClientid.isEmpty())
			return new ArrayList<TitolareModel>();
		Query q = null;
		String query = "select t from TitolareModel t";
		String where = " where t.clientid IN :listTitClientid";
		boolean writeWhere = true;
		
		if(subStringRagSoc != null && subStringRagSoc.length() > 0) {
			subStringRagSoc = subStringRagSoc.toUpperCase().replace("*", "%");
			where += " and UPPER(t.ragSoc) like :subStringRagSoc";
			writeWhere = true;
		}
		if(subStringPartitaIva != null && subStringPartitaIva.length() > 0) {
			subStringPartitaIva = subStringPartitaIva.toUpperCase().replace("*", "%");
			if(writeWhere)
				where += " and UPPER(t.piva) like :subStringPartitaIva";
			else
				where += " where UPPER(t.piva) like :subStringPartitaIva";
			writeWhere = true;
		}
		if(direzioneTemplClientid != null && direzioneTemplClientid.length() > 0) {
			query += " left join t.direzioneTempl d";
			if(writeWhere)
				where += " and d.clientid = :direzioneTemplClientid";
			else
				where += " where d.clientid = :direzioneTemplClientid";
			writeWhere = true;
		} else {
			if(titolareOrderTypeEnum == TitolareOrderTypeEnum.DirezioneCrescente || titolareOrderTypeEnum == TitolareOrderTypeEnum.DirezioneDecrescente)
				query += " left join t.direzioneTempl d";
		}
		
		query += where + " order by " + getOrder("t", "d", titolareOrderTypeEnum);
		
		q = entityManager.createQuery(query);
		q.setParameter("listTitClientid", listTitClientid);
		if(subStringRagSoc != null && subStringRagSoc.length() > 0)
			q.setParameter("subStringRagSoc", subStringRagSoc);
		if(subStringPartitaIva != null && subStringPartitaIva.length() > 0)
			q.setParameter("subStringPartitaIva", subStringPartitaIva);
		if(direzioneTemplClientid != null && direzioneTemplClientid.length() > 0)
			q.setParameter("direzioneTemplClientid", direzioneTemplClientid);

		q.setHint("javax.persistence.fetchgraph", titolareModelGraph);
		return (List<TitolareModel>)q.getResultList();
	}
	
	public boolean checkPartIvaCfisc(String partIva, String cfisc){
		Query q = entityManager.createQuery("select t from TitolareModel t where t.cfisc = :cfisc or t.piva = :partiva");
		q.setParameter("cfisc", cfisc).setParameter("partiva", partIva);
		
		 if (q.getResultList().isEmpty())
			 return false;
		 
		return true;
	}
	
	private String getOrder(String titolareTable, String direzioneTable, TitolareOrderTypeEnum titolareOrderTypeEnum) {
		switch (titolareOrderTypeEnum) {
		case RagioneSocialeCrescente:
			return titolareTable + ".ragSoc ASC";
		case RagioneSocialeDecrescente:
			return titolareTable + ".ragSoc DESC";

		case PartitaIvaCrescente:
			return titolareTable + ".piva ASC";
		case PartitaIvaDecrescente:
			return titolareTable + ".piva DESC";

		case DirezioneCrescente:
			return direzioneTable + ".nome ASC";
		case DirezioneDecrescente:
			return direzioneTable + ".nome DESC";
		}
		return "ragSoc ASC";
	}
	
}
