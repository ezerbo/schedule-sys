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
import com.rj.schedulesys.domain.Facility;
import com.rj.schedulesys.util.ObjectValidator;
import com.rj.schedulesys.util.ServiceHelper;
import com.rj.schedulesys.view.model.FacilityViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class FacilityService {
	
	@Autowired
	private  FacilityDao facilityDao;
	
	private @Autowired ObjectValidator<FacilityViewModel> validator;
	
	private @Autowired DozerBeanMapper dozerMapper;
	
	@Transactional
	public FacilityViewModel create(FacilityViewModel viewModel){
		
		log.debug("Creating facility : {}", viewModel);
		Assert.notNull(viewModel, "No facility provided");
		
		String errorMessage;
		
		Facility facility = facilityDao.findByName(viewModel.getName());
		if(facility != null){
			errorMessage = new StringBuilder().append("A facility with name '")
					.append(viewModel.getName())
					.append("' ")
					.append("already exists")
					.toString();
			ServiceHelper.logAndThrowException(errorMessage);
		}
		
		facility = facilityDao.findByPhoneNumber(viewModel.getPhoneNumber());
		if(facility != null){
			errorMessage = new StringBuilder().append("A facility with phone number '")
					.append(viewModel.getPhoneNumber())
					.append("' ")
					.append("already exists")
					.toString();
			ServiceHelper.logAndThrowException(errorMessage);
		}
		
		viewModel = this.createOrUpdate(viewModel);
		
		log.debug("Created facility : {}", viewModel);
		
		return viewModel;
	}
	
	@Transactional
	public FacilityViewModel update(FacilityViewModel viewModel){
		log.debug("Updating user : {}", viewModel);
		Assert.notNull(viewModel, "No facility provided");
		
		String errorMessage;
		
		Facility facility = facilityDao.findOne(viewModel.getId());
		if(!StringUtils.equalsIgnoreCase(facility.getName(), viewModel.getName())){
			log.warn("Facility's name updated, checking its uniqueness");
			if(facilityDao.findByName(viewModel.getName()) != null){
				errorMessage = new StringBuilder().append("A facility with name '")
						.append(viewModel.getName())
						.append("' ")
						.append("already exists")
						.toString();
				ServiceHelper.logAndThrowException(errorMessage);
			}
		}
		
		if(!StringUtils.equalsIgnoreCase(facility.getPhoneNumber(), viewModel.getPhoneNumber())){
			log.warn("Facility's phone number updated, checking its uniqueness");
			if(facilityDao.findByPhoneNumber(viewModel.getPhoneNumber()) != null){
				errorMessage = new StringBuilder().append("A facility with phone number '")
						.append(viewModel.getPhoneNumber())
						.append("' ")
						.append("already exists")
						.toString();
				ServiceHelper.logAndThrowException(errorMessage);
			}
		}
		
		viewModel = this.createOrUpdate(viewModel);
		
		log.debug("Updated facility : {}", viewModel);
		
		return viewModel;
	}
	
	@Transactional
	private FacilityViewModel createOrUpdate(FacilityViewModel viewModel){
		validator.validate(viewModel);//Throws an exception when in case of validation error
		Facility facility = dozerMapper.map(viewModel, Facility.class);
		facility = facilityDao.merge(facility);
		return dozerMapper.map(facility, FacilityViewModel.class);
	}
	
	@Transactional
	public void delete(Long id){
		log.debug("Deleting facility with id : {}", id);
		Facility facility = facilityDao.findOne(id);
		if (facility == null){
			log.error("No facility found with id : {}", id);
			throw new RuntimeException("No facility found with id : " + id);
		}
		if(!(facility.getSchedules().isEmpty() && facility.getStaffMembers().isEmpty())){
			log.error("Facility with id : {} can not be deleted", id);
			throw new RuntimeException("Facility with id : " + id + " can not be deleted");
		}
		facilityDao.delete(facility);
	}
	
	@Transactional
	public List<FacilityViewModel> findAll(){
		
		log.info("Finding all facilities");
		
		List<Facility> facilities = facilityDao.findAll();
		List<FacilityViewModel> viewModels = new LinkedList<FacilityViewModel>();
		
		for(Facility facility : facilities){
			viewModels.add(dozerMapper.map(facility, FacilityViewModel.class));
		}
		
		return viewModels;
	}
	
	@Transactional
	public FacilityViewModel findByName(String name){
		
		log.debug("Finding facility by name : {}", name);
		
		Facility facility = facilityDao.findByName(name);
		
		FacilityViewModel viewModel = null;
		if(facility != null){
			viewModel = dozerMapper.map(facility, FacilityViewModel.class); 
		}else{
			log.debug("No facility found with name : {}", name);
		}
		return viewModel;
	}
	
	@Transactional
	public FacilityViewModel findOne(Long id){
		log.debug("Finding facility by id : {}", id);
		
		Facility facility = facilityDao.findOne(id);
		
		FacilityViewModel viewModel = null;
		
		if(facility != null){
			viewModel = dozerMapper.map(facility, FacilityViewModel.class);
		}else{
			log.debug("No facility found with id : {}", id);
		}
		
		return viewModel;
	}
	
	@Transactional
	public FacilityViewModel findByPhoneNumber(String phoneNumber){
		log.info("Finding facility by phone number : {}", phoneNumber);
		
		Facility facility = facilityDao.findByPhoneNumber(phoneNumber);
		
		FacilityViewModel viewModel = null;
		
		if(facility != null){
			viewModel = dozerMapper.map(
					facilityDao.findByPhoneNumber(phoneNumber), FacilityViewModel.class
					);
		}else{
			log.debug("No facility found with phone number : {}", phoneNumber);
		}
		
		return viewModel;
	}
	
}