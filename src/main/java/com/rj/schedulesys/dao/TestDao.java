package com.rj.schedulesys.dao;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.Test;

@Repository
public class TestDao extends GenericDao<Test> {
	
	public TestDao() {
		setClazz(Test.class);
	}
	
	public Test findByName(String name){
		Test test = entityManager.createQuery(
				"from Test t where t.testName =:name", Test.class)
				.setParameter("name", name)
				.getSingleResult();
		return test;
	}
}
