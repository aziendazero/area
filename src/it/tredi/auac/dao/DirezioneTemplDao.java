package it.tredi.auac.dao;

import it.tredi.auac.orm.entity.DirezioneTempl;

import org.springframework.stereotype.Repository;

@Repository("direzioneTemplDao")
public class DirezioneTemplDao extends AbstractJpaDao<DirezioneTempl> {

	public DirezioneTemplDao() {
		super();
		setClazz(DirezioneTempl.class);
	}
	
}

