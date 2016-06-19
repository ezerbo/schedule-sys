package com.rj.schedulesys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.NurseTest;
import com.rj.schedulesys.domain.NurseTestPK;

@Repository
public class UserTestDao extends GenericDao<NurseTest> {
	
	public UserTestDao() {
		setClazz(NurseTest.class);
	}
	
	public NurseTest findById(NurseTestPK id){
		NurseTest userTest = entityManager.createQuery(
				"from UserTest ut where ut.user.id =:userId "
				+ "and ut.testType.id =:testId", NurseTest.class)
				.setParameter("userId", Long.valueOf(id.getNurseId()))
				.setParameter("testId", Long.valueOf(id.getTestId()))
				.getSingleResult();
		return userTest;
	}
	
	public List<NurseTest> findAllByUserId(Long userId){
		List<NurseTest> userTests = entityManager.createQuery(
				"from UserTest ut where ut.user.id =:userId", NurseTest.class)
				.setParameter("userId", userId)
				.getResultList();
		return userTests;
	}
}
