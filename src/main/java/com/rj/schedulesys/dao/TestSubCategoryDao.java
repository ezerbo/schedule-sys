package com.rj.schedulesys.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.TestSubCategory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class TestSubCategoryDao extends GenericDao<TestSubCategory> {

	public TestSubCategoryDao() {
		setClazz(TestSubCategory.class);
	}
	
	/**
	 * @param name
	 * @param testName
	 * @return
	 */
	public TestSubCategory findByNameAndTestName(String name, String testName){
		
		TestSubCategory testSubCategory = null;
		
		try{
			testSubCategory = entityManager.createQuery("from TestSubCategory tsc where tsc.name =:name"
					+ " and tsc.test.name =:testName", TestSubCategory.class)
					.setParameter("name", name)
					.setParameter("testName", testName)
					.getSingleResult();
		}catch(NoResultException e){
			log.warn("No test sub category with name : {} found for test with name : {}", name, testName);
		}
		
		return testSubCategory;
	}
	
	/**
	 * @param name
	 * @return
	 */
	public TestSubCategory findByName(String name){
		
		TestSubCategory testSubCategory = null;
		
		try{
			testSubCategory = entityManager.createQuery("from TestSubCategory tsc where tsc.name =:name"
					, TestSubCategory.class)
					.setParameter("name", name)
					.getSingleResult();
		}catch(NoResultException e){
			log.warn("No test sub category found with name : {}", name);
		}
		
		return testSubCategory;
	}
	
	/**
	 * @param testId
	 * @return
	 */
	public List<TestSubCategory> findAllByTest(Long testId){
		
		List<TestSubCategory> testSubCategories = entityManager.createQuery(
				"from TestSubCategory tsc where tsc.test.id =:id"
				, TestSubCategory.class)
				.setParameter("id", testId)
				.getResultList();
		
		return testSubCategories;
	}
}