package com.rj.sys.service;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.sys.dao.FacilityDao;
import com.rj.sys.domain.Facility;
import com.rj.sys.view.model.FacilityViewModel;

@Slf4j
@Service
public class FacilityService {
	
	private @Autowired FacilityDao facilityDao;
	
	private @Autowired DozerBeanMapper dozerMapper;
	
	@Transactional
	public FacilityViewModel createOrUpdateFacility(FacilityViewModel viewModel){
		
		log.info("Creating Facility : {}", viewModel);
		Facility facility = dozerMapper.map(viewModel, Facility.class);
		facility.setIsDeleted(false);
		facility = facilityDao.merge(facility);
		
		return dozerMapper.map(facility, FacilityViewModel.class);
	}
	
	@Transactional
	public FacilityViewModel deleteFacility(Long id){
		
		log.info("Deleting facility with id : {}", id);
		Facility facility = facilityDao.delete(id);
		
		return dozerMapper.map(facility, FacilityViewModel.class);
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
	public FacilityViewModel findActiveByName(String name){
		
		log.info("Finding facility by name : {}", name);
		FacilityViewModel viewModel = null;
		try{
			viewModel = dozerMapper.map(
					facilityDao.findActiveByName(name), FacilityViewModel.class
					); 
		}catch(Exception nre){
			log.info("No facility found by name : {}", name);
		}
		
		return viewModel;
	}
	
	@Transactional
	public FacilityViewModel findByName(String name){
		
		log.info("Finding facility by name : {}", name);
		FacilityViewModel viewModel = null;
		try{
			viewModel = dozerMapper.map(
					facilityDao.findByName(name), FacilityViewModel.class
					); 
		}catch(Exception nre){
			log.info("No facility found by name : {}", name);
		}
		
		return viewModel;
	}
	
	
	@Transactional
	public FacilityViewModel findActiveById(Long id){
		log.info("Finding facility by id : {}", id);
		FacilityViewModel viewModel = null;
		try{
			viewModel = dozerMapper.map(
					facilityDao.findActiveById(id), FacilityViewModel.class
					);
		}catch(Exception nre){
			log.info("No facility found with id : {}", id);
		}
		return viewModel;
	}
	
	@Transactional
	public FacilityViewModel findById(Long id){
		log.info("Finding facility by id : {}", id);
		FacilityViewModel viewModel = null;
		try{
			viewModel = dozerMapper.map(
					facilityDao.findOne(id), FacilityViewModel.class
					);
		}catch(Exception nre){
			log.info("No facility found with id : {}", id);
		}
		return viewModel;
	}
	
	@Transactional
	public FacilityViewModel findActiveByPhoneNumber(String phoneNumber){
		log.info("Finding facility by phone number : {}", phoneNumber);
		FacilityViewModel viewModel = null;
		try{
			viewModel = dozerMapper.map(
					facilityDao.findActiveByPhoneNumber(phoneNumber), FacilityViewModel.class
					);
		}catch(Exception nre){
			log.info("No facility found with phone number : {}", phoneNumber);
		}
		
		return viewModel;
	}
	
	@Transactional
	public FacilityViewModel findByPhoneNumber(String phoneNumber){
		log.info("Finding facility by phone number : {}", phoneNumber);
		FacilityViewModel viewModel = null;
		try{
			viewModel = dozerMapper.map(
					facilityDao.findByPhoneNumber(phoneNumber), FacilityViewModel.class
					);
		}catch(Exception nre){
			log.info("No facility found with phone number : {}", phoneNumber);
		}
		
		return viewModel;
	}
	
}