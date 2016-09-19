package com.rj.schedulesys.view.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rj.schedulesys.service.LicenseService;
import com.rj.schedulesys.view.model.GetLicenseViewModel;
import com.rj.schedulesys.view.model.LicenseViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/licenses")
public class LicenseController {
	
	private LicenseService licenseService;
	
	@Autowired
	public LicenseController(LicenseService licenseService) {
		this.licenseService = licenseService;
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<?> create(@RequestBody LicenseViewModel viewModel){
		log.info("Creating license : {}", viewModel);
		viewModel.setId(null);
		try{
			viewModel = licenseService.create(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("License {} created successfully", viewModel);
		return new ResponseEntity<>("License successfully created", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody LicenseViewModel viewModel){
		log.info("Updating license : {}", viewModel);
		if(licenseService.findOne(id) == null){
			log.info("No license with id : {}", id);
			return new ResponseEntity<>("No license found with id : " + viewModel.getId(), HttpStatus.NOT_FOUND);
		}
		viewModel.setId(id);
		try{
			viewModel = licenseService.update(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("License : {} successfully updated", viewModel);
		return new ResponseEntity<>("License successfully updated", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable Long id){
		log.info("Deleting license with id : {}", id);
		if(licenseService.findOne(id) == null){
			log.info("No license found with id : {}", id);
			return new ResponseEntity<>("No license found with id : " + id, HttpStatus.NOT_FOUND);
		}
		licenseService.delete(id);
		log.info("License successfully deleted");
		return new ResponseEntity<>("License successfully deleted", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> findOne(@PathVariable Long id){
		log.info("Fetching license with id : ", id);
		GetLicenseViewModel viewModel = licenseService.findOne(id);
		if(viewModel == null){
			log.warn("No license found with id : {}", id);
			return new ResponseEntity<>("No license found with id : " + id, HttpStatus.NOT_FOUND);
		}
		log.info("License found : {}", id);
		return new ResponseEntity<>(viewModel, HttpStatus.OK);
	}
}