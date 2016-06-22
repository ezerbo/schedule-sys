package com.rj.schedulesys.service;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.rj.schedulesys.dao.ScheduleStatusDao;
import com.rj.schedulesys.domain.ScheduleStatus;
import com.rj.schedulesys.util.ObjectValidator;
import com.rj.schedulesys.view.model.ScheduleStatusViewModel;

@Slf4j
@Service
public class ScheduleStatusService {
	
	private @Autowired ScheduleStatusDao scheduleStatusDao;
	
	private @Autowired ObjectValidator<ScheduleStatusViewModel> validator;
	
	private @Autowired DozerBeanMapper dozerMapper;
	
	
	@Transactional
	public ScheduleStatusViewModel create(ScheduleStatusViewModel viewModel){
		
		log.debug("Creating new schedule status : {}", viewModel);
		
		Assert.notNull(viewModel, "No schedule status provided");
		
		ScheduleStatus scheduleStatus = scheduleStatusDao.findByStatus(viewModel.getStatus());
		
		if(scheduleStatus != null){
			log.error("A schedule status with status : {} already exist", viewModel.getStatus());
			throw new RuntimeException("A schedule status with status : " + viewModel.getStatus() + " already exists");
		}
		
		viewModel = this.createOrUpdate(viewModel);
		
		log.debug("Created schedule status : {}", viewModel);
		
		return viewModel;
		
	}
	
	/**
	 * @param viewModel
	 * @return
	 */
	@Transactional
	public ScheduleStatusViewModel update(ScheduleStatusViewModel viewModel){
		
		log.debug("Updating schedule : {}", viewModel);
		
		Assert.notNull(viewModel, "No schedule post provided");
		
		ScheduleStatus scheduleStatus = scheduleStatusDao.findOne(viewModel.getId());
		
		if(scheduleStatus == null){
			log.error("No schedule status found with id : {}", viewModel.getId());
			throw new RuntimeException("No schedule status found with id : " + viewModel.getId());
		}
		
		if(!StringUtils.equalsIgnoreCase(scheduleStatus.getStatus(), viewModel.getStatus())){
			log.warn("Schedule status updated, checking its uniqueness");
			if(scheduleStatusDao.findByStatus(viewModel.getStatus()) != null){
				log.error("A schedule status with status : {} already exists", viewModel.getStatus());
				throw new RuntimeException("A schedule status with status : " + viewModel.getStatus() + " already exists");
			}
		}
		
		viewModel = this.createOrUpdate(viewModel);
		
		log.debug("Updated schedule status : {}", viewModel);
		
		return viewModel;
	}
	
	@Transactional
	private ScheduleStatusViewModel createOrUpdate(ScheduleStatusViewModel viewModel){
		
		validator.validate(viewModel);
		
		ScheduleStatus scheduleStatus = dozerMapper.map(viewModel, ScheduleStatus.class);
		scheduleStatus.setStatus(StringUtils.upperCase(scheduleStatus.getStatus()));
		scheduleStatus = scheduleStatusDao.merge(scheduleStatus);
		
		return dozerMapper.map(scheduleStatus, ScheduleStatusViewModel.class);
	}
	
	@Transactional
	public void delete(Long id){
		
		log.debug("Deleting schedule status with id : {}", id);
		
		ScheduleStatus scheduleStatus = scheduleStatusDao.findOne(id);
		
		if(scheduleStatus == null){
			log.error("No schedule status found with id : {}", id);
			throw new RuntimeException("No schedule status found with id : " + id);
		}
		
		
		if(!scheduleStatus.getSchedules().isEmpty()){
			log.error("Schedule status with id : {} can not be deleted", id);
			throw new RuntimeException("Schedule status with id : " + id + " can not be deleted");
		}
		
		scheduleStatusDao.delete(scheduleStatus);
		
		log.debug("Schedule status with id : {} successfully deleted");
	}
	
	/**
	 * @return
	 */
	@Transactional
	public List<ScheduleStatusViewModel> findAll(){
		
		log.debug("Fetching all schedule statuses");
		
		List<ScheduleStatus> scheduleStatuses = scheduleStatusDao.findAll();
		
		List<ScheduleStatusViewModel> viewModels = new LinkedList<ScheduleStatusViewModel>();
		
		for(ScheduleStatus scheduleStatus : scheduleStatuses){
			viewModels.add(
					dozerMapper.map(scheduleStatus, ScheduleStatusViewModel.class)
					);
		}
		
		log.debug("Schedules found : {}", viewModels);
		
		return viewModels;
	}
	
	/**
	 * @param id
	 * @return
	 */
	@Transactional
	public ScheduleStatusViewModel findOne(Long id){
		
		log.debug("Fetching status by id : {}", id);
		
		ScheduleStatus scheduleStatus = scheduleStatusDao.findOne(id);
		
		ScheduleStatusViewModel viewModel = null;
		
		if(scheduleStatus == null){
			log.warn("No schedule status found with id : {}", id);
		}else{
			viewModel = dozerMapper.map(scheduleStatus, ScheduleStatusViewModel.class);
		}
		
		return viewModel;
	}
	
	/**
	 * @param status
	 * @return
	 */
	@Transactional
	public ScheduleStatusViewModel findByStatus(String status){
		
		log.debug("Fetching schedule status by status : {}", status);
		
		ScheduleStatusViewModel viewModel = null;
		
		try{
			viewModel = dozerMapper.map(
					scheduleStatusDao.findByStatus(status), ScheduleStatusViewModel.class
					);
		}catch(Exception nre){
			log.warn("No schedule status found with status : {}", status);
		}
		
		log.debug("Schedule found : {}", status);
		
		return viewModel;
	}
}