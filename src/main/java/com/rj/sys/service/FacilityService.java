package com.rj.sys.service;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.NoResultException;

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
	public void createFacility(FacilityViewModel viewModel){
		log.info("Creating Facility : {}", viewModel);
	}
	
	@Transactional
	public void updateFacility(FacilityViewModel viewModel){
		log.info("Updating Facility : {}", viewModel);
	}
	
	@Transactional
	public void deleteFacility(Long id){
		//TODO add soft delete features to the service
		log.info("Deleting facility with id : {}", id);
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
		
		log.info("Finding facility by name : {}", name);
		FacilityViewModel viewModel = null;
		try{
			viewModel = dozerMapper.map(
					facilityDao.findByName(name), FacilityViewModel.class
					); 
		}catch(NoResultException nre){
			log.info("No facility found by name : {}", name);
		}
		
		return viewModel;
	}
	
	@Transactional
	public FacilityViewModel findById(Long id){
		log.info("Finding facility by id : {}", id);
		FacilityViewModel viewModel = null;
		Facility facility = facilityDao.findOne(id);
		if(facility != null){
			viewModel = dozerMapper.map(facility, FacilityViewModel.class);
		}
		return viewModel;
	}
	
	
}