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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rj.sys.service.UserService;
import com.rj.sys.view.model.UserViewModel;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {
	
	private @Autowired UserService userService;
	
	private final static String activateOperation = "activate";
	private final static String deactivateOperation = "deactivate";
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> getAllActiveUsers(){
		
		List<UserViewModel> activeUsers = userService.findAllActiveUsers();
		
		if(activeUsers.size() == 0){
			new ResponseEntity<String>("No user found", HttpStatus.OK);
		}
		
		return new ResponseEntity<List<UserViewModel>>(activeUsers, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<UserViewModel> getUserById(@PathVariable Long id){
		
		log.info("Getting user with id : {}", id);
		
		UserViewModel viewModel = userService.findOneActiveUserById(id);
		
		return new ResponseEntity<UserViewModel>(viewModel, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> createUser(@RequestBody UserViewModel viewModel){
		log.info("User creation request received : {}", viewModel);
		return new ResponseEntity<String>("User successfully saved", HttpStatus.CREATED);
	}
	
	
	@RequestMapping(method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<String> updateUser(@RequestBody UserViewModel viewModel){
		log.info("User update request received : {}", viewModel);
		return new ResponseEntity<String>("User successfully updated",HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<String> updateUserStatus(@PathVariable Long id, @RequestParam(name = "op") String operation){
		
		log.info("{} request received for user with id {}",operation, id);
		
		if(operation == activateOperation){
			return new ResponseEntity<String>("User successfully activated", HttpStatus.OK);
		}else if (operation == deactivateOperation){
			return new ResponseEntity<String>("User successfully deactivated", HttpStatus.OK);
		}else{
			return new ResponseEntity<String>("No such operation " + operation , HttpStatus.BAD_REQUEST);
		}
	}
	
}