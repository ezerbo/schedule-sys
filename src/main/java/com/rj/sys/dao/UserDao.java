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
				"from User u where u.isDeleted = false and u.id =:id", User.class)
				.setParameter("id", id)
				.getSingleResult()
		;
		return user;
	}
	
}