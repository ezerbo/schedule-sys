package com.ss.schedulesys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.ScheduleUpdate;
import com.ss.schedulesys.repository.ScheduleUpdateRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ezerbo
 *
 */

@Slf4j
@Service
@Transactional
public class ScheduleUpdateService {

	private ScheduleUpdateRepository scheduleUpdateRepository;
	
	@Autowired
	public ScheduleUpdateService(ScheduleUpdateRepository scheduleUpdateRepository) {
		this.scheduleUpdateRepository = scheduleUpdateRepository;
	}
	
	
	/**
	 * @param scheduleId
	 * @return
	 */
	@Transactional(readOnly = true)
	public ScheduleUpdate findByScheduleId(Long scheduleId){
		log.debug("Request to get revisions, upates for schedule with id : ", scheduleId);
		ScheduleUpdate scheduleUpdate = scheduleUpdateRepository.findByScheduleId(scheduleId);
		return scheduleUpdate;
	}
}
