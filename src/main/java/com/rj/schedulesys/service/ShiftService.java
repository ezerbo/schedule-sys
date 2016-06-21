package com.rj.schedulesys.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.rj.schedulesys.dao.ShiftDao;
import com.rj.schedulesys.domain.Shift;
import com.rj.schedulesys.util.ObjectValidator;
import com.rj.schedulesys.util.ServiceHelper;
import com.rj.schedulesys.view.model.ShiftViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShiftService {
	
	private @Autowired ShiftDao shiftDao;
	
	private @Autowired ObjectValidator<ShiftViewModel> validator;
	
	private @Autowired DozerBeanMapper dozerMapper;

	
	@Transactional
	public ShiftViewModel create(ShiftViewModel viewModel){
		
		log.debug("Creating new shift with", viewModel);
		
		Assert.notNull(viewModel, "No shift provided");
		
		Shift shift = shiftDao.findByName(viewModel.getName());
		
		if(shift != null){
			log.debug("A shift with name : {} already exists", viewModel.getName());
			throw new RuntimeException("A shift with name : " + viewModel.getName() + " alredy exists");
		}
		
		//TODO Move to a validation method
		if(shiftDao.findByStartAndEndTime(
				viewModel.getStartTime(), viewModel.getEndTime()
				) != null){
			log.error("A shift with start time : {} and end time : {} already exists"
					, viewModel.getStartTime(), viewModel.getEndTime());
			throw new RuntimeException("A shift with start time : " + ServiceHelper.formatLocalTime(viewModel.getStartTime()) 
			+ " and end time : " + ServiceHelper.formatLocalTime(viewModel.getEndTime()) + " already exists");
		}
		
		viewModel = this.createOrUpdate(viewModel);
		
		log.debug("Created shift : {}", viewModel);
		
		return viewModel;
		
	}
	
	@Transactional
	public ShiftViewModel update(ShiftViewModel viewModel){
		
		log.debug("Updating shift : {}", viewModel);
		
		Assert.notNull(viewModel, "No shif provided");
		Assert.notNull(viewModel.getId(), "No shift id provided");
		
		Shift shift = shiftDao.findOne(viewModel.getId());
		
		if(shift == null){
			log.error("No shift found with id : ", viewModel.getId());
			throw new RuntimeException("No shift found with id : " + viewModel.getId());
		}
		
		if(!StringUtils.equalsIgnoreCase(shift.getName(), viewModel.getName())){
			log.warn("Shift name updated checking its uniqueness");
			if(shiftDao.findByName(viewModel.getName()) != null){
				log.error("A shift with name : {} already exists", viewModel.getName());
				throw new RuntimeException("A shift with name : " + viewModel.getName() + " already exists");
			}
		}
		
		if((shift.getStartTime() != viewModel.getEndTime())
				||(shift.getEndTime() != viewModel.getEndTime())){
			log.warn("Either start or end time of both have changed, checking combiantion(startTime, endTime)'s uniqueness ");
			if(shiftDao.findByStartAndEndTime(
					viewModel.getStartTime(), viewModel.getEndTime()
					) != null){
				log.error("A shift with start time : {} and end time : {} already exists"
						, ServiceHelper.formatLocalTime(viewModel.getStartTime()), ServiceHelper.formatLocalTime(viewModel.getEndTime()));
				throw new RuntimeException("A shift with start time : " + viewModel.getStartTime() 
				+ " and end time : " + viewModel.getEndTime() + " already exists");
			}
		}
		
		viewModel = this.createOrUpdate(viewModel);
		
		log.debug("Updated shift : {}", viewModel);
		
		return viewModel;
	}
	
	@Transactional
	private ShiftViewModel createOrUpdate(ShiftViewModel viewModel){
		
		validator.validate(viewModel);
		
		if(viewModel.getStartTime().isEqual(viewModel.getEndTime())){
			log.error("Shift start and end time should not be the same");
			throw new RuntimeException("Shifts start and end time must be different");
		}
		
		Shift shift = dozerMapper.map(viewModel, Shift.class);
		shift.setName(StringUtils.upperCase(shift.getName()));
		shift  = shiftDao.merge(shift);
		
		return dozerMapper.map(shift, ShiftViewModel.class);
	}
	
	/**
	 * @return
	 */
	@Transactional
	public List<ShiftViewModel> findAll(){
		
		log.debug("Fetching all shifts");
		
		List<Shift> shifts = shiftDao.findAll();
		
		log.info("Shifts found : {}", shifts);
		List<ShiftViewModel> viewModels = new LinkedList<ShiftViewModel>();
		
		for(Shift shift : shifts){
			viewModels.add(dozerMapper.map(shift, ShiftViewModel.class));	
		}
		
		return viewModels;
	}
	
	/**
	 * @param name
	 * @return
	 */
	@Transactional
	public ShiftViewModel findByName(String name){
		
		log.info("Fetching shift with name : {}", name);
		
		ShiftViewModel viewModel = null;
		
		try{
			viewModel = dozerMapper.map(
					shiftDao.findByName(name), ShiftViewModel.class
					) ;
		}catch(Exception nre){
			log.debug("No shift found with name : {}", name);
		}
		
		return viewModel;
	}
	
	/**
	 * @param id
	 * @return
	 */
	@Transactional
	public ShiftViewModel findOne(Long id){
		
		log.info("Fetching shift with id : {}", id);
		
		Shift shift = shiftDao.findOne(id);
		ShiftViewModel shiftViewModel = null;
		
		if(shift != null){
			shiftViewModel = dozerMapper.map(shift, ShiftViewModel.class);
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
		
		Shift shift = shiftDao.findOne(id);
		
		if(shift == null){
			log.error("No shift found with id : ", id);
			throw new RuntimeException("No shift found with id : " + id);
		}
		
		if(!shift.getSchedules().isEmpty()){
			log.error("Shift with id : {} can not be deleted", id);
			throw new RuntimeException("Shift with id : " + id + " can not be deleted");
		}
		
		shiftDao.delete(shift);
		
		log.debug("Shift with id : {} successfully deleted", id);
	}
}
