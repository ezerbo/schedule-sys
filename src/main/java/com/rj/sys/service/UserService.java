package com.rj.sys.service;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.sys.dao.PositionDao;
import com.rj.sys.dao.UserDao;
import com.rj.sys.dao.UserTypeDao;
import com.rj.sys.domain.Position;
import com.rj.sys.domain.User;
import com.rj.sys.domain.UserType;
import com.rj.sys.dto.UserTO;
import com.rj.sys.view.model.UserViewModel;

@Slf4j
@Service
public class UserService {
	
	private @Autowired UserDao userDao;
	private @Autowired PositionDao positionDao;
	private @Autowired UserTypeDao userTypeDao;
	
	private @Autowired DozerBeanMapper dozerMapper;
	
	/**
	 * @param userTO
	 * @return Created user
	 */
	@Transactional
	public User createUser(UserTO userTO){
		//TODO set the position base on the user type
		User user = dozerMapper.map(userTO, User.class);
		log.debug("Creating new User : {}", user);
		
		//set type and position
		Position position = positionDao.findByName(userTO.getPositionType());
		user.setPosition(position);
		
		UserType type = userTypeDao.findByType(userTO.getType());
		user.setUserType(type);
		
		user.setDeleted(false);
		
		user = userDao.merge(user);
		
		return user;
	}
	
	@Transactional
	public List<UserViewModel> findAllActiveUsers(){
		log.info("Finding all users ");
		List<User> activeUsers = userDao.findAllActiveUsers();
		log.debug("Active users found : {}", activeUsers);
		List<UserViewModel> viewModels = new LinkedList<UserViewModel>();
		for(User user : activeUsers){
			viewModels.add(dozerMapper.map(user, UserViewModel.class));
		}
		return viewModels;
	}
	
	@Transactional
	public UserViewModel findOneActiveUserById(Long id){
		log.info("Finding one user by id : {}", id);
		User user = userDao.findOneActiveUserById(id);
		UserViewModel viewModel = dozerMapper.map(user, UserViewModel.class);
		return viewModel;
	}
	
	/**
	 * Hard deletes a user
	 * @param emailAddress : email address of the user to be deleted
	 */
	@Transactional
	public void deleteUser(String emailAddress){
		log.info("Hard deleting user with email address : {}", emailAddress);
		
	}
	
	/**
	 * 
	 * @param user : user to be updated
	 * @return Updated user
	 */
	@Transactional
	public User updateUser(UserTO user){
		log.debug("Updating user : {}", user);
		return null;
	}
	
	/**
	 * Deactivates user using his (her) email address
	 * @param emailAddress
	 * @return Deactivated user
	 */
	@Transactional
	public User deactivateUser(String emailAddress){
		
		log.debug("Soft deleting a user with username : {}", emailAddress);
		User user = userDao.findOneByEmailAddress(emailAddress);
		user.setDeleted(true);//soft deletes the user
		
		user = userDao.merge(user);
		
		return user;
	}
}