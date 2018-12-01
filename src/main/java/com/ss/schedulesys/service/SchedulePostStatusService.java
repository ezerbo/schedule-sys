package com.ss.schedulesys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.SchedulePostStatus;
import com.ss.schedulesys.repository.SchedulePostStatusRepository;
import com.ss.schedulesys.service.errors.ScheduleSysException;

import lombok.extern.slf4j.Slf4j;

/**
 * Service Implementation for managing SchedulePostStatus.
 */
@Slf4j
@Service
@Transactional
public class SchedulePostStatusService {

    private SchedulePostStatusRepository schedulePostStatusRepository;
    
    @Autowired
    public SchedulePostStatusService(SchedulePostStatusRepository schedulePostStatusRepository) {
		this.schedulePostStatusRepository = schedulePostStatusRepository;
	}

    /**
     * Save a schedulePostStatus.
     *
     * @param schedulePostStatus the entity to save
     * @return the persisted entity
     */
    public SchedulePostStatus save(SchedulePostStatus schedulePostStatus) {
        log.debug("Request to save SchedulePostStatus : {}", schedulePostStatus);
        if(schedulePostStatusRepository.findByName(schedulePostStatus.getName()) != null){
        	throw new ScheduleSysException(
        			String.format("Schedule Post Status '%s' is already in use", schedulePostStatus.getName()));
        }
        SchedulePostStatus result = schedulePostStatusRepository.save(schedulePostStatus);
        return result;
    }

    /**
     *  Get all the schedulePostStatuses.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<SchedulePostStatus> findAll() {
        log.debug("Request to get all SchedulePostStatuses");
        List<SchedulePostStatus> result = schedulePostStatusRepository.findAll();
        return result;
    }

    /**
     *  Get one schedulePostStatus by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public SchedulePostStatus findOne(Long id) {
        log.debug("Request to get SchedulePostStatus : {}", id);
        SchedulePostStatus schedulePostStatus = schedulePostStatusRepository.findOne(id);
        return schedulePostStatus;
    }

    /**
     *  Delete the  schedulePostStatus by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SchedulePostStatus : {}", id);
        schedulePostStatusRepository.delete(id);
    }

}