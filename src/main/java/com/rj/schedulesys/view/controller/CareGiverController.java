package com.rj.schedulesys.view.controller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rj.schedulesys.service.CareGiverService;
import com.rj.schedulesys.service.EmployeeService;
import com.rj.schedulesys.service.PhoneNumberService;
import com.rj.schedulesys.service.PrivateCareScheduleService;
import com.rj.schedulesys.view.model.EmployeeViewModel;
import com.rj.schedulesys.view.model.GetPrivateCareScheduleViewModel;
import com.rj.schedulesys.view.model.PhoneNumberViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/care-givers")
public class CareGiverController {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private CareGiverService careGiverService;
	
	@Autowired
	private PhoneNumberService phoneNumberService;
	
	@Autowired
	private PrivateCareScheduleService scheduleService;
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody ResponseEntity<?> create(@RequestBody EmployeeViewModel viewModel){
		
		Assert.notNull(viewModel, "No care giver provided");
		
		log.info("Creating care giver : {}", viewModel);

		try {
			careGiverService.validatePosition(viewModel.getPositionName());
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		viewModel.setId(null);
		
		try{
			viewModel = employeeService.create(viewModel);
			log.info("EMPLOYEE ID : {}", viewModel.getId());
			careGiverService.create(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Care giver successfully created");
		
		return new ResponseEntity<String>("Care giver successfully created", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public @ResponseBody ResponseEntity<?> update(@PathVariable Long id, @RequestBody EmployeeViewModel viewModel){
		
		log.info("Updating care giver : {}", viewModel);
		
		if(careGiverService.findOne(id) == null){
			log.error("No care giver found with id : {}", id);
			return new ResponseEntity<String>("No care giver found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		try {
			careGiverService.validatePosition(viewModel.getPositionName());
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		viewModel.setId(id);
		
		try{
			employeeService.update(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Care giver successfully updated");
		
		return new ResponseEntity<String>("Care giver successfully updated", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity<String> delete(@PathVariable Long id){
		
		log.info("Deleting care giver with id : {}", id);
		
		if(careGiverService.findOne(id) == null){
			log.error("No care giver found with id : {}", id);
			return new ResponseEntity<String>("No care giver found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		try{
			careGiverService.delete(id);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Care giver successfully deleted");
		
		return new ResponseEntity<String>("Care giver successfully deleted", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findOne(@PathVariable Long id){
		
		log.info("Fetching care giver with id : {}", id);
		
		EmployeeViewModel viewModel = careGiverService.findOne(id);
		
		if(viewModel == null){
			log.error("No care giver found with id : {}", id);
			return new ResponseEntity<String>("No care giver found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		log.info("Care giver found : {}", viewModel);
		
		return new ResponseEntity<EmployeeViewModel>(viewModel, HttpStatus.OK);
		
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findAll(){
		
		log.info("Fetching all care givers");
		
		List<EmployeeViewModel> viewModels = careGiverService.findAll();
		
		if(viewModels.isEmpty()){
			log.warn("No care giver found");
			return new ResponseEntity<>("No care giver found", HttpStatus.NOT_FOUND);
		}
		
		log.info("Care givers found : {}", viewModels);
		
		return new ResponseEntity<>(viewModels, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/{id}/phone-numbers", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody ResponseEntity<?> addNewPhoneNumber(@PathVariable Long id, @RequestBody PhoneNumberViewModel viewModel){
		
		log.info("Adding new phone number : {} for user with id : {}", viewModel, id);
		
		if(careGiverService.findOne(id) == null){
			log.warn("No nurse found with id : {}", id);
			return new ResponseEntity<>("No nurse found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		try{
			viewModel = phoneNumberService.create(id, viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}	
		
		log.info("Added phone number : {}", viewModel);
		
		return new ResponseEntity<>("Phone number added successfully", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}/phone-numbers/{phoneNumberId}", method = RequestMethod.PUT, consumes = "application/json")
	public @ResponseBody ResponseEntity<?> updatePhoneNumber(@PathVariable Long id, @PathVariable Long phoneNumberId
			, @RequestBody PhoneNumberViewModel viewModel){
		
		log.info("Adding new phone number : {} for care giver with id : {}", viewModel, id);
		
		if(phoneNumberService.findByCareGiverAndNumberId(id, phoneNumberId) == null){
			log.warn("No phone number with id : {} found for employee with id : {}", id);
			return new ResponseEntity<>("No phone number found with id : " + id
					+ " for care giver with id " + phoneNumberId, HttpStatus.NOT_FOUND);
		}
		viewModel.setId(phoneNumberId);
		try{
			viewModel = phoneNumberService.update(id, viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}	
		
		log.info("Added phone number : {}", viewModel);
		
		return new ResponseEntity<>("Phone number added successfully", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}/phone-numbers/{phoneNumberId}", method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity<?> deletePhoneNumber(@PathVariable Long id, @PathVariable Long phoneNumberId){
		
		log.info("Deleting phone number with id : {} for care giver with id : {}", phoneNumberId, id);
		
		if(phoneNumberService.findByCareGiverAndNumberId(id, phoneNumberId) == null){
			log.warn("No nurse found with id : {}", id);
			return new ResponseEntity<>("No phone number found with id : " + id
					+ " for care giver with id " + phoneNumberId, HttpStatus.NOT_FOUND);
		}
		
		try{
			phoneNumberService.delete(id, phoneNumberId);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}	
		
		log.info("Phone number successfully deleted");
		
		return new ResponseEntity<>("Phone number successfully deleted", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/schedules", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getSchedules(@PathVariable Long id, @RequestParam(required = false) Date startDate,
			@RequestParam(required = false) Date endDate){
		log.info("Fetching all schedule between : {} and : {} for employee with id : {}", startDate, endDate, id);
		if(employeeService.findOne(id) == null){
			log.warn("No employee found with id : {}", id);
			return new ResponseEntity<>("No employee found with id : " + id, HttpStatus.NOT_FOUND);
		}
		List<GetPrivateCareScheduleViewModel> viewModels  = new LinkedList<>();
		if(startDate == null || endDate == null){
			viewModels = scheduleService.findAllByCareGiver(id);
		}else{
			viewModels = scheduleService.findAllBetweenDatesByCareGiver(startDate, endDate, id);
		}
		if(viewModels.isEmpty()){
			log.warn("No schedule found between : {} and : {} for employee with id : {}", startDate, endDate, id);
			return new ResponseEntity<>("No schedule found between : " + startDate + " and : " +
						endDate + " for employee with id : " + id, HttpStatus.NOT_FOUND);
		}
		log.info("Schedules found  : {}", viewModels);
		return new ResponseEntity<>(viewModels, HttpStatus.OK);
	}
	
}