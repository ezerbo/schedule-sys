package com.rj.sys.view.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rj.sys.service.PositionTypeService;
import com.rj.sys.view.model.PositionTypeViewModel;

@Slf4j
@Controller
@RequestMapping("/position-types")
public class PositionTypeController {
	
	private @Autowired PositionTypeService positionTypeService;
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getAllPositionTypes(){
		log.info("Finding all position types ");
		List<PositionTypeViewModel> viewModels = positionTypeService.findAll();
		
		if(viewModels.isEmpty()){
			log.info("No position type found ");
			return new ResponseEntity<String>("No position type found", HttpStatus.NOT_FOUND);
		}
		
		log.info("Position types found : {}", viewModels);
		
		return new ResponseEntity<List<PositionTypeViewModel>>(viewModels, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{idOrType}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getPositionTypeByIdOrType(@PathVariable String idOrType){
		PositionTypeViewModel viewModel = null;
		if(StringUtils.isNumeric(idOrType)){
			log.info("Finding position type by id : {}", idOrType);
			viewModel = positionTypeService.findById(Long.valueOf(idOrType));
		}else{
			log.info("Finding position type by type : {}", idOrType);
			viewModel = positionTypeService.findByType(idOrType);
		}
		
		if(viewModel == null){
			return new ResponseEntity<String>(
					"No position type found with either id or type " + idOrType, HttpStatus.NOT_FOUND
					);
		}
		return new ResponseEntity<PositionTypeViewModel>(viewModel, HttpStatus.OK);
	}
	
}