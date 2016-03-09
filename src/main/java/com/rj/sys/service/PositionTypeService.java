package com.rj.sys.service;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.NoResultException;

import lombok.extern.slf4j.Slf4j;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.sys.dao.PositionTypeDao;
import com.rj.sys.domain.PositionType;
import com.rj.sys.view.model.PositionTypeViewModel;

@Slf4j
@Service
public class PositionTypeService {

	private @Autowired PositionTypeDao positionTypeDao;
	
	private @Autowired DozerBeanMapper dozerMapper;
	
	@Transactional
	public List<PositionTypeViewModel> findAll(){
		
		log.info("Finding all position types");
		
		List<PositionType> positionTypes = positionTypeDao.findAll();
		
		List<PositionTypeViewModel> viewModels = new LinkedList<PositionTypeViewModel>();
		
		for(PositionType positionType : positionTypes){
			viewModels.add(dozerMapper.map(positionType, PositionTypeViewModel.class));
		}
		
		return viewModels;
	}
	
	@Transactional
	public PositionTypeViewModel findById(Long id ){
		log.info("Finding position type by id : {}", id);
		PositionType positionType = positionTypeDao.findOne(id);
		
		PositionTypeViewModel viewModel = null;
		
		if(positionType != null){
			viewModel = dozerMapper.map(positionType, PositionTypeViewModel.class);
		}
		
		return viewModel;
	}
	
	@Transactional
	public PositionTypeViewModel findByType(String type){
		log.info("Finding position type by type : {}", type);
		PositionTypeViewModel viewModel = null;
		try{
			viewModel = dozerMapper.map(
					positionTypeDao.findByType(type), PositionTypeViewModel.class
					);
		}catch(NoResultException nre){
			log.info("No position type found with type : {}", type);
		}
		
		return viewModel;
	}
}
