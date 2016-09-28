package com.rj.schedulesys.view.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rj.schedulesys.service.AuthenticationService;
import com.rj.schedulesys.service.PrivateCareScheduleService;
import com.rj.schedulesys.view.model.CreatePrivateCareScheduleViewModel;
import com.rj.schedulesys.view.model.GetPrivateCareScheduleViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/private-care-schedules")
public class PrivateCareScheduleController {

	private PrivateCareScheduleService privateCareScheduleService;
	private AuthenticationService authenticationService;
	
	@Autowired
	public PrivateCareScheduleController(PrivateCareScheduleService privateCareScheduleService, AuthenticationService authenticationService) {
		this.privateCareScheduleService = privateCareScheduleService;
		this.authenticationService = authenticationService;
	}
	
	@RequestMapping(value ="/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getSchedule(@PathVariable Long id){
		log.info("Fetching schedule with id : {}", id);
		GetPrivateCareScheduleViewModel viewModel = privateCareScheduleService.findOne(id);
		if(viewModel == null){
			log.warn("No schedule found with id : {}", id);
			return new ResponseEntity<>("No schedule found with id : " + id, HttpStatus.NOT_FOUND);
		}
		log.info("Schedule found : {}", viewModel);
		return new ResponseEntity<>(viewModel, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<?> saveSchedule(@RequestBody CreatePrivateCareScheduleViewModel viewModel){
		log.info("Saving schedule : {} ", viewModel);
		viewModel.setId(null);
		try{
			viewModel = privateCareScheduleService.create(
					viewModel, authenticationService.getAuthenticatedUser().getId());
		}catch(Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Schedule saved : {}", viewModel);
		return new ResponseEntity<String>("Schedule successfully created", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CreatePrivateCareScheduleViewModel viewModel){
		log.info("Updating schedule with id : {} : {}", id, viewModel);
		GetPrivateCareScheduleViewModel vm = privateCareScheduleService.findOne(id);
		if(vm == null){
			log.info("No schedule found with id : {}", id);
			return new ResponseEntity<>("No schedule found with id : " + id, HttpStatus.NOT_FOUND);
		}
		viewModel.setId(id);
		try{
			privateCareScheduleService.update(viewModel, authenticationService.getAuthenticatedUser().getId());
		}catch(Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Updated schedule : {}", viewModel);
		return ResponseEntity.ok("Schedule updated successfully");
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable Long id){
		log.info("Deleting schedule with id : {}", id);
		if(privateCareScheduleService.findOne(id) == null){
			log.error("No schedule found with id : {}", id);
			return new  ResponseEntity<String>("No schedule found with id : " + id, HttpStatus.NOT_FOUND);
		}
		try {
			privateCareScheduleService.delete(id);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Schedule successfully deleted");
		return new ResponseEntity<String>("Schedule successfully deleted", HttpStatus.OK);
	}
}
