package com.rj.sys.view.controller;

import java.util.Date;
import java.util.List;

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

import com.rj.sys.service.AuthenticationService;
import com.rj.sys.service.FacilityService;
import com.rj.sys.service.ScheduleService;
import com.rj.sys.service.ScheduleStatusService;
import com.rj.sys.service.ShiftService;
import com.rj.sys.service.UserService;
import com.rj.sys.view.model.ScheduleViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/employees")
public class ScheduleController {
	
	private @Autowired UserService userService;
	private @Autowired ShiftService shiftService;
	private @Autowired FacilityService facilityService;
	private @Autowired ScheduleService scheduleService;
	private @Autowired ScheduleStatusService scheduleStatusService;
	private @Autowired AuthenticationService authenticationService;
	
	
	@RequestMapping(value = "/{id}/schedules", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity <?>  findScheduleByAssigneeId(@PathVariable Long id
			, @RequestParam(value = "scheduleDate", required = true) Date scheduleDate){
		
		log.info("Finding schedule by id : {}", id);
		
		if(userService.findEmployeeById(id) == null){
			log.info("No employee found by id : {}", id);
			return new ResponseEntity<String>("No employee found by id : " + id, HttpStatus.NOT_FOUND);
		}
		
		List<ScheduleViewModel> viewModels = scheduleService.findScheduleByAssigneeId(id, scheduleDate);
		
		if(viewModels.isEmpty()){
			log.info("No schedule found for employee with id : {}", id);
			return new ResponseEntity<String>(
					"No schedule found for employee with id : " + id + " on : " + scheduleDate, HttpStatus.NOT_FOUND);
		}
		
		log.info("Schdules : {} on : {} found for employee with id : {}", viewModels, scheduleDate, id);
		
		return new ResponseEntity<List<ScheduleViewModel>>(viewModels, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/schedules", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<?> saveSchedule(@RequestBody(required = true) ScheduleViewModel viewModel
			, @PathVariable Long id){
		
		log.info("Saving schedule : {} for employee with id : {}", viewModel, id);
		
		if(userService.findEmployeeById(id) == null){
			log.info("No employee found by id : {}", id);
			return new ResponseEntity<String>(
					"No employee found by id : " + id, HttpStatus.NOT_FOUND
					);
		}
		
		if(facilityService.findActiveByName(viewModel.getFacility()) == null){
			log.info("No such facility : {}", viewModel.getFacility());
			return new ResponseEntity<String>(
					"No such facility : " + viewModel.getFacility(), HttpStatus.NOT_FOUND
					);
		}
		
		if(scheduleStatusService.findByStatus(viewModel.getScheduleStatus()) == null){
			log.info("No such schedule status : {}", viewModel.getScheduleStatus());
			return new ResponseEntity<String>(
					"No such schedule status : " + viewModel.getScheduleStatus(), HttpStatus.NOT_FOUND
					);
		}
		
		if(shiftService.findByName(viewModel.getShift()) == null){
			log.info("No such shift : {}", viewModel.getShift());
			return new ResponseEntity<String>(
					"No such shift : " + viewModel.getShift(), HttpStatus.NOT_FOUND
					);
		}
		
		if(scheduleService.findScheduleByAssigneeIdAndShiftNameAndScheduleDate(id, viewModel.getShift(), viewModel.getScheduleDate()) != null){
			log.info("A schedule already exists for employee with id : {}", id);
			return new ResponseEntity<String>(
					"A schedule already exists for employee with id : " + id +" on : " + viewModel.getScheduleDate(), HttpStatus.NOT_FOUND
					);
		}
		viewModel.setId(null);
		viewModel = scheduleService.createSchedule(viewModel, authenticationService.getAuthenticationService().getId());
		log.info("Schedule saved : {}", viewModel);
		return new ResponseEntity<String>("Schedule successfully saved", HttpStatus.CREATED);
		
	}
	
//	@RequestMapping(value = "/{id}/schedules/{scheduleId}", method = RequestMethod.PUT, consumes = "application/json")
//	public ResponseEntity<?> updateSchedule(Long id,Long scheduleId, ScheduleViewModel viewModel){
//		log.info("updating schedule with id : {}");
//	}
}