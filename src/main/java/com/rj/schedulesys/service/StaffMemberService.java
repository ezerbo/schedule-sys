package com.rj.schedulesys.service;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.schedulesys.dao.FacilityDao;
import com.rj.schedulesys.dao.StaffMemberDao;
import com.rj.schedulesys.domain.Facility;
import com.rj.schedulesys.domain.StaffMember;
import com.rj.schedulesys.view.model.StaffMemberViewModel;

@Slf4j
@Service
public class StaffMemberService {
	
	private @Autowired StaffMemberDao staffMemberDao;
	private @Autowired FacilityDao facilityDao;
	
	private @Autowired DozerBeanMapper dozerMapper;
	
	@Transactional
	public StaffMemberViewModel createOrUpdateStaffMember(StaffMemberViewModel viewModel, Long facilityId){
		
		log.info("Creating a staff member : {}", viewModel);
		Facility facility = facilityDao.findActiveById(facilityId);
		StaffMember staffMember = dozerMapper.map(viewModel, StaffMember.class);
		staffMember.setFacility(facility);
		staffMember = staffMemberDao.merge(staffMember);
		
		return dozerMapper.map(staffMember, StaffMemberViewModel.class);
	}
	
	@Transactional
	public List<StaffMemberViewModel> findAllByFacilityId(Long id){
		log.info("Finding all staffMembers");
		List<StaffMember> staffMembers = staffMemberDao.findAllByFacilityId(id);
		List<StaffMemberViewModel> viewModels = new LinkedList<StaffMemberViewModel>();
		
		for(StaffMember staffMember : staffMembers){
			viewModels.add(dozerMapper.map(staffMember, StaffMemberViewModel.class));
		}
		
		return viewModels;
	}
	
	@Transactional
	public StaffMemberViewModel findByFacilityIdAndStaffMemberId(Long facilityId, Long staffMemberId){
		log.info("Finding staff member with id : {} in facility with id : {}", staffMemberId, facilityId);
		StaffMemberViewModel viewModel = null;
		try{
			viewModel = dozerMapper.map(
					staffMemberDao.findByFacilityIdAndStaffMemberId(facilityId, staffMemberId), StaffMemberViewModel.class
					);
		}catch(Exception nre){
			log.info("No staff member found with id : {} in facility with id : {}", staffMemberId, facilityId);
		}
		
		return viewModel;
	}
	
	@Transactional
	public StaffMemberViewModel find(String firstName, String lastName, String title){
		log.info("Finding staff member by fisrtname : {}, lastname : {}, title : {}", firstName, lastName, title);
		StaffMemberViewModel viewModel = null;
		try{
			viewModel = dozerMapper.map(
					staffMemberDao.find(firstName, lastName, title), StaffMemberViewModel.class
					);
		}catch(Exception nre){
			log.info("No staff member found with firstname : {}, lastname :{}, title : {}", firstName, lastName, title);
		}
		
		return viewModel;
	}
	
	@Transactional
	public StaffMemberViewModel findById(Long id){
		
		log.info("Finding staff member by id : {}", id);
		StaffMember staffMember = staffMemberDao.findOne(id);
		StaffMemberViewModel viewModel = null;
		
		if(staffMember != null){
			viewModel = dozerMapper.map(staffMember, StaffMemberViewModel.class);
		}
		return viewModel;
	}
	
	@Transactional
	public void deleteStaffMember(Long id){
		log.info("Deleting staff member with id : {}", id);
		staffMemberDao.delete(id);
	}
}