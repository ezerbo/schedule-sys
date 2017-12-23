package com.ss.schedulesys.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.Position;
import com.ss.schedulesys.repository.PositionRepository;
import com.ss.schedulesys.service.errors.ScheduleSysException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class PositionService {

	private PositionRepository positionRepository;
	
	@Autowired
	public PositionService(PositionRepository positionRepository) {
		this.positionRepository = positionRepository;
	}
	
	public Position save(Position position) {
		log.debug("Creating or Updating position : {}", position);
		Position existingPosition = positionRepository.findByName(position.getName());
		if(existingPosition != null && (position.getId() != existingPosition.getId())) {
			log.error("Position name '{}' already in use", position.getName());
			throw new ScheduleSysException(String.format("Position name '%s' already in use", position.getName()));
		}
		position = positionRepository.save(position);
		return position;
	}
	
	
	@Transactional(readOnly = true)
	public List<Position> getAll(){
		log.debug("Fetching all positions");
		List<Position> positions = positionRepository.findAll();
		return positions;
	}
	
	public void delete(Long id) {
		log.debug("Deleting position with Id : {}", id);
		Position position = Optional.ofNullable(positionRepository.getOne(id))
				.orElseThrow(() -> new ScheduleSysException(String.format("No position found with Id : '%s'", id)));
		if(position.getEmployees().isEmpty()){
			positionRepository.delete(id);
		} else {
			throw new ScheduleSysException(String.format("Position '%s' is in use",position.getName()));
		}
		
	}
}
