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
import com.rj.schedulesys.service.ScheduleService;
import com.rj.schedulesys.view.model.CreateScheduleViewModel;
import com.rj.schedulesys.view.model.GetScheduleViewModel;
import com.rj.schedulesys.view.model.UpdateScheduleViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/schedules")
public class ScheduleController {
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<?> saveSchedule(@RequestBody CreateScheduleViewModel viewModel){
		
		log.info("Saving schedule : {} ", viewModel);
		
		viewModel.setId(null);//set the schedule ID to null so that a new record can be created
		//Otherwise, if a schedule exists with the same ID, it's going to be updated
		try{
			viewModel = scheduleService.create(
					viewModel, authenticationService.getAuthenticatedUser().getId());
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Schedule saved : {}", viewModel);
		
		return new ResponseEntity<String>("Schedule successfully created", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateScheduleViewModel viewModel){
		
		log.info("Updating schedule with id : {} : {}", id, viewModel);
		
		GetScheduleViewModel vm = scheduleService.findOne(id);
		
		if(vm == null){
			log.info("No schedule found with id : {}", id);
			return new ResponseEntity<>("No schedule found with id : " + id, HttpStatus.NOT_FOUND);
		}

		viewModel.setId(id);
		
		try{
			scheduleService.update(viewModel, authenticationService.getAuthenticatedUser().getId());
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Updated schedule : {}", viewModel);
		
		return ResponseEntity.ok("Schedule updated successfully");
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable Long id){
		
		log.info("Deleting schedule with id : {}", id);
		
		if(scheduleService.findOne(id) == null){
			log.error("No schedule found with id : {}", id);
			return new  ResponseEntity<String>("No schedule found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		try {
			scheduleService.delete(id);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Schedule successfully deleted");
		
		return new ResponseEntity<String>("Schedule successfully deleted", HttpStatus.OK);
	}
	
}