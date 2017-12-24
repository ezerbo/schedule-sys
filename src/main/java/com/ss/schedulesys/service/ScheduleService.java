package com.ss.schedulesys.service;
import static java.util.stream.Collectors.groupingBy;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.CareCompany;
import com.ss.schedulesys.domain.Employee;
import com.ss.schedulesys.domain.Schedule;
import com.ss.schedulesys.domain.SchedulePostStatus;
import com.ss.schedulesys.domain.ScheduleStatus;
import com.ss.schedulesys.domain.ScheduleSummary;
import com.ss.schedulesys.domain.ScheduleSysUser;
import com.ss.schedulesys.domain.ScheduleUpdate;
import com.ss.schedulesys.repository.CareCompanyRepository;
import com.ss.schedulesys.repository.EmployeeRepository;
import com.ss.schedulesys.repository.SchedulePostStatusRepository;
import com.ss.schedulesys.repository.ScheduleRepository;
import com.ss.schedulesys.repository.ScheduleStatusRepository;
import com.ss.schedulesys.repository.ScheduleUpdateRepository;
import com.ss.schedulesys.service.errors.ScheduleSysException;

import lombok.extern.slf4j.Slf4j; 

/**
 * @author ezerbo
 * Service Implementation for managing Schedule.
 */
@Slf4j
@Service
@Transactional
public class ScheduleService {
	
	private final static String PENDING_STATUS = "PENDING";
	
	private EmployeeRepository employeeRepository;
    private ScheduleRepository scheduleRepository;
    private CareCompanyRepository careCompanyRepository;
    private ScheduleStatusRepository scheduleStatusRepository;
    private SchedulePostStatusRepository schedulePostStatusRepository;
    private ScheduleUpdateRepository scheduleUpdateRepository;
    
    public ScheduleService(ScheduleRepository scheduleRepository, CareCompanyRepository careCompanyRepository,
    		ScheduleStatusRepository scheduleStatusRepository, SchedulePostStatusRepository schedulePostStatusRepository,
    		EmployeeRepository employeeRepository, ScheduleUpdateRepository scheduleUpdateRepository) {
    	this.employeeRepository = employeeRepository;
    	this.scheduleRepository = scheduleRepository;
    	this.careCompanyRepository =  careCompanyRepository;
    	this.scheduleStatusRepository = scheduleStatusRepository;
    	this.scheduleUpdateRepository = scheduleUpdateRepository;
    	this.schedulePostStatusRepository = schedulePostStatusRepository;
	}

    /**
     * Creates a schedule.
     *
     * @param schedule the entity to save
     * @return the persisted entity
     */
    public Schedule save(Schedule schedule, ScheduleSysUser user) {
        log.debug("Request to save Schedule : {}", schedule);
    	
    	schedule = validateStatusesAndCareCompany(schedule);
    	
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
    		Schedule oldSchedule = scheduleRepository.findOne(schedule.getId());
    		schedule = oldSchedule.billed(schedule.getBilled())
    						.status(schedule.getScheduleStatus()).postStatus(schedule.getSchedulePostStatus())
    						.paid(schedule.getPaid()).comment(schedule.getComment())
    						.overtime(schedule.getOvertime()).timeSheetReceived(schedule.getTimeSheetReceived())
    						.hoursWorked(schedule.getHoursWorked()).shiftStartTime(schedule.getShiftStartTime())
    						.shiftEndTime(schedule.getShiftEndTime()).scheduleDate(schedule.getScheduleDate());
    		createScheduleUpdate(schedule, user);
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
    	log.debug("Creating schedule update log entry, user : {}, schedule : {}", user, schedule);
		ScheduleUpdate scheduleUpdate = ScheduleUpdate.builder()
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
    			.map(result -> scheduleStatusRepository.findByName(result.getName()))
    			.orElseThrow(() -> new ScheduleSysException("A valid status is required to create a schedule"));

    	SchedulePostStatus schedulePostStatus = Optional.ofNullable(schedule.getSchedulePostStatus())
    			.map(result -> schedulePostStatusRepository.findByName(result.getName()))
    			.orElse(schedulePostStatusRepository.findByName(PENDING_STATUS));
    	
    	schedule.careCompany(careCompany).status(status).postStatus(schedulePostStatus);
    	
    	return schedule;
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
     *  Get all the schedules.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Schedule> findAllByDateAndCompanyType(Date scheduleDate, String companyType) {
        log.debug("Request to get all Shifts schedules on : {}", scheduleDate);
        List<Schedule> result = scheduleRepository.findAllByDateAndCompanyType(scheduleDate, companyType);
        return result;
    }
    
    /**
     * @param careCompanyId
     * @return
     */
    @Transactional(readOnly = true)
    public List<Schedule> findAllByCareCompany(Long careCompanyId) {
    	Sort sort = new Sort(Direction.ASC, "scheduleDate");
    	log.debug("Request to get all schedules for care company with id : {}", careCompanyId);
    	List<Schedule> schedules = scheduleRepository.findAllByCareCompany(careCompanyId, sort);
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
    
    public List<ScheduleSummary> getSchedulesSummary(Date scheduleDate) {
    	List<ScheduleSummary> summaries = new LinkedList<>();
    	scheduleRepository.findAllByScheduleDate(scheduleDate).stream()
    		.collect(groupingBy(schedule -> schedule.getCareCompany()))
    		.forEach((company, schedules) -> {
    			ScheduleSummary scheduleSummary = ScheduleSummary.builder()
    					.careCompanyId(company.getId())
    					.careCompanyName(company.getName())
    					.careCompanyType(company.getCareCompanyType().getName())
    					.shiftsScheduled(Long.valueOf(schedules.size()))
    					.build();
    			summaries.add(scheduleSummary);
    		});
    	return summaries;
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