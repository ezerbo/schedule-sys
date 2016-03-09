package com.rj.sys.view.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rj.sys.service.UserService;
import com.rj.sys.view.model.UserViewModel;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {
	
	private @Autowired UserService userService;
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> getAllActiveUsers(){
		
		List<UserViewModel> activeUsers = userService.findAllActiveUsers();
		
		if(activeUsers.size() == 0){
			new ResponseEntity<String>("No user found", HttpStatus.OK);
		}
		
		return new ResponseEntity<List<UserViewModel>>(activeUsers, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> getUserById(@PathVariable Long id){
		
		log.info("Getting user with id : {}", id);
		UserViewModel viewModel = userService.findOneActiveUserById(id);
		
		if(viewModel == null){
			log.info("No user found with id : {}", id);
			return new ResponseEntity<String>("No user found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		log.info("User found : {}", viewModel);
		return new ResponseEntity<UserViewModel>(viewModel, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> createUser(@RequestBody UserViewModel viewModel){
		log.info("User creation request received : {}", viewModel);
		return new ResponseEntity<String>("User successfully saved", HttpStatus.CREATED);
	}
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<String> updateUser(@RequestBody UserViewModel viewModel){
		log.info("User update request received : {}", viewModel);
		return new ResponseEntity<String>("User successfully updated",HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, consumes = "application/json")
	public ResponseEntity<String> updateUserStatus(@PathVariable Long id){
		
		log.info("Deactivation request received for user with id {}", id);
		
		return new ResponseEntity<String>("User successfully deactivated", HttpStatus.OK);
		
	}
	
}