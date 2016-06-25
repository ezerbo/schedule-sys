package com.rj.schedulesys.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.Position;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PositionDao extends GenericDao<Position>{
	
	public PositionDao() {
		setClazz(Position.class);
	}
	
	/**
	 * @param name
	 * @return
	 */
	public Position findByName(String name){
		
		Position position = null;
		
		try{
			position = entityManager.createQuery(
					"from Position p where p.name = :name", Position.class)
					.setParameter("name", name)
					.getSingleResult();
		}catch(NoResultException e){
			log.warn("No position found with name : {}", name);
		}
		
		return position;
	}
	
	/**
	 * @param id
	 * @return
	 */
	public Position findOne(Long id){
		
		Position position = null; 
		
		try{
			position = entityManager.createQuery(
					"from Position p where p.id =:id", Position.class)
					.setParameter("id", id)
					.getSingleResult();
		}catch(NoResultException e){
			log.warn("No position found with id : {}", id);
		}
		
		return position;
	}
	
	/**
	 * @param positionType
	 * @return
	 */
	public List<Position> findAllByType(Long typeId){
		
		List<Position> positions = entityManager.createQuery(
					"from Position p where p.positionType.id =:typeId", Position.class)
					.setParameter("typeId", typeId)
					.getResultList();
		
		return positions;
	}
	
	/**
	 * @param name
	 * @param typeName
	 * @return
	 */
	public Position findByNameAndType(String name, String typeName){
		
		Position position = null;
		
		try{
			position = entityManager.createQuery(
					"from Position p where p.name =:name and p.positionType.name =:typeName", Position.class)
					.setParameter("name", name)
					.setParameter("typeName", typeName)
					.getSingleResult();
		}catch(NoResultException e){
			log.warn("No position find with name : {} and type : {}", name, typeName);
		}
		
		return position;
	}
	
}