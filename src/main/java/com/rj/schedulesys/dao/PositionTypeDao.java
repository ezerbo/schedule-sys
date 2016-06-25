package com.rj.schedulesys.dao;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.PositionType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PositionTypeDao extends GenericDao<PositionType>{

	public PositionTypeDao() {
		setClazz(PositionType.class);
	}
	
	/**
	 * @param name
	 * @return
	 */
	public PositionType findByName(String name){
		
		PositionType positionType = null;
		
		try{
			positionType = entityManager.createQuery(
					"from PositionType pt where pt.name =:name", PositionType.class)
					.setParameter("name", name)
					.getSingleResult();
		}catch(NoResultException e){
			log.warn("No position type found with name : {}", name);
		}
		
		return positionType;
	}
}