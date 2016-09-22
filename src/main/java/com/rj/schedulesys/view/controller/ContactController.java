package com.rj.schedulesys.view.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rj.schedulesys.service.ContactService;
import com.rj.schedulesys.view.model.ContactViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/contacts")
public class ContactController {

	private ContactService contactService;
	
	@Autowired
	public ContactController(ContactService contactService) {
		this.contactService = contactService;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findOne(@PathVariable Long id){
		log.info("Fetching contact with id {} ", id);
		ContactViewModel viewModel = contactService.findOne(id);
		if(viewModel == null){
			log.warn("No contact found with id : {}", id);
			return new ResponseEntity<String>(
					"No contact found with id : " + id , HttpStatus.NOT_FOUND);
		}
		log.info("Contact found : {}", viewModel);
		return new ResponseEntity<>(viewModel, HttpStatus.OK);
	}
	
	@RequestMapping( method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody ResponseEntity<String> create(@RequestBody ContactViewModel viewModel){
		log.info("Creating new contact : {}", viewModel);
		try{
			viewModel = contactService.create(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Successfuly created : {}", viewModel);
		return new ResponseEntity<String>("Successfully created contact", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public @ResponseBody ResponseEntity<String> update(@PathVariable Long id, @RequestBody ContactViewModel viewModel){
		log.info("Updating contact : {}", viewModel);
		if(contactService.findOne(id) == null){
			log.info("No contact found with id : {}", id);
			return new ResponseEntity<String>("No contact found with id : " + id, HttpStatus.NOT_FOUND);
		}
		viewModel.setId(id);//Override the id
		try{
			viewModel = contactService.update(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Successfuly updated : {}", viewModel);
		return new ResponseEntity<String>("Successfully updated contact", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity<String> delete(@PathVariable Long id){
		log.info("Deleting contact with id : {}", id);
		if(contactService.findOne(id) == null){
			log.info("No contact found with id : {}", id);
			return new ResponseEntity<String>("No contact found with id : " + id, HttpStatus.NOT_FOUND);
		}
		try {
			contactService.delete(id);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Contact with id : {} successfully deleted");
		return new ResponseEntity<String>("Contact successfully deleted", HttpStatus.OK);
	}
}
