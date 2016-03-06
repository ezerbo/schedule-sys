package com.rj.sys.view.controller;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rj.sys.service.PositionService;
import com.rj.sys.view.model.PositionViewModel;

@Slf4j
@Controller
@RequestMapping("/positions")
public class PositionController {
	
	private final static String allPositionsType = "all";
	
	private final static String nursePositionsType = "nurse";
	
	private final static String caregiverPositionsType = "caregiver";
	
	private @Autowired PositionService positionService;
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public  @ResponseBody ResponseEntity<?> findPositions(@RequestParam(name = "type") String positionType){
		
		List<PositionViewModel> viewModels = new LinkedList<PositionViewModel>();
		
		if(StringUtils.equalsIgnoreCase(positionType, allPositionsType)){
			
			log.info("Finding all positions");
			viewModels = positionService.findAllPositions();
			
		}else if(StringUtils.equalsIgnoreCase(positionType, nursePositionsType) 
				|| StringUtils.equalsIgnoreCase(positionType, caregiverPositionsType)){
			
			log.info("Finding all {} positions", positionType);
			viewModels = positionService.findAllPositions(positionType.toUpperCase());
			
		}else{
			log.error("No such position type : {}", positionType);
			return new ResponseEntity<String> (
					"No such position type : " + positionType, HttpStatus.BAD_REQUEST
					);
		}
		
		if(viewModels.isEmpty()){
			log.info("No position was found");
			return new ResponseEntity<String> ("No position found", HttpStatus.NOT_FOUND);
		}
		
		log.info("Positions found : {}", viewModels);
		return new ResponseEntity<List<PositionViewModel>> (viewModels, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/{idOrName}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findPositionByIdOrName(@PathVariable String idOrName){
		
		PositionViewModel viewModel = null;
		if(StringUtils.isNumeric(idOrName)){
			log.info("Finding position by id : {}", idOrName);
			viewModel = positionService.findById(Long.valueOf(idOrName));
		}else{
			log.info("Finding position by name : {}", idOrName);
			viewModel = positionService.findByName(idOrName);
		}
		
		if(viewModel == null){
			return new ResponseEntity<String>(
					"No position found with either id or name " + idOrName, HttpStatus.NOT_FOUND
					);
		}
		
		return new ResponseEntity<PositionViewModel>(viewModel, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody ResponseEntity<String> createPosition(@RequestBody PositionViewModel viewModel){
		log.info("Create position request received : {}", viewModel);
		return new ResponseEntity<String>("Position successfully created", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json")
	public @ResponseBody ResponseEntity<String> updatePosition(@PathVariable Long id, @RequestBody PositionViewModel viewModel){
		log.info("Update request received : {} for position with id : {}", viewModel, id);
		return new ResponseEntity<String>("Position was successfully updated", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity<String> deletePosition(@PathVariable Long id){
		log.info("Delete request received for posistion with id : {}", id);
		return new ResponseEntity<String>("Position was successfully deleted", HttpStatus.OK);
	}
	
}