package it.tredi.auac.dao;

import org.springframework.stereotype.Repository;

import it.tredi.auac.orm.entity.TipoAtto;

@Repository("tipoAttoDao")
public class TipoAttoDao extends AbstractJpaDao<TipoAtto> {

	public TipoAttoDao() {
		super();
		setClazz(TipoAtto.class);
	}
	
}