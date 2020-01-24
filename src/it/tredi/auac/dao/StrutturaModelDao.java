package it.tredi.auac.dao;

import it.tredi.auac.orm.entity.StrutturaModel;

import org.springframework.stereotype.Repository;

@Repository("strutturaModelDao")
public class StrutturaModelDao extends AbstractJpaDao<StrutturaModel> {

	public StrutturaModelDao() {
		super();
		setClazz(StrutturaModel.class);
	}
	

}
