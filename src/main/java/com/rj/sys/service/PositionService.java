package com.rj.sys.service;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.NoResultException;

import lombok.extern.slf4j.Slf4j;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.sys.dao.PositionDao;
import com.rj.sys.dao.PositionTypeDao;
import com.rj.sys.domain.Position;
import com.rj.sys.domain.PositionType;
import com.rj.sys.view.model.PositionViewModel;

@Slf4j
@Service
public class PositionService {
	
	private @Autowired PositionDao positionDao;
	private @Autowired PositionTypeDao positionTypeDao;
	
	private @Autowired DozerBeanMapper dozerMapper;
	
	
	@Transactional
	public PositionViewModel createOrUpdatePosition(PositionViewModel viewModel){
		
		log.info("Creating a position : {}", viewModel);
		
		PositionType positionType = positionTypeDao.findByType(viewModel.getPositionType());
		Position position = dozerMapper.map(viewModel, Position.class);
		position.setIsDeleted(false);
		position.setPositionType(positionType);
		position = positionDao.merge(position);
		
		return dozerMapper.map(position, PositionViewModel.class);
	}
	
	@Transactional
	public PositionViewModel deletePosition(Long id){
		log.info("Deleteing position with id : {}", id);
		Position position = positionDao.delete(id);
		return dozerMapper.map(position, PositionViewModel.class);
	}
	
	@Transactional
	public List<PositionViewModel> findAllPositions(){
		log.info("Finding all positions");
		
		List<Position> positions = positionDao.findAll();
		
		List<PositionViewModel> viewModels = new LinkedList<PositionViewModel>();
		
		for(Position position : positions){
			viewModels.add(dozerMapper.map(position, PositionViewModel.class));
		}
		
		return viewModels;
	}
	
	@Transactional
	public List<PositionViewModel> findAllPositions(String positionType){
		
		log.info("Finding all nurses' positions");
		
		List<Position> nursePositions = positionDao.findAllByType(positionType);
		
		List<PositionViewModel> viewModels = new LinkedList<PositionViewModel>();
		
		for(Position position : nursePositions){
			viewModels.add(dozerMapper.map(position, PositionViewModel.class));
		}
		
		return viewModels;
	}
	
	@Transactional
	public PositionViewModel findByName(String positionName){
		
		log.info("Finding position by name : {}", positionName);
		PositionViewModel viewModel = null;
		try{
		viewModel = dozerMapper.map(
				positionDao.findByName(positionName), PositionViewModel.class
				);
		}catch(NoResultException nre){
			log.info("No position found with name : {}", positionName);
		}
		
		return viewModel;
	}
	
	@Transactional
	public PositionViewModel findById(Long id){
		log.info("Finding position by id : {}", id);
		PositionViewModel viewModel = null;
		try{
			viewModel = dozerMapper.map(
					positionDao.findOne(id), PositionViewModel.class
					);
		}catch(NoResultException nre){
			log.info("No position found by id : {}", id);
		}
		return viewModel;
	}

}