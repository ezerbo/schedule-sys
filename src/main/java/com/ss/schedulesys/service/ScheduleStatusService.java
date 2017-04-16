package com.ss.schedulesys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.ScheduleStatus;
import com.ss.schedulesys.repository.ScheduleStatusRepository;
import com.ss.schedulesys.service.errors.ScheduleSysException;

import lombok.extern.slf4j.Slf4j;

/**
 * Service Implementation for managing ScheduleStatus.
 */
@Slf4j
@Service
@Transactional
public class ScheduleStatusService {

    private ScheduleStatusRepository scheduleStatusRepository;

    @Autowired
    public ScheduleStatusService(ScheduleStatusRepository scheduleStatusRepository) {
    	this.scheduleStatusRepository = scheduleStatusRepository;
	}
    
    /**
     * Save a scheduleStatus.
     *
     * @param scheduleStatus the entity to save
     * @return the persisted entity
     */
    public ScheduleStatus save(ScheduleStatus scheduleStatus) {
        log.debug("Request to save ScheduleStatus : {}", scheduleStatus);
        if(scheduleStatusRepository.findByName(scheduleStatus.getName()) != null){
        	throw new ScheduleSysException(String.format("Schedule Status '%s' is already in use", scheduleStatus.getName()));
        }
        ScheduleStatus result = scheduleStatusRepository.save(scheduleStatus);
        return result;
    }

    /**
     *  Get all the scheduleStatuses.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<ScheduleStatus> findAll() {
        log.debug("Request to get all ScheduleStatuses");
        List<ScheduleStatus> result = scheduleStatusRepository.findAll();
        return result;
    }

    /**
     *  Get one scheduleStatus by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ScheduleStatus findOne(Long id) {
        log.debug("Request to get ScheduleStatus : {}", id);
        ScheduleStatus scheduleStatus = scheduleStatusRepository.findOne(id);
        return scheduleStatus;
    }

    /**
     *  Delete the  scheduleStatus by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ScheduleStatus : {}", id);
        scheduleStatusRepository.delete(id);
    }

}