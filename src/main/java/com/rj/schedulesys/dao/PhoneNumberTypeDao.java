package com.rj.schedulesys.dao;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.PhoneNumberType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PhoneNumberTypeDao extends GenericDao<PhoneNumberType>{

	public PhoneNumberTypeDao() {
		setClazz(PhoneNumberType.class);
	}
	
	public PhoneNumberType findByName(String name){
		
		PhoneNumberType phoneNumberType = null;
		
		try{
			phoneNumberType = entityManager.createQuery(
					"from PhoneNumberType pnt where pnt.name =:name"
					, PhoneNumberType.class)
					.setParameter("name", name)
					.getSingleResult();	
		}catch(NoResultException e){
			log.warn("No phone number type found with name : {}", name);
		}
		
		return phoneNumberType;
	}
	
}
