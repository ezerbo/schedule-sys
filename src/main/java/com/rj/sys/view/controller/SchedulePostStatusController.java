package com.rj.sys.view.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rj.sys.service.SchedulePostStatusService;
import com.rj.sys.view.model.SchedulePostStatusViewModel;

@Slf4j
@Controller
@RequestMapping("/schedule-post-statuses")
public class SchedulePostStatusController {
	 
	private @Autowired SchedulePostStatusService schedulePostStatusService;
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> findAllScheduleStatuses(){
		log.info("Finding all schedule post  statuses");
		List<SchedulePostStatusViewModel> viewModels = schedulePostStatusService.findAll();
		
		if(viewModels.isEmpty()){
			log.info("No schedule post statuses found");
			return new ResponseEntity<String>("No schedule post statuses found", HttpStatus.NOT_FOUND);
		}
		
		log.info("Schedule post statuses found : {}", viewModels);
		return new ResponseEntity<List<SchedulePostStatusViewModel>>(viewModels, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{idOrStatus}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> findScheduleStatusByIdOrName(@PathVariable String idOrStatus){
		SchedulePostStatusViewModel viewModel = null;
		
		if(StringUtils.isNumeric(idOrStatus)){
			log.info("Finding schedule post status with id : {}", idOrStatus);
			viewModel = schedulePostStatusService.findById(Long.valueOf(idOrStatus));
		}else{
			log.info("Finding schedule post status with status : {}", idOrStatus);
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