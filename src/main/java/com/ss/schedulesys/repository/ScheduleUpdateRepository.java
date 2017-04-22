package com.ss.schedulesys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.schedulesys.domain.ScheduleUpdate;

/**
 * @author ezerbo
 *
 */
public interface ScheduleUpdateRepository extends JpaRepository<ScheduleUpdate, Long> {
	
	public ScheduleUpdate findByScheduleId(Long scheduleId);

}
