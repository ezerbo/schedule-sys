package com.rj.sys.dao;

import org.springframework.stereotype.Repository;

import com.rj.sys.domain.StaffMember;

@Repository
public class StaffMemberDao extends GenericDao<StaffMember> {
	
	public StaffMemberDao() {
		setClazz(StaffMember.class);
	}
}	
