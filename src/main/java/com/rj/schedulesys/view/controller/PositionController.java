package com.rj.schedulesys.view.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rj.schedulesys.service.PositionService;
import com.rj.schedulesys.view.model.PositionViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/positions")
public class PositionController {
	
	private @Autowired PositionService positionService;
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public  @ResponseBody ResponseEntity<?> findPositions(){
		log.info("Finding all positions");
		List<PositionViewModel> viewModels = positionService.findAllPositions();
		
		if(viewModels.isEmpty()){
			log.info("No position was found");
			return new ResponseEntity<String> ("No position found", HttpStatus.NOT_FOUND);
		}
		
		log.info("Positions found : {}", viewModels);
		return new ResponseEntity<List<PositionViewModel>> (viewModels, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/nurses", method = RequestMethod.GET, produces = "application/json")
	public  @ResponseBody ResponseEntity<?> findNursePositions(){
		log.info("Finding nurse positions");//TODO UPDATE THIS
		List<PositionViewModel> viewModels = positionService.findAllPositions("");
		
		if(viewModels.isEmpty()){
			log.info("No position was found");
			return new ResponseEntity<String> ("No nurse position found", HttpStatus.NOT_FOUND);
		}
		
		log.info("Positions found : {}", viewModels);
		return new ResponseEntity<List<PositionViewModel>> (viewModels, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/caregivers", method = RequestMethod.GET, produces = "application/json")
	public  @ResponseBody ResponseEntity<?> findCaregiverPositions(){
		log.info("Finding caregiver positions");//TODO UPDATE THIS
		List<PositionViewModel> viewModels = positionService.findAllPositions("");
		
		if(viewModels.isEmpty()){
			log.info("No position was found");
			return new ResponseEntity<String> ("No caregiver position found", HttpStatus.NOT_FOUND);
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
			log.info("No position found with either id or name : {}", idOrName);
			
			return new ResponseEntity<String>(
					"No position found with either id or name " + idOrName, HttpStatus.NOT_FOUND
					);
		}
		
		return new ResponseEntity<PositionViewModel>(viewModel, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody ResponseEntity<String> createPosition(@RequestBody PositionViewModel viewModel){
		
		log.info("Create position request received : {}", viewModel);
		//TODO UPDATE THIS
		if(!StringUtils.endsWithIgnoreCase(viewModel.getPositionType(), "")
				&& !StringUtils.endsWithIgnoreCase(viewModel.getPositionType(), "")){
			
			log.info("No such position type : {}", viewModel.getPositionType());
			
			return new ResponseEntity<String>("No such position type : " + viewModel.getPositionType(), HttpStatus.BAD_REQUEST);
		}
		
		if(positionService.findByName(viewModel.getPositionName()) != null){
			
			log.info("A position already exist with name : {}", viewModel.getPositionName());
			
			return new ResponseEntity<String>(
					"A position already exist with name : " + viewModel.getPositionName(), HttpStatus.INTERNAL_SERVER_ERROR
					);
		}
		
		viewModel = positionService.createOrUpdatePosition(viewModel);
		
		log.info("Successfully created position : {}", viewModel);
		return new ResponseEntity<String>("Position successfully created", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json")
	public @ResponseBody ResponseEntity<String> updatePosition(@PathVariable Long id, @RequestBody PositionViewModel viewModel){
		
		log.info("Update request received : {} for position with id : {}", viewModel, id);
		
		if(positionService.findById(id) == null){
			log.info("No position found by id : {}", id);
			return new ResponseEntity<String>("No position found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
//		if(positionTypeService.findByType(viewModel.getPositionType()) == null){
//			log.info("No such position type : {}", viewModel.getPositionType());
//			return new ResponseEntity<String>("No such position type " + viewModel.getPositionType(), HttpStatus.NOT_FOUND);
//		}
		
		if(positionService.findByName(viewModel.getPositionName()) != null){
			log.info("A position with name '{}' already exist", viewModel.getPositionName());
			return new ResponseEntity<String>(
					"A position with name : " + viewModel.getPositionName() + " already exist", HttpStatus.INTERNAL_SERVER_ERROR
					);
		}
		
		viewModel.setId(id);//Overriding the id received in the message body
		viewModel = positionService.createOrUpdatePosition(viewModel);
		
		log.info("Position updated successfully : {}", viewModel);
		return new ResponseEntity<String>("Position was successfully updated", HttpStatus.OK);
	}
	
//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
//	public @ResponseBody ResponseEntity<String> deletePosition(@PathVariable Long id){
//		log.info("Delete request received for posistion with id : {}", id);
//		
//		if(positionService.findById(id) == null){
//			log.info("No position found with id : {}", id);
//			return new ResponseEntity<String>("No position found with id : " + id, HttpStatus.NOT_FOUND);
//		}
//		
//		PositionViewModel viewModel = positionService.deletePosition(id);
//		
//		log.info("Successfully deleted position : {}", viewModel);
//		return new ResponseEntity<String>("Position was successfully deleted", HttpStatus.OK);
//	}
	
}