package com.rj.schedulesys.dao;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.ScheduleUpdate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ScheduleUpdateDao extends GenericDao<ScheduleUpdate> {
	
	public ScheduleUpdateDao() {
		setClazz(ScheduleUpdate.class);
	}
	
	/**
	 * @param scheduleId
	 * @return
	 */
	public ScheduleUpdate findLatestByScheduleId(Long scheduleId){
		ScheduleUpdate scheduleUpdate = null;
		try {
			scheduleUpdate = entityManager.createQuery("from ScheduleUpdate su where su.schedule.id =:scheduleId "
					+ "and su.updateTime in (select max(updateTime) from ScheduleUpdate)", ScheduleUpdate.class)
					.setParameter("scheduleId", scheduleId)
					.getSingleResult();
		} catch (NoResultException e) {
			log.warn("No schedule update found with schedule id : {}", scheduleId);
		}
		return scheduleUpdate;
	}
}
