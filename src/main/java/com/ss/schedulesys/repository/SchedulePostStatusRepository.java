package com.ss.schedulesys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.schedulesys.domain.SchedulePostStatus;

public interface SchedulePostStatusRepository extends JpaRepository<SchedulePostStatus, Long> {
	
	/**
	 * @param name
	 * @return
	 */
	public SchedulePostStatus findByName(String name);
	
	
}
