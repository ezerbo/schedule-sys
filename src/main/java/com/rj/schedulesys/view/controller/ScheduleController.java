//package com.rj.schedulesys.view.controller;
//
//import java.util.Date;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import com.rj.schedulesys.service.AuthenticationService;
//import com.rj.schedulesys.service.FacilityService;
//import com.rj.schedulesys.service.ScheduleService;
//import com.rj.schedulesys.service.ScheduleStatusService;
//import com.rj.schedulesys.service.ShiftService;
//import com.rj.schedulesys.view.model.ScheduleViewModel;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Controller
//@RequestMapping("/schedules")
//public class ScheduleController {
//	
//	private @Autowired ShiftService shiftService;
//	private @Autowired FacilityService facilityService;
//	private @Autowired ScheduleService scheduleService;
//	private @Autowired ScheduleStatusService scheduleStatusService;
//	private @Autowired AuthenticationService authenticationService;
//	
//	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
//	public ResponseEntity<?> saveSchedule(@RequestBody(required = true) ScheduleViewModel viewModel){
//		
//		log.info("Saving schedule : {} ", viewModel);
//		
//		//If an employee name is provided, make sure there is no schedule for that employee on the same date
//		if(viewModel.getEmployeeName() != null){
//			if(scheduleService.findScheduleByAssigneeNameAndShiftNameAndScheduleDate(
//					viewModel.getEmployeeName(), viewModel.getShift(), viewModel.getScheduleDate()) != null){
//				log.info("A schedule already exists for employee : {}", viewModel.getEmployeeName());
//				return new ResponseEntity<String>(
//						"A schedule already exists for employee : " + viewModel.getEmployeeName() 
//						+ " on : " + viewModel.getScheduleDate(), HttpStatus.INTERNAL_SERVER_ERROR);
//			}
//		}
//		
//		//Make sure the schedule date has been provided
//		
//		if(viewModel.getScheduleDate() == null){
//			log.error("No schedule date provided");
//			return new ResponseEntity<String>("No schedule provided", HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//		
//		//Verify that the facility provided exists
//		if(facilityService.findActiveByName(viewModel.getFacility()) == null){
//			log.info("No facility found by name : {}", viewModel.getFacility());
//			return new ResponseEntity<String>(
//					"No facility found by name : " + viewModel.getFacility(), HttpStatus.NOT_FOUND
//					);
//		}
//		//Validates the schedule status provided
//		//TODO can schedule status be CONFIRMED when no employee is provided ?
//		if(scheduleStatusService.findByStatus(StringUtils.upperCase(viewModel.getScheduleStatus())) == null){
//			log.info("No such schedule status : {}", viewModel.getScheduleStatus());
//			return new ResponseEntity<String>(
//					"No such schedule status : " + viewModel.getScheduleStatus(), HttpStatus.NOT_FOUND
//					);
//		}
//		//Verify that the shift provided is valid
//		if(shiftService.findByName(StringUtils.upperCase(viewModel.getShift())) == null){
//			log.info("No such shift : {}", viewModel.getShift());
//			return new ResponseEntity<String>(
//					"No such shift : " + viewModel.getShift(), HttpStatus.NOT_FOUND
//					);
//		}
//		
//		viewModel.setId(null);//set the schedule ID to null so that a new record can be created
//		//Otherwise, if a schedule exists with the same ID, it's going to be updated
//		try{
//		viewModel = scheduleService.createSchedule(
//				viewModel, authenticationService.getAuthenticatedUser().getId()
//				);
//		}catch(Exception e){
//			log.error("An unexpected error occured : {}", e.getMessage());
//			return new ResponseEntity<String>("An unexpected error occured", HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//		log.info("Schedule saved : {}", viewModel);
//		return new ResponseEntity<String>("Schedule successfully saved", HttpStatus.CREATED);
//	}
//	
//	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
//	public ResponseEntity<?> updateSchedule(@PathVariable Long id, @RequestBody ScheduleViewModel viewModel){
//		log.info("Updating schedule with id : {} : {}", id, viewModel);
//		
//		ScheduleViewModel vm = scheduleService.findById(id);
//		
//		if(vm == null){
//			log.info("No schedule found with id : {}", id);
//			return new ResponseEntity<>("No schedule found with id : " + id, HttpStatus.NOT_FOUND);
//		}
//		
//		if(!StringUtils.equals(vm.getFacility(), viewModel.getFacility())){
//			if(facilityService.findActiveByName(viewModel.getFacility()) == null){
//				log.info("No such facility : {}", viewModel.getFacility());
//			}
//		}
//		
//		if(!StringUtils.equals(vm.getShift(), viewModel.getShift())){
//			if(shiftService.findByName(viewModel.getShift()) == null){
//				log.info("No such shift : {}", viewModel.getShift());
//				return new ResponseEntity<String>(
//						"No such shift : " + viewModel.getShift(), HttpStatus.NOT_FOUND
//						);
//			}
//			
//			if(viewModel.getEmployeeName() != null){
//				if(scheduleService.findScheduleByAssigneeNameAndShiftNameAndScheduleDate(
//						viewModel.getEmployeeName(), viewModel.getShift(), viewModel.getScheduleDate()) != null){
//					log.info("A schedule already exists for employee with id : {}", id);
//					
//					return new ResponseEntity<String>(
//							"A schedule already exists for employee with id : " + id +" on : " + viewModel.getScheduleDate(), HttpStatus.INTERNAL_SERVER_ERROR
//							);
//				}
//			}
//		}
//		
//		viewModel.setId(id);
//		viewModel = scheduleService.updateSchedule(
//				viewModel, authenticationService.getAuthenticatedUser().getId()
//				);
//		log.info("Updated schedule : {}", viewModel);
//		
//		return ResponseEntity.ok("Schedule updated successfully");
//	}
//	
//	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
//	public ResponseEntity<?> findSchedules(@PathVariable Long id, @RequestParam Date startDate, @RequestParam Date endDate){
//		log.info("Finding schedule by ");
//		return new ResponseEntity<>(null, HttpStatus.OK);
//	}
//	
//}