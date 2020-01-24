package it.tredi.auac.dao;

import it.tredi.auac.orm.entity.TipoUdo22Templ;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Query;

//import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository("tipoUdo22TemplDao")
public class TipoUdo22TemplDao extends AbstractJpaDao<TipoUdo22Templ> {
	//private static final Logger log = Logger.getLogger(TipoUdo22TemplDao.class);
	
	public TipoUdo22TemplDao() {
		super();
		setClazz(TipoUdo22Templ.class);
	}
	
	/*public List<TipoUdo22Templ> getTipoUdo22TemplByAmbitoClientId(String ambitoClientId){
		Query q = entityManager.createQuery("select tu from TipoUdo22Templ tu where tu.clientid IN (SELECT ID_TIPO_22_FK FROM BIND_TIPO_22_AMBITO WHERE ID_AMBITO_FK=:ambitoClientId)");
		q.setParameter("clientid", ambitoClientId);
	    return (List<TipoUdo22Templ>)q.getResultList();
	}*/
	
	@SuppressWarnings("unchecked")
	public List<String> getTipoUdo22TemplClientIdByAmbitoClientId(String ambitoClientId){
		Query q = entityManager.createNativeQuery("SELECT ID_TIPO_22_FK  FROM BIND_TIPO_22_AMBITO WHERE ID_AMBITO_FK=?");
	    q.setParameter(1, ambitoClientId);
	    List<String> values = (List<String>)q.getResultList();
	    return values;
	}
	
	public TipoUdo22Templ findById(BigDecimal id) {
		Query q = entityManager.createQuery("select u from TipoUdo22Templ u where u.idTipo = :idTipo");
		q.setParameter("idTipo", id);
	    return (TipoUdo22Templ)q.getSingleResult();
	}
	

	/*
	@SuppressWarnings("unchecked")
	public List<String> getDomandeClientIdPerVerificatore(UtenteModel utenteModel){
		Query q = entityManager.createNativeQuery("SELECT DISTINCT DI.CLIENTID" + 
											" FROM DOMANDA_INST DI" +
											" INNER JOIN BIND_DOMANDA_VERIFICATORE BDV ON BDV.ID_DOMANDA_INST_FK=DI.CLIENTID" +
											" WHERE BDV.ID_UTENTE_MODEL_FK=? AND DI.STATO='" + DomandaInstDao.STATO_PROCEDIMENTO_IN_VALUTAZIONE + "'");
	    q.setParameter(1, utenteModel.getClientid());
	    List<String> values = (List<String>)q.getResultList();
	    return values;
	}

	 */
	
}
