package com.ss.schedulesys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ss.schedulesys.domain.ScheduleUpdate;

/**
 * @author ezerbo
 *
 */
public interface ScheduleUpdateRepository extends JpaRepository<ScheduleUpdate, Long> {
	
	@Query("from ScheduleUpdate su where su.schedule.id = :scheduleId")
	public List<ScheduleUpdate> findByScheduleId(@Param("scheduleId") Long scheduleId);

}
