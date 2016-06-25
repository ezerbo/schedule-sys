package com.rj.schedulesys.view.controller;

import java.util.List;

import org.elasticsearch.common.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rj.schedulesys.service.PositionService;
import com.rj.schedulesys.service.PositionTypeService;
import com.rj.schedulesys.view.model.PositionTypeViewModel;
import com.rj.schedulesys.view.model.PositionViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/position-types")
public class PositionTypeController {

	private @Autowired PositionService positionService;
	private @Autowired PositionTypeService positionTypeService;
	
	@RequestMapping(value = "/{idOrName}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findOne(@PathVariable String idOrName){
		
		PositionTypeViewModel viewModel ;
		
		if(StringUtils.isNumeric(idOrName)){
			log.info("Fetching position type with id : {}", idOrName);
			viewModel = positionTypeService.findOne(Long.valueOf(idOrName));
		}else{
			log.info("Fetching position type with name : {}", idOrName);
			viewModel = positionTypeService.findByName(idOrName);
		}
		
		if(viewModel == null){
			log.warn("No position type with id or name : {} found", idOrName);
			return new ResponseEntity<String>("No position type found with id or name : " + idOrName, HttpStatus.NOT_FOUND);
		}
		
		log.info("Position type found : {}", viewModel);
		
		return new ResponseEntity<PositionTypeViewModel>(viewModel, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findAll(){
		
		log.info("Fetching all position types");
		
		List<PositionTypeViewModel> viewModels = positionTypeService.findAll(); ;
		
		if(viewModels.isEmpty()){
			log.warn("No position type");
			return new ResponseEntity<>("No position type" , HttpStatus.NOT_FOUND);
		}
		
		log.info("Position types found : {}", viewModels);
		
		return new ResponseEntity<List<PositionTypeViewModel>>(viewModels, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/positions", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findAllPositions(@PathVariable Long id){
		
		log.info("Fetching all positions for position type with id : {}", id);
		
		if(positionTypeService.findOne(id) == null){
			log.warn("No position type found with id : {}", id);
			return new ResponseEntity<>("No position type found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		List<PositionViewModel> viewModels = positionService.findAllByType(id);
		
		if(viewModels.isEmpty()){
			log.warn("No position found for position type with id : {}", id);
			return new ResponseEntity<String>("No position found for position type with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		log.info("Position found : {}", viewModels);
		
		return new ResponseEntity<>(viewModels, HttpStatus.OK);
	}
	
}