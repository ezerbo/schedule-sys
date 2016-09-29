package com.rj.schedulesys.dao;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.PrivateCareScheduleUpdate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PrivateCareScheduleUpdateDao extends GenericDao<PrivateCareScheduleUpdate>{

	public PrivateCareScheduleUpdateDao() {
		setClazz(PrivateCareScheduleUpdate.class);
	}
	

	/**	
	 * @return
	 */
	public PrivateCareScheduleUpdate findLatestByScheduleId(Long scheduleId){
		PrivateCareScheduleUpdate scheduleUpdate = null;
		try {
			scheduleUpdate = entityManager.createQuery("from PrivateCareScheduleUpdate pcsu where pcsu.schedule.id =:scheduleId "
					+ " and pcsu.updateTime = (select max(updateTime) from PrivateCareScheduleUpdate pcsu where pcsu.schedule.id =:scheduleId)", PrivateCareScheduleUpdate.class)
					.setParameter("scheduleId", scheduleId)
					.getSingleResult();
		} catch (NoResultException e) {
			log.warn("No schedule update found with schedule id : {}", scheduleId);
		}
		return scheduleUpdate;
	}
}
