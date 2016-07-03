package com.rj.schedulesys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.CareGiver;

@Repository
public class CareGiverDao extends GenericDao<CareGiver> {

	public CareGiverDao() {
		setClazz(CareGiver.class);
	}
	
	/**
	 * @param positionName
	 * @return
	 */
	public List<CareGiver> findAllByPosition(String positionName){
		List<CareGiver> careGivers = entityManager.createQuery(
				"from CareGiver cg where cg.employee.position.name =:positionName ", CareGiver.class)
				.setParameter("positionName",positionName)
				.getResultList();
		return careGivers;
	}
	
}
