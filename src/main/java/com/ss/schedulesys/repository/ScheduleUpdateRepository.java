package com.ss.schedulesys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.schedulesys.domain.ScheduleUpdate;
import com.ss.schedulesys.domain.ScheduleUpdateId;

public interface ScheduleUpdateRepository extends JpaRepository<ScheduleUpdate, ScheduleUpdateId> {
	
	/**
	 * @param scheduleId
	 * @return
	 */
	public ScheduleUpdate findByScheduleId(Long scheduleId);

}
