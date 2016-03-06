package com.rj.sys.dao;

import org.springframework.stereotype.Repository;

import com.rj.sys.domain.UserType;

@Repository
public class UserTypeDao extends GenericDao<UserType>{
	public UserTypeDao() {
		setClazz(UserType.class);
	}
	
	/**
	 * @param userType
	 * @return
	 */
	public UserType findByType(String userType){
		UserType type = entityManager.createQuery(
				"from UserType ut where ut.type = :userType", UserType.class)
				.setParameter("userType", userType)
				.getSingleResult()
		;
		return type;
	}
	
}
