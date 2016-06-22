package com.rj.schedulesys.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.rj.schedulesys.dao.SchedulePostStatusDao;
import com.rj.schedulesys.domain.SchedulePostStatus;
import com.rj.schedulesys.util.ObjectValidator;
import com.rj.schedulesys.view.model.SchedulePostStatusViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SchedulePostStatusService {
	
	private @Autowired SchedulePostStatusDao schedulePostStatusDao;
	
	private @Autowired ObjectValidator<SchedulePostStatusViewModel> validator;
	
	private @Autowired DozerBeanMapper dozerMapper;
	
	
	/**
	 * @param viewModel
	 * @return
	 */
	@Transactional
	public SchedulePostStatusViewModel create(SchedulePostStatusViewModel viewModel){
		
		log.debug("Creating new schedule post status : {}", viewModel);
		
		Assert.notNull(viewModel, "No schedule post status provided");
		
		SchedulePostStatus schedulePostStatus = schedulePostStatusDao.findByStatus(viewModel.getStatus());
		
		if(schedulePostStatus != null){
			log.error("A schedule post status with status : {} already exists", viewModel.getStatus());
			throw new RuntimeException("A schedule post status with status : " + viewModel.getStatus() + " already exists");
		}
		
		viewModel = this.createOrUpdate(viewModel);
		
		log.debug("Created schedule post status : {}", viewModel);
		
		return viewModel;
		
	}
	
	/**
	 * @param viewModel
	 * @return
	 */
	@Transactional
	public SchedulePostStatusViewModel update(SchedulePostStatusViewModel viewModel){
		
		log.debug("Updating schedule post status : {}", viewModel);
		
		Assert.notNull(viewModel, "No schedule post status provided");
		
		SchedulePostStatus schedulePostStatus = schedulePostStatusDao.findOne(viewModel.getId());
		
		if(schedulePostStatus == null){
			log.error("No schedule post status found with id : {}", viewModel.getId());
			throw new RuntimeException("No schedule post status found with id : " + viewModel.getId());
		}
		
		if(!StringUtils.equalsIgnoreCase(schedulePostStatus.getStatus(), viewModel.getStatus())){
			log.warn("Schedule post status updated, checking its uniqueness");
			if(schedulePostStatusDao.findByStatus(viewModel.getStatus()) != null){
				log.error("A schedule post status with status : {} already exists", viewModel.getStatus());
				throw new RuntimeException("A schedule post status with status : " + viewModel.getStatus() + " already exists");
			}
		}
		
		viewModel = this.createOrUpdate(viewModel);
		
		log.debug("Updated schedule post  status : {}", viewModel);
		
		return viewModel;
	}
	
	/**
	 * @param viewModel
	 * @return
	 */
	@Transactional
	private SchedulePostStatusViewModel createOrUpdate(SchedulePostStatusViewModel viewModel){
		
		validator.validate(viewModel);
		
		SchedulePostStatus schedulePostStatus = dozerMapper.map(viewModel, SchedulePostStatus.class);
		schedulePostStatus.setStatus(StringUtils.upperCase(schedulePostStatus.getStatus()));
		schedulePostStatus = schedulePostStatusDao.merge(schedulePostStatus);
		
		return dozerMapper.map(schedulePostStatus, SchedulePostStatusViewModel.class);
	}
	
	/**
	 * @param id
	 */
	@Transactional
	public void delete(Long id){
		
		log.debug("Deleting schedule post status with id : {}", id);
		
		SchedulePostStatus scheduleStatus = schedulePostStatusDao.findOne(id);
		
		if(scheduleStatus == null){
			log.error("No schedule post status found with id : {}", id);
			throw new RuntimeException("No schedule post status found with id : " + id);
		}
		
		
		if(!scheduleStatus.getSchedules().isEmpty()){
			log.error("Schedule post status with id : {} can not be deleted", id);
			throw new RuntimeException("Schedule post status with id : " + id + " can not be deleted");
		}
		
		schedulePostStatusDao.delete(scheduleStatus);
		
		log.debug("Schedule post status with id : {} successfully deleted");
	}
	
	/**
	 * @return
	 */
	@Transactional
	public List<SchedulePostStatusViewModel> findAll(){
		
		log.debug("Fetching all schedule post statuses");
		
		List<SchedulePostStatus> schedulePostStatuses = schedulePostStatusDao.findAll();
		
		List<SchedulePostStatusViewModel> viewModels = new LinkedList<SchedulePostStatusViewModel>();
		
		for(SchedulePostStatus schedulePostStatus : schedulePostStatuses){
			viewModels.add(
					dozerMapper.map(schedulePostStatus, SchedulePostStatusViewModel.class)
					);
		}
		
		log.debug("Schedule post statuses found : {}", viewModels);
		
		return viewModels;
	}
	
	/**
	 * @param id
	 * @return
	 */
	@Transactional
	public SchedulePostStatusViewModel findOne(Long id){
		
		log.debug("Fetching schedule post status with id : {}", id);
		
		SchedulePostStatus schedulePostStatus = schedulePostStatusDao.findOne(id);
		SchedulePostStatusViewModel viewModel = null;
		
		if(schedulePostStatus != null){
			viewModel = dozerMapper.map(
					schedulePostStatus, SchedulePostStatusViewModel.class
					);
		}
		
		log.debug("Schedule post status found : {}", viewModel);
		
		return viewModel;
	}
	
	/**
	 * @param status
	 * @return
	 */
	@Transactional
	public SchedulePostStatusViewModel findByStatus(String status){
		
		log.info("Fetching schedule post status with status : {}", status);
		
		SchedulePostStatus schedulePostStatus = schedulePostStatusDao.findByStatus(status);
		
		SchedulePostStatusViewModel viewModel = null;
		
		if(schedulePostStatus != null){
			viewModel = dozerMapper.map(
					schedulePostStatus, SchedulePostStatusViewModel.class
					);
		}
		
		log.debug("Schedule post status : {}", status);
		
		return viewModel;
	}
}