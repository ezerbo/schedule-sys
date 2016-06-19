package com.rj.schedulesys.view.controller;

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

import com.rj.schedulesys.service.ScheduleStatusService;
import com.rj.schedulesys.view.model.ScheduleStatusViewModel;

@Slf4j
@Controller
@RequestMapping("/schedule-statuses")
public class ScheduleStatusController {
	
	private @Autowired ScheduleStatusService scheduleStatusService;
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> findAllScheduleStatuses(){
		log.info("Finding all schedule statuses");
		List<ScheduleStatusViewModel> viewModels = scheduleStatusService.findAll();
		
		if(viewModels.isEmpty()){
			log.info("No schedule statuses found");
			return new ResponseEntity<String>("No schedule statuses found", HttpStatus.NOT_FOUND);
		}
		
		log.info("Schedule statuses found : {}", viewModels);
		return new ResponseEntity<List<ScheduleStatusViewModel>>(viewModels, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{idOrStatus}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> findScheduleStatusByIdOrName(@PathVariable String idOrStatus){
		ScheduleStatusViewModel viewModel = null;
		
		if(StringUtils.isNumeric(idOrStatus)){
			log.info("Finding schedule status with id : {}", idOrStatus);
			viewModel = scheduleStatusService.findById(Long.valueOf(idOrStatus));
		}else{
			log.info("Finding schedule status with status : {}", idOrStatus);
			viewModel = scheduleStatusService.findByStatus(idOrStatus);
		}
		
		if(viewModel == null){
			log.info("No schedule status found with id or status : {}", idOrStatus);
			return new ResponseEntity<String>(
					"No schedule status found with id or status : " + idOrStatus, HttpStatus.NOT_FOUND
					);
		}
		
		log.info("Schedule status found : {}", viewModel);
		return new ResponseEntity<ScheduleStatusViewModel>(viewModel, HttpStatus.OK);
	}
}