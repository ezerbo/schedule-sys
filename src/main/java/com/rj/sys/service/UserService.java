package com.rj.sys.service;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.NoResultException;

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
import com.rj.sys.view.model.EmployeeViewModel;
import com.rj.sys.view.model.SupervisorViewModel;

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
	public EmployeeViewModel createOrUpdateEmployee(EmployeeViewModel viewModel){
		//TODO set the position base on the user type
		User employee = dozerMapper.map(viewModel, User.class);
		log.info("creating employee : {}", employee);
		
		//set type and position
		Position position = positionDao.findByName(viewModel.getPosition());
		employee.setPosition(position);
		
		UserType userType = userTypeDao.findByType(UserType.employeesType);
		employee.setUserType(userType);
		employee.setDeleted(false);
		employee = userDao.merge(employee);
		
		return dozerMapper.map(employee, EmployeeViewModel.class);
	}
	
	@Transactional
	public SupervisorViewModel createOrUpdateSupervisor(SupervisorViewModel viewModel){
		log.info("Creating supervisor : {}", viewModel);
		User supervisor = dozerMapper.map(viewModel, User.class);
		
		UserType userType = userTypeDao.findByType(UserType.supervisorsType);
		supervisor.setUserType(userType);
		
		supervisor.setDeleted(false);
		supervisor = userDao.merge(supervisor);
		
		return dozerMapper.map(supervisor, SupervisorViewModel.class);
	}
	
	@Transactional
	public boolean isEmailAddressExistant(String emailAddress){
		try{
			User user = userDao.findByEmailAddress(emailAddress);
			log.info("user : {} found by emailAddress : {}", user, emailAddress);
		}catch(NoResultException nre){
			log.info("No user found with email address : {}", emailAddress);
			return false;
		}
		return true;
	}
	
	@Transactional
	public boolean isPrimaryPhoneNumberExistant(String primaryPhoneNumber){
		try{
			User user = userDao.findByPrimaryPhoneNumber(primaryPhoneNumber);
			log.info("user : {} found by primary phone number : {}", user, primaryPhoneNumber);
		}catch(NoResultException nre){
			log.info("No user found with primary phone number : {}", primaryPhoneNumber);
			return false;
		}
		return true;
	}
	
	@Transactional
	public boolean isSecondaryPhoneNumberExistant(String secondaryPhoneNumber){
		try{
			User user = userDao.findBySecondaryPhoneNumber(secondaryPhoneNumber);
			log.info("user : {} found by secondary phone number {}", user, secondaryPhoneNumber);
		}catch(NoResultException nre){
			log.info("No user found with secondary phone number : {}", secondaryPhoneNumber);
			return false;
		}
		return true;
	}
	
	@Transactional
	public boolean isOtherPhoneNumberExistant(String otherPhoneNumber){
		try{
			User user = userDao.findByOtherPhoneNumber(otherPhoneNumber);
			log.info("user : {} found by other phone number : {}", user, otherPhoneNumber);
		}catch(NoResultException nre){
			log.info("No user found with other phone number : {}", otherPhoneNumber);
			return false;
		}
		return true;
	}
	
	@Transactional
	public List<EmployeeViewModel> findAllActiveUsers(){
		log.info("Finding all users ");
		List<User> activeUsers = userDao.findAllActiveUsers();
		log.debug("Active users found : {}", activeUsers);
		List<EmployeeViewModel> viewModels = new LinkedList<EmployeeViewModel>();
		for(User user : activeUsers){
			viewModels.add(dozerMapper.map(user, EmployeeViewModel.class));
		}
		return viewModels;
	}
	
	@Transactional
	public EmployeeViewModel findOneActiveUserById(Long id){
		log.info("Finding one user by id : {}", id);
		EmployeeViewModel viewModel = null;
		try{
			viewModel = dozerMapper.map(
					userDao.findOneActiveUserById(id), EmployeeViewModel.class
					);
		}catch(NoResultException nre){
			log.info("No user found with id : {}", id);
		}
		
		return viewModel;
	}
	
	@Transactional
	public EmployeeViewModel findEmployeeById(Long id){
		log.info("Finding employee by id : {}", id);
		EmployeeViewModel viewModel = null;
		try{
			viewModel = dozerMapper.map(
					userDao.findEmployeeById(id), EmployeeViewModel.class
					);
		}catch(NoResultException nre){
			log.info("No employee found with id : {}", id);
		}
		
		return viewModel;
	}
	
	@Transactional
	public SupervisorViewModel findSupervisorById(Long id){
		log.info("Finding supervisor by id : {}", id);
		SupervisorViewModel viewModel = null;
		try{
			viewModel = dozerMapper.map(
					userDao.findSupervisorById(id), SupervisorViewModel.class
					);
		}catch(NoResultException nre){
			log.info("No supervisor found by id : {}", id);
		}
		
		return viewModel;
	}
	
	/**
	 * Hard deletes a user
	 * @param emailAddress : email address of the user to be deleted
	 */
	@Transactional
	public void deleteUser(Long id){
		log.info("Deleting user with id : {}", id);
		User user = userDao.delete(id);
		log.info("User : {} has been deleted", user);
	}
	
	
	/**
	 * @return
	 */
	@Transactional
	public List<EmployeeViewModel> findAllEmployees(){
		log.info("Finding all employees");
		List<User> employees = userDao.findAllEmployees();
		List<EmployeeViewModel> viewModels = new LinkedList<EmployeeViewModel>();
		for(User employee : employees){
			viewModels.add(dozerMapper.map(employee, EmployeeViewModel.class));
		}
		
		return viewModels;
	}
	
	/**
	 * @return
	 */
	@Transactional
	public List<SupervisorViewModel> findAllSupervisors(){
		log.info("Finding all superviors");
		List<User> supervisors = userDao.findAllSupervisors();
		List<SupervisorViewModel> viewModels = new LinkedList<SupervisorViewModel>();
		for(User supervisor : supervisors){
			viewModels.add(dozerMapper.map(supervisor, SupervisorViewModel.class));
		}
		
		return viewModels;
	}

}