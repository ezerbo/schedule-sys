package com.rj.sys.service;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.sys.dao.SchedulePostStatusDao;
import com.rj.sys.domain.SchedulePostStatus;
import com.rj.sys.view.model.SchedulePostStatusViewModel;

@Slf4j
@Service
public class SchedulePostStatusService {
	
	private @Autowired SchedulePostStatusDao schedulePostStatusDao;
	
	private @Autowired DozerBeanMapper dozerMapper;
	
	@Transactional
	public List<SchedulePostStatusViewModel> findAll(){
		log.info("Finding all schedule post statuses");
		
		List<SchedulePostStatus> schedulePostStatuses = schedulePostStatusDao.findAll();
		List<SchedulePostStatusViewModel> viewModels = new LinkedList<SchedulePostStatusViewModel>();
		
		for(SchedulePostStatus schedulePostStatus : schedulePostStatuses){
			viewModels.add(
					dozerMapper.map(schedulePostStatus, SchedulePostStatusViewModel.class)
					);
		}
		
		return viewModels;
	}
	
	@Transactional
	public SchedulePostStatusViewModel findById(Long id){
		log.info("Finding schedule post status by id : {}", id);
		SchedulePostStatus schedulePostStatus = schedulePostStatusDao.findOne(id);
		SchedulePostStatusViewModel viewModel = null;
		
		if(schedulePostStatus != null){
			viewModel = dozerMapper.map(
					schedulePostStatus, SchedulePostStatusViewModel.class
					);
		}
		
		return viewModel;
	}
	
	@Transactional
	public SchedulePostStatusViewModel findByStatus(String status){
		log.info("Finding schedule by status : {}", status);
		SchedulePostStatusViewModel viewModel = null;
		try{
			viewModel = dozerMapper.map(
					schedulePostStatusDao.findByStatus(status), SchedulePostStatusViewModel.class
					);
		}catch(Exception nre){
			log.info("No schedule post status found with status : {}", status);
		}
		return viewModel;
	}
}