package com.rj.sys.dao;

import org.springframework.stereotype.Repository;

import com.rj.sys.domain.UserTest;

@Repository
public class UserTestDao extends GenericDao<UserTest> {
	
	public UserTestDao() {
		setClazz(UserTest.class);
	}
}
