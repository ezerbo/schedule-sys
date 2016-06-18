package com.rj.sys.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.LinkedList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.rj.sys.dao.ScheduleSysUserDao;
import com.rj.sys.dao.UserRoleDao;
import com.rj.sys.domain.ScheduleSysUser;
import com.rj.sys.domain.UserRole;
import com.rj.sys.utils.PasswordHashUtil;
import com.rj.sys.view.model.ScheduleSysUserViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
	
	private @Autowired ScheduleSysUserDao userDao;
	private @Autowired UserRoleDao userRoleDao;
	private @Autowired DozerBeanMapper dozerMapper;
	
	/**
	 * @param viewModel : user to be created
	 * @return Created user
	 */
	@Transactional
	public ScheduleSysUserViewModel createOrUpdate(ScheduleSysUserViewModel viewModel){
		
		log.info("Creating new user : {}", viewModel);
		Assert.notNull(viewModel, "No user provided");
		
		UserRole userRole = userRoleDao.findByRole(viewModel.getUserRole());
		if(userRole == null){
			log.error("No such user role : {}", viewModel.getUserRole());
			throw new RuntimeException("No such user role : " + userRole);
		}
		
		ScheduleSysUser user = userDao.findByUsername(viewModel.getUsername());
		if(user != null){
			log.error("A user with username : {} already exists", viewModel.getUsername());
			throw new RuntimeException("A user with username : " + viewModel.getUsername() + " already exists");
		}
		
		user = dozerMapper.map(viewModel, ScheduleSysUser.class);
		
		//Build password hash
		try {
			user.setPasswordhash(PasswordHashUtil.createHash(viewModel.getPassword()));
		} catch (NoSuchAlgorithmException|InvalidKeySpecException e) {
			log.error("Unable to create password hash");
			throw new RuntimeException("Something unexpected happened");
		}
		
		user.setUserRole(userRole);
		
		user = userDao.merge(user);
		
		return dozerMapper.map(user, ScheduleSysUserViewModel.class);
	}
	
	/**
	 * Hard deletes a user
	 * @param emailAddress : email address of the user to be deleted
	 */
	@Transactional
	public void deleteUser(Long id){
		log.info("Deleting user with id : {}", id);
		//ScheduleSysUser user = userDao.delete(id);
		//log.info("User : {} has been deleted", user);
	}
	
	public ScheduleSysUserViewModel findOne(Long id){
		log.info("Fetching user with id : {}", id);
		ScheduleSysUser user = userDao.findOne(id);
		ScheduleSysUserViewModel viewModel = null;
		if(user != null){
			viewModel = dozerMapper.map(user, ScheduleSysUserViewModel.class);
		}
		return viewModel;
	}
	
	public ScheduleSysUserViewModel findByUsername(String username){
		log.info("Fetching user with username : {}", username);
		ScheduleSysUser user = userDao.findByUsername(username);
		ScheduleSysUserViewModel viewModel = null;
		if(user != null){
			viewModel = dozerMapper.map(user, ScheduleSysUserViewModel.class);
		}
		return viewModel;
	}
	
	
	/**
	 * @param userRole
	 * @return users
	 */
	@Transactional
	public List<ScheduleSysUserViewModel> findAllByRole(String userRole){
		log.info("Finding all users with role : {}", userRole);
		List<ScheduleSysUser> users = userDao.findAllUsersByRole(userRole);
		
		List<ScheduleSysUserViewModel> viewModels = new LinkedList<ScheduleSysUserViewModel>();
		for(ScheduleSysUser user : users){
			viewModels.add(dozerMapper.map(user, ScheduleSysUserViewModel.class));
		}
		
		return viewModels;
	}
	
	/**
	 * @return users
	 */
	public List<ScheduleSysUserViewModel> findAll(){
		log.info("Fetching all users");
		List<ScheduleSysUser> users = userDao.findAll();
		List<ScheduleSysUserViewModel> viewModels = new LinkedList<ScheduleSysUserViewModel>();
		for(ScheduleSysUser user : users){
			viewModels.add(dozerMapper.map(user, ScheduleSysUserViewModel.class));
		}
		return viewModels;
	}

}