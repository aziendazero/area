package it.tredi.auac.dao;

import it.tredi.auac.orm.entity.AreaDiscipline;
import it.tredi.auac.orm.entity.ClassificazioneUdoTempl;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityGraph;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository("classificazioneUdoTemplDao")
public class ClassificazioneUdoTemplDao extends AbstractJpaDao<ClassificazioneUdoTempl> {
	public ClassificazioneUdoTemplDao() {
		super();
		setClazz(ClassificazioneUdoTempl.class);
	}
	
	public ClassificazioneUdoTempl findById(BigDecimal idClassificazioneUdoTempl) {
		Query q = entityManager.createQuery("select c from ClassificazioneUdoTempl c where c.idClassificazioneUdoTempl = :idClassificazioneUdoTempl");
		q.setParameter("idClassificazioneUdoTempl", idClassificazioneUdoTempl);
	    return (ClassificazioneUdoTempl)q.getSingleResult();
	}

	public List<ClassificazioneUdoTempl> loadAllForShowFolderSearch() {
		EntityGraph<?> graph = entityManager.getEntityGraph("ClassificazioneUdoTempl.loadAllForShowFolderSearch");
		Query q = entityManager.createQuery("select t from ClassificazioneUdoTempl t order by t.nome");
		q.setHint("javax.persistence.fetchgraph", graph);
		return (List<ClassificazioneUdoTempl>)q.getResultList();
	}

}
