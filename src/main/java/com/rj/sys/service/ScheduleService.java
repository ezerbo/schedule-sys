package com.rj.sys.service;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.sys.dao.FacilityDao;
import com.rj.sys.dao.ScheduleDao;
import com.rj.sys.dao.SchedulePostStatusDao;
import com.rj.sys.dao.ScheduleStatusDao;
import com.rj.sys.dao.ShiftDao;
import com.rj.sys.dao.UserDao;
import com.rj.sys.domain.Facility;
import com.rj.sys.domain.Schedule;
import com.rj.sys.domain.SchedulePostStatus;
import com.rj.sys.domain.ScheduleStatus;
import com.rj.sys.domain.Shift;
import com.rj.sys.domain.User;
import com.rj.sys.view.model.ScheduleViewModel;

@Slf4j
@Service
public class ScheduleService {
	
	private @Autowired UserDao userDao;
	private @Autowired ShiftDao shiftDao;
	private @Autowired FacilityDao facilityDao;
	private @Autowired ScheduleDao scheduleDao;
	private @Autowired ScheduleStatusDao scheduleStatusDao;
	private @Autowired SchedulePostStatusDao schedulePostStatusDao;
	
	private @Autowired DozerBeanMapper dozerMapper;
	
	@Transactional
	public ScheduleViewModel createSchedule(ScheduleViewModel viewModel){
		Schedule schedule = buildSchedule(viewModel);
		log.info("Creating schedule : {}", schedule);
		schedule = scheduleDao.merge(schedule);
		return dozerMapper.map(schedule, ScheduleViewModel.class);
	}
	
	@Transactional
	public ScheduleViewModel updateSchedule(ScheduleViewModel viewModel){
		Schedule schedule = buildSchedule(viewModel);
		SchedulePostStatus schedulePostStatus = schedulePostStatusDao.findByStatus(
				viewModel.getSchedulePostStatus()
				);
		schedule.setSchedulePostStatus(schedulePostStatus);
		log.debug("Updating schedule : {}",schedule);
		schedule = scheduleDao.merge(schedule);
		return dozerMapper.map(schedule, ScheduleViewModel.class);
	}
	
	@Transactional
	public List<ScheduleViewModel> findScheduleByAssigneeId(Long id){
		
		List<Schedule> schedules = scheduleDao.findByAssigneeId(id);
		List<ScheduleViewModel> viewModels = new LinkedList<ScheduleViewModel>();
		
		for(Schedule schedule : schedules){
			viewModels.add(dozerMapper.map(schedule, ScheduleViewModel.class));
		}
		
		return viewModels;
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
	
	private Schedule buildSchedule(ScheduleViewModel viewModel){
		log.info("Building schedule : {}", viewModel);
		
		User assignee = userDao.findOne(viewModel.getAssigneeId());
		Shift shift = shiftDao.findByName(viewModel.getShift());
		Facility facility = facilityDao.findActiveByName(viewModel.getFacility());
		ScheduleStatus scheduleStatus = scheduleStatusDao.findByStatus(viewModel.getScheduleStatus());
		Schedule schedule = Schedule.builder()
				.assignee(assignee)
				.facility(facility)
				.scheduleStatus(scheduleStatus)
				.shift(shift)
				.scheduleDate(viewModel.getCreateDate())
				.scheduleComment(viewModel.getScheduleComment())
				.build()
				;
		return schedule;
	}
}
