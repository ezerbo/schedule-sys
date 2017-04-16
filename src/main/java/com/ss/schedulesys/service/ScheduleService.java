package com.ss.schedulesys.service;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.config.Constants;
import com.ss.schedulesys.domain.CareCompany;
import com.ss.schedulesys.domain.Employee;
import com.ss.schedulesys.domain.Schedule;
import com.ss.schedulesys.domain.SchedulePostStatus;
import com.ss.schedulesys.domain.ScheduleStatus;
import com.ss.schedulesys.domain.ScheduleSysUser;
import com.ss.schedulesys.domain.ScheduleUpdate;
import com.ss.schedulesys.domain.ScheduleUpdateId;
import com.ss.schedulesys.repository.CareCompanyRepository;
import com.ss.schedulesys.repository.EmployeeRepository;
import com.ss.schedulesys.repository.SchedulePostStatusRepository;
import com.ss.schedulesys.repository.ScheduleRepository;
import com.ss.schedulesys.repository.ScheduleStatusRepository;
import com.ss.schedulesys.repository.ScheduleUpdateRepository;
import com.ss.schedulesys.service.errors.ScheduleSysException;
import com.ss.schedulesys.service.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ezerbo
 * Service Implementation for managing Schedule.
 */
@Slf4j
@Service
@Transactional
public class ScheduleService {
	
	//https://www.mkyong.com/regular-expressions/how-to-validate-time-in-12-hours-format-with-regular-expression/
	private final String validTimeRegex = "(1[012]|0?[1-9]):[0-5][0-9](\\s)?(?i)(am|pm)";

	private EmployeeRepository employeeRepository;
    private ScheduleRepository scheduleRepository;
    private CareCompanyRepository careCompanyRepository;
    private ScheduleStatusRepository scheduleStatusRepository;
    private SchedulePostStatusRepository schedulePostStatusRepository;
    private ScheduleUpdateRepository scheduleUpdateRepository;
    
    private DozerBeanMapper dozerBeanMapper;
    
    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository, CareCompanyRepository careCompanyRepository,
    		ScheduleStatusRepository scheduleStatusRepository, SchedulePostStatusRepository schedulePostStatusRepository,
    		EmployeeRepository employeeRepository, ScheduleUpdateRepository scheduleUpdateRepository,
    		DozerBeanMapper dozerBeanMapper) {
    	this.employeeRepository = employeeRepository;
    	this.scheduleRepository = scheduleRepository;
    	this.careCompanyRepository =  careCompanyRepository;
    	this.scheduleStatusRepository = scheduleStatusRepository;
    	this.scheduleUpdateRepository = scheduleUpdateRepository;
    	this.schedulePostStatusRepository = schedulePostStatusRepository;
    	this.dozerBeanMapper = dozerBeanMapper;
	}

    /**
     * Creates a schedule.
     *
     * @param schedule the entity to save
     * @return the persisted entity
     */
    public Schedule save(Schedule schedule, ScheduleSysUser user) {
        log.debug("Request to save Schedule : {}", schedule);
    	if(schedule.getScheduleDate().before(DateUtil.yesterday())){
			log.error("Schedule date received is in the past.");
			throw new ScheduleSysException("The schedule date provided is not valid because it is in the past.");
		}
    	
    	schedule = validateStatusesAndCareCompany(schedule);
    	if(!(validateTime(schedule.getShiftStartTime()) && validateTime(schedule.getShiftEndTime()))){
    		log.error("Invalid shift start or end time : {}, {}", schedule.getShiftStartTime(), schedule.getShiftEndTime());
    		throw new ScheduleSysException(String.format("Invalid shift start or end time : %s, %s",
    				schedule.getShiftStartTime(), schedule.getShiftEndTime()));
    	}
    	
    	Employee employee = schedule.getEmployee();
    	if(employee != null){//Schedule has been assigned
    		employee = employeeRepository.findOne(employee.getId());
    		if(employee == null){
    			throw new ScheduleSysException("The employee specified was not found.");//Make sure employee exists
    		}
    		//Make sure employee has no schedule in the same care company on the same date and the same shift
    		Schedule otherSchedule = scheduleRepository.find(employee.getId(), schedule.getCareCompany().getId(),
    				schedule.getScheduleDate(), schedule.getShiftStartTime(), schedule.getShiftEndTime());
    		
    		if(otherSchedule != null && otherSchedule.getId() != schedule.getId()){
    			throw new ScheduleSysException(String.format("%s already has a schedule in '%s' on '%s', from '%s' to '%s'",
    					employee.getFirstName().concat(" ").concat(employee.getLastName()), schedule.getCareCompany().getName(),
    					schedule.getScheduleDate(), schedule.getShiftStartTime(), schedule.getShiftEndTime()));
    		}
    		schedule.setEmployee(employee);
    		//TODO Prevent overlapping schedules for same employee
    	}
    	
    	if(schedule.getId() == null){//New schedule being created
    		schedule.scheduleSysUser(user);
    	}else{//Schedule being updated
    		log.info("Creating update record");
    		Schedule oldSchedule = scheduleRepository.findOne(schedule.getId());
    		dozerBeanMapper.map(schedule, oldSchedule);
    		createScheduleUpdate(oldSchedule, user);
    	}
    	
        Schedule result = scheduleRepository.save(schedule);
        return result;
    }
    
    /**
     * Add schedule update record
     * @param schedule
     * @param user
     * @return
     */
    private ScheduleUpdate createScheduleUpdate(Schedule schedule, ScheduleSysUser user){
    	log.debug("Creating schedule update log entry");
		ScheduleUpdateId scheduleUpdateId = ScheduleUpdateId.builder()
				.scheduleId(schedule.getId()).userId(user.getId()).build();
		ScheduleUpdate scheduleUpdate = ScheduleUpdate.builder().id(scheduleUpdateId)
				.schedule(schedule).scheduleSysUser(user).updateDate(new Date()).build();
		return scheduleUpdateRepository.save(scheduleUpdate);
    }
    
    /**
     *  Validates CareCompany statuses of a schedule
     * @param schedule
     * @return validated schedule
     */
    private Schedule validateStatusesAndCareCompany(Schedule schedule){
    	log.debug("Validating schedule : {}", schedule);
    	CareCompany careCompany = Optional.ofNullable(schedule.getCareCompany())
    			.map(result -> careCompanyRepository.findOne(result.getId()))
    			.orElseThrow(() -> new ScheduleSysException("A valid care company is required to create a schedule"));

    	ScheduleStatus status = Optional.ofNullable(schedule.getScheduleStatus())
    			.map(result -> scheduleStatusRepository.findOne(result.getId()))
    			.orElseThrow(() -> new ScheduleSysException("A valid status is required to create a schedule"));

    	SchedulePostStatus schedulePostStatus = Optional.ofNullable(schedule.getSchedulePostStatus())
    			.map(result -> schedulePostStatusRepository.findOne(result.getId()))
    			.orElse(schedulePostStatusRepository.findByName(Constants.DEFAULT_SCHEDULE_POST_STATUS));
    	
    	schedule.careCompany(careCompany).status(status).postStatus(schedulePostStatus);
    	
    	return schedule;
    }
    
    
    /**
     * @param time
     */
    private boolean validateTime(String time){
    	log.debug("Validating time : {}", time);
    	Pattern pattern = Pattern.compile(validTimeRegex);
		Matcher matcher = pattern.matcher(time);
		return matcher.matches();
    }

    /**
     *  Get all the schedules.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Schedule> findAll(Pageable pageable) {
        log.debug("Request to get all Schedules");
        Page<Schedule> result = scheduleRepository.findAll(pageable);
        return result;
    }
    
    /**
     * @param careCompanyId
     * @return
     */
    @Transactional(readOnly = true)
    public List<Schedule> findAllByCareCompany(Long careCompanyId) {
    	log.debug("Request to get all schedules for care company with id : {}", careCompanyId);
    	List<Schedule> schedules = scheduleRepository.findAllByCareCompany(careCompanyId);
    	return schedules;
    }
    
    /**
     * @param employeeId
     * @return
     */
    @Transactional(readOnly = true)
    public List<Schedule> findAllByEmployee(Long employeeId) {
    	log.debug("Request to get all schedules for employee with id : {}", employeeId);
    	List<Schedule> schedules = scheduleRepository.findAllByEmployee(employeeId);
    	return schedules;
    }

    /**
     *  Get one schedule by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Schedule findOne(Long id) {
        log.debug("Request to get Schedule : {}", id);
        Schedule schedule = scheduleRepository.findOne(id);
        return schedule;
    }

    /**
     *  Delete the  schedule by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Schedule : {}", id);
        scheduleRepository.delete(id);
    }

}