package com.rj.schedulesys.dao;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.SchedulePostStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class SchedulePostStatusDao extends GenericDao<SchedulePostStatus> {
	
	public SchedulePostStatusDao() {
		setClazz(SchedulePostStatus.class);
	}
	
public SchedulePostStatus findByStatus(String status){
		
		SchedulePostStatus schedulePostStatus = null;
		
		try{
			schedulePostStatus = entityManager.createQuery(
				"from SchedulePostStatus sps where sps.status =:status", SchedulePostStatus.class)
				.setParameter("status", status)
				.getSingleResult();
		}catch(NoResultException e){
			log.warn("No schedule pos status found with status : {}", status);
		}
		
		return schedulePostStatus;
	}
}
