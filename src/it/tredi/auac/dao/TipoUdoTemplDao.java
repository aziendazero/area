package it.tredi.auac.dao;

import it.tredi.auac.orm.entity.TipoUdoTempl;

import java.util.List;

//import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository("tipoUdoTemplDao")
public class TipoUdoTemplDao extends AbstractJpaDao<TipoUdoTempl> {
	//private static final Logger log = Logger.getLogger(TipoUdoTemplDao.class);
	
	public TipoUdoTemplDao() {
		super();
		setClazz(TipoUdoTempl.class);
	}
	
	public List<?> findAllOrderByDescr() {
		return entityManager.createQuery("select t from TipoUdoTempl t order by t.descr").getResultList();
	}	
}
