package com.rj.schedulesys.service;

import java.util.LinkedList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rj.schedulesys.dao.PhoneNumberLabelDao;
import com.rj.schedulesys.domain.PhoneNumberLabel;
import com.rj.schedulesys.view.model.PhoneNumberLabelViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PhoneNumberLabelService {

	private @Autowired PhoneNumberLabelDao phoneNumberLabelDao;
	
	private @Autowired DozerBeanMapper dozerMapper;
	
	public PhoneNumberLabelViewModel findOne(Long id){
		
		log.debug("Fetching phone number label with id : {}", id);
		
		PhoneNumberLabelViewModel viewModel = null;
		
		PhoneNumberLabel phoneNumberLabel = phoneNumberLabelDao.findOne(id);
		
		if(phoneNumberLabel != null){
			viewModel = dozerMapper.map(
					phoneNumberLabel, PhoneNumberLabelViewModel.class
					);
		}
		
		log.debug("Phone number label found : {}", viewModel);
		
		return viewModel;
	}
	
	public PhoneNumberLabelViewModel findByName(String name){
		
		log.debug("Fetching phone number label with name : {}", name);
		
		PhoneNumberLabelViewModel viewModel = null;
		
		PhoneNumberLabel phoneNumberLabel = phoneNumberLabelDao.findByName(name);
		
		if(phoneNumberLabel != null){
			viewModel = dozerMapper.map(
					phoneNumberLabel, PhoneNumberLabelViewModel.class
					);
		}
		
		log.debug("Phone number label found : {}", viewModel);
		
		return viewModel;
	}
	
	public List<PhoneNumberLabelViewModel> findAll(){
		
		log.debug("Fetching all phone number labels");
		
		List<PhoneNumberLabel> phoneNumberLabels = phoneNumberLabelDao.findAll();
		
		List<PhoneNumberLabelViewModel> viewModels = new LinkedList<>();
		
		for(PhoneNumberLabel phoneNumberLabel :  phoneNumberLabels){
			viewModels.add(dozerMapper.map(phoneNumberLabel, PhoneNumberLabelViewModel.class));
		}
		
		log.debug("Phone number labels found : {}", viewModels);
		
		return viewModels;
	}
}
