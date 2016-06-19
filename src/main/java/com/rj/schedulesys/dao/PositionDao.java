package com.rj.schedulesys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.Position;

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
				"from Position p where p.name = :name and p.isDeleted = 0", Position.class)
				.setParameter("name", positionName)
		.getSingleResult();
		
		return position;
	}
	
	/**
	 * @param id
	 * @return
	 */
	public Position findOne(Long id){
		
		Position position = entityManager.createQuery(
				"from Position p where p.id =:id and p.isDeleted = 0", Position.class)
				.setParameter("id", id)
				.getSingleResult();
		
		return position;
	}
	
	/**
	 * @param positionType
	 * @return
	 */
	public List<Position> findAllByType(String positionType){
		
		List<Position> positions = entityManager.createQuery(
				"from Position p where p.positionType.type =:positionType where isDeleted = 0", Position.class)
				.setParameter("positionType", positionType)
				.getResultList();
		
		return positions;
	}
	
}