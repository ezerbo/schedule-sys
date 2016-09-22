package com.rj.schedulesys.dao;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.FacilityScheduleUpdate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class FacilityScheduleUpdateDao extends GenericDao<FacilityScheduleUpdate> {
	
	public FacilityScheduleUpdateDao() {
		setClazz(FacilityScheduleUpdate.class);
	}
	
	/**
	 * @param scheduleId
	 * @return
	 */
	public FacilityScheduleUpdate findLatestByScheduleId(Long scheduleId){
		FacilityScheduleUpdate scheduleUpdate = null;
		try {
			scheduleUpdate = entityManager.createQuery("from FacilityScheduleUpdate fsu where fsu.schedule.id =:scheduleId "
					+ " and fsu.updateTime = (select max(updateTime) from FacilityScheduleUpdate fsu where fsu.schedule.id =:scheduleId)", FacilityScheduleUpdate.class)
					.setParameter("scheduleId", scheduleId)
					.getSingleResult();
		} catch (NoResultException e) {
			log.warn("No schedule update found with schedule id : {}", scheduleId);
		}
		return scheduleUpdate;
	}
}
