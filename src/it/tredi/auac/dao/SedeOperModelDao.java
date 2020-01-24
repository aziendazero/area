package it.tredi.auac.dao;

import it.tredi.auac.orm.entity.SedeOperModel;

import org.springframework.stereotype.Repository;

@Repository("sedeOperModelDao")
public class SedeOperModelDao extends AbstractJpaDao<SedeOperModel> {
	public SedeOperModelDao() {
		super();
		setClazz(SedeOperModel.class);
	}
	
}
