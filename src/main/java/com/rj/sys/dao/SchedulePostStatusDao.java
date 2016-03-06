package com.rj.sys.dao;

import org.springframework.stereotype.Repository;

import com.rj.sys.domain.SchedulePostStatus;

@Repository
public class SchedulePostStatusDao extends GenericDao<SchedulePostStatus> {
	
	public SchedulePostStatusDao() {
		setClazz(SchedulePostStatus.class);
	}
}
