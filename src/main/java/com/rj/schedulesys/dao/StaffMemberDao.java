package com.rj.schedulesys.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.StaffMember;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class StaffMemberDao extends GenericDao<StaffMember> {
	
	public StaffMemberDao() {
		setClazz(StaffMember.class);
	}
	
	/**
	 * @param firstName
	 * @param lastName
	 * @param title
	 * @return
	 */
	public StaffMember find(String firstName, String lastName, String title, Long facilityId){
		StaffMember staffMember = null;
		try{
			staffMember = entityManager.createQuery(
					"from StaffMember sm where sm.firstName =:firstName "
							+ "and sm.lastName =:lastName "
							+ "and sm.title =:title "
							+ "and sm.facility.id =:facilityId", StaffMember.class)
			.setParameter("firstName", firstName)
			.setParameter("lastName", lastName)
			.setParameter("title", title)
			.setParameter("facilityId", facilityId)
			.getSingleResult();
		}catch(NoResultException e){
			log.warn("No staff member found with first name : {}, last name : {} and title : {}"
					, firstName, lastName, title);
		}
		return staffMember;
	}
	
	/**
	 * @param id
	 * @return
	 */
	public List<StaffMember> findAllByFacility(Long id){
		List<StaffMember> staffMembers = entityManager.createQuery(
				"from StaffMember sm where sm.facility.id =:id "
				, StaffMember.class)
				.setParameter("id", id)
				.getResultList();
		return staffMembers;
	}
	
//	public StaffMember findByFacilityIdAndStaffMemberId(Long facilityId, Long staffMemberId){
//		StaffMember staffMember = entityManager.createQuery(
//				"from StaffMember sm where sm.id =:staffMemberId "
//				+ "and sm.facility.id =:facilityId "
//				+ "and sm.facility.isDeleted = 0", StaffMember.class)
//				.setParameter("facilityId", facilityId)
//				.setParameter("staffMemberId", staffMemberId)
//				.getSingleResult();
//		return staffMember;
//	}
	
	/**
	 * @param id
	 */
}