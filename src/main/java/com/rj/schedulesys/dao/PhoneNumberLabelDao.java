package com.rj.schedulesys.dao;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.PhoneNumberLabel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PhoneNumberLabelDao extends GenericDao<PhoneNumberLabel> {
	
	public PhoneNumberLabelDao() {
		setClazz(PhoneNumberLabel.class);
	}
	
	/**
	 * @param name
	 * @return
	 */
	public PhoneNumberLabel findByName(String name){
		
		PhoneNumberLabel phoneNumberLabel = null;
		
		try{
			phoneNumberLabel = entityManager.createQuery(
					"from PhoneNumberLabel pnl where pnl.name =:name"
					, PhoneNumberLabel.class)
					.setParameter("name", name)
					.getSingleResult();
		}catch(NoResultException e){
			log.warn("No phone number label find with name : {}", name);
		}
		
		return phoneNumberLabel;
	}
}