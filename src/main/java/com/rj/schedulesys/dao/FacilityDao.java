package com.rj.schedulesys.dao;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.Facility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class FacilityDao extends GenericDao<Facility> {
	
	public FacilityDao() {
		setClazz(Facility.class);
	}
	
	/**
	 * @param name
	 * @return
	 */
	public Facility findByName(String name){
		
		Facility facility = null;
		
		try{
			facility = entityManager.createQuery(
					"from Facility f where f.name =:name", Facility.class)
					.setParameter("name", name)
					.getSingleResult();
		}catch(NoResultException e){
			log.warn("No facility found with name {}", name);
		}
		
		return facility;
		
	}
	
	/**
	 * @param phoneNumber
	 * @return
	 */
	public Facility findByPhoneNumber(String phoneNumber){
		
		Facility facility = null;
		
		try{
			facility = entityManager.createQuery(
				"from Facility f where f.phoneNumber =:phoneNumber", Facility.class)
				.setParameter("phoneNumber", phoneNumber)
				.getSingleResult();
		}catch(NoResultException e){
			log.warn("No facility found with phone number : {}", phoneNumber);
		}
		
		return facility;
	}
	
}