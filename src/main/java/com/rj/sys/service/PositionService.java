package com.rj.sys.service;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.sys.dao.PositionDao;
import com.rj.sys.domain.Position;
import com.rj.sys.view.model.PositionViewModel;

@Slf4j
@Service
public class PositionService {
	
	private @Autowired PositionDao positionDao;
	
	private @Autowired DozerBeanMapper dozerMapper;
	
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
		
		Position position = positionDao.findByName(positionName);
		PositionViewModel viewModel = dozerMapper.map(position, PositionViewModel.class);
		
		return viewModel;
	}
	
	@Transactional
	public PositionViewModel findById(Long id){
		log.info("Finding position by id : {}", id);
		
		Position position = positionDao.findOne(id);
		PositionViewModel viewModel = dozerMapper.map(position, PositionViewModel.class);
		
		return viewModel;
	}

}
