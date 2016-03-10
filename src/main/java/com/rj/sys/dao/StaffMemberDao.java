package com.rj.sys.dao;

import org.springframework.stereotype.Repository;

import com.rj.sys.domain.StaffMember;

@Repository
public class StaffMemberDao extends GenericDao<StaffMember> {
	
	public StaffMemberDao() {
		setClazz(StaffMember.class);
	}
	
	public StaffMember find(String firstname, String lastname, String title){
		StaffMember staffMember = entityManager.createQuery(
				"from StaffMember sm where sm.firstName =:firstName "
				+ "and sm.lastName =:lastName "
				+ "and sm.title =:title", StaffMember.class)
				.setParameter("firstName", firstname)
				.setParameter("lastName", lastname)
				.setParameter("title", title)
				.getSingleResult();
		return staffMember;
	}
	
	public void delete(Long id){
		super.delete(findOne(id));
	}
}	
