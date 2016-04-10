package com.rj.sys.view.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rj.sys.service.PositionService;
import com.rj.sys.service.UserService;
import com.rj.sys.view.model.EmployeeViewModel;
import com.rj.sys.view.model.SupervisorViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {
	//TODO Split into employee and supervisor controllers
	private @Autowired UserService userService;
	private @Autowired PositionService positionService;
	
	@RequestMapping(value = "/employees", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> createEmployee(@RequestBody EmployeeViewModel viewModel){
		log.info("Employee creation request received : {}", viewModel);
		
		if(positionService.findByName(viewModel.getPosition()) == null){
			log.info("No such position : {}", viewModel.getPosition());
			return new ResponseEntity<String>(
					"No such position : " + viewModel.getPosition(), HttpStatus.NOT_FOUND
					);
		}
		
		if(userService.isEmailAddressExistant(viewModel.getEmailAddress())){
			log.info("{} already belongs to a user", viewModel.getEmailAddress());
			return new ResponseEntity<String>(
					viewModel.getEmailAddress() + " already belongs to a user", HttpStatus.INTERNAL_SERVER_ERROR
					);
		}
		
		if(userService.isPrimaryPhoneNumberExistant(viewModel.getPrimaryPhoneNumber())){
			log.info("{} already belongs to a user", viewModel.getPrimaryPhoneNumber());
			return new ResponseEntity<String>(
					viewModel.getPrimaryPhoneNumber() + " already belongs to a user", HttpStatus.INTERNAL_SERVER_ERROR
					);
		}
		
		if(StringUtils.isNotBlank(viewModel.getSecondaryPhoneNumber())){
			if(userService.isSecondaryPhoneNumberExistant(viewModel.getSecondaryPhoneNumber())){
				log.info("{} already belongs to a user", viewModel.getSecondaryPhoneNumber());
				return new ResponseEntity<String>(
						viewModel.getSecondaryPhoneNumber() + " already belongs to a user", HttpStatus.INTERNAL_SERVER_ERROR
						);
			}
		}
		
		if(StringUtils.isNotBlank(viewModel.getOtherPhoneNumber())){
			if(userService.isOtherPhoneNumberExistant(viewModel.getOtherPhoneNumber())){
				log.info("{} already belongs to a user", viewModel.getOtherPhoneNumber());
				return new ResponseEntity<String>(
						viewModel.getOtherPhoneNumber() + " already belongs to a user", HttpStatus.INTERNAL_SERVER_ERROR
						);
			}
		}
		viewModel.setId(null);
		viewModel = userService.createOrUpdateEmployee(viewModel);
		
		log.info("Successfully created employee : {}", viewModel);
		return new ResponseEntity<String>("Employee successfully created", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/employees/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<String> updateEmployee(@PathVariable Long id, @RequestBody EmployeeViewModel viewModel){
		log.info("Updating employee : {}", viewModel);
		EmployeeViewModel vm = userService.findEmployeeById(id);
		
		if(vm == null){
			log.info("No employee found with id : {}", id);
			return new ResponseEntity<String>(
					"No employee found with id : " + id, HttpStatus.NOT_FOUND
					);
		}
		
		if(!StringUtils.equalsIgnoreCase(vm.getEmailAddress(), viewModel.getEmailAddress())){
			if(userService.isEmailAddressExistant(viewModel.getEmailAddress())){
				log.info("{} already belongs to a user", viewModel.getEmailAddress());
				return new ResponseEntity<String>(
						viewModel.getEmailAddress() + " already belongs to a user", HttpStatus.INTERNAL_SERVER_ERROR
						);
			}
		}
		
		if(!StringUtils.equals(vm.getPrimaryPhoneNumber(), viewModel.getPrimaryPhoneNumber())){
			if(userService.isPrimaryPhoneNumberExistant(viewModel.getPrimaryPhoneNumber())){
				log.info("{} already belongs to a user", viewModel.getPrimaryPhoneNumber());
				return new ResponseEntity<String>(
						viewModel.getPrimaryPhoneNumber() + " already belongs to a user", HttpStatus.INTERNAL_SERVER_ERROR
						);
			}
		}
		
		if(!StringUtils.equals(vm.getSecondaryPhoneNumber(), viewModel.getSecondaryPhoneNumber())){
			if(userService.isSecondaryPhoneNumberExistant(viewModel.getSecondaryPhoneNumber())){
				log.info("{} already belongs to a user", viewModel.getSecondaryPhoneNumber());
				return new ResponseEntity<String>(
						viewModel.getSecondaryPhoneNumber() + " already belongs to a user", HttpStatus.INTERNAL_SERVER_ERROR
						);
			}
		}
		
		if(!StringUtils.equals(vm.getOtherPhoneNumber(), viewModel.getOtherPhoneNumber())){
			if(userService.isOtherPhoneNumberExistant(viewModel.getOtherPhoneNumber())){
				log.info("{} already belongs to a user", viewModel.getOtherPhoneNumber());
				return new ResponseEntity<String>(
						viewModel.getOtherPhoneNumber() + " already belongs to a user", HttpStatus.INTERNAL_SERVER_ERROR
						);
			}
		}
		
		viewModel.setId(id);//Override the id
		viewModel = userService.createOrUpdateEmployee(viewModel);
		
		log.info("Successfully updated employee : {}", viewModel);
		return new ResponseEntity<String>("Employee successfully updated", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/employees/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> findEmployeeById(@PathVariable Long id){
		log.info("Finding employee by id : {}", id);
		EmployeeViewModel viewModel = userService.findEmployeeById(id);
		if(viewModel == null){
			log.info("No employee found by id : {}", id);
			return new ResponseEntity<String>("No employee found by id : " + id, HttpStatus.NOT_FOUND);
		}
		
		log.info("Employee : {} found by id : {}", viewModel, id);
		
		return new ResponseEntity<EmployeeViewModel>(viewModel, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/employees", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getAllEmployees(){
		log.info("Finding all employees");
		List<EmployeeViewModel> viewModels = userService.findAllEmployees();
		if(viewModels.isEmpty()){
			log.info("No employee found");
			return new ResponseEntity<String>("No employee found", HttpStatus.NOT_FOUND);
		}
		
		log.info("Employees found : {}", viewModels);
		return new ResponseEntity<List<EmployeeViewModel>>(viewModels, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/employees/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteEmployee(@PathVariable Long id){
		log.info("Deleting user with id : {}", id);
		
		if(userService.findEmployeeById(id) == null){
			log.info("No employee found by id : {}", id);
			return new ResponseEntity<String>("No employee found by id : " + id, HttpStatus.NOT_FOUND);
		}
		
		userService.deleteUser(id);
		log.info("Employee with id : {} successfully deleted", id);
		
		return new ResponseEntity<String>("Employee successfully deleted", HttpStatus.NOT_FOUND);
	}
	
	
	@RequestMapping(value = "/supervisors", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> createSupervisor(@RequestBody SupervisorViewModel viewModel){
		log.info("Creating supervisor : {}", viewModel);
		
		if(userService.isPrimaryPhoneNumberExistant(viewModel.getPhoneNumber())){
			log.info("{} already belongs to a user", viewModel.getPhoneNumber());
			return new ResponseEntity<String>(
					viewModel.getPhoneNumber() + " already belongs to a user", HttpStatus.INTERNAL_SERVER_ERROR
					);
		}
		
		if(userService.isEmailAddressExistant(viewModel.getEmailAddress())){
			log.info("{} already belongs to a user", viewModel.getEmailAddress());
			return new ResponseEntity<String>(
					viewModel.getEmailAddress() + " already belongs to a user", HttpStatus.INTERNAL_SERVER_ERROR
					);
		}
		viewModel.setId(null);
		viewModel = userService.createOrUpdateSupervisor(viewModel);
		
		log.info("Supervisor successfully created : {}", viewModel);
		return new ResponseEntity<String>("Supervisor successfully created", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/supervisors/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<String> updateSupervisor(@PathVariable Long id, @RequestBody SupervisorViewModel viewModel){
		log.info("Updating supervisor with id : {}", id);
		SupervisorViewModel vm = userService.findSupervisorById(id);
		
		if(vm == null){
			log.info("No supervisor found by id : {}");
			return new ResponseEntity<String>(
					"No supervisor found by id : " + id, HttpStatus.NOT_FOUND
					);
		}
		
		if(!StringUtils.equalsIgnoreCase(vm.getEmailAddress(), viewModel.getEmailAddress())){
			if(userService.isEmailAddressExistant(viewModel.getEmailAddress())){
				log.info("{} already belongs to a user", viewModel.getEmailAddress());
				return new ResponseEntity<String>(
						viewModel.getEmailAddress() + " already belongs to a user", HttpStatus.INTERNAL_SERVER_ERROR
						);
			}
		}
		
		if(!StringUtils.equals(vm.getPhoneNumber(), viewModel.getPhoneNumber())){
			if(userService.isPrimaryPhoneNumberExistant(viewModel.getPhoneNumber())){
				log.info("{} already belongs to a user", viewModel.getPhoneNumber());
				return new ResponseEntity<String>(
						viewModel.getPhoneNumber() + " already belongs to a user", HttpStatus.INTERNAL_SERVER_ERROR
						);
			}
		}
		
		viewModel = userService.createOrUpdateSupervisor(viewModel);
		
		log.info("Supervisor successfully updated");
		
		return new ResponseEntity<String>("Supervisor successfully updated", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/supervisors/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> findSupervisorById(@PathVariable Long id){
		log.info("Finding supervisor by id : {}", id);
		SupervisorViewModel viewModel = userService.findSupervisorById(id);
		if(viewModel == null){
			log.info("No supervisor found by id : ", id);
			return new ResponseEntity<String>("No supervisor found id : " + id, HttpStatus.NOT_FOUND);
		}
		
		log.info("Supervisor found : {}", viewModel);
		
		return new ResponseEntity<SupervisorViewModel>(viewModel, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/supervisors", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> findAllSupervisors(){
		log.info("Finding all supervisors");
		List<SupervisorViewModel> viewModels = userService.findAllSupervisors();
		if(viewModels.isEmpty()){
			log.info("No suprvisor found");
			return new ResponseEntity<String>("No supervisor found", HttpStatus.NOT_FOUND);
		}
		
		log.info("Supervisors found : {}", viewModels);
		return new ResponseEntity<List<SupervisorViewModel>>(viewModels, HttpStatus.OK);
	}
	
	@RequestMapping (value = "/supervisors/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteSupervisor(@PathVariable Long id){
		log.info("Deleting supervisor wit id : {}", id);
		
		if(userService.findSupervisorById(id) == null){
			log.info("No supervisor found by id : {}", id);
			return new ResponseEntity<String>(
					"No supervisor found with id : " + id, HttpStatus.NOT_FOUND
					);
		}
		
		userService.deleteUser(id);
		log.info("Supervisor with id : {} successfully deleted", id);
		
		return new ResponseEntity<String>("Supervisor successfully deleted", HttpStatus.OK);
	}
	
}