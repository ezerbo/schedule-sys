package com.rj.schedulesys.dao;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class TestDao extends GenericDao<Test> {

	public TestDao() {
		setClazz(Test.class);
	}

	/**
	 * @param name
	 * @return
	 */
	public Test findByName(String name) {

		Test test = null;
		
		try {
			test = entityManager.createQuery("from Test t where t.name =:name", Test.class)
					.setParameter("name", name)
					.getSingleResult();
		} catch (NoResultException e) {
			log.warn("No test found with name : {}", name);
		}
		
		return test;
	}
}
