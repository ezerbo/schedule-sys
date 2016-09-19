package com.rj.schedulesys.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.schedulesys.dao.LicenseTypeDao;
import com.rj.schedulesys.domain.LicenseType;
import com.rj.schedulesys.util.ObjectValidator;
import com.rj.schedulesys.view.model.LicenseTypeViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class LicenseTypeService {

	private LicenseTypeDao licenseTypeDao;
	
	private DozerBeanMapper dozerMapper;
	private ObjectValidator<LicenseTypeViewModel> validator;
	
	@Autowired
	public LicenseTypeService(LicenseTypeDao licenseTypeDao, DozerBeanMapper dozerMapper,
			ObjectValidator<LicenseTypeViewModel> validator) {
		this.licenseTypeDao = licenseTypeDao;
		this.dozerMapper = dozerMapper;
		this.validator = validator;
	}
	
	public LicenseTypeViewModel create(LicenseTypeViewModel viewModel){
		log.debug("Creating new license type : {}", viewModel);
		validator.validate(viewModel);
		if(licenseTypeDao.findByName(viewModel.getTypeName()) != null){
			log.error("License type name : {} already in use", viewModel.getTypeName());
			throw new RuntimeException("License type name : " + viewModel.getTypeName() + " already in use");
		}
		LicenseType licenseType = dozerMapper.map(viewModel, LicenseType.class);
		licenseType = licenseTypeDao.merge(licenseType);
		viewModel.setId(licenseType.getId());
		return viewModel;
	}
	
	public LicenseTypeViewModel update(LicenseTypeViewModel viewModel){
		log.debug("Updating license type : {}", viewModel);
		LicenseType licenseType = licenseTypeDao.findOne(viewModel.getId());
		if(licenseType == null){
			log.error("No license found with id : " + viewModel.getId());
			throw new RuntimeException("No license found with id : " + viewModel.getId());
		}
		if(!StringUtils.equalsIgnoreCase(viewModel.getTypeName(), licenseType.getTypeName())){
			log.warn("License type name updated, checking its uniqueness");
			if(licenseTypeDao.findByName(viewModel.getTypeName()) != null){
				log.error("License name : {} already in use", viewModel.getTypeName());
				throw new RuntimeException("License name : " + viewModel.getTypeName() + " already in use");
			}
		}else{
			licenseType.setTypeName(viewModel.getTypeName());
			licenseType = licenseTypeDao.merge(licenseType);
		}
		return dozerMapper.map(licenseType, LicenseTypeViewModel.class);
	}
	
	public LicenseTypeViewModel findOne(Long id){
		log.debug("Fetching license type with id : {}", id);
		LicenseType licenseType = licenseTypeDao.findOne(id);
		return (licenseType == null) ? null : dozerMapper.map(licenseType, LicenseTypeViewModel.class);
	}
	
	public void delete(Long id){
		log.debug("Deleting license type with id : {}", id);
		LicenseType licenseType = licenseTypeDao.findOne(id);
		if(licenseType == null){
			log.error("No license type found with id : {}", id);
			throw new RuntimeException("No license type found with id : " + id);
		}
		if(!licenseType.getLicenses().isEmpty()){
			log.error("License type with cannot be deleted, please delete all the licenses of this type first.");
			throw new RuntimeException("License type with id cannot be deleted, please delete all the licenses of this type first");
		}
		licenseTypeDao.delete(licenseType);
		log.debug("License type with name : {} successfully deleted", licenseType.getTypeName());
	}
	
	public List<LicenseTypeViewModel> findAll(){
		log.debug("Finding all license types");
		List<LicenseType> licenses = licenseTypeDao.findAll();
		List<LicenseTypeViewModel> viewModels = new ArrayList<>();
		licenses.stream()
		.forEach(license ->{
			viewModels.add(dozerMapper.map(license, LicenseTypeViewModel.class));
		});
		return viewModels;
	}
}
