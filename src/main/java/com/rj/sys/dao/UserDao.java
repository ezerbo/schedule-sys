package com.rj.sys.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.sys.domain.User;

@Repository
public class UserDao extends GenericDao<User> {
	
	public UserDao() {
		setClazz(User.class);
	}
	
	public User delete(Long id){
		User user = findOneActiveUserById(id);
		user.setDeleted(true);
		return user;
	}
	
	public User findOneByEmailAddress(String emailAddress){
		User user = entityManager.createQuery(
				"from User u where u.isDeleted = false and u.emailAddress =:emailAddress", User.class)
				.setParameter("emailAddress", emailAddress)
				.getSingleResult()
		;
		return user;
	}
	
	/**
	 * @return activeUsers: all active users in the system
	 */
	public List<User> findAllActiveUsers(){
		List<User> users = entityManager.createQuery(
				"from User u where u.isDeleted = false", User.class)
				.getResultList()
		;
		return users;
	}
	
	/**
	 * @param id
	 * @return activeUser : single active user using the user's id
	 * @throws NoResultException : When no user could be found with given id
	 */
	public User findOneActiveUserById(Long id) throws NoResultException{
		User user = entityManager.createQuery(
				"from User u where u.isDeleted = false "
				+ "and u.id =:id", User.class)
				.setParameter("id", id)
				.getSingleResult()
		;
		return user;
	}
	
	public User findEmployeeById(Long id) throws NoResultException{
		User user = entityManager.createQuery(
				"from User u where u.isDeleted = false "
				+ "and u.id =:id "
				+ "and u.userType.type = 'EMPLOYEE' "
				, User.class)
				.setParameter("id", id)
				.getSingleResult()
		;
		return user;
	}
	
	/**
	 * @return
	 */
	public List<User> findAllEmployees(){
		List<User> employees = entityManager.createQuery(
				"from User u where u.userType.type = 'EMPLOYEE' "
				+ "and u.isDeleted = false",User.class)
				.getResultList();
		return employees;
	}
	
	/**
	 * @return
	 */
	public List<User> findAllSupervisors(){
		List<User> supervisors = entityManager.createQuery(
				"from User u where u.userType.type = 'SUPERVISOR' "
				+ "and u.isDeleted = false",User.class)
				.getResultList();
		return supervisors;
	}
	
	/**
	 * @param id
	 * @return
	 */
	public User findSupervisorById(Long id){
		User supervisor = entityManager.createQuery(
				"from User u where u.isDeleted = false "
				+ "and u.userType.type = 'SUPERVISOR' "
				+ "and u.id =:id", User.class)
				.setParameter("id", id)
				.getSingleResult();
		return supervisor;
	}
	
	/**
	 * @param emailAddress
	 * @return
	 * @throws NoResultException
	 */
	public User findByEmailAddress(String emailAddress) throws NoResultException{
		User user = entityManager.createQuery(
				"from User u where u.isDeleted = false and u.emailAddress =:emailAddress", User.class)
				.setParameter("emailAddress", emailAddress)
				.getSingleResult()
		;
		return user;
	}
	
	public User findByPrimaryPhoneNumber(String primaryPhoneNumber) throws NoResultException{
		User user = entityManager.createQuery(
				"from User u where u.isDeleted = false and u.primaryPhoneNumber =:primaryPhoneNumber", User.class)
				.setParameter("primaryPhoneNumber", primaryPhoneNumber)
				.getSingleResult()
		;
		return user;
	}
	
	public User findBySecondaryPhoneNumber(String secondaryPhoneNumber) throws NoResultException{
		User user = entityManager.createQuery(
				"from User u where u.isDeleted = false and u.secondaryPhoneNumber =:secondaryPhoneNumber", User.class)
				.setParameter("secondaryPhoneNumber", secondaryPhoneNumber)
				.getSingleResult()
		;
		return user;
	}
	
	public User findByOtherPhoneNumber(String otherPhoneNumber) throws NoResultException{
		User user = entityManager.createQuery(
				"from User u where u.isDeleted = false and u.otherPhoneNumber =:otherPhoneNumber", User.class)
				.setParameter("otherPhoneNumber", otherPhoneNumber)
				.getSingleResult()
		;
		return user;
	}
	
}