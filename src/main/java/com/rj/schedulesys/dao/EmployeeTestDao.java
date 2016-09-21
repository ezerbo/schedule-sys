package com.rj.schedulesys.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.EmployeeTest;
import com.rj.schedulesys.domain.EmployeTestPK;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class EmployeeTestDao extends GenericDao<EmployeeTest>{

	public EmployeeTestDao() {
		setClazz(EmployeeTest.class);
	}
	
	/**
	 * @param nurseTestPK
	 * @return
	 */
	public EmployeeTest findOne(EmployeTestPK nurseTestPK){
		EmployeeTest nurseTest = null;
		try{
			nurseTest = entityManager.createQuery(
					"from EmployeeTest et where et.employee.id =:employeeId and et.test.id =:testId"
					, EmployeeTest.class)
					.setParameter("employeeId", nurseTestPK.getEmployeeId())
					.setParameter("testId", nurseTestPK.getTestId())
					.getSingleResult();
		}catch(NoResultException e){
			log.warn("No EmployeeTest found with employee id : {} and test id : {}"
					, nurseTestPK.getEmployeeId(), nurseTestPK.getTestId());
		}
		return nurseTest;
	}
	
	/**
	 * @param employeeId
	 * @return
	 */
	public List<EmployeeTest> findAllByNurse(Long employeeId){
		List<EmployeeTest> employeeTests = entityManager.createQuery(
				"from EmployeeTest et where et.employee.id =:employeeId", EmployeeTest.class)
				.setParameter("employeeId", employeeId)
				.getResultList();
		return employeeTests;
	}
	
}