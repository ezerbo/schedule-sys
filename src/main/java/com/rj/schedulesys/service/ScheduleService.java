package com.rj.schedulesys.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.schedulesys.dao.EmployeeDao;
import com.rj.schedulesys.dao.FacilityDao;
import com.rj.schedulesys.dao.ScheduleDao;
import com.rj.schedulesys.dao.SchedulePostStatusDao;
import com.rj.schedulesys.dao.ScheduleStatusDao;
import com.rj.schedulesys.dao.ScheduleSysUserDao;
import com.rj.schedulesys.dao.ScheduleUpdateDao;
import com.rj.schedulesys.dao.ShiftDao;
import com.rj.schedulesys.data.ScheduleStatusConstants;
import com.rj.schedulesys.domain.Employee;
import com.rj.schedulesys.domain.Facility;
import com.rj.schedulesys.domain.Schedule;
import com.rj.schedulesys.domain.SchedulePostStatus;
import com.rj.schedulesys.domain.ScheduleStatus;
import com.rj.schedulesys.domain.ScheduleSysUser;
import com.rj.schedulesys.domain.ScheduleUpdate;
import com.rj.schedulesys.domain.ScheduleUpdatePK;
import com.rj.schedulesys.domain.Shift;
import com.rj.schedulesys.util.ObjectValidator;
import com.rj.schedulesys.view.model.CreateScheduleViewModel;
import com.rj.schedulesys.view.model.EmployeeViewModel;
import com.rj.schedulesys.view.model.FacilityViewModel;
import com.rj.schedulesys.view.model.GetScheduleViewModel;
import com.rj.schedulesys.view.model.PositionViewModel;
import com.rj.schedulesys.view.model.SchedulePostStatusViewModel;
import com.rj.schedulesys.view.model.ScheduleStatusViewModel;
import com.rj.schedulesys.view.model.ScheduleSysUserViewModel;
import com.rj.schedulesys.view.model.ShiftViewModel;
import com.rj.schedulesys.view.model.UpdateScheduleViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ScheduleService {
	
	@Autowired
	private ScheduleSysUserDao scheduleSysUserDao;
	
	@Autowired
	private ShiftDao shiftDao;
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private FacilityDao facilityDao;
	
	@Autowired
	private ScheduleDao scheduleDao;
	
	@Autowired
	private ScheduleUpdateDao scheduleUpdateDao;
	
	@Autowired
	private ScheduleStatusDao scheduleStatusDao;
	
	@Autowired
	private SchedulePostStatusDao schedulePostStatusDao;
	
	@Autowired
	private DozerBeanMapper dozerMapper;
	
	@Autowired
	private ObjectValidator<CreateScheduleViewModel> validator;
	
	/**
	 * @param viewModel
	 * @param scheduleSysUserId user creating the schedule
	 * @return
	 */
	@Transactional
	public CreateScheduleViewModel create(CreateScheduleViewModel viewModel, Long scheduleSysUserId){
		
		validator.validate(viewModel);
		
		Facility facility = validateFacility(viewModel.getFacilityId());
		
		ScheduleStatus scheduleStatus = validateScheduleStatus(viewModel.getScheduleStatusId());
		SchedulePostStatus schedulePostStatus = validateSchedulePostStatus(viewModel.getSchedulePostStatusId());
		
		Shift shift = validateShift(viewModel.getShiftId());
		
		if(StringUtils.equalsIgnoreCase(scheduleStatus.getStatus(), ScheduleStatusConstants.CONFIRMED_STATUS) 
				&& viewModel.getEmployeeId() == null){
			log.error("The schedule is of status 'CONFIRMED' but no nurse or care giver is provided");
			throw new RuntimeException("The schedule is of status 'CONFIRMED' but no nurse or care giver is provided");
		}
		
		Employee employee = null;
		
		if(viewModel.getEmployeeId() != null){
			employee = validateEmployee(viewModel.getEmployeeId());
			//Make sure the employee does not already have a shift on the schedule date received
			assertUniqueShiftPerDay(employee.getId(), shift.getId(), viewModel.getScheduleDate());
		}
		
		//User creating the schedule
		ScheduleSysUser scheduleSysUser = scheduleSysUserDao.findOne(scheduleSysUserId);
		
		Schedule schedule = Schedule.builder()
				.employee(employee)
				.facility(facility)
				.shift(shift)
				.hours(0.0)
				.overtime(0.0)
				.timesheetReceived(false)
				.scheduleComment(viewModel.getComment())
				.scheduleStatus(scheduleStatus)
				.schedulePostStatus(schedulePostStatus)
				.createDate(new Date())
				.scheduleDate(viewModel.getScheduleDate())
				.scheduleSysUser(scheduleSysUser)
				.build();
		
		log.info("Creating schedule : {}", schedule);
		
		schedule = scheduleDao.merge(schedule);
		
		viewModel = dozerMapper.map(schedule, CreateScheduleViewModel.class);
		log.info("Created schedule : {}", viewModel);
		
		return viewModel;
	}
	
	@Transactional
	public UpdateScheduleViewModel update(UpdateScheduleViewModel viewModel, Long scheduleSysUserId){

		Schedule schedule = scheduleDao.findOne(viewModel.getId());
		
		if(schedule == null){
			log.error("No schedule found with id : {}", viewModel.getId());
			throw new RuntimeException("No schedule found with id : " + viewModel.getId());
		}
		
		Facility facility = schedule.getFacility();
		
		if(facility.getId() != viewModel.getId()){
			log.warn("Schedule's facility updated, validating new facility");
			facility = validateFacility(viewModel.getFacilityId());
		}
		
		Shift shift = schedule.getShift();
		
		if(shift.getId() != viewModel.getShiftId()){
			log.warn("Schedule's shift updated, validating new shift");
			shift = validateShift(viewModel.getShiftId());
		}
		
		ScheduleStatus scheduleStatus = schedule.getScheduleStatus();
		
		if(scheduleStatus.getId() != viewModel.getId()){
			log.warn("Schedule's status updated, validating new status");
			scheduleStatus = validateScheduleStatus(viewModel.getScheduleStatusId());
		}
		
		SchedulePostStatus schedulePostStatus = null; 
				
		if(viewModel.getSchedulePostStatusId() != null){
			schedulePostStatus = validateSchedulePostStatus(viewModel.getSchedulePostStatusId());
		}
		
		Employee employee = validateEmployee(viewModel.getEmployeeId());
		
		if(viewModel.getEmployeeId() != null && schedule.getEmployee() != null){
			if(viewModel.getEmployeeId() != schedule.getEmployee().getId()){
				//Make sure the employee does not already have a shift on the schedule date received
				assertUniqueShiftPerDay(employee.getId(), shift.getId(), viewModel.getScheduleDate());
			}
		}
//		}else{
//			if(viewModel.getSchedulePostStatusId() != null){
//				log.error("Schedule post status '{}' submitted but the schedule is not assigned to any employee", schedulePostStatus.getStatus());
//				throw new RuntimeException("Schedule post status '" + schedulePostStatus.getStatus() 
//					+ "' submitted but the schedule is not assigned to any employee");
//			}
//		}
		
		if(viewModel.isTimesheetReceived() 
				&& (viewModel.getEmployeeId() == null 
				|| StringUtils.equalsIgnoreCase(scheduleStatus.getStatus(), ScheduleStatusConstants.NOT_CONFIRMED_STATUS))){
			log.error("No time sheet can be provided when the schedule has no employee or is not confirmed");
			throw new RuntimeException("No time sheet can be provided when the schedule has no employee or is not confirmed");
		}
		
		if(viewModel.isTimesheetReceived() && (viewModel.getHours() == null || viewModel.getHours() == 0)){
			log.error("Time sheet is received but the actual hours worked was not submitted");
			throw new RuntimeException("Time sheet is received but the actual hours worked was not submitted");
		}
		
		//TODO Check that there is no overtime when hours_worked < shift_capacity
		
		log.info("Hours : {}", viewModel.getHours());
		
		schedule.setScheduleDate(viewModel.getScheduleDate());
		schedule.setEmployee(employee);
		schedule.setFacility(facility);
		schedule.setShift(shift);
		schedule.setTimesheetReceived(viewModel.isTimesheetReceived());
		schedule.setHours(viewModel.getHours());
		schedule.setOvertime(viewModel.getOvertime());
		schedule.setScheduleComment(viewModel.getComment());
		schedule.setScheduleStatus(scheduleStatus);
		schedule.setSchedulePostStatus(schedulePostStatus);
		log.info("Updating schedule : {}", schedule);
		schedule = scheduleDao.merge(schedule);
		createScheduleUpdate(schedule, scheduleSysUserId);
		return dozerMapper.map(schedule, UpdateScheduleViewModel.class);
	}
	
	/**
	 * @param schedule
	 * @param scheduleSysUserId
	 * @param comment
	 */
	public void createScheduleUpdate(Schedule schedule , Long scheduleSysUserId){
		ScheduleUpdatePK pk = ScheduleUpdatePK.builder()
				.scheduleId(schedule.getId())
				.userId(scheduleSysUserId)
				.build();
		ScheduleUpdate scheduleUpdate = ScheduleUpdate.builder()
				.id(pk)
				.scheduleSysUser(scheduleSysUserDao.findOne(scheduleSysUserId))
				.build();
		scheduleUpdate = scheduleUpdateDao.merge(scheduleUpdate);
		log.info("ScheduleUpdate : {}", scheduleUpdate);
	}
	
	/**
	 * @param id
	 */
	@Transactional
	public void delete(Long id){
		log.debug("Deleting schedule with id : {}", id);
		Schedule schedule = scheduleDao.findOne(id);
		if(schedule == null){
			log.error("No schedule found with id : {}", id);
			throw new RuntimeException("No schedule found with id : " + id);
		}
		scheduleDao.delete(schedule);
	}
	
	/**
	 * @param employeeId
	 * @param shiftId
	 * @param scheduleDate
	 * @return
	 */
	private void assertUniqueShiftPerDay(Long employeeId, Long shiftId, Date scheduleDate){
		Schedule schedule = scheduleDao.findByEmployeeAndShiftAndDate(employeeId, shiftId, scheduleDate);
		if(schedule != null){
			log.error("Employee with id : {} already has a shift on  {}", employeeId, scheduleDate);
			throw new RuntimeException("Employee with id : " + employeeId + " already has a shift on " + scheduleDate);
		}
	}
	
	@Transactional
	public GetScheduleViewModel findOne(Long id){
		log.info("Finding schedule with id : {}", id);
		Schedule schedule = scheduleDao.findOne(id);
		GetScheduleViewModel viewModel = null;
		if(schedule != null){
			viewModel = this.buildGetScheduleViewModel(schedule);
		}
		log.info("Schedule found : {}", viewModel);
		return viewModel;
	}
	
	/**
	 * @param startDate
	 * @param endDate
	 * @param facilityId
	 * @return
	 */
	@Transactional
	public List<GetScheduleViewModel> findAllBetweenDatesByFacility(Date startDate, Date endDate, Long facilityId){
		log.info("Fetching schedules between startDate : {} and endDate : {} for facility with id : {}", startDate, endDate, facilityId);
		List<GetScheduleViewModel> viewModels = new LinkedList<>();
		List<Schedule> schedules = scheduleDao.findAllBetweenDatesByFacility(startDate, endDate, facilityId);
		for(Schedule schedule : schedules){
			viewModels.add(this.buildGetScheduleViewModel(schedule));
		}
		log.info("Schedule found : {}", viewModels);
		return viewModels;
	}
	
	/**
	 * @param facilityId
	 * @return
	 */
	public List<GetScheduleViewModel> findAllByFacility(Long facilityId){
		log.debug("Fetching schedules for faclity with id : {}", facilityId);
		List<Schedule> schedules = scheduleDao.findAllByFacility(facilityId);
		List<GetScheduleViewModel> viewModels = new LinkedList<>();
		for(Schedule schedule : schedules){
			viewModels.add(this.buildGetScheduleViewModel(schedule));
		}
		return viewModels;
	}
	
	/**
	 * @param shiftId
	 * @return
	 */
	public Shift validateShift(Long shiftId){
		Shift shift = shiftDao.findOne(shiftId);
		if(shift == null){
			log.error("No shift found with id : {}", shiftId);
			throw new RuntimeException("No shift found with id : " + shiftId);
		}
		return shift;
	}
	
	/**
	 * @param facilityId
	 * @return
	 */
	public Facility validateFacility(Long facilityId){
		Facility facility = facilityDao.findOne(facilityId);
		if(facility == null){
			log.error("No facility found with id : {}", facility);
			throw new RuntimeException("No facility found with id : " + facilityId);
		}
		return facility;
	}
	
	/**
	 * @param scheduleStatusId
	 * @return
	 */
	public ScheduleStatus validateScheduleStatus(Long scheduleStatusId){
		ScheduleStatus scheduleStatus = scheduleStatusDao.findOne(scheduleStatusId);
		if(scheduleStatus == null){
			log.error("No schedule status found with id : {}", scheduleStatus);
			throw new RuntimeException("No schedule status found with id : " + scheduleStatusId);
		}
		return scheduleStatus;
	}
	
	/**
	 * @param schedulePostStatusId
	 * @return
	 */
	public SchedulePostStatus validateSchedulePostStatus(Long schedulePostStatusId){
		SchedulePostStatus schedulePostStatus = schedulePostStatusDao.findOne(schedulePostStatusId);
		if(schedulePostStatus == null){
			log.error("No schedule post status found with id : {}", schedulePostStatusId);
			throw new RuntimeException("No schedule post status found with id : " + schedulePostStatusId);
		}
		return schedulePostStatus;
	}
	
	public Employee validateEmployee(Long employeeId){
		if(employeeId == null){
			return null;
		}
		Employee employee = employeeDao.findOne(employeeId);
		if(employee == null){
			log.error("No employee found with id : {}", employeeId);
			throw new RuntimeException("No employee found with id : " + employeeId);
		}
		return employee;
	}
	
	/**
	 * @param schedule
	 * @return
	 */
	private GetScheduleViewModel buildGetScheduleViewModel(Schedule schedule){
		
		EmployeeViewModel employeeVm = null;
		PositionViewModel positionVm = null;
		
		if(schedule.getEmployee() != null){
			employeeVm = dozerMapper.map(schedule.getEmployee(), EmployeeViewModel.class);
			positionVm = dozerMapper.map(schedule.getEmployee().getPosition(), PositionViewModel.class);
		}
		
		FacilityViewModel facilityVm = dozerMapper.map(schedule.getFacility(), FacilityViewModel.class);
		ShiftViewModel shiftVm = dozerMapper.map(schedule.getShift(), ShiftViewModel.class);
		ScheduleStatusViewModel scheduleStatusVm = dozerMapper.map(schedule.getScheduleStatus(), ScheduleStatusViewModel.class);
		ScheduleSysUserViewModel filledBy = dozerMapper.map(schedule.getScheduleSysUser(), ScheduleSysUserViewModel.class);
		
		SchedulePostStatusViewModel schedulePostStatusVm = null;
		
		if(schedule.getSchedulePostStatus() != null){
			schedulePostStatusVm = dozerMapper.map(schedule.getSchedulePostStatus(), SchedulePostStatusViewModel.class);
		}
		
		ScheduleSysUserViewModel lastModifiedBy = null;
		
		ScheduleUpdate scheduleUpdate = scheduleUpdateDao.findLatestByScheduleId(schedule.getId());
		
		if(scheduleUpdate != null){
			lastModifiedBy = dozerMapper.map(scheduleUpdate.getScheduleSysUser(), ScheduleSysUserViewModel.class);
		}
		
		GetScheduleViewModel viewModel = GetScheduleViewModel.builder()
				.id(schedule.getId())
				.employee(employeeVm)
				.facility(facilityVm)
				.filledBy(filledBy)
				.lastModifiedBy(lastModifiedBy)
				.hours(schedule.getHours())
				.overtime(schedule.getOvertime())
				.job(positionVm)
				.scheduleComment(schedule.getScheduleComment())
				.scheduleDate(schedule.getScheduleDate())
				.shift(shiftVm)
				.timesheetReceived(schedule.getTimesheetReceived())
				.scheduleStatus(scheduleStatusVm)
				.schedulePostStatus(schedulePostStatusVm)
				.build();
		
		return viewModel;
	}
	
}