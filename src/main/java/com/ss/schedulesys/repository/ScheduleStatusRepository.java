package com.ss.schedulesys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.schedulesys.domain.ScheduleStatus;

public interface ScheduleStatusRepository extends JpaRepository<ScheduleStatus, Long> {

	public ScheduleStatus findByName(String name);
}
