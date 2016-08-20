package com.rj.schedulesys.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.rj.schedulesys.dao.FacilityDao;
import com.rj.schedulesys.dao.StaffMemberDao;
import com.rj.schedulesys.domain.Facility;
import com.rj.schedulesys.domain.StaffMember;
import com.rj.schedulesys.util.ObjectValidator;
import com.rj.schedulesys.util.ServiceHelper;
import com.rj.schedulesys.view.model.StaffMemberViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StaffMemberService {
	
	private @Autowired FacilityDao facilityDao;
	private @Autowired StaffMemberDao staffMemberDao;
	
	private @Autowired ObjectValidator<StaffMemberViewModel> validator;
	
	private @Autowired DozerBeanMapper dozerMapper;
	
	
	@Transactional
	public StaffMemberViewModel create(StaffMemberViewModel viewModel){
		
		log.debug("Creating new staff member : {}", viewModel);
		
		Assert.notNull(viewModel, "No staff member provided");
		
		Facility facility = facilityDao.findOne(viewModel.getFacilityId());
		
		String errorMessage;
		
		if(facility == null){
			errorMessage = new StringBuilder().append("No facility found with id : ")
					.append(viewModel.getFacilityId())
					.toString();
			ServiceHelper.logAndThrowException(errorMessage);
		}
		
		//Staff members titles are always all caps
		StaffMember staffMember = staffMemberDao.find(
				viewModel.getFirstName(), viewModel.getLastName()
				, StringUtils.upperCase(viewModel.getTitle()), viewModel.getFacilityId()
				);
		
		if(staffMember != null){
			errorMessage = new StringBuilder().append("A staff member with first name '")
					.append(viewModel.getFirstName()).append("', last name '")
					.append(viewModel.getLastName()).append("' and title '")
					.append(viewModel.getTitle()).append("' already exists")
					.toString();
			ServiceHelper.logAndThrowException(errorMessage);
		}
		
		viewModel.setId(null);
		
		viewModel = this.createOrUpdate(viewModel); 
		
		log.debug("Created staff member : {}", viewModel);
		
		return viewModel;
	}
	
	@Transactional
	public StaffMemberViewModel update(StaffMemberViewModel viewModel){
		
		log.debug("Updating staff member : {}", viewModel);
		
		Assert.notNull(viewModel, "No staff member provided");
		
		Assert.notNull(viewModel.getId(), "No staff member id provided");
		
		StaffMember staffMember = staffMemberDao.findOne(viewModel.getId());
		
		if(staffMember == null){
			log.error("No staff member found with id : {}", viewModel.getId());
			throw new RuntimeException("No staff member found with id : " + viewModel.getId());
		}
		
		String errorMessage;
		//If either of the first name, last name, title has been updated, check if there is no staff
		// member with new first name, last name and title provided
		if(!(StringUtils.equalsIgnoreCase(staffMember.getFirstName(), viewModel.getFirstName())
				||StringUtils.equalsIgnoreCase(staffMember.getLastName(), viewModel.getLastName())
				||StringUtils.equalsIgnoreCase(staffMember.getTitle(), viewModel.getTitle()))){
			
			log.warn("staff member's first name and/or last name and/or title updated, checking combination(firstName, lastName, title, facility)'s uniqueness");
			
			if(staffMemberDao.find(viewModel.getFirstName(), viewModel.getLastName()
					, StringUtils.upperCase(viewModel.getTitle()), viewModel.getFacilityId()) != null){
				
				errorMessage = new StringBuilder().append("A staff member with first name '")
						.append(viewModel.getFirstName()).append("', last name '")
						.append(viewModel.getLastName()).append("' and title '")
						.append(viewModel.getTitle()).append("' already exists for facility with id : ")
						.append(viewModel.getFacilityId())
						.toString();
				ServiceHelper.logAndThrowException(errorMessage);
			}
		}
		//When the facility has been updated, check that the new one exists
		if(staffMember.getFacility().getId() != viewModel.getFacilityId()){
			log.warn("Facility updated, checking : {} existance", viewModel.getFacilityId());
			if(facilityDao.findOne(viewModel.getFacilityId()) == null){
				errorMessage = new StringBuilder().append("No facility found with id : ")
						.append(viewModel.getFacilityId())
						.toString();
				ServiceHelper.logAndThrowException(errorMessage);
						
			}
		}
		
		viewModel = this.createOrUpdate(viewModel);
		
		log.debug("Updated staff member : {}", viewModel);
		
		return viewModel;
	}
	
	@Transactional
	private StaffMemberViewModel createOrUpdate(StaffMemberViewModel viewModel){
		
		validator.validate(viewModel);
		
		Facility facility = facilityDao.findOne(viewModel.getFacilityId());
		StaffMember staffMember = dozerMapper.map(viewModel, StaffMember.class);
		staffMember.setFacility(facility);
		staffMember.setTitle(StringUtils.upperCase(staffMember.getTitle()));//Title are always all caps
		staffMember = staffMemberDao.merge(staffMember);
		
		return dozerMapper.map(staffMember, StaffMemberViewModel.class);
	}
	
	/**
	 * @param id
	 * @return
	 */
	@Transactional
	public List<StaffMemberViewModel> findAllByFacility(Long id){
		
		log.debug("Fetching all staff members for facility with id : {}", id);
		
		if(facilityDao.findOne(id) == null){
			log.error("No facility found with id : " + id);
			throw new RuntimeException("No facility found with id : " + id);
		}
		
		List<StaffMember> staffMembers = staffMemberDao.findAllByFacility(id);
		List<StaffMemberViewModel> viewModels = new LinkedList<StaffMemberViewModel>();
		
		for(StaffMember staffMember : staffMembers){
			viewModels.add(dozerMapper.map(staffMember, StaffMemberViewModel.class));
		}
		
		return viewModels;
	}
	
//	@Transactional
//	public StaffMemberViewModel findByFacilityIdAndStaffMemberId(Long facilityId, Long staffMemberId){
//		log.info("Finding staff member with id : {} in facility with id : {}", staffMemberId, facilityId);
//		StaffMemberViewModel viewModel = null;
//		try{
//			viewModel = dozerMapper.map(
//					staffMemberDao.findByFacilityIdAndStaffMemberId(facilityId, staffMemberId), StaffMemberViewModel.class
//					);
//		}catch(Exception nre){
//			log.info("No staff member found with id : {} in facility with id : {}", staffMemberId, facilityId);
//		}
//		
//		return viewModel;
//	}
	
//	/**
//	 * @param firstName
//	 * @param lastName
//	 * @param title
//	 * @return
//	 */
//	@Transactional
//	public StaffMemberViewModel find(String firstName, String lastName, String title){
//		
//		log.info("Fetching staff member by fisrt name : {}, last name : {} and title : {}", firstName, lastName, title);
//		
//		StaffMemberViewModel viewModel = null;
//		try{
//			viewModel = dozerMapper.map(
//					staffMemberDao.find(firstName, lastName, title), StaffMemberViewModel.class
//					);
//		}catch(Exception nre){
//			log.("No staff member found with firstname : {}, lastname :{}, title : {}", firstName, lastName, title);
//		}
//		
//		return viewModel;
//	}
	
	/**
	 * @param id
	 * @return
	 */
	@Transactional
	public StaffMemberViewModel findOne(Long id){		
		
		log.info("Finding staff member by id : {}", id);
		
		StaffMember staffMember = staffMemberDao.findOne(id);
		StaffMemberViewModel viewModel = null;
		
		if(staffMember != null){
			viewModel = dozerMapper.map(staffMember, StaffMemberViewModel.class);
		}else{
			log.debug("No staff member found with id : {}", id);
		}
		
		return viewModel;
	}
	
	/**
	 * @param id
	 * Deletes staff member with given id
	 */
	@Transactional
	public void delete(Long id){
		
		log.info("Deleting staff member with id : {}", id);
		
		StaffMember staffMember = staffMemberDao.findOne(id);
		
		if(staffMember == null){
			log.error("No staff member found with id : {}", id);
			throw new RuntimeException("No staff member found with id : " + id);
		}
		
		staffMemberDao.delete(staffMember);
	}
	
}