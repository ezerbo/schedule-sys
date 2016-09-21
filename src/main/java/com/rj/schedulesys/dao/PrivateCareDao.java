package com.rj.schedulesys.dao;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.PrivateCare;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PrivateCareDao extends GenericDao<PrivateCare>{

	public PrivateCareDao() {
		setClazz(PrivateCare.class);
	}

	public PrivateCare findByName(String name){
		PrivateCare privateCare = null;
		try{
			privateCare = entityManager.createQuery("from PrivateCare pc where pc.name =:name", PrivateCare.class)
					.setParameter("name", name)
					.getSingleResult();
		}catch (NoResultException e) {
			log.warn("No private care found with name : " + name);
		}
		return privateCare;
	}

	public PrivateCare findByPhoneNumber(String phoneNumber) {
		PrivateCare privateCare = null;
		try{
			privateCare = entityManager.createQuery("from PrivateCare pc where pc.phoneNumber =:phoneNumber", PrivateCare.class)
					.setParameter("phoneNumber", phoneNumber)
					.getSingleResult();
		}catch (NoResultException e) {
			log.warn("No private care found with phoneNumber : " + phoneNumber);
		}
		return privateCare;
	}
}
