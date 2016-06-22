package com.rj.schedulesys.service;

import java.util.LinkedList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.schedulesys.dao.PhoneNumberTypeDao;
import com.rj.schedulesys.domain.PhoneNumberType;
import com.rj.schedulesys.view.model.PhoneNumberTypeViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class PhoneNumberTypeService {

	private @Autowired PhoneNumberTypeDao phoneNumberTypeDao;
	
	private @Autowired DozerBeanMapper dozerMapper;
	
	/**
	 * @return
	 */
	public List<PhoneNumberTypeViewModel> findAll(){
		
		log.debug("Fetching all the phone number types");
		
		List<PhoneNumberType> phoneNumberTypes = phoneNumberTypeDao.findAll();
		
		List<PhoneNumberTypeViewModel> viewModels = new LinkedList<>();
		
		for(PhoneNumberType phoneNumberType : phoneNumberTypes){
			viewModels.add(
					dozerMapper.map(phoneNumberType, PhoneNumberTypeViewModel.class)
					);
		}
		
		log.debug("Phone number types found : {}", viewModels);
		
		return viewModels;
	}
	
	/**
	 * @param id
	 * @return
	 */
	public PhoneNumberTypeViewModel findOne(Long id){
		
		log.debug("Fetching phone number type with id : {}", id);
		
		PhoneNumberType phoneNumberType = phoneNumberTypeDao.findOne(id);
		
		PhoneNumberTypeViewModel viewModel = null;
		
		if(phoneNumberType != null){
			viewModel = dozerMapper.map(phoneNumberType, PhoneNumberTypeViewModel.class);
		}
		
		log.debug("Phone number type found : {}", viewModel);
		
		return viewModel;
	}
	
	
	/**
	 * @param name
	 * @return
	 */
	public PhoneNumberTypeViewModel findByName(String name){
		
		log.debug("Fetching phone number type with name : {}", name);
		
		PhoneNumberType phoneNumberType = phoneNumberTypeDao.findByName(name);
		
		PhoneNumberTypeViewModel viewModel = null;
		
		if(phoneNumberType != null){
			viewModel = dozerMapper.map(phoneNumberType, PhoneNumberTypeViewModel.class);
		}
		
		log.debug("Phone number type found : {}", viewModel);
		
		return viewModel;
	}
	
}
