package com.rj.schedulesys.view.controller;

import java.util.Date;
import java.util.LinkedList;
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

import com.rj.schedulesys.domain.EmployeTestPK;
import com.rj.schedulesys.service.EmployeeService;
import com.rj.schedulesys.service.EmployeeTestService;
import com.rj.schedulesys.service.FacilityScheduleService;
import com.rj.schedulesys.service.TestService;
import com.rj.schedulesys.view.model.EmployeeTestViewModel;
import com.rj.schedulesys.view.model.EmployeeViewModel;
import com.rj.schedulesys.view.model.GetNurseTestViewModel;
import com.rj.schedulesys.view.model.GetScheduleViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/employees")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private FacilityScheduleService scheduleService;
	
	@Autowired
	private EmployeeTestService employeeTestService;
	
	@Autowired
	private TestService testService;
	
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
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> findOne(@PathVariable Long id){
		log.info("Fetching employee with id : {}", id);
		EmployeeViewModel viewModel = employeeService.findOne(id);
		if(viewModel == null){
			log.warn("No employee found with id : {}", id);
			return new ResponseEntity<>("No employee found with id : " + id, HttpStatus.NOT_FOUND);
		}
		log.info("Employee found : {}", viewModel);
		return new ResponseEntity<>(viewModel, HttpStatus.OK);
	}
	

	@RequestMapping(value = "/{id}/tests", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<?> addOrUpdateTest(@PathVariable Long id, @RequestBody EmployeeTestViewModel viewModel){
		log.info("Adding new test : {} for nurse with id : {}", viewModel, id);
		if(employeeService.findOne(id) == null){
			log.warn("No employee found with id : {}", id);
			return new ResponseEntity<>("No employee found with id : " + id, HttpStatus.NOT_FOUND);
		}
		viewModel.setEmployeeId(id);
		try {
			viewModel = employeeTestService.createOrUpate(viewModel);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Test successfully added");
		return new ResponseEntity<>("Test successfully added", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}/tests", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> findAllTests(@PathVariable Long id){
		log.info("Fetching all test for employee with id", id);
		if(employeeService.findOne(id) == null){
			log.warn("No employee found with id : {}", id);
			return new ResponseEntity<>("No employee found with id : " + id, HttpStatus.NOT_FOUND);
		}
		List<GetNurseTestViewModel> viewModels = employeeTestService.findAllByEmployee(id);
		if(viewModels.isEmpty()){
			log.warn("No test found for employee with id : {}", id);
			return new ResponseEntity<>("No test found for employee with id : " + id, HttpStatus.NOT_FOUND);
		}
		log.info("Tests found : {}", viewModels);
		return new ResponseEntity<>(viewModels, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/tests/{testId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteTest(@PathVariable Long id, @PathVariable Long testId){
		log.info("Deleting EmployeeTest with employeeId : {} and testId : {}", id, testId);
		if(employeeService.findOne(id) == null){
			log.warn("No employee found with id : {}", id);
			return new ResponseEntity<>("No employee found with id : " + id, HttpStatus.NOT_FOUND);
		}
		if(testService.findOne(testId) == null){
			log.error("No test found with id : {}", testId);
			return new ResponseEntity<>("No test found with id : " + testId, HttpStatus.NOT_FOUND);
		}
		EmployeTestPK nurseTestPK = EmployeTestPK.builder()
				.testId(testId)
				.employeeId(id)
				.build();
		try {
			employeeTestService.delete(nurseTestPK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Test successfully deleted");
		return new ResponseEntity<>("Test successfully deleted", HttpStatus.OK);
	}
	
}