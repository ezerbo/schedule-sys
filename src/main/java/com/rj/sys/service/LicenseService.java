package com.rj.sys.service;

import java.util.LinkedList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.sys.dao.LicenseDao;
import com.rj.sys.dao.ScheduleSysUserDao;
import com.rj.sys.domain.License;
import com.rj.sys.domain.ScheduleSysUser;
import com.rj.sys.view.model.LicenseViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class LicenseService {
	
	private @Autowired ScheduleSysUserDao userDao;
	private @Autowired LicenseDao licenseDao;
	private @Autowired DozerBeanMapper dozerMapper;
	
	public LicenseViewModel createOrUpdateLicense(LicenseViewModel viewModel){
		log.info("Creating or updating license : {}", viewModel);
		ScheduleSysUser user = userDao.findOne(viewModel.getUserId());
		License license = License.builder()
				.id(viewModel.getId())
				//.user(user)
				//.licenseNumber(viewModel.getLicenseNumber())
				.expirationDate(viewModel.getExpirationDate())
				.build();
		license = licenseDao.merge(license);
		return dozerMapper.map(license, LicenseViewModel.class);
	}
	
	public void remove(Long id){
		log.info("Deleting License with id : {}", id);
		licenseDao.delete(licenseDao.findOne(id));
	}
	
	public LicenseViewModel findBydIdAndUserId(Long id, Long userId){
		log.info("Finding license with id : {} for user with id : {}", id, userId);
		LicenseViewModel viewModel = null;
		try{
			viewModel = dozerMapper.map(
					licenseDao.findByIdAndUserId(id, userId), LicenseViewModel.class
					);
		}catch(Exception e){
			log.info("No license with id : {} for user with id : {}", id, userId);
		}
		log.info("Found license : {}", viewModel);
		return viewModel;
	}
	
	public LicenseViewModel findById(Long id){
		
		log.info("Finding License by id : {}", id);
		License license = licenseDao.findOne(id);
		LicenseViewModel viewModel = null;
		if(license != null){
			viewModel = dozerMapper.map(license, LicenseViewModel.class);
		}
		log.info("License found : {}", viewModel);
		return viewModel;
	}
	
	public LicenseViewModel findByLicenseNumber(String number){
		log.info("Finding License by number : {}", number);
		LicenseViewModel viewModel = null;
		try{
			viewModel = dozerMapper.map(
					licenseDao.findByNumber(number), LicenseViewModel.class
					);
		}catch(Exception e){
			log.info("No license found by number : {}", number);
		}
		return viewModel;
	}
	
	public List<LicenseViewModel> findAllByUserId(Long userId){
		
		log.info("Finding all licenses for user with id : {}", userId);
		List<License> licenses = licenseDao.findAllByUserId(userId);
		List<LicenseViewModel> viewModels = new LinkedList<>(); 
		for(License license : licenses){
			viewModels.add(dozerMapper.map(license, LicenseViewModel.class));
		}
		
		log.info("Licenses : {} found for user with id : {}", viewModels, userId);
		
		return viewModels;
	}
}