package com.rj.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rj.sys.domain.TestType;

@Repository
public class TestTypeDao extends GenericDao<TestType>{
	
	public TestTypeDao() {
		setClazz(TestType.class);
	}
	
	public TestType findByName(String name){
		TestType testType = entityManager.createQuery(
				"from TestType tt where tt.typeName =:name"
				, TestType.class)
				.setParameter("name", name)
				.getSingleResult();
		return testType;
	}
	
	public TestType findByNameAndTestId(String name, Long testId){
		TestType testType = entityManager.createQuery(
				"from TestType tt where tt.typeName =:name "
				+ "and tt.test.id =:testId"
				, TestType.class)
				.setParameter("name", name)
				.setParameter("testId", testId)
				.getSingleResult();
		return testType;
	}
	
	public TestType findByIdAndTestId(Long id, Long testId){
		TestType testType = entityManager.createQuery(
				"from TestType tt where tt.id =:id "
				+ "and tt.test.id =:testId"
				, TestType.class)
				.setParameter("id", id)
				.setParameter("testId", testId)
				.getSingleResult();
		return testType;
	}
	
	public List<TestType> findAllByTestId(Long testId){
		List<TestType> testTypes = entityManager.createQuery(
				"from TestType tt where tt.test.id =:testId"
				,TestType.class)
				.setParameter("testId", testId)
				.getResultList();
		return testTypes;
	}
}
