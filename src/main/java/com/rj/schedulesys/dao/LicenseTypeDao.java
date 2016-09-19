package com.rj.schedulesys.dao;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.LicenseType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class LicenseTypeDao extends GenericDao<LicenseType>{

	public LicenseTypeDao() {
		setClazz(LicenseType.class);
	}
	
	public LicenseType findByName(String typeName){
		LicenseType licenseType = null;
		try{
			licenseType = entityManager.createQuery("from LicenseType lt where lt.typeName =:typeName"
					, LicenseType.class)
					.setParameter("typeName", typeName)
					.getSingleResult();
		}catch(NoResultException e){
			log.warn("No license type found with name : " + typeName);
		}
		return licenseType;
	}
}
