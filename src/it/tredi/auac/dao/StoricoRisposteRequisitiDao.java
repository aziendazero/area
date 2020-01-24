package it.tredi.auac.dao;

import it.tredi.auac.orm.entity.StoricoRisposteRequisiti;

import org.springframework.stereotype.Repository;

@Repository("storicoRisposteRequisitiDao")
public class StoricoRisposteRequisitiDao extends AbstractJpaDao<StoricoRisposteRequisiti> {
	
	public StoricoRisposteRequisitiDao() {
		super();
		setClazz(StoricoRisposteRequisiti.class);
	}
	
}
