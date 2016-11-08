package com.rj.schedulesys.view.controller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.dozer.DozerBeanMapper;
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

import com.rj.schedulesys.service.EmployeeService;
import com.rj.schedulesys.service.FacilityScheduleService;
import com.rj.schedulesys.service.LicenseService;
import com.rj.schedulesys.service.NurseService;
import com.rj.schedulesys.service.PhoneNumberService;
import com.rj.schedulesys.view.model.EmployeeViewModel;
import com.rj.schedulesys.view.model.GetLicenseViewModel;
import com.rj.schedulesys.view.model.GetScheduleViewModel;
import com.rj.schedulesys.view.model.LicenseViewModel;
import com.rj.schedulesys.view.model.NurseViewModel;
import com.rj.schedulesys.view.model.PhoneNumberViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/nurses")
public class NurseController {
	
	private NurseService nurseService;
	private LicenseService licenseService;
	private EmployeeService employeeService;
	private PhoneNumberService phoneNumberService;
	private FacilityScheduleService scheduleService;
	
	@Autowired
	public NurseController(NurseService nurseService, EmployeeService employeeService,
			PhoneNumberService phoneNumberService, LicenseService licenseService,
			DozerBeanMapper dozerMapper, FacilityScheduleService scheduleService) {
		this.nurseService = nurseService;
		this.employeeService = employeeService;
		this.phoneNumberService = phoneNumberService;
		this.licenseService = licenseService;
		this.scheduleService = scheduleService;
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody ResponseEntity<?> create(@RequestBody EmployeeViewModel viewModel){
		Assert.notNull(viewModel, "No nurse provided");
		log.info("Creating nurse : {}", viewModel);
		try {
			nurseService.validatePosition(viewModel.getPositionName());
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		viewModel.setId(null);
		try{
			viewModel = employeeService.create(viewModel);
			viewModel.setId(viewModel.getId());
			nurseService.create(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Nurse successfully created");
		return new ResponseEntity<String>("Nurse successfully created", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public @ResponseBody ResponseEntity<?> update(@PathVariable Long id, @RequestBody EmployeeViewModel viewModel){
		log.info("Updating nurse : {}", viewModel);
		if(nurseService.findOne(id) == null){
			log.error("No nurse found with id : {}", id);
			return new ResponseEntity<String>("No nurse found with id : " + id, HttpStatus.NOT_FOUND);
		}
		try {
			nurseService.validatePosition(viewModel.getPositionName());
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
		log.info("Nurse successfully updated");
		return new ResponseEntity<String>("Nurse successfully updated", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity<String> delete(@PathVariable Long id){
		log.info("Deleting nurse with id : {}", id);
		if(nurseService.findOne(id) == null){
			log.error("No nurse found with id : {}", id);
			return new ResponseEntity<String>("No nurse found with id : " + id, HttpStatus.NOT_FOUND);
		}
		try{
			nurseService.delete(id);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Nurse successfully deleted");
		return new ResponseEntity<String>("Nurse successfully deleted", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findOne(@PathVariable Long id){
		log.info("Fetching nurse with id : {}", id);
		if(nurseService.findOne(id) == null){
			log.error("No nurse found with id : {}", id);
			return new ResponseEntity<String>("No nurse found with id : " + id, HttpStatus.NOT_FOUND);
		}
		EmployeeViewModel viewModel = employeeService.findOne(id);
		log.info("Nurse found : {}", viewModel);
		return new ResponseEntity<EmployeeViewModel>(viewModel, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findAll(){
		log.info("Fetching all nurses");
		List<NurseViewModel> viewModels = nurseService.findAll();
		if(viewModels.isEmpty()){
			log.warn("No nurse found");
			return new ResponseEntity<>("No nurse found", HttpStatus.NOT_FOUND);
		}
		log.info("Nurses found : {}", viewModels);
		return new ResponseEntity<>(viewModels, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/phone-numbers", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody ResponseEntity<?> addNewPhoneNumber(@PathVariable Long id, @RequestBody PhoneNumberViewModel viewModel){
		
		log.info("Adding new phone number : {} for user with id : {}", viewModel, id);
		
		if(nurseService.findOne(id) == null){
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
		
		log.info("Adding new phone number : {} for user with id : {}", viewModel, id);
		
		if(phoneNumberService.findByNurseAndNumberId(id, phoneNumberId) == null){
			log.warn("No nurse found with id : {}", id);
			return new ResponseEntity<>("No phone number found with id : " + id
					+ " for employee with id " + phoneNumberId, HttpStatus.NOT_FOUND);
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
		
		log.info("Deleting phone number with id : {} for nurse with id : {}", phoneNumberId, id);
		
		if(phoneNumberService.findByNurseAndNumberId(id, phoneNumberId) == null){
			log.warn("No nurse found with id : {}", id);
			return new ResponseEntity<>("No phone number found with id : " + id
					+ " for nurse with id " + phoneNumberId, HttpStatus.NOT_FOUND);
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
	
	@RequestMapping(value = "/{id}/phone-numbers/{phoneNumberId}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> getPhoneNumber(@PathVariable Long id, @PathVariable Long phoneNumberId){
		log.info("Getting phone number with id : {} for nurse with id : {}", phoneNumberId, id);
		PhoneNumberViewModel viewModel = phoneNumberService.findByNurseAndNumberId(id, phoneNumberId);
		if(viewModel == null){
			log.warn("No nurse found with id : {}", id);
			return new ResponseEntity<>("No phone number found with id : " + id
					+ " for nurse with id " + phoneNumberId, HttpStatus.NOT_FOUND);
		}
		log.info("Phone number successfully retrieved : {}", viewModel);
		return new ResponseEntity<>(viewModel, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/licenses", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> findAllLicenses(@PathVariable Long id){
		log.info("Finding all licenses for user with id : {}", id);
		if(nurseService.findOne(id) == null){
			log.info("No nurse found with id : {}", id);
			return new ResponseEntity<>("No nurse found with id : " + id, HttpStatus.NOT_FOUND);
		}
		List<GetLicenseViewModel> viewModels = licenseService.findAllByNurse(id);
		if(viewModels.isEmpty()){
			log.info("No licenses found for nurse with id : ", id);
			return new ResponseEntity<>("No licenses found for nurse with id : " + id, HttpStatus.NOT_FOUND);
		}
		log.info("Licenses : {} found for nurse with id : {}", viewModels, id);
		return new ResponseEntity<>(viewModels, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/licenses", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<?> create(@PathVariable("id") Long nurseId, @RequestBody LicenseViewModel viewModel){
		if(nurseService.findOne(nurseId) == null){
			log.info("No nurse found with id : {}", nurseId);
			return new ResponseEntity<>("No nurse found with id : " + nurseId, HttpStatus.NOT_FOUND);
		}
		log.info("Creating license : {}", viewModel);
		viewModel.setId(null);
		viewModel.setNurseId(nurseId);
		try{
			viewModel = licenseService.create(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("License {} created successfully", viewModel);
		return new ResponseEntity<>("License successfully created", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}/licenses/{licenseId}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<?> update(@PathVariable Long id, @PathVariable Long licenseId,
			@RequestBody LicenseViewModel viewModel){
		log.info("Updating license : {}", viewModel);
		NurseViewModel nurse = nurseService.findOne(id);
		if(nurse == null){
			log.info("No nurse found with id : {}", id);
			return new ResponseEntity<>("No nurse found with id : " + id, HttpStatus.NOT_FOUND);
		}
		GetLicenseViewModel license = licenseService.findOne(licenseId);
		if(license == null){
			log.info("No license with id : {}", licenseId);
			return new ResponseEntity<>("No license found with id : " + licenseId, HttpStatus.NOT_FOUND);
		}
		if(license.getNurse().getId() != nurse.getId()){
			log.error("No license with id : {} found for nurse with id : {}", license.getId(), nurse.getId());
			return new ResponseEntity<>("No license with id : " 
					+ license.getNurse().getId() + " found for nurse with id : {}" + nurse.getId(), HttpStatus.NOT_FOUND);
		}
		viewModel.setNurseId(id);
		viewModel.setId(licenseId);
		try{
			viewModel = licenseService.update(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("License : {} successfully updated", viewModel);
		return new ResponseEntity<>("License successfully updated", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/schedules", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getSchedules(@PathVariable Long id, @RequestParam(required = false) Date startDate,
			@RequestParam(required = false) Date endDate){
		log.info("Fetching all schedule between : {} and : {} for employee with id : {}", startDate, endDate, id);
		if(employeeService.findOne(id) == null){
			log.warn("No employee found with id : {}", id);
			return new ResponseEntity<>("No employee found with id : " + id, HttpStatus.NOT_FOUND);
		}
		List<GetScheduleViewModel> viewModels  = new LinkedList<>();
		if(startDate == null || endDate == null){
			viewModels = scheduleService.findAllByNurse(id);
		}else{
			viewModels = scheduleService.findAllBetweenDatesByNurse(startDate, endDate, id);
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