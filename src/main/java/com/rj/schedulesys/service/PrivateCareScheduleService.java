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
import com.rj.schedulesys.dao.PrivateCareDao;
import com.rj.schedulesys.dao.PrivateCareScheduleDao;
import com.rj.schedulesys.dao.PrivateCareScheduleUpdateDao;
import com.rj.schedulesys.dao.PrivateCareShiftDao;
import com.rj.schedulesys.dao.ScheduleStatusDao;
import com.rj.schedulesys.dao.ScheduleSysUserDao;
import com.rj.schedulesys.data.ScheduleStatusConstants;
import com.rj.schedulesys.domain.Employee;
import com.rj.schedulesys.domain.PrivateCare;
import com.rj.schedulesys.domain.PrivateCareSchedule;
import com.rj.schedulesys.domain.PrivateCareScheduleUpdate;
import com.rj.schedulesys.domain.PrivateCareScheduleUpdatePK;
import com.rj.schedulesys.domain.PrivateCareShift;
import com.rj.schedulesys.domain.ScheduleStatus;
import com.rj.schedulesys.domain.ScheduleSysUser;
import com.rj.schedulesys.util.ObjectValidator;
import com.rj.schedulesys.util.ServiceHelper;
import com.rj.schedulesys.view.model.CreatePrivateCareScheduleViewModel;
import com.rj.schedulesys.view.model.EmployeeViewModel;
import com.rj.schedulesys.view.model.GetPrivateCareScheduleViewModel;
import com.rj.schedulesys.view.model.PrivateCareShiftViewModel;
import com.rj.schedulesys.view.model.PrivateCareViewModel;
import com.rj.schedulesys.view.model.ScheduleStatusViewModel;
import com.rj.schedulesys.view.model.ScheduleSysUserViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PrivateCareScheduleService {

	
	private EmployeeDao employeeDao;
	private PrivateCareShiftDao shiftDao;
	private PrivateCareDao privateCareDao;
	private ScheduleStatusDao scheduleStatusDao;
	private ScheduleSysUserDao scheduleSysUserDao;
	private PrivateCareScheduleDao privateCareScheduleDao;
	private PrivateCareScheduleUpdateDao privateCareScheduleUpdateDao;
	
	private DozerBeanMapper dozerMapper;
	private ObjectValidator<CreatePrivateCareScheduleViewModel> validator;
	
	@Autowired
	public PrivateCareScheduleService(EmployeeDao employeeDao, PrivateCareShiftDao shiftDao, PrivateCareDao privateCareDao,
			ScheduleStatusDao scheduleStatusDao, ScheduleSysUserDao scheduleSysUserDao, DozerBeanMapper dozerMapper,
			PrivateCareScheduleDao privateCareScheduleDao, ObjectValidator<CreatePrivateCareScheduleViewModel> validator,
			PrivateCareScheduleUpdateDao privateCareScheduleUpdateDao) {
		this.shiftDao = shiftDao;
		this.privateCareDao = privateCareDao;
		this.scheduleStatusDao = scheduleStatusDao;
		this.scheduleSysUserDao = scheduleSysUserDao;
		this.privateCareScheduleDao = privateCareScheduleDao;
		this.dozerMapper = dozerMapper;
		this.validator = validator;
		this.privateCareScheduleUpdateDao = privateCareScheduleUpdateDao;
		this.employeeDao = employeeDao;
	}
	
	/**
	 * @param viewModel
	 * @param scheduleSysUserId user creating the schedule
	 * @return
	 */
	@Transactional
	public CreatePrivateCareScheduleViewModel create(CreatePrivateCareScheduleViewModel viewModel, Long scheduleSysUserId){
		validator.validate(viewModel);
		PrivateCare privateCare = validatePrivateCare(viewModel.getPrivateCareId());
		ScheduleStatus scheduleStatus = validateScheduleStatus(viewModel.getScheduleStatusId());
		PrivateCareShift startShift = validateShift(viewModel.getStartShiftId());
		PrivateCareShift endShift = validateShift(viewModel.getEndShiftId());
		if(StringUtils.equalsIgnoreCase(scheduleStatus.getStatus(), ScheduleStatusConstants.CONFIRMED_STATUS) 
				&& viewModel.getEmployeeId() == null){
			log.error("The schedule is of status 'CONFIRMED' but no nurse or care giver is provided");
			throw new RuntimeException("The schedule is of status 'CONFIRMED' but no care giver is provided");
		}
		if(viewModel.getScheduleDate().before(ServiceHelper.yesterday())){
			log.error("Schedule date provided was a past date : {}");
			throw new RuntimeException("The schedule date provided is not valid. Please provide a future date. ");
		}
		Employee employee = null;
		if(viewModel.getEmployeeId() != null){
			employee = validateEmployee(viewModel.getEmployeeId());
			assertUniqueShiftPerDay(employee.getId(), startShift.getId(), endShift.getId(), viewModel.getScheduleDate());
		}
		//User creating the schedule
		ScheduleSysUser scheduleSysUser = scheduleSysUserDao.findOne(scheduleSysUserId);
		PrivateCareSchedule schedule = PrivateCareSchedule.builder()
				.employee(employee)
				.privateCare(privateCare)
				.startShift(startShift)
				.endShift(endShift)
				//.timesheetReceived(false)
				.scheduleComment(viewModel.getComment())
				.scheduleStatus(scheduleStatus)
				.createDate(new Date())
				.scheduleDate(viewModel.getScheduleDate())
				.scheduleSysUser(scheduleSysUser)
				.build();
		
		schedule = privateCareScheduleDao.merge(schedule);
		viewModel = dozerMapper.map(schedule, CreatePrivateCareScheduleViewModel.class);
		return viewModel;
	}
	
	@Transactional
	public CreatePrivateCareScheduleViewModel update(CreatePrivateCareScheduleViewModel viewModel, Long scheduleSysUserId){
		PrivateCareSchedule schedule = privateCareScheduleDao.findOne(viewModel.getId());
		if(schedule == null){
			log.error("No schedule found with id : {}", viewModel.getId());
			throw new RuntimeException("No schedule found with id : " + viewModel.getId());
		}
		PrivateCare privateCare = schedule.getPrivateCare();
		if(privateCare.getId() != viewModel.getId()){
			log.warn("Schedule's private care updated, validating new private care");
			privateCare = validatePrivateCare(viewModel.getPrivateCareId());
		}
		PrivateCareShift startShift = schedule.getStartShift();
		PrivateCareShift endShift = schedule.getEndShift();
		if(startShift.getId() != viewModel.getStartShiftId()){
			log.warn("Schedule's shift start time updated, validating new shift");
			startShift = validateShift(viewModel.getStartShiftId());
		}
		
		if(endShift.getId() != viewModel.getEndShiftId()){
			log.warn("Schedule's shift end time updated, validating new shift");
			endShift = validateShift(viewModel.getEndShiftId());
		}
		
		ScheduleStatus scheduleStatus = schedule.getScheduleStatus();
		if(scheduleStatus.getId() != viewModel.getScheduleStatusId()){
			log.warn("Schedule's status updated, validating new status");
			scheduleStatus = validateScheduleStatus(viewModel.getScheduleStatusId());
		}
		Employee employee = validateEmployee(viewModel.getEmployeeId());
		if(viewModel.getEmployeeId() != null && schedule.getEmployee() != null){
			if(viewModel.getEmployeeId() != schedule.getEmployee().getId()){
				//Make sure the employee does not already have a shift on the schedule date received
				assertUniqueShiftPerDay(employee.getId(), startShift.getId(), endShift.getId(), viewModel.getScheduleDate());
			}
		}
		schedule.setScheduleDate(viewModel.getScheduleDate());
		schedule.setEmployee(employee);
		schedule.setPrivateCare(privateCare);
		schedule.setStartShift(startShift);
		schedule.setEndShift(endShift);
		schedule.setPaid(viewModel.isPaid());
		schedule.setBilled(viewModel.isBilled());
		schedule.setScheduleComment(viewModel.getComment());
		schedule.setScheduleStatus(scheduleStatus);
		schedule = privateCareScheduleDao.merge(schedule);
		createScheduleUpdate(schedule, scheduleSysUserId);
		return dozerMapper.map(schedule, CreatePrivateCareScheduleViewModel.class);
	}
	
	/**
	 * @param schedule
	 * @param scheduleSysUserId
	 * @param comment
	 */
	public void createScheduleUpdate(PrivateCareSchedule schedule , Long scheduleSysUserId){
		PrivateCareScheduleUpdatePK pk = PrivateCareScheduleUpdatePK.builder()
				.scheduleId(schedule.getId())
				.userId(scheduleSysUserId)
				.build();
		PrivateCareScheduleUpdate scheduleUpdate = PrivateCareScheduleUpdate.builder()
				.id(pk)
				.scheduleSysUser(scheduleSysUserDao.findOne(scheduleSysUserId))
				.build();
		scheduleUpdate = privateCareScheduleUpdateDao.merge(scheduleUpdate);
		log.info("ScheduleUpdate : {}", scheduleUpdate);
	}
	
	/**
	 * @param id
	 */
	@Transactional
	public void delete(Long id){
		log.debug("Deleting schedule with id : {}", id);
		PrivateCareSchedule schedule = privateCareScheduleDao.findOne(id);
		if(schedule == null){
			log.error("No schedule found with id : {}", id);
			throw new RuntimeException("No schedule found with id : " + id);
		}
		privateCareScheduleDao.delete(schedule);
	}
	
	/**
	 * @param nurseId
	 * @param shiftId
	 * @param scheduleDate
	 * @return
	 */
	private void assertUniqueShiftPerDay(Long employeeId, Long startShiftId, Long endShiftId, Date scheduleDate){
		PrivateCareSchedule schedule = privateCareScheduleDao.findByEmployeeAndShiftAndDate(
				employeeId, startShiftId,endShiftId, scheduleDate);
		if(schedule != null){
			log.error("Care giver with id : {} already has a shift on  {}", employeeId, scheduleDate);
			throw new RuntimeException("Employee with id : " + employeeId + " already has a shift on " + scheduleDate);
		}
	}
	
	@Transactional
	public GetPrivateCareScheduleViewModel findOne(Long id){
		log.info("Finding schedule with id : {}", id);
		PrivateCareSchedule schedule = privateCareScheduleDao.findOne(id);
		GetPrivateCareScheduleViewModel viewModel = null;
		if(schedule != null){
			viewModel = this.buildGetScheduleViewModel(schedule);
		}
		log.info("Schedule found : {}", viewModel);
		return viewModel;
	}
	
	/**
	 * @param startDate
	 * @param endDate
	 * @param privateCareId
	 * @return
	 */
	@Transactional
	public List<GetPrivateCareScheduleViewModel> findAllBetweenDatesByPrivateCare(Date startDate, Date endDate, Long privateCareId){
		log.info("Fetching schedules between startDate : {} and endDate : {} for private care with id : {}", startDate, endDate, privateCareId);
		List<GetPrivateCareScheduleViewModel> viewModels = new LinkedList<>();
		List<PrivateCareSchedule> schedules = privateCareScheduleDao.findAllBetweenDatesByPrivateCare(startDate, endDate, privateCareId);
		schedules.stream()
		.forEach(schedule -> {
			viewModels.add(this.buildGetScheduleViewModel(schedule));
		});
		log.info("Schedule found : {}", viewModels);
		return viewModels;
	}
	
	@Transactional
	public List<GetPrivateCareScheduleViewModel> findAllBetweenDatesByEmployee(Date startDate, Date endDate, Long employeeId){
		log.info("Fetching schedules between startDate : {} and endDate : {} for care giver with id : {}", startDate, endDate, employeeId);
		List<GetPrivateCareScheduleViewModel> viewModels = new LinkedList<>();
		List<PrivateCareSchedule> schedules = privateCareScheduleDao.findAllBetweenDatesByEmployeeId(startDate, endDate, employeeId);
		schedules.stream()
		.forEach(schedule -> {
			viewModels.add(this.buildGetScheduleViewModel(schedule));
		});
		log.info("Schedules found : {}", viewModels);
		return viewModels;
	}
	
	public List<GetPrivateCareScheduleViewModel> findAllByEmployee(Long employeeId){
		log.debug("Fetching schedules for care giver with id : {}", employeeId);
		List<PrivateCareSchedule> schedules = privateCareScheduleDao.findAllByEmployeeId(employeeId);
		List<GetPrivateCareScheduleViewModel> viewModels = new LinkedList<>();
		schedules.stream()
		.forEach(schedule -> {
			viewModels.add(this.buildGetScheduleViewModel(schedule));
		});
		return viewModels;
	}
	
	
	/**
	 * @param privateCareId
	 * @return
	 */
	public List<GetPrivateCareScheduleViewModel> findAllByPrivateCare(Long privateCareId){
		log.debug("Fetching schedules for faclity with id : {}", privateCareId);
		List<PrivateCareSchedule> schedules = privateCareScheduleDao.findAllByPrivateCare(privateCareId);
		List<GetPrivateCareScheduleViewModel> viewModels = new LinkedList<>();
		schedules.stream()
		.forEach(schedule -> {
			viewModels.add(buildGetScheduleViewModel(schedule));
		});
		return viewModels;
	}
	
	/**
	 * @param shiftId
	 * @return
	 */
	public PrivateCareShift validateShift(Long shiftId){
		PrivateCareShift shift = shiftDao.findOne(shiftId);
		if(shift == null){
			log.error("No shift found with id : {}", shiftId);
			throw new RuntimeException("No shift found with id : " + shiftId);
		}
		return shift;
	}
	
	/**
	 * @param privateCareId
	 * @return
	 */
	public PrivateCare validatePrivateCare(Long privateCareId){
		PrivateCare privateCare = privateCareDao.findOne(privateCareId);
		if(privateCare == null){
			log.error("No private care found with id : {}", privateCare);
			throw new RuntimeException("No private care found with id : " + privateCareId);
		}
		return privateCare;
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
	
	public Employee validateEmployee(Long employeeId){
		if(employeeId == null){
			return null;
		}
		Employee employee = employeeDao.findOne(employeeId);
		if(employee == null){
			log.error("No employee found with id : {}", employee);
			throw new RuntimeException("No care employee found with id : " + employee);
		}
		return employee;
	}
	
	/**
	 * @param schedule
	 * @return
	 */
	private GetPrivateCareScheduleViewModel buildGetScheduleViewModel(PrivateCareSchedule schedule){
		EmployeeViewModel employeeVm = null;
		if(schedule.getEmployee() != null){
			employeeVm = dozerMapper.map(schedule.getEmployee(), EmployeeViewModel.class);
		}
		PrivateCareViewModel facilityVm = dozerMapper.map(schedule.getPrivateCare(), PrivateCareViewModel.class);
		PrivateCareShiftViewModel startShiftVm = dozerMapper.map(schedule.getStartShift(), PrivateCareShiftViewModel.class);
		PrivateCareShiftViewModel endShiftVm = dozerMapper.map(schedule.getEndShift(), PrivateCareShiftViewModel.class);
		ScheduleStatusViewModel scheduleStatusVm = dozerMapper.map(schedule.getScheduleStatus(), ScheduleStatusViewModel.class);
		ScheduleSysUserViewModel filledBy = dozerMapper.map(schedule.getScheduleSysUser(), ScheduleSysUserViewModel.class);
		ScheduleSysUserViewModel lastModifiedBy = null;
		PrivateCareScheduleUpdate scheduleUpdate = privateCareScheduleUpdateDao.findLatestByScheduleId(schedule.getId());
		if(scheduleUpdate != null){
			lastModifiedBy = dozerMapper.map(scheduleUpdate.getScheduleSysUser(), ScheduleSysUserViewModel.class);
		}
		GetPrivateCareScheduleViewModel viewModel = GetPrivateCareScheduleViewModel.builder()
				.id(schedule.getId()).employee(employeeVm)
				.privateCare(facilityVm).filledBy(filledBy)
				.lastModifiedBy(lastModifiedBy).scheduleComment(schedule.getScheduleComment())
				.scheduleDate(schedule.getScheduleDate()).startShift(startShiftVm).endShift(endShiftVm)
				.timesheetReceived(schedule.getTimesheetReceived())
				.billed(schedule.getBilled()).paid(schedule.getPaid())
				.scheduleStatus(scheduleStatusVm)
				.build();
		return viewModel;
	}
	
}
