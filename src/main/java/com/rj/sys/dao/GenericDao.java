package com.rj.sys.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ezerbo
 */
public class GenericDao<T> {
	
	private Class<T> clazz;
	
	@PersistenceContext @Setter @Getter
	protected EntityManager entityManager;

	public final void setClazz(final Class<T> clazzToSet) {
		this.clazz = clazzToSet;
	}

	public T findOne(final Long id) {
		return entityManager.find(clazz, id);
	}

	public List<T> findAll() {
		return entityManager.createQuery("from " + clazz.getName(), clazz)
				.getResultList();
	}

	public void create(final T entity) {
		entityManager.persist(entity);
	}

	public T merge(final T entity) {
		return entityManager.merge(entity);
	}

	public void delete(final T entity) {
		entityManager.remove(entity);
	}
	
	public void referesh(final T entity){
		entityManager.refresh(entity);
	}
}