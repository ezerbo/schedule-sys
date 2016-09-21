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

import com.rj.schedulesys.service.PrivateCareService;
import com.rj.schedulesys.view.model.PrivateCareViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/private-cares")
public class PrivateCareController {
	
	private PrivateCareService privateCareService;
	
	@Autowired
	public PrivateCareController(PrivateCareService privateCareService) {
		this.privateCareService = privateCareService;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findAll(){
		log.info("Finding all private cares");
		List<PrivateCareViewModel> viewModels = privateCareService.findAll();
		if(viewModels.isEmpty()){
			return new ResponseEntity<String>("No private care found", HttpStatus.NOT_FOUND);
		}
		log.info("Private cares found : {}", viewModels);
		return new ResponseEntity<>(viewModels, HttpStatus.OK);
	}
	
	/**
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findOne(@PathVariable Long id){
		log.info("Fetching private care with id : {}", id);
		PrivateCareViewModel viewModel = privateCareService.findOne(id);
		if(viewModel == null){
			return new ResponseEntity<String>(
					"No private care found with either id : " + id, HttpStatus.NOT_FOUND);
		}
		log.info("Private care found : {}", viewModel);
		return new ResponseEntity<>(viewModel, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody ResponseEntity<?> create(@RequestBody PrivateCareViewModel viewModel){
		log.info("Creating new private care : {}", viewModel);
		try{
			viewModel = privateCareService.create(viewModel);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Successfully created private care : {}", viewModel);
		return new ResponseEntity<String>("Private care successfully created", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<?> update(@PathVariable Long id, @RequestBody PrivateCareViewModel viewModel){
		log.info("Updating private care with id: {}", id);
		if(privateCareService.findOne(id) == null){
			log.info("No private care found with id : {}", id);
			return new ResponseEntity<String>("No private care found with id : " + id, HttpStatus.NOT_FOUND);
		}
		viewModel.setId(id);//overriding the id
		try{
			viewModel = privateCareService.update(viewModel);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Private care successfully updated : {}", viewModel);
		return new ResponseEntity<String>("Private care successfully updated", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity<?> delete(@PathVariable Long id){
		log.info("Deleting private care with id : {}", id);
		if(privateCareService.findOne(id) == null){
			return new ResponseEntity<String>("No private care found with id : " + id, HttpStatus.NOT_FOUND);
		}
		try{
			privateCareService.delete(id);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Private care with id successfully deleted : {}", id);
		return new ResponseEntity<String>("Private care successfully deleted", HttpStatus.OK);
	}
}
