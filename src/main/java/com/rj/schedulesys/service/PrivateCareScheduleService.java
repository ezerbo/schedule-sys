package com.rj.schedulesys.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.schedulesys.dao.CareGiverDao;
import com.rj.schedulesys.dao.PrivateCareDao;
import com.rj.schedulesys.dao.PrivateCareScheduleDao;
import com.rj.schedulesys.dao.PrivateCareScheduleUpdateDao;
import com.rj.schedulesys.dao.PrivateCareShiftDao;
import com.rj.schedulesys.dao.ScheduleStatusDao;
import com.rj.schedulesys.dao.ScheduleSysUserDao;
import com.rj.schedulesys.data.ScheduleStatusConstants;
import com.rj.schedulesys.domain.CareGiver;
import com.rj.schedulesys.domain.PrivateCare;
import com.rj.schedulesys.domain.PrivateCareSchedule;
import com.rj.schedulesys.domain.PrivateCareScheduleUpdate;
import com.rj.schedulesys.domain.PrivateCareScheduleUpdatePK;
import com.rj.schedulesys.domain.PrivateCareShift;
import com.rj.schedulesys.domain.ScheduleStatus;
import com.rj.schedulesys.domain.ScheduleSysUser;
import com.rj.schedulesys.util.ObjectValidator;
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

	
	private CareGiverDao careGiverDao;
	private PrivateCareShiftDao shiftDao;
	private PrivateCareDao privateCareDao;
	private ScheduleStatusDao scheduleStatusDao;
	private ScheduleSysUserDao scheduleSysUserDao;
	private PrivateCareScheduleDao privateCareScheduleDao;
	private PrivateCareScheduleUpdateDao privateCareScheduleUpdateDao;
	
	private DozerBeanMapper dozerMapper;
	private ObjectValidator<CreatePrivateCareScheduleViewModel> validator;
	
	@Autowired
	public PrivateCareScheduleService(CareGiverDao careGiverDao, PrivateCareShiftDao shiftDao, PrivateCareDao privateCareDao,
			ScheduleStatusDao scheduleStatusDao, ScheduleSysUserDao scheduleSysUserDao, DozerBeanMapper dozerMapper,
			PrivateCareScheduleDao privateCareScheduleDao, ObjectValidator<CreatePrivateCareScheduleViewModel> validator,
			PrivateCareScheduleUpdateDao privateCareScheduleUpdateDao) {
		this.shiftDao = shiftDao;
		this.careGiverDao = careGiverDao;
		this.privateCareDao = privateCareDao;
		this.scheduleStatusDao = scheduleStatusDao;
		this.scheduleSysUserDao = scheduleSysUserDao;
		this.privateCareScheduleDao = privateCareScheduleDao;
		this.dozerMapper = dozerMapper;
		this.validator = validator;
		this.privateCareScheduleUpdateDao = privateCareScheduleUpdateDao;
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
		PrivateCareShift shift = validateShift(viewModel.getShiftId());
		if(StringUtils.equalsIgnoreCase(scheduleStatus.getStatus(), ScheduleStatusConstants.CONFIRMED_STATUS) 
				&& viewModel.getCareGiverId() == null){
			log.error("The schedule is of status 'CONFIRMED' but no nurse or care giver is provided");
			throw new RuntimeException("The schedule is of status 'CONFIRMED' but no care giver is provided");
		}
		if(viewModel.getScheduleDate().before(new Date())){
			log.error("Schedule date provided was a past date : {}");
			throw new RuntimeException("The schedule date provided is not valid. Please provide a future date. ");
		}
		CareGiver nurse = null;
		if(viewModel.getCareGiverId() != null){
			nurse = validateCareGiver(viewModel.getCareGiverId());
			assertUniqueShiftPerDay(nurse.getId(), shift.getId(), viewModel.getScheduleDate());
		}
		//User creating the schedule
		ScheduleSysUser scheduleSysUser = scheduleSysUserDao.findOne(scheduleSysUserId);
		PrivateCareSchedule schedule = PrivateCareSchedule.builder()
				.careGiver(nurse)
				.privateCare(privateCare)
				.shift(shift)
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
		PrivateCareShift shift = schedule.getShift();
		if(shift.getId() != viewModel.getShiftId()){
			log.warn("Schedule's shift updated, validating new shift");
			shift = validateShift(viewModel.getShiftId());
		}
		ScheduleStatus scheduleStatus = schedule.getScheduleStatus();
		if(scheduleStatus.getId() != viewModel.getScheduleStatusId()){
			log.warn("Schedule's status updated, validating new status");
			scheduleStatus = validateScheduleStatus(viewModel.getScheduleStatusId());
		}
		CareGiver employee = validateCareGiver(viewModel.getCareGiverId());
		if(viewModel.getCareGiverId() != null && schedule.getCareGiver() != null){
			if(viewModel.getCareGiverId() != schedule.getCareGiver().getId()){
				//Make sure the employee does not already have a shift on the schedule date received
				assertUniqueShiftPerDay(employee.getId(), shift.getId(), viewModel.getScheduleDate());
			}
		}
		schedule.setScheduleDate(viewModel.getScheduleDate());
		schedule.setCareGiver(employee);
		schedule.setPrivateCare(privateCare);
		schedule.setShift(shift);
//		schedule.setTimesheetReceived(viewModel.isTimesheetReceived());
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
	private void assertUniqueShiftPerDay(Long nurseId, Long shiftId, Date scheduleDate){
		PrivateCareSchedule schedule = privateCareScheduleDao.findByCareGiverAndShiftAndDate(nurseId, shiftId, scheduleDate);
		if(schedule != null){
			log.error("Care giver with id : {} already has a shift on  {}", nurseId, scheduleDate);
			throw new RuntimeException("Care giver with id : " + nurseId + " already has a shift on " + scheduleDate);
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
	public List<GetPrivateCareScheduleViewModel> findAllBetweenDatesByCareGiver(Date startDate, Date endDate, Long nurseId){
		log.info("Fetching schedules between startDate : {} and endDate : {} for care giver with id : {}", startDate, endDate, nurseId);
		List<GetPrivateCareScheduleViewModel> viewModels = new LinkedList<>();
		List<PrivateCareSchedule> schedules = privateCareScheduleDao.findAllBetweenDatesByCareGiver(startDate, endDate, nurseId);
		schedules.stream()
		.forEach(schedule -> {
			viewModels.add(this.buildGetScheduleViewModel(schedule));
		});
		log.info("Schedules found : {}", viewModels);
		return viewModels;
	}
	
	public List<GetPrivateCareScheduleViewModel> findAllByCareGiver(Long nurseId){
		log.debug("Fetching schedules for care giver with id : {}", nurseId);
		List<PrivateCareSchedule> schedules = privateCareScheduleDao.findAllByCareGiver(nurseId);
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
	
	public CareGiver validateCareGiver(Long careGiverId){
		if(careGiverId == null){
			return null;
		}
		CareGiver careGiver = careGiverDao.findOne(careGiverId);
		if(careGiver == null){
			log.error("No care giver found with id : {}", careGiverId);
			throw new RuntimeException("No care giver found with id : " + careGiverId);
		}
		return careGiver;
	}
	
	/**
	 * @param schedule
	 * @return
	 */
	private GetPrivateCareScheduleViewModel buildGetScheduleViewModel(PrivateCareSchedule schedule){
		EmployeeViewModel employeeVm = null;
		if(schedule.getCareGiver() != null){
			employeeVm = dozerMapper.map(schedule.getCareGiver().getEmployee(), EmployeeViewModel.class);
		}
		PrivateCareViewModel facilityVm = dozerMapper.map(schedule.getPrivateCare(), PrivateCareViewModel.class);
		PrivateCareShiftViewModel shiftVm = dozerMapper.map(schedule.getShift(), PrivateCareShiftViewModel.class);
		ScheduleStatusViewModel scheduleStatusVm = dozerMapper.map(schedule.getScheduleStatus(), ScheduleStatusViewModel.class);
		ScheduleSysUserViewModel filledBy = dozerMapper.map(schedule.getScheduleSysUser(), ScheduleSysUserViewModel.class);
		ScheduleSysUserViewModel lastModifiedBy = null;
		PrivateCareScheduleUpdate scheduleUpdate = privateCareScheduleUpdateDao.findLatestByScheduleId(schedule.getId());
		if(scheduleUpdate != null){
			lastModifiedBy = dozerMapper.map(scheduleUpdate.getScheduleSysUser(), ScheduleSysUserViewModel.class);
		}
		GetPrivateCareScheduleViewModel viewModel = GetPrivateCareScheduleViewModel.builder()
				.id(schedule.getId())
				.employee(employeeVm)
				.privateCare(facilityVm)
				.filledBy(filledBy)
				.lastModifiedBy(lastModifiedBy)
				.scheduleComment(schedule.getScheduleComment())
				.scheduleDate(schedule.getScheduleDate())
				.shift(shiftVm)
				.timesheetReceived(schedule.getTimesheetReceived())
				.scheduleStatus(scheduleStatusVm)
				.build();
		return viewModel;
	}
	
}
