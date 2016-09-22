package com.rj.schedulesys.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.schedulesys.dao.FacilityDao;
import com.rj.schedulesys.dao.FacilityScheduleUpdateDao;
import com.rj.schedulesys.dao.FacilityShiftDao;
import com.rj.schedulesys.dao.NurseDao;
import com.rj.schedulesys.dao.FacilityScheduleDao;
import com.rj.schedulesys.dao.SchedulePostStatusDao;
import com.rj.schedulesys.dao.ScheduleStatusDao;
import com.rj.schedulesys.dao.ScheduleSysUserDao;
import com.rj.schedulesys.data.ScheduleStatusConstants;
import com.rj.schedulesys.domain.Facility;
import com.rj.schedulesys.domain.FacilityScheduleUpdate;
import com.rj.schedulesys.domain.FacilityShift;
import com.rj.schedulesys.domain.Nurse;
import com.rj.schedulesys.domain.FacilitySchedule;
import com.rj.schedulesys.domain.SchedulePostStatus;
import com.rj.schedulesys.domain.ScheduleStatus;
import com.rj.schedulesys.domain.ScheduleSysUser;
import com.rj.schedulesys.domain.ScheduleUpdatePK;
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
public class FacilityScheduleService {
	
	private FacilityShiftDao shiftDao;
	private NurseDao nurseDao;
	private FacilityDao facilityDao;
	private FacilityScheduleDao scheduleDao;
	private ScheduleStatusDao scheduleStatusDao;
	private ScheduleSysUserDao scheduleSysUserDao;
	private FacilityScheduleUpdateDao facilityScheduleUpdateDao;
	private SchedulePostStatusDao schedulePostStatusDao;
	
	private DozerBeanMapper dozerMapper;
	private ObjectValidator<CreateScheduleViewModel> validator;
	
	@Autowired
	public FacilityScheduleService(ScheduleSysUserDao scheduleSysUserDao, FacilityShiftDao shiftDao, NurseDao nurseDao,
			FacilityDao facilityDao, FacilityScheduleDao scheduleDao, FacilityScheduleUpdateDao facilityScheduleUpdateDao,
			SchedulePostStatusDao schedulePostStatusDao, ScheduleStatusDao scheduleStatusDao, DozerBeanMapper dozerMapper,
			ObjectValidator<CreateScheduleViewModel> validator) {
		this.scheduleSysUserDao = scheduleSysUserDao;
		this.shiftDao = shiftDao;
		this.nurseDao = nurseDao;
		this.facilityDao = facilityDao;
		this.scheduleDao = scheduleDao;
		this.facilityScheduleUpdateDao = facilityScheduleUpdateDao;
		this.scheduleStatusDao = scheduleStatusDao;
		this.schedulePostStatusDao = schedulePostStatusDao;
		this.dozerMapper = dozerMapper;
		this.validator = validator;
	}
	
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
		FacilityShift shift = validateShift(viewModel.getShiftId());
		if(StringUtils.equalsIgnoreCase(scheduleStatus.getStatus(), ScheduleStatusConstants.CONFIRMED_STATUS) 
				&& viewModel.getEmployeeId() == null){
			log.error("The schedule is of status 'CONFIRMED' but no nurse or care giver is provided");
			throw new RuntimeException("The schedule is of status 'CONFIRMED' but no nurse or care giver is provided");
		}
		Nurse nurse = null;
		if(viewModel.getEmployeeId() != null){
			nurse = validateNurse(viewModel.getEmployeeId());
			//Make sure the employee does not already have a shift on the schedule date received
			assertUniqueShiftPerDay(nurse.getId(), shift.getId(), viewModel.getScheduleDate());
		}
		//User creating the schedule
		ScheduleSysUser scheduleSysUser = scheduleSysUserDao.findOne(scheduleSysUserId);
		FacilitySchedule schedule = FacilitySchedule.builder()
				.nurse(nurse)
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
		
		schedule = scheduleDao.merge(schedule);
		viewModel = dozerMapper.map(schedule, CreateScheduleViewModel.class);
		return viewModel;
	}
	
	@Transactional
	public UpdateScheduleViewModel update(UpdateScheduleViewModel viewModel, Long scheduleSysUserId){
		FacilitySchedule schedule = scheduleDao.findOne(viewModel.getId());
		if(schedule == null){
			log.error("No schedule found with id : {}", viewModel.getId());
			throw new RuntimeException("No schedule found with id : " + viewModel.getId());
		}
		Facility facility = schedule.getFacility();
		if(facility.getId() != viewModel.getId()){
			log.warn("Schedule's facility updated, validating new facility");
			facility = validateFacility(viewModel.getFacilityId());
		}
		
		FacilityShift shift = schedule.getShift();
		
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
		
		Nurse employee = validateNurse(viewModel.getEmployeeId());
		
		if(viewModel.getEmployeeId() != null && schedule.getNurse() != null){
			if(viewModel.getEmployeeId() != schedule.getNurse().getId()){
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
		
		schedule.setScheduleDate(viewModel.getScheduleDate());
		schedule.setNurse(employee);
		schedule.setFacility(facility);
		schedule.setShift(shift);
		schedule.setTimesheetReceived(viewModel.isTimesheetReceived());
		schedule.setHours(viewModel.getHours());
		schedule.setOvertime(viewModel.getOvertime());
		schedule.setScheduleComment(viewModel.getComment());
		schedule.setScheduleStatus(scheduleStatus);
		schedule.setSchedulePostStatus(schedulePostStatus);
		schedule = scheduleDao.merge(schedule);
		createScheduleUpdate(schedule, scheduleSysUserId);
		return dozerMapper.map(schedule, UpdateScheduleViewModel.class);
	}
	
	/**
	 * @param schedule
	 * @param scheduleSysUserId
	 * @param comment
	 */
	public void createScheduleUpdate(FacilitySchedule schedule , Long scheduleSysUserId){
		ScheduleUpdatePK pk = ScheduleUpdatePK.builder()
				.scheduleId(schedule.getId())
				.userId(scheduleSysUserId)
				.build();
		FacilityScheduleUpdate scheduleUpdate = FacilityScheduleUpdate.builder()
				.id(pk)
				.scheduleSysUser(scheduleSysUserDao.findOne(scheduleSysUserId))
				.build();
		scheduleUpdate = facilityScheduleUpdateDao.merge(scheduleUpdate);
		log.info("ScheduleUpdate : {}", scheduleUpdate);
	}
	
	/**
	 * @param id
	 */
	@Transactional
	public void delete(Long id){
		log.debug("Deleting schedule with id : {}", id);
		FacilitySchedule schedule = scheduleDao.findOne(id);
		if(schedule == null){
			log.error("No schedule found with id : {}", id);
			throw new RuntimeException("No schedule found with id : " + id);
		}
		scheduleDao.delete(schedule);
	}
	
	/**
	 * @param nurseId
	 * @param shiftId
	 * @param scheduleDate
	 * @return
	 */
	private void assertUniqueShiftPerDay(Long nurseId, Long shiftId, Date scheduleDate){
		FacilitySchedule schedule = scheduleDao.findByNurseAndShiftAndDate(nurseId, shiftId, scheduleDate);
		if(schedule != null){
			log.error("Nurse with id : {} already has a shift on  {}", nurseId, scheduleDate);
			throw new RuntimeException("Nurse with id : " + nurseId + " already has a shift on " + scheduleDate);
		}
	}
	
	@Transactional
	public GetScheduleViewModel findOne(Long id){
		log.info("Finding schedule with id : {}", id);
		FacilitySchedule schedule = scheduleDao.findOne(id);
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
		List<FacilitySchedule> schedules = scheduleDao.findAllBetweenDatesByFacility(startDate, endDate, facilityId);
		for(FacilitySchedule schedule : schedules){
			viewModels.add(this.buildGetScheduleViewModel(schedule));
		}
		log.info("Schedule found : {}", viewModels);
		return viewModels;
	}
	
	@Transactional
	public List<GetScheduleViewModel> findAllBetweenDatesByNurse(Date startDate, Date endDate, Long nurseId){
		log.info("Fetching schedules between startDate : {} and endDate : {} for employee with id : {}", startDate, endDate, nurseId);
		List<GetScheduleViewModel> viewModels = new LinkedList<>();
		List<FacilitySchedule> schedules = scheduleDao.findAllBetweenDatesByNurse(startDate, endDate, nurseId);
		for(FacilitySchedule schedule : schedules){
			viewModels.add(this.buildGetScheduleViewModel(schedule));
		}
		log.info("Schedule found : {}", viewModels);
		return viewModels;
	}
	
	public List<GetScheduleViewModel> findAllByNurse(Long nurseId){
		log.debug("Fetching schedules for employee with id : {}", nurseId);
		List<FacilitySchedule> schedules = scheduleDao.findAllByNurse(nurseId);
		List<GetScheduleViewModel> viewModels = new LinkedList<>();
		for(FacilitySchedule schedule : schedules){
			viewModels.add(this.buildGetScheduleViewModel(schedule));
		}
		return viewModels;
	}
	
	
	/**
	 * @param facilityId
	 * @return
	 */
	public List<GetScheduleViewModel> findAllByFacility(Long facilityId){
		log.debug("Fetching schedules for faclity with id : {}", facilityId);
		List<FacilitySchedule> schedules = scheduleDao.findAllByFacility(facilityId);
		List<GetScheduleViewModel> viewModels = new LinkedList<>();
		for(FacilitySchedule schedule : schedules){
			viewModels.add(this.buildGetScheduleViewModel(schedule));
		}
		return viewModels;
	}
	
	/**
	 * @param shiftId
	 * @return
	 */
	public FacilityShift validateShift(Long shiftId){
		FacilityShift shift = shiftDao.findOne(shiftId);
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
	
	public Nurse validateNurse(Long nurseId){
		if(nurseId == null){
			return null;
		}
		Nurse employee = nurseDao.findOne(nurseId);
		if(employee == null){
			log.error("No employee found with id : {}", nurseId);
			throw new RuntimeException("No employee found with id : " + nurseId);
		}
		return employee;
	}
	
	/**
	 * @param schedule
	 * @return
	 */
	private GetScheduleViewModel buildGetScheduleViewModel(FacilitySchedule schedule){
		EmployeeViewModel employeeVm = null;
		PositionViewModel positionVm = null;
		if(schedule.getNurse() != null){
			employeeVm = dozerMapper.map(schedule.getNurse().getEmployee(), EmployeeViewModel.class);
			positionVm = dozerMapper.map(schedule.getNurse().getEmployee().getPosition(), PositionViewModel.class);
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
		log.info("Schedule ID : {}", schedule.getId());
		FacilityScheduleUpdate scheduleUpdate = facilityScheduleUpdateDao.findLatestByScheduleId(schedule.getId());
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