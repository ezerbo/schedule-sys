package com.rj.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rj.sys.domain.License;

@Repository
public class LicenseDao extends GenericDao<License> {
	
	public LicenseDao() {
		setClazz(License.class);
	}
	
	public License findByNumber(String number){
		License license = entityManager.createQuery(
				"from License l where l.licenseNumber =:number"
				, License.class)
				.setParameter("number", number)
				.getSingleResult();
		return license;
	}
	
	public List<License> findAllByUserId(Long userId){
		List<License> licenses = entityManager.createQuery(
				"from License l where l.user.id =:userId", License.class)
				.setParameter("userId", userId)
				.getResultList();
		return licenses;
	}
	
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
