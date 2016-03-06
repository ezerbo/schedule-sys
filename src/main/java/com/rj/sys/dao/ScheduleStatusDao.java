package com.rj.sys.dao;

import org.springframework.stereotype.Repository;

import com.rj.sys.domain.ScheduleStatus;

@Repository
public class ScheduleStatusDao extends GenericDao<ScheduleStatus> {
	
	public ScheduleStatusDao() {
		setClazz(ScheduleStatus.class);
	}
}
