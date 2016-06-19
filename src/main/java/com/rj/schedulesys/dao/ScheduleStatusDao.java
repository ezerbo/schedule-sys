package com.rj.schedulesys.dao;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.ScheduleStatus;

@Repository
public class ScheduleStatusDao extends GenericDao<ScheduleStatus> {
	
	public ScheduleStatusDao() {
		setClazz(ScheduleStatus.class);
	}
	
	public ScheduleStatus findByStatus(String status){
		
		ScheduleStatus statusSchedule = entityManager.createQuery(
				"from ScheduleStatus ss where ss.status =:status", ScheduleStatus.class)
				.setParameter("status", status)
				.getSingleResult();
		
		return statusSchedule;
	}
}
