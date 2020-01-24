package it.tredi.auac.dao;

import java.util.List;

import javax.persistence.EntityGraph;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import it.tredi.auac.orm.entity.AreaDiscipline;


@Repository("areaDisciplineDao")
public class AreaDisciplineDao extends AbstractJpaDao<AreaDiscipline> {
	
	public AreaDisciplineDao() {
		super();
		setClazz(AreaDiscipline.class);
	}

	public List<AreaDiscipline> findAllForList() {
		EntityGraph<?> areaDisciplineGraph = entityManager.getEntityGraph("AreaDiscipline.loadAllForList");
		Query q = entityManager.createQuery("select t from AreaDiscipline t order by t.denominazione");
		q.setHint("javax.persistence.fetchgraph", areaDisciplineGraph);
		return (List<AreaDiscipline>)q.getResultList();
	}

}