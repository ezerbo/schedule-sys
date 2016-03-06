package com.rj.sys.dao;

import org.springframework.stereotype.Repository;

import com.rj.sys.domain.Schedule;

@Repository
public class ScheduleDao extends GenericDao<Schedule> {
	
	public ScheduleDao() {
		setClazz(Schedule.class);
	}
}
