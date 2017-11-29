package com.ss.schedulesys.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.schedulesys.domain.ScheduleSummary;
import com.ss.schedulesys.repository.ScheduleSummaryRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ScheduleSummaryService {

	private ScheduleSummaryRepository scheduleSummaryRepository;
	
	@Autowired
	public ScheduleSummaryService(ScheduleSummaryRepository scheduleSummaryRepository) {
		this.scheduleSummaryRepository = scheduleSummaryRepository;
	}
	
	public List<ScheduleSummary> getSchedulesSummary(Date scheduleDate) throws Exception {
		log.debug("Getting schedules summary for : {}", scheduleDate);
		return scheduleSummaryRepository.getSchedulesSummary(scheduleDate);
	}
}
