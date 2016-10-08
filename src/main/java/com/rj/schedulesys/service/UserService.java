package com.rj.schedulesys.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.rj.schedulesys.dao.FacilityScheduleDao;
import com.rj.schedulesys.dao.FacilityScheduleUpdateDao;
import com.rj.schedulesys.dao.PrivateCareScheduleDao;
import com.rj.schedulesys.dao.PrivateCareScheduleUpdateDao;
import com.rj.schedulesys.dao.ScheduleSysUserDao;
import com.rj.schedulesys.dao.UserRoleDao;
import com.rj.schedulesys.domain.ScheduleSysUser;
import com.rj.schedulesys.domain.UserRole;
import com.rj.schedulesys.util.ObjectValidator;
import com.rj.schedulesys.util.PasswordHashUtil;
import com.rj.schedulesys.view.model.PasswordResetRequestViewModel;
import com.rj.schedulesys.view.model.PasswordUpdateViewModel;
import com.rj.schedulesys.view.model.ScheduleSysUserViewModel;
import com.rj.schedulesys.view.model.UserActivityViewModel;
import com.rj.schedulesys.view.model.UserProfileViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
	
	private UserRoleDao userRoleDao;
	private ScheduleSysUserDao userDao;
	private FacilityScheduleDao facilityScheduleDao;
	private PrivateCareScheduleDao privateCareScheduleDao;
	private FacilityScheduleUpdateDao facilityScheduleUpdateDao;
	private PrivateCareScheduleUpdateDao privateCareScheduleUpdateDao;
	
	private DozerBeanMapper dozerMapper;
	
	private ObjectValidator<ScheduleSysUserViewModel> userValidator;
	private ObjectValidator<UserProfileViewModel> userProfileValidator;
	private ObjectValidator<PasswordUpdateViewModel> passwordViewModelValidator;
	
	
	@Autowired
	public UserService(UserRoleDao userRoleDao, ScheduleSysUserDao userDao, FacilityScheduleDao facilityScheduleDao,
			PrivateCareScheduleDao privateCareScheduleDao, FacilityScheduleUpdateDao facilityScheduleUpdateDao,
			PrivateCareScheduleUpdateDao privateCareScheduleUpdateDao,
			ObjectValidator<ScheduleSysUserViewModel> userValidator, ObjectValidator<UserProfileViewModel> userProfileValidator,
			ObjectValidator<PasswordUpdateViewModel> passwordViewModelValidator, DozerBeanMapper dozerMapper) {
		this.userRoleDao = userRoleDao;
		this.userDao = userDao;
		this.dozerMapper = dozerMapper;
		this.userValidator = userValidator;
		this.userProfileValidator = userProfileValidator;
		this.passwordViewModelValidator = passwordViewModelValidator;
		
		this.facilityScheduleDao = facilityScheduleDao;
		this.privateCareScheduleDao = privateCareScheduleDao;
		this.facilityScheduleUpdateDao = facilityScheduleUpdateDao;
		this.privateCareScheduleUpdateDao = privateCareScheduleUpdateDao;
	}
	
	/**
	 * @param viewModel : user to be created
	 * @return Created user
	 */
	@Transactional
	public ScheduleSysUserViewModel create(ScheduleSysUserViewModel viewModel){
		Assert.notNull(viewModel, "No user provided");
		log.debug("Creating new user : {}", viewModel);
		
		String errorMessage = "";
		if(userDao.findByUsername(viewModel.getUsername()) != null){
			errorMessage = new StringBuilder()
					.append("A user with username '")
					.append(viewModel.getUsername())
					.append("' ")
					.append("already exists").toString();
		}else if(userDao.findByEmailAddress(viewModel.getEmailAddress()) != null){
			errorMessage = new StringBuilder()
					.append("Email address '")
					.append(viewModel.getEmailAddress())
					.append("' ")
					.append("already in use").toString();
		}
		
		if(StringUtils.isNoneBlank(errorMessage)){
			log.error(errorMessage);
			throw new RuntimeException(errorMessage);
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
		String errorMessage = "";
		if(!StringUtils.equals(user.getUsername(), viewModel.getUsername())){
			log.warn("Username altered, checking its uniqueness");
			if(userDao.findByUsername(viewModel.getUsername()) != null){
				errorMessage = new StringBuilder()
						.append("A user with username '")
						.append(viewModel.getUsername())
						.append("' ")
						.append("already exists").toString();
			}
		}else if(!StringUtils.equals(user.getEmailAddress(), viewModel.getEmailAddress())){
			log.warn("Email address altered, checking its uniqueness");
			if(userDao.findByEmailAddress(viewModel.getEmailAddress()) != null){
				errorMessage = new StringBuilder()
						.append("Email address '")
						.append(viewModel.getEmailAddress())
						.append("' ")
						.append("already in use").toString();
			}
		}
		
		if(StringUtils.isNoneBlank(errorMessage)){
			log.error(errorMessage);
			throw new RuntimeException(errorMessage);
		}
		
		viewModel = this.createOrUpdate(viewModel);
		return viewModel;
	}
	
	/**
	 * @param viewModel
	 * @return
	 */
	public ScheduleSysUserViewModel createOrUpdate(ScheduleSysUserViewModel viewModel){
		userValidator.validate(viewModel);
		UserRole userRole = validateUserRole(viewModel.getUserRole());
		ScheduleSysUser user = null; 
		if(viewModel.getId() != null){
			user = userDao.findOne(viewModel.getId());
		}else{
			user = ScheduleSysUser.builder().build();
		}
		user.setUsername(viewModel.getUsername());
		user.setEmailAddress(viewModel.getEmailAddress());
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
	
	@Transactional
	public void createPassword(UserProfileViewModel viewModel) throws Exception{
		Assert.notNull(viewModel, "User profile needed");
		userProfileValidator.validate(viewModel);
		log.info("Fetching user with token : {}", viewModel.getActivationToken());
		ScheduleSysUser user = userDao.findByToken(viewModel.getActivationToken());
		if(user == null){
			log.error("Invalid token '{}'", viewModel.getActivationToken());
			throw new RuntimeException("Invalid activation token");
		}
		user.setPasswordhash(PasswordHashUtil.createHash(viewModel.getPassword()));
		user.setIsActivated(Boolean.TRUE);
		user.setToken(null);//Remove the token.//TODO Create a table specifically for tokens(id, userId, create_date, expiration_date)
		userDao.merge(user);
	}
	
	@Transactional
	public void updatePassword(PasswordUpdateViewModel viewModel, String username) throws Exception{
		Assert.notNull(viewModel, "No password update view model provided");
		passwordViewModelValidator.validate(viewModel);
		ScheduleSysUser user = userDao.findByUsername(username);
		if(user == null){
			log.error("No user found with username : {}", username);
			throw new RuntimeException("No user found with username : " + username);
		}
		boolean isPasswordValid = PasswordHashUtil.validatePassword(viewModel.getOldPassword(), user.getPasswordhash());
		if(!isPasswordValid){
			log.error("Incorrect password provided");
			throw new RuntimeException("The old password provided is incorrect");
		}
		user.setPasswordhash(PasswordHashUtil.createHash(viewModel.getNewPassword()));
		userDao.merge(user);
	}
	
	@Transactional
	public void createPasswordResetToken(PasswordResetRequestViewModel viewModel){
		Assert.notNull(viewModel, "No password reset view model provided");
		log.info("Creating password reset token for user with username or email address : {}", viewModel.getUsernameOrEmailAddress());
		ScheduleSysUser user = userDao.findByEmailAddress(viewModel.getUsernameOrEmailAddress());
		if(user == null){
			user = userDao.findByUsername(viewModel.getUsernameOrEmailAddress());
			if(user == null){
				log.error("No user found with username or email address : {}", viewModel.getUsernameOrEmailAddress());
				throw new RuntimeException("No user found with username or email address : " + viewModel.getUsernameOrEmailAddress());
			}
		}
		user.setToken(UUID.randomUUID().toString());
		userDao.merge(user);
	}
	
	public UserActivityViewModel getActivityLogs(Date startDate, Date endDate, String username){
		int facilitySchedulesCreated = facilityScheduleDao.findAllByDatesAndUser(startDate, endDate, username).size();
		int facilitySchedulesUpdated = facilityScheduleUpdateDao.findByDatesByUser(new DateTime(startDate), new DateTime(endDate), username).size();
		
		int privateCareSchedulesCreated = privateCareScheduleDao.findAllByDatesAndUser(startDate, endDate, username).size();
		int privateCareSchedulesUpdated = privateCareScheduleUpdateDao.findByDatesByUser(new DateTime(startDate), new DateTime(endDate), username).size();
		UserActivityViewModel activity = UserActivityViewModel.builder()
				.schedulesCreated(facilitySchedulesCreated + privateCareSchedulesCreated)
				.schedulesUpdated(facilitySchedulesUpdated + privateCareSchedulesUpdated)
				.build();
		return activity;
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
	 * @param usermame
	 * @return
	 */
	public ScheduleSysUser loadByUsername(String username){
		log.debug("Loading user by username : {}", username);
		return userDao.findByUsername(username);
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
	 * @param roleName
	 * @return userRole : Entire role object.
	 * @throws RuntimeException : When the role is not found
	 */
	public UserRole validateUserRole(String roleName){
		UserRole userRole = userRoleDao.findByRole(roleName);
		if(userRole == null){
			log.error("No such user role : {}", roleName);
			throw new RuntimeException("No such user role : " + roleName);
		}
		return userRole;
	}

}