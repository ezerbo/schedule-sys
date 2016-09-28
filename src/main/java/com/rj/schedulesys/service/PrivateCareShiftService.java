package com.rj.schedulesys.service;

import java.util.LinkedList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.rj.schedulesys.dao.PrivateCareShiftDao;
import com.rj.schedulesys.domain.PrivateCareShift;
import com.rj.schedulesys.util.ObjectValidator;
import com.rj.schedulesys.view.model.PrivateCareShiftViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PrivateCareShiftService {
	
	private PrivateCareShiftDao privateCareShiftDao;
	private ObjectValidator<PrivateCareShiftViewModel> validator;
	private DozerBeanMapper dozerMapper;
	
	@Autowired
	public PrivateCareShiftService(PrivateCareShiftDao privateCareShiftDao, ObjectValidator<PrivateCareShiftViewModel> validator,
			DozerBeanMapper dozerMapper) {
		this.privateCareShiftDao = privateCareShiftDao;
		this.validator = validator;
		this.dozerMapper = dozerMapper;
	}
	
	
	@Transactional
	public PrivateCareShiftViewModel create(PrivateCareShiftViewModel viewModel){
		log.debug("Creating new shift with", viewModel);
		Assert.notNull(viewModel, "No shift provided");
		if(privateCareShiftDao.findByStartAndEndTime(
				viewModel.getStartTime(), viewModel.getEndTime()) != null){
			log.error("A shift with start time : {} and end time : {} already exists"
					, viewModel.getStartTime(), viewModel.getEndTime());
			throw new RuntimeException("A shift with start time : " + viewModel.getStartTime()
			+ " and end time : " + viewModel.getEndTime() + " already exists");
		}
		viewModel = this.createOrUpdate(viewModel);
		log.debug("Created shift : {}", viewModel);
		return viewModel;
	}
	
	@Transactional
	public PrivateCareShiftViewModel update(PrivateCareShiftViewModel viewModel){
		log.debug("Updating shift : {}", viewModel);
		Assert.notNull(viewModel, "No shif provided");
		Assert.notNull(viewModel.getId(), "No shift id provided");
		PrivateCareShift shift = privateCareShiftDao.findOne(viewModel.getId());
		if(shift == null){
			log.error("No shift found with id : ", viewModel.getId());
			throw new RuntimeException("No shift found with id : " + viewModel.getId());
		}
		if((shift.getStartTime() != viewModel.getEndTime())
				||(shift.getEndTime() != viewModel.getEndTime())){
			log.warn("Either start or end time of both have changed, checking combiantion(startTime, endTime)'s uniqueness ");
			if(privateCareShiftDao.findByStartAndEndTime(
					viewModel.getStartTime(), viewModel.getEndTime()
					) != null){
				log.error("A shift with start time : {} and end time : {} already exists"
						, viewModel.getStartTime(), viewModel.getEndTime());
				throw new RuntimeException("A shift with start time : " + viewModel.getStartTime() 
				+ " and end time : " + viewModel.getEndTime() + " already exists");
			}
		}
		viewModel = this.createOrUpdate(viewModel);
		log.debug("Updated shift : {}", viewModel);
		return viewModel;
	}
	
	@Transactional
	private PrivateCareShiftViewModel createOrUpdate(PrivateCareShiftViewModel viewModel){
		validator.validate(viewModel);
		if(viewModel.getStartTime().equals(viewModel.getEndTime())){
			log.error("Shift start and end time should not be the same");
			throw new RuntimeException("Shifts start and end time must be different");
		}
		PrivateCareShift shift = dozerMapper.map(viewModel, PrivateCareShift.class);
		shift  = privateCareShiftDao.merge(shift);
		return dozerMapper.map(shift, PrivateCareShiftViewModel.class);
	}
	
	/**
	 * @return
	 */
	@Transactional
	public List<PrivateCareShiftViewModel> findAll(){
		log.debug("Fetching all shifts");
		List<PrivateCareShift> shifts = privateCareShiftDao.findAll();
		log.info("Shifts found : {}", shifts);
		List<PrivateCareShiftViewModel> viewModels = new LinkedList<PrivateCareShiftViewModel>();
		shifts.stream()
		.forEach(shift -> {
			viewModels.add(dozerMapper.map(shift, PrivateCareShiftViewModel.class));
		});
		return viewModels;
	}
	
	/**
	 * @param id
	 * @return
	 */
	@Transactional
	public PrivateCareShiftViewModel findOne(Long id){
		log.info("Fetching shift with id : {}", id);
		PrivateCareShift shift = privateCareShiftDao.findOne(id);
		PrivateCareShiftViewModel shiftViewModel = null;
		if(shift != null){
			shiftViewModel = dozerMapper.map(shift, PrivateCareShiftViewModel.class);
		}else{
			log.warn("No shift found with id : ", id);
		}
		return shiftViewModel;
	}
	
	/**
	 * @param id
	 */
	@Transactional
	public void delete(Long id){
		log.debug("Deleting shift with id : ", id);
		PrivateCareShift shift = privateCareShiftDao.findOne(id);
		if(shift == null){
			log.error("No shift found with id : ", id);
			throw new RuntimeException("No shift found with id : " + id);
		}
//		if(!shift.getSchedules().isEmpty()){
//			log.error("Shift with id : {} can not be deleted", id);
//			throw new RuntimeException("Shift with id : " + id + " can not be deleted");
//		}
		privateCareShiftDao.delete(shift);
		log.debug("Shift with id : {} successfully deleted", id);
	}
}
