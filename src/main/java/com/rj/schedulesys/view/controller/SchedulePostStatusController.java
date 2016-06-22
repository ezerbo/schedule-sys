package com.rj.schedulesys.view.controller;

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

import com.rj.schedulesys.service.SchedulePostStatusService;
import com.rj.schedulesys.view.model.SchedulePostStatusViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/schedule-post-statuses")
public class SchedulePostStatusController {
	 
	private @Autowired SchedulePostStatusService schedulePostStatusService;
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> create(@RequestBody SchedulePostStatusViewModel viewModel){
		
		log.info("Creating schedule post status : {}", viewModel);
		
		try{
			schedulePostStatusService.create(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Schedule post status created successfully");
		
		return new ResponseEntity<String>("Schedule post status successfully created", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<String> update(@PathVariable Long id, @RequestBody SchedulePostStatusViewModel viewModel){
		
		log.info("Updating schedule post status with id : {}", id);
		
		if(schedulePostStatusService.findOne(id) == null){
			log.error("No schedule post status found with id : {}", id);
			return new ResponseEntity<String>("No schedule post status found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		viewModel.setId(id);
		
		try{
			schedulePostStatusService.update(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Schedule post status updated successfully");
		
		return new ResponseEntity<String>("Schedule post status successfully updated", HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable Long id){
		
		log.info("Deleting schedule post status with id : {}", id);
		
		if(schedulePostStatusService.findOne(id) == null){
			log.error("No schedule post status found with id : {}", id);
			return new ResponseEntity<String>("No schedule post status found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		try{
			schedulePostStatusService.delete(id);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Schedule post status deleted successfully");
		
		return new ResponseEntity<String>("Schedule post status successfully deleted", HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> findAll(){
		
		log.info("Fetching all schedule post statuses");
		
		List<SchedulePostStatusViewModel> viewModels = schedulePostStatusService.findAll();
		
		if(viewModels.isEmpty()){
			log.info("No schedule post statuses found");
			return new ResponseEntity<String>("No schedule post statuses found", HttpStatus.NOT_FOUND);
		}
		
		log.info("Schedule post statuses found : {}", viewModels);
		
		return new ResponseEntity<List<SchedulePostStatusViewModel>>(viewModels, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{idOrStatus}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> findOne(@PathVariable String idOrStatus){
		
		SchedulePostStatusViewModel viewModel = null;
		 
		if(StringUtils.isNumeric(idOrStatus)){
			log.info("Fetching schedule post status with id : {}", idOrStatus);
			viewModel = schedulePostStatusService.findOne(Long.valueOf(idOrStatus));
		}else{
			log.info("Fetching schedule post status with status : {}", idOrStatus);
			viewModel = schedulePostStatusService.findByStatus(idOrStatus);
		}
		
		if(viewModel == null){
			log.info("No schedule post status found with id or status : {}", idOrStatus);
			return new ResponseEntity<String>(
					"No schedule post status found with id or status : " + idOrStatus, HttpStatus.NOT_FOUND
					);
		}
		
		log.info("Schedule post status found : {}", viewModel);
		
		return new ResponseEntity<SchedulePostStatusViewModel>(viewModel, HttpStatus.OK);
	}
}