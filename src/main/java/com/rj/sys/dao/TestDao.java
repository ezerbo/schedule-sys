package com.rj.sys.dao;

import org.springframework.stereotype.Repository;

import com.rj.sys.domain.Test;

@Repository
public class TestDao extends GenericDao<Test> {
	
	public TestDao() {
		setClazz(Test.class);
	}
}
