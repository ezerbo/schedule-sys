package com.rj.schedulesys.view.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rj.schedulesys.service.PrivateCareShiftService;
import com.rj.schedulesys.view.model.PrivateCareShiftViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/private-care-shifts")
public class PrivateCareShiftController {

	private PrivateCareShiftService privateCareShiftService;
	
	@Autowired
	public PrivateCareShiftController(PrivateCareShiftService privateCareShiftService) {
		this.privateCareShiftService = privateCareShiftService;
	}
	
	@RequestMapping(method = RequestMethod.POST , consumes = "application/json")
	public @ResponseBody ResponseEntity<String> create(@RequestBody PrivateCareShiftViewModel viewModel){
		log.info("Creating new shift : {}", viewModel);
		try{
			viewModel = privateCareShiftService.create(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Created shift : {}", viewModel);
		return new ResponseEntity<String>("Shift successfully created", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public @ResponseBody ResponseEntity<String> update(@PathVariable Long id, @RequestBody PrivateCareShiftViewModel viewModel){
		log.info("Updating shift : {}", viewModel);
		if(privateCareShiftService.findOne(id) == null){
			log.error("No shift found with id : {}", id);
			return new ResponseEntity<String>("No shift found with id : " + id, HttpStatus.NOT_FOUND);
		}
		try{
			viewModel = privateCareShiftService.update(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
		}
		log.info("Updated shift : {}", viewModel);
		return new ResponseEntity<String>("Shift successfully updated", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable Long id){
		log.info("Deleting shift with id : {}", id);
		if(privateCareShiftService.findOne(id) == null){
			log.error("No shift found with id : {}", id);
			return new ResponseEntity<String>("No shift found with id : " + id, HttpStatus.NOT_FOUND);
		}
		try {
			privateCareShiftService.delete(id);
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
		List<PrivateCareShiftViewModel> shifts = privateCareShiftService.findAll();
		if(shifts.isEmpty()){
			return new ResponseEntity<String>("No shift found", HttpStatus.NOT_FOUND);
		}
		log.info("Shifts found : {}", shifts);
		return new ResponseEntity<>(shifts, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findOne(@PathVariable String id){
		PrivateCareShiftViewModel viewModel = privateCareShiftService.findOne(Long.valueOf(id));
		if(viewModel == null){
			log.error("No shift found with id : {}", id);
			return new ResponseEntity<String>(
					"No shift found with id : " + id, HttpStatus.NOT_FOUND);
			}
		
		return new ResponseEntity<>(viewModel, HttpStatus.OK);
	}
	
}