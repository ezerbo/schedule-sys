package com.rj.schedulesys.view.controller;

import java.util.Date;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.rj.schedulesys.service.ContactService;
import com.rj.schedulesys.service.PrivateCareScheduleService;
import com.rj.schedulesys.service.PrivateCareService;
import com.rj.schedulesys.view.model.ContactViewModel;
import com.rj.schedulesys.view.model.GetPrivateCareScheduleViewModel;
import com.rj.schedulesys.view.model.PrivateCareViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/private-cares")
public class PrivateCareController {
	
	private ContactService contactService;
	private PrivateCareService privateCareService;
	private PrivateCareScheduleService scheduleService;
	
	@Autowired
	public PrivateCareController(PrivateCareService privateCareService, ContactService contactService,
			PrivateCareScheduleService scheduleService) {
		this.privateCareService = privateCareService;
		this.contactService = contactService;
		this.scheduleService = scheduleService;
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
	
	@RequestMapping(value ="/{id}/contacts", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findAllContacts(@PathVariable Long id){
		log.info("Finding all contacts ");
		if(privateCareService.findOne(id) == null){
			log.warn("No private care found with id : {}", id);
			return new ResponseEntity<String>("No private care found with id : " + id, HttpStatus.NOT_FOUND);
		}
		List<ContactViewModel> viewModels = contactService.findAllByPrivateCare(id);
		if(viewModels.isEmpty()){
			log.info("No contacts found");
			return new ResponseEntity<String>("No contact found", HttpStatus.NOT_FOUND);
		}
		log.info("Contacts found : {}", viewModels);
		return new ResponseEntity<>(viewModels, HttpStatus.OK);
	}
	
	@RequestMapping(value ="/{id}/contacts", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody ResponseEntity<?> addContact(@PathVariable Long id, @RequestBody ContactViewModel viewModel){
		log.info("Creating contact : {} ", viewModel);
		if(privateCareService.findOne(id) == null){
			log.warn("No private cate found with id : {}", id);
			return new ResponseEntity<String>("No private care found with id : " + id, HttpStatus.NOT_FOUND);
		}
		viewModel.setPrivateCareId(id);
		try{
			contactService.create(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Contact successfully created");
		return new ResponseEntity<>("Contact successfully created", HttpStatus.OK);
	}
	
	@RequestMapping(value ="/{id}/contacts/{contactId}", method = RequestMethod.PUT, produces = "application/json")
	public @ResponseBody ResponseEntity<?> updateContact(@PathVariable Long id, @PathVariable Long contactId,
			@RequestBody ContactViewModel viewModel){
		log.info("Updating contact : {}", viewModel);
		PrivateCareViewModel facility = privateCareService.findOne(id);
		if(facility == null){
			log.warn("No private care found with id : {}", id);
			return new ResponseEntity<String>("No private care found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		ContactViewModel staffMember = contactService.findOne(contactId);
		if(staffMember == null){
			log.error("No private care found with id : {}", contactId);
			return new ResponseEntity<String>("No private care found with id : " + contactId, HttpStatus.NOT_FOUND);
		}
		
		if(facility.getId() != staffMember.getPrivateCareId()){
			log.warn("No contact with id : {} found for facility with id : {}", contactId, id);
			return new ResponseEntity<String>("No contact with id : " + contactId 
					+ " found for private care with id : " + id, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		viewModel.setPrivateCareId(id);
		viewModel.setId(contactId);
		try{
			contactService.update(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Contact successfully created");
		return new ResponseEntity<>("Contact successfully created", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/schedules", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> findSchedules(@PathVariable Long id, @RequestParam(required = false) Date startDate
			, @RequestParam(required = false) Date endDate){
		log.info("Finding schedules between startDate : {} and endDate : {} for private care with id : {}", startDate, endDate);
		if(privateCareService.findOne(id) == null){
			log.info("No private care found with id : {}", id);
			return new ResponseEntity<String>("No private care found with id : " + id, HttpStatus.NOT_FOUND);
		}
		List<GetPrivateCareScheduleViewModel> viewModels = null;
		if(startDate == null || endDate == null){
			viewModels = scheduleService.findAllByPrivateCare(id);
		}else{
			viewModels = scheduleService.findAllBetweenDatesByPrivateCare(startDate, endDate, id);
		}
		if(viewModels.isEmpty()){
			log.info("No schedules found between : {} and : {} for private with id : {}", startDate, endDate, id);
			return new ResponseEntity<>(
					"No schedules found between : " + startDate + " and : " + endDate + " for private with id : " + id, HttpStatus.NOT_FOUND
					);
		}
		log.info("Schedules found : {}", viewModels);
		return new ResponseEntity<>(viewModels, HttpStatus.OK);
	}
}
