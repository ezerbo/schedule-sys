package com.rj.schedulesys.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.rj.schedulesys.dao.PositionDao;
import com.rj.schedulesys.dao.PositionTypeDao;
import com.rj.schedulesys.domain.Position;
import com.rj.schedulesys.domain.PositionType;
import com.rj.schedulesys.util.ObjectValidator;
import com.rj.schedulesys.view.model.PositionViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PositionService {
	
	private @Autowired PositionDao positionDao;
	private @Autowired PositionTypeDao positionTypeDao;
	
	private @Autowired ObjectValidator<PositionViewModel> validator;
	
	private @Autowired DozerBeanMapper dozerMapper;
	
	
	/**
	 * @param viewModel
	 * @return
	 */
	@Transactional
	public PositionViewModel create(PositionViewModel viewModel){
		
		Assert.notNull(viewModel, "No position provided");
		
		log.debug("Creating position : {}", viewModel);
		
		if(positionTypeDao.findByName(viewModel.getPositionTypeName()) == null){
			log.error("No position type found with name : {}", viewModel.getPositionTypeName());
			throw new RuntimeException("No position type found with name : " + viewModel.getPositionTypeName());
		}
		
		if(positionDao.findByNameAndType(viewModel.getName(), viewModel.getPositionTypeName()) != null){
			log.error("A position with name : {} already exists with for position type with name : {}"
					, viewModel.getName(), viewModel.getPositionTypeName());
			throw new RuntimeException("A position with name : " + viewModel.getName() 
			+ " already exists for position type with name : " + viewModel.getPositionTypeName());
		}
		
		viewModel = this.createOrUpdate(viewModel);
		
		log.debug("Position created : {}", viewModel);
		
		return viewModel;
	}
	
	/**
	 * @param viewModel
	 * @return
	 */
	@Transactional
	public PositionViewModel update(PositionViewModel viewModel){
		
		Assert.notNull(viewModel, "No position provided");
		
		log.debug("Updating position : {}", viewModel);
		
		Position position = positionDao.findOne(viewModel.getId());
		
		if(position == null){
			log.error("No position found with id : {}", viewModel.getId());
			throw new RuntimeException("No position found with id : " + viewModel.getId());
		}
		
		if(positionTypeDao.findByName(viewModel.getPositionTypeName()) == null){
			log.error("No position type found with name : {}", viewModel.getPositionTypeName());
			throw new RuntimeException("No position type found with name : " + viewModel.getPositionTypeName());
		}
		
		if(!StringUtils.equalsIgnoreCase(position.getName(), viewModel.getName())){
			
			log.warn("Position name updated, checking its uniqueness");
			
			if(positionDao.findByNameAndType(viewModel.getName(), viewModel.getPositionTypeName()) != null){
				
				log.error("A position with name : {} already exists with for position type with name : {}"
						, viewModel.getName(), viewModel.getPositionTypeName());
				
				throw new RuntimeException("A position with name : " + viewModel.getName() 
				+ " already exists for position type with name : " + viewModel.getPositionTypeName());
			}
		}
		
		viewModel = this.createOrUpdate(viewModel);
		
		log.debug("Position updated : {}", viewModel);
		
		return viewModel;
	}
	
	/**
	 * @param viewModel
	 * @return
	 */
	@Transactional
	public PositionViewModel createOrUpdate(PositionViewModel viewModel){
		
		validator.validate(viewModel);
		
		Position position = dozerMapper.map(viewModel, Position.class);
		
		PositionType positionType = positionTypeDao.findByName(viewModel.getPositionTypeName());
		position.setPositionType(positionType);
		
		position = positionDao.merge(position);
		
		return dozerMapper.map(position, PositionViewModel.class);
	}
	
	/**
	 * @param id
	 */
	@Transactional
	public void delete(Long id){
		
		log.info("Deleteing position with id : {}", id);
		
		Position position = positionDao.findOne(id);
		
		if(position == null){
			log.error("No position found with id : {}", id);
			throw new RuntimeException("No position found with id : " + id);
		}
		
		if(!position.getEmployees().isEmpty()){
			log.error("Position with id : {} cannot be deleted");
			throw new RuntimeException("Position with id : " + id + " can not be deleted");
		}
		
		positionDao.delete(position);
		
		log.debug("Position with id : {} successfully deleted");
		
	}
	
	/**
	 * @param positionType
	 * @return
	 */
	@Transactional
	public List<PositionViewModel> findAllByType(Long positionTypeId){
		
		log.info("Fetching all posistions with id : {}", positionTypeId);
		
		PositionType positionType = positionTypeDao.findOne(positionTypeId);
		
		if(positionType == null){
			log.error("No position type found with id : {}", positionTypeId);
			throw new RuntimeException("No position type found with id : " + positionTypeId);
		}
		
		List<Position> nursePositions = positionDao.findAllByType(positionTypeId);
		
		List<PositionViewModel> viewModels = new LinkedList<PositionViewModel>();
		
		for(Position position : nursePositions){
			PositionViewModel viewModel = dozerMapper.map(position, PositionViewModel.class);
			viewModel.setPositionTypeName(positionType.getName());
			viewModels.add(viewModel);
		}
		
		return viewModels;
	}
	
	/**
	 * @param positionName
	 * @return
	 */
	@Transactional
	public PositionViewModel findByName(String positionName){
		
		log.info("Finding position by name : {}", positionName);
		
		PositionViewModel viewModel = null;
		Position position = positionDao.findByName(positionName);
		
		if(position == null){
			log.warn("No position found with name : {}", positionName);
		}else{
			viewModel = dozerMapper.map(position, PositionViewModel.class);
			viewModel.setPositionTypeName(position.getName());
		}
		
		log.debug("Position found : {}", viewModel);
		
		return viewModel;
	}
	
	/**
	 * @param id
	 * @return
	 */
	@Transactional
	public PositionViewModel findOne(Long id){
		
		log.debug("Finding position with id : {}", id);
		
		PositionViewModel viewModel = null;
		
		Position position = positionDao.findOne(id);
		
		if(position == null){
			log.warn("No position found with id : {}", id);
		}else{
			viewModel = dozerMapper.map(position, PositionViewModel.class);
			viewModel.setPositionTypeName(position.getPositionType().getName());
		}
		
		log.debug("Position found : {}", viewModel);
		
		return viewModel;
	}

}