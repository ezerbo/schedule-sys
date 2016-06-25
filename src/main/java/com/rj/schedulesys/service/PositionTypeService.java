package com.rj.schedulesys.service;

import java.util.LinkedList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rj.schedulesys.dao.PositionTypeDao;
import com.rj.schedulesys.domain.PositionType;
import com.rj.schedulesys.view.model.PositionTypeViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PositionTypeService {

	
	private @Autowired PositionTypeDao positionTypeDao;
	
	private @Autowired DozerBeanMapper dozerMapper;
	
	/**
	 * @param id
	 * @return
	 */
	public PositionTypeViewModel findOne(Long id){
		
		log.debug("Fetching position type with id : {}", id);
		
		PositionType positionType = positionTypeDao.findOne(id);
		
		PositionTypeViewModel viewModel = null;
		
		if(positionType == null){
			log.warn("No position type found with id : {}", id);
		}else{
			viewModel = dozerMapper.map(positionType, PositionTypeViewModel.class);
		}
		
		log.debug("Position found with with id : {}", id);
		
		return viewModel;
	}
	
	/**
	 * @param name
	 * @return
	 */
	public PositionTypeViewModel findByName(String name){
		
		log.debug("Fetching position type with name : {}", name);
		
		PositionTypeViewModel viewModel = null;
		
		PositionType positionType = positionTypeDao.findByName(name);
		
		if(positionType == null){
			log.debug("No psition type found with name : {}", name);
		}else{
			viewModel = dozerMapper.map(positionType, PositionTypeViewModel.class);
		}
		
		log.debug("Position type found : {}", viewModel);
		
		return viewModel;
	}
	
	public List<PositionTypeViewModel> findAll(){
		
		log.debug("Fetching all position types");
		
		List<PositionType> positionTypes = positionTypeDao.findAll();
		List<PositionTypeViewModel> viewModels = new LinkedList<>();
		
		for(PositionType positionType : positionTypes){
			viewModels.add(dozerMapper.map(positionType, PositionTypeViewModel.class));
		}
		
		log.debug("Position types found : {}", viewModels);
		
		return viewModels;
	}
}