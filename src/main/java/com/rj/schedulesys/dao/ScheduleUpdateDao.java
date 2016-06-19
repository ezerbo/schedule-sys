package com.rj.schedulesys.dao;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.ScheduleUpdate;

@Repository
public class ScheduleUpdateDao extends GenericDao<ScheduleUpdate> {
	
	public ScheduleUpdateDao() {
		setClazz(ScheduleUpdate.class);
	}
}
