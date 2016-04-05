package com.rj.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rj.sys.domain.UserTest;
import com.rj.sys.domain.UserTestPK;

@Repository
public class UserTestDao extends GenericDao<UserTest> {
	
	public UserTestDao() {
		setClazz(UserTest.class);
	}
	
	public UserTest findById(UserTestPK id){
		UserTest userTest = entityManager.createQuery(
				"from UserTest ut where ut.user.id =:userId "
				+ "and ut.testType.id =:testId", UserTest.class)
				.setParameter("userId", Long.valueOf(id.getUserId()))
				.setParameter("testId", Long.valueOf(id.getTestTypeId()))
				.getSingleResult();
		return userTest;
	}
	
	public List<UserTest> findAllByUserId(Long userId){
		List<UserTest> userTests = entityManager.createQuery(
				"from UserTest ut where ut.user.id =:userId", UserTest.class)
				.setParameter("userId", userId)
				.getResultList();
		return userTests;
	}
}
