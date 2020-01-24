package it.tredi.auac.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class AbstractJpaDao<T extends Serializable> 
{
	private Class<T> clazz;

	//unitName e' il nome del bean che spring inietta utile per avere piu' EntityManager che puntano a DB differenti
	//@PersistenceContext(unitName="emf")
	@PersistenceContext
	EntityManager entityManager;

	public void setClazz(Class<T> clazzToSet) {
		this.clazz = clazzToSet;
	}

	public T findOne(String id) {
		return entityManager.find(clazz, id);
	}

	public List<?> findAll() {
		return entityManager.createQuery("select x from " + clazz.getSimpleName() + " x").getResultList();
	}

	public void save(T entity) {
		entityManager.persist(entity);
	}

	public T update(T entity) {
		return entityManager.merge(entity);
	}

	public void delete(T entity) {
		entityManager.remove(entity);
	}

	public void deleteById(String entityId) {
		T entity = findOne(entityId);
		delete(entity);
	}
	
	public Connection getConnection(){
		return (Connection)entityManager.unwrap(Connection.class);
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

}
