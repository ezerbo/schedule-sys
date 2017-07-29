package com.ss.schedulesys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.Position;
import com.ss.schedulesys.repository.PositionRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PositionService {

	private PositionRepository positionRepository;
	
	@Autowired
	public PositionService(PositionRepository positionRepository) {
		this.positionRepository = positionRepository;
	}
	
	@Transactional(readOnly = true)
	public List<Position> getAll(){
		log.debug("Fetching all positions");
		List<Position> positions = positionRepository.findAll();
		return positions;
	}
}
