package com.rj.schedulesys.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.PhoneNumber;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PhoneNumberDao extends GenericDao<PhoneNumber> {

	public PhoneNumberDao() {
		setClazz(PhoneNumber.class);
	}
	
	/**
	 * @param employeeId
	 * @return phone numbers of employee with id 'employeeId'
	 */
	public List<PhoneNumber> findByEmployee(Long employeeId){
		List<PhoneNumber> phoneNumbers = entityManager.createQuery(
				"from PhoneNumber pn where pn.employee.id =:employeeId", PhoneNumber.class)
				.setParameter("employeeId", employeeId)
				.getResultList();
		return phoneNumbers;
		
	}
	
	/**
	 * @param number
	 * @return
	 */
	public PhoneNumber findByNumber(String number){
		PhoneNumber phoneNumber = null;
		try{
			phoneNumber = entityManager.createQuery(
					"from PhoneNumber pn where pn.number =:number", PhoneNumber.class)
			.setParameter("number", number)
			.getSingleResult();
		}catch(NoResultException e){
			log.warn("No phone number found with number : {}", number);
		}
		return phoneNumber;
	}
	
	/**
	 * @param employeeId
	 * @param labelName
	 * @return
	 */
	public PhoneNumber findByEmployeeAndLabel(Long employeeId, String labelName){
		PhoneNumber phoneNumber = null;
		try{
			phoneNumber = entityManager.createQuery(
					"from PhoneNumber pn where pn.employee.id =:employeeId and pn.phoneNumberLabel.name =:labelName", PhoneNumber.class)
					.setParameter("employeeId", employeeId)
					.setParameter("labelName", labelName)
					.getSingleResult();
		}catch(NoResultException e){
			log.warn("No phone number found with label {} for employee with id : {}", labelName, employeeId);
		}
		return phoneNumber;
	}
	
	/**
	 * @param employeeId
	 * @param numberId
	 * @return
	 */
	public PhoneNumber findByEmployeeIdAndNumberId(Long employeeId, Long numberId){
		PhoneNumber phoneNumber = null;
		try{
			phoneNumber = entityManager.createQuery(
					"from PhoneNumber pn where pn.id =:numberId and pn.employee.id =:employeeId"
					, PhoneNumber.class)
					.setParameter("numberId", numberId)
					.setParameter("employeeId", employeeId)
					.getSingleResult();
		}catch(NoResultException e){
			log.warn("No phone number found with id : {} for employee with id : {}", numberId, employeeId);
		}
		return phoneNumber;
	}
	
	/**
	 * @param nurseId
	 * @param numberId
	 * @return
	 */
	public PhoneNumber findByNurseIdAndNumberId(Long nurseId, Long numberId){
		PhoneNumber phoneNumber = null;
		try{
			phoneNumber = entityManager.createQuery(
					"from PhoneNumber pn where pn.id =:numberId and pn.employee.id =:nurseId"
					, PhoneNumber.class)
					.setParameter("numberId", numberId)
					.setParameter("nurseId", nurseId)
					.getSingleResult();
		}catch(NoResultException e){
			log.warn("No phone number found with id : {} for employee with id : {}", numberId, nurseId);
		}
		return phoneNumber;
	}
	
	/**
	 * @param employeeId
	 * @param numberId
	 * @return
	 */
	public PhoneNumber findByCareGiverIdAndNumberId(Long careGiverId, Long numberId){
		PhoneNumber phoneNumber = null;
		try{
			phoneNumber = entityManager.createQuery(
					"from PhoneNumber pn where pn.id =:numberId and pn.employee.careGiver.id =:careGiverId"
					, PhoneNumber.class)
					.setParameter("numberId", numberId)
					.setParameter("careGiverId", careGiverId)
					.getSingleResult();
		}catch(NoResultException e){
			log.warn("No phone number found with id : {} for employee with id : {}", numberId, careGiverId);
		}
		return phoneNumber;
	}
}