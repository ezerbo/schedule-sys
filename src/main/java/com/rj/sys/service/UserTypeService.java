package com.rj.sys.service;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.NoResultException;

import lombok.extern.slf4j.Slf4j;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.sys.dao.UserTypeDao;
import com.rj.sys.domain.UserType;
import com.rj.sys.view.model.UserTypeViewModel;

@Slf4j
@Service
public class UserTypeService {
	
	private @Autowired UserTypeDao userTypeDao;
	
	private @Autowired DozerBeanMapper dozerMapper;
	
	@Transactional
	public UserTypeViewModel findByType(String userType){
		log.info("Finding user type : {}", userType);
		UserTypeViewModel viewModel = null;
		try{
			viewModel = dozerMapper.map(
						userTypeDao.findByType(userType), UserTypeViewModel.class
					);
		}catch(NoResultException nre){
			log.info("No such user type : {}", userType);
		}
		return viewModel;
	}
	
	@Transactional
	public UserTypeViewModel findById(Long id){
		log.info("Finding user type by id : {}", id);
		UserTypeViewModel viewModel = null;
		UserType userType = userTypeDao.findOne(id);
		if(userType != null){
			viewModel = dozerMapper.map(userType, UserTypeViewModel.class);
		}
		return viewModel;
	}
	
	@Transactional
	public List<UserTypeViewModel> findAll(){
		
		log.info("Finding all user types");
		
		List<UserType> userTypes = userTypeDao.findAll();
		List<UserTypeViewModel> viewModels = new LinkedList<UserTypeViewModel>();
		
		for(UserType userType : userTypes){
			viewModels.add(dozerMapper.map(userType, UserTypeViewModel.class));
		}
		
		return viewModels;
	}
	
}
