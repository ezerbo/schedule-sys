package com.rj.sys.view.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rj.sys.service.FacilityService;
import com.rj.sys.view.model.FacilityViewModel;

@Slf4j
@Controller
@RequestMapping("/facilities")
public class FacilityController {
	
	private @Autowired FacilityService facilityService;

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findAllFacilities(){
		
		log.info("Finding all facilities");
		
		List<FacilityViewModel> viewModels = facilityService.findAll();
		if(viewModels.isEmpty()){
			return new ResponseEntity<String>("No facility found", HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<List<FacilityViewModel>>(viewModels, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{idOrName}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findByIdOrName(@PathVariable String idOrName){
		FacilityViewModel viewModel = null;
		if(StringUtils.isNumeric(idOrName)){
			log.info("Finding facility by id : {}", idOrName);
			viewModel = facilityService.findById(Long.valueOf(idOrName));
		}else{
			log.info("Finding facility by name : {}", idOrName);
			viewModel = facilityService.findByName(idOrName);
		}
		
		if(viewModel == null){
			return new ResponseEntity<String>(
					"No facility found with either id or name : " + idOrName, HttpStatus.NOT_FOUND
					);
		}
		
		return new ResponseEntity<FacilityViewModel>(viewModel, HttpStatus.OK);
		
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody ResponseEntity<?> createFacility(@RequestBody FacilityViewModel viewModel){
		log.info("Create request recieved for facility : {}", viewModel);
		return new ResponseEntity<String>("Facility successfully created", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public @ResponseBody ResponseEntity<?> updateFacility(@PathVariable Long id, @RequestBody FacilityViewModel viewModel){
		log.info("Update request recieved for facility {} with id: {}", viewModel, id);
		return new ResponseEntity<String>("Facility successfully updated", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, consumes = "application/json")
	public @ResponseBody ResponseEntity<?> deleteFacility(@PathVariable Long id){
		log.info("Delete request recieved for facility with id : {}", id);
		return new ResponseEntity<String>("Facility successfully deleted", HttpStatus.CREATED);
	}
	
}