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

import com.rj.schedulesys.service.StaffMemberService;
import com.rj.schedulesys.view.model.StaffMemberViewModel;

import lombok.extern.slf4j.Slf4j;
//http://stackoverflow.com/questions/30126754/how-to-implement-basic-spring-security-session-management-for-single-page-angu
@Slf4j
@Controller
@RequestMapping("/staff-members")
public class StaffMemberController {
	
	private @Autowired StaffMemberService staffMemberService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findOne(@PathVariable Long id){
		
		log.info("Fetching staff member with id {} ", id);
		
		StaffMemberViewModel viewModel = staffMemberService.findOne(id);
		
		if(viewModel == null){
			log.warn("No staff member found with id : {}", id);
			return new ResponseEntity<String>(
					"No staff member found with id : " + id , HttpStatus.NOT_FOUND
					);
		}
		
		log.info("Staff member found : {}", viewModel);
		
		return new ResponseEntity<StaffMemberViewModel>(viewModel, HttpStatus.OK);
	}
	
	@RequestMapping( method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody ResponseEntity<String> create(@RequestBody StaffMemberViewModel viewModel){
		
		log.info("Creating new staff member : {}", viewModel);
		
		try{
			viewModel = staffMemberService.create(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Successfuly created : {}", viewModel);
		
		return new ResponseEntity<String>("Successfully created staff member", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public @ResponseBody ResponseEntity<String> update(@PathVariable Long id, @RequestBody StaffMemberViewModel viewModel){
		
		log.info("Updating staff member : {}", viewModel);
		
		if(staffMemberService.findOne(id) == null){
			log.info("No staff member found with id : {}", id);
			return new ResponseEntity<String>("No staff member found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		viewModel.setId(id);//Override the id
		
		try{
			viewModel = staffMemberService.update(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Successfuly updated : {}", viewModel);
		
		return new ResponseEntity<String>("Successfully updated staff member", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity<String> delete(@PathVariable Long id){
		
		log.info("Deleting staff member with id : {}", id);
		
		if(staffMemberService.findOne(id) == null){
			log.info("No staff member found with id : {}", id);
			return new ResponseEntity<String>("No staff member found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		try {
			staffMemberService.delete(id);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Staff member with id : {} successfully deleted");
		
		return new ResponseEntity<String>("Staff member successfully deleted", HttpStatus.OK);
	}
}