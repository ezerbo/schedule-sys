package com.rj.schedulesys.view.controller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.rj.schedulesys.service.EmployeeService;
import com.rj.schedulesys.service.ScheduleService;
import com.rj.schedulesys.view.model.EmployeeViewModel;
import com.rj.schedulesys.view.model.GetScheduleViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/employees")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private ScheduleService scheduleService;
	
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
			viewModels = scheduleService.findAllByEmployee(id);
		}else{
			viewModels = scheduleService.findAllBetweenDatesByEmployee(startDate, endDate, id);
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
}