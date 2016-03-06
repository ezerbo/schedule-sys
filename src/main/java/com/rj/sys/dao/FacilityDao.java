package com.rj.sys.dao;

import org.springframework.stereotype.Repository;

import com.rj.sys.domain.Facility;

@Repository
public class FacilityDao extends GenericDao<Facility> {
	
	public FacilityDao() {
		setClazz(Facility.class);
	}
	
	public Facility findByName(String name){
		Facility facility = entityManager.createQuery(
				"from Facility f where f.name =:name", Facility.class)
				.setParameter("name", name)
				.getSingleResult();
		return facility;
	}
}
