package com.rj.schedulesys.dao;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.ScheduleStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ScheduleStatusDao extends GenericDao<ScheduleStatus> {
	
	public ScheduleStatusDao() {
		setClazz(ScheduleStatus.class);
	}
	
	/**
	 * @param status
	 * @return
	 */
	public ScheduleStatus findByStatus(String status){
		
		ScheduleStatus scheduleStatus = null;
		
		try{
			scheduleStatus = entityManager.createQuery(
					"from ScheduleStatus ss where ss.status =:status", ScheduleStatus.class)
			.setParameter("status", status)
			.getSingleResult();
		}catch(Exception e){
			log.warn("No schedule status found with status : {}", status);
		}
		
		return scheduleStatus;
	}
}
