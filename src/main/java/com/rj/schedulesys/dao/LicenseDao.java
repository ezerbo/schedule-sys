package com.rj.schedulesys.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.License;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class LicenseDao extends GenericDao<License> {
	
	public LicenseDao() {
		setClazz(License.class);
	}
	
	/**
	 * @param number
	 * @return
	 */
	public License findByNumber(String number){
		
		License license = null;
		
		try{
			license = entityManager.createQuery(
					"from License l where l.number =:number"
					, License.class)
					.setParameter("number", number)
					.getSingleResult();
		}catch(NoResultException e){
			log.error("No license found with number : {}", number);
		}
		
		return license;
	}
	
	/**
	 * @param nurseId
	 * @return
	 */
	public List<License> findAllByNurse(Long nurseId){
		List<License> licenses = entityManager.createQuery(
				"from License l where l.nurse.id =:nurseId", License.class)
				.setParameter("nurseId", nurseId)
				.getResultList();
		return licenses;
	}
	
	/**
	 * @param id
	 * @param userId
	 * @return
	 */
	public License findByIdAndUserId(Long id, Long userId){
		License license = entityManager.createQuery(
				"from License l where l.id =:id "
				+ " and l.user.id =:userId"
				, License.class)
				.setParameter("id", id)
				.setParameter("userId", userId)
				.getSingleResult();
		return license;
	}
}
