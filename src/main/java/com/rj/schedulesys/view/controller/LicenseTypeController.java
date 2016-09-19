package com.rj.schedulesys.view.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rj.schedulesys.service.LicenseTypeService;
import com.rj.schedulesys.view.model.LicenseTypeViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/license-types")
public class LicenseTypeController {

	private LicenseTypeService licenseTypeService;
	
	@Autowired
	public LicenseTypeController(LicenseTypeService licenseTypeService) {
		this.licenseTypeService = licenseTypeService;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findOne(@PathVariable Long id){
		log.info("Fetching license type by id : {}", id);
		LicenseTypeViewModel viewModel = null;
		viewModel = licenseTypeService.findOne(id);
		if(viewModel == null){
			log.info("No license type found with id : {}", id);
			return new ResponseEntity<String>(
					"No license type found with id : " + id, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(viewModel, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findAll(){
		log.info("Fetching all license types");
		List<LicenseTypeViewModel> viewModels = licenseTypeService.findAll();
		if(viewModels.isEmpty()){
			log.info("No license type found");
			return new ResponseEntity<String>("No license type found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(viewModels, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody ResponseEntity<String> create(@RequestBody LicenseTypeViewModel viewModel){
		log.info("Creating new license type : {}", viewModel);
		viewModel.setId(null);
		try{
			viewModel =	licenseTypeService.create(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Successfully created license type : {}", viewModel);
		return new ResponseEntity<String>("License type successfully created", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json")
	public @ResponseBody ResponseEntity<String> update(@PathVariable Long id, @RequestBody LicenseTypeViewModel viewModel){
		log.info("Updating license type with id", id);
		if(licenseTypeService.findOne(id) == null){
			log.info("No license type found by id : {}", id);
			return new ResponseEntity<String>("No license type found with id : " + id, HttpStatus.NOT_FOUND);
		}
		viewModel.setId(id);//Overriding the id received in the message body
		try{
			viewModel = licenseTypeService.update(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("License type updated successfully : {}", viewModel);
		return new ResponseEntity<String>("License type successfully updated", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity<String> delete(@PathVariable Long id){
		log.info("Deleting license type with id : {}", id);
		if(licenseTypeService.findOne(id) == null){
			log.info("No license type found with id : {}", id);
			return new ResponseEntity<String>("No license type found with id : " + id, HttpStatus.NOT_FOUND);
		}
		try{
			licenseTypeService.delete(id);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Successfully deleted license type with id : {}", id);
		return new ResponseEntity<String>("License type successfully deleted", HttpStatus.OK);
	}
}
