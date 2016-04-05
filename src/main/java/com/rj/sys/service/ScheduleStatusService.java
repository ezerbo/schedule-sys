package com.rj.sys.service;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.sys.dao.ScheduleStatusDao;
import com.rj.sys.domain.ScheduleStatus;
import com.rj.sys.view.model.ScheduleStatusViewModel;

@Slf4j
@Service
public class ScheduleStatusService {
	
	private @Autowired ScheduleStatusDao scheduleStatusDao;
	
	private @Autowired DozerBeanMapper dozerMapper;
	
	@Transactional
	public List<ScheduleStatusViewModel> findAll(){
		
		log.info("Finding all schedule statuses");
		List<ScheduleStatus> scheduleStatuses = scheduleStatusDao.findAll();
		List<ScheduleStatusViewModel> viewModels = new LinkedList<ScheduleStatusViewModel>();
		for(ScheduleStatus scheduleStatus : scheduleStatuses){
			viewModels.add(
					dozerMapper.map(scheduleStatus, ScheduleStatusViewModel.class)
					);
		}
		
		return viewModels;
	}
	
	@Transactional
	public ScheduleStatusViewModel findById(Long id){
		log.info("Finding status by id : {}", id);
		
		ScheduleStatus scheduleStatus = scheduleStatusDao.findOne(id);
		ScheduleStatusViewModel viewModel = null;
		
		if(scheduleStatus != null){
			viewModel = dozerMapper.map(scheduleStatus, ScheduleStatusViewModel.class);
		}
		
		return viewModel;
	}
	
	@Transactional
	public ScheduleStatusViewModel findByStatus(String status){
		
		log.info("Finding schedule status by status : {}", status);
		ScheduleStatusViewModel viewModel = null;
		
		try{
			viewModel = dozerMapper.map(
					scheduleStatusDao.findByStatus(status), ScheduleStatusViewModel.class
					);
		}catch(Exception nre){
			log.info("No schedule status found with status : {}", status);
		}
		
		return viewModel;
	}
}