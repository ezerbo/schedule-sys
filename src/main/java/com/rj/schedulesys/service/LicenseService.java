package com.rj.schedulesys.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.schedulesys.dao.LicenseDao;
import com.rj.schedulesys.dao.NurseDao;
import com.rj.schedulesys.domain.License;
import com.rj.schedulesys.domain.Nurse;
import com.rj.schedulesys.util.ObjectValidator;
import com.rj.schedulesys.view.model.LicenseViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class LicenseService {
	
	@Autowired
	private NurseDao nurseDao;
	
	@Autowired
	private LicenseDao licenseDao;
	
	@Autowired
	private DozerBeanMapper dozerMapper;
	
	@Autowired
	private ObjectValidator<LicenseViewModel> validator;
	
	/**
	 * @param viewModel
	 * @return
	 */
	public LicenseViewModel create(LicenseViewModel viewModel){
		
		log.debug("Creating new license : {}", viewModel);
		
		Nurse nurse = nurseDao.findOne(viewModel.getNurseId());
		
		if(nurse == null){
			log.error("No nurse found with id : {}", viewModel.getId());
			throw new RuntimeException("No nurse found with id : " + viewModel.getNurseId());
		}
		
		if(licenseDao.findByNumber(viewModel.getNumber()) != null){
			log.error("License number : {} is already in use", viewModel.getNumber());
			throw new RuntimeException("License number " + viewModel.getNumber() + " is already in use");
		}
		
		viewModel = this.createOrUpdate(viewModel);
		
		log.debug("Created license : {}", viewModel);
		
		return viewModel;
		
	}
	
	/**
	 * @param viewModel
	 * @return
	 */
	public LicenseViewModel update(LicenseViewModel viewModel){
		
		log.debug("Updating license : {}", viewModel);
		
		Nurse nurse = nurseDao.findOne(viewModel.getNurseId());
		
		if(nurse == null){
			log.error("No nurse found with id : {}", viewModel.getId());
			throw new RuntimeException("No nurse found with id : " + viewModel.getNurseId());
		}
		
		License license = licenseDao.findOne(viewModel.getId());
		
		if(license == null){
			log.error("No license found with id : {}", viewModel.getId());
			throw new RuntimeException("No license found with id : " + viewModel.getId());
		}
		
		if(!StringUtils.equals(license.getNumber(), viewModel.getNumber())){
			
			log.warn("License number updated, checking its uniqueness");
			
			if(licenseDao.findByNumber(viewModel.getNumber()) != null){
				log.error("License number : {} is already in use", viewModel.getNumber());
				throw new RuntimeException("License number " + viewModel.getNumber() + " is already in use");
			}
			
		}
		
		viewModel = this.createOrUpdate(viewModel);
		
		log.debug("Updated license : {}", viewModel);
		
		return viewModel;
		
	}
	
	/**
	 * @param viewModel
	 * @return
	 */
	private LicenseViewModel createOrUpdate(LicenseViewModel viewModel){
		
		validator.validate(viewModel);
		
		Nurse user = nurseDao.findOne(viewModel.getNurseId());
		
		License license = License.builder()
				.id(viewModel.getId())
				.nurse(user)
				.number(viewModel.getNumber())
				.expirationDate(viewModel.getExpirationDate())
				.build();
		
		license = licenseDao.merge(license);
		
		return dozerMapper.map(license, LicenseViewModel.class);
	}
	
	/**
	 * @param id
	 */
	public void delete(Long id){
		
		log.info("Deleting License with id : {}", id);
		
		License license = licenseDao.findOne(id);
		
		if(license == null){
			log.error("No license found with id : {}", id);
			throw new RuntimeException("No license found with id : " + id);
		}
		
		licenseDao.delete(license);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public LicenseViewModel findOne(Long id){
		
		log.info("Finding License by id : {}", id);
		
		License license = licenseDao.findOne(id);
		
		LicenseViewModel viewModel = null;
		
		if(license == null){
			log.warn("No license found with id : {}", id);
		}else{
			viewModel = dozerMapper.map(license, LicenseViewModel.class);
		}
		
		log.info("License found : {}", viewModel);
		
		return viewModel;
	}
	
	/**
	 * @param userId
	 * @return
	 */
	public List<LicenseViewModel> findAllByNurse(Long nurseId){
		
		log.debug("Finding all licenses for nurse with id : {}", nurseId);
		
		if(nurseDao.findOne(nurseId) == null){
			log.error("No nurse found with id : {}", nurseId);
			throw new RuntimeException("No nurse found with id : " + nurseId);
		}
		
		List<License> licenses = licenseDao.findAllByNurse(nurseId);
		
		List<LicenseViewModel> viewModels = new LinkedList<>(); 
		
		for(License license : licenses){
			viewModels.add(dozerMapper.map(license, LicenseViewModel.class));
		}
		
		log.debug("Licenses : {} found for nurse with id : {}", viewModels, nurseId);
		
		return viewModels;
	}
}