package com.rj.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rj.sys.domain.Position;

@Repository
public class PositionDao extends GenericDao<Position>{
	
	public PositionDao() {
		setClazz(Position.class);
	}
	
	/**
	 * @param positionName
	 * @return
	 */
	public Position findByName(String positionName){
		
		Position position = entityManager.createQuery(
				"from Position p where p.name = :name ", Position.class)
				.setParameter("name", positionName)
		.getSingleResult();
		
		return position;
	}
	
	/**
	 * @param positionType
	 * @return
	 */
	public List<Position> findAllByType(String positionType){
		
		List<Position> positions = entityManager.createQuery(
				"from Position p where p.positionType.type =:positionType", Position.class)
				.setParameter("positionType", positionType)
				.getResultList();
		
		return positions;
	}
		
}