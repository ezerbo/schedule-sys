package com.rj.schedulesys.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.rj.schedulesys.dao.ScheduleSysUserDao;
import com.rj.schedulesys.dao.UserRoleDao;
import com.rj.schedulesys.domain.ScheduleSysUser;
import com.rj.schedulesys.domain.UserRole;
import com.rj.schedulesys.util.ObjectValidator;
import com.rj.schedulesys.util.PasswordHashUtil;
import com.rj.schedulesys.util.ServiceHelper;
import com.rj.schedulesys.view.model.ScheduleSysUserViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
	
	private @Autowired UserRoleDao userRoleDao;
	private @Autowired ScheduleSysUserDao userDao;
	
	private @Autowired ObjectValidator<ScheduleSysUserViewModel> validator;
	
	private @Autowired DozerBeanMapper dozerMapper;
	
	/**
	 * @param viewModel : user to be created
	 * @return Created user
	 */
	@Transactional
	public ScheduleSysUserViewModel create(ScheduleSysUserViewModel viewModel){
		
		Assert.notNull(viewModel, "No user provided");
		
		log.debug("Creating new user : {}", viewModel);
		
		ScheduleSysUser user = userDao.findByUsername(viewModel.getUsername());
		
		if(user != null){
			String errorMessage = new StringBuilder()
					.append("A user with username '")
					.append(viewModel.getUsername())
					.append("' ")
					.append("already exists").toString();
			
			ServiceHelper.logAndThrowException(errorMessage);
		}
		
		viewModel = this.createOrUpdate(viewModel);
		
		return viewModel;
	}
	
	/**
	 * @param viewModel
	 * @return
	 */
	@Transactional
	public ScheduleSysUserViewModel update(ScheduleSysUserViewModel viewModel){
		
		Assert.notNull(viewModel, "No user provided");
		
		log.debug("Updating user : {}", viewModel);
		
		ScheduleSysUser user = userDao.findOne(viewModel.getId());
		
		if(!StringUtils.equals(user.getUsername(), viewModel.getUsername())){
			log.warn("Username altered, checking its uniqueness");
			if(userDao.findByUsername(viewModel.getUsername()) != null){
				String errorMessage = new StringBuilder()
						.append("A user with username '")
						.append(viewModel.getUsername())
						.append("' ")
						.append("already exists").toString();
				ServiceHelper.logAndThrowException(errorMessage);
			}
		}
		
		viewModel = this.createOrUpdate(viewModel);
		
		return viewModel;
	}
	
	/**
	 * @param viewModel
	 * @return
	 */
	public ScheduleSysUserViewModel createOrUpdate(ScheduleSysUserViewModel viewModel){
		
		validator.validate(viewModel);//throws RuntimException in case of validation error
		
		UserRole userRole = validateUserRole(viewModel.getUserRole());
		
		ScheduleSysUser user = dozerMapper.map(viewModel, ScheduleSysUser.class);

		user.setPasswordhash(UserService.createPasswordHash(viewModel.getPassword()));
		
		user.setUserRole(userRole);
		
		user = userDao.merge(user);
		
		return dozerMapper.map(user, ScheduleSysUserViewModel.class);
	}
	
	/**
	 * Hard deletes a user
	 * @param id : id of the user to be deleted
	 */
	@Transactional
	public void delete(Long id){
		
		log.debug("Deleting user with id : {}", id);
		ScheduleSysUser scheduleSysUser = userDao.findOne(id);
		  
		if(scheduleSysUser == null){
			log.error("No user found with Id : {}", id);
			throw new RuntimeException("No user found with id : " + id);
		}
		
		if(scheduleSysUser.getSchedules().isEmpty() 
				&& scheduleSysUser.getScheduleUpdates().isEmpty()){
			//User is deleted when (s)he has never created or updated a schedule
			userDao.delete(scheduleSysUser);	
			
		}else{
			log.error("User with id : {} can not be deleted", id);
			throw new RuntimeException("User with id : " + id + " can not be deleted");
		}
		
	}
	
	/**
	 * @param id
	 * @return
	 */
	public ScheduleSysUserViewModel findOne(Long id){
		log.info("Fetching user with id : {}", id);
		ScheduleSysUser user = userDao.findOne(id);
		ScheduleSysUserViewModel viewModel = null;
		if(user != null){
			viewModel = dozerMapper.map(user, ScheduleSysUserViewModel.class);
		}
		return viewModel;
	}
	
	/**
	 * @param username
	 * @return
	 */
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
	
	/**
	 * @param plainPassword
	 * @return
	 */
	public static String createPasswordHash(String plainPassword){
		
		String passwordHash ;
		
		try {
			passwordHash = PasswordHashUtil.createHash(plainPassword);
		} catch (NoSuchAlgorithmException|InvalidKeySpecException e) {
			log.error("Unable to create password hash");
			throw new RuntimeException("Something unexpected happened");
		}
		
		return passwordHash;
	}
	
	/**
	 * @param roleName
	 * @return userRole : Entire role object.
	 * @throws RuntimeException : When the role is not found
	 */
	public UserRole validateUserRole(String roleName){
		
		UserRole userRole = userRoleDao.findByRole(roleName);
		
		if(userRole == null){
			log.error("No such user role : {}", roleName);
			throw new RuntimeException("No such user role : " + userRole);
		}
		
		return userRole;
	}

}