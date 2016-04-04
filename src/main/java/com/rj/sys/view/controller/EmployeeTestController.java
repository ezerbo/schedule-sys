package com.rj.sys.view.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rj.sys.service.TestService;
import com.rj.sys.service.TestTypeService;
import com.rj.sys.service.UserService;
import com.rj.sys.view.model.UserTestViewModel;

@Slf4j
@Controller
@RequestMapping(value = "/users/employees")
public class EmployeeTestController {

	private @Autowired UserService userService;
	private @Autowired TestService testService;
	private @Autowired TestTypeService testTypeService;
	
	@RequestMapping(value = "/{id}/tests", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<?> addTest(@PathVariable Long id, @RequestBody UserTestViewModel viewModel){
		log.info("Adding new test for employee with id {}", id);
		
		if(userService.findEmployeeById(id) == null){
			log.info("No employee found with id : {}", id);
			return new ResponseEntity<String>("No employee found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		if(testTypeService.findByName(viewModel.getTestTypeName()) == null){
			log.info("No test type fond with name : {}", viewModel.getTestTypeName());
			return new ResponseEntity<String>(
					"No test type found with name : " + viewModel.getTestTypeName(), HttpStatus.NOT_FOUND
					);
		}
		
		return new ResponseEntity<String>("Test type added successfully", HttpStatus.NOT_FOUND);
		
	}
}