package com.rj.schedulesys.dao;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.UserRole;

import lombok.extern.slf4j.Slf4j;


/**
 * @author ezerbo
 *
 */
@Slf4j
@Repository
public class UserRoleDao extends GenericDao<UserRole>{

	public UserRoleDao() {
		setClazz(UserRole.class);
	}
	
	/**
	 * @param role : Name of the role
	 * @return userRole : Entire role object
	 */
	public UserRole findByRole(String role){
		
		UserRole userRole =  null;
		
		try{
			userRole = entityManager.createQuery(
					"from UserRole ur where ur.userRole = :userRole", UserRole.class)
			.setParameter("userRole", role)
			.getSingleResult();
		}catch(NoResultException e){
			log.warn("No such role : ", role);
		}
		
		return userRole;
	}
	
}
