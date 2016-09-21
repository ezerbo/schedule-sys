package com.rj.schedulesys.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.rj.schedulesys.dao.PrivateCareDao;
import com.rj.schedulesys.domain.PrivateCare;
import com.rj.schedulesys.util.ObjectValidator;
import com.rj.schedulesys.util.ServiceHelper;
import com.rj.schedulesys.view.model.PrivateCareViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PrivateCareService {

	private PrivateCareDao privateCareDao;
	
	private DozerBeanMapper dozerMapper;
	private ObjectValidator<PrivateCareViewModel> validator;
	
	@Autowired
	public PrivateCareService(PrivateCareDao privateCareDao, DozerBeanMapper dozerMapper,
			ObjectValidator<PrivateCareViewModel> validator) {
		this.privateCareDao = privateCareDao;
		this.dozerMapper = dozerMapper;
		this.validator = validator;
	}
	
	@Transactional
	public PrivateCareViewModel create(PrivateCareViewModel viewModel){
		log.debug("Creating private care : {}", viewModel);
		Assert.notNull(viewModel, "No private care provided");
		String errorMessage;
		PrivateCare privateCare = privateCareDao.findByName(viewModel.getName());
		if(privateCare != null){
			errorMessage = new StringBuilder().append("A private care with name '")
					.append(viewModel.getName())
					.append("' ")
					.append("already exists")
					.toString();
			ServiceHelper.logAndThrowException(errorMessage);
		}
		privateCare = privateCareDao.findByPhoneNumber(viewModel.getPhoneNumber());
		if(privateCare != null){
			errorMessage = new StringBuilder().append("A private care with phone number '")
					.append(viewModel.getPhoneNumber())
					.append("' ")
					.append("already exists")
					.toString();
			ServiceHelper.logAndThrowException(errorMessage);
		}
		viewModel = this.createOrUpdate(viewModel);
		log.debug("Created private care : {}", viewModel);
		return viewModel;
	}
	
	@Transactional
	public PrivateCareViewModel update(PrivateCareViewModel viewModel){
		log.debug("Updating user : {}", viewModel);
		Assert.notNull(viewModel, "No private care provided");
		String errorMessage;
		PrivateCare privateCare = privateCareDao.findOne(viewModel.getId());
		if(!StringUtils.equalsIgnoreCase(privateCare.getName(), viewModel.getName())){
			log.warn("Private care's name updated, checking its uniqueness");
			if(privateCareDao.findByName(viewModel.getName()) != null){
				errorMessage = new StringBuilder().append("A private care with name '")
						.append(viewModel.getName())
						.append("' ")
						.append("already exists")
						.toString();
				ServiceHelper.logAndThrowException(errorMessage);
			}
		}
		if(!StringUtils.equalsIgnoreCase(privateCare.getPhoneNumber(), viewModel.getPhoneNumber())){
			log.warn("Private care's phone number updated, checking its uniqueness");
			if(privateCareDao.findByPhoneNumber(viewModel.getPhoneNumber()) != null){
				errorMessage = new StringBuilder().append("A private care with phone number '")
						.append(viewModel.getPhoneNumber())
						.append("' ")
						.append("already exists")
						.toString();
				ServiceHelper.logAndThrowException(errorMessage);
			}
		}
		viewModel = this.createOrUpdate(viewModel);
		log.debug("Updated private care : {}", viewModel);
		return viewModel;
	}
	
	@Transactional
	private PrivateCareViewModel createOrUpdate(PrivateCareViewModel viewModel){
		validator.validate(viewModel);//Throws an exception when in case of validation error
		PrivateCare privateCare = dozerMapper.map(viewModel, PrivateCare.class);
		privateCare = privateCareDao.merge(privateCare);
		return dozerMapper.map(privateCare, PrivateCareViewModel.class);
	}
	
	@Transactional
	public void delete(Long id){
		log.debug("Deleting private care with id : {}", id);
		PrivateCare privateCare = privateCareDao.findOne(id);
		if (privateCare == null){
			log.error("No private care found with id : {}", id);
			throw new RuntimeException("No private care found with id : " + id);
		}
		//TODO Uncomment this once schedules have been added to private care
//		if(!(facility.getSchedules().isEmpty() && facility.getStaffMembers().isEmpty())){
//			log.error("Facility with id : {} can not be deleted", id);
//			throw new RuntimeException("Facility with id : " + id + " can not be deleted");
//		}
		privateCareDao.delete(privateCare);
	}
	
	@Transactional
	public List<PrivateCareViewModel> findAll(){
		log.info("Finding all private cares");
		List<PrivateCare> privateCares = privateCareDao.findAll();
		List<PrivateCareViewModel> viewModels = new LinkedList<PrivateCareViewModel>();
		privateCares.stream()
		.forEach(privateCare -> {
			viewModels.add(dozerMapper.map(privateCare, PrivateCareViewModel.class));
		});
		return viewModels;
	}
	
	@Transactional
	public PrivateCareViewModel findByName(String name){
		log.debug("Finding private caere by name : {}", name);
		PrivateCare privateCare = privateCareDao.findByName(name);
		PrivateCareViewModel viewModel = null;
		if(privateCare != null){
			viewModel = dozerMapper.map(privateCare, PrivateCareViewModel.class); 
		}else{
			log.debug("No private care found with name : {}", name);
		}
		return viewModel;
	}
	
	@Transactional
	public PrivateCareViewModel findOne(Long id){
		log.debug("Finding private care by id : {}", id);
		PrivateCare privateCare = privateCareDao.findOne(id);
		PrivateCareViewModel viewModel = null;
		if(privateCare != null){
			viewModel = dozerMapper.map(privateCare, PrivateCareViewModel.class);
		}else{
			log.debug("No private care found with id : {}", id);
		}
		return viewModel;
	}
	
	@Transactional
	public PrivateCareViewModel findByPhoneNumber(String phoneNumber){
		log.info("Finding private care by phone number : {}", phoneNumber);
		PrivateCare privateCare = privateCareDao.findByPhoneNumber(phoneNumber);
		PrivateCareViewModel viewModel = null;
		if(privateCare != null){
			viewModel = dozerMapper.map(
					privateCareDao.findByPhoneNumber(phoneNumber), PrivateCareViewModel.class
					);
		}else{
			log.debug("No private care found with phone number : {}", phoneNumber);
		}
		return viewModel;
	}
}
