package com.rj.schedulesys.dao;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.ScheduleSysUser;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ezerbo
 *
 */
@Slf4j
@Repository
public class ScheduleSysUserDao extends GenericDao<ScheduleSysUser> {
	
	public ScheduleSysUserDao() {
		setClazz(ScheduleSysUser.class);
	}
	
	/**
	 * @param username
	 * @return user
	 */
	public ScheduleSysUser findByUsername(String username){
		ScheduleSysUser user = null ;
		try{
			user = entityManager.createQuery(
					"from ScheduleSysUser u where u.username =:username", ScheduleSysUser.class)
					.setParameter("username", username)
					.getSingleResult();
		}catch(NoResultException e){
			log.warn("No user found with username : {}", username);
		}
		return user;
	}
	
	/**
	 * @return users : An empty list is return when no user can be found
	 */
	public List<ScheduleSysUser> findAllUsersByRole(String userRole){
		List<ScheduleSysUser> users = new LinkedList<ScheduleSysUser>();
		try{
			users = entityManager.createQuery(
					"from ScheduleSysUser u where u.userRole.userRole =:userRole "
					, ScheduleSysUser.class)
					.setParameter("userRole", userRole)
					.getResultList();
		}catch(NoResultException e){
			log.warn("No user found with role : {}", userRole);
		}
		return users;
	}
	
}