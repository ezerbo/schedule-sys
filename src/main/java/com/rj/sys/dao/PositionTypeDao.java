package com.rj.sys.dao;

import org.springframework.stereotype.Repository;

import com.rj.sys.domain.PositionType;

@Repository
public class PositionTypeDao extends GenericDao<PositionType>{

	public PositionTypeDao() {
		setClazz(PositionType.class);
	}
	
	
	public PositionType findByType(String type){
		
		PositionType positionType = entityManager.createQuery(
				"from PositionType pt where pt.type =:type", PositionType.class)
				.setParameter("type", type)
				.getSingleResult();
		
		return positionType;
	}
}
