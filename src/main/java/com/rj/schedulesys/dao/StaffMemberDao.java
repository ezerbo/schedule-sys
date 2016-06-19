package com.rj.schedulesys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.StaffMember;

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
	
	public List<StaffMember> findAllByFacilityId(Long id){
		List<StaffMember> staffMembers = entityManager.createQuery(
				"from StaffMember sm where sm.facility.id =:id "
				+ "and sm.facility.isDeleted = 0", StaffMember.class)
				.setParameter("id", id)
				.getResultList();
		return staffMembers;
	}
	
	public StaffMember findByFacilityIdAndStaffMemberId(Long facilityId, Long staffMemberId){
		StaffMember staffMember = entityManager.createQuery(
				"from StaffMember sm where sm.id =:staffMemberId "
				+ "and sm.facility.id =:facilityId "
				+ "and sm.facility.isDeleted = 0", StaffMember.class)
				.setParameter("facilityId", facilityId)
				.setParameter("staffMemberId", staffMemberId)
				.getSingleResult();
		return staffMember;
	}
	
	public void delete(Long id){
		super.delete(findOne(id));
	}
}