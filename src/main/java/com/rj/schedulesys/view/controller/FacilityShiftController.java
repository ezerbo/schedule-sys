package com.rj.schedulesys.view.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.rj.schedulesys.service.FacilityShiftService;
import com.rj.schedulesys.view.model.ShiftViewModel;

@Slf4j
@Controller
@RequestMapping("/facility-shifts")
public class FacilityShiftController {
	
	private FacilityShiftService shiftService;
	
	@Autowired
	public FacilityShiftController(FacilityShiftService shiftService) {
		this.shiftService = shiftService;
	}
	
	@RequestMapping(method = RequestMethod.POST , consumes = "application/json")
	public @ResponseBody ResponseEntity<String> create(@RequestBody ShiftViewModel viewModel){
		log.info("Creating new shift : {}", viewModel);
		try{
			viewModel = shiftService.create(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Created shift : {}", viewModel);
		return new ResponseEntity<String>("Shift successfully created", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public @ResponseBody ResponseEntity<String> update(@PathVariable Long id, @RequestBody ShiftViewModel viewModel){
		log.info("Updating shift : {}", viewModel);
		if(shiftService.findOne(id) == null){
			log.error("No shift found with id : {}", id);
			return new ResponseEntity<String>("No shift found with id : " + id, HttpStatus.NOT_FOUND);
		}
		try{
			viewModel = shiftService.update(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
		}
		log.info("Updated shift : {}", viewModel);
		return new ResponseEntity<String>("Shift successfully updated", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable Long id){
		log.info("Deleting shift with id : {}", id);
		if(shiftService.findOne(id) == null){
			log.error("No shift found with id : {}", id);
			return new ResponseEntity<String>("No shift found with id : " + id, HttpStatus.NOT_FOUND);
		}
		try {
			shiftService.delete(id);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Shift with id : {} successfully deleted", id);
		
		return new ResponseEntity<String>("Shift successfully deleted", HttpStatus.OK);
	}
	 
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findAll(){
		
		log.info("Fetching all shifts");
		
		List<ShiftViewModel> shifts = shiftService.findAll();
		
		if(shifts.isEmpty()){
			return new ResponseEntity<String>("No shift found", HttpStatus.NOT_FOUND);
		}
		
		log.info("Shifts found : {}", shifts);
		
		return new ResponseEntity<List<ShiftViewModel>>(shifts, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{idOrName}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findOne(@PathVariable String idOrName){
		
		ShiftViewModel viewModel = null;
		
		if(StringUtils.isNumeric(idOrName)){
			log.info("Finding shift with id : {}", idOrName);
			viewModel = shiftService.findOne(Long.valueOf(idOrName));
		}else{
			log.info("Finding shift with name : {}", idOrName);
			viewModel = shiftService.findByName(idOrName);
		}
		
		if(viewModel == null){
			log.error("No shift found with name or id : {}", idOrName);
			return new ResponseEntity<String>(
					"No shift found with either id or name : " + idOrName, HttpStatus.NOT_FOUND
					);
		}
		
		return new ResponseEntity<ShiftViewModel>(viewModel, HttpStatus.OK);
	}
}