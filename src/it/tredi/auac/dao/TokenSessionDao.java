package it.tredi.auac.dao;

import it.tredi.auac.orm.entity.TokenSession;

import org.springframework.stereotype.Repository;

@Repository("tokenSessionDao")
public class TokenSessionDao extends AbstractJpaDao<TokenSession> {

	public TokenSessionDao() {
		super();
		setClazz(TokenSession.class);
	}
	
}