package com.rj.schedulesys.view.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.rj.schedulesys.domain.NurseTestPK;
import com.rj.schedulesys.service.EmployeeService;
import com.rj.schedulesys.service.LicenseService;
import com.rj.schedulesys.service.NurseService;
import com.rj.schedulesys.service.NurseTestService;
import com.rj.schedulesys.service.PhoneNumberService;
import com.rj.schedulesys.service.TestService;
import com.rj.schedulesys.view.model.EmployeeViewModel;
import com.rj.schedulesys.view.model.LicenseViewModel;
import com.rj.schedulesys.view.model.NurseTestViewModel;
import com.rj.schedulesys.view.model.NurseViewModel;
import com.rj.schedulesys.view.model.PhoneNumberViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/nurses")
public class NurseController {
	
	@Autowired
	private TestService testService;
	
	@Autowired
	private NurseService nurseService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private NurseTestService nurseTestService;
	
	@Autowired
	private PhoneNumberService phoneNumberService;
	
	@Autowired
	private LicenseService licenseService;
	
	@Autowired
	private DozerBeanMapper dozerMapper;
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody ResponseEntity<?> create(@RequestBody NurseViewModel viewModel){
		
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
			EmployeeViewModel employeeViewModel = dozerMapper.map(viewModel, EmployeeViewModel.class);
			employeeViewModel = employeeService.create(employeeViewModel);
			viewModel.setId(employeeViewModel.getId());
			nurseService.create(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Nurse successfully created");
		
		return new ResponseEntity<String>("Nurse successfully created", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public @ResponseBody ResponseEntity<?> update(@PathVariable Long id, @RequestBody NurseViewModel viewModel){
		
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
			EmployeeViewModel employeeViewModel = dozerMapper.map(viewModel, EmployeeViewModel.class);
			employeeService.update(employeeViewModel);
			nurseService.update(viewModel);
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
		
		NurseViewModel viewModel = nurseService.findOne(id);
		
		if(viewModel == null){
			log.error("No nurse found with id : {}", id);
			return new ResponseEntity<String>("No nurse found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		log.info("Nurse found : {}", viewModel);
		
		return new ResponseEntity<NurseViewModel>(viewModel, HttpStatus.OK);
		
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
	
	@RequestMapping(value = "/{id}/licenses", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> findAllLicenses(@PathVariable Long id){
		
		log.info("Finding all licenses for user with id : {}", id);
		
		if(nurseService.findOne(id) == null){
			log.info("No nurse found with id : {}", id);
			return new ResponseEntity<>("No nurse found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		List<LicenseViewModel> viewModels = licenseService.findAllByNurse(id);
		
		if(viewModels.isEmpty()){
			log.info("No licenses found for nurse with id : ", id);
			return new ResponseEntity<>("No licenses found for nurse with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		log.info("Licenses : {} found for nurse with id : {}", viewModels, id);
		
		return new ResponseEntity<>(viewModels, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/tests", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<?> addOrUpdateTest(@PathVariable Long id, @RequestBody NurseTestViewModel viewModel){
		
		log.info("Adding new test : {} for nurse with id : {}", viewModel, id);
		
		if(nurseService.findOne(id) == null){
			log.warn("No nurse found with id : {}", id);
			return new ResponseEntity<>("No nurse found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		viewModel.setNurseId(id);
		
		try {
			viewModel = nurseTestService.createOrUpate(viewModel);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Test successfully added");
		
		return new ResponseEntity<>("Test successfully added", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}/tests", method = RequestMethod.GET)
	public ResponseEntity<?> findAllTests(@PathVariable Long id){
		
		log.info("Fetching all test for nurse with id", id);
		
		if(nurseService.findOne(id) == null){
			log.warn("No nurse found with id : {}", id);
			return new ResponseEntity<>("No nurse found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		List<NurseTestViewModel> viewModels = nurseTestService.findAllByNurse(id);
		
		if(viewModels.isEmpty()){
			log.warn("No test found for nurse with id : {}", id);
			return new ResponseEntity<>("No test found for nurse with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		log.info("Tests found : {}", viewModels);
		
		return new ResponseEntity<>(viewModels, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/tests/{testId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteTest(@PathVariable Long id, @PathVariable Long testId){
		
		log.info("Deleting NurseTest with nurseId : {} and testId : {}", id, testId);
		
		if(nurseService.findOne(id) == null){
			log.warn("No nurse found with id : {}", id);
			return new ResponseEntity<>("No nurse found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		if(testService.findOne(testId) == null){
			log.error("No test found with id : {}", testId);
			return new ResponseEntity<>("No test found with id : " + testId, HttpStatus.NOT_FOUND);
		}
		
		NurseTestPK nurseTestPK = NurseTestPK.builder()
				.testId(testId)
				.nurseId(id)
				.build();
		
		try {
			nurseTestService.delete(nurseTestPK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Test successfully deleted");
		
		return new ResponseEntity<>("Test successfully deleted", HttpStatus.OK);
	}
	
	
	
}