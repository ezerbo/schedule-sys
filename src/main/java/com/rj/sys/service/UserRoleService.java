package com.rj.sys.service;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.sys.dao.UserRoleDao;
import com.rj.sys.domain.UserRole;
import com.rj.sys.view.model.UserRoleViewModel;

@Slf4j
@Service
public class UserRoleService {
	
	private @Autowired UserRoleDao userRoleDao;
	
	private @Autowired DozerBeanMapper dozerMapper;
	
	@Transactional
	public UserRoleViewModel findByRole(String role){
		log.info("Finding user role : {}", role);
		UserRole userRole = userRoleDao.findByRole(role);
		UserRoleViewModel viewModel = null;
		if(userRole != null){
			viewModel = dozerMapper.map(userRole, UserRoleViewModel.class);
		}
		
		return viewModel;
	}
	
	@Transactional
	public UserRoleViewModel findById(Long id){
		log.info("Finding user role by id : {}", id);
		UserRoleViewModel viewModel = null;
		UserRole userRole = userRoleDao.findOne(id);
		if(userRole != null){
			viewModel = dozerMapper.map(userRole, UserRoleViewModel.class);
		}
		
		return viewModel;
	}
	
	@Transactional
	public List<UserRoleViewModel> findAll(){
		log.info("Finding all user roles");
		
		List<UserRole> userRoles = userRoleDao.findAll();
		List<UserRoleViewModel> viewModels = new LinkedList<UserRoleViewModel>();
		
		for(UserRole userRole : userRoles){
			viewModels.add(dozerMapper.map(userRole, UserRoleViewModel.class));
		}
		
		return viewModels;
	}
	
}
