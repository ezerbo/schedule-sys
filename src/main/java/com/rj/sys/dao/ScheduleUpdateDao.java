package com.rj.sys.dao;

import org.springframework.stereotype.Repository;

import com.rj.sys.domain.ScheduleUpdate;

@Repository
public class ScheduleUpdateDao extends GenericDao<ScheduleUpdate> {
	
	public ScheduleUpdateDao() {
		setClazz(ScheduleUpdate.class);
	}
}
