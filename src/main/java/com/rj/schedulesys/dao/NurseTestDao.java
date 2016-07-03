package com.rj.schedulesys.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.NurseTest;
import com.rj.schedulesys.domain.NurseTestPK;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class NurseTestDao extends GenericDao<NurseTest>{

	public NurseTestDao() {
		setClazz(NurseTest.class);
	}
	
	/**
	 * @param nurseTestPK
	 * @return
	 */
	public NurseTest findOne(NurseTestPK nurseTestPK){
		NurseTest nurseTest = null;
		try{
			nurseTest = entityManager.createQuery(
					"from NurseTest nt where nt.nurse.id =:nurseId and nt.test.id =:testId"
					, NurseTest.class)
					.setParameter("nurseId", nurseTestPK.getNurseId())
					.setParameter("testId", nurseTestPK.getTestId())
					.getSingleResult();
		}catch(NoResultException e){
			log.warn("No NurseTest found with nurse id : {} and test id : {}"
					, nurseTestPK.getNurseId(), nurseTestPK.getTestId());
		}
		return nurseTest;
	}
	
	/**
	 * @param nurseId
	 * @return
	 */
	public List<NurseTest> findAllByNurse(Long nurseId){
		List<NurseTest> userTests = entityManager.createQuery(
				"from NurseTest nt where nt.nurse.id =:nurseId", NurseTest.class)
				.setParameter("nurseId", nurseId)
				.getResultList();
		return userTests;
	}
	
}
