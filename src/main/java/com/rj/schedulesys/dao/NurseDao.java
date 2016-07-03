package com.rj.schedulesys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.Nurse;

@Repository
public class NurseDao extends GenericDao<Nurse>{

	public NurseDao() {
		setClazz(Nurse.class);
	}
	
	/**
	 * @param positionName
	 * @return
	 */
	public List<Nurse> findAllByPosition(String positionName){
		List<Nurse> nurses = entityManager.createQuery(
				"from Nurse n where n.employee.position.name =:positionName ", Nurse.class)
				.setParameter("positionName",positionName)
				.getResultList();
		return nurses;
	}
	
}
