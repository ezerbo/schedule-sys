package com.rj.sys.service;

import lombok.extern.slf4j.Slf4j;

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
	
	@Transactional
	public ScheduleViewModel createSchedule(ScheduleViewModel viewModel){
		log.info("Creating schedule : {}", viewModel);
		User assignee = userDao.findOne(viewModel.getAssigneeId());
		Shift shift = shiftDao.findByName(viewModel.getShift());
		Facility facility = facilityDao.findByName(viewModel.getFacility());
		ScheduleStatus scheduleStatus = scheduleStatusDao.findByStatus(viewModel.getScheduleStatus());
		//SchedulePostStatus schedulePostStatus = schedulePostStatusDao.findByStatus(viewModel.getSchedulePostStatus());
		Schedule schedule = Schedule.builder()
				.assignee(assignee)
				.facility(facility)
				.scheduleStatus(scheduleStatus)
				.shift(shift)
				.scheduleDate(viewModel.getCreateDate())
				.scheduleComment(viewModel.getScheduleComment())
				.build();
		return viewModel;
	}
	
	@Transactional
	public void updateSchedule(){
		log.debug("Updating user {}");
	}
}
