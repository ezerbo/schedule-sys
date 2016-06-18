package com.rj.sys.view.controller;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rj.sys.service.FacilityService;
import com.rj.sys.service.ScheduleService;
import com.rj.sys.view.model.FacilityViewModel;
import com.rj.sys.view.model.ScheduleViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/facilities")
public class FacilityController {
	
	private @Autowired FacilityService facilityService;
	private @Autowired ScheduleService scheduleService;

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findAllFacilities(){
		
		log.info("Finding all facilities");
		
		List<FacilityViewModel> viewModels = facilityService.findAll();
		if(viewModels.isEmpty()){
			return new ResponseEntity<String>("No facility found", HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<List<FacilityViewModel>>(viewModels, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findByIdOrName(@PathVariable Long id){
		
		FacilityViewModel viewModel = facilityService.findActiveById(id);
		
		if(viewModel == null){
			return new ResponseEntity<String>(
					"No facility found with either id : " + id, HttpStatus.NOT_FOUND
					);
		}
		
		return new ResponseEntity<FacilityViewModel>(viewModel, HttpStatus.OK);
		
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody ResponseEntity<?> createFacility(
			@RequestBody @Validated FacilityViewModel viewModel, BindingResult result){
		//TODO add validation messages in properties files
		log.info("Create request recieved for facility : {}", viewModel);
		
		if(result.hasErrors()){
			List<ObjectError> errors = result.getGlobalErrors();
			log.error("Validation error occured : {}", errors);
			return new ResponseEntity<List<ObjectError>>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(facilityService.findByName(viewModel.getName()) != null){
			log.info("A facility already exist with name : {}", viewModel.getName());
			return new ResponseEntity<String>(
					"A facility with name : " + viewModel.getName() + " already exist", HttpStatus.INTERNAL_SERVER_ERROR
					);
		}
		
		if(facilityService.findByPhoneNumber(viewModel.getPhoneNumber()) != null){
			log.info("A facility with phone number : {} already exist", viewModel.getPhoneNumber());
			return new ResponseEntity<String>(
					"A facility with phone number : " + viewModel.getPhoneNumber() + " already exist", HttpStatus.INTERNAL_SERVER_ERROR
					);
		}
		
		viewModel = facilityService.createOrUpdateFacility(viewModel);
		
		log.info("Successfully created facility : {}", viewModel);
		return new ResponseEntity<String>("Facility successfully created", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public @ResponseBody ResponseEntity<?> updateFacility(
			@PathVariable Long id, @RequestBody FacilityViewModel viewModel, BindingResult result){
		
		log.info("Update request recieved for facility {} with id: {}", viewModel, id);
		FacilityViewModel vm = facilityService.findById(id);
		
		if(vm == null){
			log.info("No facikity found with id : {}", id);
			return new ResponseEntity<String>("No facility found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		if(result.hasErrors()){
			List<ObjectError> errors = result.getGlobalErrors();
			log.error("Validation error occured : {}", errors);
			return new ResponseEntity<List<ObjectError>>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(!StringUtils.equalsIgnoreCase(vm.getName(), viewModel.getName())){
			if(facilityService.findByName(viewModel.getName()) != null){
				log.info("A facility already exist with name : {}", viewModel.getName());
				return new ResponseEntity<String>(
						"A facility with name : " + viewModel.getName() + " already exist", HttpStatus.INTERNAL_SERVER_ERROR
						);
			}
		}
		
		if(!StringUtils.equalsIgnoreCase(vm.getName(), viewModel.getName())){
			if(facilityService.findByPhoneNumber(viewModel.getPhoneNumber()) != null){
				log.info("A facility with phone number : {} already exist", viewModel.getPhoneNumber());
				return new ResponseEntity<String>(
						"A facility with phone number : " + viewModel.getPhoneNumber() + " already exist", HttpStatus.INTERNAL_SERVER_ERROR
						);
			}
		}
		
		viewModel.setId(id);//overriding the id
		
		viewModel = facilityService.createOrUpdateFacility(viewModel);
		
		log.info("Facility successfully updated : {}", viewModel);
		return new ResponseEntity<String>("Facility successfully updated", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity<?> deleteFacility(@PathVariable Long id){
		
		log.info("Delete request recieved for facility with id : {}", id);
		//TODO add a soft delete feature to Facility
		if(facilityService.findActiveById(id) == null){
			log.info("No facility found with id : {}", id);
			return new ResponseEntity<String>("No facility found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		FacilityViewModel viewModel = null;//facilityService.deleteFacility(id);
		
		log.info("Facility successfully deleted : {}", viewModel);
		return new ResponseEntity<String>("Facility successfully deleted", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/schedules", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> findSchedules(@PathVariable Long id, @RequestParam Date startDate, @RequestParam Date endDate){
		log.info("Finding schedules between startDate : {} and endDate : {} for facility with id : {}", startDate, endDate);
		
		if(facilityService.findActiveById(id) == null){
			log.info("No facility found with id : {}", id);
			return new ResponseEntity<String>("No facility found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		List<ScheduleViewModel> viewModels = scheduleService.findAllBetweenDatesByFacilityId(startDate, endDate, id);
		if(viewModels.isEmpty()){
			log.info("No schedules found between : {} and : {} for facility with id : {}", startDate, endDate, id);
			return new ResponseEntity<>(
					"No schedules found between : " + startDate + " and : " + endDate + " for facility with id : " + id, HttpStatus.NOT_FOUND
					);
		}
	
		log.info("Schedules found : {}", viewModels);
		return new ResponseEntity<>(viewModels, HttpStatus.OK);
	}
	
}