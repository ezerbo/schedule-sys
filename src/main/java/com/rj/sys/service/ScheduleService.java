package com.rj.sys.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.sys.dao.FacilityDao;
import com.rj.sys.dao.ScheduleDao;
import com.rj.sys.dao.SchedulePostStatusDao;
import com.rj.sys.dao.ScheduleStatusDao;
import com.rj.sys.dao.ScheduleUpdateDao;
import com.rj.sys.dao.ShiftDao;
import com.rj.sys.dao.UserDao;
import com.rj.sys.domain.Facility;
import com.rj.sys.domain.Schedule;
import com.rj.sys.domain.SchedulePostStatus;
import com.rj.sys.domain.ScheduleStatus;
import com.rj.sys.domain.ScheduleUpdate;
import com.rj.sys.domain.ScheduleUpdatePK;
import com.rj.sys.domain.Shift;
import com.rj.sys.domain.User;
import com.rj.sys.utils.Constants;
import com.rj.sys.utils.ObjectValidator;
import com.rj.sys.utils.ServiceHelper;
import com.rj.sys.view.model.EmployeeScheduleViewModel;
import com.rj.sys.view.model.ScheduleUpdateViewModel;
import com.rj.sys.view.model.ScheduleViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ScheduleService {
	
	private @Autowired UserDao userDao;
	private @Autowired ShiftDao shiftDao;
	private @Autowired FacilityDao facilityDao;
	private @Autowired ScheduleDao scheduleDao;
	private @Autowired ScheduleUpdateDao scheduleUpdateDao;
	private @Autowired ScheduleStatusDao scheduleStatusDao;
	private @Autowired SchedulePostStatusDao schedulePostStatusDao;
	
	private @Autowired DozerBeanMapper dozerMapper;
	
	private @Autowired ObjectValidator<ScheduleViewModel> validator;
	
	@Transactional
	public ScheduleViewModel createSchedule(ScheduleViewModel viewModel, Long assignerId){
		validator.validate(viewModel);
		Schedule schedule = buildSchedule(viewModel, assignerId);
		log.info("Creating schedule : {}", schedule);
		schedule = scheduleDao.merge(schedule);
		viewModel = dozerMapper.map(schedule, ScheduleViewModel.class);
		log.info("Created schedule : {}", viewModel);
		return viewModel;
	}
	
	@Transactional
	public ScheduleUpdateViewModel updateSchedule(ScheduleUpdateViewModel viewModel, Long userUpdatingScheduleId){
		Schedule schedule = buildSchedule(viewModel);
		User userUpdatingSchedule = userDao.findOne(userUpdatingScheduleId);
		log.info("Updating schedule : {}", schedule);
		schedule = scheduleDao.merge(schedule);
		
		ScheduleUpdatePK pk = ScheduleUpdatePK.builder()
				.scheduleId(schedule.getId().intValue())
				.userId(userUpdatingScheduleId.intValue())
				.build();
		
		
		ScheduleUpdate scheduleUpdate = ScheduleUpdate.builder()
				.id(pk)
				.schedule(schedule)
				.user(userUpdatingSchedule)
				.updateTime(new DateTime())
				.build();
		scheduleUpdate = scheduleUpdateDao.merge(scheduleUpdate);
		log.info("ScheduleUpdate : {}", scheduleUpdate);
		
		return dozerMapper.map(schedule, ScheduleUpdateViewModel.class);
	}
	
	@Transactional
	public ScheduleViewModel findById(Long id){
		log.info("Finding schedule with id : {}", id);
		Schedule schedule = scheduleDao.findOne(id);
		ScheduleViewModel viewModel = null;
		if(schedule != null){
			viewModel = dozerMapper.map(schedule, ScheduleViewModel.class);
		}
		
		log.info("Schedule found : {}", viewModel);
		return viewModel;
	}
	
	@Transactional
	public List<EmployeeScheduleViewModel> findAllBetweenDatesByFacilityId(Date startDate, Date endDate, Long facilityId){
		
		log.info("Finding schedules between startDate : {} and endDate : {} for facility with id : {}", startDate, endDate, facilityId);
		
		List<EmployeeScheduleViewModel> viewModels = new LinkedList<>();
		List<Schedule> schedules = scheduleDao.findAllBetweenDatesByFacilityId(startDate, endDate, facilityId);
		for(Schedule schedule : schedules){
			
			EmployeeScheduleViewModel viewModel = dozerMapper.map(schedule, EmployeeScheduleViewModel.class);
			User assigner = schedule.getAssigner();
			User assignee = schedule.getAssignee();
			viewModel.setFilledBy(ServiceHelper.formatFirstAndLastNames(assigner));
			
			if(assignee != null){
				viewModel.setJob(assignee.getPosition().getName());
				viewModel.setEmployeeName(ServiceHelper.formatFirstAndLastNames(assignee));
			}else{
				viewModel.setEmployeeName(Constants.MISSING_EMPLOYEE);
			}
			
			List<ScheduleUpdate> scheduleUpdates = schedule.getScheduleUpdates();
			if(!scheduleUpdates.isEmpty()){
				ScheduleUpdate scheduleUpdate = scheduleUpdates.get(scheduleUpdates.size() - 1);//last update
				viewModel.setLastModifiedBy(ServiceHelper.formatFirstAndLastNames(scheduleUpdate.getUser()));
			}
			
			viewModels.add(viewModel);
		}
		
		
		log.info("Schedule found : {}", viewModels);
		return viewModels;
	}
	
	@Transactional
	public ScheduleViewModel findByScheduleIdAndFacilityId(Long scheduleId, Long facilityId){
		log.info("Finding schedule with id : {} for facility with id : {}", scheduleId, facilityId);
		ScheduleViewModel viewModel = null;
		try{
			viewModel = dozerMapper.map(
					scheduleDao.findByIdAndFacilityId(scheduleId, facilityId), ScheduleViewModel.class
					);
		}catch(Exception e){
			log.info("No schedule found with id : {} for facility with id : {}", scheduleId, facilityId);
		}
		log.info("Schedule found : {}", viewModel);
		return viewModel;
	}
	
	@Transactional
	public List<ScheduleViewModel> findScheduleByAssigneeId(Long id, Date scheduleDate){
		
		List<Schedule> schedules = scheduleDao.findByAssigneeId(id, scheduleDate);
		List<ScheduleViewModel> viewModels = new LinkedList<ScheduleViewModel>();
		
		for(Schedule schedule : schedules){
			viewModels.add(dozerMapper.map(schedule, ScheduleViewModel.class));
		}
		
		return viewModels;
	}
	
	@Transactional
	public ScheduleViewModel findByAssigneeIdAndScheduleId(Long assigneeId, Long scheduleId){
		log.info("Finding schedule by assigneeId : {} and schedule id : {}", assigneeId, scheduleId);
		ScheduleViewModel viewModel = null;
		try{
			viewModel = dozerMapper.map(
					scheduleDao.findByIdAndAssigneeId(scheduleId, assigneeId), ScheduleViewModel.class
					);
		}catch(Exception e){
			log.info("No schedule found with id : {} for user with id : {}", scheduleId, assigneeId);
		}
		return viewModel;
	}
	
	@Transactional
	public ScheduleUpdateViewModel findByFacilityIdAndScheduleId(Long facilityId, Long scheduleId){
		log.info("Finding schedule by facilityId : {} and scheduleId : {}", facilityId, scheduleId);
		return null;
	}
	
	@Transactional
	public ScheduleViewModel findScheduleByAssigneeNameAndShiftNameAndScheduleDate(String assigneeName, String shift, Date scheduleDate){
		String firstAndLastNames[] = ServiceHelper.getFirstAndLastNames(assigneeName);
		ScheduleViewModel viewModel = null;
		
		try{
			viewModel = dozerMapper.map(
					scheduleDao.findByAssigneeNameAndShiftNameAndScheduleDate(
							firstAndLastNames[0],firstAndLastNames[1], shift, scheduleDate
							), ScheduleViewModel.class
					);
		}catch(Exception nre){
			log.info(
					"No schedule found for employee with name : {} on shift with name : {}", firstAndLastNames[0], shift
					);
			log.error("", nre);
		}
		
		return viewModel;
	}
	
	@Transactional
	public List<ScheduleViewModel> findScheduleByAssigneerId(Long id){
		
		List<Schedule> schedules = scheduleDao.findByAssigneerId(id);
		List<ScheduleViewModel> viewModels = new LinkedList<ScheduleViewModel>();
		
		for(Schedule schedule : schedules){
			viewModels.add(dozerMapper.map(schedule, ScheduleViewModel.class));
		}
		
		return viewModels;
	}
	
	private Schedule buildSchedule(ScheduleViewModel viewModel, Long assignerId){
		log.info("Building schedule : {}", viewModel);
		
		User assignee = findAssignee(viewModel.getEmployeeName());
		
		User assigner = userDao.findOne(assignerId);
		Shift shift = shiftDao.findByName(viewModel.getShift());
		Facility facility = facilityDao.findActiveByName(viewModel.getFacility());
		ScheduleStatus scheduleStatus = scheduleStatusDao.findByStatus(viewModel.getScheduleStatus());
		Schedule schedule = Schedule.builder()
				.assignee(assignee)
				.assigner(assigner)
				.facility(facility)
				.scheduleStatus(scheduleStatus)
				.shift(shift)
				.scheduleComment(viewModel.getScheduleComment())
				.scheduleDate(viewModel.getScheduleDate())
				.build()
				;
		return schedule;
	}
	
	private Schedule buildSchedule(ScheduleUpdateViewModel viewModel){
		log.info("Building schedule with : {}", viewModel);
		
		User assignee = findAssignee(viewModel.getEmployeeName());
		User assigner = scheduleDao.findOne(viewModel.getId()).getAssigner();
		Shift shift = shiftDao.findByName(viewModel.getShift());
		Facility facility = facilityDao.findActiveByName(viewModel.getFacility());
		ScheduleStatus scheduleStatus = scheduleStatusDao.findByStatus(viewModel.getScheduleStatus());
		SchedulePostStatus schedulePostStatus = null;
		
		if(viewModel.getSchedulePostStatus() == null){
			try{
				schedulePostStatus = schedulePostStatusDao.findByStatus(
						viewModel.getSchedulePostStatus()
						);
			}catch(Exception nre){
				log.info("No schedule status found by status : {}", viewModel.getScheduleStatus());
			}
		}
		
		Schedule schedule = Schedule.builder()
				.id(viewModel.getId())
				.assignee(assignee)
				.assigner(assigner)
				.facility(facility)
				.scheduleStatus(scheduleStatus)
				.schedulePostStatus(schedulePostStatus)
				.shift(shift)
				.scheduleComment(viewModel.getScheduleComment())
				.scheduleDate(viewModel.getScheduleDate())
				.build();

		return schedule;
	}
	
	public User findAssignee(String employeeName){
		log.info("Finding assignee from name : {}", employeeName);
		User assignee = null;
		if(employeeName != null){
			String [] firstAndLastnames = ServiceHelper.getFirstAndLastNames(employeeName);
			assignee = userDao.findByFirstAndLastNames(firstAndLastnames[0], firstAndLastnames[1]);
		}
		return assignee;
	}
}