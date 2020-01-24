package it.tredi.auac.dao;

import it.tredi.auac.orm.entity.TipoProcTempl;

import java.math.BigDecimal;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository("tipoProcTemplDao")
public class TipoProcTemplDao extends AbstractJpaDao<TipoProcTempl> {

	public TipoProcTemplDao() {
		super();
		setClazz(TipoProcTempl.class);
	}
	
	public TipoProcTempl findById(BigDecimal id) {
		Query q = entityManager.createQuery("select u from TipoProcTempl u where u.idTipo = :idTipo");
		q.setParameter("idTipo", id);
	    return (TipoProcTempl)q.getSingleResult();
	}
}

