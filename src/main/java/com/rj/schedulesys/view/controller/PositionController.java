package com.rj.schedulesys.view.controller;

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
	
	@RequestMapping(value = "/{idOrName}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findOne(@PathVariable String idOrName){
		
		PositionViewModel viewModel = null;
		
		if(StringUtils.isNumeric(idOrName)){
			log.info("Fetching position by id : {}", idOrName);
			viewModel = positionService.findOne(Long.valueOf(idOrName));
		}else{
			log.info("Fetching position by name : {}", idOrName);
			viewModel = positionService.findByName(idOrName);
		}
		
		if(viewModel == null){
			log.info("No position found with either id or name : {}", idOrName);
			
			return new ResponseEntity<String>(
					"No position found with either id or name : " + idOrName, HttpStatus.NOT_FOUND
					);
		}
		
		return new ResponseEntity<PositionViewModel>(viewModel, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody ResponseEntity<String> create(@RequestBody PositionViewModel viewModel){
		
		log.info("Create position request received : {}", viewModel);
		
		viewModel.setId(null);
		
		try{
			viewModel =	positionService.create(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Successfully created position : {}", viewModel);
		
		return new ResponseEntity<String>("Position successfully created", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json")
	public @ResponseBody ResponseEntity<String> update(@PathVariable Long id, @RequestBody PositionViewModel viewModel){
		
		log.info("Update request received : {} for position with id : {}", viewModel, id);
		
		if(positionService.findOne(id) == null){
			log.info("No position found by id : {}", id);
			return new ResponseEntity<String>("No position found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		viewModel.setId(id);//Overriding the id received in the message body
		
		try{
			viewModel = positionService.update(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Position updated successfully : {}", viewModel);
		
		return new ResponseEntity<String>("Position successfully updated", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity<String> delete(@PathVariable Long id){
		
		log.info("Deleting posistion with id : {}", id);
		
		if(positionService.findOne(id) == null){
			log.info("No position found with id : {}", id);
			return new ResponseEntity<String>("No position found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		try{
			positionService.delete(id);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Successfully deleted position with id : {}", id);
		
		return new ResponseEntity<String>("Position successfully deleted", HttpStatus.OK);
	}
	
}