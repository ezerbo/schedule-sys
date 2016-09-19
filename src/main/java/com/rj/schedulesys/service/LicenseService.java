package com.rj.schedulesys.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.schedulesys.dao.LicenseDao;
import com.rj.schedulesys.dao.LicenseTypeDao;
import com.rj.schedulesys.dao.NurseDao;
import com.rj.schedulesys.domain.License;
import com.rj.schedulesys.domain.LicenseType;
import com.rj.schedulesys.domain.Nurse;
import com.rj.schedulesys.util.ObjectValidator;
import com.rj.schedulesys.view.model.GetLicenseViewModel;
import com.rj.schedulesys.view.model.LicenseTypeViewModel;
import com.rj.schedulesys.view.model.LicenseViewModel;
import com.rj.schedulesys.view.model.NurseViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class LicenseService {
	
	private NurseDao nurseDao;
	private LicenseDao licenseDao;
	private LicenseTypeDao licenseTypeDao;
	
	private DozerBeanMapper dozerMapper;
	private ObjectValidator<LicenseViewModel> validator;
	
	@Autowired
	public LicenseService(NurseDao nurseDao, LicenseDao licenseDao, LicenseTypeDao licenseTypeDao,
			DozerBeanMapper dozerMapper, ObjectValidator<LicenseViewModel> validator) {
		this.nurseDao = nurseDao;
		this.licenseDao = licenseDao;
		this.licenseTypeDao = licenseTypeDao;
		this.dozerMapper = dozerMapper;
		this.validator = validator;
	}
	
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
		if(licenseTypeDao.findOne(viewModel.getLicenseTypeId()) == null){
			log.error("No license type found with id : {}", viewModel.getLicenseTypeId());
			throw new RuntimeException("No license type found with id : " + viewModel.getLicenseTypeId());
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
		LicenseType licenseType = licenseTypeDao.findOne(viewModel.getLicenseTypeId());
		License license = License.builder()
				.id(viewModel.getId())
				.licenseType(licenseType)
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
	public GetLicenseViewModel findOne(Long id){
		log.info("Finding License by id : {}", id);
		License license = licenseDao.findOne(id);
		GetLicenseViewModel viewModel = null;
		if(license == null){
			log.warn("No license found with id : {}", id);
		}else{
			viewModel = new GetLicenseViewModel();
			viewModel.setId(license.getId());
			viewModel.setNumber(license.getNumber());
			viewModel.setExpirationDate(license.getExpirationDate());
			viewModel.setNurse(dozerMapper.map(license.getNurse().getEmployee(), NurseViewModel.class));
			viewModel.setLicenseType(dozerMapper.map(license.getLicenseType(), LicenseTypeViewModel.class));
		}
		log.info("License found : {}", viewModel);
		return viewModel;
	}
	
	/**
	 * @param userId
	 * @return
	 */
	public List<GetLicenseViewModel> findAllByNurse(Long nurseId){
		log.debug("Finding all licenses for nurse with id : {}", nurseId);
		if(nurseDao.findOne(nurseId) == null){
			log.error("No nurse found with id : {}", nurseId);
			throw new RuntimeException("No nurse found with id : " + nurseId);
		}
		List<License> licenses = licenseDao.findAllByNurse(nurseId);
		List<GetLicenseViewModel> viewModels = new LinkedList<>(); 
		licenses.stream().forEach(license ->{
			GetLicenseViewModel viewModel = new GetLicenseViewModel();
			viewModel.setId(license.getId());
			viewModel.setNumber(license.getNumber());
			viewModel.setExpirationDate(license.getExpirationDate());
			viewModel.setNurse(dozerMapper.map(license.getNurse().getEmployee(), NurseViewModel.class));
			viewModel.setLicenseType(dozerMapper.map(license.getLicenseType(), LicenseTypeViewModel.class));
			viewModels.add(viewModel);
		});
		log.debug("Licenses : {} found for nurse with id : {}", viewModels, nurseId);
		return viewModels;
	}
}