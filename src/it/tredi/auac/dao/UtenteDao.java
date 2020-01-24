package it.tredi.auac.dao;

import it.tredi.auac.orm.entity.UtenteModel;

import java.security.MessageDigest;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository("utenteDao")
public class UtenteDao extends AbstractJpaDao<UtenteModel> {

	public UtenteDao() {
		super();
		setClazz(UtenteModel.class);
	}
	
	@SuppressWarnings("unchecked")
	public UtenteModel checkUser(String login, String password, boolean encodePassword) throws Exception {
		String encodedPassword = "";
		if (password != null) {
			if (encodePassword) {
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte []encoded = md.digest(password.getBytes());
				encodedPassword = it.highwaytech.util.Text.bytesToHex(encoded).toUpperCase();				
			}
			else {
				encodedPassword = password.toUpperCase();
			}
		}
		String queryS = "select u from UtenteModel u where u.username = :login";
		if (password != null) 
			queryS += " and upper(u.passwd) = :password";
	    Query q = entityManager.createQuery(queryS);
	    q.setParameter("login", login);
	    if (password != null) 
	    	q.setParameter("password", encodedPassword);
	    List<UtenteModel> usersL = q.getResultList(); 
	    
	    return usersL.size() == 0? null : usersL.get(0);
	}

	@SuppressWarnings("unchecked")
	public UtenteModel checkUserByUsernameCas(String login) throws Exception {
		String queryS = "select u from UtenteModel u where u.usernameCas = :login";
	    Query q = entityManager.createQuery(queryS);
	    q.setParameter("login", login);
	    List<UtenteModel> usersL = q.getResultList(); 
	    
	    return usersL.size() == 0? null : usersL.get(0);
	}

	public UtenteModel getByUserName(String username) throws Exception {
		String queryS = "select u from UtenteModel u where u.username = :username";
	    Query q = entityManager.createQuery(queryS);
	    q.setParameter("username", username);
	    List<UtenteModel> usersL = q.getResultList(); 
	    
	    return usersL.size() == 0? null : usersL.get(0);
	}

	public List<UtenteModel> getListByUserNames(Set<String> usernames) throws Exception {
		String queryS = "select u from UtenteModel u where u.username IN :usernames";
	    Query q = entityManager.createQuery(queryS);
	    q.setParameter("usernames", usernames);
	    List<UtenteModel> usersL = q.getResultList(); 
	    
	    return usersL;
	}

	public List<UtenteModel> getListByUserNameLike(String username) throws Exception {
		String queryS = "select u from UtenteModel u where LOWER(u.username) LIKE :username";
	    Query q = entityManager.createQuery(queryS);
	    q.setParameter("username", "%" + username.toLowerCase() + "%");
	    List<UtenteModel> usersL = q.getResultList(); 
	    
	    return usersL;
	}

	public List<UtenteModel> getListByUserNameCasLike(String username) throws Exception {
		String queryS = "select u from UtenteModel u where LOWER(u.usernameCas) LIKE :username";
	    Query q = entityManager.createQuery(queryS);
	    q.setParameter("username", "%" + username.toLowerCase() + "%");
	    List<UtenteModel> usersL = q.getResultList(); 
	    
	    return usersL;
	}
}
