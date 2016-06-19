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

import com.rj.schedulesys.service.UserService;
import com.rj.schedulesys.view.model.ScheduleSysUserViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {
	
	private @Autowired UserService userService;
	
	/**
	 * @param viewModel
	 * @return message
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> create(@RequestBody ScheduleSysUserViewModel viewModel){
		
		log.info("Creating new user : {}", viewModel);
		
		viewModel.setId(null);
		
		try{
			viewModel = userService.create(viewModel);
		}catch(Exception e){
			log.error("{} occurred while creating a user", e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Successfully created user : {}", viewModel);
		
		return new ResponseEntity<String>("User successfully created", HttpStatus.CREATED);
	}
	
	/**
	 * @param id
	 * @param viewModel
	 * @return message
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<String> update(@PathVariable Long id, @RequestBody ScheduleSysUserViewModel viewModel){
		
		log.info("Updating user : {}", viewModel);
		
		ScheduleSysUserViewModel vm = userService.findOne(id);
		
		if(vm == null){
			log.warn("No user found with id : {}", id);
			return new ResponseEntity<String>(
					"No user found with id : " + id, HttpStatus.NOT_FOUND
					);
		}
		
		viewModel.setId(id);//Override the id
		
		try{
			viewModel = userService.update(viewModel);	
		}catch(Exception e){
			log.error("{} occurred while updating a user ", e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Successfully updated user : {}", viewModel);
		
		return new ResponseEntity<String>("User successfully updated", HttpStatus.OK);
	}
	
	/**
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> finById(@PathVariable Long id){
		
		log.info("Finding user by id : {}", id);
		
		ScheduleSysUserViewModel viewModel = userService.findOne(id);
		
		if(viewModel == null){
			log.info("No user found by id : {}", id);
			return new ResponseEntity<String>("No user found by id : " + id, HttpStatus.NOT_FOUND);
		}
		
		log.info("User : {} found by id : {}", viewModel, id);
		
		return new ResponseEntity<ScheduleSysUserViewModel>(viewModel, HttpStatus.OK);
	}
	
	/**
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> findAll(){
		
		log.info("Finding all users");
		
		List<ScheduleSysUserViewModel> viewModels = userService.findAll();
		
		if(viewModels.isEmpty()){
			log.info("No user found");
			return new ResponseEntity<String>("No user found", HttpStatus.NOT_FOUND);
		}
		
		log.info("Users found : {}", viewModels);
		
		return new ResponseEntity<List<ScheduleSysUserViewModel>>(viewModels, HttpStatus.OK);
	}
	
	/**
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable Long id){
	
		log.info("Deleting user with id : {}", id);
		
		if(userService.findOne(id) == null){
			log.info("No user found with id : {}", id);
			return new ResponseEntity<String>("No user found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		try{
			userService.delete(id);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("User with id : {} successfully deleted", id);
		
		return new ResponseEntity<String>("User successfully deleted", HttpStatus.OK);
	}
	
}