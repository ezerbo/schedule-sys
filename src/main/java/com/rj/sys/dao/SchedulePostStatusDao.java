package com.rj.sys.dao;

import org.springframework.stereotype.Repository;

import com.rj.sys.domain.SchedulePostStatus;

@Repository
public class SchedulePostStatusDao extends GenericDao<SchedulePostStatus> {
	
	public SchedulePostStatusDao() {
		setClazz(SchedulePostStatus.class);
	}
	
public SchedulePostStatus findByStatus(String status){
		
		SchedulePostStatus statusPostSchedule = entityManager.createQuery(
				"from SchedulePostStatus sps where sps.status =:status", SchedulePostStatus.class)
				.setParameter("status", status)
				.getSingleResult();
		
		return statusPostSchedule;
	}
}
